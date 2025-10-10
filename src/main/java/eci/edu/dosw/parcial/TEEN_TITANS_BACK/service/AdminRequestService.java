package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Servicio para administradores con funcionalidades avanzadas de gestión de solicitudes,
 * reportes estadísticos y análisis de datos.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Service
public class AdminRequestService {

    private final ScheduleChangeRequestRepository scheduleChangeRequestRepository;
    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;
    private final CourseRepository courseRepository;
    private final ReviewStepRepository reviewStepRepository;
    private final StudentAcademicProgressRepository studentAcademicProgressRepository;
    private final DeanRepository deanRepository;
    private final AdministratorRepository administratorRepository;

    /**
     * Constructor para inyección de dependencias.
     */
    @Autowired
    public AdminRequestService(ScheduleChangeRequestRepository scheduleChangeRequestRepository,
                               StudentRepository studentRepository,
                               GroupRepository groupRepository,
                               CourseRepository courseRepository,
                               ReviewStepRepository reviewStepRepository,
                               StudentAcademicProgressRepository studentAcademicProgressRepository,
                               DeanRepository deanRepository,
                               AdministratorRepository administratorRepository) {
        this.scheduleChangeRequestRepository = scheduleChangeRequestRepository;
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
        this.courseRepository = courseRepository;
        this.reviewStepRepository = reviewStepRepository;
        this.studentAcademicProgressRepository = studentAcademicProgressRepository;
        this.deanRepository = deanRepository;
        this.administratorRepository = administratorRepository;
    }

    /**
     * Obtiene todas las solicitudes de una facultad específica.
     *
     * @param faculty Nombre de la facultad
     * @return Lista de solicitudes de la facultad
     */
    public List<ScheduleChangeRequest> getRequestsByFaculty(String faculty) {
        List<StudentAcademicProgress> facultyProgress = studentAcademicProgressRepository.findByFaculty(faculty);
        List<String> facultyStudentIds = facultyProgress.stream()
                .map(progress -> progress.getStudent().getId())
                .collect(Collectors.toList());

        List<ScheduleChangeRequest> allRequests = scheduleChangeRequestRepository.findAll();
        return allRequests.stream()
                .filter(request -> request.getStudent() != null &&
                        facultyStudentIds.contains(request.getStudent().getId()))
                .sorted((r1, r2) -> r2.getSubmissionDate().compareTo(r1.getSubmissionDate()))
                .collect(Collectors.toList());
    }

    /**
     * Responde a una solicitud con una decisión (aprobación o rechazo).
     *
     * @param requestId ID de la solicitud
     * @param decision Decisión (APPROVED o REJECTED)
     * @param comments Comentarios de la decisión
     * @return Solicitud actualizada
     */
    public ScheduleChangeRequest respondToRequest(String requestId, RequestStatus decision, String comments) {
        ScheduleChangeRequest request = findRequestById(requestId);

        if (decision != RequestStatus.APPROVED && decision != RequestStatus.REJECTED) {
            throw new RuntimeException("La decisión debe ser APPROVED o REJECTED");
        }

        request.setStatus(decision);

        ReviewStep decisionStep = new ReviewStep(
                "ADMIN_SYSTEM",
                UserRole.ADMINISTRATOR,
                decision == RequestStatus.APPROVED ? "SOLICITUD_APROBADA" : "SOLICITUD_RECHAZADA",
                comments
        );
        request.addReviewStep(decisionStep);

        return scheduleChangeRequestRepository.save(request);
    }

    /**
     * Solicita información adicional para una solicitud.
     *
     * @param requestId ID de la solicitud
     * @param comments Información adicional requerida
     * @return Solicitud actualizada
     */
    public ScheduleChangeRequest requestAdditionalInfo(String requestId, String comments) {
        ScheduleChangeRequest request = findRequestById(requestId);
        request.setStatus(RequestStatus.UNDER_REVIEW);

        ReviewStep infoRequestStep = new ReviewStep(
                "ADMIN_SYSTEM",
                UserRole.ADMINISTRATOR,
                "INFORMACION_ADICIONAL_SOLICITADA",
                "Se requiere información adicional: " + comments
        );
        request.addReviewStep(infoRequestStep);

        return scheduleChangeRequestRepository.save(request);
    }

    /**
     * Obtiene todas las solicitudes del sistema.
     *
     * @return Lista de todas las solicitudes
     */
    public List<ScheduleChangeRequest> getGlobalRequests() {
        List<ScheduleChangeRequest> allRequests = scheduleChangeRequestRepository.findAll();
        allRequests.sort((r1, r2) -> r2.getSubmissionDate().compareTo(r1.getSubmissionDate()));
        return allRequests;
    }

    /**
     * Aprueba un caso especial (bypass de validaciones normales).
     *
     * @param requestId ID de la solicitud
     * @param comments Justificación de la aprobación especial
     * @return Solicitud aprobada
     */
    public ScheduleChangeRequest approveSpecialCase(String requestId, String comments) {
        ScheduleChangeRequest request = findRequestById(requestId);
        request.setStatus(RequestStatus.APPROVED);

        ReviewStep specialApprovalStep = new ReviewStep(
                "ADMIN_SYSTEM",
                UserRole.ADMINISTRATOR,
                "CASO_ESPECIAL_APROBADO",
                "Aprobación especial: " + comments
        );
        request.addReviewStep(specialApprovalStep);

        return scheduleChangeRequestRepository.save(request);
    }

    /**
     * Obtiene solicitudes que requieren tratamiento especial.
     *
     * @return Lista de casos especiales
     */
    public List<ScheduleChangeRequest> getSpecialCases() {
        List<ScheduleChangeRequest> allRequests = scheduleChangeRequestRepository.findAll();

        return allRequests.stream()
                .filter(request -> isSpecialCase(request))
                .sorted((r1, r2) -> r2.getSubmissionDate().compareTo(r1.getSubmissionDate()))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene solicitudes ordenadas por prioridad.
     *
     * @return Lista de solicitudes ordenadas por prioridad
     */
    public List<ScheduleChangeRequest> getRequestsByPriority() {
        List<ScheduleChangeRequest> pendingRequests = scheduleChangeRequestRepository
                .findByStatus(RequestStatus.PENDING);

        List<RequestWithPriority> requestsWithPriority = new ArrayList<>();
        for (ScheduleChangeRequest request : pendingRequests) {
            double priority = calculateRequestPriority(request);
            requestsWithPriority.add(new RequestWithPriority(request, priority));
        }

        requestsWithPriority.sort((r1, r2) -> Double.compare(r2.priority, r1.priority));

        return requestsWithPriority.stream()
                .map(rwp -> rwp.request)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene estadísticas de aprobación por facultad.
     *
     * @param faculty Nombre de la facultad
     * @return Estadísticas de aprobación
     */
    public ApprovalStats getApprovalRateByFaculty(String faculty) {
        List<ScheduleChangeRequest> facultyRequests = getRequestsByFaculty(faculty);
        return calculateApprovalStats(facultyRequests);
    }

    /**
     * Obtiene estadísticas de aprobación por curso.
     *
     * @param courseCode Código del curso
     * @return Estadísticas de aprobación
     */
    public ApprovalStats getApprovalRateByCourse(String courseCode) {
        List<Group> courseGroups = groupRepository.findByCourse_CourseCode(courseCode);
        List<String> groupIds = courseGroups.stream()
                .map(Group::getGroupId)
                .collect(Collectors.toList());

        List<ScheduleChangeRequest> allRequests = scheduleChangeRequestRepository.findAll();
        List<ScheduleChangeRequest> courseRequests = allRequests.stream()
                .filter(request ->
                        groupIds.contains(request.getCurrentGroup().getGroupId()) ||
                                groupIds.contains(request.getRequestedGroup().getGroupId()))
                .collect(Collectors.toList());

        return calculateApprovalStats(courseRequests);
    }

    /**
     * Obtiene estadísticas de aprobación por grupo.
     *
     * @param groupId ID del grupo
     * @return Estadísticas de aprobación
     */
    public ApprovalStats getApprovalRateByGroup(String groupId) {
        List<ScheduleChangeRequest> currentGroupRequests = scheduleChangeRequestRepository
                .findByCurrentGroupId(groupId);
        List<ScheduleChangeRequest> requestedGroupRequests = scheduleChangeRequestRepository
                .findByRequestedGroupId(groupId);

        Set<ScheduleChangeRequest> allGroupRequests = new HashSet<>();
        allGroupRequests.addAll(currentGroupRequests);
        allGroupRequests.addAll(requestedGroupRequests);

        return calculateApprovalStats(new ArrayList<>(allGroupRequests));
    }

    /**
     * Obtiene estadísticas globales de aprobación.
     *
     * @return Estadísticas globales
     */
    public ApprovalStats getGlobalApprovalRate() {
        List<ScheduleChangeRequest> allRequests = scheduleChangeRequestRepository.findAll();
        return calculateApprovalStats(allRequests);
    }

    /**
     * Genera reporte de reasignaciones.
     *
     * @return Estadísticas de reasignaciones
     */
    public ReassignmentStats generateReassignmentReport() {
        List<ScheduleChangeRequest> allRequests = scheduleChangeRequestRepository.findAll();
        List<ScheduleChangeRequest> approvedRequests = allRequests.stream()
                .filter(req -> req.getStatus() == RequestStatus.APPROVED)
                .collect(Collectors.toList());

        ReassignmentStats stats = new ReassignmentStats();
        stats.setTotalReassignments(approvedRequests.size());
        stats.setReassignmentsByFaculty(calculateReassignmentsByFaculty(approvedRequests));
        stats.setReassignmentsByCourse(calculateReassignmentsByCourse(approvedRequests));
        stats.setAverageProcessingTime(calculateAverageProcessingTime(approvedRequests));

        return stats;
    }

    /**
     * Genera reporte de un curso específico.
     *
     * @param courseCode Código del curso
     * @return Estadísticas del curso
     */
    public CourseStats generateCourseReport(String courseCode) {
        Optional<Course> courseOpt = courseRepository.findById(courseCode);
        if (courseOpt.isEmpty()) {
            throw new RuntimeException("Curso no encontrado: " + courseCode);
        }

        Course course = courseOpt.get();
        List<Group> courseGroups = groupRepository.findByCourse_CourseCode(courseCode);

        CourseStats stats = new CourseStats();
        stats.setCourseCode(courseCode);
        stats.setCourseName(course.getName());

        Map<String, Integer> requestsByGroup = new HashMap<>();
        for (Group group : courseGroups) {
            List<ScheduleChangeRequest> groupRequests = scheduleChangeRequestRepository
                    .findByCurrentGroupId(group.getGroupId());
            requestsByGroup.put(group.getGroupId(), groupRequests.size());
        }

        stats.setRequestsByGroup(requestsByGroup);

        ApprovalStats approvalStats = getApprovalRateByCourse(courseCode);
        stats.setTotalRequests(approvalStats.getTotalRequests());
        stats.setApprovalRate(approvalStats.getApprovalRate());

        return stats;
    }

    /**
     * Genera reporte de un grupo específico.
     *
     * @param groupId ID del grupo
     * @return Estadísticas del grupo
     */
    public GroupStats generateGroupReport(String groupId) {
        Optional<Group> groupOpt = groupRepository.findById(groupId);
        if (groupOpt.isEmpty()) {
            throw new RuntimeException("Grupo no encontrado: " + groupId);
        }

        Group group = groupOpt.get();
        Classroom classroom = group.getClassroom();

        GroupStats stats = new GroupStats();
        stats.setGroupId(groupId);
        stats.setCapacity(classroom != null ? classroom.getCapacity() : 0);
        stats.setCurrentStudents(getCurrentStudentCountInGroup(groupId));
        stats.setOccupancyRate(calculateOccupancyRate(groupId));
        stats.setIncomingRequests(scheduleChangeRequestRepository.findByRequestedGroupId(groupId).size());
        stats.setOutgoingRequests(scheduleChangeRequestRepository.findByCurrentGroupId(groupId).size());

        return stats;
    }

    /**
     * Genera reporte de una facultad específica.
     *
     * @param faculty Nombre de la facultad
     * @return Estadísticas de la facultad
     */
    public FacultyStats generateFacultyReport(String faculty) {
        List<StudentAcademicProgress> facultyStudents = studentAcademicProgressRepository.findByFaculty(faculty);
        List<ScheduleChangeRequest> facultyRequests = getRequestsByFaculty(faculty);
        ApprovalStats approvalStats = getApprovalRateByFaculty(faculty);

        FacultyStats stats = new FacultyStats();
        stats.setFaculty(faculty);
        stats.setTotalStudents(facultyStudents.size());
        stats.setTotalRequests(facultyRequests.size());
        stats.setApprovalRate(approvalStats.getApprovalRate());
        stats.setRequestsByCourse(calculateRequestsByCourse(facultyRequests));

        return stats;
    }

    /**
     * Genera reporte global del sistema.
     *
     * @return Estadísticas globales
     */
    public GlobalStats generateGlobalReport() {
        List<ScheduleChangeRequest> allRequests = scheduleChangeRequestRepository.findAll();
        ApprovalStats globalApproval = getGlobalApprovalRate();

        GlobalStats stats = new GlobalStats();
        stats.setTotalRequests(allRequests.size());
        stats.setTotalApproved(globalApproval.getApprovedRequests());
        stats.setTotalRejected(globalApproval.getRejectedRequests());
        stats.setTotalPending(globalApproval.getPendingRequests());
        stats.setOverallApprovalRate(globalApproval.getApprovalRate());
        stats.setRequestsByFaculty(calculateRequestsByFaculty(allRequests));
        stats.setRequestsByStatus(calculateRequestsByStatus(allRequests));

        return stats;
    }


    private ScheduleChangeRequest findRequestById(String requestId) {
        return scheduleChangeRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada: " + requestId));
    }

    private boolean isSpecialCase(ScheduleChangeRequest request) {


        if (request.getReviewHistory().size() > 3) {
            return true;
        }

        if (request.getReason() != null &&
                (request.getReason().toLowerCase().contains("médico") ||
                        request.getReason().toLowerCase().contains("emergencia"))) {
            return true;
        }

        long daysPending = TimeUnit.MILLISECONDS.toDays(
                System.currentTimeMillis() - request.getSubmissionDate().getTime()
        );
        if (daysPending > 14) {
            return true;
        }

        return false;
    }

    private double calculateRequestPriority(ScheduleChangeRequest request) {
        double priority = 0.0;


        if (request.getReason() != null) {
            String reason = request.getReason().toLowerCase();
            if (reason.contains("médico") || reason.contains("emergencia")) {
                priority += 3.0;
            } else if (reason.contains("trabajo") || reason.contains("familia")) {
                priority += 2.0;
            }
        }

        long daysInQueue = TimeUnit.MILLISECONDS.toDays(
                System.currentTimeMillis() - request.getSubmissionDate().getTime()
        );
        priority += Math.min(daysInQueue / 7.0, 2.0);

        Student student = request.getStudent();
        if (student != null && student.getGradeAverage() != null) {
            if (student.getGradeAverage() >= 4.0) {
                priority += 1.5;
            } else if (student.getGradeAverage() >= 3.0) {
                priority += 1.0;
            }
        }

        return priority;
    }

    private ApprovalStats calculateApprovalStats(List<ScheduleChangeRequest> requests) {
        ApprovalStats stats = new ApprovalStats();

        int total = requests.size();
        int approved = (int) requests.stream()
                .filter(req -> req.getStatus() == RequestStatus.APPROVED)
                .count();
        int rejected = (int) requests.stream()
                .filter(req -> req.getStatus() == RequestStatus.REJECTED)
                .count();
        int pending = (int) requests.stream()
                .filter(req -> req.getStatus() == RequestStatus.PENDING ||
                        req.getStatus() == RequestStatus.UNDER_REVIEW)
                .count();

        double approvalRate = total > 0 ? ((double) approved / total) * 100 : 0.0;

        stats.setTotalRequests(total);
        stats.setApprovedRequests(approved);
        stats.setRejectedRequests(rejected);
        stats.setPendingRequests(pending);
        stats.setApprovalRate(Math.round(approvalRate * 100.0) / 100.0);

        return stats;
    }

    private Map<String, Integer> calculateReassignmentsByFaculty(List<ScheduleChangeRequest> approvedRequests) {
        Map<String, Integer> reassignmentsByFaculty = new HashMap<>();

        for (ScheduleChangeRequest request : approvedRequests) {
            String faculty = getStudentFaculty(request.getStudent());
            reassignmentsByFaculty.merge(faculty, 1, Integer::sum);
        }

        return reassignmentsByFaculty;
    }

    private Map<String, Integer> calculateReassignmentsByCourse(List<ScheduleChangeRequest> approvedRequests) {
        Map<String, Integer> reassignmentsByCourse = new HashMap<>();

        for (ScheduleChangeRequest request : approvedRequests) {
            String courseCode = request.getRequestedGroup().getCourse().getCourseCode();
            reassignmentsByCourse.merge(courseCode, 1, Integer::sum);
        }

        return reassignmentsByCourse;
    }

    private double calculateAverageProcessingTime(List<ScheduleChangeRequest> approvedRequests) {
        if (approvedRequests.isEmpty()) return 0.0;

        double totalDays = 0;
        int count = 0;

        for (ScheduleChangeRequest request : approvedRequests) {
            if (request.getSubmissionDate() != null && request.getResolutionDate() != null) {
                long diff = request.getResolutionDate().getTime() - request.getSubmissionDate().getTime();
                totalDays += TimeUnit.MILLISECONDS.toDays(diff);
                count++;
            }
        }

        return count > 0 ? totalDays / count : 0.0;
    }

    private Map<String, Integer> calculateRequestsByCourse(List<ScheduleChangeRequest> requests) {
        Map<String, Integer> requestsByCourse = new HashMap<>();

        for (ScheduleChangeRequest request : requests) {
            String courseCode = request.getRequestedGroup().getCourse().getCourseCode();
            requestsByCourse.merge(courseCode, 1, Integer::sum);
        }

        return requestsByCourse;
    }

    private Map<String, Integer> calculateRequestsByFaculty(List<ScheduleChangeRequest> requests) {
        Map<String, Integer> requestsByFaculty = new HashMap<>();

        for (ScheduleChangeRequest request : requests) {
            String faculty = getStudentFaculty(request.getStudent());
            requestsByFaculty.merge(faculty, 1, Integer::sum);
        }

        return requestsByFaculty;
    }

    private Map<String, Integer> calculateRequestsByStatus(List<ScheduleChangeRequest> requests) {
        Map<String, Integer> requestsByStatus = new HashMap<>();

        for (ScheduleChangeRequest request : requests) {
            String status = request.getStatus().name();
            requestsByStatus.merge(status, 1, Integer::sum);
        }

        return requestsByStatus;
    }

    private String getStudentFaculty(Student student) {
        if (student == null) return "DESCONOCIDA";

        List<StudentAcademicProgress> progressList = studentAcademicProgressRepository
                .findByAcademicProgram(student.getAcademicProgram());

        StudentAcademicProgress progress = progressList.stream()
                .filter(p -> p.getStudent() != null && p.getStudent().getId().equals(student.getId()))
                .findFirst()
                .orElse(null);

        return progress != null && progress.getFaculty() != null ? progress.getFaculty() : "DESCONOCIDA";
    }

    private int getCurrentStudentCountInGroup(String groupId) {
        List<ScheduleChangeRequest> groupRequests = scheduleChangeRequestRepository.findByRequestedGroupId(groupId);
        long approvedCount = groupRequests.stream()
                .filter(req -> req.getStatus() == RequestStatus.APPROVED)
                .count();
        return 20 + (int) approvedCount;
    }

    private double calculateOccupancyRate(String groupId) {
        Optional<Group> group = groupRepository.findById(groupId);
        if (group.isEmpty()) return 0.0;

        Classroom classroom = group.get().getClassroom();
        if (classroom == null) return 0.0;

        int currentStudents = getCurrentStudentCountInGroup(groupId);
        return ((double) currentStudents / classroom.getCapacity()) * 100;
    }


    public static class ApprovalStats {
        private double approvalRate;
        private int totalRequests;
        private int approvedRequests;
        private int rejectedRequests;
        private int pendingRequests;

        public double getApprovalRate() { return approvalRate; }
        public void setApprovalRate(double approvalRate) { this.approvalRate = approvalRate; }
        public int getTotalRequests() { return totalRequests; }
        public void setTotalRequests(int totalRequests) { this.totalRequests = totalRequests; }
        public int getApprovedRequests() { return approvedRequests; }
        public void setApprovedRequests(int approvedRequests) { this.approvedRequests = approvedRequests; }
        public int getRejectedRequests() { return rejectedRequests; }
        public void setRejectedRequests(int rejectedRequests) { this.rejectedRequests = rejectedRequests; }
        public int getPendingRequests() { return pendingRequests; }
        public void setPendingRequests(int pendingRequests) { this.pendingRequests = pendingRequests; }
    }

    public static class ReassignmentStats {
        private int totalReassignments;
        private Map<String, Integer> reassignmentsByFaculty;
        private Map<String, Integer> reassignmentsByCourse;
        private double averageProcessingTime;

        public int getTotalReassignments() { return totalReassignments; }
        public void setTotalReassignments(int totalReassignments) { this.totalReassignments = totalReassignments; }
        public Map<String, Integer> getReassignmentsByFaculty() { return reassignmentsByFaculty; }
        public void setReassignmentsByFaculty(Map<String, Integer> reassignmentsByFaculty) { this.reassignmentsByFaculty = reassignmentsByFaculty; }
        public Map<String, Integer> getReassignmentsByCourse() { return reassignmentsByCourse; }
        public void setReassignmentsByCourse(Map<String, Integer> reassignmentsByCourse) { this.reassignmentsByCourse = reassignmentsByCourse; }
        public double getAverageProcessingTime() { return averageProcessingTime; }
        public void setAverageProcessingTime(double averageProcessingTime) { this.averageProcessingTime = averageProcessingTime; }
    }

    public static class CourseStats {
        private String courseCode;
        private String courseName;
        private int totalRequests;
        private double approvalRate;
        private Map<String, Integer> requestsByGroup;

        public String getCourseCode() { return courseCode; }
        public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
        public String getCourseName() { return courseName; }
        public void setCourseName(String courseName) { this.courseName = courseName; }
        public int getTotalRequests() { return totalRequests; }
        public void setTotalRequests(int totalRequests) { this.totalRequests = totalRequests; }
        public double getApprovalRate() { return approvalRate; }
        public void setApprovalRate(double approvalRate) { this.approvalRate = approvalRate; }
        public Map<String, Integer> getRequestsByGroup() { return requestsByGroup; }
        public void setRequestsByGroup(Map<String, Integer> requestsByGroup) { this.requestsByGroup = requestsByGroup; }
    }

    public static class GroupStats {
        private String groupId;
        private int currentStudents;
        private int capacity;
        private double occupancyRate;
        private int incomingRequests;
        private int outgoingRequests;

        public String getGroupId() { return groupId; }
        public void setGroupId(String groupId) { this.groupId = groupId; }
        public int getCurrentStudents() { return currentStudents; }
        public void setCurrentStudents(int currentStudents) { this.currentStudents = currentStudents; }
        public int getCapacity() { return capacity; }
        public void setCapacity(int capacity) { this.capacity = capacity; }
        public double getOccupancyRate() { return occupancyRate; }
        public void setOccupancyRate(double occupancyRate) { this.occupancyRate = occupancyRate; }
        public int getIncomingRequests() { return incomingRequests; }
        public void setIncomingRequests(int incomingRequests) { this.incomingRequests = incomingRequests; }
        public int getOutgoingRequests() { return outgoingRequests; }
        public void setOutgoingRequests(int outgoingRequests) { this.outgoingRequests = outgoingRequests; }
    }

    public static class FacultyStats {
        private String faculty;
        private int totalStudents;
        private int totalRequests;
        private double approvalRate;
        private Map<String, Integer> requestsByCourse;

        public String getFaculty() { return faculty; }
        public void setFaculty(String faculty) { this.faculty = faculty; }
        public int getTotalStudents() { return totalStudents; }
        public void setTotalStudents(int totalStudents) { this.totalStudents = totalStudents; }
        public int getTotalRequests() { return totalRequests; }
        public void setTotalRequests(int totalRequests) { this.totalRequests = totalRequests; }
        public double getApprovalRate() { return approvalRate; }
        public void setApprovalRate(double approvalRate) { this.approvalRate = approvalRate; }
        public Map<String, Integer> getRequestsByCourse() { return requestsByCourse; }
        public void setRequestsByCourse(Map<String, Integer> requestsByCourse) { this.requestsByCourse = requestsByCourse; }
    }

    public static class GlobalStats {
        private int totalRequests;
        private int totalApproved;
        private int totalRejected;
        private int totalPending;
        private double overallApprovalRate;
        private Map<String, Integer> requestsByFaculty;
        private Map<String, Integer> requestsByStatus;

        public int getTotalRequests() { return totalRequests; }
        public void setTotalRequests(int totalRequests) { this.totalRequests = totalRequests; }
        public int getTotalApproved() { return totalApproved; }
        public void setTotalApproved(int totalApproved) { this.totalApproved = totalApproved; }
        public int getTotalRejected() { return totalRejected; }
        public void setTotalRejected(int totalRejected) { this.totalRejected = totalRejected; }
        public int getTotalPending() { return totalPending; }
        public void setTotalPending(int totalPending) { this.totalPending = totalPending; }
        public double getOverallApprovalRate() { return overallApprovalRate; }
        public void setOverallApprovalRate(double overallApprovalRate) { this.overallApprovalRate = overallApprovalRate; }
        public Map<String, Integer> getRequestsByFaculty() { return requestsByFaculty; }
        public void setRequestsByFaculty(Map<String, Integer> requestsByFaculty) { this.requestsByFaculty = requestsByFaculty; }
        public Map<String, Integer> getRequestsByStatus() { return requestsByStatus; }
        public void setRequestsByStatus(Map<String, Integer> requestsByStatus) { this.requestsByStatus = requestsByStatus; }
    }

    private static class RequestWithPriority {
        ScheduleChangeRequest request;
        double priority;

        RequestWithPriority(ScheduleChangeRequest request, double priority) {
            this.request = request;
            this.priority = priority;
        }
    }
}
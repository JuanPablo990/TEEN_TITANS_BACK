package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.RequestStatus;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class AdminRequestService {

    private final ScheduleChangeRequestRepository scheduleChangeRequestRepository;
    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;
    private final CourseRepository courseRepository;
    private final StudentAcademicProgressRepository studentAcademicProgressRepository;

    @Autowired
    public AdminRequestService(ScheduleChangeRequestRepository scheduleChangeRequestRepository,
                               StudentRepository studentRepository,
                               GroupRepository groupRepository,
                               CourseRepository courseRepository,
                               StudentAcademicProgressRepository studentAcademicProgressRepository) {
        this.scheduleChangeRequestRepository = scheduleChangeRequestRepository;
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
        this.courseRepository = courseRepository;
        this.studentAcademicProgressRepository = studentAcademicProgressRepository;
    }

    // ========== MÉTODOS PRINCIPALES SIMPLIFICADOS ==========

    public List<ScheduleChangeRequest> getRequestsByFaculty(String faculty) {
        // Obtener todos los progresos académicos de la facultad
        List<StudentAcademicProgress> facultyProgress = studentAcademicProgressRepository.findByFaculty(faculty);

        // Extraer los IDs de estudiantes de manera simple
        List<String> facultyStudentIds = new ArrayList<>();
        for (StudentAcademicProgress progress : facultyProgress) {
            if (progress.getStudent() != null && progress.getStudent().getId() != null) {
                facultyStudentIds.add(progress.getStudent().getId());
            }
        }

        // Buscar solicitudes de estos estudiantes
        List<ScheduleChangeRequest> allRequests = scheduleChangeRequestRepository.findAll();
        List<ScheduleChangeRequest> facultyRequests = new ArrayList<>();

        for (ScheduleChangeRequest request : allRequests) {
            if (request.getStudent() != null && facultyStudentIds.contains(request.getStudent().getId())) {
                facultyRequests.add(request);
            }
        }

        // Ordenar por fecha
        facultyRequests.sort((r1, r2) -> r2.getSubmissionDate().compareTo(r1.getSubmissionDate()));
        return facultyRequests;
    }

    public ScheduleChangeRequest respondToRequest(String requestId, RequestStatus decision, String comments) {
        ScheduleChangeRequest request = findRequestById(requestId);

        if (decision != RequestStatus.APPROVED && decision != RequestStatus.REJECTED) {
            throw new AppException("La decisión debe ser APPROVED o REJECTED");
        }

        request.setStatus(decision);
        request.setResolutionDate(new Date());

        // Crear paso de revisión simple
        ReviewStep decisionStep = new ReviewStep();
        decisionStep.setUserId("ADMIN_SYSTEM");
        decisionStep.setUserRole(UserRole.ADMINISTRATOR);
        decisionStep.setAction(decision == RequestStatus.APPROVED ? "SOLICITUD_APROBADA" : "SOLICITUD_RECHAZADA");
        decisionStep.setComments(comments);
        decisionStep.setTimestamp(new Date());

        request.getReviewHistory().add(decisionStep);

        return scheduleChangeRequestRepository.save(request);
    }

    public ScheduleChangeRequest requestAdditionalInfo(String requestId, String comments) {
        ScheduleChangeRequest request = findRequestById(requestId);
        request.setStatus(RequestStatus.UNDER_REVIEW);

        ReviewStep infoStep = new ReviewStep();
        infoStep.setUserId("ADMIN_SYSTEM");
        infoStep.setUserRole(UserRole.ADMINISTRATOR);
        infoStep.setAction("INFORMACION_ADICIONAL_SOLICITADA");
        infoStep.setComments("Se requiere información adicional: " + comments);
        infoStep.setTimestamp(new Date());

        request.getReviewHistory().add(infoStep);

        return scheduleChangeRequestRepository.save(request);
    }

    public List<ScheduleChangeRequest> getGlobalRequests() {
        List<ScheduleChangeRequest> allRequests = scheduleChangeRequestRepository.findAll();
        allRequests.sort((r1, r2) -> r2.getSubmissionDate().compareTo(r1.getSubmissionDate()));
        return allRequests;
    }

    public ScheduleChangeRequest approveSpecialCase(String requestId, String comments) {
        ScheduleChangeRequest request = findRequestById(requestId);
        request.setStatus(RequestStatus.APPROVED);
        request.setResolutionDate(new Date());

        ReviewStep specialStep = new ReviewStep();
        specialStep.setUserId("ADMIN_SYSTEM");
        specialStep.setUserRole(UserRole.ADMINISTRATOR);
        specialStep.setAction("CASO_ESPECIAL_APROBADO");
        specialStep.setComments("Aprobación especial: " + comments);
        specialStep.setTimestamp(new Date());

        request.getReviewHistory().add(specialStep);

        return scheduleChangeRequestRepository.save(request);
    }

    public List<ScheduleChangeRequest> getSpecialCases() {
        List<ScheduleChangeRequest> allRequests = scheduleChangeRequestRepository.findAll();
        List<ScheduleChangeRequest> specialCases = new ArrayList<>();

        for (ScheduleChangeRequest request : allRequests) {
            if (isSpecialCase(request)) {
                specialCases.add(request);
            }
        }

        specialCases.sort((r1, r2) -> r2.getSubmissionDate().compareTo(r1.getSubmissionDate()));
        return specialCases;
    }

    // ========== MÉTODOS DE ESTADÍSTICAS SIMPLIFICADOS ==========

    public ApprovalStats getApprovalRateByFaculty(String faculty) {
        List<ScheduleChangeRequest> facultyRequests = getRequestsByFaculty(faculty);
        return calculateApprovalStats(facultyRequests);
    }

    public ApprovalStats getGlobalApprovalRate() {
        List<ScheduleChangeRequest> allRequests = scheduleChangeRequestRepository.findAll();
        return calculateApprovalStats(allRequests);
    }

    public GlobalStats generateGlobalReport() {
        List<ScheduleChangeRequest> allRequests = scheduleChangeRequestRepository.findAll();
        ApprovalStats globalApproval = getGlobalApprovalRate();

        GlobalStats stats = new GlobalStats();
        stats.setTotalRequests(allRequests.size());
        stats.setTotalApproved(globalApproval.getApprovedRequests());
        stats.setTotalRejected(globalApproval.getRejectedRequests());
        stats.setTotalPending(globalApproval.getPendingRequests());
        stats.setOverallApprovalRate(globalApproval.getApprovalRate());
        stats.setRequestsByStatus(calculateRequestsByStatus(allRequests));

        return stats;
    }

    public FacultyStats generateFacultyReport(String faculty) {
        List<StudentAcademicProgress> facultyStudents = studentAcademicProgressRepository.findByFaculty(faculty);
        List<ScheduleChangeRequest> facultyRequests = getRequestsByFaculty(faculty);
        ApprovalStats approvalStats = getApprovalRateByFaculty(faculty);

        FacultyStats stats = new FacultyStats();
        stats.setFaculty(faculty);
        stats.setTotalStudents(facultyStudents.size());
        stats.setTotalRequests(facultyRequests.size());
        stats.setApprovalRate(approvalStats.getApprovalRate());

        return stats;
    }

    // ========== MÉTODOS PRIVADOS SIMPLIFICADOS ==========

    private ScheduleChangeRequest findRequestById(String requestId) {
        return scheduleChangeRequestRepository.findById(requestId)
                .orElseThrow(() -> new AppException("Solicitud no encontrada: " + requestId));
    }

    private boolean isSpecialCase(ScheduleChangeRequest request) {
        // Casos con muchas revisiones
        if (request.getReviewHistory().size() > 3) {
            return true;
        }

        // Casos médicos o de emergencia
        if (request.getReason() != null &&
                (request.getReason().toLowerCase().contains("médico") ||
                        request.getReason().toLowerCase().contains("emergencia"))) {
            return true;
        }

        // Casos pendientes por mucho tiempo
        long daysPending = TimeUnit.MILLISECONDS.toDays(
                System.currentTimeMillis() - request.getSubmissionDate().getTime()
        );
        return daysPending > 14;
    }

    private ApprovalStats calculateApprovalStats(List<ScheduleChangeRequest> requests) {
        ApprovalStats stats = new ApprovalStats();

        int total = requests.size();
        int approved = 0;
        int rejected = 0;
        int pending = 0;

        for (ScheduleChangeRequest request : requests) {
            switch (request.getStatus()) {
                case APPROVED -> approved++;
                case REJECTED -> rejected++;
                case PENDING, UNDER_REVIEW -> pending++;
            }
        }

        double approvalRate = total > 0 ? ((double) approved / total) * 100 : 0.0;

        stats.setTotalRequests(total);
        stats.setApprovedRequests(approved);
        stats.setRejectedRequests(rejected);
        stats.setPendingRequests(pending);
        stats.setApprovalRate(Math.round(approvalRate * 100.0) / 100.0);

        return stats;
    }

    private Map<String, Integer> calculateRequestsByStatus(List<ScheduleChangeRequest> requests) {
        Map<String, Integer> requestsByStatus = new HashMap<>();

        for (ScheduleChangeRequest request : requests) {
            String status = request.getStatus().name();
            requestsByStatus.put(status, requestsByStatus.getOrDefault(status, 0) + 1);
        }

        return requestsByStatus;
    }

    // ========== CLASES DE ESTADÍSTICAS ==========

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

    public static class FacultyStats {
        private String faculty;
        private int totalStudents;
        private int totalRequests;
        private double approvalRate;

        public String getFaculty() { return faculty; }
        public void setFaculty(String faculty) { this.faculty = faculty; }
        public int getTotalStudents() { return totalStudents; }
        public void setTotalStudents(int totalStudents) { this.totalStudents = totalStudents; }
        public int getTotalRequests() { return totalRequests; }
        public void setTotalRequests(int totalRequests) { this.totalRequests = totalRequests; }
        public double getApprovalRate() { return approvalRate; }
        public void setApprovalRate(double approvalRate) { this.approvalRate = approvalRate; }
    }

    public static class GlobalStats {
        private int totalRequests;
        private int totalApproved;
        private int totalRejected;
        private int totalPending;
        private double overallApprovalRate;
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
        public Map<String, Integer> getRequestsByStatus() { return requestsByStatus; }
        public void setRequestsByStatus(Map<String, Integer> requestsByStatus) { this.requestsByStatus = requestsByStatus; }
    }
}
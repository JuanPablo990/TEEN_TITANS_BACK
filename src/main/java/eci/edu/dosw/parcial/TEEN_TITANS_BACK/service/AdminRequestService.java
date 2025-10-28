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

/**
 * Servicio para la gestión administrativa de solicitudes de cambio de horario.
 * Proporciona funcionalidades para revisar, aprobar, rechazar y generar reportes de solicitudes.
 *
 ** @author Equipo Teen Titans
 *  * @version 1.0
 *  * @since 2025
 */
@Service
public class AdminRequestService {

    private final ScheduleChangeRequestRepository scheduleChangeRequestRepository;
    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;
    private final CourseRepository courseRepository;
    private final StudentAcademicProgressRepository studentAcademicProgressRepository;

    /**
     * Constructor para la inyección de dependencias.
     *
     * @param scheduleChangeRequestRepository Repositorio de solicitudes de cambio de horario
     * @param studentRepository Repositorio de estudiantes
     * @param groupRepository Repositorio de grupos
     * @param courseRepository Repositorio de cursos
     * @param studentAcademicProgressRepository Repositorio de progreso académico
     */
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

    /**
     * Obtiene las solicitudes de cambio de horario por facultad.
     *
     * @param faculty Facultad a filtrar
     * @return Lista de solicitudes ordenadas por fecha descendente
     */
    public List<ScheduleChangeRequest> getRequestsByFaculty(String faculty) {
        List<StudentAcademicProgress> facultyProgress = studentAcademicProgressRepository.findByFaculty(faculty);

        List<String> facultyStudentIds = new ArrayList<>();
        for (StudentAcademicProgress progress : facultyProgress) {
            if (progress.getStudent() != null && progress.getStudent().getId() != null) {
                facultyStudentIds.add(progress.getStudent().getId());
            }
        }

        List<ScheduleChangeRequest> allRequests = scheduleChangeRequestRepository.findAll();
        List<ScheduleChangeRequest> facultyRequests = new ArrayList<>();

        for (ScheduleChangeRequest request : allRequests) {
            if (request.getStudent() != null && facultyStudentIds.contains(request.getStudent().getId())) {
                facultyRequests.add(request);
            }
        }

        facultyRequests.sort((r1, r2) -> r2.getSubmissionDate().compareTo(r1.getSubmissionDate()));
        return facultyRequests;
    }

    /**
     * Responde a una solicitud con una decisión de aprobación o rechazo.
     *
     * @param requestId ID de la solicitud
     * @param decision Decisión (APPROVED o REJECTED)
     * @param comments Comentarios de la decisión
     * @return Solicitud actualizada
     * @throws AppException si la decisión no es válida
     */
    public ScheduleChangeRequest respondToRequest(String requestId, RequestStatus decision, String comments) {
        ScheduleChangeRequest request = findRequestById(requestId);

        if (decision != RequestStatus.APPROVED && decision != RequestStatus.REJECTED) {
            throw new AppException("La decisión debe ser APPROVED o REJECTED");
        }

        request.setStatus(decision);
        request.setResolutionDate(new Date());

        ReviewStep decisionStep = new ReviewStep();
        decisionStep.setUserId("ADMIN_SYSTEM");
        decisionStep.setUserRole(UserRole.ADMINISTRATOR);
        decisionStep.setAction(decision == RequestStatus.APPROVED ? "SOLICITUD_APROBADA" : "SOLICITUD_RECHAZADA");
        decisionStep.setComments(comments);
        decisionStep.setTimestamp(new Date());

        request.getReviewHistory().add(decisionStep);

        return scheduleChangeRequestRepository.save(request);
    }

    /**
     * Solicita información adicional para una solicitud.
     *
     * @param requestId ID de la solicitud
     * @param comments Comentarios sobre la información requerida
     * @return Solicitud actualizada
     */
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

    /**
     * Obtiene todas las solicitudes del sistema.
     *
     * @return Lista de todas las solicitudes ordenadas por fecha descendente
     */
    public List<ScheduleChangeRequest> getGlobalRequests() {
        List<ScheduleChangeRequest> allRequests = scheduleChangeRequestRepository.findAll();
        allRequests.sort((r1, r2) -> r2.getSubmissionDate().compareTo(r1.getSubmissionDate()));
        return allRequests;
    }

    /**
     * Aprueba un caso especial con comentarios específicos.
     *
     * @param requestId ID de la solicitud
     * @param comments Comentarios de la aprobación especial
     * @return Solicitud aprobada
     */
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

    /**
     * Obtiene los casos especiales del sistema.
     *
     * @return Lista de casos especiales ordenados por fecha descendente
     */
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

    /**
     * Obtiene estadísticas de aprobación por facultad.
     *
     * @param faculty Facultad a consultar
     * @return Estadísticas de aprobación
     */
    public ApprovalStats getApprovalRateByFaculty(String faculty) {
        List<ScheduleChangeRequest> facultyRequests = getRequestsByFaculty(faculty);
        return calculateApprovalStats(facultyRequests);
    }

    /**
     * Obtiene estadísticas de aprobación globales.
     *
     * @return Estadísticas de aprobación globales
     */
    public ApprovalStats getGlobalApprovalRate() {
        List<ScheduleChangeRequest> allRequests = scheduleChangeRequestRepository.findAll();
        return calculateApprovalStats(allRequests);
    }

    /**
     * Genera un reporte global del sistema.
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
        stats.setRequestsByStatus(calculateRequestsByStatus(allRequests));

        return stats;
    }

    /**
     * Genera un reporte específico por facultad.
     *
     * @param faculty Facultad a reportar
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

        return stats;
    }

    /**
     * Busca una solicitud por ID.
     *
     * @param requestId ID de la solicitud
     * @return Solicitud encontrada
     * @throws AppException si no se encuentra la solicitud
     */
    private ScheduleChangeRequest findRequestById(String requestId) {
        return scheduleChangeRequestRepository.findById(requestId)
                .orElseThrow(() -> new AppException("Solicitud no encontrada: " + requestId));
    }

    /**
     * Determina si una solicitud es un caso especial.
     *
     * @param request Solicitud a evaluar
     * @return true si es un caso especial, false en caso contrario
     */
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
        return daysPending > 14;
    }

    /**
     * Calcula estadísticas de aprobación a partir de una lista de solicitudes.
     *
     * @param requests Lista de solicitudes
     * @return Estadísticas de aprobación
     */
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

    /**
     * Calcula la distribución de solicitudes por estado.
     *
     * @param requests Lista de solicitudes
     * @return Mapa con el conteo por estado
     */
    private Map<String, Integer> calculateRequestsByStatus(List<ScheduleChangeRequest> requests) {
        Map<String, Integer> requestsByStatus = new HashMap<>();

        for (ScheduleChangeRequest request : requests) {
            String status = request.getStatus().name();
            requestsByStatus.put(status, requestsByStatus.getOrDefault(status, 0) + 1);
        }

        return requestsByStatus;
    }

    /**
     * Clase para representar estadísticas de aprobación.
     */
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

    /**
     * Clase para representar estadísticas por facultad.
     */
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

    /**
     * Clase para representar estadísticas globales.
     */
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
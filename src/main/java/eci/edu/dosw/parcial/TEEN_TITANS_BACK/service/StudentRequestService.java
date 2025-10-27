package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.RequestStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar las solicitudes de estudiantes con funcionalidades específicas
 * como tasas de aprobación, prioridades, grupos alternativos y tiempos de espera.
 *
 * @author Equipo Teen Titans
 * @version 2.0
 * @since 2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StudentRequestService {

    private final ScheduleChangeRequestRepository scheduleChangeRequestRepository;
    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;
    private final ReviewStepRepository reviewStepRepository;
    private final StudentAcademicProgressRepository studentAcademicProgressRepository;

    // Constantes para configuraciones
    private static final double GPA_WEIGHT = 0.4;
    private static final double SEMESTER_WEIGHT = 0.2;
    private static final double URGENCY_WEIGHT = 0.2;
    private static final double TIME_WEIGHT = 0.2;
    private static final double CAPACITY_ALERT_THRESHOLD = 0.8;
    private static final int MAX_RECOMMENDED_GROUPS = 5;
    private static final int MAX_REVIEW_HOURS_FOR_CANCELLATION = 24;
    private static final int DEFAULT_AVERAGE_PROCESSING_DAYS = 3;

    /**
     * Calcula la tasa de aprobación histórica de un estudiante.
     */
    public double getStudentApprovalRate(String studentId) {
        validateStudentExists(studentId);

        List<ScheduleChangeRequest> allRequests = scheduleChangeRequestRepository.findByStudentId(studentId);

        if (allRequests.isEmpty()) {
            return 0.0;
        }

        List<ScheduleChangeRequest> resolvedRequests = filterResolvedRequests(allRequests);

        if (resolvedRequests.isEmpty()) {
            return 0.0;
        }

        long approvedCount = countApprovedRequests(resolvedRequests);
        return calculateApprovalRate(approvedCount, resolvedRequests.size());
    }

    /**
     * Obtiene todas las solicitudes pendientes de un estudiante.
     */
    public List<ScheduleChangeRequest> getPendingRequestsStatus(String studentId) {
        validateStudentExists(studentId);

        List<ScheduleChangeRequest> pendingRequests = scheduleChangeRequestRepository
                .findByStudentIdAndStatus(studentId, RequestStatus.PENDING);

        List<ScheduleChangeRequest> underReviewRequests = scheduleChangeRequestRepository
                .findByStudentIdAndStatus(studentId, RequestStatus.UNDER_REVIEW);

        return mergeAndSortRequests(pendingRequests, underReviewRequests);
    }

    /**
     * Calcula la posición de prioridad de una solicitud en la cola de procesamiento.
     */
    public int getRequestPriorityPosition(String requestId) {
        ScheduleChangeRequest request = findRequestById(requestId);
        RequestPriority currentRequestPriority = calculateRequestPriority(request);

        List<RequestPriority> allPendingPriorities = getAllPendingRequestPriorities();

        return findPriorityPosition(currentRequestPriority, allPendingPriorities);
    }

    /**
     * Obtiene grupos alternativos recomendados para una solicitud.
     */
    public List<Group> getRecommendedAlternativeGroups(String requestId) {
        ScheduleChangeRequest request = findRequestById(requestId);
        Group requestedGroup = request.getRequestedGroup();
        Course course = requestedGroup.getCourse();

        List<Group> allCourseGroups = groupRepository.findByCourse_CourseCode(course.getCourseCode());

        return allCourseGroups.stream()
                .filter(group -> !group.getGroupId().equals(requestedGroup.getGroupId()))
                .filter(this::isGroupAvailable)
                .filter(group -> isScheduleCompatible(request.getCurrentGroup().getSchedule(), group.getSchedule()))
                .sorted(this::compareGroupsByCapacity)
                .limit(MAX_RECOMMENDED_GROUPS)
                .collect(Collectors.toList());
    }

    /**
     * Calcula el tiempo de espera estimado para una solicitud.
     */
    public String getRequestEstimatedWaitTime(String requestId) {
        ScheduleChangeRequest request = findRequestById(requestId);

        if (!isRequestPendingOrUnderReview(request)) {
            return "Solicitud ya procesada";
        }

        int position = getRequestPriorityPosition(requestId);
        double estimatedDays = calculateEstimatedProcessingDays(position);

        return formatWaitTimeEstimation(estimatedDays);
    }

    /**
     * Determina si una solicitud puede ser cancelada según su estado y tiempo en revisión.
     */
    public boolean canCancelRequest(String requestId) {
        ScheduleChangeRequest request = findRequestById(requestId);

        if (!isRequestPendingOrUnderReview(request)) {
            return false;
        }

        if (request.getStatus() == RequestStatus.UNDER_REVIEW) {
            return calculateHoursInReview(request) <= MAX_REVIEW_HOURS_FOR_CANCELLATION;
        }

        return true;
    }

    /**
     * Obtiene la lista de documentos requeridos según el tipo de solicitud.
     */
    public List<String> getRequiredDocuments(String requestType) {
        RequestDocumentType documentType = RequestDocumentType.fromString(requestType);
        return documentType.getRequiredDocuments();
    }

    // Métodos privados de validación
    private void validateStudentExists(String studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new AppException("El estudiante con ID " + studentId + " no existe.");
        }
    }

    private ScheduleChangeRequest findRequestById(String requestId) {
        return scheduleChangeRequestRepository.findById(requestId)
                .orElseThrow(() -> new AppException("No se encontró la solicitud con ID: " + requestId));
    }

    // Métodos privados de filtrado y cálculo
    private List<ScheduleChangeRequest> filterResolvedRequests(List<ScheduleChangeRequest> requests) {
        return requests.stream()
                .filter(request -> request.getStatus() == RequestStatus.APPROVED ||
                        request.getStatus() == RequestStatus.REJECTED)
                .collect(Collectors.toList());
    }

    private long countApprovedRequests(List<ScheduleChangeRequest> resolvedRequests) {
        return resolvedRequests.stream()
                .filter(request -> request.getStatus() == RequestStatus.APPROVED)
                .count();
    }

    private double calculateApprovalRate(long approvedCount, int totalResolved) {
        double approvalRate = ((double) approvedCount / totalResolved) * 100;
        return Math.round(approvalRate * 100.0) / 100.0;
    }

    private List<ScheduleChangeRequest> mergeAndSortRequests(List<ScheduleChangeRequest> list1,
                                                             List<ScheduleChangeRequest> list2) {
        List<ScheduleChangeRequest> allRequests = new ArrayList<>();
        allRequests.addAll(list1);
        allRequests.addAll(list2);

        allRequests.sort((r1, r2) -> r2.getSubmissionDate().compareTo(r1.getSubmissionDate()));
        return allRequests;
    }

    // Métodos de cálculo de prioridad
    private RequestPriority calculateRequestPriority(ScheduleChangeRequest request) {
        Student student = request.getStudent();
        StudentAcademicProgress progress = getStudentAcademicProgress(student);
        double priorityScore = calculatePriorityScore(student, progress, request);

        return new RequestPriority(request.getRequestId(), priorityScore, request.getSubmissionDate());
    }

    private StudentAcademicProgress getStudentAcademicProgress(Student student) {
        return studentAcademicProgressRepository.findByStudentId(student.getId())
                .orElse(null);
    }

    private double calculatePriorityScore(Student student, StudentAcademicProgress progress, ScheduleChangeRequest request) {
        double score = 0.0;

        if (progress != null && progress.getCumulativeGPA() != null) {
            score += progress.getCumulativeGPA() * GPA_WEIGHT;
        }

        if (student.getSemester() != null) {
            score += (student.getSemester() / 10.0) * SEMESTER_WEIGHT;
        }

        score += calculateUrgencyFactor(request.getReason()) * URGENCY_WEIGHT;

        long daysOld = calculateDaysInQueue(request.getSubmissionDate());
        score += (Math.min(daysOld, 30) / 30.0) * TIME_WEIGHT;

        return score;
    }

    private double calculateUrgencyFactor(String reason) {
        if (!StringUtils.hasText(reason)) {
            return 0.3;
        }

        String lowerReason = reason.toLowerCase();
        if (lowerReason.contains("médico") || lowerReason.contains("salud") || lowerReason.contains("emergencia")) {
            return 1.0;
        } else if (lowerReason.contains("trabajo") || lowerReason.contains("familia")) {
            return 0.7;
        } else if (lowerReason.contains("horario") || lowerReason.contains("conflicto")) {
            return 0.5;
        } else {
            return 0.3;
        }
    }

    private List<RequestPriority> getAllPendingRequestPriorities() {
        List<ScheduleChangeRequest> allPendingRequests = scheduleChangeRequestRepository
                .findByStatus(RequestStatus.PENDING);

        return allPendingRequests.stream()
                .map(this::calculateRequestPriority)
                .collect(Collectors.toList());
    }

    private int findPriorityPosition(RequestPriority targetPriority, List<RequestPriority> allPriorities) {
        List<RequestPriority> sortedPriorities = allPriorities.stream()
                .sorted(this::compareRequestPriorities)
                .collect(Collectors.toList());

        for (int i = 0; i < sortedPriorities.size(); i++) {
            if (sortedPriorities.get(i).requestId.equals(targetPriority.requestId)) {
                return i + 1;
            }
        }

        return -1;
    }

    private int compareRequestPriorities(RequestPriority p1, RequestPriority p2) {
        int scoreCompare = Double.compare(p2.score, p1.score);
        if (scoreCompare != 0) return scoreCompare;
        return p1.submissionDate.compareTo(p2.submissionDate);
    }

    // Métodos de validación de grupos
    private boolean isGroupAvailable(Group group) {
        return !getGroupCapacityAlert(group.getGroupId());
    }

    private boolean getGroupCapacityAlert(String groupId) {
        Optional<Group> group = groupRepository.findById(groupId);
        if (group.isEmpty()) return true;

        Classroom classroom = group.get().getClassroom();
        if (classroom == null) return false;

        int currentStudents = getCurrentStudentCountInGroup(groupId);
        double occupancyRate = (double) currentStudents / classroom.getCapacity();
        return occupancyRate >= CAPACITY_ALERT_THRESHOLD;
    }

    private int getCurrentStudentCountInGroup(String groupId) {
        List<ScheduleChangeRequest> groupRequests = scheduleChangeRequestRepository.findByRequestedGroupId(groupId);
        long approvedCount = groupRequests.stream()
                .filter(req -> req.getStatus() == RequestStatus.APPROVED)
                .count();
        return 20 + (int) approvedCount; // Base de 20 estudiantes más los aprobados
    }

    private boolean isScheduleCompatible(Schedule currentSchedule, Schedule newSchedule) {
        if (currentSchedule == null || newSchedule == null) return true;
        return !currentSchedule.getDayOfWeek().equals(newSchedule.getDayOfWeek()) ||
                !currentSchedule.getPeriod().equals(newSchedule.getPeriod());
    }

    private int compareGroupsByCapacity(Group g1, Group g2) {
        int capacity1 = g1.getClassroom().getCapacity() - getCurrentStudentCountInGroup(g1.getGroupId());
        int capacity2 = g2.getClassroom().getCapacity() - getCurrentStudentCountInGroup(g2.getGroupId());
        return Integer.compare(capacity2, capacity1);
    }

    // Métodos de cálculo de tiempo
    private boolean isRequestPendingOrUnderReview(ScheduleChangeRequest request) {
        return request.getStatus() == RequestStatus.PENDING ||
                request.getStatus() == RequestStatus.UNDER_REVIEW;
    }

    private double calculateEstimatedProcessingDays(int position) {
        double avgProcessingDays = calculateAverageProcessingTime();
        return (position * avgProcessingDays) / 2.0;
    }

    private String formatWaitTimeEstimation(double estimatedDays) {
        if (estimatedDays < 1) return "Menos de 1 día";
        else if (estimatedDays <= 3) return "1-3 días";
        else if (estimatedDays <= 7) return "3-7 días";
        else return "Más de 1 semana";
    }

    private long calculateDaysInQueue(Date submissionDate) {
        long diff = System.currentTimeMillis() - submissionDate.getTime();
        return TimeUnit.MILLISECONDS.toDays(diff);
    }

    private long calculateHoursInReview(ScheduleChangeRequest request) {
        Optional<ReviewStep> reviewStep = request.getReviewHistory().stream()
                .filter(step -> "EN_REVISION".equals(step.getAction()))
                .findFirst();

        return reviewStep.map(step ->
                TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis() - step.getTimestamp().getTime())
        ).orElse(0L);
    }

    private double calculateAverageProcessingTime() {
        Date lastMonth = Date.from(LocalDateTime.now().minusDays(30).atZone(ZoneId.systemDefault()).toInstant());

        List<ScheduleChangeRequest> recentResolved = scheduleChangeRequestRepository.findByResolutionDateAfter(lastMonth);
        if (recentResolved.isEmpty()) return DEFAULT_AVERAGE_PROCESSING_DAYS;

        double totalDays = recentResolved.stream()
                .filter(req -> req.getSubmissionDate() != null && req.getResolutionDate() != null)
                .mapToLong(req -> {
                    long diff = req.getResolutionDate().getTime() - req.getSubmissionDate().getTime();
                    return TimeUnit.MILLISECONDS.toDays(diff);
                })
                .sum();

        long count = recentResolved.stream()
                .filter(req -> req.getSubmissionDate() != null && req.getResolutionDate() != null)
                .count();

        return count > 0 ? totalDays / count : DEFAULT_AVERAGE_PROCESSING_DAYS;
    }

    // Clases internas y enums auxiliares
    private static class RequestPriority {
        final String requestId;
        final double score;
        final Date submissionDate;

        RequestPriority(String requestId, double score, Date submissionDate) {
            this.requestId = requestId;
            this.score = score;
            this.submissionDate = submissionDate;
        }
    }

    private enum RequestDocumentType {
        GROUP_CHANGE(Arrays.asList("Formulario de cambio de grupo", "Justificación escrita",
                "Horario actual", "Horario solicitado")),

        COURSE_CHANGE(Arrays.asList("Formulario de cambio de curso", "Justificación académica",
                "Plan de estudios actual", "Aprobación del coordinador",
                "Historial académico")),

        SPECIAL_REQUEST(Arrays.asList("Formulario de solicitud especial", "Informe médico o justificación",
                "Aprobación del decano", "Plan de contingencia")),

        DEFAULT(Arrays.asList("Formulario general de solicitud", "Documento de identificación"));

        private final List<String> requiredDocuments;

        RequestDocumentType(List<String> requiredDocuments) {
            this.requiredDocuments = requiredDocuments;
        }

        public List<String> getRequiredDocuments() {
            return new ArrayList<>(requiredDocuments);
        }

        public static RequestDocumentType fromString(String requestType) {
            if (!StringUtils.hasText(requestType)) {
                return DEFAULT;
            }

            try {
                return RequestDocumentType.valueOf(requestType.toUpperCase());
            } catch (IllegalArgumentException e) {
                log.warn("Tipo de solicitud no reconocido: {}, usando documentos por defecto", requestType);
                return DEFAULT;
            }
        }
    }
}
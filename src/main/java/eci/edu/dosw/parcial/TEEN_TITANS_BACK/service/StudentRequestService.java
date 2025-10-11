package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar las solicitudes de estudiantes con funcionalidades específicas
 * como tasas de aprobación, prioridades, grupos alternativos y tiempos de espera.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Service
public class StudentRequestService {

    private final ScheduleChangeRequestRepository scheduleChangeRequestRepository;
    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;
    private final ReviewStepRepository reviewStepRepository;
    private final StudentAcademicProgressRepository studentAcademicProgressRepository;

    /**
     * Constructor para inyección de dependencias.
     */
    @Autowired
    public StudentRequestService(ScheduleChangeRequestRepository scheduleChangeRequestRepository,
                                 StudentRepository studentRepository,
                                 GroupRepository groupRepository,
                                 ReviewStepRepository reviewStepRepository,
                                 StudentAcademicProgressRepository studentAcademicProgressRepository) {
        this.scheduleChangeRequestRepository = scheduleChangeRequestRepository;
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
        this.reviewStepRepository = reviewStepRepository;
        this.studentAcademicProgressRepository = studentAcademicProgressRepository;
    }

    /**
     * Calcula la tasa de aprobación histórica de un estudiante.
     * La tasa se calcula como: (solicitudes aprobadas / total de solicitudes resueltas) * 100
     *
     * @param studentId ID del estudiante
     * @return Tasa de aprobación en porcentaje (0.0 a 100.0)
     * @throws AppException si el estudiante no existe
     */
    public double getStudentApprovalRate(String studentId) {
        validateStudent(studentId);

        List<ScheduleChangeRequest> allRequests = scheduleChangeRequestRepository.findByStudentId(studentId);

        if (allRequests.isEmpty()) {
            return 0.0;
        }

        List<ScheduleChangeRequest> resolvedRequests = allRequests.stream()
                .filter(request -> request.getStatus() == RequestStatus.APPROVED ||
                        request.getStatus() == RequestStatus.REJECTED)
                .collect(Collectors.toList());

        if (resolvedRequests.isEmpty()) {
            return 0.0;
        }

        long approvedCount = resolvedRequests.stream()
                .filter(request -> request.getStatus() == RequestStatus.APPROVED)
                .count();

        double approvalRate = ((double) approvedCount / resolvedRequests.size()) * 100;
        return Math.round(approvalRate * 100.0) / 100.0;
    }

    /**
     * Obtiene todas las solicitudes pendientes de un estudiante.
     *
     * @param studentId ID del estudiante
     * @return Lista de solicitudes en estado PENDING o UNDER_REVIEW
     * @throws AppException si el estudiante no existe
     */
    public List<ScheduleChangeRequest> getPendingRequestsStatus(String studentId) {
        validateStudent(studentId);

        List<ScheduleChangeRequest> pendingRequests = scheduleChangeRequestRepository
                .findByStudentIdAndStatus(studentId, RequestStatus.PENDING);

        List<ScheduleChangeRequest> underReviewRequests = scheduleChangeRequestRepository
                .findByStudentIdAndStatus(studentId, RequestStatus.UNDER_REVIEW);

        List<ScheduleChangeRequest> allPending = new ArrayList<>();
        allPending.addAll(pendingRequests);
        allPending.addAll(underReviewRequests);

        allPending.sort((r1, r2) -> r2.getSubmissionDate().compareTo(r1.getSubmissionDate()));

        return allPending;
    }

    /**
     * Calcula la posición de prioridad de una solicitud en la cola de procesamiento.
     *
     * @param requestId ID de la solicitud
     * @return Posición en la cola de prioridad (1 = mayor prioridad)
     * @throws AppException si la solicitud o el estudiante no existen
     */
    public int getRequestPriorityPosition(String requestId) {
        ScheduleChangeRequest request = findRequestById(requestId);
        String studentId = request.getStudent().getId();

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new AppException("El estudiante con ID " + studentId + " no existe."));

        List<StudentAcademicProgress> progressList = studentAcademicProgressRepository
                .findByAcademicProgram(student.getAcademicProgram());

        StudentAcademicProgress progress = findProgressByStudent(progressList, studentId);

        double priorityScore = calculatePriorityScore(student, progress, request);

        List<ScheduleChangeRequest> allPendingRequests = scheduleChangeRequestRepository
                .findByStatus(RequestStatus.PENDING);

        List<RequestPriority> priorities = new ArrayList<>();
        for (ScheduleChangeRequest req : allPendingRequests) {
            Student reqStudent = req.getStudent();
            List<StudentAcademicProgress> reqProgressList = studentAcademicProgressRepository
                    .findByAcademicProgram(reqStudent.getAcademicProgram());
            StudentAcademicProgress reqProgress = findProgressByStudent(reqProgressList, reqStudent.getId());

            double reqScore = calculatePriorityScore(reqStudent, reqProgress, req);
            priorities.add(new RequestPriority(req.getRequestId(), reqScore, req.getSubmissionDate()));
        }

        priorities.sort((p1, p2) -> {
            int scoreCompare = Double.compare(p2.score, p1.score);
            if (scoreCompare != 0) return scoreCompare;
            return p1.submissionDate.compareTo(p2.submissionDate);
        });

        for (int i = 0; i < priorities.size(); i++) {
            if (priorities.get(i).requestId.equals(requestId)) {
                return i + 1;
            }
        }

        return -1;
    }

    /**
     * Obtiene grupos alternativos recomendados para una solicitud.
     *
     * @param requestId ID de la solicitud
     * @return Lista de grupos alternativos recomendados
     * @throws AppException si la solicitud no existe
     */
    public List<Group> getRecommendedAlternativeGroups(String requestId) {
        ScheduleChangeRequest request = findRequestById(requestId);
        Group requestedGroup = request.getRequestedGroup();
        Course course = requestedGroup.getCourse();

        List<Group> allCourseGroups = groupRepository.findByCourse_CourseCode(course.getCourseCode());

        return allCourseGroups.stream()
                .filter(group -> !group.getGroupId().equals(requestedGroup.getGroupId()))
                .filter(group -> !getGroupCapacityAlert(group.getGroupId()))
                .filter(group -> isScheduleCompatible(request.getCurrentGroup().getSchedule(), group.getSchedule()))
                .sorted((g1, g2) -> {
                    int capacity1 = g1.getClassroom().getCapacity() - getCurrentStudentCountInGroup(g1.getGroupId());
                    int capacity2 = g2.getClassroom().getCapacity() - getCurrentStudentCountInGroup(g2.getGroupId());
                    return Integer.compare(capacity2, capacity1);
                })
                .limit(5)
                .collect(Collectors.toList());
    }

    /**
     * Calcula el tiempo de espera estimado para una solicitud.
     *
     * @param requestId ID de la solicitud
     * @return Estimación del tiempo de espera en formato textual
     * @throws AppException si la solicitud no existe
     */
    public String getRequestEstimatedWaitTime(String requestId) {
        ScheduleChangeRequest request = findRequestById(requestId);

        if (request.getStatus() != RequestStatus.PENDING && request.getStatus() != RequestStatus.UNDER_REVIEW) {
            return "Solicitud ya procesada";
        }

        int position = getRequestPriorityPosition(requestId);
        long daysInQueue = calculateDaysInQueue(request.getSubmissionDate());

        double avgProcessingDays = calculateAverageProcessingTime();
        double estimatedDays = (position * avgProcessingDays) / 2.0;

        if (estimatedDays < 1) return "Menos de 1 día";
        else if (estimatedDays <= 3) return "1-3 días";
        else if (estimatedDays <= 7) return "3-7 días";
        else return "Más de 1 semana";
    }

    /**
     * Determina si una solicitud puede ser cancelada según su estado y tiempo en revisión.
     *
     * @param requestId ID de la solicitud
     * @return true si puede cancelarse, false en caso contrario
     * @throws AppException si la solicitud no existe
     */
    public boolean canCancelRequest(String requestId) {
        ScheduleChangeRequest request = findRequestById(requestId);

        if (request.getStatus() != RequestStatus.PENDING && request.getStatus() != RequestStatus.UNDER_REVIEW) {
            return false;
        }

        if (request.getStatus() == RequestStatus.UNDER_REVIEW) {
            long hoursInReview = calculateHoursInReview(request);
            if (hoursInReview > 24) {
                return false;
            }
        }

        return true;
    }

    /**
     * Obtiene la lista de documentos requeridos según el tipo de solicitud.
     *
     * @param requestType Tipo de solicitud (por ejemplo: GROUP_CHANGE, COURSE_CHANGE, SPECIAL_REQUEST)
     * @return Lista de nombres de documentos requeridos
     */
    public List<String> getRequiredDocuments(String requestType) {
        List<String> documents = new ArrayList<>();

        switch (requestType.toUpperCase()) {
            case "GROUP_CHANGE" -> {
                documents.add("Formulario de cambio de grupo");
                documents.add("Justificación escrita");
                documents.add("Horario actual");
                documents.add("Horario solicitado");
            }
            case "COURSE_CHANGE" -> {
                documents.add("Formulario de cambio de curso");
                documents.add("Justificación académica");
                documents.add("Plan de estudios actual");
                documents.add("Aprobación del coordinador");
                documents.add("Historial académico");
            }
            case "SPECIAL_REQUEST" -> {
                documents.add("Formulario de solicitud especial");
                documents.add("Informe médico o justificación");
                documents.add("Aprobación del decano");
                documents.add("Plan de contingencia");
            }
            default -> {
                documents.add("Formulario general de solicitud");
                documents.add("Documento de identificación");
            }
        }

        return documents;
    }

    /**
     * Verifica si un estudiante con el ID especificado existe.
     *
     * @param studentId ID del estudiante
     * @throws AppException si el estudiante no existe
     */
    private void validateStudent(String studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new AppException("El estudiante con ID " + studentId + " no existe.");
        }
    }

    /**
     * Busca una solicitud en el repositorio por su ID.
     *
     * @param requestId ID de la solicitud
     * @return Objeto {@link ScheduleChangeRequest} correspondiente
     * @throws AppException si la solicitud no existe
     */
    private ScheduleChangeRequest findRequestById(String requestId) {
        return scheduleChangeRequestRepository.findById(requestId)
                .orElseThrow(() -> new AppException("No se encontró la solicitud con ID: " + requestId));
    }

    /**
     * Encuentra el progreso académico de un estudiante dentro de una lista.
     *
     * @param progressList Lista de progresos académicos
     * @param studentId    ID del estudiante
     * @return Objeto {@link StudentAcademicProgress} correspondiente o null si no se encuentra
     */
    private StudentAcademicProgress findProgressByStudent(List<StudentAcademicProgress> progressList, String studentId) {
        return progressList.stream()
                .filter(p -> p.getStudent() != null && p.getStudent().getId().equals(studentId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Calcula el puntaje de prioridad para una solicitud específica.
     */
    private double calculatePriorityScore(Student student, StudentAcademicProgress progress, ScheduleChangeRequest request) {
        double score = 0.0;
        if (progress != null && progress.getCumulativeGPA() != null) score += progress.getCumulativeGPA() * 0.4;
        if (student.getSemester() != null) score += (student.getSemester() / 10.0) * 0.2;
        score += calculateUrgencyFactor(request.getReason()) * 0.2;
        long daysOld = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - request.getSubmissionDate().getTime());
        score += (Math.min(daysOld, 30) / 30.0) * 0.2;
        return score;
    }

    /**
     * Calcula un factor de urgencia basado en palabras clave dentro de la razón de la solicitud.
     */
    private double calculateUrgencyFactor(String reason) {
        reason = reason.toLowerCase();
        if (reason.contains("médico") || reason.contains("salud") || reason.contains("emergencia")) return 1.0;
        else if (reason.contains("trabajo") || reason.contains("familia")) return 0.7;
        else if (reason.contains("horario") || reason.contains("conflicto")) return 0.5;
        else return 0.3;
    }

    /**
     * Determina si un grupo está cerca de su límite de capacidad.
     */
    private boolean getGroupCapacityAlert(String groupId) {
        Optional<Group> group = groupRepository.findById(groupId);
        if (group.isEmpty()) return true;
        Classroom classroom = group.get().getClassroom();
        if (classroom == null) return false;
        int currentStudents = getCurrentStudentCountInGroup(groupId);
        double occupancyRate = (double) currentStudents / classroom.getCapacity();
        return occupancyRate >= 0.8;
    }

    /**
     * Obtiene el número actual de estudiantes aprobados en un grupo.
     */
    private int getCurrentStudentCountInGroup(String groupId) {
        List<ScheduleChangeRequest> groupRequests = scheduleChangeRequestRepository.findByRequestedGroupId(groupId);
        long approvedCount = groupRequests.stream()
                .filter(req -> req.getStatus() == RequestStatus.APPROVED)
                .count();
        return 20 + (int) approvedCount;
    }

    /**
     * Verifica si dos horarios son compatibles (sin traslape de día o periodo).
     */
    private boolean isScheduleCompatible(Schedule currentSchedule, Schedule newSchedule) {
        if (currentSchedule == null || newSchedule == null) return true;
        return !currentSchedule.getDayOfWeek().equals(newSchedule.getDayOfWeek()) ||
                !currentSchedule.getPeriod().equals(newSchedule.getPeriod());
    }

    /**
     * Calcula los días transcurridos desde la fecha de envío de la solicitud.
     */
    private long calculateDaysInQueue(Date submissionDate) {
        long diff = System.currentTimeMillis() - submissionDate.getTime();
        return TimeUnit.MILLISECONDS.toDays(diff);
    }

    /**
     * Calcula las horas que una solicitud ha estado en revisión.
     */
    private long calculateHoursInReview(ScheduleChangeRequest request) {
        Optional<ReviewStep> reviewStep = request.getReviewHistory().stream()
                .filter(step -> "EN_REVISION".equals(step.getAction()))
                .findFirst();

        return reviewStep.map(step ->
                TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis() - step.getTimestamp().getTime())
        ).orElse(0L);
    }

    /**
     * Calcula el promedio de tiempo de procesamiento de solicitudes resueltas en los últimos 30 días.
     */
    private double calculateAverageProcessingTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -30);
        Date lastMonth = calendar.getTime();

        List<ScheduleChangeRequest> recentResolved = scheduleChangeRequestRepository.findByResolutionDateAfter(lastMonth);
        if (recentResolved.isEmpty()) return 3.0;

        double totalDays = 0;
        int count = 0;
        for (ScheduleChangeRequest request : recentResolved) {
            if (request.getSubmissionDate() != null && request.getResolutionDate() != null) {
                long diff = request.getResolutionDate().getTime() - request.getSubmissionDate().getTime();
                totalDays += TimeUnit.MILLISECONDS.toDays(diff);
                count++;
            }
        }
        return count > 0 ? totalDays / count : 3.0;
    }

    /**
     * Clase interna auxiliar para manejar las prioridades de solicitudes.
     */
    private static class RequestPriority {
        String requestId;
        double score;
        Date submissionDate;

        RequestPriority(String requestId, double score, Date submissionDate) {
            this.requestId = requestId;
            this.score = score;
            this.submissionDate = submissionDate;
        }
    }
}

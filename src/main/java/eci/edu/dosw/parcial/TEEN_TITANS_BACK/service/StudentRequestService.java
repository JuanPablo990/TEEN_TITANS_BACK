package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

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
     * @throws RuntimeException si el estudiante no existe
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
        return Math.round(approvalRate * 100.0) / 100.0; // Redondear a 2 decimales
    }

    /**
     * Obtiene todas las solicitudes pendientes de un estudiante.
     *
     * @param studentId ID del estudiante
     * @return Lista de solicitudes en estado PENDING o UNDER_REVIEW
     * @throws RuntimeException si el estudiante no existe
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
     * La prioridad se basa en: GPA del estudiante, semestre, urgencia y antigüedad.
     *
     * @param requestId ID de la solicitud
     * @return Posición en la cola de prioridad (1 = mayor prioridad)
     * @throws RuntimeException si la solicitud no existe
     */
    public int getRequestPriorityPosition(String requestId) {
        ScheduleChangeRequest request = findRequestById(requestId);
        String studentId = request.getStudent().getId();

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado: " + studentId));

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
                return i + 1; // Posición base 1
            }
        }

        return -1;
    }

    /**
     * Obtiene grupos alternativos recomendados para una solicitud.
     * Los grupos se seleccionan basados en: mismo curso, capacidad disponible y horario compatible.
     *
     * @param requestId ID de la solicitud
     * @return Lista de grupos alternativos recomendados
     * @throws RuntimeException si la solicitud no existe
     */
    public List<Group> getRecommendedAlternativeGroups(String requestId) {
        ScheduleChangeRequest request = findRequestById(requestId);
        Group requestedGroup = request.getRequestedGroup();
        Course course = requestedGroup.getCourse();

        List<Group> allCourseGroups = groupRepository.findByCourse_CourseCode(course.getCourseCode());

        List<Group> alternatives = allCourseGroups.stream()
                .filter(group -> !group.getGroupId().equals(requestedGroup.getGroupId()))
                .filter(group -> !getGroupCapacityAlert(group.getGroupId()))
                .filter(group -> isScheduleCompatible(request.getCurrentGroup().getSchedule(), group.getSchedule())) // Horario compatible
                .sorted((g1, g2) -> {
                    int capacity1 = g1.getClassroom().getCapacity() - getCurrentStudentCountInGroup(g1.getGroupId());
                    int capacity2 = g2.getClassroom().getCapacity() - getCurrentStudentCountInGroup(g2.getGroupId());
                    return Integer.compare(capacity2, capacity1);
                })
                .limit(5)
                .collect(Collectors.toList());

        return alternatives;
    }

    /**
     * Calcula el tiempo de espera estimado para una solicitud.
     * Basado en: posición en cola, historial de procesamiento y complejidad.
     *
     * @param requestId ID de la solicitud
     * @return String con el tiempo estimado (ej: "2-3 días", "1 semana")
     * @throws RuntimeException si la solicitud no existe
     */
    public String getRequestEstimatedWaitTime(String requestId) {
        ScheduleChangeRequest request = findRequestById(requestId);

        if (request.getStatus() != RequestStatus.PENDING && request.getStatus() != RequestStatus.UNDER_REVIEW) {
            return "Solicitud ya procesada";
        }

        int position = getRequestPriorityPosition(requestId);
        long daysInQueue = calculateDaysInQueue(request.getSubmissionDate());

        double avgProcessingDays = calculateAverageProcessingTime();

        double estimatedDays = (position * avgProcessingDays) / 2.0; // Asumiendo procesamiento paralelo

        if (estimatedDays < 1) {
            return "Menos de 1 día";
        } else if (estimatedDays <= 3) {
            return "1-3 días";
        } else if (estimatedDays <= 7) {
            return "3-7 días";
        } else {
            return "Más de 1 semana";
        }
    }

    /**
     * Verifica si una solicitud puede ser cancelada.
     * Una solicitud puede cancelarse si está en estado PENDING o UNDER_REVIEW
     * y no ha estado en revisión por más de 24 horas.
     *
     * @param requestId ID de la solicitud
     * @return true si la solicitud puede ser cancelada
     * @throws RuntimeException si la solicitud no existe
     */
    public boolean canCancelRequest(String requestId) {
        ScheduleChangeRequest request = findRequestById(requestId);

        if (request.getStatus() != RequestStatus.PENDING && request.getStatus() != RequestStatus.UNDER_REVIEW) {
            return false;
        }

        if (request.getStatus() == RequestStatus.UNDER_REVIEW) {
            long hoursInReview = calculateHoursInReview(request);
            if (hoursInReview > 24) {
                return false; // Demasiado tiempo en revisión para cancelar
            }
        }

        return true;
    }

    /**
     * Obtiene la lista de documentos requeridos según el tipo de solicitud.
     *
     * @param requestType Tipo de solicitud ("GROUP_CHANGE", "COURSE_CHANGE", etc.)
     * @return Lista de documentos requeridos
     */
    public List<String> getRequiredDocuments(String requestType) {
        List<String> documents = new ArrayList<>();

        switch (requestType.toUpperCase()) {
            case "GROUP_CHANGE":
                documents.add("Formulario de cambio de grupo");
                documents.add("Justificación escrita");
                documents.add("Horario actual");
                documents.add("Horario solicitado");
                break;

            case "COURSE_CHANGE":
                documents.add("Formulario de cambio de curso");
                documents.add("Justificación académica");
                documents.add("Plan de estudios actual");
                documents.add("Aprobación del coordinador");
                documents.add("Historial académico");
                break;

            case "SPECIAL_REQUEST":
                documents.add("Formulario de solicitud especial");
                documents.add("Informe médico o justificación");
                documents.add("Aprobación del decano");
                documents.add("Plan de contingencia");
                break;

            default:
                documents.add("Formulario general de solicitud");
                documents.add("Documento de identificación");
                break;
        }

        return documents;
    }


    private void validateStudent(String studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new RuntimeException("Estudiante no encontrado: " + studentId);
        }
    }

    private ScheduleChangeRequest findRequestById(String requestId) {
        return scheduleChangeRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada: " + requestId));
    }

    private StudentAcademicProgress findProgressByStudent(List<StudentAcademicProgress> progressList, String studentId) {
        return progressList.stream()
                .filter(p -> p.getStudent() != null && p.getStudent().getId().equals(studentId))
                .findFirst()
                .orElse(null);
    }

    private double calculatePriorityScore(Student student, StudentAcademicProgress progress, ScheduleChangeRequest request) {
        double score = 0.0;

        if (progress != null && progress.getCumulativeGPA() != null) {
            score += progress.getCumulativeGPA() * 0.4;
        }

        if (student.getSemester() != null) {
            score += (student.getSemester() / 10.0) * 0.2;
        }

        score += calculateUrgencyFactor(request.getReason()) * 0.2;

        long daysOld = TimeUnit.MILLISECONDS.toDays(
                System.currentTimeMillis() - request.getSubmissionDate().getTime()
        );
        score += (Math.min(daysOld, 30) / 30.0) * 0.2; // Normalizado a 30 días

        return score;
    }

    private double calculateUrgencyFactor(String reason) {
        reason = reason.toLowerCase();

        if (reason.contains("médico") || reason.contains("salud") || reason.contains("emergencia")) {
            return 1.0;
        } else if (reason.contains("trabajo") || reason.contains("familia")) {
            return 0.7;
        } else if (reason.contains("horario") || reason.contains("conflicto")) {
            return 0.5;
        } else {
            return 0.3;
        }
    }

    private boolean getGroupCapacityAlert(String groupId) {
        Optional<Group> group = groupRepository.findById(groupId);
        if (group.isEmpty()) return true;

        Classroom classroom = group.get().getClassroom();
        if (classroom == null) return false;

        int currentStudents = getCurrentStudentCountInGroup(groupId);
        double occupancyRate = (double) currentStudents / classroom.getCapacity();
        return occupancyRate >= 0.8;
    }

    private int getCurrentStudentCountInGroup(String groupId) {
        List<ScheduleChangeRequest> groupRequests = scheduleChangeRequestRepository.findByRequestedGroupId(groupId);
        long approvedCount = groupRequests.stream()
                .filter(req -> req.getStatus() == RequestStatus.APPROVED)
                .count();
        return 20 + (int) approvedCount;
    }

    private boolean isScheduleCompatible(Schedule currentSchedule, Schedule newSchedule) {
        if (currentSchedule == null || newSchedule == null) return true;

        return !currentSchedule.getDayOfWeek().equals(newSchedule.getDayOfWeek()) ||
                !currentSchedule.getPeriod().equals(newSchedule.getPeriod());
    }

    private long calculateDaysInQueue(Date submissionDate) {
        long diff = System.currentTimeMillis() - submissionDate.getTime();
        return TimeUnit.MILLISECONDS.toDays(diff);
    }

    private long calculateHoursInReview(ScheduleChangeRequest request) {
        Optional<ReviewStep> reviewStep = request.getReviewHistory().stream()
                .filter(step -> "EN_REVISION".equals(step.getAction()))
                .findFirst();

        if (reviewStep.isPresent()) {
            long diff = System.currentTimeMillis() - reviewStep.get().getTimestamp().getTime();
            return TimeUnit.MILLISECONDS.toHours(diff);
        }

        return 0;
    }

    private double calculateAverageProcessingTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -30); // Últimos 30 días
        Date lastMonth = calendar.getTime();

        List<ScheduleChangeRequest> recentResolved = scheduleChangeRequestRepository
                .findByResolutionDateAfter(lastMonth);

        if (recentResolved.isEmpty()) {
            return 3.0;
        }

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
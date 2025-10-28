package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.RequestStatus;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Servicio para gestionar solicitudes de cambio de horario académico.
 * Proporciona funcionalidades para crear, aprobar, rechazar y consultar solicitudes de cambio.
 *
 * @author Equipo Teen Titans
 * @version 2.0
 * @since 2025
 */
@Service
@Transactional
public class RequestService {

    private static final int MAX_PENDING_REQUESTS = 3;
    private static final double CAPACITY_ALERT_THRESHOLD = 0.8;

    private final ScheduleChangeRequestRepository scheduleChangeRequestRepository;
    private final ReviewStepRepository reviewStepRepository;
    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final RequestValidator requestValidator;
    private final RequestIdGenerator requestIdGenerator;

    /**
     * Constructor para la inyección de dependencias.
     *
     * @param scheduleChangeRequestRepository Repositorio de solicitudes de cambio
     * @param reviewStepRepository Repositorio de pasos de revisión
     * @param groupRepository Repositorio de grupos
     * @param studentRepository Repositorio de estudiantes
     * @param courseRepository Repositorio de cursos
     */
    @Autowired
    public RequestService(ScheduleChangeRequestRepository scheduleChangeRequestRepository,
                          ReviewStepRepository reviewStepRepository,
                          GroupRepository groupRepository,
                          StudentRepository studentRepository,
                          CourseRepository courseRepository) {
        this.scheduleChangeRequestRepository = scheduleChangeRequestRepository;
        this.reviewStepRepository = reviewStepRepository;
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.requestValidator = new RequestValidator();
        this.requestIdGenerator = new RequestIdGenerator();
    }

    /**
     * Crea una solicitud de cambio de grupo para un estudiante.
     *
     * @param studentId ID del estudiante
     * @param currentGroupId ID del grupo actual
     * @param requestedGroupId ID del grupo solicitado
     * @param reason Razón del cambio
     * @return Solicitud de cambio creada
     * @throws AppException si las validaciones fallan
     */
    public ScheduleChangeRequest createGroupChangeRequest(String studentId, String currentGroupId,
                                                          String requestedGroupId, String reason) {
        Student student = validateStudent(studentId);
        Group currentGroup = validateGroup(currentGroupId);
        Group requestedGroup = validateGroup(requestedGroupId);

        requestValidator.validateGroupChange(studentId, currentGroupId, requestedGroupId);
        requestValidator.validateGroupCapacity(requestedGroupId);
        requestValidator.validatePendingRequestsLimit(studentId, scheduleChangeRequestRepository);

        ScheduleChangeRequest request = buildRequest(student, currentGroup, requestedGroup, reason);
        addInitialReviewStep(request, studentId, "SOLICITUD_CREADA", "Solicitud de cambio de grupo creada por el estudiante");

        return scheduleChangeRequestRepository.save(request);
    }

    /**
     * Crea una solicitud de cambio de curso para un estudiante.
     *
     * @param studentId ID del estudiante
     * @param currentCourseCode Código del curso actual
     * @param requestedCourseCode Código del curso solicitado
     * @param reason Razón del cambio
     * @return Solicitud de cambio creada
     * @throws AppException si las validaciones fallan
     */
    public ScheduleChangeRequest createCourseChangeRequest(String studentId, String currentCourseCode,
                                                           String requestedCourseCode, String reason) {
        Student student = validateStudent(studentId);
        Course currentCourse = validateCourse(currentCourseCode);
        Course requestedCourse = validateCourse(requestedCourseCode);

        requestValidator.validateCourseChange(studentId, currentCourseCode, requestedCourseCode);
        requestValidator.validatePendingRequestsLimit(studentId, scheduleChangeRequestRepository);

        Group currentGroup = findAvailableGroupForCourse(currentCourseCode);
        Group requestedGroup = findAvailableGroupForCourse(requestedCourseCode);

        ScheduleChangeRequest request = buildRequest(student, currentGroup, requestedGroup, reason);
        addInitialReviewStep(request, studentId, "SOLICITUD_CREADA", "Solicitud de cambio de curso creada por el estudiante");

        return scheduleChangeRequestRepository.save(request);
    }

    /**
     * Aprueba una solicitud de cambio.
     *
     * @param requestId ID de la solicitud
     * @param reviewerId ID del revisor
     * @param reviewerRole Rol del revisor
     * @param comments Comentarios de la aprobación
     * @return Solicitud aprobada
     * @throws AppException si no se puede aprobar por capacidad
     */
    public ScheduleChangeRequest approveRequest(String requestId, String reviewerId,
                                                UserRole reviewerRole, String comments) {
        ScheduleChangeRequest request = findRequestById(requestId);

        if (getGroupCapacityAlert(request.getRequestedGroup().getGroupId())) {
            throw new AppException("No se puede aprobar la solicitud: el grupo solicitado ya no tiene capacidad disponible");
        }

        request.setStatus(RequestStatus.APPROVED);
        request.setResolutionDate(new Date());

        addReviewStep(request, reviewerId, reviewerRole, "SOLICITUD_APROBADA", comments);

        return scheduleChangeRequestRepository.save(request);
    }

    /**
     * Rechaza una solicitud de cambio.
     *
     * @param requestId ID de la solicitud
     * @param reviewerId ID del revisor
     * @param reviewerRole Rol del revisor
     * @param comments Comentarios del rechazo
     * @return Solicitud rechazada
     */
    public ScheduleChangeRequest rejectRequest(String requestId, String reviewerId,
                                               UserRole reviewerRole, String comments) {
        ScheduleChangeRequest request = findRequestById(requestId);
        request.setStatus(RequestStatus.REJECTED);
        request.setResolutionDate(new Date());

        addReviewStep(request, reviewerId, reviewerRole, "SOLICITUD_RECHAZADA", comments);

        return scheduleChangeRequestRepository.save(request);
    }

    /**
     * Cancela una solicitud por parte del estudiante.
     *
     * @param requestId ID de la solicitud
     * @param studentId ID del estudiante
     * @return Solicitud cancelada
     * @throws AppException si el estudiante no es el propietario o el estado no es cancelable
     */
    public ScheduleChangeRequest cancelRequest(String requestId, String studentId) {
        ScheduleChangeRequest request = findRequestById(requestId);

        if (!request.getStudent().getId().equals(studentId)) {
            throw new AppException("Solo el estudiante propietario puede cancelar la solicitud");
        }

        if (request.getStatus() != RequestStatus.PENDING && request.getStatus() != RequestStatus.UNDER_REVIEW) {
            throw new AppException("Solo se pueden cancelar solicitudes en estado PENDING o UNDER_REVIEW");
        }

        request.setStatus(RequestStatus.CANCELLED);
        addReviewStep(request, studentId, UserRole.STUDENT, "SOLICITUD_CANCELADA", "Solicitud cancelada por el estudiante");

        return scheduleChangeRequestRepository.save(request);
    }

    /**
     * Obtiene el estado de una solicitud.
     *
     * @param requestId ID de la solicitud
     * @return Estado de la solicitud
     */
    public RequestStatus getRequestStatus(String requestId) {
        return findRequestById(requestId).getStatus();
    }

    /**
     * Obtiene el historial de solicitudes de un estudiante.
     *
     * @param studentId ID del estudiante
     * @return Lista de solicitudes ordenadas por fecha descendente
     */
    public List<ScheduleChangeRequest> getRequestHistory(String studentId) {
        validateStudent(studentId);
        return scheduleChangeRequestRepository.findByStudentIdOrderBySubmissionDateDesc(studentId);
    }

    /**
     * Obtiene el historial de decisiones de una solicitud.
     *
     * @param requestId ID de la solicitud
     * @return Lista de pasos de revisión
     */
    public List<ReviewStep> getDecisionHistory(String requestId) {
        return findRequestById(requestId).getReviewHistory();
    }

    /**
     * Obtiene estadísticas de solicitudes de un estudiante.
     *
     * @param studentId ID del estudiante
     * @return Mapa con estadísticas de solicitudes
     */
    public Map<String, Object> getRequestStatistics(String studentId) {
        validateStudent(studentId);
        List<ScheduleChangeRequest> allRequests = scheduleChangeRequestRepository.findByStudentId(studentId);

        Map<String, Object> stats = new HashMap<>();
        stats.put("total", allRequests.size());
        stats.put("pending", countByStatus(allRequests, RequestStatus.PENDING));
        stats.put("approved", countByStatus(allRequests, RequestStatus.APPROVED));
        stats.put("rejected", countByStatus(allRequests, RequestStatus.REJECTED));
        stats.put("cancelled", countByStatus(allRequests, RequestStatus.CANCELLED));
        stats.put("under_review", countByStatus(allRequests, RequestStatus.UNDER_REVIEW));

        return stats;
    }

    /**
     * Verifica si un grupo tiene alerta de capacidad.
     *
     * @param groupId ID del grupo
     * @return true si el grupo está cerca de su capacidad máxima
     */
    public boolean getGroupCapacityAlert(String groupId) {
        Group group = validateGroup(groupId);
        Classroom classroom = group.getClassroom();

        if (classroom == null) {
            return false;
        }

        int currentStudents = getCurrentStudentCountInGroup(groupId);
        int capacity = classroom.getCapacity();
        double occupancyRate = (double) currentStudents / capacity;

        return occupancyRate >= CAPACITY_ALERT_THRESHOLD;
    }

    /**
     * Actualiza una solicitud existente.
     *
     * @param requestId ID de la solicitud
     * @param updates Mapa con los campos a actualizar
     * @return Solicitud actualizada
     * @throws AppException si la solicitud está en estado final
     */
    public ScheduleChangeRequest updateRequest(String requestId, Map<String, Object> updates) {
        ScheduleChangeRequest request = findRequestById(requestId);

        if (isFinalStatus(request.getStatus())) {
            throw new AppException("No se puede modificar una solicitud en estado final: " + request.getStatus());
        }

        applyUpdates(request, updates);
        addReviewStep(request, "SYSTEM", UserRole.ADMINISTRATOR, "SOLICITUD_ACTUALIZADA",
                "Solicitud actualizada: " + String.join(", ", updates.keySet()));

        return scheduleChangeRequestRepository.save(request);
    }

    /**
     * Elimina una solicitud.
     *
     * @param requestId ID de la solicitud
     * @return true si la eliminación fue exitosa
     * @throws AppException si la solicitud no está en estado PENDING
     */
    public boolean deleteRequest(String requestId) {
        ScheduleChangeRequest request = findRequestById(requestId);

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new AppException("Solo se pueden eliminar solicitudes en estado PENDING");
        }

        scheduleChangeRequestRepository.delete(request);
        return true;
    }

    private ScheduleChangeRequest findRequestById(String requestId) {
        return scheduleChangeRequestRepository.findById(requestId)
                .orElseThrow(() -> new AppException("Solicitud no encontrada: " + requestId));
    }

    private Student validateStudent(String studentId) {
        return studentRepository.findById(studentId)
                .filter(Student::isActive)
                .orElseThrow(() -> new AppException("Estudiante no válido o inactivo: " + studentId));
    }

    private Group validateGroup(String groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new AppException("Grupo no encontrado: " + groupId));
    }

    private Course validateCourse(String courseCode) {
        return courseRepository.findById(courseCode)
                .filter(Course::getIsActive)
                .orElseThrow(() -> new AppException("Curso no encontrado o inactivo: " + courseCode));
    }

    private Group findAvailableGroupForCourse(String courseCode) {
        return groupRepository.findByCourse_CourseCode(courseCode).stream()
                .filter(group -> !getGroupCapacityAlert(group.getGroupId()))
                .findFirst()
                .orElseThrow(() -> new AppException("No hay grupos disponibles con capacidad para el curso: " + courseCode));
    }

    private ScheduleChangeRequest buildRequest(Student student, Group currentGroup,
                                               Group requestedGroup, String reason) {
        return ScheduleChangeRequest.builder()
                .requestId(requestIdGenerator.generate())
                .student(student)
                .currentGroup(currentGroup)
                .requestedGroup(requestedGroup)
                .reason(reason)
                .status(RequestStatus.PENDING)
                .submissionDate(new Date())
                .build();
    }

    private void addInitialReviewStep(ScheduleChangeRequest request, String userId,
                                      String action, String comments) {
        ReviewStep initialStep = ReviewStep.builder()
                .userId(userId)
                .userRole(UserRole.STUDENT)
                .action(action)
                .comments(comments)
                .timestamp(new Date())
                .build();
        request.addReviewStep(initialStep);
    }

    private void addReviewStep(ScheduleChangeRequest request, String userId,
                               UserRole userRole, String action, String comments) {
        ReviewStep reviewStep = ReviewStep.builder()
                .userId(userId)
                .userRole(userRole)
                .action(action)
                .comments(comments)
                .timestamp(new Date())
                .build();
        request.addReviewStep(reviewStep);
    }

    private void applyUpdates(ScheduleChangeRequest request, Map<String, Object> updates) {
        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            switch (entry.getKey()) {
                case "reason" -> {
                    if (entry.getValue() instanceof String reason) {
                        request.setReason(reason);
                    }
                }
                case "requestedGroup" -> {
                    if (entry.getValue() instanceof Group newGroup) {
                        requestValidator.validateGroupCapacity(newGroup.getGroupId());
                        request.setRequestedGroup(newGroup);
                    }
                }
                case "status" -> {
                    if (entry.getValue() instanceof RequestStatus newStatus) {
                        if (newStatus == RequestStatus.APPROVED || newStatus == RequestStatus.REJECTED) {
                            throw new AppException("No se puede cambiar manualmente a estado APPROVED o REJECTED");
                        }
                        request.setStatus(newStatus);
                    }
                }
                default -> throw new AppException("Campo no permitido para actualización: " + entry.getKey());
            }
        }
    }

    private boolean isFinalStatus(RequestStatus status) {
        return status == RequestStatus.APPROVED ||
                status == RequestStatus.REJECTED ||
                status == RequestStatus.CANCELLED;
    }

    private long countByStatus(List<ScheduleChangeRequest> requests, RequestStatus status) {
        return requests.stream()
                .filter(request -> request.getStatus() == status)
                .count();
    }

    private int getCurrentStudentCountInGroup(String groupId) {
        List<ScheduleChangeRequest> approvedRequests = scheduleChangeRequestRepository.findByRequestedGroupId(groupId);
        long approvedCount = approvedRequests.stream()
                .filter(request -> request.getStatus() == RequestStatus.APPROVED)
                .count();
        return 20 + (int) approvedCount;
    }

    /**
     * Clase para validaciones de solicitudes.
     */
    private class RequestValidator {

        /**
         * Valida que el cambio de grupo sea válido.
         */
        void validateGroupChange(String studentId, String currentGroupId, String requestedGroupId) {
            if (currentGroupId.equals(requestedGroupId)) {
                throw new AppException("El grupo actual y el solicitado no pueden ser el mismo");
            }
        }

        /**
         * Valida que el cambio de curso sea válido.
         */
        void validateCourseChange(String studentId, String currentCourseCode, String requestedCourseCode) {
            if (currentCourseCode.equals(requestedCourseCode)) {
                throw new AppException("El curso actual y el solicitado no pueden ser el mismo");
            }
        }

        /**
         * Valida la capacidad del grupo solicitado.
         */
        void validateGroupCapacity(String groupId) {
            if (RequestService.this.getGroupCapacityAlert(groupId)) {
                throw new AppException("El grupo solicitado está cerca de su capacidad máxima");
            }
        }

        /**
         * Valida el límite de solicitudes pendientes del estudiante.
         */
        void validatePendingRequestsLimit(String studentId,
                                          ScheduleChangeRequestRepository repository) {
            long pendingCount = repository.countByStudentIdAndStatus(studentId, RequestStatus.PENDING);
            if (pendingCount >= MAX_PENDING_REQUESTS) {
                throw new AppException("El estudiante tiene demasiadas solicitudes pendientes. Límite: " + MAX_PENDING_REQUESTS);
            }
        }
    }

    /**
     * Clase para generación de IDs de solicitud.
     */
    private static class RequestIdGenerator {

        /**
         * Genera un ID único para la solicitud.
         *
         * @return ID de solicitud generado
         */
        String generate() {
            return "REQ-" + System.currentTimeMillis() + "-" +
                    UUID.randomUUID().toString().substring(0, 8);
        }
    }
}
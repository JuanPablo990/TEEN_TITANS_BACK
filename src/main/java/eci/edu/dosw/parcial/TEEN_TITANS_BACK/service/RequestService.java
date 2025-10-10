package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Servicio para gestionar solicitudes de cambio de horario académico.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Service
public class RequestService {

    private final ScheduleChangeRequestRepository scheduleChangeRequestRepository;
    private final ReviewStepRepository reviewStepRepository;
    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    /**
     * Constructor para inyección de dependencias.
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
    }

    /**
     * Crea una solicitud de cambio de grupo para un estudiante.
     *
     * @param student Estudiante que realiza la solicitud
     * @param currentGroup Grupo actual del estudiante
     * @param requestedGroup Grupo solicitado por el estudiante
     * @param reason Justificación para el cambio
     * @return ScheduleChangeRequest creada
     * @throws RuntimeException si la validación falla
     */
    public ScheduleChangeRequest createGroupChangeRequest(Student student, Group currentGroup,
                                                          Group requestedGroup, String reason) {
        validateStudent(student.getId());
        validateGroups(currentGroup.getGroupId(), requestedGroup.getGroupId());
        validateGroupCapacity(requestedGroup.getGroupId());
        validatePendingRequestsLimit(student.getId());

        String requestId = generateRequestId();
        ScheduleChangeRequest request = new ScheduleChangeRequest(
                requestId, student, currentGroup, requestedGroup, reason
        );

        ReviewStep initialStep = new ReviewStep(
                student.getId(),
                UserRole.STUDENT,
                "SOLICITUD_CREADA",
                "Solicitud de cambio de grupo creada por el estudiante"
        );
        request.addReviewStep(initialStep);

        return scheduleChangeRequestRepository.save(request);
    }

    /**
     * Crea una solicitud de cambio de curso para un estudiante.
     *
     * @param student Estudiante que realiza la solicitud
     * @param currentCourse Curso actual del estudiante
     * @param requestedCourse Curso solicitado por el estudiante
     * @param reason Justificación para el cambio
     * @return ScheduleChangeRequest creada
     * @throws RuntimeException si la validación falla
     */
    public ScheduleChangeRequest createCourseChangeRequest(Student student, Course currentCourse,
                                                           Course requestedCourse, String reason) {
        validateStudent(student.getId());
        validateCourses(currentCourse.getCourseCode(), requestedCourse.getCourseCode());
        validatePendingRequestsLimit(student.getId());

        List<Group> currentGroups = groupRepository.findByCourse_CourseCode(currentCourse.getCourseCode());
        List<Group> requestedGroups = groupRepository.findByCourse_CourseCode(requestedCourse.getCourseCode());

        if (currentGroups.isEmpty() || requestedGroups.isEmpty()) {
            throw new RuntimeException("No se encontraron grupos disponibles para los cursos especificados");
        }

        Group currentGroup = currentGroups.get(0);

        Group requestedGroup = requestedGroups.stream()
                .filter(group -> !getGroupCapacityAlert(group.getGroupId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No hay grupos disponibles con capacidad para el curso solicitado"));

        String requestId = generateRequestId();
        ScheduleChangeRequest request = new ScheduleChangeRequest(
                requestId, student, currentGroup, requestedGroup, reason
        );

        ReviewStep initialStep = new ReviewStep(
                student.getId(),
                UserRole.STUDENT,
                "SOLICITUD_CREADA",
                "Solicitud de cambio de curso creada por el estudiante"
        );
        request.addReviewStep(initialStep);

        return scheduleChangeRequestRepository.save(request);
    }

    /**
     * Obtiene el estado actual de una solicitud.
     *
     * @param requestId ID de la solicitud
     * @return RequestStatus estado de la solicitud
     * @throws RuntimeException si la solicitud no existe
     */
    public RequestStatus getRequestStatus(String requestId) {
        ScheduleChangeRequest request = findRequestById(requestId);
        return request.getStatus();
    }

    /**
     * Obtiene el historial de solicitudes de un estudiante.
     *
     * @param studentId ID del estudiante
     * @return Lista de ScheduleChangeRequest del estudiante
     * @throws RuntimeException si el estudiante no existe
     */
    public List<ScheduleChangeRequest> getRequestHistory(String studentId) {
        validateStudent(studentId);

        List<ScheduleChangeRequest> requests = scheduleChangeRequestRepository.findByStudentId(studentId);

        requests.sort((r1, r2) -> r2.getSubmissionDate().compareTo(r1.getSubmissionDate()));

        return requests;
    }

    /**
     * Obtiene el historial de decisiones/revisiones de una solicitud.
     *
     * @param requestId ID de la solicitud
     * @return Lista de ReviewStep con el historial de decisiones
     * @throws RuntimeException si la solicitud no existe
     */
    public List<ReviewStep> getDecisionHistory(String requestId) {
        ScheduleChangeRequest request = findRequestById(requestId);
        return request.getReviewHistory();
    }

    /**
     * Actualiza una solicitud existente.
     *
     * @param requestId ID de la solicitud a actualizar
     * @param updates Mapa con los campos a actualizar
     * @return ScheduleChangeRequest actualizada
     * @throws RuntimeException si la solicitud no existe o las actualizaciones son inválidas
     */
    public ScheduleChangeRequest updateRequest(String requestId, Map<String, Object> updates) {
        ScheduleChangeRequest request = findRequestById(requestId);

        if (request.getStatus() == RequestStatus.APPROVED ||
                request.getStatus() == RequestStatus.REJECTED ||
                request.getStatus() == RequestStatus.CANCELLED) {
            throw new RuntimeException("No se puede modificar una solicitud en estado final: " + request.getStatus());
        }

        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            String field = entry.getKey();
            Object value = entry.getValue();

            switch (field) {
                case "reason":
                    if (value instanceof String) {
                        request.setReason((String) value);
                    }
                    break;
                case "requestedGroup":
                    if (value instanceof Group) {
                        Group newGroup = (Group) value;
                        validateGroupCapacity(newGroup.getGroupId());
                        request.setRequestedGroup(newGroup);
                    }
                    break;
                case "status":
                    if (value instanceof RequestStatus) {
                        RequestStatus newStatus = (RequestStatus) value;
                        if (newStatus == RequestStatus.APPROVED || newStatus == RequestStatus.REJECTED) {
                            throw new RuntimeException("No se puede cambiar manualmente a estado APPROVED o REJECTED");
                        }
                        request.setStatus(newStatus);
                    }
                    break;
                default:
                    throw new RuntimeException("Campo no permitido para actualización: " + field);
            }
        }

        ReviewStep updateStep = new ReviewStep(
                "SYSTEM",
                UserRole.ADMINISTRATOR,
                "SOLICITUD_ACTUALIZADA",
                "Solicitud actualizada: " + String.join(", ", updates.keySet())
        );
        request.addReviewStep(updateStep);

        return scheduleChangeRequestRepository.save(request);
    }

    /**
     * Elimina una solicitud (solo si está en estado PENDING).
     *
     * @param requestId ID de la solicitud a eliminar
     * @return true si fue eliminada, false en caso contrario
     */
    public boolean deleteRequest(String requestId) {
        Optional<ScheduleChangeRequest> requestOptional = scheduleChangeRequestRepository.findById(requestId);

        if (requestOptional.isEmpty()) {
            return false;
        }

        ScheduleChangeRequest request = requestOptional.get();

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("Solo se pueden eliminar solicitudes en estado PENDING");
        }

        scheduleChangeRequestRepository.delete(request);
        return true;
    }

    /**
     * Verifica si un grupo tiene capacidad disponible.
     *
     * @param groupId ID del grupo a verificar
     * @return true si el grupo está cerca de su capacidad máxima, false si tiene disponibilidad
     */
    public boolean getGroupCapacityAlert(String groupId) {
        Optional<Group> groupOptional = groupRepository.findById(groupId);

        if (groupOptional.isEmpty()) {
            throw new RuntimeException("Grupo no encontrado: " + groupId);
        }

        Group group = groupOptional.get();

        Classroom classroom = group.getClassroom();
        if (classroom == null) {
            return false;
        }

        int currentStudents = getCurrentStudentCountInGroup(groupId);
        int capacity = classroom.getCapacity();

        double occupancyRate = (double) currentStudents / capacity;
        return occupancyRate >= 0.8;
    }



    private ScheduleChangeRequest findRequestById(String requestId) {
        return scheduleChangeRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada: " + requestId));
    }

    private void validateStudent(String studentId) {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isEmpty() || !student.get().getActive()) {
            throw new RuntimeException("Estudiante no válido o inactivo: " + studentId);
        }
    }

    private void validateGroups(String currentGroupId, String requestedGroupId) {
        if (currentGroupId.equals(requestedGroupId)) {
            throw new RuntimeException("El grupo actual y el solicitado no pueden ser el mismo");
        }

        Optional<Group> currentGroup = groupRepository.findById(currentGroupId);
        Optional<Group> requestedGroup = groupRepository.findById(requestedGroupId);

        if (currentGroup.isEmpty() || requestedGroup.isEmpty()) {
            throw new RuntimeException("Uno o ambos grupos no existen");
        }
    }

    private void validateCourses(String currentCourseCode, String requestedCourseCode) {
        if (currentCourseCode.equals(requestedCourseCode)) {
            throw new RuntimeException("El curso actual y el solicitado no pueden ser el mismo");
        }

        Optional<Course> currentCourse = courseRepository.findById(currentCourseCode);
        Optional<Course> requestedCourse = courseRepository.findById(requestedCourseCode);

        if (currentCourse.isEmpty() || requestedCourse.isEmpty()) {
            throw new RuntimeException("Uno o ambos cursos no existen");
        }

        if (!currentCourse.get().isActive() || !requestedCourse.get().isActive()) {
            throw new RuntimeException("Uno o ambos cursos están inactivos");
        }
    }

    private void validateGroupCapacity(String groupId) {
        if (getGroupCapacityAlert(groupId)) {
            throw new RuntimeException("El grupo solicitado está cerca de su capacidad máxima");
        }
    }

    private void validatePendingRequestsLimit(String studentId) {
        long pendingCount = scheduleChangeRequestRepository.countByStudentIdAndStatus(studentId, RequestStatus.PENDING);
        if (pendingCount >= 3) { // Límite de 3 solicitudes pendientes
            throw new RuntimeException("El estudiante tiene demasiadas solicitudes pendientes. Límite: 3");
        }
    }

    private int getCurrentStudentCountInGroup(String groupId) {
        List<ScheduleChangeRequest> approvedRequests = scheduleChangeRequestRepository.findByRequestedGroupId(groupId);
        long approvedCount = approvedRequests.stream()
                .filter(request -> request.getStatus() == RequestStatus.APPROVED)
                .count();

        return 20 + (int) approvedCount;
    }

    private String generateRequestId() {
        return "REQ-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8);
    }


    /**
     * Aprueba una solicitud de cambio.
     *
     * @param requestId ID de la solicitud
     * @param reviewerId ID del usuario que aprueba
     * @param reviewerRole Rol del usuario que aprueba
     * @param comments Comentarios de la aprobación
     * @return ScheduleChangeRequest aprobada
     */
    public ScheduleChangeRequest approveRequest(String requestId, String reviewerId,
                                                UserRole reviewerRole, String comments) {
        ScheduleChangeRequest request = findRequestById(requestId);

        if (getGroupCapacityAlert(request.getRequestedGroup().getGroupId())) {
            throw new RuntimeException("No se puede aprobar la solicitud: el grupo solicitado ya no tiene capacidad disponible");
        }

        request.setStatus(RequestStatus.APPROVED);

        ReviewStep approvalStep = new ReviewStep(
                reviewerId,
                reviewerRole,
                "SOLICITUD_APROBADA",
                comments
        );
        request.addReviewStep(approvalStep);

        return scheduleChangeRequestRepository.save(request);
    }

    /**
     * Rechaza una solicitud de cambio.
     *
     * @param requestId ID de la solicitud
     * @param reviewerId ID del usuario que rechaza
     * @param reviewerRole Rol del usuario que rechaza
     * @param comments Comentarios del rechazo
     * @return ScheduleChangeRequest rechazada
     */
    public ScheduleChangeRequest rejectRequest(String requestId, String reviewerId,
                                               UserRole reviewerRole, String comments) {
        ScheduleChangeRequest request = findRequestById(requestId);
        request.setStatus(RequestStatus.REJECTED);

        ReviewStep rejectionStep = new ReviewStep(
                reviewerId,
                reviewerRole,
                "SOLICITUD_RECHAZADA",
                comments
        );
        request.addReviewStep(rejectionStep);

        return scheduleChangeRequestRepository.save(request);
    }

    /**
     * Cancela una solicitud (solo el estudiante puede cancelar sus propias solicitudes pendientes).
     *
     * @param requestId ID de la solicitud
     * @param studentId ID del estudiante que cancela
     * @return ScheduleChangeRequest cancelada
     */
    public ScheduleChangeRequest cancelRequest(String requestId, String studentId) {
        ScheduleChangeRequest request = findRequestById(requestId);

        if (!request.getStudent().getId().equals(studentId)) {
            throw new RuntimeException("Solo el estudiante propietario puede cancelar la solicitud");
        }

        if (request.getStatus() != RequestStatus.PENDING && request.getStatus() != RequestStatus.UNDER_REVIEW) {
            throw new RuntimeException("Solo se pueden cancelar solicitudes en estado PENDING o UNDER_REVIEW");
        }

        request.setStatus(RequestStatus.CANCELLED);

        ReviewStep cancelStep = new ReviewStep(
                studentId,
                UserRole.STUDENT,
                "SOLICITUD_CANCELADA",
                "Solicitud cancelada por el estudiante"
        );
        request.addReviewStep(cancelStep);

        return scheduleChangeRequestRepository.save(request);
    }

    /**
     * Obtiene estadísticas de solicitudes por estudiante.
     *
     * @param studentId ID del estudiante
     * @return Mapa con estadísticas de solicitudes
     */
    public Map<String, Object> getRequestStatistics(String studentId) {
        validateStudent(studentId);

        List<ScheduleChangeRequest> allRequests = scheduleChangeRequestRepository.findByStudentId(studentId);

        Map<String, Object> stats = new HashMap<>();
        stats.put("total", allRequests.size());
        stats.put("pending", allRequests.stream().filter(r -> r.getStatus() == RequestStatus.PENDING).count());
        stats.put("approved", allRequests.stream().filter(r -> r.getStatus() == RequestStatus.APPROVED).count());
        stats.put("rejected", allRequests.stream().filter(r -> r.getStatus() == RequestStatus.REJECTED).count());
        stats.put("cancelled", allRequests.stream().filter(r -> r.getStatus() == RequestStatus.CANCELLED).count());
        stats.put("under_review", allRequests.stream().filter(r -> r.getStatus() == RequestStatus.UNDER_REVIEW).count());

        return stats;
    }
}
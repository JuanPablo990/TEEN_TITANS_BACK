package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
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

    public ScheduleChangeRequest createCourseChangeRequest(Student student, Course currentCourse,
                                                           Course requestedCourse, String reason) {
        validateStudent(student.getId());
        validateCourses(currentCourse.getCourseCode(), requestedCourse.getCourseCode());
        validatePendingRequestsLimit(student.getId());

        List<Group> currentGroups = groupRepository.findByCourse_CourseCode(currentCourse.getCourseCode());
        List<Group> requestedGroups = groupRepository.findByCourse_CourseCode(requestedCourse.getCourseCode());

        if (currentGroups.isEmpty() || requestedGroups.isEmpty()) {
            throw new AppException("No se encontraron grupos disponibles para los cursos especificados");
        }

        Group currentGroup = currentGroups.get(0);

        Group requestedGroup = requestedGroups.stream()
                .filter(group -> !getGroupCapacityAlert(group.getGroupId()))
                .findFirst()
                .orElseThrow(() -> new AppException("No hay grupos disponibles con capacidad para el curso solicitado"));

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

    public RequestStatus getRequestStatus(String requestId) {
        ScheduleChangeRequest request = findRequestById(requestId);
        return request.getStatus();
    }

    public List<ScheduleChangeRequest> getRequestHistory(String studentId) {
        validateStudent(studentId);
        List<ScheduleChangeRequest> requests = scheduleChangeRequestRepository.findByStudentId(studentId);
        requests.sort((r1, r2) -> r2.getSubmissionDate().compareTo(r1.getSubmissionDate()));
        return requests;
    }

    public List<ReviewStep> getDecisionHistory(String requestId) {
        ScheduleChangeRequest request = findRequestById(requestId);
        return request.getReviewHistory();
    }

    public ScheduleChangeRequest updateRequest(String requestId, Map<String, Object> updates) {
        ScheduleChangeRequest request = findRequestById(requestId);

        if (request.getStatus() == RequestStatus.APPROVED ||
                request.getStatus() == RequestStatus.REJECTED ||
                request.getStatus() == RequestStatus.CANCELLED) {
            throw new AppException("No se puede modificar una solicitud en estado final: " + request.getStatus());
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
                            throw new AppException("No se puede cambiar manualmente a estado APPROVED o REJECTED");
                        }
                        request.setStatus(newStatus);
                    }
                    break;
                default:
                    throw new AppException("Campo no permitido para actualización: " + field);
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

    public boolean deleteRequest(String requestId) {
        Optional<ScheduleChangeRequest> requestOptional = scheduleChangeRequestRepository.findById(requestId);

        if (requestOptional.isEmpty()) {
            return false;
        }

        ScheduleChangeRequest request = requestOptional.get();

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new AppException("Solo se pueden eliminar solicitudes en estado PENDING");
        }

        scheduleChangeRequestRepository.delete(request);
        return true;
    }

    public boolean getGroupCapacityAlert(String groupId) {
        Optional<Group> groupOptional = groupRepository.findById(groupId);

        if (groupOptional.isEmpty()) {
            throw new AppException("Grupo no encontrado: " + groupId);
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
                .orElseThrow(() -> new AppException("Solicitud no encontrada: " + requestId));
    }

    private void validateStudent(String studentId) {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isEmpty() || !student.get().getActive()) {
            throw new AppException("Estudiante no válido o inactivo: " + studentId);
        }
    }

    private void validateGroups(String currentGroupId, String requestedGroupId) {
        if (currentGroupId.equals(requestedGroupId)) {
            throw new AppException("El grupo actual y el solicitado no pueden ser el mismo");
        }

        Optional<Group> currentGroup = groupRepository.findById(currentGroupId);
        Optional<Group> requestedGroup = groupRepository.findById(requestedGroupId);

        if (currentGroup.isEmpty() || requestedGroup.isEmpty()) {
            throw new AppException("Uno o ambos grupos no existen");
        }
    }

    private void validateCourses(String currentCourseCode, String requestedCourseCode) {
        if (currentCourseCode.equals(requestedCourseCode)) {
            throw new AppException("El curso actual y el solicitado no pueden ser el mismo");
        }

        Optional<Course> currentCourse = courseRepository.findById(currentCourseCode);
        Optional<Course> requestedCourse = courseRepository.findById(requestedCourseCode);

        if (currentCourse.isEmpty() || requestedCourse.isEmpty()) {
            throw new AppException("Uno o ambos cursos no existen");
        }

        if (!currentCourse.get().isActive() || !requestedCourse.get().isActive()) {
            throw new AppException("Uno o ambos cursos están inactivos");
        }
    }

    private void validateGroupCapacity(String groupId) {
        if (getGroupCapacityAlert(groupId)) {
            throw new AppException("El grupo solicitado está cerca de su capacidad máxima");
        }
    }

    private void validatePendingRequestsLimit(String studentId) {
        long pendingCount = scheduleChangeRequestRepository.countByStudentIdAndStatus(studentId, RequestStatus.PENDING);
        if (pendingCount >= 3) {
            throw new AppException("El estudiante tiene demasiadas solicitudes pendientes. Límite: 3");
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

    public ScheduleChangeRequest approveRequest(String requestId, String reviewerId,
                                                UserRole reviewerRole, String comments) {
        ScheduleChangeRequest request = findRequestById(requestId);

        if (getGroupCapacityAlert(request.getRequestedGroup().getGroupId())) {
            throw new AppException("No se puede aprobar la solicitud: el grupo solicitado ya no tiene capacidad disponible");
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

    public ScheduleChangeRequest cancelRequest(String requestId, String studentId) {
        ScheduleChangeRequest request = findRequestById(requestId);

        if (!request.getStudent().getId().equals(studentId)) {
            throw new AppException("Solo el estudiante propietario puede cancelar la solicitud");
        }

        if (request.getStatus() != RequestStatus.PENDING && request.getStatus() != RequestStatus.UNDER_REVIEW) {
            throw new AppException("Solo se pueden cancelar solicitudes en estado PENDING o UNDER_REVIEW");
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

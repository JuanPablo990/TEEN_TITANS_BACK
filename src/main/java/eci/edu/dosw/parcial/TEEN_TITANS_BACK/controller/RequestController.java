package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto.ScheduleRequestDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto.AcademicDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.RequestService;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestionar las solicitudes de cambio de horario académico.
 *
 * <p>Permite crear, consultar, actualizar, eliminar y revisar solicitudes relacionadas
 * con cambios de grupo o curso, así como obtener estadísticas e historial.</p>
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@RestController
@RequestMapping("/api/requests")
@CrossOrigin(origins = "*")
public class RequestController {

    private final RequestService requestService;

    /**
     * Constructor del controlador.
     *
     * @param requestService servicio para la gestión de solicitudes
     */
    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    /**
     * Crea una solicitud de cambio de grupo.
     *
     * @param request datos de la solicitud de cambio de grupo
     * @return la solicitud creada o un mensaje de error
     */
    @PostMapping("/group-change")
    public ResponseEntity<?> createGroupChangeRequest(@RequestBody GroupChangeRequest request) {
        try {
            ScheduleChangeRequest createdRequest = requestService.createGroupChangeRequest(
                    request.getStudent(),
                    request.getCurrentGroup(),
                    request.getRequestedGroup(),
                    request.getReason()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRequest);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor al crear la solicitud"));
        }
    }

    /**
     * Crea una solicitud de cambio de curso.
     *
     * @param request datos de la solicitud de cambio de curso
     * @return la solicitud creada o un mensaje de error
     */
    @PostMapping("/course-change")
    public ResponseEntity<?> createCourseChangeRequest(@RequestBody CourseChangeRequest request) {
        try {
            ScheduleChangeRequest createdRequest = requestService.createCourseChangeRequest(
                    request.getStudent(),
                    request.getCurrentCourse(),
                    request.getRequestedCourse(),
                    request.getReason()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRequest);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor al crear la solicitud"));
        }
    }

    /**
     * Obtiene el estado actual de una solicitud.
     *
     * @param requestId identificador de la solicitud
     * @return el estado de la solicitud o un mensaje de error
     */
    @GetMapping("/{requestId}/status")
    public ResponseEntity<?> getRequestStatus(@PathVariable String requestId) {
        try {
            RequestStatus status = requestService.getRequestStatus(requestId);
            return ResponseEntity.ok(Map.of("status", status));
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Obtiene el historial de solicitudes de un estudiante.
     *
     * @param studentId identificador del estudiante
     * @return lista de solicitudes o un mensaje de error
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getRequestHistory(@PathVariable String studentId) {
        try {
            List<ScheduleChangeRequest> requests = requestService.getRequestHistory(studentId);
            return ResponseEntity.ok(requests);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Obtiene el historial de decisiones de una solicitud.
     *
     * @param requestId identificador de la solicitud
     * @return lista de pasos de revisión o un mensaje de error
     */
    @GetMapping("/{requestId}/decision-history")
    public ResponseEntity<?> getDecisionHistory(@PathVariable String requestId) {
        try {
            List<ReviewStep> decisionHistory = requestService.getDecisionHistory(requestId);
            return ResponseEntity.ok(decisionHistory);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Actualiza los datos de una solicitud existente.
     *
     * @param requestId identificador de la solicitud
     * @param updates   mapa con los campos a actualizar
     * @return solicitud actualizada o un mensaje de error
     */
    @PutMapping("/{requestId}")
    public ResponseEntity<?> updateRequest(@PathVariable String requestId,
                                           @RequestBody Map<String, Object> updates) {
        try {
            ScheduleChangeRequest updatedRequest = requestService.updateRequest(requestId, updates);
            return ResponseEntity.ok(updatedRequest);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Elimina una solicitud.
     *
     * @param requestId identificador de la solicitud
     * @return respuesta vacía o mensaje de error si no se encuentra
     */
    @DeleteMapping("/{requestId}")
    public ResponseEntity<?> deleteRequest(@PathVariable String requestId) {
        try {
            boolean deleted = requestService.deleteRequest(requestId);
            if (deleted) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Solicitud no encontrada"));
            }
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Verifica si un grupo ha alcanzado su límite de capacidad.
     *
     * @param groupId identificador del grupo
     * @return alerta de capacidad o mensaje de error
     */
    @GetMapping("/groups/{groupId}/capacity-alert")
    public ResponseEntity<?> getGroupCapacityAlert(@PathVariable String groupId) {
        try {
            boolean capacityAlert = requestService.getGroupCapacityAlert(groupId);
            return ResponseEntity.ok(Map.of("capacityAlert", capacityAlert));
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Aprueba una solicitud existente.
     *
     * @param requestId     identificador de la solicitud
     * @param reviewRequest datos del revisor
     * @return solicitud aprobada o mensaje de error
     */
    @PostMapping("/{requestId}/approve")
    public ResponseEntity<?> approveRequest(@PathVariable String requestId,
                                            @RequestBody ReviewActionRequest reviewRequest) {
        try {
            ScheduleChangeRequest approvedRequest = requestService.approveRequest(
                    requestId,
                    reviewRequest.getReviewerId(),
                    reviewRequest.getReviewerRole(),
                    reviewRequest.getComments()
            );
            return ResponseEntity.ok(approvedRequest);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Rechaza una solicitud existente.
     *
     * @param requestId     identificador de la solicitud
     * @param reviewRequest datos del revisor
     * @return solicitud rechazada o mensaje de error
     */
    @PostMapping("/{requestId}/reject")
    public ResponseEntity<?> rejectRequest(@PathVariable String requestId,
                                           @RequestBody ReviewActionRequest reviewRequest) {
        try {
            ScheduleChangeRequest rejectedRequest = requestService.rejectRequest(
                    requestId,
                    reviewRequest.getReviewerId(),
                    reviewRequest.getReviewerRole(),
                    reviewRequest.getComments()
            );
            return ResponseEntity.ok(rejectedRequest);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Cancela una solicitud realizada por un estudiante.
     *
     * @param requestId     identificador de la solicitud
     * @param cancelRequest información del estudiante solicitante
     * @return solicitud cancelada o mensaje de error
     */
    @PostMapping("/{requestId}/cancel")
    public ResponseEntity<?> cancelRequest(@PathVariable String requestId,
                                           @RequestBody CancelRequest cancelRequest) {
        try {
            ScheduleChangeRequest cancelledRequest = requestService.cancelRequest(
                    requestId,
                    cancelRequest.getStudentId()
            );
            return ResponseEntity.ok(cancelledRequest);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Obtiene estadísticas de solicitudes asociadas a un estudiante.
     *
     * @param studentId identificador del estudiante
     * @return mapa con las estadísticas o mensaje de error
     */
    @GetMapping("/student/{studentId}/statistics")
    public ResponseEntity<?> getRequestStatistics(@PathVariable String studentId) {
        try {
            Map<String, Object> statistics = requestService.getRequestStatistics(studentId);
            return ResponseEntity.ok(statistics);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * DTO para solicitudes de cambio de grupo.
     */
    public static class GroupChangeRequest {
        private Student student;
        private Group currentGroup;
        private Group requestedGroup;
        private String reason;

        public Student getStudent() { return student; }
        public void setStudent(Student student) { this.student = student; }
        public Group getCurrentGroup() { return currentGroup; }
        public void setCurrentGroup(Group currentGroup) { this.currentGroup = currentGroup; }
        public Group getRequestedGroup() { return requestedGroup; }
        public void setRequestedGroup(Group requestedGroup) { this.requestedGroup = requestedGroup; }
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }

    /**
     * DTO para solicitudes de cambio de curso.
     */
    public static class CourseChangeRequest {
        private Student student;
        private Course currentCourse;
        private Course requestedCourse;
        private String reason;

        public Student getStudent() { return student; }
        public void setStudent(Student student) { this.student = student; }
        public Course getCurrentCourse() { return currentCourse; }
        public void setCurrentCourse(Course currentCourse) { this.currentCourse = currentCourse; }
        public Course getRequestedCourse() { return requestedCourse; }
        public void setRequestedCourse(Course requestedCourse) { this.requestedCourse = requestedCourse; }
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }

    /**
     * DTO para acciones de revisión (aprobación o rechazo).
     */
    public static class ReviewActionRequest {
        private String reviewerId;
        private UserRole reviewerRole;
        private String comments;

        public String getReviewerId() { return reviewerId; }
        public void setReviewerId(String reviewerId) { this.reviewerId = reviewerId; }
        public UserRole getReviewerRole() { return reviewerRole; }
        public void setReviewerRole(UserRole reviewerRole) { this.reviewerRole = reviewerRole; }
        public String getComments() { return comments; }
        public void setComments(String comments) { this.comments = comments; }
    }

    /**
     * DTO para solicitudes de cancelación de solicitud.
     */
    public static class CancelRequest {
        private String studentId;

        public String getStudentId() { return studentId; }
        public void setStudentId(String studentId) { this.studentId = studentId; }
    }
}

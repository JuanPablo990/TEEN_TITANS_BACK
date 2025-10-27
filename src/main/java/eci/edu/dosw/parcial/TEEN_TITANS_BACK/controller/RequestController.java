package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.RequestService;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.RequestStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestionar las solicitudes de cambio de horario académico.
 * Permite crear, consultar, actualizar, eliminar y revisar solicitudes relacionadas
 * con cambios de grupo o curso, así como obtener estadísticas e historial.
 *
 * @author C
 * @version 1.0
 * @since 2025
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/requests")
@CrossOrigin(origins = "*")
public class RequestController {

    private final RequestService requestService;

    /**
     * Crea una solicitud de cambio de grupo.
     *
     * @param request la solicitud de cambio de grupo
     * @return ResponseEntity con la solicitud creada
     */
    @PostMapping("/group-change")
    public ResponseEntity<?> createGroupChangeRequest(@RequestBody GroupChangeRequest request) {
        try {
            ScheduleChangeRequest createdRequest = requestService.createGroupChangeRequest(
                    request.getStudentId(),
                    request.getCurrentGroupId(),
                    request.getRequestedGroupId(),
                    request.getReason()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRequest);
        } catch (AppException e) {
            log.warn("Error al crear solicitud de cambio de grupo: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error interno al crear solicitud de cambio de grupo", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor al crear la solicitud"));
        }
    }

    /**
     * Crea una solicitud de cambio de curso.
     *
     * @param request la solicitud de cambio de curso
     * @return ResponseEntity con la solicitud creada
     */
    @PostMapping("/course-change")
    public ResponseEntity<?> createCourseChangeRequest(@RequestBody CourseChangeRequest request) {
        try {
            ScheduleChangeRequest createdRequest = requestService.createCourseChangeRequest(
                    request.getStudentId(),
                    request.getCurrentCourseCode(),
                    request.getRequestedCourseCode(),
                    request.getReason()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRequest);
        } catch (AppException e) {
            log.warn("Error al crear solicitud de cambio de curso: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error interno al crear solicitud de cambio de curso", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor al crear la solicitud"));
        }
    }

    /**
     * Obtiene el estado de una solicitud.
     *
     * @param requestId el ID de la solicitud
     * @return ResponseEntity con el estado de la solicitud
     */
    @GetMapping("/{requestId}/status")
    public ResponseEntity<?> getRequestStatus(@PathVariable String requestId) {
        try {
            RequestStatus status = requestService.getRequestStatus(requestId);
            return ResponseEntity.ok(Map.of("status", status));
        } catch (AppException e) {
            log.warn("Solicitud no encontrada: {}", requestId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Obtiene el historial de solicitudes de un estudiante.
     *
     * @param studentId el ID del estudiante
     * @return ResponseEntity con el historial de solicitudes
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getRequestHistory(@PathVariable String studentId) {
        try {
            List<ScheduleChangeRequest> requests = requestService.getRequestHistory(studentId);
            return ResponseEntity.ok(requests);
        } catch (AppException e) {
            log.warn("Error al obtener historial para estudiante: {}", studentId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Obtiene el historial de decisiones de una solicitud.
     *
     * @param requestId el ID de la solicitud
     * @return ResponseEntity con el historial de decisiones
     */
    @GetMapping("/{requestId}/decision-history")
    public ResponseEntity<?> getDecisionHistory(@PathVariable String requestId) {
        try {
            List<ReviewStep> decisionHistory = requestService.getDecisionHistory(requestId);
            return ResponseEntity.ok(decisionHistory);
        } catch (AppException e) {
            log.warn("Solicitud no encontrada para historial: {}", requestId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Verifica si hay alerta de capacidad para un grupo.
     *
     * @param groupId el ID del grupo
     * @return ResponseEntity con el estado de la alerta de capacidad
     */
    @GetMapping("/groups/{groupId}/capacity-alert")
    public ResponseEntity<?> getGroupCapacityAlert(@PathVariable String groupId) {
        try {
            boolean capacityAlert = requestService.getGroupCapacityAlert(groupId);
            return ResponseEntity.ok(Map.of("capacityAlert", capacityAlert));
        } catch (AppException e) {
            log.warn("Grupo no encontrado: {}", groupId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Obtiene estadísticas de solicitudes de un estudiante.
     *
     * @param studentId el ID del estudiante
     * @return ResponseEntity con las estadísticas de solicitudes
     */
    @GetMapping("/student/{studentId}/statistics")
    public ResponseEntity<?> getRequestStatistics(@PathVariable String studentId) {
        try {
            Map<String, Object> statistics = requestService.getRequestStatistics(studentId);
            return ResponseEntity.ok(statistics);
        } catch (AppException e) {
            log.warn("Error al obtener estadísticas para estudiante: {}", studentId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Actualiza una solicitud existente.
     *
     * @param requestId el ID de la solicitud
     * @param updates los campos a actualizar
     * @return ResponseEntity con la solicitud actualizada
     */
    @PutMapping("/{requestId}")
    public ResponseEntity<?> updateRequest(@PathVariable String requestId,
                                           @RequestBody Map<String, Object> updates) {
        try {
            ScheduleChangeRequest updatedRequest = requestService.updateRequest(requestId, updates);
            return ResponseEntity.ok(updatedRequest);
        } catch (AppException e) {
            log.warn("Error al actualizar solicitud: {}", requestId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Elimina una solicitud.
     *
     * @param requestId el ID de la solicitud
     * @return ResponseEntity sin contenido si se elimina correctamente
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
            log.warn("Error al eliminar solicitud: {}", requestId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Aprueba una solicitud.
     *
     * @param requestId el ID de la solicitud
     * @param reviewRequest la información de la revisión
     * @return ResponseEntity con la solicitud aprobada
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
            log.warn("Error al aprobar solicitud: {}", requestId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Rechaza una solicitud.
     *
     * @param requestId el ID de la solicitud
     * @param reviewRequest la información de la revisión
     * @return ResponseEntity con la solicitud rechazada
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
            log.warn("Error al rechazar solicitud: {}", requestId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Cancela una solicitud.
     *
     * @param requestId el ID de la solicitud
     * @param cancelRequest la información de cancelación
     * @return ResponseEntity con la solicitud cancelada
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
            log.warn("Error al cancelar solicitud: {}", requestId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * DTO para solicitud de cambio de grupo.
     */
    @Data
    public static class GroupChangeRequest {
        private String studentId;
        private String currentGroupId;
        private String requestedGroupId;
        private String reason;
    }

    /**
     * DTO para solicitud de cambio de curso.
     */
    @Data
    public static class CourseChangeRequest {
        private String studentId;
        private String currentCourseCode;
        private String requestedCourseCode;
        private String reason;
    }

    /**
     * DTO para acción de revisión.
     */
    @Data
    public static class ReviewActionRequest {
        private String reviewerId;
        private UserRole reviewerRole;
        private String comments;
    }

    /**
     * DTO para cancelación de solicitud.
     */
    @Data
    public static class CancelRequest {
        private String studentId;
    }
}
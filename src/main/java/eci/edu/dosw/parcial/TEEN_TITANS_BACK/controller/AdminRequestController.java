package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.RequestStatus;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.ScheduleChangeRequest;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.AdminRequestService;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestión de solicitudes administrativas y generación de reportes.
 * Proporciona endpoints para manejar solicitudes, estadísticas y reportes globales y por facultad.
 *
 * @author
 * Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/requests")
@CrossOrigin(origins = "*")
public class AdminRequestController {

    private final AdminRequestService adminRequestService;

    private static final String INTERNAL_SERVER_ERROR = "Error interno del servidor";
    private static final String NOT_FOUND = "Recurso no encontrado";

    /**
     * Obtiene todas las solicitudes globales.
     * @return Lista de solicitudes registradas.
     */
    @GetMapping
    public ResponseEntity<?> getGlobalRequests() {
        try {
            List<ScheduleChangeRequest> requests = adminRequestService.getGlobalRequests();
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            log.error("Error al obtener solicitudes globales", e);
            return errorResponse("al obtener las solicitudes globales");
        }
    }

    /**
     * Obtiene las solicitudes de una facultad específica.
     * @param faculty nombre de la facultad.
     * @return Lista de solicitudes por facultad.
     */
    @GetMapping("/faculty/{faculty}")
    public ResponseEntity<?> getRequestsByFaculty(@PathVariable String faculty) {
        try {
            List<ScheduleChangeRequest> requests = adminRequestService.getRequestsByFaculty(faculty);
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            log.error("Error al obtener solicitudes por facultad: {}", faculty, e);
            return errorResponse("al obtener las solicitudes por facultad");
        }
    }

    /**
     * Obtiene los casos especiales de solicitudes.
     * @return Lista de casos especiales.
     */
    @GetMapping("/special-cases")
    public ResponseEntity<?> getSpecialCases() {
        try {
            List<ScheduleChangeRequest> specialCases = adminRequestService.getSpecialCases();
            return ResponseEntity.ok(specialCases);
        } catch (Exception e) {
            log.error("Error al obtener casos especiales", e);
            return errorResponse("al obtener los casos especiales");
        }
    }

    /**
     * Responde a una solicitud con una decisión administrativa.
     * @param requestId ID de la solicitud.
     * @param requestBody cuerpo con la decisión y comentarios.
     * @return Solicitud actualizada.
     */
    @PutMapping("/{requestId}/respond")
    public ResponseEntity<?> respondToRequest(
            @PathVariable String requestId,
            @RequestBody Map<String, String> requestBody) {
        try {
            String decisionStr = requestBody.get("decision");
            String comments = requestBody.get("comments");

            if (decisionStr == null || decisionStr.isBlank()) {
                return badRequestResponse("El campo 'decision' es requerido");
            }

            RequestStatus decision = parseRequestStatus(decisionStr);
            if (decision == null) {
                return badRequestResponse("Decisión no válida. Debe ser APPROVED o REJECTED");
            }

            ScheduleChangeRequest updatedRequest = adminRequestService.respondToRequest(requestId, decision, comments);
            return ResponseEntity.ok(updatedRequest);
        } catch (AppException e) {
            log.warn("Solicitud no encontrada: {}", requestId);
            return notFoundResponse(e.getMessage());
        } catch (Exception e) {
            log.error("Error al responder a la solicitud: {}", requestId, e);
            return errorResponse("al responder a la solicitud");
        }
    }

    /**
     * Solicita información adicional sobre una solicitud.
     * @param requestId ID de la solicitud.
     * @param requestBody cuerpo con los comentarios.
     * @return Solicitud actualizada.
     */
    @PutMapping("/{requestId}/request-info")
    public ResponseEntity<?> requestAdditionalInfo(
            @PathVariable String requestId,
            @RequestBody Map<String, String> requestBody) {
        try {
            String comments = requestBody.get("comments");
            if (comments == null || comments.isBlank()) {
                return badRequestResponse("El campo 'comments' es requerido");
            }

            ScheduleChangeRequest updatedRequest = adminRequestService.requestAdditionalInfo(requestId, comments);
            return ResponseEntity.ok(updatedRequest);
        } catch (AppException e) {
            log.warn("Solicitud no encontrada para información adicional: {}", requestId);
            return notFoundResponse(e.getMessage());
        } catch (Exception e) {
            log.error("Error al solicitar información adicional: {}", requestId, e);
            return errorResponse("al solicitar información adicional");
        }
    }

    /**
     * Aprueba un caso especial.
     * @param requestId ID de la solicitud.
     * @param requestBody cuerpo con los comentarios.
     * @return Solicitud actualizada.
     */
    @PutMapping("/{requestId}/approve-special")
    public ResponseEntity<?> approveSpecialCase(
            @PathVariable String requestId,
            @RequestBody Map<String, String> requestBody) {
        try {
            String comments = requestBody.get("comments");
            if (comments == null || comments.isBlank()) {
                return badRequestResponse("El campo 'comments' es requerido");
            }

            ScheduleChangeRequest updatedRequest = adminRequestService.approveSpecialCase(requestId, comments);
            return ResponseEntity.ok(updatedRequest);
        } catch (AppException e) {
            log.warn("Caso especial no encontrado: {}", requestId);
            return notFoundResponse(e.getMessage());
        } catch (Exception e) {
            log.error("Error al aprobar caso especial: {}", requestId, e);
            return errorResponse("al aprobar el caso especial");
        }
    }

    /**
     * Obtiene las estadísticas de aprobación por facultad.
     * @param faculty nombre de la facultad.
     * @return Estadísticas de aprobación.
     */
    @GetMapping("/stats/approval/faculty/{faculty}")
    public ResponseEntity<?> getApprovalRateByFaculty(@PathVariable String faculty) {
        try {
            AdminRequestService.ApprovalStats stats = adminRequestService.getApprovalRateByFaculty(faculty);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("Error al obtener estadísticas por facultad: {}", faculty, e);
            return errorResponse("al obtener las estadísticas por facultad");
        }
    }

    /**
     * Obtiene las estadísticas globales de aprobación.
     * @return Estadísticas globales.
     */
    @GetMapping("/stats/approval/global")
    public ResponseEntity<?> getGlobalApprovalRate() {
        try {
            AdminRequestService.ApprovalStats stats = adminRequestService.getGlobalApprovalRate();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("Error al obtener estadísticas globales", e);
            return errorResponse("al obtener las estadísticas globales");
        }
    }

    /**
     * Genera un reporte de estadísticas por facultad.
     * @param faculty nombre de la facultad.
     * @return Reporte con estadísticas detalladas.
     */
    @GetMapping("/stats/faculty/{faculty}")
    public ResponseEntity<?> generateFacultyReport(@PathVariable String faculty) {
        try {
            AdminRequestService.FacultyStats stats = adminRequestService.generateFacultyReport(faculty);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("Error al generar reporte de facultad: {}", faculty, e);
            return errorResponse("al generar el reporte de la facultad");
        }
    }

    /**
     * Genera un reporte global con estadísticas de solicitudes.
     * @return Reporte global.
     */
    @GetMapping("/stats/global")
    public ResponseEntity<?> generateGlobalReport() {
        try {
            AdminRequestService.GlobalStats stats = adminRequestService.generateGlobalReport();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("Error al generar reporte global", e);
            return errorResponse("al generar el reporte global");
        }
    }

    /**
     * Verifica el estado del controlador.
     * @return Mensaje de confirmación del estado del servicio.
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        log.info("Health check realizado exitosamente");
        return ResponseEntity.ok("AdminRequestController is working properly");
    }

    private ResponseEntity<String> errorResponse(String operation) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(INTERNAL_SERVER_ERROR + " " + operation);
    }

    private ResponseEntity<String> notFoundResponse(String message) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message != null ? message : NOT_FOUND);
    }

    private ResponseEntity<String> badRequestResponse(String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    private RequestStatus parseRequestStatus(String decisionStr) {
        try {
            return RequestStatus.valueOf(decisionStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}

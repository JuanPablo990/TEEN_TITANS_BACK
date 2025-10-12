package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.StudentRequestService;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestionar solicitudes estudiantiles como cambios de horario,
 * grupos alternativos, tasas de aprobación, prioridades y elegibilidad.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@RestController
@RequestMapping("/api/student-requests")
@CrossOrigin(origins = "*")
public class StudentRequestController {

    private final StudentRequestService studentRequestService;

    /**
     * Constructor del controlador.
     *
     * @param studentRequestService servicio de gestión de solicitudes estudiantiles.
     */
    @Autowired
    public StudentRequestController(StudentRequestService studentRequestService) {
        this.studentRequestService = studentRequestService;
    }

    /**
     * Obtiene la tasa de aprobación histórica de un estudiante.
     *
     * @param studentId ID del estudiante.
     * @return tasa de aprobación y su formato porcentual.
     */
    @GetMapping("/students/{studentId}/approval-rate")
    public ResponseEntity<?> getStudentApprovalRate(@PathVariable String studentId) {
        try {
            double approvalRate = studentRequestService.getStudentApprovalRate(studentId);
            return ResponseEntity.ok(Map.of(
                    "studentId", studentId,
                    "approvalRate", approvalRate,
                    "approvalRateFormatted", String.format("%.2f%%", approvalRate)
            ));
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al calcular la tasa de aprobación"));
        }
    }

    /**
     * Obtiene las solicitudes pendientes de un estudiante.
     *
     * @param studentId ID del estudiante.
     * @return lista de solicitudes pendientes.
     */
    @GetMapping("/students/{studentId}/pending-requests")
    public ResponseEntity<?> getPendingRequestsStatus(@PathVariable String studentId) {
        try {
            List<ScheduleChangeRequest> pendingRequests = studentRequestService.getPendingRequestsStatus(studentId);
            return ResponseEntity.ok(Map.of(
                    "studentId", studentId,
                    "pendingRequests", pendingRequests,
                    "count", pendingRequests.size()
            ));
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener las solicitudes pendientes"));
        }
    }

    /**
     * Obtiene la posición de prioridad de una solicitud en la cola.
     *
     * @param requestId ID de la solicitud.
     * @return posición de prioridad en la cola.
     */
    @GetMapping("/requests/{requestId}/priority-position")
    public ResponseEntity<?> getRequestPriorityPosition(@PathVariable String requestId) {
        try {
            int position = studentRequestService.getRequestPriorityPosition(requestId);
            return ResponseEntity.ok(Map.of(
                    "requestId", requestId,
                    "priorityPosition", position,
                    "queueInfo", position > 0 ?
                            String.format("Posición %d en la cola de prioridad", position) :
                            "Solicitud no encontrada en cola de prioridad"
            ));
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener la posición de prioridad"));
        }
    }

    /**
     * Obtiene los grupos alternativos recomendados para una solicitud.
     *
     * @param requestId ID de la solicitud.
     * @return grupos alternativos disponibles.
     */
    @GetMapping("/requests/{requestId}/alternative-groups")
    public ResponseEntity<?> getRecommendedAlternativeGroups(@PathVariable String requestId) {
        try {
            List<Group> alternativeGroups = studentRequestService.getRecommendedAlternativeGroups(requestId);
            return ResponseEntity.ok(Map.of(
                    "requestId", requestId,
                    "alternativeGroups", alternativeGroups,
                    "count", alternativeGroups.size(),
                    "message", alternativeGroups.isEmpty() ?
                            "No se encontraron grupos alternativos disponibles" :
                            String.format("Se encontraron %d grupos alternativos", alternativeGroups.size())
            ));
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener grupos alternativos"));
        }
    }

    /**
     * Obtiene el tiempo de espera estimado para una solicitud.
     *
     * @param requestId ID de la solicitud.
     * @return tiempo estimado de espera.
     */
    @GetMapping("/requests/{requestId}/estimated-wait-time")
    public ResponseEntity<?> getRequestEstimatedWaitTime(@PathVariable String requestId) {
        try {
            String estimatedWaitTime = studentRequestService.getRequestEstimatedWaitTime(requestId);
            return ResponseEntity.ok(Map.of(
                    "requestId", requestId,
                    "estimatedWaitTime", estimatedWaitTime
            ));
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al calcular el tiempo de espera estimado"));
        }
    }

    /**
     * Verifica si una solicitud puede ser cancelada.
     *
     * @param requestId ID de la solicitud.
     * @return información de elegibilidad de cancelación.
     */
    @GetMapping("/requests/{requestId}/cancel-eligibility")
    public ResponseEntity<?> canCancelRequest(@PathVariable String requestId) {
        try {
            boolean canCancel = studentRequestService.canCancelRequest(requestId);
            return ResponseEntity.ok(Map.of(
                    "requestId", requestId,
                    "canCancel", canCancel,
                    "message", canCancel ?
                            "La solicitud puede ser cancelada" :
                            "La solicitud no puede ser cancelada en este momento"
            ));
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al verificar elegibilidad de cancelación"));
        }
    }

    /**
     * Obtiene los documentos requeridos para un tipo de solicitud.
     *
     * @param requestType tipo de solicitud.
     * @return lista de documentos requeridos.
     */
    @GetMapping("/required-documents")
    public ResponseEntity<?> getRequiredDocuments(@RequestParam String requestType) {
        try {
            List<String> documents = studentRequestService.getRequiredDocuments(requestType);
            return ResponseEntity.ok(Map.of(
                    "requestType", requestType,
                    "requiredDocuments", documents,
                    "count", documents.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener los documentos requeridos"));
        }
    }

    /**
     * Obtiene información detallada del estado de una solicitud.
     *
     * @param requestId ID de la solicitud.
     * @return detalles del estado de la solicitud.
     */
    @GetMapping("/requests/{requestId}/status-details")
    public ResponseEntity<?> getRequestStatusDetails(@PathVariable String requestId) {
        try {
            int priorityPosition = studentRequestService.getRequestPriorityPosition(requestId);
            String estimatedWaitTime = studentRequestService.getRequestEstimatedWaitTime(requestId);
            boolean canCancel = studentRequestService.canCancelRequest(requestId);
            List<Group> alternativeGroups = studentRequestService.getRecommendedAlternativeGroups(requestId);

            return ResponseEntity.ok(Map.of(
                    "requestId", requestId,
                    "priorityPosition", priorityPosition,
                    "estimatedWaitTime", estimatedWaitTime,
                    "canCancel", canCancel,
                    "alternativeGroupsAvailable", !alternativeGroups.isEmpty(),
                    "alternativeGroupsCount", alternativeGroups.size(),
                    "summary", generateStatusSummary(priorityPosition, estimatedWaitTime, canCancel, alternativeGroups.size())
            ));
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener los detalles del estado"));
        }
    }

    /**
     * Obtiene métricas generales del estudiante relacionadas con sus solicitudes.
     *
     * @param studentId ID del estudiante.
     * @return métricas y recomendaciones del rendimiento.
     */
    @GetMapping("/students/{studentId}/metrics")
    public ResponseEntity<?> getStudentRequestMetrics(@PathVariable String studentId) {
        try {
            double approvalRate = studentRequestService.getStudentApprovalRate(studentId);
            List<ScheduleChangeRequest> pendingRequests = studentRequestService.getPendingRequestsStatus(studentId);

            return ResponseEntity.ok(Map.of(
                    "studentId", studentId,
                    "approvalRate", approvalRate,
                    "approvalRateFormatted", String.format("%.2f%%", approvalRate),
                    "pendingRequestsCount", pendingRequests.size(),
                    "pendingRequests", pendingRequests,
                    "performanceRating", getPerformanceRating(approvalRate),
                    "recommendations", generateRecommendations(approvalRate, pendingRequests.size())
            ));
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener las métricas del estudiante"));
        }
    }

    /**
     * Verifica si un estudiante puede enviar una nueva solicitud.
     *
     * @param studentId ID del estudiante.
     * @param requestType tipo de solicitud.
     * @return información de elegibilidad.
     */
    @GetMapping("/students/{studentId}/new-request-eligibility")
    public ResponseEntity<?> getNewRequestEligibility(@PathVariable String studentId,
                                                      @RequestParam String requestType) {
        try {
            List<ScheduleChangeRequest> pendingRequests = studentRequestService.getPendingRequestsStatus(studentId);
            List<String> requiredDocuments = studentRequestService.getRequiredDocuments(requestType);
            boolean hasPendingLimit = pendingRequests.size() < 3;
            boolean isEligible = hasPendingLimit;

            return ResponseEntity.ok(Map.of(
                    "studentId", studentId,
                    "requestType", requestType,
                    "eligible", isEligible,
                    "hasPendingLimit", hasPendingLimit,
                    "currentPendingCount", pendingRequests.size(),
                    "maxPendingAllowed", 3,
                    "requiredDocuments", requiredDocuments,
                    "message", isEligible ?
                            "Puede enviar una nueva solicitud" :
                            "No puede enviar más solicitudes pendientes"
            ));
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al verificar elegibilidad"));
        }
    }

    private String generateStatusSummary(int priorityPosition, String estimatedWaitTime,
                                         boolean canCancel, int alternativeGroupsCount) {
        StringBuilder summary = new StringBuilder();

        if (priorityPosition > 0)
            summary.append(String.format("Su solicitud está en posición %d de la cola. ", priorityPosition));

        summary.append(String.format("Tiempo estimado de espera: %s. ", estimatedWaitTime));
        summary.append(canCancel ? "Puede cancelar esta solicitud. " : "No puede cancelar esta solicitud en este momento. ");
        summary.append(alternativeGroupsCount > 0 ?
                String.format("Hay %d grupos alternativos disponibles.", alternativeGroupsCount) :
                "No hay grupos alternativos disponibles.");

        return summary.toString();
    }

    private String getPerformanceRating(double approvalRate) {
        if (approvalRate >= 80) return "EXCELENTE";
        else if (approvalRate >= 60) return "BUENO";
        else if (approvalRate >= 40) return "REGULAR";
        else return "BAJO";
    }

    private List<String> generateRecommendations(double approvalRate, int pendingCount) {
        java.util.List<String> recommendations = new java.util.ArrayList<>();

        if (approvalRate < 50) recommendations.add("Considere mejorar la justificación de sus solicitudes");
        if (pendingCount >= 2) recommendations.add("Tiene múltiples solicitudes pendientes, espere respuestas antes de enviar más");
        if (pendingCount == 0 && approvalRate > 70) recommendations.add("Puede enviar nuevas solicitudes cuando lo necesite");
        if (recommendations.isEmpty()) recommendations.add("Continúe con el buen trabajo");

        return recommendations;
    }

    /**
     * DTO para peticiones de documentos.
     */
    public static class DocumentRequest {
        private String requestType;

        public String getRequestType() { return requestType; }
        public void setRequestType(String requestType) { this.requestType = requestType; }
    }

    /**
     * DTO para verificar elegibilidad de nuevas solicitudes.
     */
    public static class EligibilityRequest {
        private String studentId;
        private String requestType;

        public String getStudentId() { return studentId; }
        public void setStudentId(String studentId) { this.studentId = studentId; }
        public String getRequestType() { return requestType; }
        public void setRequestType(String requestType) { this.requestType = requestType; }
    }
}

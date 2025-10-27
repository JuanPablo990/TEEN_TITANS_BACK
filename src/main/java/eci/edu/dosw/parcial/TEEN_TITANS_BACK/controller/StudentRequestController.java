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
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Controlador REST para gestionar solicitudes estudiantiles como cambios de horario,
 * grupos alternativos, tasas de aprobación, prioridades y elegibilidad.
 *
 * @author Equipo Teen Titans
 * @version 2.0
 * @since 2025
 */
@RestController
@RequestMapping("/api/student-requests")
@CrossOrigin(origins = "*")
public class StudentRequestController {

    private final StudentRequestService studentRequestService;
    private static final int MAX_PENDING_REQUESTS = 3;

    /**
     * Constructor del controlador de solicitudes estudiantiles.
     *
     * @param studentRequestService el servicio de solicitudes estudiantiles
     */
    @Autowired
    public StudentRequestController(StudentRequestService studentRequestService) {
        this.studentRequestService = studentRequestService;
    }

    /**
     * Obtiene la tasa de aprobación de un estudiante.
     *
     * @param studentId el ID del estudiante
     * @return ResponseEntity con la tasa de aprobación
     */
    @GetMapping("/students/{studentId}/approval-rate")
    public ResponseEntity<?> getStudentApprovalRate(@PathVariable String studentId) {
        try {
            double approvalRate = studentRequestService.getStudentApprovalRate(studentId);
            return ResponseEntity.ok(buildApprovalRateResponse(studentId, approvalRate));
        } catch (AppException e) {
            return buildNotFoundResponse(e.getMessage());
        } catch (Exception e) {
            return buildErrorResponse("Error al calcular la tasa de aprobación");
        }
    }

    /**
     * Obtiene las solicitudes pendientes de un estudiante.
     *
     * @param studentId el ID del estudiante
     * @return ResponseEntity con las solicitudes pendientes
     */
    @GetMapping("/students/{studentId}/pending-requests")
    public ResponseEntity<?> getPendingRequestsStatus(@PathVariable String studentId) {
        try {
            List<ScheduleChangeRequest> pendingRequests = studentRequestService.getPendingRequestsStatus(studentId);
            return ResponseEntity.ok(buildPendingRequestsResponse(studentId, pendingRequests));
        } catch (AppException e) {
            return buildNotFoundResponse(e.getMessage());
        } catch (Exception e) {
            return buildErrorResponse("Error al obtener las solicitudes pendientes");
        }
    }

    /**
     * Obtiene las métricas de solicitudes de un estudiante.
     *
     * @param studentId el ID del estudiante
     * @return ResponseEntity con las métricas del estudiante
     */
    @GetMapping("/students/{studentId}/metrics")
    public ResponseEntity<?> getStudentRequestMetrics(@PathVariable String studentId) {
        try {
            double approvalRate = studentRequestService.getStudentApprovalRate(studentId);
            List<ScheduleChangeRequest> pendingRequests = studentRequestService.getPendingRequestsStatus(studentId);
            return ResponseEntity.ok(buildStudentMetricsResponse(studentId, approvalRate, pendingRequests));
        } catch (AppException e) {
            return buildNotFoundResponse(e.getMessage());
        } catch (Exception e) {
            return buildErrorResponse("Error al obtener las métricas del estudiante");
        }
    }

    /**
     * Verifica la elegibilidad para nueva solicitud.
     *
     * @param studentId el ID del estudiante
     * @param requestType el tipo de solicitud
     * @return ResponseEntity con la información de elegibilidad
     */
    @GetMapping("/students/{studentId}/new-request-eligibility")
    public ResponseEntity<?> getNewRequestEligibility(@PathVariable String studentId,
                                                      @RequestParam String requestType) {
        try {
            List<ScheduleChangeRequest> pendingRequests = studentRequestService.getPendingRequestsStatus(studentId);
            List<String> requiredDocuments = studentRequestService.getRequiredDocuments(requestType);
            return ResponseEntity.ok(buildEligibilityResponse(studentId, requestType, pendingRequests, requiredDocuments));
        } catch (AppException e) {
            return buildNotFoundResponse(e.getMessage());
        } catch (Exception e) {
            return buildErrorResponse("Error al verificar elegibilidad");
        }
    }

    /**
     * Obtiene la posición de prioridad de una solicitud.
     *
     * @param requestId el ID de la solicitud
     * @return ResponseEntity con la posición de prioridad
     */
    @GetMapping("/requests/{requestId}/priority-position")
    public ResponseEntity<?> getRequestPriorityPosition(@PathVariable String requestId) {
        try {
            int position = studentRequestService.getRequestPriorityPosition(requestId);
            return ResponseEntity.ok(buildPriorityPositionResponse(requestId, position));
        } catch (AppException e) {
            return buildNotFoundResponse(e.getMessage());
        } catch (Exception e) {
            return buildErrorResponse("Error al obtener la posición de prioridad");
        }
    }

    /**
     * Obtiene grupos alternativos recomendados.
     *
     * @param requestId el ID de la solicitud
     * @return ResponseEntity con los grupos alternativos
     */
    @GetMapping("/requests/{requestId}/alternative-groups")
    public ResponseEntity<?> getRecommendedAlternativeGroups(@PathVariable String requestId) {
        try {
            List<Group> alternativeGroups = studentRequestService.getRecommendedAlternativeGroups(requestId);
            return ResponseEntity.ok(buildAlternativeGroupsResponse(requestId, alternativeGroups));
        } catch (AppException e) {
            return buildNotFoundResponse(e.getMessage());
        } catch (Exception e) {
            return buildErrorResponse("Error al obtener grupos alternativos");
        }
    }

    /**
     * Obtiene el tiempo de espera estimado de una solicitud.
     *
     * @param requestId el ID de la solicitud
     * @return ResponseEntity con el tiempo de espera estimado
     */
    @GetMapping("/requests/{requestId}/estimated-wait-time")
    public ResponseEntity<?> getRequestEstimatedWaitTime(@PathVariable String requestId) {
        try {
            String estimatedWaitTime = studentRequestService.getRequestEstimatedWaitTime(requestId);
            return ResponseEntity.ok(buildWaitTimeResponse(requestId, estimatedWaitTime));
        } catch (AppException e) {
            return buildNotFoundResponse(e.getMessage());
        } catch (Exception e) {
            return buildErrorResponse("Error al calcular el tiempo de espera estimado");
        }
    }

    /**
     * Verifica si una solicitud puede ser cancelada.
     *
     * @param requestId el ID de la solicitud
     * @return ResponseEntity con la elegibilidad de cancelación
     */
    @GetMapping("/requests/{requestId}/cancel-eligibility")
    public ResponseEntity<?> canCancelRequest(@PathVariable String requestId) {
        try {
            boolean canCancel = studentRequestService.canCancelRequest(requestId);
            return ResponseEntity.ok(buildCancelEligibilityResponse(requestId, canCancel));
        } catch (AppException e) {
            return buildNotFoundResponse(e.getMessage());
        } catch (Exception e) {
            return buildErrorResponse("Error al verificar elegibilidad de cancelación");
        }
    }

    /**
     * Obtiene los detalles del estado de una solicitud.
     *
     * @param requestId el ID de la solicitud
     * @return ResponseEntity con los detalles del estado
     */
    @GetMapping("/requests/{requestId}/status-details")
    public ResponseEntity<?> getRequestStatusDetails(@PathVariable String requestId) {
        try {
            int priorityPosition = studentRequestService.getRequestPriorityPosition(requestId);
            String estimatedWaitTime = studentRequestService.getRequestEstimatedWaitTime(requestId);
            boolean canCancel = studentRequestService.canCancelRequest(requestId);
            List<Group> alternativeGroups = studentRequestService.getRecommendedAlternativeGroups(requestId);

            return ResponseEntity.ok(buildStatusDetailsResponse(requestId, priorityPosition,
                    estimatedWaitTime, canCancel, alternativeGroups));
        } catch (AppException e) {
            return buildNotFoundResponse(e.getMessage());
        } catch (Exception e) {
            return buildErrorResponse("Error al obtener los detalles del estado");
        }
    }

    /**
     * Obtiene los documentos requeridos para un tipo de solicitud.
     *
     * @param requestType el tipo de solicitud
     * @return ResponseEntity con los documentos requeridos
     */
    @GetMapping("/required-documents")
    public ResponseEntity<?> getRequiredDocuments(@RequestParam String requestType) {
        try {
            List<String> documents = studentRequestService.getRequiredDocuments(requestType);
            return ResponseEntity.ok(buildRequiredDocumentsResponse(requestType, documents));
        } catch (Exception e) {
            return buildErrorResponse("Error al obtener los documentos requeridos");
        }
    }

    /**
     * Construye la respuesta de tasa de aprobación.
     *
     * @param studentId el ID del estudiante
     * @param approvalRate la tasa de aprobación
     * @return mapa con la respuesta
     */
    private Map<String, Object> buildApprovalRateResponse(String studentId, double approvalRate) {
        Map<String, Object> response = new HashMap<>();
        response.put("studentId", studentId);
        response.put("approvalRate", approvalRate);
        response.put("approvalRateFormatted", String.format("%.2f%%", approvalRate));
        return response;
    }

    /**
     * Construye la respuesta de solicitudes pendientes.
     *
     * @param studentId el ID del estudiante
     * @param pendingRequests las solicitudes pendientes
     * @return mapa con la respuesta
     */
    private Map<String, Object> buildPendingRequestsResponse(String studentId, List<ScheduleChangeRequest> pendingRequests) {
        Map<String, Object> response = new HashMap<>();
        response.put("studentId", studentId);
        response.put("pendingRequests", pendingRequests);
        response.put("count", pendingRequests.size());
        return response;
    }

    /**
     * Construye la respuesta de posición de prioridad.
     *
     * @param requestId el ID de la solicitud
     * @param position la posición de prioridad
     * @return mapa con la respuesta
     */
    private Map<String, Object> buildPriorityPositionResponse(String requestId, int position) {
        Map<String, Object> response = new HashMap<>();
        response.put("requestId", requestId);
        response.put("priorityPosition", position);
        response.put("queueInfo", position > 0 ?
                String.format("Posición %d en la cola de prioridad", position) :
                "Solicitud no encontrada en cola de prioridad");
        return response;
    }

    /**
     * Construye la respuesta de grupos alternativos.
     *
     * @param requestId el ID de la solicitud
     * @param alternativeGroups los grupos alternativos
     * @return mapa con la respuesta
     */
    private Map<String, Object> buildAlternativeGroupsResponse(String requestId, List<Group> alternativeGroups) {
        Map<String, Object> response = new HashMap<>();
        response.put("requestId", requestId);
        response.put("alternativeGroups", alternativeGroups);
        response.put("count", alternativeGroups.size());
        response.put("message", alternativeGroups.isEmpty() ?
                "No se encontraron grupos alternativos disponibles" :
                String.format("Se encontraron %d grupos alternativos", alternativeGroups.size()));
        return response;
    }

    /**
     * Construye la respuesta de tiempo de espera.
     *
     * @param requestId el ID de la solicitud
     * @param estimatedWaitTime el tiempo de espera estimado
     * @return mapa con la respuesta
     */
    private Map<String, Object> buildWaitTimeResponse(String requestId, String estimatedWaitTime) {
        Map<String, Object> response = new HashMap<>();
        response.put("requestId", requestId);
        response.put("estimatedWaitTime", estimatedWaitTime);
        return response;
    }

    /**
     * Construye la respuesta de elegibilidad de cancelación.
     *
     * @param requestId el ID de la solicitud
     * @param canCancel si puede ser cancelada
     * @return mapa con la respuesta
     */
    private Map<String, Object> buildCancelEligibilityResponse(String requestId, boolean canCancel) {
        Map<String, Object> response = new HashMap<>();
        response.put("requestId", requestId);
        response.put("canCancel", canCancel);
        response.put("message", canCancel ?
                "La solicitud puede ser cancelada" :
                "La solicitud no puede ser cancelada en este momento");
        return response;
    }

    /**
     * Construye la respuesta de documentos requeridos.
     *
     * @param requestType el tipo de solicitud
     * @param documents los documentos requeridos
     * @return mapa con la respuesta
     */
    private Map<String, Object> buildRequiredDocumentsResponse(String requestType, List<String> documents) {
        Map<String, Object> response = new HashMap<>();
        response.put("requestType", requestType);
        response.put("requiredDocuments", documents);
        response.put("count", documents.size());
        return response;
    }

    /**
     * Construye la respuesta de detalles del estado.
     *
     * @param requestId el ID de la solicitud
     * @param priorityPosition la posición de prioridad
     * @param estimatedWaitTime el tiempo de espera estimado
     * @param canCancel si puede ser cancelada
     * @param alternativeGroups los grupos alternativos
     * @return mapa con la respuesta
     */
    private Map<String, Object> buildStatusDetailsResponse(String requestId, int priorityPosition,
                                                           String estimatedWaitTime, boolean canCancel,
                                                           List<Group> alternativeGroups) {
        Map<String, Object> response = new HashMap<>();
        response.put("requestId", requestId);
        response.put("priorityPosition", priorityPosition);
        response.put("estimatedWaitTime", estimatedWaitTime);
        response.put("canCancel", canCancel);
        response.put("alternativeGroupsAvailable", !alternativeGroups.isEmpty());
        response.put("alternativeGroupsCount", alternativeGroups.size());
        response.put("summary", generateStatusSummary(priorityPosition, estimatedWaitTime, canCancel, alternativeGroups.size()));
        return response;
    }

    /**
     * Construye la respuesta de métricas del estudiante.
     *
     * @param studentId el ID del estudiante
     * @param approvalRate la tasa de aprobación
     * @param pendingRequests las solicitudes pendientes
     * @return mapa con la respuesta
     */
    private Map<String, Object> buildStudentMetricsResponse(String studentId, double approvalRate,
                                                            List<ScheduleChangeRequest> pendingRequests) {
        Map<String, Object> response = new HashMap<>();
        response.put("studentId", studentId);
        response.put("approvalRate", approvalRate);
        response.put("approvalRateFormatted", String.format("%.2f%%", approvalRate));
        response.put("pendingRequestsCount", pendingRequests.size());
        response.put("pendingRequests", pendingRequests);
        response.put("performanceRating", getPerformanceRating(approvalRate));
        response.put("recommendations", generateRecommendations(approvalRate, pendingRequests.size()));
        return response;
    }

    /**
     * Construye la respuesta de elegibilidad.
     *
     * @param studentId el ID del estudiante
     * @param requestType el tipo de solicitud
     * @param pendingRequests las solicitudes pendientes
     * @param requiredDocuments los documentos requeridos
     * @return mapa con la respuesta
     */
    private Map<String, Object> buildEligibilityResponse(String studentId, String requestType,
                                                         List<ScheduleChangeRequest> pendingRequests,
                                                         List<String> requiredDocuments) {
        boolean hasPendingLimit = pendingRequests.size() < MAX_PENDING_REQUESTS;
        boolean isEligible = hasPendingLimit;

        Map<String, Object> response = new HashMap<>();
        response.put("studentId", studentId);
        response.put("requestType", requestType);
        response.put("eligible", isEligible);
        response.put("hasPendingLimit", hasPendingLimit);
        response.put("currentPendingCount", pendingRequests.size());
        response.put("maxPendingAllowed", MAX_PENDING_REQUESTS);
        response.put("requiredDocuments", requiredDocuments);
        response.put("message", isEligible ?
                "Puede enviar una nueva solicitud" :
                "No puede enviar más solicitudes pendientes");
        return response;
    }

    /**
     * Construye una respuesta de error 404.
     *
     * @param message el mensaje de error
     * @return ResponseEntity con el error
     */
    private ResponseEntity<Map<String, String>> buildNotFoundResponse(String message) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", message));
    }

    /**
     * Construye una respuesta de error 500.
     *
     * @param message el mensaje de error
     * @return ResponseEntity con el error
     */
    private ResponseEntity<Map<String, String>> buildErrorResponse(String message) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", message));
    }

    /**
     * Genera un resumen del estado.
     *
     * @param priorityPosition la posición de prioridad
     * @param estimatedWaitTime el tiempo de espera estimado
     * @param canCancel si puede ser cancelada
     * @param alternativeGroupsCount el número de grupos alternativos
     * @return el resumen del estado
     */
    private String generateStatusSummary(int priorityPosition, String estimatedWaitTime,
                                         boolean canCancel, int alternativeGroupsCount) {
        StringBuilder summary = new StringBuilder();

        if (priorityPosition > 0) {
            summary.append(String.format("Su solicitud está en posición %d de la cola. ", priorityPosition));
        }

        summary.append(String.format("Tiempo estimado de espera: %s. ", estimatedWaitTime));
        summary.append(canCancel ? "Puede cancelar esta solicitud. " : "No puede cancelar esta solicitud en este momento. ");
        summary.append(alternativeGroupsCount > 0 ?
                String.format("Hay %d grupos alternativos disponibles.", alternativeGroupsCount) :
                "No hay grupos alternativos disponibles.");

        return summary.toString();
    }

    /**
     * Obtiene la calificación de rendimiento.
     *
     * @param approvalRate la tasa de aprobación
     * @return la calificación de rendimiento
     */
    private String getPerformanceRating(double approvalRate) {
        if (approvalRate >= 80) return "EXCELENTE";
        else if (approvalRate >= 60) return "BUENO";
        else if (approvalRate >= 40) return "REGULAR";
        else return "BAJO";
    }

    /**
     * Genera recomendaciones para el estudiante.
     *
     * @param approvalRate la tasa de aprobación
     * @param pendingCount el número de solicitudes pendientes
     * @return lista de recomendaciones
     */
    private List<String> generateRecommendations(double approvalRate, int pendingCount) {
        List<String> recommendations = new ArrayList<>();

        if (approvalRate < 50) recommendations.add("Considere mejorar la justificación de sus solicitudes");
        if (pendingCount >= 2) recommendations.add("Tiene múltiples solicitudes pendientes, espere respuestas antes de enviar más");
        if (pendingCount == 0 && approvalRate > 70) recommendations.add("Puede enviar nuevas solicitudes cuando lo necesite");
        if (recommendations.isEmpty()) recommendations.add("Continúe con el buen trabajo");

        return recommendations;
    }
}
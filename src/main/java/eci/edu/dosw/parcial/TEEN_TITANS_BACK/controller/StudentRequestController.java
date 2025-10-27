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

    @Autowired
    public StudentRequestController(StudentRequestService studentRequestService) {
        this.studentRequestService = studentRequestService;
    }

    // ========== ENDPOINTS DE ESTUDIANTES ==========

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

    // ========== ENDPOINTS DE SOLICITUDES ==========

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

    // ========== ENDPOINTS GENERALES ==========

    @GetMapping("/required-documents")
    public ResponseEntity<?> getRequiredDocuments(@RequestParam String requestType) {
        try {
            List<String> documents = studentRequestService.getRequiredDocuments(requestType);
            return ResponseEntity.ok(buildRequiredDocumentsResponse(requestType, documents));
        } catch (Exception e) {
            return buildErrorResponse("Error al obtener los documentos requeridos");
        }
    }

    // ========== MÉTODOS DE CONSTRUCCIÓN DE RESPUESTAS ==========

    private Map<String, Object> buildApprovalRateResponse(String studentId, double approvalRate) {
        Map<String, Object> response = new HashMap<>();
        response.put("studentId", studentId);
        response.put("approvalRate", approvalRate);
        response.put("approvalRateFormatted", String.format("%.2f%%", approvalRate));
        return response;
    }

    private Map<String, Object> buildPendingRequestsResponse(String studentId, List<ScheduleChangeRequest> pendingRequests) {
        Map<String, Object> response = new HashMap<>();
        response.put("studentId", studentId);
        response.put("pendingRequests", pendingRequests);
        response.put("count", pendingRequests.size());
        return response;
    }

    private Map<String, Object> buildPriorityPositionResponse(String requestId, int position) {
        Map<String, Object> response = new HashMap<>();
        response.put("requestId", requestId);
        response.put("priorityPosition", position);
        response.put("queueInfo", position > 0 ?
                String.format("Posición %d en la cola de prioridad", position) :
                "Solicitud no encontrada en cola de prioridad");
        return response;
    }

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

    private Map<String, Object> buildWaitTimeResponse(String requestId, String estimatedWaitTime) {
        Map<String, Object> response = new HashMap<>();
        response.put("requestId", requestId);
        response.put("estimatedWaitTime", estimatedWaitTime);
        return response;
    }

    private Map<String, Object> buildCancelEligibilityResponse(String requestId, boolean canCancel) {
        Map<String, Object> response = new HashMap<>();
        response.put("requestId", requestId);
        response.put("canCancel", canCancel);
        response.put("message", canCancel ?
                "La solicitud puede ser cancelada" :
                "La solicitud no puede ser cancelada en este momento");
        return response;
    }

    private Map<String, Object> buildRequiredDocumentsResponse(String requestType, List<String> documents) {
        Map<String, Object> response = new HashMap<>();
        response.put("requestType", requestType);
        response.put("requiredDocuments", documents);
        response.put("count", documents.size());
        return response;
    }

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

    // ========== MÉTODOS AUXILIARES ==========

    private ResponseEntity<Map<String, String>> buildNotFoundResponse(String message) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", message));
    }

    private ResponseEntity<Map<String, String>> buildErrorResponse(String message) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", message));
    }

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

    private String getPerformanceRating(double approvalRate) {
        if (approvalRate >= 80) return "EXCELENTE";
        else if (approvalRate >= 60) return "BUENO";
        else if (approvalRate >= 40) return "REGULAR";
        else return "BAJO";
    }

    private List<String> generateRecommendations(double approvalRate, int pendingCount) {
        List<String> recommendations = new ArrayList<>();

        if (approvalRate < 50) recommendations.add("Considere mejorar la justificación de sus solicitudes");
        if (pendingCount >= 2) recommendations.add("Tiene múltiples solicitudes pendientes, espere respuestas antes de enviar más");
        if (pendingCount == 0 && approvalRate > 70) recommendations.add("Puede enviar nuevas solicitudes cuando lo necesite");
        if (recommendations.isEmpty()) recommendations.add("Continúe con el buen trabajo");

        return recommendations;
    }
}
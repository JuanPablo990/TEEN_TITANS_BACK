package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.RequestStatus;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.ScheduleChangeRequest;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.AdminRequestService;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestión avanzada de solicitudes y reportes estadísticos.
 * <p>
 * Proporciona endpoints para la gestión de solicitudes de cambio de horario,
 * análisis de datos y generación de reportes estadísticos.
 * </p>
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@RestController
@RequestMapping("/api/admin/requests")
@CrossOrigin(origins = "*")
public class AdminRequestController {

    @Autowired
    private AdminRequestService adminRequestService;



    /**
     * Obtiene todas las solicitudes del sistema.
     *
     * @return ResponseEntity con la lista de todas las solicitudes
     */
    @GetMapping
    public ResponseEntity<?> getGlobalRequests() {
        try {
            List<ScheduleChangeRequest> requests = adminRequestService.getGlobalRequests();
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al obtener las solicitudes globales");
        }
    }

    /**
     * Obtiene todas las solicitudes de una facultad específica.
     *
     * @param faculty Nombre de la facultad
     * @return ResponseEntity con la lista de solicitudes de la facultad
     */
    @GetMapping("/faculty/{faculty}")
    public ResponseEntity<?> getRequestsByFaculty(@PathVariable String faculty) {
        try {
            List<ScheduleChangeRequest> requests = adminRequestService.getRequestsByFaculty(faculty);
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al obtener las solicitudes por facultad");
        }
    }

    /**
     * Responde a una solicitud con una decisión.
     *
     * @param requestId ID de la solicitud
     * @param requestBody Cuerpo de la solicitud con decisión y comentarios
     * @return ResponseEntity con la solicitud actualizada
     */
    @PutMapping("/{requestId}/respond")
    public ResponseEntity<?> respondToRequest(@PathVariable String requestId,
                                              @RequestBody Map<String, String> requestBody) {
        try {
            String decisionStr = requestBody.get("decision");
            String comments = requestBody.get("comments");

            if (decisionStr == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El campo 'decision' es requerido");
            }

            RequestStatus decision;
            try {
                decision = RequestStatus.valueOf(decisionStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Decisión no válida. Debe ser APPROVED o REJECTED");
            }

            ScheduleChangeRequest updatedRequest = adminRequestService.respondToRequest(requestId, decision, comments);
            return ResponseEntity.ok(updatedRequest);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al responder a la solicitud");
        }
    }

    /**
     * Solicita información adicional para una solicitud.
     *
     * @param requestId ID de la solicitud
     * @param requestBody Cuerpo de la solicitud con comentarios
     * @return ResponseEntity con la solicitud actualizada
     */
    @PutMapping("/{requestId}/request-info")
    public ResponseEntity<?> requestAdditionalInfo(@PathVariable String requestId,
                                                   @RequestBody Map<String, String> requestBody) {
        try {
            String comments = requestBody.get("comments");
            if (comments == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El campo 'comments' es requerido");
            }

            ScheduleChangeRequest updatedRequest = adminRequestService.requestAdditionalInfo(requestId, comments);
            return ResponseEntity.ok(updatedRequest);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al solicitar información adicional");
        }
    }

    /**
     * Aprueba un caso especial.
     *
     * @param requestId ID de la solicitud
     * @param requestBody Cuerpo de la solicitud con comentarios
     * @return ResponseEntity con la solicitud aprobada
     */
    @PutMapping("/{requestId}/approve-special")
    public ResponseEntity<?> approveSpecialCase(@PathVariable String requestId,
                                                @RequestBody Map<String, String> requestBody) {
        try {
            String comments = requestBody.get("comments");
            if (comments == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El campo 'comments' es requerido");
            }

            ScheduleChangeRequest updatedRequest = adminRequestService.approveSpecialCase(requestId, comments);
            return ResponseEntity.ok(updatedRequest);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al aprobar el caso especial");
        }
    }

    /**
     * Obtiene solicitudes que requieren tratamiento especial.
     *
     * @return ResponseEntity con la lista de casos especiales
     */
    @GetMapping("/special-cases")
    public ResponseEntity<?> getSpecialCases() {
        try {
            List<ScheduleChangeRequest> specialCases = adminRequestService.getSpecialCases();
            return ResponseEntity.ok(specialCases);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al obtener los casos especiales");
        }
    }

    /**
     * Obtiene solicitudes ordenadas por prioridad.
     *
     * @return ResponseEntity con la lista de solicitudes ordenadas por prioridad
     */
    @GetMapping("/priority")
    public ResponseEntity<?> getRequestsByPriority() {
        try {
            List<ScheduleChangeRequest> prioritizedRequests = adminRequestService.getRequestsByPriority();
            return ResponseEntity.ok(prioritizedRequests);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al obtener las solicitudes por prioridad");
        }
    }



    /**
     * Obtiene estadísticas de aprobación por facultad.
     *
     * @param faculty Nombre de la facultad
     * @return ResponseEntity con las estadísticas de aprobación
     */
    @GetMapping("/stats/approval/faculty/{faculty}")
    public ResponseEntity<?> getApprovalRateByFaculty(@PathVariable String faculty) {
        try {
            AdminRequestService.ApprovalStats stats = adminRequestService.getApprovalRateByFaculty(faculty);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al obtener las estadísticas por facultad");
        }
    }

    /**
     * Obtiene estadísticas de aprobación por curso.
     *
     * @param courseCode Código del curso
     * @return ResponseEntity con las estadísticas de aprobación
     */
    @GetMapping("/stats/approval/course/{courseCode}")
    public ResponseEntity<?> getApprovalRateByCourse(@PathVariable String courseCode) {
        try {
            AdminRequestService.ApprovalStats stats = adminRequestService.getApprovalRateByCourse(courseCode);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al obtener las estadísticas por curso");
        }
    }

    /**
     * Obtiene estadísticas de aprobación por grupo.
     *
     * @param groupId ID del grupo
     * @return ResponseEntity con las estadísticas de aprobación
     */
    @GetMapping("/stats/approval/group/{groupId}")
    public ResponseEntity<?> getApprovalRateByGroup(@PathVariable String groupId) {
        try {
            AdminRequestService.ApprovalStats stats = adminRequestService.getApprovalRateByGroup(groupId);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al obtener las estadísticas por grupo");
        }
    }

    /**
     * Obtiene estadísticas globales de aprobación.
     *
     * @return ResponseEntity con las estadísticas globales
     */
    @GetMapping("/stats/approval/global")
    public ResponseEntity<?> getGlobalApprovalRate() {
        try {
            AdminRequestService.ApprovalStats stats = adminRequestService.getGlobalApprovalRate();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al obtener las estadísticas globales");
        }
    }



    /**
     * Genera reporte de reasignaciones.
     *
     * @return ResponseEntity con las estadísticas de reasignaciones
     */
    @GetMapping("/stats/reassignments")
    public ResponseEntity<?> generateReassignmentReport() {
        try {
            AdminRequestService.ReassignmentStats stats = adminRequestService.generateReassignmentReport();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al generar el reporte de reasignaciones");
        }
    }

    /**
     * Genera reporte de un curso específico.
     *
     * @param courseCode Código del curso
     * @return ResponseEntity con las estadísticas del curso
     */
    @GetMapping("/stats/course/{courseCode}")
    public ResponseEntity<?> generateCourseReport(@PathVariable String courseCode) {
        try {
            AdminRequestService.CourseStats stats = adminRequestService.generateCourseReport(courseCode);
            return ResponseEntity.ok(stats);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al generar el reporte del curso");
        }
    }

    /**
     * Genera reporte de un grupo específico.
     *
     * @param groupId ID del grupo
     * @return ResponseEntity con las estadísticas del grupo
     */
    @GetMapping("/stats/group/{groupId}")
    public ResponseEntity<?> generateGroupReport(@PathVariable String groupId) {
        try {
            AdminRequestService.GroupStats stats = adminRequestService.generateGroupReport(groupId);
            return ResponseEntity.ok(stats);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al generar el reporte del grupo");
        }
    }

    /**
     * Genera reporte de una facultad específica.
     *
     * @param faculty Nombre de la facultad
     * @return ResponseEntity con las estadísticas de la facultad
     */
    @GetMapping("/stats/faculty/{faculty}")
    public ResponseEntity<?> generateFacultyReport(@PathVariable String faculty) {
        try {
            AdminRequestService.FacultyStats stats = adminRequestService.generateFacultyReport(faculty);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al generar el reporte de la facultad");
        }
    }

    /**
     * Genera reporte global del sistema.
     *
     * @return ResponseEntity con las estadísticas globales
     */
    @GetMapping("/stats/global")
    public ResponseEntity<?> generateGlobalReport() {
        try {
            AdminRequestService.GlobalStats stats = adminRequestService.generateGlobalReport();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al generar el reporte global");
        }
    }

    /**
     * Endpoint de salud para verificar que el controlador está funcionando.
     *
     * @return Mensaje de confirmación
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("AdminRequestController is working properly");
    }
}
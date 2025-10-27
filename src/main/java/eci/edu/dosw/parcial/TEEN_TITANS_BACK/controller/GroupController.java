package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Course;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.ScheduleChangeRequest;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.AdminGroupService;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para operaciones básicas de consulta de grupos académicos.
 * <p>
 * Proporciona endpoints de solo lectura para consultar información de grupos,
 * cursos, capacidades y listas de espera. Estas operaciones pueden ser utilizadas
 * por diferentes roles del sistema (estudiantes, profesores, coordinadores).
 * </p>
 *
 * @author Equipo Teen Titans
 * @version 2.0
 */
@Slf4j
@RestController
@RequestMapping("/api/groups")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class GroupController {

    private final AdminGroupService adminGroupService;

    /**
     * Obtiene el curso asociado a un grupo específico.
     */
    @GetMapping("/{groupId}/course")
    public ResponseEntity<?> getCourse(@PathVariable String groupId) {
        log.info("Solicitando curso para el grupo: {}", groupId);
        try {
            Course course = adminGroupService.getCourse(groupId);
            log.debug("Curso encontrado: {} para grupo: {}", course.getName(), groupId);
            return ResponseEntity.ok(course);
        } catch (AppException e) {
            log.warn("Grupo no encontrado: {}", groupId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.error("Error interno al obtener curso para grupo {}: {}", groupId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al obtener el curso del grupo");
        }
    }

    /**
     * Obtiene la capacidad máxima del aula asignada al grupo.
     */
    @GetMapping("/{groupId}/max-capacity")
    public ResponseEntity<?> getMaxCapacity(@PathVariable String groupId) {
        log.info("Solicitando capacidad máxima para el grupo: {}", groupId);
        try {
            Integer maxCapacity = adminGroupService.getMaxCapacity(groupId);
            log.debug("Capacidad máxima del grupo {}: {}", groupId, maxCapacity);
            return ResponseEntity.ok(maxCapacity);
        } catch (AppException e) {
            log.warn("Capacidad no encontrada para grupo: {}", groupId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.error("Error interno al obtener capacidad para grupo {}: {}", groupId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al obtener la capacidad máxima");
        }
    }

    /**
     * Calcula el número actual de estudiantes matriculados en el grupo.
     */
    @GetMapping("/{groupId}/current-enrollment")
    public ResponseEntity<?> getCurrentEnrollment(@PathVariable String groupId) {
        log.info("Solicitando matrícula actual para el grupo: {}", groupId);
        try {
            Integer enrollment = adminGroupService.getCurrentEnrollment(groupId);
            log.debug("Matrícula actual del grupo {}: {} estudiantes", groupId, enrollment);
            return ResponseEntity.ok(enrollment);
        } catch (AppException e) {
            log.warn("No se pudo obtener matrícula para grupo: {}", groupId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.error("Error interno al obtener matrícula para grupo {}: {}", groupId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al obtener la inscripción actual");
        }
    }

    /**
     * Devuelve la lista de solicitudes en espera asociadas al grupo.
     */
    @GetMapping("/{groupId}/waiting-list")
    public ResponseEntity<?> getWaitingList(@PathVariable String groupId) {
        log.info("Solicitando lista de espera para el grupo: {}", groupId);
        try {
            List<ScheduleChangeRequest> waitingList = adminGroupService.getWaitingList(groupId);
            log.debug("Lista de espera del grupo {}: {} solicitudes", groupId, waitingList.size());
            return ResponseEntity.ok(waitingList);
        } catch (AppException e) {
            log.warn("No se pudo obtener lista de espera para grupo: {}", groupId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.error("Error interno al obtener lista de espera para grupo {}: {}", groupId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al obtener la lista de espera");
        }
    }

    /**
     * Obtiene un mapa con el total de estudiantes inscritos por grupo dentro de un curso específico.
     */
    @GetMapping("/courses/{courseCode}/enrollment-by-group")
    public ResponseEntity<?> getTotalEnrolledByCourse(@PathVariable String courseCode) {
        log.info("Solicitando inscripciones por grupo para el curso: {}", courseCode);
        try {
            Map<String, Integer> enrollmentByGroup = adminGroupService.getTotalEnrolledByCourse(courseCode);
            log.debug("Inscripciones por grupo para curso {}: {} grupos", courseCode, enrollmentByGroup.size());
            return ResponseEntity.ok(enrollmentByGroup);
        } catch (AppException e) {
            log.warn("No se pudo obtener inscripciones para curso: {}", courseCode);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.error("Error interno al obtener inscripciones para curso {}: {}", courseCode, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al obtener las inscripciones por grupo");
        }
    }

    /**
     * Calcula la disponibilidad de cupos en un grupo.
     */
    @GetMapping("/{groupId}/availability")
    public ResponseEntity<?> getGroupAvailability(@PathVariable String groupId) {
        log.info("Calculando disponibilidad para el grupo: {}", groupId);
        try {
            Integer maxCapacity = adminGroupService.getMaxCapacity(groupId);
            Integer currentEnrollment = adminGroupService.getCurrentEnrollment(groupId);
            Integer availableSpots = maxCapacity - currentEnrollment;

            // Cálculo seguro del porcentaje de ocupación (evita división por cero y convierte long a int)
            int occupancyRate = 0;
            if (maxCapacity != null && maxCapacity > 0) {
                double occupancyPercentage = ((double) currentEnrollment / maxCapacity) * 100;
                occupancyRate = (int) Math.round(occupancyPercentage); // Cast explícito de long a int
            }

            Map<String, Object> availability = Map.of(
                    "groupId", groupId,
                    "maxCapacity", maxCapacity,
                    "currentEnrollment", currentEnrollment,
                    "availableSpots", availableSpots,
                    "occupancyRate", occupancyRate
            );

            log.debug("Disponibilidad del grupo {}: {} cupos disponibles ({}% ocupado)",
                    groupId, availableSpots, occupancyRate);
            return ResponseEntity.ok(availability);
        } catch (AppException e) {
            log.warn("No se pudo calcular disponibilidad para grupo: {}", groupId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.error("Error interno al calcular disponibilidad para grupo {}: {}", groupId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al calcular la disponibilidad");
        }
    }

    /**
     * Verifica si un grupo tiene cupos disponibles.
     */
    @GetMapping("/{groupId}/has-available-spots")
    public ResponseEntity<?> hasAvailableSpots(@PathVariable String groupId) {
        log.info("Verificando disponibilidad de cupos para el grupo: {}", groupId);
        try {
            Integer maxCapacity = adminGroupService.getMaxCapacity(groupId);
            Integer currentEnrollment = adminGroupService.getCurrentEnrollment(groupId);
            boolean hasSpots = currentEnrollment < maxCapacity;
            int availableSpots = maxCapacity - currentEnrollment;

            Map<String, Object> response = Map.of(
                    "groupId", groupId,
                    "hasAvailableSpots", hasSpots,
                    "availableSpots", availableSpots
            );

            log.debug("Verificación de cupos grupo {}: {} cupos disponibles", groupId, availableSpots);
            return ResponseEntity.ok(response);
        } catch (AppException e) {
            log.warn("No se pudo verificar disponibilidad para grupo: {}", groupId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.error("Error interno al verificar disponibilidad para grupo {}: {}", groupId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al verificar disponibilidad de cupos");
        }
    }

    /**
     * Obtiene información consolidada de un grupo.
     */
    @GetMapping("/{groupId}/info")
    public ResponseEntity<?> getGroupInfo(@PathVariable String groupId) {
        log.info("Solicitando información completa del grupo: {}", groupId);
        try {
            Course course = adminGroupService.getCourse(groupId);
            Integer maxCapacity = adminGroupService.getMaxCapacity(groupId);
            Integer currentEnrollment = adminGroupService.getCurrentEnrollment(groupId);
            List<ScheduleChangeRequest> waitingList = adminGroupService.getWaitingList(groupId);

            int availableSpots = maxCapacity - currentEnrollment;

            // Cálculo seguro del porcentaje de ocupación (evita división por cero)
            int occupancyRate = 0;
            if (maxCapacity != null && maxCapacity > 0) {
                double occupancyPercentage = ((double) currentEnrollment / maxCapacity) * 100;
                occupancyRate = (int) Math.round(occupancyPercentage);
            }

            Map<String, Object> groupInfo = Map.of(
                    "groupId", groupId,
                    "course", course,
                    "capacity", Map.of(
                            "max", maxCapacity,
                            "current", currentEnrollment,
                            "available", availableSpots,
                            "occupancyRate", occupancyRate
                    ),
                    "waitingList", Map.of(
                            "count", waitingList.size(),
                            "requests", waitingList
                    )
            );

            log.debug("Información del grupo {} obtenida exitosamente", groupId);
            return ResponseEntity.ok(groupInfo);
        } catch (AppException e) {
            log.warn("No se pudo obtener información para grupo: {}", groupId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.error("Error interno al obtener información para grupo {}: {}", groupId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al obtener la información del grupo");
        }
    }

    /**
     * Endpoint de salud para verificar que el controlador está funcionando.
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        log.info("Health check solicitado");
        return ResponseEntity.ok("GroupController is working properly with AdminGroupService");
    }
}
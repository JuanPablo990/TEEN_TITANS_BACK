package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Course;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.ScheduleChangeRequest;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.GroupService;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
 * @version 1.0
 */
@RestController
@RequestMapping("/api/groups")
@CrossOrigin(origins = "*")
public class GroupController {

    @Autowired
    @Qualifier("adminGroupService") // AÑADIR ESTA LÍNEA
    private GroupService groupService;

    /**
     * Obtiene el curso asociado a un grupo específico.
     *
     * @param groupId identificador único del grupo
     * @return ResponseEntity con el curso del grupo
     */
    @GetMapping("/{groupId}/course")
    public ResponseEntity<?> getCourse(@PathVariable String groupId) {
        try {
            Course course = groupService.getCourse(groupId);
            return ResponseEntity.ok(course);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al obtener el curso del grupo");
        }
    }

    /**
     * Obtiene la capacidad máxima del aula asignada al grupo.
     *
     * @param groupId identificador único del grupo
     * @return ResponseEntity con la capacidad máxima del grupo
     */
    @GetMapping("/{groupId}/max-capacity")
    public ResponseEntity<?> getMaxCapacity(@PathVariable String groupId) {
        try {
            Integer maxCapacity = groupService.getMaxCapacity(groupId);
            return ResponseEntity.ok(maxCapacity);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al obtener la capacidad máxima");
        }
    }

    /**
     * Calcula el número actual de estudiantes matriculados en el grupo.
     *
     * @param groupId identificador único del grupo
     * @return ResponseEntity con el número actual de estudiantes inscritos
     */
    @GetMapping("/{groupId}/current-enrollment")
    public ResponseEntity<?> getCurrentEnrollment(@PathVariable String groupId) {
        try {
            Integer enrollment = groupService.getCurrentEnrollment(groupId);
            return ResponseEntity.ok(enrollment);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al obtener la inscripción actual");
        }
    }

    /**
     * Devuelve la lista de solicitudes en espera asociadas al grupo.
     *
     * @param groupId identificador único del grupo
     * @return ResponseEntity con la lista de solicitudes de cambio de horario
     */
    @GetMapping("/{groupId}/waiting-list")
    public ResponseEntity<?> getWaitingList(@PathVariable String groupId) {
        try {
            List<ScheduleChangeRequest> waitingList = groupService.getWaitingList(groupId);
            return ResponseEntity.ok(waitingList);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al obtener la lista de espera");
        }
    }

    /**
     * Obtiene un mapa con el total de estudiantes inscritos por grupo dentro de un curso específico.
     *
     * @param courseCode código del curso académico
     * @return ResponseEntity con el mapa de grupos y sus totales de inscripción
     */
    @GetMapping("/courses/{courseCode}/enrollment-by-group")
    public ResponseEntity<?> getTotalEnrolledByCourse(@PathVariable String courseCode) {
        try {
            Map<String, Integer> enrollmentByGroup = groupService.getTotalEnrolledByCourse(courseCode);
            return ResponseEntity.ok(enrollmentByGroup);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al obtener las inscripciones por grupo");
        }
    }

    /**
     * Calcula la disponibilidad de cupos en un grupo.
     *
     * @param groupId identificador único del grupo
     * @return ResponseEntity con la disponibilidad de cupos
     */
    @GetMapping("/{groupId}/availability")
    public ResponseEntity<?> getGroupAvailability(@PathVariable String groupId) {
        try {
            Integer maxCapacity = groupService.getMaxCapacity(groupId);
            Integer currentEnrollment = groupService.getCurrentEnrollment(groupId);
            Integer availableSpots = maxCapacity - currentEnrollment;

            Map<String, Object> availability = Map.of(
                    "groupId", groupId,
                    "maxCapacity", maxCapacity,
                    "currentEnrollment", currentEnrollment,
                    "availableSpots", availableSpots,
                    "occupancyRate", Math.round((double) currentEnrollment / maxCapacity * 100)
            );

            return ResponseEntity.ok(availability);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al calcular la disponibilidad");
        }
    }

    /**
     * Verifica si un grupo tiene cupos disponibles.
     *
     * @param groupId identificador único del grupo
     * @return ResponseEntity con el resultado de la verificación
     */
    @GetMapping("/{groupId}/has-available-spots")
    public ResponseEntity<?> hasAvailableSpots(@PathVariable String groupId) {
        try {
            Integer maxCapacity = groupService.getMaxCapacity(groupId);
            Integer currentEnrollment = groupService.getCurrentEnrollment(groupId);
            boolean hasSpots = currentEnrollment < maxCapacity;

            Map<String, Object> response = Map.of(
                    "groupId", groupId,
                    "hasAvailableSpots", hasSpots,
                    "availableSpots", maxCapacity - currentEnrollment
            );

            return ResponseEntity.ok(response);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al verificar disponibilidad de cupos");
        }
    }

    /**
     * Obtiene información consolidada de un grupo.
     *
     * @param groupId identificador único del grupo
     * @return ResponseEntity con información completa del grupo
     */
    @GetMapping("/{groupId}/info")
    public ResponseEntity<?> getGroupInfo(@PathVariable String groupId) {
        try {
            Course course = groupService.getCourse(groupId);
            Integer maxCapacity = groupService.getMaxCapacity(groupId);
            Integer currentEnrollment = groupService.getCurrentEnrollment(groupId);
            List<ScheduleChangeRequest> waitingList = groupService.getWaitingList(groupId);

            Map<String, Object> groupInfo = Map.of(
                    "groupId", groupId,
                    "course", course,
                    "capacity", Map.of(
                            "max", maxCapacity,
                            "current", currentEnrollment,
                            "available", maxCapacity - currentEnrollment,
                            "occupancyRate", Math.round((double) currentEnrollment / maxCapacity * 100)
                    ),
                    "waitingList", Map.of(
                            "count", waitingList.size(),
                            "requests", waitingList
                    )
            );

            return ResponseEntity.ok(groupInfo);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al obtener la información del grupo");
        }
    }

    /**
     * Endpoint de salud para verificar que el controlador está funcionando.
     *
     * @return Mensaje de confirmación
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("GroupController is working properly");
    }
}
package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.AdminGroupService;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para operaciones administrativas de grupos, cursos y asignaciones académicas.
 * <p>
 * Proporciona endpoints para la gestión completa de grupos, cursos, profesores y estudiantes.
 * </p>
 *
 * @author Equipo Teen Titans
 * @version 1.0
 */
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminGroupController {

    @Autowired
    private AdminGroupService adminGroupService;

    // Constantes para mensajes
    private static final String INTERNAL_SERVER_ERROR = "Error interno del servidor";
    private static final String NOT_FOUND = "Recurso no encontrado";
    private static final String SUCCESS = "Operación exitosa";

    // --- Gestión de Grupos ---
    @PostMapping("/groups")
    public ResponseEntity<?> createGroup(@RequestBody Group group) {
        try {
            Group createdGroup = adminGroupService.createGroup(group);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdGroup);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return errorResponse("al crear el grupo");
        }
    }

    @GetMapping("/groups")
    public ResponseEntity<?> getAllGroups() {
        try {
            List<Group> groups = adminGroupService.listAllGroups();
            return ResponseEntity.ok(groups);
        } catch (Exception e) {
            return errorResponse("al obtener los grupos");
        }
    }

    // --- Asignación de Profesores ---
    @PutMapping("/groups/{groupId}/professor/{professorId}")
    public ResponseEntity<?> assignProfessorToGroup(@PathVariable String groupId,
                                                    @PathVariable String professorId) {
        try {
            adminGroupService.assignProfessorToGroup(groupId, professorId);
            return ResponseEntity.ok().body("Profesor asignado al grupo exitosamente");
        } catch (AppException e) {
            return notFoundResponse(e.getMessage());
        } catch (Exception e) {
            return errorResponse("al asignar el profesor al grupo");
        }
    }

    // --- Asignación de Aulas ---
    @PutMapping("/groups/{groupId}/classroom/{classroomId}")
    public ResponseEntity<?> assignClassroomToGroup(@PathVariable String groupId,
                                                    @PathVariable String classroomId) {
        try {
            adminGroupService.assignClassroomToGroup(groupId, classroomId);
            return ResponseEntity.ok().body("Aula asignada al grupo exitosamente");
        } catch (AppException e) {
            return notFoundResponse(e.getMessage());
        } catch (Exception e) {
            return errorResponse("al asignar el aula al grupo");
        }
    }

    // --- Consultas de Grupos ---
    @GetMapping("/groups/{groupId}/course")
    public ResponseEntity<?> getCourse(@PathVariable String groupId) {
        try {
            Course course = adminGroupService.getCourse(groupId);
            return ResponseEntity.ok(course);
        } catch (AppException e) {
            return notFoundResponse(e.getMessage());
        } catch (Exception e) {
            return errorResponse("al obtener el curso del grupo");
        }
    }

    @GetMapping("/groups/{groupId}/max-capacity")
    public ResponseEntity<?> getMaxCapacity(@PathVariable String groupId) {
        try {
            Integer maxCapacity = adminGroupService.getMaxCapacity(groupId);
            return ResponseEntity.ok(maxCapacity);
        } catch (AppException e) {
            return notFoundResponse(e.getMessage());
        } catch (Exception e) {
            return errorResponse("al obtener la capacidad máxima");
        }
    }

    @GetMapping("/groups/{groupId}/current-enrollment")
    public ResponseEntity<?> getCurrentEnrollment(@PathVariable String groupId) {
        try {
            Integer enrollment = adminGroupService.getCurrentEnrollment(groupId);
            return ResponseEntity.ok(enrollment);
        } catch (AppException e) {
            return notFoundResponse(e.getMessage());
        } catch (Exception e) {
            return errorResponse("al obtener la inscripción actual");
        }
    }

    @GetMapping("/groups/{groupId}/waiting-list")
    public ResponseEntity<?> getWaitingList(@PathVariable String groupId) {
        try {
            List<ScheduleChangeRequest> waitingList = adminGroupService.getWaitingList(groupId);
            return ResponseEntity.ok(waitingList);
        } catch (AppException e) {
            return notFoundResponse(e.getMessage());
        } catch (Exception e) {
            return errorResponse("al obtener la lista de espera");
        }
    }

    @GetMapping("/courses/{courseCode}/enrollment-by-group")
    public ResponseEntity<?> getTotalEnrolledByCourse(@PathVariable String courseCode) {
        try {
            Map<String, Integer> enrollmentByGroup = adminGroupService.getTotalEnrolledByCourse(courseCode);
            return ResponseEntity.ok(enrollmentByGroup);
        } catch (AppException e) {
            return notFoundResponse(e.getMessage());
        } catch (Exception e) {
            return errorResponse("al obtener las inscripciones por grupo");
        }
    }

    // --- Métodos auxiliares para respuestas de error ---
    private ResponseEntity<String> errorResponse(String operation) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(INTERNAL_SERVER_ERROR + " " + operation);
    }

    private ResponseEntity<String> notFoundResponse(String message) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }
}
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
 * Controlador REST para la gestión administrativa de grupos, cursos, profesores y aulas.
 * Proporciona endpoints para crear grupos, asignar profesores o aulas, y consultar información
 * académica relacionada.
 *
 * @author
 * @version 1.0
 */
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminGroupController {

    @Autowired
    private AdminGroupService adminGroupService;

    private static final String INTERNAL_SERVER_ERROR = "Error interno del servidor";
    private static final String NOT_FOUND = "Recurso no encontrado";
    private static final String SUCCESS = "Operación exitosa";

    /**
     * Crea un nuevo grupo académico.
     *
     * @param group objeto {@link Group} con los datos del grupo a crear.
     * @return respuesta con el grupo creado o un mensaje de error.
     */
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

    /**
     * Obtiene todos los grupos registrados.
     *
     * @return lista de grupos o mensaje de error.
     */
    @GetMapping("/groups")
    public ResponseEntity<?> getAllGroups() {
        try {
            List<Group> groups = adminGroupService.listAllGroups();
            return ResponseEntity.ok(groups);
        } catch (Exception e) {
            return errorResponse("al obtener los grupos");
        }
    }

    /**
     * Asigna un profesor a un grupo.
     *
     * @param groupId     identificador del grupo.
     * @param professorId identificador del profesor.
     * @return mensaje de éxito o error.
     */
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

    /**
     * Asigna un aula a un grupo.
     *
     * @param groupId     identificador del grupo.
     * @param classroomId identificador del aula.
     * @return mensaje de éxito o error.
     */
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

    /**
     * Obtiene el curso asociado a un grupo.
     *
     * @param groupId identificador del grupo.
     * @return objeto {@link Course} o mensaje de error.
     */
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

    /**
     * Obtiene la capacidad máxima permitida de un grupo.
     *
     * @param groupId identificador del grupo.
     * @return capacidad máxima o mensaje de error.
     */
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

    /**
     * Obtiene la cantidad actual de estudiantes inscritos en un grupo.
     *
     * @param groupId identificador del grupo.
     * @return número de inscripciones actuales o mensaje de error.
     */
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

    /**
     * Obtiene la lista de solicitudes en espera para un grupo.
     *
     * @param groupId identificador del grupo.
     * @return lista de {@link ScheduleChangeRequest} o mensaje de error.
     */
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

    /**
     * Obtiene el total de estudiantes inscritos en cada grupo de un curso.
     *
     * @param courseCode código del curso.
     * @return mapa con el ID del grupo y su número de inscritos, o mensaje de error.
     */
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

    /**
     * Genera una respuesta de error interno del servidor.
     *
     * @param operation descripción de la operación que falló.
     * @return respuesta con código HTTP 500.
     */
    private ResponseEntity<String> errorResponse(String operation) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(INTERNAL_SERVER_ERROR + " " + operation);
    }

    /**
     * Genera una respuesta de recurso no encontrado.
     *
     * @param message mensaje de error a mostrar.
     * @return respuesta con código HTTP 404.
     */
    private ResponseEntity<String> notFoundResponse(String message) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }
}

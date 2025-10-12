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
 * Proporciona endpoints para la gestión completa de periodos académicos, cursos, grupos,
 * profesores y estudiantes por parte de administradores.
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



    /**
     * Configura un nuevo periodo académico.
     *
     * @param period Periodo académico a configurar
     * @return ResponseEntity con el resultado de la operación
     */
    @PostMapping("/periods")
    public ResponseEntity<?> configureAcademicPeriod(@RequestBody AcademicPeriod period) {
        try {
            adminGroupService.configureAcademicPeriod(period);
            return ResponseEntity.ok().body("Periodo académico configurado exitosamente");
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al configurar el periodo académico");
        }
    }



    /**
     * Crea un nuevo curso.
     *
     * @param course Curso a crear
     * @return ResponseEntity con el curso creado
     */
    @PostMapping("/courses")
    public ResponseEntity<?> createCourse(@RequestBody Course course) {
        try {
            Course createdCourse = adminGroupService.createCourse(course);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al crear el curso");
        }
    }

    /**
     * Actualiza un curso existente.
     *
     * @param courseCode Código del curso a actualizar
     * @param course Datos actualizados del curso
     * @return ResponseEntity con el curso actualizado
     */
    @PutMapping("/courses/{courseCode}")
    public ResponseEntity<?> updateCourse(@PathVariable String courseCode, @RequestBody Course course) {
        try {
            Course updatedCourse = adminGroupService.updateCourse(courseCode, course);
            return ResponseEntity.ok(updatedCourse);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al actualizar el curso");
        }
    }

    /**
     * Elimina un curso.
     *
     * @param courseCode Código del curso a eliminar
     * @return ResponseEntity con el resultado de la operación
     */
    @DeleteMapping("/courses/{courseCode}")
    public ResponseEntity<?> deleteCourse(@PathVariable String courseCode) {
        try {
            adminGroupService.deleteCourse(courseCode);
            return ResponseEntity.ok().body("Curso eliminado exitosamente");
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al eliminar el curso");
        }
    }

    /**
     * Actualiza la capacidad de las aulas de todos los grupos de un curso.
     *
     * @param courseCode Código del curso
     * @param request Mapa con la nueva capacidad
     * @return ResponseEntity con el resultado de la operación
     */
    @PutMapping("/courses/{courseCode}/capacity")
    public ResponseEntity<?> updateCourseCapacity(@PathVariable String courseCode,
                                                  @RequestBody Map<String, Integer> request) {
        try {
            Integer newCapacity = request.get("newCapacity");
            if (newCapacity == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El campo 'newCapacity' es requerido");
            }

            adminGroupService.updateCourseCapacity(courseCode, newCapacity);
            return ResponseEntity.ok().body("Capacidad actualizada exitosamente para todos los grupos del curso");
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al actualizar la capacidad");
        }
    }



    /**
     * Crea un nuevo grupo.
     *
     * @param group Grupo a crear
     * @return ResponseEntity con el grupo creado
     */
    @PostMapping("/groups")
    public ResponseEntity<?> createGroup(@RequestBody Group group) {
        try {
            Group createdGroup = adminGroupService.createGroup(group);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdGroup);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al crear el grupo");
        }
    }

    /**
     * Actualiza un grupo existente.
     *
     * @param groupId ID del grupo a actualizar
     * @param updatedGroup Datos actualizados del grupo
     * @return ResponseEntity con el grupo actualizado
     */
    @PutMapping("/groups/{groupId}")
    public ResponseEntity<?> updateGroup(@PathVariable String groupId, @RequestBody Group updatedGroup) {
        try {
            Group group = adminGroupService.updateGroup(groupId, updatedGroup);
            return ResponseEntity.ok(group);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al actualizar el grupo");
        }
    }

    /**
     * Elimina un grupo.
     *
     * @param groupId ID del grupo a eliminar
     * @return ResponseEntity con el resultado de la operación
     */
    @DeleteMapping("/groups/{groupId}")
    public ResponseEntity<?> deleteGroup(@PathVariable String groupId) {
        try {
            adminGroupService.deleteGroup(groupId);
            return ResponseEntity.ok().body("Grupo eliminado exitosamente");
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al eliminar el grupo");
        }
    }

    /**
     * Obtiene todos los grupos.
     *
     * @return ResponseEntity con la lista de todos los grupos
     */
    @GetMapping("/groups")
    public ResponseEntity<?> getAllGroups() {
        try {
            List<Group> groups = adminGroupService.getAllGroups();
            return ResponseEntity.ok(groups);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al obtener los grupos");
        }
    }

    /**
     * Obtiene los grupos más solicitados.
     *
     * @return ResponseEntity con la lista de los grupos más solicitados
     */
    @GetMapping("/groups/most-requested")
    public ResponseEntity<?> getMostRequestedGroups() {
        try {
            List<Group> groups = adminGroupService.getMostRequestedGroups();
            return ResponseEntity.ok(groups);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al obtener los grupos más solicitados");
        }
    }

    /**
     * Obtiene los grupos que se dictan en un día específico.
     *
     * @param dayOfWeek Día de la semana
     * @return ResponseEntity con la lista de grupos del día especificado
     */
    @GetMapping("/groups/day/{dayOfWeek}")
    public ResponseEntity<?> getGroupsByDay(@PathVariable String dayOfWeek) {
        try {
            List<Group> groups = adminGroupService.getGroupsByDay(dayOfWeek);
            return ResponseEntity.ok(groups);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al obtener los grupos por día");
        }
    }



    /**
     * Asigna un profesor a un grupo.
     *
     * @param groupId ID del grupo
     * @param professorId ID del profesor
     * @return ResponseEntity con el grupo actualizado
     */
    @PutMapping("/groups/{groupId}/professor/{professorId}")
    public ResponseEntity<?> assignProfessorToGroup(@PathVariable String groupId,
                                                    @PathVariable String professorId) {
        try {
            Group updatedGroup = adminGroupService.assignProfessorToGroup(professorId, groupId);
            return ResponseEntity.ok(updatedGroup);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al asignar el profesor al grupo");
        }
    }

    /**
     * Remueve la asignación de profesor de un grupo.
     *
     * @param groupId ID del grupo
     * @return ResponseEntity con el grupo actualizado
     */
    @DeleteMapping("/groups/{groupId}/professor")
    public ResponseEntity<?> removeProfessorFromGroup(@PathVariable String groupId) {
        try {
            Group updatedGroup = adminGroupService.removeProfessorFromGroup(groupId);
            return ResponseEntity.ok(updatedGroup);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al remover el profesor del grupo");
        }
    }



    /**
     * Asigna un estudiante a un grupo.
     *
     * @param groupId ID del grupo
     * @param studentId ID del estudiante
     * @return ResponseEntity con el resultado de la operación
     */
    @PostMapping("/groups/{groupId}/students/{studentId}")
    public ResponseEntity<?> assignStudentToGroup(@PathVariable String groupId,
                                                  @PathVariable String studentId) {
        try {
            adminGroupService.assignStudentToGroup(studentId, groupId);
            return ResponseEntity.ok().body("Estudiante asignado al grupo exitosamente");
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al asignar el estudiante al grupo");
        }
    }

    /**
     * Remueve un estudiante de un grupo.
     *
     * @param groupId ID del grupo
     * @param studentId ID del estudiante
     * @return ResponseEntity con el resultado de la operación
     */
    @DeleteMapping("/groups/{groupId}/students/{studentId}")
    public ResponseEntity<?> removeStudentFromGroup(@PathVariable String groupId,
                                                    @PathVariable String studentId) {
        try {
            adminGroupService.removeStudentFromGroup(studentId, groupId);
            return ResponseEntity.ok().body("Estudiante removido del grupo exitosamente");
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al remover el estudiante del grupo");
        }
    }

    /**
     * Asigna un curso a un estudiante.
     *
     * @param studentId ID del estudiante
     * @param courseCode Código del curso
     * @return ResponseEntity con el resultado de la operación
     */
    @PostMapping("/students/{studentId}/courses/{courseCode}")
    public ResponseEntity<?> assignCourseToStudent(@PathVariable String studentId,
                                                   @PathVariable String courseCode) {
        try {
            adminGroupService.assignCourseToStudent(studentId, courseCode);
            return ResponseEntity.ok().body("Curso asignado al estudiante exitosamente");
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al asignar el curso al estudiante");
        }
    }

    /**
     * Remueve un curso de un estudiante.
     *
     * @param studentId ID del estudiante
     * @param courseCode Código del curso
     * @return ResponseEntity con el resultado de la operación
     */
    @DeleteMapping("/students/{studentId}/courses/{courseCode}")
    public ResponseEntity<?> removeCourseFromStudent(@PathVariable String studentId,
                                                     @PathVariable String courseCode) {
        try {
            adminGroupService.removeCourseFromStudent(studentId, courseCode);
            return ResponseEntity.ok().body("Curso removido del estudiante exitosamente");
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al remover el curso del estudiante");
        }
    }



    /**
     * Obtiene el curso asociado a un grupo.
     *
     * @param groupId ID del grupo
     * @return ResponseEntity con el curso del grupo
     */
    @GetMapping("/groups/{groupId}/course")
    public ResponseEntity<?> getCourse(@PathVariable String groupId) {
        try {
            Course course = adminGroupService.getCourse(groupId);
            return ResponseEntity.ok(course);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al obtener el curso del grupo");
        }
    }

    /**
     * Obtiene la capacidad máxima de un grupo.
     *
     * @param groupId ID del grupo
     * @return ResponseEntity con la capacidad máxima
     */
    @GetMapping("/groups/{groupId}/max-capacity")
    public ResponseEntity<?> getMaxCapacity(@PathVariable String groupId) {
        try {
            Integer maxCapacity = adminGroupService.getMaxCapacity(groupId);
            return ResponseEntity.ok(maxCapacity);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al obtener la capacidad máxima");
        }
    }

    /**
     * Obtiene la cantidad actual de estudiantes inscritos en un grupo.
     *
     * @param groupId ID del grupo
     * @return ResponseEntity con la cantidad de inscritos
     */
    @GetMapping("/groups/{groupId}/current-enrollment")
    public ResponseEntity<?> getCurrentEnrollment(@PathVariable String groupId) {
        try {
            Integer enrollment = adminGroupService.getCurrentEnrollment(groupId);
            return ResponseEntity.ok(enrollment);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al obtener la inscripción actual");
        }
    }

    /**
     * Obtiene el total de estudiantes inscritos por grupo dentro de un curso.
     *
     * @param courseCode Código del curso
     * @return ResponseEntity con el mapa de grupos e inscripciones
     */
    @GetMapping("/courses/{courseCode}/enrollment-by-group")
    public ResponseEntity<?> getTotalEnrolledByCourse(@PathVariable String courseCode) {
        try {
            Map<String, Integer> enrollmentByGroup = adminGroupService.getTotalEnrolledByCourse(courseCode);
            return ResponseEntity.ok(enrollmentByGroup);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al obtener las inscripciones por grupo");
        }
    }
}
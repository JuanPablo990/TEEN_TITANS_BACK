package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.GeneralManagementService;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar todas las entidades que no tienen CRUD completo en otros servicios.
 * Proporciona operaciones CRUD para AcademicPeriod, Classroom, Course, CourseStatusDetail,
 * Group, ReviewStep, Schedule, y StudentAcademicProgress.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Slf4j
@RestController
@RequestMapping("/api/general-management")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GeneralManagementController {

    private final GeneralManagementService generalManagementService;

    /**
     * Crea un nuevo período académico.
     *
     * @param academicPeriod el período académico a crear
     * @return ResponseEntity con el período académico creado
     */
    @PostMapping("/academic-periods")
    public ResponseEntity<AcademicPeriod> createAcademicPeriod(@RequestBody AcademicPeriod academicPeriod) {
        try {
            log.info("Creando nuevo período académico: {}", academicPeriod.getName());
            AcademicPeriod created = generalManagementService.createAcademicPeriod(academicPeriod);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error al crear período académico: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Obtiene todos los períodos académicos.
     *
     * @return ResponseEntity con la lista de períodos académicos
     */
    @GetMapping("/academic-periods")
    public ResponseEntity<List<AcademicPeriod>> getAllAcademicPeriods() {
        try {
            log.info("Obteniendo todos los períodos académicos");
            List<AcademicPeriod> periods = generalManagementService.getAllAcademicPeriods();
            return new ResponseEntity<>(periods, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al obtener períodos académicos: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene un período académico por su ID.
     *
     * @param periodId el ID del período académico
     * @return ResponseEntity con el período académico encontrado
     */
    @GetMapping("/academic-periods/{periodId}")
    public ResponseEntity<AcademicPeriod> getAcademicPeriodById(@PathVariable String periodId) {
        try {
            log.info("Obteniendo período académico con ID: {}", periodId);
            AcademicPeriod period = generalManagementService.getAcademicPeriodById(periodId);
            return new ResponseEntity<>(period, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al obtener período académico: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Obtiene el período académico actual.
     *
     * @return ResponseEntity con el período académico actual
     */
    @GetMapping("/academic-periods/current")
    public ResponseEntity<AcademicPeriod> getCurrentAcademicPeriod() {
        try {
            log.info("Obteniendo período académico actual");
            AcademicPeriod period = generalManagementService.getCurrentAcademicPeriod();
            return new ResponseEntity<>(period, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al obtener período académico actual: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Actualiza un período académico existente.
     *
     * @param periodId el ID del período académico a actualizar
     * @param academicPeriod los nuevos datos del período académico
     * @return ResponseEntity con el período académico actualizado
     */
    @PutMapping("/academic-periods/{periodId}")
    public ResponseEntity<AcademicPeriod> updateAcademicPeriod(
            @PathVariable String periodId,
            @RequestBody AcademicPeriod academicPeriod) {
        try {
            log.info("Actualizando período académico con ID: {}", periodId);
            AcademicPeriod updated = generalManagementService.updateAcademicPeriod(periodId, academicPeriod);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al actualizar período académico: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Elimina un período académico.
     *
     * @param periodId el ID del período académico a eliminar
     * @return ResponseEntity sin contenido
     */
    @DeleteMapping("/academic-periods/{periodId}")
    public ResponseEntity<Void> deleteAcademicPeriod(@PathVariable String periodId) {
        try {
            log.info("Eliminando período académico con ID: {}", periodId);
            generalManagementService.deleteAcademicPeriod(periodId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            log.error("Error al eliminar período académico: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Activa un período académico.
     *
     * @param periodId el ID del período académico a activar
     * @return ResponseEntity con el período académico activado
     */
    @PutMapping("/academic-periods/{periodId}/activate")
    public ResponseEntity<AcademicPeriod> activateAcademicPeriod(@PathVariable String periodId) {
        try {
            log.info("Activando período académico con ID: {}", periodId);
            AcademicPeriod activated = generalManagementService.activateAcademicPeriod(periodId);
            return new ResponseEntity<>(activated, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al activar período académico: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Crea un nuevo aula.
     *
     * @param classroom el aula a crear
     * @return ResponseEntity con el aula creada
     */
    @PostMapping("/classrooms")
    public ResponseEntity<Classroom> createClassroom(@RequestBody Classroom classroom) {
        try {
            log.info("Creando nueva aula: {} {}", classroom.getBuilding(), classroom.getRoomNumber());
            Classroom created = generalManagementService.createClassroom(classroom);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error al crear aula: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Obtiene todas las aulas.
     *
     * @return ResponseEntity con la lista de aulas
     */
    @GetMapping("/classrooms")
    public ResponseEntity<List<Classroom>> getAllClassrooms() {
        try {
            log.info("Obteniendo todas las aulas");
            List<Classroom> classrooms = generalManagementService.getAllClassrooms();
            return new ResponseEntity<>(classrooms, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al obtener aulas: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene un aula por su ID.
     *
     * @param classroomId el ID del aula
     * @return ResponseEntity con el aula encontrada
     */
    @GetMapping("/classrooms/{classroomId}")
    public ResponseEntity<Classroom> getClassroomById(@PathVariable String classroomId) {
        try {
            log.info("Obteniendo aula con ID: {}", classroomId);
            Classroom classroom = generalManagementService.getClassroomById(classroomId);
            return new ResponseEntity<>(classroom, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al obtener aula: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Obtiene aulas por tipo.
     *
     * @param roomType el tipo de aula
     * @return ResponseEntity con la lista de aulas del tipo especificado
     */
    @GetMapping("/classrooms/type/{roomType}")
    public ResponseEntity<List<Classroom>> getClassroomsByType(@PathVariable String roomType) {
        try {
            log.info("Obteniendo aulas por tipo: {}", roomType);
            List<Classroom> classrooms = generalManagementService.getClassroomsByType(roomType);
            return new ResponseEntity<>(classrooms, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al obtener aulas por tipo: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Obtiene aulas por edificio.
     *
     * @param building el edificio
     * @return ResponseEntity con la lista de aulas del edificio especificado
     */
    @GetMapping("/classrooms/building/{building}")
    public ResponseEntity<List<Classroom>> getClassroomsByBuilding(@PathVariable String building) {
        try {
            log.info("Obteniendo aulas por edificio: {}", building);
            List<Classroom> classrooms = generalManagementService.getClassroomsByBuilding(building);
            return new ResponseEntity<>(classrooms, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al obtener aulas por edificio: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene aulas con capacidad mínima.
     *
     * @param minCapacity la capacidad mínima
     * @return ResponseEntity con la lista de aulas que cumplen con la capacidad mínima
     */
    @GetMapping("/classrooms/capacity/{minCapacity}")
    public ResponseEntity<List<Classroom>> getClassroomsWithMinCapacity(@PathVariable Integer minCapacity) {
        try {
            log.info("Obteniendo aulas con capacidad mínima: {}", minCapacity);
            List<Classroom> classrooms = generalManagementService.getClassroomsWithMinCapacity(minCapacity);
            return new ResponseEntity<>(classrooms, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al obtener aulas por capacidad: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Actualiza un aula existente.
     *
     * @param classroomId el ID del aula a actualizar
     * @param classroom los nuevos datos del aula
     * @return ResponseEntity con el aula actualizada
     */
    @PutMapping("/classrooms/{classroomId}")
    public ResponseEntity<Classroom> updateClassroom(
            @PathVariable String classroomId,
            @RequestBody Classroom classroom) {
        try {
            log.info("Actualizando aula con ID: {}", classroomId);
            Classroom updated = generalManagementService.updateClassroom(classroomId, classroom);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al actualizar aula: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Elimina un aula.
     *
     * @param classroomId el ID del aula a eliminar
     * @return ResponseEntity sin contenido
     */
    @DeleteMapping("/classrooms/{classroomId}")
    public ResponseEntity<Void> deleteClassroom(@PathVariable String classroomId) {
        try {
            log.info("Eliminando aula con ID: {}", classroomId);
            generalManagementService.deleteClassroom(classroomId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            log.error("Error al eliminar aula: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Crea un nuevo curso.
     *
     * @param course el curso a crear
     * @return ResponseEntity con el curso creado
     */
    @PostMapping("/courses")
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        try {
            log.info("Creando nuevo curso: {} - {}", course.getCourseCode(), course.getName());
            Course created = generalManagementService.createCourse(course);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error al crear curso: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Obtiene todos los cursos.
     *
     * @return ResponseEntity con la lista de cursos
     */
    @GetMapping("/courses")
    public ResponseEntity<List<Course>> getAllCourses() {
        try {
            log.info("Obteniendo todos los cursos");
            List<Course> courses = generalManagementService.getAllCourses();
            return new ResponseEntity<>(courses, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al obtener cursos: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene un curso por su código.
     *
     * @param courseCode el código del curso
     * @return ResponseEntity con el curso encontrado
     */
    @GetMapping("/courses/{courseCode}")
    public ResponseEntity<Course> getCourseByCode(@PathVariable String courseCode) {
        try {
            log.info("Obteniendo curso con código: {}", courseCode);
            Course course = generalManagementService.getCourseByCode(courseCode);
            return new ResponseEntity<>(course, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al obtener curso: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Obtiene los cursos activos.
     *
     * @return ResponseEntity con la lista de cursos activos
     */
    @GetMapping("/courses/active")
    public ResponseEntity<List<Course>> getActiveCourses() {
        try {
            log.info("Obteniendo cursos activos");
            List<Course> courses = generalManagementService.getActiveCourses();
            return new ResponseEntity<>(courses, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al obtener cursos activos: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene cursos por programa académico.
     *
     * @param academicProgram el programa académico
     * @return ResponseEntity con la lista de cursos del programa especificado
     */
    @GetMapping("/courses/program/{academicProgram}")
    public ResponseEntity<List<Course>> getCoursesByProgram(@PathVariable String academicProgram) {
        try {
            log.info("Obteniendo cursos por programa: {}", academicProgram);
            List<Course> courses = generalManagementService.getCoursesByProgram(academicProgram);
            return new ResponseEntity<>(courses, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al obtener cursos por programa: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Actualiza un curso existente.
     *
     * @param courseCode el código del curso a actualizar
     * @param course los nuevos datos del curso
     * @return ResponseEntity con el curso actualizado
     */
    @PutMapping("/courses/{courseCode}")
    public ResponseEntity<Course> updateCourse(
            @PathVariable String courseCode,
            @RequestBody Course course) {
        try {
            log.info("Actualizando curso con código: {}", courseCode);
            Course updated = generalManagementService.updateCourse(courseCode, course);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al actualizar curso: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Elimina un curso.
     *
     * @param courseCode el código del curso a eliminar
     * @return ResponseEntity sin contenido
     */
    @DeleteMapping("/courses/{courseCode}")
    public ResponseEntity<Void> deleteCourse(@PathVariable String courseCode) {
        try {
            log.info("Eliminando curso con código: {}", courseCode);
            generalManagementService.deleteCourse(courseCode);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            log.error("Error al eliminar curso: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Cambia el estado de un curso.
     *
     * @param courseCode el código del curso
     * @param active true para activar, false para desactivar
     * @return ResponseEntity con el curso actualizado
     */
    @PutMapping("/courses/{courseCode}/status")
    public ResponseEntity<Course> toggleCourseStatus(
            @PathVariable String courseCode,
            @RequestParam boolean active) {
        try {
            log.info("{} curso con código: {}", active ? "Activando" : "Desactivando", courseCode);
            Course updated = generalManagementService.toggleCourseStatus(courseCode, active);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al cambiar estado del curso: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Crea un nuevo detalle de estado de curso.
     *
     * @param courseStatusDetail el detalle de estado de curso a crear
     * @return ResponseEntity con el detalle creado
     */
    @PostMapping("/course-status-details")
    public ResponseEntity<CourseStatusDetail> createCourseStatusDetail(@RequestBody CourseStatusDetail courseStatusDetail) {
        try {
            log.info("Creando nuevo detalle de estado de curso para estudiante: {}",
                    courseStatusDetail.getStudentId());
            CourseStatusDetail created = generalManagementService.createCourseStatusDetail(courseStatusDetail);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error al crear detalle de estado de curso: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Obtiene todos los detalles de estado de curso.
     *
     * @return ResponseEntity con la lista de detalles de estado de curso
     */
    @GetMapping("/course-status-details")
    public ResponseEntity<List<CourseStatusDetail>> getAllCourseStatusDetails() {
        try {
            log.info("Obteniendo todos los detalles de estado de curso");
            List<CourseStatusDetail> details = generalManagementService.getAllCourseStatusDetails();
            return new ResponseEntity<>(details, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al obtener detalles de estado de curso: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene un detalle de estado de curso por su ID.
     *
     * @param id el ID del detalle
     * @return ResponseEntity con el detalle encontrado
     */
    @GetMapping("/course-status-details/{id}")
    public ResponseEntity<CourseStatusDetail> getCourseStatusDetailById(@PathVariable String id) {
        try {
            log.info("Obteniendo detalle de estado de curso con ID: {}", id);
            CourseStatusDetail detail = generalManagementService.getCourseStatusDetailById(id);
            return new ResponseEntity<>(detail, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al obtener detalle de estado de curso: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Obtiene detalles de estado de curso por estudiante.
     *
     * @param studentId el ID del estudiante
     * @return ResponseEntity con la lista de detalles del estudiante
     */
    @GetMapping("/course-status-details/student/{studentId}")
    public ResponseEntity<List<CourseStatusDetail>> getCourseStatusDetailsByStudent(@PathVariable String studentId) {
        try {
            log.info("Obteniendo detalles de estado de curso para estudiante: {}", studentId);
            List<CourseStatusDetail> details = generalManagementService.getCourseStatusDetailsByStudent(studentId);
            return new ResponseEntity<>(details, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al obtener detalles de estado de curso por estudiante: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene detalles de estado de curso por curso.
     *
     * @param courseCode el código del curso
     * @return ResponseEntity con la lista de detalles del curso
     */
    @GetMapping("/course-status-details/course/{courseCode}")
    public ResponseEntity<List<CourseStatusDetail>> getCourseStatusDetailsByCourse(@PathVariable String courseCode) {
        try {
            log.info("Obteniendo detalles de estado de curso para curso: {}", courseCode);
            List<CourseStatusDetail> details = generalManagementService.getCourseStatusDetailsByCourse(courseCode);
            return new ResponseEntity<>(details, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al obtener detalles de estado de curso por curso: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene detalles de estado de curso por semestre.
     *
     * @param semester el semestre
     * @return ResponseEntity con la lista de detalles del semestre
     */
    @GetMapping("/course-status-details/semester/{semester}")
    public ResponseEntity<List<CourseStatusDetail>> getCourseStatusDetailsBySemester(@PathVariable String semester) {
        try {
            log.info("Obteniendo detalles de estado de curso para semestre: {}", semester);
            List<CourseStatusDetail> details = generalManagementService.getCourseStatusDetailsBySemester(semester);
            return new ResponseEntity<>(details, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al obtener detalles de estado de curso por semestre: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Actualiza un detalle de estado de curso existente.
     *
     * @param id el ID del detalle a actualizar
     * @param courseStatusDetail los nuevos datos del detalle
     * @return ResponseEntity con el detalle actualizado
     */
    @PutMapping("/course-status-details/{id}")
    public ResponseEntity<CourseStatusDetail> updateCourseStatusDetail(
            @PathVariable String id,
            @RequestBody CourseStatusDetail courseStatusDetail) {
        try {
            log.info("Actualizando detalle de estado de curso con ID: {}", id);
            CourseStatusDetail updated = generalManagementService.updateCourseStatusDetail(id, courseStatusDetail);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al actualizar detalle de estado de curso: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Elimina un detalle de estado de curso.
     *
     * @param id el ID del detalle a eliminar
     * @return ResponseEntity sin contenido
     */
    @DeleteMapping("/course-status-details/{id}")
    public ResponseEntity<Void> deleteCourseStatusDetail(@PathVariable String id) {
        try {
            log.info("Eliminando detalle de estado de curso con ID: {}", id);
            generalManagementService.deleteCourseStatusDetail(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            log.error("Error al eliminar detalle de estado de curso: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Elimina un grupo.
     *
     * @param groupId el ID del grupo a eliminar
     * @return ResponseEntity sin contenido
     */
    @DeleteMapping("/groups/{groupId}")
    public ResponseEntity<Void> deleteGroup(@PathVariable String groupId) {
        try {
            log.info("Eliminando grupo con ID: {}", groupId);
            generalManagementService.deleteGroup(groupId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            log.error("Error al eliminar grupo: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Crea un nuevo paso de revisión.
     *
     * @param reviewStep el paso de revisión a crear
     * @return ResponseEntity con el paso de revisión creado
     */
    @PostMapping("/review-steps")
    public ResponseEntity<ReviewStep> createReviewStep(@RequestBody ReviewStep reviewStep) {
        try {
            log.info("Creando nuevo paso de revisión para usuario: {}", reviewStep.getUserId());
            ReviewStep created = generalManagementService.createReviewStep(reviewStep);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error al crear paso de revisión: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Obtiene todos los pasos de revisión.
     *
     * @return ResponseEntity con la lista de pasos de revisión
     */
    @GetMapping("/review-steps")
    public ResponseEntity<List<ReviewStep>> getAllReviewSteps() {
        try {
            log.info("Obteniendo todos los pasos de revisión");
            List<ReviewStep> steps = generalManagementService.getAllReviewSteps();
            return new ResponseEntity<>(steps, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al obtener pasos de revisión: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene un paso de revisión por su ID.
     *
     * @param id el ID del paso de revisión
     * @return ResponseEntity con el paso de revisión encontrado
     */
    @GetMapping("/review-steps/{id}")
    public ResponseEntity<ReviewStep> getReviewStepById(@PathVariable String id) {
        try {
            log.info("Obteniendo paso de revisión con ID: {}", id);
            ReviewStep step = generalManagementService.getReviewStepById(id);
            return new ResponseEntity<>(step, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al obtener paso de revisión: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Obtiene pasos de revisión por usuario.
     *
     * @param userId el ID del usuario
     * @return ResponseEntity con la lista de pasos de revisión del usuario
     */
    @GetMapping("/review-steps/user/{userId}")
    public ResponseEntity<List<ReviewStep>> getReviewStepsByUser(@PathVariable String userId) {
        try {
            log.info("Obteniendo pasos de revisión para usuario: {}", userId);
            List<ReviewStep> steps = generalManagementService.getReviewStepsByUser(userId);
            return new ResponseEntity<>(steps, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al obtener pasos de revisión por usuario: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene pasos de revisión por rol de usuario.
     *
     * @param userRole el rol del usuario
     * @return ResponseEntity con la lista de pasos de revisión del rol
     */
    @GetMapping("/review-steps/role/{userRole}")
    public ResponseEntity<List<ReviewStep>> getReviewStepsByUserRole(@PathVariable String userRole) {
        try {
            log.info("Obteniendo pasos de revisión por rol: {}", userRole);
            List<ReviewStep> steps = generalManagementService.getReviewStepsByUserRole(userRole);
            return new ResponseEntity<>(steps, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al obtener pasos de revisión por rol: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Crea un nuevo horario.
     *
     * @param schedule el horario a crear
     * @return ResponseEntity con el horario creado
     */
    @PostMapping("/schedules")
    public ResponseEntity<Schedule> createSchedule(@RequestBody Schedule schedule) {
        try {
            log.info("Creando nuevo horario: {} {} {}",
                    schedule.getDayOfWeek(), schedule.getStartHour(), schedule.getEndHour());
            Schedule created = generalManagementService.createSchedule(schedule);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error al crear horario: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Obtiene todos los horarios.
     *
     * @return ResponseEntity con la lista de horarios
     */
    @GetMapping("/schedules")
    public ResponseEntity<List<Schedule>> getAllSchedules() {
        try {
            log.info("Obteniendo todos los horarios");
            List<Schedule> schedules = generalManagementService.getAllSchedules();
            return new ResponseEntity<>(schedules, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al obtener horarios: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene un horario por su ID.
     *
     * @param scheduleId el ID del horario
     * @return ResponseEntity con el horario encontrado
     */
    @GetMapping("/schedules/{scheduleId}")
    public ResponseEntity<Schedule> getScheduleById(@PathVariable String scheduleId) {
        try {
            log.info("Obteniendo horario con ID: {}", scheduleId);
            Schedule schedule = generalManagementService.getScheduleById(scheduleId);
            return new ResponseEntity<>(schedule, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al obtener horario: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Obtiene horarios por día de la semana.
     *
     * @param dayOfWeek el día de la semana
     * @return ResponseEntity con la lista de horarios del día especificado
     */
    @GetMapping("/schedules/day/{dayOfWeek}")
    public ResponseEntity<List<Schedule>> getSchedulesByDay(@PathVariable String dayOfWeek) {
        try {
            log.info("Obteniendo horarios por día: {}", dayOfWeek);
            List<Schedule> schedules = generalManagementService.getSchedulesByDay(dayOfWeek);
            return new ResponseEntity<>(schedules, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al obtener horarios por día: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Actualiza un horario existente.
     *
     * @param scheduleId el ID del horario a actualizar
     * @param schedule los nuevos datos del horario
     * @return ResponseEntity con el horario actualizado
     */
    @PutMapping("/schedules/{scheduleId}")
    public ResponseEntity<Schedule> updateSchedule(
            @PathVariable String scheduleId,
            @RequestBody Schedule schedule) {
        try {
            log.info("Actualizando horario con ID: {}", scheduleId);
            Schedule updated = generalManagementService.updateSchedule(scheduleId, schedule);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al actualizar horario: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Elimina un horario.
     *
     * @param scheduleId el ID del horario a eliminar
     * @return ResponseEntity sin contenido
     */
    @DeleteMapping("/schedules/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable String scheduleId) {
        try {
            log.info("Eliminando horario con ID: {}", scheduleId);
            generalManagementService.deleteSchedule(scheduleId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            log.error("Error al eliminar horario: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Crea un nuevo progreso académico de estudiante.
     *
     * @param progress el progreso académico a crear
     * @return ResponseEntity con el progreso académico creado
     */
    @PostMapping("/student-academic-progress")
    public ResponseEntity<StudentAcademicProgress> createStudentAcademicProgress(@RequestBody StudentAcademicProgress progress) {
        try {
            log.info("Creando nuevo progreso académico para estudiante: {}",
                    progress.getStudent().getId());
            StudentAcademicProgress created = generalManagementService.createStudentAcademicProgress(progress);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error al crear progreso académico: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Obtiene todos los progresos académicos de estudiantes.
     *
     * @return ResponseEntity con la lista de progresos académicos
     */
    @GetMapping("/student-academic-progress")
    public ResponseEntity<List<StudentAcademicProgress>> getAllStudentAcademicProgress() {
        try {
            log.info("Obteniendo todos los progresos académicos");
            List<StudentAcademicProgress> progressList = generalManagementService.getAllStudentAcademicProgress();
            return new ResponseEntity<>(progressList, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al obtener progresos académicos: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene un progreso académico por su ID.
     *
     * @param id el ID del progreso académico
     * @return ResponseEntity con el progreso académico encontrado
     */
    @GetMapping("/student-academic-progress/{id}")
    public ResponseEntity<StudentAcademicProgress> getStudentAcademicProgressById(@PathVariable String id) {
        try {
            log.info("Obteniendo progreso académico con ID: {}", id);
            StudentAcademicProgress progress = generalManagementService.getStudentAcademicProgressById(id);
            return new ResponseEntity<>(progress, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al obtener progreso académico: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Obtiene el progreso académico por ID de estudiante.
     *
     * @param studentId el ID del estudiante
     * @return ResponseEntity con el progreso académico del estudiante
     */
    @GetMapping("/student-academic-progress/student/{studentId}")
    public ResponseEntity<StudentAcademicProgress> getStudentAcademicProgressByStudentId(@PathVariable String studentId) {
        try {
            log.info("Obteniendo progreso académico para estudiante: {}", studentId);
            StudentAcademicProgress progress = generalManagementService.getStudentAcademicProgressByStudentId(studentId);
            return new ResponseEntity<>(progress, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al obtener progreso académico por estudiante: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Obtiene progresos académicos por facultad.
     *
     * @param faculty la facultad
     * @return ResponseEntity con la lista de progresos académicos de la facultad
     */
    @GetMapping("/student-academic-progress/faculty/{faculty}")
    public ResponseEntity<List<StudentAcademicProgress>> getStudentAcademicProgressByFaculty(@PathVariable String faculty) {
        try {
            log.info("Obteniendo progresos académicos por facultad: {}", faculty);
            List<StudentAcademicProgress> progressList = generalManagementService.getStudentAcademicProgressByFaculty(faculty);
            return new ResponseEntity<>(progressList, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al obtener progresos académicos por facultad: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene progresos académicos por programa académico.
     *
     * @param academicProgram el programa académico
     * @return ResponseEntity con la lista de progresos académicos del programa
     */
    @GetMapping("/student-academic-progress/program/{academicProgram}")
    public ResponseEntity<List<StudentAcademicProgress>> getStudentAcademicProgressByProgram(@PathVariable String academicProgram) {
        try {
            log.info("Obteniendo progresos académicos por programa: {}", academicProgram);
            List<StudentAcademicProgress> progressList = generalManagementService.getStudentAcademicProgressByProgram(academicProgram);
            return new ResponseEntity<>(progressList, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al obtener progresos académicos por programa: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Actualiza un progreso académico existente.
     *
     * @param id el ID del progreso académico a actualizar
     * @param progress los nuevos datos del progreso académico
     * @return ResponseEntity con el progreso académico actualizado
     */
    @PutMapping("/student-academic-progress/{id}")
    public ResponseEntity<StudentAcademicProgress> updateStudentAcademicProgress(
            @PathVariable String id,
            @RequestBody StudentAcademicProgress progress) {
        try {
            log.info("Actualizando progreso académico con ID: {}", id);
            StudentAcademicProgress updated = generalManagementService.updateStudentAcademicProgress(id, progress);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al actualizar progreso académico: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Elimina un progreso académico.
     *
     * @param id el ID del progreso académico a eliminar
     * @return ResponseEntity sin contenido
     */
    @DeleteMapping("/student-academic-progress/{id}")
    public ResponseEntity<Void> deleteStudentAcademicProgress(@PathVariable String id) {
        try {
            log.info("Eliminando progreso académico con ID: {}", id);
            generalManagementService.deleteStudentAcademicProgress(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            log.error("Error al eliminar progreso académico: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Obtiene estadísticas del sistema.
     *
     * @return ResponseEntity con las estadísticas del sistema
     */
    @GetMapping("/statistics")
    public ResponseEntity<GeneralManagementService.SystemStatistics> getSystemStatistics() {
        try {
            log.info("Obteniendo estadísticas del sistema");
            GeneralManagementService.SystemStatistics stats = generalManagementService.getSystemStatistics();
            return new ResponseEntity<>(stats, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al obtener estadísticas del sistema: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Verifica la integridad referencial de una entidad.
     *
     * @param entityType el tipo de entidad
     * @param entityId el ID de la entidad
     * @return ResponseEntity con el resultado de la verificación
     */
    @GetMapping("/integrity-check/{entityType}/{entityId}")
    public ResponseEntity<Boolean> checkReferentialIntegrity(
            @PathVariable String entityType,
            @PathVariable String entityId) {
        try {
            log.info("Verificando integridad referencial para {} con ID: {}", entityType, entityId);
            boolean isSafe = generalManagementService.checkReferentialIntegrity(entityType, entityId);
            return new ResponseEntity<>(isSafe, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al verificar integridad referencial: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Verifica el estado del controlador.
     *
     * @return ResponseEntity con el mensaje de estado
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        log.info("Health check - General Management Controller está funcionando");
        return new ResponseEntity<>("General Management Controller está funcionando correctamente", HttpStatus.OK);
    }
}
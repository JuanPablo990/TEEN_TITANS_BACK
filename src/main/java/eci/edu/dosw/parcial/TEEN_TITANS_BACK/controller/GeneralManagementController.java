package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.GeneralManagementService;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.RoomType;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
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

    // ========== ENDPOINTS DE ACADEMIC PERIOD ==========

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

    // ========== ENDPOINTS DE CLASSROOM ==========

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

    // ========== ENDPOINTS DE COURSE ==========

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

    // ========== ENDPOINTS DE COURSE STATUS DETAIL ==========

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

    // ========== ENDPOINTS DE GROUP ==========

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

    // ========== ENDPOINTS DE REVIEW STEP ==========

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

    // ========== ENDPOINTS DE SCHEDULE ==========

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

    // ========== ENDPOINTS DE STUDENT ACADEMIC PROGRESS ==========

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

    // ========== ENDPOINTS DE UTILIDAD ==========

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

    // ========== ENDPOINTS DE HEALTH CHECK ==========

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        log.info("Health check - General Management Controller está funcionando");
        return new ResponseEntity<>("General Management Controller está funcionando correctamente", HttpStatus.OK);
    }
}
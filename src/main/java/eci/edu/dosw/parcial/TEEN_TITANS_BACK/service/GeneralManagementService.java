package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.RoomType;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio general para gestionar todas las entidades que no tienen CRUD completo en otros servicios.
 * Proporciona operaciones CRUD para AcademicPeriod, Classroom, Course, CourseStatusDetail,
 * Group, ReviewStep, Schedule, y StudentAcademicProgress.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GeneralManagementService {

    // Repositorios para todas las entidades sin CRUD completo
    private final AcademicPeriodRepository academicPeriodRepository;
    private final ClassroomRepository classroomRepository;
    private final CourseRepository courseRepository;
    private final CourseStatusDetailRepository courseStatusDetailRepository;
    private final GroupRepository groupRepository;
    private final ReviewStepRepository reviewStepRepository;
    private final ScheduleRepository scheduleRepository;
    private final StudentAcademicProgressRepository studentAcademicProgressRepository;

    // ========== ACADEMIC PERIOD CRUD ==========

    /**
     * Crea un nuevo período académico.
     */
    public AcademicPeriod createAcademicPeriod(AcademicPeriod academicPeriod) {
        log.info("Creando nuevo período académico: {}", academicPeriod.getName());

        if (academicPeriodRepository.existsById(academicPeriod.getPeriodId())) {
            throw new AppException("Ya existe un período académico con ID: " + academicPeriod.getPeriodId());
        }

        // Si se está creando un período activo, desactivar otros períodos activos
        if (academicPeriod.isActive()) {
            deactivateOtherAcademicPeriods();
        }

        return academicPeriodRepository.save(academicPeriod);
    }

    /**
     * Obtiene todos los períodos académicos.
     */
    public List<AcademicPeriod> getAllAcademicPeriods() {
        log.debug("Obteniendo todos los períodos académicos");
        return academicPeriodRepository.findAll();
    }

    /**
     * Obtiene un período académico por ID.
     */
    public AcademicPeriod getAcademicPeriodById(String periodId) {
        log.debug("Obteniendo período académico con ID: {}", periodId);
        return academicPeriodRepository.findById(periodId)
                .orElseThrow(() -> new AppException("Período académico no encontrado con ID: " + periodId));
    }

    /**
     * Obtiene el período académico activo actual.
     */
    public AcademicPeriod getCurrentAcademicPeriod() {
        log.debug("Obteniendo período académico activo actual");
        return academicPeriodRepository.findByIsActiveTrue()
                .orElseThrow(() -> new AppException("No hay período académico activo"));
    }

    /**
     * Actualiza un período académico existente.
     */
    public AcademicPeriod updateAcademicPeriod(String periodId, AcademicPeriod academicPeriod) {
        log.info("Actualizando período académico con ID: {}", periodId);

        if (!academicPeriodRepository.existsById(periodId)) {
            throw new AppException("Período académico no encontrado con ID: " + periodId);
        }

        // Si se está activando este período, desactivar otros
        if (academicPeriod.isActive()) {
            deactivateOtherAcademicPeriods();
        }

        academicPeriod.setPeriodId(periodId);
        return academicPeriodRepository.save(academicPeriod);
    }

    /**
     * Elimina un período académico.
     */
    public void deleteAcademicPeriod(String periodId) {
        log.info("Eliminando período académico con ID: {}", periodId);

        AcademicPeriod period = academicPeriodRepository.findById(periodId)
                .orElseThrow(() -> new AppException("Período académico no encontrado con ID: " + periodId));

        if (period.isActive()) {
            throw new AppException("No se puede eliminar un período académico activo");
        }

        academicPeriodRepository.deleteById(periodId);
    }

    /**
     * Activa un período académico y desactiva los demás.
     */
    public AcademicPeriod activateAcademicPeriod(String periodId) {
        log.info("Activando período académico con ID: {}", periodId);

        AcademicPeriod period = academicPeriodRepository.findById(periodId)
                .orElseThrow(() -> new AppException("Período académico no encontrado con ID: " + periodId));

        deactivateOtherAcademicPeriods();
        period.setActive(true);

        return academicPeriodRepository.save(period);
    }

    private void deactivateOtherAcademicPeriods() {
        log.debug("Desactivando otros períodos académicos activos");
        List<AcademicPeriod> activePeriods = academicPeriodRepository.findByIsActive(true);
        for (AcademicPeriod period : activePeriods) {
            period.setActive(false);
            academicPeriodRepository.save(period);
        }
    }

    // ========== CLASSROOM CRUD ==========

    /**
     * Crea un nuevo aula.
     */
    public Classroom createClassroom(Classroom classroom) {
        log.info("Creando nueva aula: {} {}", classroom.getBuilding(), classroom.getRoomNumber());
        return classroomRepository.save(classroom);
    }

    /**
     * Obtiene todas las aulas.
     */
    public List<Classroom> getAllClassrooms() {
        log.debug("Obteniendo todas las aulas");
        return classroomRepository.findAll();
    }

    /**
     * Obtiene un aula por ID.
     */
    public Classroom getClassroomById(String classroomId) {
        log.debug("Obteniendo aula con ID: {}", classroomId);
        return classroomRepository.findById(classroomId)
                .orElseThrow(() -> new AppException("Aula no encontrada con ID: " + classroomId));
    }

    /**
     * Actualiza un aula existente.
     */
    public Classroom updateClassroom(String classroomId, Classroom classroom) {
        log.info("Actualizando aula con ID: {}", classroomId);

        if (!classroomRepository.existsById(classroomId)) {
            throw new AppException("Aula no encontrada con ID: " + classroomId);
        }

        classroom.setClassroomId(classroomId);
        return classroomRepository.save(classroom);
    }

    /**
     * Elimina un aula.
     */
    public void deleteClassroom(String classroomId) {
        log.info("Eliminando aula con ID: {}", classroomId);

        if (!classroomRepository.existsById(classroomId)) {
            throw new AppException("Aula no encontrada con ID: " + classroomId);
        }

        // Verificar si el aula está siendo usada por algún grupo
        List<Group> groupsUsingClassroom = groupRepository.findByClassroom_ClassroomId(classroomId);
        if (!groupsUsingClassroom.isEmpty()) {
            throw new AppException("No se puede eliminar el aula porque está siendo utilizada por " +
                    groupsUsingClassroom.size() + " grupo(s)");
        }

        classroomRepository.deleteById(classroomId);
    }

    /**
     * Obtiene aulas por tipo (usando el enum RoomType).
     */
    public List<Classroom> getClassroomsByType(RoomType roomType) {
        log.debug("Obteniendo aulas por tipo: {}", roomType);
        return classroomRepository.findByRoomType(roomType);
    }

    /**
     * Obtiene aulas por tipo usando String (conversión automática a enum).
     */
    public List<Classroom> getClassroomsByType(String roomTypeString) {
        log.debug("Obteniendo aulas por tipo: {}", roomTypeString);
        try {
            RoomType roomType = RoomType.valueOf(roomTypeString.toUpperCase());
            return classroomRepository.findByRoomType(roomType);
        } catch (IllegalArgumentException e) {
            throw new AppException("Tipo de aula no válido: " + roomTypeString);
        }
    }

    /**
     * Obtiene aulas por edificio.
     */
    public List<Classroom> getClassroomsByBuilding(String building) {
        log.debug("Obteniendo aulas por edificio: {}", building);
        return classroomRepository.findByBuilding(building);
    }

    /**
     * Obtiene aulas con capacidad mayor o igual a la especificada.
     */
    public List<Classroom> getClassroomsWithMinCapacity(Integer minCapacity) {
        log.debug("Obteniendo aulas con capacidad mínima: {}", minCapacity);
        return classroomRepository.findByCapacityGreaterThanEqual(minCapacity);
    }

    // ========== COURSE CRUD ==========

    /**
     * Crea un nuevo curso.
     */
    public Course createCourse(Course course) {
        log.info("Creando nuevo curso: {} - {}", course.getCourseCode(), course.getName());

        if (courseRepository.existsById(course.getCourseCode())) {
            throw new AppException("Ya existe un curso con código: " + course.getCourseCode());
        }

        return courseRepository.save(course);
    }

    /**
     * Obtiene todos los cursos.
     */
    public List<Course> getAllCourses() {
        log.debug("Obteniendo todos los cursos");
        return courseRepository.findAll();
    }

    /**
     * Obtiene un curso por código.
     */
    public Course getCourseByCode(String courseCode) {
        log.debug("Obteniendo curso con código: {}", courseCode);
        return courseRepository.findById(courseCode)
                .orElseThrow(() -> new AppException("Curso no encontrado con código: " + courseCode));
    }

    /**
     * Actualiza un curso existente.
     */
    public Course updateCourse(String courseCode, Course course) {
        log.info("Actualizando curso con código: {}", courseCode);

        if (!courseRepository.existsById(courseCode)) {
            throw new AppException("Curso no encontrado con código: " + courseCode);
        }

        course.setCourseCode(courseCode);
        return courseRepository.save(course);
    }

    /**
     * Elimina un curso.
     */
    public void deleteCourse(String courseCode) {
        log.info("Eliminando curso con código: {}", courseCode);

        if (!courseRepository.existsById(courseCode)) {
            throw new AppException("Curso no encontrado con código: " + courseCode);
        }

        // Verificar si el curso está siendo usado por algún grupo
        List<Group> groupsUsingCourse = groupRepository.findByCourse_CourseCode(courseCode);
        if (!groupsUsingCourse.isEmpty()) {
            throw new AppException("No se puede eliminar el curso porque está siendo utilizado por " +
                    groupsUsingCourse.size() + " grupo(s)");
        }

        courseRepository.deleteById(courseCode);
    }

    /**
     * Obtiene cursos activos.
     */
    public List<Course> getActiveCourses() {
        log.debug("Obteniendo cursos activos");
        return courseRepository.findByIsActive(true);
    }

    /**
     * Obtiene cursos por programa académico.
     */
    public List<Course> getCoursesByProgram(String academicProgram) {
        log.debug("Obteniendo cursos por programa académico: {}", academicProgram);
        return courseRepository.findByAcademicProgram(academicProgram);
    }

    /**
     * Activa/desactiva un curso.
     */
    public Course toggleCourseStatus(String courseCode, boolean isActive) {
        log.info("{} curso con código: {}", isActive ? "Activando" : "Desactivando", courseCode);

        Course course = courseRepository.findById(courseCode)
                .orElseThrow(() -> new AppException("Curso no encontrado con código: " + courseCode));

        course.setIsActive(isActive);
        return courseRepository.save(course);
    }

    // ========== COURSE STATUS DETAIL CRUD ==========

    /**
     * Crea un nuevo detalle de estado de curso.
     */
    public CourseStatusDetail createCourseStatusDetail(CourseStatusDetail courseStatusDetail) {
        log.info("Creando nuevo detalle de estado de curso para estudiante: {}",
                courseStatusDetail.getStudentId());
        return courseStatusDetailRepository.save(courseStatusDetail);
    }

    /**
     * Obtiene todos los detalles de estado de curso.
     */
    public List<CourseStatusDetail> getAllCourseStatusDetails() {
        log.debug("Obteniendo todos los detalles de estado de curso");
        return courseStatusDetailRepository.findAll();
    }

    /**
     * Obtiene un detalle de estado de curso por ID.
     */
    public CourseStatusDetail getCourseStatusDetailById(String id) {
        log.debug("Obteniendo detalle de estado de curso con ID: {}", id);
        return courseStatusDetailRepository.findById(id)
                .orElseThrow(() -> new AppException("Detalle de estado de curso no encontrado con ID: " + id));
    }

    /**
     * Actualiza un detalle de estado de curso existente.
     */
    public CourseStatusDetail updateCourseStatusDetail(String id, CourseStatusDetail courseStatusDetail) {
        log.info("Actualizando detalle de estado de curso con ID: {}", id);

        if (!courseStatusDetailRepository.existsById(id)) {
            throw new AppException("Detalle de estado de curso no encontrado con ID: " + id);
        }

        courseStatusDetail.setId(id);
        return courseStatusDetailRepository.save(courseStatusDetail);
    }

    /**
     * Elimina un detalle de estado de curso.
     */
    public void deleteCourseStatusDetail(String id) {
        log.info("Eliminando detalle de estado de curso con ID: {}", id);

        if (!courseStatusDetailRepository.existsById(id)) {
            throw new AppException("Detalle de estado de curso no encontrado con ID: " + id);
        }

        courseStatusDetailRepository.deleteById(id);
    }

    /**
     * Obtiene detalles de estado de curso por estudiante.
     */
    public List<CourseStatusDetail> getCourseStatusDetailsByStudent(String studentId) {
        log.debug("Obteniendo detalles de estado de curso para estudiante: {}", studentId);
        return courseStatusDetailRepository.findByStudentId(studentId);
    }

    /**
     * Obtiene detalles de estado de curso por curso.
     */
    public List<CourseStatusDetail> getCourseStatusDetailsByCourse(String courseCode) {
        log.debug("Obteniendo detalles de estado de curso para curso: {}", courseCode);
        return courseStatusDetailRepository.findByCourse_CourseCode(courseCode);
    }

    /**
     * Obtiene detalles de estado de curso por semestre.
     */
    public List<CourseStatusDetail> getCourseStatusDetailsBySemester(String semester) {
        log.debug("Obteniendo detalles de estado de curso para semestre: {}", semester);
        return courseStatusDetailRepository.findBySemester(semester);
    }

    // ========== GROUP CRUD ==========

    /**
     * Elimina un grupo (completando el CRUD que faltaba en AdminGroupService).
     */
    public void deleteGroup(String groupId) {
        log.info("Eliminando grupo con ID: {}", groupId);

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new AppException("Grupo no encontrado con ID: " + groupId));

        // Verificar si el grupo está siendo usado en CourseStatusDetail
        List<CourseStatusDetail> courseStatusDetails = courseStatusDetailRepository.findByGroup_GroupId(groupId);
        if (!courseStatusDetails.isEmpty()) {
            throw new AppException("No se puede eliminar el grupo porque está siendo utilizado en " +
                    courseStatusDetails.size() + " registro(s) de estado de curso");
        }

        groupRepository.deleteById(groupId);
    }

    // ========== REVIEW STEP CRUD ==========

    /**
     * Crea un nuevo paso de revisión.
     */
    public ReviewStep createReviewStep(ReviewStep reviewStep) {
        log.info("Creando nuevo paso de revisión para usuario: {}", reviewStep.getUserId());
        return reviewStepRepository.save(reviewStep);
    }

    /**
     * Obtiene todos los pasos de revisión.
     */
    public List<ReviewStep> getAllReviewSteps() {
        log.debug("Obteniendo todos los pasos de revisión");
        return reviewStepRepository.findAll();
    }

    /**
     * Obtiene un paso de revisión por ID.
     */
    public ReviewStep getReviewStepById(String id) {
        log.debug("Obteniendo paso de revisión con ID: {}", id);
        return reviewStepRepository.findById(id)
                .orElseThrow(() -> new AppException("Paso de revisión no encontrado con ID: " + id));
    }

    /**
     * Obtiene pasos de revisión por usuario.
     */
    public List<ReviewStep> getReviewStepsByUser(String userId) {
        log.debug("Obteniendo pasos de revisión para usuario: {}", userId);
        return reviewStepRepository.findByUserId(userId);
    }

    /**
     * Obtiene pasos de revisión por rol de usuario (usando enum UserRole).
     */
    public List<ReviewStep> getReviewStepsByUserRole(UserRole userRole) {
        log.debug("Obteniendo pasos de revisión por rol: {}", userRole);
        return reviewStepRepository.findByUserRole(userRole);
    }

    /**
     * Obtiene pasos de revisión por rol de usuario (usando String).
     */
    public List<ReviewStep> getReviewStepsByUserRole(String userRoleString) {
        log.debug("Obteniendo pasos de revisión por rol: {}", userRoleString);
        try {
            UserRole userRole = UserRole.valueOf(userRoleString.toUpperCase());
            return reviewStepRepository.findByUserRole(userRole);
        } catch (IllegalArgumentException e) {
            throw new AppException("Rol de usuario no válido: " + userRoleString);
        }
    }

    // ========== SCHEDULE CRUD ==========

    /**
     * Crea un nuevo horario.
     */
    public Schedule createSchedule(Schedule schedule) {
        log.info("Creando nuevo horario: {} {} {}",
                schedule.getDayOfWeek(), schedule.getStartHour(), schedule.getEndHour());
        return scheduleRepository.save(schedule);
    }

    /**
     * Obtiene todos los horarios.
     */
    public List<Schedule> getAllSchedules() {
        log.debug("Obteniendo todos los horarios");
        return scheduleRepository.findAll();
    }

    /**
     * Obtiene un horario por ID.
     */
    public Schedule getScheduleById(String scheduleId) {
        log.debug("Obteniendo horario con ID: {}", scheduleId);
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new AppException("Horario no encontrado con ID: " + scheduleId));
    }

    /**
     * Actualiza un horario existente.
     */
    public Schedule updateSchedule(String scheduleId, Schedule schedule) {
        log.info("Actualizando horario con ID: {}", scheduleId);

        if (!scheduleRepository.existsById(scheduleId)) {
            throw new AppException("Horario no encontrado con ID: " + scheduleId);
        }

        schedule.setScheduleId(scheduleId);
        return scheduleRepository.save(schedule);
    }

    /**
     * Elimina un horario.
     */
    public void deleteSchedule(String scheduleId) {
        log.info("Eliminando horario con ID: {}", scheduleId);

        if (!scheduleRepository.existsById(scheduleId)) {
            throw new AppException("Horario no encontrado con ID: " + scheduleId);
        }

        // Verificar si el horario está siendo usado por algún grupo
        List<Group> groupsUsingSchedule = groupRepository.findBySchedule_ScheduleId(scheduleId);
        if (!groupsUsingSchedule.isEmpty()) {
            throw new AppException("No se puede eliminar el horario porque está siendo utilizado por " +
                    groupsUsingSchedule.size() + " grupo(s)");
        }

        scheduleRepository.deleteById(scheduleId);
    }

    /**
     * Obtiene horarios por día de la semana.
     */
    public List<Schedule> getSchedulesByDay(String dayOfWeek) {
        log.debug("Obteniendo horarios por día: {}", dayOfWeek);
        return scheduleRepository.findByDayOfWeek(dayOfWeek);
    }

    // ========== STUDENT ACADEMIC PROGRESS CRUD ==========

    /**
     * Crea un nuevo progreso académico de estudiante.
     */
    public StudentAcademicProgress createStudentAcademicProgress(StudentAcademicProgress progress) {
        log.info("Creando nuevo progreso académico para estudiante: {}",
                progress.getStudent().getId());
        return studentAcademicProgressRepository.save(progress);
    }

    /**
     * Obtiene todos los progresos académicos.
     */
    public List<StudentAcademicProgress> getAllStudentAcademicProgress() {
        log.debug("Obteniendo todos los progresos académicos");
        return studentAcademicProgressRepository.findAll();
    }

    /**
     * Obtiene un progreso académico por ID.
     */
    public StudentAcademicProgress getStudentAcademicProgressById(String id) {
        log.debug("Obteniendo progreso académico con ID: {}", id);
        return studentAcademicProgressRepository.findById(id)
                .orElseThrow(() -> new AppException("Progreso académico no encontrado con ID: " + id));
    }

    /**
     * Actualiza un progreso académico existente.
     */
    public StudentAcademicProgress updateStudentAcademicProgress(String id, StudentAcademicProgress progress) {
        log.info("Actualizando progreso académico con ID: {}", id);

        if (!studentAcademicProgressRepository.existsById(id)) {
            throw new AppException("Progreso académico no encontrado con ID: " + id);
        }

        progress.setId(id);
        return studentAcademicProgressRepository.save(progress);
    }

    /**
     * Elimina un progreso académico.
     */
    public void deleteStudentAcademicProgress(String id) {
        log.info("Eliminando progreso académico con ID: {}", id);

        if (!studentAcademicProgressRepository.existsById(id)) {
            throw new AppException("Progreso académico no encontrado con ID: " + id);
        }

        studentAcademicProgressRepository.deleteById(id);
    }

    /**
     * Obtiene progresos académicos por facultad.
     */
    public List<StudentAcademicProgress> getStudentAcademicProgressByFaculty(String faculty) {
        log.debug("Obteniendo progresos académicos por facultad: {}", faculty);
        return studentAcademicProgressRepository.findByFaculty(faculty);
    }

    /**
     * Obtiene progresos académicos por programa académico.
     */
    public List<StudentAcademicProgress> getStudentAcademicProgressByProgram(String academicProgram) {
        log.debug("Obteniendo progresos académicos por programa: {}", academicProgram);
        return studentAcademicProgressRepository.findByAcademicProgram(academicProgram);
    }

    /**
     * Obtiene el progreso académico de un estudiante específico.
     */
    public StudentAcademicProgress getStudentAcademicProgressByStudentId(String studentId) {
        log.debug("Obteniendo progreso académico para estudiante: {}", studentId);
        return studentAcademicProgressRepository.findByStudentId(studentId)
                .orElseThrow(() -> new AppException("Progreso académico no encontrado para el estudiante: " + studentId));
    }

    // ========== MÉTODOS DE UTILIDAD ==========

    /**
     * Verifica la integridad referencial antes de eliminar entidades.
     */
    public boolean checkReferentialIntegrity(String entityType, String entityId) {
        log.debug("Verificando integridad referencial para {} con ID: {}", entityType, entityId);

        switch (entityType.toLowerCase()) {
            case "classroom":
                return groupRepository.findByClassroom_ClassroomId(entityId).isEmpty();

            case "course":
                return groupRepository.findByCourse_CourseCode(entityId).isEmpty();

            case "schedule":
                return groupRepository.findBySchedule_ScheduleId(entityId).isEmpty();

            case "group":
                return courseStatusDetailRepository.findByGroup_GroupId(entityId).isEmpty();

            default:
                return true;
        }
    }

    /**
     * Obtiene estadísticas generales del sistema.
     */
    public SystemStatistics getSystemStatistics() {
        log.debug("Generando estadísticas del sistema");

        SystemStatistics stats = new SystemStatistics();
        stats.setTotalClassrooms(classroomRepository.count());
        stats.setTotalCourses(courseRepository.count());
        stats.setTotalGroups(groupRepository.count());
        stats.setTotalSchedules(scheduleRepository.count());
        stats.setTotalAcademicPeriods(academicPeriodRepository.count());

        // Contar períodos académicos activos usando el método existente
        List<AcademicPeriod> activePeriods = academicPeriodRepository.findByIsActive(true);
        stats.setActiveAcademicPeriods((long) activePeriods.size());

        return stats;
    }

    /**
     * Clase para contener estadísticas del sistema.
     */
    public static class SystemStatistics {
        private long totalClassrooms;
        private long totalCourses;
        private long totalGroups;
        private long totalSchedules;
        private long totalAcademicPeriods;
        private long activeAcademicPeriods;

        // Getters y Setters
        public long getTotalClassrooms() { return totalClassrooms; }
        public void setTotalClassrooms(long totalClassrooms) { this.totalClassrooms = totalClassrooms; }
        public long getTotalCourses() { return totalCourses; }
        public void setTotalCourses(long totalCourses) { this.totalCourses = totalCourses; }
        public long getTotalGroups() { return totalGroups; }
        public void setTotalGroups(long totalGroups) { this.totalGroups = totalGroups; }
        public long getTotalSchedules() { return totalSchedules; }
        public void setTotalSchedules(long totalSchedules) { this.totalSchedules = totalSchedules; }
        public long getTotalAcademicPeriods() { return totalAcademicPeriods; }
        public void setTotalAcademicPeriods(long totalAcademicPeriods) { this.totalAcademicPeriods = totalAcademicPeriods; }
        public long getActiveAcademicPeriods() { return activeAcademicPeriods; }
        public void setActiveAcademicPeriods(long activeAcademicPeriods) { this.activeAcademicPeriods = activeAcademicPeriods; }

        @Override
        public String toString() {
            return String.format(
                    "Estadísticas del Sistema:%n" +
                            "- Total de aulas: %d%n" +
                            "- Total de cursos: %d%n" +
                            "- Total de grupos: %d%n" +
                            "- Total de horarios: %d%n" +
                            "- Total de períodos académicos: %d%n" +
                            "- Períodos académicos activos: %d",
                    totalClassrooms, totalCourses, totalGroups, totalSchedules,
                    totalAcademicPeriods, activeAcademicPeriods
            );
        }
    }
}
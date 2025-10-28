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

    private final AcademicPeriodRepository academicPeriodRepository;
    private final ClassroomRepository classroomRepository;
    private final CourseRepository courseRepository;
    private final CourseStatusDetailRepository courseStatusDetailRepository;
    private final GroupRepository groupRepository;
    private final ReviewStepRepository reviewStepRepository;
    private final ScheduleRepository scheduleRepository;
    private final StudentAcademicProgressRepository studentAcademicProgressRepository;

    /**
     * Crea un nuevo período académico.
     *
     * @param academicPeriod Período académico a crear
     * @return Período académico creado
     * @throws AppException si ya existe un período con el mismo ID
     */
    public AcademicPeriod createAcademicPeriod(AcademicPeriod academicPeriod) {
        log.info("Creando nuevo período académico: {}", academicPeriod.getName());

        if (academicPeriodRepository.existsById(academicPeriod.getPeriodId())) {
            throw new AppException("Ya existe un período académico con ID: " + academicPeriod.getPeriodId());
        }

        if (academicPeriod.isActive()) {
            deactivateOtherAcademicPeriods();
        }

        return academicPeriodRepository.save(academicPeriod);
    }

    /**
     * Obtiene todos los períodos académicos.
     *
     * @return Lista de todos los períodos académicos
     */
    public List<AcademicPeriod> getAllAcademicPeriods() {
        log.debug("Obteniendo todos los períodos académicos");
        return academicPeriodRepository.findAll();
    }

    /**
     * Obtiene un período académico por ID.
     *
     * @param periodId ID del período académico
     * @return Período académico encontrado
     * @throws AppException si no se encuentra el período
     */
    public AcademicPeriod getAcademicPeriodById(String periodId) {
        log.debug("Obteniendo período académico con ID: {}", periodId);
        return academicPeriodRepository.findById(periodId)
                .orElseThrow(() -> new AppException("Período académico no encontrado con ID: " + periodId));
    }

    /**
     * Obtiene el período académico activo actual.
     *
     * @return Período académico activo
     * @throws AppException si no hay período activo
     */
    public AcademicPeriod getCurrentAcademicPeriod() {
        log.debug("Obteniendo período académico activo actual");
        return academicPeriodRepository.findByIsActiveTrue()
                .orElseThrow(() -> new AppException("No hay período académico activo"));
    }

    /**
     * Actualiza un período académico existente.
     *
     * @param periodId ID del período a actualizar
     * @param academicPeriod Nuevos datos del período
     * @return Período actualizado
     * @throws AppException si no se encuentra el período
     */
    public AcademicPeriod updateAcademicPeriod(String periodId, AcademicPeriod academicPeriod) {
        log.info("Actualizando período académico con ID: {}", periodId);

        if (!academicPeriodRepository.existsById(periodId)) {
            throw new AppException("Período académico no encontrado con ID: " + periodId);
        }

        if (academicPeriod.isActive()) {
            deactivateOtherAcademicPeriods();
        }

        academicPeriod.setPeriodId(periodId);
        return academicPeriodRepository.save(academicPeriod);
    }

    /**
     * Elimina un período académico.
     *
     * @param periodId ID del período a eliminar
     * @throws AppException si no se encuentra el período o está activo
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
     *
     * @param periodId ID del período a activar
     * @return Período activado
     * @throws AppException si no se encuentra el período
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

    /**
     * Crea un nuevo aula.
     *
     * @param classroom Aula a crear
     * @return Aula creada
     */
    public Classroom createClassroom(Classroom classroom) {
        log.info("Creando nueva aula: {} {}", classroom.getBuilding(), classroom.getRoomNumber());
        return classroomRepository.save(classroom);
    }

    /**
     * Obtiene todas las aulas.
     *
     * @return Lista de todas las aulas
     */
    public List<Classroom> getAllClassrooms() {
        log.debug("Obteniendo todas las aulas");
        return classroomRepository.findAll();
    }

    /**
     * Obtiene un aula por ID.
     *
     * @param classroomId ID del aula
     * @return Aula encontrada
     * @throws AppException si no se encuentra el aula
     */
    public Classroom getClassroomById(String classroomId) {
        log.debug("Obteniendo aula con ID: {}", classroomId);
        return classroomRepository.findById(classroomId)
                .orElseThrow(() -> new AppException("Aula no encontrada con ID: " + classroomId));
    }

    /**
     * Actualiza un aula existente.
     *
     * @param classroomId ID del aula a actualizar
     * @param classroom Nuevos datos del aula
     * @return Aula actualizada
     * @throws AppException si no se encuentra el aula
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
     *
     * @param classroomId ID del aula a eliminar
     * @throws AppException si no se encuentra el aula o está en uso
     */
    public void deleteClassroom(String classroomId) {
        log.info("Eliminando aula con ID: {}", classroomId);

        if (!classroomRepository.existsById(classroomId)) {
            throw new AppException("Aula no encontrada con ID: " + classroomId);
        }

        List<Group> groupsUsingClassroom = groupRepository.findByClassroom_ClassroomId(classroomId);
        if (!groupsUsingClassroom.isEmpty()) {
            throw new AppException("No se puede eliminar el aula porque está siendo utilizada por " +
                    groupsUsingClassroom.size() + " grupo(s)");
        }

        classroomRepository.deleteById(classroomId);
    }

    /**
     * Obtiene aulas por tipo.
     *
     * @param roomType Tipo de aula
     * @return Lista de aulas del tipo especificado
     */
    public List<Classroom> getClassroomsByType(RoomType roomType) {
        log.debug("Obteniendo aulas por tipo: {}", roomType);
        return classroomRepository.findByRoomType(roomType);
    }

    /**
     * Obtiene aulas por tipo usando String.
     *
     * @param roomTypeString Tipo de aula como String
     * @return Lista de aulas del tipo especificado
     * @throws AppException si el tipo no es válido
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
     *
     * @param building Edificio a buscar
     * @return Lista de aulas en el edificio
     */
    public List<Classroom> getClassroomsByBuilding(String building) {
        log.debug("Obteniendo aulas por edificio: {}", building);
        return classroomRepository.findByBuilding(building);
    }

    /**
     * Obtiene aulas con capacidad mayor o igual a la especificada.
     *
     * @param minCapacity Capacidad mínima
     * @return Lista de aulas con capacidad suficiente
     */
    public List<Classroom> getClassroomsWithMinCapacity(Integer minCapacity) {
        log.debug("Obteniendo aulas con capacidad mínima: {}", minCapacity);
        return classroomRepository.findByCapacityGreaterThanEqual(minCapacity);
    }

    /**
     * Crea un nuevo curso.
     *
     * @param course Curso a crear
     * @return Curso creado
     * @throws AppException si ya existe un curso con el mismo código
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
     *
     * @return Lista de todos los cursos
     */
    public List<Course> getAllCourses() {
        log.debug("Obteniendo todos los cursos");
        return courseRepository.findAll();
    }

    /**
     * Obtiene un curso por código.
     *
     * @param courseCode Código del curso
     * @return Curso encontrado
     * @throws AppException si no se encuentra el curso
     */
    public Course getCourseByCode(String courseCode) {
        log.debug("Obteniendo curso con código: {}", courseCode);
        return courseRepository.findById(courseCode)
                .orElseThrow(() -> new AppException("Curso no encontrado con código: " + courseCode));
    }

    /**
     * Actualiza un curso existente.
     *
     * @param courseCode Código del curso a actualizar
     * @param course Nuevos datos del curso
     * @return Curso actualizado
     * @throws AppException si no se encuentra el curso
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
     *
     * @param courseCode Código del curso a eliminar
     * @throws AppException si no se encuentra el curso o está en uso
     */
    public void deleteCourse(String courseCode) {
        log.info("Eliminando curso con código: {}", courseCode);

        if (!courseRepository.existsById(courseCode)) {
            throw new AppException("Curso no encontrado con código: " + courseCode);
        }

        List<Group> groupsUsingCourse = groupRepository.findByCourse_CourseCode(courseCode);
        if (!groupsUsingCourse.isEmpty()) {
            throw new AppException("No se puede eliminar el curso porque está siendo utilizado por " +
                    groupsUsingCourse.size() + " grupo(s)");
        }

        courseRepository.deleteById(courseCode);
    }

    /**
     * Obtiene cursos activos.
     *
     * @return Lista de cursos activos
     */
    public List<Course> getActiveCourses() {
        log.debug("Obteniendo cursos activos");
        return courseRepository.findByIsActive(true);
    }

    /**
     * Obtiene cursos por programa académico.
     *
     * @param academicProgram Programa académico
     * @return Lista de cursos del programa
     */
    public List<Course> getCoursesByProgram(String academicProgram) {
        log.debug("Obteniendo cursos por programa académico: {}", academicProgram);
        return courseRepository.findByAcademicProgram(academicProgram);
    }

    /**
     * Activa o desactiva un curso.
     *
     * @param courseCode Código del curso
     * @param isActive Estado a establecer
     * @return Curso actualizado
     * @throws AppException si no se encuentra el curso
     */
    public Course toggleCourseStatus(String courseCode, boolean isActive) {
        log.info("{} curso con código: {}", isActive ? "Activando" : "Desactivando", courseCode);

        Course course = courseRepository.findById(courseCode)
                .orElseThrow(() -> new AppException("Curso no encontrado con código: " + courseCode));

        course.setIsActive(isActive);
        return courseRepository.save(course);
    }

    /**
     * Crea un nuevo detalle de estado de curso.
     *
     * @param courseStatusDetail Detalle a crear
     * @return Detalle creado
     */
    public CourseStatusDetail createCourseStatusDetail(CourseStatusDetail courseStatusDetail) {
        log.info("Creando nuevo detalle de estado de curso para estudiante: {}",
                courseStatusDetail.getStudentId());
        return courseStatusDetailRepository.save(courseStatusDetail);
    }

    /**
     * Obtiene todos los detalles de estado de curso.
     *
     * @return Lista de todos los detalles
     */
    public List<CourseStatusDetail> getAllCourseStatusDetails() {
        log.debug("Obteniendo todos los detalles de estado de curso");
        return courseStatusDetailRepository.findAll();
    }

    /**
     * Obtiene un detalle de estado de curso por ID.
     *
     * @param id ID del detalle
     * @return Detalle encontrado
     * @throws AppException si no se encuentra el detalle
     */
    public CourseStatusDetail getCourseStatusDetailById(String id) {
        log.debug("Obteniendo detalle de estado de curso con ID: {}", id);
        return courseStatusDetailRepository.findById(id)
                .orElseThrow(() -> new AppException("Detalle de estado de curso no encontrado con ID: " + id));
    }

    /**
     * Actualiza un detalle de estado de curso existente.
     *
     * @param id ID del detalle a actualizar
     * @param courseStatusDetail Nuevos datos del detalle
     * @return Detalle actualizado
     * @throws AppException si no se encuentra el detalle
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
     *
     * @param id ID del detalle a eliminar
     * @throws AppException si no se encuentra el detalle
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
     *
     * @param studentId ID del estudiante
     * @return Lista de detalles del estudiante
     */
    public List<CourseStatusDetail> getCourseStatusDetailsByStudent(String studentId) {
        log.debug("Obteniendo detalles de estado de curso para estudiante: {}", studentId);
        return courseStatusDetailRepository.findByStudentId(studentId);
    }

    /**
     * Obtiene detalles de estado de curso por curso.
     *
     * @param courseCode Código del curso
     * @return Lista de detalles del curso
     */
    public List<CourseStatusDetail> getCourseStatusDetailsByCourse(String courseCode) {
        log.debug("Obteniendo detalles de estado de curso para curso: {}", courseCode);
        return courseStatusDetailRepository.findByCourse_CourseCode(courseCode);
    }

    /**
     * Obtiene detalles de estado de curso por semestre.
     *
     * @param semester Semestre
     * @return Lista de detalles del semestre
     */
    public List<CourseStatusDetail> getCourseStatusDetailsBySemester(String semester) {
        log.debug("Obteniendo detalles de estado de curso para semestre: {}", semester);
        return courseStatusDetailRepository.findBySemester(semester);
    }

    /**
     * Elimina un grupo.
     *
     * @param groupId ID del grupo a eliminar
     * @throws AppException si no se encuentra el grupo o está en uso
     */
    public void deleteGroup(String groupId) {
        log.info("Eliminando grupo con ID: {}", groupId);

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new AppException("Grupo no encontrado con ID: " + groupId));

        List<CourseStatusDetail> courseStatusDetails = courseStatusDetailRepository.findByGroup_GroupId(groupId);
        if (!courseStatusDetails.isEmpty()) {
            throw new AppException("No se puede eliminar el grupo porque está siendo utilizado en " +
                    courseStatusDetails.size() + " registro(s) de estado de curso");
        }

        groupRepository.deleteById(groupId);
    }

    /**
     * Crea un nuevo paso de revisión.
     *
     * @param reviewStep Paso de revisión a crear
     * @return Paso de revisión creado
     */
    public ReviewStep createReviewStep(ReviewStep reviewStep) {
        log.info("Creando nuevo paso de revisión para usuario: {}", reviewStep.getUserId());
        return reviewStepRepository.save(reviewStep);
    }

    /**
     * Obtiene todos los pasos de revisión.
     *
     * @return Lista de todos los pasos de revisión
     */
    public List<ReviewStep> getAllReviewSteps() {
        log.debug("Obteniendo todos los pasos de revisión");
        return reviewStepRepository.findAll();
    }

    /**
     * Obtiene un paso de revisión por ID.
     *
     * @param id ID del paso de revisión
     * @return Paso de revisión encontrado
     * @throws AppException si no se encuentra el paso
     */
    public ReviewStep getReviewStepById(String id) {
        log.debug("Obteniendo paso de revisión con ID: {}", id);
        return reviewStepRepository.findById(id)
                .orElseThrow(() -> new AppException("Paso de revisión no encontrado con ID: " + id));
    }

    /**
     * Obtiene pasos de revisión por usuario.
     *
     * @param userId ID del usuario
     * @return Lista de pasos del usuario
     */
    public List<ReviewStep> getReviewStepsByUser(String userId) {
        log.debug("Obteniendo pasos de revisión para usuario: {}", userId);
        return reviewStepRepository.findByUserId(userId);
    }

    /**
     * Obtiene pasos de revisión por rol de usuario.
     *
     * @param userRole Rol del usuario
     * @return Lista de pasos del rol
     */
    public List<ReviewStep> getReviewStepsByUserRole(UserRole userRole) {
        log.debug("Obteniendo pasos de revisión por rol: {}", userRole);
        return reviewStepRepository.findByUserRole(userRole);
    }

    /**
     * Obtiene pasos de revisión por rol de usuario usando String.
     *
     * @param userRoleString Rol del usuario como String
     * @return Lista de pasos del rol
     * @throws AppException si el rol no es válido
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

    /**
     * Crea un nuevo horario.
     *
     * @param schedule Horario a crear
     * @return Horario creado
     */
    public Schedule createSchedule(Schedule schedule) {
        log.info("Creando nuevo horario: {} {} {}",
                schedule.getDayOfWeek(), schedule.getStartHour(), schedule.getEndHour());
        return scheduleRepository.save(schedule);
    }

    /**
     * Obtiene todos los horarios.
     *
     * @return Lista de todos los horarios
     */
    public List<Schedule> getAllSchedules() {
        log.debug("Obteniendo todos los horarios");
        return scheduleRepository.findAll();
    }

    /**
     * Obtiene un horario por ID.
     *
     * @param scheduleId ID del horario
     * @return Horario encontrado
     * @throws AppException si no se encuentra el horario
     */
    public Schedule getScheduleById(String scheduleId) {
        log.debug("Obteniendo horario con ID: {}", scheduleId);
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new AppException("Horario no encontrado con ID: " + scheduleId));
    }

    /**
     * Actualiza un horario existente.
     *
     * @param scheduleId ID del horario a actualizar
     * @param schedule Nuevos datos del horario
     * @return Horario actualizado
     * @throws AppException si no se encuentra el horario
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
     *
     * @param scheduleId ID del horario a eliminar
     * @throws AppException si no se encuentra el horario o está en uso
     */
    public void deleteSchedule(String scheduleId) {
        log.info("Eliminando horario con ID: {}", scheduleId);

        if (!scheduleRepository.existsById(scheduleId)) {
            throw new AppException("Horario no encontrado con ID: " + scheduleId);
        }

        List<Group> groupsUsingSchedule = groupRepository.findBySchedule_ScheduleId(scheduleId);
        if (!groupsUsingSchedule.isEmpty()) {
            throw new AppException("No se puede eliminar el horario porque está siendo utilizado por " +
                    groupsUsingSchedule.size() + " grupo(s)");
        }

        scheduleRepository.deleteById(scheduleId);
    }

    /**
     * Obtiene horarios por día de la semana.
     *
     * @param dayOfWeek Día de la semana
     * @return Lista de horarios del día
     */
    public List<Schedule> getSchedulesByDay(String dayOfWeek) {
        log.debug("Obteniendo horarios por día: {}", dayOfWeek);
        return scheduleRepository.findByDayOfWeek(dayOfWeek);
    }

    /**
     * Crea un nuevo progreso académico de estudiante.
     *
     * @param progress Progreso académico a crear
     * @return Progreso académico creado
     */
    public StudentAcademicProgress createStudentAcademicProgress(StudentAcademicProgress progress) {
        log.info("Creando nuevo progreso académico para estudiante: {}",
                progress.getStudent().getId());
        return studentAcademicProgressRepository.save(progress);
    }

    /**
     * Obtiene todos los progresos académicos.
     *
     * @return Lista de todos los progresos académicos
     */
    public List<StudentAcademicProgress> getAllStudentAcademicProgress() {
        log.debug("Obteniendo todos los progresos académicos");
        return studentAcademicProgressRepository.findAll();
    }

    /**
     * Obtiene un progreso académico por ID.
     *
     * @param id ID del progreso académico
     * @return Progreso académico encontrado
     * @throws AppException si no se encuentra el progreso
     */
    public StudentAcademicProgress getStudentAcademicProgressById(String id) {
        log.debug("Obteniendo progreso académico con ID: {}", id);
        return studentAcademicProgressRepository.findById(id)
                .orElseThrow(() -> new AppException("Progreso académico no encontrado con ID: " + id));
    }

    /**
     * Actualiza un progreso académico existente.
     *
     * @param id ID del progreso a actualizar
     * @param progress Nuevos datos del progreso
     * @return Progreso actualizado
     * @throws AppException si no se encuentra el progreso
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
     *
     * @param id ID del progreso a eliminar
     * @throws AppException si no se encuentra el progreso
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
     *
     * @param faculty Facultad
     * @return Lista de progresos de la facultad
     */
    public List<StudentAcademicProgress> getStudentAcademicProgressByFaculty(String faculty) {
        log.debug("Obteniendo progresos académicos por facultad: {}", faculty);
        return studentAcademicProgressRepository.findByFaculty(faculty);
    }

    /**
     * Obtiene progresos académicos por programa académico.
     *
     * @param academicProgram Programa académico
     * @return Lista de progresos del programa
     */
    public List<StudentAcademicProgress> getStudentAcademicProgressByProgram(String academicProgram) {
        log.debug("Obteniendo progresos académicos por programa: {}", academicProgram);
        return studentAcademicProgressRepository.findByAcademicProgram(academicProgram);
    }

    /**
     * Obtiene el progreso académico de un estudiante específico.
     *
     * @param studentId ID del estudiante
     * @return Progreso académico del estudiante
     * @throws AppException si no se encuentra el progreso
     */
    public StudentAcademicProgress getStudentAcademicProgressByStudentId(String studentId) {
        log.debug("Obteniendo progreso académico para estudiante: {}", studentId);
        return studentAcademicProgressRepository.findByStudentId(studentId)
                .orElseThrow(() -> new AppException("Progreso académico no encontrado para el estudiante: " + studentId));
    }

    /**
     * Verifica la integridad referencial antes de eliminar entidades.
     *
     * @param entityType Tipo de entidad
     * @param entityId ID de la entidad
     * @return true si no hay referencias, false en caso contrario
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
     *
     * @return Estadísticas del sistema
     */
    public SystemStatistics getSystemStatistics() {
        log.debug("Generando estadísticas del sistema");

        SystemStatistics stats = new SystemStatistics();
        stats.setTotalClassrooms(classroomRepository.count());
        stats.setTotalCourses(courseRepository.count());
        stats.setTotalGroups(groupRepository.count());
        stats.setTotalSchedules(scheduleRepository.count());
        stats.setTotalAcademicPeriods(academicPeriodRepository.count());

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
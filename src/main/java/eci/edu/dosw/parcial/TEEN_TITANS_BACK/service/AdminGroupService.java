package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio administrativo para la gestión de grupos, cursos y asignaciones académicas.
 * <p>
 * Extiende {@link GroupService} e implementa las operaciones necesarias para la administración
 * de periodos académicos, cursos, grupos, profesores y estudiantes.
 * </p>
 */
@Service
public class AdminGroupService extends GroupService {

    @Autowired
    private AcademicPeriodRepository academicPeriodRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentAcademicProgressRepository studentAcademicProgressRepository;

    @Autowired
    private CourseStatusDetailRepository courseStatusDetailRepository;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    /**
     * Obtiene el curso asociado a un grupo.
     *
     * @param groupId identificador del grupo
     * @return el curso del grupo
     * @throws AppException si el grupo no existe
     */
    @Override
    public Course getCourse(String groupId) {
        return groupRepository.findById(groupId)
                .map(Group::getCourse)
                .orElseThrow(() -> new AppException("Grupo no encontrado con ID: " + groupId));
    }

    /**
     * Obtiene la capacidad máxima de un grupo según su aula asignada.
     *
     * @param groupId identificador del grupo
     * @return capacidad máxima del aula
     * @throws AppException si el grupo no existe
     */
    @Override
    public Integer getMaxCapacity(String groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new AppException("Grupo no encontrado con ID: " + groupId));

        if (group.getClassroom() == null) {
            throw new AppException("El grupo no tiene aula asignada: " + groupId);
        }

        return group.getClassroom().getCapacity();
    }

    /**
     * Obtiene la cantidad actual estimada de estudiantes inscritos en un grupo.
     *
     * @param groupId identificador del grupo
     * @return número estimado de estudiantes inscritos
     * @throws AppException si el grupo no existe
     */
    @Override
    public Integer getCurrentEnrollment(String groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new AppException("Grupo no encontrado con ID: " + groupId));

        if (group.getClassroom() == null) {
            throw new AppException("El grupo no tiene aula asignada: " + groupId);
        }

        return (int) (group.getClassroom().getCapacity() * 0.6);
    }

    /**
     * Retorna la lista de solicitudes de cambio de horario pendientes para un grupo.
     *
     * @param groupId identificador del grupo
     * @return lista vacía por defecto
     */
    @Override
    public List<ScheduleChangeRequest> getWaitingList(String groupId) {
        // Verificar que el grupo existe
        if (!groupRepository.existsById(groupId)) {
            throw new AppException("Grupo no encontrado con ID: " + groupId);
        }
        return Collections.emptyList();
    }

    /**
     * Obtiene el total de estudiantes inscritos por grupo dentro de un curso.
     *
     * @param courseCode código del curso
     * @return mapa con los IDs de grupo y el número de inscritos
     * @throws AppException si el curso no existe
     */
    @Override
    public Map<String, Integer> getTotalEnrolledByCourse(String courseCode) {
        if (!courseRepository.existsById(courseCode)) {
            throw new AppException("Curso no encontrado con código: " + courseCode);
        }

        return groupRepository.findByCourse_CourseCode(courseCode).stream()
                .collect(Collectors.toMap(
                        Group::getGroupId,
                        group -> getCurrentEnrollment(group.getGroupId())
                ));
    }

    /**
     * Configura y guarda un nuevo periodo académico.
     *
     * @param period objeto {@link AcademicPeriod} a registrar
     */
    public void configureAcademicPeriod(AcademicPeriod period) {
        academicPeriodRepository.save(period);
    }

    /**
     * Actualiza la capacidad de las aulas de todos los grupos de un curso.
     *
     * @param courseCode código del curso
     * @param newCapacity nueva capacidad del aula
     * @throws AppException si el curso no existe
     */
    public void updateCourseCapacity(String courseCode, int newCapacity) {
        if (!courseRepository.existsById(courseCode)) {
            throw new AppException("Curso no encontrado con código: " + courseCode);
        }

        List<Group> groups = groupRepository.findByCourse_CourseCode(courseCode);
        if (groups.isEmpty()) {
            throw new AppException("No se encontraron grupos para el curso: " + courseCode);
        }

        groups.forEach(group -> {
            if (group.getClassroom() != null) {
                Classroom currentClassroom = group.getClassroom();
                Classroom updatedClassroom = new Classroom(
                        currentClassroom.getClassroomId(),
                        currentClassroom.getBuilding(),
                        currentClassroom.getRoomNumber(),
                        newCapacity,
                        currentClassroom.getRoomType()
                );
                classroomRepository.save(updatedClassroom);
            }
        });
    }

    /**
     * Crea un nuevo curso.
     *
     * @param course objeto {@link Course} a registrar
     * @return el curso creado
     */
    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    /**
     * Actualiza los datos de un curso existente.
     *
     * @param courseCode código del curso
     * @param course objeto {@link Course} actualizado
     * @return el curso actualizado
     * @throws AppException si el curso no existe
     */
    public Course updateCourse(String courseCode, Course course) {
        if (!courseRepository.existsById(courseCode)) {
            throw new AppException("Curso no encontrado con código: " + courseCode);
        }
        course.setCourseCode(courseCode);
        return courseRepository.save(course);
    }

    /**
     * Elimina un curso por su código.
     *
     * @param courseCode código del curso
     * @throws AppException si el curso no existe
     */
    public void deleteCourse(String courseCode) {
        if (!courseRepository.existsById(courseCode)) {
            throw new AppException("Curso no encontrado con código: " + courseCode);
        }
        courseRepository.deleteById(courseCode);
    }

    /**
     * Crea un nuevo grupo.
     *
     * @param group objeto {@link Group} a registrar
     * @return el grupo creado
     */
    public Group createGroup(Group group) {
        return groupRepository.save(group);
    }

    /**
     * Actualiza los datos de un grupo existente.
     *
     * @param groupId identificador del grupo
     * @param updatedGroup grupo actualizado
     * @return grupo actualizado
     * @throws AppException si el grupo no existe
     */
    public Group updateGroup(String groupId, Group updatedGroup) {
        if (!groupRepository.existsById(groupId)) {
            throw new AppException("Grupo no encontrado con ID: " + groupId);
        }

        // Crear NUEVA instancia usando el constructor de Group
        Group groupToSave = new Group(
                groupId,                    // ID específico para la actualización
                updatedGroup.getSection(),
                updatedGroup.getCourse(),
                updatedGroup.getProfessor(),
                updatedGroup.getSchedule(),
                updatedGroup.getClassroom()
        );

        return groupRepository.save(groupToSave);
    }

    /**
     * Elimina un grupo.
     *
     * @param groupId identificador del grupo
     * @throws AppException si el grupo no existe
     */
    public void deleteGroup(String groupId) {
        if (!groupRepository.existsById(groupId)) {
            throw new AppException("Grupo no encontrado con ID: " + groupId);
        }
        groupRepository.deleteById(groupId);
    }

    /**
     * Asigna un profesor a un grupo.
     *
     * @param professorId identificador del profesor
     * @param groupId identificador del grupo
     * @return grupo actualizado con el profesor asignado
     * @throws AppException si el grupo o profesor no existen
     */
    public Group assignProfessorToGroup(String professorId, String groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new AppException("Grupo no encontrado con ID: " + groupId));

        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new AppException("Profesor no encontrado con ID: " + professorId));

        Group updatedGroup = new Group(
                group.getGroupId(),
                group.getSection(),
                group.getCourse(),
                professor,
                group.getSchedule(),
                group.getClassroom()
        );
        return groupRepository.save(updatedGroup);
    }

    /**
     * Elimina la asignación de profesor de un grupo.
     *
     * @param groupId identificador del grupo
     * @return grupo actualizado sin profesor
     * @throws AppException si el grupo no existe
     */
    public Group removeProfessorFromGroup(String groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new AppException("Grupo no encontrado con ID: " + groupId));

        Group updatedGroup = new Group(
                group.getGroupId(),
                group.getSection(),
                group.getCourse(),
                null,
                group.getSchedule(),
                group.getClassroom()
        );
        return groupRepository.save(updatedGroup);
    }

    /**
     * Obtiene los grupos que se dictan en un día específico.
     *
     * @param dayOfWeek día de la semana
     * @return lista de grupos que tienen clase ese día
     */
    public List<Group> getGroupsByDay(String dayOfWeek) {
        return groupRepository.findAll().stream()
                .filter(group -> group.getSchedule() != null)
                .filter(group -> dayOfWeek.equals(group.getSchedule().getDayOfWeek()))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todos los grupos existentes.
     *
     * @return lista de grupos
     */
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    /**
     * Obtiene los grupos más solicitados según la cantidad de inscritos.
     *
     * @return lista con los 10 grupos más solicitados
     */
    public List<Group> getMostRequestedGroups() {
        return groupRepository.findAll().stream()
                .sorted((g1, g2) -> Integer.compare(
                        getCurrentEnrollment(g2.getGroupId()),
                        getCurrentEnrollment(g1.getGroupId())
                ))
                .limit(10)
                .collect(Collectors.toList());
    }

    /**
     * Asigna un estudiante a un grupo.
     *
     * @param studentId identificador del estudiante
     * @param groupId identificador del grupo
     * @throws AppException si el estudiante o grupo no existen
     */
    public void assignStudentToGroup(String studentId, String groupId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new AppException("Estudiante no encontrado con ID: " + studentId));

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new AppException("Grupo no encontrado con ID: " + groupId));

        // Aquí iría la lógica real de asignación
        // Por ahora solo verificamos que existan
    }

    /**
     * Remueve un estudiante de un grupo.
     *
     * @param studentId identificador del estudiante
     * @param groupId identificador del grupo
     * @throws AppException si el estudiante o grupo no existen
     */
    public void removeStudentFromGroup(String studentId, String groupId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new AppException("Estudiante no encontrado con ID: " + studentId));

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new AppException("Grupo no encontrado con ID: " + groupId));

        // Aquí iría la lógica real de remoción
        // Por ahora solo verificamos que existan
    }

    /**
     * Asigna un curso a un estudiante y actualiza su progreso académico.
     *
     * @param studentId identificador del estudiante
     * @param courseCode código del curso
     * @throws AppException si no se encuentra estudiante o curso
     */
    public void assignCourseToStudent(String studentId, String courseCode) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new AppException("Estudiante no encontrado con ID: " + studentId));

        Course course = courseRepository.findById(courseCode)
                .orElseThrow(() -> new AppException("Curso no encontrado con código: " + courseCode));

        CourseStatusDetail newCourseStatus = new CourseStatusDetail(
                UUID.randomUUID().toString(),
                course,
                CourseStatus.ENROLLED,
                null,
                "2025-1",
                new Date(),
                null,
                null,
                null,
                course.getCredits(),
                null,
                "Asignado por administrador"
        );

        courseStatusDetailRepository.save(newCourseStatus);

        Optional<StudentAcademicProgress> progressOpt = studentAcademicProgressRepository.findById(studentId);
        if (progressOpt.isPresent()) {
            StudentAcademicProgress progress = progressOpt.get();
            List<CourseStatusDetail> coursesStatus = new ArrayList<>(progress.getCoursesStatus());
            coursesStatus.add(newCourseStatus);

            StudentAcademicProgress updatedProgress = new StudentAcademicProgress(
                    progress.getId(),
                    progress.getStudent(),
                    progress.getAcademicProgram(),
                    progress.getFaculty(),
                    progress.getCurriculumType(),
                    progress.getCurrentSemester(),
                    progress.getTotalSemesters(),
                    progress.getCompletedCredits(),
                    progress.getTotalCreditsRequired(),
                    progress.getCumulativeGPA(),
                    coursesStatus
            );

            studentAcademicProgressRepository.save(updatedProgress);
        } else {
            throw new AppException("No se encontró progreso académico para el estudiante: " + studentId);
        }
    }

    /**
     * Elimina un curso del historial académico de un estudiante.
     *
     * @param studentId identificador del estudiante
     * @param courseCode código del curso
     * @throws AppException si no se encuentra el progreso académico del estudiante
     */
    public void removeCourseFromStudent(String studentId, String courseCode) {
        StudentAcademicProgress progress = studentAcademicProgressRepository.findById(studentId)
                .orElseThrow(() -> new AppException("Progreso académico no encontrado para el estudiante: " + studentId));

        List<CourseStatusDetail> updatedCourses = progress.getCoursesStatus().stream()
                .filter(courseStatus -> !courseCode.equals(courseStatus.getCourse().getCourseCode()))
                .collect(Collectors.toList());

        StudentAcademicProgress updatedProgress = new StudentAcademicProgress(
                progress.getId(),
                progress.getStudent(),
                progress.getAcademicProgram(),
                progress.getFaculty(),
                progress.getCurriculumType(),
                progress.getCurrentSemester(),
                progress.getTotalSemesters(),
                progress.getCompletedCredits(),
                progress.getTotalCreditsRequired(),
                progress.getCumulativeGPA(),
                updatedCourses
        );

        studentAcademicProgressRepository.save(updatedProgress);
    }
}
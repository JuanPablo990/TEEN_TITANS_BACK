package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

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
     * @return el curso del grupo, o {@code null} si no existe
     */
    @Override
    public Course getCourse(String groupId) {
        return groupRepository.findById(groupId)
                .map(Group::getCourse)
                .orElse(null);
    }

    /**
     * Obtiene la capacidad máxima de un grupo según su aula asignada.
     *
     * @param groupId identificador del grupo
     * @return capacidad máxima del aula o 0 si no se encuentra
     */
    @Override
    public Integer getMaxCapacity(String groupId) {
        return groupRepository.findById(groupId)
                .map(Group::getClassroom)
                .map(Classroom::getCapacity)
                .orElse(0);
    }

    /**
     * Obtiene la cantidad actual estimada de estudiantes inscritos en un grupo.
     *
     * @param groupId identificador del grupo
     * @return número estimado de estudiantes inscritos
     */
    @Override
    public Integer getCurrentEnrollment(String groupId) {
        return groupRepository.findById(groupId)
                .map(group -> (int) (group.getClassroom().getCapacity() * 0.6))
                .orElse(0);
    }

    /**
     * Retorna la lista de solicitudes de cambio de horario pendientes para un grupo.
     *
     * @param groupId identificador del grupo
     * @return lista vacía por defecto
     */
    @Override
    public List<ScheduleChangeRequest> getWaitingList(String groupId) {
        return Collections.emptyList();
    }

    /**
     * Obtiene el total de estudiantes inscritos por grupo dentro de un curso.
     *
     * @param courseCode código del curso
     * @return mapa con los IDs de grupo y el número de inscritos
     */
    @Override
    public Map<String, Integer> getTotalEnrolledByCourse(String courseCode) {
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
     */
    public void updateCourseCapacity(String courseCode, int newCapacity) {
        groupRepository.findByCourse_CourseCode(courseCode).forEach(group -> {
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
     */
    public Course updateCourse(String courseCode, Course course) {
        return courseRepository.save(course);
    }

    /**
     * Elimina un curso por su código.
     *
     * @param courseCode código del curso
     * @return {@code true} si fue eliminado, {@code false} si no existe
     */
    public boolean deleteCourse(String courseCode) {
        if (courseRepository.existsById(courseCode)) {
            courseRepository.deleteById(courseCode);
            return true;
        }
        return false;
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
     */
    public Group updateGroup(String groupId, Group updatedGroup) {
        return groupRepository.save(updatedGroup);
    }

    /**
     * Elimina un grupo.
     *
     * @param groupId identificador del grupo
     * @return {@code true} si fue eliminado, {@code false} si no existe
     */
    public boolean deleteGroup(String groupId) {
        if (groupRepository.existsById(groupId)) {
            groupRepository.deleteById(groupId);
            return true;
        }
        return false;
    }

    /**
     * Asigna un profesor a un grupo.
     *
     * @param professorId identificador del profesor
     * @param groupId identificador del grupo
     * @return grupo actualizado con el profesor asignado, o {@code null} si no se encontró
     */
    public Group assignProfessorToGroup(String professorId, String groupId) {
        Optional<Group> groupOpt = groupRepository.findById(groupId);
        Optional<Professor> professorOpt = professorRepository.findById(professorId);

        if (groupOpt.isPresent() && professorOpt.isPresent()) {
            Group group = groupOpt.get();
            Professor professor = professorOpt.get();

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
        return null;
    }

    /**
     * Elimina la asignación de profesor de un grupo.
     *
     * @param groupId identificador del grupo
     * @return grupo actualizado sin profesor, o {@code null} si no existe
     */
    public Group removeProfessorFromGroup(String groupId) {
        return groupRepository.findById(groupId)
                .map(group -> {
                    Group updatedGroup = new Group(
                            group.getGroupId(),
                            group.getSection(),
                            group.getCourse(),
                            null,
                            group.getSchedule(),
                            group.getClassroom()
                    );
                    return groupRepository.save(updatedGroup);
                })
                .orElse(null);
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
     * @return {@code true} si la asignación fue exitosa, {@code false} en caso contrario
     */
    public boolean assignStudentToGroup(String studentId, String groupId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        Optional<Group> groupOpt = groupRepository.findById(groupId);
        return studentOpt.isPresent() && groupOpt.isPresent();
    }

    /**
     * Remueve un estudiante de un grupo.
     *
     * @param studentId identificador del estudiante
     * @param groupId identificador del grupo
     * @return {@code true} si la remoción fue exitosa, {@code false} en caso contrario
     */
    public boolean removeStudentFromGroup(String studentId, String groupId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        Optional<Group> groupOpt = groupRepository.findById(groupId);
        return studentOpt.isPresent() && groupOpt.isPresent();
    }

    /**
     * Asigna un curso a un estudiante y actualiza su progreso académico.
     *
     * @param studentId identificador del estudiante
     * @param courseCode código del curso
     * @return {@code true} si la asignación fue exitosa, {@code false} si no se encontró estudiante o curso
     */
    public boolean assignCourseToStudent(String studentId, String courseCode) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        Optional<Course> courseOpt = courseRepository.findById(courseCode);

        if (studentOpt.isPresent() && courseOpt.isPresent()) {
            Course course = courseOpt.get();

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
            }
            return true;
        }
        return false;
    }

    /**
     * Elimina un curso del historial académico de un estudiante.
     *
     * @param studentId identificador del estudiante
     * @param courseCode código del curso
     * @return {@code true} si la eliminación fue exitosa, {@code false} en caso contrario
     */
    public boolean removeCourseFromStudent(String studentId, String courseCode) {
        Optional<StudentAcademicProgress> progressOpt = studentAcademicProgressRepository.findById(studentId);

        if (progressOpt.isPresent()) {
            StudentAcademicProgress progress = progressOpt.get();
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
            return true;
        }
        return false;
    }
}

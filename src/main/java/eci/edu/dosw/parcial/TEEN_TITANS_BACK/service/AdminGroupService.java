package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio administrativo para la gestión de grupos, cursos y asignaciones académicas.
 * Extiende {@link GroupService} e implementa las operaciones necesarias para la administración
 * de grupos, cursos, aulas y profesores.
 *
 *  * @author Equipo Teen Titans
 *  * @version 1.0
 *  * @since 2025
 */
@Slf4j
@Service
public class AdminGroupService extends GroupService {

    private final CourseRepository courseRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final StudentAcademicProgressRepository studentAcademicProgressRepository;
    private final CourseStatusDetailRepository courseStatusDetailRepository;
    private final ClassroomRepository classroomRepository;
    private final ProfessorRepository professorRepository;

    /**
     * Constructor principal que inicializa las dependencias del servicio y el padre.
     *
     * @param groupRepository Repositorio de grupos
     * @param courseStatusDetailRepository Repositorio de detalles de estado de curso
     * @param scheduleChangeRequestRepository Repositorio de solicitudes de cambio de horario
     * @param courseRepository Repositorio de cursos
     * @param userRepository Repositorio de usuarios
     * @param studentRepository Repositorio de estudiantes
     * @param studentAcademicProgressRepository Repositorio de progreso académico
     * @param classroomRepository Repositorio de aulas
     * @param professorRepository Repositorio de profesores
     */
    public AdminGroupService(
            GroupRepository groupRepository,
            CourseStatusDetailRepository courseStatusDetailRepository,
            ScheduleChangeRequestRepository scheduleChangeRequestRepository,
            CourseRepository courseRepository,
            UserRepository userRepository,
            StudentRepository studentRepository,
            StudentAcademicProgressRepository studentAcademicProgressRepository,
            ClassroomRepository classroomRepository,
            ProfessorRepository professorRepository
    ) {
        super(groupRepository, courseStatusDetailRepository, scheduleChangeRequestRepository);
        this.courseRepository = courseRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.studentAcademicProgressRepository = studentAcademicProgressRepository;
        this.courseStatusDetailRepository = courseStatusDetailRepository;
        this.classroomRepository = classroomRepository;
        this.professorRepository = professorRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Course getCourse(String groupId) {
        return groupRepository.findById(groupId)
                .map(Group::getCourse)
                .orElseThrow(() -> new AppException("Grupo no encontrado con ID: " + groupId));
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    @Override
    public List<ScheduleChangeRequest> getWaitingList(String groupId) {
        if (!groupRepository.existsById(groupId)) {
            throw new AppException("Grupo no encontrado con ID: " + groupId);
        }
        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
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
     * Asigna un profesor a un grupo específico.
     *
     * @param groupId ID del grupo
     * @param professorId ID del profesor
     * @throws AppException si el grupo o profesor no existen
     */
    public void assignProfessorToGroup(String groupId, String professorId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new AppException("Grupo no encontrado: " + groupId));
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new AppException("Profesor no encontrado: " + professorId));

        group.setProfessor(professor);
        groupRepository.save(group);
        log.info("Profesor {} asignado al grupo {}", professorId, groupId);
    }

    /**
     * Asigna un aula a un grupo específico.
     *
     * @param groupId ID del grupo
     * @param classroomId ID del aula
     * @throws AppException si el grupo o aula no existen
     */
    public void assignClassroomToGroup(String groupId, String classroomId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new AppException("Grupo no encontrado: " + groupId));
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new AppException("Aula no encontrada: " + classroomId));

        group.setClassroom(classroom);
        groupRepository.save(group);
        log.info("Aula {} asignada al grupo {}", classroomId, groupId);
    }

    /**
     * Crea un nuevo grupo para un curso existente.
     *
     * @param group Grupo a registrar
     * @return Grupo registrado
     * @throws AppException si el curso especificado no existe
     */
    public Group createGroup(Group group) {
        if (group.getCourse() == null || !courseRepository.existsById(group.getCourse().getCourseCode())) {
            throw new AppException("El curso especificado no existe o es inválido.");
        }
        return groupRepository.save(group);
    }

    /**
     * Obtiene todos los grupos registrados en el sistema.
     *
     * @return Lista de todos los grupos
     */
    public List<Group> listAllGroups() {
        return groupRepository.findAll();
    }
}
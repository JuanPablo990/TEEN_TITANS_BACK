package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto.GroupDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AdminGroupService extends GroupService {

    private final CourseRepository courseRepository;
    private final GroupRepository groupRepository;
    private final ClassroomRepository classroomRepository;
    private final ProfessorRepository professorRepository;
    private final ScheduleRepository scheduleRepository;

    public AdminGroupService(
            GroupRepository groupRepository,
            CourseStatusDetailRepository courseStatusDetailRepository,
            ScheduleChangeRequestRepository scheduleChangeRequestRepository,
            CourseRepository courseRepository,
            ClassroomRepository classroomRepository,
            ProfessorRepository professorRepository,
            ScheduleRepository scheduleRepository
    ) {
        super(groupRepository, courseStatusDetailRepository, scheduleChangeRequestRepository);
        this.courseRepository = courseRepository;
        this.groupRepository = groupRepository;
        this.classroomRepository = classroomRepository;
        this.professorRepository = professorRepository;
        this.scheduleRepository = scheduleRepository;
    }

    // ... (todos los métodos existentes que ya tienes)

    /**
     * Crea un grupo a partir de un DTO con validaciones completas
     */
    public Group createGroupFromDTO(GroupDTO groupDTO) {
        log.info("Creando grupo desde DTO: {}", groupDTO.getGroupId());

        // Buscar todas las entidades referenciadas
        Course course = courseRepository.findById(groupDTO.getCourseId())
                .orElseThrow(() -> new AppException("Curso no encontrado: " + groupDTO.getCourseId()));

        Professor professor = professorRepository.findById(groupDTO.getProfessorId())
                .orElseThrow(() -> new AppException("Profesor no encontrado: " + groupDTO.getProfessorId()));

        Classroom classroom = classroomRepository.findById(groupDTO.getClassroomId())
                .orElseThrow(() -> new AppException("Aula no encontrada: " + groupDTO.getClassroomId()));

        Schedule schedule = scheduleRepository.findById(groupDTO.getScheduleId())
                .orElseThrow(() -> new AppException("Horario no encontrado: " + groupDTO.getScheduleId()));

        // Crear el grupo
        Group group = new Group(
                groupDTO.getGroupId(),
                groupDTO.getSection(),
                course,
                professor,
                schedule,
                classroom
        );

        return createGroup(group);
    }

    /**
     * Obtiene un grupo por su ID
     */
    public Group getGroupById(String groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new AppException("Grupo no encontrado: " + groupId));
    }

    /**
     * Elimina un grupo por su ID
     */
    public void deleteGroup(String groupId) {
        if (!groupRepository.existsById(groupId)) {
            throw new AppException("Grupo no encontrado: " + groupId);
        }

        // Verificar si hay estudiantes matriculados en el grupo
        int enrollmentCount = getCurrentEnrollment(groupId);
        if (enrollmentCount > 0) {
            throw new AppException("No se puede eliminar el grupo " + groupId +
                    " porque tiene " + enrollmentCount + " estudiantes matriculados");
        }

        // Verificar si hay solicitudes pendientes para el grupo
        List<ScheduleChangeRequest> waitingList = getWaitingList(groupId);
        if (!waitingList.isEmpty()) {
            throw new AppException("No se puede eliminar el grupo " + groupId +
                    " porque tiene " + waitingList.size() + " solicitudes pendientes");
        }

        groupRepository.deleteById(groupId);
        log.info("Grupo eliminado exitosamente: {}", groupId);
    }
}
package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Course;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.ScheduleChangeRequest;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.RequestStatus;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.CourseStatusDetailRepository;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.GroupRepository;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.ScheduleChangeRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de grupos académicos.
 * <p>
 * Esta clase proporciona métodos para consultar datos de capacidad,
 * matrículas y listas de espera de grupos académicos.
 * </p>
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Slf4j
@Service
@RequiredArgsConstructor

public class GroupService {

    private final GroupRepository groupRepository;
    private final CourseStatusDetailRepository courseStatusDetailRepository;
    private final ScheduleChangeRequestRepository scheduleChangeRequestRepository;

    /**
     * Obtiene el curso asociado a un grupo específico.
     *
     * @param groupId identificador único del grupo.
     * @return el objeto {@link Course} correspondiente al grupo, o {@code null} si no existe.
     */
    public Course getCourse(String groupId) {
        log.debug("Obteniendo curso para el grupo: {}", groupId);
        return groupRepository.findById(groupId)
                .map(group -> {
                    log.info("Curso encontrado para el grupo {}: {}", groupId, group.getCourse().getName());
                    return group.getCourse();
                })
                .orElseGet(() -> {
                    log.warn("No se encontró el grupo con ID: {}", groupId);
                    return null;
                });
    }

    /**
     * Obtiene la capacidad máxima del aula asignada al grupo.
     *
     * @param groupId identificador único del grupo.
     * @return la capacidad máxima del grupo, o {@code 0} si no se encuentra información.
     */
    public Integer getMaxCapacity(String groupId) {
        log.debug("Obteniendo capacidad máxima para el grupo: {}", groupId);
        return groupRepository.findById(groupId)
                .map(group -> {
                    Integer capacity = group.getClassroom().getCapacity();
                    log.info("Capacidad máxima del grupo {}: {}", groupId, capacity);
                    return capacity;
                })
                .orElseGet(() -> {
                    log.warn("No se encontró capacidad para el grupo con ID: {}", groupId);
                    return 0;
                });
    }

    /**
     * Calcula el número actual de estudiantes matriculados en el grupo.
     *
     * @param groupId identificador único del grupo.
     * @return el número actual de estudiantes inscritos en el grupo.
     */
    public Integer getCurrentEnrollment(String groupId) {
        log.debug("Calculando matrícula actual para el grupo: {}", groupId);
        int enrollmentCount = courseStatusDetailRepository.findByGroup_GroupId(groupId).size();
        log.info("Matrícula actual del grupo {}: {} estudiantes", groupId, enrollmentCount);
        return enrollmentCount;
    }

    /**
     * Devuelve la lista de solicitudes en espera asociadas al grupo.
     * <p>
     * Las solicitudes de cambio de horario representan estudiantes que desean
     * ingresar al grupo o modificar su horario actual.
     * </p>
     *
     * @param groupId identificador único del grupo.
     * @return una lista de {@link ScheduleChangeRequest} correspondientes a la lista de espera.
     */
    public List<ScheduleChangeRequest> getWaitingList(String groupId) {
        log.debug("Obteniendo lista de espera para el grupo: {}", groupId);

        // Obtener solicitudes pendientes que están relacionadas con este grupo
        // como grupo solicitado (estudiantes que quieren entrar a este grupo)
        List<ScheduleChangeRequest> waitingList = scheduleChangeRequestRepository
                .findByRequestedGroupId(groupId)
                .stream()
                .filter(request -> request.getStatus() == RequestStatus.PENDING)
                .collect(Collectors.toList());

        log.info("Lista de espera del grupo {}: {} solicitudes", groupId, waitingList.size());
        return waitingList;
    }

    /**
     * Obtiene un mapa con el total de estudiantes inscritos por grupo
     * dentro de un curso específico.
     * <p>
     * La clave del mapa corresponde al identificador del grupo, y el valor
     * al número de estudiantes actualmente matriculados en dicho grupo.
     * </p>
     *
     * @param courseCode código del curso académico.
     * @return un mapa con los identificadores de grupo y sus respectivos totales de inscripción.
     */
    public Map<String, Integer> getTotalEnrolledByCourse(String courseCode) {
        log.debug("Obteniendo total de inscritos por grupo para el curso: {}", courseCode);

        // Obtener todos los grupos del curso
        var groups = groupRepository.findByCourse_CourseCode(courseCode);

        // Crear mapa con el conteo de estudiantes por grupo
        Map<String, Integer> enrollmentMap = groups.stream()
                .collect(Collectors.toMap(
                        group -> group.getGroupId(), // CORREGIDO: usar getGroupId() en lugar de getId()
                        group -> courseStatusDetailRepository.findByGroup_GroupId(group.getGroupId()).size()
                ));

        log.info("Total de inscritos por grupo para el curso {}: {} grupos", courseCode, enrollmentMap.size());
        return enrollmentMap;
    }

    /**
     * Método alternativo para obtener la lista de espera incluyendo diferentes estados
     */
    public List<ScheduleChangeRequest> getExtendedWaitingList(String groupId) {
        log.debug("Obteniendo lista de espera extendida para el grupo: {}", groupId);

        // Incluir solicitudes PENDING y UNDER_REVIEW
        List<RequestStatus> waitingStatuses = List.of(RequestStatus.PENDING, RequestStatus.UNDER_REVIEW);

        List<ScheduleChangeRequest> waitingList = scheduleChangeRequestRepository
                .findByRequestedGroupId(groupId)
                .stream()
                .filter(request -> waitingStatuses.contains(request.getStatus()))
                .collect(Collectors.toList());

        log.info("Lista de espera extendida del grupo {}: {} solicitudes", groupId, waitingList.size());
        return waitingList;
    }

    /**
     * Obtiene las solicitudes de cambio para un estudiante específico en este grupo
     */
    public List<ScheduleChangeRequest> getStudentWaitingRequests(String groupId, String studentId) {
        log.debug("Obteniendo solicitudes de espera del estudiante {} para el grupo: {}", studentId, groupId);

        List<ScheduleChangeRequest> studentRequests = scheduleChangeRequestRepository
                .findByStudentIdAndStatusIn(studentId, List.of(RequestStatus.PENDING, RequestStatus.UNDER_REVIEW))
                .stream()
                .filter(request -> groupId.equals(request.getRequestedGroup().getGroupId())) // CORREGIDO: usar getGroupId()
                .collect(Collectors.toList());

        log.info("Solicitudes de espera del estudiante {} para el grupo {}: {}", studentId, groupId, studentRequests.size());
        return studentRequests;
    }
}
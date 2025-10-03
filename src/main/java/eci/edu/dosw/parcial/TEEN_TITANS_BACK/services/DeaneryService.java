package eci.edu.dosw.parcial.TEEN_TITANS_BACK.services;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dtos.DeaneryRequestDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Request;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Student;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Subject;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.RequestStatus;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.AcademicCycle;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio que gestiona solicitudes académicas dirigidas a decanatura,
 * incluyendo aprobación, rechazo, monitoreo de cupos y alertas.
 */
@Service
public class DeaneryService {

    private final List<Request> requests = new ArrayList<>();
    private final List<Student> students = new ArrayList<>();
    private final List<AcademicCycle> academicCycles = new ArrayList<>();

    /**
     * Obtiene todas las solicitudes registradas en una facultad.
     *
     * @param faculty Nombre de la facultad
     * @return Lista de solicitudes de la facultad
     */
    public List<DeaneryRequestDTO> getFacultyRequests(String faculty) {
        return requests.stream()
                .filter(request -> Optional.ofNullable(request.getStudent())
                        .map(Student::getCareer)
                        .filter(career -> career.equals(faculty))
                        .isPresent())
                .map(this::convertToDeaneryRequestDTO)
                .sorted(Comparator.comparing(DeaneryRequestDTO::getPriority).reversed()
                        .thenComparing(DeaneryRequestDTO::getCreationDate))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene la información académica de un estudiante.
     *
     * @param studentId Identificador del estudiante
     * @return Ciclo académico asociado al estudiante
     */
    public AcademicCycle getStudentAcademicInfo(String studentId) {
        return academicCycles.stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Información académica no encontrada para el estudiante: " + studentId));
    }

    /**
     * Obtiene los grupos disponibles asociados a una asignatura.
     *
     * @param subjectId Identificador de la asignatura
     * @return Lista de grupos disponibles
     */
    public List<Integer> getAvailableGroups(String subjectId) {
        return requests.stream()
                .filter(request -> request.getOriginalSubject() != null)
                .map(Request::getOriginalGroup)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Aprueba una solicitud específica de decanatura.
     *
     * @param requestId Identificador de la solicitud
     * @param comments Comentarios de resolución
     * @return Solicitud aprobada en forma de DTO
     */
    public DeaneryRequestDTO approveRequest(String requestId, String comments) {
        return requests.stream()
                .filter(request -> request.getId().equals(requestId))
                .findFirst()
                .map(request -> {
                    DeaneryRequestDTO dto = convertToDeaneryRequestDTO(request);
                    dto.setStatus(RequestStatus.APPROVED.name());
                    dto.setObservations(comments);
                    dto.setResolutionDate(new Date());
                    return dto;
                })
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada: " + requestId));
    }

    /**
     * Rechaza una solicitud específica de decanatura.
     *
     * @param requestId Identificador de la solicitud
     * @param reason Razón del rechazo
     * @return Solicitud rechazada en forma de DTO
     */
    public DeaneryRequestDTO rejectRequest(String requestId, String reason) {
        return requests.stream()
                .filter(request -> request.getId().equals(requestId))
                .findFirst()
                .map(request -> {
                    DeaneryRequestDTO dto = convertToDeaneryRequestDTO(request);
                    dto.setStatus(RequestStatus.REJECTED.name());
                    dto.setObservations(reason);
                    dto.setResolutionDate(new Date());
                    return dto;
                })
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada: " + requestId));
    }

    /**
     * Solicita información adicional sobre una solicitud existente.
     *
     * @param requestId Identificador de la solicitud
     * @param message Mensaje con la información solicitada
     * @return Solicitud actualizada en forma de DTO
     */
    public DeaneryRequestDTO requestAdditionalInfo(String requestId, String message) {
        return requests.stream()
                .filter(request -> request.getId().equals(requestId))
                .findFirst()
                .map(request -> {
                    DeaneryRequestDTO dto = convertToDeaneryRequestDTO(request);
                    String currentObservations = dto.getObservations() != null ?
                            dto.getObservations() + "\nSolicitud de información: " + message :
                            "Solicitud de información: " + message;
                    dto.setObservations(currentObservations);
                    return dto;
                })
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada: " + requestId));
    }

    /**
     * Obtiene las alertas de capacidad de grupo de una facultad.
     *
     * @param faculty Nombre de la facultad
     * @return Lista de solicitudes críticas
     */
    public List<DeaneryRequestDTO> getGroupCapacityAlerts(String faculty) {
        return requests.stream()
                .filter(request -> request.getStatus() == RequestStatus.PENDING)
                .filter(request -> request.getPriority() >= 8)
                .filter(request -> Optional.ofNullable(request.getStudent())
                        .map(Student::getCareer)
                        .filter(career -> career.equals(faculty))
                        .isPresent())
                .map(this::convertToDeaneryRequestDTO)
                .sorted(Comparator.comparing(DeaneryRequestDTO::getPriority).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Monitorea la capacidad de un grupo específico.
     *
     * @param groupId Identificador del grupo
     * @return Mapa con información del estado del grupo
     */
    public Map<String, Object> monitorGroupCapacity(String groupId) {
        long groupRequests = requests.stream()
                .filter(request -> String.valueOf(request.getOriginalGroup()).equals(groupId) ||
                        String.valueOf(request.getTargetGroup()).equals(groupId))
                .count();

        Map<String, Object> capacityStatus = new HashMap<>();
        capacityStatus.put("groupId", groupId);
        capacityStatus.put("activeRequests", groupRequests);
        capacityStatus.put("status", groupRequests > 5 ? "HIGH_DEMAND" : "NORMAL");
        capacityStatus.put("timestamp", new Date());
        return capacityStatus;
    }

    /**
     * Convierte una solicitud en un objeto DTO para decanatura.
     *
     * @param request Solicitud original
     * @return DTO de la solicitud
     */
    private DeaneryRequestDTO convertToDeaneryRequestDTO(Request request) {
        DeaneryRequestDTO dto = new DeaneryRequestDTO();
        dto.setId(request.getId());
        dto.setType(request.getType().name());
        dto.setStudent(request.getStudent());
        dto.setOriginalSubject(request.getOriginalSubject());
        dto.setTargetSubject(request.getTargetSubject());
        dto.setOriginalGroup(request.getOriginalGroup());
        dto.setTargetGroup(request.getTargetGroup());
        dto.setStatus(request.getStatus().name());
        dto.setPriority(request.getPriority());
        dto.setCreationDate(request.getCreationDate());
        dto.setResolutionDate(request.getSolveDate());
        dto.setObservations(request.getObservations());
        return dto;
    }

    /**
     * Agrega una nueva solicitud al sistema.
     *
     * @param request Solicitud a registrar
     */
    public void addRequest(Request request) {
        requests.add(request);
    }

    /**
     * Agrega un nuevo estudiante al sistema.
     *
     * @param student Estudiante a registrar
     */
    public void addStudent(Student student) {
        students.add(student);
    }

    /**
     * Agrega un ciclo académico al sistema.
     *
     * @param cycle Ciclo académico a registrar
     */
    public void addAcademicCycle(AcademicCycle cycle) {
        academicCycles.add(cycle);
    }
}

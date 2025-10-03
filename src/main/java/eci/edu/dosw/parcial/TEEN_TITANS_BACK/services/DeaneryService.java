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

@Service
public class DeaneryService {

    private final List<Request> requests = new ArrayList<>();
    private final List<Student> students = new ArrayList<>();
    private final List<AcademicCycle> academicCycles = new ArrayList<>();

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

    public AcademicCycle getStudentAcademicInfo(String studentId) {
        return academicCycles.stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Información académica no encontrada para el estudiante: " + studentId));
    }

    public List<Integer> getAvailableGroups(String subjectId) {
        return requests.stream()
                .filter(request -> request.getOriginalSubject() != null)
                .map(Request::getOriginalGroup)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

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

    public void addRequest(Request request) {
        requests.add(request);
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void addAcademicCycle(AcademicCycle cycle) {
        academicCycles.add(cycle);
    }
}
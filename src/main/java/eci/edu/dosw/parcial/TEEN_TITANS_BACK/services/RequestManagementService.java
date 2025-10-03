package eci.edu.dosw.parcial.TEEN_TITANS_BACK.services;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dtos.StudentRequestDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Request;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Student;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Subject;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.RequestType;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.RequestStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RequestManagementService {

    private final List<Request> requests = new ArrayList<>();

    public Request createRequestForStudent(StudentRequestDTO requestData) {
        Request request = new Request() {
            @Override public String getId() { return UUID.randomUUID().toString(); }
            @Override public RequestType getType() { return RequestType.valueOf(requestData.getRequestType()); }
            @Override public Student getStudent() { return requestData.getStudent(); }
            @Override public Subject getOriginalSubject() { return requestData.getOriginalSubject(); }
            @Override public Subject getTargetSubject() { return requestData.getTargetSubject(); }
            @Override public int getOriginalGroup() { return requestData.getOriginalGroup(); }
            @Override public int getTargetGroup() { return requestData.getTargetGroup(); }
            @Override public RequestStatus getStatus() { return RequestStatus.PENDING; }
            @Override public int getPriority() { return 5; }
            @Override public Date getCreationDate() { return new Date(); }
            @Override public Date getSolveDate() { return null; }
            @Override public String getObservations() { return requestData.getReason(); }
        };
        requests.add(request);
        return request;
    }

    public List<Request> getPendingRequests() {
        return requests.stream()
                .filter(request -> request.getStatus() == RequestStatus.PENDING)
                .sorted(Comparator.comparing(Request::getCreationDate))
                .collect(Collectors.toList());
    }

    public Request cancelRequest(String requestId) {
        return requests.stream()
                .filter(request -> request.getId().equals(requestId))
                .findFirst()
                .map(request -> createUpdatedRequest(request, RequestStatus.REJECTED, "Cancelado por el usuario"))
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
    }

    public Request approveRequest(String requestId, String comments) {
        return requests.stream()
                .filter(request -> request.getId().equals(requestId))
                .findFirst()
                .map(request -> createUpdatedRequest(request, RequestStatus.APPROVED, comments))
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
    }

    public Request rejectRequest(String requestId, String reason) {
        return requests.stream()
                .filter(request -> request.getId().equals(requestId))
                .findFirst()
                .map(request -> createUpdatedRequest(request, RequestStatus.REJECTED, reason))
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
    }

    public Request requestAdditionalInfo(String requestId, String message) {
        return requests.stream()
                .filter(request -> request.getId().equals(requestId))
                .findFirst()
                .map(request -> {
                    String newObservations = request.getObservations() + "\nSolicitud de info: " + message;
                    return createUpdatedRequest(request, request.getStatus(), newObservations);
                })
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
    }

    public List<Request> searchRequests(String searchTerm) {
        return requests.stream()
                .filter(request ->
                        request.getId().contains(searchTerm) ||
                                (request.getStudent() != null && request.getStudent().getId().contains(searchTerm)) ||
                                (request.getOriginalSubject() != null && request.getOriginalSubject().getName().contains(searchTerm)) ||
                                (request.getTargetSubject() != null && request.getTargetSubject().getName().contains(searchTerm))
                )
                .collect(Collectors.toList());
    }

    public List<Request> getRequestsByStatus(RequestStatus status) {
        return requests.stream()
                .filter(request -> request.getStatus() == status)
                .sorted(Comparator.comparing(Request::getCreationDate))
                .collect(Collectors.toList());
    }

    private Request createUpdatedRequest(Request original, RequestStatus newStatus, String newObservations) {
        return new Request() {
            @Override public String getId() { return original.getId(); }
            @Override public RequestType getType() { return original.getType(); }
            @Override public Student getStudent() { return original.getStudent(); }
            @Override public Subject getOriginalSubject() { return original.getOriginalSubject(); }
            @Override public Subject getTargetSubject() { return original.getTargetSubject(); }
            @Override public int getOriginalGroup() { return original.getOriginalGroup(); }
            @Override public int getTargetGroup() { return original.getTargetGroup(); }
            @Override public RequestStatus getStatus() { return newStatus; }
            @Override public int getPriority() { return original.getPriority(); }
            @Override public Date getCreationDate() { return original.getCreationDate(); }
            @Override public Date getSolveDate() { return newStatus != RequestStatus.PENDING ? new Date() : original.getSolveDate(); }
            @Override public String getObservations() { return newObservations; }
        };
    }
}
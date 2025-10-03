package eci.edu.dosw.parcial.TEEN_TITANS_BACK.services;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dtos.RequestDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Request;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.RequestStatus;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Student;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class RequestService {

    private final Map<String, Request> requests = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1L);

    public Request createChangeRequest(RequestDTO requestDTO) {
        String requestId = "REQ-" + idCounter.getAndIncrement();

        Request newRequest = new Request();
        try {
            java.lang.reflect.Field idField = Request.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(newRequest, requestId);

            java.lang.reflect.Field typeField = Request.class.getDeclaredField("type");
            typeField.setAccessible(true);
            typeField.set(newRequest, requestDTO.getType());

            java.lang.reflect.Field statusField = Request.class.getDeclaredField("status");
            statusField.setAccessible(true);
            statusField.set(newRequest, RequestStatus.PENDING);

            java.lang.reflect.Field creationDateField = Request.class.getDeclaredField("creationDate");
            creationDateField.setAccessible(true);
            creationDateField.set(newRequest, new Date());

        } catch (Exception e) {
            throw new RuntimeException("Error creating request", e);
        }

        requests.put(requestId, newRequest);
        return newRequest;
    }

    public RequestStatus getRequestStatus(String requestId) {
        Request request = requests.get(requestId);
        if (request == null) {
            throw new IllegalArgumentException("Request not found: " + requestId);
        }
        return request.getStatus();
    }

    public List<Request> getStudentRequests(String studentId) {
        return requests.values().stream()
                .filter(request -> request.getStudent() != null)
                .filter(request -> extractStudentId(request.getStudent()).equals(studentId))
                .collect(Collectors.toList());
    }

    public List<Request> getPendingRequests(String studentId) {
        return requests.values().stream()
                .filter(request -> request.getStudent() != null)
                .filter(request -> extractStudentId(request.getStudent()).equals(studentId))
                .filter(request -> request.getStatus() == RequestStatus.PENDING)
                .collect(Collectors.toList());
    }

    public List<Request> getRequestHistory(String studentId) {
        return requests.values().stream()
                .filter(request -> request.getStudent() != null)
                .filter(request -> extractStudentId(request.getStudent()).equals(studentId))
                .filter(request -> request.getStatus() != RequestStatus.PENDING)
                .collect(Collectors.toList());
    }

    public boolean cancelRequest(String requestId) {
        Request request = requests.get(requestId);
        if (request != null && request.getStatus() == RequestStatus.PENDING) {
            requests.remove(requestId);
            return true;
        }
        return false;
    }

    private String extractStudentId(Student student) {
        return student.getStandard() + "-" + student.getCareer();
    }
}
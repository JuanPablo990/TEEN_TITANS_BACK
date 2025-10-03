package eci.edu.dosw.parcial.TEEN_TITANS_BACK.services;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dtos.ChangeRequestData;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Request;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Subject;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Student;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.RequestType;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.RequestStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProcessingService {

    private final List<Request> requests = new ArrayList<>();

    public Request receiveStudentRequest(ChangeRequestData requestData) {
        Request request = createRequestFromData(requestData);
        requests.add(request);
        return request;
    }

    public Request assignAutomaticPriority(Request request) {
        int priority = calculatePriority(request.getType());
        Request processedRequest = createRequestWithPriority(request, priority);
        requests.remove(request);
        requests.add(processedRequest);
        return processedRequest;
    }

    public boolean routeRequestToFaculty(String requestId) {
        return requests.stream()
                .anyMatch(request -> request.getId().equals(requestId) &&
                        request.getStudent() != null);
    }

    public boolean validateScheduleOverlap(String requestId) {
        return requests.stream()
                .filter(request -> request.getId().equals(requestId))
                .findFirst()
                .map(request -> {
                    if (request.getOriginalSubject() == null || request.getTargetSubject() == null) {
                        return true;
                    }
                    return !request.getOriginalSubject().getClassTime().equals(request.getTargetSubject().getClassTime());
                })
                .orElse(false);
    }

    public boolean validateCapacity(String requestId) {
        return requests.stream()
                .filter(request -> request.getId().equals(requestId))
                .findFirst()
                .map(request -> {
                    if (request.getTargetSubject() == null) return true;
                    return request.getTargetSubject().getRegistered() < request.getTargetSubject().getQuotas();
                })
                .orElse(false);
    }

    public List<String> getRequestAuditTrail(String requestId) {
        return requests.stream()
                .filter(request -> request.getId().equals(requestId))
                .findFirst()
                .map(request -> Arrays.asList(
                        "Solicitud creada: " + request.getCreationDate(),
                        "Tipo: " + request.getType(),
                        "Prioridad: " + request.getPriority(),
                        "Estado: " + request.getStatus()
                ))
                .orElse(Collections.emptyList());
    }

    private Request createRequestFromData(ChangeRequestData requestData) {
        return new Request() {
            @Override
            public String getId() { return UUID.randomUUID().toString(); }

            @Override
            public RequestType getType() { return RequestType.valueOf(requestData.getRequestType()); }

            @Override
            public Student getStudent() { return requestData.getStudent(); }

            @Override
            public Subject getOriginalSubject() { return requestData.getOriginalSubject(); }

            @Override
            public Subject getTargetSubject() { return requestData.getTargetSubject(); }

            @Override
            public int getOriginalGroup() { return requestData.getOriginalGroup(); }

            @Override
            public int getTargetGroup() { return requestData.getTargetGroup(); }

            @Override
            public RequestStatus getStatus() { return RequestStatus.PENDING; }

            @Override
            public int getPriority() { return calculatePriority(RequestType.valueOf(requestData.getRequestType())); }

            @Override
            public Date getCreationDate() { return new Date(); }

            @Override
            public Date getSolveDate() { return null; }

            @Override
            public String getObservations() { return requestData.getReason(); }
        };
    }

    private Request createRequestWithPriority(Request original, int priority) {
        return new Request() {
            @Override
            public String getId() { return original.getId(); }

            @Override
            public RequestType getType() { return original.getType(); }

            @Override
            public Student getStudent() { return original.getStudent(); }

            @Override
            public Subject getOriginalSubject() { return original.getOriginalSubject(); }

            @Override
            public Subject getTargetSubject() { return original.getTargetSubject(); }

            @Override
            public int getOriginalGroup() { return original.getOriginalGroup(); }

            @Override
            public int getTargetGroup() { return original.getTargetGroup(); }

            @Override
            public RequestStatus getStatus() { return original.getStatus(); }

            @Override
            public int getPriority() { return priority; }

            @Override
            public Date getCreationDate() { return original.getCreationDate(); }

            @Override
            public Date getSolveDate() { return original.getSolveDate(); }

            @Override
            public String getObservations() { return original.getObservations(); }
        };
    }

    private int calculatePriority(RequestType type) {
        switch (type) {
            case REQUEST_FOR_EXCEPTIONAL_CIRCUMSTANCES: return 9;
            case REQUEST_FOR_LATE_REGISTRATION: return 7;
            case REQUEST_FOR_GROUP_CHANGE: return 5;
            case REQUEST_FOR_COURSE_CANCELATION: return 3;
            default: return 1;
        }
    }
}
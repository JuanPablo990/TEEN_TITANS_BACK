package eci.edu.dosw.parcial.TEEN_TITANS_BACK.services;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dtos.RequestDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Request;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.RequestStatus;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.RequestType;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class RequestServiceTest {

    private RequestService requestService;
    private RequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        requestService = new RequestService();

        requestDTO = new RequestDTO();
        requestDTO.setType(RequestType.REQUEST_FOR_GROUP_CHANGE);
    }

    @Test
    void testCreateChangeRequest() {
        Request result = requestService.createChangeRequest(requestDTO);

        assertNotNull(result);
    }

    @Test
    void testGetRequestStatusExists() {
        Request request = requestService.createChangeRequest(requestDTO);
        String requestId = request.getId();

        RequestStatus result = requestService.getRequestStatus(requestId);

        assertEquals(RequestStatus.PENDING, result);
    }

    @Test
    void testGetRequestStatusNotExists() {
        assertThrows(IllegalArgumentException.class, () -> {
            requestService.getRequestStatus("nonexistent");
        });
    }

    @Test
    void testGetStudentRequestsEmpty() {
        String studentId = "1-Engineering";

        var result = requestService.getStudentRequests(studentId);

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetPendingRequestsEmpty() {
        String studentId = "1-Engineering";

        var result = requestService.getPendingRequests(studentId);

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetRequestHistoryEmpty() {
        String studentId = "1-Engineering";

        var result = requestService.getRequestHistory(studentId);

        assertTrue(result.isEmpty());
    }

    @Test
    void testCancelRequestExists() {
        Request request = requestService.createChangeRequest(requestDTO);
        String requestId = request.getId();

        boolean result = requestService.cancelRequest(requestId);

        assertTrue(result);
    }

    @Test
    void testCancelRequestNotExists() {
        boolean result = requestService.cancelRequest("nonexistent");

        assertFalse(result);
    }

    @Test
    void testCreateMultipleRequests() {
        Request request1 = requestService.createChangeRequest(requestDTO);
        Request request2 = requestService.createChangeRequest(requestDTO);

        assertNotNull(request1);
        assertNotNull(request2);
        assertNotEquals(request1.getId(), request2.getId());
    }

    @Test
    void testGetRequestStatusAfterCreation() {
        Request request = requestService.createChangeRequest(requestDTO);

        RequestStatus status = requestService.getRequestStatus(request.getId());

        assertEquals(RequestStatus.PENDING, status);
    }

    @Test
    void testCancelRequestAlreadyProcessed() {
        Request request = requestService.createChangeRequest(requestDTO);
        String requestId = request.getId();

        boolean firstCancel = requestService.cancelRequest(requestId);
        boolean secondCancel = requestService.cancelRequest(requestId);

        assertTrue(firstCancel);
        assertFalse(secondCancel);
    }

    @Test
    void testGetStudentRequestsWithStudentData() {
        Request request = requestService.createChangeRequest(requestDTO);

        var result = requestService.getStudentRequests("1-Engineering");

        assertNotNull(result);
    }

    @Test
    void testGetPendingRequestsWithStudentData() {
        Request request = requestService.createChangeRequest(requestDTO);

        var result = requestService.getPendingRequests("1-Engineering");

        assertNotNull(result);
    }

    @Test
    void testGetRequestHistoryWithStudentData() {
        Request request = requestService.createChangeRequest(requestDTO);

        var result = requestService.getRequestHistory("1-Engineering");

        assertNotNull(result);
    }

    @Test
    void testRequestIdFormat() {
        Request request = requestService.createChangeRequest(requestDTO);

        assertNotNull(request.getId());
        assertTrue(request.getId().startsWith("REQ-"));
        assertTrue(request.getId().length() > 4);
    }

    @Test
    void testRequestCreationDate() {
        Request request = requestService.createChangeRequest(requestDTO);

        assertNotNull(request.getCreationDate());
    }

    @Test
    void testMultipleRequestsDifferentIds() {
        Request request1 = requestService.createChangeRequest(requestDTO);
        Request request2 = requestService.createChangeRequest(requestDTO);
        Request request3 = requestService.createChangeRequest(requestDTO);

        assertNotEquals(request1.getId(), request2.getId());
        assertNotEquals(request2.getId(), request3.getId());
        assertNotEquals(request1.getId(), request3.getId());
    }

    @Test
    void testRequestTypePreserved() {
        requestDTO.setType(RequestType.REQUEST_FOR_GROUP_CHANGE);
        Request request = requestService.createChangeRequest(requestDTO);

        assertEquals(RequestType.REQUEST_FOR_GROUP_CHANGE, request.getType());
    }

    @Test
    void testRequestInitialStatus() {
        Request request = requestService.createChangeRequest(requestDTO);

        assertEquals(RequestStatus.PENDING, request.getStatus());
    }

    @Test
    void testEmptyRequestServiceState() {
        assertEquals(0, requestService.getStudentRequests("any").size());
        assertEquals(0, requestService.getPendingRequests("any").size());
        assertEquals(0, requestService.getRequestHistory("any").size());
    }

}
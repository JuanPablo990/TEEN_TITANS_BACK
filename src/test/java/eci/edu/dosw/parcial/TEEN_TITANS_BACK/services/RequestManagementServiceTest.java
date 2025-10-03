package eci.edu.dosw.parcial.TEEN_TITANS_BACK.services;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dtos.StudentRequestDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Request;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.RequestStatus;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.RequestType;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class RequestManagementServiceTest {

    private RequestManagementService requestManagementService;
    private StudentRequestDTO studentRequestDTO;

    @BeforeEach
    void setUp() {
        requestManagementService = new RequestManagementService();

        studentRequestDTO = new StudentRequestDTO();
        studentRequestDTO.setRequestType("REQUEST_FOR_GROUP_CHANGE");
    }

    @Test
    void testCreateRequestForStudent() {
        Request result = requestManagementService.createRequestForStudent(studentRequestDTO);

        assertNotNull(result);
        assertEquals(RequestStatus.PENDING, result.getStatus());
        assertEquals(RequestType.REQUEST_FOR_GROUP_CHANGE, result.getType());
    }

    @Test
    void testGetPendingRequestsEmpty() {
        var result = requestManagementService.getPendingRequests();

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetPendingRequestsWithData() {
        requestManagementService.createRequestForStudent(studentRequestDTO);

        var result = requestManagementService.getPendingRequests();

        assertFalse(result.isEmpty());
        assertEquals(RequestStatus.PENDING, result.get(0).getStatus());
    }


    @Test
    void testCancelRequestNotExists() {
        assertThrows(RuntimeException.class, () -> {
            requestManagementService.cancelRequest("nonexistent");
        });
    }




    @Test
    void testSearchRequestsEmpty() {
        var result = requestManagementService.searchRequests("test");

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetRequestsByStatusEmpty() {
        var result = requestManagementService.getRequestsByStatus(RequestStatus.PENDING);

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetRequestsByStatusWithData() {
        requestManagementService.createRequestForStudent(studentRequestDTO);

        var result = requestManagementService.getRequestsByStatus(RequestStatus.PENDING);

        assertFalse(result.isEmpty());
        assertEquals(RequestStatus.PENDING, result.get(0).getStatus());
    }

    @Test
    void testCreateRequestForCourseCancelation() {
        studentRequestDTO.setRequestType("REQUEST_FOR_COURSE_CANCELATION");

        Request result = requestManagementService.createRequestForStudent(studentRequestDTO);

        assertEquals(RequestType.REQUEST_FOR_COURSE_CANCELATION, result.getType());
    }

    @Test
    void testCreateRequestForLateRegistration() {
        studentRequestDTO.setRequestType("REQUEST_FOR_LATE_REGISTRATION");

        Request result = requestManagementService.createRequestForStudent(studentRequestDTO);

        assertEquals(RequestType.REQUEST_FOR_LATE_REGISTRATION, result.getType());
    }
}
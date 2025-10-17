package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.StudentRequestService;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentRequestControllerTest {

    @Mock
    private StudentRequestService studentRequestService;

    @InjectMocks
    private StudentRequestController studentRequestController;

    private Student student;
    private ScheduleChangeRequest scheduleChangeRequest;
    private Group group;
    private String studentId;
    private String requestId;

    @BeforeEach
    void setUp() {
        studentId = "student123";
        requestId = "request456";

        student = new Student();
        student.setId(studentId);
        student.setName("John Doe");
        student.setEmail("john.doe@university.edu");

        // Usar el constructor de Group en lugar de setters
        group = new Group("group123", "Group A", null, null, null, null);

        scheduleChangeRequest = new ScheduleChangeRequest();
        scheduleChangeRequest.setRequestId(requestId);
        scheduleChangeRequest.setStudent(student);
        scheduleChangeRequest.setCurrentGroup(group);
        scheduleChangeRequest.setRequestedGroup(group);
        scheduleChangeRequest.setReason("Test reason");
        scheduleChangeRequest.setStatus(RequestStatus.PENDING);
    }

    @Test
    void getStudentApprovalRate_HappyPath() {
        double approvalRate = 85.5;
        when(studentRequestService.getStudentApprovalRate(studentId)).thenReturn(approvalRate);

        ResponseEntity<?> response = studentRequestController.getStudentApprovalRate(studentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentRequestService, times(1)).getStudentApprovalRate(studentId);
    }

    @Test
    void getStudentApprovalRate_ErrorPath_AppException() {
        when(studentRequestService.getStudentApprovalRate(studentId)).thenThrow(new AppException("Student not found"));

        ResponseEntity<?> response = studentRequestController.getStudentApprovalRate(studentId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentRequestService, times(1)).getStudentApprovalRate(studentId);
    }

    @Test
    void getStudentApprovalRate_ErrorPath_GeneralException() {
        when(studentRequestService.getStudentApprovalRate(studentId)).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = studentRequestController.getStudentApprovalRate(studentId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentRequestService, times(1)).getStudentApprovalRate(studentId);
    }

    @Test
    void getPendingRequestsStatus_HappyPath() {
        List<ScheduleChangeRequest> pendingRequests = Arrays.asList(scheduleChangeRequest);
        when(studentRequestService.getPendingRequestsStatus(studentId)).thenReturn(pendingRequests);

        ResponseEntity<?> response = studentRequestController.getPendingRequestsStatus(studentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentRequestService, times(1)).getPendingRequestsStatus(studentId);
    }

    @Test
    void getPendingRequestsStatus_ErrorPath_AppException() {
        when(studentRequestService.getPendingRequestsStatus(studentId)).thenThrow(new AppException("Student not found"));

        ResponseEntity<?> response = studentRequestController.getPendingRequestsStatus(studentId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentRequestService, times(1)).getPendingRequestsStatus(studentId);
    }

    @Test
    void getPendingRequestsStatus_ErrorPath_GeneralException() {
        when(studentRequestService.getPendingRequestsStatus(studentId)).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = studentRequestController.getPendingRequestsStatus(studentId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentRequestService, times(1)).getPendingRequestsStatus(studentId);
    }

    @Test
    void getRequestPriorityPosition_HappyPath() {
        int position = 3;
        when(studentRequestService.getRequestPriorityPosition(requestId)).thenReturn(position);

        ResponseEntity<?> response = studentRequestController.getRequestPriorityPosition(requestId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentRequestService, times(1)).getRequestPriorityPosition(requestId);
    }

    @Test
    void getRequestPriorityPosition_ErrorPath_AppException() {
        when(studentRequestService.getRequestPriorityPosition(requestId)).thenThrow(new AppException("Request not found"));

        ResponseEntity<?> response = studentRequestController.getRequestPriorityPosition(requestId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentRequestService, times(1)).getRequestPriorityPosition(requestId);
    }

    @Test
    void getRequestPriorityPosition_ErrorPath_GeneralException() {
        when(studentRequestService.getRequestPriorityPosition(requestId)).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = studentRequestController.getRequestPriorityPosition(requestId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentRequestService, times(1)).getRequestPriorityPosition(requestId);
    }

    @Test
    void getRecommendedAlternativeGroups_HappyPath() {
        List<Group> alternativeGroups = Arrays.asList(group);
        when(studentRequestService.getRecommendedAlternativeGroups(requestId)).thenReturn(alternativeGroups);

        ResponseEntity<?> response = studentRequestController.getRecommendedAlternativeGroups(requestId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentRequestService, times(1)).getRecommendedAlternativeGroups(requestId);
    }

    @Test
    void getRecommendedAlternativeGroups_ErrorPath_AppException() {
        when(studentRequestService.getRecommendedAlternativeGroups(requestId)).thenThrow(new AppException("Request not found"));

        ResponseEntity<?> response = studentRequestController.getRecommendedAlternativeGroups(requestId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentRequestService, times(1)).getRecommendedAlternativeGroups(requestId);
    }

    @Test
    void getRecommendedAlternativeGroups_ErrorPath_GeneralException() {
        when(studentRequestService.getRecommendedAlternativeGroups(requestId)).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = studentRequestController.getRecommendedAlternativeGroups(requestId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentRequestService, times(1)).getRecommendedAlternativeGroups(requestId);
    }

    @Test
    void getRequestEstimatedWaitTime_HappyPath() {
        String waitTime = "2 days";
        when(studentRequestService.getRequestEstimatedWaitTime(requestId)).thenReturn(waitTime);

        ResponseEntity<?> response = studentRequestController.getRequestEstimatedWaitTime(requestId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentRequestService, times(1)).getRequestEstimatedWaitTime(requestId);
    }

    @Test
    void getRequestEstimatedWaitTime_ErrorPath_AppException() {
        when(studentRequestService.getRequestEstimatedWaitTime(requestId)).thenThrow(new AppException("Request not found"));

        ResponseEntity<?> response = studentRequestController.getRequestEstimatedWaitTime(requestId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentRequestService, times(1)).getRequestEstimatedWaitTime(requestId);
    }

    @Test
    void getRequestEstimatedWaitTime_ErrorPath_GeneralException() {
        when(studentRequestService.getRequestEstimatedWaitTime(requestId)).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = studentRequestController.getRequestEstimatedWaitTime(requestId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentRequestService, times(1)).getRequestEstimatedWaitTime(requestId);
    }

    @Test
    void canCancelRequest_HappyPath_True() {
        when(studentRequestService.canCancelRequest(requestId)).thenReturn(true);

        ResponseEntity<?> response = studentRequestController.canCancelRequest(requestId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentRequestService, times(1)).canCancelRequest(requestId);
    }

    @Test
    void canCancelRequest_HappyPath_False() {
        when(studentRequestService.canCancelRequest(requestId)).thenReturn(false);

        ResponseEntity<?> response = studentRequestController.canCancelRequest(requestId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentRequestService, times(1)).canCancelRequest(requestId);
    }

    @Test
    void canCancelRequest_ErrorPath_AppException() {
        when(studentRequestService.canCancelRequest(requestId)).thenThrow(new AppException("Request not found"));

        ResponseEntity<?> response = studentRequestController.canCancelRequest(requestId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentRequestService, times(1)).canCancelRequest(requestId);
    }

    @Test
    void canCancelRequest_ErrorPath_GeneralException() {
        when(studentRequestService.canCancelRequest(requestId)).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = studentRequestController.canCancelRequest(requestId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentRequestService, times(1)).canCancelRequest(requestId);
    }

    @Test
    void getRequiredDocuments_HappyPath() {
        List<String> documents = Arrays.asList("Document A", "Document B");
        when(studentRequestService.getRequiredDocuments("change_group")).thenReturn(documents);

        ResponseEntity<?> response = studentRequestController.getRequiredDocuments("change_group");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentRequestService, times(1)).getRequiredDocuments("change_group");
    }

    @Test
    void getRequiredDocuments_ErrorPath_GeneralException() {
        when(studentRequestService.getRequiredDocuments("change_group")).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = studentRequestController.getRequiredDocuments("change_group");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentRequestService, times(1)).getRequiredDocuments("change_group");
    }

    @Test
    void getRequestStatusDetails_HappyPath() {
        int priorityPosition = 2;
        String waitTime = "1 day";
        boolean canCancel = true;
        List<Group> alternativeGroups = Arrays.asList(group);

        when(studentRequestService.getRequestPriorityPosition(requestId)).thenReturn(priorityPosition);
        when(studentRequestService.getRequestEstimatedWaitTime(requestId)).thenReturn(waitTime);
        when(studentRequestService.canCancelRequest(requestId)).thenReturn(canCancel);
        when(studentRequestService.getRecommendedAlternativeGroups(requestId)).thenReturn(alternativeGroups);

        ResponseEntity<?> response = studentRequestController.getRequestStatusDetails(requestId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentRequestService, times(1)).getRequestPriorityPosition(requestId);
        verify(studentRequestService, times(1)).getRequestEstimatedWaitTime(requestId);
        verify(studentRequestService, times(1)).canCancelRequest(requestId);
        verify(studentRequestService, times(1)).getRecommendedAlternativeGroups(requestId);
    }

    @Test
    void getRequestStatusDetails_ErrorPath_AppException() {
        when(studentRequestService.getRequestPriorityPosition(requestId)).thenThrow(new AppException("Request not found"));

        ResponseEntity<?> response = studentRequestController.getRequestStatusDetails(requestId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentRequestService, times(1)).getRequestPriorityPosition(requestId);
    }

    @Test
    void getRequestStatusDetails_ErrorPath_GeneralException() {
        when(studentRequestService.getRequestPriorityPosition(requestId)).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = studentRequestController.getRequestStatusDetails(requestId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentRequestService, times(1)).getRequestPriorityPosition(requestId);
    }

    @Test
    void getStudentRequestMetrics_HappyPath() {
        double approvalRate = 75.0;
        List<ScheduleChangeRequest> pendingRequests = Arrays.asList(scheduleChangeRequest);

        when(studentRequestService.getStudentApprovalRate(studentId)).thenReturn(approvalRate);
        when(studentRequestService.getPendingRequestsStatus(studentId)).thenReturn(pendingRequests);

        ResponseEntity<?> response = studentRequestController.getStudentRequestMetrics(studentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentRequestService, times(1)).getStudentApprovalRate(studentId);
        verify(studentRequestService, times(1)).getPendingRequestsStatus(studentId);
    }

    @Test
    void getStudentRequestMetrics_ErrorPath_AppException() {
        when(studentRequestService.getStudentApprovalRate(studentId)).thenThrow(new AppException("Student not found"));

        ResponseEntity<?> response = studentRequestController.getStudentRequestMetrics(studentId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentRequestService, times(1)).getStudentApprovalRate(studentId);
    }

    @Test
    void getStudentRequestMetrics_ErrorPath_GeneralException() {
        when(studentRequestService.getStudentApprovalRate(studentId)).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = studentRequestController.getStudentRequestMetrics(studentId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentRequestService, times(1)).getStudentApprovalRate(studentId);
    }

    @Test
    void getNewRequestEligibility_HappyPath_Eligible() {
        List<ScheduleChangeRequest> pendingRequests = Arrays.asList(scheduleChangeRequest);
        List<String> requiredDocuments = Arrays.asList("Document A", "Document B");

        when(studentRequestService.getPendingRequestsStatus(studentId)).thenReturn(pendingRequests);
        when(studentRequestService.getRequiredDocuments("change_group")).thenReturn(requiredDocuments);

        ResponseEntity<?> response = studentRequestController.getNewRequestEligibility(studentId, "change_group");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentRequestService, times(1)).getPendingRequestsStatus(studentId);
        verify(studentRequestService, times(1)).getRequiredDocuments("change_group");
    }

    @Test
    void getNewRequestEligibility_HappyPath_NotEligible() {
        ScheduleChangeRequest request2 = new ScheduleChangeRequest();
        ScheduleChangeRequest request3 = new ScheduleChangeRequest();
        List<ScheduleChangeRequest> pendingRequests = Arrays.asList(scheduleChangeRequest, request2, request3);
        List<String> requiredDocuments = Arrays.asList("Document A", "Document B");

        when(studentRequestService.getPendingRequestsStatus(studentId)).thenReturn(pendingRequests);
        when(studentRequestService.getRequiredDocuments("change_group")).thenReturn(requiredDocuments);

        ResponseEntity<?> response = studentRequestController.getNewRequestEligibility(studentId, "change_group");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentRequestService, times(1)).getPendingRequestsStatus(studentId);
        verify(studentRequestService, times(1)).getRequiredDocuments("change_group");
    }

    @Test
    void getNewRequestEligibility_ErrorPath_AppException() {
        when(studentRequestService.getPendingRequestsStatus(studentId)).thenThrow(new AppException("Student not found"));

        ResponseEntity<?> response = studentRequestController.getNewRequestEligibility(studentId, "change_group");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentRequestService, times(1)).getPendingRequestsStatus(studentId);
    }

    @Test
    void getNewRequestEligibility_ErrorPath_GeneralException() {
        when(studentRequestService.getPendingRequestsStatus(studentId)).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = studentRequestController.getNewRequestEligibility(studentId, "change_group");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentRequestService, times(1)).getPendingRequestsStatus(studentId);
    }
}
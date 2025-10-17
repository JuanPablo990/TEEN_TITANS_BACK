package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.RequestService;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestControllerTest {

    @Mock
    private RequestService requestService;

    @InjectMocks
    private RequestController requestController;

    private ScheduleChangeRequest scheduleChangeRequest;
    private Student student;
    private Group group;
    private String requestId;
    private String studentId;

    @BeforeEach
    void setUp() {
        requestId = "req123";
        studentId = "stu456";

        student = new Student();
        group = new Group();

        scheduleChangeRequest = new ScheduleChangeRequest();
        scheduleChangeRequest.setRequestId(requestId);
        scheduleChangeRequest.setStudent(student);
        scheduleChangeRequest.setCurrentGroup(group);
        scheduleChangeRequest.setRequestedGroup(group);
        scheduleChangeRequest.setReason("Test reason");
        scheduleChangeRequest.setStatus(RequestStatus.PENDING);
    }

    @Test
    void createGroupChangeRequest_HappyPath() {
        RequestController.GroupChangeRequest groupRequest = new RequestController.GroupChangeRequest();
        groupRequest.setStudent(student);
        groupRequest.setCurrentGroup(group);
        groupRequest.setRequestedGroup(group);
        groupRequest.setReason("Test reason");

        when(requestService.createGroupChangeRequest(any(Student.class), any(Group.class), any(Group.class), anyString()))
                .thenReturn(scheduleChangeRequest);

        ResponseEntity<?> response = requestController.createGroupChangeRequest(groupRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(scheduleChangeRequest, response.getBody());
        verify(requestService, times(1)).createGroupChangeRequest(student, group, group, "Test reason");
    }

    @Test
    void createGroupChangeRequest_ErrorPath() {
        RequestController.GroupChangeRequest groupRequest = new RequestController.GroupChangeRequest();
        // Establecer datos reales para evitar NullPointerException en el controlador
        groupRequest.setStudent(new Student());
        groupRequest.setCurrentGroup(new Group());
        groupRequest.setRequestedGroup(new Group());
        groupRequest.setReason("Test reason");

        when(requestService.createGroupChangeRequest(any(Student.class), any(Group.class), any(Group.class), anyString()))
                .thenThrow(new AppException("Error creating request"));

        ResponseEntity<?> response = requestController.createGroupChangeRequest(groupRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(requestService, times(1)).createGroupChangeRequest(any(Student.class), any(Group.class), any(Group.class), eq("Test reason"));
    }

    @Test
    void createCourseChangeRequest_HappyPath() {
        RequestController.CourseChangeRequest courseRequest = new RequestController.CourseChangeRequest();
        courseRequest.setStudent(student);
        courseRequest.setCurrentCourse(new Course());
        courseRequest.setRequestedCourse(new Course());
        courseRequest.setReason("Test reason");

        when(requestService.createCourseChangeRequest(any(Student.class), any(Course.class), any(Course.class), anyString()))
                .thenReturn(scheduleChangeRequest);

        ResponseEntity<?> response = requestController.createCourseChangeRequest(courseRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(scheduleChangeRequest, response.getBody());
        verify(requestService, times(1)).createCourseChangeRequest(student, courseRequest.getCurrentCourse(), courseRequest.getRequestedCourse(), "Test reason");
    }


    @Test
    void createCourseChangeRequest_ErrorPath() {
        RequestController.CourseChangeRequest courseRequest = new RequestController.CourseChangeRequest();
        courseRequest.setStudent(student);
        courseRequest.setCurrentCourse(new Course());
        courseRequest.setRequestedCourse(new Course());
        courseRequest.setReason("Test reason");

        when(requestService.createCourseChangeRequest(any(Student.class), any(Course.class), any(Course.class), anyString()))
                .thenThrow(new AppException("Error creating request"));

        ResponseEntity<?> response = requestController.createCourseChangeRequest(courseRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(requestService, times(1)).createCourseChangeRequest(student, courseRequest.getCurrentCourse(), courseRequest.getRequestedCourse(), "Test reason");
    }

    @Test
    void getRequestStatus_HappyPath() {
        when(requestService.getRequestStatus(requestId)).thenReturn(RequestStatus.APPROVED);

        ResponseEntity<?> response = requestController.getRequestStatus(requestId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(requestService, times(1)).getRequestStatus(requestId);
    }

    @Test
    void getRequestStatus_ErrorPath() {
        when(requestService.getRequestStatus(requestId)).thenThrow(new AppException("Request not found"));

        ResponseEntity<?> response = requestController.getRequestStatus(requestId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(requestService, times(1)).getRequestStatus(requestId);
    }

    @Test
    void getRequestHistory_HappyPath() {
        List<ScheduleChangeRequest> requests = Arrays.asList(scheduleChangeRequest);
        when(requestService.getRequestHistory(studentId)).thenReturn(requests);

        ResponseEntity<?> response = requestController.getRequestHistory(studentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(requests, response.getBody());
        verify(requestService, times(1)).getRequestHistory(studentId);
    }

    @Test
    void getRequestHistory_ErrorPath() {
        when(requestService.getRequestHistory(studentId)).thenThrow(new AppException("Student not found"));

        ResponseEntity<?> response = requestController.getRequestHistory(studentId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(requestService, times(1)).getRequestHistory(studentId);
    }

    @Test
    void getDecisionHistory_HappyPath() {
        List<ReviewStep> decisionHistory = Arrays.asList(new ReviewStep());
        when(requestService.getDecisionHistory(requestId)).thenReturn(decisionHistory);

        ResponseEntity<?> response = requestController.getDecisionHistory(requestId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(decisionHistory, response.getBody());
        verify(requestService, times(1)).getDecisionHistory(requestId);
    }

    @Test
    void getDecisionHistory_ErrorPath() {
        when(requestService.getDecisionHistory(requestId)).thenThrow(new AppException("Request not found"));

        ResponseEntity<?> response = requestController.getDecisionHistory(requestId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(requestService, times(1)).getDecisionHistory(requestId);
    }

    @Test
    void updateRequest_HappyPath() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("reason", "Updated reason");

        when(requestService.updateRequest(eq(requestId), any(Map.class))).thenReturn(scheduleChangeRequest);

        ResponseEntity<?> response = requestController.updateRequest(requestId, updates);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(scheduleChangeRequest, response.getBody());
        verify(requestService, times(1)).updateRequest(requestId, updates);
    }

    @Test
    void updateRequest_ErrorPath() {
        Map<String, Object> updates = new HashMap<>();

        when(requestService.updateRequest(eq(requestId), any(Map.class))).thenThrow(new AppException("Update failed"));

        ResponseEntity<?> response = requestController.updateRequest(requestId, updates);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(requestService, times(1)).updateRequest(requestId, updates);
    }

    @Test
    void deleteRequest_HappyPath() {
        when(requestService.deleteRequest(requestId)).thenReturn(true);

        ResponseEntity<?> response = requestController.deleteRequest(requestId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(requestService, times(1)).deleteRequest(requestId);
    }

    @Test
    void deleteRequest_ErrorPath_NotFound() {
        when(requestService.deleteRequest(requestId)).thenReturn(false);

        ResponseEntity<?> response = requestController.deleteRequest(requestId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(requestService, times(1)).deleteRequest(requestId);
    }

    @Test
    void deleteRequest_ErrorPath_Exception() {
        when(requestService.deleteRequest(requestId)).thenThrow(new AppException("Delete failed"));

        ResponseEntity<?> response = requestController.deleteRequest(requestId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(requestService, times(1)).deleteRequest(requestId);
    }

    @Test
    void getGroupCapacityAlert_HappyPath() {
        when(requestService.getGroupCapacityAlert(requestId)).thenReturn(true);

        ResponseEntity<?> response = requestController.getGroupCapacityAlert(requestId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(requestService, times(1)).getGroupCapacityAlert(requestId);
    }

    @Test
    void getGroupCapacityAlert_ErrorPath() {
        when(requestService.getGroupCapacityAlert(requestId)).thenThrow(new AppException("Group not found"));

        ResponseEntity<?> response = requestController.getGroupCapacityAlert(requestId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(requestService, times(1)).getGroupCapacityAlert(requestId);
    }

    @Test
    void approveRequest_HappyPath() {
        RequestController.ReviewActionRequest reviewRequest = new RequestController.ReviewActionRequest();
        reviewRequest.setReviewerId("rev123");
        reviewRequest.setReviewerRole(UserRole.ADMINISTRATOR);
        reviewRequest.setComments("Approved");

        when(requestService.approveRequest(anyString(), anyString(), any(UserRole.class), anyString()))
                .thenReturn(scheduleChangeRequest);

        ResponseEntity<?> response = requestController.approveRequest(requestId, reviewRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(scheduleChangeRequest, response.getBody());
        verify(requestService, times(1)).approveRequest(requestId, "rev123", UserRole.ADMINISTRATOR, "Approved");
    }

    @Test
    void approveRequest_ErrorPath() {
        RequestController.ReviewActionRequest reviewRequest = new RequestController.ReviewActionRequest();
        reviewRequest.setReviewerId("rev123");
        reviewRequest.setReviewerRole(UserRole.ADMINISTRATOR);
        reviewRequest.setComments("Test comment");

        when(requestService.approveRequest(anyString(), anyString(), any(UserRole.class), anyString()))
                .thenThrow(new AppException("Approve failed"));

        ResponseEntity<?> response = requestController.approveRequest(requestId, reviewRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(requestService, times(1)).approveRequest(requestId, "rev123", UserRole.ADMINISTRATOR, "Test comment");
    }


    @Test
    void rejectRequest_HappyPath() {
        RequestController.ReviewActionRequest reviewRequest = new RequestController.ReviewActionRequest();
        reviewRequest.setReviewerId("rev123");
        reviewRequest.setReviewerRole(UserRole.ADMINISTRATOR);
        reviewRequest.setComments("Rejected");

        when(requestService.rejectRequest(anyString(), anyString(), any(UserRole.class), anyString()))
                .thenReturn(scheduleChangeRequest);

        ResponseEntity<?> response = requestController.rejectRequest(requestId, reviewRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(scheduleChangeRequest, response.getBody());
        verify(requestService, times(1)).rejectRequest(requestId, "rev123", UserRole.ADMINISTRATOR, "Rejected");
    }

    @Test
    void rejectRequest_ErrorPath() {
        RequestController.ReviewActionRequest reviewRequest = new RequestController.ReviewActionRequest();
        reviewRequest.setReviewerId("rev123");
        reviewRequest.setReviewerRole(UserRole.ADMINISTRATOR);
        reviewRequest.setComments("Test comment");

        when(requestService.rejectRequest(anyString(), anyString(), any(UserRole.class), anyString()))
                .thenThrow(new AppException("Reject failed"));

        ResponseEntity<?> response = requestController.rejectRequest(requestId, reviewRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(requestService, times(1)).rejectRequest(requestId, "rev123", UserRole.ADMINISTRATOR, "Test comment");
    }

    @Test
    void cancelRequest_HappyPath() {
        RequestController.CancelRequest cancelRequest = new RequestController.CancelRequest();
        cancelRequest.setStudentId(studentId);

        when(requestService.cancelRequest(anyString(), anyString())).thenReturn(scheduleChangeRequest);

        ResponseEntity<?> response = requestController.cancelRequest(requestId, cancelRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(scheduleChangeRequest, response.getBody());
        verify(requestService, times(1)).cancelRequest(requestId, studentId);
    }

    @Test
    void cancelRequest_ErrorPath() {
        RequestController.CancelRequest cancelRequest = new RequestController.CancelRequest();
        cancelRequest.setStudentId(studentId);

        when(requestService.cancelRequest(anyString(), anyString())).thenThrow(new AppException("Cancel failed"));

        ResponseEntity<?> response = requestController.cancelRequest(requestId, cancelRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(requestService, times(1)).cancelRequest(requestId, studentId);
    }

    @Test
    void getRequestStatistics_HappyPath() {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalRequests", 5);
        statistics.put("approved", 3);

        when(requestService.getRequestStatistics(studentId)).thenReturn(statistics);

        ResponseEntity<?> response = requestController.getRequestStatistics(studentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(statistics, response.getBody());
        verify(requestService, times(1)).getRequestStatistics(studentId);
    }

    @Test
    void getRequestStatistics_ErrorPath() {
        when(requestService.getRequestStatistics(studentId)).thenThrow(new AppException("Statistics error"));

        ResponseEntity<?> response = requestController.getRequestStatistics(studentId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(requestService, times(1)).getRequestStatistics(studentId);
    }
}
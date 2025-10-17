package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

class StudentRequestServiceTest {

    @Mock
    private ScheduleChangeRequestRepository scheduleChangeRequestRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private ReviewStepRepository reviewStepRepository;

    @Mock
    private StudentAcademicProgressRepository studentAcademicProgressRepository;

    @InjectMocks
    private StudentRequestService studentRequestService;

    @Test
    void getStudentApprovalRate_WhenStudentExists_ShouldReturnApprovalRate() {
        String studentId = "student1";

        ScheduleChangeRequest approvedRequest = new ScheduleChangeRequest();
        approvedRequest.setStatus(RequestStatus.APPROVED);

        ScheduleChangeRequest rejectedRequest = new ScheduleChangeRequest();
        rejectedRequest.setStatus(RequestStatus.REJECTED);

        List<ScheduleChangeRequest> requests = Arrays.asList(approvedRequest, rejectedRequest);

        when(studentRepository.existsById(studentId)).thenReturn(true);
        when(scheduleChangeRequestRepository.findByStudentId(studentId)).thenReturn(requests);

        double result = studentRequestService.getStudentApprovalRate(studentId);

        assertEquals(50.0, result);
    }

    @Test
    void getStudentApprovalRate_WhenStudentNotExists_ShouldThrowException() {
        String studentId = "nonexistent";

        when(studentRepository.existsById(studentId)).thenReturn(false);

        AppException exception = assertThrows(AppException.class, () -> {
            studentRequestService.getStudentApprovalRate(studentId);
        });

        assertEquals("El estudiante con ID " + studentId + " no existe.", exception.getMessage());
    }

    @Test
    void getPendingRequestsStatus_WhenStudentExists_ShouldReturnPendingRequests() {
        String studentId = "student1";

        ScheduleChangeRequest pendingRequest = new ScheduleChangeRequest();
        pendingRequest.setStatus(RequestStatus.PENDING);
        pendingRequest.setSubmissionDate(new Date());

        ScheduleChangeRequest underReviewRequest = new ScheduleChangeRequest();
        underReviewRequest.setStatus(RequestStatus.UNDER_REVIEW);
        underReviewRequest.setSubmissionDate(new Date(System.currentTimeMillis() - 1000));

        List<ScheduleChangeRequest> pendingRequests = Arrays.asList(pendingRequest);
        List<ScheduleChangeRequest> underReviewRequests = Arrays.asList(underReviewRequest);

        when(studentRepository.existsById(studentId)).thenReturn(true);
        when(scheduleChangeRequestRepository.findByStudentIdAndStatus(studentId, RequestStatus.PENDING))
                .thenReturn(pendingRequests);
        when(scheduleChangeRequestRepository.findByStudentIdAndStatus(studentId, RequestStatus.UNDER_REVIEW))
                .thenReturn(underReviewRequests);

        List<ScheduleChangeRequest> result = studentRequestService.getPendingRequestsStatus(studentId);

        assertEquals(2, result.size());
    }

    @Test
    void getRequestPriorityPosition_WhenValidRequest_ShouldReturnPosition() {
        String requestId = "REQ-123";
        String studentId = "student1";

        Student student = new Student(studentId, "John Doe", "john@example.com", "password", "Engineering", 5);

        Course course = new Course("COURSE1", "Math", 3, "Math course", "Engineering", true);
        Professor professor = new Professor();
        Schedule schedule = new Schedule();
        Classroom classroom = new Classroom("class1", "Building A", "101", 30, RoomType.REGULAR);
        Group group = new Group("group1", "A", course, professor, schedule, classroom);

        ScheduleChangeRequest request = new ScheduleChangeRequest(requestId, student, group, group, "Reason");
        request.setSubmissionDate(new Date());

        StudentAcademicProgress progress = new StudentAcademicProgress("progress1", student, "Engineering", "Engineering Faculty", "Regular", 5, 10, 100, 200, 4.0, new ArrayList<>());

        List<StudentAcademicProgress> progressList = Arrays.asList(progress);
        List<ScheduleChangeRequest> pendingRequests = Arrays.asList(request);

        when(scheduleChangeRequestRepository.findById(requestId)).thenReturn(Optional.of(request));
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(studentAcademicProgressRepository.findByAcademicProgram("Engineering")).thenReturn(progressList);
        when(scheduleChangeRequestRepository.findByStatus(RequestStatus.PENDING)).thenReturn(pendingRequests);

        int result = studentRequestService.getRequestPriorityPosition(requestId);

        assertEquals(1, result);
    }

    @Test
    void getRequestEstimatedWaitTime_WhenPendingRequest_ShouldReturnEstimation() {
        String requestId = "REQ-123";
        String studentId = "student1";

        Student student = new Student(studentId, "John Doe", "john@example.com", "password", "Engineering", 5);

        Course course = new Course("COURSE1", "Math", 3, "Math course", "Engineering", true);
        Professor professor = new Professor();
        Schedule schedule = new Schedule();
        Classroom classroom = new Classroom("class1", "Building A", "101", 30, RoomType.REGULAR);
        Group group = new Group("group1", "A", course, professor, schedule, classroom);

        ScheduleChangeRequest request = new ScheduleChangeRequest(requestId, student, group, group, "Reason");
        request.setStatus(RequestStatus.PENDING);
        request.setSubmissionDate(new Date(System.currentTimeMillis() - 86400000));

        StudentAcademicProgress progress = new StudentAcademicProgress("progress1", student, "Engineering", "Engineering Faculty", "Regular", 5, 10, 100, 200, 4.0, new ArrayList<>());

        List<StudentAcademicProgress> progressList = Arrays.asList(progress);
        List<ScheduleChangeRequest> pendingRequests = Arrays.asList(request);

        when(scheduleChangeRequestRepository.findById(requestId)).thenReturn(Optional.of(request));
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(studentAcademicProgressRepository.findByAcademicProgram("Engineering")).thenReturn(progressList);
        when(scheduleChangeRequestRepository.findByStatus(RequestStatus.PENDING)).thenReturn(pendingRequests);
        when(scheduleChangeRequestRepository.findByResolutionDateAfter(any(Date.class))).thenReturn(Collections.emptyList());

        String result = studentRequestService.getRequestEstimatedWaitTime(requestId);

        assertNotNull(result);
        assertTrue(result.equals("1-3 días") || result.equals("3-7 días") || result.equals("Menos de 1 día") || result.equals("Más de 1 semana"));
    }

    @Test
    void canCancelRequest_WhenPendingRequest_ShouldReturnTrue() {
        String requestId = "REQ-123";

        ScheduleChangeRequest request = new ScheduleChangeRequest();
        request.setRequestId(requestId);
        request.setStatus(RequestStatus.PENDING);

        when(scheduleChangeRequestRepository.findById(requestId)).thenReturn(Optional.of(request));

        boolean result = studentRequestService.canCancelRequest(requestId);

        assertTrue(result);
    }

    @Test
    void canCancelRequest_WhenApprovedRequest_ShouldReturnFalse() {
        String requestId = "REQ-123";

        ScheduleChangeRequest request = new ScheduleChangeRequest();
        request.setRequestId(requestId);
        request.setStatus(RequestStatus.APPROVED);

        when(scheduleChangeRequestRepository.findById(requestId)).thenReturn(Optional.of(request));

        boolean result = studentRequestService.canCancelRequest(requestId);

        assertFalse(result);
    }

    @Test
    void getRequiredDocuments_WhenGroupChange_ShouldReturnGroupChangeDocuments() {
        List<String> result = studentRequestService.getRequiredDocuments("GROUP_CHANGE");

        assertEquals(4, result.size());
        assertTrue(result.contains("Formulario de cambio de grupo"));
    }

    @Test
    void getRequiredDocuments_WhenCourseChange_ShouldReturnCourseChangeDocuments() {
        List<String> result = studentRequestService.getRequiredDocuments("COURSE_CHANGE");

        assertEquals(5, result.size());
        assertTrue(result.contains("Formulario de cambio de curso"));
    }

    @Test
    void getRequiredDocuments_WhenSpecialRequest_ShouldReturnSpecialRequestDocuments() {
        List<String> result = studentRequestService.getRequiredDocuments("SPECIAL_REQUEST");

        assertEquals(4, result.size());
        assertTrue(result.contains("Formulario de solicitud especial"));
    }

    @Test
    void getRequiredDocuments_WhenUnknownType_ShouldReturnDefaultDocuments() {
        List<String> result = studentRequestService.getRequiredDocuments("UNKNOWN_TYPE");

        assertEquals(2, result.size());
        assertTrue(result.contains("Formulario general de solicitud"));
    }
}
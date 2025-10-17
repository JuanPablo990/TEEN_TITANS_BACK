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
class RequestServiceTest {

    @Mock
    private ScheduleChangeRequestRepository scheduleChangeRequestRepository;

    @Mock
    private ReviewStepRepository reviewStepRepository;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private RequestService requestService;

    @Test
    void createGroupChangeRequest_WhenValidData_ShouldCreateRequest() {
        Student student = new Student();
        student.setId("student1");
        student.setActive(true);

        Course course = new Course("COURSE1", "Math", 3, "Math course", "Engineering", true);
        Professor professor = new Professor();
        Schedule schedule = new Schedule();
        Classroom classroom = new Classroom("class1", "Building A", "101", 30, RoomType.REGULAR);

        Group currentGroup = new Group("group1", "A", course, professor, schedule, classroom);
        Group requestedGroup = new Group("group2", "B", course, professor, schedule, classroom);

        when(studentRepository.findById("student1")).thenReturn(Optional.of(student));
        when(groupRepository.findById("group1")).thenReturn(Optional.of(currentGroup));
        when(groupRepository.findById("group2")).thenReturn(Optional.of(requestedGroup));
        when(scheduleChangeRequestRepository.countByStudentIdAndStatus("student1", RequestStatus.PENDING)).thenReturn(0L);
        when(scheduleChangeRequestRepository.save(any(ScheduleChangeRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ScheduleChangeRequest result = requestService.createGroupChangeRequest(student, currentGroup, requestedGroup, "Conflict");

        assertNotNull(result);
        assertEquals("student1", result.getStudent().getId());
        assertEquals("group1", result.getCurrentGroup().getGroupId());
        assertEquals("group2", result.getRequestedGroup().getGroupId());
        verify(scheduleChangeRequestRepository).save(any(ScheduleChangeRequest.class));
    }

    @Test
    void createGroupChangeRequest_WhenStudentHasThreePendingRequests_ShouldThrowException() {
        Student student = new Student();
        student.setId("student1");
        student.setActive(true);

        Course course = new Course("COURSE1", "Math", 3, "Math course", "Engineering", true);
        Professor professor = new Professor();
        Schedule schedule = new Schedule();
        Classroom classroom = new Classroom("class1", "Building A", "101", 30, RoomType.REGULAR);

        Group currentGroup = new Group("group1", "A", course, professor, schedule, classroom);
        Group requestedGroup = new Group("group2", "B", course, professor, schedule, classroom);

        when(studentRepository.findById("student1")).thenReturn(Optional.of(student));
        when(groupRepository.findById("group1")).thenReturn(Optional.of(currentGroup));
        when(groupRepository.findById("group2")).thenReturn(Optional.of(requestedGroup));
        when(scheduleChangeRequestRepository.countByStudentIdAndStatus("student1", RequestStatus.PENDING)).thenReturn(3L);

        AppException exception = assertThrows(AppException.class, () -> {
            requestService.createGroupChangeRequest(student, currentGroup, requestedGroup, "Reason");
        });

        assertEquals("El estudiante tiene demasiadas solicitudes pendientes. Límite: 3", exception.getMessage());
        verify(scheduleChangeRequestRepository, never()).save(any(ScheduleChangeRequest.class));
    }

    @Test
    void createCourseChangeRequest_WhenValidData_ShouldCreateRequest() {
        Student student = new Student();
        student.setId("student1");
        student.setActive(true);

        Course currentCourse = new Course("COURSE1", "Math", 3, "Math course", "Engineering", true);
        Course requestedCourse = new Course("COURSE2", "Physics", 3, "Physics course", "Engineering", true);

        Professor professor = new Professor();
        Schedule schedule = new Schedule();
        Classroom classroom = new Classroom("class1", "Building A", "101", 30, RoomType.REGULAR);

        Group currentGroup = new Group("group1", "A", currentCourse, professor, schedule, classroom);
        Group requestedGroup = new Group("group2", "B", requestedCourse, professor, schedule, classroom);

        when(studentRepository.findById("student1")).thenReturn(Optional.of(student));
        when(courseRepository.findById("COURSE1")).thenReturn(Optional.of(currentCourse));
        when(courseRepository.findById("COURSE2")).thenReturn(Optional.of(requestedCourse));
        when(groupRepository.findByCourse_CourseCode("COURSE1")).thenReturn(Arrays.asList(currentGroup));
        when(groupRepository.findByCourse_CourseCode("COURSE2")).thenReturn(Arrays.asList(requestedGroup));
        when(scheduleChangeRequestRepository.countByStudentIdAndStatus("student1", RequestStatus.PENDING)).thenReturn(0L);
        when(scheduleChangeRequestRepository.save(any(ScheduleChangeRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(groupRepository.findById("group2")).thenReturn(Optional.of(requestedGroup));
        when(scheduleChangeRequestRepository.findByRequestedGroupId("group2")).thenReturn(Collections.emptyList());

        ScheduleChangeRequest result = requestService.createCourseChangeRequest(student, currentCourse, requestedCourse, "Want to change course");

        assertNotNull(result);
        assertEquals("student1", result.getStudent().getId());
        assertEquals("COURSE1", result.getCurrentGroup().getCourse().getCourseCode());
        assertEquals("COURSE2", result.getRequestedGroup().getCourse().getCourseCode());
        verify(scheduleChangeRequestRepository).save(any(ScheduleChangeRequest.class));
    }

    @Test
    void createCourseChangeRequest_WhenSameCourse_ShouldThrowException() {
        Student student = new Student();
        student.setId("student1");
        student.setActive(true);

        Course currentCourse = new Course("COURSE1", "Math", 3, "Math course", "Engineering", true);
        Course requestedCourse = new Course("COURSE1", "Math", 3, "Math course", "Engineering", true);

        when(studentRepository.findById("student1")).thenReturn(Optional.of(student));

        AppException exception = assertThrows(AppException.class, () -> {
            requestService.createCourseChangeRequest(student, currentCourse, requestedCourse, "Reason");
        });

        assertEquals("El curso actual y el solicitado no pueden ser el mismo", exception.getMessage());
        verify(scheduleChangeRequestRepository, never()).save(any(ScheduleChangeRequest.class));
    }

    @Test
    void approveRequest_WhenValidData_ShouldApproveRequest() {
        String requestId = "REQ-123";
        String reviewerId = "prof1";

        Student student = new Student();
        student.setId("student1");

        Course course = new Course("COURSE1", "Math", 3, "Math course", "Engineering", true);
        Professor professor = new Professor();
        Schedule schedule = new Schedule();
        Classroom classroom = new Classroom("class1", "Building A", "101", 30, RoomType.REGULAR);

        Group currentGroup = new Group("group1", "A", course, professor, schedule, classroom);
        Group requestedGroup = new Group("group2", "B", course, professor, schedule, classroom);

        ScheduleChangeRequest request = new ScheduleChangeRequest(requestId, student, currentGroup, requestedGroup, "Reason");
        request.setStatus(RequestStatus.UNDER_REVIEW);

        when(scheduleChangeRequestRepository.findById(requestId)).thenReturn(Optional.of(request));
        when(groupRepository.findById("group2")).thenReturn(Optional.of(requestedGroup));
        when(scheduleChangeRequestRepository.findByRequestedGroupId("group2")).thenReturn(Collections.emptyList());
        when(scheduleChangeRequestRepository.save(any(ScheduleChangeRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ScheduleChangeRequest result = requestService.approveRequest(requestId, reviewerId, UserRole.PROFESSOR, "Approved");

        assertEquals(RequestStatus.APPROVED, result.getStatus());
        verify(scheduleChangeRequestRepository).save(request);
    }

    @Test
    void getRequestStatus_WhenRequestExists_ShouldReturnStatus() {
        String requestId = "REQ-123";

        Student student = new Student();
        Course course = new Course("COURSE1", "Math", 3, "Math course", "Engineering", true);
        Professor professor = new Professor();
        Schedule schedule = new Schedule();
        Classroom classroom = new Classroom("class1", "Building A", "101", 30, RoomType.REGULAR);
        Group currentGroup = new Group("group1", "A", course, professor, schedule, classroom);
        Group requestedGroup = new Group("group2", "B", course, professor, schedule, classroom);

        ScheduleChangeRequest request = new ScheduleChangeRequest(requestId, student, currentGroup, requestedGroup, "Reason");
        request.setStatus(RequestStatus.APPROVED);

        when(scheduleChangeRequestRepository.findById(requestId)).thenReturn(Optional.of(request));

        RequestStatus result = requestService.getRequestStatus(requestId);

        assertEquals(RequestStatus.APPROVED, result);
        verify(scheduleChangeRequestRepository).findById(requestId);
    }

    @Test
    void getRequestStatus_WhenRequestNotExists_ShouldThrowException() {
        String requestId = "NON_EXISTENT";
        when(scheduleChangeRequestRepository.findById(requestId)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            requestService.getRequestStatus(requestId);
        });

        assertEquals("Solicitud no encontrada: " + requestId, exception.getMessage());
        verify(scheduleChangeRequestRepository).findById(requestId);
    }

    @Test
    void cancelRequest_WhenStudentIsOwner_ShouldCancelRequest() {
        String requestId = "REQ-123";
        String studentId = "student1";

        Student student = new Student();
        student.setId(studentId);

        Course course = new Course("COURSE1", "Math", 3, "Math course", "Engineering", true);
        Professor professor = new Professor();
        Schedule schedule = new Schedule();
        Classroom classroom = new Classroom("class1", "Building A", "101", 30, RoomType.REGULAR);
        Group currentGroup = new Group("group1", "A", course, professor, schedule, classroom);
        Group requestedGroup = new Group("group2", "B", course, professor, schedule, classroom);

        ScheduleChangeRequest request = new ScheduleChangeRequest(requestId, student, currentGroup, requestedGroup, "Reason");
        request.setStatus(RequestStatus.PENDING);

        when(scheduleChangeRequestRepository.findById(requestId)).thenReturn(Optional.of(request));
        when(scheduleChangeRequestRepository.save(any(ScheduleChangeRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ScheduleChangeRequest result = requestService.cancelRequest(requestId, studentId);

        assertEquals(RequestStatus.CANCELLED, result.getStatus());
        verify(scheduleChangeRequestRepository).save(request);
    }

    @Test
    void cancelRequest_WhenStudentNotOwner_ShouldThrowException() {
        String requestId = "REQ-123";
        String wrongStudentId = "student2";

        Student student = new Student();
        student.setId("student1");

        Course course = new Course("COURSE1", "Math", 3, "Math course", "Engineering", true);
        Professor professor = new Professor();
        Schedule schedule = new Schedule();
        Classroom classroom = new Classroom("class1", "Building A", "101", 30, RoomType.REGULAR);
        Group currentGroup = new Group("group1", "A", course, professor, schedule, classroom);
        Group requestedGroup = new Group("group2", "B", course, professor, schedule, classroom);

        ScheduleChangeRequest request = new ScheduleChangeRequest(requestId, student, currentGroup, requestedGroup, "Reason");

        when(scheduleChangeRequestRepository.findById(requestId)).thenReturn(Optional.of(request));

        AppException exception = assertThrows(AppException.class, () -> {
            requestService.cancelRequest(requestId, wrongStudentId);
        });

        assertEquals("Solo el estudiante propietario puede cancelar la solicitud", exception.getMessage());
        verify(scheduleChangeRequestRepository, never()).save(any(ScheduleChangeRequest.class));
    }

    @Test
    void rejectRequest_WhenValidData_ShouldRejectRequest() {
        String requestId = "REQ-123";
        String reviewerId = "prof1";

        Student student = new Student();
        student.setId("student1");

        Course course = new Course("COURSE1", "Math", 3, "Math course", "Engineering", true);
        Professor professor = new Professor();
        Schedule schedule = new Schedule();
        Classroom classroom = new Classroom("class1", "Building A", "101", 30, RoomType.REGULAR);
        Group currentGroup = new Group("group1", "A", course, professor, schedule, classroom);
        Group requestedGroup = new Group("group2", "B", course, professor, schedule, classroom);

        ScheduleChangeRequest request = new ScheduleChangeRequest(requestId, student, currentGroup, requestedGroup, "Reason");

        when(scheduleChangeRequestRepository.findById(requestId)).thenReturn(Optional.of(request));
        when(scheduleChangeRequestRepository.save(any(ScheduleChangeRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ScheduleChangeRequest result = requestService.rejectRequest(requestId, reviewerId, UserRole.PROFESSOR, "Rejected");

        assertEquals(RequestStatus.REJECTED, result.getStatus());
        verify(scheduleChangeRequestRepository).save(request);
    }

    @Test
    void deleteRequest_WhenPendingRequest_ShouldDeleteRequest() {
        String requestId = "REQ-123";

        Student student = new Student();
        student.setId("student1");

        Course course = new Course("COURSE1", "Math", 3, "Math course", "Engineering", true);
        Professor professor = new Professor();
        Schedule schedule = new Schedule();
        Classroom classroom = new Classroom("class1", "Building A", "101", 30, RoomType.REGULAR);
        Group currentGroup = new Group("group1", "A", course, professor, schedule, classroom);
        Group requestedGroup = new Group("group2", "B", course, professor, schedule, classroom);

        ScheduleChangeRequest request = new ScheduleChangeRequest(requestId, student, currentGroup, requestedGroup, "Reason");
        request.setStatus(RequestStatus.PENDING);

        when(scheduleChangeRequestRepository.findById(requestId)).thenReturn(Optional.of(request));

        boolean result = requestService.deleteRequest(requestId);

        assertTrue(result);
        verify(scheduleChangeRequestRepository).delete(request);
    }

    @Test
    void deleteRequest_WhenNonPendingRequest_ShouldThrowException() {
        String requestId = "REQ-123";

        Student student = new Student();
        student.setId("student1");

        Course course = new Course("COURSE1", "Math", 3, "Math course", "Engineering", true);
        Professor professor = new Professor();
        Schedule schedule = new Schedule();
        Classroom classroom = new Classroom("class1", "Building A", "101", 30, RoomType.REGULAR);
        Group currentGroup = new Group("group1", "A", course, professor, schedule, classroom);
        Group requestedGroup = new Group("group2", "B", course, professor, schedule, classroom);

        ScheduleChangeRequest request = new ScheduleChangeRequest(requestId, student, currentGroup, requestedGroup, "Reason");
        request.setStatus(RequestStatus.APPROVED);

        when(scheduleChangeRequestRepository.findById(requestId)).thenReturn(Optional.of(request));

        AppException exception = assertThrows(AppException.class, () -> {
            requestService.deleteRequest(requestId);
        });

        assertEquals("Solo se pueden eliminar solicitudes en estado PENDING", exception.getMessage());
        verify(scheduleChangeRequestRepository, never()).delete(any(ScheduleChangeRequest.class));
    }
}
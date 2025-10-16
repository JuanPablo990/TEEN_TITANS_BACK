package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ScheduleChangeRequestRepositoryTest {

    @Mock
    private ScheduleChangeRequestRepository scheduleChangeRequestRepository;

    private ScheduleChangeRequest pendingRequest;
    private ScheduleChangeRequest approvedRequest;
    private ScheduleChangeRequest rejectedRequest;
    private Student student;
    private Group currentGroup;
    private Group requestedGroup;

    @BeforeEach
    void setUp() {
        student = new Student();

        // Crear objetos necesarios para el constructor de Group
        Course course = new Course();
        Professor professor = new Professor();
        Schedule schedule = new Schedule();
        Classroom classroom = new Classroom();

        currentGroup = new Group("G001", "Sección A", course, professor, schedule, classroom);
        requestedGroup = new Group("G002", "Sección B", course, professor, schedule, classroom);

        pendingRequest = new ScheduleChangeRequest(
                "REQ001", student, currentGroup, requestedGroup, "Conflicto de horario"
        );

        approvedRequest = new ScheduleChangeRequest(
                "REQ002", student, currentGroup, requestedGroup, "Preferencia personal"
        );
        approvedRequest.setStatus(RequestStatus.APPROVED);

        rejectedRequest = new ScheduleChangeRequest(
                "REQ003", student, currentGroup, requestedGroup, "Razón inválida"
        );
        rejectedRequest.setStatus(RequestStatus.REJECTED);
    }

    // Los métodos de prueba permanecen iguales...
    @Test
    void findByStudentId_HappyPath_ReturnsRequests() {
        String studentId = "ST001";
        List<ScheduleChangeRequest> expectedRequests = Arrays.asList(pendingRequest, approvedRequest);
        when(scheduleChangeRequestRepository.findByStudentId(studentId)).thenReturn(expectedRequests);

        List<ScheduleChangeRequest> result = scheduleChangeRequestRepository.findByStudentId(studentId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(scheduleChangeRequestRepository, times(1)).findByStudentId(studentId);
    }

    @Test
    void findByStudentId_HappyPath_SingleRequest() {
        String studentId = "ST002";
        List<ScheduleChangeRequest> expectedRequests = Collections.singletonList(pendingRequest);
        when(scheduleChangeRequestRepository.findByStudentId(studentId)).thenReturn(expectedRequests);

        List<ScheduleChangeRequest> result = scheduleChangeRequestRepository.findByStudentId(studentId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(scheduleChangeRequestRepository, times(1)).findByStudentId(studentId);
    }

    @Test
    void findByStudentId_Error_NoRequestsFound() {
        String studentId = "ST999";
        when(scheduleChangeRequestRepository.findByStudentId(studentId)).thenReturn(Collections.emptyList());

        List<ScheduleChangeRequest> result = scheduleChangeRequestRepository.findByStudentId(studentId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(scheduleChangeRequestRepository, times(1)).findByStudentId(studentId);
    }

    @Test
    void findByStudentId_Error_NullParameter() {
        when(scheduleChangeRequestRepository.findByStudentId(null)).thenReturn(Collections.emptyList());

        List<ScheduleChangeRequest> result = scheduleChangeRequestRepository.findByStudentId(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(scheduleChangeRequestRepository, times(1)).findByStudentId(null);
    }

    @Test
    void findByStatus_HappyPath_PendingRequests() {
        List<ScheduleChangeRequest> expectedRequests = Collections.singletonList(pendingRequest);
        when(scheduleChangeRequestRepository.findByStatus(RequestStatus.PENDING)).thenReturn(expectedRequests);

        List<ScheduleChangeRequest> result = scheduleChangeRequestRepository.findByStatus(RequestStatus.PENDING);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(RequestStatus.PENDING, result.get(0).getStatus());
        verify(scheduleChangeRequestRepository, times(1)).findByStatus(RequestStatus.PENDING);
    }

    @Test
    void findByStatus_HappyPath_ApprovedRequests() {
        List<ScheduleChangeRequest> expectedRequests = Collections.singletonList(approvedRequest);
        when(scheduleChangeRequestRepository.findByStatus(RequestStatus.APPROVED)).thenReturn(expectedRequests);

        List<ScheduleChangeRequest> result = scheduleChangeRequestRepository.findByStatus(RequestStatus.APPROVED);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(RequestStatus.APPROVED, result.get(0).getStatus());
        verify(scheduleChangeRequestRepository, times(1)).findByStatus(RequestStatus.APPROVED);
    }

    @Test
    void findByStatus_Error_NoRequestsWithStatus() {
        when(scheduleChangeRequestRepository.findByStatus(RequestStatus.REJECTED)).thenReturn(Collections.emptyList());

        List<ScheduleChangeRequest> result = scheduleChangeRequestRepository.findByStatus(RequestStatus.REJECTED);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(scheduleChangeRequestRepository, times(1)).findByStatus(RequestStatus.REJECTED);
    }

    @Test
    void findByStatus_Error_NullParameter() {
        when(scheduleChangeRequestRepository.findByStatus(null)).thenReturn(Collections.emptyList());

        List<ScheduleChangeRequest> result = scheduleChangeRequestRepository.findByStatus(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(scheduleChangeRequestRepository, times(1)).findByStatus(null);
    }

    @Test
    void findByCurrentGroupId_HappyPath_ReturnsRequests() {
        String groupId = "G001";
        List<ScheduleChangeRequest> expectedRequests = Collections.singletonList(pendingRequest);
        when(scheduleChangeRequestRepository.findByCurrentGroupId(groupId)).thenReturn(expectedRequests);

        List<ScheduleChangeRequest> result = scheduleChangeRequestRepository.findByCurrentGroupId(groupId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(scheduleChangeRequestRepository, times(1)).findByCurrentGroupId(groupId);
    }

    @Test
    void findByCurrentGroupId_HappyPath_MultipleRequests() {
        String groupId = "G001";
        List<ScheduleChangeRequest> expectedRequests = Arrays.asList(pendingRequest, approvedRequest);
        when(scheduleChangeRequestRepository.findByCurrentGroupId(groupId)).thenReturn(expectedRequests);

        List<ScheduleChangeRequest> result = scheduleChangeRequestRepository.findByCurrentGroupId(groupId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(scheduleChangeRequestRepository, times(1)).findByCurrentGroupId(groupId);
    }

    @Test
    void findByCurrentGroupId_Error_NoRequestsForGroup() {
        String groupId = "G999";
        when(scheduleChangeRequestRepository.findByCurrentGroupId(groupId)).thenReturn(Collections.emptyList());

        List<ScheduleChangeRequest> result = scheduleChangeRequestRepository.findByCurrentGroupId(groupId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(scheduleChangeRequestRepository, times(1)).findByCurrentGroupId(groupId);
    }

    @Test
    void findByCurrentGroupId_Error_NullParameter() {
        when(scheduleChangeRequestRepository.findByCurrentGroupId(null)).thenReturn(Collections.emptyList());

        List<ScheduleChangeRequest> result = scheduleChangeRequestRepository.findByCurrentGroupId(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(scheduleChangeRequestRepository, times(1)).findByCurrentGroupId(null);
    }

    @Test
    void findByRequestedGroupId_HappyPath_ReturnsRequests() {
        String groupId = "G002";
        List<ScheduleChangeRequest> expectedRequests = Collections.singletonList(pendingRequest);
        when(scheduleChangeRequestRepository.findByRequestedGroupId(groupId)).thenReturn(expectedRequests);

        List<ScheduleChangeRequest> result = scheduleChangeRequestRepository.findByRequestedGroupId(groupId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(scheduleChangeRequestRepository, times(1)).findByRequestedGroupId(groupId);
    }

    @Test
    void findByRequestedGroupId_HappyPath_MultipleRequests() {
        String groupId = "G002";
        List<ScheduleChangeRequest> expectedRequests = Arrays.asList(pendingRequest, approvedRequest);
        when(scheduleChangeRequestRepository.findByRequestedGroupId(groupId)).thenReturn(expectedRequests);

        List<ScheduleChangeRequest> result = scheduleChangeRequestRepository.findByRequestedGroupId(groupId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(scheduleChangeRequestRepository, times(1)).findByRequestedGroupId(groupId);
    }

    @Test
    void findByRequestedGroupId_Error_NoRequestsForGroup() {
        String groupId = "G999";
        when(scheduleChangeRequestRepository.findByRequestedGroupId(groupId)).thenReturn(Collections.emptyList());

        List<ScheduleChangeRequest> result = scheduleChangeRequestRepository.findByRequestedGroupId(groupId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(scheduleChangeRequestRepository, times(1)).findByRequestedGroupId(groupId);
    }

    @Test
    void findByRequestedGroupId_Error_NullParameter() {
        when(scheduleChangeRequestRepository.findByRequestedGroupId(null)).thenReturn(Collections.emptyList());

        List<ScheduleChangeRequest> result = scheduleChangeRequestRepository.findByRequestedGroupId(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(scheduleChangeRequestRepository, times(1)).findByRequestedGroupId(null);
    }

    @Test
    void findBySubmissionDateAfter_HappyPath_ReturnsRequests() {
        Date date = new Date(System.currentTimeMillis() - 86400000);
        List<ScheduleChangeRequest> expectedRequests = Collections.singletonList(pendingRequest);
        when(scheduleChangeRequestRepository.findBySubmissionDateAfter(date)).thenReturn(expectedRequests);

        List<ScheduleChangeRequest> result = scheduleChangeRequestRepository.findBySubmissionDateAfter(date);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(scheduleChangeRequestRepository, times(1)).findBySubmissionDateAfter(date);
    }

    @Test
    void findBySubmissionDateAfter_HappyPath_MultipleRequests() {
        Date date = new Date(System.currentTimeMillis() - 86400000);
        List<ScheduleChangeRequest> expectedRequests = Arrays.asList(pendingRequest, approvedRequest);
        when(scheduleChangeRequestRepository.findBySubmissionDateAfter(date)).thenReturn(expectedRequests);

        List<ScheduleChangeRequest> result = scheduleChangeRequestRepository.findBySubmissionDateAfter(date);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(scheduleChangeRequestRepository, times(1)).findBySubmissionDateAfter(date);
    }

    @Test
    void findBySubmissionDateAfter_Error_NoRequestsAfterDate() {
        Date date = new Date(System.currentTimeMillis() + 86400000);
        when(scheduleChangeRequestRepository.findBySubmissionDateAfter(date)).thenReturn(Collections.emptyList());

        List<ScheduleChangeRequest> result = scheduleChangeRequestRepository.findBySubmissionDateAfter(date);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(scheduleChangeRequestRepository, times(1)).findBySubmissionDateAfter(date);
    }

    @Test
    void findBySubmissionDateAfter_Error_NullParameter() {
        when(scheduleChangeRequestRepository.findBySubmissionDateAfter(null)).thenReturn(Collections.emptyList());

        List<ScheduleChangeRequest> result = scheduleChangeRequestRepository.findBySubmissionDateAfter(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(scheduleChangeRequestRepository, times(1)).findBySubmissionDateAfter(null);
    }

    @Test
    void findBySubmissionDateBefore_HappyPath_ReturnsRequests() {
        Date date = new Date(System.currentTimeMillis() + 86400000);
        List<ScheduleChangeRequest> expectedRequests = Collections.singletonList(pendingRequest);
        when(scheduleChangeRequestRepository.findBySubmissionDateBefore(date)).thenReturn(expectedRequests);

        List<ScheduleChangeRequest> result = scheduleChangeRequestRepository.findBySubmissionDateBefore(date);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(scheduleChangeRequestRepository, times(1)).findBySubmissionDateBefore(date);
    }

    @Test
    void findBySubmissionDateBefore_HappyPath_MultipleRequests() {
        Date date = new Date(System.currentTimeMillis() + 86400000);
        List<ScheduleChangeRequest> expectedRequests = Arrays.asList(pendingRequest, approvedRequest);
        when(scheduleChangeRequestRepository.findBySubmissionDateBefore(date)).thenReturn(expectedRequests);

        List<ScheduleChangeRequest> result = scheduleChangeRequestRepository.findBySubmissionDateBefore(date);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(scheduleChangeRequestRepository, times(1)).findBySubmissionDateBefore(date);
    }

    @Test
    void findBySubmissionDateBefore_Error_NoRequestsBeforeDate() {
        Date date = new Date(System.currentTimeMillis() - 86400000);
        when(scheduleChangeRequestRepository.findBySubmissionDateBefore(date)).thenReturn(Collections.emptyList());

        List<ScheduleChangeRequest> result = scheduleChangeRequestRepository.findBySubmissionDateBefore(date);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(scheduleChangeRequestRepository, times(1)).findBySubmissionDateBefore(date);
    }

    @Test
    void findBySubmissionDateBefore_Error_NullParameter() {
        when(scheduleChangeRequestRepository.findBySubmissionDateBefore(null)).thenReturn(Collections.emptyList());

        List<ScheduleChangeRequest> result = scheduleChangeRequestRepository.findBySubmissionDateBefore(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(scheduleChangeRequestRepository, times(1)).findBySubmissionDateBefore(null);
    }

    @Test
    void findByStudentIdAndStatus_HappyPath_ReturnsRequests() {
        String studentId = "ST001";
        List<ScheduleChangeRequest> expectedRequests = Collections.singletonList(pendingRequest);
        when(scheduleChangeRequestRepository.findByStudentIdAndStatus(studentId, RequestStatus.PENDING))
                .thenReturn(expectedRequests);

        List<ScheduleChangeRequest> result = scheduleChangeRequestRepository
                .findByStudentIdAndStatus(studentId, RequestStatus.PENDING);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(RequestStatus.PENDING, result.get(0).getStatus());
        verify(scheduleChangeRequestRepository, times(1)).findByStudentIdAndStatus(studentId, RequestStatus.PENDING);
    }

    @Test
    void findByStudentIdAndStatus_HappyPath_DifferentStatus() {
        String studentId = "ST001";
        List<ScheduleChangeRequest> expectedRequests = Collections.singletonList(approvedRequest);
        when(scheduleChangeRequestRepository.findByStudentIdAndStatus(studentId, RequestStatus.APPROVED))
                .thenReturn(expectedRequests);

        List<ScheduleChangeRequest> result = scheduleChangeRequestRepository
                .findByStudentIdAndStatus(studentId, RequestStatus.APPROVED);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(RequestStatus.APPROVED, result.get(0).getStatus());
        verify(scheduleChangeRequestRepository, times(1)).findByStudentIdAndStatus(studentId, RequestStatus.APPROVED);
    }

    @Test
    void findByStudentIdAndStatus_Error_NoMatchingRequests() {
        String studentId = "ST999";
        when(scheduleChangeRequestRepository.findByStudentIdAndStatus(studentId, RequestStatus.PENDING))
                .thenReturn(Collections.emptyList());

        List<ScheduleChangeRequest> result = scheduleChangeRequestRepository
                .findByStudentIdAndStatus(studentId, RequestStatus.PENDING);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(scheduleChangeRequestRepository, times(1)).findByStudentIdAndStatus(studentId, RequestStatus.PENDING);
    }

    @Test
    void findByStudentIdAndStatus_Error_NullParameters() {
        when(scheduleChangeRequestRepository.findByStudentIdAndStatus(null, null))
                .thenReturn(Collections.emptyList());

        List<ScheduleChangeRequest> result = scheduleChangeRequestRepository
                .findByStudentIdAndStatus(null, null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(scheduleChangeRequestRepository, times(1)).findByStudentIdAndStatus(null, null);
    }

    @Test
    void findByResolutionDateAfter_HappyPath_ReturnsRequests() {
        Date date = new Date(System.currentTimeMillis() - 86400000);
        List<ScheduleChangeRequest> expectedRequests = Collections.singletonList(approvedRequest);
        when(scheduleChangeRequestRepository.findByResolutionDateAfter(date)).thenReturn(expectedRequests);

        List<ScheduleChangeRequest> result = scheduleChangeRequestRepository.findByResolutionDateAfter(date);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertNotNull(result.get(0).getResolutionDate());
        verify(scheduleChangeRequestRepository, times(1)).findByResolutionDateAfter(date);
    }

    @Test
    void findByResolutionDateAfter_HappyPath_MultipleRequests() {
        Date date = new Date(System.currentTimeMillis() - 86400000);
        List<ScheduleChangeRequest> expectedRequests = Arrays.asList(approvedRequest, rejectedRequest);
        when(scheduleChangeRequestRepository.findByResolutionDateAfter(date)).thenReturn(expectedRequests);

        List<ScheduleChangeRequest> result = scheduleChangeRequestRepository.findByResolutionDateAfter(date);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(scheduleChangeRequestRepository, times(1)).findByResolutionDateAfter(date);
    }

    @Test
    void findByResolutionDateAfter_Error_NoRequestsAfterDate() {
        Date date = new Date(System.currentTimeMillis() + 86400000);
        when(scheduleChangeRequestRepository.findByResolutionDateAfter(date)).thenReturn(Collections.emptyList());

        List<ScheduleChangeRequest> result = scheduleChangeRequestRepository.findByResolutionDateAfter(date);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(scheduleChangeRequestRepository, times(1)).findByResolutionDateAfter(date);
    }

    @Test
    void findByResolutionDateAfter_Error_NullParameter() {
        when(scheduleChangeRequestRepository.findByResolutionDateAfter(null)).thenReturn(Collections.emptyList());

        List<ScheduleChangeRequest> result = scheduleChangeRequestRepository.findByResolutionDateAfter(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(scheduleChangeRequestRepository, times(1)).findByResolutionDateAfter(null);
    }

    @Test
    void countByStudentIdAndStatus_HappyPath_ReturnsCount() {
        String studentId = "ST001";
        when(scheduleChangeRequestRepository.countByStudentIdAndStatus(studentId, RequestStatus.PENDING))
                .thenReturn(2L);

        long result = scheduleChangeRequestRepository.countByStudentIdAndStatus(studentId, RequestStatus.PENDING);

        assertEquals(2L, result);
        verify(scheduleChangeRequestRepository, times(1)).countByStudentIdAndStatus(studentId, RequestStatus.PENDING);
    }

    @Test
    void countByStudentIdAndStatus_HappyPath_DifferentStatus() {
        String studentId = "ST001";
        when(scheduleChangeRequestRepository.countByStudentIdAndStatus(studentId, RequestStatus.APPROVED))
                .thenReturn(1L);

        long result = scheduleChangeRequestRepository.countByStudentIdAndStatus(studentId, RequestStatus.APPROVED);

        assertEquals(1L, result);
        verify(scheduleChangeRequestRepository, times(1)).countByStudentIdAndStatus(studentId, RequestStatus.APPROVED);
    }

    @Test
    void countByStudentIdAndStatus_Error_ZeroCount() {
        String studentId = "ST999";
        when(scheduleChangeRequestRepository.countByStudentIdAndStatus(studentId, RequestStatus.PENDING))
                .thenReturn(0L);

        long result = scheduleChangeRequestRepository.countByStudentIdAndStatus(studentId, RequestStatus.PENDING);

        assertEquals(0L, result);
        verify(scheduleChangeRequestRepository, times(1)).countByStudentIdAndStatus(studentId, RequestStatus.PENDING);
    }

    @Test
    void countByStudentIdAndStatus_Error_NullParameters() {
        when(scheduleChangeRequestRepository.countByStudentIdAndStatus(null, null))
                .thenReturn(0L);

        long result = scheduleChangeRequestRepository.countByStudentIdAndStatus(null, null);

        assertEquals(0L, result);
        verify(scheduleChangeRequestRepository, times(1)).countByStudentIdAndStatus(null, null);
    }
}
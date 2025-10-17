package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Course;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.CourseStatusDetail;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Group;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Professor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseStatusDetailRepositoryTest {

    @Mock
    private CourseStatusDetailRepository repository;

    private CourseStatusDetail detail1;
    private CourseStatusDetail detail2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        detail1 = new CourseStatusDetail(
                "1", new Course(), null, 4.2, "2024-2",
                new Date(120, 5, 10), new Date(120, 12, 20),
                new Group(), new Professor(), 3, true, "Buen desempeño");
        detail2 = new CourseStatusDetail(
                "2", new Course(), null, 2.5, "2025-1",
                new Date(121, 1, 10), new Date(121, 5, 20),
                new Group(), new Professor(), 2, false, "Debe mejorar");
    }

    @Test
    void testFindBySemester_HappyPath() {
        when(repository.findBySemester("2024-2")).thenReturn(Arrays.asList(detail1));
        List<CourseStatusDetail> result = repository.findBySemester("2024-2");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("2024-2", result.get(0).getSemester());
        verify(repository, times(1)).findBySemester("2024-2");
    }

    @Test
    void testFindBySemester_ErrorPath() {
        when(repository.findBySemester("2030-1")).thenReturn(Collections.emptyList());
        List<CourseStatusDetail> result = repository.findBySemester("2030-1");
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findBySemester("2030-1");
    }

    @Test
    void testFindByIsApproved_HappyPath() {
        when(repository.findByIsApproved(true)).thenReturn(Arrays.asList(detail1));
        List<CourseStatusDetail> result = repository.findByIsApproved(true);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).isApproved());
        verify(repository, times(1)).findByIsApproved(true);
    }

    @Test
    void testFindByIsApproved_ErrorPath() {
        when(repository.findByIsApproved(false)).thenReturn(Collections.emptyList());
        List<CourseStatusDetail> result = repository.findByIsApproved(false);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findByIsApproved(false);
    }

    @Test
    void testFindByGradeGreaterThanEqual_HappyPath() {
        when(repository.findByGradeGreaterThanEqual(4.0)).thenReturn(Arrays.asList(detail1));
        List<CourseStatusDetail> result = repository.findByGradeGreaterThanEqual(4.0);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getGrade() >= 4.0);
        verify(repository, times(1)).findByGradeGreaterThanEqual(4.0);
    }

    @Test
    void testFindByGradeGreaterThanEqual_ErrorPath() {
        when(repository.findByGradeGreaterThanEqual(5.0)).thenReturn(Collections.emptyList());
        List<CourseStatusDetail> result = repository.findByGradeGreaterThanEqual(5.0);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findByGradeGreaterThanEqual(5.0);
    }

    @Test
    void testFindByCompletionDateBefore_HappyPath() {
        Date limit = new Date(121, 1, 1);
        when(repository.findByCompletionDateBefore(limit)).thenReturn(Arrays.asList(detail1));
        List<CourseStatusDetail> result = repository.findByCompletionDateBefore(limit);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getCompletionDate().before(limit));
        verify(repository, times(1)).findByCompletionDateBefore(limit);
    }

    @Test
    void testFindByCompletionDateBefore_ErrorPath() {
        Date limit = new Date(100, 1, 1);
        when(repository.findByCompletionDateBefore(limit)).thenReturn(Collections.emptyList());
        List<CourseStatusDetail> result = repository.findByCompletionDateBefore(limit);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findByCompletionDateBefore(limit);
    }

    @Test
    void testFindByEnrollmentDateAfter_HappyPath() {
        Date limit = new Date(119, 1, 1);
        when(repository.findByEnrollmentDateAfter(limit)).thenReturn(Arrays.asList(detail2));
        List<CourseStatusDetail> result = repository.findByEnrollmentDateAfter(limit);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getEnrollmentDate().after(limit));
        verify(repository, times(1)).findByEnrollmentDateAfter(limit);
    }

    @Test
    void testFindByEnrollmentDateAfter_ErrorPath() {
        Date limit = new Date(130, 1, 1);
        when(repository.findByEnrollmentDateAfter(limit)).thenReturn(Collections.emptyList());
        List<CourseStatusDetail> result = repository.findByEnrollmentDateAfter(limit);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findByEnrollmentDateAfter(limit);
    }
}

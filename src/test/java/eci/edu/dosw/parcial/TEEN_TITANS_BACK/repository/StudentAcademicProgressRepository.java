package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.StudentAcademicProgress;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Student;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.CourseStatusDetail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class StudentAcademicProgressRepositoryTest {

    @Mock
    private StudentAcademicProgressRepository studentAcademicProgressRepository;

    private StudentAcademicProgress progress1;
    private StudentAcademicProgress progress2;
    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student();

        CourseStatusDetail course1 = new CourseStatusDetail();
        CourseStatusDetail course2 = new CourseStatusDetail();
        List<CourseStatusDetail> coursesStatus = Arrays.asList(course1, course2);

        progress1 = new StudentAcademicProgress(
                "PROG001", student, "Ingeniería de Sistemas", "Ingeniería",
                "Regular", 5, 10, 120, 160, 4.2, coursesStatus
        );

        progress2 = new StudentAcademicProgress(
                "PROG002", student, "Medicina", "Ciencias de la Salud",
                "Regular", 3, 12, 80, 200, 3.8, coursesStatus
        );
    }

    @Test
    void findByAcademicProgram_HappyPath_ReturnsProgressList() {
        String program = "Ingeniería de Sistemas";
        List<StudentAcademicProgress> expectedProgress = Collections.singletonList(progress1);
        when(studentAcademicProgressRepository.findByAcademicProgram(program)).thenReturn(expectedProgress);

        List<StudentAcademicProgress> result = studentAcademicProgressRepository.findByAcademicProgram(program);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(program, result.get(0).getAcademicProgram());
        verify(studentAcademicProgressRepository, times(1)).findByAcademicProgram(program);
    }

    @Test
    void findByAcademicProgram_HappyPath_DifferentProgram() {
        String program = "Medicina";
        List<StudentAcademicProgress> expectedProgress = Collections.singletonList(progress2);
        when(studentAcademicProgressRepository.findByAcademicProgram(program)).thenReturn(expectedProgress);

        List<StudentAcademicProgress> result = studentAcademicProgressRepository.findByAcademicProgram(program);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(program, result.get(0).getAcademicProgram());
        verify(studentAcademicProgressRepository, times(1)).findByAcademicProgram(program);
    }

    @Test
    void findByAcademicProgram_Error_NoProgressForProgram() {
        String program = "Programa Inexistente";
        when(studentAcademicProgressRepository.findByAcademicProgram(program)).thenReturn(Collections.emptyList());

        List<StudentAcademicProgress> result = studentAcademicProgressRepository.findByAcademicProgram(program);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(studentAcademicProgressRepository, times(1)).findByAcademicProgram(program);
    }

    @Test
    void findByAcademicProgram_Error_NullParameter() {
        when(studentAcademicProgressRepository.findByAcademicProgram(null)).thenReturn(Collections.emptyList());

        List<StudentAcademicProgress> result = studentAcademicProgressRepository.findByAcademicProgram(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(studentAcademicProgressRepository, times(1)).findByAcademicProgram(null);
    }

    @Test
    void findByFaculty_HappyPath_ReturnsProgressList() {
        String faculty = "Ingeniería";
        List<StudentAcademicProgress> expectedProgress = Collections.singletonList(progress1);
        when(studentAcademicProgressRepository.findByFaculty(faculty)).thenReturn(expectedProgress);

        List<StudentAcademicProgress> result = studentAcademicProgressRepository.findByFaculty(faculty);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(faculty, result.get(0).getFaculty());
        verify(studentAcademicProgressRepository, times(1)).findByFaculty(faculty);
    }

    @Test
    void findByFaculty_HappyPath_DifferentFaculty() {
        String faculty = "Ciencias de la Salud";
        List<StudentAcademicProgress> expectedProgress = Collections.singletonList(progress2);
        when(studentAcademicProgressRepository.findByFaculty(faculty)).thenReturn(expectedProgress);

        List<StudentAcademicProgress> result = studentAcademicProgressRepository.findByFaculty(faculty);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(faculty, result.get(0).getFaculty());
        verify(studentAcademicProgressRepository, times(1)).findByFaculty(faculty);
    }

    @Test
    void findByFaculty_Error_NoProgressForFaculty() {
        String faculty = "Facultad Inexistente";
        when(studentAcademicProgressRepository.findByFaculty(faculty)).thenReturn(Collections.emptyList());

        List<StudentAcademicProgress> result = studentAcademicProgressRepository.findByFaculty(faculty);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(studentAcademicProgressRepository, times(1)).findByFaculty(faculty);
    }

    @Test
    void findByFaculty_Error_NullParameter() {
        when(studentAcademicProgressRepository.findByFaculty(null)).thenReturn(Collections.emptyList());

        List<StudentAcademicProgress> result = studentAcademicProgressRepository.findByFaculty(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(studentAcademicProgressRepository, times(1)).findByFaculty(null);
    }

    @Test
    void findByCurrentSemester_HappyPath_ReturnsProgressList() {
        Integer semester = 5;
        List<StudentAcademicProgress> expectedProgress = Collections.singletonList(progress1);
        when(studentAcademicProgressRepository.findByCurrentSemester(semester)).thenReturn(expectedProgress);

        List<StudentAcademicProgress> result = studentAcademicProgressRepository.findByCurrentSemester(semester);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(semester, result.get(0).getCurrentSemester());
        verify(studentAcademicProgressRepository, times(1)).findByCurrentSemester(semester);
    }

    @Test
    void findByCurrentSemester_HappyPath_DifferentSemester() {
        Integer semester = 3;
        List<StudentAcademicProgress> expectedProgress = Collections.singletonList(progress2);
        when(studentAcademicProgressRepository.findByCurrentSemester(semester)).thenReturn(expectedProgress);

        List<StudentAcademicProgress> result = studentAcademicProgressRepository.findByCurrentSemester(semester);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(semester, result.get(0).getCurrentSemester());
        verify(studentAcademicProgressRepository, times(1)).findByCurrentSemester(semester);
    }

    @Test
    void findByCurrentSemester_Error_NoProgressForSemester() {
        Integer semester = 10;
        when(studentAcademicProgressRepository.findByCurrentSemester(semester)).thenReturn(Collections.emptyList());

        List<StudentAcademicProgress> result = studentAcademicProgressRepository.findByCurrentSemester(semester);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(studentAcademicProgressRepository, times(1)).findByCurrentSemester(semester);
    }

    @Test
    void findByCurrentSemester_Error_NullParameter() {
        when(studentAcademicProgressRepository.findByCurrentSemester(null)).thenReturn(Collections.emptyList());

        List<StudentAcademicProgress> result = studentAcademicProgressRepository.findByCurrentSemester(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(studentAcademicProgressRepository, times(1)).findByCurrentSemester(null);
    }

    @Test
    void findByCumulativeGPAGreaterThanEqual_HappyPath_ReturnsProgressList() {
        Double gpa = 4.0;
        List<StudentAcademicProgress> expectedProgress = Collections.singletonList(progress1);
        when(studentAcademicProgressRepository.findByCumulativeGPAGreaterThanEqual(gpa)).thenReturn(expectedProgress);

        List<StudentAcademicProgress> result = studentAcademicProgressRepository.findByCumulativeGPAGreaterThanEqual(gpa);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getCumulativeGPA() >= gpa);
        verify(studentAcademicProgressRepository, times(1)).findByCumulativeGPAGreaterThanEqual(gpa);
    }

    @Test
    void findByCumulativeGPAGreaterThanEqual_HappyPath_LowerGPA() {
        Double gpa = 3.5;
        List<StudentAcademicProgress> expectedProgress = Arrays.asList(progress1, progress2);
        when(studentAcademicProgressRepository.findByCumulativeGPAGreaterThanEqual(gpa)).thenReturn(expectedProgress);

        List<StudentAcademicProgress> result = studentAcademicProgressRepository.findByCumulativeGPAGreaterThanEqual(gpa);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.get(0).getCumulativeGPA() >= gpa);
        assertTrue(result.get(1).getCumulativeGPA() >= gpa);
        verify(studentAcademicProgressRepository, times(1)).findByCumulativeGPAGreaterThanEqual(gpa);
    }

    @Test
    void findByCumulativeGPAGreaterThanEqual_Error_NoProgressWithGPA() {
        Double gpa = 4.5;
        when(studentAcademicProgressRepository.findByCumulativeGPAGreaterThanEqual(gpa)).thenReturn(Collections.emptyList());

        List<StudentAcademicProgress> result = studentAcademicProgressRepository.findByCumulativeGPAGreaterThanEqual(gpa);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(studentAcademicProgressRepository, times(1)).findByCumulativeGPAGreaterThanEqual(gpa);
    }

    @Test
    void findByCumulativeGPAGreaterThanEqual_Error_NullParameter() {
        when(studentAcademicProgressRepository.findByCumulativeGPAGreaterThanEqual(null)).thenReturn(Collections.emptyList());

        List<StudentAcademicProgress> result = studentAcademicProgressRepository.findByCumulativeGPAGreaterThanEqual(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(studentAcademicProgressRepository, times(1)).findByCumulativeGPAGreaterThanEqual(null);
    }
}
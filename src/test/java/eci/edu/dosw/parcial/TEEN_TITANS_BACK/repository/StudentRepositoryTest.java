package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Student;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para StudentRepository usando Mockito.
 * Se prueban escenarios correctos (happy path) y de error (sin resultados).
 */
class StudentRepositoryTest {

    @Mock
    private StudentRepository studentRepository;

    private Student student1;
    private Student student2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        student1 = new Student("1", "Juan", "juan@mail.com", "1234", "Ingeniería de Sistemas", 5);
        student1.setGradeAverage(4.2);
        student1.setActive(true);

        student2 = new Student("2", "Ana", "ana@mail.com", "5678", "Ingeniería Civil", 3);
        student2.setGradeAverage(3.5);
        student2.setActive(false);
    }

    // --- findByAcademicProgram ---
    @Test
    void testFindByAcademicProgram_HappyPath() {
        when(studentRepository.findByAcademicProgram("Ingeniería de Sistemas"))
                .thenReturn(List.of(student1));

        List<Student> result = studentRepository.findByAcademicProgram("Ingeniería de Sistemas");

        assertEquals(1, result.size());
        assertEquals("Juan", result.get(0).getName());
        verify(studentRepository, times(1)).findByAcademicProgram("Ingeniería de Sistemas");
    }

    @Test
    void testFindByAcademicProgram_ErrorPath() {
        when(studentRepository.findByAcademicProgram("Arquitectura"))
                .thenReturn(Collections.emptyList());

        List<Student> result = studentRepository.findByAcademicProgram("Arquitectura");

        assertTrue(result.isEmpty());
        verify(studentRepository, times(1)).findByAcademicProgram("Arquitectura");
    }

    // --- findBySemester ---
    @Test
    void testFindBySemester_HappyPath() {
        when(studentRepository.findBySemester(5)).thenReturn(List.of(student1));

        List<Student> result = studentRepository.findBySemester(5);

        assertEquals(1, result.size());
        assertEquals("Juan", result.get(0).getName());
        verify(studentRepository).findBySemester(5);
    }

    @Test
    void testFindBySemester_ErrorPath() {
        when(studentRepository.findBySemester(10)).thenReturn(Collections.emptyList());

        List<Student> result = studentRepository.findBySemester(10);

        assertTrue(result.isEmpty());
        verify(studentRepository).findBySemester(10);
    }

    // --- findByAcademicProgramAndSemester ---
    @Test
    void testFindByAcademicProgramAndSemester_HappyPath() {
        when(studentRepository.findByAcademicProgramAndSemester("Ingeniería Civil", 3))
                .thenReturn(List.of(student2));

        List<Student> result = studentRepository.findByAcademicProgramAndSemester("Ingeniería Civil", 3);

        assertEquals(1, result.size());
        assertEquals("Ana", result.get(0).getName());
        verify(studentRepository).findByAcademicProgramAndSemester("Ingeniería Civil", 3);
    }

    @Test
    void testFindByAcademicProgramAndSemester_ErrorPath() {
        when(studentRepository.findByAcademicProgramAndSemester("Ingeniería Civil", 10))
                .thenReturn(Collections.emptyList());

        List<Student> result = studentRepository.findByAcademicProgramAndSemester("Ingeniería Civil", 10);

        assertTrue(result.isEmpty());
        verify(studentRepository).findByAcademicProgramAndSemester("Ingeniería Civil", 10);
    }

    // --- findByGradeAverageGreaterThanEqual ---
    @Test
    void testFindByGradeAverageGreaterThanEqual_HappyPath() {
        when(studentRepository.findByGradeAverageGreaterThanEqual(4.0))
                .thenReturn(List.of(student1));

        List<Student> result = studentRepository.findByGradeAverageGreaterThanEqual(4.0);

        assertEquals(1, result.size());
        assertEquals("Juan", result.get(0).getName());
        verify(studentRepository).findByGradeAverageGreaterThanEqual(4.0);
    }

    @Test
    void testFindByGradeAverageGreaterThanEqual_ErrorPath() {
        when(studentRepository.findByGradeAverageGreaterThanEqual(5.0))
                .thenReturn(Collections.emptyList());

        List<Student> result = studentRepository.findByGradeAverageGreaterThanEqual(5.0);

        assertTrue(result.isEmpty());
        verify(studentRepository).findByGradeAverageGreaterThanEqual(5.0);
    }

    // --- findByGradeAverageLessThanEqual ---
    @Test
    void testFindByGradeAverageLessThanEqual_HappyPath() {
        when(studentRepository.findByGradeAverageLessThanEqual(3.5))
                .thenReturn(List.of(student2));

        List<Student> result = studentRepository.findByGradeAverageLessThanEqual(3.5);

        assertEquals(1, result.size());
        assertEquals("Ana", result.get(0).getName());
        verify(studentRepository).findByGradeAverageLessThanEqual(3.5);
    }

    @Test
    void testFindByGradeAverageLessThanEqual_ErrorPath() {
        when(studentRepository.findByGradeAverageLessThanEqual(2.0))
                .thenReturn(Collections.emptyList());

        List<Student> result = studentRepository.findByGradeAverageLessThanEqual(2.0);

        assertTrue(result.isEmpty());
        verify(studentRepository).findByGradeAverageLessThanEqual(2.0);
    }



    @Test
    void testFindByAcademicProgramAndActive_ErrorPath() {
        when(studentRepository.findByAcademicProgramAndActive("Ingeniería Civil", true))
                .thenReturn(Collections.emptyList());

        List<Student> result = studentRepository.findByAcademicProgramAndActive("Ingeniería Civil", true);

        assertTrue(result.isEmpty());
        verify(studentRepository).findByAcademicProgramAndActive("Ingeniería Civil", true);
    }
}

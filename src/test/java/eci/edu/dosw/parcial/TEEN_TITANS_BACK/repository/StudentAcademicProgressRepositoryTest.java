package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.StudentAcademicProgress;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Student;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.CourseStatusDetail;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;
import java.util.Collections;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataMongoTest
public class StudentAcademicProgressRepositoryTest {

    @MockBean
    private StudentAcademicProgressRepository studentAcademicProgressRepository;

    private Student student1;
    private Student student2;
    private Student student3;
    private CourseStatusDetail courseStatus1;
    private CourseStatusDetail courseStatus2;
    private StudentAcademicProgress progress1;
    private StudentAcademicProgress progress2;
    private StudentAcademicProgress progress3;

    @BeforeEach
    void setUp() {
        student1 = new Student("STU001", "John Doe", "john.doe@university.edu", "password", "Computer Science", 5);
        student1.setGradeAverage(4.2);

        student2 = new Student("STU002", "Jane Smith", "jane.smith@university.edu", "password", "Computer Science", 3);
        student2.setGradeAverage(3.5);

        student3 = new Student("STU003", "Bob Johnson", "bob.johnson@university.edu", "password", "Mathematics", 4);
        student3.setGradeAverage(3.8);

        courseStatus1 = new CourseStatusDetail();
        courseStatus2 = new CourseStatusDetail();

        progress1 = new StudentAcademicProgress(
                "PROG001", student1, "Computer Science", "Engineering", "Regular",
                5, 10, 120, 160, 4.2, Arrays.asList(courseStatus1, courseStatus2)
        );

        progress2 = new StudentAcademicProgress(
                "PROG002", student2, "Computer Science", "Engineering", "Regular",
                3, 10, 80, 160, 3.5, Arrays.asList(courseStatus1)
        );

        progress3 = new StudentAcademicProgress(
                "PROG003", student3, "Mathematics", "Science", "Regular",
                4, 8, 96, 128, 3.8, Arrays.asList(courseStatus2)
        );
    }

    @Test
    @DisplayName("Caso exitoso - findByAcademicProgram retorna progresos del programa")
    void testFindByAcademicProgram_Exitoso() {
        when(studentAcademicProgressRepository.findByAcademicProgram("Computer Science"))
                .thenReturn(List.of(progress1, progress2));

        List<StudentAcademicProgress> resultado = studentAcademicProgressRepository.findByAcademicProgram("Computer Science");

        assertAll("Verificar progresos de Computer Science",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals("Computer Science", resultado.get(0).getAcademicProgram()),
                () -> assertEquals("Computer Science", resultado.get(1).getAcademicProgram())
        );

        verify(studentAcademicProgressRepository, times(1)).findByAcademicProgram("Computer Science");
    }

    @Test
    @DisplayName("Caso error - findByAcademicProgram retorna lista vacía para programa inexistente")
    void testFindByAcademicProgram_ProgramaInexistente() {
        when(studentAcademicProgressRepository.findByAcademicProgram("Physics"))
                .thenReturn(Collections.emptyList());

        List<StudentAcademicProgress> resultado = studentAcademicProgressRepository.findByAcademicProgram("Physics");

        assertTrue(resultado.isEmpty());
        verify(studentAcademicProgressRepository, times(1)).findByAcademicProgram("Physics");
    }

    @Test
    @DisplayName("Caso exitoso - findByFaculty retorna progresos por facultad")
    void testFindByFaculty_Exitoso() {
        when(studentAcademicProgressRepository.findByFaculty("Engineering"))
                .thenReturn(List.of(progress1, progress2));

        List<StudentAcademicProgress> resultado = studentAcademicProgressRepository.findByFaculty("Engineering");

        assertAll("Verificar progresos de Engineering",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals("Engineering", resultado.get(0).getFaculty()),
                () -> assertEquals("Engineering", resultado.get(1).getFaculty())
        );

        verify(studentAcademicProgressRepository, times(1)).findByFaculty("Engineering");
    }

    @Test
    @DisplayName("Caso exitoso - findByCurrentSemester retorna progresos por semestre")
    void testFindByCurrentSemester_Exitoso() {
        when(studentAcademicProgressRepository.findByCurrentSemester(5))
                .thenReturn(List.of(progress1));

        List<StudentAcademicProgress> resultado = studentAcademicProgressRepository.findByCurrentSemester(5);

        assertAll("Verificar progresos del semestre 5",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals(5, resultado.get(0).getCurrentSemester())
        );

        verify(studentAcademicProgressRepository, times(1)).findByCurrentSemester(5);
    }

    @Test
    @DisplayName("Caso exitoso - findByStudentId retorna progreso del estudiante")
    void testFindByStudentId_Exitoso() {
        when(studentAcademicProgressRepository.findByStudentId("STU001"))
                .thenReturn(Optional.of(progress1));

        Optional<StudentAcademicProgress> resultado = studentAcademicProgressRepository.findByStudentId("STU001");

        assertAll("Verificar progreso del estudiante STU001",
                () -> assertTrue(resultado.isPresent()),
                () -> assertEquals("STU001", resultado.get().getStudent().getId()),
                () -> assertEquals("John Doe", resultado.get().getStudent().getName())
        );

        verify(studentAcademicProgressRepository, times(1)).findByStudentId("STU001");
    }

    @Test
    @DisplayName("Caso error - findByStudentId retorna vacío para estudiante inexistente")
    void testFindByStudentId_EstudianteInexistente() {
        when(studentAcademicProgressRepository.findByStudentId("STU999"))
                .thenReturn(Optional.empty());

        Optional<StudentAcademicProgress> resultado = studentAcademicProgressRepository.findByStudentId("STU999");

        assertFalse(resultado.isPresent());
        verify(studentAcademicProgressRepository, times(1)).findByStudentId("STU999");
    }

    @Test
    @DisplayName("Caso exitoso - findByCumulativeGPAGreaterThanEqual retorna estudiantes con GPA alto")
    void testFindByCumulativeGPAGreaterThanEqual_Exitoso() {
        when(studentAcademicProgressRepository.findByCumulativeGPAGreaterThanEqual(4.0))
                .thenReturn(List.of(progress1));

        List<StudentAcademicProgress> resultado = studentAcademicProgressRepository.findByCumulativeGPAGreaterThanEqual(4.0);

        assertAll("Verificar estudiantes con GPA >= 4.0",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertTrue(resultado.get(0).getCumulativeGPA() >= 4.0)
        );

        verify(studentAcademicProgressRepository, times(1)).findByCumulativeGPAGreaterThanEqual(4.0);
    }

    @Test
    @DisplayName("Caso exitoso - findByCumulativeGPALessThanEqual retorna estudiantes con GPA bajo")
    void testFindByCumulativeGPALessThanEqual_Exitoso() {
        when(studentAcademicProgressRepository.findByCumulativeGPALessThanEqual(3.6))
                .thenReturn(List.of(progress2));

        List<StudentAcademicProgress> resultado = studentAcademicProgressRepository.findByCumulativeGPALessThanEqual(3.6);

        assertAll("Verificar estudiantes con GPA <= 3.6",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertTrue(resultado.get(0).getCumulativeGPA() <= 3.6)
        );

        verify(studentAcademicProgressRepository, times(1)).findByCumulativeGPALessThanEqual(3.6);
    }



    @Test
    @DisplayName("Caso exitoso - findByCurrentSemesterBetween retorna estudiantes en rango de semestres")
    void testFindByCurrentSemesterBetween_Exitoso() {
        when(studentAcademicProgressRepository.findByCurrentSemesterBetween(3, 5))
                .thenReturn(List.of(progress1, progress2, progress3));

        List<StudentAcademicProgress> resultado = studentAcademicProgressRepository.findByCurrentSemesterBetween(3, 5);

        assertAll("Verificar estudiantes en rango de semestres",
                () -> assertNotNull(resultado),
                () -> assertEquals(3, resultado.size()),
                () -> assertTrue(resultado.get(0).getCurrentSemester() >= 3 && resultado.get(0).getCurrentSemester() <= 5),
                () -> assertTrue(resultado.get(1).getCurrentSemester() >= 3 && resultado.get(1).getCurrentSemester() <= 5),
                () -> assertTrue(resultado.get(2).getCurrentSemester() >= 3 && resultado.get(2).getCurrentSemester() <= 5)
        );

        verify(studentAcademicProgressRepository, times(1)).findByCurrentSemesterBetween(3, 5);
    }



    @Test
    @DisplayName("Caso exitoso - findByAcademicProgramAndFaculty retorna progresos filtrados")
    void testFindByAcademicProgramAndFaculty_Exitoso() {
        when(studentAcademicProgressRepository.findByAcademicProgramAndFaculty("Computer Science", "Engineering"))
                .thenReturn(List.of(progress1, progress2));

        List<StudentAcademicProgress> resultado = studentAcademicProgressRepository.findByAcademicProgramAndFaculty("Computer Science", "Engineering");

        assertAll("Verificar progresos por programa y facultad",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals("Computer Science", resultado.get(0).getAcademicProgram()),
                () -> assertEquals("Engineering", resultado.get(0).getFaculty())
        );

        verify(studentAcademicProgressRepository, times(1)).findByAcademicProgramAndFaculty("Computer Science", "Engineering");
    }

    @Test
    @DisplayName("Caso exitoso - findByAcademicProgramAndCurrentSemester retorna progresos por programa y semestre")
    void testFindByAcademicProgramAndCurrentSemester_Exitoso() {
        when(studentAcademicProgressRepository.findByAcademicProgramAndCurrentSemester("Computer Science", 5))
                .thenReturn(List.of(progress1));

        List<StudentAcademicProgress> resultado = studentAcademicProgressRepository.findByAcademicProgramAndCurrentSemester("Computer Science", 5);

        assertAll("Verificar progresos por programa y semestre",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("Computer Science", resultado.get(0).getAcademicProgram()),
                () -> assertEquals(5, resultado.get(0).getCurrentSemester())
        );

        verify(studentAcademicProgressRepository, times(1)).findByAcademicProgramAndCurrentSemester("Computer Science", 5);
    }

    @Test
    @DisplayName("Caso exitoso - findByAcademicProgramAndCumulativeGPAGreaterThanEqual retorna estudiantes destacados")
    void testFindByAcademicProgramAndCumulativeGPAGreaterThanEqual_Exitoso() {
        when(studentAcademicProgressRepository.findByAcademicProgramAndCumulativeGPAGreaterThanEqual("Computer Science", 4.0))
                .thenReturn(List.of(progress1));

        List<StudentAcademicProgress> resultado = studentAcademicProgressRepository.findByAcademicProgramAndCumulativeGPAGreaterThanEqual("Computer Science", 4.0);

        assertAll("Verificar estudiantes destacados por programa",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("Computer Science", resultado.get(0).getAcademicProgram()),
                () -> assertTrue(resultado.get(0).getCumulativeGPA() >= 4.0)
        );

        verify(studentAcademicProgressRepository, times(1)).findByAcademicProgramAndCumulativeGPAGreaterThanEqual("Computer Science", 4.0);
    }

    @Test
    @DisplayName("Caso exitoso - findByOrderByCumulativeGPADesc retorna estudiantes ordenados por GPA")
    void testFindByOrderByCumulativeGPADesc_Exitoso() {
        when(studentAcademicProgressRepository.findByOrderByCumulativeGPADesc())
                .thenReturn(List.of(progress1, progress3, progress2));

        List<StudentAcademicProgress> resultado = studentAcademicProgressRepository.findByOrderByCumulativeGPADesc();

        assertAll("Verificar estudiantes ordenados por GPA descendente",
                () -> assertNotNull(resultado),
                () -> assertEquals(3, resultado.size()),
                () -> assertTrue(resultado.get(0).getCumulativeGPA() >= resultado.get(1).getCumulativeGPA()),
                () -> assertTrue(resultado.get(1).getCumulativeGPA() >= resultado.get(2).getCumulativeGPA())
        );

        verify(studentAcademicProgressRepository, times(1)).findByOrderByCumulativeGPADesc();
    }

    @Test
    @DisplayName("Caso exitoso - countByAcademicProgram retorna conteo correcto")
    void testCountByAcademicProgram_Exitoso() {
        when(studentAcademicProgressRepository.countByAcademicProgram("Computer Science"))
                .thenReturn(2L);

        long resultado = studentAcademicProgressRepository.countByAcademicProgram("Computer Science");

        assertEquals(2L, resultado);
        verify(studentAcademicProgressRepository, times(1)).countByAcademicProgram("Computer Science");
    }

    @Test
    @DisplayName("Caso exitoso - countByFaculty retorna conteo por facultad")
    void testCountByFaculty_Exitoso() {
        when(studentAcademicProgressRepository.countByFaculty("Engineering"))
                .thenReturn(2L);

        long resultado = studentAcademicProgressRepository.countByFaculty("Engineering");

        assertEquals(2L, resultado);
        verify(studentAcademicProgressRepository, times(1)).countByFaculty("Engineering");
    }

    @Test
    @DisplayName("Caso exitoso - countByCurrentSemester retorna conteo por semestre")
    void testCountByCurrentSemester_Exitoso() {
        when(studentAcademicProgressRepository.countByCurrentSemester(5))
                .thenReturn(1L);

        long resultado = studentAcademicProgressRepository.countByCurrentSemester(5);

        assertEquals(1L, resultado);
        verify(studentAcademicProgressRepository, times(1)).countByCurrentSemester(5);
    }

    @Test
    @DisplayName("Caso exitoso - countByCumulativeGPAGreaterThanEqual retorna conteo por GPA")
    void testCountByCumulativeGPAGreaterThanEqual_Exitoso() {
        when(studentAcademicProgressRepository.countByCumulativeGPAGreaterThanEqual(3.5))
                .thenReturn(3L);

        long resultado = studentAcademicProgressRepository.countByCumulativeGPAGreaterThanEqual(3.5);

        assertEquals(3L, resultado);
        verify(studentAcademicProgressRepository, times(1)).countByCumulativeGPAGreaterThanEqual(3.5);
    }

    @Test
    @DisplayName("Caso exitoso - existsByStudentId retorna true cuando existe")
    void testExistsByStudentId_Exitoso() {
        when(studentAcademicProgressRepository.existsByStudentId("STU001"))
                .thenReturn(true);

        boolean resultado = studentAcademicProgressRepository.existsByStudentId("STU001");

        assertTrue(resultado);
        verify(studentAcademicProgressRepository, times(1)).existsByStudentId("STU001");
    }

    @Test
    @DisplayName("Caso error - existsByStudentId retorna false cuando no existe")
    void testExistsByStudentId_NoExiste() {
        when(studentAcademicProgressRepository.existsByStudentId("STU999"))
                .thenReturn(false);

        boolean resultado = studentAcademicProgressRepository.existsByStudentId("STU999");

        assertFalse(resultado);
        verify(studentAcademicProgressRepository, times(1)).existsByStudentId("STU999");
    }

    @Test
    @DisplayName("Caso exitoso - existsByAcademicProgramAndStudentId retorna true")
    void testExistsByAcademicProgramAndStudentId_Exitoso() {
        when(studentAcademicProgressRepository.existsByAcademicProgramAndStudentId("Computer Science", "STU001"))
                .thenReturn(true);

        boolean resultado = studentAcademicProgressRepository.existsByAcademicProgramAndStudentId("Computer Science", "STU001");

        assertTrue(resultado);
        verify(studentAcademicProgressRepository, times(1)).existsByAcademicProgramAndStudentId("Computer Science", "STU001");
    }

    @Test
    @DisplayName("Caso exitoso - findByAcademicProgramIn retorna progresos de múltiples programas")
    void testFindByAcademicProgramIn_Exitoso() {
        when(studentAcademicProgressRepository.findByAcademicProgramIn(List.of("Computer Science", "Mathematics")))
                .thenReturn(List.of(progress1, progress2, progress3));

        List<StudentAcademicProgress> resultado = studentAcademicProgressRepository.findByAcademicProgramIn(List.of("Computer Science", "Mathematics"));

        assertAll("Verificar progresos de múltiples programas",
                () -> assertNotNull(resultado),
                () -> assertEquals(3, resultado.size()),
                () -> assertTrue(resultado.stream().anyMatch(p -> p.getAcademicProgram().equals("Computer Science"))),
                () -> assertTrue(resultado.stream().anyMatch(p -> p.getAcademicProgram().equals("Mathematics")))
        );

        verify(studentAcademicProgressRepository, times(1)).findByAcademicProgramIn(List.of("Computer Science", "Mathematics"));
    }

    @Test
    @DisplayName("Caso exitoso - findByFacultyIn retorna progresos de múltiples facultades")
    void testFindByFacultyIn_Exitoso() {
        when(studentAcademicProgressRepository.findByFacultyIn(List.of("Engineering", "Science")))
                .thenReturn(List.of(progress1, progress2, progress3));

        List<StudentAcademicProgress> resultado = studentAcademicProgressRepository.findByFacultyIn(List.of("Engineering", "Science"));

        assertAll("Verificar progresos de múltiples facultades",
                () -> assertNotNull(resultado),
                () -> assertEquals(3, resultado.size()),
                () -> assertTrue(resultado.stream().anyMatch(p -> p.getFaculty().equals("Engineering"))),
                () -> assertTrue(resultado.stream().anyMatch(p -> p.getFaculty().equals("Science")))
        );

        verify(studentAcademicProgressRepository, times(1)).findByFacultyIn(List.of("Engineering", "Science"));
    }

    @Test
    @DisplayName("Caso exitoso - findFirstByStudentIdOrderByCumulativeGPADesc retorna mejor progreso")
    void testFindFirstByStudentIdOrderByCumulativeGPADesc_Exitoso() {
        when(studentAcademicProgressRepository.findFirstByStudentIdOrderByCumulativeGPADesc("STU001"))
                .thenReturn(Optional.of(progress1));

        Optional<StudentAcademicProgress> resultado = studentAcademicProgressRepository.findFirstByStudentIdOrderByCumulativeGPADesc("STU001");

        assertAll("Verificar mejor progreso del estudiante",
                () -> assertTrue(resultado.isPresent()),
                () -> assertEquals("STU001", resultado.get().getStudent().getId()),
                () -> assertEquals(4.2, resultado.get().getCumulativeGPA())
        );

        verify(studentAcademicProgressRepository, times(1)).findFirstByStudentIdOrderByCumulativeGPADesc("STU001");
    }

    @Test
    @DisplayName("Caso exitoso - findByAcademicProgramRegex con query personalizada")
    void testFindByAcademicProgramRegex_Exitoso() {
        when(studentAcademicProgressRepository.findByAcademicProgramRegex(".*Computer.*"))
                .thenReturn(List.of(progress1, progress2));

        List<StudentAcademicProgress> resultado = studentAcademicProgressRepository.findByAcademicProgramRegex(".*Computer.*");

        assertAll("Verificar búsqueda por regex de programa",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertTrue(resultado.get(0).getAcademicProgram().toLowerCase().contains("computer")),
                () -> assertTrue(resultado.get(1).getAcademicProgram().toLowerCase().contains("computer"))
        );

        verify(studentAcademicProgressRepository, times(1)).findByAcademicProgramRegex(".*Computer.*");
    }


    @Test
    @DisplayName("Caso exitoso - findHighAchieversByProgram con query personalizada")
    void testFindHighAchieversByProgram_Exitoso() {
        when(studentAcademicProgressRepository.findHighAchieversByProgram("Computer Science", 4.0))
                .thenReturn(List.of(progress1));

        List<StudentAcademicProgress> resultado = studentAcademicProgressRepository.findHighAchieversByProgram("Computer Science", 4.0);

        assertAll("Verificar estudiantes destacados por programa",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("Computer Science", resultado.get(0).getAcademicProgram()),
                () -> assertTrue(resultado.get(0).getCumulativeGPA() >= 4.0)
        );

        verify(studentAcademicProgressRepository, times(1)).findHighAchieversByProgram("Computer Science", 4.0);
    }

    @Test
    @DisplayName("Caso exitoso - findByAcademicProgramSortedByGPA con query personalizada")
    void testFindByAcademicProgramSortedByGPA_Exitoso() {
        when(studentAcademicProgressRepository.findByAcademicProgramSortedByGPA("Computer Science"))
                .thenReturn(List.of(progress1, progress2));

        List<StudentAcademicProgress> resultado = studentAcademicProgressRepository.findByAcademicProgramSortedByGPA("Computer Science");

        assertAll("Verificar estudiantes ordenados por GPA",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertTrue(resultado.get(0).getCumulativeGPA() >= resultado.get(1).getCumulativeGPA()),
                () -> assertEquals("Computer Science", resultado.get(0).getAcademicProgram())
        );

        verify(studentAcademicProgressRepository, times(1)).findByAcademicProgramSortedByGPA("Computer Science");
    }

    @Test
    @DisplayName("Caso borde - findByAcademicProgram con programa nulo")
    void testFindByAcademicProgram_Nulo() {
        when(studentAcademicProgressRepository.findByAcademicProgram(null))
                .thenReturn(Collections.emptyList());

        List<StudentAcademicProgress> resultado = studentAcademicProgressRepository.findByAcademicProgram(null);

        assertTrue(resultado.isEmpty());
        verify(studentAcademicProgressRepository, times(1)).findByAcademicProgram(null);
    }

    @Test
    @DisplayName("Caso borde - findByCurrentSemester con semestre nulo")
    void testFindByCurrentSemester_Nulo() {
        when(studentAcademicProgressRepository.findByCurrentSemester(null))
                .thenReturn(Collections.emptyList());

        List<StudentAcademicProgress> resultado = studentAcademicProgressRepository.findByCurrentSemester(null);

        assertTrue(resultado.isEmpty());
        verify(studentAcademicProgressRepository, times(1)).findByCurrentSemester(null);
    }

    @Test
    @DisplayName("Caso borde - Verificar integridad de datos en progresos encontrados")
    void testIntegridadDatosProgresos() {
        when(studentAcademicProgressRepository.findByStudentId("STU001"))
                .thenReturn(Optional.of(progress1));

        Optional<StudentAcademicProgress> resultado = studentAcademicProgressRepository.findByStudentId("STU001");

        assertAll("Verificar integridad completa del progreso académico",
                () -> assertTrue(resultado.isPresent()),
                () -> assertEquals("PROG001", resultado.get().getId()),
                () -> assertNotNull(resultado.get().getStudent()),
                () -> assertEquals("Computer Science", resultado.get().getAcademicProgram()),
                () -> assertEquals("Engineering", resultado.get().getFaculty()),
                () -> assertEquals("Regular", resultado.get().getCurriculumType()),
                () -> assertEquals(5, resultado.get().getCurrentSemester()),
                () -> assertEquals(10, resultado.get().getTotalSemesters()),
                () -> assertEquals(120, resultado.get().getCompletedCredits()),
                () -> assertEquals(160, resultado.get().getTotalCreditsRequired()),
                () -> assertEquals(4.2, resultado.get().getCumulativeGPA()),
                () -> assertNotNull(resultado.get().getCoursesStatus()),
                () -> assertInstanceOf(StudentAcademicProgress.class, resultado.get()),
                () -> assertNotNull(resultado.get().toString())
        );

        verify(studentAcademicProgressRepository, times(1)).findByStudentId("STU001");
    }


    @Test
    @DisplayName("Caso exitoso - findTopStudentsByProgramAndSemester con query personalizada")
    void testFindTopStudentsByProgramAndSemester_Exitoso() {
        when(studentAcademicProgressRepository.findTopStudentsByProgramAndSemester("Computer Science", 5, 4.0))
                .thenReturn(List.of(progress1));

        List<StudentAcademicProgress> resultado = studentAcademicProgressRepository.findTopStudentsByProgramAndSemester("Computer Science", 5, 4.0);

        assertAll("Verificar mejores estudiantes por programa y semestre",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("Computer Science", resultado.get(0).getAcademicProgram()),
                () -> assertEquals(5, resultado.get(0).getCurrentSemester()),
                () -> assertTrue(resultado.get(0).getCumulativeGPA() >= 4.0)
        );

        verify(studentAcademicProgressRepository, times(1)).findTopStudentsByProgramAndSemester("Computer Science", 5, 4.0);
    }
}
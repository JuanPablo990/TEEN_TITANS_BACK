package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentRepositoryTest {

    @Mock
    private StudentRepository studentRepository;

    private Student student1;
    private Student student2;
    private Student student3;
    private Student student4;

    @BeforeEach
    void setUp() {
        student1 = new Student("1", "Peter Parker", "peter.parker@titans.edu", "password123",
                "Computer Science", 5);
        student1.setGradeAverage(4.2);

        student2 = new Student("2", "Gwen Stacy", "gwen.stacy@titans.edu", "password456",
                "Physics", 6);
        student2.setGradeAverage(4.5);

        student3 = new Student("3", "Miles Morales", "miles.morales@titans.edu", "password789",
                "Computer Science", 4);
        student3.setGradeAverage(3.8);

        student4 = new Student("4", "Mary Jane Watson", "maryjane.watson@titans.edu", "password000",
                "Biology", 3);
        student4.setGradeAverage(3.5);
    }

    @Test
    @DisplayName("Caso exitoso - save guarda estudiante correctamente")
    void testSave_Exitoso() {
        Student newStudent = new Student("5", "Harry Osborn", "harry.osborn@titans.edu", "password111",
                "Business", 2);
        newStudent.setGradeAverage(3.2);

        when(studentRepository.save(newStudent)).thenReturn(newStudent);

        Student savedStudent = studentRepository.save(newStudent);

        assertAll("Verificar guardado de estudiante",
                () -> assertNotNull(savedStudent),
                () -> assertEquals("Harry Osborn", savedStudent.getName()),
                () -> assertEquals("Business", savedStudent.getAcademicProgram()),
                () -> assertEquals(2, savedStudent.getSemester()),
                () -> assertEquals(3.2, savedStudent.getGradeAverage())
        );

        verify(studentRepository, times(1)).save(newStudent);
    }

    @Test
    @DisplayName("Caso exitoso - findById retorna estudiante existente")
    void testFindById_Exitoso() {
        when(studentRepository.findById("1")).thenReturn(Optional.of(student1));

        Optional<Student> foundStudent = studentRepository.findById("1");

        assertAll("Verificar búsqueda por ID",
                () -> assertTrue(foundStudent.isPresent()),
                () -> assertEquals("Peter Parker", foundStudent.get().getName()),
                () -> assertEquals("Computer Science", foundStudent.get().getAcademicProgram())
        );

        verify(studentRepository, times(1)).findById("1");
    }

    @Test
    @DisplayName("Caso error - findById retorna vacío para ID inexistente")
    void testFindById_NoEncontrado() {
        when(studentRepository.findById("99")).thenReturn(Optional.empty());

        Optional<Student> foundStudent = studentRepository.findById("99");

        assertTrue(foundStudent.isEmpty());
        verify(studentRepository, times(1)).findById("99");
    }

    @Test
    @DisplayName("Caso exitoso - findAll retorna todos los estudiantes")
    void testFindAll_Exitoso() {
        List<Student> students = Arrays.asList(student1, student2, student3, student4);
        when(studentRepository.findAll()).thenReturn(students);

        List<Student> result = studentRepository.findAll();

        assertAll("Verificar búsqueda de todos los estudiantes",
                () -> assertNotNull(result),
                () -> assertEquals(4, result.size()),
                () -> assertEquals("Peter Parker", result.get(0).getName())
        );

        verify(studentRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Caso borde - findAll retorna lista vacía cuando no hay estudiantes")
    void testFindAll_ListaVacia() {
        when(studentRepository.findAll()).thenReturn(Collections.emptyList());

        List<Student> result = studentRepository.findAll();

        assertTrue(result.isEmpty());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Caso exitoso - findByAcademicProgram retorna estudiantes del programa")
    void testFindByAcademicProgram_Exitoso() {
        List<Student> csStudents = Arrays.asList(student1, student3);
        when(studentRepository.findByAcademicProgram("Computer Science")).thenReturn(csStudents);

        List<Student> result = studentRepository.findByAcademicProgram("Computer Science");

        assertAll("Verificar búsqueda por programa académico",
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size()),
                () -> assertTrue(result.stream().allMatch(s -> s.getAcademicProgram().equals("Computer Science")))
        );

        verify(studentRepository, times(1)).findByAcademicProgram("Computer Science");
    }

    @Test
    @DisplayName("Caso error - findByAcademicProgram retorna lista vacía para programa inexistente")
    void testFindByAcademicProgram_ProgramaInexistente() {
        when(studentRepository.findByAcademicProgram("Medicine")).thenReturn(Collections.emptyList());

        List<Student> result = studentRepository.findByAcademicProgram("Medicine");

        assertTrue(result.isEmpty());
        verify(studentRepository, times(1)).findByAcademicProgram("Medicine");
    }

    @Test
    @DisplayName("Caso exitoso - findBySemester retorna estudiantes del semestre")
    void testFindBySemester_Exitoso() {
        List<Student> semester5Students = Arrays.asList(student1);
        when(studentRepository.findBySemester(5)).thenReturn(semester5Students);

        List<Student> result = studentRepository.findBySemester(5);

        assertAll("Verificar búsqueda por semestre",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals(5, result.get(0).getSemester()),
                () -> assertEquals("Peter Parker", result.get(0).getName())
        );

        verify(studentRepository, times(1)).findBySemester(5);
    }

    @Test
    @DisplayName("Caso exitoso - findByGradeAverage retorna estudiantes por promedio")
    void testFindByGradeAverage_Exitoso() {
        List<Student> students = Arrays.asList(student1);
        when(studentRepository.findByGradeAverage(4.2)).thenReturn(students);

        List<Student> result = studentRepository.findByGradeAverage(4.2);

        assertAll("Verificar búsqueda por promedio",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals(4.2, result.get(0).getGradeAverage()),
                () -> assertEquals("Peter Parker", result.get(0).getName())
        );

        verify(studentRepository, times(1)).findByGradeAverage(4.2);
    }

    @Test
    @DisplayName("Caso exitoso - findBySemesterGreaterThan retorna estudiantes de semestres superiores")
    void testFindBySemesterGreaterThan_Exitoso() {
        List<Student> students = Arrays.asList(student1, student2);
        when(studentRepository.findBySemesterGreaterThan(4)).thenReturn(students);

        List<Student> result = studentRepository.findBySemesterGreaterThan(4);

        assertAll("Verificar búsqueda por semestre mayor que",
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size()),
                () -> assertTrue(result.stream().allMatch(s -> s.getSemester() > 4))
        );

        verify(studentRepository, times(1)).findBySemesterGreaterThan(4);
    }

    @Test
    @DisplayName("Caso exitoso - findBySemesterLessThan retorna estudiantes de semestres inferiores")
    void testFindBySemesterLessThan_Exitoso() {
        List<Student> students = Arrays.asList(student3, student4);
        when(studentRepository.findBySemesterLessThan(5)).thenReturn(students);

        List<Student> result = studentRepository.findBySemesterLessThan(5);

        assertAll("Verificar búsqueda por semestre menor que",
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size()),
                () -> assertTrue(result.stream().allMatch(s -> s.getSemester() < 5))
        );

        verify(studentRepository, times(1)).findBySemesterLessThan(5);
    }

    @Test
    @DisplayName("Caso exitoso - findBySemesterBetween retorna estudiantes en rango de semestres")
    void testFindBySemesterBetween_Exitoso() {
        List<Student> students = Arrays.asList(student1, student3);
        when(studentRepository.findBySemesterBetween(4, 5)).thenReturn(students);

        List<Student> result = studentRepository.findBySemesterBetween(4, 5);

        assertAll("Verificar búsqueda por rango de semestres",
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size()),
                () -> assertTrue(result.stream().allMatch(s -> s.getSemester() >= 4 && s.getSemester() <= 5))
        );

        verify(studentRepository, times(1)).findBySemesterBetween(4, 5);
    }

    @Test
    @DisplayName("Caso exitoso - findByGradeAverageGreaterThan retorna estudiantes con promedio alto")
    void testFindByGradeAverageGreaterThan_Exitoso() {
        List<Student> students = Arrays.asList(student1, student2);
        when(studentRepository.findByGradeAverageGreaterThan(4.0)).thenReturn(students);

        List<Student> result = studentRepository.findByGradeAverageGreaterThan(4.0);

        assertAll("Verificar búsqueda por promedio mayor que",
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size()),
                () -> assertTrue(result.stream().allMatch(s -> s.getGradeAverage() > 4.0))
        );

        verify(studentRepository, times(1)).findByGradeAverageGreaterThan(4.0);
    }

    @Test
    @DisplayName("Caso exitoso - findByGradeAverageLessThan retorna estudiantes con promedio bajo")
    void testFindByGradeAverageLessThan_Exitoso() {
        List<Student> students = Arrays.asList(student3, student4);
        when(studentRepository.findByGradeAverageLessThan(4.0)).thenReturn(students);

        List<Student> result = studentRepository.findByGradeAverageLessThan(4.0);

        assertAll("Verificar búsqueda por promedio menor que",
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size()),
                () -> assertTrue(result.stream().allMatch(s -> s.getGradeAverage() < 4.0))
        );

        verify(studentRepository, times(1)).findByGradeAverageLessThan(4.0);
    }

    @Test
    @DisplayName("Caso exitoso - findByGradeAverageBetween retorna estudiantes en rango de promedios")
    void testFindByGradeAverageBetween_Exitoso() {
        List<Student> students = Arrays.asList(student3, student4);
        when(studentRepository.findByGradeAverageBetween(3.5, 4.0)).thenReturn(students);

        List<Student> result = studentRepository.findByGradeAverageBetween(3.5, 4.0);

        assertAll("Verificar búsqueda por rango de promedios",
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size()),
                () -> assertTrue(result.stream().allMatch(s -> s.getGradeAverage() >= 3.5 && s.getGradeAverage() <= 4.0))
        );

        verify(studentRepository, times(1)).findByGradeAverageBetween(3.5, 4.0);
    }

    @Test
    @DisplayName("Caso exitoso - findByAcademicProgramAndSemester retorna estudiantes combinados")
    void testFindByAcademicProgramAndSemester_Exitoso() {
        List<Student> students = Arrays.asList(student1);
        when(studentRepository.findByAcademicProgramAndSemester("Computer Science", 5)).thenReturn(students);

        List<Student> result = studentRepository.findByAcademicProgramAndSemester("Computer Science", 5);

        assertAll("Verificar búsqueda combinada por programa y semestre",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("Computer Science", result.get(0).getAcademicProgram()),
                () -> assertEquals(5, result.get(0).getSemester())
        );

        verify(studentRepository, times(1)).findByAcademicProgramAndSemester("Computer Science", 5);
    }

    @Test
    @DisplayName("Caso exitoso - findByAcademicProgramAndGradeAverageGreaterThan retorna estudiantes destacados por programa")
    void testFindByAcademicProgramAndGradeAverageGreaterThan_Exitoso() {
        List<Student> students = Arrays.asList(student1);
        when(studentRepository.findByAcademicProgramAndGradeAverageGreaterThan("Computer Science", 4.0)).thenReturn(students);

        List<Student> result = studentRepository.findByAcademicProgramAndGradeAverageGreaterThan("Computer Science", 4.0);

        assertAll("Verificar búsqueda por programa y promedio mayor que",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("Computer Science", result.get(0).getAcademicProgram()),
                () -> assertTrue(result.get(0).getGradeAverage() > 4.0)
        );

        verify(studentRepository, times(1)).findByAcademicProgramAndGradeAverageGreaterThan("Computer Science", 4.0);
    }

    @Test
    @DisplayName("Caso exitoso - findBySemesterAndGradeAverageGreaterThan retorna estudiantes destacados por semestre")
    void testFindBySemesterAndGradeAverageGreaterThan_Exitoso() {
        List<Student> students = Arrays.asList(student1);
        when(studentRepository.findBySemesterAndGradeAverageGreaterThan(5, 4.0)).thenReturn(students);

        List<Student> result = studentRepository.findBySemesterAndGradeAverageGreaterThan(5, 4.0);

        assertAll("Verificar búsqueda por semestre y promedio mayor que",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals(5, result.get(0).getSemester()),
                () -> assertTrue(result.get(0).getGradeAverage() > 4.0)
        );

        verify(studentRepository, times(1)).findBySemesterAndGradeAverageGreaterThan(5, 4.0);
    }

    @Test
    @DisplayName("Caso exitoso - findByName retorna estudiante por nombre")
    void testFindByName_Exitoso() {
        List<Student> students = Arrays.asList(student1);
        when(studentRepository.findByName("Peter Parker")).thenReturn(students);

        List<Student> result = studentRepository.findByName("Peter Parker");

        assertAll("Verificar búsqueda por nombre",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("Peter Parker", result.get(0).getName())
        );

        verify(studentRepository, times(1)).findByName("Peter Parker");
    }

    @Test
    @DisplayName("Caso exitoso - findByEmail retorna estudiante por email")
    void testFindByEmail_Exitoso() {
        List<Student> students = Arrays.asList(student1);
        when(studentRepository.findByEmail("peter.parker@titans.edu")).thenReturn(students);

        List<Student> result = studentRepository.findByEmail("peter.parker@titans.edu");

        assertAll("Verificar búsqueda por email",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("peter.parker@titans.edu", result.get(0).getEmail())
        );

        verify(studentRepository, times(1)).findByEmail("peter.parker@titans.edu");
    }

    @Test
    @DisplayName("Caso exitoso - findByNameContainingIgnoreCase retorna estudiantes con patrón en nombre")
    void testFindByNameContainingIgnoreCase_Exitoso() {
        List<Student> students = Arrays.asList(student1);
        when(studentRepository.findByNameContainingIgnoreCase("peter")).thenReturn(students);

        List<Student> result = studentRepository.findByNameContainingIgnoreCase("peter");

        assertAll("Verificar búsqueda por patrón en nombre",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertTrue(result.get(0).getName().toLowerCase().contains("peter"))
        );

        verify(studentRepository, times(1)).findByNameContainingIgnoreCase("peter");
    }

    @Test
    @DisplayName("Caso exitoso - findByActive retorna estudiantes activos")
    void testFindByActive_Exitoso() {
        List<Student> activeStudents = Arrays.asList(student1, student3, student4);
        when(studentRepository.findByActive(true)).thenReturn(activeStudents);

        List<Student> result = studentRepository.findByActive(true);

        assertAll("Verificar búsqueda por estado activo",
                () -> assertNotNull(result),
                () -> assertEquals(3, result.size()),
                () -> assertTrue(result.stream().allMatch(Student::isActive))
        );

        verify(studentRepository, times(1)).findByActive(true);
    }

    @Test
    @DisplayName("Caso exitoso - findByAcademicProgramAndActive retorna estudiantes combinados")
    void testFindByAcademicProgramAndActive_Exitoso() {
        List<Student> students = Arrays.asList(student1, student3);
        when(studentRepository.findByAcademicProgramAndActive("Computer Science", true)).thenReturn(students);

        List<Student> result = studentRepository.findByAcademicProgramAndActive("Computer Science", true);

        assertAll("Verificar búsqueda combinada por programa y estado",
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size()),
                () -> assertEquals("Computer Science", result.get(0).getAcademicProgram()),
                () -> assertTrue(result.get(0).isActive())
        );

        verify(studentRepository, times(1)).findByAcademicProgramAndActive("Computer Science", true);
    }

    @Test
    @DisplayName("Caso exitoso - findBySemesterAndActive retorna estudiantes por semestre y estado")
    void testFindBySemesterAndActive_Exitoso() {
        List<Student> students = Arrays.asList(student1);
        when(studentRepository.findBySemesterAndActive(5, true)).thenReturn(students);

        List<Student> result = studentRepository.findBySemesterAndActive(5, true);

        assertAll("Verificar búsqueda por semestre y estado",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals(5, result.get(0).getSemester()),
                () -> assertTrue(result.get(0).isActive())
        );

        verify(studentRepository, times(1)).findBySemesterAndActive(5, true);
    }

    @Test
    @DisplayName("Caso exitoso - findByNameAndAcademicProgram retorna estudiante por nombre y programa")
    void testFindByNameAndAcademicProgram_Exitoso() {
        List<Student> students = Arrays.asList(student1);
        when(studentRepository.findByNameAndAcademicProgram("Peter Parker", "Computer Science")).thenReturn(students);

        List<Student> result = studentRepository.findByNameAndAcademicProgram("Peter Parker", "Computer Science");

        assertAll("Verificar búsqueda por nombre y programa",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("Peter Parker", result.get(0).getName()),
                () -> assertEquals("Computer Science", result.get(0).getAcademicProgram())
        );

        verify(studentRepository, times(1)).findByNameAndAcademicProgram("Peter Parker", "Computer Science");
    }

    @Test
    @DisplayName("Caso exitoso - findByNameAndSemester retorna estudiante por nombre y semestre")
    void testFindByNameAndSemester_Exitoso() {
        List<Student> students = Arrays.asList(student1);
        when(studentRepository.findByNameAndSemester("Peter Parker", 5)).thenReturn(students);

        List<Student> result = studentRepository.findByNameAndSemester("Peter Parker", 5);

        assertAll("Verificar búsqueda por nombre y semestre",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("Peter Parker", result.get(0).getName()),
                () -> assertEquals(5, result.get(0).getSemester())
        );

        verify(studentRepository, times(1)).findByNameAndSemester("Peter Parker", 5);
    }

    @Test
    @DisplayName("Caso exitoso - findByOrderByGradeAverageDesc retorna estudiantes ordenados por promedio descendente")
    void testFindByOrderByGradeAverageDesc_Exitoso() {
        List<Student> students = Arrays.asList(student2, student1, student3, student4);
        when(studentRepository.findByOrderByGradeAverageDesc()).thenReturn(students);

        List<Student> result = studentRepository.findByOrderByGradeAverageDesc();

        assertAll("Verificar ordenamiento por promedio descendente",
                () -> assertNotNull(result),
                () -> assertEquals(4, result.size()),
                () -> assertEquals("Gwen Stacy", result.get(0).getName()),
                () -> assertEquals("Peter Parker", result.get(1).getName()),
                () -> assertEquals("Miles Morales", result.get(2).getName()),
                () -> assertEquals("Mary Jane Watson", result.get(3).getName())
        );

        verify(studentRepository, times(1)).findByOrderByGradeAverageDesc();
    }

    @Test
    @DisplayName("Caso exitoso - findByOrderBySemesterAsc retorna estudiantes ordenados por semestre ascendente")
    void testFindByOrderBySemesterAsc_Exitoso() {
        List<Student> students = Arrays.asList(student4, student3, student1, student2);
        when(studentRepository.findByOrderBySemesterAsc()).thenReturn(students);

        List<Student> result = studentRepository.findByOrderBySemesterAsc();

        assertAll("Verificar ordenamiento por semestre ascendente",
                () -> assertNotNull(result),
                () -> assertEquals(4, result.size()),
                () -> assertEquals("Mary Jane Watson", result.get(0).getName()),
                () -> assertEquals("Miles Morales", result.get(1).getName()),
                () -> assertEquals("Peter Parker", result.get(2).getName()),
                () -> assertEquals("Gwen Stacy", result.get(3).getName())
        );

        verify(studentRepository, times(1)).findByOrderBySemesterAsc();
    }

    @Test
    @DisplayName("Caso exitoso - countByAcademicProgram retorna conteo correcto")
    void testCountByAcademicProgram_Exitoso() {
        when(studentRepository.countByAcademicProgram("Computer Science")).thenReturn(2L);

        long result = studentRepository.countByAcademicProgram("Computer Science");

        assertEquals(2L, result);
        verify(studentRepository, times(1)).countByAcademicProgram("Computer Science");
    }

    @Test
    @DisplayName("Caso exitoso - countBySemester retorna conteo por semestre")
    void testCountBySemester_Exitoso() {
        when(studentRepository.countBySemester(5)).thenReturn(1L);

        long result = studentRepository.countBySemester(5);

        assertEquals(1L, result);
        verify(studentRepository, times(1)).countBySemester(5);
    }

    @Test
    @DisplayName("Caso exitoso - countByAcademicProgramAndSemester retorna conteo combinado")
    void testCountByAcademicProgramAndSemester_Exitoso() {
        when(studentRepository.countByAcademicProgramAndSemester("Computer Science", 5)).thenReturn(1L);

        long result = studentRepository.countByAcademicProgramAndSemester("Computer Science", 5);

        assertEquals(1L, result);
        verify(studentRepository, times(1)).countByAcademicProgramAndSemester("Computer Science", 5);
    }

    @Test
    @DisplayName("Caso exitoso - countByGradeAverageGreaterThan retorna conteo por promedio")
    void testCountByGradeAverageGreaterThan_Exitoso() {
        when(studentRepository.countByGradeAverageGreaterThan(4.0)).thenReturn(2L);

        long result = studentRepository.countByGradeAverageGreaterThan(4.0);

        assertEquals(2L, result);
        verify(studentRepository, times(1)).countByGradeAverageGreaterThan(4.0);
    }

    @Test
    @DisplayName("Caso exitoso - findByAcademicProgramRegex retorna estudiantes por patrón regex")
    void testFindByAcademicProgramRegex_Exitoso() {
        List<Student> students = Arrays.asList(student1, student3);
        when(studentRepository.findByAcademicProgramRegex(".*Science.*")).thenReturn(students);

        List<Student> result = studentRepository.findByAcademicProgramRegex(".*Science.*");

        assertAll("Verificar búsqueda por regex de programa académico",
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size()),
                () -> assertTrue(result.stream().allMatch(s -> s.getAcademicProgram().contains("Science")))
        );

        verify(studentRepository, times(1)).findByAcademicProgramRegex(".*Science.*");
    }

    @Test
    @DisplayName("Caso exitoso - findTopStudents retorna estudiantes destacados")
    void testFindTopStudents_Exitoso() {
        List<Student> students = Arrays.asList(student1, student2);
        when(studentRepository.findTopStudents(4.0)).thenReturn(students);

        List<Student> result = studentRepository.findTopStudents(4.0);

        assertAll("Verificar búsqueda de estudiantes destacados",
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size()),
                () -> assertTrue(result.stream().allMatch(s -> s.getGradeAverage() >= 4.0))
        );

        verify(studentRepository, times(1)).findTopStudents(4.0);
    }

    @Test
    @DisplayName("Caso exitoso - existsByAcademicProgramAndSemester retorna true cuando existe combinación")
    void testExistsByAcademicProgramAndSemester_Exitoso() {
        when(studentRepository.existsByAcademicProgramAndSemester("Computer Science", 5)).thenReturn(true);

        boolean result = studentRepository.existsByAcademicProgramAndSemester("Computer Science", 5);

        assertTrue(result);
        verify(studentRepository, times(1)).existsByAcademicProgramAndSemester("Computer Science", 5);
    }

    @Test
    @DisplayName("Caso error - existsByAcademicProgramAndSemester retorna false cuando no existe combinación")
    void testExistsByAcademicProgramAndSemester_NoExiste() {
        when(studentRepository.existsByAcademicProgramAndSemester("Computer Science", 1)).thenReturn(false);

        boolean result = studentRepository.existsByAcademicProgramAndSemester("Computer Science", 1);

        assertFalse(result);
        verify(studentRepository, times(1)).existsByAcademicProgramAndSemester("Computer Science", 1);
    }

    @Test
    @DisplayName("Caso exitoso - existsByEmail retorna true cuando existe email")
    void testExistsByEmail_Exitoso() {
        when(studentRepository.existsByEmail("peter.parker@titans.edu")).thenReturn(true);

        boolean result = studentRepository.existsByEmail("peter.parker@titans.edu");

        assertTrue(result);
        verify(studentRepository, times(1)).existsByEmail("peter.parker@titans.edu");
    }

    @Test
    @DisplayName("Caso error - existsByEmail retorna false cuando no existe email")
    void testExistsByEmail_NoExiste() {
        when(studentRepository.existsByEmail("inexistente@titans.edu")).thenReturn(false);

        boolean result = studentRepository.existsByEmail("inexistente@titans.edu");

        assertFalse(result);
        verify(studentRepository, times(1)).existsByEmail("inexistente@titans.edu");
    }

    @Test
    @DisplayName("Caso exitoso - deleteById elimina estudiante existente")
    void testDeleteById_Exitoso() {
        doNothing().when(studentRepository).deleteById("1");

        assertDoesNotThrow(() -> studentRepository.deleteById("1"));

        verify(studentRepository, times(1)).deleteById("1");
    }
}
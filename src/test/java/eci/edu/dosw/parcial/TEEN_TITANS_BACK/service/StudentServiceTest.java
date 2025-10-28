package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Student;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.StudentRepository;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student1;
    private Student student2;
    private Student student3;

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
    }

    @Test
    @DisplayName("Caso exitoso - createStudent crea estudiante correctamente")
    void testCreateStudent_Exitoso() {
        when(studentRepository.findByEmail(student1.getEmail())).thenReturn(Collections.emptyList());
        when(studentRepository.save(any(Student.class))).thenReturn(student1);

        Student resultado = studentService.createStudent(student1);

        assertAll("Verificar creación de estudiante",
                () -> assertNotNull(resultado),
                () -> assertEquals("Peter Parker", resultado.getName()),
                () -> assertEquals("Computer Science", resultado.getAcademicProgram()),
                () -> assertEquals(5, resultado.getSemester())
        );

        verify(studentRepository, times(1)).findByEmail(student1.getEmail());
        verify(studentRepository, times(1)).save(student1);
    }

    @Test
    @DisplayName("Caso error - createStudent lanza excepción cuando email ya existe")
    void testCreateStudent_EmailExistente() {
        when(studentRepository.findByEmail(student1.getEmail())).thenReturn(Arrays.asList(student2));

        AppException exception = assertThrows(AppException.class,
                () -> studentService.createStudent(student1));

        assertEquals("El email ya está registrado: " + student1.getEmail(), exception.getMessage());

        verify(studentRepository, never()).save(any(Student.class));
        verify(studentRepository, times(1)).findByEmail(student1.getEmail());
    }

    @Test
    @DisplayName("Caso exitoso - getStudentById retorna estudiante existente")
    void testGetStudentById_Exitoso() {
        when(studentRepository.findById("1")).thenReturn(Optional.of(student1));

        Student resultado = studentService.getStudentById("1");

        assertAll("Verificar estudiante encontrado",
                () -> assertNotNull(resultado),
                () -> assertEquals("1", resultado.getId()),
                () -> assertEquals("Peter Parker", resultado.getName())
        );

        verify(studentRepository, times(1)).findById("1");
    }

    @Test
    @DisplayName("Caso error - getStudentById lanza excepción cuando ID no existe")
    void testGetStudentById_NoEncontrado() {
        when(studentRepository.findById("99")).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class,
                () -> studentService.getStudentById("99"));

        assertEquals("Estudiante no encontrado con ID: 99", exception.getMessage());
        verify(studentRepository, times(1)).findById("99");
    }

    @Test
    @DisplayName("Caso exitoso - getAllStudents retorna todos los estudiantes")
    void testGetAllStudents_Exitoso() {
        List<Student> estudiantes = Arrays.asList(student1, student2, student3);
        when(studentRepository.findAll()).thenReturn(estudiantes);

        List<Student> resultado = studentService.getAllStudents();

        assertAll("Verificar lista de estudiantes",
                () -> assertNotNull(resultado),
                () -> assertEquals(3, resultado.size()),
                () -> assertEquals("Peter Parker", resultado.get(0).getName())
        );

        verify(studentRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Caso borde - getAllStudents retorna lista vacía cuando no hay estudiantes")
    void testGetAllStudents_ListaVacia() {
        when(studentRepository.findAll()).thenReturn(Collections.emptyList());

        List<Student> resultado = studentService.getAllStudents();

        assertTrue(resultado.isEmpty());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Caso exitoso - updateStudent actualiza estudiante correctamente")
    void testUpdateStudent_Exitoso() {
        when(studentRepository.existsById("1")).thenReturn(true);
        when(studentRepository.findByEmail(student1.getEmail())).thenReturn(Arrays.asList(student1));
        when(studentRepository.save(any(Student.class))).thenReturn(student1);

        Student estudianteActualizado = new Student("1", "Peter Parker Updated",
                "peter.parker@titans.edu", "newpassword",
                "Software Engineering", 6);
        estudianteActualizado.setGradeAverage(4.3);

        Student resultado = studentService.updateStudent("1", estudianteActualizado);

        assertAll("Verificar actualización de estudiante",
                () -> assertNotNull(resultado),
                () -> assertEquals("1", resultado.getId())
        );

        verify(studentRepository, times(1)).existsById("1");
        verify(studentRepository, times(1)).findByEmail(student1.getEmail());
        verify(studentRepository, times(1)).save(estudianteActualizado);
    }

    @Test
    @DisplayName("Caso error - updateStudent lanza excepción cuando ID no existe")
    void testUpdateStudent_NoEncontrado() {
        when(studentRepository.existsById("99")).thenReturn(false);

        AppException exception = assertThrows(AppException.class,
                () -> studentService.updateStudent("99", student1));

        assertEquals("Estudiante no encontrado con ID: 99", exception.getMessage());

        verify(studentRepository, times(1)).existsById("99");
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    @DisplayName("Caso error - updateStudent lanza excepción cuando email ya está en uso")
    void testUpdateStudent_EmailEnUso() {
        when(studentRepository.existsById("1")).thenReturn(true);
        when(studentRepository.findByEmail("nuevo.email@titans.edu")).thenReturn(Arrays.asList(student2));

        Student estudianteActualizado = new Student("1", "Peter Parker",
                "nuevo.email@titans.edu", "password123",
                "Computer Science", 5);
        estudianteActualizado.setGradeAverage(4.2);

        AppException exception = assertThrows(AppException.class,
                () -> studentService.updateStudent("1", estudianteActualizado));

        assertEquals("El email ya está en uso por otro estudiante: nuevo.email@titans.edu", exception.getMessage());

        verify(studentRepository, times(1)).existsById("1");
        verify(studentRepository, times(1)).findByEmail("nuevo.email@titans.edu");
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    @DisplayName("Caso exitoso - deleteStudent elimina estudiante existente")
    void testDeleteStudent_Exitoso() {
        when(studentRepository.existsById("1")).thenReturn(true);
        doNothing().when(studentRepository).deleteById("1");

        assertDoesNotThrow(() -> studentService.deleteStudent("1"));

        verify(studentRepository, times(1)).existsById("1");
        verify(studentRepository, times(1)).deleteById("1");
    }

    @Test
    @DisplayName("Caso error - deleteStudent lanza excepción cuando ID no existe")
    void testDeleteStudent_NoEncontrado() {
        when(studentRepository.existsById("99")).thenReturn(false);

        AppException exception = assertThrows(AppException.class,
                () -> studentService.deleteStudent("99"));

        assertEquals("Estudiante no encontrado con ID: 99", exception.getMessage());

        verify(studentRepository, times(1)).existsById("99");
        verify(studentRepository, never()).deleteById(anyString());
    }

    @Test
    @DisplayName("Caso exitoso - findStudentByEmail retorna estudiante cuando existe")
    void testFindStudentByEmail_Exitoso() {
        when(studentRepository.findByEmail("peter.parker@titans.edu")).thenReturn(Arrays.asList(student1));

        Optional<Student> resultado = studentService.findStudentByEmail("peter.parker@titans.edu");

        assertAll("Verificar estudiante por email",
                () -> assertTrue(resultado.isPresent()),
                () -> assertEquals("Peter Parker", resultado.get().getName())
        );

        verify(studentRepository, times(1)).findByEmail("peter.parker@titans.edu");
    }

    @Test
    @DisplayName("Caso error - findStudentByEmail retorna Optional vacío cuando no existe")
    void testFindStudentByEmail_NoEncontrado() {
        when(studentRepository.findByEmail("inexistente@titans.edu")).thenReturn(Collections.emptyList());

        Optional<Student> resultado = studentService.findStudentByEmail("inexistente@titans.edu");

        assertTrue(resultado.isEmpty());
        verify(studentRepository, times(1)).findByEmail("inexistente@titans.edu");
    }

    @Test
    @DisplayName("Caso exitoso - findByAcademicProgram retorna estudiantes del programa")
    void testFindByAcademicProgram_Exitoso() {
        List<Student> estudiantesCS = Arrays.asList(student1, student3);
        when(studentRepository.findByAcademicProgram("Computer Science")).thenReturn(estudiantesCS);

        List<Student> resultado = studentService.findByAcademicProgram("Computer Science");

        assertAll("Verificar estudiantes por programa académico",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertTrue(resultado.stream().allMatch(s -> "Computer Science".equals(s.getAcademicProgram())))
        );

        verify(studentRepository, times(1)).findByAcademicProgram("Computer Science");
    }

    @Test
    @DisplayName("Caso exitoso - findBySemester retorna estudiantes del semestre")
    void testFindBySemester_Exitoso() {
        List<Student> estudiantesSem5 = Arrays.asList(student1);
        when(studentRepository.findBySemester(5)).thenReturn(estudiantesSem5);

        List<Student> resultado = studentService.findBySemester(5);

        assertAll("Verificar estudiantes por semestre",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals(5, resultado.get(0).getSemester())
        );

        verify(studentRepository, times(1)).findBySemester(5);
    }

    @Test
    @DisplayName("Caso exitoso - findByGradeAverageGreaterThan retorna estudiantes con promedio alto")
    void testFindByGradeAverageGreaterThan_Exitoso() {
        List<Student> estudiantesTop = Arrays.asList(student1, student2);
        when(studentRepository.findByGradeAverageGreaterThan(4.0)).thenReturn(estudiantesTop);

        List<Student> resultado = studentService.findByGradeAverageGreaterThan(4.0);

        assertAll("Verificar estudiantes con promedio alto",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertTrue(resultado.stream().allMatch(s -> s.getGradeAverage() > 4.0))
        );

        verify(studentRepository, times(1)).findByGradeAverageGreaterThan(4.0);
    }

    @Test
    @DisplayName("Caso exitoso - findByAcademicProgramAndSemester retorna estudiantes combinados")
    void testFindByAcademicProgramAndSemester_Exitoso() {
        List<Student> estudiantes = Arrays.asList(student1);
        when(studentRepository.findByAcademicProgramAndSemester("Computer Science", 5)).thenReturn(estudiantes);

        List<Student> resultado = studentService.findByAcademicProgramAndSemester("Computer Science", 5);

        assertAll("Verificar estudiantes por programa y semestre",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("Computer Science", resultado.get(0).getAcademicProgram()),
                () -> assertEquals(5, resultado.get(0).getSemester())
        );

        verify(studentRepository, times(1)).findByAcademicProgramAndSemester("Computer Science", 5);
    }

    @Test
    @DisplayName("Caso exitoso - findBySemesterBetween retorna estudiantes en rango de semestres")
    void testFindBySemesterBetween_Exitoso() {
        List<Student> estudiantesRango = Arrays.asList(student1, student3);
        when(studentRepository.findBySemesterBetween(4, 6)).thenReturn(estudiantesRango);

        List<Student> resultado = studentService.findBySemesterBetween(4, 6);

        assertAll("Verificar estudiantes en rango de semestres",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertTrue(resultado.stream().allMatch(s -> s.getSemester() >= 4 && s.getSemester() <= 6))
        );

        verify(studentRepository, times(1)).findBySemesterBetween(4, 6);
    }

    @Test
    @DisplayName("Caso exitoso - updateStudentGrade actualiza promedio correctamente")
    void testUpdateStudentGrade_Exitoso() {
        when(studentRepository.findById("1")).thenReturn(Optional.of(student1));
        when(studentRepository.save(any(Student.class))).thenReturn(student1);

        Student resultado = studentService.updateStudentGrade("1", 4.8);

        assertAll("Verificar actualización de promedio",
                () -> assertNotNull(resultado),
                () -> assertEquals(4.8, resultado.getGradeAverage())
        );

        verify(studentRepository, times(1)).findById("1");
        verify(studentRepository, times(1)).save(student1);
    }

    @Test
    @DisplayName("Caso exitoso - countByAcademicProgram retorna conteo correcto")
    void testCountByAcademicProgram_Exitoso() {
        when(studentRepository.countByAcademicProgram("Computer Science")).thenReturn(2L);

        long resultado = studentService.countByAcademicProgram("Computer Science");

        assertEquals(2L, resultado);
        verify(studentRepository, times(1)).countByAcademicProgram("Computer Science");
    }

    @Test
    @DisplayName("Caso borde - findByGradeAverageBetween retorna lista vacía cuando no hay coincidencias")
    void testFindByGradeAverageBetween_SinCoincidencias() {
        when(studentRepository.findByGradeAverageBetween(5.0, 6.0)).thenReturn(Collections.emptyList());

        List<Student> resultado = studentService.findByGradeAverageBetween(5.0, 6.0);

        assertTrue(resultado.isEmpty());
        verify(studentRepository, times(1)).findByGradeAverageBetween(5.0, 6.0);
    }

    @Test
    @DisplayName("Caso exitoso - findByNameContaining retorna estudiantes por nombre")
    void testFindByNameContaining_Exitoso() {
        List<Student> estudiantes = Arrays.asList(student1);
        when(studentRepository.findByNameContainingIgnoreCase("Peter")).thenReturn(estudiantes);

        List<Student> resultado = studentService.findByNameContaining("Peter");

        assertAll("Verificar estudiantes por nombre",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("Peter Parker", resultado.get(0).getName())
        );

        verify(studentRepository, times(1)).findByNameContainingIgnoreCase("Peter");
    }

    @Test
    @DisplayName("Caso exitoso - findTopStudents retorna estudiantes destacados")
    void testFindTopStudents_Exitoso() {
        List<Student> estudiantesTop = Arrays.asList(student1, student2);
        when(studentRepository.findTopStudents(4.0)).thenReturn(estudiantesTop);

        List<Student> resultado = studentService.findTopStudents(4.0);

        assertAll("Verificar estudiantes destacados",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size())
        );

        verify(studentRepository, times(1)).findTopStudents(4.0);
    }
}
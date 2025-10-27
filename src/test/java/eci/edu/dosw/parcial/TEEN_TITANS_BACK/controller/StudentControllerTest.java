package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto.StudentDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Student;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.StudentService;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private Student student1;
    private Student student2;
    private StudentDTO studentDTO1;
    private StudentDTO studentDTO2;

    @BeforeEach
    void setUp() {
        student1 = new Student();
        student1.setId("1");
        student1.setName("Estudiante A");
        student1.setEmail("estudianteA@titans.edu");
        student1.setAcademicProgram("Ingeniería");
        student1.setSemester(5);
        student1.setGradeAverage(4.2);
        student1.setActive(true);

        student2 = new Student();
        student2.setId("2");
        student2.setName("Estudiante B");
        student2.setEmail("estudianteB@titans.edu");
        student2.setAcademicProgram("Medicina");
        student2.setSemester(3);
        student2.setGradeAverage(3.8);
        student2.setActive(true);

        studentDTO1 = new StudentDTO();
        studentDTO1.setId("1");
        studentDTO1.setName("Estudiante A");
        studentDTO1.setEmail("estudianteA@titans.edu");
        studentDTO1.setAcademicProgram("Ingeniería");
        studentDTO1.setSemester(5);
        studentDTO1.setGradeAverage(4.2);
        studentDTO1.setActive(true);

        studentDTO2 = new StudentDTO();
        studentDTO2.setId("2");
        studentDTO2.setName("Estudiante B");
        studentDTO2.setEmail("estudianteB@titans.edu");
        studentDTO2.setAcademicProgram("Medicina");
        studentDTO2.setSemester(3);
        studentDTO2.setGradeAverage(3.8);
        studentDTO2.setActive(true);
    }

    @Test
    @DisplayName("Caso exitoso - createStudent crea estudiante correctamente")
    void testCreateStudent_Exitoso() {
        when(studentService.createStudent(any(Student.class))).thenReturn(student1);

        ResponseEntity<?> response = studentController.createStudent(studentDTO1);

        assertAll("Verificar respuesta de creación exitosa",
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(StudentDTO.class, response.getBody()),
                () -> assertEquals("Estudiante A", ((StudentDTO) response.getBody()).getName())
        );

        verify(studentService, times(1)).createStudent(any(Student.class));
    }

    @Test
    @DisplayName("Caso error - createStudent retorna error cuando AppException ocurre")
    void testCreateStudent_ErrorAppException() {
        when(studentService.createStudent(any(Student.class)))
                .thenThrow(new AppException("Error al crear estudiante"));

        ResponseEntity<?> response = studentController.createStudent(studentDTO1);

        assertAll("Verificar respuesta de error por AppException",
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> assertTrue(((Map<?, ?>) response.getBody()).containsKey("error"))
        );

        verify(studentService, times(1)).createStudent(any(Student.class));
    }

    @Test
    @DisplayName("Caso exitoso - getStudentById retorna estudiante existente")
    void testGetStudentById_Exitoso() {
        when(studentService.getStudentById("1")).thenReturn(student1);

        ResponseEntity<?> response = studentController.getStudentById("1");

        assertAll("Verificar obtención exitosa de estudiante",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(StudentDTO.class, response.getBody()),
                () -> assertEquals("1", ((StudentDTO) response.getBody()).getId()),
                () -> assertEquals("Estudiante A", ((StudentDTO) response.getBody()).getName())
        );

        verify(studentService, times(1)).getStudentById("1");
    }

    @Test
    @DisplayName("Caso error - getStudentById retorna 404 cuando estudiante no existe")
    void testGetStudentById_NoEncontrado() {
        when(studentService.getStudentById("99"))
                .thenThrow(new AppException("Estudiante no encontrado"));

        ResponseEntity<?> response = studentController.getStudentById("99");

        assertAll("Verificar respuesta 404",
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertTrue(((Map<?, ?>) response.getBody()).containsKey("error"))
        );

        verify(studentService, times(1)).getStudentById("99");
    }

    @Test
    @DisplayName("Caso exitoso - getAllStudents retorna lista de estudiantes")
    void testGetAllStudents_Exitoso() {
        List<Student> students = Arrays.asList(student1, student2);
        when(studentService.getAllStudents()).thenReturn(students);

        ResponseEntity<?> response = studentController.getAllStudents();

        assertAll("Verificar obtención exitosa de todos los estudiantes",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> assertTrue(((Map<?, ?>) response.getBody()).containsKey("students")),
                () -> assertTrue(((Map<?, ?>) response.getBody()).containsKey("count")),
                () -> assertEquals(2, ((Map<?, ?>) response.getBody()).get("count"))
        );

        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    @DisplayName("Caso error - getAllStudents retorna error interno")
    void testGetAllStudents_ErrorInterno() {
        when(studentService.getAllStudents()).thenThrow(new RuntimeException("Error de base de datos"));

        ResponseEntity<?> response = studentController.getAllStudents();

        assertAll("Verificar error interno del servidor",
                () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertTrue(((Map<?, ?>) response.getBody()).containsKey("error"))
        );

        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    @DisplayName("Caso exitoso - updateStudent actualiza estudiante correctamente")
    void testUpdateStudent_Exitoso() {
        when(studentService.updateStudent(eq("1"), any(Student.class))).thenReturn(student1);

        ResponseEntity<?> response = studentController.updateStudent("1", studentDTO1);

        assertAll("Verificar actualización exitosa",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(StudentDTO.class, response.getBody())
        );

        verify(studentService, times(1)).updateStudent(eq("1"), any(Student.class));
    }

    @Test
    @DisplayName("Caso error - updateStudent retorna 404 cuando estudiante no existe")
    void testUpdateStudent_NoEncontrado() {
        when(studentService.updateStudent(eq("99"), any(Student.class)))
                .thenThrow(new AppException("Estudiante no encontrado"));

        ResponseEntity<?> response = studentController.updateStudent("99", studentDTO1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(studentService, times(1)).updateStudent(eq("99"), any(Student.class));
    }

    @Test
    @DisplayName("Caso exitoso - deleteStudent elimina estudiante correctamente")
    void testDeleteStudent_Exitoso() {
        doNothing().when(studentService).deleteStudent("1");

        ResponseEntity<?> response = studentController.deleteStudent("1");

        assertAll("Verificar eliminación exitosa",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> assertEquals("1", ((Map<?, ?>) response.getBody()).get("deletedId"))
        );

        verify(studentService, times(1)).deleteStudent("1");
    }

    @Test
    @DisplayName("Caso error - deleteStudent retorna 404 cuando estudiante no existe")
    void testDeleteStudent_NoEncontrado() {
        doThrow(new AppException("Estudiante no encontrado"))
                .when(studentService).deleteStudent("99");

        ResponseEntity<?> response = studentController.deleteStudent("99");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(studentService, times(1)).deleteStudent("99");
    }

    @Test
    @DisplayName("Caso exitoso - findByAcademicProgram retorna estudiantes del programa")
    void testFindByAcademicProgram_Exitoso() {
        List<Student> students = Arrays.asList(student1);
        when(studentService.findByAcademicProgram("Ingeniería")).thenReturn(students);

        ResponseEntity<?> response = studentController.findByAcademicProgram("Ingeniería");

        assertAll("Verificar búsqueda por programa exitosa",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> assertEquals("Ingeniería", ((Map<?, ?>) response.getBody()).get("program")),
                () -> assertEquals(1, ((Map<?, ?>) response.getBody()).get("count"))
        );

        verify(studentService, times(1)).findByAcademicProgram("Ingeniería");
    }

    @Test
    @DisplayName("Caso borde - findByAcademicProgram retorna lista vacía para programa sin estudiantes")
    void testFindByAcademicProgram_ProgramaSinEstudiantes() {
        when(studentService.findByAcademicProgram("Arquitectura")).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = studentController.findByAcademicProgram("Arquitectura");

        assertAll("Verificar programa sin estudiantes",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(0, ((Map<?, ?>) response.getBody()).get("count"))
        );

        verify(studentService, times(1)).findByAcademicProgram("Arquitectura");
    }

    @Test
    @DisplayName("Caso exitoso - findBySemester retorna estudiantes del semestre")
    void testFindBySemester_Exitoso() {
        List<Student> students = Arrays.asList(student1);
        when(studentService.findBySemester(5)).thenReturn(students);

        ResponseEntity<?> response = studentController.findBySemester(5);

        assertAll("Verificar búsqueda por semestre exitosa",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals(5, ((Map<?, ?>) response.getBody()).get("semester")),
                () -> assertEquals(1, ((Map<?, ?>) response.getBody()).get("count"))
        );

        verify(studentService, times(1)).findBySemester(5);
    }

    @Test
    @DisplayName("Caso exitoso - findByGradeAverageGreaterThan retorna estudiantes con promedio mayor")
    void testFindByGradeAverageGreaterThan_Exitoso() {
        List<Student> students = Arrays.asList(student1);
        when(studentService.findByGradeAverageGreaterThan(4.0)).thenReturn(students);

        ResponseEntity<?> response = studentController.findByGradeAverageGreaterThan(4.0);

        assertAll("Verificar búsqueda por promedio exitosa",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals(4.0, ((Map<?, ?>) response.getBody()).get("minGrade")),
                () -> assertEquals(1, ((Map<?, ?>) response.getBody()).get("count"))
        );

        verify(studentService, times(1)).findByGradeAverageGreaterThan(4.0);
    }

    @Test
    @DisplayName("Caso exitoso - getActiveStudents retorna estudiantes activos")
    void testGetActiveStudents_Exitoso() {
        List<Student> allStudents = Arrays.asList(student1, student2);
        when(studentService.getAllStudents()).thenReturn(allStudents);

        ResponseEntity<?> response = studentController.getActiveStudents();

        assertAll("Verificar obtención de estudiantes activos",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertTrue(((Map<?, ?>) response.getBody()).containsKey("activeStudents")),
                () -> assertTrue(((Map<?, ?>) response.getBody()).containsKey("activePercentage"))
        );

        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    @DisplayName("Caso exitoso - getStudentStatistics retorna estadísticas")
    void testGetStudentStatistics_Exitoso() {
        List<Student> allStudents = Arrays.asList(student1, student2);
        when(studentService.getAllStudents()).thenReturn(allStudents);

        ResponseEntity<?> response = studentController.getStudentStatistics();

        assertAll("Verificar obtención de estadísticas",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertTrue(((Map<?, ?>) response.getBody()).containsKey("totalStudents")),
                () -> assertTrue(((Map<?, ?>) response.getBody()).containsKey("averageGrade")),
                () -> assertTrue(((Map<?, ?>) response.getBody()).containsKey("studentsByProgram"))
        );

        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    @DisplayName("Caso exitoso - searchStudents retorna estudiantes filtrados")
    void testSearchStudents_Exitoso() {
        List<Student> allStudents = Arrays.asList(student1, student2);
        when(studentService.getAllStudents()).thenReturn(allStudents);

        ResponseEntity<?> response = studentController.searchStudents("Ingeniería", 5, 4.0, true);

        assertAll("Verificar búsqueda con filtros",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertTrue(((Map<?, ?>) response.getBody()).containsKey("searchCriteria")),
                () -> assertTrue(((Map<?, ?>) response.getBody()).containsKey("students"))
        );

        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    @DisplayName("Caso borde - getAllStudents retorna lista vacía cuando no hay estudiantes")
    void testGetAllStudents_ListaVacia() {
        when(studentService.getAllStudents()).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = studentController.getAllStudents();

        assertAll("Verificar lista vacía de estudiantes",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(0, ((Map<?, ?>) response.getBody()).get("count"))
        );

        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    @DisplayName("Caso error - createStudent retorna error interno por excepción genérica")
    void testCreateStudent_ErrorInterno() {
        when(studentService.createStudent(any(Student.class)))
                .thenThrow(new RuntimeException("Error de conexión"));

        ResponseEntity<?> response = studentController.createStudent(studentDTO1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(studentService, times(1)).createStudent(any(Student.class));
    }

    @Test
    @DisplayName("Caso borde - getActiveStudents con lista vacía")
    void testGetActiveStudents_ListaVacia() {
        when(studentService.getAllStudents()).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = studentController.getActiveStudents();

        assertAll("Verificar estudiantes activos con lista vacía",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(0, ((Map<?, ?>) response.getBody()).get("count")),
                () -> assertEquals(0.0, ((Map<?, ?>) response.getBody()).get("activePercentage"))
        );

        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    @DisplayName("Caso borde - getStudentStatistics con lista vacía")
    void testGetStudentStatistics_ListaVacia() {
        when(studentService.getAllStudents()).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = studentController.getStudentStatistics();

        assertAll("Verificar estadísticas con lista vacía",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(0, ((Map<?, ?>) response.getBody()).get("totalStudents")),
                () -> assertEquals(0.0, ((Map<?, ?>) response.getBody()).get("averageGrade"))
        );

        verify(studentService, times(1)).getAllStudents();
    }

}
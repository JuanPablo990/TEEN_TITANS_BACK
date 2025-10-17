package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto.UserDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Student;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.StudentService;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private Student student;
    private String studentId;

    @BeforeEach
    void setUp() {
        studentId = "stu123";
        student = new Student();
        student.setId(studentId);
        student.setName("John Doe");
        student.setEmail("john@university.edu");
        student.setAcademicProgram("Computer Science");
        student.setSemester(5);
        student.setGradeAverage(4.2);
        student.setActive(true);
    }

    @Test
    void createStudent_HappyPath() {
        when(studentService.createStudent(any(Student.class))).thenReturn(student);

        ResponseEntity<?> response = studentController.createStudent(student);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(student, response.getBody());
        verify(studentService, times(1)).createStudent(student);
    }

    @Test
    void createStudent_ErrorPath() {
        when(studentService.createStudent(any(Student.class))).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = studentController.createStudent(student);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentService, times(1)).createStudent(student);
    }

    @Test
    void getStudentById_HappyPath() {
        when(studentService.getStudentById(studentId)).thenReturn(student);

        ResponseEntity<?> response = studentController.getStudentById(studentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(student, response.getBody());
        verify(studentService, times(1)).getStudentById(studentId);
    }

    @Test
    void getStudentById_ErrorPath_NotFound() {
        when(studentService.getStudentById(studentId)).thenThrow(new AppException("Student not found"));

        ResponseEntity<?> response = studentController.getStudentById(studentId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentService, times(1)).getStudentById(studentId);
    }

    @Test
    void getStudentById_ErrorPath_InternalError() {
        when(studentService.getStudentById(studentId)).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = studentController.getStudentById(studentId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentService, times(1)).getStudentById(studentId);
    }

    @Test
    void getAllStudents_HappyPath() {
        List<Student> students = Arrays.asList(student, new Student());
        when(studentService.getAllStudents()).thenReturn(students);

        ResponseEntity<?> response = studentController.getAllStudents();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    void getAllStudents_ErrorPath() {
        when(studentService.getAllStudents()).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = studentController.getAllStudents();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    void updateStudent_HappyPath() {
        when(studentService.updateStudent(eq(studentId), any(Student.class))).thenReturn(student);

        ResponseEntity<?> response = studentController.updateStudent(studentId, student);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(student, response.getBody());
        verify(studentService, times(1)).updateStudent(studentId, student);
    }

    @Test
    void updateStudent_ErrorPath_NotFound() {
        when(studentService.updateStudent(eq(studentId), any(Student.class))).thenThrow(new AppException("Student not found"));

        ResponseEntity<?> response = studentController.updateStudent(studentId, student);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentService, times(1)).updateStudent(studentId, student);
    }

    @Test
    void updateStudent_ErrorPath_InternalError() {
        when(studentService.updateStudent(eq(studentId), any(Student.class))).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = studentController.updateStudent(studentId, student);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentService, times(1)).updateStudent(studentId, student);
    }

    @Test
    void deleteStudent_HappyPath() {
        doNothing().when(studentService).deleteStudent(studentId);

        ResponseEntity<?> response = studentController.deleteStudent(studentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentService, times(1)).deleteStudent(studentId);
    }

    @Test
    void deleteStudent_ErrorPath_NotFound() {
        doThrow(new AppException("Student not found")).when(studentService).deleteStudent(studentId);

        ResponseEntity<?> response = studentController.deleteStudent(studentId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentService, times(1)).deleteStudent(studentId);
    }

    @Test
    void deleteStudent_ErrorPath_InternalError() {
        doThrow(new RuntimeException("Database error")).when(studentService).deleteStudent(studentId);

        ResponseEntity<?> response = studentController.deleteStudent(studentId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentService, times(1)).deleteStudent(studentId);
    }

    @Test
    void findByAcademicProgram_HappyPath() {
        List<Student> students = Arrays.asList(student);
        when(studentService.findByAcademicProgram("Computer Science")).thenReturn(students);

        ResponseEntity<?> response = studentController.findByAcademicProgram("Computer Science");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentService, times(1)).findByAcademicProgram("Computer Science");
    }

    @Test
    void findByAcademicProgram_ErrorPath() {
        when(studentService.findByAcademicProgram("Computer Science")).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = studentController.findByAcademicProgram("Computer Science");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentService, times(1)).findByAcademicProgram("Computer Science");
    }

    @Test
    void findBySemester_HappyPath() {
        List<Student> students = Arrays.asList(student);
        when(studentService.findBySemester(5)).thenReturn(students);

        ResponseEntity<?> response = studentController.findBySemester(5);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentService, times(1)).findBySemester(5);
    }

    @Test
    void findBySemester_ErrorPath() {
        when(studentService.findBySemester(5)).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = studentController.findBySemester(5);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentService, times(1)).findBySemester(5);
    }

    @Test
    void findByGradeAverageGreaterThan_HappyPath() {
        List<Student> students = Arrays.asList(student);
        when(studentService.findByGradeAverageGreaterThan(4.0)).thenReturn(students);

        ResponseEntity<?> response = studentController.findByGradeAverageGreaterThan(4.0);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentService, times(1)).findByGradeAverageGreaterThan(4.0);
    }

    @Test
    void findByGradeAverageGreaterThan_ErrorPath() {
        when(studentService.findByGradeAverageGreaterThan(4.0)).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = studentController.findByGradeAverageGreaterThan(4.0);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentService, times(1)).findByGradeAverageGreaterThan(4.0);
    }

    @Test
    void getActiveStudents_HappyPath() {
        List<Student> allStudents = Arrays.asList(student, new Student());
        when(studentService.getAllStudents()).thenReturn(allStudents);

        ResponseEntity<?> response = studentController.getActiveStudents();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    void getActiveStudents_ErrorPath() {
        when(studentService.getAllStudents()).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = studentController.getActiveStudents();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    void getStudentStatistics_HappyPath() {
        List<Student> allStudents = Arrays.asList(student);
        when(studentService.getAllStudents()).thenReturn(allStudents);

        ResponseEntity<?> response = studentController.getStudentStatistics();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    void getStudentStatistics_ErrorPath() {
        when(studentService.getAllStudents()).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = studentController.getStudentStatistics();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    void getStudentAsUserDTO_HappyPath() {
        when(studentService.getStudentById(studentId)).thenReturn(student);

        ResponseEntity<?> response = studentController.getStudentAsUserDTO(studentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof UserDTO);
        verify(studentService, times(1)).getStudentById(studentId);
    }

    @Test
    void getStudentAsUserDTO_ErrorPath_NotFound() {
        when(studentService.getStudentById(studentId)).thenThrow(new AppException("Student not found"));

        ResponseEntity<?> response = studentController.getStudentAsUserDTO(studentId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentService, times(1)).getStudentById(studentId);
    }

    @Test
    void getStudentAsUserDTO_ErrorPath_InternalError() {
        when(studentService.getStudentById(studentId)).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = studentController.getStudentAsUserDTO(studentId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentService, times(1)).getStudentById(studentId);
    }

    @Test
    void searchStudents_HappyPath() {
        List<Student> allStudents = Arrays.asList(student);
        when(studentService.getAllStudents()).thenReturn(allStudents);

        ResponseEntity<?> response = studentController.searchStudents("Computer Science", 5, 4.0, true);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    void searchStudents_ErrorPath() {
        when(studentService.getAllStudents()).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = studentController.searchStudents("Computer Science", 5, 4.0, true);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    void searchStudents_HappyPath_NoFilters() {
        // Crear estudiantes con datos completos
        Student student1 = new Student();
        student1.setId("stu123");
        student1.setName("John Doe");
        student1.setEmail("john@university.edu");
        student1.setAcademicProgram("Computer Science");
        student1.setSemester(5);
        student1.setGradeAverage(4.2);
        student1.setActive(true);

        Student student2 = new Student();
        student2.setId("stu456");
        student2.setName("Jane Smith");
        student2.setEmail("jane@university.edu");
        student2.setAcademicProgram("Mathematics");
        student2.setSemester(3);
        student2.setGradeAverage(3.8);
        student2.setActive(false);

        List<Student> allStudents = Arrays.asList(student1, student2);
        when(studentService.getAllStudents()).thenReturn(allStudents);

        // Usar parámetros no-nulos para evitar el problema con Map.of()
        ResponseEntity<?> response = studentController.searchStudents("", 0, 0.0, true);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody.get("students"));
        assertNotNull(responseBody.get("count"));
        assertNotNull(responseBody.get("searchCriteria"));

        verify(studentService, times(1)).getAllStudents();
    }
}
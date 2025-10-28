package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto.LoginRequest;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private DeanRepository deanRepository;

    @Mock
    private AdministratorRepository administratorRepository;

    @InjectMocks
    private AuthService authService;

    private Student testStudent;
    private Professor testProfessor;
    private Dean testDean;
    private Administrator testAdmin;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        testStudent = new Student("STU001", "Bruce Wayne", "bruce@wayne.com", "password123", "Ingeniería", 5);
        testStudent.setActive(true);

        testProfessor = new Professor("PROF001", "Alfred Pennyworth", "alfred@wayne.com", "prof123", "Computer Science", true, List.of("Java"));
        testProfessor.setActive(true);

        testDean = new Dean("DEAN001", "Clark Kent", "clark@dailyplanet.com", "dean123", "Ingeniería", "Oficina 101");
        testDean.setActive(true);

        testAdmin = new Administrator("ADMIN001", "Diana Prince", "diana@themyscira.com", "admin123", "Tecnología");
        testAdmin.setActive(true);

        loginRequest = new LoginRequest();
    }

    @Test
    @DisplayName("Éxito - login estudiante con credenciales válidas")
    void testLogin_EstudianteExitoso() {
        loginRequest.setEmail("bruce@wayne.com");
        loginRequest.setPassword("password123");
        loginRequest.setRole(UserRole.STUDENT);

        when(studentRepository.findByEmail("bruce@wayne.com")).thenReturn(List.of(testStudent));

        Object resultado = authService.login(loginRequest);

        assertAll("Verificar login estudiante exitoso",
                () -> assertNotNull(resultado),
                () -> assertInstanceOf(Student.class, resultado),
                () -> assertEquals("STU001", ((Student) resultado).getId()),
                () -> assertEquals("Bruce Wayne", ((Student) resultado).getName())
        );

        verify(studentRepository, times(1)).findByEmail("bruce@wayne.com");
    }

    @Test
    @DisplayName("Éxito - login profesor con credenciales válidas")
    void testLogin_ProfesorExitoso() {
        loginRequest.setEmail("alfred@wayne.com");
        loginRequest.setPassword("prof123");
        loginRequest.setRole(UserRole.PROFESSOR);

        when(professorRepository.findByEmail("alfred@wayne.com")).thenReturn(List.of(testProfessor));

        Object resultado = authService.login(loginRequest);

        assertAll("Verificar login profesor exitoso",
                () -> assertNotNull(resultado),
                () -> assertInstanceOf(Professor.class, resultado),
                () -> assertEquals("PROF001", ((Professor) resultado).getId()),
                () -> assertEquals("Alfred Pennyworth", ((Professor) resultado).getName())
        );

        verify(professorRepository, times(1)).findByEmail("alfred@wayne.com");
    }

    @Test
    @DisplayName("Éxito - login decano con credenciales válidas")
    void testLogin_DeanExitoso() {
        loginRequest.setEmail("clark@dailyplanet.com");
        loginRequest.setPassword("dean123");
        loginRequest.setRole(UserRole.DEAN);

        when(deanRepository.findByEmail("clark@dailyplanet.com")).thenReturn(List.of(testDean));

        Object resultado = authService.login(loginRequest);

        assertAll("Verificar login decano exitoso",
                () -> assertNotNull(resultado),
                () -> assertInstanceOf(Dean.class, resultado),
                () -> assertEquals("DEAN001", ((Dean) resultado).getId()),
                () -> assertEquals("Clark Kent", ((Dean) resultado).getName())
        );

        verify(deanRepository, times(1)).findByEmail("clark@dailyplanet.com");
    }

    @Test
    @DisplayName("Éxito - login administrador con credenciales válidas")
    void testLogin_AdministradorExitoso() {
        loginRequest.setEmail("diana@themyscira.com");
        loginRequest.setPassword("admin123");
        loginRequest.setRole(UserRole.ADMINISTRATOR);

        when(administratorRepository.findByEmail("diana@themyscira.com")).thenReturn(List.of(testAdmin));

        Object resultado = authService.login(loginRequest);

        assertAll("Verificar login administrador exitoso",
                () -> assertNotNull(resultado),
                () -> assertInstanceOf(Administrator.class, resultado),
                () -> assertEquals("ADMIN001", ((Administrator) resultado).getId()),
                () -> assertEquals("Diana Prince", ((Administrator) resultado).getName())
        );

        verify(administratorRepository, times(1)).findByEmail("diana@themyscira.com");
    }

    @Test
    @DisplayName("Error - login estudiante con contraseña incorrecta")
    void testLogin_EstudianteContraseñaIncorrecta() {
        loginRequest.setEmail("bruce@wayne.com");
        loginRequest.setPassword("wrongpassword");
        loginRequest.setRole(UserRole.STUDENT);

        when(studentRepository.findByEmail("bruce@wayne.com")).thenReturn(List.of(testStudent));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.login(loginRequest);
        });

        assertEquals("Contraseña incorrecta", exception.getMessage());
        verify(studentRepository, times(1)).findByEmail("bruce@wayne.com");
    }

    @Test
    @DisplayName("Error - login estudiante no encontrado")
    void testLogin_EstudianteNoEncontrado() {
        loginRequest.setEmail("nonexistent@email.com");
        loginRequest.setPassword("password123");
        loginRequest.setRole(UserRole.STUDENT);

        when(studentRepository.findByEmail("nonexistent@email.com")).thenReturn(List.of());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.login(loginRequest);
        });

        assertEquals("Usuario no encontrado o inactivo", exception.getMessage());
        verify(studentRepository, times(1)).findByEmail("nonexistent@email.com");
    }

    @Test
    @DisplayName("Error - login estudiante inactivo")
    void testLogin_EstudianteInactivo() {
        testStudent.setActive(false);
        loginRequest.setEmail("bruce@wayne.com");
        loginRequest.setPassword("password123");
        loginRequest.setRole(UserRole.STUDENT);

        when(studentRepository.findByEmail("bruce@wayne.com")).thenReturn(List.of(testStudent));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.login(loginRequest);
        });

        assertEquals("Usuario no encontrado o inactivo", exception.getMessage());
        verify(studentRepository, times(1)).findByEmail("bruce@wayne.com");
    }

    @Test
    @DisplayName("Error - login profesor inactivo")
    void testLogin_ProfesorInactivo() {
        testProfessor.setActive(false);
        loginRequest.setEmail("alfred@wayne.com");
        loginRequest.setPassword("prof123");
        loginRequest.setRole(UserRole.PROFESSOR);

        when(professorRepository.findByEmail("alfred@wayne.com")).thenReturn(List.of(testProfessor));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.login(loginRequest);
        });

        assertEquals("Usuario no encontrado o inactivo", exception.getMessage());
        verify(professorRepository, times(1)).findByEmail("alfred@wayne.com");
    }

    @Test
    @DisplayName("Error - login decano inactivo")
    void testLogin_DeanInactivo() {
        testDean.setActive(false);
        loginRequest.setEmail("clark@dailyplanet.com");
        loginRequest.setPassword("dean123");
        loginRequest.setRole(UserRole.DEAN);

        when(deanRepository.findByEmail("clark@dailyplanet.com")).thenReturn(List.of(testDean));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.login(loginRequest);
        });

        assertEquals("Usuario no encontrado o inactivo", exception.getMessage());
        verify(deanRepository, times(1)).findByEmail("clark@dailyplanet.com");
    }

    @Test
    @DisplayName("Error - login administrador inactivo")
    void testLogin_AdministradorInactivo() {
        testAdmin.setActive(false);
        loginRequest.setEmail("diana@themyscira.com");
        loginRequest.setPassword("admin123");
        loginRequest.setRole(UserRole.ADMINISTRATOR);

        when(administratorRepository.findByEmail("diana@themyscira.com")).thenReturn(List.of(testAdmin));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.login(loginRequest);
        });

        assertEquals("Usuario no encontrado o inactivo", exception.getMessage());
        verify(administratorRepository, times(1)).findByEmail("diana@themyscira.com");
    }



    @Test
    @DisplayName("Caso borde - login con email case insensitive")
    void testLogin_EmailCaseInsensitive() {
        loginRequest.setEmail("BRUCE@WAYNE.COM");
        loginRequest.setPassword("password123");
        loginRequest.setRole(UserRole.STUDENT);

        when(studentRepository.findByEmail("BRUCE@WAYNE.COM")).thenReturn(List.of(testStudent));

        Object resultado = authService.login(loginRequest);

        assertNotNull(resultado);
        assertInstanceOf(Student.class, resultado);
        verify(studentRepository, times(1)).findByEmail("BRUCE@WAYNE.COM");
    }

    @Test
    @DisplayName("Caso borde - login con contraseña case sensitive")
    void testLogin_ContraseñaCaseSensitive() {
        loginRequest.setEmail("bruce@wayne.com");
        loginRequest.setPassword("PASSWORD123");
        loginRequest.setRole(UserRole.STUDENT);

        when(studentRepository.findByEmail("bruce@wayne.com")).thenReturn(List.of(testStudent));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.login(loginRequest);
        });

        assertEquals("Contraseña incorrecta", exception.getMessage());
        verify(studentRepository, times(1)).findByEmail("bruce@wayne.com");
    }

    @Test
    @DisplayName("Caso borde - login con múltiples usuarios mismo email diferente rol")
    void testLogin_MultiplesUsuariosMismoEmail() {
        loginRequest.setEmail("user@university.com");
        loginRequest.setPassword("student123");
        loginRequest.setRole(UserRole.STUDENT);

        Student student = new Student("STU002", "Student Name", "user@university.com", "student123", "Program", 3);
        student.setActive(true);

        when(studentRepository.findByEmail("user@university.com")).thenReturn(List.of(student));

        Object resultado = authService.login(loginRequest);

        assertNotNull(resultado);
        assertInstanceOf(Student.class, resultado);
        assertEquals("STU002", ((Student) resultado).getId());
        verify(studentRepository, times(1)).findByEmail("user@university.com");
        verify(professorRepository, never()).findByEmail(anyString());
        verify(deanRepository, never()).findByEmail(anyString());
        verify(administratorRepository, never()).findByEmail(anyString());
    }

    @Test
    @DisplayName("Caso borde - login con usuario activo entre inactivos")
    void testLogin_UsuarioActivoEntreInactivos() {
        Student inactiveStudent = new Student("STU003", "Inactive Student", "mixed@email.com", "pass123", "Program", 2);
        inactiveStudent.setActive(false);

        Student activeStudent = new Student("STU004", "Active Student", "mixed@email.com", "correctpass", "Program", 3);
        activeStudent.setActive(true);

        loginRequest.setEmail("mixed@email.com");
        loginRequest.setPassword("correctpass");
        loginRequest.setRole(UserRole.STUDENT);

        when(studentRepository.findByEmail("mixed@email.com")).thenReturn(List.of(inactiveStudent, activeStudent));

        Object resultado = authService.login(loginRequest);

        assertNotNull(resultado);
        assertInstanceOf(Student.class, resultado);
        assertEquals("STU004", ((Student) resultado).getId());
        verify(studentRepository, times(1)).findByEmail("mixed@email.com");
    }

    @Test
    @DisplayName("Caso borde - login con todos los usuarios inactivos")
    void testLogin_TodosUsuariosInactivos() {
        Student inactiveStudent1 = new Student("STU005", "Inactive 1", "inactive@email.com", "pass1", "Program", 2);
        inactiveStudent1.setActive(false);

        Student inactiveStudent2 = new Student("STU006", "Inactive 2", "inactive@email.com", "pass2", "Program", 3);
        inactiveStudent2.setActive(false);

        loginRequest.setEmail("inactive@email.com");
        loginRequest.setPassword("pass1");
        loginRequest.setRole(UserRole.STUDENT);

        when(studentRepository.findByEmail("inactive@email.com")).thenReturn(List.of(inactiveStudent1, inactiveStudent2));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.login(loginRequest);
        });

        assertEquals("Usuario no encontrado o inactivo", exception.getMessage());
        verify(studentRepository, times(1)).findByEmail("inactive@email.com");
    }

    @Test
    @DisplayName("Caso borde - login con contraseña vacía")
    void testLogin_ContraseñaVacia() {
        loginRequest.setEmail("bruce@wayne.com");
        loginRequest.setPassword("");
        loginRequest.setRole(UserRole.STUDENT);

        when(studentRepository.findByEmail("bruce@wayne.com")).thenReturn(List.of(testStudent));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.login(loginRequest);
        });

        assertEquals("Contraseña incorrecta", exception.getMessage());
        verify(studentRepository, times(1)).findByEmail("bruce@wayne.com");
    }

    @Test
    @DisplayName("Caso borde - login con email vacío")
    void testLogin_EmailVacio() {
        loginRequest.setEmail("");
        loginRequest.setPassword("password123");
        loginRequest.setRole(UserRole.STUDENT);

        when(studentRepository.findByEmail("")).thenReturn(List.of());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.login(loginRequest);
        });

        assertEquals("Usuario no encontrado o inactivo", exception.getMessage());
        verify(studentRepository, times(1)).findByEmail("");
    }

    @Test
    @DisplayName("Caso borde - login con null en email")
    void testLogin_EmailNull() {
        loginRequest.setEmail(null);
        loginRequest.setPassword("password123");
        loginRequest.setRole(UserRole.STUDENT);

        when(studentRepository.findByEmail(null)).thenReturn(List.of());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.login(loginRequest);
        });

        assertEquals("Usuario no encontrado o inactivo", exception.getMessage());
        verify(studentRepository, times(1)).findByEmail(null);
    }

    @Test
    @DisplayName("Caso borde - login con null en contraseña")
    void testLogin_ContraseñaNull() {
        loginRequest.setEmail("bruce@wayne.com");
        loginRequest.setPassword(null);
        loginRequest.setRole(UserRole.STUDENT);

        when(studentRepository.findByEmail("bruce@wayne.com")).thenReturn(List.of(testStudent));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.login(loginRequest);
        });

        assertEquals("Contraseña incorrecta", exception.getMessage());
        verify(studentRepository, times(1)).findByEmail("bruce@wayne.com");
    }

    @Test
    @DisplayName("Caso borde - login con múltiples usuarios activos mismo email")
    void testLogin_MultiplesUsuariosActivosMismoEmail() {
        Student student1 = new Student("STU007", "Student One", "duplicate@email.com", "pass1", "Program", 2);
        student1.setActive(true);

        Student student2 = new Student("STU008", "Student Two", "duplicate@email.com", "pass2", "Program", 3);
        student2.setActive(true);

        loginRequest.setEmail("duplicate@email.com");
        loginRequest.setPassword("pass1");
        loginRequest.setRole(UserRole.STUDENT);

        when(studentRepository.findByEmail("duplicate@email.com")).thenReturn(List.of(student1, student2));

        Object resultado = authService.login(loginRequest);

        assertNotNull(resultado);
        assertInstanceOf(Student.class, resultado);
        assertEquals("STU007", ((Student) resultado).getId());
        verify(studentRepository, times(1)).findByEmail("duplicate@email.com");
    }
}
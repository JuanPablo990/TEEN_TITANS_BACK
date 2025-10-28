package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto.LoginRequest;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Student;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private LoginRequest loginRequestValido;
    private LoginRequest loginRequestInvalido;
    private Student estudianteActivo;

    @BeforeEach
    void setUp() {
        loginRequestValido = new LoginRequest();
        loginRequestValido.setEmail("robin@titans.edu");
        loginRequestValido.setPassword("password123");
        loginRequestValido.setRole(UserRole.STUDENT);

        loginRequestInvalido = new LoginRequest();
        loginRequestInvalido.setEmail("usuario@titans.edu");
        loginRequestInvalido.setPassword("passwordErroneo");
        loginRequestInvalido.setRole(UserRole.STUDENT);

        estudianteActivo = new Student();
        estudianteActivo.setEmail("robin@titans.edu");
        estudianteActivo.setPassword("password123");
        estudianteActivo.setActive(true);
    }

    @Test
    @DisplayName("Caso exitoso - Login con credenciales válidas")
    void testLogin_Exitoso() {
        when(authService.login(loginRequestValido))
                .thenReturn(estudianteActivo);

        ResponseEntity<?> respuesta = authController.login(loginRequestValido);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());

        Map<String, Object> cuerpoRespuesta = (Map<String, Object>) respuesta.getBody();
        assertAll("Validar respuesta exitosa",
                () -> assertNotNull(cuerpoRespuesta),
                () -> assertTrue((Boolean) cuerpoRespuesta.get("success")),
                () -> assertEquals("Login exitoso", cuerpoRespuesta.get("message")),
                () -> assertEquals(estudianteActivo, cuerpoRespuesta.get("user"))
        );

        verify(authService, times(1)).login(loginRequestValido);
    }

    @Test
    @DisplayName("Caso error - Credenciales inválidas")
    void testLogin_CredencialesInvalidas() {
        when(authService.login(loginRequestInvalido))
                .thenThrow(new RuntimeException("Contraseña incorrecta"));

        ResponseEntity<?> respuesta = authController.login(loginRequestInvalido);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());

        Map<String, Object> cuerpoRespuesta = (Map<String, Object>) respuesta.getBody();
        assertAll("Validar respuesta de error",
                () -> assertNotNull(cuerpoRespuesta),
                () -> assertFalse((Boolean) cuerpoRespuesta.get("success")),
                () -> assertEquals("Contraseña incorrecta", cuerpoRespuesta.get("message")),
                () -> assertNull(cuerpoRespuesta.get("user"))
        );

        verify(authService, times(1)).login(loginRequestInvalido);
    }

    @Test
    @DisplayName("Caso error - Usuario no encontrado")
    void testLogin_UsuarioNoEncontrado() {
        when(authService.login(loginRequestInvalido))
                .thenThrow(new RuntimeException("Usuario no encontrado o inactivo"));

        ResponseEntity<?> respuesta = authController.login(loginRequestInvalido);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());

        Map<String, Object> cuerpoRespuesta = (Map<String, Object>) respuesta.getBody();
        assertAll("Validar respuesta usuario no encontrado",
                () -> assertFalse((Boolean) cuerpoRespuesta.get("success")),
                () -> assertEquals("Usuario no encontrado o inactivo", cuerpoRespuesta.get("message")),
                () -> assertNull(cuerpoRespuesta.get("user"))
        );

        verify(authService, times(1)).login(loginRequestInvalido);
    }

    @Test
    @DisplayName("Caso error - Rol no válido")
    void testLogin_RolNoValido() {
        LoginRequest requestRolInvalido = new LoginRequest();
        requestRolInvalido.setEmail("usuario@titans.edu");
        requestRolInvalido.setPassword("password123");
        requestRolInvalido.setRole(null);

        when(authService.login(requestRolInvalido))
                .thenThrow(new RuntimeException("Rol no válido: null"));

        ResponseEntity<?> respuesta = authController.login(requestRolInvalido);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());

        Map<String, Object> cuerpoRespuesta = (Map<String, Object>) respuesta.getBody();
        assertAll("Validar respuesta rol no válido",
                () -> assertFalse((Boolean) cuerpoRespuesta.get("success")),
                () -> assertEquals("Rol no válido: null", cuerpoRespuesta.get("message")),
                () -> assertNull(cuerpoRespuesta.get("user"))
        );

        verify(authService, times(1)).login(requestRolInvalido);
    }

    @Test
    @DisplayName("Caso borde - Login con request null")
    void testLogin_RequestNull() {
        when(authService.login(null))
                .thenThrow(new RuntimeException("Usuario no encontrado o inactivo"));

        ResponseEntity<?> respuesta = authController.login(null);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());

        Map<String, Object> cuerpoRespuesta = (Map<String, Object>) respuesta.getBody();
        assertAll("Validar respuesta con request null",
                () -> assertFalse((Boolean) cuerpoRespuesta.get("success")),
                () -> assertEquals("Usuario no encontrado o inactivo", cuerpoRespuesta.get("message")),
                () -> assertNull(cuerpoRespuesta.get("user"))
        );

        verify(authService, times(1)).login(null);
    }

    @Test
    @DisplayName("Caso borde - Login con campos vacíos")
    void testLogin_CamposVacios() {
        LoginRequest requestVacio = new LoginRequest();
        requestVacio.setEmail("");
        requestVacio.setPassword("");
        requestVacio.setRole(UserRole.STUDENT);

        when(authService.login(requestVacio))
                .thenThrow(new RuntimeException("Usuario no encontrado o inactivo"));

        ResponseEntity<?> respuesta = authController.login(requestVacio);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());

        Map<String, Object> cuerpoRespuesta = (Map<String, Object>) respuesta.getBody();
        assertAll("Validar respuesta con campos vacíos",
                () -> assertFalse((Boolean) cuerpoRespuesta.get("success")),
                () -> assertEquals("Usuario no encontrado o inactivo", cuerpoRespuesta.get("message")),
                () -> assertNull(cuerpoRespuesta.get("user"))
        );

        verify(authService, times(1)).login(requestVacio);
    }

    @Test
    @DisplayName("Caso excepción - No propaga excepción")
    void testLogin_NoPropagaExcepcion() {
        when(authService.login(loginRequestInvalido))
                .thenThrow(new RuntimeException("Error inesperado"));

        assertDoesNotThrow(() -> {
            ResponseEntity<?> respuesta = authController.login(loginRequestInvalido);
            assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        });

        verify(authService, times(1)).login(loginRequestInvalido);
    }

    @Test
    @DisplayName("Caso exitoso - Diferentes tipos de usuario")
    void testLogin_DiferentesTiposUsuario() {
        LoginRequest requestProfesor = new LoginRequest();
        requestProfesor.setEmail("starfire@titans.edu");
        requestProfesor.setPassword("password123");
        requestProfesor.setRole(UserRole.PROFESSOR);

        when(authService.login(requestProfesor))
                .thenReturn(estudianteActivo);

        ResponseEntity<?> respuesta = authController.login(requestProfesor);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());

        Map<String, Object> cuerpoRespuesta = (Map<String, Object>) respuesta.getBody();
        assertTrue((Boolean) cuerpoRespuesta.get("success"));
        assertEquals("Login exitoso", cuerpoRespuesta.get("message"));

        verify(authService, times(1)).login(requestProfesor);
    }
}
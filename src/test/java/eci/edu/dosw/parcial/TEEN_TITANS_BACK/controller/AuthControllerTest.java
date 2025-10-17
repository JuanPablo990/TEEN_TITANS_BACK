package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.User;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.UserRole;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.AuthService;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private User testUser;
    private String validToken;

    @BeforeEach
    void setUp() {
        testUser = new User("123", "John Doe", "john@university.edu", "password123", UserRole.STUDENT);
        validToken = "valid.jwt.token";
    }

    @Test
    void login_HappyPath() {
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("email", "john@university.edu");
        loginRequest.put("password", "password123");

        Map<String, Object> authResult = new HashMap<>();
        authResult.put("success", true);
        authResult.put("token", validToken);
        authResult.put("user", testUser);

        when(authService.login("john@university.edu", "password123")).thenReturn(authResult);

        ResponseEntity<?> response = authController.login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResult, response.getBody());
        verify(authService, times(1)).login("john@university.edu", "password123");
    }

    @Test
    void login_ErrorPath_InvalidCredentials() {
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("email", "john@university.edu");
        loginRequest.put("password", "wrongpassword");

        Map<String, Object> authResult = new HashMap<>();
        authResult.put("success", false);
        authResult.put("message", "Invalid credentials");

        when(authService.login("john@university.edu", "wrongpassword")).thenReturn(authResult);

        ResponseEntity<?> response = authController.login(loginRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(authResult, response.getBody());
        verify(authService, times(1)).login("john@university.edu", "wrongpassword");
    }

    @Test
    void login_ErrorPath_MissingEmail() {
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("password", "password123");

        ResponseEntity<?> response = authController.login(loginRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El campo 'email' es requerido", response.getBody());
        verify(authService, never()).login(anyString(), anyString());
    }

    @Test
    void login_ErrorPath_MissingPassword() {
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("email", "john@university.edu");

        ResponseEntity<?> response = authController.login(loginRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El campo 'password' es requerido", response.getBody());
        verify(authService, never()).login(anyString(), anyString());
    }

    @Test
    void register_HappyPath() {
        when(authService.register(testUser)).thenReturn(testUser);

        ResponseEntity<?> response = authController.register(testUser);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testUser, response.getBody());
        verify(authService, times(1)).register(testUser);
    }

    @Test
    void register_ErrorPath_AppException() {
        when(authService.register(testUser)).thenThrow(new AppException("Email already exists"));

        ResponseEntity<?> response = authController.register(testUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email already exists", response.getBody());
        verify(authService, times(1)).register(testUser);
    }

    @Test
    void register_ErrorPath_MissingEmail() {
        User invalidUser = new User();
        invalidUser.setName("John Doe");
        invalidUser.setPassword("password123");

        ResponseEntity<?> response = authController.register(invalidUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El campo 'email' es requerido", response.getBody());
        verify(authService, never()).register(any(User.class));
    }

    @Test
    void register_ErrorPath_MissingPassword() {
        User invalidUser = new User();
        invalidUser.setName("John Doe");
        invalidUser.setEmail("john@university.edu");

        ResponseEntity<?> response = authController.register(invalidUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El campo 'password' es requerido", response.getBody());
        verify(authService, never()).register(any(User.class));
    }

    @Test
    void register_ErrorPath_MissingName() {
        User invalidUser = new User();
        invalidUser.setEmail("john@university.edu");
        invalidUser.setPassword("password123");

        ResponseEntity<?> response = authController.register(invalidUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El campo 'name' es requerido", response.getBody());
        verify(authService, never()).register(any(User.class));
    }

    @Test
    void validateToken_HappyPath() {
        Map<String, String> tokenRequest = new HashMap<>();
        tokenRequest.put("token", validToken);

        when(authService.validateToken(validToken)).thenReturn(true);

        ResponseEntity<?> response = authController.validateToken(tokenRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(authService, times(1)).validateToken(validToken);
    }

    @Test
    void validateToken_ErrorPath_InvalidToken() {
        Map<String, String> tokenRequest = new HashMap<>();
        tokenRequest.put("token", "invalid.token");

        when(authService.validateToken("invalid.token")).thenReturn(false);

        ResponseEntity<?> response = authController.validateToken(tokenRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(authService, times(1)).validateToken("invalid.token");
    }

    @Test
    void validateToken_ErrorPath_MissingToken() {
        Map<String, String> tokenRequest = new HashMap<>();

        ResponseEntity<?> response = authController.validateToken(tokenRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El campo 'token' es requerido", response.getBody());
        verify(authService, never()).validateToken(anyString());
    }

    @Test
    void getCurrentUser_HappyPath() {
        String authHeader = "Bearer " + validToken;

        when(authService.validateAndGetUserFromToken(validToken)).thenReturn(testUser);

        ResponseEntity<?> response = authController.getCurrentUser(authHeader);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUser, response.getBody());
        verify(authService, times(1)).validateAndGetUserFromToken(validToken);
    }

    @Test
    void getCurrentUser_ErrorPath_InvalidToken() {
        String authHeader = "Bearer invalid.token";

        when(authService.validateAndGetUserFromToken("invalid.token")).thenThrow(new AppException("Invalid token"));

        ResponseEntity<?> response = authController.getCurrentUser(authHeader);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid token", response.getBody());
        verify(authService, times(1)).validateAndGetUserFromToken("invalid.token");
    }

    @Test
    void getCurrentUser_ErrorPath_MissingHeader() {
        ResponseEntity<?> response = authController.getCurrentUser(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Token de autorización requerido", response.getBody());
        verify(authService, never()).validateAndGetUserFromToken(anyString());
    }

    @Test
    void logout_HappyPath() {
        String authHeader = "Bearer " + validToken;

        doNothing().when(authService).logout(validToken);

        ResponseEntity<?> response = authController.logout(authHeader);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(authService, times(1)).logout(validToken);
    }

    @Test
    void logout_ErrorPath_MissingHeader() {
        ResponseEntity<?> response = authController.logout(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Token de autorización requerido", response.getBody());
        verify(authService, never()).logout(anyString());
    }

    @Test
    void getUserFromToken_HappyPath() {
        Map<String, String> tokenRequest = new HashMap<>();
        tokenRequest.put("token", validToken);

        when(authService.getUserFromToken(validToken)).thenReturn(testUser);

        ResponseEntity<?> response = authController.getUserFromToken(tokenRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUser, response.getBody());
        verify(authService, times(1)).getUserFromToken(validToken);
    }

    @Test
    void getUserFromToken_ErrorPath_UserNotFound() {
        Map<String, String> tokenRequest = new HashMap<>();
        tokenRequest.put("token", validToken);

        when(authService.getUserFromToken(validToken)).thenReturn(null);

        ResponseEntity<?> response = authController.getUserFromToken(tokenRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No se encontró usuario para el token proporcionado", response.getBody());
        verify(authService, times(1)).getUserFromToken(validToken);
    }

    @Test
    void getUserFromToken_ErrorPath_MissingToken() {
        Map<String, String> tokenRequest = new HashMap<>();

        ResponseEntity<?> response = authController.getUserFromToken(tokenRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El campo 'token' es requerido", response.getBody());
        verify(authService, never()).getUserFromToken(anyString());
    }

    @Test
    void changePassword_HappyPath() {
        String authHeader = "Bearer " + validToken;
        Map<String, String> passwordRequest = new HashMap<>();
        passwordRequest.put("currentPassword", "oldPassword123");
        passwordRequest.put("newPassword", "newPassword123");

        when(authService.validateAndGetUserFromToken(validToken)).thenReturn(testUser);
        doNothing().when(authService).changePassword("oldPassword123", "newPassword123");

        ResponseEntity<?> response = authController.changePassword(authHeader, passwordRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(authService, times(1)).validateAndGetUserFromToken(validToken);
        verify(authService, times(1)).changePassword("oldPassword123", "newPassword123");
    }

    @Test
    void changePassword_ErrorPath_AppException() {
        String authHeader = "Bearer " + validToken;
        Map<String, String> passwordRequest = new HashMap<>();
        passwordRequest.put("currentPassword", "wrongPassword");
        passwordRequest.put("newPassword", "newPassword123");

        when(authService.validateAndGetUserFromToken(validToken)).thenReturn(testUser);
        doThrow(new AppException("Current password is incorrect")).when(authService).changePassword("wrongPassword", "newPassword123");

        ResponseEntity<?> response = authController.changePassword(authHeader, passwordRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Current password is incorrect", response.getBody());
        verify(authService, times(1)).changePassword("wrongPassword", "newPassword123");
    }

    @Test
    void changePassword_ErrorPath_InvalidToken() {
        String authHeader = "Bearer invalid.token";
        Map<String, String> passwordRequest = new HashMap<>();
        passwordRequest.put("currentPassword", "oldPassword123");
        passwordRequest.put("newPassword", "newPassword123");

        when(authService.validateAndGetUserFromToken("invalid.token")).thenThrow(new AppException("Invalid token"));

        ResponseEntity<?> response = authController.changePassword(authHeader, passwordRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid token", response.getBody());
        verify(authService, never()).changePassword(anyString(), anyString());
    }

    @Test
    void changePassword_ErrorPath_MissingCurrentPassword() {
        String authHeader = "Bearer " + validToken;
        Map<String, String> passwordRequest = new HashMap<>();
        passwordRequest.put("newPassword", "newPassword123");

        ResponseEntity<?> response = authController.changePassword(authHeader, passwordRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El campo 'currentPassword' es requerido", response.getBody());
        verify(authService, never()).changePassword(anyString(), anyString());
    }

    @Test
    void changePassword_ErrorPath_MissingNewPassword() {
        String authHeader = "Bearer " + validToken;
        Map<String, String> passwordRequest = new HashMap<>();
        passwordRequest.put("currentPassword", "oldPassword123");

        ResponseEntity<?> response = authController.changePassword(authHeader, passwordRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El campo 'newPassword' es requerido", response.getBody());
        verify(authService, never()).changePassword(anyString(), anyString());
    }

    @Test
    void changePassword_ErrorPath_ShortNewPassword() {
        String authHeader = "Bearer " + validToken;
        Map<String, String> passwordRequest = new HashMap<>();
        passwordRequest.put("currentPassword", "oldPassword123");
        passwordRequest.put("newPassword", "123");

        ResponseEntity<?> response = authController.changePassword(authHeader, passwordRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("La nueva contraseña debe tener al menos 6 caracteres", response.getBody());
        verify(authService, never()).validateAndGetUserFromToken(anyString());
        verify(authService, never()).changePassword(anyString(), anyString());
    }

    @Test
    void healthCheck_HappyPath() {
        ResponseEntity<String> response = authController.healthCheck();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("AuthController is working properly", response.getBody());
    }
}
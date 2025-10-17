package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.User;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.UserRole;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    void login_WhenValidCredentials_ReturnsSuccess() {
        User user = new User("1", "John Doe", "john@test.com", "pass123", UserRole.STUDENT);
        user.setActive(true);
        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.of(user));

        Map<String, Object> result = authService.login("john@test.com", "pass123");

        assertTrue((Boolean) result.get("success"));
        assertEquals("Login exitoso", result.get("message"));
        assertNotNull(result.get("token"));
    }

    @Test
    void login_WhenUserNotFound_ReturnsFailure() {
        when(userRepository.findByEmail("nonexistent@test.com")).thenReturn(Optional.empty());

        Map<String, Object> result = authService.login("nonexistent@test.com", "pass123");

        assertFalse((Boolean) result.get("success"));
        assertEquals("Usuario no encontrado", result.get("message"));
    }

    @Test
    void login_WhenUserInactive_ReturnsFailure() {
        User user = new User("1", "John Doe", "john@test.com", "pass123", UserRole.STUDENT);
        user.setActive(false);
        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.of(user));

        Map<String, Object> result = authService.login("john@test.com", "pass123");

        assertFalse((Boolean) result.get("success"));
        assertEquals("Usuario inactivo", result.get("message"));
    }

    @Test
    void login_WhenWrongPassword_ReturnsFailure() {
        User user = new User("1", "John Doe", "john@test.com", "pass123", UserRole.STUDENT);
        user.setActive(true);
        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.of(user));

        Map<String, Object> result = authService.login("john@test.com", "wrongpass");

        assertFalse((Boolean) result.get("success"));
        assertEquals("Credenciales inválidas", result.get("message"));
    }

    @Test
    void register_WhenNewUser_ReturnsSavedUser() {
        User newUser = new User("1", "John Doe", "john@test.com", "pass123", UserRole.STUDENT);
        when(userRepository.existsByEmail("john@test.com")).thenReturn(false);
        when(userRepository.save(newUser)).thenReturn(newUser);

        User result = authService.register(newUser);

        assertNotNull(result);
        assertEquals(newUser, result);
        verify(userRepository).save(newUser);
    }

    @Test
    void register_WhenEmailExists_ThrowsAppException() {
        User existingUser = new User("1", "John Doe", "john@test.com", "pass123", UserRole.STUDENT);
        when(userRepository.existsByEmail("john@test.com")).thenReturn(true);

        assertThrows(AppException.class, () -> authService.register(existingUser));
    }

    @Test
    void validateToken_WhenValidToken_ReturnsTrue() {
        User user = new User("1", "John Doe", "john@test.com", "pass123", UserRole.STUDENT);
        user.setActive(true);
        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.of(user));

        Map<String, Object> loginResult = authService.login("john@test.com", "pass123");
        // Verificar que el login fue exitoso
        assertTrue((Boolean) loginResult.get("success"));
        String token = (String) loginResult.get("token");
        // Verificar que el token no es nulo
        assertNotNull(token);

        boolean result = authService.validateToken(token);

        assertTrue(result);
    }

    @Test
    void validateToken_WhenInvalidToken_ReturnsFalse() {
        boolean result = authService.validateToken("invalid_token");

        assertFalse(result);
    }

    @Test
    void getCurrentUser_WhenUserLoggedIn_ReturnsUser() {
        User user = new User("1", "John Doe", "john@test.com", "pass123", UserRole.STUDENT);
        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.of(user));
        authService.login("john@test.com", "pass123");

        User result = authService.getCurrentUser();

        assertEquals(user, result);
    }

    @Test
    void getCurrentUser_WhenNoUserLoggedIn_ThrowsAppException() {
        assertThrows(AppException.class, () -> authService.getCurrentUser());
    }

    @Test
    void logout_WhenValidToken_RemovesTokenAndCurrentUser() {
        User user = new User("1", "John Doe", "john@test.com", "pass123", UserRole.STUDENT);
        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.of(user));
        Map<String, Object> loginResult = authService.login("john@test.com", "pass123");
        String token = (String) loginResult.get("token");

        authService.logout(token);

        assertFalse(authService.validateToken(token));
        assertThrows(AppException.class, () -> authService.getCurrentUser());
    }

    @Test
    void logout_WhenInvalidToken_DoesNothing() {
        authService.logout("invalid_token");
        assertFalse(authService.validateToken("invalid_token"));
    }

    @Test
    void getUserFromToken_WhenValidToken_ReturnsUser() {
        User user = new User("1", "John Doe", "john@test.com", "pass123", UserRole.STUDENT);
        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.of(user));
        Map<String, Object> loginResult = authService.login("john@test.com", "pass123");
        String token = (String) loginResult.get("token");

        User result = authService.getUserFromToken(token);

        assertEquals(user, result);
    }

    @Test
    void getUserFromToken_WhenInvalidToken_ReturnsNull() {
        User result = authService.getUserFromToken("invalid_token");

        assertNull(result);
    }

    @Test
    void validateAndGetUserFromToken_WhenValidToken_ReturnsUser() {
        User user = new User("1", "John Doe", "john@test.com", "pass123", UserRole.STUDENT);
        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.of(user));
        Map<String, Object> loginResult = authService.login("john@test.com", "pass123");
        String token = (String) loginResult.get("token");

        User result = authService.validateAndGetUserFromToken(token);

        assertEquals(user, result);
    }

    @Test
    void validateAndGetUserFromToken_WhenInvalidToken_ThrowsAppException() {
        assertThrows(AppException.class, () -> authService.validateAndGetUserFromToken("invalid_token"));
    }

    @Test
    void changePassword_WhenValidCurrentPassword_UpdatesPassword() {
        User user = new User("1", "John Doe", "john@test.com", "pass123", UserRole.STUDENT);
        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        authService.login("john@test.com", "pass123");

        authService.changePassword("pass123", "newpass");

        verify(userRepository).save(any(User.class));
    }

    @Test
    void changePassword_WhenInvalidCurrentPassword_ThrowsAppException() {
        User user = new User("1", "John Doe", "john@test.com", "pass123", UserRole.STUDENT);
        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.of(user));
        authService.login("john@test.com", "pass123");

        assertThrows(AppException.class, () -> authService.changePassword("wrongpass", "newpass"));
    }

    @Test
    void changePassword_WhenNoCurrentUser_ThrowsAppException() {
        assertThrows(AppException.class, () -> authService.changePassword("pass123", "newpass"));
    }
}
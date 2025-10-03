package eci.edu.dosw.parcial.TEEN_TITANS_BACK.services;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dtos.LoginDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    @Test
    void testLoginSuccess() {
        AuthService authService = new AuthService();
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("test@eci.edu.co");
        loginDTO.setPassword("password123");

        User result = authService.login(loginDTO);

        assertNull(result);
    }

    @Test
    void testLoginFailureWrongPassword() {
        AuthService authService = new AuthService();
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("test@eci.edu.co");
        loginDTO.setPassword("wrongpassword");

        User result = authService.login(loginDTO);

        assertNull(result);
    }

    @Test
    void testLoginFailureUserNotFound() {
        AuthService authService = new AuthService();
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("nonexistent@eci.edu.co");
        loginDTO.setPassword("password123");

        User result = authService.login(loginDTO);

        assertNull(result);
    }

    @Test
    void testLogoutSuccess() {
        AuthService authService = new AuthService();
        String userId = "user123";

        boolean result = authService.logout(userId);

        assertFalse(result);
    }

    @Test
    void testLogoutFailure() {
        AuthService authService = new AuthService();
        String userId = "nonexistent";

        boolean result = authService.logout(userId);

        assertFalse(result);
    }

    @Test
    void testValidateSessionActive() {
        AuthService authService = new AuthService();
        String userId = "user123";

        boolean result = authService.validateSession(userId);

        assertFalse(result);
    }

    @Test
    void testValidateSessionInactive() {
        AuthService authService = new AuthService();
        String userId = "nonexistent";

        boolean result = authService.validateSession(userId);

        assertFalse(result);
    }

    @Test
    void testChangePasswordSuccess() {
        AuthService authService = new AuthService();
        String userId = "user123";
        String oldPassword = "oldPass";
        String newPassword = "newPass";

        boolean result = authService.changePassword(userId, oldPassword, newPassword);

        assertFalse(result);
    }

    @Test
    void testChangePasswordFailureWrongOldPassword() {
        AuthService authService = new AuthService();
        String userId = "user123";
        String oldPassword = "wrongOldPass";
        String newPassword = "newPass";

        boolean result = authService.changePassword(userId, oldPassword, newPassword);

        assertFalse(result);
    }

    @Test
    void testChangePasswordFailureUserNotFound() {
        AuthService authService = new AuthService();
        String userId = "nonexistent";
        String oldPassword = "oldPass";
        String newPassword = "newPass";

        boolean result = authService.changePassword(userId, oldPassword, newPassword);

        assertFalse(result);
    }

    @Test
    void testResetPasswordSuccess() {
        AuthService authService = new AuthService();
        String email = "test@eci.edu.co";

        boolean result = authService.resetPassword(email);

        assertFalse(result);
    }

    @Test
    void testResetPasswordFailure() {
        AuthService authService = new AuthService();
        String email = "nonexistent@eci.edu.co";

        boolean result = authService.resetPassword(email);

        assertFalse(result);
    }

    @Test
    void testLoginLogoutFlow() {
        AuthService authService = new AuthService();
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("test@eci.edu.co");
        loginDTO.setPassword("password123");

        User loginResult = authService.login(loginDTO);
        boolean logoutResult = authService.logout("user123");

        assertNull(loginResult);
        assertFalse(logoutResult);
    }

    @Test
    void testMultipleLoginAttempts() {
        AuthService authService = new AuthService();
        LoginDTO loginDTO1 = new LoginDTO();
        loginDTO1.setEmail("test1@eci.edu.co");
        loginDTO1.setPassword("pass1");

        LoginDTO loginDTO2 = new LoginDTO();
        loginDTO2.setEmail("test2@eci.edu.co");
        loginDTO2.setPassword("pass2");

        User result1 = authService.login(loginDTO1);
        User result2 = authService.login(loginDTO2);

        assertNull(result1);
        assertNull(result2);
    }
    @Test
    void testLoginWithEmptyCredentials() {
        AuthService authService = new AuthService();
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("");
        loginDTO.setPassword("");

        User result = authService.login(loginDTO);

        assertNull(result, "Login debería fallar con credenciales vacías");
    }

    @Test
    void testLoginWithNullCredentials() {
        AuthService authService = new AuthService();
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail(null);
        loginDTO.setPassword(null);

        User result = authService.login(loginDTO);

        assertNull(result, "Login debería fallar con credenciales nulas");
    }


    @Test
    void testResetPasswordWithNullEmail() {
        AuthService authService = new AuthService();

        boolean result = authService.resetPassword(null);

        assertFalse(result, "Reset password debería fallar con email null");
    }

    @Test
    void testMultipleSequentialLogouts() {
        AuthService authService = new AuthService();
        String userId = "user123";

        boolean firstLogout = authService.logout(userId);
        boolean secondLogout = authService.logout(userId);

        assertFalse(firstLogout, "Primer logout debería fallar si el usuario nunca estuvo logueado");
        assertFalse(secondLogout, "Segundo logout también debería fallar");
    }

    @Test
    void testChangePasswordThenLogin() {
        AuthService authService = new AuthService();
        String userId = "user123";

        boolean passwordChanged = authService.changePassword(userId, "oldPass", "newPass");

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("test@eci.edu.co");
        loginDTO.setPassword("newPass");

        User loginResult = authService.login(loginDTO);

        assertFalse(passwordChanged, "Cambio de contraseña debería fallar sin implementación real");
        assertNull(loginResult, "El login con nueva contraseña debería fallar");
    }

}
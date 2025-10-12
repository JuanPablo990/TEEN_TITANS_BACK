package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.User;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.AuthService;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controlador REST para la autenticación y autorización de usuarios en el sistema universitario.
 * <p>
 * Proporciona endpoints para login, registro, validación de tokens, gestión de sesiones
 * y cambio de contraseñas.
 * </p>
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Autentica a un usuario en el sistema utilizando email y contraseña.
     *
     * @param loginRequest Mapa con email y password
     * @return ResponseEntity con los resultados de la autenticación
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        try {
            String email = loginRequest.get("email");
            String password = loginRequest.get("password");

            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El campo 'email' es requerido");
            }

            if (password == null || password.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El campo 'password' es requerido");
            }

            Map<String, Object> authResult = authService.login(email, password);

            boolean success = (Boolean) authResult.get("success");
            if (success) {
                return ResponseEntity.ok(authResult);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(authResult);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor durante el login");
        }
    }

    /**
     * Registra un nuevo usuario en el sistema universitario.
     *
     * @param user Usuario a registrar
     * @return ResponseEntity con el usuario registrado
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El campo 'email' es requerido");
            }

            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El campo 'password' es requerido");
            }

            if (user.getName() == null || user.getName().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El campo 'name' es requerido");
            }

            User registeredUser = authService.register(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor durante el registro");
        }
    }

    /**
     * Valida la autenticidad y vigencia de un token de acceso.
     *
     * @param tokenRequest Mapa con el token a validar
     * @return ResponseEntity con el resultado de la validación
     */
    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestBody Map<String, String> tokenRequest) {
        try {
            String token = tokenRequest.get("token");

            if (token == null || token.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El campo 'token' es requerido");
            }

            boolean isValid = authService.validateToken(token);
            Map<String, Object> response = Map.of(
                    "valid", isValid,
                    "message", isValid ? "Token válido" : "Token inválido"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al validar el token");
        }
    }

    /**
     * Obtiene el usuario actualmente autenticado en el sistema.
     *
     * @param tokenHeader Token de autenticación en el header
     * @return ResponseEntity con el usuario autenticado
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String tokenHeader) {
        try {
            String token = extractTokenFromHeader(tokenHeader);

            if (token == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Token de autorización requerido");
            }

            User currentUser = authService.validateAndGetUserFromToken(token);
            return ResponseEntity.ok(currentUser);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al obtener el usuario actual");
        }
    }

    /**
     * Cierra la sesión del usuario actual y elimina el token de acceso.
     *
     * @param tokenHeader Token de autenticación en el header
     * @return ResponseEntity con el resultado de la operación
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String tokenHeader) {
        try {
            String token = extractTokenFromHeader(tokenHeader);

            if (token == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Token de autorización requerido");
            }

            authService.logout(token);

            Map<String, Object> response = Map.of(
                    "success", true,
                    "message", "Logout exitoso"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor durante el logout");
        }
    }

    /**
     * Obtiene el usuario asociado a un token de acceso específico.
     *
     * @param tokenRequest Mapa con el token
     * @return ResponseEntity con el usuario asociado al token
     */
    @PostMapping("/user-from-token")
    public ResponseEntity<?> getUserFromToken(@RequestBody Map<String, String> tokenRequest) {
        try {
            String token = tokenRequest.get("token");

            if (token == null || token.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El campo 'token' es requerido");
            }

            User user = authService.getUserFromToken(token);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró usuario para el token proporcionado");
            }

            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al obtener el usuario del token");
        }
    }

    /**
     * Cambia la contraseña del usuario actual.
     *
     * @param tokenHeader Token de autenticación en el header
     * @param passwordRequest Mapa con la contraseña actual y la nueva contraseña
     * @return ResponseEntity con el resultado de la operación
     */
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestHeader("Authorization") String tokenHeader,
                                            @RequestBody Map<String, String> passwordRequest) {
        try {
            String token = extractTokenFromHeader(tokenHeader);

            if (token == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Token de autorización requerido");
            }

            String currentPassword = passwordRequest.get("currentPassword");
            String newPassword = passwordRequest.get("newPassword");

            if (currentPassword == null || currentPassword.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El campo 'currentPassword' es requerido");
            }

            if (newPassword == null || newPassword.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El campo 'newPassword' es requerido");
            }

            if (newPassword.length() < 6) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("La nueva contraseña debe tener al menos 6 caracteres");
            }

            // Validar que el token es válido antes de cambiar la contraseña
            authService.validateAndGetUserFromToken(token);

            authService.changePassword(currentPassword, newPassword);

            Map<String, Object> response = Map.of(
                    "success", true,
                    "message", "Contraseña cambiada exitosamente"
            );

            return ResponseEntity.ok(response);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al cambiar la contraseña");
        }
    }

    /**
     * Endpoint de salud para verificar que el controlador está funcionando.
     *
     * @return Mensaje de confirmación
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("AuthController is working properly");
    }

    /**
     * Extrae el token del header de autorización.
     * Soporta formatos: "Bearer {token}" o simplemente "{token}"
     *
     * @param authHeader Header de autorización
     * @return Token extraído o null si no es válido
     */
    private String extractTokenFromHeader(String authHeader) {
        if (authHeader == null || authHeader.trim().isEmpty()) {
            return null;
        }

        if (authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return authHeader;
    }
}
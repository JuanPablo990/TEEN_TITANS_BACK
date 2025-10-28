package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto.LoginDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto.LoginResponseDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para manejar las operaciones de autenticación
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Permitir CORS para el frontend
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * Endpoint para autenticar usuarios
     *
     * @param loginDTO Credenciales de login (email, password y rol)
     * @return Respuesta de autenticación
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        try {
            LoginResponseDTO response = loginService.authenticate(loginDTO);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            LoginResponseDTO errorResponse = new LoginResponseDTO(
                    false,
                    e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    /**
     * Endpoint para verificar si un email existe para un rol específico
     *
     * @param email Email a verificar
     * @param role Rol a verificar (puede ser string en cualquier formato)
     * @return true si el email existe para el rol especificado
     */
    @GetMapping("/check-email/{email}/role/{role}")
    public ResponseEntity<Boolean> checkEmailExistsForRole(
            @PathVariable String email,
            @PathVariable String role) {
        try {
            boolean exists = loginService.emailExistsForRole(email, role);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    /**
     * Endpoint para verificar si un email existe en cualquier rol
     *
     * @param email Email a verificar
     * @return true si el email existe
     */
    @GetMapping("/check-email/{email}")
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
        try {
            boolean exists = loginService.emailExists(email);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    /**
     * Endpoint de debug para verificar información del usuario
     *
     * @param email Email del usuario a debuggear
     * @param role Rol a verificar (opcional)
     * @return Información de debug del usuario
     */
    @GetMapping("/debug/{email}")
    public ResponseEntity<String> debugUser(
            @PathVariable String email,
            @RequestParam(required = false) String role) {
        try {
            String debugInfo = loginService.debugUserInfo(email, role != null ? role : "STUDENT");
            return ResponseEntity.ok(debugInfo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en debug: " + e.getMessage());
        }
    }

    /**
     * Endpoint de health check para el servicio de autenticación
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Servicio de autenticación funcionando correctamente");
    }
}
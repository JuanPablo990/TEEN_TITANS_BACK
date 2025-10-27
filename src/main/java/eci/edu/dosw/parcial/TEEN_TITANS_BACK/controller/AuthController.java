package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto.LoginRequest;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador encargado de gestionar las operaciones relacionadas con la autenticación de usuarios.
 *
 * <p>Proporciona endpoints para el inicio de sesión y comunicación con el servicio de autenticación.</p>
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025-10
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Endpoint para iniciar sesión en el sistema.
     *
     * <p>Recibe las credenciales del usuario (correo electrónico y contraseña) y delega la validación
     * al servicio {@link AuthService}. Si las credenciales son válidas, devuelve los datos del usuario
     * junto con un mensaje de éxito.</p>
     *
     * @param loginRequest Objeto que contiene las credenciales del usuario.
     * @return Una respuesta HTTP con el resultado de la autenticación.
     * <ul>
     *     <li><b>200 OK</b> si el inicio de sesión fue exitoso.</li>
     *     <li><b>400 Bad Request</b> si las credenciales son incorrectas o ocurre un error.</li>
     * </ul>
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Object user = authService.login(loginRequest);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Login exitoso");
            response.put("user", user);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("user", null);

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}

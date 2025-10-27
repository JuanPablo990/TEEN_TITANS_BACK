package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import lombok.Data;

/**
 * Data Transfer Object (DTO) utilizado para recibir las credenciales de inicio de sesión.
 *
 * <p>Este objeto se emplea en el endpoint de autenticación para encapsular los datos
 * que el usuario envía al sistema al intentar iniciar sesión.</p>
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025-10
 */
@Data
public class LoginRequest {

    /**
     * Correo electrónico del usuario utilizado para autenticarse.
     */
    private String email;

    /**
     * Contraseña del usuario asociada a la cuenta.
     */
    private String password;

    /**
     * Rol del usuario dentro del sistema.
     * <p>Por ejemplo: ADMIN, USER, o cualquier otro definido en {@link UserRole}.</p>
     */
    private UserRole role;
}

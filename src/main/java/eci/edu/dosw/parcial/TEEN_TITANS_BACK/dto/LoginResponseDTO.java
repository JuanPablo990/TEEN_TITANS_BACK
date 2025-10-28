package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la respuesta del login
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private String id;
    private String name;
    private String email;
    private UserRole role;
    private boolean success;
    private String message;
    private String token; // Para futura implementación de JWT

    public LoginResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public LoginResponseDTO(String id, String name, String email, UserRole role, boolean success, String message) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.success = success;
        this.message = message;
    }
}
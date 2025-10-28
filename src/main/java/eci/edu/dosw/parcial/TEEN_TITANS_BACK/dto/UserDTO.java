package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import lombok.Data;
import java.util.Date;

/**
 * Data Transfer Object (DTO) para la entidad {@code User}.
 *
 * Esta clase se utiliza para transferir la información básica de los usuarios
 * del sistema entre las capas de la aplicación.
 * Incluye atributos comunes como credenciales, rol, estado y fechas de registro.
 *
 * La contraseña solo se incluye durante operaciones de creación o actualización.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Data
public class UserDTO {
    private String id;
    private String name;
    private String email;
    private String password;
    private String role;
    private Boolean active;
    private Date createdAt;
    private Date updatedAt;
}
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

    /** Identificador único del usuario. */
    private String id;

    /** Nombre completo del usuario. */
    private String name;

    /** Correo electrónico institucional del usuario. */
    private String email;

    /** Contraseña del usuario (solo para creación o actualización). */
    private String password; // Solo para creación/actualización

    /** Rol asignado al usuario dentro del sistema (por ejemplo, STUDENT, PROFESSOR, ADMIN). */
    private String role;

    /** Indica si el usuario se encuentra activo en el sistema. */
    private Boolean active;

    /** Fecha de creación del registro del usuario. */
    private Date createdAt;

    /** Fecha de la última actualización del registro del usuario. */
    private Date updatedAt;
}
package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import lombok.Data;
import java.util.Date;

/**
 * Data Transfer Object (DTO) para la entidad {@code Dean}.
 *
 * Esta clase se utiliza para transferir los datos de los decanos
 * entre las diferentes capas del sistema sin exponer información sensible,
 * como las contraseñas.
 *
 * Contiene los atributos principales del decano, incluyendo su facultad,
 * ubicación de oficina y estado de actividad.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Data
public class DeanDTO {

    /** Identificador único del decano. */
    private String id;

    /** Nombre completo del decano. */
    private String name;

    /** Correo electrónico institucional del decano. */
    private String email;

    /** Facultad a la que pertenece el decano. */
    private String faculty;

    /** Ubicación de la oficina del decano dentro de la institución. */
    private String officeLocation;

    /** Estado de actividad del decano (activo o inactivo). */
    private Boolean active;

    /** Fecha en la que se creó el registro del decano. */
    private Date createdAt;

    /** Fecha de la última actualización del registro del decano. */
    private Date updatedAt;

    // ⚠️ Este DTO no incluye el campo "password" por motivos de seguridad.
}
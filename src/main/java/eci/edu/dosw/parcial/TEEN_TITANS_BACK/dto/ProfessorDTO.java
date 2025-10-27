package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import lombok.Data;
import java.util.List;
import java.util.Date;

/**
 * Data Transfer Object (DTO) para la entidad {@code Professor}.
 *
 * Esta clase se utiliza para transferir la información de los profesores
 * entre las capas de la aplicación sin incluir datos sensibles como la contraseña.
 *
 * Incluye información académica y laboral del profesor, como su departamento,
 * estado de titularidad y áreas de especialización.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Data
public class ProfessorDTO {

    /** Identificador único del profesor. */
    private String id;

    /** Nombre completo del profesor. */
    private String name;

    /** Correo electrónico institucional del profesor. */
    private String email;

    /** Departamento al que pertenece el profesor. */
    private String department;

    /** Indica si el profesor es titular (true) o no titular (false). */
    private Boolean isTenured;

    /** Lista de áreas de especialización o experiencia del profesor. */
    private List<String> areasOfExpertise;

    /** Estado de actividad del profesor (activo o inactivo). */
    private Boolean active;

    /** Fecha en la que se creó el registro del profesor. */
    private Date createdAt;

    /** Fecha de la última actualización del registro del profesor. */
    private Date updatedAt;

    // ⚠️ Este DTO no incluye el campo "password" por razones de seguridad.
}
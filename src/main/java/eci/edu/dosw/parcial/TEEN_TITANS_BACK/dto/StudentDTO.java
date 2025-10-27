package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import lombok.Data;
import java.util.Date;

/**
 * Data Transfer Object (DTO) para la entidad {@code Student}.
 *
 * Esta clase se utiliza para transferir información de los estudiantes
 * entre las capas de la aplicación. Incluye datos académicos y de estado
 * del estudiante, y puede incluir la contraseña únicamente en operaciones
 * de creación de usuario.
 *
 * Contiene atributos como el programa académico, semestre actual y promedio
 * de calificaciones del estudiante.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Data
public class StudentDTO {

    /** Identificador único del estudiante. */
    private String id;

    /** Nombre completo del estudiante. */
    private String name;

    /** Correo electrónico institucional del estudiante. */
    private String email;

    /** Contraseña del estudiante (solo utilizada durante la creación). */
    private String password; // Solo para creación

    /** Rol asignado al usuario dentro del sistema (por ejemplo, STUDENT). */
    private String role;

    /** Estado de actividad del estudiante (activo o inactivo). */
    private Boolean active;

    /** Fecha en la que se creó el registro del estudiante. */
    private Date createdAt;

    /** Fecha de la última actualización del registro del estudiante. */
    private Date updatedAt;

    /** Programa académico al que pertenece el estudiante. */
    private String academicProgram;

    /** Semestre actual que cursa el estudiante. */
    private Integer semester;

    /** Promedio general de calificaciones del estudiante. */
    private Double gradeAverage;
}
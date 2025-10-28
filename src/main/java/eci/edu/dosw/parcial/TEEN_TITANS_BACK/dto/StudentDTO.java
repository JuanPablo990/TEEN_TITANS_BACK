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
    private String id;
    private String name;
    private String email;
    private String password;
    private Object role;
    private Boolean active;
    private Date createdAt;
    private Date updatedAt;
    private String academicProgram;
    private Integer semester;
    private Double gradeAverage;
}
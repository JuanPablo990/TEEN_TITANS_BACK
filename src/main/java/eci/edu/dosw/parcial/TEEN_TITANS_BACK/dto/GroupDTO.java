package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO para representar un grupo de estudiantes para un curso específico.
 * Se utiliza para transferir datos entre capas sin exponer la entidad completa.
 *
 * @author Equipo Teen Titans
 * @version 2.0
 * @since 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDTO {

    public static final String ID_PATTERN = "^[A-Za-z0-9_-]+$";
    public static final int MIN_ID_LENGTH = 2;
    public static final int MAX_ID_LENGTH = 20;

    @NotBlank(message = "El ID del grupo es obligatorio")
    @Size(min = MIN_ID_LENGTH, max = MAX_ID_LENGTH,
            message = "El ID del grupo debe tener entre {min} y {max} caracteres")
    @Pattern(regexp = ID_PATTERN,
            message = "El ID del grupo solo puede contener letras, números, guiones y guiones bajos")
    private String groupId;

    @NotBlank(message = "La sección es obligatoria")
    @Size(min = 1, max = 10, message = "La sección debe tener entre 1 y 10 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9]+$",
            message = "La sección solo puede contener letras y números")
    private String section;

    @NotBlank(message = "El ID del curso es obligatorio")
    @Size(min = 5, max = 20, message = "El ID del curso debe tener entre 5 y 20 caracteres")
    @Pattern(regexp = ID_PATTERN,
            message = "El ID del curso solo puede contener letras, números y guiones")
    private String courseId;

    @NotBlank(message = "El ID del profesor es obligatorio")
    @Size(min = 5, max = 50, message = "El ID del profesor debe tener entre 5 y 50 caracteres")
    private String professorId;

    @NotBlank(message = "El ID del horario es obligatorio")
    @Size(min = 5, max = 20, message = "El ID del horario debe tener entre 5 y 20 caracteres")
    @Pattern(regexp = ID_PATTERN,
            message = "El ID del horario solo puede contener letras, números y guiones")
    private String scheduleId;

    @NotBlank(message = "El ID del aula es obligatorio")
    @Size(min = 5, max = 20, message = "El ID del aula debe tener entre 5 y 20 caracteres")
    @Pattern(regexp = ID_PATTERN,
            message = "El ID del aula solo puede contener letras, números y guiones")
    private String classroomId;
}
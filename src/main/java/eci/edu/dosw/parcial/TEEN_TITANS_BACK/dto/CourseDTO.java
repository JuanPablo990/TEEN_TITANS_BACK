package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO para representar un curso en el sistema académico.
 * Se utiliza para transferir datos entre capas sin exponer la entidad completa.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {
    private String courseCode;
    private String name;
    private Integer credits;
    private String description;
    private String academicProgram;
    private Boolean isActive;
}
package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Representa un curso en el sistema académico.
 * Esta clase encapsula toda la información relevante sobre un curso específico,
 * incluyendo su código, créditos y programa académico al que pertenece.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Document(collection = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    private String courseCode;
    private String name;
    private Integer credits;
    private String description;
    private String academicProgram;
    private Boolean isActive;

    /**
     * Constructor sin el courseCode, para cuando se quiera generar automáticamente.
     *
     * @param name Nombre descriptivo del curso
     * @param credits Número de créditos asignados
     * @param description Descripción detallada del curso
     * @param academicProgram Programa académico al que pertenece
     * @param isActive Indica si el curso está activo
     */
    public Course(String name, Integer credits, String description, String academicProgram, Boolean isActive) {
        this.name = name;
        this.credits = credits;
        this.description = description;
        this.academicProgram = academicProgram;
        this.isActive = isActive;
    }
}
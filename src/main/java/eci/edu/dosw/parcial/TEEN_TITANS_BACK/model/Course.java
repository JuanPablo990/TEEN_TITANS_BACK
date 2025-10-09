package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
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
public class Course {
    @Id
    private String courseCode;
    private String name;
    private Integer credits;
    private String description;
    private String academicProgram;
    private Boolean isActive;

    /**
     * Constructor vacío. Requerido para frameworks que necesitan instanciar la clase sin parámetros.
     */
    public Course() {
    }

    /**
     * Constructor con todos los parámetros.
     *
     * @param courseCode Código único del curso
     * @param name Nombre descriptivo del curso
     * @param credits Número de créditos asignados
     * @param description Descripción detallada del curso
     * @param academicProgram Programa académico al que pertenece
     * @param isActive Indica si el curso está activo
     */
    public Course(String courseCode, String name, Integer credits, String description, String academicProgram, Boolean isActive) {
        this.courseCode = courseCode;
        this.name = name;
        this.credits = credits;
        this.description = description;
        this.academicProgram = academicProgram;
        this.isActive = isActive;
    }

    /**
     * Obtiene el código único que identifica el curso.
     *
     * @return el código del curso como String
     */
    public String getCourseCode() {
        return courseCode;
    }

    /**
     * Obtiene el nombre descriptivo del curso.
     *
     * @return el nombre del curso como String
     */
    public String getName() {
        return name;
    }

    /**
     * Obtiene el número de créditos asignados al curso.
     *
     * @return el número de créditos como Integer
     */
    public Integer getCredits() {
        return credits;
    }

    /**
     * Obtiene la descripción detallada del curso.
     *
     * @return la descripción del curso como String
     */
    public String getDescription() {
        return description;
    }

    /**
     * Obtiene el programa académico al que pertenece el curso.
     *
     * @return el programa académico como String
     */
    public String getAcademicProgram() {
        return academicProgram;
    }

    /**
     * Verifica si este curso está actualmente activo en el sistema.
     *
     * @return true si el curso está activo, false en caso contrario
     */
    public Boolean isActive() {
        return isActive;
    }
}
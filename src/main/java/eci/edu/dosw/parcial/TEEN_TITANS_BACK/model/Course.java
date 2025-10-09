package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

/**
 * Representa un curso en el sistema académico.
 * Esta clase encapsula toda la información relevante sobre un curso específico,
 * incluyendo su código, créditos y programa académico al que pertenece.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
public class Course {
    private String courseCode;
    private String name;
    private Integer credits;
    private String description;
    private String academicProgram;
    private Boolean isActive;

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
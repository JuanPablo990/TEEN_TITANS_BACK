package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

/**
 * Representa un estudiante dentro del sistema universitario.
 * Esta clase extiende de User y añade información académica específica
 * como programa académico, semestre y promedio de calificaciones.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
public class Student extends User {
    private String academicProgram;
    private Integer semester;
    private Double gradeAverage;

    /**
     * Constructor para crear un nuevo estudiante.
     *
     * @param id el identificador único del estudiante
     * @param name el nombre completo del estudiante
     * @param email el correo electrónico institucional
     * @param password la contraseña de acceso
     * @param academicProgram el programa académico del estudiante
     * @param semester el semestre actual del estudiante
     */
    public Student(String id, String name, String email, String password,
                   String academicProgram, Integer semester) {
        super(id, name, email, password, UserRole.STUDENT);
        this.academicProgram = academicProgram;
        this.semester = semester;
        this.gradeAverage = 0.0;
    }

    /**
     * Obtiene el programa académico en el que está matriculado el estudiante.
     *
     * @return el programa académico como String
     */
    public String getAcademicProgram() {
        return academicProgram;
    }

    /**
     * Obtiene el semestre actual del estudiante.
     *
     * @return el semestre como Integer
     */
    public Integer getSemester() {
        return semester;
    }

    /**
     * Obtiene el promedio de calificaciones del estudiante.
     *
     * @return el promedio como Double
     */
    public Double getGradeAverage() {
        return gradeAverage;
    }

    /**
     * Actualiza el programa académico del estudiante.
     *
     * @param academicProgram el nuevo programa académico
     */
    public void setAcademicProgram(String academicProgram) {
        this.academicProgram = academicProgram;
        updateTimestamp();
    }

    /**
     * Actualiza el semestre actual del estudiante.
     *
     * @param semester el nuevo semestre
     */
    public void setSemester(Integer semester) {
        this.semester = semester;
        updateTimestamp();
    }

    /**
     * Actualiza el promedio de calificaciones del estudiante.
     *
     * @param gradeAverage el nuevo promedio de calificaciones
     */
    public void setGradeAverage(Double gradeAverage) {
        this.gradeAverage = gradeAverage;
        updateTimestamp();
    }
}
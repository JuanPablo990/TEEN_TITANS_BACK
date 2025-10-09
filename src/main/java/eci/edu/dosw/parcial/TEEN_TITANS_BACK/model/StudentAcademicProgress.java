package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Representa el progreso académico de un estudiante en el sistema.
 * Esta clase encapsula toda la información relacionada con el avance académico del estudiante,
 * incluyendo créditos completados, semestre actual y promedio acumulado.
 *
 * Cada instancia de esta clase se almacena como un documento en la colección "student_progress" de MongoDB.
 * El campo {@code id} actúa como identificador único del documento.
 *
 * @author Equipo Teen Titans
 * @version 1.1
 * @since 2025
 */
@Document(collection = "student_progress")
public class StudentAcademicProgress {

    @Id
    private String id;

    @DBRef
    private Student student;

    private String academicProgram;
    private String faculty;
    private String curriculumType;
    private Integer currentSemester;
    private Integer totalSemesters;
    private Integer completedCredits;
    private Integer totalCreditsRequired;
    private Double cumulativeGPA;
    private List<CourseStatusDetail> coursesStatus;

    /**
     * Constructor vacío. Requerido por frameworks que necesitan instanciar la clase sin parámetros.
     */
    public StudentAcademicProgress() {
    }

    /**
     * Constructor con todos los parámetros.
     *
     * @param id Identificador único del registro de progreso académico
     * @param student Estudiante asociado a este progreso académico
     * @param academicProgram Programa académico en el que está inscrito el estudiante
     * @param faculty Facultad a la que pertenece el estudiante
     * @param curriculumType Tipo de currículo del estudiante
     * @param currentSemester Semestre actual del estudiante
     * @param totalSemesters Total de semestres del programa académico
     * @param completedCredits Créditos completados por el estudiante
     * @param totalCreditsRequired Total de créditos requeridos para graduación
     * @param cumulativeGPA Promedio académico acumulado (GPA)
     * @param coursesStatus Lista del estado de todos los cursos del estudiante
     */
    public StudentAcademicProgress(String id, Student student, String academicProgram, String faculty, String curriculumType,
                                   Integer currentSemester, Integer totalSemesters, Integer completedCredits,
                                   Integer totalCreditsRequired, Double cumulativeGPA, List<CourseStatusDetail> coursesStatus) {
        this.id = id;
        this.student = student;
        this.academicProgram = academicProgram;
        this.faculty = faculty;
        this.curriculumType = curriculumType;
        this.currentSemester = currentSemester;
        this.totalSemesters = totalSemesters;
        this.completedCredits = completedCredits;
        this.totalCreditsRequired = totalCreditsRequired;
        this.cumulativeGPA = cumulativeGPA;
        this.coursesStatus = coursesStatus;
    }

    /**
     * Obtiene el identificador único del registro de progreso académico.
     *
     * @return el identificador del documento como String
     */
    public String getId() {
        return id;
    }

    /**
     * Obtiene el estudiante asociado a este progreso académico.
     *
     * @return el objeto {@link Student} asociado
     */
    public Student getStudent() {
        return student;
    }

    /**
     * Obtiene el programa académico en el que está inscrito el estudiante.
     *
     * @return el programa académico como String
     */
    public String getAcademicProgram() {
        return academicProgram;
    }

    /**
     * Obtiene la facultad a la que pertenece el estudiante.
     *
     * @return la facultad como String
     */
    public String getFaculty() {
        return faculty;
    }

    /**
     * Obtiene el tipo de currículo del estudiante.
     *
     * @return el tipo de currículo como String
     */
    public String getCurriculumType() {
        return curriculumType;
    }

    /**
     * Obtiene el semestre actual en el que se encuentra el estudiante.
     *
     * @return el semestre actual como Integer
     */
    public Integer getCurrentSemester() {
        return currentSemester;
    }

    /**
     * Obtiene el total de semestres que dura el programa académico.
     *
     * @return el total de semestres como Integer
     */
    public Integer getTotalSemesters() {
        return totalSemesters;
    }

    /**
     * Obtiene el número de créditos completados por el estudiante.
     *
     * @return los créditos completados como Integer
     */
    public Integer getCompletedCredits() {
        return completedCredits;
    }

    /**
     * Obtiene el total de créditos requeridos para la graduación.
     *
     * @return el total de créditos requeridos como Integer
     */
    public Integer getTotalCreditsRequired() {
        return totalCreditsRequired;
    }

    /**
     * Obtiene el promedio académico acumulado (GPA) del estudiante.
     *
     * @return el GPA acumulado como Double
     */
    public Double getCumulativeGPA() {
        return cumulativeGPA;
    }

    /**
     * Obtiene la lista del estado de todos los cursos del estudiante.
     *
     * @return la lista de {@link CourseStatusDetail}
     */
    public List<CourseStatusDetail> getCoursesStatus() {
        return coursesStatus;
    }
}

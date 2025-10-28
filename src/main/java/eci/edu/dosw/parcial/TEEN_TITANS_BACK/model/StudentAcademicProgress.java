package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

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
@Data
@NoArgsConstructor
@AllArgsConstructor
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
     * Constructor personalizado sin el campo ID (para cuando MongoDB genera automáticamente el ID).
     *
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
    public StudentAcademicProgress(Student student, String academicProgram, String faculty, String curriculumType,
                                   Integer currentSemester, Integer totalSemesters, Integer completedCredits,
                                   Integer totalCreditsRequired, Double cumulativeGPA, List<CourseStatusDetail> coursesStatus) {
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
}
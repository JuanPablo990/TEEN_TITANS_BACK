package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import java.util.Date;

/**
 * Representa el estado detallado de un curso para un estudiante.
 * Esta clase encapsula toda la información relacionada con el progreso y resultado
 * de un curso específico en el historial académico del estudiante.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
public class CourseStatusDetail {
    private Course course;
    private CourseStatus status;
    private Double grade;
    private String semester;
    private Date enrollmentDate;
    private Date completionDate;
    private Group group;
    private Professor professor;
    private Integer creditsEarned;
    private Boolean isApproved;
    private String comments;

    /**
     * Obtiene el curso asociado a este detalle de estado.
     *
     * @return el objeto Course asociado
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Obtiene el estado actual del curso en el progreso del estudiante.
     *
     * @return el estado del curso como CourseStatus
     */
    public CourseStatus getStatus() {
        return status;
    }

    /**
     * Obtiene la calificación obtenida por el estudiante en el curso.
     *
     * @return la calificación como Double
     */
    public Double getGrade() {
        return grade;
    }

    /**
     * Obtiene el semestre en el que se cursó la materia.
     *
     * @return el semestre como String
     */
    public String getSemester() {
        return semester;
    }

    /**
     * Obtiene la fecha en la que el estudiante se matriculó en el curso.
     *
     * @return la fecha de matrícula como Date
     */
    public Date getEnrollmentDate() {
        return enrollmentDate;
    }

    /**
     * Obtiene la fecha en la que el estudiante completó el curso.
     *
     * @return la fecha de finalización como Date
     */
    public Date getCompletionDate() {
        return completionDate;
    }

    /**
     * Obtiene el grupo en el que el estudiante cursó la materia.
     *
     * @return el objeto Group asociado
     */
    public Group getGroup() {
        return group;
    }

    /**
     * Obtiene el profesor que impartió el curso al estudiante.
     *
     * @return el objeto Professor asociado
     */
    public Professor getProfessor() {
        return professor;
    }

    /**
     * Obtiene el número de créditos obtenidos por el estudiante en este curso.
     *
     * @return los créditos obtenidos como Integer
     */
    public Integer getCreditsEarned() {
        return creditsEarned;
    }

    /**
     * Verifica si el estudiante aprobó el curso.
     *
     * @return true si el curso fue aprobado, false en caso contrario
     */
    public Boolean isApproved() {
        return isApproved;
    }

    /**
     * Obtiene comentarios adicionales sobre el desempeño en el curso.
     *
     * @return los comentarios como String
     */
    public String getComments() {
        return comments;
    }
}

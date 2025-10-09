package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Representa el estado detallado de un curso para un estudiante.
 * Esta clase encapsula toda la información relacionada con el progreso y resultado
 * de un curso específico dentro del historial académico del estudiante.
 *
 * Cada instancia de esta clase se almacena como un documento en la colección
 * "course_status_details" en MongoDB.
 *
 * @author Equipo Teen Titans
 * @version 1.1
 * @since 2025
 */
@Document(collection = "course_status_details")
public class CourseStatusDetail {

    @Id
    private String id; // Identificador único del documento en MongoDB

    @DBRef
    private Course course; // Referencia al curso asociado

    private CourseStatus status;
    private Double grade;
    private String semester;
    private Date enrollmentDate;
    private Date completionDate;

    @DBRef
    private Group group; // Referencia al grupo donde se cursó

    @DBRef
    private Professor professor; // Referencia al profesor que impartió el curso

    private Integer creditsEarned;
    private Boolean isApproved;
    private String comments;

    /**
     * Constructor vacío. Requerido para frameworks que necesitan instanciar la clase sin parámetros.
     */
    public CourseStatusDetail() {
    }

    /**
     * Constructor con todos los parámetros.
     *
     * @param id Identificador único del registro de detalle de curso
     * @param course Curso asociado a este detalle
     * @param status Estado actual del curso (en progreso, aprobado, reprobado, etc.)
     * @param grade Calificación obtenida en el curso
     * @param semester Semestre en el que se cursó la materia
     * @param enrollmentDate Fecha de matrícula en el curso
     * @param completionDate Fecha de finalización del curso
     * @param group Grupo en el que se cursó la materia
     * @param professor Profesor que impartió el curso
     * @param creditsEarned Créditos obtenidos en el curso
     * @param isApproved Indica si el curso fue aprobado
     * @param comments Comentarios adicionales sobre el desempeño
     */
    public CourseStatusDetail(String id, Course course, CourseStatus status, Double grade, String semester,
                              Date enrollmentDate, Date completionDate, Group group, Professor professor,
                              Integer creditsEarned, Boolean isApproved, String comments) {
        this.id = id;
        this.course = course;
        this.status = status;
        this.grade = grade;
        this.semester = semester;
        this.enrollmentDate = enrollmentDate;
        this.completionDate = completionDate;
        this.group = group;
        this.professor = professor;
        this.creditsEarned = creditsEarned;
        this.isApproved = isApproved;
        this.comments = comments;
    }

    /**
     * Obtiene el identificador único del detalle de curso.
     *
     * @return el ID del documento como String
     */
    public String getId() {
        return id;
    }

    /**
     * Obtiene el curso asociado a este detalle de estado.
     *
     * @return el objeto {@link Course} asociado
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Obtiene el estado actual del curso en el progreso del estudiante.
     *
     * @return el estado del curso como {@link CourseStatus}
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
     * @return la fecha de matrícula como {@link Date}
     */
    public Date getEnrollmentDate() {
        return enrollmentDate;
    }

    /**
     * Obtiene la fecha en la que el estudiante completó el curso.
     *
     * @return la fecha de finalización como {@link Date}
     */
    public Date getCompletionDate() {
        return completionDate;
    }

    /**
     * Obtiene el grupo en el que el estudiante cursó la materia.
     *
     * @return el objeto {@link Group} asociado
     */
    public Group getGroup() {
        return group;
    }

    /**
     * Obtiene el profesor que impartió el curso al estudiante.
     *
     * @return el objeto {@link Professor} asociado
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

package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import java.util.Date;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.CourseStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

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
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseStatusDetail {

    @Id
    private String id; // Identificador único del documento en MongoDB

    @DBRef
    private Course course; // Referencia al curso asociado

    // SOLUCIÓN DEFINITIVA: Usar solo studentId como String, no ambas propiedades
    private String studentId; // ID del estudiante

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
     * Constructor personalizado sin el campo ID (para cuando MongoDB genera automáticamente el ID).
     *
     * @param course Curso asociado a este detalle
     * @param studentId ID del estudiante
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
    public CourseStatusDetail(Course course, String studentId, CourseStatus status, Double grade, String semester,
                              Date enrollmentDate, Date completionDate, Group group, Professor professor,
                              Integer creditsEarned, Boolean isApproved, String comments) {
        this.course = course;
        this.studentId = studentId;
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
}
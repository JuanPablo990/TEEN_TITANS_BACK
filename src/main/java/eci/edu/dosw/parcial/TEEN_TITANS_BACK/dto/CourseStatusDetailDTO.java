package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.CourseStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;

/**
 * DTO para el estado detallado de un curso.
 * Se utiliza para transferir datos entre capas sin exponer la entidad completa.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseStatusDetailDTO {
    private String id;
    private String courseId;
    private String courseCode;
    private String courseName;
    private String studentId;
    private CourseStatus status;
    private Double grade;
    private String semester;
    private Date enrollmentDate;
    private Date completionDate;
    private String groupId;
    private String groupCode;
    private String professorId;
    private String professorName;
    private Integer creditsEarned;
    private Boolean isApproved;
    private String comments;
}
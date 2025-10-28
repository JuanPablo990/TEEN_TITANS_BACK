package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO para representar un grupo de estudiantes para un curso específico.
 * Se utiliza para transferir datos entre capas sin exponer la entidad completa.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDTO {
    private String groupId;
    private String section;
    private String courseId;
    private String professorId;
    private String scheduleId;
    private String classroomId;
}
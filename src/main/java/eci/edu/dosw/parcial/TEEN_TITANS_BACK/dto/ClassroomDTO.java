package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.RoomType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO para representar un aula o sala de clases.
 * Se utiliza para transferir datos entre capas sin exponer la entidad completa.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomDTO {
    private String classroomId;
    private String building;
    private String roomNumber;
    private Integer capacity;
    private RoomType roomType;
    private String displayName; // Campo calculado para mostrar
    private Boolean isAvailable; // Campo adicional para estado
}
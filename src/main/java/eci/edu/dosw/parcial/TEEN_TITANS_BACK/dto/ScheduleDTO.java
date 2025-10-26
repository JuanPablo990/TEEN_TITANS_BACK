package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO para representar un horario académico.
 * Se utiliza para transferir datos entre capas sin exponer la entidad completa.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDTO {
    private String scheduleId;
    private String dayOfWeek;
    private String startHour;
    private String endHour;
    private String period;
}
package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Representa un horario para un curso o actividad académica.
 * Esta clase encapsula la información de los días y horarios en los que se imparte una clase.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Document(collection = "schedules")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {
    @Id
    private String scheduleId;
    private String dayOfWeek;
    private String startHour;
    private String endHour;
    private String period;

    /**
     * Constructor sin el scheduleId, para cuando MongoDB genera automáticamente el ID.
     *
     * @param dayOfWeek Día de la semana en que se imparte la clase
     * @param startHour Hora de inicio de la clase
     * @param endHour Hora de finalización de la clase
     * @param period Período académico asociado a este horario
     */
    public Schedule(String dayOfWeek, String startHour, String endHour, String period) {
        this.dayOfWeek = dayOfWeek;
        this.startHour = startHour;
        this.endHour = endHour;
        this.period = period;
    }
}
package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

/**
 * Representa un horario para un curso o actividad académica.
 * Esta clase encapsula la información de los días y horarios en los que se imparte una clase.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
public class Schedule {
    private String scheduleId;
    private String dayOfWeek;
    private String startHour;
    private String endHour;
    private String period;

    /**
     * Obtiene el identificador único del horario.
     *
     * @return el ID del horario como String
     */
    public String getScheduleId() {
        return scheduleId;
    }

    /**
     * Obtiene el día de la semana en que se imparte la clase.
     *
     * @return el día de la semana como String
     */
    public String getDayOfWeek() {
        return dayOfWeek;
    }

    /**
     * Obtiene la hora de inicio de la clase.
     *
     * @return la hora de inicio como String
     */
    public String getStartHour() {
        return startHour;
    }

    /**
     * Obtiene la hora de finalización de la clase.
     *
     * @return la hora de fin como String
     */
    public String getEndHour() {
        return endHour;
    }

    /**
     * Obtiene el período académico asociado a este horario.
     *
     * @return el período académico como String
     */
    public String getPeriod() {
        return period;
    }
}
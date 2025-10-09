package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para manejar las operaciones CRUD y consultas personalizadas
 * sobre la colección de horarios (Schedule) en MongoDB.
 *
 * Este repositorio permite realizar búsquedas basadas en el día de la semana,
 * la hora de inicio, la hora de fin y el período académico, además de las
 * operaciones CRUD básicas proporcionadas por Spring Data.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Repository
public interface ScheduleRepository extends MongoRepository<Schedule, String> {

    /**
     * Busca todos los horarios correspondientes a un día específico de la semana.
     *
     * @param dayOfWeek Día de la semana (por ejemplo, "Lunes", "Martes").
     * @return Lista de horarios programados para el día indicado.
     */
    List<Schedule> findByDayOfWeek(String dayOfWeek);

    /**
     * Busca todos los horarios que comienzan a una hora específica.
     *
     * @param startHour Hora de inicio del horario (por ejemplo, "08:00").
     * @return Lista de horarios que inician a la hora indicada.
     */
    List<Schedule> findByStartHour(String startHour);

    /**
     * Busca todos los horarios que finalizan a una hora específica.
     *
     * @param endHour Hora de finalización del horario (por ejemplo, "10:00").
     * @return Lista de horarios que terminan a la hora indicada.
     */
    List<Schedule> findByEndHour(String endHour);

    /**
     * Busca todos los horarios asociados a un período académico determinado.
     *
     * @param period Identificador o nombre del período académico.
     * @return Lista de horarios correspondientes al período indicado.
     */
    List<Schedule> findByPeriod(String period);
}

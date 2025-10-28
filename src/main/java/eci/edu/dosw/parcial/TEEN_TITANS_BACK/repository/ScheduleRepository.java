package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para manejar las operaciones CRUD y consultas personalizadas
 * sobre la colección de horarios (Schedule) en MongoDB.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Repository
public interface ScheduleRepository extends MongoRepository<Schedule, String> {

    List<Schedule> findByDayOfWeek(String dayOfWeek);

    List<Schedule> findByStartHour(String startHour);

    List<Schedule> findByEndHour(String endHour);

    List<Schedule> findByPeriod(String period);

    List<Schedule> findByDayOfWeekAndPeriod(String dayOfWeek, String period);

    List<Schedule> findByDayOfWeekAndStartHour(String dayOfWeek, String startHour);

    List<Schedule> findByDayOfWeekAndEndHour(String dayOfWeek, String endHour);

    List<Schedule> findByStartHourAndEndHour(String startHour, String endHour);

    List<Schedule> findByPeriodAndStartHour(String period, String startHour);

    List<Schedule> findByPeriodAndEndHour(String period, String endHour);

    List<Schedule> findByDayOfWeekAndStartHourAndEndHour(String dayOfWeek, String startHour, String endHour);

    List<Schedule> findByStartHourGreaterThanEqual(String startHour);

    List<Schedule> findByStartHourLessThanEqual(String startHour);

    List<Schedule> findByEndHourGreaterThanEqual(String endHour);

    List<Schedule> findByEndHourLessThanEqual(String endHour);

    List<Schedule> findByOrderByStartHourAsc();

    List<Schedule> findByOrderByEndHourAsc();

    List<Schedule> findByDayOfWeekOrderByStartHourAsc(String dayOfWeek);

    List<Schedule> findByPeriodOrderByStartHourAsc(String period);

    List<Schedule> findByDayOfWeekAndPeriodOrderByStartHourAsc(String dayOfWeek, String period);

    long countByDayOfWeek(String dayOfWeek);

    long countByPeriod(String period);

    long countByDayOfWeekAndPeriod(String dayOfWeek, String period);

    long countByStartHour(String startHour);

    long countByEndHour(String endHour);

    @Query("{ 'dayOfWeek': { $regex: ?0, $options: 'i' } }")
    List<Schedule> findByDayOfWeekRegex(String dayOfWeekPattern);

    @Query("{ 'period': { $regex: ?0, $options: 'i' } }")
    List<Schedule> findByPeriodRegex(String periodPattern);

    @Query("{ 'startHour': { $gte: ?0, $lte: ?1 } }")
    List<Schedule> findByStartHourBetween(String startHourFrom, String startHourTo);

    @Query("{ 'endHour': { $gte: ?0, $lte: ?1 } }")
    List<Schedule> findByEndHourBetween(String endHourFrom, String endHourTo);

    @Query("{ 'dayOfWeek': ?0, 'startHour': { $gte: ?1 }, 'endHour': { $lte: ?2 } }")
    List<Schedule> findByDayAndTimeRange(String dayOfWeek, String startHourFrom, String endHourTo);

    @Query("{ 'period': ?0, 'dayOfWeek': ?1, 'startHour': { $gte: ?2 } }")
    List<Schedule> findByPeriodAndDayAndStartTime(String period, String dayOfWeek, String startHourFrom);

    @Query(value = "{ 'dayOfWeek': ?0 }", sort = "{ 'startHour': 1 }")
    List<Schedule> findByDayOfWeekSortedByStartHour(String dayOfWeek);

    @Query(value = "{ 'period': ?0 }", sort = "{ 'dayOfWeek': 1, 'startHour': 1 }")
    List<Schedule> findByPeriodSortedByDayAndStartHour(String period);

    boolean existsByDayOfWeekAndStartHourAndEndHour(String dayOfWeek, String startHour, String endHour);

    boolean existsByPeriodAndDayOfWeekAndStartHour(String period, String dayOfWeek, String startHour);

    boolean existsByPeriod(String period);

    List<Schedule> findByDayOfWeekIn(List<String> daysOfWeek);

    List<Schedule> findByPeriodIn(List<String> periods);

    List<Schedule> findByStartHourIn(List<String> startHours);

    @Query("{ 'dayOfWeek': ?0, $or: [ { $and: [ { 'startHour': { $lte: ?1 } }, { 'endHour': { $gt: ?1 } } ] }, { $and: [ { 'startHour': { $lt: ?2 } }, { 'endHour': { $gte: ?2 } } ] }, { $and: [ { 'startHour': { $gte: ?1 } }, { 'endHour': { $lte: ?2 } } ] } ] }")
    List<Schedule> findConflictingSchedules(String dayOfWeek, String startHour, String endHour);

    @Query("{ 'period': ?0, 'dayOfWeek': ?1, 'startHour': { $gte: ?2 }, 'endHour': { $lte: ?3 } }")
    List<Schedule> findAvailableSchedulesInTimeSlot(String period, String dayOfWeek, String startHour, String endHour);

    Optional<Schedule> findByDayOfWeekAndStartHourAndEndHourAndPeriod(String dayOfWeek, String startHour, String endHour, String period);
}
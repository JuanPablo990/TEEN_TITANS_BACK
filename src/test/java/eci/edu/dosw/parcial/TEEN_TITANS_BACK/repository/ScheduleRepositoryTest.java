package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Schedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;
import java.util.Collections;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataMongoTest
public class ScheduleRepositoryTest {

    @MockBean
    private ScheduleRepository scheduleRepository;

    private Schedule schedule1;
    private Schedule schedule2;
    private Schedule schedule3;
    private Schedule schedule4;
    private Schedule schedule5;

    @BeforeEach
    void setUp() {
        schedule1 = new Schedule("SCHED001", "Monday", "08:00", "10:00", "Morning");
        schedule2 = new Schedule("SCHED002", "Monday", "10:00", "12:00", "Morning");
        schedule3 = new Schedule("SCHED003", "Wednesday", "14:00", "16:00", "Afternoon");
        schedule4 = new Schedule("SCHED004", "Friday", "16:00", "18:00", "Evening");
        schedule5 = new Schedule("SCHED005", "Monday", "07:00", "09:00", "Morning");
    }

    @Test
    @DisplayName("Caso exitoso - findByDayOfWeek retorna horarios del día")
    void testFindByDayOfWeek_Exitoso() {
        when(scheduleRepository.findByDayOfWeek("Monday"))
                .thenReturn(List.of(schedule1, schedule2, schedule5));

        List<Schedule> resultado = scheduleRepository.findByDayOfWeek("Monday");

        assertAll("Verificar horarios del lunes",
                () -> assertNotNull(resultado),
                () -> assertEquals(3, resultado.size()),
                () -> assertEquals("Monday", resultado.get(0).getDayOfWeek()),
                () -> assertEquals("Monday", resultado.get(1).getDayOfWeek()),
                () -> assertEquals("Monday", resultado.get(2).getDayOfWeek())
        );

        verify(scheduleRepository, times(1)).findByDayOfWeek("Monday");
    }

    @Test
    @DisplayName("Caso error - findByDayOfWeek retorna lista vacía para día inexistente")
    void testFindByDayOfWeek_DiaInexistente() {
        when(scheduleRepository.findByDayOfWeek("Sunday"))
                .thenReturn(Collections.emptyList());

        List<Schedule> resultado = scheduleRepository.findByDayOfWeek("Sunday");

        assertTrue(resultado.isEmpty());
        verify(scheduleRepository, times(1)).findByDayOfWeek("Sunday");
    }

    @Test
    @DisplayName("Caso exitoso - findByStartHour retorna horarios por hora de inicio")
    void testFindByStartHour_Exitoso() {
        when(scheduleRepository.findByStartHour("08:00"))
                .thenReturn(List.of(schedule1));

        List<Schedule> resultado = scheduleRepository.findByStartHour("08:00");

        assertAll("Verificar horarios que inician a las 08:00",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("08:00", resultado.get(0).getStartHour())
        );

        verify(scheduleRepository, times(1)).findByStartHour("08:00");
    }

    @Test
    @DisplayName("Caso exitoso - findByEndHour retorna horarios por hora de fin")
    void testFindByEndHour_Exitoso() {
        when(scheduleRepository.findByEndHour("10:00"))
                .thenReturn(List.of(schedule1));

        List<Schedule> resultado = scheduleRepository.findByEndHour("10:00");

        assertAll("Verificar horarios que terminan a las 10:00",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("10:00", resultado.get(0).getEndHour())
        );

        verify(scheduleRepository, times(1)).findByEndHour("10:00");
    }

    @Test
    @DisplayName("Caso exitoso - findByPeriod retorna horarios por período")
    void testFindByPeriod_Exitoso() {
        when(scheduleRepository.findByPeriod("Morning"))
                .thenReturn(List.of(schedule1, schedule2, schedule5));

        List<Schedule> resultado = scheduleRepository.findByPeriod("Morning");

        assertAll("Verificar horarios del período Morning",
                () -> assertNotNull(resultado),
                () -> assertEquals(3, resultado.size()),
                () -> assertEquals("Morning", resultado.get(0).getPeriod()),
                () -> assertEquals("Morning", resultado.get(1).getPeriod()),
                () -> assertEquals("Morning", resultado.get(2).getPeriod())
        );

        verify(scheduleRepository, times(1)).findByPeriod("Morning");
    }

    @Test
    @DisplayName("Caso exitoso - findByDayOfWeekAndPeriod retorna horarios filtrados")
    void testFindByDayOfWeekAndPeriod_Exitoso() {
        when(scheduleRepository.findByDayOfWeekAndPeriod("Monday", "Morning"))
                .thenReturn(List.of(schedule1, schedule2, schedule5));

        List<Schedule> resultado = scheduleRepository.findByDayOfWeekAndPeriod("Monday", "Morning");

        assertAll("Verificar horarios del lunes en la mañana",
                () -> assertNotNull(resultado),
                () -> assertEquals(3, resultado.size()),
                () -> assertEquals("Monday", resultado.get(0).getDayOfWeek()),
                () -> assertEquals("Morning", resultado.get(0).getPeriod())
        );

        verify(scheduleRepository, times(1)).findByDayOfWeekAndPeriod("Monday", "Morning");
    }

    @Test
    @DisplayName("Caso exitoso - findByDayOfWeekAndStartHour retorna horario específico")
    void testFindByDayOfWeekAndStartHour_Exitoso() {
        when(scheduleRepository.findByDayOfWeekAndStartHour("Monday", "08:00"))
                .thenReturn(List.of(schedule1));

        List<Schedule> resultado = scheduleRepository.findByDayOfWeekAndStartHour("Monday", "08:00");

        assertAll("Verificar horario específico por día y hora de inicio",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("Monday", resultado.get(0).getDayOfWeek()),
                () -> assertEquals("08:00", resultado.get(0).getStartHour())
        );

        verify(scheduleRepository, times(1)).findByDayOfWeekAndStartHour("Monday", "08:00");
    }

    @Test
    @DisplayName("Caso exitoso - findByStartHourAndEndHour retorna horario por rango")
    void testFindByStartHourAndEndHour_Exitoso() {
        when(scheduleRepository.findByStartHourAndEndHour("08:00", "10:00"))
                .thenReturn(List.of(schedule1));

        List<Schedule> resultado = scheduleRepository.findByStartHourAndEndHour("08:00", "10:00");

        assertAll("Verificar horario por hora de inicio y fin",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("08:00", resultado.get(0).getStartHour()),
                () -> assertEquals("10:00", resultado.get(0).getEndHour())
        );

        verify(scheduleRepository, times(1)).findByStartHourAndEndHour("08:00", "10:00");
    }

    @Test
    @DisplayName("Caso exitoso - findByStartHourGreaterThanEqual retorna horarios que inician después")
    void testFindByStartHourGreaterThanEqual_Exitoso() {
        when(scheduleRepository.findByStartHourGreaterThanEqual("10:00"))
                .thenReturn(List.of(schedule2, schedule3, schedule4));

        List<Schedule> resultado = scheduleRepository.findByStartHourGreaterThanEqual("10:00");

        assertAll("Verificar horarios que inician a las 10:00 o después",
                () -> assertNotNull(resultado),
                () -> assertEquals(3, resultado.size()),
                () -> assertTrue(resultado.get(0).getStartHour().compareTo("10:00") >= 0),
                () -> assertTrue(resultado.get(1).getStartHour().compareTo("10:00") >= 0),
                () -> assertTrue(resultado.get(2).getStartHour().compareTo("10:00") >= 0)
        );

        verify(scheduleRepository, times(1)).findByStartHourGreaterThanEqual("10:00");
    }

    @Test
    @DisplayName("Caso exitoso - findByStartHourLessThanEqual retorna horarios que inician antes")
    void testFindByStartHourLessThanEqual_Exitoso() {
        when(scheduleRepository.findByStartHourLessThanEqual("08:00"))
                .thenReturn(List.of(schedule1, schedule5));

        List<Schedule> resultado = scheduleRepository.findByStartHourLessThanEqual("08:00");

        assertAll("Verificar horarios que inician a las 08:00 o antes",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertTrue(resultado.get(0).getStartHour().compareTo("08:00") <= 0),
                () -> assertTrue(resultado.get(1).getStartHour().compareTo("08:00") <= 0)
        );

        verify(scheduleRepository, times(1)).findByStartHourLessThanEqual("08:00");
    }

    @Test
    @DisplayName("Caso exitoso - findByOrderByStartHourAsc retorna horarios ordenados")
    void testFindByOrderByStartHourAsc_Exitoso() {
        when(scheduleRepository.findByOrderByStartHourAsc())
                .thenReturn(List.of(schedule5, schedule1, schedule2, schedule3, schedule4));

        List<Schedule> resultado = scheduleRepository.findByOrderByStartHourAsc();

        assertAll("Verificar horarios ordenados por hora de inicio ascendente",
                () -> assertNotNull(resultado),
                () -> assertEquals(5, resultado.size()),
                () -> assertEquals("07:00", resultado.get(0).getStartHour()),
                () -> assertEquals("08:00", resultado.get(1).getStartHour()),
                () -> assertEquals("10:00", resultado.get(2).getStartHour()),
                () -> assertEquals("14:00", resultado.get(3).getStartHour()),
                () -> assertEquals("16:00", resultado.get(4).getStartHour())
        );

        verify(scheduleRepository, times(1)).findByOrderByStartHourAsc();
    }

    @Test
    @DisplayName("Caso exitoso - findByDayOfWeekOrderByStartHourAsc retorna horarios ordenados por día")
    void testFindByDayOfWeekOrderByStartHourAsc_Exitoso() {
        when(scheduleRepository.findByDayOfWeekOrderByStartHourAsc("Monday"))
                .thenReturn(List.of(schedule5, schedule1, schedule2));

        List<Schedule> resultado = scheduleRepository.findByDayOfWeekOrderByStartHourAsc("Monday");

        assertAll("Verificar horarios del lunes ordenados por hora de inicio",
                () -> assertNotNull(resultado),
                () -> assertEquals(3, resultado.size()),
                () -> assertEquals("Monday", resultado.get(0).getDayOfWeek()),
                () -> assertEquals("Monday", resultado.get(1).getDayOfWeek()),
                () -> assertEquals("Monday", resultado.get(2).getDayOfWeek()),
                () -> assertEquals("07:00", resultado.get(0).getStartHour()),
                () -> assertEquals("08:00", resultado.get(1).getStartHour()),
                () -> assertEquals("10:00", resultado.get(2).getStartHour())
        );

        verify(scheduleRepository, times(1)).findByDayOfWeekOrderByStartHourAsc("Monday");
    }

    @Test
    @DisplayName("Caso exitoso - countByDayOfWeek retorna conteo correcto")
    void testCountByDayOfWeek_Exitoso() {
        when(scheduleRepository.countByDayOfWeek("Monday"))
                .thenReturn(3L);

        long resultado = scheduleRepository.countByDayOfWeek("Monday");

        assertEquals(3L, resultado);
        verify(scheduleRepository, times(1)).countByDayOfWeek("Monday");
    }

    @Test
    @DisplayName("Caso exitoso - countByPeriod retorna conteo por período")
    void testCountByPeriod_Exitoso() {
        when(scheduleRepository.countByPeriod("Morning"))
                .thenReturn(3L);

        long resultado = scheduleRepository.countByPeriod("Morning");

        assertEquals(3L, resultado);
        verify(scheduleRepository, times(1)).countByPeriod("Morning");
    }

    @Test
    @DisplayName("Caso exitoso - countByDayOfWeekAndPeriod retorna conteo combinado")
    void testCountByDayOfWeekAndPeriod_Exitoso() {
        when(scheduleRepository.countByDayOfWeekAndPeriod("Monday", "Morning"))
                .thenReturn(3L);

        long resultado = scheduleRepository.countByDayOfWeekAndPeriod("Monday", "Morning");

        assertEquals(3L, resultado);
        verify(scheduleRepository, times(1)).countByDayOfWeekAndPeriod("Monday", "Morning");
    }

    @Test
    @DisplayName("Caso exitoso - existsByDayOfWeekAndStartHourAndEndHour retorna true cuando existe")
    void testExistsByDayOfWeekAndStartHourAndEndHour_Exitoso() {
        when(scheduleRepository.existsByDayOfWeekAndStartHourAndEndHour("Monday", "08:00", "10:00"))
                .thenReturn(true);

        boolean resultado = scheduleRepository.existsByDayOfWeekAndStartHourAndEndHour("Monday", "08:00", "10:00");

        assertTrue(resultado);
        verify(scheduleRepository, times(1)).existsByDayOfWeekAndStartHourAndEndHour("Monday", "08:00", "10:00");
    }

    @Test
    @DisplayName("Caso error - existsByDayOfWeekAndStartHourAndEndHour retorna false cuando no existe")
    void testExistsByDayOfWeekAndStartHourAndEndHour_NoExiste() {
        when(scheduleRepository.existsByDayOfWeekAndStartHourAndEndHour("Sunday", "00:00", "02:00"))
                .thenReturn(false);

        boolean resultado = scheduleRepository.existsByDayOfWeekAndStartHourAndEndHour("Sunday", "00:00", "02:00");

        assertFalse(resultado);
        verify(scheduleRepository, times(1)).existsByDayOfWeekAndStartHourAndEndHour("Sunday", "00:00", "02:00");
    }

    @Test
    @DisplayName("Caso exitoso - existsByPeriod retorna true")
    void testExistsByPeriod_Exitoso() {
        when(scheduleRepository.existsByPeriod("Morning"))
                .thenReturn(true);

        boolean resultado = scheduleRepository.existsByPeriod("Morning");

        assertTrue(resultado);
        verify(scheduleRepository, times(1)).existsByPeriod("Morning");
    }

    @Test
    @DisplayName("Caso exitoso - findByDayOfWeekIn retorna horarios de múltiples días")
    void testFindByDayOfWeekIn_Exitoso() {
        when(scheduleRepository.findByDayOfWeekIn(List.of("Monday", "Wednesday")))
                .thenReturn(List.of(schedule1, schedule2, schedule3, schedule5));

        List<Schedule> resultado = scheduleRepository.findByDayOfWeekIn(List.of("Monday", "Wednesday"));

        assertAll("Verificar horarios de múltiples días",
                () -> assertNotNull(resultado),
                () -> assertEquals(4, resultado.size()),
                () -> assertTrue(resultado.stream().anyMatch(s -> s.getDayOfWeek().equals("Monday"))),
                () -> assertTrue(resultado.stream().anyMatch(s -> s.getDayOfWeek().equals("Wednesday")))
        );

        verify(scheduleRepository, times(1)).findByDayOfWeekIn(List.of("Monday", "Wednesday"));
    }

    @Test
    @DisplayName("Caso exitoso - findByPeriodIn retorna horarios de múltiples períodos")
    void testFindByPeriodIn_Exitoso() {
        when(scheduleRepository.findByPeriodIn(List.of("Morning", "Afternoon")))
                .thenReturn(List.of(schedule1, schedule2, schedule3, schedule5));

        List<Schedule> resultado = scheduleRepository.findByPeriodIn(List.of("Morning", "Afternoon"));

        assertAll("Verificar horarios de múltiples períodos",
                () -> assertNotNull(resultado),
                () -> assertEquals(4, resultado.size()),
                () -> assertTrue(resultado.stream().anyMatch(s -> s.getPeriod().equals("Morning"))),
                () -> assertTrue(resultado.stream().anyMatch(s -> s.getPeriod().equals("Afternoon")))
        );

        verify(scheduleRepository, times(1)).findByPeriodIn(List.of("Morning", "Afternoon"));
    }

    @Test
    @DisplayName("Caso exitoso - findByDayOfWeekRegex con query personalizada")
    void testFindByDayOfWeekRegex_Exitoso() {
        when(scheduleRepository.findByDayOfWeekRegex(".*day.*"))
                .thenReturn(List.of(schedule1, schedule2, schedule3, schedule5));

        List<Schedule> resultado = scheduleRepository.findByDayOfWeekRegex(".*day.*");

        assertAll("Verificar búsqueda por regex de día",
                () -> assertNotNull(resultado),
                () -> assertEquals(4, resultado.size()),
                () -> assertTrue(resultado.get(0).getDayOfWeek().toLowerCase().contains("day")),
                () -> assertTrue(resultado.get(1).getDayOfWeek().toLowerCase().contains("day")),
                () -> assertTrue(resultado.get(2).getDayOfWeek().toLowerCase().contains("day")),
                () -> assertTrue(resultado.get(3).getDayOfWeek().toLowerCase().contains("day"))
        );

        verify(scheduleRepository, times(1)).findByDayOfWeekRegex(".*day.*");
    }

    @Test
    @DisplayName("Caso exitoso - findByStartHourBetween con query personalizada")
    void testFindByStartHourBetween_Exitoso() {
        when(scheduleRepository.findByStartHourBetween("08:00", "12:00"))
                .thenReturn(List.of(schedule1, schedule2));

        List<Schedule> resultado = scheduleRepository.findByStartHourBetween("08:00", "12:00");

        assertAll("Verificar horarios con hora de inicio en rango",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertTrue(resultado.get(0).getStartHour().compareTo("08:00") >= 0 && resultado.get(0).getStartHour().compareTo("12:00") <= 0),
                () -> assertTrue(resultado.get(1).getStartHour().compareTo("08:00") >= 0 && resultado.get(1).getStartHour().compareTo("12:00") <= 0)
        );

        verify(scheduleRepository, times(1)).findByStartHourBetween("08:00", "12:00");
    }

    @Test
    @DisplayName("Caso exitoso - findByDayAndTimeRange con query personalizada")
    void testFindByDayAndTimeRange_Exitoso() {
        when(scheduleRepository.findByDayAndTimeRange("Monday", "07:00", "12:00"))
                .thenReturn(List.of(schedule1, schedule2, schedule5));

        List<Schedule> resultado = scheduleRepository.findByDayAndTimeRange("Monday", "07:00", "12:00");

        assertAll("Verificar horarios por día y rango de tiempo",
                () -> assertNotNull(resultado),
                () -> assertEquals(3, resultado.size()),
                () -> assertEquals("Monday", resultado.get(0).getDayOfWeek()),
                () -> assertTrue(resultado.get(0).getStartHour().compareTo("07:00") >= 0),
                () -> assertTrue(resultado.get(0).getEndHour().compareTo("12:00") <= 0)
        );

        verify(scheduleRepository, times(1)).findByDayAndTimeRange("Monday", "07:00", "12:00");
    }

    @Test
    @DisplayName("Caso exitoso - findByDayOfWeekSortedByStartHour con query personalizada")
    void testFindByDayOfWeekSortedByStartHour_Exitoso() {
        when(scheduleRepository.findByDayOfWeekSortedByStartHour("Monday"))
                .thenReturn(List.of(schedule5, schedule1, schedule2));

        List<Schedule> resultado = scheduleRepository.findByDayOfWeekSortedByStartHour("Monday");

        assertAll("Verificar horarios ordenados por hora de inicio",
                () -> assertNotNull(resultado),
                () -> assertEquals(3, resultado.size()),
                () -> assertEquals("Monday", resultado.get(0).getDayOfWeek()),
                () -> assertEquals("07:00", resultado.get(0).getStartHour()),
                () -> assertEquals("08:00", resultado.get(1).getStartHour()),
                () -> assertEquals("10:00", resultado.get(2).getStartHour())
        );

        verify(scheduleRepository, times(1)).findByDayOfWeekSortedByStartHour("Monday");
    }

    @Test
    @DisplayName("Caso exitoso - findConflictingSchedules con query personalizada")
    void testFindConflictingSchedules_Exitoso() {
        when(scheduleRepository.findConflictingSchedules("Monday", "09:00", "11:00"))
                .thenReturn(List.of(schedule1, schedule2));

        List<Schedule> resultado = scheduleRepository.findConflictingSchedules("Monday", "09:00", "11:00");

        assertAll("Verificar horarios conflictivos",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals("Monday", resultado.get(0).getDayOfWeek()),
                () -> assertEquals("Monday", resultado.get(1).getDayOfWeek())
        );

        verify(scheduleRepository, times(1)).findConflictingSchedules("Monday", "09:00", "11:00");
    }

    @Test
    @DisplayName("Caso exitoso - findByDayOfWeekAndStartHourAndEndHourAndPeriod retorna horario específico")
    void testFindByDayOfWeekAndStartHourAndEndHourAndPeriod_Exitoso() {
        when(scheduleRepository.findByDayOfWeekAndStartHourAndEndHourAndPeriod("Monday", "08:00", "10:00", "Morning"))
                .thenReturn(Optional.of(schedule1));

        Optional<Schedule> resultado = scheduleRepository.findByDayOfWeekAndStartHourAndEndHourAndPeriod("Monday", "08:00", "10:00", "Morning");

        assertAll("Verificar horario específico por todos los campos",
                () -> assertTrue(resultado.isPresent()),
                () -> assertEquals("Monday", resultado.get().getDayOfWeek()),
                () -> assertEquals("08:00", resultado.get().getStartHour()),
                () -> assertEquals("10:00", resultado.get().getEndHour()),
                () -> assertEquals("Morning", resultado.get().getPeriod())
        );

        verify(scheduleRepository, times(1)).findByDayOfWeekAndStartHourAndEndHourAndPeriod("Monday", "08:00", "10:00", "Morning");
    }

    @Test
    @DisplayName("Caso borde - findByDayOfWeek con día nulo")
    void testFindByDayOfWeek_Nulo() {
        when(scheduleRepository.findByDayOfWeek(null))
                .thenReturn(Collections.emptyList());

        List<Schedule> resultado = scheduleRepository.findByDayOfWeek(null);

        assertTrue(resultado.isEmpty());
        verify(scheduleRepository, times(1)).findByDayOfWeek(null);
    }

    @Test
    @DisplayName("Caso borde - findByPeriod con período nulo")
    void testFindByPeriod_Nulo() {
        when(scheduleRepository.findByPeriod(null))
                .thenReturn(Collections.emptyList());

        List<Schedule> resultado = scheduleRepository.findByPeriod(null);

        assertTrue(resultado.isEmpty());
        verify(scheduleRepository, times(1)).findByPeriod(null);
    }

    @Test
    @DisplayName("Caso borde - Verificar integridad de datos en horarios encontrados")
    void testIntegridadDatosHorarios() {
        when(scheduleRepository.findByDayOfWeekAndStartHour("Monday", "08:00"))
                .thenReturn(List.of(schedule1));

        List<Schedule> resultado = scheduleRepository.findByDayOfWeekAndStartHour("Monday", "08:00");

        assertAll("Verificar integridad completa del horario",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("SCHED001", resultado.get(0).getScheduleId()),
                () -> assertEquals("Monday", resultado.get(0).getDayOfWeek()),
                () -> assertEquals("08:00", resultado.get(0).getStartHour()),
                () -> assertEquals("10:00", resultado.get(0).getEndHour()),
                () -> assertEquals("Morning", resultado.get(0).getPeriod()),
                () -> assertInstanceOf(Schedule.class, resultado.get(0)),
                () -> assertNotNull(resultado.get(0).toString())
        );

        verify(scheduleRepository, times(1)).findByDayOfWeekAndStartHour("Monday", "08:00");
    }

    @Test
    @DisplayName("Caso exitoso - findByPeriodSortedByDayAndStartHour con query personalizada")
    void testFindByPeriodSortedByDayAndStartHour_Exitoso() {
        when(scheduleRepository.findByPeriodSortedByDayAndStartHour("Morning"))
                .thenReturn(List.of(schedule5, schedule1, schedule2));

        List<Schedule> resultado = scheduleRepository.findByPeriodSortedByDayAndStartHour("Morning");

        assertAll("Verificar horarios ordenados por día y hora de inicio",
                () -> assertNotNull(resultado),
                () -> assertEquals(3, resultado.size()),
                () -> assertEquals("Morning", resultado.get(0).getPeriod()),
                () -> assertEquals("Morning", resultado.get(1).getPeriod()),
                () -> assertEquals("Morning", resultado.get(2).getPeriod())
        );

        verify(scheduleRepository, times(1)).findByPeriodSortedByDayAndStartHour("Morning");
    }

    @Test
    @DisplayName("Caso exitoso - findAvailableSchedulesInTimeSlot con query personalizada")
    void testFindAvailableSchedulesInTimeSlot_Exitoso() {
        when(scheduleRepository.findAvailableSchedulesInTimeSlot("Morning", "Monday", "07:00", "12:00"))
                .thenReturn(List.of(schedule1, schedule2, schedule5));

        List<Schedule> resultado = scheduleRepository.findAvailableSchedulesInTimeSlot("Morning", "Monday", "07:00", "12:00");

        assertAll("Verificar horarios disponibles en slot de tiempo",
                () -> assertNotNull(resultado),
                () -> assertEquals(3, resultado.size()),
                () -> assertEquals("Morning", resultado.get(0).getPeriod()),
                () -> assertEquals("Monday", resultado.get(0).getDayOfWeek()),
                () -> assertTrue(resultado.get(0).getStartHour().compareTo("07:00") >= 0),
                () -> assertTrue(resultado.get(0).getEndHour().compareTo("12:00") <= 0)
        );

        verify(scheduleRepository, times(1)).findAvailableSchedulesInTimeSlot("Morning", "Monday", "07:00", "12:00");
    }
}
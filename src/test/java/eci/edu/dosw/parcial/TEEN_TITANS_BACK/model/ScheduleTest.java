package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ScheduleTest {

    private Schedule schedule;

    @BeforeEach
    void setUp() {
        schedule = new Schedule(
                "SCH001",
                "Lunes",
                "08:00",
                "10:00",
                "2025-1"
        );
    }

    @Test
    @DisplayName("Caso exitoso - Constructor con todos los parámetros inicializa correctamente")
    void testConstructorCompleto_Exitoso() {
        assertAll("Validar todos los campos del constructor completo",
                () -> assertEquals("SCH001", schedule.getScheduleId()),
                () -> assertEquals("Lunes", schedule.getDayOfWeek()),
                () -> assertEquals("08:00", schedule.getStartHour()),
                () -> assertEquals("10:00", schedule.getEndHour()),
                () -> assertEquals("2025-1", schedule.getPeriod())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Constructor sin scheduleId para generación automática")
    void testConstructorSinScheduleId_Exitoso() {
        Schedule scheduleSinId = new Schedule(
                "Martes",
                "14:00",
                "16:00",
                "2025-1"
        );

        assertAll("Validar constructor sin scheduleId",
                () -> assertNull(scheduleSinId.getScheduleId()),
                () -> assertEquals("Martes", scheduleSinId.getDayOfWeek()),
                () -> assertEquals("14:00", scheduleSinId.getStartHour()),
                () -> assertEquals("16:00", scheduleSinId.getEndHour()),
                () -> assertEquals("2025-1", scheduleSinId.getPeriod())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Constructor por defecto inicializa con valores por defecto")
    void testConstructorPorDefecto_Exitoso() {
        Schedule scheduleVacio = new Schedule();

        assertAll("Validar valores por defecto",
                () -> assertNull(scheduleVacio.getScheduleId()),
                () -> assertNull(scheduleVacio.getDayOfWeek()),
                () -> assertNull(scheduleVacio.getStartHour()),
                () -> assertNull(scheduleVacio.getEndHour()),
                () -> assertNull(scheduleVacio.getPeriod())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Setters y Getters funcionan correctamente")
    void testSettersAndGetters_Exitoso() {
        Schedule scheduleTest = new Schedule();

        scheduleTest.setScheduleId("SCH002");
        scheduleTest.setDayOfWeek("Miércoles");
        scheduleTest.setStartHour("10:00");
        scheduleTest.setEndHour("12:00");
        scheduleTest.setPeriod("2025-2");

        assertAll("Validar setters y getters",
                () -> assertEquals("SCH002", scheduleTest.getScheduleId()),
                () -> assertEquals("Miércoles", scheduleTest.getDayOfWeek()),
                () -> assertEquals("10:00", scheduleTest.getStartHour()),
                () -> assertEquals("12:00", scheduleTest.getEndHour()),
                () -> assertEquals("2025-2", scheduleTest.getPeriod())
        );
    }

    @Test
    @DisplayName("Caso borde - Campos con valores null")
    void testCamposConValoresNull_Borde() {
        Schedule scheduleNull = new Schedule(
                null,
                null,
                null,
                null,
                null
        );

        assertAll("Validar campos null",
                () -> assertNull(scheduleNull.getScheduleId()),
                () -> assertNull(scheduleNull.getDayOfWeek()),
                () -> assertNull(scheduleNull.getStartHour()),
                () -> assertNull(scheduleNull.getEndHour()),
                () -> assertNull(scheduleNull.getPeriod())
        );
    }

    @Test
    @DisplayName("Caso borde - Campos con valores vacíos")
    void testCamposConValoresVacios_Borde() {
        schedule.setScheduleId("");
        schedule.setDayOfWeek("");
        schedule.setStartHour("");
        schedule.setEndHour("");
        schedule.setPeriod("");

        assertAll("Validar campos vacíos",
                () -> assertEquals("", schedule.getScheduleId()),
                () -> assertEquals("", schedule.getDayOfWeek()),
                () -> assertEquals("", schedule.getStartHour()),
                () -> assertEquals("", schedule.getEndHour()),
                () -> assertEquals("", schedule.getPeriod())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Equals y HashCode generados por Lombok")
    void testEqualsAndHashCode_Exitoso() {
        Schedule schedule1 = new Schedule(
                "SCH001",
                "Lunes",
                "08:00",
                "10:00",
                "2025-1"
        );

        Schedule schedule2 = new Schedule(
                "SCH001",
                "Lunes",
                "08:00",
                "10:00",
                "2025-1"
        );

        Schedule schedule3 = new Schedule(
                "SCH002",
                "Martes",
                "14:00",
                "16:00",
                "2025-2"
        );

        assertEquals(schedule1, schedule2);
        assertNotEquals(schedule1, schedule3);
        assertEquals(schedule1.hashCode(), schedule2.hashCode());
        assertNotEquals(schedule1.hashCode(), schedule3.hashCode());
    }

    @Test
    @DisplayName("Caso borde - Equals con objetos null y diferente clase")
    void testEqualsCasosBorde_Borde() {
        Schedule scheduleTest = new Schedule(
                "SCH001",
                "Lunes",
                "08:00",
                "10:00",
                "2025-1"
        );

        assertNotEquals(null, scheduleTest);
        assertNotEquals("No soy un Schedule", scheduleTest);
    }

    @Test
    @DisplayName("Caso exitoso - ToString generado por Lombok")
    void testToString_Exitoso() {
        String resultadoToString = schedule.toString();

        assertAll("ToString debe contener todos los campos principales",
                () -> assertTrue(resultadoToString.contains("SCH001")),
                () -> assertTrue(resultadoToString.contains("Lunes")),
                () -> assertTrue(resultadoToString.contains("08:00")),
                () -> assertTrue(resultadoToString.contains("10:00")),
                () -> assertTrue(resultadoToString.contains("2025-1")),
                () -> assertTrue(resultadoToString.contains("Schedule"))
        );
    }

    @Test
    @DisplayName("Caso compuesto - Validación completa de múltiples escenarios")
    void testSchedule_ValidacionCompleta_Compuesto() {
        assertAll("Validación completa de todas las propiedades",
                () -> assertEquals("SCH001", schedule.getScheduleId()),
                () -> assertEquals("Lunes", schedule.getDayOfWeek()),
                () -> assertEquals("08:00", schedule.getStartHour()),
                () -> assertEquals("10:00", schedule.getEndHour()),
                () -> assertEquals("2025-1", schedule.getPeriod()),
                () -> assertNotNull(schedule.toString()),
                () -> assertTrue(schedule.toString().contains("Schedule"))
        );
    }

    @Test
    @DisplayName("Caso borde - ScheduleId con diferentes formatos")
    void testScheduleIdFormatos_Borde() {
        schedule.setScheduleId("2025-MATH-101-LUNES");
        assertEquals("2025-MATH-101-LUNES", schedule.getScheduleId());

        schedule.setScheduleId("SCHEDULE_PROGRAMACION_1");
        assertEquals("SCHEDULE_PROGRAMACION_1", schedule.getScheduleId());

        schedule.setScheduleId("123456789");
        assertEquals("123456789", schedule.getScheduleId());
    }

    @Test
    @DisplayName("Caso borde - DayOfWeek con diferentes días")
    void testDayOfWeekDiferentesDias_Borde() {
        schedule.setDayOfWeek("Lunes");
        assertEquals("Lunes", schedule.getDayOfWeek());

        schedule.setDayOfWeek("Martes");
        assertEquals("Martes", schedule.getDayOfWeek());

        schedule.setDayOfWeek("Miércoles");
        assertEquals("Miércoles", schedule.getDayOfWeek());

        schedule.setDayOfWeek("Jueves");
        assertEquals("Jueves", schedule.getDayOfWeek());

        schedule.setDayOfWeek("Viernes");
        assertEquals("Viernes", schedule.getDayOfWeek());

        schedule.setDayOfWeek("Sábado");
        assertEquals("Sábado", schedule.getDayOfWeek());

        schedule.setDayOfWeek("Domingo");
        assertEquals("Domingo", schedule.getDayOfWeek());
    }

    @Test
    @DisplayName("Caso borde - Horas con diferentes formatos")
    void testHorasFormatos_Borde() {
        schedule.setStartHour("08:00");
        assertEquals("08:00", schedule.getStartHour());

        schedule.setStartHour("14:30");
        assertEquals("14:30", schedule.getStartHour());

        schedule.setStartHour("18:15");
        assertEquals("18:15", schedule.getStartHour());

        schedule.setStartHour("8:00");
        assertEquals("8:00", schedule.getStartHour());

        schedule.setStartHour("20:00");
        assertEquals("20:00", schedule.getStartHour());

        schedule.setEndHour("09:45");
        assertEquals("09:45", schedule.getEndHour());

        schedule.setEndHour("16:20");
        assertEquals("16:20", schedule.getEndHour());

        schedule.setEndHour("21:30");
        assertEquals("21:30", schedule.getEndHour());
    }

    @Test
    @DisplayName("Caso borde - Period con diferentes formatos")
    void testPeriodFormatos_Borde() {
        schedule.setPeriod("2025-1");
        assertEquals("2025-1", schedule.getPeriod());

        schedule.setPeriod("2024-2");
        assertEquals("2024-2", schedule.getPeriod());

        schedule.setPeriod("VERANO-2025");
        assertEquals("VERANO-2025", schedule.getPeriod());

        schedule.setPeriod("PERIODO_ESPECIAL");
        assertEquals("PERIODO_ESPECIAL", schedule.getPeriod());

        schedule.setPeriod("2025-I");
        assertEquals("2025-I", schedule.getPeriod());

        schedule.setPeriod("2025-II");
        assertEquals("2025-II", schedule.getPeriod());
    }

    @Test
    @DisplayName("Caso borde - Horarios nocturnos")
    void testHorariosNocturnos_Borde() {
        Schedule horarioNocturno = new Schedule(
                "SCH_NOC",
                "Miércoles",
                "18:00",
                "20:00",
                "2025-1"
        );

        assertAll("Validar horario nocturno",
                () -> assertEquals("SCH_NOC", horarioNocturno.getScheduleId()),
                () -> assertEquals("Miércoles", horarioNocturno.getDayOfWeek()),
                () -> assertEquals("18:00", horarioNocturno.getStartHour()),
                () -> assertEquals("20:00", horarioNocturno.getEndHour()),
                () -> assertEquals("2025-1", horarioNocturno.getPeriod())
        );
    }

    @Test
    @DisplayName("Caso borde - Horarios de fin de semana")
    void testHorariosFinDeSemana_Borde() {
        Schedule horarioSabado = new Schedule(
                "SCH_SAB",
                "Sábado",
                "07:00",
                "12:00",
                "2025-1"
        );

        Schedule horarioDomingo = new Schedule(
                "SCH_DOM",
                "Domingo",
                "14:00",
                "18:00",
                "2025-1"
        );

        assertAll("Validar horarios de fin de semana",
                () -> assertEquals("Sábado", horarioSabado.getDayOfWeek()),
                () -> assertEquals("07:00", horarioSabado.getStartHour()),
                () -> assertEquals("12:00", horarioSabado.getEndHour()),
                () -> assertEquals("Domingo", horarioDomingo.getDayOfWeek()),
                () -> assertEquals("14:00", horarioDomingo.getStartHour()),
                () -> assertEquals("18:00", horarioDomingo.getEndHour())
        );
    }

    @Test
    @DisplayName("Caso borde - Horarios con minutos específicos")
    void testHorariosConMinutos_Borde() {
        Schedule horarioConMinutos = new Schedule(
                "SCH_DET",
                "Jueves",
                "08:15",
                "10:45",
                "2025-1"
        );

        assertAll("Validar horario con minutos específicos",
                () -> assertEquals("08:15", horarioConMinutos.getStartHour()),
                () -> assertEquals("10:45", horarioConMinutos.getEndHour())
        );
    }

    @Test
    @DisplayName("Caso borde - Campos con espacios en blanco")
    void testCamposConEspaciosBlanco_Borde() {
        schedule.setDayOfWeek("  Lunes  ");
        schedule.setStartHour("  08:00  ");
        schedule.setEndHour("  10:00  ");
        schedule.setPeriod("  2025-1  ");

        assertAll("Validar campos con espacios en blanco",
                () -> assertEquals("  Lunes  ", schedule.getDayOfWeek()),
                () -> assertEquals("  08:00  ", schedule.getStartHour()),
                () -> assertEquals("  10:00  ", schedule.getEndHour()),
                () -> assertEquals("  2025-1  ", schedule.getPeriod())
        );
    }

    @Test
    @DisplayName("Caso borde - Días de la semana en inglés")
    void testDiasSemanaIngles_Borde() {
        schedule.setDayOfWeek("Monday");
        assertEquals("Monday", schedule.getDayOfWeek());

        schedule.setDayOfWeek("Tuesday");
        assertEquals("Tuesday", schedule.getDayOfWeek());

        schedule.setDayOfWeek("Wednesday");
        assertEquals("Wednesday", schedule.getDayOfWeek());
    }
}
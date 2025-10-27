package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ScheduleDTOTest {

    private ScheduleDTO scheduleDTO;

    @BeforeEach
    void setUp() {
        scheduleDTO = new ScheduleDTO();
    }

    @Test
    @DisplayName("Caso exitoso - Constructor con parámetros inicializa todos los campos")
    void testConstructorConParametros_Exitoso() {
        ScheduleDTO dto = new ScheduleDTO(
                "SCH001",
                "Lunes",
                "08:00",
                "10:00",
                "2025-1"
        );

        assertAll("Validar todos los campos del constructor",
                () -> assertEquals("SCH001", dto.getScheduleId()),
                () -> assertEquals("Lunes", dto.getDayOfWeek()),
                () -> assertEquals("08:00", dto.getStartHour()),
                () -> assertEquals("10:00", dto.getEndHour()),
                () -> assertEquals("2025-1", dto.getPeriod())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Setters y Getters funcionan correctamente")
    void testSettersAndGetters_Exitoso() {
        scheduleDTO.setScheduleId("SCH002");
        scheduleDTO.setDayOfWeek("Martes");
        scheduleDTO.setStartHour("14:00");
        scheduleDTO.setEndHour("16:00");
        scheduleDTO.setPeriod("2025-2");

        assertAll("Validar setters y getters",
                () -> assertEquals("SCH002", scheduleDTO.getScheduleId()),
                () -> assertEquals("Martes", scheduleDTO.getDayOfWeek()),
                () -> assertEquals("14:00", scheduleDTO.getStartHour()),
                () -> assertEquals("16:00", scheduleDTO.getEndHour()),
                () -> assertEquals("2025-2", scheduleDTO.getPeriod())
        );
    }

    @Test
    @DisplayName("Caso borde - Constructor por defecto inicializa con null")
    void testConstructorPorDefecto_Borde() {
        ScheduleDTO dtoVacio = new ScheduleDTO();

        assertAll("Campos deben ser null",
                () -> assertNull(dtoVacio.getScheduleId()),
                () -> assertNull(dtoVacio.getDayOfWeek()),
                () -> assertNull(dtoVacio.getStartHour()),
                () -> assertNull(dtoVacio.getEndHour()),
                () -> assertNull(dtoVacio.getPeriod())
        );
    }

    @Test
    @DisplayName("Caso borde - Setear valores null funciona correctamente")
    void testSettersConNull_Borde() {
        scheduleDTO.setScheduleId("SCH003");
        scheduleDTO.setDayOfWeek("Miércoles");

        scheduleDTO.setScheduleId(null);
        scheduleDTO.setDayOfWeek(null);
        scheduleDTO.setStartHour(null);
        scheduleDTO.setEndHour(null);
        scheduleDTO.setPeriod(null);

        assertAll("Todos los campos deben ser null",
                () -> assertNull(scheduleDTO.getScheduleId()),
                () -> assertNull(scheduleDTO.getDayOfWeek()),
                () -> assertNull(scheduleDTO.getStartHour()),
                () -> assertNull(scheduleDTO.getEndHour()),
                () -> assertNull(scheduleDTO.getPeriod())
        );
    }

    @Test
    @DisplayName("Caso borde - Campos con valores vacíos")
    void testCamposConValoresVacios_Borde() {
        scheduleDTO.setScheduleId("");
        scheduleDTO.setDayOfWeek("");
        scheduleDTO.setStartHour("");
        scheduleDTO.setEndHour("");
        scheduleDTO.setPeriod("");

        assertAll("Validar campos vacíos",
                () -> assertEquals("", scheduleDTO.getScheduleId()),
                () -> assertEquals("", scheduleDTO.getDayOfWeek()),
                () -> assertEquals("", scheduleDTO.getStartHour()),
                () -> assertEquals("", scheduleDTO.getEndHour()),
                () -> assertEquals("", scheduleDTO.getPeriod())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Equals y HashCode generados por Lombok")
    void testEqualsAndHashCode_Exitoso() {
        ScheduleDTO dto1 = new ScheduleDTO("SCH001", "Lunes", "08:00", "10:00", "2025-1");
        ScheduleDTO dto2 = new ScheduleDTO("SCH001", "Lunes", "08:00", "10:00", "2025-1");
        ScheduleDTO dto3 = new ScheduleDTO("SCH002", "Martes", "14:00", "16:00", "2025-2");

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    @DisplayName("Caso borde - Equals con objetos null y diferente clase")
    void testEqualsCasosBorde_Borde() {
        ScheduleDTO dto1 = new ScheduleDTO("SCH001", "Lunes", "08:00", "10:00", "2025-1");

        assertNotEquals(null, dto1);
        assertNotEquals("No soy un ScheduleDTO", dto1);
    }

    @Test
    @DisplayName("Caso exitoso - ToString generado por Lombok")
    void testToString_Exitoso() {
        scheduleDTO.setScheduleId("SCH004");
        scheduleDTO.setDayOfWeek("Viernes");
        scheduleDTO.setStartHour("18:00");
        scheduleDTO.setEndHour("20:00");
        scheduleDTO.setPeriod("2025-3");

        String resultadoToString = scheduleDTO.toString();

        assertAll("ToString debe contener todos los campos",
                () -> assertTrue(resultadoToString.contains("SCH004")),
                () -> assertTrue(resultadoToString.contains("Viernes")),
                () -> assertTrue(resultadoToString.contains("18:00")),
                () -> assertTrue(resultadoToString.contains("20:00")),
                () -> assertTrue(resultadoToString.contains("2025-3"))
        );
    }

    @Test
    @DisplayName("Caso compuesto - Validación completa de múltiples escenarios")
    void testScheduleDTO_ValidacionCompleta_Compuesto() {
        ScheduleDTO dtoCompleto = new ScheduleDTO("SCH005", "Jueves", "10:00", "12:00", "2025-1");

        assertAll("Validación completa de todas las propiedades",
                () -> assertEquals("SCH005", dtoCompleto.getScheduleId()),
                () -> assertEquals("Jueves", dtoCompleto.getDayOfWeek()),
                () -> assertEquals("10:00", dtoCompleto.getStartHour()),
                () -> assertEquals("12:00", dtoCompleto.getEndHour()),
                () -> assertEquals("2025-1", dtoCompleto.getPeriod()),
                () -> assertNotNull(dtoCompleto.toString()),
                () -> assertTrue(dtoCompleto.toString().contains("ScheduleDTO"))
        );
    }

    @Test
    @DisplayName("Caso borde - Valores con espacios en blanco")
    void testValoresConEspaciosBlanco_Borde() {
        scheduleDTO.setScheduleId("  SCH006  ");
        scheduleDTO.setDayOfWeek("  Lunes  ");
        scheduleDTO.setStartHour("  09:00  ");
        scheduleDTO.setEndHour("  11:00  ");
        scheduleDTO.setPeriod("  2025-1  ");

        assertAll("Validar valores con espacios",
                () -> assertEquals("  SCH006  ", scheduleDTO.getScheduleId()),
                () -> assertEquals("  Lunes  ", scheduleDTO.getDayOfWeek()),
                () -> assertEquals("  09:00  ", scheduleDTO.getStartHour()),
                () -> assertEquals("  11:00  ", scheduleDTO.getEndHour()),
                () -> assertEquals("  2025-1  ", scheduleDTO.getPeriod())
        );
    }
}
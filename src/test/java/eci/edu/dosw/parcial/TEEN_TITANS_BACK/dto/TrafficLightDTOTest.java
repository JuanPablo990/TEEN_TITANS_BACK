package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TrafficLightDTOTest {

    @Test
    @DisplayName("Caso exitoso - Constructor con parámetros inicializa todos los campos")
    void testConstructorConParametros_Exitoso() {
        LocalDateTime timestamp = LocalDateTime.now();
        Object data = new Object();

        TrafficLightDTO dto = new TrafficLightDTO(true, "Operación completada", data, timestamp);

        assertAll("Validar todos los campos del constructor",
                () -> assertTrue(dto.isSuccess()),
                () -> assertEquals("Operación completada", dto.getMessage()),
                () -> assertEquals(data, dto.getData()),
                () -> assertEquals(timestamp, dto.getTimestamp())
        );
    }

    @Test
    @DisplayName("Caso borde - Constructor por defecto inicializa con valores por defecto")
    void testConstructorPorDefecto_Borde() {
        TrafficLightDTO dto = new TrafficLightDTO();

        assertAll("Campos deben tener valores por defecto",
                () -> assertFalse(dto.isSuccess()),
                () -> assertNull(dto.getMessage()),
                () -> assertNull(dto.getData()),
                () -> assertNull(dto.getTimestamp())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Método success crea DTO con valores correctos")
    void testMetodoSuccess_Exitoso() {
        String testData = "Datos de prueba";

        TrafficLightDTO dto = TrafficLightDTO.success(testData);

        assertAll("Validar DTO creado con método success",
                () -> assertTrue(dto.isSuccess()),
                () -> assertEquals("Operación exitosa", dto.getMessage()),
                () -> assertEquals(testData, dto.getData()),
                () -> assertNotNull(dto.getTimestamp()),
                () -> assertTrue(dto.getTimestamp().isBefore(LocalDateTime.now().plusSeconds(1)) ||
                        dto.getTimestamp().isAfter(LocalDateTime.now().minusSeconds(1)))
        );
    }

    @Test
    @DisplayName("Caso exitoso - Método success con datos null")
    void testMetodoSuccess_ConDatosNull_Exitoso() {
        TrafficLightDTO dto = TrafficLightDTO.success(null);

        assertAll("Validar DTO success con datos null",
                () -> assertTrue(dto.isSuccess()),
                () -> assertEquals("Operación exitosa", dto.getMessage()),
                () -> assertNull(dto.getData()),
                () -> assertNotNull(dto.getTimestamp())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Método error crea DTO con valores correctos")
    void testMetodoError_Exitoso() {
        String errorMessage = "Error en la operación";

        TrafficLightDTO dto = TrafficLightDTO.error(errorMessage);

        assertAll("Validar DTO creado con método error",
                () -> assertFalse(dto.isSuccess()),
                () -> assertEquals(errorMessage, dto.getMessage()),
                () -> assertNull(dto.getData()),
                () -> assertNotNull(dto.getTimestamp())
        );
    }

    @Test
    @DisplayName("Caso borde - Método error con mensaje vacío")
    void testMetodoError_MensajeVacio_Borde() {
        TrafficLightDTO dto = TrafficLightDTO.error("");

        assertAll("Validar DTO error con mensaje vacío",
                () -> assertFalse(dto.isSuccess()),
                () -> assertEquals("", dto.getMessage()),
                () -> assertNull(dto.getData()),
                () -> assertNotNull(dto.getTimestamp())
        );
    }

    @Test
    @DisplayName("Caso borde - Método error con mensaje null")
    void testMetodoError_MensajeNull_Borde() {
        TrafficLightDTO dto = TrafficLightDTO.error(null);

        assertAll("Validar DTO error con mensaje null",
                () -> assertFalse(dto.isSuccess()),
                () -> assertNull(dto.getMessage()),
                () -> assertNull(dto.getData()),
                () -> assertNotNull(dto.getTimestamp())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Setters y Getters funcionan correctamente")
    void testSettersAndGetters_Exitoso() {
        TrafficLightDTO dto = new TrafficLightDTO();
        LocalDateTime newTimestamp = LocalDateTime.now().plusHours(1);
        Object newData = "Nuevos datos";

        dto.setSuccess(false);
        dto.setMessage("Nuevo mensaje");
        dto.setData(newData);
        dto.setTimestamp(newTimestamp);

        assertAll("Validar setters y getters",
                () -> assertFalse(dto.isSuccess()),
                () -> assertEquals("Nuevo mensaje", dto.getMessage()),
                () -> assertEquals(newData, dto.getData()),
                () -> assertEquals(newTimestamp, dto.getTimestamp())
        );
    }

    @Test
    @DisplayName("Caso borde - Campo success con diferentes valores booleanos")
    void testCampoSuccess_ValoresBooleanos_Borde() {
        TrafficLightDTO dto = new TrafficLightDTO();

        dto.setSuccess(true);
        assertTrue(dto.isSuccess());

        dto.setSuccess(false);
        assertFalse(dto.isSuccess());
    }

    @Test
    @DisplayName("Caso borde - Campo data con diferentes tipos de objetos")
    void testCampoData_DiferentesTipos_Borde() {
        TrafficLightDTO dto = new TrafficLightDTO();

        dto.setData("String data");
        assertEquals("String data", dto.getData());

        dto.setData(12345);
        assertEquals(12345, dto.getData());

        dto.setData(null);
        assertNull(dto.getData());

        Object complexObject = new Object();
        dto.setData(complexObject);
        assertEquals(complexObject, dto.getData());
    }

    @Test
    @DisplayName("Caso exitoso - Equals y HashCode generados por Lombok")
    void testEqualsAndHashCode_Exitoso() {
        LocalDateTime timestamp1 = LocalDateTime.of(2025, 1, 1, 10, 0);
        LocalDateTime timestamp2 = LocalDateTime.of(2025, 1, 1, 10, 0);

        TrafficLightDTO dto1 = new TrafficLightDTO(true, "Mensaje", "Data", timestamp1);
        TrafficLightDTO dto2 = new TrafficLightDTO(true, "Mensaje", "Data", timestamp2);
        TrafficLightDTO dto3 = new TrafficLightDTO(false, "Error", null, LocalDateTime.now());

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    @DisplayName("Caso borde - Equals con objetos null y diferente clase")
    void testEqualsCasosBorde_Borde() {
        TrafficLightDTO dto = new TrafficLightDTO(true, "Test", "Data", LocalDateTime.now());

        assertNotEquals(null, dto);
        assertNotEquals("No soy un TrafficLightDTO", dto);
    }

    @Test
    @DisplayName("Caso exitoso - ToString generado por Lombok")
    void testToString_Exitoso() {
        LocalDateTime timestamp = LocalDateTime.of(2025, 1, 1, 10, 30, 0);
        TrafficLightDTO dto = new TrafficLightDTO(true, "Operación exitosa", "Test Data", timestamp);

        String resultadoToString = dto.toString();

        assertAll("ToString debe contener todos los campos",
                () -> assertTrue(resultadoToString.contains("true")),
                () -> assertTrue(resultadoToString.contains("Operación exitosa")),
                () -> assertTrue(resultadoToString.contains("Test Data")),
                () -> assertTrue(resultadoToString.contains("2025-01-01T10:30")),
                () -> assertTrue(resultadoToString.contains("TrafficLightDTO"))
        );
    }

    @Test
    @DisplayName("Caso compuesto - Validación completa de múltiples escenarios")
    void testTrafficLightDTO_ValidacionCompleta_Compuesto() {
        LocalDateTime timestamp = LocalDateTime.now();
        Object testData = new Object();

        TrafficLightDTO dto = new TrafficLightDTO(true, "Test completo", testData, timestamp);

        assertAll("Validación completa de todas las propiedades",
                () -> assertTrue(dto.isSuccess()),
                () -> assertEquals("Test completo", dto.getMessage()),
                () -> assertEquals(testData, dto.getData()),
                () -> assertEquals(timestamp, dto.getTimestamp()),
                () -> assertNotNull(dto.toString()),
                () -> assertTrue(dto.toString().contains("TrafficLightDTO"))
        );
    }

    @Test
    @DisplayName("Caso borde - Timestamp con valores extremos")
    void testTimestampValoresExtremos_Borde() {
        LocalDateTime minTimestamp = LocalDateTime.MIN;
        LocalDateTime maxTimestamp = LocalDateTime.MAX;

        TrafficLightDTO dtoMin = new TrafficLightDTO(false, "Min", null, minTimestamp);
        TrafficLightDTO dtoMax = new TrafficLightDTO(true, "Max", null, maxTimestamp);

        assertEquals(minTimestamp, dtoMin.getTimestamp());
        assertEquals(maxTimestamp, dtoMax.getTimestamp());
    }

    @Test
    @DisplayName("Caso borde - Mensajes con caracteres especiales y largos")
    void testMensajesCaracteresEspeciales_Borde() {
        String mensajeEspecial = "Mensaje con ñ, áéíóú, 中文, 🚀";
        String mensajeLargo = "A".repeat(1000);

        TrafficLightDTO dtoEspecial = TrafficLightDTO.error(mensajeEspecial);
        TrafficLightDTO dtoLargo = TrafficLightDTO.error(mensajeLargo);

        assertEquals(mensajeEspecial, dtoEspecial.getMessage());
        assertEquals(mensajeLargo, dtoLargo.getMessage());
    }
}
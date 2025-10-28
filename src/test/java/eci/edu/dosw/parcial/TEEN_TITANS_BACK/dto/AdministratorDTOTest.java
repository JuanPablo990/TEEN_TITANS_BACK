package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class AdministratorDTOTest {

    private AdministratorDTO administratorDTO;
    private Date testDate;

    @BeforeEach
    void setUp() {
        administratorDTO = new AdministratorDTO();
        testDate = new Date();
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para id")
    void testIdSetterAndGetter() {
        administratorDTO.setId("admin123");

        assertEquals("admin123", administratorDTO.getId());
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para name")
    void testNameSetterAndGetter() {
        administratorDTO.setName("Bruce Wayne");

        assertEquals("Bruce Wayne", administratorDTO.getName());
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para email")
    void testEmailSetterAndGetter() {
        administratorDTO.setEmail("bruce.wayne@titans.edu");

        assertEquals("bruce.wayne@titans.edu", administratorDTO.getEmail());
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para department")
    void testDepartmentSetterAndGetter() {
        administratorDTO.setDepartment("Security");

        assertEquals("Security", administratorDTO.getDepartment());
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para active")
    void testActiveSetterAndGetter() {
        administratorDTO.setActive(true);

        assertTrue(administratorDTO.getActive());
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para createdAt")
    void testCreatedAtSetterAndGetter() {
        administratorDTO.setCreatedAt(testDate);

        assertEquals(testDate, administratorDTO.getCreatedAt());
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para updatedAt")
    void testUpdatedAtSetterAndGetter() {
        administratorDTO.setUpdatedAt(testDate);

        assertEquals(testDate, administratorDTO.getUpdatedAt());
    }

    @Test
    @DisplayName("Caso borde - active puede ser false")
    void testActiveCanBeFalse() {
        administratorDTO.setActive(false);

        assertFalse(administratorDTO.getActive());
    }

    @Test
    @DisplayName("Caso borde - active puede ser null")
    void testActiveCanBeNull() {
        administratorDTO.setActive(null);

        assertNull(administratorDTO.getActive());
    }

    @Test
    @DisplayName("Caso borde - createdAt puede ser null")
    void testCreatedAtCanBeNull() {
        administratorDTO.setCreatedAt(null);

        assertNull(administratorDTO.getCreatedAt());
    }

    @Test
    @DisplayName("Caso borde - updatedAt puede ser null")
    void testUpdatedAtCanBeNull() {
        administratorDTO.setUpdatedAt(null);

        assertNull(administratorDTO.getUpdatedAt());
    }

    @Test
    @DisplayName("Caso borde - campos string pueden ser null")
    void testStringFieldsCanBeNull() {
        administratorDTO.setId(null);
        administratorDTO.setName(null);
        administratorDTO.setEmail(null);
        administratorDTO.setDepartment(null);

        assertAll("Verificar que todos los campos string pueden ser null",
                () -> assertNull(administratorDTO.getId()),
                () -> assertNull(administratorDTO.getName()),
                () -> assertNull(administratorDTO.getEmail()),
                () -> assertNull(administratorDTO.getDepartment())
        );
    }

    @Test
    @DisplayName("Caso exitoso - equals y hashCode funcionan correctamente")
    void testEqualsAndHashCode() {
        AdministratorDTO dto1 = new AdministratorDTO();
        dto1.setId("1");
        dto1.setName("Admin One");

        AdministratorDTO dto2 = new AdministratorDTO();
        dto2.setId("1");
        dto2.setName("Admin One");

        AdministratorDTO dto3 = new AdministratorDTO();
        dto3.setId("2");
        dto3.setName("Admin Two");

        assertAll("Verificar equals y hashCode",
                () -> assertEquals(dto1, dto2),
                () -> assertEquals(dto1.hashCode(), dto2.hashCode()),
                () -> assertNotEquals(dto1, dto3),
                () -> assertNotEquals(dto1.hashCode(), dto3.hashCode())
        );
    }

    @Test
    @DisplayName("Caso borde - equals con null")
    void testEqualsWithNull() {
        AdministratorDTO dto = new AdministratorDTO();
        dto.setId("1");

        assertNotEquals(null, dto);
    }

    @Test
    @DisplayName("Caso borde - equals con objeto de diferente clase")
    void testEqualsWithDifferentClass() {
        AdministratorDTO dto = new AdministratorDTO();
        dto.setId("1");

        assertNotEquals("string object", dto);
    }

    @Test
    @DisplayName("Caso exitoso - toString no lanza excepción")
    void testToStringDoesNotThrowException() {
        administratorDTO.setId("1");
        administratorDTO.setName("Test Admin");
        administratorDTO.setEmail("test@titans.edu");

        assertDoesNotThrow(() -> administratorDTO.toString());
    }

    @Test
    @DisplayName("Caso borde - objeto completo con todos los campos")
    void testCompleteObject() {
        administratorDTO.setId("admin-001");
        administratorDTO.setName("Alfred Pennyworth");
        administratorDTO.setEmail("alfred@titans.edu");
        administratorDTO.setDepartment("Operations");
        administratorDTO.setActive(true);
        administratorDTO.setCreatedAt(testDate);
        administratorDTO.setUpdatedAt(testDate);

        assertAll("Verificar objeto completo",
                () -> assertEquals("admin-001", administratorDTO.getId()),
                () -> assertEquals("Alfred Pennyworth", administratorDTO.getName()),
                () -> assertEquals("alfred@titans.edu", administratorDTO.getEmail()),
                () -> assertEquals("Operations", administratorDTO.getDepartment()),
                () -> assertTrue(administratorDTO.getActive()),
                () -> assertEquals(testDate, administratorDTO.getCreatedAt()),
                () -> assertEquals(testDate, administratorDTO.getUpdatedAt())
        );
    }

    @Test
    @DisplayName("Caso borde - campos con valores vacíos")
    void testEmptyStringValues() {
        administratorDTO.setId("");
        administratorDTO.setName("");
        administratorDTO.setEmail("");
        administratorDTO.setDepartment("");

        assertAll("Verificar campos con valores vacíos",
                () -> assertEquals("", administratorDTO.getId()),
                () -> assertEquals("", administratorDTO.getName()),
                () -> assertEquals("", administratorDTO.getEmail()),
                () -> assertEquals("", administratorDTO.getDepartment())
        );
    }
}
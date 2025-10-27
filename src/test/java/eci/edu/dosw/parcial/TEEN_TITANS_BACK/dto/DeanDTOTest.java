package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class DeanDTOTest {

    private DeanDTO deanDTO;
    private Date testDate;

    @BeforeEach
    void setUp() {
        deanDTO = new DeanDTO();
        testDate = new Date();
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para id")
    void testIdSetterAndGetter() {
        deanDTO.setId("dean123");

        assertEquals("dean123", deanDTO.getId());
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para name")
    void testNameSetterAndGetter() {
        deanDTO.setName("Dr. Stephen Strange");

        assertEquals("Dr. Stephen Strange", deanDTO.getName());
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para email")
    void testEmailSetterAndGetter() {
        deanDTO.setEmail("strange@titans.edu");

        assertEquals("strange@titans.edu", deanDTO.getEmail());
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para faculty")
    void testFacultySetterAndGetter() {
        deanDTO.setFaculty("Mystic Arts");

        assertEquals("Mystic Arts", deanDTO.getFaculty());
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para officeLocation")
    void testOfficeLocationSetterAndGetter() {
        deanDTO.setOfficeLocation("Sanctum Sanctorum");

        assertEquals("Sanctum Sanctorum", deanDTO.getOfficeLocation());
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para active")
    void testActiveSetterAndGetter() {
        deanDTO.setActive(true);

        assertTrue(deanDTO.getActive());
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para createdAt")
    void testCreatedAtSetterAndGetter() {
        deanDTO.setCreatedAt(testDate);

        assertEquals(testDate, deanDTO.getCreatedAt());
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para updatedAt")
    void testUpdatedAtSetterAndGetter() {
        deanDTO.setUpdatedAt(testDate);

        assertEquals(testDate, deanDTO.getUpdatedAt());
    }

    @Test
    @DisplayName("Caso borde - active puede ser false")
    void testActiveCanBeFalse() {
        deanDTO.setActive(false);

        assertFalse(deanDTO.getActive());
    }

    @Test
    @DisplayName("Caso borde - active puede ser null")
    void testActiveCanBeNull() {
        deanDTO.setActive(null);

        assertNull(deanDTO.getActive());
    }

    @Test
    @DisplayName("Caso borde - createdAt puede ser null")
    void testCreatedAtCanBeNull() {
        deanDTO.setCreatedAt(null);

        assertNull(deanDTO.getCreatedAt());
    }

    @Test
    @DisplayName("Caso borde - updatedAt puede ser null")
    void testUpdatedAtCanBeNull() {
        deanDTO.setUpdatedAt(null);

        assertNull(deanDTO.getUpdatedAt());
    }

    @Test
    @DisplayName("Caso borde - campos string pueden ser null")
    void testStringFieldsCanBeNull() {
        deanDTO.setId(null);
        deanDTO.setName(null);
        deanDTO.setEmail(null);
        deanDTO.setFaculty(null);
        deanDTO.setOfficeLocation(null);

        assertAll("Verificar que todos los campos string pueden ser null",
                () -> assertNull(deanDTO.getId()),
                () -> assertNull(deanDTO.getName()),
                () -> assertNull(deanDTO.getEmail()),
                () -> assertNull(deanDTO.getFaculty()),
                () -> assertNull(deanDTO.getOfficeLocation())
        );
    }

    @Test
    @DisplayName("Caso exitoso - equals y hashCode funcionan correctamente")
    void testEqualsAndHashCode() {
        DeanDTO dto1 = new DeanDTO();
        dto1.setId("1");
        dto1.setName("Dean One");

        DeanDTO dto2 = new DeanDTO();
        dto2.setId("1");
        dto2.setName("Dean One");

        DeanDTO dto3 = new DeanDTO();
        dto3.setId("2");
        dto3.setName("Dean Two");

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
        DeanDTO dto = new DeanDTO();
        dto.setId("1");

        assertNotEquals(null, dto);
    }

    @Test
    @DisplayName("Caso borde - equals con objeto de diferente clase")
    void testEqualsWithDifferentClass() {
        DeanDTO dto = new DeanDTO();
        dto.setId("1");

        assertNotEquals("string object", dto);
    }

    @Test
    @DisplayName("Caso exitoso - toString no lanza excepción")
    void testToStringDoesNotThrowException() {
        deanDTO.setId("1");
        deanDTO.setName("Test Dean");
        deanDTO.setEmail("test@titans.edu");

        assertDoesNotThrow(() -> deanDTO.toString());
    }

    @Test
    @DisplayName("Caso borde - objeto completo con todos los campos")
    void testCompleteObject() {
        deanDTO.setId("dean-001");
        deanDTO.setName("Dr. Charles Xavier");
        deanDTO.setEmail("xavier@titans.edu");
        deanDTO.setFaculty("Mutant Studies");
        deanDTO.setOfficeLocation("X-Mansion Office");
        deanDTO.setActive(true);
        deanDTO.setCreatedAt(testDate);
        deanDTO.setUpdatedAt(testDate);

        assertAll("Verificar objeto completo",
                () -> assertEquals("dean-001", deanDTO.getId()),
                () -> assertEquals("Dr. Charles Xavier", deanDTO.getName()),
                () -> assertEquals("xavier@titans.edu", deanDTO.getEmail()),
                () -> assertEquals("Mutant Studies", deanDTO.getFaculty()),
                () -> assertEquals("X-Mansion Office", deanDTO.getOfficeLocation()),
                () -> assertTrue(deanDTO.getActive()),
                () -> assertEquals(testDate, deanDTO.getCreatedAt()),
                () -> assertEquals(testDate, deanDTO.getUpdatedAt())
        );
    }

    @Test
    @DisplayName("Caso borde - campos con valores vacíos")
    void testEmptyStringValues() {
        deanDTO.setId("");
        deanDTO.setName("");
        deanDTO.setEmail("");
        deanDTO.setFaculty("");
        deanDTO.setOfficeLocation("");

        assertAll("Verificar campos con valores vacíos",
                () -> assertEquals("", deanDTO.getId()),
                () -> assertEquals("", deanDTO.getName()),
                () -> assertEquals("", deanDTO.getEmail()),
                () -> assertEquals("", deanDTO.getFaculty()),
                () -> assertEquals("", deanDTO.getOfficeLocation())
        );
    }

    @Test
    @DisplayName("Caso borde - faculty y officeLocation con valores largos")
    void testLongStringValues() {
        String longFaculty = "Faculty of Advanced Mystical Sciences and Arcane Arts";
        String longOffice = "Office located in the eastern wing of the main campus building near the library";

        deanDTO.setFaculty(longFaculty);
        deanDTO.setOfficeLocation(longOffice);

        assertAll("Verificar campos con valores largos",
                () -> assertEquals(longFaculty, deanDTO.getFaculty()),
                () -> assertEquals(longOffice, deanDTO.getOfficeLocation())
        );
    }

    @Test
    @DisplayName("Caso borde - diferentes fechas para createdAt y updatedAt")
    void testDifferentDates() {
        Date createdAt = new Date();
        Date updatedAt = new Date(createdAt.getTime() + 1000);

        deanDTO.setCreatedAt(createdAt);
        deanDTO.setUpdatedAt(updatedAt);

        assertAll("Verificar diferentes fechas",
                () -> assertEquals(createdAt, deanDTO.getCreatedAt()),
                () -> assertEquals(updatedAt, deanDTO.getUpdatedAt()),
                () -> assertNotEquals(deanDTO.getCreatedAt(), deanDTO.getUpdatedAt())
        );
    }
}
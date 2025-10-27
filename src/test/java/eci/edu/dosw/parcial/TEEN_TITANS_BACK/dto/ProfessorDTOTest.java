package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Date;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ProfessorDTOTest {

    private ProfessorDTO professorDTO;
    private Date testDate;

    @BeforeEach
    void setUp() {
        professorDTO = new ProfessorDTO();
        testDate = new Date();
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para id")
    void testIdSetterAndGetter() {
        professorDTO.setId("prof123");

        assertEquals("prof123", professorDTO.getId());
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para name")
    void testNameSetterAndGetter() {
        professorDTO.setName("Dr. Reed Richards");

        assertEquals("Dr. Reed Richards", professorDTO.getName());
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para email")
    void testEmailSetterAndGetter() {
        professorDTO.setEmail("richards@titans.edu");

        assertEquals("richards@titans.edu", professorDTO.getEmail());
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para department")
    void testDepartmentSetterAndGetter() {
        professorDTO.setDepartment("Physics");

        assertEquals("Physics", professorDTO.getDepartment());
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para isTenured")
    void testIsTenuredSetterAndGetter() {
        professorDTO.setIsTenured(true);

        assertTrue(professorDTO.getIsTenured());
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para areasOfExpertise")
    void testAreasOfExpertiseSetterAndGetter() {
        List<String> expertise = Arrays.asList("Quantum Mechanics", "Astrophysics", "Cosmology");
        professorDTO.setAreasOfExpertise(expertise);

        assertEquals(3, professorDTO.getAreasOfExpertise().size());
        assertTrue(professorDTO.getAreasOfExpertise().contains("Quantum Mechanics"));
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para active")
    void testActiveSetterAndGetter() {
        professorDTO.setActive(true);

        assertTrue(professorDTO.getActive());
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para createdAt")
    void testCreatedAtSetterAndGetter() {
        professorDTO.setCreatedAt(testDate);

        assertEquals(testDate, professorDTO.getCreatedAt());
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para updatedAt")
    void testUpdatedAtSetterAndGetter() {
        professorDTO.setUpdatedAt(testDate);

        assertEquals(testDate, professorDTO.getUpdatedAt());
    }

    @Test
    @DisplayName("Caso borde - isTenured puede ser false")
    void testIsTenuredCanBeFalse() {
        professorDTO.setIsTenured(false);

        assertFalse(professorDTO.getIsTenured());
    }

    @Test
    @DisplayName("Caso borde - isTenured puede ser null")
    void testIsTenuredCanBeNull() {
        professorDTO.setIsTenured(null);

        assertNull(professorDTO.getIsTenured());
    }

    @Test
    @DisplayName("Caso borde - active puede ser false")
    void testActiveCanBeFalse() {
        professorDTO.setActive(false);

        assertFalse(professorDTO.getActive());
    }

    @Test
    @DisplayName("Caso borde - active puede ser null")
    void testActiveCanBeNull() {
        professorDTO.setActive(null);

        assertNull(professorDTO.getActive());
    }

    @Test
    @DisplayName("Caso borde - areasOfExpertise puede ser null")
    void testAreasOfExpertiseCanBeNull() {
        professorDTO.setAreasOfExpertise(null);

        assertNull(professorDTO.getAreasOfExpertise());
    }

    @Test
    @DisplayName("Caso borde - areasOfExpertise puede ser lista vacía")
    void testAreasOfExpertiseCanBeEmpty() {
        professorDTO.setAreasOfExpertise(Collections.emptyList());

        assertNotNull(professorDTO.getAreasOfExpertise());
        assertTrue(professorDTO.getAreasOfExpertise().isEmpty());
    }

    @Test
    @DisplayName("Caso borde - createdAt puede ser null")
    void testCreatedAtCanBeNull() {
        professorDTO.setCreatedAt(null);

        assertNull(professorDTO.getCreatedAt());
    }

    @Test
    @DisplayName("Caso borde - updatedAt puede ser null")
    void testUpdatedAtCanBeNull() {
        professorDTO.setUpdatedAt(null);

        assertNull(professorDTO.getUpdatedAt());
    }

    @Test
    @DisplayName("Caso borde - campos string pueden ser null")
    void testStringFieldsCanBeNull() {
        professorDTO.setId(null);
        professorDTO.setName(null);
        professorDTO.setEmail(null);
        professorDTO.setDepartment(null);

        assertAll("Verificar que todos los campos string pueden ser null",
                () -> assertNull(professorDTO.getId()),
                () -> assertNull(professorDTO.getName()),
                () -> assertNull(professorDTO.getEmail()),
                () -> assertNull(professorDTO.getDepartment())
        );
    }

    @Test
    @DisplayName("Caso exitoso - equals y hashCode funcionan correctamente")
    void testEqualsAndHashCode() {
        ProfessorDTO dto1 = new ProfessorDTO();
        dto1.setId("1");
        dto1.setName("Professor One");

        ProfessorDTO dto2 = new ProfessorDTO();
        dto2.setId("1");
        dto2.setName("Professor One");

        ProfessorDTO dto3 = new ProfessorDTO();
        dto3.setId("2");
        dto3.setName("Professor Two");

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
        ProfessorDTO dto = new ProfessorDTO();
        dto.setId("1");

        assertNotEquals(null, dto);
    }

    @Test
    @DisplayName("Caso borde - equals con objeto de diferente clase")
    void testEqualsWithDifferentClass() {
        ProfessorDTO dto = new ProfessorDTO();
        dto.setId("1");

        assertNotEquals("string object", dto);
    }

    @Test
    @DisplayName("Caso exitoso - toString no lanza excepción")
    void testToStringDoesNotThrowException() {
        professorDTO.setId("1");
        professorDTO.setName("Test Professor");
        professorDTO.setEmail("test@titans.edu");

        assertDoesNotThrow(() -> professorDTO.toString());
    }

    @Test
    @DisplayName("Caso borde - objeto completo con todos los campos")
    void testCompleteObject() {
        List<String> expertise = Arrays.asList("Robotics", "AI", "Cybersecurity");

        professorDTO.setId("prof-001");
        professorDTO.setName("Dr. Tony Stark");
        professorDTO.setEmail("stark@titans.edu");
        professorDTO.setDepartment("Engineering");
        professorDTO.setIsTenured(true);
        professorDTO.setAreasOfExpertise(expertise);
        professorDTO.setActive(true);
        professorDTO.setCreatedAt(testDate);
        professorDTO.setUpdatedAt(testDate);

        assertAll("Verificar objeto completo",
                () -> assertEquals("prof-001", professorDTO.getId()),
                () -> assertEquals("Dr. Tony Stark", professorDTO.getName()),
                () -> assertEquals("stark@titans.edu", professorDTO.getEmail()),
                () -> assertEquals("Engineering", professorDTO.getDepartment()),
                () -> assertTrue(professorDTO.getIsTenured()),
                () -> assertEquals(3, professorDTO.getAreasOfExpertise().size()),
                () -> assertTrue(professorDTO.getAreasOfExpertise().contains("AI")),
                () -> assertTrue(professorDTO.getActive()),
                () -> assertEquals(testDate, professorDTO.getCreatedAt()),
                () -> assertEquals(testDate, professorDTO.getUpdatedAt())
        );
    }

    @Test
    @DisplayName("Caso borde - campos con valores vacíos")
    void testEmptyStringValues() {
        professorDTO.setId("");
        professorDTO.setName("");
        professorDTO.setEmail("");
        professorDTO.setDepartment("");

        assertAll("Verificar campos con valores vacíos",
                () -> assertEquals("", professorDTO.getId()),
                () -> assertEquals("", professorDTO.getName()),
                () -> assertEquals("", professorDTO.getEmail()),
                () -> assertEquals("", professorDTO.getDepartment())
        );
    }

    @Test
    @DisplayName("Caso borde - areasOfExpertise con lista grande")
    void testLargeAreasOfExpertise() {
        List<String> largeExpertise = Arrays.asList(
                "Quantum Physics", "Relativity", "String Theory",
                "Particle Physics", "Cosmology", "Astrophysics",
                "Thermodynamics", "Electromagnetism", "Optics"
        );

        professorDTO.setAreasOfExpertise(largeExpertise);

        assertEquals(9, professorDTO.getAreasOfExpertise().size());
        assertTrue(professorDTO.getAreasOfExpertise().contains("String Theory"));
    }

    @Test
    @DisplayName("Caso borde - diferentes fechas para createdAt y updatedAt")
    void testDifferentDates() {
        Date createdAt = new Date();
        Date updatedAt = new Date(createdAt.getTime() + 1000);

        professorDTO.setCreatedAt(createdAt);
        professorDTO.setUpdatedAt(updatedAt);

        assertAll("Verificar diferentes fechas",
                () -> assertEquals(createdAt, professorDTO.getCreatedAt()),
                () -> assertEquals(updatedAt, professorDTO.getUpdatedAt()),
                () -> assertNotEquals(professorDTO.getCreatedAt(), professorDTO.getUpdatedAt())
        );
    }

    @Test
    @DisplayName("Caso borde - professor sin tenure y inactivo")
    void testNonTenuredInactiveProfessor() {
        professorDTO.setIsTenured(false);
        professorDTO.setActive(false);
        professorDTO.setAreasOfExpertise(null);

        assertAll("Verificar professor sin tenure e inactivo",
                () -> assertFalse(professorDTO.getIsTenured()),
                () -> assertFalse(professorDTO.getActive()),
                () -> assertNull(professorDTO.getAreasOfExpertise())
        );
    }

    @Test
    @DisplayName("Caso borde - areasOfExpertise con valores duplicados")
    void testAreasOfExpertiseWithDuplicates() {
        List<String> expertiseWithDuplicates = Arrays.asList("Mathematics", "Mathematics", "Physics");

        professorDTO.setAreasOfExpertise(expertiseWithDuplicates);

        assertEquals(3, professorDTO.getAreasOfExpertise().size());
        assertEquals("Mathematics", professorDTO.getAreasOfExpertise().get(0));
        assertEquals("Mathematics", professorDTO.getAreasOfExpertise().get(1));
    }
}
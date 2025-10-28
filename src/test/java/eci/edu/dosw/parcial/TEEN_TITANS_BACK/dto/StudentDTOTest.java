package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class StudentDTOTest {

    private StudentDTO studentDTO;
    private Date testDate;

    @BeforeEach
    void setUp() {
        studentDTO = new StudentDTO();
        testDate = new Date();
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para id")
    void testIdSetterAndGetter() {
        studentDTO.setId("student123");

        assertEquals("student123", studentDTO.getId());
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para name")
    void testNameSetterAndGetter() {
        studentDTO.setName("Peter Parker");

        assertEquals("Peter Parker", studentDTO.getName());
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para email")
    void testEmailSetterAndGetter() {
        studentDTO.setEmail("parker@titans.edu");

        assertEquals("parker@titans.edu", studentDTO.getEmail());
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para password")
    void testPasswordSetterAndGetter() {
        studentDTO.setPassword("securePassword123");

        assertEquals("securePassword123", studentDTO.getPassword());
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para role")
    void testRoleSetterAndGetter() {
        studentDTO.setRole("STUDENT");

        assertEquals("STUDENT", studentDTO.getRole());
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para active")
    void testActiveSetterAndGetter() {
        studentDTO.setActive(true);

        assertTrue(studentDTO.getActive());
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para createdAt")
    void testCreatedAtSetterAndGetter() {
        studentDTO.setCreatedAt(testDate);

        assertEquals(testDate, studentDTO.getCreatedAt());
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para updatedAt")
    void testUpdatedAtSetterAndGetter() {
        studentDTO.setUpdatedAt(testDate);

        assertEquals(testDate, studentDTO.getUpdatedAt());
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para academicProgram")
    void testAcademicProgramSetterAndGetter() {
        studentDTO.setAcademicProgram("Computer Science");

        assertEquals("Computer Science", studentDTO.getAcademicProgram());
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para semester")
    void testSemesterSetterAndGetter() {
        studentDTO.setSemester(5);

        assertEquals(5, studentDTO.getSemester());
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para gradeAverage")
    void testGradeAverageSetterAndGetter() {
        studentDTO.setGradeAverage(4.2);

        assertEquals(4.2, studentDTO.getGradeAverage());
    }

    @Test
    @DisplayName("Caso borde - active puede ser false")
    void testActiveCanBeFalse() {
        studentDTO.setActive(false);

        assertFalse(studentDTO.getActive());
    }

    @Test
    @DisplayName("Caso borde - active puede ser null")
    void testActiveCanBeNull() {
        studentDTO.setActive(null);

        assertNull(studentDTO.getActive());
    }

    @Test
    @DisplayName("Caso borde - semester puede ser null")
    void testSemesterCanBeNull() {
        studentDTO.setSemester(null);

        assertNull(studentDTO.getSemester());
    }

    @Test
    @DisplayName("Caso borde - gradeAverage puede ser null")
    void testGradeAverageCanBeNull() {
        studentDTO.setGradeAverage(null);

        assertNull(studentDTO.getGradeAverage());
    }

    @Test
    @DisplayName("Caso borde - gradeAverage puede ser cero")
    void testGradeAverageCanBeZero() {
        studentDTO.setGradeAverage(0.0);

        assertEquals(0.0, studentDTO.getGradeAverage());
    }

    @Test
    @DisplayName("Caso borde - semester valores límite")
    void testSemesterBoundaryValues() {
        studentDTO.setSemester(1);
        assertEquals(1, studentDTO.getSemester());

        studentDTO.setSemester(10);
        assertEquals(10, studentDTO.getSemester());
    }

    @Test
    @DisplayName("Caso borde - gradeAverage valores límite")
    void testGradeAverageBoundaryValues() {
        studentDTO.setGradeAverage(0.0);
        assertEquals(0.0, studentDTO.getGradeAverage());

        studentDTO.setGradeAverage(5.0);
        assertEquals(5.0, studentDTO.getGradeAverage());
    }

    @Test
    @DisplayName("Caso borde - createdAt puede ser null")
    void testCreatedAtCanBeNull() {
        studentDTO.setCreatedAt(null);

        assertNull(studentDTO.getCreatedAt());
    }

    @Test
    @DisplayName("Caso borde - updatedAt puede ser null")
    void testUpdatedAtCanBeNull() {
        studentDTO.setUpdatedAt(null);

        assertNull(studentDTO.getUpdatedAt());
    }

    @Test
    @DisplayName("Caso borde - campos string pueden ser null")
    void testStringFieldsCanBeNull() {
        studentDTO.setId(null);
        studentDTO.setName(null);
        studentDTO.setEmail(null);
        studentDTO.setPassword(null);
        studentDTO.setRole(null);
        studentDTO.setAcademicProgram(null);

        assertAll("Verificar que todos los campos string pueden ser null",
                () -> assertNull(studentDTO.getId()),
                () -> assertNull(studentDTO.getName()),
                () -> assertNull(studentDTO.getEmail()),
                () -> assertNull(studentDTO.getPassword()),
                () -> assertNull(studentDTO.getRole()),
                () -> assertNull(studentDTO.getAcademicProgram())
        );
    }

    @Test
    @DisplayName("Caso exitoso - equals y hashCode funcionan correctamente")
    void testEqualsAndHashCode() {
        StudentDTO dto1 = new StudentDTO();
        dto1.setId("1");
        dto1.setName("Student One");

        StudentDTO dto2 = new StudentDTO();
        dto2.setId("1");
        dto2.setName("Student One");

        StudentDTO dto3 = new StudentDTO();
        dto3.setId("2");
        dto3.setName("Student Two");

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
        StudentDTO dto = new StudentDTO();
        dto.setId("1");

        assertNotEquals(null, dto);
    }

    @Test
    @DisplayName("Caso borde - equals con objeto de diferente clase")
    void testEqualsWithDifferentClass() {
        StudentDTO dto = new StudentDTO();
        dto.setId("1");

        assertNotEquals("string object", dto);
    }

    @Test
    @DisplayName("Caso exitoso - toString no lanza excepción")
    void testToStringDoesNotThrowException() {
        studentDTO.setId("1");
        studentDTO.setName("Test Student");
        studentDTO.setEmail("test@titans.edu");

        assertDoesNotThrow(() -> studentDTO.toString());
    }

    @Test
    @DisplayName("Caso borde - objeto completo con todos los campos")
    void testCompleteObject() {
        studentDTO.setId("student-001");
        studentDTO.setName("Miles Morales");
        studentDTO.setEmail("morales@titans.edu");
        studentDTO.setPassword("encryptedPass");
        studentDTO.setRole("STUDENT");
        studentDTO.setActive(true);
        studentDTO.setCreatedAt(testDate);
        studentDTO.setUpdatedAt(testDate);
        studentDTO.setAcademicProgram("Biotechnology");
        studentDTO.setSemester(6);
        studentDTO.setGradeAverage(4.5);

        assertAll("Verificar objeto completo",
                () -> assertEquals("student-001", studentDTO.getId()),
                () -> assertEquals("Miles Morales", studentDTO.getName()),
                () -> assertEquals("morales@titans.edu", studentDTO.getEmail()),
                () -> assertEquals("encryptedPass", studentDTO.getPassword()),
                () -> assertEquals("STUDENT", studentDTO.getRole()),
                () -> assertTrue(studentDTO.getActive()),
                () -> assertEquals(testDate, studentDTO.getCreatedAt()),
                () -> assertEquals(testDate, studentDTO.getUpdatedAt()),
                () -> assertEquals("Biotechnology", studentDTO.getAcademicProgram()),
                () -> assertEquals(6, studentDTO.getSemester()),
                () -> assertEquals(4.5, studentDTO.getGradeAverage())
        );
    }

    @Test
    @DisplayName("Caso borde - campos con valores vacíos")
    void testEmptyStringValues() {
        studentDTO.setId("");
        studentDTO.setName("");
        studentDTO.setEmail("");
        studentDTO.setPassword("");
        studentDTO.setRole("");
        studentDTO.setAcademicProgram("");

        assertAll("Verificar campos con valores vacíos",
                () -> assertEquals("", studentDTO.getId()),
                () -> assertEquals("", studentDTO.getName()),
                () -> assertEquals("", studentDTO.getEmail()),
                () -> assertEquals("", studentDTO.getPassword()),
                () -> assertEquals("", studentDTO.getRole()),
                () -> assertEquals("", studentDTO.getAcademicProgram())
        );
    }

    @Test
    @DisplayName("Caso borde - diferentes fechas para createdAt y updatedAt")
    void testDifferentDates() {
        Date createdAt = new Date();
        Date updatedAt = new Date(createdAt.getTime() + 1000);

        studentDTO.setCreatedAt(createdAt);
        studentDTO.setUpdatedAt(updatedAt);

        assertAll("Verificar diferentes fechas",
                () -> assertEquals(createdAt, studentDTO.getCreatedAt()),
                () -> assertEquals(updatedAt, studentDTO.getUpdatedAt()),
                () -> assertNotEquals(studentDTO.getCreatedAt(), studentDTO.getUpdatedAt())
        );
    }

    @Test
    @DisplayName("Caso borde - student inactivo con bajo promedio")
    void testInactiveStudentWithLowAverage() {
        studentDTO.setActive(false);
        studentDTO.setGradeAverage(2.5);
        studentDTO.setSemester(1);

        assertAll("Verificar student inactivo con bajo promedio",
                () -> assertFalse(studentDTO.getActive()),
                () -> assertEquals(2.5, studentDTO.getGradeAverage()),
                () -> assertEquals(1, studentDTO.getSemester())
        );
    }

    @Test
    @DisplayName("Caso borde - student sin programa académico definido")
    void testStudentWithoutAcademicProgram() {
        studentDTO.setAcademicProgram(null);
        studentDTO.setSemester(null);
        studentDTO.setGradeAverage(null);

        assertAll("Verificar student sin información académica",
                () -> assertNull(studentDTO.getAcademicProgram()),
                () -> assertNull(studentDTO.getSemester()),
                () -> assertNull(studentDTO.getGradeAverage())
        );
    }

    @Test
    @DisplayName("Caso borde - password con caracteres especiales")
    void testPasswordWithSpecialCharacters() {
        String complexPassword = "P@ssw0rd!123#";
        studentDTO.setPassword(complexPassword);

        assertEquals(complexPassword, studentDTO.getPassword());
    }

    @Test
    @DisplayName("Caso borde - academicProgram con nombre largo")
    void testLongAcademicProgram() {
        String longProgram = "International Relations and Global Political Economy";
        studentDTO.setAcademicProgram(longProgram);

        assertEquals(longProgram, studentDTO.getAcademicProgram());
    }

    @Test
    @DisplayName("Caso borde - gradeAverage con decimales")
    void testGradeAverageWithDecimals() {
        studentDTO.setGradeAverage(3.75);

        assertEquals(3.75, studentDTO.getGradeAverage());
    }
}
package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class UserDTOTest {

    private UserDTO userDTO;
    private Date testDate;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        testDate = new Date();
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para id")
    void testIdSetterAndGetter() {
        userDTO.setId("user123");

        assertEquals("user123", userDTO.getId());
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para name")
    void testNameSetterAndGetter() {
        userDTO.setName("Clark Kent");

        assertEquals("Clark Kent", userDTO.getName());
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para email")
    void testEmailSetterAndGetter() {
        userDTO.setEmail("kent@titans.edu");

        assertEquals("kent@titans.edu", userDTO.getEmail());
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para password")
    void testPasswordSetterAndGetter() {
        userDTO.setPassword("securePassword123");

        assertEquals("securePassword123", userDTO.getPassword());
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para role")
    void testRoleSetterAndGetter() {
        userDTO.setRole("ADMIN");

        assertEquals("ADMIN", userDTO.getRole());
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para active")
    void testActiveSetterAndGetter() {
        userDTO.setActive(true);

        assertTrue(userDTO.getActive());
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para createdAt")
    void testCreatedAtSetterAndGetter() {
        userDTO.setCreatedAt(testDate);

        assertEquals(testDate, userDTO.getCreatedAt());
    }

    @Test
    @DisplayName("Caso exitoso - setter y getter para updatedAt")
    void testUpdatedAtSetterAndGetter() {
        userDTO.setUpdatedAt(testDate);

        assertEquals(testDate, userDTO.getUpdatedAt());
    }

    @Test
    @DisplayName("Caso borde - active puede ser false")
    void testActiveCanBeFalse() {
        userDTO.setActive(false);

        assertFalse(userDTO.getActive());
    }

    @Test
    @DisplayName("Caso borde - active puede ser null")
    void testActiveCanBeNull() {
        userDTO.setActive(null);

        assertNull(userDTO.getActive());
    }

    @Test
    @DisplayName("Caso borde - createdAt puede ser null")
    void testCreatedAtCanBeNull() {
        userDTO.setCreatedAt(null);

        assertNull(userDTO.getCreatedAt());
    }

    @Test
    @DisplayName("Caso borde - updatedAt puede ser null")
    void testUpdatedAtCanBeNull() {
        userDTO.setUpdatedAt(null);

        assertNull(userDTO.getUpdatedAt());
    }

    @Test
    @DisplayName("Caso borde - campos string pueden ser null")
    void testStringFieldsCanBeNull() {
        userDTO.setId(null);
        userDTO.setName(null);
        userDTO.setEmail(null);
        userDTO.setPassword(null);
        userDTO.setRole(null);

        assertAll("Verificar que todos los campos string pueden ser null",
                () -> assertNull(userDTO.getId()),
                () -> assertNull(userDTO.getName()),
                () -> assertNull(userDTO.getEmail()),
                () -> assertNull(userDTO.getPassword()),
                () -> assertNull(userDTO.getRole())
        );
    }

    @Test
    @DisplayName("Caso exitoso - equals y hashCode funcionan correctamente")
    void testEqualsAndHashCode() {
        UserDTO dto1 = new UserDTO();
        dto1.setId("1");
        dto1.setName("User One");

        UserDTO dto2 = new UserDTO();
        dto2.setId("1");
        dto2.setName("User One");

        UserDTO dto3 = new UserDTO();
        dto3.setId("2");
        dto3.setName("User Two");

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
        UserDTO dto = new UserDTO();
        dto.setId("1");

        assertNotEquals(null, dto);
    }

    @Test
    @DisplayName("Caso borde - equals con objeto de diferente clase")
    void testEqualsWithDifferentClass() {
        UserDTO dto = new UserDTO();
        dto.setId("1");

        assertNotEquals("string object", dto);
    }

    @Test
    @DisplayName("Caso exitoso - toString no lanza excepción")
    void testToStringDoesNotThrowException() {
        userDTO.setId("1");
        userDTO.setName("Test User");
        userDTO.setEmail("test@titans.edu");

        assertDoesNotThrow(() -> userDTO.toString());
    }

    @Test
    @DisplayName("Caso borde - objeto completo con todos los campos")
    void testCompleteObject() {
        userDTO.setId("user-001");
        userDTO.setName("Diana Prince");
        userDTO.setEmail("prince@titans.edu");
        userDTO.setPassword("amazonSecret");
        userDTO.setRole("DEAN");
        userDTO.setActive(true);
        userDTO.setCreatedAt(testDate);
        userDTO.setUpdatedAt(testDate);

        assertAll("Verificar objeto completo",
                () -> assertEquals("user-001", userDTO.getId()),
                () -> assertEquals("Diana Prince", userDTO.getName()),
                () -> assertEquals("prince@titans.edu", userDTO.getEmail()),
                () -> assertEquals("amazonSecret", userDTO.getPassword()),
                () -> assertEquals("DEAN", userDTO.getRole()),
                () -> assertTrue(userDTO.getActive()),
                () -> assertEquals(testDate, userDTO.getCreatedAt()),
                () -> assertEquals(testDate, userDTO.getUpdatedAt())
        );
    }

    @Test
    @DisplayName("Caso borde - campos con valores vacíos")
    void testEmptyStringValues() {
        userDTO.setId("");
        userDTO.setName("");
        userDTO.setEmail("");
        userDTO.setPassword("");
        userDTO.setRole("");

        assertAll("Verificar campos con valores vacíos",
                () -> assertEquals("", userDTO.getId()),
                () -> assertEquals("", userDTO.getName()),
                () -> assertEquals("", userDTO.getEmail()),
                () -> assertEquals("", userDTO.getPassword()),
                () -> assertEquals("", userDTO.getRole())
        );
    }

    @Test
    @DisplayName("Caso borde - diferentes fechas para createdAt y updatedAt")
    void testDifferentDates() {
        Date createdAt = new Date();
        Date updatedAt = new Date(createdAt.getTime() + 1000);

        userDTO.setCreatedAt(createdAt);
        userDTO.setUpdatedAt(updatedAt);

        assertAll("Verificar diferentes fechas",
                () -> assertEquals(createdAt, userDTO.getCreatedAt()),
                () -> assertEquals(updatedAt, userDTO.getUpdatedAt()),
                () -> assertNotEquals(userDTO.getCreatedAt(), userDTO.getUpdatedAt())
        );
    }

    @Test
    @DisplayName("Caso borde - user inactivo")
    void testInactiveUser() {
        userDTO.setActive(false);
        userDTO.setRole("STUDENT");

        assertAll("Verificar user inactivo",
                () -> assertFalse(userDTO.getActive()),
                () -> assertEquals("STUDENT", userDTO.getRole())
        );
    }

    @Test
    @DisplayName("Caso borde - password con caracteres especiales")
    void testPasswordWithSpecialCharacters() {
        String complexPassword = "P@ssw0rd!123#$_%&*";
        userDTO.setPassword(complexPassword);

        assertEquals(complexPassword, userDTO.getPassword());
    }

    @Test
    @DisplayName("Caso borde - email con formato complejo")
    void testEmailWithComplexFormat() {
        String complexEmail = "firstname.lastname+tag@subdomain.titans.edu";
        userDTO.setEmail(complexEmail);

        assertEquals(complexEmail, userDTO.getEmail());
    }

    @Test
    @DisplayName("Caso borde - name con caracteres especiales")
    void testNameWithSpecialCharacters() {
        String nameWithAccents = "María José Rodríguez";
        userDTO.setName(nameWithAccents);

        assertEquals(nameWithAccents, userDTO.getName());
    }

    @Test
    @DisplayName("Caso borde - role en minúsculas")
    void testRoleInLowerCase() {
        userDTO.setRole("professor");

        assertEquals("professor", userDTO.getRole());
    }

    @Test
    @DisplayName("Caso borde - role con espacios")
    void testRoleWithSpaces() {
        userDTO.setRole("system admin");

        assertEquals("system admin", userDTO.getRole());
    }

    @Test
    @DisplayName("Caso borde - user sin password")
    void testUserWithoutPassword() {
        userDTO.setPassword(null);

        assertNull(userDTO.getPassword());
    }

    @Test
    @DisplayName("Caso borde - user con todos los campos null")
    void testUserWithAllFieldsNull() {
        UserDTO nullUser = new UserDTO();

        assertAll("Verificar user con todos los campos null",
                () -> assertNull(nullUser.getId()),
                () -> assertNull(nullUser.getName()),
                () -> assertNull(nullUser.getEmail()),
                () -> assertNull(nullUser.getPassword()),
                () -> assertNull(nullUser.getRole()),
                () -> assertNull(nullUser.getActive()),
                () -> assertNull(nullUser.getCreatedAt()),
                () -> assertNull(nullUser.getUpdatedAt())
        );
    }

    @Test
    @DisplayName("Caso borde - user con ID muy largo")
    void testUserWithLongId() {
        String longId = "user-" + "x".repeat(100);
        userDTO.setId(longId);

        assertEquals(longId, userDTO.getId());
    }
}
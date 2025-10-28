package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginResponseDTOTest {

    @Test
    void testAllArgsConstructor() {
        LoginResponseDTO response = new LoginResponseDTO(
                "123", "John Doe", "john@test.com",
                UserRole.STUDENT, true, "Success", "token123"
        );

        assertEquals("123", response.getId());
        assertEquals("John Doe", response.getName());
        assertEquals("john@test.com", response.getEmail());
        assertEquals(UserRole.STUDENT, response.getRole());
        assertTrue(response.isSuccess());
        assertEquals("Success", response.getMessage());
        assertEquals("token123", response.getToken());
    }

    @Test
    void testConstructorWithoutToken() {
        LoginResponseDTO response = new LoginResponseDTO(
                "456", "Jane Smith", "jane@test.com",
                UserRole.PROFESSOR, true, "Login successful"
        );

        assertEquals("456", response.getId());
        assertEquals("Jane Smith", response.getName());
        assertEquals("jane@test.com", response.getEmail());
        assertEquals(UserRole.PROFESSOR, response.getRole());
        assertTrue(response.isSuccess());
        assertEquals("Login successful", response.getMessage());
        assertNull(response.getToken());
    }

    @Test
    void testSuccessMessageConstructor() {
        LoginResponseDTO response = new LoginResponseDTO(true, "Operation completed");

        assertTrue(response.isSuccess());
        assertEquals("Operation completed", response.getMessage());
        assertNull(response.getId());
        assertNull(response.getName());
        assertNull(response.getEmail());
        assertNull(response.getRole());
        assertNull(response.getToken());
    }

    @Test
    void testNoArgsConstructor() {
        LoginResponseDTO response = new LoginResponseDTO();

        assertNull(response.getId());
        assertNull(response.getName());
        assertNull(response.getEmail());
        assertNull(response.getRole());
        assertFalse(response.isSuccess());
        assertNull(response.getMessage());
        assertNull(response.getToken());
    }

    @Test
    void testSettersAndGetters() {
        LoginResponseDTO response = new LoginResponseDTO();

        response.setId("789");
        response.setName("Bob Wilson");
        response.setEmail("bob@test.com");
        response.setRole(UserRole.ADMINISTRATOR);
        response.setSuccess(false);
        response.setMessage("Error occurred");
        response.setToken("jwt_token");

        assertEquals("789", response.getId());
        assertEquals("Bob Wilson", response.getName());
        assertEquals("bob@test.com", response.getEmail());
        assertEquals(UserRole.ADMINISTRATOR, response.getRole());
        assertFalse(response.isSuccess());
        assertEquals("Error occurred", response.getMessage());
        assertEquals("jwt_token", response.getToken());
    }

    @Test
    void testEqualsAndHashCode() {
        LoginResponseDTO response1 = new LoginResponseDTO("1", "User1", "user1@test.com", UserRole.STUDENT, true, "Success");
        LoginResponseDTO response2 = new LoginResponseDTO("1", "User1", "user1@test.com", UserRole.STUDENT, true, "Success");

        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void testNotEquals() {
        LoginResponseDTO response1 = new LoginResponseDTO("1", "User1", "user1@test.com", UserRole.STUDENT, true, "Success");
        LoginResponseDTO response2 = new LoginResponseDTO("2", "User2", "user2@test.com", UserRole.PROFESSOR, false, "Error");

        assertNotEquals(response1, response2);
        assertNotEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void testToString() {
        LoginResponseDTO response = new LoginResponseDTO("123", "Test User", "test@test.com", UserRole.DEAN, true, "Test");

        String toString = response.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("123"));
        assertTrue(toString.contains("Test User"));
        assertTrue(toString.contains("test@test.com"));
        assertTrue(toString.contains("DEAN"));
    }
}
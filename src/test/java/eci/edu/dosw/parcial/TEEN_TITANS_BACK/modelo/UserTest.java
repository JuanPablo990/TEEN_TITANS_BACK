package eci.edu.dosw.parcial.TEEN_TITANS_BACK.modelo;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import java.util.Date;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(
                "USR-001",
                "Bruce Wayne",
                "bwayne@eci.edu.co",
                "password123",
                UserRole.STUDENT
        );
    }

    // Happy Path Tests
    @Test
    void testUserCreation_WithFiveParameterConstructor_Success() {
        // ARRANGE
        String expectedId = "USR-001";
        String expectedName = "Bruce Wayne";
        String expectedEmail = "bwayne@eci.edu.co";
        String expectedPassword = "password123";
        UserRole expectedRole = UserRole.STUDENT;

        // ACT
        User newUser = new User(expectedId, expectedName, expectedEmail, expectedPassword, expectedRole);

        // ASSERT - Verificar cada campo individualmente
        assertEquals(expectedId, newUser.getId());
        assertEquals(expectedName, newUser.getName());
        assertEquals(expectedEmail, newUser.getEmail());
        assertEquals(expectedPassword, newUser.getPassword());
        assertEquals(expectedRole, newUser.getRole());
        assertTrue(newUser.isActive());
        assertNotNull(newUser.getCreatedAt());
        assertNotNull(newUser.getUpdatedAt());
    }

    @Test
    void testUserCreation_WithAllArgsConstructor_Success() {
        // ARRANGE
        Date customDate = new Date(System.currentTimeMillis() - 1000);
        String expectedId = "USR-002";
        String expectedName = "Clark Kent";
        String expectedEmail = "ckent@eci.edu.co";
        String expectedPassword = "supersecret";
        UserRole expectedRole = UserRole.PROFESSOR;

        // ACT
        User fullUser = new User(
                expectedId,
                expectedName,
                expectedEmail,
                expectedPassword,
                expectedRole,
                false,
                customDate,
                customDate
        );

        // ASSERT
        assertEquals(expectedId, fullUser.getId());
        assertEquals(expectedName, fullUser.getName());
        assertEquals(expectedEmail, fullUser.getEmail());
        assertEquals(expectedPassword, fullUser.getPassword());
        assertEquals(expectedRole, fullUser.getRole());
        assertFalse(fullUser.isActive());
        assertEquals(customDate, fullUser.getCreatedAt());
        assertEquals(customDate, fullUser.getUpdatedAt());
    }

    // Error Path Tests
    @Test
    void testUserCreation_NullRole_ShouldNotThrowException() {
        // ARRANGE
        UserRole nullRole = null;

        // ACT
        User userWithNullRole = new User(
                "USR-003",
                "Test User",
                "test@eci.edu.co",
                "password",
                nullRole
        );

        // ASSERT
        assertNull(userWithNullRole.getRole());
        assertTrue(userWithNullRole.isActive());
    }

    @Test
    void testUserCreation_EmptyFields_ShouldAcceptEmptyValues() {
        // ARRANGE
        String emptyId = "";
        String emptyName = "";
        String emptyEmail = "";
        String emptyPassword = "";

        // ACT
        User emptyUser = new User(
                emptyId,
                emptyName,
                emptyEmail,
                emptyPassword,
                UserRole.ADMINISTRATOR
        );

        // ASSERT
        assertEquals(emptyId, emptyUser.getId());
        assertEquals(emptyName, emptyUser.getName());
        assertEquals(emptyEmail, emptyUser.getEmail());
        assertEquals(emptyPassword, emptyUser.getPassword());
        assertEquals(UserRole.ADMINISTRATOR, emptyUser.getRole());
        assertTrue(emptyUser.isActive());
    }

    @Test
    void testNoArgsConstructor_CreatesInstanceWithDefaultValues() {
        // ARRANGE & ACT
        User emptyUser = new User();

        // ASSERT
        assertNotNull(emptyUser);
        assertNull(emptyUser.getId());
        assertNull(emptyUser.getName());
        assertNull(emptyUser.getEmail());
        assertNull(emptyUser.getPassword());
        assertNull(emptyUser.getRole());
        assertTrue(emptyUser.isActive());
        assertNotNull(emptyUser.getCreatedAt());
        assertNotNull(emptyUser.getUpdatedAt());
    }

    @Test
    void testSetters_UpdateAllFieldsCorrectly() {
        // ARRANGE
        User user = new User();
        String newId = "NEW-001";
        String newName = "Diana Prince";
        String newEmail = "dprince@eci.edu.co";
        String newPassword = "newpass123";
        UserRole newRole = UserRole.DEAN;
        Date newDate = new Date(System.currentTimeMillis() - 5000);

        // ACT
        user.setId(newId);
        user.setName(newName);
        user.setEmail(newEmail);
        user.setPassword(newPassword);
        user.setRole(newRole);
        user.setActive(false);
        user.setCreatedAt(newDate);
        user.setUpdatedAt(newDate);

        // ASSERT
        assertEquals(newId, user.getId());
        assertEquals(newName, user.getName());
        assertEquals(newEmail, user.getEmail());
        assertEquals(newPassword, user.getPassword());
        assertEquals(newRole, user.getRole());
        assertFalse(user.isActive());
        assertEquals(newDate, user.getCreatedAt());
        assertEquals(newDate, user.getUpdatedAt());
    }

    @Test
    void testDefaultValues_AreSetCorrectlyInFiveParamConstructor() {
        // ARRANGE & ACT
        User user = new User("ID-001", "John Doe", "john@eci.edu.co", "pass", UserRole.STUDENT);

        // ASSERT - Verificar valores por defecto
        assertTrue(user.isActive(), "User should be active by default");
        assertNotNull(user.getCreatedAt(), "CreatedAt should not be null");
        assertNotNull(user.getUpdatedAt(), "UpdatedAt should not be null");

        // Verificar que las fechas son recientes (dentro de los últimos 5 segundos)
        long currentTime = System.currentTimeMillis();
        long createdAtTime = user.getCreatedAt().getTime();
        long updatedAtTime = user.getUpdatedAt().getTime();

        assertTrue(Math.abs(currentTime - createdAtTime) < 5000, "CreatedAt should be recent");
        assertTrue(Math.abs(currentTime - updatedAtTime) < 5000, "UpdatedAt should be recent");
    }

    @Test
    void testUserEquality_BasedOnId() {
        // ARRANGE
        User user1 = new User("SAME-ID", "User One", "user1@test.com", "pass1", UserRole.STUDENT);
        User user2 = new User("SAME-ID", "User Two", "user2@test.com", "pass2", UserRole.PROFESSOR);
        User user3 = new User("DIFFERENT-ID", "User One", "user1@test.com", "pass1", UserRole.STUDENT);

        // ACT & ASSERT
        assertEquals(user1.getId(), user2.getId(), "Users with same ID should have equal IDs");
        assertNotEquals(user1.getId(), user3.getId(), "Users with different IDs should have different IDs");
    }

    @Test
    void testUserStateChanges_ReflectInGetters() {
        // ARRANGE
        User user = new User();
        String originalName = "Original Name";
        String updatedName = "Updated Name";

        // ACT - Set initial state
        user.setName(originalName);
        String firstName = user.getName();

        // ACT - Update state
        user.setName(updatedName);
        String secondName = user.getName();

        // ASSERT
        assertEquals(originalName, firstName, "First get should return original value");
        assertEquals(updatedName, secondName, "Second get should return updated value");
        assertNotEquals(firstName, secondName, "Values should be different after update");
    }
}
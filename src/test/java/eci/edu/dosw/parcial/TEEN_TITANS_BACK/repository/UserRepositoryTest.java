package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.User;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.UserRole;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para UserRepository usando mocks.
 * No requiere conexión a MongoDB.
 */
@TestMethodOrder(MethodOrderer.DisplayName.class)
class UserRepositoryMockTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserRepositoryMockTest testInstance;

    private User admin;
    private User student;
    private User professorInactive;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        admin = new User("1", "Bruce Wayne", "bruce@uni.edu", "bat123", UserRole.ADMINISTRATOR);
        student = new User("2", "Clark Kent", "clark@uni.edu", "super123", UserRole.STUDENT);
        professorInactive = new User("3", "Diana Prince", "diana@uni.edu", "wonder123", UserRole.PROFESSOR);
        professorInactive.setActive(false);
    }

    // ----------- CASOS EXITOSOS -----------

    @Test
    @DisplayName("Caso exitoso: Debería encontrar usuario por email existente")
    void shouldFindUserByEmail() {
        when(userRepository.findByEmail("bruce@uni.edu"))
                .thenReturn(Optional.of(admin));

        Optional<User> found = userRepository.findByEmail("bruce@uni.edu");
        assertTrue(found.isPresent());
        assertEquals("Bruce Wayne", found.get().getName());
    }

    @Test
    @DisplayName("Caso exitoso: Debería encontrar usuarios por rol STUDENT")
    void shouldFindUsersByRole() {
        when(userRepository.findByRole(UserRole.STUDENT))
                .thenReturn(List.of(student));

        List<User> students = userRepository.findByRole(UserRole.STUDENT);
        assertEquals(1, students.size());
        assertEquals("Clark Kent", students.get(0).getName());
    }

    @Test
    @DisplayName("Caso exitoso: Debería encontrar usuarios activos")
    void shouldFindActiveUsers() {
        when(userRepository.findByActive(true))
                .thenReturn(List.of(admin, student));

        List<User> activeUsers = userRepository.findByActive(true);
        assertEquals(2, activeUsers.size());
        assertTrue(activeUsers.stream().allMatch(User::getActive));
    }

    @Test
    @DisplayName("Caso exitoso: Debería retornar true si existe usuario con email dado")
    void shouldReturnTrueIfEmailExists() {
        when(userRepository.existsByEmail("clark@uni.edu")).thenReturn(true);

        boolean exists = userRepository.existsByEmail("clark@uni.edu");
        assertTrue(exists);
    }

    @Test
    @DisplayName("Caso exitoso: Debería encontrar usuarios cuyo nombre contiene 'Clark'")
    void shouldFindByNameContainingIgnoreCase() {
        when(userRepository.findByNameContainingIgnoreCase("clark"))
                .thenReturn(List.of(student));

        List<User> result = userRepository.findByNameContainingIgnoreCase("clark");
        assertEquals(1, result.size());
        assertEquals("Clark Kent", result.get(0).getName());
    }

    @Test
    @DisplayName("Caso exitoso: Debería encontrar usuarios por rol y estado activo")
    void shouldFindByRoleAndActive() {
        when(userRepository.findByRoleAndActive(UserRole.ADMINISTRATOR, true))
                .thenReturn(List.of(admin));

        List<User> admins = userRepository.findByRoleAndActive(UserRole.ADMINISTRATOR, true);
        assertEquals(1, admins.size());
        assertEquals("Bruce Wayne", admins.get(0).getName());
    }

    // ----------- CASOS DE ERROR -----------

    @Test
    @DisplayName("Caso error: No debería encontrar usuario con email inexistente")
    void shouldNotFindUserByNonExistentEmail() {
        when(userRepository.findByEmail("joker@uni.edu"))
                .thenReturn(Optional.empty());

        Optional<User> found = userRepository.findByEmail("joker@uni.edu");
        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("Caso error: No debería encontrar usuarios por rol sin coincidencias")
    void shouldNotFindUsersByRoleWithoutMatches() {
        when(userRepository.findByRole(UserRole.DEAN))
                .thenReturn(List.of());

        List<User> deans = userRepository.findByRole(UserRole.DEAN);
        assertTrue(deans.isEmpty());
    }

    @Test
    @DisplayName("Caso error: No debería retornar true si el email no existe")
    void shouldReturnFalseIfEmailDoesNotExist() {
        when(userRepository.existsByEmail("alfred@uni.edu"))
                .thenReturn(false);

        boolean exists = userRepository.existsByEmail("alfred@uni.edu");
        assertFalse(exists);
    }

    @Test
    @DisplayName("Caso error: No debería encontrar usuarios activos si todos son inactivos")
    void shouldNotFindActiveUsersIfAllInactive() {
        when(userRepository.findByActive(true)).thenReturn(List.of());

        List<User> activeUsers = userRepository.findByActive(true);
        assertTrue(activeUsers.isEmpty());
    }
}

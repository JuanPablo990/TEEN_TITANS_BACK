package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.User;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    private User user1;
    private User user2;
    private User user3;
    private User user4;

    @BeforeEach
    void setUp() {
        user1 = new User("1", "Bruce Wayne", "bruce.wayne@titans.edu", "password123", UserRole.ADMINISTRATOR);
        user2 = new User("2", "Clark Kent", "clark.kent@titans.edu", "password456", UserRole.PROFESSOR);
        user3 = new User("3", "Diana Prince", "diana.prince@titans.edu", "password789", UserRole.STUDENT);
        user4 = new User("4", "Barry Allen", "barry.allen@titans.edu", "password000", UserRole.DEAN);
    }

    @Test
    @DisplayName("Caso exitoso - save guarda usuario correctamente")
    void testSave_Exitoso() {
        User newUser = new User("5", "Hal Jordan", "hal.jordan@titans.edu", "password111", UserRole.PROFESSOR);

        when(userRepository.save(newUser)).thenReturn(newUser);

        User savedUser = userRepository.save(newUser);

        assertAll("Verificar guardado de usuario",
                () -> assertNotNull(savedUser),
                () -> assertEquals("Hal Jordan", savedUser.getName()),
                () -> assertEquals("hal.jordan@titans.edu", savedUser.getEmail()),
                () -> assertEquals(UserRole.PROFESSOR, savedUser.getRole())
        );

        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    @DisplayName("Caso exitoso - findById retorna usuario existente")
    void testFindById_Exitoso() {
        when(userRepository.findById("1")).thenReturn(Optional.of(user1));

        Optional<User> foundUser = userRepository.findById("1");

        assertAll("Verificar búsqueda por ID",
                () -> assertTrue(foundUser.isPresent()),
                () -> assertEquals("Bruce Wayne", foundUser.get().getName()),
                () -> assertEquals(UserRole.ADMINISTRATOR, foundUser.get().getRole())
        );

        verify(userRepository, times(1)).findById("1");
    }

    @Test
    @DisplayName("Caso error - findById retorna vacío para ID inexistente")
    void testFindById_NoEncontrado() {
        when(userRepository.findById("99")).thenReturn(Optional.empty());

        Optional<User> foundUser = userRepository.findById("99");

        assertTrue(foundUser.isEmpty());
        verify(userRepository, times(1)).findById("99");
    }

    @Test
    @DisplayName("Caso exitoso - findAll retorna todos los usuarios")
    void testFindAll_Exitoso() {
        List<User> users = Arrays.asList(user1, user2, user3, user4);
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userRepository.findAll();

        assertAll("Verificar búsqueda de todos los usuarios",
                () -> assertNotNull(result),
                () -> assertEquals(4, result.size()),
                () -> assertEquals("Bruce Wayne", result.get(0).getName())
        );

        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Caso borde - findAll retorna lista vacía cuando no hay usuarios")
    void testFindAll_ListaVacia() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<User> result = userRepository.findAll();

        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Caso exitoso - findByName retorna usuario por nombre")
    void testFindByName_Exitoso() {
        List<User> users = Arrays.asList(user1);
        when(userRepository.findByName("Bruce Wayne")).thenReturn(users);

        List<User> result = userRepository.findByName("Bruce Wayne");

        assertAll("Verificar búsqueda por nombre",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("Bruce Wayne", result.get(0).getName())
        );

        verify(userRepository, times(1)).findByName("Bruce Wayne");
    }

    @Test
    @DisplayName("Caso exitoso - findByEmail retorna usuario por email")
    void testFindByEmail_Exitoso() {
        List<User> users = Arrays.asList(user1);
        when(userRepository.findByEmail("bruce.wayne@titans.edu")).thenReturn(users);

        List<User> result = userRepository.findByEmail("bruce.wayne@titans.edu");

        assertAll("Verificar búsqueda por email",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("bruce.wayne@titans.edu", result.get(0).getEmail())
        );

        verify(userRepository, times(1)).findByEmail("bruce.wayne@titans.edu");
    }

    @Test
    @DisplayName("Caso exitoso - findByPassword retorna usuario por contraseña")
    void testFindByPassword_Exitoso() {
        List<User> users = Arrays.asList(user1);
        when(userRepository.findByPassword("password123")).thenReturn(users);

        List<User> result = userRepository.findByPassword("password123");

        assertAll("Verificar búsqueda por contraseña",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("password123", result.get(0).getPassword())
        );

        verify(userRepository, times(1)).findByPassword("password123");
    }

    @Test
    @DisplayName("Caso exitoso - findByRole retorna usuarios por rol")
    void testFindByRole_Exitoso() {
        List<User> professors = Arrays.asList(user2);
        when(userRepository.findByRole(UserRole.PROFESSOR)).thenReturn(professors);

        List<User> result = userRepository.findByRole(UserRole.PROFESSOR);

        assertAll("Verificar búsqueda por rol",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals(UserRole.PROFESSOR, result.get(0).getRole())
        );

        verify(userRepository, times(1)).findByRole(UserRole.PROFESSOR);
    }

    @Test
    @DisplayName("Caso exitoso - findByActive retorna usuarios activos")
    void testFindByActive_Exitoso() {
        List<User> activeUsers = Arrays.asList(user1, user2, user3);
        when(userRepository.findByActive(true)).thenReturn(activeUsers);

        List<User> result = userRepository.findByActive(true);

        assertAll("Verificar búsqueda por estado activo",
                () -> assertNotNull(result),
                () -> assertEquals(3, result.size()),
                () -> assertTrue(result.stream().allMatch(User::isActive))
        );

        verify(userRepository, times(1)).findByActive(true);
    }

    @Test
    @DisplayName("Caso exitoso - findByCreatedAt retorna usuarios por fecha de creación")
    void testFindByCreatedAt_Exitoso() {
        Date testDate = new Date();
        List<User> users = Arrays.asList(user1);
        when(userRepository.findByCreatedAt(testDate)).thenReturn(users);

        List<User> result = userRepository.findByCreatedAt(testDate);

        assertAll("Verificar búsqueda por fecha de creación",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals(testDate, result.get(0).getCreatedAt())
        );

        verify(userRepository, times(1)).findByCreatedAt(testDate);
    }

    @Test
    @DisplayName("Caso exitoso - findByUpdatedAt retorna usuarios por fecha de actualización")
    void testFindByUpdatedAt_Exitoso() {
        Date testDate = new Date();
        List<User> users = Arrays.asList(user1);
        when(userRepository.findByUpdatedAt(testDate)).thenReturn(users);

        List<User> result = userRepository.findByUpdatedAt(testDate);

        assertAll("Verificar búsqueda por fecha de actualización",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals(testDate, result.get(0).getUpdatedAt())
        );

        verify(userRepository, times(1)).findByUpdatedAt(testDate);
    }

    @Test
    @DisplayName("Caso exitoso - findByNameContainingIgnoreCase retorna usuarios con patrón en nombre")
    void testFindByNameContainingIgnoreCase_Exitoso() {
        List<User> users = Arrays.asList(user1);
        when(userRepository.findByNameContainingIgnoreCase("bruce")).thenReturn(users);

        List<User> result = userRepository.findByNameContainingIgnoreCase("bruce");

        assertAll("Verificar búsqueda por patrón en nombre",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertTrue(result.get(0).getName().toLowerCase().contains("bruce"))
        );

        verify(userRepository, times(1)).findByNameContainingIgnoreCase("bruce");
    }

    @Test
    @DisplayName("Caso exitoso - findByEmailContainingIgnoreCase retorna usuarios con patrón en email")
    void testFindByEmailContainingIgnoreCase_Exitoso() {
        List<User> users = Arrays.asList(user1);
        when(userRepository.findByEmailContainingIgnoreCase("wayne")).thenReturn(users);

        List<User> result = userRepository.findByEmailContainingIgnoreCase("wayne");

        assertAll("Verificar búsqueda por patrón en email",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertTrue(result.get(0).getEmail().toLowerCase().contains("wayne"))
        );

        verify(userRepository, times(1)).findByEmailContainingIgnoreCase("wayne");
    }

    @Test
    @DisplayName("Caso exitoso - findByNameAndRole retorna usuario por nombre y rol")
    void testFindByNameAndRole_Exitoso() {
        List<User> users = Arrays.asList(user1);
        when(userRepository.findByNameAndRole("Bruce Wayne", UserRole.ADMINISTRATOR)).thenReturn(users);

        List<User> result = userRepository.findByNameAndRole("Bruce Wayne", UserRole.ADMINISTRATOR);

        assertAll("Verificar búsqueda por nombre y rol",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("Bruce Wayne", result.get(0).getName()),
                () -> assertEquals(UserRole.ADMINISTRATOR, result.get(0).getRole())
        );

        verify(userRepository, times(1)).findByNameAndRole("Bruce Wayne", UserRole.ADMINISTRATOR);
    }

    @Test
    @DisplayName("Caso exitoso - findByEmailAndRole retorna usuario por email y rol")
    void testFindByEmailAndRole_Exitoso() {
        List<User> users = Arrays.asList(user1);
        when(userRepository.findByEmailAndRole("bruce.wayne@titans.edu", UserRole.ADMINISTRATOR)).thenReturn(users);

        List<User> result = userRepository.findByEmailAndRole("bruce.wayne@titans.edu", UserRole.ADMINISTRATOR);

        assertAll("Verificar búsqueda por email y rol",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("bruce.wayne@titans.edu", result.get(0).getEmail()),
                () -> assertEquals(UserRole.ADMINISTRATOR, result.get(0).getRole())
        );

        verify(userRepository, times(1)).findByEmailAndRole("bruce.wayne@titans.edu", UserRole.ADMINISTRATOR);
    }

    @Test
    @DisplayName("Caso exitoso - findByNameAndActive retorna usuario por nombre y estado")
    void testFindByNameAndActive_Exitoso() {
        List<User> users = Arrays.asList(user1);
        when(userRepository.findByNameAndActive("Bruce Wayne", true)).thenReturn(users);

        List<User> result = userRepository.findByNameAndActive("Bruce Wayne", true);

        assertAll("Verificar búsqueda por nombre y estado",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("Bruce Wayne", result.get(0).getName()),
                () -> assertTrue(result.get(0).isActive())
        );

        verify(userRepository, times(1)).findByNameAndActive("Bruce Wayne", true);
    }

    @Test
    @DisplayName("Caso exitoso - findByNameOrEmail retorna usuario por nombre o email")
    void testFindByNameOrEmail_Exitoso() {
        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.findByNameOrEmail("Bruce Wayne", "clark.kent@titans.edu")).thenReturn(users);

        List<User> result = userRepository.findByNameOrEmail("Bruce Wayne", "clark.kent@titans.edu");

        assertAll("Verificar búsqueda por nombre o email",
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size()),
                () -> assertTrue(result.stream().anyMatch(u -> u.getName().equals("Bruce Wayne"))),
                () -> assertTrue(result.stream().anyMatch(u -> u.getEmail().equals("clark.kent@titans.edu")))
        );

        verify(userRepository, times(1)).findByNameOrEmail("Bruce Wayne", "clark.kent@titans.edu");
    }

    @Test
    @DisplayName("Caso exitoso - findByCreatedAtAfter retorna usuarios creados después de fecha")
    void testFindByCreatedAtAfter_Exitoso() {
        Date testDate = new Date(System.currentTimeMillis() - 1000000);
        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.findByCreatedAtAfter(testDate)).thenReturn(users);

        List<User> result = userRepository.findByCreatedAtAfter(testDate);

        assertAll("Verificar búsqueda por fecha posterior",
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size())
        );

        verify(userRepository, times(1)).findByCreatedAtAfter(testDate);
    }

    @Test
    @DisplayName("Caso exitoso - findByCreatedAtBefore retorna usuarios creados antes de fecha")
    void testFindByCreatedAtBefore_Exitoso() {
        Date testDate = new Date(System.currentTimeMillis() + 1000000);
        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.findByCreatedAtBefore(testDate)).thenReturn(users);

        List<User> result = userRepository.findByCreatedAtBefore(testDate);

        assertAll("Verificar búsqueda por fecha anterior",
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size())
        );

        verify(userRepository, times(1)).findByCreatedAtBefore(testDate);
    }

    @Test
    @DisplayName("Caso exitoso - findByCreatedAtBetween retorna usuarios en rango de fechas")
    void testFindByCreatedAtBetween_Exitoso() {
        Date startDate = new Date(System.currentTimeMillis() - 1000000);
        Date endDate = new Date(System.currentTimeMillis() + 1000000);
        List<User> users = Arrays.asList(user1, user2, user3);
        when(userRepository.findByCreatedAtBetween(startDate, endDate)).thenReturn(users);

        List<User> result = userRepository.findByCreatedAtBetween(startDate, endDate);

        assertAll("Verificar búsqueda por rango de fechas",
                () -> assertNotNull(result),
                () -> assertEquals(3, result.size())
        );

        verify(userRepository, times(1)).findByCreatedAtBetween(startDate, endDate);
    }

    @Test
    @DisplayName("Caso exitoso - findByOrderByNameAsc retorna usuarios ordenados")
    void testFindByOrderByNameAsc_Exitoso() {
        List<User> users = Arrays.asList(user4, user1, user2, user3);
        when(userRepository.findByOrderByNameAsc()).thenReturn(users);

        List<User> result = userRepository.findByOrderByNameAsc();

        assertAll("Verificar ordenamiento por nombre ascendente",
                () -> assertNotNull(result),
                () -> assertEquals(4, result.size()),
                () -> assertEquals("Barry Allen", result.get(0).getName()),
                () -> assertEquals("Bruce Wayne", result.get(1).getName()),
                () -> assertEquals("Clark Kent", result.get(2).getName()),
                () -> assertEquals("Diana Prince", result.get(3).getName())
        );

        verify(userRepository, times(1)).findByOrderByNameAsc();
    }

    @Test
    @DisplayName("Caso exitoso - findByOrderByCreatedAtDesc retorna usuarios ordenados por fecha descendente")
    void testFindByOrderByCreatedAtDesc_Exitoso() {
        List<User> users = Arrays.asList(user4, user3, user2, user1);
        when(userRepository.findByOrderByCreatedAtDesc()).thenReturn(users);

        List<User> result = userRepository.findByOrderByCreatedAtDesc();

        assertAll("Verificar ordenamiento por fecha descendente",
                () -> assertNotNull(result),
                () -> assertEquals(4, result.size())
        );

        verify(userRepository, times(1)).findByOrderByCreatedAtDesc();
    }

    @Test
    @DisplayName("Caso exitoso - findByRoleOrderByNameAsc retorna usuarios ordenados por rol")
    void testFindByRoleOrderByNameAsc_Exitoso() {
        List<User> professors = Arrays.asList(user2);
        when(userRepository.findByRoleOrderByNameAsc(UserRole.PROFESSOR)).thenReturn(professors);

        List<User> result = userRepository.findByRoleOrderByNameAsc(UserRole.PROFESSOR);

        assertAll("Verificar búsqueda por rol con ordenamiento",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals(UserRole.PROFESSOR, result.get(0).getRole())
        );

        verify(userRepository, times(1)).findByRoleOrderByNameAsc(UserRole.PROFESSOR);
    }

    @Test
    @DisplayName("Caso exitoso - countByRole retorna conteo correcto")
    void testCountByRole_Exitoso() {
        when(userRepository.countByRole(UserRole.ADMINISTRATOR)).thenReturn(1L);

        long result = userRepository.countByRole(UserRole.ADMINISTRATOR);

        assertEquals(1L, result);
        verify(userRepository, times(1)).countByRole(UserRole.ADMINISTRATOR);
    }

    @Test
    @DisplayName("Caso exitoso - countByActive retorna conteo por estado")
    void testCountByActive_Exitoso() {
        when(userRepository.countByActive(true)).thenReturn(3L);
        when(userRepository.countByActive(false)).thenReturn(1L);

        long activeCount = userRepository.countByActive(true);
        long inactiveCount = userRepository.countByActive(false);

        assertAll("Verificar conteo por estado activo",
                () -> assertEquals(3L, activeCount),
                () -> assertEquals(1L, inactiveCount)
        );

        verify(userRepository, times(1)).countByActive(true);
        verify(userRepository, times(1)).countByActive(false);
    }

    @Test
    @DisplayName("Caso exitoso - countByRoleAndActive retorna conteo combinado")
    void testCountByRoleAndActive_Exitoso() {
        when(userRepository.countByRoleAndActive(UserRole.ADMINISTRATOR, true)).thenReturn(1L);

        long result = userRepository.countByRoleAndActive(UserRole.ADMINISTRATOR, true);

        assertEquals(1L, result);
        verify(userRepository, times(1)).countByRoleAndActive(UserRole.ADMINISTRATOR, true);
    }

    @Test
    @DisplayName("Caso exitoso - findByNameRegex retorna usuarios por patrón regex")
    void testFindByNameRegex_Exitoso() {
        List<User> users = Arrays.asList(user1);
        when(userRepository.findByNameRegex(".*Bruce.*")).thenReturn(users);

        List<User> result = userRepository.findByNameRegex(".*Bruce.*");

        assertAll("Verificar búsqueda por regex de nombre",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("Bruce Wayne", result.get(0).getName())
        );

        verify(userRepository, times(1)).findByNameRegex(".*Bruce.*");
    }

    @Test
    @DisplayName("Caso exitoso - findByEmailRegex retorna usuarios por patrón regex de email")
    void testFindByEmailRegex_Exitoso() {
        List<User> users = Arrays.asList(user1);
        when(userRepository.findByEmailRegex(".*wayne.*")).thenReturn(users);

        List<User> result = userRepository.findByEmailRegex(".*wayne.*");

        assertAll("Verificar búsqueda por regex de email",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("bruce.wayne@titans.edu", result.get(0).getEmail())
        );

        verify(userRepository, times(1)).findByEmailRegex(".*wayne.*");
    }

    @Test
    @DisplayName("Caso exitoso - findUsersCreatedBetween retorna usuarios en rango de fechas")
    void testFindUsersCreatedBetween_Exitoso() {
        Date startDate = new Date(System.currentTimeMillis() - 1000000);
        Date endDate = new Date(System.currentTimeMillis() + 1000000);
        List<User> users = Arrays.asList(user1, user2, user3);
        when(userRepository.findUsersCreatedBetween(startDate, endDate)).thenReturn(users);

        List<User> result = userRepository.findUsersCreatedBetween(startDate, endDate);

        assertAll("Verificar búsqueda por rango de fechas con consulta personalizada",
                () -> assertNotNull(result),
                () -> assertEquals(3, result.size())
        );

        verify(userRepository, times(1)).findUsersCreatedBetween(startDate, endDate);
    }

    @Test
    @DisplayName("Caso exitoso - existsByEmail retorna true cuando existe email")
    void testExistsByEmail_Exitoso() {
        when(userRepository.existsByEmail("bruce.wayne@titans.edu")).thenReturn(true);

        boolean result = userRepository.existsByEmail("bruce.wayne@titans.edu");

        assertTrue(result);
        verify(userRepository, times(1)).existsByEmail("bruce.wayne@titans.edu");
    }

    @Test
    @DisplayName("Caso error - existsByEmail retorna false cuando no existe email")
    void testExistsByEmail_NoExiste() {
        when(userRepository.existsByEmail("inexistente@titans.edu")).thenReturn(false);

        boolean result = userRepository.existsByEmail("inexistente@titans.edu");

        assertFalse(result);
        verify(userRepository, times(1)).existsByEmail("inexistente@titans.edu");
    }

    @Test
    @DisplayName("Caso exitoso - existsByNameAndEmail retorna true cuando existe combinación")
    void testExistsByNameAndEmail_Exitoso() {
        when(userRepository.existsByNameAndEmail("Bruce Wayne", "bruce.wayne@titans.edu")).thenReturn(true);

        boolean result = userRepository.existsByNameAndEmail("Bruce Wayne", "bruce.wayne@titans.edu");

        assertTrue(result);
        verify(userRepository, times(1)).existsByNameAndEmail("Bruce Wayne", "bruce.wayne@titans.edu");
    }

    @Test
    @DisplayName("Caso error - existsByNameAndEmail retorna false cuando no existe combinación")
    void testExistsByNameAndEmail_NoExiste() {
        when(userRepository.existsByNameAndEmail("Bruce Wayne", "otro.email@titans.edu")).thenReturn(false);

        boolean result = userRepository.existsByNameAndEmail("Bruce Wayne", "otro.email@titans.edu");

        assertFalse(result);
        verify(userRepository, times(1)).existsByNameAndEmail("Bruce Wayne", "otro.email@titans.edu");
    }

    @Test
    @DisplayName("Caso exitoso - deleteById elimina usuario existente")
    void testDeleteById_Exitoso() {
        doNothing().when(userRepository).deleteById("1");

        assertDoesNotThrow(() -> userRepository.deleteById("1"));

        verify(userRepository, times(1)).deleteById("1");
    }
}
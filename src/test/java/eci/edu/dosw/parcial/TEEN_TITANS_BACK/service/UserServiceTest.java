package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.User;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.UserRepository;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() {
        user1 = new User("1", "Bruce Wayne", "bruce.wayne@titans.edu", "password123", UserRole.ADMINISTRATOR);
        user1.setActive(true);

        user2 = new User("2", "Clark Kent", "clark.kent@titans.edu", "password456", UserRole.PROFESSOR);
        user2.setActive(true);

        user3 = new User("3", "Diana Prince", "diana.prince@titans.edu", "password789", UserRole.STUDENT);
        user3.setActive(false);
    }

    @Test
    @DisplayName("Caso exitoso - createUser crea usuario correctamente")
    void testCreateUser_Exitoso() {
        when(userRepository.existsByEmail(user1.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user1);

        User resultado = userService.createUser(user1);

        assertAll("Verificar creación de usuario",
                () -> assertNotNull(resultado),
                () -> assertEquals("Bruce Wayne", resultado.getName()),
                () -> assertEquals("bruce.wayne@titans.edu", resultado.getEmail()),
                () -> assertEquals(UserRole.ADMINISTRATOR, resultado.getRole()),
                () -> assertTrue(resultado.isActive())
        );

        verify(userRepository, times(1)).existsByEmail(user1.getEmail());
        verify(userRepository, times(1)).save(user1);
    }

    @Test
    @DisplayName("Caso error - createUser lanza excepción cuando email ya existe")
    void testCreateUser_EmailExistente() {
        when(userRepository.existsByEmail(user1.getEmail())).thenReturn(true);

        AppException exception = assertThrows(AppException.class,
                () -> userService.createUser(user1));

        assertEquals("El email ya está registrado: " + user1.getEmail(), exception.getMessage());

        verify(userRepository, never()).save(any(User.class));
        verify(userRepository, times(1)).existsByEmail(user1.getEmail());
    }

    @Test
    @DisplayName("Caso exitoso - getUserById retorna usuario existente")
    void testGetUserById_Exitoso() {
        when(userRepository.findById("1")).thenReturn(Optional.of(user1));

        Optional<User> resultado = userService.getUserById("1");

        assertAll("Verificar usuario encontrado",
                () -> assertTrue(resultado.isPresent()),
                () -> assertEquals("1", resultado.get().getId()),
                () -> assertEquals("Bruce Wayne", resultado.get().getName()),
                () -> assertEquals(UserRole.ADMINISTRATOR, resultado.get().getRole())
        );

        verify(userRepository, times(1)).findById("1");
    }

    @Test
    @DisplayName("Caso error - getUserById retorna Optional vacío cuando ID no existe")
    void testGetUserById_NoEncontrado() {
        when(userRepository.findById("99")).thenReturn(Optional.empty());

        Optional<User> resultado = userService.getUserById("99");

        assertTrue(resultado.isEmpty());
        verify(userRepository, times(1)).findById("99");
    }

    @Test
    @DisplayName("Caso exitoso - getAllUsers retorna todos los usuarios")
    void testGetAllUsers_Exitoso() {
        List<User> usuarios = Arrays.asList(user1, user2, user3);
        when(userRepository.findAll()).thenReturn(usuarios);

        List<User> resultado = userService.getAllUsers();

        assertAll("Verificar lista de usuarios",
                () -> assertNotNull(resultado),
                () -> assertEquals(3, resultado.size()),
                () -> assertEquals("Bruce Wayne", resultado.get(0).getName()),
                () -> assertEquals("Clark Kent", resultado.get(1).getName()),
                () -> assertEquals("Diana Prince", resultado.get(2).getName())
        );

        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Caso borde - getAllUsers retorna lista vacía cuando no hay usuarios")
    void testGetAllUsers_ListaVacia() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<User> resultado = userService.getAllUsers();

        assertTrue(resultado.isEmpty());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Caso exitoso - updateUser actualiza usuario correctamente")
    void testUpdateUser_Exitoso() {
        when(userRepository.existsById("1")).thenReturn(true);
        when(userRepository.findByEmail(user1.getEmail())).thenReturn(Arrays.asList(user1));
        when(userRepository.save(any(User.class))).thenReturn(user1);

        User usuarioActualizado = new User("1", "Bruce Wayne Updated",
                "bruce.wayne@titans.edu", "newpassword",
                UserRole.ADMINISTRATOR);
        usuarioActualizado.setActive(true);

        User resultado = userService.updateUser("1", usuarioActualizado);

        assertAll("Verificar actualización de usuario",
                () -> assertNotNull(resultado),
                () -> assertEquals("1", resultado.getId())
        );

        verify(userRepository, times(1)).existsById("1");
        verify(userRepository, times(1)).findByEmail(user1.getEmail());
        verify(userRepository, times(1)).save(usuarioActualizado);
    }

    @Test
    @DisplayName("Caso error - updateUser lanza excepción cuando ID no existe")
    void testUpdateUser_NoEncontrado() {
        when(userRepository.existsById("99")).thenReturn(false);

        AppException exception = assertThrows(AppException.class,
                () -> userService.updateUser("99", user1));

        assertEquals("Usuario no encontrado con ID: 99", exception.getMessage());

        verify(userRepository, times(1)).existsById("99");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Caso error - updateUser lanza excepción cuando email ya está en uso")
    void testUpdateUser_EmailEnUso() {
        when(userRepository.existsById("1")).thenReturn(true);
        when(userRepository.findByEmail("nuevo.email@titans.edu")).thenReturn(Arrays.asList(user2));

        User usuarioActualizado = new User("1", "Bruce Wayne",
                "nuevo.email@titans.edu", "password123",
                UserRole.ADMINISTRATOR);

        AppException exception = assertThrows(AppException.class,
                () -> userService.updateUser("1", usuarioActualizado));

        assertEquals("El email ya está en uso por otro usuario: nuevo.email@titans.edu", exception.getMessage());

        verify(userRepository, times(1)).existsById("1");
        verify(userRepository, times(1)).findByEmail("nuevo.email@titans.edu");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Caso exitoso - deleteUser elimina usuario existente")
    void testDeleteUser_Exitoso() {
        when(userRepository.existsById("1")).thenReturn(true);
        doNothing().when(userRepository).deleteById("1");

        assertDoesNotThrow(() -> userService.deleteUser("1"));

        verify(userRepository, times(1)).existsById("1");
        verify(userRepository, times(1)).deleteById("1");
    }

    @Test
    @DisplayName("Caso error - deleteUser lanza excepción cuando ID no existe")
    void testDeleteUser_NoEncontrado() {
        when(userRepository.existsById("99")).thenReturn(false);

        AppException exception = assertThrows(AppException.class,
                () -> userService.deleteUser("99"));

        assertEquals("Usuario no encontrado con ID: 99", exception.getMessage());

        verify(userRepository, times(1)).existsById("99");
        verify(userRepository, never()).deleteById(anyString());
    }

    @Test
    @DisplayName("Caso exitoso - findByEmail retorna usuario cuando existe")
    void testFindByEmail_Exitoso() {
        when(userRepository.findByEmail("bruce.wayne@titans.edu")).thenReturn(Arrays.asList(user1));

        Optional<User> resultado = userService.findByEmail("bruce.wayne@titans.edu");

        assertAll("Verificar usuario por email",
                () -> assertTrue(resultado.isPresent()),
                () -> assertEquals("Bruce Wayne", resultado.get().getName()),
                () -> assertEquals("bruce.wayne@titans.edu", resultado.get().getEmail())
        );

        verify(userRepository, times(1)).findByEmail("bruce.wayne@titans.edu");
    }

    @Test
    @DisplayName("Caso error - findByEmail retorna Optional vacío cuando no existe")
    void testFindByEmail_NoEncontrado() {
        when(userRepository.findByEmail("inexistente@titans.edu")).thenReturn(Collections.emptyList());

        Optional<User> resultado = userService.findByEmail("inexistente@titans.edu");

        assertTrue(resultado.isEmpty());
        verify(userRepository, times(1)).findByEmail("inexistente@titans.edu");
    }

    @Test
    @DisplayName("Caso exitoso - findByRole retorna usuarios por rol")
    void testFindByRole_Exitoso() {
        List<User> administradores = Arrays.asList(user1);
        when(userRepository.findByRole(UserRole.ADMINISTRATOR)).thenReturn(administradores);

        List<User> resultado = userService.findByRole(UserRole.ADMINISTRATOR);

        assertAll("Verificar usuarios por rol",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals(UserRole.ADMINISTRATOR, resultado.get(0).getRole()),
                () -> assertEquals("Bruce Wayne", resultado.get(0).getName())
        );

        verify(userRepository, times(1)).findByRole(UserRole.ADMINISTRATOR);
    }

    @Test
    @DisplayName("Caso exitoso - activateUser activa usuario correctamente")
    void testActivateUser_Exitoso() {
        when(userRepository.findById("3")).thenReturn(Optional.of(user3));
        when(userRepository.save(any(User.class))).thenReturn(user3);

        User resultado = userService.activateUser("3");

        assertAll("Verificar activación de usuario",
                () -> assertNotNull(resultado),
                () -> assertTrue(resultado.isActive())
        );

        verify(userRepository, times(1)).findById("3");
        verify(userRepository, times(1)).save(user3);
    }

    @Test
    @DisplayName("Caso error - activateUser lanza excepción cuando ID no existe")
    void testActivateUser_NoEncontrado() {
        when(userRepository.findById("99")).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class,
                () -> userService.activateUser("99"));

        assertEquals("Usuario no encontrado con ID: 99", exception.getMessage());

        verify(userRepository, times(1)).findById("99");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Caso exitoso - deactivateUser desactiva usuario correctamente")
    void testDeactivateUser_Exitoso() {
        when(userRepository.findById("1")).thenReturn(Optional.of(user1));
        when(userRepository.save(any(User.class))).thenReturn(user1);

        User resultado = userService.deactivateUser("1");

        assertAll("Verificar desactivación de usuario",
                () -> assertNotNull(resultado),
                () -> assertFalse(resultado.isActive())
        );

        verify(userRepository, times(1)).findById("1");
        verify(userRepository, times(1)).save(user1);
    }

    @Test
    @DisplayName("Caso exitoso - existsByEmail retorna true cuando email existe")
    void testExistsByEmail_Exitoso() {
        when(userRepository.existsByEmail("bruce.wayne@titans.edu")).thenReturn(true);

        boolean resultado = userService.existsByEmail("bruce.wayne@titans.edu");

        assertTrue(resultado);
        verify(userRepository, times(1)).existsByEmail("bruce.wayne@titans.edu");
    }

    @Test
    @DisplayName("Caso error - existsByEmail retorna false cuando email no existe")
    void testExistsByEmail_NoExiste() {
        when(userRepository.existsByEmail("inexistente@titans.edu")).thenReturn(false);

        boolean resultado = userService.existsByEmail("inexistente@titans.edu");

        assertFalse(resultado);
        verify(userRepository, times(1)).existsByEmail("inexistente@titans.edu");
    }

    @Test
    @DisplayName("Caso exitoso - findByNameContaining retorna usuarios por nombre")
    void testFindByNameContaining_Exitoso() {
        List<User> usuarios = Arrays.asList(user1);
        when(userRepository.findByNameContainingIgnoreCase("Bruce")).thenReturn(usuarios);

        List<User> resultado = userService.findByNameContaining("Bruce");

        assertAll("Verificar usuarios por nombre",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("Bruce Wayne", resultado.get(0).getName())
        );

        verify(userRepository, times(1)).findByNameContainingIgnoreCase("Bruce");
    }

    @Test
    @DisplayName("Caso exitoso - findByActive retorna usuarios activos")
    void testFindByActive_Exitoso() {
        List<User> usuariosActivos = Arrays.asList(user1, user2);
        when(userRepository.findByActive(true)).thenReturn(usuariosActivos);

        List<User> resultado = userService.findByActive(true);

        assertAll("Verificar usuarios activos",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertTrue(resultado.stream().allMatch(User::isActive))
        );

        verify(userRepository, times(1)).findByActive(true);
    }

    @Test
    @DisplayName("Caso exitoso - countByRole retorna conteo correcto")
    void testCountByRole_Exitoso() {
        when(userRepository.countByRole(UserRole.ADMINISTRATOR)).thenReturn(1L);

        long resultado = userService.countByRole(UserRole.ADMINISTRATOR);

        assertEquals(1L, resultado);
        verify(userRepository, times(1)).countByRole(UserRole.ADMINISTRATOR);
    }

    @Test
    @DisplayName("Caso borde - findByRole retorna lista vacía cuando no hay usuarios del rol")
    void testFindByRole_ListaVacia() {
        when(userRepository.findByRole(UserRole.DEAN)).thenReturn(Collections.emptyList());

        List<User> resultado = userService.findByRole(UserRole.DEAN);

        assertTrue(resultado.isEmpty());
        verify(userRepository, times(1)).findByRole(UserRole.DEAN);
    }

    @Test
    @DisplayName("Caso borde - countByRole retorna cero cuando no hay usuarios del rol")
    void testCountByRole_Cero() {
        when(userRepository.countByRole(UserRole.DEAN)).thenReturn(0L);

        long resultado = userService.countByRole(UserRole.DEAN);

        assertEquals(0L, resultado);
        verify(userRepository, times(1)).countByRole(UserRole.DEAN);
    }

    @Test
    @DisplayName("Caso borde - findByActive retorna lista vacía cuando no hay usuarios activos")
    void testFindByActive_ListaVacia() {
        when(userRepository.findByActive(false)).thenReturn(Collections.emptyList());

        List<User> resultado = userService.findByActive(false);

        assertTrue(resultado.isEmpty());
        verify(userRepository, times(1)).findByActive(false);
    }

    @Test
    @DisplayName("Caso borde - findByNameContaining retorna lista vacía cuando no hay coincidencias")
    void testFindByNameContaining_ListaVacia() {
        when(userRepository.findByNameContainingIgnoreCase("Inexistente")).thenReturn(Collections.emptyList());

        List<User> resultado = userService.findByNameContaining("Inexistente");

        assertTrue(resultado.isEmpty());
        verify(userRepository, times(1)).findByNameContainingIgnoreCase("Inexistente");
    }
}
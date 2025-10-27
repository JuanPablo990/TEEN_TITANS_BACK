package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto.UserDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.User;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.UserService;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User user1;
    private User user2;
    private UserDTO userDTO1;
    private UserDTO userDTO2;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId("1");
        user1.setName("Usuario A");
        user1.setEmail("usuarioA@titans.edu");
        user1.setRole(UserRole.STUDENT);
        user1.setActive(true);

        user2 = new User();
        user2.setId("2");
        user2.setName("Usuario B");
        user2.setEmail("usuarioB@titans.edu");
        user2.setRole(UserRole.PROFESSOR);
        user2.setActive(false);

        userDTO1 = new UserDTO();
        userDTO1.setId("1");
        userDTO1.setName("Usuario A");
        userDTO1.setEmail("usuarioA@titans.edu");
        userDTO1.setRole("STUDENT");
        userDTO1.setActive(true);

        userDTO2 = new UserDTO();
        userDTO2.setId("2");
        userDTO2.setName("Usuario B");
        userDTO2.setEmail("usuarioB@titans.edu");
        userDTO2.setRole("PROFESSOR");
        userDTO2.setActive(false);
    }

    @Test
    @DisplayName("Caso exitoso - createUser crea usuario correctamente")
    void testCreateUser_Exitoso() {
        when(userService.createUser(any(User.class))).thenReturn(user1);

        ResponseEntity<?> response = userController.createUser(userDTO1);

        assertAll("Verificar respuesta de creación exitosa",
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(UserDTO.class, response.getBody()),
                () -> assertEquals("Usuario A", ((UserDTO) response.getBody()).getName())
        );

        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    @DisplayName("Caso error - createUser retorna error cuando AppException ocurre")
    void testCreateUser_ErrorAppException() {
        when(userService.createUser(any(User.class)))
                .thenThrow(new AppException("Error al crear usuario"));

        ResponseEntity<?> response = userController.createUser(userDTO1);

        assertAll("Verificar respuesta de error por AppException",
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> assertTrue(((Map<?, ?>) response.getBody()).containsKey("error"))
        );

        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    @DisplayName("Caso exitoso - getUserById retorna usuario existente")
    void testGetUserById_Exitoso() {
        when(userService.getUserById("1")).thenReturn(Optional.of(user1));

        ResponseEntity<?> response = userController.getUserById("1");

        assertAll("Verificar obtención exitosa de usuario",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(UserDTO.class, response.getBody()),
                () -> assertEquals("1", ((UserDTO) response.getBody()).getId()),
                () -> assertEquals("Usuario A", ((UserDTO) response.getBody()).getName())
        );

        verify(userService, times(1)).getUserById("1");
    }

    @Test
    @DisplayName("Caso error - getUserById retorna 404 cuando usuario no existe")
    void testGetUserById_NoEncontrado() {
        when(userService.getUserById("99")).thenReturn(Optional.empty());

        ResponseEntity<?> response = userController.getUserById("99");

        assertAll("Verificar respuesta 404",
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertTrue(((Map<?, ?>) response.getBody()).containsKey("error"))
        );

        verify(userService, times(1)).getUserById("99");
    }

    @Test
    @DisplayName("Caso exitoso - getAllUsers retorna lista de usuarios")
    void testGetAllUsers_Exitoso() {
        List<User> users = Arrays.asList(user1, user2);
        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<?> response = userController.getAllUsers();

        assertAll("Verificar obtención exitosa de todos los usuarios",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> assertTrue(((Map<?, ?>) response.getBody()).containsKey("users")),
                () -> assertTrue(((Map<?, ?>) response.getBody()).containsKey("count")),
                () -> assertEquals(2, ((Map<?, ?>) response.getBody()).get("count"))
        );

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("Caso error - getAllUsers retorna error interno")
    void testGetAllUsers_ErrorInterno() {
        when(userService.getAllUsers()).thenThrow(new RuntimeException("Error de base de datos"));

        ResponseEntity<?> response = userController.getAllUsers();

        assertAll("Verificar error interno del servidor",
                () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertTrue(((Map<?, ?>) response.getBody()).containsKey("error"))
        );

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("Caso exitoso - updateUser actualiza usuario correctamente")
    void testUpdateUser_Exitoso() {
        when(userService.updateUser(eq("1"), any(User.class))).thenReturn(user1);

        ResponseEntity<?> response = userController.updateUser("1", userDTO1);

        assertAll("Verificar actualización exitosa",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(UserDTO.class, response.getBody())
        );

        verify(userService, times(1)).updateUser(eq("1"), any(User.class));
    }

    @Test
    @DisplayName("Caso error - updateUser retorna error cuando usuario no existe")
    void testUpdateUser_NoEncontrado() {
        when(userService.updateUser(eq("99"), any(User.class)))
                .thenThrow(new AppException("Usuario no encontrado"));

        ResponseEntity<?> response = userController.updateUser("99", userDTO1);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(userService, times(1)).updateUser(eq("99"), any(User.class));
    }

    @Test
    @DisplayName("Caso exitoso - deleteUser elimina usuario correctamente")
    void testDeleteUser_Exitoso() {
        doNothing().when(userService).deleteUser("1");

        ResponseEntity<?> response = userController.deleteUser("1");

        assertAll("Verificar eliminación exitosa",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> assertEquals("1", ((Map<?, ?>) response.getBody()).get("deletedId"))
        );

        verify(userService, times(1)).deleteUser("1");
    }

    @Test
    @DisplayName("Caso error - deleteUser retorna 404 cuando usuario no existe")
    void testDeleteUser_NoEncontrado() {
        doThrow(new AppException("Usuario no encontrado"))
                .when(userService).deleteUser("99");

        ResponseEntity<?> response = userController.deleteUser("99");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService, times(1)).deleteUser("99");
    }

    @Test
    @DisplayName("Caso exitoso - getUserByEmail retorna usuario por email")
    void testGetUserByEmail_Exitoso() {
        when(userService.findByEmail("usuarioA@titans.edu")).thenReturn(Optional.of(user1));

        ResponseEntity<?> response = userController.getUserByEmail("usuarioA@titans.edu");

        assertAll("Verificar búsqueda por email exitosa",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(UserDTO.class, response.getBody()),
                () -> assertEquals("usuarioA@titans.edu", ((UserDTO) response.getBody()).getEmail())
        );

        verify(userService, times(1)).findByEmail("usuarioA@titans.edu");
    }

    @Test
    @DisplayName("Caso error - getUserByEmail retorna 404 cuando email no existe")
    void testGetUserByEmail_NoEncontrado() {
        when(userService.findByEmail("inexistente@titans.edu")).thenReturn(Optional.empty());

        ResponseEntity<?> response = userController.getUserByEmail("inexistente@titans.edu");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService, times(1)).findByEmail("inexistente@titans.edu");
    }

    @Test
    @DisplayName("Caso exitoso - getUsersByRole retorna usuarios por rol")
    void testGetUsersByRole_Exitoso() {
        List<User> users = Arrays.asList(user1);
        when(userService.findByRole(UserRole.STUDENT)).thenReturn(users);

        ResponseEntity<?> response = userController.getUsersByRole("student");

        assertAll("Verificar búsqueda por rol exitosa",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> assertEquals("student", ((Map<?, ?>) response.getBody()).get("role")),
                () -> assertEquals(1, ((Map<?, ?>) response.getBody()).get("count"))
        );

        verify(userService, times(1)).findByRole(UserRole.STUDENT);
    }

    @Test
    @DisplayName("Caso error - getUsersByRole retorna error por rol inválido")
    void testGetUsersByRole_RolInvalido() {
        ResponseEntity<?> response = userController.getUsersByRole("rol_invalido");

        assertAll("Verificar error por rol inválido",
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertTrue(((Map<?, ?>) response.getBody()).containsKey("error"))
        );
    }




    @Test
    @DisplayName("Caso exitoso - getUserStatistics retorna estadísticas")
    void testGetUserStatistics_Exitoso() {
        List<User> allUsers = Arrays.asList(user1, user2);
        when(userService.getAllUsers()).thenReturn(allUsers);

        ResponseEntity<?> response = userController.getUserStatistics();

        assertAll("Verificar obtención de estadísticas",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertTrue(((Map<?, ?>) response.getBody()).containsKey("totalUsers")),
                () -> assertTrue(((Map<?, ?>) response.getBody()).containsKey("activeUsers")),
                () -> assertTrue(((Map<?, ?>) response.getBody()).containsKey("usersByRole"))
        );

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("Caso exitoso - searchUsers retorna usuarios filtrados")
    void testSearchUsers_Exitoso() {
        List<User> allUsers = Arrays.asList(user1, user2);
        when(userService.getAllUsers()).thenReturn(allUsers);

        ResponseEntity<?> response = userController.searchUsers("STUDENT", true);

        assertAll("Verificar búsqueda con filtros",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertTrue(((Map<?, ?>) response.getBody()).containsKey("searchCriteria")),
                () -> assertTrue(((Map<?, ?>) response.getBody()).containsKey("users"))
        );

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("Caso exitoso - checkEmailExists retorna false para email disponible")
    void testCheckEmailExists_EmailDisponible() {
        when(userService.existsByEmail("nuevo@titans.edu")).thenReturn(false);

        ResponseEntity<?> response = userController.checkEmailExists("nuevo@titans.edu");

        assertAll("Verificar email disponible",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals(false, ((Map<?, ?>) response.getBody()).get("exists"))
        );

        verify(userService, times(1)).existsByEmail("nuevo@titans.edu");
    }

    @Test
    @DisplayName("Caso exitoso - checkEmailExists retorna true para email existente")
    void testCheckEmailExists_EmailExistente() {
        when(userService.existsByEmail("usuarioA@titans.edu")).thenReturn(true);

        ResponseEntity<?> response = userController.checkEmailExists("usuarioA@titans.edu");

        assertAll("Verificar email existente",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(true, ((Map<?, ?>) response.getBody()).get("exists"))
        );

        verify(userService, times(1)).existsByEmail("usuarioA@titans.edu");
    }

    @Test
    @DisplayName("Caso borde - getAllUsers retorna lista vacía cuando no hay usuarios")
    void testGetAllUsers_ListaVacia() {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = userController.getAllUsers();

        assertAll("Verificar lista vacía de usuarios",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(0, ((Map<?, ?>) response.getBody()).get("count"))
        );

        verify(userService, times(1)).getAllUsers();
    }





    @Test
    @DisplayName("Caso error - activateUser retorna 404 cuando usuario no existe")
    void testActivateUser_NoEncontrado() {
        when(userService.activateUser("99"))
                .thenThrow(new AppException("Usuario no encontrado"));

        ResponseEntity<?> response = userController.activateUser("99");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService, times(1)).activateUser("99");
    }

    @Test
    @DisplayName("Caso error - deactivateUser retorna 404 cuando usuario no existe")
    void testDeactivateUser_NoEncontrado() {
        when(userService.deactivateUser("99"))
                .thenThrow(new AppException("Usuario no encontrado"));

        ResponseEntity<?> response = userController.deactivateUser("99");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService, times(1)).deactivateUser("99");
    }
}
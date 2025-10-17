package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto.UserDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.User;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.UserRole;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;
    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();

        // Setup test data
        user = new User();
        user.setId("1");
        user.setName("John Doe");
        user.setEmail("john.doe@university.edu");
        user.setPassword("password123");
        user.setRole(UserRole.STUDENT);
        user.setActive(true);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());

        userDTO = new UserDTO();
        userDTO.setId("1");
        userDTO.setName("John Doe");
        userDTO.setEmail("john.doe@university.edu");
        userDTO.setRole("STUDENT");
        userDTO.setActive(true);
        userDTO.setCreatedAt(new Date());
        userDTO.setUpdatedAt(new Date());
    }

    @Test
    void createUser_Success() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@university.edu"))
                .andExpect(jsonPath("$.password").doesNotExist()); // Password no debe estar en la respuesta

        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    void createUser_AppException() throws Exception {
        when(userService.createUser(any(User.class))).thenThrow(new AppException("Error creating user"));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Error creating user"));
    }

    @Test
    void getUserById_Success() throws Exception {
        when(userService.getUserById("1")).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@university.edu"))
                .andExpect(jsonPath("$.password").doesNotExist()); // Password no debe estar en la respuesta

        verify(userService, times(1)).getUserById("1");
    }

    @Test
    void getUserById_NotFound() throws Exception {
        when(userService.getUserById("1")).thenReturn(null);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Usuario no encontrado: 1"));
    }

    @Test
    void getAllUsers_Success() throws Exception {
        List<User> users = Arrays.asList(user);
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users").isArray())
                .andExpect(jsonPath("$.users[0].name").value("John Doe"))
                .andExpect(jsonPath("$.users[0].password").doesNotExist()) // Password no debe estar en la respuesta
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void updateUser_Success() throws Exception {
        when(userService.getUserById("1")).thenReturn(user);
        when(userService.updateUser(anyString(), any(User.class))).thenReturn(user);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.password").doesNotExist()); // Password no debe estar en la respuesta

        verify(userService, times(1)).updateUser(anyString(), any(User.class));
    }

    @Test
    void updateUser_NotFound() throws Exception {
        when(userService.getUserById("1")).thenReturn(null);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Usuario no encontrado: 1"));
    }

    @Test
    void deleteUser_Success() throws Exception {
        doNothing().when(userService).deleteUser("1");

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Usuario eliminado exitosamente"))
                .andExpect(jsonPath("$.deletedId").value("1"))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(userService, times(1)).deleteUser("1");
    }

    @Test
    void deleteUser_AppException() throws Exception {
        doThrow(new AppException("User not found")).when(userService).deleteUser("1");

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User not found"));
    }

    @Test
    void getUserByEmail_Success() throws Exception {
        when(userService.findByEmail("john.doe@university.edu")).thenReturn(user);

        mockMvc.perform(get("/api/users/email/john.doe@university.edu"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john.doe@university.edu"))
                .andExpect(jsonPath("$.password").doesNotExist()); // Password no debe estar en la respuesta

        verify(userService, times(1)).findByEmail("john.doe@university.edu");
    }

    @Test
    void getUserByEmail_NotFound() throws Exception {
        when(userService.findByEmail("nonexistent@email.com")).thenReturn(null);

        mockMvc.perform(get("/api/users/email/nonexistent@email.com"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Usuario no encontrado con email: nonexistent@email.com"));
    }

    @Test
    void getUsersByRole_Success() throws Exception {
        List<User> users = Arrays.asList(user);
        when(userService.findByRole(UserRole.STUDENT)).thenReturn(users);

        mockMvc.perform(get("/api/users/role/student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("student"))
                .andExpect(jsonPath("$.users").isArray())
                .andExpect(jsonPath("$.users[0].password").doesNotExist()) // Password no debe estar en la respuesta
                .andExpect(jsonPath("$.count").value(1));

        verify(userService, times(1)).findByRole(UserRole.STUDENT);
    }

    @Test
    void getUsersByRole_InvalidRole() throws Exception {
        mockMvc.perform(get("/api/users/role/invalid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Rol inválido: invalid"));
    }

    @Test
    void activateUser_Success() throws Exception {
        User activatedUser = user;
        activatedUser.setActive(true);
        when(userService.activateUser("1")).thenReturn(activatedUser);

        mockMvc.perform(put("/api/users/1/activate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Usuario activado exitosamente"))
                .andExpect(jsonPath("$.user.active").value(true))
                .andExpect(jsonPath("$.user.password").doesNotExist()); // Password no debe estar en la respuesta

        verify(userService, times(1)).activateUser("1");
    }

    @Test
    void deactivateUser_Success() throws Exception {
        User deactivatedUser = user;
        deactivatedUser.setActive(false);
        when(userService.deactivateUser("1")).thenReturn(deactivatedUser);

        mockMvc.perform(put("/api/users/1/deactivate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Usuario desactivado exitosamente"))
                .andExpect(jsonPath("$.user.active").value(false))
                .andExpect(jsonPath("$.user.password").doesNotExist()); // Password no debe estar en la respuesta

        verify(userService, times(1)).deactivateUser("1");
    }

    @Test
    void getUserStatistics_Success() throws Exception {
        List<User> users = Arrays.asList(user);
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalUsers").value(1))
                .andExpect(jsonPath("$.activeUsers").value(1))
                .andExpect(jsonPath("$.inactiveUsers").value(0))
                .andExpect(jsonPath("$.usersByRole.STUDENT").value(1))
                .andExpect(jsonPath("$.activePercentage").exists());

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void searchUsers_Success() throws Exception {
        List<User> users = Arrays.asList(user);
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users/search")
                        .param("role", "STUDENT")
                        .param("active", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.searchCriteria.role").value("STUDENT"))
                .andExpect(jsonPath("$.searchCriteria.active").value(true))
                .andExpect(jsonPath("$.users[0].password").doesNotExist()) // Password no debe estar en la respuesta
                .andExpect(jsonPath("$.count").value(1));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void checkEmailExists_Exists() throws Exception {
        when(userService.findByEmail("john.doe@university.edu")).thenReturn(user);

        mockMvc.perform(get("/api/users/check-email/john.doe@university.edu"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john.doe@university.edu"))
                .andExpect(jsonPath("$.exists").value(true))
                .andExpect(jsonPath("$.message").value("El email ya está registrado"));

        verify(userService, times(1)).findByEmail("john.doe@university.edu");
    }

    @Test
    void checkEmailExists_NotExists() throws Exception {
        when(userService.findByEmail("new@email.com")).thenReturn(null);

        mockMvc.perform(get("/api/users/check-email/new@email.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exists").value(false))
                .andExpect(jsonPath("$.message").value("El email está disponible"));

        verify(userService, times(1)).findByEmail("new@email.com");
    }

    // Test adicional para verificar que el password no se expone en ninguna respuesta
    @Test
    void passwordNotExposedInAnyResponse() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(user);
        when(userService.getUserById("1")).thenReturn(user);
        when(userService.getAllUsers()).thenReturn(Arrays.asList(user));

        // Verificar en create
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(jsonPath("$.password").doesNotExist());

        // Verificar en get by id
        mockMvc.perform(get("/api/users/1"))
                .andExpect(jsonPath("$.password").doesNotExist());

        // Verificar en get all
        mockMvc.perform(get("/api/users"))
                .andExpect(jsonPath("$.users[0].password").doesNotExist());
    }

    // Test para verificar el manejo de roles por defecto
    @Test
    void createUser_WithDefaultRole() throws Exception {
        UserDTO userWithoutRole = new UserDTO();
        userWithoutRole.setName("Test User");
        userWithoutRole.setEmail("test@university.edu");
        userWithoutRole.setPassword("password");
        // No establecer role explícitamente

        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userWithoutRole)))
                .andExpect(status().isCreated());

        verify(userService, times(1)).createUser(any(User.class));
    }
}
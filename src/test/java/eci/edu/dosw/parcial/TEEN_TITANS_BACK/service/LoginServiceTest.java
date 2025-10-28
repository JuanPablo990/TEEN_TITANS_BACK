package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto.LoginDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto.LoginResponseDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private AdministratorRepository administratorRepository;

    @Mock
    private DeanRepository deanRepository;

    @InjectMocks
    private LoginService loginService;

    private Student activeStudent;
    private Student inactiveStudent;
    private Professor professor;
    private Administrator administrator;
    private Dean dean;
    private User user;

    @BeforeEach
    void setUp() {
        activeStudent = new Student("1", "Juan Pérez", "juan@test.com", "password123", "Ingeniería", 5);
        inactiveStudent = new Student("2", "María García", "maria@test.com", "password456", "Medicina", 3);
        inactiveStudent.setActive(false);

        professor = new Professor("3", "Carlos Ruiz", "profesor@test.com", "prof123",
                "Matemáticas", true, Arrays.asList("Algebra", "Cálculo"));

        administrator = new Administrator("4", "Admin User", "admin@test.com", "admin123", "TI");

        dean = new Dean("5", "Dean User", "dean@test.com", "dean123", "Ingeniería", "Oficina 101");

        user = new User("6", "General User", "user@test.com", "user123", UserRole.STUDENT);
    }

    @Test
    void authenticate_SuccessfulStudentLogin() {
        LoginDTO loginDTO = new LoginDTO("juan@test.com", "password123", UserRole.STUDENT);

        when(studentRepository.findByEmail("juan@test.com"))
                .thenReturn(Arrays.asList(activeStudent));

        LoginResponseDTO response = loginService.authenticate(loginDTO);

        assertNotNull(response);
        assertEquals("1", response.getId());
        assertEquals("Juan Pérez", response.getName());
        assertEquals("juan@test.com", response.getEmail());
        assertEquals(UserRole.STUDENT, response.getRole());
        assertTrue(response.isSuccess());
        assertEquals("Login exitoso", response.getMessage());
    }

    @Test
    void authenticate_SuccessfulProfessorLoginWithStringRole() {
        LoginDTO loginDTO = new LoginDTO("profesor@test.com", "prof123", "PROFESSOR");

        when(professorRepository.findByEmail("profesor@test.com"))
                .thenReturn(Arrays.asList(professor));

        LoginResponseDTO response = loginService.authenticate(loginDTO);

        assertNotNull(response);
        assertEquals("3", response.getId());
        assertEquals("Carlos Ruiz", response.getName());
        assertEquals("profesor@test.com", response.getEmail());
        assertEquals(UserRole.PROFESSOR, response.getRole());
        assertTrue(response.isSuccess());
        assertEquals("Login exitoso", response.getMessage());
    }

    @Test
    void authenticate_SuccessfulAdministratorLogin() {
        LoginDTO loginDTO = new LoginDTO("admin@test.com", "admin123", UserRole.ADMINISTRATOR);

        when(administratorRepository.findByEmail("admin@test.com"))
                .thenReturn(Arrays.asList(administrator));

        LoginResponseDTO response = loginService.authenticate(loginDTO);

        assertNotNull(response);
        assertEquals("4", response.getId());
        assertEquals("Admin User", response.getName());
        assertEquals("admin@test.com", response.getEmail());
        assertEquals(UserRole.ADMINISTRATOR, response.getRole());
        assertTrue(response.isSuccess());
        assertEquals("Login exitoso", response.getMessage());
    }

    @Test
    void authenticate_SuccessfulDeanLogin() {
        LoginDTO loginDTO = new LoginDTO("dean@test.com", "dean123", UserRole.DEAN);

        when(deanRepository.findByEmail("dean@test.com"))
                .thenReturn(Arrays.asList(dean));

        LoginResponseDTO response = loginService.authenticate(loginDTO);

        assertNotNull(response);
        assertEquals("5", response.getId());
        assertEquals("Dean User", response.getName());
        assertEquals("dean@test.com", response.getEmail());
        assertEquals(UserRole.DEAN, response.getRole());
        assertTrue(response.isSuccess());
        assertEquals("Login exitoso", response.getMessage());
    }

    @Test
    void authenticate_SuccessfulUserLogin() {
        LoginDTO loginDTO = new LoginDTO("user@test.com", "user123", UserRole.STUDENT);

        when(userRepository.findByEmail("user@test.com"))
                .thenReturn(Arrays.asList(user));

        LoginResponseDTO response = loginService.authenticate(loginDTO);

        assertNotNull(response);
        assertEquals("6", response.getId());
        assertEquals("General User", response.getName());
        assertEquals("user@test.com", response.getEmail());
        assertEquals(UserRole.STUDENT, response.getRole());
        assertTrue(response.isSuccess());
        assertEquals("Login exitoso", response.getMessage());
    }

    @Test
    void authenticate_UserNotFound() {
        LoginDTO loginDTO = new LoginDTO("nonexistent@test.com", "password", UserRole.STUDENT);

        when(studentRepository.findByEmail("nonexistent@test.com")).thenReturn(Collections.emptyList());
        when(professorRepository.findByEmail("nonexistent@test.com")).thenReturn(Collections.emptyList());
        when(administratorRepository.findByEmail("nonexistent@test.com")).thenReturn(Collections.emptyList());
        when(deanRepository.findByEmail("nonexistent@test.com")).thenReturn(Collections.emptyList());
        when(userRepository.findByEmail("nonexistent@test.com")).thenReturn(Collections.emptyList());

        AppException exception = assertThrows(AppException.class, () -> {
            loginService.authenticate(loginDTO);
        });

        assertEquals("No se encontró usuario con el email: nonexistent@test.com", exception.getMessage());
    }

    @Test
    void authenticate_WrongPassword_ThrowsAppException() {
        LoginDTO loginDTO = new LoginDTO("juan@test.com", "wrongpassword", UserRole.STUDENT);

        when(studentRepository.findByEmail("juan@test.com"))
                .thenReturn(Arrays.asList(activeStudent));

        AppException exception = assertThrows(AppException.class, () -> {
            loginService.authenticate(loginDTO);
        });

        assertEquals("Contraseña incorrecta", exception.getMessage());
    }

    @Test
    void authenticate_InactiveUser_ThrowsAppException() {
        LoginDTO loginDTO = new LoginDTO("maria@test.com", "password456", UserRole.STUDENT);

        when(studentRepository.findByEmail("maria@test.com"))
                .thenReturn(Arrays.asList(inactiveStudent));

        AppException exception = assertThrows(AppException.class, () -> {
            loginService.authenticate(loginDTO);
        });

        assertEquals("El usuario está inactivo. Contacte al administrador", exception.getMessage());
    }

    @Test
    void authenticate_RoleMismatch_ThrowsAppException() {
        LoginDTO loginDTO = new LoginDTO("juan@test.com", "password123", UserRole.PROFESSOR);

        when(studentRepository.findByEmail("juan@test.com"))
                .thenReturn(Arrays.asList(activeStudent));

        AppException exception = assertThrows(AppException.class, () -> {
            loginService.authenticate(loginDTO);
        });

        assertTrue(exception.getMessage().contains("El usuario no tiene el rol especificado"));
    }

    @Test
    void authenticate_NullEmail_ThrowsAppException() {
        LoginDTO loginDTO = new LoginDTO(null, "password123", UserRole.STUDENT);

        AppException exception = assertThrows(AppException.class, () -> {
            loginService.authenticate(loginDTO);
        });

        assertEquals("El email es requerido", exception.getMessage());
    }

    @Test
    void authenticate_EmptyEmail_ThrowsAppException() {
        LoginDTO loginDTO = new LoginDTO("", "password123", UserRole.STUDENT);

        AppException exception = assertThrows(AppException.class, () -> {
            loginService.authenticate(loginDTO);
        });

        assertEquals("El email es requerido", exception.getMessage());
    }

    @Test
    void authenticate_NullPassword_ThrowsAppException() {
        LoginDTO loginDTO = new LoginDTO("test@test.com", null, UserRole.STUDENT);

        AppException exception = assertThrows(AppException.class, () -> {
            loginService.authenticate(loginDTO);
        });

        assertEquals("La contraseña es requerida", exception.getMessage());
    }

    @Test
    void authenticate_EmptyPassword_ThrowsAppException() {
        LoginDTO loginDTO = new LoginDTO("test@test.com", "", UserRole.STUDENT);

        AppException exception = assertThrows(AppException.class, () -> {
            loginService.authenticate(loginDTO);
        });

        assertEquals("La contraseña es requerida", exception.getMessage());
    }

    @Test
    void authenticate_NullRole_ThrowsAppException() {
        LoginDTO loginDTO = new LoginDTO("test@test.com", "password123", null);

        AppException exception = assertThrows(AppException.class, () -> {
            loginService.authenticate(loginDTO);
        });

        assertEquals("El rol es requerido", exception.getMessage());
    }

    @Test
    void authenticate_WithCaseInsensitiveRoleString() {
        LoginDTO loginDTO = new LoginDTO("profesor@test.com", "prof123", "professor");

        when(professorRepository.findByEmail("profesor@test.com"))
                .thenReturn(Arrays.asList(professor));

        LoginResponseDTO response = loginService.authenticate(loginDTO);

        assertNotNull(response);
        assertEquals(UserRole.PROFESSOR, response.getRole());
        assertTrue(response.isSuccess());
    }


    @Test
    void emailExists_StudentExists() {
        when(studentRepository.existsByEmail("test@test.com")).thenReturn(true);

        boolean result = loginService.emailExists("test@test.com");

        assertTrue(result);
    }

    @Test
    void emailExists_ProfessorExists() {
        when(professorRepository.existsByEmail("test@test.com")).thenReturn(true);

        boolean result = loginService.emailExists("test@test.com");

        assertTrue(result);
    }

    @Test
    void emailExists_AdministratorExists() {
        when(administratorRepository.existsByEmail("test@test.com")).thenReturn(true);

        boolean result = loginService.emailExists("test@test.com");

        assertTrue(result);
    }

    @Test
    void emailExists_DeanExists() {
        when(deanRepository.existsByEmail("test@test.com")).thenReturn(true);

        boolean result = loginService.emailExists("test@test.com");

        assertTrue(result);
    }

    @Test
    void emailExists_UserExists() {
        when(userRepository.existsByEmail("test@test.com")).thenReturn(true);

        boolean result = loginService.emailExists("test@test.com");

        assertTrue(result);
    }

    @Test
    void emailExists_NotFound() {
        when(studentRepository.existsByEmail("nonexistent@test.com")).thenReturn(false);
        when(professorRepository.existsByEmail("nonexistent@test.com")).thenReturn(false);
        when(administratorRepository.existsByEmail("nonexistent@test.com")).thenReturn(false);
        when(deanRepository.existsByEmail("nonexistent@test.com")).thenReturn(false);
        when(userRepository.existsByEmail("nonexistent@test.com")).thenReturn(false);

        boolean result = loginService.emailExists("nonexistent@test.com");

        assertFalse(result);
    }

    @Test
    void emailExistsForRole_ExistsAndMatches() {
        when(studentRepository.findByEmail("test@test.com")).thenReturn(Arrays.asList(activeStudent));

        boolean result = loginService.emailExistsForRole("test@test.com", UserRole.STUDENT);

        assertTrue(result);
    }

    @Test
    void emailExistsForRole_ExistsButRoleMismatch() {
        when(studentRepository.findByEmail("test@test.com")).thenReturn(Arrays.asList(activeStudent));

        boolean result = loginService.emailExistsForRole("test@test.com", UserRole.PROFESSOR);

        assertFalse(result);
    }

    @Test
    void emailExistsForRole_NotFound() {
        when(studentRepository.findByEmail("nonexistent@test.com")).thenReturn(Collections.emptyList());
        when(professorRepository.findByEmail("nonexistent@test.com")).thenReturn(Collections.emptyList());
        when(administratorRepository.findByEmail("nonexistent@test.com")).thenReturn(Collections.emptyList());
        when(deanRepository.findByEmail("nonexistent@test.com")).thenReturn(Collections.emptyList());
        when(userRepository.findByEmail("nonexistent@test.com")).thenReturn(Collections.emptyList());

        boolean result = loginService.emailExistsForRole("nonexistent@test.com", UserRole.STUDENT);

        assertFalse(result);
    }

    @Test
    void emailExistsForRole_WithStringRole() {
        when(studentRepository.findByEmail("test@test.com")).thenReturn(Arrays.asList(activeStudent));

        boolean result = loginService.emailExistsForRole("test@test.com", "STUDENT");

        assertTrue(result);
    }

    @Test
    void emailExistsForRole_WithCaseInsensitiveStringRole() {
        when(studentRepository.findByEmail("test@test.com")).thenReturn(Arrays.asList(activeStudent));

        boolean result = loginService.emailExistsForRole("test@test.com", "student");

        assertTrue(result);
    }

    @Test
    void getUserById_Found() {
        when(userRepository.findById("1")).thenReturn(Optional.of(activeStudent));

        Optional<User> result = loginService.getUserById("1");

        assertTrue(result.isPresent());
        assertEquals("1", result.get().getId());
    }

    @Test
    void getUserById_NotFound() {
        when(userRepository.findById("999")).thenReturn(Optional.empty());

        Optional<User> result = loginService.getUserById("999");

        assertFalse(result.isPresent());
    }

    @Test
    void debugUserInfo_UserFound() {
        when(studentRepository.findByEmail("juan@test.com")).thenReturn(Arrays.asList(activeStudent));

        String result = loginService.debugUserInfo("juan@test.com", UserRole.STUDENT);

        assertNotNull(result);
        assertTrue(result.contains("juan@test.com"));
        assertTrue(result.contains("Student"));
    }

    @Test
    void debugUserInfo_UserNotFound() {
        when(studentRepository.findByEmail("nonexistent@test.com")).thenReturn(Collections.emptyList());
        when(professorRepository.findByEmail("nonexistent@test.com")).thenReturn(Collections.emptyList());
        when(administratorRepository.findByEmail("nonexistent@test.com")).thenReturn(Collections.emptyList());
        when(deanRepository.findByEmail("nonexistent@test.com")).thenReturn(Collections.emptyList());
        when(userRepository.findByEmail("nonexistent@test.com")).thenReturn(Collections.emptyList());

        String result = loginService.debugUserInfo("nonexistent@test.com", UserRole.STUDENT);

        assertTrue(result.contains("No se encontró usuario con email: nonexistent@test.com"));
    }

    @Test
    void debugUserInfo_RoleParseError() {
        when(studentRepository.findByEmail("juan@test.com")).thenReturn(Arrays.asList(activeStudent));

        String result = loginService.debugUserInfo("juan@test.com", "INVALID_ROLE");

        assertTrue(result.contains("Error parseando rol"));
    }

    @Test
    void debugUserInfo_NullRole() {
        when(studentRepository.findByEmail("juan@test.com")).thenReturn(Arrays.asList(activeStudent));

        String result = loginService.debugUserInfo("juan@test.com", null);

        assertNotNull(result);
        assertTrue(result.contains("juan@test.com"));
        assertTrue(result.contains("Sin rol específico"));
    }

    @Test
    void authenticate_UserWithNullRole_ThrowsAppException() {
        Student studentWithNullRole = new Student("7", "Test Student", "nullrole@test.com", "password", "Program", 1);
        studentWithNullRole.setRole(null);

        when(studentRepository.findByEmail("nullrole@test.com")).thenReturn(Arrays.asList(studentWithNullRole));

        LoginDTO loginDTO = new LoginDTO("nullrole@test.com", "password", UserRole.STUDENT);

        AppException exception = assertThrows(AppException.class, () -> {
            loginService.authenticate(loginDTO);
        });

        assertTrue(exception.getMessage().contains("El usuario no tiene el rol especificado"));
    }
}
package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto.LoginDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto.LoginResponseDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.User;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Student;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Professor;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Administrator;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Dean;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.UserRepository;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.StudentRepository;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.ProfessorRepository;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.AdministratorRepository;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.DeanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para manejar el proceso de autenticación de usuarios
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Service
public class LoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private AdministratorRepository administratorRepository;

    @Autowired
    private DeanRepository deanRepository;

    /**
     * Autentica un usuario basado en email, password y rol
     *
     * @param loginDTO DTO con las credenciales de login
     * @return Respuesta del login con información del usuario
     * @throws AppException si las credenciales son inválidas o el usuario no tiene el rol especificado
     */
    public LoginResponseDTO authenticate(LoginDTO loginDTO) {
        String email = loginDTO.getEmail();
        String password = loginDTO.getPassword();
        Object roleObj = loginDTO.getRole();

        // Validar campos obligatorios
        if (email == null || email.trim().isEmpty()) {
            throw new AppException("El email es requerido");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new AppException("La contraseña es requerida");
        }

        if (roleObj == null) {
            throw new AppException("El rol es requerido");
        }

        // Convertir el rol a UserRole de manera flexible
        UserRole requestedRole = parseUserRole(roleObj);

        // Buscar usuario en TODAS las colecciones sin importar el rol
        User user = findUserByEmailInAnyCollection(email);

        // Si no encontramos usuario
        if (user == null) {
            throw new AppException("No se encontró usuario con el email: " + email);
        }

        // DEBUG: Información del usuario encontrado
        System.out.println("DEBUG - Usuario encontrado:");
        System.out.println("  Email: " + user.getEmail());
        System.out.println("  Rol: " + user.getRole());
        System.out.println("  Tipo de usuario: " + user.getClass().getSimpleName());

        // Validar contraseña
        if (!user.getPassword().equals(password)) {
            throw new AppException("Contraseña incorrecta");
        }

        // Validar que el usuario esté activo
        if (!user.isActive()) {
            throw new AppException("El usuario está inactivo. Contacte al administrador");
        }

        // Validar que el rol coincida - COMPARACIÓN MUY FLEXIBLE
        if (!rolesMatch(user.getRole(), requestedRole)) {
            String userRoleStr = user.getRole() != null ? user.getRole().name() : "NULL";
            throw new AppException("El usuario no tiene el rol especificado. Rol del usuario: " +
                    userRoleStr + ", Rol solicitado: " + requestedRole.name());
        }

        return createSuccessResponse(user);
    }

    /**
     * Busca usuario en TODAS las colecciones
     */
    private User findUserByEmailInAnyCollection(String email) {
        // Buscar en estudiantes
        List<Student> students = studentRepository.findByEmail(email);
        if (!students.isEmpty()) {
            return students.get(0);
        }

        // Buscar en profesores
        List<Professor> professors = professorRepository.findByEmail(email);
        if (!professors.isEmpty()) {
            return professors.get(0);
        }

        // Buscar en administradores
        List<Administrator> administrators = administratorRepository.findByEmail(email);
        if (!administrators.isEmpty()) {
            return administrators.get(0);
        }

        // Buscar en decanos
        List<Dean> deans = deanRepository.findByEmail(email);
        if (!deans.isEmpty()) {
            return deans.get(0);
        }

        // Buscar en usuarios generales
        List<User> users = userRepository.findByEmail(email);
        if (!users.isEmpty()) {
            return users.get(0);
        }

        return null;
    }

    /**
     * Convierte flexiblemente cualquier objeto a UserRole
     */
    private UserRole parseUserRole(Object roleObj) {
        if (roleObj instanceof UserRole) {
            return (UserRole) roleObj;
        }

        if (roleObj instanceof String) {
            String roleStr = ((String) roleObj).toUpperCase().trim();
            try {
                return UserRole.valueOf(roleStr);
            } catch (IllegalArgumentException e) {
                // Intentar con coincidencias parciales
                for (UserRole userRole : UserRole.values()) {
                    if (userRole.name().equalsIgnoreCase(roleStr) ||
                            userRole.name().toLowerCase().contains(roleStr.toLowerCase()) ||
                            roleStr.toUpperCase().contains(userRole.name())) {
                        return userRole;
                    }
                }
                throw new AppException("Rol no válido: " + roleStr + ". Roles válidos: STUDENT, PROFESSOR, ADMINISTRATOR, DEAN");
            }
        }

        throw new AppException("Formato de rol no válido. Debe ser string o UserRole");
    }

    /**
     * Compara roles de manera flexible - MANEJA NULLS
     */
    private boolean rolesMatch(UserRole userRole, UserRole requestedRole) {
        // Si ambos son null, coinciden
        if (userRole == null && requestedRole == null) {
            return true;
        }

        // Si solo uno es null, no coinciden
        if (userRole == null || requestedRole == null) {
            return false;
        }

        // Comparación normal
        return userRole == requestedRole ||
                userRole.name().equalsIgnoreCase(requestedRole.name());
    }

    /**
     * Crea una respuesta exitosa de login
     */
    private LoginResponseDTO createSuccessResponse(User user) {
        return new LoginResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                true,
                "Login exitoso"
        );
    }

    /**
     * Verifica si un email existe en el sistema para un rol específico
     */
    public boolean emailExistsForRole(String email, Object roleObj) {
        try {
            User user = findUserByEmailInAnyCollection(email);
            if (user == null) {
                return false;
            }

            UserRole requestedRole = parseUserRole(roleObj);
            return rolesMatch(user.getRole(), requestedRole);
        } catch (AppException e) {
            return false;
        }
    }

    /**
     * Verifica si un email existe en cualquier rol del sistema
     */
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email) ||
                studentRepository.existsByEmail(email) ||
                professorRepository.existsByEmail(email) ||
                administratorRepository.existsByEmail(email) ||
                deanRepository.existsByEmail(email);
    }

    /**
     * Obtiene usuario por ID (para verificación posterior al login)
     */
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    /**
     * Método de debug para verificar la información del usuario
     */
    public String debugUserInfo(String email, Object roleObj) {
        try {
            User user = findUserByEmailInAnyCollection(email);
            if (user == null) {
                return "No se encontró usuario con email: " + email;
            }

            String roleInfo = "Sin rol específico";
            if (roleObj != null) {
                try {
                    UserRole requestedRole = parseUserRole(roleObj);
                    roleInfo = "Rol solicitado: " + requestedRole + ", Coincide: " + rolesMatch(user.getRole(), requestedRole);
                } catch (Exception e) {
                    roleInfo = "Error parseando rol: " + e.getMessage();
                }
            }

            return String.format(
                    "Usuario encontrado - Tipo: %s, Email: %s, Rol: %s, Activo: %s, Contraseña: %s | %s",
                    user.getClass().getSimpleName(),
                    user.getEmail(),
                    user.getRole(),
                    user.isActive(),
                    user.getPassword(),
                    roleInfo
            );
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto.UserDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.User;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador REST que gestiona las operaciones CRUD y consultas avanzadas
 * relacionadas con los usuarios del sistema. Proporciona endpoints para
 * crear, actualizar, eliminar, buscar y filtrar usuarios, así como estadísticas
 * generales y verificación de correo electrónico.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025-10
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    /**
     * Constructor del controlador que inyecta la dependencia del servicio de usuarios.
     *
     * @param userService servicio que maneja la lógica de negocio relacionada con usuarios.
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Crea un nuevo usuario en el sistema.
     *
     * @param userDTO datos del usuario a registrar.
     * @return respuesta HTTP con el usuario creado o un mensaje de error.
     */
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        try {
            User user = convertToUser(userDTO);
            User createdUser = userService.createUser(user);
            UserDTO responseDTO = convertToUserDTO(createdUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al crear el usuario: " + e.getMessage()));
        }
    }

    /**
     * Obtiene un usuario por su identificador único.
     *
     * @param id identificador del usuario.
     * @return usuario correspondiente o mensaje de error si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        try {
            User user = userService.getUserById(id)
                    .orElseThrow(() -> new AppException("Usuario no encontrado con ID: " + id));
            UserDTO userDTO = convertToUserDTO(user);
            return ResponseEntity.ok(userDTO);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener el usuario"));
        }
    }

    /**
     * Obtiene la lista completa de usuarios registrados.
     *
     * @return lista de usuarios, su cantidad y marca temporal.
     */
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            List<UserDTO> userDTOs = users.stream()
                    .map(this::convertToUserDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "users", userDTOs,
                    "count", userDTOs.size(),
                    "timestamp", new Date()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener la lista de usuarios"));
        }
    }

    /**
     * Actualiza la información de un usuario existente.
     *
     * @param id identificador del usuario a actualizar.
     * @param userDTO datos actualizados del usuario.
     * @return usuario actualizado o mensaje de error.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody UserDTO userDTO) {
        try {
            User user = convertToUser(userDTO);
            User updatedUser = userService.updateUser(id, user);
            UserDTO responseDTO = convertToUserDTO(updatedUser);
            return ResponseEntity.ok(responseDTO);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al actualizar el usuario"));
        }
    }

    /**
     * Elimina un usuario por su identificador.
     *
     * @param id identificador del usuario a eliminar.
     * @return mensaje de confirmación o error.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(Map.of(
                    "message", "Usuario eliminado exitosamente",
                    "deletedId", id,
                    "timestamp", new Date()
            ));
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al eliminar el usuario"));
        }
    }

    /**
     * Busca un usuario por su dirección de correo electrónico.
     *
     * @param email correo electrónico del usuario.
     * @return usuario correspondiente o mensaje de error.
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        try {
            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new AppException("Usuario no encontrado con email: " + email));
            UserDTO userDTO = convertToUserDTO(user);
            return ResponseEntity.ok(userDTO);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al buscar usuario por email"));
        }
    }

    /**
     * Obtiene los usuarios filtrados por rol.
     *
     * @param role nombre del rol a filtrar.
     * @return lista de usuarios con el rol especificado.
     */
    @GetMapping("/role/{role}")
    public ResponseEntity<?> getUsersByRole(@PathVariable String role) {
        try {
            UserRole userRole = UserRole.valueOf(role.toUpperCase());
            List<User> users = userService.findByRole(userRole);
            List<UserDTO> userDTOs = users.stream()
                    .map(this::convertToUserDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "role", role,
                    "users", userDTOs,
                    "count", userDTOs.size()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Rol inválido: " + role));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al buscar usuarios por rol"));
        }
    }

    /**
     * Activa un usuario inactivo.
     *
     * @param id identificador del usuario a activar.
     * @return usuario activado o mensaje de error.
     */
    @PutMapping("/{id}/activate")
    public ResponseEntity<?> activateUser(@PathVariable String id) {
        try {
            User activatedUser = userService.activateUser(id);
            UserDTO userDTO = convertToUserDTO(activatedUser);
            return ResponseEntity.ok(Map.of(
                    "message", "Usuario activado exitosamente",
                    "user", userDTO
            ));
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al activar el usuario"));
        }
    }

    /**
     * Desactiva un usuario activo.
     *
     * @param id identificador del usuario a desactivar.
     * @return usuario desactivado o mensaje de error.
     */
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateUser(@PathVariable String id) {
        try {
            User deactivatedUser = userService.deactivateUser(id);
            UserDTO userDTO = convertToUserDTO(deactivatedUser);
            return ResponseEntity.ok(Map.of(
                    "message", "Usuario desactivado exitosamente",
                    "user", userDTO
            ));
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al desactivar el usuario"));
        }
    }

    /**
     * Obtiene estadísticas generales sobre los usuarios registrados.
     *
     * @return mapa con totales, conteo por rol, y porcentaje de usuarios activos.
     */
    @GetMapping("/statistics")
    public ResponseEntity<?> getUserStatistics() {
        try {
            List<User> allUsers = userService.getAllUsers();
            long activeCount = allUsers.stream().filter(User::isActive).count();
            long inactiveCount = allUsers.size() - activeCount;

            Map<UserRole, Long> usersByRole = allUsers.stream()
                    .collect(Collectors.groupingBy(User::getRole, Collectors.counting()));

            return ResponseEntity.ok(Map.of(
                    "totalUsers", allUsers.size(),
                    "activeUsers", activeCount,
                    "inactiveUsers", inactiveCount,
                    "usersByRole", usersByRole,
                    "activePercentage", allUsers.isEmpty() ?
                            0 : Math.round((double) activeCount / allUsers.size() * 100 * 100.0) / 100.0
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener estadísticas de usuarios"));
        }
    }

    /**
     * Busca usuarios mediante filtros opcionales de rol y estado.
     *
     * @param role   rol a filtrar (opcional).
     * @param active estado activo o inactivo (opcional).
     * @return lista de usuarios filtrados según los parámetros.
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Boolean active) {

        try {
            List<User> allUsers = userService.getAllUsers();
            List<User> filteredUsers = allUsers.stream()
                    .filter(user -> role == null || user.getRole().name().equalsIgnoreCase(role))
                    .filter(user -> active == null || active.equals(user.isActive()))
                    .collect(Collectors.toList());

            List<UserDTO> userDTOs = filteredUsers.stream()
                    .map(this::convertToUserDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "searchCriteria", Map.of(
                            "role", role,
                            "active", active
                    ),
                    "users", userDTOs,
                    "count", userDTOs.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error en la búsqueda de usuarios"));
        }
    }

    /**
     * Verifica si un correo electrónico ya está registrado en el sistema.
     *
     * @param email correo a verificar.
     * @return resultado indicando si el correo existe o está disponible.
     */
    @GetMapping("/check-email/{email}")
    public ResponseEntity<?> checkEmailExists(@PathVariable String email) {
        try {
            boolean exists = userService.existsByEmail(email);
            return ResponseEntity.ok(Map.of(
                    "email", email,
                    "exists", exists,
                    "message", exists ? "El email ya está registrado" : "El email está disponible"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al verificar el email"));
        }
    }

    /**
     * Convierte un objeto {@link UserDTO} a un objeto de dominio {@link User}.
     *
     * @param userDTO objeto de transferencia.
     * @return entidad de usuario lista para persistencia.
     */
    private User convertToUser(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());

        if (userDTO.getRole() != null) {
            try {
                user.setRole(UserRole.valueOf(userDTO.getRole().toUpperCase()));
            } catch (IllegalArgumentException e) {
                user.setRole(UserRole.STUDENT);
            }
        } else {
            user.setRole(UserRole.STUDENT);
        }

        user.setActive(userDTO.getActive() != null ? userDTO.getActive() : true);
        user.setCreatedAt(userDTO.getCreatedAt() != null ? userDTO.getCreatedAt() : new Date());
        user.setUpdatedAt(new Date());

        return user;
    }

    /**
     * Convierte una entidad {@link User} a su representación {@link UserDTO}.
     *
     * @param user entidad de usuario.
     * @return objeto de transferencia de datos.
     */
    private UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setRole(user.getRole().name());
        userDTO.setActive(user.isActive());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUpdatedAt(user.getUpdatedAt());
        return userDTO;
    }
}
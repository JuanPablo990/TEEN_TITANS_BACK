package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto.UserDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.User;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.UserRole;
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
 * Controlador REST para gestionar operaciones CRUD de usuarios base del sistema.
 * Maneja solo la información común de usuarios, los datos específicos por rol
 * deben manejarse en controladores especializados.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param userService Servicio de usuarios.
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Crea un nuevo usuario en el sistema.
     *
     * @param userDTO DTO con los datos del usuario a crear.
     * @return Usuario creado.
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
     * Obtiene un usuario por su ID.
     *
     * @param id Identificador del usuario.
     * @return Usuario encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        try {
            User user = userService.getUserById(id);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Usuario no encontrado: " + id));
            }
            UserDTO userDTO = convertToUserDTO(user);
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener el usuario"));
        }
    }

    /**
     * Obtiene la lista de todos los usuarios registrados.
     *
     * @return Lista de usuarios y su conteo total.
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
     * Actualiza la información básica de un usuario existente.
     *
     * @param id      ID del usuario.
     * @param userDTO Datos actualizados.
     * @return Usuario actualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody UserDTO userDTO) {
        try {
            User existingUser = userService.getUserById(id);
            if (existingUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Usuario no encontrado: " + id));
            }

            // Actualizar solo campos básicos
            updateUserFromDTO(existingUser, userDTO);
            User updatedUser = userService.updateUser(id, existingUser);
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
     * Elimina un usuario por su ID.
     *
     * @param id ID del usuario a eliminar.
     * @return Mensaje de confirmación.
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
     * Busca un usuario por su email.
     *
     * @param email Email del usuario.
     * @return Usuario encontrado.
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        try {
            User user = userService.findByEmail(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Usuario no encontrado con email: " + email));
            }
            UserDTO userDTO = convertToUserDTO(user);
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al buscar usuario por email"));
        }
    }

    /**
     * Busca usuarios por rol.
     *
     * @param role Rol de los usuarios a buscar.
     * @return Lista de usuarios con el rol especificado.
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
     * Activa un usuario en el sistema.
     *
     * @param id ID del usuario a activar.
     * @return Usuario activado.
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
     * Desactiva un usuario en el sistema.
     *
     * @param id ID del usuario a desactivar.
     * @return Usuario desactivado.
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
     * Obtiene estadísticas generales sobre los usuarios.
     *
     * @return Estadísticas agrupadas por rol y estado.
     */
    @GetMapping("/statistics")
    public ResponseEntity<?> getUserStatistics() {
        try {
            List<User> allUsers = userService.getAllUsers();

            long activeCount = allUsers.stream().filter(User::getActive).count();
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
     * Realiza una búsqueda de usuarios por múltiples criterios básicos.
     *
     * @param role   Rol del usuario (opcional).
     * @param active Estado activo (opcional).
     * @return Lista filtrada de usuarios.
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Boolean active) {

        try {
            List<User> allUsers = userService.getAllUsers();
            List<User> filteredUsers = allUsers.stream()
                    .filter(user -> role == null || user.getRole().name().equalsIgnoreCase(role))
                    .filter(user -> active == null || active.equals(user.getActive()))
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
     * Verifica si un email ya está registrado en el sistema.
     *
     * @param email Email a verificar.
     * @return Resultado de la verificación.
     */
    @GetMapping("/check-email/{email}")
    public ResponseEntity<?> checkEmailExists(@PathVariable String email) {
        try {
            boolean exists = userService.findByEmail(email) != null;
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
     * Convierte un UserDTO a una entidad User (solo campos básicos).
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
                user.setRole(UserRole.STUDENT); // Valor por defecto
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
     * Actualiza solo los campos básicos de un usuario desde un DTO.
     */
    private void updateUserFromDTO(User user, UserDTO userDTO) {
        if (userDTO.getName() != null) {
            user.setName(userDTO.getName());
        }
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getPassword() != null) {
            user.setPassword(userDTO.getPassword());
        }
        if (userDTO.getRole() != null) {
            try {
                user.setRole(UserRole.valueOf(userDTO.getRole().toUpperCase()));
            } catch (IllegalArgumentException e) {
                // Mantener el rol actual si el nuevo rol es inválido
            }
        }
        if (userDTO.getActive() != null) {
            user.setActive(userDTO.getActive());
        }
        user.setUpdatedAt(new Date());
    }

    /**
     * Convierte una entidad User a un UserDTO (solo campos básicos).
     */
    private UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        // No incluir password por seguridad
        userDTO.setRole(user.getRole().name());
        userDTO.setActive(user.getActive());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUpdatedAt(user.getUpdatedAt());

        return userDTO;
    }

    /**
     * DTO para crear un usuario básico.
     */
    public static class UserCreateRequest {
        private String name;
        private String email;
        private String password;
        private String role;
        private Boolean active;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public Boolean getActive() { return active; }
        public void setActive(Boolean active) { this.active = active; }
    }

    /**
     * DTO para actualizar un usuario básico.
     */
    public static class UserUpdateRequest {
        private String name;
        private String email;
        private String password;
        private String role;
        private Boolean active;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public Boolean getActive() { return active; }
        public void setActive(Boolean active) { this.active = active; }
    }
}
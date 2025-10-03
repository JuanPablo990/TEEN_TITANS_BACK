package eci.edu.dosw.parcial.TEEN_TITANS_BACK.services;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dtos.UserDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.User;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Administrator;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Dean;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class UserService {

    private final ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

    /**
     * Crea un nuevo usuario a partir de un UserDTO y lo guarda en memoria.
     */
    public User createUser(UserDTO userData) {
        User user = createUserFromDTO(userData);
        if (user != null) {
            users.put(user.getId(), user);
            return user;
        }
        return null;
    }

    /**
     * Obtiene un usuario según su identificador único.
     */
    public User getUserById(String userId) {
        return users.get(userId);
    }

    /**
     * Busca un usuario por su correo electrónico.
     */
    public User getUserByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getBring().equals(email))
                .findFirst()
                .orElse(null);
    }

    /**
     * Actualiza los datos de un usuario existente.
     */
    public User updateUser(String userId, UserDTO userData) {
        User existingUser = users.get(userId);
        if (existingUser != null) {
            User updatedUser = createUserFromDTO(userData);
            users.put(userId, updatedUser);
            return updatedUser;
        }
        return null;
    }

    /**
     * Elimina un usuario del sistema usando su ID.
     */
    public boolean deleteUser(String userId) {
        return users.remove(userId) != null;
    }

    /**
     * Retorna la lista completa de usuarios registrados.
     */
    public List<User> getAllUsers() {
        return users.values().stream().collect(Collectors.toList());
    }

    /**
     * Obtiene los usuarios que pertenecen a un rol específico (Administrator, Dean, etc.).
     */
    public List<User> getUsersByRole(String role) {
        return users.values().stream()
                .filter(user -> user.getClass().getSimpleName().equalsIgnoreCase(role))
                .collect(Collectors.toList());
    }

    /**
     * Crea un objeto User según el rol especificado en el DTO.
     */
    private User createUserFromDTO(UserDTO dto) {
        try {
            User user;
            switch (dto.getRole().toUpperCase()) {
                case "ADMINISTRATOR":
                    Administrator admin = new Administrator();
                    user = admin;
                    break;
                case "DEAN":
                    Dean dean = new Dean();
                    user = dean;
                    break;
                default:
                    return null;
            }
            return user;
        } catch (Exception e) {
            return null;
        }
    }
}

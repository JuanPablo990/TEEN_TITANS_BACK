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

    public User createUser(UserDTO userData) {
        User user = createUserFromDTO(userData);
        if (user != null) {
            users.put(user.getId(), user);
            return user;
        }
        return null;
    }

    public User getUserById(String userId) {
        return users.get(userId);
    }

    public User getUserByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getBring().equals(email))
                .findFirst()
                .orElse(null);
    }

    public User updateUser(String userId, UserDTO userData) {
        User existingUser = users.get(userId);
        if (existingUser != null) {
            User updatedUser = createUserFromDTO(userData);
            users.put(userId, updatedUser);
            return updatedUser;
        }
        return null;
    }

    public boolean deleteUser(String userId) {
        return users.remove(userId) != null;
    }

    public List<User> getAllUsers() {
        return users.values().stream().collect(Collectors.toList());
    }

    public List<User> getUsersByRole(String role) {
        return users.values().stream()
                .filter(user -> user.getClass().getSimpleName().equalsIgnoreCase(role))
                .collect(Collectors.toList());
    }

    private User createUserFromDTO(UserDTO dto) {
        try {
            User user;
            switch (dto.getRole().toUpperCase()) {
                case "ADMINISTRATOR":
                    Administrator admin = new Administrator();
                    // Usar reflection para setear campos si es necesario
                    user = admin;
                    break;
                case "DEAN":
                    Dean dean = new Dean();
                    // Usar reflection para setear campos si es necesario
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
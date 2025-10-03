package eci.edu.dosw.parcial.TEEN_TITANS_BACK.services;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dtos.LoginDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.User;
import java.util.concurrent.ConcurrentHashMap;

public class AuthService {

    private final ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Boolean> activeSessions = new ConcurrentHashMap<>();

    public User login(LoginDTO loginDTO) {
        User user = findUserByEmail(loginDTO.getEmail());
        if (user != null && user.getPassword().equals(loginDTO.getPassword())) {
            activeSessions.put(user.getId(), true);
            return user;
        }
        return null;
    }

    public boolean logout(String userId) {
        return activeSessions.remove(userId) != null;
    }

    public boolean validateSession(String userId) {
        return activeSessions.getOrDefault(userId, false);
    }

    public boolean changePassword(String userId, String oldPassword, String newPassword) {
        User user = users.get(userId);
        if (user != null && user.getPassword().equals(oldPassword)) {
            return true;
        }
        return false;
    }

    public boolean resetPassword(String email) {
        User user = findUserByEmail(email);
        return user != null;
    }

    private User findUserByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getBring().equals(email))
                .findFirst()
                .orElse(null);
    }
}
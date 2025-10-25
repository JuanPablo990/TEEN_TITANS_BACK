package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.User;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new AppException("El email ya está registrado: " + user.getEmail());
        }
        return userRepository.save(user);
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(String id, User user) {
        if (!userRepository.existsById(id)) {
            throw new AppException("Usuario no encontrado con ID: " + id);
        }

        // CORREGIDO: Maneja List en lugar de Optional
        List<User> existingUsersWithEmail = userRepository.findByEmail(user.getEmail());
        if (!existingUsersWithEmail.isEmpty() &&
                !existingUsersWithEmail.get(0).getId().equals(id)) {
            throw new AppException("El email ya está en uso por otro usuario: " + user.getEmail());
        }

        user.setId(id);
        return userRepository.save(user);
    }

    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new AppException("Usuario no encontrado con ID: " + id);
        }
        userRepository.deleteById(id);
    }

    public Optional<User> findByEmail(String email) {
        List<User> users = userRepository.findByEmail(email);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    public List<User> findByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    public User activateUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException("Usuario no encontrado con ID: " + id));
        user.setActive(true);
        return userRepository.save(user);
    }

    public User deactivateUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException("Usuario no encontrado con ID: " + id));
        user.setActive(false);
        return userRepository.save(user);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public List<User> findByNameContaining(String name) {
        return userRepository.findByNameContainingIgnoreCase(name);
    }

    public List<User> findByActive(boolean active) {
        return userRepository.findByActive(active);
    }

    public long countByRole(UserRole role) {
        return userRepository.countByRole(role);
    }
}
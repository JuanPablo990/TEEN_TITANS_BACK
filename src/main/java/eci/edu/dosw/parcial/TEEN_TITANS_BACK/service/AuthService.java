package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.User;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.UserRepository;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Servicio para manejar la autenticación y autorización de usuarios en el sistema universitario.
 * Proporciona funcionalidades de login, registro, validación de tokens y gestión de sesiones.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    private Map<String, User> activeTokens = new HashMap<>();
    private User currentUser;

    /**
     * Autentica a un usuario en el sistema utilizando email y contraseña.
     *
     * @param email el correo electrónico del usuario
     * @param password la contraseña del usuario
     * @return Map con los resultados de la autenticación incluyendo éxito, token, usuario y mensaje
     */
    public Map<String, Object> login(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        Map<String, Object> response = new HashMap<>();

        if (userOptional.isEmpty()) {
            response.put("success", false);
            response.put("message", "Usuario no encontrado");
            return response;
        }

        User user = userOptional.get();

        if (!user.getActive()) {
            response.put("success", false);
            response.put("message", "Usuario inactivo");
            return response;
        }

        if (!user.getPassword().equals(password)) {
            response.put("success", false);
            response.put("message", "Credenciales inválidas");
            return response;
        }

        String token = "token_" + user.getId() + "_" + System.currentTimeMillis();
        activeTokens.put(token, user);
        currentUser = user;

        response.put("success", true);
        response.put("token", token);
        response.put("user", user);
        response.put("message", "Login exitoso");

        return response;
    }

    /**
     * Registra un nuevo usuario en el sistema universitario.
     *
     * @param user el objeto User a registrar
     * @return el usuario registrado y guardado en la base de datos
     * @throws AppException si el email ya existe en el sistema
     */
    public User register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new AppException("El email ya está registrado: " + user.getEmail());
        }

        user.setActive(true);
        return userRepository.save(user);
    }

    /**
     * Valida la autenticidad y vigencia de un token de acceso.
     *
     * @param token el token a validar
     * @return true si el token es válido y activo, false en caso contrario
     */
    public boolean validateToken(String token) {
        return token != null && activeTokens.containsKey(token);
    }

    /**
     * Obtiene el usuario actualmente autenticado en el sistema.
     *
     * @return el usuario autenticado actual
     * @throws AppException si no hay usuario autenticado
     */
    public User getCurrentUser() {
        if (currentUser == null) {
            throw new AppException("No hay usuario autenticado");
        }
        return currentUser;
    }

    /**
     * Cierra la sesión del usuario actual y elimina el token de acceso.
     *
     * @param token el token de acceso a invalidar
     */
    public void logout(String token) {
        activeTokens.remove(token);
        if (currentUser != null && token.contains(currentUser.getId())) {
            currentUser = null;
        }
    }

    /**
     * Obtiene el usuario asociado a un token de acceso específico.
     *
     * @param token el token de acceso
     * @return el usuario asociado al token, o null si el token no es válido
     */
    public User getUserFromToken(String token) {
        return activeTokens.get(token);
    }

    /**
     * Valida si un token es válido y retorna el usuario asociado.
     *
     * @param token el token a validar
     * @return el usuario asociado al token
     * @throws AppException si el token no es válido
     */
    public User validateAndGetUserFromToken(String token) {
        if (!validateToken(token)) {
            throw new AppException("Token inválido o expirado");
        }
        return getUserFromToken(token);
    }

    /**
     * Cambia la contraseña del usuario actual.
     *
     * @param currentPassword la contraseña actual
     * @param newPassword la nueva contraseña
     * @throws AppException si la contraseña actual es incorrecta o no hay usuario autenticado
     */
    public void changePassword(String currentPassword, String newPassword) {
        if (currentUser == null) {
            throw new AppException("No hay usuario autenticado");
        }

        if (!currentUser.getPassword().equals(currentPassword)) {
            throw new AppException("La contraseña actual es incorrecta");
        }

        currentUser.setPassword(newPassword);
        userRepository.save(currentUser);
    }
}
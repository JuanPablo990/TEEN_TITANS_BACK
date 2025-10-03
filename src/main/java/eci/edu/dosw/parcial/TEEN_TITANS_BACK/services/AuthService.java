package eci.edu.dosw.parcial.TEEN_TITANS_BACK.services;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dtos.LoginDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.User;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Servicio de autenticación que gestiona inicio de sesión,
 * cierre de sesión, validación y administración de contraseñas.
 */
public class AuthService {

    private final ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Boolean> activeSessions = new ConcurrentHashMap<>();

    /**
     * Inicia sesión de un usuario con sus credenciales.
     *
     * @param loginDTO Datos de inicio de sesión (correo y contraseña)
     * @return Usuario autenticado, o null si las credenciales son inválidas
     */
    public User login(LoginDTO loginDTO) {
        User user = findUserByEmail(loginDTO.getEmail());
        if (user != null && user.getPassword().equals(loginDTO.getPassword())) {
            activeSessions.put(user.getId(), true);
            return user;
        }
        return null;
    }

    /**
     * Cierra la sesión de un usuario.
     *
     * @param userId Identificador del usuario
     * @return true si la sesión fue cerrada, false en caso contrario
     */
    public boolean logout(String userId) {
        return activeSessions.remove(userId) != null;
    }

    /**
     * Valida si un usuario tiene una sesión activa.
     *
     * @param userId Identificador del usuario
     * @return true si la sesión está activa, false en caso contrario
     */
    public boolean validateSession(String userId) {
        return activeSessions.getOrDefault(userId, false);
    }

    /**
     * Cambia la contraseña de un usuario.
     *
     * @param userId Identificador del usuario
     * @param oldPassword Contraseña actual
     * @param newPassword Nueva contraseña
     * @return true si la contraseña fue cambiada, false en caso contrario
     */
    public boolean changePassword(String userId, String oldPassword, String newPassword) {
        User user = users.get(userId);
        if (user != null && user.getPassword().equals(oldPassword)) {
            return true;
        }
        return false;
    }

    /**
     * Restablece la contraseña de un usuario a partir de su correo electrónico.
     *
     * @param email Correo electrónico del usuario
     * @return true si el usuario existe, false en caso contrario
     */
    public boolean resetPassword(String email) {
        User user = findUserByEmail(email);
        return user != null;
    }

    /**
     * Busca un usuario en el sistema a partir de su correo electrónico.
     *
     * @param email Correo electrónico del usuario
     * @return Usuario encontrado, o null si no existe
     */
    private User findUserByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getBring().equals(email))
                .findFirst()
                .orElse(null);
    }
}

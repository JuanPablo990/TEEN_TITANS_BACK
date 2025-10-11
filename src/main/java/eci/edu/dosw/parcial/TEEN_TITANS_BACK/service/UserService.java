package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.User;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.UserRole;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio abstracto para operaciones CRUD de usuarios
 * Proporciona funcionalidades base para la gestión de usuarios
 */
@Service
public abstract class UserService {

    @Autowired
    protected UserRepository userRepository;

    /**
     * Crea un nuevo usuario en el sistema
     * @param user Usuario a crear
     * @return Usuario creado
     */
    public User createUser(User user) {
        // Validar que el email no exista
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new AppException("El email ya está registrado: " + user.getEmail());
        }
        return userRepository.save(user);
    }

    /**
     * Obtiene un usuario por su ID
     * @param id Identificador del usuario
     * @return Usuario encontrado o null si no existe
     */
    public User getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Obtiene todos los usuarios del sistema
     * @return Lista de todos los usuarios
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Actualiza la información de un usuario
     * @param id Identificador del usuario a actualizar
     * @param user Nuevos datos del usuario
     * @return Usuario actualizado
     */
    public User updateUser(String id, User user) {
        // Verificar que el usuario existe
        if (!userRepository.existsById(id)) {
            throw new AppException("Usuario no encontrado con ID: " + id);
        }

        Optional<User> existingUserWithEmail = userRepository.findByEmail(user.getEmail());
        if (existingUserWithEmail.isPresent() && !existingUserWithEmail.get().getId().equals(id)) {
            throw new AppException("El email ya está en uso por otro usuario: " + user.getEmail());
        }

        user.setId(id);
        return userRepository.save(user);
    }

    /**
     * Elimina un usuario del sistema
     * @param id Identificador del usuario a eliminar
     */
    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new AppException("Usuario no encontrado con ID: " + id);
        }
        userRepository.deleteById(id);
    }

    /**
     * Busca un usuario por su email
     * @param email Email del usuario
     * @return Usuario encontrado o null si no existe
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    /**
     * Busca usuarios por rol
     * @param role Rol de los usuarios a buscar
     * @return Lista de usuarios con el rol especificado
     */
    public List<User> findByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    /**
     * Activa un usuario en el sistema
     * @param id Identificador del usuario a activar
     * @return Usuario activado
     */
    public User activateUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException("Usuario no encontrado con ID: " + id));

        user.setActive(true);
        return userRepository.save(user);
    }

    /**
     * Desactiva un usuario en el sistema
     * @param id Identificador del usuario a desactivar
     * @return Usuario desactivado
     */
    public User deactivateUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException("Usuario no encontrado con ID: " + id));

        user.setActive(false);
        return userRepository.save(user);
    }
}

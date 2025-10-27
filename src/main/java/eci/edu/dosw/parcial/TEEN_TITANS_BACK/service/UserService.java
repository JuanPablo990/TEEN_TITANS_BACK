package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.User;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de usuarios del sistema.
 * Proporciona operaciones CRUD y métodos de búsqueda para usuarios.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Crea un nuevo usuario en el sistema.
     *
     * @param user Usuario a crear
     * @return Usuario creado
     * @throws AppException si el email ya está registrado
     */
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new AppException("El email ya está registrado: " + user.getEmail());
        }
        return userRepository.save(user);
    }

    /**
     * Obtiene un usuario por su ID.
     *
     * @param id ID del usuario
     * @return Optional con el usuario encontrado
     */
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    /**
     * Obtiene todos los usuarios del sistema.
     *
     * @return Lista de todos los usuarios
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Actualiza la información de un usuario existente.
     *
     * @param id ID del usuario a actualizar
     * @param user Nuevos datos del usuario
     * @return Usuario actualizado
     * @throws AppException si el usuario no existe o el email está en uso
     */
    public User updateUser(String id, User user) {
        if (!userRepository.existsById(id)) {
            throw new AppException("Usuario no encontrado con ID: " + id);
        }

        List<User> existingUsersWithEmail = userRepository.findByEmail(user.getEmail());
        if (!existingUsersWithEmail.isEmpty() &&
                !existingUsersWithEmail.get(0).getId().equals(id)) {
            throw new AppException("El email ya está en uso por otro usuario: " + user.getEmail());
        }

        user.setId(id);
        return userRepository.save(user);
    }

    /**
     * Elimina un usuario del sistema.
     *
     * @param id ID del usuario a eliminar
     * @throws AppException si el usuario no existe
     */
    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new AppException("Usuario no encontrado con ID: " + id);
        }
        userRepository.deleteById(id);
    }

    /**
     * Busca un usuario por su email.
     *
     * @param email Email del usuario
     * @return Optional con el usuario encontrado
     */
    public Optional<User> findByEmail(String email) {
        List<User> users = userRepository.findByEmail(email);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    /**
     * Busca usuarios por rol.
     *
     * @param role Rol de usuario
     * @return Lista de usuarios con el rol especificado
     */
    public List<User> findByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    /**
     * Activa un usuario.
     *
     * @param id ID del usuario a activar
     * @return Usuario activado
     * @throws AppException si el usuario no existe
     */
    public User activateUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException("Usuario no encontrado con ID: " + id));
        user.setActive(true);
        return userRepository.save(user);
    }

    /**
     * Desactiva un usuario.
     *
     * @param id ID del usuario a desactivar
     * @return Usuario desactivado
     * @throws AppException si el usuario no existe
     */
    public User deactivateUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException("Usuario no encontrado con ID: " + id));
        user.setActive(false);
        return userRepository.save(user);
    }

    /**
     * Verifica si existe un usuario con el email especificado.
     *
     * @param email Email a verificar
     * @return true si existe un usuario con ese email
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Busca usuarios por nombre (búsqueda parcial sin distinguir mayúsculas/minúsculas).
     *
     * @param name Texto del nombre a buscar
     * @return Lista de usuarios que coinciden con el nombre
     */
    public List<User> findByNameContaining(String name) {
        return userRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Busca usuarios por estado activo/inactivo.
     *
     * @param active Estado activo
     * @return Lista de usuarios con el estado especificado
     */
    public List<User> findByActive(boolean active) {
        return userRepository.findByActive(active);
    }

    /**
     * Cuenta usuarios por rol.
     *
     * @param role Rol de usuario
     * @return Número de usuarios con el rol especificado
     */
    public long countByRole(UserRole role) {
        return userRepository.countByRole(role);
    }
}
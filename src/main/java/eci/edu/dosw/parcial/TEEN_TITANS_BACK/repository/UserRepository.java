package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;


import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.User;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.UserRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para manejar las operaciones CRUD y consultas personalizadas
 * sobre la colección de usuarios (User) en MongoDB.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * Busca un usuario por su email exacto.
     * @param email Email del usuario.
     * @return El usuario encontrado o null si no existe.
     */
    Optional<User> findByEmail(String email);

    /**
     * Busca usuarios por su rol en el sistema.
     * @param role Rol del usuario.
     * @return Lista de usuarios con el rol especificado.
     */
    List<User> findByRole(UserRole role);

    /**
     * Busca usuarios por su estado de activación.
     * @param active Estado del usuario (true = activo, false = inactivo).
     * @return Lista de usuarios filtrados por estado.
     */
    List<User> findByActive(boolean active);

    /**
     * Verifica si existe un usuario con el email especificado.
     * @param email Email a verificar.
     * @return true si existe un usuario con ese email, false en caso contrario.
     */
    boolean existsByEmail(String email);

    /**
     * Busca usuarios por nombre (búsqueda case-insensitive).
     * @param name Nombre o parte del nombre del usuario.
     * @return Lista de usuarios que coinciden con el nombre.
     */
    List<User> findByNameContainingIgnoreCase(String name);

    /**
     * Busca usuarios por rol y estado de activación.
     * @param role Rol del usuario.
     * @param active Estado de activación.
     * @return Lista de usuarios que cumplen con ambos criterios.
     */
    List<User> findByRoleAndActive(UserRole role, boolean active);
}

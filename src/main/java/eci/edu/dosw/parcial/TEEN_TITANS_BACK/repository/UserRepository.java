package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.User;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la gestión de usuarios en la base de datos MongoDB.
 * Proporciona métodos para buscar usuarios por diferentes criterios.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findById(String id);
    List<User> findByName(String name);
    List<User> findByEmail(String email);
    List<User> findByPassword(String password);
    List<User> findByRole(UserRole role);
    List<User> findByActive(boolean active);
    List<User> findByCreatedAt(Date createdAt);
    List<User> findByUpdatedAt(Date updatedAt);

    List<User> findByNameContainingIgnoreCase(String name);
    List<User> findByEmailContainingIgnoreCase(String email);

    List<User> findByNameAndRole(String name, UserRole role);
    List<User> findByEmailAndRole(String email, UserRole role);
    List<User> findByNameAndActive(String name, boolean active);

    List<User> findByNameOrEmail(String name, String email);

    List<User> findByCreatedAtAfter(Date date);
    List<User> findByCreatedAtBefore(Date date);
    List<User> findByCreatedAtBetween(Date startDate, Date endDate);

    List<User> findByOrderByNameAsc();
    List<User> findByOrderByCreatedAtDesc();
    List<User> findByRoleOrderByNameAsc(UserRole role);

    long countByRole(UserRole role);
    long countByActive(boolean active);
    long countByRoleAndActive(UserRole role, boolean active);

    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    List<User> findByNameRegex(String namePattern);

    @Query("{ 'email': { $regex: ?0, $options: 'i' } }")
    List<User> findByEmailRegex(String emailPattern);

    @Query("{ 'createdAt': { $gte: ?0, $lte: ?1 } }")
    List<User> findUsersCreatedBetween(Date startDate, Date endDate);

    boolean existsByEmail(String email);
    boolean existsByNameAndEmail(String name, String email);
}
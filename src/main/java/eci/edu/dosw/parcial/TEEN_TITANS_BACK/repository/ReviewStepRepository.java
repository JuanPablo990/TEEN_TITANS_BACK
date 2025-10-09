package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.ReviewStep;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.UserRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Repositorio para manejar las operaciones CRUD y consultas personalizadas
 * sobre la colección de pasos de revisión (ReviewStep) en MongoDB.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Repository
public interface ReviewStepRepository extends MongoRepository<ReviewStep, String> {

    /**
     * Busca pasos de revisión por ID de usuario.
     * @param userId ID del usuario que realizó la acción.
     * @return Lista de pasos de revisión realizados por el usuario.
     */
    List<ReviewStep> findByUserId(String userId);

    /**
     * Busca pasos de revisión por rol de usuario.
     * @param userRole Rol del usuario que realizó la acción.
     * @return Lista de pasos de revisión realizados por usuarios con el rol especificado.
     */
    List<ReviewStep> findByUserRole(UserRole userRole);

    /**
     * Busca pasos de revisión por tipo de acción.
     * @param action Acción realizada durante la revisión.
     * @return Lista de pasos de revisión con la acción especificada.
     */
    List<ReviewStep> findByAction(String action);

    /**
     * Busca pasos de revisión realizados después de una fecha específica.
     * @param timestamp Fecha límite inferior.
     * @return Lista de pasos de revisión realizados después de la fecha indicada.
     */
    List<ReviewStep> findByTimestampAfter(Date timestamp);

    /**
     * Busca pasos de revisión realizados antes de una fecha específica.
     * @param timestamp Fecha límite superior.
     * @return Lista de pasos de revisión realizados antes de la fecha indicada.
     */
    List<ReviewStep> findByTimestampBefore(Date timestamp);

    /**
     * Busca pasos de revisión realizados por un usuario específico y acción.
     * @param userId ID del usuario.
     * @param action Acción realizada.
     * @return Lista de pasos de revisión que cumplen con ambos criterios.
     */
    List<ReviewStep> findByUserIdAndAction(String userId, String action);
}
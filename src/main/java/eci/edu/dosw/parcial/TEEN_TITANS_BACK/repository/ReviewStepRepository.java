package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.ReviewStep;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
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

    List<ReviewStep> findByUserId(String userId);

    List<ReviewStep> findByUserRole(UserRole userRole);

    List<ReviewStep> findByAction(String action);

    List<ReviewStep> findByCommentsContaining(String comment);

    List<ReviewStep> findByTimestampAfter(Date timestamp);

    List<ReviewStep> findByTimestampBefore(Date timestamp);

    List<ReviewStep> findByTimestampBetween(Date startDate, Date endDate);

    List<ReviewStep> findByUserIdAndUserRole(String userId, UserRole userRole);

    List<ReviewStep> findByUserIdAndAction(String userId, String action);

    List<ReviewStep> findByUserRoleAndAction(UserRole userRole, String action);

    List<ReviewStep> findByUserIdAndUserRoleAndAction(String userId, UserRole userRole, String action);

    List<ReviewStep> findByUserIdAndTimestampAfter(String userId, Date timestamp);

    List<ReviewStep> findByUserRoleAndTimestampAfter(UserRole userRole, Date timestamp);

    List<ReviewStep> findByOrderByTimestampDesc();

    List<ReviewStep> findByUserIdOrderByTimestampDesc(String userId);

    List<ReviewStep> findByUserRoleOrderByTimestampDesc(UserRole userRole);

    List<ReviewStep> findByActionOrderByTimestampDesc(String action);

    List<ReviewStep> findByUserIdAndUserRoleOrderByTimestampDesc(String userId, UserRole userRole);

    long countByUserId(String userId);

    long countByUserRole(UserRole userRole);

    long countByAction(String action);

    long countByUserIdAndUserRole(String userId, UserRole userRole);

    long countByTimestampAfter(Date timestamp);

    long countByTimestampBetween(Date startDate, Date endDate);

    @Query("{ 'userId': { $regex: ?0, $options: 'i' } }")
    List<ReviewStep> findByUserIdRegex(String userIdPattern);

    @Query("{ 'action': { $regex: ?0, $options: 'i' } }")
    List<ReviewStep> findByActionRegex(String actionPattern);

    @Query("{ 'comments': { $regex: ?0, $options: 'i' } }")
    List<ReviewStep> findByCommentsRegex(String commentsPattern);

    @Query("{ 'userId': ?0, 'timestamp': { $gte: ?1, $lte: ?2 } }")
    List<ReviewStep> findByUserIdAndTimestampBetween(String userId, Date startDate, Date endDate);

    @Query("{ 'userRole': ?0, 'timestamp': { $gte: ?1, $lte: ?2 } }")
    List<ReviewStep> findByUserRoleAndTimestampBetween(UserRole userRole, Date startDate, Date endDate);

    @Query("{ 'action': ?0, 'timestamp': { $gte: ?1, $lte: ?2 } }")
    List<ReviewStep> findByActionAndTimestampBetween(String action, Date startDate, Date endDate);

    @Query(value = "{ 'userId': ?0 }", sort = "{ 'timestamp': -1 }")
    List<ReviewStep> findByUserIdSortedByTimestampDesc(String userId);

    @Query(value = "{ 'userRole': ?0 }", sort = "{ 'timestamp': -1 }")
    List<ReviewStep> findByUserRoleSortedByTimestampDesc(UserRole userRole);

    @Query("{ 'userRole': { $in: ?0 }, 'timestamp': { $gte: ?1 } }")
    List<ReviewStep> findByUserRolesAndTimestampAfter(List<UserRole> userRoles, Date timestamp);

    @Query("{ 'action': { $in: ?0 }, 'timestamp': { $gte: ?1, $lte: ?2 } }")
    List<ReviewStep> findByActionsAndTimestampBetween(List<String> actions, Date startDate, Date endDate);

    @Query(value = "{}", sort = "{ 'timestamp': -1 }")
    List<ReviewStep> findRecentReviewSteps();

    @Query(value = "{ 'userId': ?0 }", sort = "{ 'timestamp': -1 }", count = true)
    long countRecentByUserId(String userId);

    boolean existsByUserId(String userId);

    boolean existsByUserRole(UserRole userRole);

    boolean existsByAction(String action);

    boolean existsByUserIdAndUserRole(String userId, UserRole userRole);

    boolean existsByUserIdAndAction(String userId, String action);

    List<ReviewStep> findByUserIdIn(List<String> userIds);

    List<ReviewStep> findByUserRoleIn(List<UserRole> userRoles);

    List<ReviewStep> findByActionIn(List<String> actions);

    List<ReviewStep> findByUserIdInAndUserRoleIn(List<String> userIds, List<UserRole> userRoles);

    List<ReviewStep> findByCommentsIsNull();

    List<ReviewStep> findByCommentsIsNotNull();

    @Query("{ 'comments': { $exists: true, $ne: '' } }")
    List<ReviewStep> findStepsWithNonEmptyComments();

    ReviewStep findFirstByUserIdOrderByTimestampDesc(String userId);

    ReviewStep findFirstByUserRoleOrderByTimestampDesc(UserRole userRole);
}
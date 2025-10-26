package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.ScheduleChangeRequest;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.RequestStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para manejar las operaciones CRUD y consultas personalizadas
 * sobre la colección de solicitudes de cambio de horario (ScheduleChangeRequest) en MongoDB.
 *
 * @author Equipo Teen Titans
 * @version 2.0
 * @since 2025
 */
@Repository
public interface ScheduleChangeRequestRepository extends MongoRepository<ScheduleChangeRequest, String> {

    // Búsquedas básicas por atributos individuales
    List<ScheduleChangeRequest> findByStudentId(String studentId);
    List<ScheduleChangeRequest> findByStatus(RequestStatus status);
    List<ScheduleChangeRequest> findByReasonContainingIgnoreCase(String reason);

    // Búsquedas por fechas
    List<ScheduleChangeRequest> findBySubmissionDateAfter(Date submissionDate);
    List<ScheduleChangeRequest> findBySubmissionDateBefore(Date submissionDate);
    List<ScheduleChangeRequest> findBySubmissionDateBetween(Date startDate, Date endDate);
    List<ScheduleChangeRequest> findByResolutionDateAfter(Date resolutionDate);
    List<ScheduleChangeRequest> findByResolutionDateBefore(Date resolutionDate);
    List<ScheduleChangeRequest> findByResolutionDateBetween(Date startDate, Date endDate);

    // Búsquedas por grupos (usando @Query para campos anidados)
    @Query("{ 'currentGroup.groupId': ?0 }")
    List<ScheduleChangeRequest> findByCurrentGroupId(String currentGroupId);

    @Query("{ 'requestedGroup.groupId': ?0 }")
    List<ScheduleChangeRequest> findByRequestedGroupId(String requestedGroupId);

    @Query("{ 'currentGroup.course.courseCode': ?0 }")
    List<ScheduleChangeRequest> findByCurrentCourseCode(String courseCode);

    @Query("{ 'requestedGroup.course.courseCode': ?0 }")
    List<ScheduleChangeRequest> findByRequestedCourseCode(String courseCode);

    @Query("{ 'currentGroup.professor.id': ?0 }")
    List<ScheduleChangeRequest> findByCurrentProfessorId(String professorId);

    @Query("{ 'requestedGroup.professor.id': ?0 }")
    List<ScheduleChangeRequest> findByRequestedProfessorId(String professorId);

    // Búsquedas combinadas
    List<ScheduleChangeRequest> findByStudentIdAndStatus(String studentId, RequestStatus status);
    List<ScheduleChangeRequest> findByStudentIdAndSubmissionDateAfter(String studentId, Date submissionDate);
    List<ScheduleChangeRequest> findByStatusAndSubmissionDateAfter(RequestStatus status, Date submissionDate);
    List<ScheduleChangeRequest> findByStatusAndSubmissionDateBefore(RequestStatus status, Date submissionDate);
    List<ScheduleChangeRequest> findByStudentIdAndStatusAndSubmissionDateAfter(String studentId, RequestStatus status, Date submissionDate);

    // Búsquedas con ordenamiento
    List<ScheduleChangeRequest> findByStudentIdOrderBySubmissionDateDesc(String studentId);
    List<ScheduleChangeRequest> findByStatusOrderBySubmissionDateDesc(RequestStatus status);
    List<ScheduleChangeRequest> findByOrderBySubmissionDateDesc();
    List<ScheduleChangeRequest> findByOrderBySubmissionDateAsc();
    List<ScheduleChangeRequest> findByStatusOrderBySubmissionDateAsc(RequestStatus status);
    List<ScheduleChangeRequest> findByStudentIdOrderBySubmissionDateAsc(String studentId);

    // Consultas de conteo
    long countByStudentId(String studentId);
    long countByStatus(RequestStatus status);
    long countByStudentIdAndStatus(String studentId, RequestStatus status);
    long countBySubmissionDateAfter(Date submissionDate);
    long countBySubmissionDateBefore(Date submissionDate);
    long countBySubmissionDateBetween(Date startDate, Date endDate);
    long countByResolutionDateIsNotNull();
    long countByResolutionDateIsNull();

    // Consultas personalizadas con @Query
    @Query("{ 'studentId': ?0, 'submissionDate': { $gte: ?1, $lte: ?2 } }")
    List<ScheduleChangeRequest> findByStudentIdAndSubmissionDateBetween(String studentId, Date startDate, Date endDate);

    @Query("{ 'status': ?0, 'submissionDate': { $gte: ?1, $lte: ?2 } }")
    List<ScheduleChangeRequest> findByStatusAndSubmissionDateBetween(RequestStatus status, Date startDate, Date endDate);

    @Query("{ 'studentId': ?0, 'status': { $in: ?1 } }")
    List<ScheduleChangeRequest> findByStudentIdAndStatusIn(String studentId, List<RequestStatus> statuses);

    @Query("{ 'submissionDate': { $gte: ?0 }, 'resolutionDate': { $lte: ?1 } }")
    List<ScheduleChangeRequest> findResolvedInDateRange(Date startDate, Date endDate);

    @Query("{ 'reason': { $regex: ?0, $options: 'i' } }")
    List<ScheduleChangeRequest> findByReasonRegex(String reasonPattern);

    @Query("{ 'studentId': ?0, 'reason': { $regex: ?1, $options: 'i' } }")
    List<ScheduleChangeRequest> findByStudentIdAndReasonRegex(String studentId, String reasonPattern);

    @Query(value = "{ 'studentId': ?0 }", sort = "{ 'submissionDate': -1 }")
    List<ScheduleChangeRequest> findRecentByStudentId(String studentId);

    @Query(value = "{ 'status': ?0 }", sort = "{ 'submissionDate': -1 }")
    List<ScheduleChangeRequest> findRecentByStatus(RequestStatus status);

    // Consultas para análisis y reportes
    @Query(value = "{}", sort = "{ 'submissionDate': -1 }")
    List<ScheduleChangeRequest> findRecentRequests();

    @Query("{ 'submissionDate': { $gte: ?0 }, 'status': ?1 }")
    List<ScheduleChangeRequest> findSinceDateByStatus(Date sinceDate, RequestStatus status);

    @Query("{ 'studentId': ?0, 'currentGroup.groupId': ?1, 'status': { $in: [?2, ?3] } }")
    List<ScheduleChangeRequest> findStudentGroupRequestsWithStatus(String studentId, String groupId,
                                                                   RequestStatus status1, RequestStatus status2);

    // Consultas para estadísticas
    @Query(value = "{ 'status': ?0 }", count = true)
    long countRequestsByStatus(RequestStatus status);

    @Query(value = "{ 'studentId': ?0, 'status': ?1 }", count = true)
    long countStudentRequestsByStatus(String studentId, RequestStatus status);

    // Verificación de existencia
    boolean existsByStudentId(String studentId);
    boolean existsByStudentIdAndStatus(String studentId, RequestStatus status);

    @Query("{ 'studentId': ?0, 'currentGroup.groupId': ?1, 'status': { $in: ['PENDING', 'UNDER_REVIEW'] } }")
    boolean existsPendingOrUnderReviewByStudentAndGroup(String studentId, String groupId);

    @Query("{ 'studentId': ?0, 'requestedGroup.groupId': ?1, 'status': { $in: ['PENDING', 'UNDER_REVIEW'] } }")
    boolean existsPendingOrUnderReviewByStudentAndRequestedGroup(String studentId, String requestedGroupId);

    // Búsqueda por múltiples valores
    List<ScheduleChangeRequest> findByStudentIdIn(List<String> studentIds);
    List<ScheduleChangeRequest> findByStatusIn(List<RequestStatus> statuses);

    // Consultas para limpieza de datos
    @Query("{ 'submissionDate': { $lt: ?0 }, 'status': { $in: ['APPROVED', 'REJECTED'] } }")
    List<ScheduleChangeRequest> findResolvedBeforeDate(Date date);

    @Query("{ 'submissionDate': { $lt: ?0 } }")
    List<ScheduleChangeRequest> findOlderThan(Date date);

    // Búsqueda de solicitudes sin resolución
    @Query("{ 'resolutionDate': null }")
    List<ScheduleChangeRequest> findWithoutResolution();

    @Query("{ 'resolutionDate': null, 'status': { $in: ['APPROVED', 'REJECTED'] } }")
    List<ScheduleChangeRequest> findResolvedWithoutResolutionDate();

    // Consultas avanzadas para dashboard
    @Query(value = "{ 'submissionDate': { $gte: ?0 } }", count = true)
    long countSinceDate(Date sinceDate);

    @Query(value = "{ 'studentId': ?0, 'submissionDate': { $gte: ?1 } }", count = true)
    long countStudentRequestsSinceDate(String studentId, Date sinceDate);

    /**
     * Encuentra la solicitud más reciente de un estudiante.
     * @param studentId ID del estudiante
     * @return La solicitud más reciente del estudiante, si existe
     */
    Optional<ScheduleChangeRequest> findFirstByStudentIdOrderBySubmissionDateDesc(String studentId);

    /**
     * Encuentra solicitudes con historial de revisión no vacío.
     * @return Lista de solicitudes con historial de revisión
     */
    @Query("{ 'reviewHistory.0': { $exists: true } }")
    List<ScheduleChangeRequest> findWithReviewHistory();

    /**
     * Encuentra solicitudes sin historial de revisión.
     * @return Lista de solicitudes sin historial de revisión
     */
    @Query("{ 'reviewHistory': { $size: 0 } }")
    List<ScheduleChangeRequest> findWithoutReviewHistory();

    /**
     * Encuentra solicitudes por cantidad de pasos de revisión.
     * @param minSteps Mínimo número de pasos de revisión
     * @return Lista de solicitudes con al menos minSteps pasos de revisión
     */
    @Query("{ 'reviewHistory': { $size: { $gte: ?0 } } }")
    List<ScheduleChangeRequest> findByMinReviewSteps(int minSteps);
}
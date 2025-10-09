package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.ScheduleChangeRequest;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.RequestStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Repositorio para manejar las operaciones CRUD y consultas personalizadas
 * sobre la colección de solicitudes de cambio de horario (ScheduleChangeRequest) en MongoDB.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Repository
public interface ScheduleChangeRequestRepository extends MongoRepository<ScheduleChangeRequest, String> {

    /**
     * Busca solicitudes por ID del estudiante.
     * @param studentId ID del estudiante que realizó la solicitud.
     * @return Lista de solicitudes del estudiante especificado.
     */
    List<ScheduleChangeRequest> findByStudentId(String studentId);

    /**
     * Busca solicitudes por estado actual.
     * @param status Estado de la solicitud.
     * @return Lista de solicitudes con el estado especificado.
     */
    List<ScheduleChangeRequest> findByStatus(RequestStatus status);

    /**
     * Busca solicitudes por grupo actual.
     * @param currentGroupId ID del grupo actual.
     * @return Lista de solicitudes con el grupo actual especificado.
     */
    List<ScheduleChangeRequest> findByCurrentGroupId(String currentGroupId);

    /**
     * Busca solicitudes por grupo solicitado.
     * @param requestedGroupId ID del grupo solicitado.
     * @return Lista de solicitudes con el grupo solicitado especificado.
     */
    List<ScheduleChangeRequest> findByRequestedGroupId(String requestedGroupId);

    /**
     * Busca solicitudes enviadas después de una fecha específica.
     * @param submissionDate Fecha límite inferior.
     * @return Lista de solicitudes enviadas después de la fecha indicada.
     */
    List<ScheduleChangeRequest> findBySubmissionDateAfter(Date submissionDate);

    /**
     * Busca solicitudes enviadas antes de una fecha específica.
     * @param submissionDate Fecha límite superior.
     * @return Lista de solicitudes enviadas antes de la fecha indicada.
     */
    List<ScheduleChangeRequest> findBySubmissionDateBefore(Date submissionDate);

    /**
     * Busca solicitudes por estudiante y estado.
     * @param studentId ID del estudiante.
     * @param status Estado de la solicitud.
     * @return Lista de solicitudes que cumplen con ambos criterios.
     */
    List<ScheduleChangeRequest> findByStudentIdAndStatus(String studentId, RequestStatus status);

    /**
     * Busca solicitudes resueltas después de una fecha específica.
     * @param resolutionDate Fecha límite inferior para resolución.
     * @return Lista de solicitudes resueltas después de la fecha indicada.
     */
    List<ScheduleChangeRequest> findByResolutionDateAfter(Date resolutionDate);

    /**
     * Cuenta el número de solicitudes pendientes de un estudiante.
     * @param studentId ID del estudiante.
     * @return Número de solicitudes pendientes del estudiante.
     */
    long countByStudentIdAndStatus(String studentId, RequestStatus status);
}
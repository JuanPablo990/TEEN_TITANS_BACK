package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * DTO (Data Transfer Object) para representar una solicitud de cambio de horario académico.
 * Esta clase se utiliza para transferir datos entre las capas de la aplicación sin exponer
 * la entidad completa del modelo.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleChangeRequestDTO {

    private String requestId;
    private String studentId;
    private String studentName;
    private String currentGroupId;
    private String currentGroupSection;
    private String requestedGroupId;
    private String requestedGroupSection;
    private String reason;
    private RequestStatus status;
    private Date submissionDate;
    private Date resolutionDate;
    private List<ReviewStepDTO> reviewHistory;

    /**
     * Verifica si la solicitud ha sido resuelta (aprobada o rechazada).
     * @return true si está aprobada o rechazada, false en caso contrario
     */
    public boolean isResolved() {
        return status == RequestStatus.APPROVED || status == RequestStatus.REJECTED;
    }

    /**
     * Verifica si la solicitud está pendiente o en revisión.
     * @return true si está pendiente o en revisión, false en caso contrario
     */
    public boolean isPendingOrUnderReview() {
        return status == RequestStatus.PENDING || status == RequestStatus.UNDER_REVIEW;
    }

    /**
     * Verifica si la solicitud fue cancelada.
     * @return true si está cancelada, false en caso contrario
     */
    public boolean isCancelled() {
        return status == RequestStatus.CANCELLED;
    }

    /**
     * Obtiene la duración desde la submission hasta la resolución (o hasta ahora si no está resuelta).
     * @return duración en días
     */
    public long getProcessingDurationInDays() {
        Date endDate = resolutionDate != null ? resolutionDate : new Date();
        if (submissionDate == null) return 0;
        long diff = endDate.getTime() - submissionDate.getTime();
        return diff / (1000 * 60 * 60 * 24); // Convertir milisegundos a días
    }

    /**
     * Obtiene el número de pasos de revisión en el historial.
     * @return cantidad de pasos de revisión
     */
    public int getReviewStepCount() {
        return reviewHistory != null ? reviewHistory.size() : 0;
    }

    /**
     * Verifica si la solicitud tiene un historial de revisiones.
     * @return true si tiene pasos de revisión, false en caso contrario
     */
    public boolean hasReviewHistory() {
        return reviewHistory != null && !reviewHistory.isEmpty();
    }

    /**
     * Obtiene el último paso de revisión del historial.
     * @return el último ReviewStepDTO o null si no hay historial
     */
    public ReviewStepDTO getLastReviewStep() {
        if (reviewHistory == null || reviewHistory.isEmpty()) {
            return null;
        }
        return reviewHistory.get(reviewHistory.size() - 1);
    }

    /**
     * Obtiene una descripción resumida del estado de la solicitud.
     * @return descripción del estado
     */
    public String getStatusDescription() {
        if (status == null) return "Estado desconocido";

        switch (status) {
            case PENDING:
                return "Pendiente de revisión";
            case UNDER_REVIEW:
                return "En proceso de revisión";
            case APPROVED:
                return "Aprobada";
            case REJECTED:
                return "Rechazada";
            case CANCELLED:
                return "Cancelada";
            default:
                return "Estado desconocido";
        }
    }
}
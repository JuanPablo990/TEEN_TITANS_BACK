package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.RequestStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * Representa una solicitud de cambio de horario académico.
 * Esta clase gestiona todo el proceso de solicitud de cambio de grupo,
 * incluyendo el historial de revisiones y el estado actual de la solicitud.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "schedule_change_requests")
public class ScheduleChangeRequest {
    @Id
    private String requestId;

    @DBRef
    private Student student;

    private Group currentGroup;
    private Group requestedGroup;
    private String reason;

    @Builder.Default
    private RequestStatus status = RequestStatus.PENDING;

    @Builder.Default
    private Date submissionDate = new Date();

    private Date resolutionDate;

    @DBRef
    @Builder.Default
    private List<ReviewStep> reviewHistory = new ArrayList<>();

    /**
     * Constructor para crear una nueva solicitud de cambio de horario.
     * Mantenemos este constructor específico para compatibilidad
     *
     * @param requestId el identificador único de la solicitud
     * @param student el estudiante que realiza la solicitud
     * @param currentGroup el grupo actual del estudiante
     * @param requestedGroup el grupo solicitado por el estudiante
     * @param reason la justificación para el cambio de grupo
     */
    public ScheduleChangeRequest(String requestId, Student student, Group currentGroup,
                                 Group requestedGroup, String reason) {
        this.requestId = requestId;
        this.student = student;
        this.currentGroup = currentGroup;
        this.requestedGroup = requestedGroup;
        this.reason = reason;
        this.status = RequestStatus.PENDING;
        this.submissionDate = new Date();
        this.reviewHistory = new ArrayList<>();
    }

    /**
     * Actualiza el estado de la solicitud.
     * Si el nuevo estado es APROBADO o RECHAZADO, se establece automáticamente
     * la fecha de resolución.
     *
     * @param status el nuevo estado de la solicitud
     */
    public void setStatus(RequestStatus status) {
        this.status = status;
        if (status == RequestStatus.APPROVED || status == RequestStatus.REJECTED) {
            this.resolutionDate = new Date();
        }
    }

    /**
     * Agrega un nuevo paso al historial de revisiones.
     *
     * @param reviewStep el paso de revisión a agregar al historial
     */
    public void addReviewStep(ReviewStep reviewStep) {
        this.reviewHistory.add(reviewStep);
    }
}
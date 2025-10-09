package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa una solicitud de cambio de horario académico.
 * Esta clase gestiona todo el proceso de solicitud de cambio de grupo,
 * incluyendo el historial de revisiones y el estado actual de la solicitud.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
public class ScheduleChangeRequest {
    private String requestId;
    private Student student;
    private Group currentGroup;
    private Group requestedGroup;
    private String reason;
    private RequestStatus status;
    private Date submissionDate;
    private Date resolutionDate;
    private List<ReviewStep> reviewHistory;

    /**
     * Constructor vacío para la clase ScheduleChangeRequest.
     */
    public ScheduleChangeRequest() {
        this.reviewHistory = new ArrayList<>();
    }

    /**
     * Constructor para crear una nueva solicitud de cambio de horario.
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
     * Obtiene el identificador único de la solicitud.
     *
     * @return el ID de la solicitud como String
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Obtiene el estudiante que realizó la solicitud.
     *
     * @return el objeto Student que hizo la solicitud
     */
    public Student getStudent() {
        return student;
    }

    /**
     * Obtiene el grupo actual del estudiante.
     *
     * @return el grupo actual como objeto Group
     */
    public Group getCurrentGroup() {
        return currentGroup;
    }

    /**
     * Obtiene el grupo solicitado por el estudiante.
     *
     * @return el grupo solicitado como objeto Group
     */
    public Group getRequestedGroup() {
        return requestedGroup;
    }

    /**
     * Obtiene la justificación proporcionada para el cambio.
     *
     * @return la razón del cambio como String
     */
    public String getReason() {
        return reason;
    }

    /**
     * Obtiene el estado actual de la solicitud.
     *
     * @return el estado de la solicitud como RequestStatus
     */
    public RequestStatus getStatus() {
        return status;
    }

    /**
     * Obtiene la fecha y hora en que se envió la solicitud.
     *
     * @return la fecha de envío como objeto Date
     */
    public Date getSubmissionDate() {
        return submissionDate;
    }

    /**
     * Obtiene la fecha y hora en que se resolvió la solicitud.
     * Este valor es null hasta que la solicitud sea aprobada o rechazada.
     *
     * @return la fecha de resolución como objeto Date, o null si está pendiente
     */
    public Date getResolutionDate() {
        return resolutionDate;
    }

    /**
     * Obtiene el historial completo de revisiones de la solicitud.
     *
     * @return la lista de pasos de revisión como List<ReviewStep>
     */
    public List<ReviewStep> getReviewHistory() {
        return reviewHistory;
    }

    /**
     * Actualiza el identificador único de la solicitud.
     *
     * @param requestId el nuevo identificador de la solicitud
     */
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    /**
     * Actualiza el estudiante que realizó la solicitud.
     *
     * @param student el nuevo estudiante solicitante
     */
    public void setStudent(Student student) {
        this.student = student;
    }

    /**
     * Actualiza el grupo actual del estudiante.
     *
     * @param currentGroup el nuevo grupo actual
     */
    public void setCurrentGroup(Group currentGroup) {
        this.currentGroup = currentGroup;
    }

    /**
     * Actualiza el grupo solicitado por el estudiante.
     *
     * @param requestedGroup el nuevo grupo solicitado
     */
    public void setRequestedGroup(Group requestedGroup) {
        this.requestedGroup = requestedGroup;
    }

    /**
     * Actualiza la justificación de la solicitud.
     *
     * @param reason la nueva justificación para el cambio
     */
    public void setReason(String reason) {
        this.reason = reason;
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
     * Actualiza la fecha de envío de la solicitud.
     *
     * @param submissionDate la nueva fecha de envío
     */
    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    /**
     * Actualiza la fecha de resolución de la solicitud.
     *
     * @param resolutionDate la nueva fecha de resolución
     */
    public void setResolutionDate(Date resolutionDate) {
        this.resolutionDate = resolutionDate;
    }

    /**
     * Actualiza el historial de revisiones de la solicitud.
     *
     * @param reviewHistory el nuevo historial de revisiones
     */
    public void setReviewHistory(List<ReviewStep> reviewHistory) {
        this.reviewHistory = reviewHistory;
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
package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import java.util.Date;
import java.util.List;

/**
 * DTO que representa una solicitud de cambio de horario (ScheduleChangeRequest),
 * incluyendo el historial completo de revisiones realizadas por diferentes usuarios.
 * <p>
 * Este objeto combina la información principal de la solicitud y los pasos de revisión
 * que ha tenido a lo largo del proceso, permitiendo su uso tanto para mostrar como para
 * registrar cambios en la revisión.
 * </p>
 *
 * @author Equipo Teen Titans
 * @version 1.0
 */
public class ScheduleRequestDTO {

    // ===== Datos de la solicitud de cambio de horario =====
    private String requestId;
    private String studentId;
    private String currentGroupId;
    private String requestedGroupId;
    private String reason;
    private String status;
    private Date submissionDate;
    private Date resolutionDate;

    private List<ReviewStepDetailDTO> reviewHistory;

    /**
     * Clase interna que representa un paso del proceso de revisión de una solicitud de cambio de horario.
     * Incluye información sobre quién revisó, su rol, la acción tomada y los comentarios asociados.
     */
    public static class ReviewStepDetailDTO {
        private String userId;
        private String userRole;
        private String action;
        private Date timestamp;
        private String comments;

        /**
         * Constructor vacío para facilitar la serialización y deserialización.
         */
        public ReviewStepDetailDTO() {}

        /**
         * Constructor que inicializa un paso de revisión con la información principal.
         *
         * @param userId   Identificador del usuario que realizó la acción.
         * @param userRole Rol del usuario (por ejemplo: "Docente", "Coordinador").
         * @param action   Acción tomada (por ejemplo: "APPROVED", "REJECTED").
         * @param comments Comentarios adicionales del revisor.
         */
        public ReviewStepDetailDTO(String userId, String userRole, String action, String comments) {
            this.userId = userId;
            this.userRole = userRole;
            this.action = action;
            this.comments = comments;
            this.timestamp = new Date();
        }

        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }

        public String getUserRole() { return userRole; }
        public void setUserRole(String userRole) { this.userRole = userRole; }

        public String getAction() { return action; }
        public void setAction(String action) { this.action = action; }

        public Date getTimestamp() { return timestamp; }
        public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }

        public String getComments() { return comments; }
        public void setComments(String comments) { this.comments = comments; }
    }

    /**
     * Constructor vacío por defecto.
     */
    public ScheduleRequestDTO() {}

    /**
     * Constructor que inicializa una solicitud de cambio de horario con los datos principales.
     *
     * @param requestId        Identificador único de la solicitud.
     * @param studentId        Identificador del estudiante que realiza la solicitud.
     * @param currentGroupId   Identificador del grupo actual del estudiante.
     * @param requestedGroupId Identificador del grupo solicitado.
     * @param reason           Razón por la cual el estudiante solicita el cambio.
     */
    public ScheduleRequestDTO(String requestId, String studentId, String currentGroupId,
                              String requestedGroupId, String reason) {
        this.requestId = requestId;
        this.studentId = studentId;
        this.currentGroupId = currentGroupId;
        this.requestedGroupId = requestedGroupId;
        this.reason = reason;
        this.status = "PENDING";
        this.submissionDate = new Date();
    }

    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getCurrentGroupId() { return currentGroupId; }
    public void setCurrentGroupId(String currentGroupId) { this.currentGroupId = currentGroupId; }

    public String getRequestedGroupId() { return requestedGroupId; }
    public void setRequestedGroupId(String requestedGroupId) { this.requestedGroupId = requestedGroupId; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getSubmissionDate() { return submissionDate; }
    public void setSubmissionDate(Date submissionDate) { this.submissionDate = submissionDate; }

    public Date getResolutionDate() { return resolutionDate; }
    public void setResolutionDate(Date resolutionDate) { this.resolutionDate = resolutionDate; }

    public List<ReviewStepDetailDTO> getReviewHistory() { return reviewHistory; }
    public void setReviewHistory(List<ReviewStepDetailDTO> reviewHistory) { this.reviewHistory = reviewHistory; }

    /**
     * Agrega un nuevo paso de revisión al historial de la solicitud.
     *
     * @param reviewStep Objeto {@link ReviewStepDetailDTO} que representa un paso del proceso de revisión.
     */
    public void addReviewStep(ReviewStepDetailDTO reviewStep) {
        if (this.reviewHistory == null) {
            this.reviewHistory = new java.util.ArrayList<>();
        }
        this.reviewHistory.add(reviewStep);
    }
}

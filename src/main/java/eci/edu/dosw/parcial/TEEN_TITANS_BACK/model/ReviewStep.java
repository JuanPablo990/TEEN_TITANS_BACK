package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Representa un paso individual en el proceso de revisión de una solicitud.
 * Esta clase registra cada acción realizada durante el flujo de revisión
 * de cambios en horarios académicos.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */

@Document(collection = "review_steps")
public class ReviewStep {
    private String userId;
    private UserRole userRole;
    private String action;
    private Date timestamp;
    private String comments;

    /**
     * Constructor vacío para la clase ReviewStep.
     */
    public ReviewStep() {
    }

    /**
     * Constructor para crear un nuevo paso de revisión.
     *
     * @param userId el identificador del usuario que realiza la acción
     * @param userRole el rol del usuario que realiza la acción
     * @param action la acción realizada durante la revisión
     * @param comments comentarios adicionales sobre la acción
     */
    public ReviewStep(String userId, UserRole userRole, String action, String comments) {
        this.userId = userId;
        this.userRole = userRole;
        this.action = action;
        this.comments = comments;
        this.timestamp = new Date();
    }

    /**
     * Obtiene el identificador del usuario que realizó la acción de revisión.
     *
     * @return el ID del usuario como String
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Obtiene el rol del usuario que realizó la acción de revisión.
     *
     * @return el rol del usuario como UserRole
     */
    public UserRole getUserRole() {
        return userRole;
    }

    /**
     * Obtiene la acción específica realizada durante la revisión.
     *
     * @return la acción realizada como String
     */
    public String getAction() {
        return action;
    }

    /**
     * Obtiene la fecha y hora en que se realizó la acción de revisión.
     *
     * @return la fecha y hora de la acción como objeto Date
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Obtiene los comentarios adicionales asociados a la acción de revisión.
     *
     * @return los comentarios como String
     */
    public String getComments() {
        return comments;
    }

    /**
     * Actualiza el identificador del usuario que realizó la acción.
     *
     * @param userId el nuevo identificador del usuario
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Actualiza el rol del usuario que realizó la acción.
     *
     * @param userRole el nuevo rol del usuario
     */
    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    /**
     * Actualiza la acción realizada durante la revisión.
     *
     * @param action la nueva acción realizada
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * Actualiza la fecha y hora de la acción de revisión.
     *
     * @param timestamp la nueva fecha y hora
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Actualiza los comentarios asociados a este paso de revisión.
     *
     * @param comments los nuevos comentarios a registrar
     */
    public void setComments(String comments) {
        this.comments = comments;
    }
}
package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "review_steps")
public class ReviewStep {
    private String userId;
    private UserRole userRole;
    private String action;

    @Builder.Default
    private Date timestamp = new Date();

    private String comments;

    /**
     * Constructor para crear un nuevo paso de revisión.
     * Mantenemos este constructor para compatibilidad con código existente.
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
}
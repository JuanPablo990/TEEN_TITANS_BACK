package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * DTO (Data Transfer Object) para representar un paso de revisión en las operaciones de la API.
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
public class ReviewStepDTO {

    private String userId;
    private UserRole userRole;
    private String action;
    private Date timestamp;
    private String comments;

    /**
     * Verifica si el paso de revisión tiene comentarios.
     * @return true si tiene comentarios no vacíos, false en caso contrario
     */
    public boolean hasComments() {
        return comments != null && !comments.trim().isEmpty();
    }

    /**
     * Obtiene una descripción resumida de la acción.
     * @return descripción formateada de la acción
     */
    public String getActionDescription() {
        if (action == null) return "Sin acción";
        return String.format("%s - %s", userRole != null ? userRole.name() : "Sin rol", action);
    }

    /**
     * Verifica si el paso fue realizado recientemente (en los últimos 7 días).
     * @return true si es reciente, false en caso contrario
     */
    public boolean isRecent() {
        if (timestamp == null) return false;
        Date now = new Date();
        long diff = now.getTime() - timestamp.getTime();
        return diff < (7L * 24 * 60 * 60 * 1000); // 7 días en milisegundos
    }
}
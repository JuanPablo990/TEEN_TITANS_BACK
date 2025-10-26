package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * DTO (Data Transfer Object) para representar un período académico en las operaciones de la API.
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
public class AcademicPeriodDTO {

    private String periodId;
    private String name;
    private Date startDate;
    private Date endDate;
    private Date enrollmentStart;
    private Date enrollmentEnd;
    private Date adjustmentPeriodStart;
    private Date adjustmentPeriodEnd;
    private boolean isActive;

    /**
     * Verifica si el período académico está actualmente activo basado en las fechas.
     * @return true si la fecha actual está entre startDate y endDate, false en caso contrario
     */
    public boolean isCurrentlyActive() {
        Date now = new Date();
        return startDate != null && endDate != null &&
                now.after(startDate) && now.before(endDate);
    }

    /**
     * Verifica si el período de matrícula está actualmente abierto.
     * @return true si la fecha actual está dentro del período de matrícula, false en caso contrario
     */
    public boolean isEnrollmentOpen() {
        Date now = new Date();
        return enrollmentStart != null && enrollmentEnd != null &&
                now.after(enrollmentStart) && now.before(enrollmentEnd);
    }

    /**
     * Verifica si el período de ajuste está actualmente abierto.
     * @return true si la fecha actual está dentro del período de ajuste, false en caso contrario
     */
    public boolean isAdjustmentPeriodOpen() {
        Date now = new Date();
        return adjustmentPeriodStart != null && adjustmentPeriodEnd != null &&
                now.after(adjustmentPeriodStart) && now.before(adjustmentPeriodEnd);
    }

    /**
     * Obtiene la duración del período académico en días.
     * @return número de días de duración del período, o 0 si las fechas no son válidas
     */
    public long getDurationInDays() {
        if (startDate != null && endDate != null && endDate.after(startDate)) {
            long diff = endDate.getTime() - startDate.getTime();
            return diff / (1000 * 60 * 60 * 24); // Convertir milisegundos a días
        }
        return 0;
    }
}
package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import java.util.Date;

/**
 * Representa un objeto de transferencia de datos (DTO) para un período académico.
 * Contiene información sobre las fechas clave del período, incluyendo matrícula,
 * ajustes y estado de actividad.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
public class AcademicPeriodDTO {

    private String periodId;
    private String name;
    private Date startDate;
    private Date endDate;
    private Date enrollmentStart;
    private Date enrollmentEnd;
    private Date adjustmentPeriodStart;
    private Date adjustmentPeriodEnd;
    private Boolean active;

    /**
     * Constructor vacío por defecto.
     */
    public AcademicPeriodDTO() {
    }

    /**
     * Crea una nueva instancia de {@link AcademicPeriodDTO} con todos los atributos.
     *
     * @param periodId Identificador del período académico.
     * @param name Nombre del período académico.
     * @param startDate Fecha de inicio del período.
     * @param endDate Fecha de finalización del período.
     * @param enrollmentStart Fecha de inicio del período de matrícula.
     * @param enrollmentEnd Fecha de finalización del período de matrícula.
     * @param adjustmentPeriodStart Fecha de inicio del período de ajustes.
     * @param adjustmentPeriodEnd Fecha de finalización del período de ajustes.
     * @param active Indica si el período académico está activo.
     */
    public AcademicPeriodDTO(String periodId, String name, Date startDate, Date endDate,
                             Date enrollmentStart, Date enrollmentEnd,
                             Date adjustmentPeriodStart, Date adjustmentPeriodEnd,
                             Boolean active) {
        this.periodId = periodId;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.enrollmentStart = enrollmentStart;
        this.enrollmentEnd = enrollmentEnd;
        this.adjustmentPeriodStart = adjustmentPeriodStart;
        this.adjustmentPeriodEnd = adjustmentPeriodEnd;
        this.active = active;
    }

    public String getPeriodId() { return periodId; }
    public void setPeriodId(String periodId) { this.periodId = periodId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public Date getEnrollmentStart() { return enrollmentStart; }
    public void setEnrollmentStart(Date enrollmentStart) { this.enrollmentStart = enrollmentStart; }

    public Date getEnrollmentEnd() { return enrollmentEnd; }
    public void setEnrollmentEnd(Date enrollmentEnd) { this.enrollmentEnd = enrollmentEnd; }

    public Date getAdjustmentPeriodStart() { return adjustmentPeriodStart; }
    public void setAdjustmentPeriodStart(Date adjustmentPeriodStart) { this.adjustmentPeriodStart = adjustmentPeriodStart; }

    public Date getAdjustmentPeriodEnd() { return adjustmentPeriodEnd; }
    public void setAdjustmentPeriodEnd(Date adjustmentPeriodEnd) { this.adjustmentPeriodEnd = adjustmentPeriodEnd; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    /**
     * Verifica si el período académico está actualmente activo.
     *
     * @return {@code true} si el período está activo y la fecha actual se encuentra dentro del rango;
     *         {@code false} en caso contrario.
     */
    public Boolean isCurrentlyActive() {
        if (active == null || !active) {
            return false;
        }
        Date now = new Date();
        return now.after(startDate) && now.before(endDate);
    }

    /**
     * Verifica si el período de matrícula está activo en la fecha actual.
     *
     * @return {@code true} si la fecha actual está dentro del período de matrícula;
     *         {@code false} en caso contrario.
     */
    public Boolean isEnrollmentPeriodActive() {
        Date now = new Date();
        return now.after(enrollmentStart) && now.before(enrollmentEnd);
    }

    /**
     * Verifica si el período de ajustes está activo en la fecha actual.
     *
     * @return {@code true} si la fecha actual está dentro del período de ajustes;
     *         {@code false} en caso contrario.
     */
    public Boolean isAdjustmentPeriodActive() {
        Date now = new Date();
        return now.after(adjustmentPeriodStart) && now.before(adjustmentPeriodEnd);
    }

    /**
     * Calcula la duración total del período académico en días.
     *
     * @return Número de días entre la fecha de inicio y la de finalización.
     *         Si alguna de las fechas es nula, retorna 0.
     */
    public Long getDurationInDays() {
        if (startDate == null || endDate == null) {
            return 0L;
        }
        long diff = endDate.getTime() - startDate.getTime();
        return diff / (1000 * 60 * 60 * 24);
    }

    /**
     * Obtiene el estado actual completo del período académico, basado en la fecha actual
     * y en las fases de matrícula y ajustes.
     *
     * @return Estado textual del período, como "ENROLLMENT_ACTIVE", "ADJUSTMENT_ACTIVE" o "COMPLETED".
     */
    public String getFullStatus() {
        if (active == null || !active) {
            return "INACTIVE";
        }

        Date now = new Date();

        if (now.before(enrollmentStart)) {
            return "BEFORE_ENROLLMENT";
        } else if (isEnrollmentPeriodActive()) {
            return "ENROLLMENT_ACTIVE";
        } else if (now.after(enrollmentEnd) && now.before(adjustmentPeriodStart)) {
            return "BETWEEN_ENROLLMENT_AND_ADJUSTMENT";
        } else if (isAdjustmentPeriodActive()) {
            return "ADJUSTMENT_ACTIVE";
        } else if (now.after(adjustmentPeriodEnd) && now.before(startDate)) {
            return "BEFORE_ACADEMIC_PERIOD";
        } else if (isCurrentlyActive()) {
            return "ACADEMIC_PERIOD_ACTIVE";
        } else if (now.after(endDate)) {
            return "COMPLETED";
        } else {
            return "UNKNOWN";
        }
    }

    /**
     * Devuelve una representación en cadena del objeto {@link AcademicPeriodDTO}.
     *
     * @return Cadena con información relevante del período académico.
     */
    @Override
    public String toString() {
        return "AcademicPeriodDTO{" +
                "periodId='" + periodId + '\'' +
                ", name='" + name + '\'' +
                ", active=" + active +
                ", status=" + getFullStatus() +
                '}';
    }
}

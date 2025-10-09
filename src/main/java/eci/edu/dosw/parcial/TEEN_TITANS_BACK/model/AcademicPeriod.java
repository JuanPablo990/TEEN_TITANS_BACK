package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Representa un período académico en el sistema educativo.
 * Esta clase encapsula toda la información relevante sobre un término académico específico,
 * incluyendo períodos de matrícula y ajuste.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Document(collection = "academic_periods")
public class AcademicPeriod {
    @Id
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
     * Constructor vacío. Requerido para frameworks que necesitan instanciar la clase sin parámetros.
     */
    public AcademicPeriod() {

    }

    /**
     * Constructor con todos los parámetros.
     *
     * @param periodId Identificador único del período académico
     * @param name Nombre descriptivo del período
     * @param startDate Fecha de inicio del período académico
     * @param endDate Fecha de fin del período académico
     * @param enrollmentStart Fecha de inicio del período de matrícula
     * @param enrollmentEnd Fecha de fin del período de matrícula
     * @param adjustmentPeriodStart Fecha de inicio del período de ajuste
     * @param adjustmentPeriodEnd Fecha de fin del período de ajuste
     * @param isActive Indica si el período está activo
     */
    public AcademicPeriod(String periodId, String name, Date startDate, Date endDate,
                          Date enrollmentStart, Date enrollmentEnd,
                          Date adjustmentPeriodStart, Date adjustmentPeriodEnd,
                          boolean isActive) {
        this.periodId = periodId;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.enrollmentStart = enrollmentStart;
        this.enrollmentEnd = enrollmentEnd;
        this.adjustmentPeriodStart = adjustmentPeriodStart;
        this.adjustmentPeriodEnd = adjustmentPeriodEnd;
        this.isActive = isActive;
    }


    /**
     * Obtiene el identificador único de este período académico.
     *
     * @return el ID del período como String
     */
    public String getPeriodId() {
        return periodId;
    }

    /**
     * Obtiene el nombre descriptivo de este período académico.
     *
     * @return el nombre del período como String
     */
    public String getName() {
        return name;
    }

    /**
     * Obtiene la fecha de inicio de este período académico.
     *
     * @return la fecha de inicio como objeto Date
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Obtiene la fecha de fin de este período académico.
     *
     * @return la fecha de fin como objeto Date
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Obtiene la fecha de inicio del período de matrícula.
     *
     * @return la fecha de inicio de matrícula como objeto Date
     */
    public Date getEnrollmentStart() {
        return enrollmentStart;
    }

    /**
     * Obtiene la fecha de fin del período de matrícula.
     *
     * @return la fecha de fin de matrícula como objeto Date
     */
    public Date getEnrollmentEnd() {
        return enrollmentEnd;
    }

    /**
     * Obtiene la fecha de inicio del período de ajuste.
     * El período de ajuste normalmente permite a los estudiantes agregar o eliminar cursos.
     *
     * @return la fecha de inicio del período de ajuste como objeto Date
     */
    public Date getAdjustmentPeriodStart() {
        return adjustmentPeriodStart;
    }

    /**
     * Obtiene la fecha de fin del período de ajuste.
     * El período de ajuste normalmente permite a los estudiantes agregar o eliminar cursos.
     *
     * @return la fecha de fin del período de ajuste como objeto Date
     */
    public Date getAdjustmentPeriodEnd() {
        return adjustmentPeriodEnd;
    }

    /**
     * Verifica si este período académico está actualmente activo.
     *
     * @return true si el período está activo, false en caso contrario
     */
    public boolean isActive() {
        return isActive;
    }
}

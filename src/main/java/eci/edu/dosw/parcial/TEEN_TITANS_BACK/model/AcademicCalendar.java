package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import java.util.Date;

/**
 * Representa el calendario académico con fechas importantes para registro, cancelación y cambios.
 */
public class AcademicCalendar {
    private Date registrationStartDate;
    private Date registrationEndDate;
    private Date cancellationStartDate;
    private Date cancellationEndDate;
    private Date changeStartDate;
    private Date changeEndDate;

    /**
     * Obtiene la fecha de inicio de registro.
     * @return Fecha de inicio de registro.
     */
    public Date getRegistrationStartDate() {
        return registrationStartDate;
    }

    /**
     * Obtiene la fecha de finalización de registro.
     * @return Fecha de finalización de registro.
     */
    public Date getRegistrationEndDate() {
        return registrationEndDate;
    }

    /**
     * Obtiene la fecha de inicio de cancelación.
     * @return Fecha de inicio de cancelación.
     */
    public Date getCancellationStartDate() {
        return cancellationStartDate;
    }

    /**
     * Obtiene la fecha de finalización de cancelación.
     * @return Fecha de finalización de cancelación.
     */
    public Date getCancellationEndDate() {
        return cancellationEndDate;
    }

    /**
     * Obtiene la fecha de inicio de cambios.
     * @return Fecha de inicio de cambios.
     */
    public Date getChangeStartDate() {
        return changeStartDate;
    }

    /**
     * Obtiene la fecha de finalización de cambios.
     * @return Fecha de finalización de cambios.
     */
    public Date getChangeEndDate() {
        return changeEndDate;
    }
}
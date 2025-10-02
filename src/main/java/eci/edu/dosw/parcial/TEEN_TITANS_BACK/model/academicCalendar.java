package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import java.util.Date;
import java.util.List;

public class academicCalendar {
    private Date registrationStartDate;
    private Date registrationEndDate;
    private Date cancellationStartDate;
    private Date cancellationEndDate;
    private Date changeStartDate;
    private Date changeEndDate;

    public Date getRegistrationStartDate() {
        return registrationStartDate;
    }

    public Date getRegistrationEndDate() {
        return registrationEndDate;
    }

    public Date getCancellationStartDate() {
        return cancellationStartDate;
    }

    public Date getCancellationEndDate() {
        return cancellationEndDate;
    }

    public Date getChangeStartDate() {
        return changeStartDate;
    }

    public Date getChangeEndDate() {
        return changeEndDate;
    }
}
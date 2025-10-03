package eci.edu.dosw.parcial.TEEN_TITANS_BACK.testModel;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.AcademicCalendar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class AcademicCalendarTest {

    private AcademicCalendar calendar;

    @BeforeEach
    void setUp() {
        calendar = new AcademicCalendar();
    }

    @Test
    void testInitialValuesAreNull() {
        assertAll("Fechas iniciales nulas",
                () -> assertNull(calendar.getRegistrationStartDate()),
                () -> assertNull(calendar.getRegistrationEndDate()),
                () -> assertNull(calendar.getCancellationStartDate()),
                () -> assertNull(calendar.getCancellationEndDate()),
                () -> assertNull(calendar.getChangeStartDate()),
                () -> assertNull(calendar.getChangeEndDate())
        );
    }

    @Test
    void testSetAndGetRegistrationDates() throws Exception {
        Date start = new Date();
        Date end = new Date(start.getTime() + 1000);

        setPrivateField("registrationStartDate", start);
        setPrivateField("registrationEndDate", end);

        assertAll("Registro",
                () -> assertEquals(start, calendar.getRegistrationStartDate()),
                () -> assertEquals(end, calendar.getRegistrationEndDate())
        );
    }

    @Test
    void testSetAndGetCancellationDates() throws Exception {
        Date start = new Date();
        Date end = new Date(start.getTime() + 2000);

        setPrivateField("cancellationStartDate", start);
        setPrivateField("cancellationEndDate", end);

        assertAll("Cancelación",
                () -> assertEquals(start, calendar.getCancellationStartDate()),
                () -> assertEquals(end, calendar.getCancellationEndDate())
        );
    }

    @Test
    void testSetAndGetChangeDates() throws Exception {
        Date start = new Date();
        Date end = new Date(start.getTime() + 3000);

        setPrivateField("changeStartDate", start);
        setPrivateField("changeEndDate", end);

        assertAll("Cambios",
                () -> assertEquals(start, calendar.getChangeStartDate()),
                () -> assertEquals(end, calendar.getChangeEndDate())
        );
    }

    private void setPrivateField(String fieldName, Object value) throws Exception {
        Field field = AcademicCalendar.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(calendar, value);
    }
}

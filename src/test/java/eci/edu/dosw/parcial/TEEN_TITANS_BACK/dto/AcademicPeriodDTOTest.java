package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

class AcademicPeriodDTOTest {

    @Test
    void testIsCurrentlyActive_True() {
        AcademicPeriodDTO dto = new AcademicPeriodDTO();
        dto.setActive(true);
        dto.setStartDate(new Date(System.currentTimeMillis() - 100000));
        dto.setEndDate(new Date(System.currentTimeMillis() + 100000));

        boolean result = dto.isCurrentlyActive();

        assertTrue(result);
    }

    @Test
    void testIsCurrentlyActive_False() {
        AcademicPeriodDTO dto = new AcademicPeriodDTO();
        dto.setActive(false);

        boolean result = dto.isCurrentlyActive();

        assertFalse(result);
    }

    @Test
    void testIsEnrollmentPeriodActive_True() {
        AcademicPeriodDTO dto = new AcademicPeriodDTO();
        dto.setEnrollmentStart(new Date(System.currentTimeMillis() - 100000));
        dto.setEnrollmentEnd(new Date(System.currentTimeMillis() + 100000));

        boolean result = dto.isEnrollmentPeriodActive();

        assertTrue(result);
    }

    @Test
    void testIsEnrollmentPeriodActive_False() {
        AcademicPeriodDTO dto = new AcademicPeriodDTO();
        dto.setEnrollmentStart(new Date(System.currentTimeMillis() + 100000));
        dto.setEnrollmentEnd(new Date(System.currentTimeMillis() + 200000));

        boolean result = dto.isEnrollmentPeriodActive();

        assertFalse(result);
    }

    @Test
    void testIsAdjustmentPeriodActive_True() {
        AcademicPeriodDTO dto = new AcademicPeriodDTO();
        dto.setAdjustmentPeriodStart(new Date(System.currentTimeMillis() - 100000));
        dto.setAdjustmentPeriodEnd(new Date(System.currentTimeMillis() + 100000));

        boolean result = dto.isAdjustmentPeriodActive();

        assertTrue(result);
    }

    @Test
    void testIsAdjustmentPeriodActive_False() {
        AcademicPeriodDTO dto = new AcademicPeriodDTO();
        dto.setAdjustmentPeriodStart(new Date(System.currentTimeMillis() + 100000));
        dto.setAdjustmentPeriodEnd(new Date(System.currentTimeMillis() + 200000));

        boolean result = dto.isAdjustmentPeriodActive();

        assertFalse(result);
    }

    @Test
    void testGetDurationInDays_Valid() {
        AcademicPeriodDTO dto = new AcademicPeriodDTO();
        dto.setStartDate(new Date(0));
        dto.setEndDate(new Date(86400000 * 5L));

        Long result = dto.getDurationInDays();

        assertEquals(5L, result);
    }

    @Test
    void testGetDurationInDays_Zero() {
        AcademicPeriodDTO dto = new AcademicPeriodDTO();
        dto.setStartDate(null);
        dto.setEndDate(new Date());

        Long result = dto.getDurationInDays();

        assertEquals(0L, result);
    }

    @Test
    void testGetFullStatus_EnrollmentActive() {
        AcademicPeriodDTO dto = new AcademicPeriodDTO();
        dto.setActive(true);
        dto.setEnrollmentStart(new Date(System.currentTimeMillis() - 100000));
        dto.setEnrollmentEnd(new Date(System.currentTimeMillis() + 100000));

        String result = dto.getFullStatus();

        assertEquals("ENROLLMENT_ACTIVE", result);
    }

    @Test
    void testGetFullStatus_Inactive() {
        AcademicPeriodDTO dto = new AcademicPeriodDTO();
        dto.setActive(false);

        String result = dto.getFullStatus();

        assertEquals("INACTIVE", result);
    }
}

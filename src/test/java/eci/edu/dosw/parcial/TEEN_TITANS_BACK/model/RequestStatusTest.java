package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequestStatusTest {

    @Test
    void testEnumValues() {
        RequestStatus[] statuses = RequestStatus.values();
        assertEquals(4, statuses.length, "Debe haber exactamente 4 estados en RequestStatus");

        assertEquals(RequestStatus.PENDING, statuses[0]);
        assertEquals(RequestStatus.UNDER_REVIEW, statuses[1]);
        assertEquals(RequestStatus.APPROVED, statuses[2]);
        assertEquals(RequestStatus.REJECTED, statuses[3]);
    }

    @Test
    void testValueOf() {
        assertEquals(RequestStatus.PENDING, RequestStatus.valueOf("PENDING"));
        assertEquals(RequestStatus.UNDER_REVIEW, RequestStatus.valueOf("UNDER_REVIEW"));
        assertEquals(RequestStatus.APPROVED, RequestStatus.valueOf("APPROVED"));
        assertEquals(RequestStatus.REJECTED, RequestStatus.valueOf("REJECTED"));
    }

    @Test
    void testValueOfThrowsExceptionForInvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> {
            RequestStatus.valueOf("INVALID_STATUS");
        });
    }
}

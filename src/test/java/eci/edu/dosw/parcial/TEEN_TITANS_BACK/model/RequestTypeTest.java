package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequestTypeTest {

    @Test
    void testEnumValues() {
        RequestType[] types = RequestType.values();
        assertEquals(6, types.length, "Debe haber exactamente 6 tipos de solicitudes");

        assertEquals(RequestType.REQUEST_FOR_GROUP_CHANGE, types[0]);
        assertEquals(RequestType.REQUEST_FOR_COURSE_CANCELATION, types[1]);
        assertEquals(RequestType.REQUEST_FOR_LATE_REGISTRATION, types[2]);
        assertEquals(RequestType.REQUEST_FOR_STUDENT_GROUP_EXCHANGE, types[3]);
        assertEquals(RequestType.COMPOSITE_REQUEST, types[4]);
        assertEquals(RequestType.REQUEST_FOR_EXCEPTIONAL_CIRCUMSTANCES, types[5]);
    }

    @Test
    void testValueOf() {
        assertEquals(RequestType.REQUEST_FOR_GROUP_CHANGE, RequestType.valueOf("REQUEST_FOR_GROUP_CHANGE"));
        assertEquals(RequestType.REQUEST_FOR_COURSE_CANCELATION, RequestType.valueOf("REQUEST_FOR_COURSE_CANCELATION"));
        assertEquals(RequestType.REQUEST_FOR_LATE_REGISTRATION, RequestType.valueOf("REQUEST_FOR_LATE_REGISTRATION"));
        assertEquals(RequestType.REQUEST_FOR_STUDENT_GROUP_EXCHANGE, RequestType.valueOf("REQUEST_FOR_STUDENT_GROUP_EXCHANGE"));
        assertEquals(RequestType.COMPOSITE_REQUEST, RequestType.valueOf("COMPOSITE_REQUEST"));
        assertEquals(RequestType.REQUEST_FOR_EXCEPTIONAL_CIRCUMSTANCES, RequestType.valueOf("REQUEST_FOR_EXCEPTIONAL_CIRCUMSTANCES"));
    }

    @Test
    void testValueOfThrowsExceptionForInvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> {
            RequestType.valueOf("INVALID_TYPE");
        });
    }
}

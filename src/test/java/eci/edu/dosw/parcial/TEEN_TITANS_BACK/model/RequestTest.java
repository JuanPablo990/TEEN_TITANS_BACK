package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class RequestTest {

    private Request request;

    @BeforeEach
    void setUp() {
        request = new Request();
    }

    @Test
    void testInitialValues() {
        assertAll("Valores iniciales",
                () -> assertNull(request.getId()),
                () -> assertNull(request.getType()),
                () -> assertNull(request.getStudent()),
                () -> assertNull(request.getOriginalSubject()),
                () -> assertNull(request.getTargetSubject()),
                () -> assertEquals(0, request.getOriginalGroup()),
                () -> assertEquals(0, request.getTargetGroup()),
                () -> assertNull(request.getStatus()),
                () -> assertEquals(0, request.getPriority()),
                () -> assertNull(request.getCreationDate()),
                () -> assertNull(request.getSolveDate()),
                () -> assertNull(request.getObservations())
        );
    }

    @Test
    void testSetAndGetBasicValues() throws Exception {
        setPrivateField("id", "REQ-101");
        setPrivateField("originalGroup", 10);
        setPrivateField("targetGroup", 20);
        setPrivateField("priority", 1);
        setPrivateField("observations", "Cambio por cruce de horarios");

        assertAll("Básicos",
                () -> assertEquals("REQ-101", request.getId()),
                () -> assertEquals(10, request.getOriginalGroup()),
                () -> assertEquals(20, request.getTargetGroup()),
                () -> assertEquals(1, request.getPriority()),
                () -> assertEquals("Cambio por cruce de horarios", request.getObservations())
        );
    }

    @Test
    void testSetAndGetDates() throws Exception {
        Date creation = new Date();
        Date resolution = new Date(creation.getTime() + 1000);

        setPrivateField("creationDate", creation);
        setPrivateField("resolutionDate", resolution);

        assertAll("Fechas",
                () -> assertEquals(creation, request.getCreationDate()),
                () -> assertEquals(resolution, request.getSolveDate())
        );
    }

    @Test
    void testDifferentInstancesNotSame() throws Exception {
        Request anotherRequest = new Request();
        setPrivateField("id", "REQ-200");

        assertNotSame(request, anotherRequest);
        assertNotEquals(anotherRequest.getId(), request.getId());
    }


    private void setPrivateField(String fieldName, Object value) throws Exception {
        Field field = Request.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(request, value);
    }
}

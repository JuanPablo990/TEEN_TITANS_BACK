package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class DeanTest {

    private Dean dean;

    @BeforeEach
    void setUp() {
        dean = new Dean();
    }

    @Test
    void testInitialValueIsNull() {
        assertNull(dean.getFaculty(), "La facultad debe ser null al inicio");
    }

    @Test
    void testSetAndGetFaculty() throws Exception {
        setPrivateField("faculty", "Ingeniería de Sistemas");
        assertEquals("Ingeniería de Sistemas", dean.getFaculty());
    }

    @Test
    void testDifferentInstancesNotSame() throws Exception {
        Dean anotherDean = new Dean();
        setPrivateField("faculty", "Administración");

        assertNotSame(dean, anotherDean);
        assertNotEquals(anotherDean.getFaculty(), dean.getFaculty());
    }

    @Test
    void testIsInstanceOfUser() {
        assertInstanceOf(User.class, dean);
    }

    private void setPrivateField(String fieldName, Object value) throws Exception {
        Field field = Dean.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(dean, value);
    }
}

package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class SubjectTest {

    private Subject subject;

    @BeforeEach
    void setUp() throws Exception {
        subject = new Subject();

        setField("group", 101);
        setField("name", "Programación Orientada a Objetos");
        setField("credits", 4);
        setField("quotas", 40);
        setField("teacher", "Dr. Strange");
        setField("registered", 35);
        setField("classTime", "Lunes 8:00 - 10:00");
    }

    private void setField(String fieldName, Object value) throws Exception {
        Field field = Subject.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(subject, value);
    }

    @Test
    void testBasicAssertions() {
        assertEquals(101, subject.getGroup(), "El grupo debe coincidir");
        assertNotEquals(999, subject.getGroup(), "El grupo no debe ser 999");

        assertTrue(subject.getCredits() > 0, "Los créditos deben ser mayores a 0");
        assertFalse(subject.getName().isEmpty(), "El nombre no debe estar vacío");

        assertNotNull(subject.getTeacher(), "El profesor no debe ser null");
    }

    @Test
    void testInstanceAndSame() {
        assertInstanceOf(Subject.class, subject, "El objeto debe ser instancia de Subject");

        Subject sameReference = subject;
        assertSame(subject, sameReference, "Ambas referencias deben apuntar al mismo objeto");

        Subject differentSubject = new Subject();
        assertNotSame(subject, differentSubject, "Debe ser un objeto distinto");
    }

    @Test
    void testAllPropertiesTogether() {
        assertAll("Validar propiedades de Subject",
                () -> assertEquals(101, subject.getGroup()),
                () -> assertEquals("Programación Orientada a Objetos", subject.getName()),
                () -> assertEquals(4, subject.getCredits()),
                () -> assertEquals(40, subject.getQuotas()),
                () -> assertEquals("Dr. Strange", subject.getTeacher()),
                () -> assertEquals(35, subject.getRegistered()),
                () -> assertEquals("Lunes 8:00 - 10:00", subject.getClassTime())
        );
    }

    @Test
    void testDoesNotThrow() {
        assertDoesNotThrow(() -> subject.getClassTime(), "No debería lanzar excepción al acceder a classTime");
    }

    @Test
    void testFailExample() {
        if (subject.getCredits() < 0) {
            fail("Los créditos no deben ser negativos");
        }
    }
}

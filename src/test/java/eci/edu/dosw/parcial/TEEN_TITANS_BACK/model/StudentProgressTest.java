package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class StudentProgressTest {

    private StudentProgress progress;

    @BeforeEach
    void setUp() throws Exception {
        progress = new StudentProgress();

        setField("approvedCredits", 80);
        setField("pendingCredits", 20);
        setField("approvedCourses", 25);
        setField("pendingCourses", 5);
        setField("failedCourses", 2);
        setField("enrolledCourses", 6);
    }

    private void setField(String fieldName, Object value) throws Exception {
        Field field = StudentProgress.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(progress, value);
    }

    @Test
    void testBasicAssertions() {
        assertEquals(80, progress.getApprovedCredits(), "Debe coincidir el número de créditos aprobados");
        assertNotEquals(0, progress.getPendingCredits(), "Los créditos pendientes no deben ser cero");

        assertTrue(progress.getApprovedCourses() > progress.getFailedCourses(), "Debe haber más cursos aprobados que reprobados");
        assertFalse(progress.getPendingCourses() < 0, "Los cursos pendientes no deben ser negativos");

        assertNotNull(progress, "El objeto StudentProgress no debe ser null");
    }

    @Test
    void testInstanceAndSame() {
        assertInstanceOf(StudentProgress.class, progress, "El objeto debe ser instancia de StudentProgress");

        StudentProgress sameReference = progress;
        assertSame(progress, sameReference, "Ambas referencias deben apuntar al mismo objeto");

        StudentProgress differentProgress = new StudentProgress();
        assertNotSame(progress, differentProgress, "Deben ser objetos diferentes");
    }

    @Test
    void testAllPropertiesTogether() {
        assertAll("Validar propiedades de StudentProgress",
                () -> assertEquals(80, progress.getApprovedCredits()),
                () -> assertEquals(20, progress.getPendingCredits()),
                () -> assertEquals(25, progress.getApprovedCourses()),
                () -> assertEquals(5, progress.getPendingCourses()),
                () -> assertEquals(2, progress.getFailedCourses()),
                () -> assertEquals(6, progress.getEnrolledCourses())
        );
    }

    @Test
    void testDoesNotThrow() {
        assertDoesNotThrow(() -> progress.getApprovedCourses(), "No debería lanzar excepción al acceder a approvedCourses");
    }

    @Test
    void testFailExample() {
        if (progress.getApprovedCredits() < 0) {
            fail("Los créditos aprobados no deben ser negativos");
        }
    }
}

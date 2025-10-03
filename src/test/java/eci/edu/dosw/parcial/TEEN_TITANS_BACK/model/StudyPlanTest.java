package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class StudyPlanTest {

    private StudyPlan plan;

    @BeforeEach
    void setUp() throws Exception {
        plan = new StudyPlan();

        setField("id", 101);
        setField("name", "Ingeniería de Sistemas");
        setField("totalCredits", 160);
        setField("totalCourses", 50);
    }

    private void setField(String fieldName, Object value) throws Exception {
        Field field = StudyPlan.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(plan, value);
    }

    @Test
    void testBasicAssertions() {
        assertEquals(101, plan.getId(), "El id del plan debe coincidir");
        assertNotEquals(999, plan.getId(), "El id no debe coincidir con un valor incorrecto");

        assertTrue(plan.getTotalCredits() > 0, "Los créditos deben ser mayores a 0");
        assertFalse(plan.getName().isEmpty(), "El nombre no debe estar vacío");

        assertNotNull(plan.getName(), "El nombre del plan no debe ser null");
    }

    @Test
    void testInstanceAndSame() {
        assertInstanceOf(StudyPlan.class, plan, "El objeto debe ser instancia de StudyPlan");

        StudyPlan sameReference = plan;
        assertSame(plan, sameReference, "Ambas referencias deben apuntar al mismo objeto");

        StudyPlan differentPlan = new StudyPlan();
        assertNotSame(plan, differentPlan, "Deben ser objetos distintos");
    }

    @Test
    void testAllPropertiesTogether() {
        assertAll("Validar propiedades de StudyPlan",
                () -> assertEquals(101, plan.getId()),
                () -> assertEquals("Ingeniería de Sistemas", plan.getName()),
                () -> assertEquals(160, plan.getTotalCredits()),
                () -> assertEquals(50, plan.getTotalCourses())
        );
    }

    @Test
    void testDoesNotThrow() {
        assertDoesNotThrow(() -> plan.getTotalCredits(), "No debería lanzar excepción al acceder a totalCredits");
    }

    @Test
    void testFailExample() {
        if (plan.getTotalCourses() < 0) {
            fail("El número de cursos no debe ser negativo");
        }
    }
}

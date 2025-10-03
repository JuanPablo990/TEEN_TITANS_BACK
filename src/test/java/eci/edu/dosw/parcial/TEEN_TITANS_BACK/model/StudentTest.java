package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    private Student student;

    @BeforeEach
    void setUp() throws Exception {
        student = new Student();

        setField("id", "ST123");
        setField("standard", 5);
        setField("career", "Ingeniería de Sistemas");
        setField("gatSemester", "2025-1");
        setField("gatCareer", "Ciencias Computacionales");
    }

    private void setField(String fieldName, Object value) throws Exception {
        Field field = Student.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(student, value);
    }

    @Test
    void testBasicAssertions() {
        assertEquals("ST123", student.getId(), "El id debe coincidir con el valor asignado");
        assertNotEquals("ST999", student.getId(), "El id no debe coincidir con otro valor");

        assertTrue(student.getStandard() > 0, "El estándar debe ser mayor que 0");
        assertFalse(student.getCareer().isEmpty(), "La carrera no debe estar vacía");

        assertNotNull(student.getCareer(), "La carrera no debe ser null");
    }

    @Test
    void testInstanceAndSame() {
        assertInstanceOf(Student.class, student, "El objeto debe ser una instancia de Student");

        Student sameReference = student;
        assertSame(student, sameReference, "Ambas referencias deben apuntar al mismo objeto");

        Student differentStudent = new Student();
        assertNotSame(student, differentStudent, "Debe ser un objeto distinto");
    }

    @Test
    void testAllPropertiesTogether() {
        assertAll("Validar propiedades del estudiante",
                () -> assertEquals("ST123", student.getId()),
                () -> assertEquals(5, student.getStandard()),
                () -> assertEquals("Ingeniería de Sistemas", student.getCareer()),
                () -> assertEquals("2025-1", student.getGatSemester()),
                () -> assertEquals("Ciencias Computacionales", student.getGatCareer())
        );
    }

    @Test
    void testFailExample() {
        if (student.getId() == null) {
            fail("El estudiante debería tener un id válido");
        }
    }

    @Test
    void testDoesNotThrow() {
        assertDoesNotThrow(() -> student.getCareer(), "No debería lanzar excepción al acceder a career");
    }
}

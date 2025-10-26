package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    private Student student1;
    private Student student2;
    private Student student3;

    @BeforeEach
    void setUp() {
        student1 = new Student("1", "Peter Parker", "peter.parker@titans.edu", "password123",
                "Computer Science", 5);
        student1.setGradeAverage(4.2);

        student2 = new Student("2", "Gwen Stacy", "gwen.stacy@titans.edu", "password456",
                "Physics", 6);
        student2.setGradeAverage(4.5);

        student3 = new Student("1", "Peter Parker", "peter.parker@titans.edu", "password123",
                "Computer Science", 5);
        student3.setGradeAverage(4.2);
    }

    @Test
    @DisplayName("Caso exitoso - Constructor inicializa correctamente los campos")
    void testConstructor_Exitoso() {
        assertAll("Verificar inicialización del constructor",
                () -> assertEquals("1", student1.getId()),
                () -> assertEquals("Peter Parker", student1.getName()),
                () -> assertEquals("peter.parker@titans.edu", student1.getEmail()),
                () -> assertEquals("password123", student1.getPassword()),
                () -> assertEquals(UserRole.STUDENT, student1.getRole()),
                () -> assertEquals("Computer Science", student1.getAcademicProgram()),
                () -> assertEquals(5, student1.getSemester()),
                () -> assertEquals(4.2, student1.getGradeAverage()),
                () -> assertTrue(student1.isActive())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Constructor asigna gradeAverage por defecto a 0.0")
    void testConstructor_GradeAveragePorDefecto() {
        Student student = new Student("4", "Miles Morales", "miles.morales@titans.edu", "password789",
                "Engineering", 3);

        assertAll("Verificar gradeAverage por defecto",
                () -> assertEquals("4", student.getId()),
                () -> assertEquals("Miles Morales", student.getName()),
                () -> assertEquals(0.0, student.getGradeAverage()),
                () -> assertEquals(UserRole.STUDENT, student.getRole())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Getters y Setters funcionan correctamente")
    void testGettersAndSetters_Exitoso() {
        Student student = new Student();

        student.setId("10");
        student.setName("Mary Jane Watson");
        student.setEmail("maryjane.watson@titans.edu");
        student.setPassword("newpassword");
        student.setAcademicProgram("Biology");
        student.setSemester(4);
        student.setGradeAverage(3.8);
        student.setActive(false);

        assertAll("Verificar getters y setters",
                () -> assertEquals("10", student.getId()),
                () -> assertEquals("Mary Jane Watson", student.getName()),
                () -> assertEquals("maryjane.watson@titans.edu", student.getEmail()),
                () -> assertEquals("newpassword", student.getPassword()),
                () -> assertEquals("Biology", student.getAcademicProgram()),
                () -> assertEquals(4, student.getSemester()),
                () -> assertEquals(3.8, student.getGradeAverage()),
                () -> assertFalse(student.isActive())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Equals retorna true para objetos iguales")
    void testEquals_ObjetosIguales() {
        assertEquals(student1, student3);
    }

    @Test
    @DisplayName("Caso exitoso - Equals retorna false para objetos diferentes")
    void testEquals_ObjetosDiferentes() {
        assertNotEquals(student1, student2);
    }

    @Test
    @DisplayName("Caso exitoso - HashCode es consistente para objetos iguales")
    void testHashCode_Consistente() {
        assertEquals(student1.hashCode(), student3.hashCode());
    }

    @Test
    @DisplayName("Caso exitoso - HashCode es diferente para objetos distintos")
    void testHashCode_Diferente() {
        assertNotEquals(student1.hashCode(), student2.hashCode());
    }



    @Test
    @DisplayName("Caso borde - Constructor con null en academicProgram y semester")
    void testConstructor_CamposNull() {
        Student student = new Student("3", "Harry Osborn", "harry.osborn@titans.edu", "password789",
                null, null);

        assertAll("Verificar constructor con campos null",
                () -> assertEquals("3", student.getId()),
                () -> assertEquals("Harry Osborn", student.getName()),
                () -> assertEquals("harry.osborn@titans.edu", student.getEmail()),
                () -> assertNull(student.getAcademicProgram()),
                () -> assertNull(student.getSemester()),
                () -> assertEquals(0.0, student.getGradeAverage()),
                () -> assertEquals(UserRole.STUDENT, student.getRole())
        );
    }

    @Test
    @DisplayName("Caso borde - Set academicProgram, semester y gradeAverage a null")
    void testSetCampos_Null() {
        student1.setAcademicProgram(null);
        student1.setSemester(null);
        student1.setGradeAverage(null);

        assertAll("Verificar set a null",
                () -> assertNull(student1.getAcademicProgram()),
                () -> assertNull(student1.getSemester()),
                () -> assertNull(student1.getGradeAverage())
        );
    }

    @Test
    @DisplayName("Caso borde - Set academicProgram vacío")
    void testSetAcademicProgram_Vacio() {
        student1.setAcademicProgram("");
        assertEquals("", student1.getAcademicProgram());
    }

    @Test
    @DisplayName("Caso borde - Semester con valor cero")
    void testSemester_Cero() {
        student1.setSemester(0);
        assertEquals(0, student1.getSemester());
    }

    @Test
    @DisplayName("Caso borde - Semester con valor negativo")
    void testSemester_Negativo() {
        student1.setSemester(-1);
        assertEquals(-1, student1.getSemester());
    }

    @Test
    @DisplayName("Caso borde - GradeAverage con valor cero")
    void testGradeAverage_Cero() {
        student1.setGradeAverage(0.0);
        assertEquals(0.0, student1.getGradeAverage());
    }

    @Test
    @DisplayName("Caso borde - GradeAverage con valor negativo")
    void testGradeAverage_Negativo() {
        student1.setGradeAverage(-1.5);
        assertEquals(-1.5, student1.getGradeAverage());
    }

    @Test
    @DisplayName("Caso borde - GradeAverage con valor máximo")
    void testGradeAverage_ValorMaximo() {
        student1.setGradeAverage(5.0);
        assertEquals(5.0, student1.getGradeAverage());
    }

    @Test
    @DisplayName("Caso borde - GradeAverage con valor superior al máximo")
    void testGradeAverage_SuperiorAlMaximo() {
        student1.setGradeAverage(6.0);
        assertEquals(6.0, student1.getGradeAverage());
    }

    @Test
    @DisplayName("Caso exitoso - Herencia de User funciona correctamente")
    void testHerencia_Exitoso() {
        assertAll("Verificar herencia de User",
                () -> assertTrue(student1 instanceof User),
                () -> assertEquals(UserRole.STUDENT, student1.getRole()),
                () -> assertNotNull(student1.getCreatedAt()),
                () -> assertNotNull(student1.getUpdatedAt())
        );
    }

    @Test
    @DisplayName("Caso borde - Comparación con null")
    void testEquals_ConNull() {
        assertNotEquals(null, student1);
    }

    @Test
    @DisplayName("Caso borde - Comparación con objeto de diferente clase")
    void testEquals_DiferenteClase() {
        Object otroObjeto = new Object();
        assertNotEquals(student1, otroObjeto);
    }

    @Test
    @DisplayName("Caso exitoso - Campos heredados se pueden modificar")
    void testCamposHeredados_Modificacion() {
        student1.setName("Peter Parker Modificado");
        student1.setEmail("peter.modificado@titans.edu");
        student1.setActive(false);

        assertAll("Verificar modificación de campos heredados",
                () -> assertEquals("Peter Parker Modificado", student1.getName()),
                () -> assertEquals("peter.modificado@titans.edu", student1.getEmail()),
                () -> assertFalse(student1.isActive())
        );
    }

    @Test
    @DisplayName("Caso borde - Constructor con valores límite")
    void testConstructor_ValoresLimite() {
        Student student = new Student("", "", "", "", "", 0);

        assertAll("Verificar constructor con valores límite",
                () -> assertEquals("", student.getId()),
                () -> assertEquals("", student.getName()),
                () -> assertEquals("", student.getEmail()),
                () -> assertEquals("", student.getPassword()),
                () -> assertEquals("", student.getAcademicProgram()),
                () -> assertEquals(0, student.getSemester()),
                () -> assertEquals(0.0, student.getGradeAverage())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Actualización de campos específicos de Student")
    void testActualizacionCamposEspecificos() {
        student1.setAcademicProgram("Software Engineering");
        student1.setSemester(6);
        student1.setGradeAverage(4.8);

        assertAll("Verificar actualización de campos específicos",
                () -> assertEquals("Software Engineering", student1.getAcademicProgram()),
                () -> assertEquals(6, student1.getSemester()),
                () -> assertEquals(4.8, student1.getGradeAverage())
        );
    }

    @Test
    @DisplayName("Caso borde - AcademicProgram con espacios en blanco")
    void testAcademicProgram_ConEspacios() {
        student1.setAcademicProgram("  Computer Science and Engineering  ");
        assertEquals("  Computer Science and Engineering  ", student1.getAcademicProgram());
    }

    @Test
    @DisplayName("Caso borde - Semester con valor máximo")
    void testSemester_ValorMaximo() {
        student1.setSemester(10);
        assertEquals(10, student1.getSemester());
    }

    @Test
    @DisplayName("Caso borde - GradeAverage con decimales")
    void testGradeAverage_Decimales() {
        student1.setGradeAverage(3.75);
        assertEquals(3.75, student1.getGradeAverage());
    }

    @Test
    @DisplayName("Caso borde - GradeAverage con muchos decimales")
    void testGradeAverage_MuchosDecimales() {
        student1.setGradeAverage(4.123456789);
        assertEquals(4.123456789, student1.getGradeAverage());
    }

    @Test
    @DisplayName("Caso borde - Semester nulo y luego asignado")
    void testSemester_NuloLuegoAsignado() {
        Student student = new Student("5", "Flash Thompson", "flash.thompson@titans.edu", "password999",
                "Business", null);
        assertNull(student.getSemester());

        student.setSemester(2);
        assertEquals(2, student.getSemester());
    }

    @Test
    @DisplayName("Caso borde - AcademicProgram nulo y luego asignado")
    void testAcademicProgram_NuloLuegoAsignado() {
        Student student = new Student("6", "Betty Brant", "betty.brant@titans.edu", "password888",
                null, 3);
        assertNull(student.getAcademicProgram());

        student.setAcademicProgram("Journalism");
        assertEquals("Journalism", student.getAcademicProgram());
    }

    @Test
    @DisplayName("Caso borde - GradeAverage nulo y luego asignado")
    void testGradeAverage_NuloLuegoAsignado() {
        student1.setGradeAverage(null);
        assertNull(student1.getGradeAverage());

        student1.setGradeAverage(3.9);
        assertEquals(3.9, student1.getGradeAverage());
    }
}
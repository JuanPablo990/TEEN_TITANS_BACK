package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeanTest {

    private Dean dean1;
    private Dean dean2;
    private Dean dean3;

    @BeforeEach
    void setUp() {
        dean1 = new Dean("1", "Dr. Charles Xavier", "charles.xavier@titans.edu", "password123",
                "School of Mutant Studies", "Office 101");
        dean2 = new Dean("2", "Dr. Stephen Strange", "stephen.strange@titans.edu", "password456",
                "School of Mystic Arts", "Office 202");
        dean3 = new Dean("1", "Dr. Charles Xavier", "charles.xavier@titans.edu", "password123",
                "School of Mutant Studies", "Office 101");
    }

    @Test
    @DisplayName("Caso exitoso - Constructor inicializa correctamente los campos")
    void testConstructor_Exitoso() {
        assertAll("Verificar inicialización del constructor",
                () -> assertEquals("1", dean1.getId()),
                () -> assertEquals("Dr. Charles Xavier", dean1.getName()),
                () -> assertEquals("charles.xavier@titans.edu", dean1.getEmail()),
                () -> assertEquals("password123", dean1.getPassword()),
                () -> assertEquals(UserRole.DEAN, dean1.getRole()),
                () -> assertEquals("School of Mutant Studies", dean1.getFaculty()),
                () -> assertEquals("Office 101", dean1.getOfficeLocation()),
                () -> assertTrue(dean1.isActive())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Getters y Setters funcionan correctamente")
    void testGettersAndSetters_Exitoso() {
        Dean dean = new Dean();

        dean.setId("10");
        dean.setName("Dr. Henry McCoy");
        dean.setEmail("henry.mccoy@titans.edu");
        dean.setPassword("newpassword");
        dean.setFaculty("School of Science");
        dean.setOfficeLocation("Office 303");
        dean.setActive(false);

        assertAll("Verificar getters y setters",
                () -> assertEquals("10", dean.getId()),
                () -> assertEquals("Dr. Henry McCoy", dean.getName()),
                () -> assertEquals("henry.mccoy@titans.edu", dean.getEmail()),
                () -> assertEquals("newpassword", dean.getPassword()),
                () -> assertEquals("School of Science", dean.getFaculty()),
                () -> assertEquals("Office 303", dean.getOfficeLocation()),
                () -> assertFalse(dean.isActive())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Equals retorna true para objetos iguales")
    void testEquals_ObjetosIguales() {
        assertEquals(dean1, dean3);
    }

    @Test
    @DisplayName("Caso exitoso - Equals retorna false para objetos diferentes")
    void testEquals_ObjetosDiferentes() {
        assertNotEquals(dean1, dean2);
    }

    @Test
    @DisplayName("Caso exitoso - HashCode es consistente para objetos iguales")
    void testHashCode_Consistente() {
        assertEquals(dean1.hashCode(), dean3.hashCode());
    }

    @Test
    @DisplayName("Caso exitoso - HashCode es diferente para objetos distintos")
    void testHashCode_Diferente() {
        assertNotEquals(dean1.hashCode(), dean2.hashCode());
    }



    @Test
    @DisplayName("Caso borde - Constructor con null en faculty y officeLocation")
    void testConstructor_FacultyYOfficeLocationNull() {
        Dean dean = new Dean("3", "Dr. Reed Richards", "reed.richards@titans.edu", "password789", null, null);

        assertAll("Verificar constructor con faculty y officeLocation null",
                () -> assertEquals("3", dean.getId()),
                () -> assertEquals("Dr. Reed Richards", dean.getName()),
                () -> assertEquals("reed.richards@titans.edu", dean.getEmail()),
                () -> assertNull(dean.getFaculty()),
                () -> assertNull(dean.getOfficeLocation()),
                () -> assertEquals(UserRole.DEAN, dean.getRole())
        );
    }

    @Test
    @DisplayName("Caso borde - Set faculty y officeLocation a null")
    void testSetFacultyYOfficeLocation_Null() {
        dean1.setFaculty(null);
        dean1.setOfficeLocation(null);

        assertAll("Verificar set a null",
                () -> assertNull(dean1.getFaculty()),
                () -> assertNull(dean1.getOfficeLocation())
        );
    }

    @Test
    @DisplayName("Caso borde - Set faculty y officeLocation vacíos")
    void testSetFacultyYOfficeLocation_Vacios() {
        dean1.setFaculty("");
        dean1.setOfficeLocation("");

        assertAll("Verificar set vacíos",
                () -> assertEquals("", dean1.getFaculty()),
                () -> assertEquals("", dean1.getOfficeLocation())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Herencia de User funciona correctamente")
    void testHerencia_Exitoso() {
        assertAll("Verificar herencia de User",
                () -> assertTrue(dean1 instanceof User),
                () -> assertEquals(UserRole.DEAN, dean1.getRole()),
                () -> assertNotNull(dean1.getCreatedAt()),
                () -> assertNotNull(dean1.getUpdatedAt())
        );
    }

    @Test
    @DisplayName("Caso borde - Comparación con null")
    void testEquals_ConNull() {
        assertNotEquals(null, dean1);
    }

    @Test
    @DisplayName("Caso borde - Comparación con objeto de diferente clase")
    void testEquals_DiferenteClase() {
        Object otroObjeto = new Object();
        assertNotEquals(dean1, otroObjeto);
    }

    @Test
    @DisplayName("Caso exitoso - Campos heredados se pueden modificar")
    void testCamposHeredados_Modificacion() {
        dean1.setName("Dr. Charles Xavier Modificado");
        dean1.setEmail("charles.modificado@titans.edu");
        dean1.setActive(false);

        assertAll("Verificar modificación de campos heredados",
                () -> assertEquals("Dr. Charles Xavier Modificado", dean1.getName()),
                () -> assertEquals("charles.modificado@titans.edu", dean1.getEmail()),
                () -> assertFalse(dean1.isActive())
        );
    }

    @Test
    @DisplayName("Caso borde - Constructor con valores límite")
    void testConstructor_ValoresLimite() {
        Dean dean = new Dean("", "", "", "", "", "");

        assertAll("Verificar constructor con valores límite",
                () -> assertEquals("", dean.getId()),
                () -> assertEquals("", dean.getName()),
                () -> assertEquals("", dean.getEmail()),
                () -> assertEquals("", dean.getPassword()),
                () -> assertEquals("", dean.getFaculty()),
                () -> assertEquals("", dean.getOfficeLocation())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Actualización de campos específicos de Dean")
    void testActualizacionCamposEspecificos() {
        dean1.setFaculty("School of Advanced Mutant Studies");
        dean1.setOfficeLocation("Office 501 - Executive Suite");

        assertAll("Verificar actualización de campos específicos",
                () -> assertEquals("School of Advanced Mutant Studies", dean1.getFaculty()),
                () -> assertEquals("Office 501 - Executive Suite", dean1.getOfficeLocation())
        );
    }

    @Test
    @DisplayName("Caso borde - Faculty con espacios en blanco")
    void testFaculty_ConEspacios() {
        dean1.setFaculty("  School of Engineering  ");

        assertEquals("  School of Engineering  ", dean1.getFaculty());
    }

    @Test
    @DisplayName("Caso borde - OfficeLocation con caracteres especiales")
    void testOfficeLocation_CaracteresEspeciales() {
        dean1.setOfficeLocation("Office #A-100 (Building C)");

        assertEquals("Office #A-100 (Building C)", dean1.getOfficeLocation());
    }
}
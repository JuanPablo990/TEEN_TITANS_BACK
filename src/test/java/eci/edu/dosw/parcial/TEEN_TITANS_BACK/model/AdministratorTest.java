package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdministratorTest {

    private Administrator admin1;
    private Administrator admin2;
    private Administrator admin3;

    @BeforeEach
    void setUp() {
        admin1 = new Administrator("1", "Bruce Wayne", "bruce.wayne@titans.edu", "password123", "Security");
        admin2 = new Administrator("2", "Alfred Pennyworth", "alfred.pennyworth@titans.edu", "password456", "Operations");
        admin3 = new Administrator("1", "Bruce Wayne", "bruce.wayne@titans.edu", "password123", "Security");
    }

    @Test
    @DisplayName("Caso exitoso - Constructor inicializa correctamente los campos")
    void testConstructor_Exitoso() {
        assertAll("Verificar inicialización del constructor",
                () -> assertEquals("1", admin1.getId()),
                () -> assertEquals("Bruce Wayne", admin1.getName()),
                () -> assertEquals("bruce.wayne@titans.edu", admin1.getEmail()),
                () -> assertEquals("password123", admin1.getPassword()),
                () -> assertEquals(UserRole.ADMINISTRATOR, admin1.getRole()),
                () -> assertEquals("Security", admin1.getDepartment()),
                () -> assertTrue(admin1.isActive())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Getters y Setters funcionan correctamente")
    void testGettersAndSetters_Exitoso() {
        Administrator admin = new Administrator();

        admin.setId("10");
        admin.setName("Dick Grayson");
        admin.setEmail("dick.grayson@titans.edu");
        admin.setPassword("newpassword");
        admin.setDepartment("Technology");
        admin.setActive(false);

        assertAll("Verificar getters y setters",
                () -> assertEquals("10", admin.getId()),
                () -> assertEquals("Dick Grayson", admin.getName()),
                () -> assertEquals("dick.grayson@titans.edu", admin.getEmail()),
                () -> assertEquals("newpassword", admin.getPassword()),
                () -> assertEquals("Technology", admin.getDepartment()),
                () -> assertFalse(admin.isActive())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Equals retorna true para objetos iguales")
    void testEquals_ObjetosIguales() {
        assertEquals(admin1, admin3);
    }

    @Test
    @DisplayName("Caso exitoso - Equals retorna false para objetos diferentes")
    void testEquals_ObjetosDiferentes() {
        assertNotEquals(admin1, admin2);
    }

    @Test
    @DisplayName("Caso exitoso - HashCode es consistente para objetos iguales")
    void testHashCode_Consistente() {
        assertEquals(admin1.hashCode(), admin3.hashCode());
    }

    @Test
    @DisplayName("Caso exitoso - HashCode es diferente para objetos distintos")
    void testHashCode_Diferente() {
        assertNotEquals(admin1.hashCode(), admin2.hashCode());
    }



    @Test
    @DisplayName("Caso borde - Constructor con null en department")
    void testConstructor_DepartmentNull() {
        Administrator admin = new Administrator("3", "Barbara Gordon", "barbara.gordon@titans.edu", "password789", null);

        assertAll("Verificar constructor con department null",
                () -> assertEquals("3", admin.getId()),
                () -> assertEquals("Barbara Gordon", admin.getName()),
                () -> assertEquals("barbara.gordon@titans.edu", admin.getEmail()),
                () -> assertNull(admin.getDepartment()),
                () -> assertEquals(UserRole.ADMINISTRATOR, admin.getRole())
        );
    }

    @Test
    @DisplayName("Caso borde - Set department a null")
    void testSetDepartment_Null() {
        admin1.setDepartment(null);

        assertNull(admin1.getDepartment());
    }

    @Test
    @DisplayName("Caso borde - Set department vacío")
    void testSetDepartment_Vacio() {
        admin1.setDepartment("");

        assertEquals("", admin1.getDepartment());
    }

    @Test
    @DisplayName("Caso exitoso - Herencia de User funciona correctamente")
    void testHerencia_Exitoso() {
        assertAll("Verificar herencia de User",
                () -> assertTrue(admin1 instanceof User),
                () -> assertEquals(UserRole.ADMINISTRATOR, admin1.getRole()),
                () -> assertNotNull(admin1.getCreatedAt()),
                () -> assertNotNull(admin1.getUpdatedAt())
        );
    }

    @Test
    @DisplayName("Caso borde - Comparación con null")
    void testEquals_ConNull() {
        assertNotEquals(null, admin1);
    }

    @Test
    @DisplayName("Caso borde - Comparación con objeto de diferente clase")
    void testEquals_DiferenteClase() {
        Object otroObjeto = new Object();
        assertNotEquals(admin1, otroObjeto);
    }

    @Test
    @DisplayName("Caso exitoso - Campos heredados se pueden modificar")
    void testCamposHeredados_Modificacion() {
        admin1.setName("Bruce Wayne Modificado");
        admin1.setEmail("bruce.modificado@titans.edu");
        admin1.setActive(false);

        assertAll("Verificar modificación de campos heredados",
                () -> assertEquals("Bruce Wayne Modificado", admin1.getName()),
                () -> assertEquals("bruce.modificado@titans.edu", admin1.getEmail()),
                () -> assertFalse(admin1.isActive())
        );
    }

    @Test
    @DisplayName("Caso borde - Constructor con valores límite")
    void testConstructor_ValoresLimite() {
        Administrator admin = new Administrator("", "", "", "", "");

        assertAll("Verificar constructor con valores límite",
                () -> assertEquals("", admin.getId()),
                () -> assertEquals("", admin.getName()),
                () -> assertEquals("", admin.getEmail()),
                () -> assertEquals("", admin.getPassword()),
                () -> assertEquals("", admin.getDepartment())
        );
    }
}
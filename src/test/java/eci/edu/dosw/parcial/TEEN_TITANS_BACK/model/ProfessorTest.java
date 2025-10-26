package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ProfessorTest {

    private Professor professor1;
    private Professor professor2;
    private Professor professor3;

    @BeforeEach
    void setUp() {
        List<String> areas1 = Arrays.asList("Nuclear Physics", "Gamma Radiation", "Quantum Mechanics");
        List<String> areas2 = Arrays.asList("Genetics", "Mutation Studies", "Telepathy");
        List<String> areas3 = Arrays.asList("Nuclear Physics", "Gamma Radiation", "Quantum Mechanics");

        professor1 = new Professor("1", "Dr. Bruce Banner", "bruce.banner@titans.edu", "password123",
                "Physics", true, areas1);
        professor2 = new Professor("2", "Dr. Charles Xavier", "charles.xavier@titans.edu", "password456",
                "Genetics", true, areas2);
        professor3 = new Professor("1", "Dr. Bruce Banner", "bruce.banner@titans.edu", "password123",
                "Physics", true, areas3);
    }

    @Test
    @DisplayName("Caso exitoso - Constructor inicializa correctamente los campos")
    void testConstructor_Exitoso() {
        assertAll("Verificar inicialización del constructor",
                () -> assertEquals("1", professor1.getId()),
                () -> assertEquals("Dr. Bruce Banner", professor1.getName()),
                () -> assertEquals("bruce.banner@titans.edu", professor1.getEmail()),
                () -> assertEquals("password123", professor1.getPassword()),
                () -> assertEquals(UserRole.PROFESSOR, professor1.getRole()),
                () -> assertEquals("Physics", professor1.getDepartment()),
                () -> assertTrue(professor1.getIsTenured()),
                () -> assertEquals(3, professor1.getAreasOfExpertise().size()),
                () -> assertTrue(professor1.getAreasOfExpertise().contains("Nuclear Physics")),
                () -> assertTrue(professor1.isActive())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Getters y Setters funcionan correctamente")
    void testGettersAndSetters_Exitoso() {
        Professor professor = new Professor();
        List<String> newAreas = Arrays.asList("Artificial Intelligence", "Machine Learning");

        professor.setId("10");
        professor.setName("Dr. Tony Stark");
        professor.setEmail("tony.stark@titans.edu");
        professor.setPassword("newpassword");
        professor.setDepartment("Engineering");
        professor.setIsTenured(false);
        professor.setAreasOfExpertise(newAreas);
        professor.setActive(false);

        assertAll("Verificar getters y setters",
                () -> assertEquals("10", professor.getId()),
                () -> assertEquals("Dr. Tony Stark", professor.getName()),
                () -> assertEquals("tony.stark@titans.edu", professor.getEmail()),
                () -> assertEquals("newpassword", professor.getPassword()),
                () -> assertEquals("Engineering", professor.getDepartment()),
                () -> assertFalse(professor.getIsTenured()),
                () -> assertEquals(2, professor.getAreasOfExpertise().size()),
                () -> assertTrue(professor.getAreasOfExpertise().contains("Artificial Intelligence")),
                () -> assertFalse(professor.isActive())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Equals retorna true para objetos iguales")
    void testEquals_ObjetosIguales() {
        assertEquals(professor1, professor3);
    }

    @Test
    @DisplayName("Caso exitoso - Equals retorna false para objetos diferentes")
    void testEquals_ObjetosDiferentes() {
        assertNotEquals(professor1, professor2);
    }

    @Test
    @DisplayName("Caso exitoso - HashCode es consistente para objetos iguales")
    void testHashCode_Consistente() {
        assertEquals(professor1.hashCode(), professor3.hashCode());
    }

    @Test
    @DisplayName("Caso exitoso - HashCode es diferente para objetos distintos")
    void testHashCode_Diferente() {
        assertNotEquals(professor1.hashCode(), professor2.hashCode());
    }


    @Test
    @DisplayName("Caso borde - Constructor con null en department, isTenured y areasOfExpertise")
    void testConstructor_CamposNull() {
        Professor professor = new Professor("3", "Dr. Reed Richards", "reed.richards@titans.edu", "password789",
                null, null, null);

        assertAll("Verificar constructor con campos null",
                () -> assertEquals("3", professor.getId()),
                () -> assertEquals("Dr. Reed Richards", professor.getName()),
                () -> assertEquals("reed.richards@titans.edu", professor.getEmail()),
                () -> assertNull(professor.getDepartment()),
                () -> assertNull(professor.getIsTenured()),
                () -> assertNull(professor.getAreasOfExpertise()),
                () -> assertEquals(UserRole.PROFESSOR, professor.getRole())
        );
    }

    @Test
    @DisplayName("Caso borde - Set department, isTenured y areasOfExpertise a null")
    void testSetCampos_Null() {
        professor1.setDepartment(null);
        professor1.setIsTenured(null);
        professor1.setAreasOfExpertise(null);

        assertAll("Verificar set a null",
                () -> assertNull(professor1.getDepartment()),
                () -> assertNull(professor1.getIsTenured()),
                () -> assertNull(professor1.getAreasOfExpertise())
        );
    }

    @Test
    @DisplayName("Caso borde - Set department vacío")
    void testSetDepartment_Vacio() {
        professor1.setDepartment("");
        assertEquals("", professor1.getDepartment());
    }

    @Test
    @DisplayName("Caso borde - AreasOfExpertise lista vacía")
    void testAreasOfExpertise_ListaVacia() {
        professor1.setAreasOfExpertise(Collections.emptyList());

        assertAll("Verificar lista vacía de áreas de expertise",
                () -> assertNotNull(professor1.getAreasOfExpertise()),
                () -> assertTrue(professor1.getAreasOfExpertise().isEmpty())
        );
    }

    @Test
    @DisplayName("Caso borde - AreasOfExpertise con elementos null")
    void testAreasOfExpertise_ElementosNull() {
        List<String> areasWithNull = Arrays.asList("Physics", null, "Mathematics");
        professor1.setAreasOfExpertise(areasWithNull);

        assertAll("Verificar áreas de expertise con elementos null",
                () -> assertEquals(3, professor1.getAreasOfExpertise().size()),
                () -> assertTrue(professor1.getAreasOfExpertise().contains(null))
        );
    }

    @Test
    @DisplayName("Caso exitoso - Herencia de User funciona correctamente")
    void testHerencia_Exitoso() {
        assertAll("Verificar herencia de User",
                () -> assertTrue(professor1 instanceof User),
                () -> assertEquals(UserRole.PROFESSOR, professor1.getRole()),
                () -> assertNotNull(professor1.getCreatedAt()),
                () -> assertNotNull(professor1.getUpdatedAt())
        );
    }

    @Test
    @DisplayName("Caso borde - Comparación con null")
    void testEquals_ConNull() {
        assertNotEquals(null, professor1);
    }

    @Test
    @DisplayName("Caso borde - Comparación con objeto de diferente clase")
    void testEquals_DiferenteClase() {
        Object otroObjeto = new Object();
        assertNotEquals(professor1, otroObjeto);
    }

    @Test
    @DisplayName("Caso exitoso - Campos heredados se pueden modificar")
    void testCamposHeredados_Modificacion() {
        professor1.setName("Dr. Bruce Banner Modificado");
        professor1.setEmail("bruce.modificado@titans.edu");
        professor1.setActive(false);

        assertAll("Verificar modificación de campos heredados",
                () -> assertEquals("Dr. Bruce Banner Modificado", professor1.getName()),
                () -> assertEquals("bruce.modificado@titans.edu", professor1.getEmail()),
                () -> assertFalse(professor1.isActive())
        );
    }

    @Test
    @DisplayName("Caso borde - Constructor con valores límite")
    void testConstructor_ValoresLimite() {
        Professor professor = new Professor("", "", "", "", "", false, Collections.emptyList());

        assertAll("Verificar constructor con valores límite",
                () -> assertEquals("", professor.getId()),
                () -> assertEquals("", professor.getName()),
                () -> assertEquals("", professor.getEmail()),
                () -> assertEquals("", professor.getPassword()),
                () -> assertEquals("", professor.getDepartment()),
                () -> assertFalse(professor.getIsTenured()),
                () -> assertTrue(professor.getAreasOfExpertise().isEmpty())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Actualización de campos específicos de Professor")
    void testActualizacionCamposEspecificos() {
        List<String> nuevasAreas = Arrays.asList("Quantum Computing", "String Theory");

        professor1.setDepartment("Advanced Physics");
        professor1.setIsTenured(false);
        professor1.setAreasOfExpertise(nuevasAreas);

        assertAll("Verificar actualización de campos específicos",
                () -> assertEquals("Advanced Physics", professor1.getDepartment()),
                () -> assertFalse(professor1.getIsTenured()),
                () -> assertEquals(2, professor1.getAreasOfExpertise().size()),
                () -> assertTrue(professor1.getAreasOfExpertise().contains("Quantum Computing"))
        );
    }

    @Test
    @DisplayName("Caso borde - Department con espacios en blanco")
    void testDepartment_ConEspacios() {
        professor1.setDepartment("  Department of Physics  ");
        assertEquals("  Department of Physics  ", professor1.getDepartment());
    }

    @Test
    @DisplayName("Caso borde - AreasOfExpertise con elementos duplicados")
    void testAreasOfExpertise_Duplicados() {
        List<String> areasDuplicadas = Arrays.asList("Physics", "Physics", "Mathematics");
        professor1.setAreasOfExpertise(areasDuplicadas);

        assertEquals(3, professor1.getAreasOfExpertise().size());
    }

    @Test
    @DisplayName("Caso borde - IsTenured con valor false")
    void testIsTenured_False() {
        professor1.setIsTenured(false);
        assertFalse(professor1.getIsTenured());
    }

    @Test
    @DisplayName("Caso borde - AreasOfExpertise con un solo elemento")
    void testAreasOfExpertise_UnElemento() {
        List<String> unaArea = Arrays.asList("Physics");
        professor1.setAreasOfExpertise(unaArea);

        assertAll("Verificar una sola área de expertise",
                () -> assertEquals(1, professor1.getAreasOfExpertise().size()),
                () -> assertEquals("Physics", professor1.getAreasOfExpertise().get(0))
        );
    }

    @Test
    @DisplayName("Caso borde - AreasOfExpertise con muchos elementos")
    void testAreasOfExpertise_MuchosElementos() {
        List<String> muchasAreas = Arrays.asList(
                "Physics", "Mathematics", "Chemistry", "Biology",
                "Computer Science", "Engineering", "Astronomy"
        );
        professor1.setAreasOfExpertise(muchasAreas);

        assertEquals(7, professor1.getAreasOfExpertise().size());
    }
}
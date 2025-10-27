package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourseDTOTest {

    private CourseDTO courseDTO;

    @BeforeEach
    void setUp() {
        courseDTO = new CourseDTO();
        courseDTO.setCourseCode("MATH101");
        courseDTO.setName("Cálculo I");
        courseDTO.setCredits(4);
        courseDTO.setDescription("Introducción al cálculo diferencial");
        courseDTO.setAcademicProgram("Ingeniería");
        courseDTO.setIsActive(true);
    }

    @Test
    @DisplayName("Caso exitoso - Creación de CourseDTO con constructor completo")
    void testCourseDTOConstructorCompleto_Exitoso() {
        CourseDTO dto = new CourseDTO("PHYS201", "Física II", 3, "Termodinámica y ondas", "Ciencias", false);

        assertAll("Verificación de constructor completo",
                () -> assertEquals("PHYS201", dto.getCourseCode()),
                () -> assertEquals("Física II", dto.getName()),
                () -> assertEquals(3, dto.getCredits()),
                () -> assertEquals("Termodinámica y ondas", dto.getDescription()),
                () -> assertEquals("Ciencias", dto.getAcademicProgram()),
                () -> assertFalse(dto.getIsActive())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Getters y setters funcionan correctamente")
    void testGettersAndSetters_Exitoso() {
        assertAll("Verificación de getters y setters",
                () -> assertEquals("MATH101", courseDTO.getCourseCode()),
                () -> assertEquals("Cálculo I", courseDTO.getName()),
                () -> assertEquals(4, courseDTO.getCredits()),
                () -> assertEquals("Introducción al cálculo diferencial", courseDTO.getDescription()),
                () -> assertEquals("Ingeniería", courseDTO.getAcademicProgram()),
                () -> assertTrue(courseDTO.getIsActive())
        );
    }

    @Test
    @DisplayName("Caso borde - Constructor sin argumentos crea instancia")
    void testNoArgsConstructor_Exitoso() {
        CourseDTO emptyDTO = new CourseDTO();
        assertNotNull(emptyDTO);
    }

    @Test
    @DisplayName("Caso borde - equals retorna true para DTOs idénticos")
    void testEquals_Exitoso() {
        CourseDTO dto1 = new CourseDTO("MATH101", "Cálculo I", 4, "Introducción al cálculo", "Ingeniería", true);
        CourseDTO dto2 = new CourseDTO("MATH101", "Cálculo I", 4, "Introducción al cálculo", "Ingeniería", true);

        assertEquals(dto1, dto2);
    }

    @Test
    @DisplayName("Caso error - equals retorna false para DTOs diferentes")
    void testEquals_Diferentes() {
        CourseDTO dto1 = new CourseDTO("MATH101", "Cálculo I", 4, "Introducción al cálculo", "Ingeniería", true);
        CourseDTO dto2 = new CourseDTO("PHYS201", "Física II", 3, "Termodinámica", "Ciencias", false);

        assertNotEquals(dto1, dto2);
    }

    @Test
    @DisplayName("Caso borde - hashCode consistente para DTOs iguales")
    void testHashCode_Exitoso() {
        CourseDTO dto1 = new CourseDTO("MATH101", "Cálculo I", 4, "Introducción al cálculo", "Ingeniería", true);
        CourseDTO dto2 = new CourseDTO("MATH101", "Cálculo I", 4, "Introducción al cálculo", "Ingeniería", true);

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    @DisplayName("Caso borde - toString no retorna null")
    void testToString_NoNull() {
        String toStringResult = courseDTO.toString();
        assertNotNull(toStringResult);
        assertFalse(toStringResult.isEmpty());
    }

    @Test
    @DisplayName("Caso borde - Campos null manejados correctamente")
    void testCamposNull_Exitoso() {
        CourseDTO dto = new CourseDTO();
        dto.setCourseCode(null);
        dto.setName(null);
        dto.setCredits(null);
        dto.setDescription(null);
        dto.setAcademicProgram(null);
        dto.setIsActive(null);

        assertAll("Verificación de campos null",
                () -> assertNull(dto.getCourseCode()),
                () -> assertNull(dto.getName()),
                () -> assertNull(dto.getCredits()),
                () -> assertNull(dto.getDescription()),
                () -> assertNull(dto.getAcademicProgram()),
                () -> assertNull(dto.getIsActive())
        );
    }

    @Test
    @DisplayName("Caso borde - Créditos cero manejados correctamente")
    void testCreditosCero_Exitoso() {
        courseDTO.setCredits(0);
        assertEquals(0, courseDTO.getCredits());
    }

    @Test
    @DisplayName("Caso error - Créditos negativos manejados correctamente")
    void testCreditosNegativos_Exitoso() {
        courseDTO.setCredits(-5);
        assertEquals(-5, courseDTO.getCredits());
    }

    @Test
    @DisplayName("Caso borde - CourseCode con formato especial")
    void testCourseCodeFormatoEspecial_Exitoso() {
        courseDTO.setCourseCode("CS-101-A");
        assertEquals("CS-101-A", courseDTO.getCourseCode());

        courseDTO.setCourseCode("MATH_202_B");
        assertEquals("MATH_202_B", courseDTO.getCourseCode());

        courseDTO.setCourseCode("PHY101");
        assertEquals("PHY101", courseDTO.getCourseCode());
    }

    @Test
    @DisplayName("Caso borde - Nombre con caracteres especiales")
    void testNombreCaracteresEspeciales_Exitoso() {
        courseDTO.setName("Cálculo II: Integrales Múltiples");
        assertEquals("Cálculo II: Integrales Múltiples", courseDTO.getName());

        courseDTO.setName("Física (Laboratorio)");
        assertEquals("Física (Laboratorio)", courseDTO.getName());

        courseDTO.setName("Programación I - Java");
        assertEquals("Programación I - Java", courseDTO.getName());
    }

    @Test
    @DisplayName("Caso borde - Descripción larga")
    void testDescripcionLarga_Exitoso() {
        String descripcionLarga = "Este curso cubre los fundamentos de la programación orientada a objetos " +
                "utilizando Java como lenguaje principal. Se incluyen temas como clases, " +
                "objetos, herencia, polimorfismo y encapsulamiento.";
        courseDTO.setDescription(descripcionLarga);
        assertEquals(descripcionLarga, courseDTO.getDescription());
    }

    @Test
    @DisplayName("Caso borde - AcademicProgram con diferentes programas")
    void testAcademicProgramDiferentes_Exitoso() {
        courseDTO.setAcademicProgram("Ingeniería de Sistemas");
        assertEquals("Ingeniería de Sistemas", courseDTO.getAcademicProgram());

        courseDTO.setAcademicProgram("Medicina");
        assertEquals("Medicina", courseDTO.getAcademicProgram());

        courseDTO.setAcademicProgram("Administración de Empresas");
        assertEquals("Administración de Empresas", courseDTO.getAcademicProgram());

        courseDTO.setAcademicProgram("Arquitectura");
        assertEquals("Arquitectura", courseDTO.getAcademicProgram());
    }

    @Test
    @DisplayName("Caso exitoso - IsActive con diferentes valores booleanos")
    void testIsActiveValoresBooleanos_Exitoso() {
        courseDTO.setIsActive(true);
        assertTrue(courseDTO.getIsActive());

        courseDTO.setIsActive(false);
        assertFalse(courseDTO.getIsActive());

        courseDTO.setIsActive(null);
        assertNull(courseDTO.getIsActive());
    }

    @Test
    @DisplayName("Caso borde - Créditos con valores extremos")
    void testCreditosValoresExtremos_Exitoso() {
        courseDTO.setCredits(1);
        assertEquals(1, courseDTO.getCredits());

        courseDTO.setCredits(10);
        assertEquals(10, courseDTO.getCredits());

        courseDTO.setCredits(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, courseDTO.getCredits());
    }

    @Test
    @DisplayName("Caso borde - Descripción vacía")
    void testDescripcionVacia_Exitoso() {
        courseDTO.setDescription("");
        assertEquals("", courseDTO.getDescription());

        courseDTO.setDescription("   ");
        assertEquals("   ", courseDTO.getDescription());
    }

    @Test
    @DisplayName("Caso borde - AcademicProgram vacío")
    void testAcademicProgramVacio_Exitoso() {
        courseDTO.setAcademicProgram("");
        assertEquals("", courseDTO.getAcademicProgram());
    }

    @Test
    @DisplayName("Caso borde - CourseCode vacío")
    void testCourseCodeVacio_Exitoso() {
        courseDTO.setCourseCode("");
        assertEquals("", courseDTO.getCourseCode());
    }

    @Test
    @DisplayName("Caso borde - Nombre vacío")
    void testNombreVacio_Exitoso() {
        courseDTO.setName("");
        assertEquals("", courseDTO.getName());
    }
}
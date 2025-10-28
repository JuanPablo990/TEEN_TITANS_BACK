package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CourseTest {

    private Course course;

    @BeforeEach
    void setUp() {
        course = new Course(
                "MATH101",
                "Cálculo I",
                4,
                "Curso introductorio de cálculo diferencial e integral",
                "Ingeniería de Sistemas",
                true
        );
    }

    @Test
    @DisplayName("Caso exitoso - Constructor con todos los parámetros inicializa correctamente")
    void testConstructorCompleto_Exitoso() {
        assertAll("Validar todos los campos del constructor completo",
                () -> assertEquals("MATH101", course.getCourseCode()),
                () -> assertEquals("Cálculo I", course.getName()),
                () -> assertEquals(4, course.getCredits()),
                () -> assertEquals("Curso introductorio de cálculo diferencial e integral", course.getDescription()),
                () -> assertEquals("Ingeniería de Sistemas", course.getAcademicProgram()),
                () -> assertTrue(course.getIsActive())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Constructor sin courseCode para generación automática")
    void testConstructorSinCourseCode_Exitoso() {
        Course courseSinCodigo = new Course(
                "Física I",
                3,
                "Curso de física mecánica y termodinámica",
                "Ingeniería Civil",
                false
        );

        assertAll("Validar constructor sin courseCode",
                () -> assertNull(courseSinCodigo.getCourseCode()),
                () -> assertEquals("Física I", courseSinCodigo.getName()),
                () -> assertEquals(3, courseSinCodigo.getCredits()),
                () -> assertEquals("Curso de física mecánica y termodinámica", courseSinCodigo.getDescription()),
                () -> assertEquals("Ingeniería Civil", courseSinCodigo.getAcademicProgram()),
                () -> assertFalse(courseSinCodigo.getIsActive())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Constructor por defecto inicializa con valores por defecto")
    void testConstructorPorDefecto_Exitoso() {
        Course courseVacio = new Course();

        assertAll("Validar valores por defecto",
                () -> assertNull(courseVacio.getCourseCode()),
                () -> assertNull(courseVacio.getName()),
                () -> assertNull(courseVacio.getCredits()),
                () -> assertNull(courseVacio.getDescription()),
                () -> assertNull(courseVacio.getAcademicProgram()),
                () -> assertNull(courseVacio.getIsActive())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Setters y Getters funcionan correctamente")
    void testSettersAndGetters_Exitoso() {
        Course courseTest = new Course();

        courseTest.setCourseCode("PHY201");
        courseTest.setName("Física II");
        courseTest.setCredits(4);
        courseTest.setDescription("Curso de electromagnetismo y óptica");
        courseTest.setAcademicProgram("Ingeniería Eléctrica");
        courseTest.setIsActive(true);

        assertAll("Validar setters y getters",
                () -> assertEquals("PHY201", courseTest.getCourseCode()),
                () -> assertEquals("Física II", courseTest.getName()),
                () -> assertEquals(4, courseTest.getCredits()),
                () -> assertEquals("Curso de electromagnetismo y óptica", courseTest.getDescription()),
                () -> assertEquals("Ingeniería Eléctrica", courseTest.getAcademicProgram()),
                () -> assertTrue(courseTest.getIsActive())
        );
    }

    @Test
    @DisplayName("Caso borde - Campos con valores null")
    void testCamposConValoresNull_Borde() {
        Course courseNull = new Course(
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertAll("Validar campos null",
                () -> assertNull(courseNull.getCourseCode()),
                () -> assertNull(courseNull.getName()),
                () -> assertNull(courseNull.getCredits()),
                () -> assertNull(courseNull.getDescription()),
                () -> assertNull(courseNull.getAcademicProgram()),
                () -> assertNull(courseNull.getIsActive())
        );
    }

    @Test
    @DisplayName("Caso borde - Campos con valores vacíos")
    void testCamposConValoresVacios_Borde() {
        course.setCourseCode("");
        course.setName("");
        course.setDescription("");
        course.setAcademicProgram("");

        assertAll("Validar campos vacíos",
                () -> assertEquals("", course.getCourseCode()),
                () -> assertEquals("", course.getName()),
                () -> assertEquals("", course.getDescription()),
                () -> assertEquals("", course.getAcademicProgram())
        );
    }

    @Test
    @DisplayName("Caso borde - Créditos con valores extremos")
    void testCreditosValoresExtremos_Borde() {
        course.setCredits(0);
        assertEquals(0, course.getCredits());

        course.setCredits(10);
        assertEquals(10, course.getCredits());

        course.setCredits(-1);
        assertEquals(-1, course.getCredits());
    }

    @Test
    @DisplayName("Caso borde - Campo isActive con diferentes valores booleanos")
    void testCampoIsActive_ValoresBooleanos_Borde() {
        course.setIsActive(true);
        assertTrue(course.getIsActive());

        course.setIsActive(false);
        assertFalse(course.getIsActive());

        course.setIsActive(null);
        assertNull(course.getIsActive());
    }

    @Test
    @DisplayName("Caso exitoso - Equals y HashCode generados por Lombok")
    void testEqualsAndHashCode_Exitoso() {
        Course course1 = new Course(
                "CHEM101",
                "Química General",
                3,
                "Curso básico de química",
                "Ingeniería Química",
                true
        );

        Course course2 = new Course(
                "CHEM101",
                "Química General",
                3,
                "Curso básico de química",
                "Ingeniería Química",
                true
        );

        Course course3 = new Course(
                "MATH201",
                "Cálculo II",
                4,
                "Curso avanzado de cálculo",
                "Ingeniería de Sistemas",
                false
        );

        assertEquals(course1, course2);
        assertNotEquals(course1, course3);
        assertEquals(course1.hashCode(), course2.hashCode());
        assertNotEquals(course1.hashCode(), course3.hashCode());
    }

    @Test
    @DisplayName("Caso borde - Equals con objetos null y diferente clase")
    void testEqualsCasosBorde_Borde() {
        Course courseTest = new Course(
                "TEST101",
                "Curso Test",
                1,
                "Descripción test",
                "Programa Test",
                true
        );

        assertNotEquals(null, courseTest);
        assertNotEquals("No soy un Course", courseTest);
    }

    @Test
    @DisplayName("Caso exitoso - ToString generado por Lombok")
    void testToString_Exitoso() {
        String resultadoToString = course.toString();

        assertAll("ToString debe contener todos los campos principales",
                () -> assertTrue(resultadoToString.contains("MATH101")),
                () -> assertTrue(resultadoToString.contains("Cálculo I")),
                () -> assertTrue(resultadoToString.contains("4")),
                () -> assertTrue(resultadoToString.contains("Curso introductorio de cálculo diferencial e integral")),
                () -> assertTrue(resultadoToString.contains("Ingeniería de Sistemas")),
                () -> assertTrue(resultadoToString.contains("true")),
                () -> assertTrue(resultadoToString.contains("Course"))
        );
    }

    @Test
    @DisplayName("Caso compuesto - Validación completa de múltiples escenarios")
    void testCourse_ValidacionCompleta_Compuesto() {
        assertAll("Validación completa de todas las propiedades",
                () -> assertEquals("MATH101", course.getCourseCode()),
                () -> assertEquals("Cálculo I", course.getName()),
                () -> assertEquals(4, course.getCredits()),
                () -> assertEquals("Curso introductorio de cálculo diferencial e integral", course.getDescription()),
                () -> assertEquals("Ingeniería de Sistemas", course.getAcademicProgram()),
                () -> assertTrue(course.getIsActive()),
                () -> assertNotNull(course.toString()),
                () -> assertTrue(course.toString().contains("Course"))
        );
    }

    @Test
    @DisplayName("Caso borde - CourseCode con diferentes formatos")
    void testCourseCodeFormatos_Borde() {
        course.setCourseCode("CS-101");
        assertEquals("CS-101", course.getCourseCode());

        course.setCourseCode("MATH_2025_1");
        assertEquals("MATH_2025_1", course.getCourseCode());

        course.setCourseCode("12345");
        assertEquals("12345", course.getCourseCode());
    }

    @Test
    @DisplayName("Caso borde - Nombres con caracteres especiales")
    void testNombresCaracteresEspeciales_Borde() {
        course.setName("Cálculo Diferencial e Integral ");
        assertEquals("Cálculo Diferencial e Integral ", course.getName());

        course.setName("Programación I - Java™");
        assertEquals("Programación I - Java™", course.getName());

        String nombreLargo = "A".repeat(200);
        course.setName(nombreLargo);
        assertEquals(nombreLargo, course.getName());
    }

    @Test
    @DisplayName("Caso borde - Descripciones largas")
    void testDescripcionesLargas_Borde() {
        String descripcionLarga = "Este es un curso muy extenso que cubre todos los aspectos fundamentales ".repeat(10);
        course.setDescription(descripcionLarga);
        assertEquals(descripcionLarga, course.getDescription());

        course.setDescription("");
        assertEquals("", course.getDescription());

        course.setDescription(null);
        assertNull(course.getDescription());
    }

    @Test
    @DisplayName("Caso borde - Programas académicos con diferentes formatos")
    void testAcademicProgramFormatos_Borde() {
        course.setAcademicProgram("Ing. de Sistemas");
        assertEquals("Ing. de Sistemas", course.getAcademicProgram());

        course.setAcademicProgram("Medicina - Especialización");
        assertEquals("Medicina - Especialización", course.getAcademicProgram());

        course.setAcademicProgram("ADMINISTRACIÓN DE EMPRESAS");
        assertEquals("ADMINISTRACIÓN DE EMPRESAS", course.getAcademicProgram());
    }

    @Test
    @DisplayName("Caso borde - Cursos inactivos")
    void testCursosInactivos_Borde() {
        Course cursoInactivo = new Course(
                "OLD101",
                "Curso Obsoleto",
                2,
                "Este curso ya no se ofrece",
                "Programa Antiguo",
                false
        );

        assertAll("Validar curso inactivo",
                () -> assertEquals("OLD101", cursoInactivo.getCourseCode()),
                () -> assertEquals("Curso Obsoleto", cursoInactivo.getName()),
                () -> assertEquals(2, cursoInactivo.getCredits()),
                () -> assertEquals("Este curso ya no se ofrece", cursoInactivo.getDescription()),
                () -> assertEquals("Programa Antiguo", cursoInactivo.getAcademicProgram()),
                () -> assertFalse(cursoInactivo.getIsActive())
        );
    }
}
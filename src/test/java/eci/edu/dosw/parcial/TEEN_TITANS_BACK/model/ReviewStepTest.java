package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class ReviewStepTest {

    private ReviewStep reviewStep;

    @BeforeEach
    void setUp() {
        reviewStep = ReviewStep.builder()
                .userId("USER001")
                .userRole(UserRole.ADMINISTRATOR)
                .action("Aprobación inicial")
                .timestamp(new Date())
                .comments("Solicitud revisada y aprobada inicialmente")
                .build();
    }

    @Test
    @DisplayName("Caso exitoso - Builder inicializa todos los campos correctamente")
    void testBuilderInicializacion_Exitoso() {
        assertAll("Validar todos los campos inicializados por el builder",
                () -> assertEquals("USER001", reviewStep.getUserId()),
                () -> assertEquals(UserRole.ADMINISTRATOR, reviewStep.getUserRole()),
                () -> assertEquals("Aprobación inicial", reviewStep.getAction()),
                () -> assertNotNull(reviewStep.getTimestamp()),
                () -> assertEquals("Solicitud revisada y aprobada inicialmente", reviewStep.getComments())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Constructor personalizado inicializa campos correctamente")
    void testConstructorPersonalizado_Exitoso() {
        ReviewStep stepPersonalizado = new ReviewStep(
                "PROF001",
                UserRole.PROFESSOR,
                "Revisión académica",
                "El estudiante cumple con los requisitos"
        );

        assertAll("Validar constructor personalizado",
                () -> assertEquals("PROF001", stepPersonalizado.getUserId()),
                () -> assertEquals(UserRole.PROFESSOR, stepPersonalizado.getUserRole()),
                () -> assertEquals("Revisión académica", stepPersonalizado.getAction()),
                () -> assertNotNull(stepPersonalizado.getTimestamp()),
                () -> assertEquals("El estudiante cumple con los requisitos", stepPersonalizado.getComments())
        );
    }


    @Test
    @DisplayName("Caso exitoso - Constructor con todos los parámetros")
    void testConstructorConParametros_Exitoso() {
        Date timestamp = new Date(System.currentTimeMillis() - 1000000);
        ReviewStep stepCompleto = new ReviewStep(
                "DEAN001",
                UserRole.DEAN,
                "Aprobación final",
                timestamp,
                "Solicitud aprobada por decanato"
        );

        assertAll("Validar constructor con parámetros",
                () -> assertEquals("DEAN001", stepCompleto.getUserId()),
                () -> assertEquals(UserRole.DEAN, stepCompleto.getUserRole()),
                () -> assertEquals("Aprobación final", stepCompleto.getAction()),
                () -> assertEquals(timestamp, stepCompleto.getTimestamp()),
                () -> assertEquals("Solicitud aprobada por decanato", stepCompleto.getComments())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Setters y Getters funcionan correctamente")
    void testSettersAndGetters_Exitoso() {
        ReviewStep stepTest = new ReviewStep();
        Date newTimestamp = new Date(System.currentTimeMillis() - 500000);

        stepTest.setUserId("STU001");
        stepTest.setUserRole(UserRole.STUDENT);
        stepTest.setAction("Solicitud enviada");
        stepTest.setTimestamp(newTimestamp);
        stepTest.setComments("El estudiante envió la solicitud");

        assertAll("Validar setters y getters",
                () -> assertEquals("STU001", stepTest.getUserId()),
                () -> assertEquals(UserRole.STUDENT, stepTest.getUserRole()),
                () -> assertEquals("Solicitud enviada", stepTest.getAction()),
                () -> assertEquals(newTimestamp, stepTest.getTimestamp()),
                () -> assertEquals("El estudiante envió la solicitud", stepTest.getComments())
        );
    }

    @Test
    @DisplayName("Caso borde - Campos con valores null")
    void testCamposConValoresNull_Borde() {
        ReviewStep stepNull = ReviewStep.builder()
                .userId(null)
                .userRole(null)
                .action(null)
                .timestamp(null)
                .comments(null)
                .build();

        assertAll("Validar campos null",
                () -> assertNull(stepNull.getUserId()),
                () -> assertNull(stepNull.getUserRole()),
                () -> assertNull(stepNull.getAction()),
                () -> assertNull(stepNull.getTimestamp()),
                () -> assertNull(stepNull.getComments())
        );
    }

    @Test
    @DisplayName("Caso borde - Campos con valores vacíos")
    void testCamposConValoresVacios_Borde() {
        reviewStep.setUserId("");
        reviewStep.setAction("");
        reviewStep.setComments("");

        assertAll("Validar campos vacíos",
                () -> assertEquals("", reviewStep.getUserId()),
                () -> assertEquals("", reviewStep.getAction()),
                () -> assertEquals("", reviewStep.getComments())
        );
    }

    @Test
    @DisplayName("Caso borde - Builder con valor por defecto para timestamp")
    void testBuilderValorPorDefectoTimestamp_Borde() {
        ReviewStep stepSinTimestamp = ReviewStep.builder()
                .userId("USER002")
                .userRole(UserRole.PROFESSOR)
                .action("Revisión")
                .comments("Sin timestamp específico")
                .build();

        assertAll("Validar timestamp por defecto en builder",
                () -> assertEquals("USER002", stepSinTimestamp.getUserId()),
                () -> assertEquals(UserRole.PROFESSOR, stepSinTimestamp.getUserRole()),
                () -> assertEquals("Revisión", stepSinTimestamp.getAction()),
                () -> assertNotNull(stepSinTimestamp.getTimestamp()),
                () -> assertEquals("Sin timestamp específico", stepSinTimestamp.getComments())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Equals y HashCode generados por Lombok")
    void testEqualsAndHashCode_Exitoso() {
        Date timestamp1 = new Date(1704067200000L); // 1 de enero 2024
        Date timestamp2 = new Date(1704067200000L); // 1 de enero 2024

        ReviewStep step1 = ReviewStep.builder()
                .userId("USER001")
                .userRole(UserRole.ADMINISTRATOR)
                .action("Aprobación")
                .timestamp(timestamp1)
                .comments("Comentario")
                .build();

        ReviewStep step2 = ReviewStep.builder()
                .userId("USER001")
                .userRole(UserRole.ADMINISTRATOR)
                .action("Aprobación")
                .timestamp(timestamp2)
                .comments("Comentario")
                .build();

        ReviewStep step3 = ReviewStep.builder()
                .userId("USER002")
                .userRole(UserRole.PROFESSOR)
                .action("Revisión")
                .timestamp(new Date())
                .comments("Otro comentario")
                .build();

        assertEquals(step1, step2);
        assertNotEquals(step1, step3);
        assertEquals(step1.hashCode(), step2.hashCode());
        assertNotEquals(step1.hashCode(), step3.hashCode());
    }

    @Test
    @DisplayName("Caso borde - Equals con objetos null y diferente clase")
    void testEqualsCasosBorde_Borde() {
        ReviewStep stepTest = ReviewStep.builder()
                .userId("USER001")
                .userRole(UserRole.ADMINISTRATOR)
                .action("Test")
                .build();

        assertNotEquals(null, stepTest);
        assertNotEquals("No soy un ReviewStep", stepTest);
    }

    @Test
    @DisplayName("Caso exitoso - ToString generado por Lombok")
    void testToString_Exitoso() {
        String resultadoToString = reviewStep.toString();

        assertAll("ToString debe contener todos los campos principales",
                () -> assertTrue(resultadoToString.contains("USER001")),
                () -> assertTrue(resultadoToString.contains("ADMINISTRATOR")),
                () -> assertTrue(resultadoToString.contains("Aprobación inicial")),
                () -> assertTrue(resultadoToString.contains("Solicitud revisada y aprobada inicialmente")),
                () -> assertTrue(resultadoToString.contains("ReviewStep"))
        );
    }

    @Test
    @DisplayName("Caso compuesto - Validación completa de múltiples escenarios")
    void testReviewStep_ValidacionCompleta_Compuesto() {
        assertAll("Validación completa de todas las propiedades",
                () -> assertEquals("USER001", reviewStep.getUserId()),
                () -> assertEquals(UserRole.ADMINISTRATOR, reviewStep.getUserRole()),
                () -> assertEquals("Aprobación inicial", reviewStep.getAction()),
                () -> assertNotNull(reviewStep.getTimestamp()),
                () -> assertEquals("Solicitud revisada y aprobada inicialmente", reviewStep.getComments()),
                () -> assertNotNull(reviewStep.toString()),
                () -> assertTrue(reviewStep.toString().contains("ReviewStep"))
        );
    }

    @Test
    @DisplayName("Caso borde - UserRole con todos los valores posibles")
    void testUserRoleTodosLosValores_Borde() {
        ReviewStep stepStudent = ReviewStep.builder()
                .userId("STU001")
                .userRole(UserRole.STUDENT)
                .action("Envío solicitud")
                .build();

        ReviewStep stepProfessor = ReviewStep.builder()
                .userId("PROF001")
                .userRole(UserRole.PROFESSOR)
                .action("Revisión académica")
                .build();

        ReviewStep stepAdmin = ReviewStep.builder()
                .userId("ADMIN001")
                .userRole(UserRole.ADMINISTRATOR)
                .action("Aprobación administrativa")
                .build();

        ReviewStep stepDean = ReviewStep.builder()
                .userId("DEAN001")
                .userRole(UserRole.DEAN)
                .action("Aprobación final")
                .build();

        assertAll("Validar todos los roles de usuario",
                () -> assertEquals(UserRole.STUDENT, stepStudent.getUserRole()),
                () -> assertEquals(UserRole.PROFESSOR, stepProfessor.getUserRole()),
                () -> assertEquals(UserRole.ADMINISTRATOR, stepAdmin.getUserRole()),
                () -> assertEquals(UserRole.DEAN, stepDean.getUserRole())
        );
    }

    @Test
    @DisplayName("Caso borde - UserId con diferentes formatos")
    void testUserIdFormatos_Borde() {
        reviewStep.setUserId("202512345");
        assertEquals("202512345", reviewStep.getUserId());

        reviewStep.setUserId("PROF-MATH-001");
        assertEquals("PROF-MATH-001", reviewStep.getUserId());

        reviewStep.setUserId("ADMIN_SISTEMAS");
        assertEquals("ADMIN_SISTEMAS", reviewStep.getUserId());
    }

    @Test
    @DisplayName("Caso borde - Acciones con diferentes formatos")
    void testAccionesFormatos_Borde() {
        reviewStep.setAction("APROBACIÓN_INICIAL");
        assertEquals("APROBACIÓN_INICIAL", reviewStep.getAction());

        reviewStep.setAction("rechazo por conflicto de horario");
        assertEquals("rechazo por conflicto de horario", reviewStep.getAction());

        reviewStep.setAction("Análisis de viabilidad académica");
        assertEquals("Análisis de viabilidad académica", reviewStep.getAction());
    }

    @Test
    @DisplayName("Caso borde - Comentarios largos")
    void testComentariosLargos_Borde() {
        String comentarioLargo = "Este es un comentario muy extenso que describe detalladamente ".repeat(10);
        reviewStep.setComments(comentarioLargo);
        assertEquals(comentarioLargo, reviewStep.getComments());

        reviewStep.setComments("");
        assertEquals("", reviewStep.getComments());

        reviewStep.setComments(null);
        assertNull(reviewStep.getComments());
    }

    @Test
    @DisplayName("Caso borde - Timestamp con valores extremos")
    void testTimestampValoresExtremos_Borde() {
        Date minDate = new Date(Long.MIN_VALUE);
        Date maxDate = new Date(Long.MAX_VALUE);

        reviewStep.setTimestamp(minDate);
        assertEquals(minDate, reviewStep.getTimestamp());

        reviewStep.setTimestamp(maxDate);
        assertEquals(maxDate, reviewStep.getTimestamp());

        reviewStep.setTimestamp(null);
        assertNull(reviewStep.getTimestamp());
    }

    @Test
    @DisplayName("Caso borde - Constructor personalizado sin comentarios")
    void testConstructorPersonalizadoSinComentarios_Borde() {
        ReviewStep stepSinComentarios = new ReviewStep(
                "USER003",
                UserRole.PROFESSOR,
                "Revisión rápida",
                null
        );

        assertAll("Validar constructor sin comentarios",
                () -> assertEquals("USER003", stepSinComentarios.getUserId()),
                () -> assertEquals(UserRole.PROFESSOR, stepSinComentarios.getUserRole()),
                () -> assertEquals("Revisión rápida", stepSinComentarios.getAction()),
                () -> assertNotNull(stepSinComentarios.getTimestamp()),
                () -> assertNull(stepSinComentarios.getComments())
        );
    }
}
package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class AcademicPeriodTest {

    private AcademicPeriod academicPeriod;
    private Date startDate;
    private Date endDate;
    private Date enrollmentStart;
    private Date enrollmentEnd;
    private Date adjustmentStart;
    private Date adjustmentEnd;

    @BeforeEach
    void setUp() {
        startDate = new Date(System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000));
        endDate = new Date(System.currentTimeMillis() + (90L * 24 * 60 * 60 * 1000));
        enrollmentStart = new Date(System.currentTimeMillis() - (60L * 24 * 60 * 60 * 1000));
        enrollmentEnd = new Date(System.currentTimeMillis() - (15L * 24 * 60 * 60 * 1000));
        adjustmentStart = new Date(System.currentTimeMillis() - (10L * 24 * 60 * 60 * 1000));
        adjustmentEnd = new Date(System.currentTimeMillis() + (10L * 24 * 60 * 60 * 1000));

        academicPeriod = AcademicPeriod.builder()
                .periodId("PERIOD_2025_1")
                .name("Primer Semestre 2025")
                .startDate(startDate)
                .endDate(endDate)
                .enrollmentStart(enrollmentStart)
                .enrollmentEnd(enrollmentEnd)
                .adjustmentPeriodStart(adjustmentStart)
                .adjustmentPeriodEnd(adjustmentEnd)
                .isActive(true)
                .build();
    }

    @Test
    @DisplayName("Caso exitoso - Builder inicializa todos los campos correctamente")
    void testBuilderInicializacion_Exitoso() {
        assertAll("Validar todos los campos inicializados por el builder",
                () -> assertEquals("PERIOD_2025_1", academicPeriod.getPeriodId()),
                () -> assertEquals("Primer Semestre 2025", academicPeriod.getName()),
                () -> assertEquals(startDate, academicPeriod.getStartDate()),
                () -> assertEquals(endDate, academicPeriod.getEndDate()),
                () -> assertEquals(enrollmentStart, academicPeriod.getEnrollmentStart()),
                () -> assertEquals(enrollmentEnd, academicPeriod.getEnrollmentEnd()),
                () -> assertEquals(adjustmentStart, academicPeriod.getAdjustmentPeriodStart()),
                () -> assertEquals(adjustmentEnd, academicPeriod.getAdjustmentPeriodEnd()),
                () -> assertTrue(academicPeriod.isActive())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Constructor por defecto inicializa con valores por defecto")
    void testConstructorPorDefecto_Exitoso() {
        AcademicPeriod period = new AcademicPeriod();

        assertAll("Validar valores por defecto",
                () -> assertNull(period.getPeriodId()),
                () -> assertNull(period.getName()),
                () -> assertNull(period.getStartDate()),
                () -> assertNull(period.getEndDate()),
                () -> assertNull(period.getEnrollmentStart()),
                () -> assertNull(period.getEnrollmentEnd()),
                () -> assertNull(period.getAdjustmentPeriodStart()),
                () -> assertNull(period.getAdjustmentPeriodEnd()),
                () -> assertFalse(period.isActive())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Constructor con todos los parámetros")
    void testConstructorConParametros_Exitoso() {
        AcademicPeriod period = new AcademicPeriod(
                "PERIOD_2025_2",
                "Segundo Semestre 2025",
                startDate,
                endDate,
                enrollmentStart,
                enrollmentEnd,
                adjustmentStart,
                adjustmentEnd,
                false
        );

        assertAll("Validar constructor con parámetros",
                () -> assertEquals("PERIOD_2025_2", period.getPeriodId()),
                () -> assertEquals("Segundo Semestre 2025", period.getName()),
                () -> assertEquals(startDate, period.getStartDate()),
                () -> assertEquals(endDate, period.getEndDate()),
                () -> assertEquals(enrollmentStart, period.getEnrollmentStart()),
                () -> assertEquals(enrollmentEnd, period.getEnrollmentEnd()),
                () -> assertEquals(adjustmentStart, period.getAdjustmentPeriodStart()),
                () -> assertEquals(adjustmentEnd, period.getAdjustmentPeriodEnd()),
                () -> assertFalse(period.isActive())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Setters y Getters funcionan correctamente")
    void testSettersAndGetters_Exitoso() {
        AcademicPeriod period = new AcademicPeriod();
        Date newStartDate = new Date();
        Date newEndDate = new Date(System.currentTimeMillis() + 1000000);

        period.setPeriodId("PERIOD_2025_2");
        period.setName("Segundo Semestre 2025");
        period.setStartDate(newStartDate);
        period.setEndDate(newEndDate);
        period.setEnrollmentStart(newStartDate);
        period.setEnrollmentEnd(newEndDate);
        period.setAdjustmentPeriodStart(newStartDate);
        period.setAdjustmentPeriodEnd(newEndDate);
        period.setActive(false);

        assertAll("Validar setters y getters",
                () -> assertEquals("PERIOD_2025_2", period.getPeriodId()),
                () -> assertEquals("Segundo Semestre 2025", period.getName()),
                () -> assertEquals(newStartDate, period.getStartDate()),
                () -> assertEquals(newEndDate, period.getEndDate()),
                () -> assertEquals(newStartDate, period.getEnrollmentStart()),
                () -> assertEquals(newEndDate, period.getEnrollmentEnd()),
                () -> assertEquals(newStartDate, period.getAdjustmentPeriodStart()),
                () -> assertEquals(newEndDate, period.getAdjustmentPeriodEnd()),
                () -> assertFalse(period.isActive())
        );
    }

    @Test
    @DisplayName("Caso borde - Campos con valores null")
    void testCamposConValoresNull_Borde() {
        AcademicPeriod period = AcademicPeriod.builder()
                .periodId(null)
                .name(null)
                .startDate(null)
                .endDate(null)
                .enrollmentStart(null)
                .enrollmentEnd(null)
                .adjustmentPeriodStart(null)
                .adjustmentPeriodEnd(null)
                .isActive(false)
                .build();

        assertAll("Validar campos null",
                () -> assertNull(period.getPeriodId()),
                () -> assertNull(period.getName()),
                () -> assertNull(period.getStartDate()),
                () -> assertNull(period.getEndDate()),
                () -> assertNull(period.getEnrollmentStart()),
                () -> assertNull(period.getEnrollmentEnd()),
                () -> assertNull(period.getAdjustmentPeriodStart()),
                () -> assertNull(period.getAdjustmentPeriodEnd()),
                () -> assertFalse(period.isActive())
        );
    }

    @Test
    @DisplayName("Caso borde - Campos con valores vacíos")
    void testCamposConValoresVacios_Borde() {
        academicPeriod.setPeriodId("");
        academicPeriod.setName("");

        assertAll("Validar campos vacíos",
                () -> assertEquals("", academicPeriod.getPeriodId()),
                () -> assertEquals("", academicPeriod.getName())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Equals y HashCode generados por Lombok")
    void testEqualsAndHashCode_Exitoso() {
        AcademicPeriod period1 = AcademicPeriod.builder()
                .periodId("PERIOD_001")
                .name("Periodo 1")
                .startDate(startDate)
                .endDate(endDate)
                .isActive(true)
                .build();

        AcademicPeriod period2 = AcademicPeriod.builder()
                .periodId("PERIOD_001")
                .name("Periodo 1")
                .startDate(startDate)
                .endDate(endDate)
                .isActive(true)
                .build();

        AcademicPeriod period3 = AcademicPeriod.builder()
                .periodId("PERIOD_002")
                .name("Periodo 2")
                .startDate(new Date())
                .endDate(new Date())
                .isActive(false)
                .build();

        assertEquals(period1, period2);
        assertNotEquals(period1, period3);
        assertEquals(period1.hashCode(), period2.hashCode());
        assertNotEquals(period1.hashCode(), period3.hashCode());
    }

    @Test
    @DisplayName("Caso borde - Equals con objetos null y diferente clase")
    void testEqualsCasosBorde_Borde() {
        AcademicPeriod period = AcademicPeriod.builder()
                .periodId("PERIOD_001")
                .name("Test")
                .build();

        assertNotEquals(null, period);
        assertNotEquals("No soy un AcademicPeriod", period);
    }

    @Test
    @DisplayName("Caso exitoso - ToString generado por Lombok")
    void testToString_Exitoso() {
        String resultadoToString = academicPeriod.toString();

        assertAll("ToString debe contener todos los campos principales",
                () -> assertTrue(resultadoToString.contains("PERIOD_2025_1")),
                () -> assertTrue(resultadoToString.contains("Primer Semestre 2025")),
                () -> assertTrue(resultadoToString.contains("AcademicPeriod"))
        );
    }

    @Test
    @DisplayName("Caso compuesto - Validación completa de múltiples escenarios")
    void testAcademicPeriod_ValidacionCompleta_Compuesto() {
        assertAll("Validación completa de todas las propiedades",
                () -> assertEquals("PERIOD_2025_1", academicPeriod.getPeriodId()),
                () -> assertEquals("Primer Semestre 2025", academicPeriod.getName()),
                () -> assertEquals(startDate, academicPeriod.getStartDate()),
                () -> assertEquals(endDate, academicPeriod.getEndDate()),
                () -> assertEquals(enrollmentStart, academicPeriod.getEnrollmentStart()),
                () -> assertEquals(enrollmentEnd, academicPeriod.getEnrollmentEnd()),
                () -> assertEquals(adjustmentStart, academicPeriod.getAdjustmentPeriodStart()),
                () -> assertEquals(adjustmentEnd, academicPeriod.getAdjustmentPeriodEnd()),
                () -> assertTrue(academicPeriod.isActive()),
                () -> assertNotNull(academicPeriod.toString()),
                () -> assertTrue(academicPeriod.toString().contains("AcademicPeriod"))
        );
    }

    @Test
    @DisplayName("Caso borde - Valores extremos en fechas")
    void testValoresExtremosFechas_Borde() {
        Date minDate = new Date(Long.MIN_VALUE);
        Date maxDate = new Date(Long.MAX_VALUE);

        AcademicPeriod period = AcademicPeriod.builder()
                .periodId("PERIOD_EXTREMO")
                .startDate(minDate)
                .endDate(maxDate)
                .enrollmentStart(minDate)
                .enrollmentEnd(maxDate)
                .adjustmentPeriodStart(minDate)
                .adjustmentPeriodEnd(maxDate)
                .build();

        assertAll("Validar fechas extremas",
                () -> assertEquals(minDate, period.getStartDate()),
                () -> assertEquals(maxDate, period.getEndDate()),
                () -> assertEquals(minDate, period.getEnrollmentStart()),
                () -> assertEquals(maxDate, period.getEnrollmentEnd()),
                () -> assertEquals(minDate, period.getAdjustmentPeriodStart()),
                () -> assertEquals(maxDate, period.getAdjustmentPeriodEnd())
        );
    }

    @Test
    @DisplayName("Caso borde - Campo isActive con diferentes valores booleanos")
    void testCampoIsActive_ValoresBooleanos_Borde() {
        academicPeriod.setActive(true);
        assertTrue(academicPeriod.isActive());

        academicPeriod.setActive(false);
        assertFalse(academicPeriod.isActive());
    }

    @Test
    @DisplayName("Caso borde - Nombres con caracteres especiales y largos")
    void testNombresCaracteresEspeciales_Borde() {
        academicPeriod.setName("Período Académico 2025-Ⅰ 🎓");
        assertEquals("Período Académico 2025-Ⅰ 🎓", academicPeriod.getName());

        String nombreLargo = "A".repeat(500);
        academicPeriod.setName(nombreLargo);
        assertEquals(nombreLargo, academicPeriod.getName());
    }

    @Test
    @DisplayName("Caso borde - IDs con diferentes formatos")
    void testIDsConDiferentesFormatos_Borde() {
        academicPeriod.setPeriodId("2025-1");
        assertEquals("2025-1", academicPeriod.getPeriodId());

        academicPeriod.setPeriodId("PERIODO_VERANO_2025");
        assertEquals("PERIODO_VERANO_2025", academicPeriod.getPeriodId());

        academicPeriod.setPeriodId("12345");
        assertEquals("12345", academicPeriod.getPeriodId());
    }

    @Test
    @DisplayName("Caso borde - Builder con valor por defecto para isActive")
    void testBuilderValorPorDefecto_Borde() {
        AcademicPeriod period = AcademicPeriod.builder()
                .periodId("TEST")
                .name("Test")
                .build();

        assertFalse(period.isActive());
    }
}
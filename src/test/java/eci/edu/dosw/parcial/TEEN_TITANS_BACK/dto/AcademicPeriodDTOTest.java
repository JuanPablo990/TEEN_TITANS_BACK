package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class AcademicPeriodDTOTest {

    private AcademicPeriodDTO academicPeriodDTO;
    private Date currentDate;
    private Date startDate;
    private Date endDate;
    private Date enrollmentStart;
    private Date enrollmentEnd;
    private Date adjustmentPeriodStart;
    private Date adjustmentPeriodEnd;

    @BeforeEach
    void setUp() {
        currentDate = new Date();

        startDate = new Date(currentDate.getTime() - (1000 * 60 * 60 * 24 * 30));
        endDate = new Date(currentDate.getTime() + (1000 * 60 * 60 * 24 * 30));
        enrollmentStart = new Date(currentDate.getTime() - (1000 * 60 * 60 * 24 * 15));
        enrollmentEnd = new Date(currentDate.getTime() + (1000 * 60 * 60 * 24 * 15));
        adjustmentPeriodStart = new Date(currentDate.getTime() - (1000 * 60 * 60 * 24 * 5));
        adjustmentPeriodEnd = new Date(currentDate.getTime() + (1000 * 60 * 60 * 24 * 5));

        academicPeriodDTO = AcademicPeriodDTO.builder()
                .periodId("PERIOD_2025_01")
                .name("Primer Semestre 2025")
                .startDate(startDate)
                .endDate(endDate)
                .enrollmentStart(enrollmentStart)
                .enrollmentEnd(enrollmentEnd)
                .adjustmentPeriodStart(adjustmentPeriodStart)
                .adjustmentPeriodEnd(adjustmentPeriodEnd)
                .isActive(true)
                .build();
    }

    @Test
    @DisplayName("Caso exitoso - Creación de AcademicPeriodDTO con builder")
    void testAcademicPeriodDTOBuilder_Exitoso() {
        assertAll("Verificación de campos del DTO",
                () -> assertEquals("PERIOD_2025_01", academicPeriodDTO.getPeriodId()),
                () -> assertEquals("Primer Semestre 2025", academicPeriodDTO.getName()),
                () -> assertEquals(startDate, academicPeriodDTO.getStartDate()),
                () -> assertEquals(endDate, academicPeriodDTO.getEndDate()),
                () -> assertEquals(enrollmentStart, academicPeriodDTO.getEnrollmentStart()),
                () -> assertEquals(enrollmentEnd, academicPeriodDTO.getEnrollmentEnd()),
                () -> assertEquals(adjustmentPeriodStart, academicPeriodDTO.getAdjustmentPeriodStart()),
                () -> assertEquals(adjustmentPeriodEnd, academicPeriodDTO.getAdjustmentPeriodEnd()),
                () -> assertTrue(academicPeriodDTO.isActive())
        );
    }


    @Test
    @DisplayName("Caso error - isCurrentlyActive retorna false cuando las fechas son null")
    void testIsCurrentlyActive_FechasNull() {
        academicPeriodDTO.setStartDate(null);
        academicPeriodDTO.setEndDate(null);

        boolean isActive = academicPeriodDTO.isCurrentlyActive();
        assertFalse(isActive);
    }

    @Test
    @DisplayName("Caso exitoso - isEnrollmentOpen retorna true cuando el período de matrícula está abierto")
    void testIsEnrollmentOpen_Exitoso() {
        boolean isOpen = academicPeriodDTO.isEnrollmentOpen();
        assertTrue(isOpen);
    }

    @Test
    @DisplayName("Caso error - isEnrollmentOpen retorna false cuando el período de matrícula ha terminado")
    void testIsEnrollmentOpen_MatriculaCerrada() {
        Date pastEnrollmentStart = new Date(currentDate.getTime() - (1000 * 60 * 60 * 24 * 30));
        Date pastEnrollmentEnd = new Date(currentDate.getTime() - (1000 * 60 * 60 * 24 * 15));

        academicPeriodDTO.setEnrollmentStart(pastEnrollmentStart);
        academicPeriodDTO.setEnrollmentEnd(pastEnrollmentEnd);

        boolean isOpen = academicPeriodDTO.isEnrollmentOpen();
        assertFalse(isOpen);
    }

    @Test
    @DisplayName("Caso exitoso - isAdjustmentPeriodOpen retorna true cuando el período de ajuste está abierto")
    void testIsAdjustmentPeriodOpen_Exitoso() {
        boolean isOpen = academicPeriodDTO.isAdjustmentPeriodOpen();
        assertTrue(isOpen);
    }

    @Test
    @DisplayName("Caso borde - isAdjustmentPeriodOpen retorna false cuando no hay fechas de ajuste")
    void testIsAdjustmentPeriodOpen_SinFechasAjuste() {
        academicPeriodDTO.setAdjustmentPeriodStart(null);
        academicPeriodDTO.setAdjustmentPeriodEnd(null);

        boolean isOpen = academicPeriodDTO.isAdjustmentPeriodOpen();
        assertFalse(isOpen);
    }



    @Test
    @DisplayName("Caso borde - getDurationInDays retorna 0 cuando alguna fecha es null")
    void testGetDurationInDays_FechasNull() {
        academicPeriodDTO.setStartDate(null);
        academicPeriodDTO.setEndDate(null);

        long duration = academicPeriodDTO.getDurationInDays();
        assertEquals(0, duration);
    }

    @Test
    @DisplayName("Caso borde - equals y hashCode funcionan correctamente")
    void testEqualsAndHashCode_Exitoso() {
        AcademicPeriodDTO dto1 = AcademicPeriodDTO.builder()
                .periodId("PERIOD_2025_01")
                .name("Primer Semestre 2025")
                .startDate(startDate)
                .endDate(endDate)
                .build();

        AcademicPeriodDTO dto2 = AcademicPeriodDTO.builder()
                .periodId("PERIOD_2025_01")
                .name("Primer Semestre 2025")
                .startDate(startDate)
                .endDate(endDate)
                .build();

        assertAll("Verificación de equals y hashCode",
                () -> assertEquals(dto1, dto2),
                () -> assertEquals(dto1.hashCode(), dto2.hashCode())
        );
    }

    @Test
    @DisplayName("Caso borde - toString no retorna null")
    void testToString_NoNull() {
        String toStringResult = academicPeriodDTO.toString();
        assertNotNull(toStringResult);
        assertFalse(toStringResult.isEmpty());
    }

    @Test
    @DisplayName("Caso borde - Constructor sin argumentos crea instancia")
    void testNoArgsConstructor_Exitoso() {
        AcademicPeriodDTO emptyDTO = new AcademicPeriodDTO();
        assertNotNull(emptyDTO);
    }

    @Test
    @DisplayName("Caso borde - Setters funcionan correctamente")
    void testSetters_Exitoso() {
        AcademicPeriodDTO dto = new AcademicPeriodDTO();
        dto.setPeriodId("TEST_ID");
        dto.setName("Test Period");
        dto.setActive(false);

        assertAll("Verificación de setters",
                () -> assertEquals("TEST_ID", dto.getPeriodId()),
                () -> assertEquals("Test Period", dto.getName()),
                () -> assertFalse(dto.isActive())
        );
    }
}
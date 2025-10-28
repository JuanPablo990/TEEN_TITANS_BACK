package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.AcademicPeriod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataMongoTest
public class AcademicPeriodRepositoryTest {

    @MockBean
    private AcademicPeriodRepository academicPeriodRepository;

    private AcademicPeriod period1;
    private AcademicPeriod period2;
    private AcademicPeriod period3;
    private Date startDate1;
    private Date endDate1;
    private Date startDate2;
    private Date endDate2;

    @BeforeEach
    void setUp() {
        Calendar cal = Calendar.getInstance();

        cal.set(2024, Calendar.JANUARY, 1);
        startDate1 = cal.getTime();
        cal.set(2024, Calendar.MARCH, 31);
        endDate1 = cal.getTime();

        cal.set(2025, Calendar.JANUARY, 1);
        startDate2 = cal.getTime();
        cal.set(2025, Calendar.MARCH, 31);
        endDate2 = cal.getTime();

        period1 = AcademicPeriod.builder()
                .periodId("1")
                .name("2024-1")
                .startDate(startDate1)
                .endDate(endDate1)
                .enrollmentStart(startDate1)
                .enrollmentEnd(endDate1)
                .adjustmentPeriodStart(startDate1)
                .adjustmentPeriodEnd(endDate1)
                .isActive(false)
                .build();

        period2 = AcademicPeriod.builder()
                .periodId("2")
                .name("2025-1")
                .startDate(startDate2)
                .endDate(endDate2)
                .enrollmentStart(startDate2)
                .enrollmentEnd(endDate2)
                .adjustmentPeriodStart(startDate2)
                .adjustmentPeriodEnd(endDate2)
                .isActive(true)
                .build();

        period3 = AcademicPeriod.builder()
                .periodId("3")
                .name("2025-2")
                .startDate(new Date())
                .endDate(new Date(System.currentTimeMillis() + 86400000))
                .enrollmentStart(new Date())
                .enrollmentEnd(new Date(System.currentTimeMillis() + 86400000))
                .adjustmentPeriodStart(new Date())
                .adjustmentPeriodEnd(new Date(System.currentTimeMillis() + 86400000))
                .isActive(true)
                .build();
    }

    @Test
    @DisplayName("Caso exitoso - findByName retorna período cuando existe")
    void testFindByName_Exitoso() {
        when(academicPeriodRepository.findByName("2025-1"))
                .thenReturn(Optional.of(period2));

        Optional<AcademicPeriod> resultado = academicPeriodRepository.findByName("2025-1");

        assertAll("Verificar período encontrado por nombre",
                () -> assertTrue(resultado.isPresent()),
                () -> assertEquals("2025-1", resultado.get().getName()),
                () -> assertEquals("2", resultado.get().getPeriodId()),
                () -> assertTrue(resultado.get().isActive())
        );

        verify(academicPeriodRepository, times(1)).findByName("2025-1");
    }

    @Test
    @DisplayName("Caso error - findByName retorna vacío cuando no existe")
    void testFindByName_PeriodoNoExiste() {
        when(academicPeriodRepository.findByName("2026-1"))
                .thenReturn(Optional.empty());

        Optional<AcademicPeriod> resultado = academicPeriodRepository.findByName("2026-1");

        assertAll("Verificar que no se encuentra período inexistente",
                () -> assertFalse(resultado.isPresent()),
                () -> assertTrue(resultado.isEmpty())
        );

        verify(academicPeriodRepository, times(1)).findByName("2026-1");
    }

    @Test
    @DisplayName("Caso exitoso - findByIsActive retorna períodos activos")
    void testFindByIsActive_Exitoso() {
        when(academicPeriodRepository.findByIsActive(true))
                .thenReturn(List.of(period2, period3));

        List<AcademicPeriod> resultado = academicPeriodRepository.findByIsActive(true);

        assertAll("Verificar períodos activos",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertTrue(resultado.get(0).isActive()),
                () -> assertTrue(resultado.get(1).isActive()),
                () -> assertEquals("2025-1", resultado.get(0).getName()),
                () -> assertEquals("2025-2", resultado.get(1).getName())
        );

        verify(academicPeriodRepository, times(1)).findByIsActive(true);
    }

    @Test
    @DisplayName("Caso error - findByIsActive retorna lista vacía cuando no hay períodos activos")
    void testFindByIsActive_SinPeriodosActivos() {
        when(academicPeriodRepository.findByIsActive(true))
                .thenReturn(Collections.emptyList());

        List<AcademicPeriod> resultado = academicPeriodRepository.findByIsActive(true);

        assertTrue(resultado.isEmpty());
        verify(academicPeriodRepository, times(1)).findByIsActive(true);
    }


    @Test
    @DisplayName("Caso exitoso - findByEndDateBefore retorna períodos pasados")
    void testFindByEndDateBefore_Exitoso() {
        Date fechaReferencia = new Date();

        when(academicPeriodRepository.findByEndDateBefore(fechaReferencia))
                .thenReturn(List.of(period1));

        List<AcademicPeriod> resultado = academicPeriodRepository.findByEndDateBefore(fechaReferencia);

        assertAll("Verificar períodos con endDate antes de la fecha referencia",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertTrue(resultado.get(0).getEndDate().before(fechaReferencia)),
                () -> assertEquals("2024-1", resultado.get(0).getName())
        );

        verify(academicPeriodRepository, times(1)).findByEndDateBefore(fechaReferencia);
    }

    @Test
    @DisplayName("Caso exitoso - findByStartDateBetween retorna períodos en rango")
    void testFindByStartDateBetween_Exitoso() {
        Calendar cal = Calendar.getInstance();
        cal.set(2024, Calendar.DECEMBER, 1);
        Date startRange = cal.getTime();

        cal.set(2025, Calendar.DECEMBER, 1);
        Date endRange = cal.getTime();

        when(academicPeriodRepository.findByStartDateBetween(startRange, endRange))
                .thenReturn(List.of(period2));

        List<AcademicPeriod> resultado = academicPeriodRepository.findByStartDateBetween(startRange, endRange);

        assertAll("Verificar períodos dentro del rango de fechas",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertTrue(resultado.get(0).getStartDate().after(startRange)),
                () -> assertTrue(resultado.get(0).getStartDate().before(endRange)),
                () -> assertEquals("2025-1", resultado.get(0).getName())
        );

        verify(academicPeriodRepository, times(1)).findByStartDateBetween(startRange, endRange);
    }

    @Test
    @DisplayName("Caso exitoso - findByIsActiveAndStartDateBetween retorna períodos activos en rango")
    void testFindByIsActiveAndStartDateBetween_Exitoso() {
        Calendar cal = Calendar.getInstance();
        cal.set(2024, Calendar.DECEMBER, 1);
        Date startRange = cal.getTime();

        cal.set(2025, Calendar.DECEMBER, 1);
        Date endRange = cal.getTime();

        when(academicPeriodRepository.findByIsActiveAndStartDateBetween(true, startRange, endRange))
                .thenReturn(List.of(period2));

        List<AcademicPeriod> resultado = academicPeriodRepository.findByIsActiveAndStartDateBetween(true, startRange, endRange);

        assertAll("Verificar períodos activos dentro del rango de fechas",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertTrue(resultado.get(0).isActive()),
                () -> assertTrue(resultado.get(0).getStartDate().after(startRange)),
                () -> assertTrue(resultado.get(0).getStartDate().before(endRange)),
                () -> assertEquals("2025-1", resultado.get(0).getName())
        );

        verify(academicPeriodRepository, times(1)).findByIsActiveAndStartDateBetween(true, startRange, endRange);
    }

    @Test
    @DisplayName("Caso exitoso - findByIsActiveTrue retorna período activo actual")
    void testFindByIsActiveTrue_Exitoso() {
        when(academicPeriodRepository.findByIsActiveTrue())
                .thenReturn(Optional.of(period2));

        Optional<AcademicPeriod> resultado = academicPeriodRepository.findByIsActiveTrue();

        assertAll("Verificar período activo actual",
                () -> assertTrue(resultado.isPresent()),
                () -> assertTrue(resultado.get().isActive()),
                () -> assertEquals("2025-1", resultado.get().getName()),
                () -> assertEquals("2", resultado.get().getPeriodId())
        );

        verify(academicPeriodRepository, times(1)).findByIsActiveTrue();
    }

    @Test
    @DisplayName("Caso error - findByIsActiveTrue retorna vacío cuando no hay período activo")
    void testFindByIsActiveTrue_SinPeriodoActivo() {
        when(academicPeriodRepository.findByIsActiveTrue())
                .thenReturn(Optional.empty());

        Optional<AcademicPeriod> resultado = academicPeriodRepository.findByIsActiveTrue();

        assertAll("Verificar que no hay período activo",
                () -> assertFalse(resultado.isPresent()),
                () -> assertTrue(resultado.isEmpty())
        );

        verify(academicPeriodRepository, times(1)).findByIsActiveTrue();
    }

    @Test
    @DisplayName("Caso borde - findByName con nombre nulo")
    void testFindByName_NombreNulo() {
        when(academicPeriodRepository.findByName(null))
                .thenReturn(Optional.empty());

        Optional<AcademicPeriod> resultado = academicPeriodRepository.findByName(null);

        assertFalse(resultado.isPresent());
        verify(academicPeriodRepository, times(1)).findByName(null);
    }

    @Test
    @DisplayName("Caso borde - findByStartDateBetween con fechas nulas")
    void testFindByStartDateBetween_FechasNulas() {
        when(academicPeriodRepository.findByStartDateBetween(null, null))
                .thenReturn(Collections.emptyList());

        List<AcademicPeriod> resultado = academicPeriodRepository.findByStartDateBetween(null, null);

        assertTrue(resultado.isEmpty());
        verify(academicPeriodRepository, times(1)).findByStartDateBetween(null, null);
    }

    @Test
    @DisplayName("Caso borde - findByIsActive con múltiples estados")
    void testFindByIsActive_MultiplesEstados() {
        when(academicPeriodRepository.findByIsActive(false))
                .thenReturn(List.of(period1));

        List<AcademicPeriod> resultadoInactivos = academicPeriodRepository.findByIsActive(false);

        assertAll("Verificar períodos inactivos",
                () -> assertNotNull(resultadoInactivos),
                () -> assertEquals(1, resultadoInactivos.size()),
                () -> assertFalse(resultadoInactivos.get(0).isActive())
        );

        verify(academicPeriodRepository, times(1)).findByIsActive(false);
    }

    @Test
    @DisplayName("Caso borde - Verificar integridad de datos en períodos encontrados")
    void testIntegridadDatosPeriodos() {
        when(academicPeriodRepository.findByName("2025-1"))
                .thenReturn(Optional.of(period2));

        Optional<AcademicPeriod> resultado = academicPeriodRepository.findByName("2025-1");

        assertAll("Verificar integridad completa del período",
                () -> assertTrue(resultado.isPresent()),
                () -> assertEquals("2", resultado.get().getPeriodId()),
                () -> assertEquals("2025-1", resultado.get().getName()),
                () -> assertNotNull(resultado.get().getStartDate()),
                () -> assertNotNull(resultado.get().getEndDate()),
                () -> assertNotNull(resultado.get().getEnrollmentStart()),
                () -> assertNotNull(resultado.get().getEnrollmentEnd()),
                () -> assertNotNull(resultado.get().getAdjustmentPeriodStart()),
                () -> assertNotNull(resultado.get().getAdjustmentPeriodEnd()),
                () -> assertInstanceOf(AcademicPeriod.class, resultado.get()),
                () -> assertNotSame(period1, resultado.get())
        );
    }
}
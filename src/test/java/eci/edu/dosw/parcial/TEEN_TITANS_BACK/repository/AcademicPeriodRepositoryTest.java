package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.AcademicPeriod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataMongoTest
public class AcademicPeriodRepositoryTest {

    @MockBean
    private AcademicPeriodRepository academicPeriodRepository;

    private AcademicPeriod period1;
    private AcademicPeriod period2;

    private Date date1;
    private Date date2;
    private Date date3;

    @BeforeEach
    void setUp() {
        date1 = new Date(1704067200000L);
        date2 = new Date(1711929600000L);
        date3 = new Date(1719878400000L);

        period1 = new AcademicPeriod(
                "1", "2024-1",
                date1, date2,
                date1, date2,
                date1, date2,
                true
        );

        period2 = new AcademicPeriod(
                "2", "2024-2",
                date2, date3,
                date2, date3,
                date2, date3,
                false
        );
    }


    @Test
    @DisplayName("Caso exitoso - findByName retorna período existente")
    void testFindByName_Exitoso() {
        when(academicPeriodRepository.findByName("2024-1")).thenReturn(period1);

        AcademicPeriod resultado = academicPeriodRepository.findByName("2024-1");

        assertNotNull(resultado);
        assertEquals("2024-1", resultado.getName());
        assertTrue(resultado.isActive());
        verify(academicPeriodRepository, times(1)).findByName("2024-1");
    }

    @Test
    @DisplayName("Caso error - findByName retorna null para nombre inexistente")
    void testFindByName_Error() {
        when(academicPeriodRepository.findByName("2023-3")).thenReturn(null);

        AcademicPeriod resultado = academicPeriodRepository.findByName("2023-3");

        assertNull(resultado);
        verify(academicPeriodRepository, times(1)).findByName("2023-3");
    }


    @Test
    @DisplayName("Caso exitoso - findByIsActive retorna períodos activos")
    void testFindByIsActive_Exitoso() {
        when(academicPeriodRepository.findByIsActive(true)).thenReturn(List.of(period1));

        List<AcademicPeriod> resultado = academicPeriodRepository.findByIsActive(true);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).isActive());
        verify(academicPeriodRepository, times(1)).findByIsActive(true);
    }

    @Test
    @DisplayName("Caso error - findByIsActive retorna lista vacía cuando no hay períodos activos")
    void testFindByIsActive_Error() {
        when(academicPeriodRepository.findByIsActive(true)).thenReturn(Collections.emptyList());

        List<AcademicPeriod> resultado = academicPeriodRepository.findByIsActive(true);

        assertTrue(resultado.isEmpty());
        verify(academicPeriodRepository, times(1)).findByIsActive(true);
    }


    @Test
    @DisplayName("Caso exitoso - findByStartDateAfter retorna períodos posteriores a la fecha")
    void testFindByStartDateAfter_Exitoso() {
        when(academicPeriodRepository.findByStartDateAfter(date1)).thenReturn(List.of(period2));

        List<AcademicPeriod> resultado = academicPeriodRepository.findByStartDateAfter(date1);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getStartDate().after(date1));
        verify(academicPeriodRepository, times(1)).findByStartDateAfter(date1);
    }

    @Test
    @DisplayName("Caso error - findByStartDateAfter retorna lista vacía si no hay resultados")
    void testFindByStartDateAfter_Error() {
        when(academicPeriodRepository.findByStartDateAfter(date3)).thenReturn(Collections.emptyList());

        List<AcademicPeriod> resultado = academicPeriodRepository.findByStartDateAfter(date3);

        assertTrue(resultado.isEmpty());
        verify(academicPeriodRepository, times(1)).findByStartDateAfter(date3);
    }


    @Test
    @DisplayName("Caso exitoso - findByEndDateBefore retorna períodos anteriores a la fecha")
    void testFindByEndDateBefore_Exitoso() {
        when(academicPeriodRepository.findByEndDateBefore(date3)).thenReturn(List.of(period1));

        List<AcademicPeriod> resultado = academicPeriodRepository.findByEndDateBefore(date3);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getEndDate().before(date3));
        verify(academicPeriodRepository, times(1)).findByEndDateBefore(date3);
    }

    @Test
    @DisplayName("Caso error - findByEndDateBefore retorna lista vacía cuando no hay períodos")
    void testFindByEndDateBefore_Error() {
        when(academicPeriodRepository.findByEndDateBefore(date1)).thenReturn(Collections.emptyList());

        List<AcademicPeriod> resultado = academicPeriodRepository.findByEndDateBefore(date1);

        assertTrue(resultado.isEmpty());
        verify(academicPeriodRepository, times(1)).findByEndDateBefore(date1);
    }


    @Test
    @DisplayName("Caso exitoso - findByStartDateBetween retorna períodos dentro del rango")
    void testFindByStartDateBetween_Exitoso() {
        when(academicPeriodRepository.findByStartDateBetween(date1, date3)).thenReturn(List.of(period1, period2));

        List<AcademicPeriod> resultado = academicPeriodRepository.findByStartDateBetween(date1, date3);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(academicPeriodRepository, times(1)).findByStartDateBetween(date1, date3);
    }

    @Test
    @DisplayName("Caso error - findByStartDateBetween retorna lista vacía fuera del rango")
    void testFindByStartDateBetween_Error() {
        when(academicPeriodRepository.findByStartDateBetween(new Date(0), new Date(1000000))).thenReturn(Collections.emptyList());

        List<AcademicPeriod> resultado = academicPeriodRepository.findByStartDateBetween(new Date(0), new Date(1000000));

        assertTrue(resultado.isEmpty());
        verify(academicPeriodRepository, times(1)).findByStartDateBetween(any(), any());
    }


    @Test
    @DisplayName("Caso exitoso - findByIsActiveAndStartDateBetween retorna períodos activos en rango")
    void testFindByIsActiveAndStartDateBetween_Exitoso() {
        when(academicPeriodRepository.findByIsActiveAndStartDateBetween(true, date1, date3)).thenReturn(List.of(period1));

        List<AcademicPeriod> resultado = academicPeriodRepository.findByIsActiveAndStartDateBetween(true, date1, date3);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).isActive());
        verify(academicPeriodRepository, times(1)).findByIsActiveAndStartDateBetween(true, date1, date3);
    }

    @Test
    @DisplayName("Caso error - findByIsActiveAndStartDateBetween retorna lista vacía cuando no hay coincidencias")
    void testFindByIsActiveAndStartDateBetween_Error() {
        when(academicPeriodRepository.findByIsActiveAndStartDateBetween(true, date2, date3))
                .thenReturn(Collections.emptyList());

        List<AcademicPeriod> resultado = academicPeriodRepository.findByIsActiveAndStartDateBetween(true, date2, date3);

        assertTrue(resultado.isEmpty());
        verify(academicPeriodRepository, times(1)).findByIsActiveAndStartDateBetween(true, date2, date3);
    }
}
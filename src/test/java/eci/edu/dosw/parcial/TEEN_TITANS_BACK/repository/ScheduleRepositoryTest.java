package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Schedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DuplicateKeyException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ScheduleRepositoryTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    private Schedule schedule1;
    private Schedule schedule2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        schedule1 = new Schedule("S001", "Lunes", "08:00", "10:00", "2025-1");
        schedule2 = new Schedule("S002", "Martes", "10:00", "12:00", "2025-1");
    }

    @Test
    @DisplayName("Caso exitoso - Guardar un horario correctamente")
    void testSaveSchedule_Exitoso() {
        when(scheduleRepository.save(schedule1)).thenReturn(schedule1);

        Schedule resultado = scheduleRepository.save(schedule1);

        assertNotNull(resultado);
        assertEquals("Lunes", resultado.getDayOfWeek());
        assertEquals("08:00", resultado.getStartHour());
        verify(scheduleRepository, times(1)).save(schedule1);
    }

    @Test
    @DisplayName("Caso error - Guardar un horario con ID duplicado")
    void testSaveSchedule_Error() {
        when(scheduleRepository.save(schedule1)).thenThrow(DuplicateKeyException.class);

        assertThrows(DuplicateKeyException.class, () -> scheduleRepository.save(schedule1));
        verify(scheduleRepository, times(1)).save(schedule1);
    }

    @Test
    @DisplayName("Caso exitoso - Buscar horario por ID existente")
    void testFindById_Exitoso() {
        when(scheduleRepository.findById("S001")).thenReturn(Optional.of(schedule1));

        Optional<Schedule> resultado = scheduleRepository.findById("S001");

        assertTrue(resultado.isPresent());
        assertEquals("S001", resultado.get().getScheduleId());
        assertEquals("Lunes", resultado.get().getDayOfWeek());
        verify(scheduleRepository, times(1)).findById("S001");
    }

    @Test
    @DisplayName("Caso error - Buscar horario por ID inexistente")
    void testFindById_Error() {
        when(scheduleRepository.findById("S999")).thenReturn(Optional.empty());

        Optional<Schedule> resultado = scheduleRepository.findById("S999");

        assertTrue(resultado.isEmpty());
        verify(scheduleRepository, times(1)).findById("S999");
    }

    @Test
    @DisplayName("Caso exitoso - Buscar horarios por día de la semana")
    void testFindByDayOfWeek_Exitoso() {
        when(scheduleRepository.findByDayOfWeek("Lunes")).thenReturn(List.of(schedule1));

        List<Schedule> resultado = scheduleRepository.findByDayOfWeek("Lunes");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Lunes", resultado.get(0).getDayOfWeek());
        verify(scheduleRepository, times(1)).findByDayOfWeek("Lunes");
    }

    @Test
    @DisplayName("Caso error - Buscar horarios por día inexistente")
    void testFindByDayOfWeek_Error() {
        when(scheduleRepository.findByDayOfWeek("Domingo")).thenReturn(List.of());

        List<Schedule> resultado = scheduleRepository.findByDayOfWeek("Domingo");

        assertTrue(resultado.isEmpty());
        verify(scheduleRepository, times(1)).findByDayOfWeek("Domingo");
    }

    @Test
    @DisplayName("Caso exitoso - Buscar horarios por período académico")
    void testFindByPeriod_Exitoso() {
        when(scheduleRepository.findByPeriod("2025-1")).thenReturn(List.of(schedule1, schedule2));

        List<Schedule> resultado = scheduleRepository.findByPeriod("2025-1");

        assertEquals(2, resultado.size());
        assertEquals("2025-1", resultado.get(0).getPeriod());
        verify(scheduleRepository, times(1)).findByPeriod("2025-1");
    }

    @Test
    @DisplayName("Caso error - Buscar horarios por período inexistente")
    void testFindByPeriod_Error() {
        when(scheduleRepository.findByPeriod("2030-1")).thenReturn(List.of());

        List<Schedule> resultado = scheduleRepository.findByPeriod("2030-1");

        assertTrue(resultado.isEmpty());
        verify(scheduleRepository, times(1)).findByPeriod("2030-1");
    }
}

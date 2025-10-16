package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.ReviewStep;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.UserRole;
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
public class ReviewStepRepositoryTest {

    @MockBean
    private ReviewStepRepository reviewStepRepository;

    private ReviewStep step1;
    private ReviewStep step2;
    private Date pastDate;
    private Date futureDate;

    @BeforeEach
    void setUp() {
        step1 = new ReviewStep("U001", UserRole.PROFESSOR, "APPROVED", "Revisión completada");
        step2 = new ReviewStep("U002", UserRole.DEAN, "REJECTED", "Observaciones pendientes");
        pastDate = new Date(System.currentTimeMillis() - 100000);
        futureDate = new Date(System.currentTimeMillis() + 100000);
    }

    @Test
    @DisplayName("Caso exitoso - findByUserId retorna pasos por usuario")
    void testFindByUserId_Exitoso() {
        when(reviewStepRepository.findByUserId("U001")).thenReturn(List.of(step1));
        List<ReviewStep> resultado = reviewStepRepository.findByUserId("U001");
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("U001", resultado.get(0).getUserId());
        verify(reviewStepRepository, times(1)).findByUserId("U001");
    }

    @Test
    @DisplayName("Caso error - findByUserId retorna lista vacía para usuario inexistente")
    void testFindByUserId_Error() {
        when(reviewStepRepository.findByUserId("U999")).thenReturn(Collections.emptyList());
        List<ReviewStep> resultado = reviewStepRepository.findByUserId("U999");
        assertTrue(resultado.isEmpty());
        verify(reviewStepRepository, times(1)).findByUserId("U999");
    }

    @Test
    @DisplayName("Caso exitoso - findByUserRole retorna pasos por rol de usuario")
    void testFindByUserRole_Exitoso() {
        when(reviewStepRepository.findByUserRole(UserRole.DEAN)).thenReturn(List.of(step2));
        List<ReviewStep> resultado = reviewStepRepository.findByUserRole(UserRole.DEAN);
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(UserRole.DEAN, resultado.get(0).getUserRole());
        verify(reviewStepRepository, times(1)).findByUserRole(UserRole.DEAN);
    }

    @Test
    @DisplayName("Caso error - findByUserRole retorna lista vacía para rol inexistente")
    void testFindByUserRole_Error() {
        when(reviewStepRepository.findByUserRole(UserRole.ADMINISTRATOR)).thenReturn(Collections.emptyList());
        List<ReviewStep> resultado = reviewStepRepository.findByUserRole(UserRole.ADMINISTRATOR);
        assertTrue(resultado.isEmpty());
        verify(reviewStepRepository, times(1)).findByUserRole(UserRole.ADMINISTRATOR);
    }

    @Test
    @DisplayName("Caso exitoso - findByAction retorna pasos por acción")
    void testFindByAction_Exitoso() {
        when(reviewStepRepository.findByAction("APPROVED")).thenReturn(List.of(step1));
        List<ReviewStep> resultado = reviewStepRepository.findByAction("APPROVED");
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("APPROVED", resultado.get(0).getAction());
        verify(reviewStepRepository, times(1)).findByAction("APPROVED");
    }

    @Test
    @DisplayName("Caso error - findByAction retorna lista vacía para acción inexistente")
    void testFindByAction_Error() {
        when(reviewStepRepository.findByAction("UNKNOWN")).thenReturn(Collections.emptyList());
        List<ReviewStep> resultado = reviewStepRepository.findByAction("UNKNOWN");
        assertTrue(resultado.isEmpty());
        verify(reviewStepRepository, times(1)).findByAction("UNKNOWN");
    }

    @Test
    @DisplayName("Caso exitoso - findByTimestampAfter retorna pasos posteriores a fecha")
    void testFindByTimestampAfter_Exitoso() {
        when(reviewStepRepository.findByTimestampAfter(pastDate)).thenReturn(List.of(step1, step2));
        List<ReviewStep> resultado = reviewStepRepository.findByTimestampAfter(pastDate);
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(reviewStepRepository, times(1)).findByTimestampAfter(pastDate);
    }

    @Test
    @DisplayName("Caso error - findByTimestampAfter retorna lista vacía cuando no hay coincidencias")
    void testFindByTimestampAfter_Error() {
        when(reviewStepRepository.findByTimestampAfter(futureDate)).thenReturn(Collections.emptyList());
        List<ReviewStep> resultado = reviewStepRepository.findByTimestampAfter(futureDate);
        assertTrue(resultado.isEmpty());
        verify(reviewStepRepository, times(1)).findByTimestampAfter(futureDate);
    }

    @Test
    @DisplayName("Caso exitoso - findByTimestampBefore retorna pasos anteriores a fecha")
    void testFindByTimestampBefore_Exitoso() {
        when(reviewStepRepository.findByTimestampBefore(futureDate)).thenReturn(List.of(step1, step2));
        List<ReviewStep> resultado = reviewStepRepository.findByTimestampBefore(futureDate);
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(reviewStepRepository, times(1)).findByTimestampBefore(futureDate);
    }

    @Test
    @DisplayName("Caso error - findByTimestampBefore retorna lista vacía cuando no hay coincidencias")
    void testFindByTimestampBefore_Error() {
        when(reviewStepRepository.findByTimestampBefore(pastDate)).thenReturn(Collections.emptyList());
        List<ReviewStep> resultado = reviewStepRepository.findByTimestampBefore(pastDate);
        assertTrue(resultado.isEmpty());
        verify(reviewStepRepository, times(1)).findByTimestampBefore(pastDate);
    }

    @Test
    @DisplayName("Caso exitoso - findByUserIdAndAction retorna pasos por usuario y acción")
    void testFindByUserIdAndAction_Exitoso() {
        when(reviewStepRepository.findByUserIdAndAction("U001", "APPROVED")).thenReturn(List.of(step1));
        List<ReviewStep> resultado = reviewStepRepository.findByUserIdAndAction("U001", "APPROVED");
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("U001", resultado.get(0).getUserId());
        assertEquals("APPROVED", resultado.get(0).getAction());
        verify(reviewStepRepository, times(1)).findByUserIdAndAction("U001", "APPROVED");
    }

    @Test
    @DisplayName("Caso error - findByUserIdAndAction retorna lista vacía cuando no hay coincidencias")
    void testFindByUserIdAndAction_Error() {
        when(reviewStepRepository.findByUserIdAndAction("U999", "UNKNOWN")).thenReturn(Collections.emptyList());
        List<ReviewStep> resultado = reviewStepRepository.findByUserIdAndAction("U999", "UNKNOWN");
        assertTrue(resultado.isEmpty());
        verify(reviewStepRepository, times(1)).findByUserIdAndAction("U999", "UNKNOWN");
    }
}

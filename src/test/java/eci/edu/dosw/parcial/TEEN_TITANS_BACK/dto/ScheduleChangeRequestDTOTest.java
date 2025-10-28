package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.RequestStatus;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ScheduleChangeRequestDTOTest {

    private ScheduleChangeRequestDTO dto;
    private ReviewStepDTO reviewStep1;
    private ReviewStepDTO reviewStep2;
    private Date submissionDate;
    private Date resolutionDate;

    @BeforeEach
    void setUp() {
        submissionDate = new Date(System.currentTimeMillis() - (10L * 24 * 60 * 60 * 1000));
        resolutionDate = new Date(System.currentTimeMillis() - (2L * 24 * 60 * 60 * 1000));

        reviewStep1 = ReviewStepDTO.builder()
                .userId("PROF001")
                .userRole(UserRole.PROFESSOR)
                .action("Revisión inicial")
                .timestamp(new Date(System.currentTimeMillis() - (5L * 24 * 60 * 60 * 1000)))
                .comments("Solicitud en proceso de revisión")
                .build();

        reviewStep2 = ReviewStepDTO.builder()
                .userId("ADMIN001")
                .userRole(UserRole.ADMINISTRATOR)
                .action("Aprobación final")
                .timestamp(new Date(System.currentTimeMillis() - (1L * 24 * 60 * 60 * 1000)))
                .comments("Solicitud aprobada")
                .build();

        dto = ScheduleChangeRequestDTO.builder()
                .requestId("REQ001")
                .studentId("STU001")
                .studentName("Juan Pérez")
                .currentGroupId("GRP001")
                .currentGroupSection("A")
                .requestedGroupId("GRP002")
                .requestedGroupSection("B")
                .reason("Conflicto de horario")
                .status(RequestStatus.APPROVED)
                .submissionDate(submissionDate)
                .resolutionDate(resolutionDate)
                .reviewHistory(Arrays.asList(reviewStep1, reviewStep2))
                .build();
    }

    @Test
    @DisplayName("Caso exitoso - isResolved retorna true para estado APPROVED")
    void testIsResolved_Approved_Exitoso() {
        dto.setStatus(RequestStatus.APPROVED);
        boolean resultado = dto.isResolved();
        assertTrue(resultado);
    }

    @Test
    @DisplayName("Caso exitoso - isResolved retorna true para estado REJECTED")
    void testIsResolved_Rejected_Exitoso() {
        dto.setStatus(RequestStatus.REJECTED);
        boolean resultado = dto.isResolved();
        assertTrue(resultado);
    }

    @Test
    @DisplayName("Caso error - isResolved retorna false para estado PENDING")
    void testIsResolved_Pending_Error() {
        dto.setStatus(RequestStatus.PENDING);
        boolean resultado = dto.isResolved();
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Caso error - isResolved retorna false para estado UNDER_REVIEW")
    void testIsResolved_UnderReview_Error() {
        dto.setStatus(RequestStatus.UNDER_REVIEW);
        boolean resultado = dto.isResolved();
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Caso error - isResolved retorna false para estado CANCELLED")
    void testIsResolved_Cancelled_Error() {
        dto.setStatus(RequestStatus.CANCELLED);
        boolean resultado = dto.isResolved();
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Caso exitoso - isPendingOrUnderReview retorna true para PENDING")
    void testIsPendingOrUnderReview_Pending_Exitoso() {
        dto.setStatus(RequestStatus.PENDING);
        boolean resultado = dto.isPendingOrUnderReview();
        assertTrue(resultado);
    }

    @Test
    @DisplayName("Caso exitoso - isPendingOrUnderReview retorna true para UNDER_REVIEW")
    void testIsPendingOrUnderReview_UnderReview_Exitoso() {
        dto.setStatus(RequestStatus.UNDER_REVIEW);
        boolean resultado = dto.isPendingOrUnderReview();
        assertTrue(resultado);
    }

    @Test
    @DisplayName("Caso error - isPendingOrUnderReview retorna false para APPROVED")
    void testIsPendingOrUnderReview_Approved_Error() {
        dto.setStatus(RequestStatus.APPROVED);
        boolean resultado = dto.isPendingOrUnderReview();
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Caso exitoso - isCancelled retorna true para CANCELLED")
    void testIsCancelled_Cancelled_Exitoso() {
        dto.setStatus(RequestStatus.CANCELLED);
        boolean resultado = dto.isCancelled();
        assertTrue(resultado);
    }

    @Test
    @DisplayName("Caso error - isCancelled retorna false para APPROVED")
    void testIsCancelled_Approved_Error() {
        dto.setStatus(RequestStatus.APPROVED);
        boolean resultado = dto.isCancelled();
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Caso exitoso - getProcessingDurationInDays calcula duración correcta")
    void testGetProcessingDurationInDays_ConFechas_Exitoso() {
        long duracion = dto.getProcessingDurationInDays();
        assertEquals(8L, duracion);
    }

    @Test
    @DisplayName("Caso borde - getProcessingDurationInDays retorna 0 sin submissionDate")
    void testGetProcessingDurationInDays_SinSubmissionDate_Borde() {
        dto.setSubmissionDate(null);
        long duracion = dto.getProcessingDurationInDays();
        assertEquals(0L, duracion);
    }

    @Test
    @DisplayName("Caso exitoso - getReviewStepCount retorna cantidad correcta")
    void testGetReviewStepCount_ConHistorial_Exitoso() {
        int count = dto.getReviewStepCount();
        assertEquals(2, count);
    }

    @Test
    @DisplayName("Caso error - getReviewStepCount retorna 0 sin historial")
    void testGetReviewStepCount_SinHistorial_Error() {
        dto.setReviewHistory(null);
        int count = dto.getReviewStepCount();
        assertEquals(0, count);
    }

    @Test
    @DisplayName("Caso exitoso - hasReviewHistory retorna true con historial")
    void testHasReviewHistory_ConHistorial_Exitoso() {
        boolean tieneHistorial = dto.hasReviewHistory();
        assertTrue(tieneHistorial);
    }

    @Test
    @DisplayName("Caso error - hasReviewHistory retorna false sin historial")
    void testHasReviewHistory_SinHistorial_Error() {
        dto.setReviewHistory(null);
        boolean tieneHistorial = dto.hasReviewHistory();
        assertFalse(tieneHistorial);
    }

    @Test
    @DisplayName("Caso error - hasReviewHistory retorna false con lista vacía")
    void testHasReviewHistory_ListaVacia_Error() {
        dto.setReviewHistory(Collections.emptyList());
        boolean tieneHistorial = dto.hasReviewHistory();
        assertFalse(tieneHistorial);
    }

    @Test
    @DisplayName("Caso exitoso - getLastReviewStep retorna último paso")
    void testGetLastReviewStep_ConHistorial_Exitoso() {
        ReviewStepDTO ultimoPaso = dto.getLastReviewStep();
        assertNotNull(ultimoPaso);
        assertEquals("ADMIN001", ultimoPaso.getUserId());
        assertEquals("Aprobación final", ultimoPaso.getAction());
    }

    @Test
    @DisplayName("Caso error - getLastReviewStep retorna null sin historial")
    void testGetLastReviewStep_SinHistorial_Error() {
        dto.setReviewHistory(null);
        ReviewStepDTO ultimoPaso = dto.getLastReviewStep();
        assertNull(ultimoPaso);
    }

    @Test
    @DisplayName("Caso error - getLastReviewStep retorna null con lista vacía")
    void testGetLastReviewStep_ListaVacia_Error() {
        dto.setReviewHistory(Collections.emptyList());
        ReviewStepDTO ultimoPaso = dto.getLastReviewStep();
        assertNull(ultimoPaso);
    }

    @Test
    @DisplayName("Caso exitoso - getStatusDescription retorna descripción correcta para APPROVED")
    void testGetStatusDescription_Approved_Exitoso() {
        dto.setStatus(RequestStatus.APPROVED);
        String descripcion = dto.getStatusDescription();
        assertEquals("Aprobada", descripcion);
    }

    @Test
    @DisplayName("Caso exitoso - getStatusDescription retorna descripción correcta para PENDING")
    void testGetStatusDescription_Pending_Exitoso() {
        dto.setStatus(RequestStatus.PENDING);
        String descripcion = dto.getStatusDescription();
        assertEquals("Pendiente de revisión", descripcion);
    }

    @Test
    @DisplayName("Caso borde - getStatusDescription retorna desconocido para null")
    void testGetStatusDescription_Null_Borde() {
        dto.setStatus(null);
        String descripcion = dto.getStatusDescription();
        assertEquals("Estado desconocido", descripcion);
    }

    @Test
    @DisplayName("Caso borde - getProcessingDurationInDays usa fecha actual sin resolutionDate")
    void testGetProcessingDurationInDays_SinResolutionDate_Borde() {
        dto.setResolutionDate(null);
        long duracion = dto.getProcessingDurationInDays();
        assertTrue(duracion >= 10L);
    }

    @Test
    @DisplayName("Caso compuesto - validar múltiples propiedades del DTO")
    void testScheduleChangeRequestDTO_ValidacionCompleta_Compuesto() {
        assertAll("Validación completa del DTO",
                () -> assertEquals("REQ001", dto.getRequestId()),
                () -> assertEquals("STU001", dto.getStudentId()),
                () -> assertEquals("Juan Pérez", dto.getStudentName()),
                () -> assertEquals("GRP001", dto.getCurrentGroupId()),
                () -> assertEquals("A", dto.getCurrentGroupSection()),
                () -> assertEquals("GRP002", dto.getRequestedGroupId()),
                () -> assertEquals("B", dto.getRequestedGroupSection()),
                () -> assertEquals("Conflicto de horario", dto.getReason()),
                () -> assertEquals(RequestStatus.APPROVED, dto.getStatus()),
                () -> assertNotNull(dto.getSubmissionDate()),
                () -> assertNotNull(dto.getResolutionDate()),
                () -> assertEquals(2, dto.getReviewHistory().size())
        );
    }
}
package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AcademicDTOTest {

    @Test
    void constructorVacio_CreaInstancia() {
        AcademicDTO dto = new AcademicDTO();

        assertNotNull(dto);
    }

    @Test
    void constructorCompleto_InicializaTodosLosCampos() {
        Date now = new Date();
        AcademicDTO dto = new AcademicDTO(
                "CS101", "Computer Science", 3, "Programming fundamentals",
                "Computer Science", true, "SCH001", "Monday", "08:00", "10:00",
                "2025-1", "REQ001", "STU001", "GRP001", "GRP002", "Schedule conflict",
                "PENDING", now, null, "STAT001", "ENROLLED", 85.5, "2025-1",
                now, null, 3, false, "Good progress", "GRP001", "A", "CLASS001",
                "Engineering", "101", 30, "Lecture"
        );

        assertEquals("CS101", dto.getCourseCode());
        assertEquals("Computer Science", dto.getCourseName());
        assertEquals(3, dto.getCourseCredits());
        assertEquals("PENDING", dto.getRequestStatus());
    }

    @Test
    void settersYGetters_FuncionanCorrectamente() {
        AcademicDTO dto = new AcademicDTO();
        Date testDate = new Date();

        dto.setCourseCode("MATH101");
        dto.setCourseName("Calculus");
        dto.setCourseCredits(4);
        dto.setRequestStatus("APPROVED");
        dto.setSubmissionDate(testDate);
        dto.setGrade(92.0);

        assertEquals("MATH101", dto.getCourseCode());
        assertEquals("Calculus", dto.getCourseName());
        assertEquals(4, dto.getCourseCredits());
        assertEquals("APPROVED", dto.getRequestStatus());
        assertEquals(testDate, dto.getSubmissionDate());
        assertEquals(92.0, dto.getGrade());
    }

    @Test
    void addReviewStep_ConListaNueva_CreaListaYAgrega() {
        AcademicDTO dto = new AcademicDTO();
        AcademicDTO.ReviewStepDTO reviewStep = new AcademicDTO.ReviewStepDTO(
                "USER001", "PROFESSOR", "APPROVED", "Looks good"
        );

        dto.addReviewStep(reviewStep);

        assertNotNull(dto.getReviewHistory());
        assertEquals(1, dto.getReviewHistory().size());
        assertEquals("USER001", dto.getReviewHistory().get(0).getUserId());
    }

    @Test
    void addReviewStep_ConListaExistente_AgregaALaLista() {
        AcademicDTO dto = new AcademicDTO();
        AcademicDTO.ReviewStepDTO reviewStep1 = new AcademicDTO.ReviewStepDTO(
                "USER001", "PROFESSOR", "REVIEWED", "Initial review"
        );
        AcademicDTO.ReviewStepDTO reviewStep2 = new AcademicDTO.ReviewStepDTO(
                "USER002", "DEAN", "APPROVED", "Final approval"
        );

        dto.addReviewStep(reviewStep1);
        dto.addReviewStep(reviewStep2);

        assertEquals(2, dto.getReviewHistory().size());
        assertEquals("DEAN", dto.getReviewHistory().get(1).getUserRole());
    }

    @Test
    void reviewStepDTO_ConstructorVacio_CreaInstancia() {
        AcademicDTO.ReviewStepDTO reviewStep = new AcademicDTO.ReviewStepDTO();

        assertNotNull(reviewStep);
    }

    @Test
    void reviewStepDTO_ConstructorCompleto_InicializaCampos() {
        AcademicDTO.ReviewStepDTO reviewStep = new AcademicDTO.ReviewStepDTO(
                "USER003", "ADMIN", "REJECTED", "Missing information"
        );

        assertEquals("USER003", reviewStep.getUserId());
        assertEquals("ADMIN", reviewStep.getUserRole());
        assertEquals("REJECTED", reviewStep.getAction());
        assertEquals("Missing information", reviewStep.getComments());
        assertNotNull(reviewStep.getTimestamp());
    }

    @Test
    void reviewStepDTO_SettersYGetters_FuncionanCorrectamente() {
        AcademicDTO.ReviewStepDTO reviewStep = new AcademicDTO.ReviewStepDTO();
        Date testDate = new Date();

        reviewStep.setUserId("USER004");
        reviewStep.setUserRole("PROFESSOR");
        reviewStep.setAction("COMMENTED");
        reviewStep.setComments("Need more details");
        reviewStep.setTimestamp(testDate);

        assertEquals("USER004", reviewStep.getUserId());
        assertEquals("PROFESSOR", reviewStep.getUserRole());
        assertEquals("COMMENTED", reviewStep.getAction());
        assertEquals("Need more details", reviewStep.getComments());
        assertEquals(testDate, reviewStep.getTimestamp());
    }

    @Test
    void camposBooleanos_FuncionanCorrectamente() {
        AcademicDTO dto = new AcademicDTO();

        dto.setIsCourseActive(true);
        dto.setIsApproved(false);

        assertTrue(dto.getIsCourseActive());
        assertFalse(dto.getIsApproved());
    }

    @Test
    void camposNumericos_FuncionanCorrectamente() {
        AcademicDTO dto = new AcademicDTO();

        dto.setCourseCredits(4);
        dto.setCreditsEarned(120);
        dto.setCapacity(50);
        dto.setGrade(87.5);

        assertEquals(4, dto.getCourseCredits());
        assertEquals(120, dto.getCreditsEarned());
        assertEquals(50, dto.getCapacity());
        assertEquals(87.5, dto.getGrade());
    }

    @Test
    void camposFecha_FuncionanCorrectamente() {
        AcademicDTO dto = new AcademicDTO();
        Date enrollmentDate = new Date();
        Date completionDate = new Date(System.currentTimeMillis() + 86400000);

        dto.setEnrollmentDate(enrollmentDate);
        dto.setCompletionDate(completionDate);

        assertEquals(enrollmentDate, dto.getEnrollmentDate());
        assertEquals(completionDate, dto.getCompletionDate());
    }

    @Test
    void setReviewHistory_ReemplazaListaExistente() {
        AcademicDTO dto = new AcademicDTO();
        AcademicDTO.ReviewStepDTO reviewStep1 = new AcademicDTO.ReviewStepDTO("USER001", "PROFESSOR", "ACTION1", "Comment1");
        dto.addReviewStep(reviewStep1);

        List<AcademicDTO.ReviewStepDTO> newHistory = Arrays.asList(
                new AcademicDTO.ReviewStepDTO("USER002", "DEAN", "ACTION2", "Comment2"),
                new AcademicDTO.ReviewStepDTO("USER003", "ADMIN", "ACTION3", "Comment3")
        );

        dto.setReviewHistory(newHistory);

        assertEquals(2, dto.getReviewHistory().size());
        assertEquals("DEAN", dto.getReviewHistory().get(0).getUserRole());
    }
}
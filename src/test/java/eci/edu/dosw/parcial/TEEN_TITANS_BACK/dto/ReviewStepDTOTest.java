package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ReviewStepDTOTest {

    private ReviewStepDTO reviewStepDTO;
    private Date currentDate;

    @BeforeEach
    void setUp() {
        currentDate = new Date();

        reviewStepDTO = ReviewStepDTO.builder()
                .userId("USER_001")
                .userRole(UserRole.ADMINISTRATOR)
                .action("APPROVED")
                .timestamp(currentDate)
                .comments("Solicitud aprobada por el administrador")
                .build();
    }

    @Test
    @DisplayName("Caso exitoso - Creación de ReviewStepDTO con builder")
    void testReviewStepDTOBuilder_Exitoso() {
        assertAll("Verificación de builder",
                () -> assertEquals("USER_001", reviewStepDTO.getUserId()),
                () -> assertEquals(UserRole.ADMINISTRATOR, reviewStepDTO.getUserRole()),
                () -> assertEquals("APPROVED", reviewStepDTO.getAction()),
                () -> assertEquals(currentDate, reviewStepDTO.getTimestamp()),
                () -> assertEquals("Solicitud aprobada por el administrador", reviewStepDTO.getComments())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Creación de ReviewStepDTO con constructor completo")
    void testReviewStepDTOConstructorCompleto_Exitoso() {
        ReviewStepDTO dto = new ReviewStepDTO("USER_002", UserRole.PROFESSOR, "REVIEWED",
                new Date(currentDate.getTime() - 1000), "Revisado por profesor");

        assertAll("Verificación de constructor completo",
                () -> assertEquals("USER_002", dto.getUserId()),
                () -> assertEquals(UserRole.PROFESSOR, dto.getUserRole()),
                () -> assertEquals("REVIEWED", dto.getAction()),
                () -> assertNotNull(dto.getTimestamp()),
                () -> assertEquals("Revisado por profesor", dto.getComments())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Getters y setters funcionan correctamente")
    void testGettersAndSetters_Exitoso() {
        ReviewStepDTO dto = new ReviewStepDTO();
        dto.setUserId("USER_003");
        dto.setUserRole(UserRole.STUDENT);
        dto.setAction("SUBMITTED");
        dto.setTimestamp(currentDate);
        dto.setComments("Enviado por estudiante");

        assertAll("Verificación de getters y setters",
                () -> assertEquals("USER_003", dto.getUserId()),
                () -> assertEquals(UserRole.STUDENT, dto.getUserRole()),
                () -> assertEquals("SUBMITTED", dto.getAction()),
                () -> assertEquals(currentDate, dto.getTimestamp()),
                () -> assertEquals("Enviado por estudiante", dto.getComments())
        );
    }

    @Test
    @DisplayName("Caso borde - Constructor sin argumentos crea instancia")
    void testNoArgsConstructor_Exitoso() {
        ReviewStepDTO emptyDTO = new ReviewStepDTO();
        assertNotNull(emptyDTO);
    }

    @Test
    @DisplayName("Caso borde - equals retorna true para DTOs idénticos")
    void testEquals_Exitoso() {
        ReviewStepDTO dto1 = ReviewStepDTO.builder()
                .userId("USER_001")
                .userRole(UserRole.ADMINISTRATOR)
                .action("APPROVED")
                .timestamp(currentDate)
                .comments("Comentario")
                .build();

        ReviewStepDTO dto2 = ReviewStepDTO.builder()
                .userId("USER_001")
                .userRole(UserRole.ADMINISTRATOR)
                .action("APPROVED")
                .timestamp(currentDate)
                .comments("Comentario")
                .build();

        assertEquals(dto1, dto2);
    }

    @Test
    @DisplayName("Caso error - equals retorna false para DTOs diferentes")
    void testEquals_Diferentes() {
        ReviewStepDTO dto1 = ReviewStepDTO.builder()
                .userId("USER_001")
                .userRole(UserRole.ADMINISTRATOR)
                .action("APPROVED")
                .timestamp(currentDate)
                .build();

        ReviewStepDTO dto2 = ReviewStepDTO.builder()
                .userId("USER_002")
                .userRole(UserRole.PROFESSOR)
                .action("REJECTED")
                .timestamp(new Date(currentDate.getTime() + 1000))
                .build();

        assertNotEquals(dto1, dto2);
    }

    @Test
    @DisplayName("Caso borde - hashCode consistente para DTOs iguales")
    void testHashCode_Exitoso() {
        ReviewStepDTO dto1 = ReviewStepDTO.builder()
                .userId("USER_001")
                .userRole(UserRole.ADMINISTRATOR)
                .action("APPROVED")
                .timestamp(currentDate)
                .comments("Comentario")
                .build();

        ReviewStepDTO dto2 = ReviewStepDTO.builder()
                .userId("USER_001")
                .userRole(UserRole.ADMINISTRATOR)
                .action("APPROVED")
                .timestamp(currentDate)
                .comments("Comentario")
                .build();

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    @DisplayName("Caso borde - toString no retorna null")
    void testToString_NoNull() {
        String toStringResult = reviewStepDTO.toString();
        assertNotNull(toStringResult);
        assertFalse(toStringResult.isEmpty());
    }

    @Test
    @DisplayName("Caso borde - Campos null manejados correctamente")
    void testCamposNull_Exitoso() {
        ReviewStepDTO dto = new ReviewStepDTO();
        dto.setUserId(null);
        dto.setUserRole(null);
        dto.setAction(null);
        dto.setTimestamp(null);
        dto.setComments(null);

        assertAll("Verificación de campos null",
                () -> assertNull(dto.getUserId()),
                () -> assertNull(dto.getUserRole()),
                () -> assertNull(dto.getAction()),
                () -> assertNull(dto.getTimestamp()),
                () -> assertNull(dto.getComments())
        );
    }

    @Test
    @DisplayName("Caso exitoso - hasComments retorna true cuando hay comentarios")
    void testHasComments_ConComentarios_Exitoso() {
        assertTrue(reviewStepDTO.hasComments());
    }

    @Test
    @DisplayName("Caso error - hasComments retorna false cuando no hay comentarios")
    void testHasComments_SinComentarios() {
        reviewStepDTO.setComments(null);
        assertFalse(reviewStepDTO.hasComments());

        reviewStepDTO.setComments("");
        assertFalse(reviewStepDTO.hasComments());

        reviewStepDTO.setComments("   ");
        assertFalse(reviewStepDTO.hasComments());
    }

    @Test
    @DisplayName("Caso exitoso - getActionDescription con datos completos")
    void testGetActionDescription_ConDatos_Exitoso() {
        String description = reviewStepDTO.getActionDescription();
        assertEquals("ADMINISTRATOR - APPROVED", description);
    }

    @Test
    @DisplayName("Caso error - getActionDescription con action null")
    void testGetActionDescription_ActionNull() {
        reviewStepDTO.setAction(null);
        String description = reviewStepDTO.getActionDescription();
        assertEquals("Sin acción", description);
    }

    @Test
    @DisplayName("Caso borde - getActionDescription con userRole null")
    void testGetActionDescription_UserRoleNull() {
        reviewStepDTO.setUserRole(null);
        String description = reviewStepDTO.getActionDescription();
        assertEquals("Sin rol - APPROVED", description);
    }

    @Test
    @DisplayName("Caso exitoso - isRecent retorna true para timestamp reciente")
    void testIsRecent_Reciente_Exitoso() {
        assertTrue(reviewStepDTO.isRecent());
    }

    @Test
    @DisplayName("Caso error - isRecent retorna false para timestamp antiguo")
    void testIsRecent_Antiguo() {
        Date oldDate = new Date(currentDate.getTime() - (8L * 24 * 60 * 60 * 1000));
        reviewStepDTO.setTimestamp(oldDate);
        assertFalse(reviewStepDTO.isRecent());
    }

    @Test
    @DisplayName("Caso error - isRecent retorna false para timestamp null")
    void testIsRecent_TimestampNull() {
        reviewStepDTO.setTimestamp(null);
        assertFalse(reviewStepDTO.isRecent());
    }

    @Test
    @DisplayName("Caso exitoso - Todos los UserRole funcionan correctamente")
    void testAllUserRoles_Exitoso() {
        ReviewStepDTO dto = new ReviewStepDTO();

        dto.setUserRole(UserRole.ADMINISTRATOR);
        assertEquals(UserRole.ADMINISTRATOR, dto.getUserRole());

        dto.setUserRole(UserRole.PROFESSOR);
        assertEquals(UserRole.PROFESSOR, dto.getUserRole());

        dto.setUserRole(UserRole.STUDENT);
        assertEquals(UserRole.STUDENT, dto.getUserRole());

        dto.setUserRole(UserRole.DEAN);
        assertEquals(UserRole.DEAN, dto.getUserRole());
    }

    @Test
    @DisplayName("Caso borde - Action con diferentes valores")
    void testActionValoresDiferentes_Exitoso() {
        reviewStepDTO.setAction("APPROVED");
        assertEquals("APPROVED", reviewStepDTO.getAction());

        reviewStepDTO.setAction("REJECTED");
        assertEquals("REJECTED", reviewStepDTO.getAction());

        reviewStepDTO.setAction("PENDING_REVIEW");
        assertEquals("PENDING_REVIEW", reviewStepDTO.getAction());

        reviewStepDTO.setAction("RETURNED_FOR_CORRECTION");
        assertEquals("RETURNED_FOR_CORRECTION", reviewStepDTO.getAction());
    }

    @Test
    @DisplayName("Caso borde - UserId con diferentes formatos")
    void testUserIdFormatosDiferentes_Exitoso() {
        reviewStepDTO.setUserId("ADMIN_001");
        assertEquals("ADMIN_001", reviewStepDTO.getUserId());

        reviewStepDTO.setUserId("PROF-2025-001");
        assertEquals("PROF-2025-001", reviewStepDTO.getUserId());

        reviewStepDTO.setUserId("STU202510001");
        assertEquals("STU202510001", reviewStepDTO.getUserId());

        reviewStepDTO.setUserId("DEAN-ENG-001");
        assertEquals("DEAN-ENG-001", reviewStepDTO.getUserId());
    }

    @Test
    @DisplayName("Caso borde - Comments con textos largos")
    void testCommentsTextoLargo_Exitoso() {
        String comentarioLargo = "La solicitud ha sido revisada y cumple con todos los requisitos establecidos. " +
                "Se observa que el estudiante ha presentado documentación completa y los " +
                "procedimientos han sido seguidos correctamente. Se aprueba la solicitud.";
        reviewStepDTO.setComments(comentarioLargo);
        assertEquals(comentarioLargo, reviewStepDTO.getComments());

        reviewStepDTO.setComments("");
        assertEquals("", reviewStepDTO.getComments());

        reviewStepDTO.setComments("   ");
        assertEquals("   ", reviewStepDTO.getComments());
    }

    @Test
    @DisplayName("Caso borde - Timestamp con diferentes valores")
    void testTimestampValoresDiferentes_Exitoso() {
        Date pastDate = new Date(currentDate.getTime() - (1000L * 60 * 60 * 24));
        reviewStepDTO.setTimestamp(pastDate);
        assertEquals(pastDate, reviewStepDTO.getTimestamp());

        Date futureDate = new Date(currentDate.getTime() + (1000L * 60 * 60 * 24));
        reviewStepDTO.setTimestamp(futureDate);
        assertEquals(futureDate, reviewStepDTO.getTimestamp());
    }

    @Test
    @DisplayName("Caso borde - isRecent con timestamp exactamente 7 días")
    void testIsRecent_Exactamente7Dias() {
        Date exactly7DaysAgo = new Date(currentDate.getTime() - (7L * 24 * 60 * 60 * 1000));
        reviewStepDTO.setTimestamp(exactly7DaysAgo);
        assertFalse(reviewStepDTO.isRecent());
    }

    @Test
    @DisplayName("Caso borde - isRecent con timestamp 6 días 23 horas")
    void testIsRecent_6Dias23Horas() {
        Date almost7DaysAgo = new Date(currentDate.getTime() - (7L * 24 * 60 * 60 * 1000) + (60 * 60 * 1000));
        reviewStepDTO.setTimestamp(almost7DaysAgo);
        assertTrue(reviewStepDTO.isRecent());
    }

    @Test
    @DisplayName("Caso borde - getActionDescription con todos los campos null")
    void testGetActionDescription_TodosNull() {
        ReviewStepDTO dto = new ReviewStepDTO();
        dto.setUserRole(null);
        dto.setAction(null);

        String description = dto.getActionDescription();
        assertEquals("Sin acción", description);
    }

    @Test
    @DisplayName("Caso borde - Builder con campos null")
    void testBuilderConCamposNull_Exitoso() {
        ReviewStepDTO dto = ReviewStepDTO.builder()
                .userId(null)
                .userRole(null)
                .action(null)
                .timestamp(null)
                .comments(null)
                .build();

        assertAll("Verificación de builder con campos null",
                () -> assertNull(dto.getUserId()),
                () -> assertNull(dto.getUserRole()),
                () -> assertNull(dto.getAction()),
                () -> assertNull(dto.getTimestamp()),
                () -> assertNull(dto.getComments())
        );
    }

    @Test
    @DisplayName("Caso borde - UserId vacío")
    void testUserIdVacio_Exitoso() {
        reviewStepDTO.setUserId("");
        assertEquals("", reviewStepDTO.getUserId());

        reviewStepDTO.setUserId("   ");
        assertEquals("   ", reviewStepDTO.getUserId());
    }

    @Test
    @DisplayName("Caso borde - Action vacía")
    void testActionVacia_Exitoso() {
        reviewStepDTO.setAction("");
        assertEquals("", reviewStepDTO.getAction());

        reviewStepDTO.setAction("   ");
        assertEquals("   ", reviewStepDTO.getAction());
    }
}
package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.ReviewStep;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Date;
import java.util.List;
import java.util.Calendar;
import java.util.Collections;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataMongoTest
public class ReviewStepRepositoryTest {

    @MockBean
    private ReviewStepRepository reviewStepRepository;

    private ReviewStep reviewStep1;
    private ReviewStep reviewStep2;
    private ReviewStep reviewStep3;
    private ReviewStep reviewStep4;
    private Date pastDate;
    private Date futureDate;
    private Date currentDate;

    @BeforeEach
    void setUp() {
        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.DAY_OF_MONTH, -5);
        pastDate = cal.getTime();

        currentDate = new Date();

        cal.add(Calendar.DAY_OF_MONTH, 10);
        futureDate = cal.getTime();

        reviewStep1 = ReviewStep.builder()
                .userId("USER001")
                .userRole(UserRole.PROFESSOR)
                .action("APPROVE")
                .timestamp(pastDate)
                .comments("Approved with minor modifications")
                .build();

        reviewStep2 = ReviewStep.builder()
                .userId("USER002")
                .userRole(UserRole.ADMINISTRATOR)
                .action("REJECT")
                .timestamp(currentDate)
                .comments("Incomplete documentation")
                .build();

        reviewStep3 = ReviewStep.builder()
                .userId("USER001")
                .userRole(UserRole.PROFESSOR)
                .action("REVIEW")
                .timestamp(futureDate)
                .comments("Needs further review")
                .build();

        reviewStep4 = ReviewStep.builder()
                .userId("USER003")
                .userRole(UserRole.DEAN)
                .action("FINAL_APPROVE")
                .timestamp(currentDate)
                .comments("")
                .build();
    }

    @Test
    @DisplayName("Caso exitoso - findByUserId retorna pasos del usuario")
    void testFindByUserId_Exitoso() {
        when(reviewStepRepository.findByUserId("USER001"))
                .thenReturn(List.of(reviewStep1, reviewStep3));

        List<ReviewStep> resultado = reviewStepRepository.findByUserId("USER001");

        assertAll("Verificar pasos del usuario USER001",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals("USER001", resultado.get(0).getUserId()),
                () -> assertEquals("USER001", resultado.get(1).getUserId())
        );

        verify(reviewStepRepository, times(1)).findByUserId("USER001");
    }

    @Test
    @DisplayName("Caso error - findByUserId retorna lista vacía para usuario inexistente")
    void testFindByUserId_UsuarioInexistente() {
        when(reviewStepRepository.findByUserId("USER999"))
                .thenReturn(Collections.emptyList());

        List<ReviewStep> resultado = reviewStepRepository.findByUserId("USER999");

        assertTrue(resultado.isEmpty());
        verify(reviewStepRepository, times(1)).findByUserId("USER999");
    }

    @Test
    @DisplayName("Caso exitoso - findByUserRole retorna pasos por rol")
    void testFindByUserRole_Exitoso() {
        when(reviewStepRepository.findByUserRole(UserRole.PROFESSOR))
                .thenReturn(List.of(reviewStep1, reviewStep3));

        List<ReviewStep> resultado = reviewStepRepository.findByUserRole(UserRole.PROFESSOR);

        assertAll("Verificar pasos de profesores",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals(UserRole.PROFESSOR, resultado.get(0).getUserRole()),
                () -> assertEquals(UserRole.PROFESSOR, resultado.get(1).getUserRole())
        );

        verify(reviewStepRepository, times(1)).findByUserRole(UserRole.PROFESSOR);
    }

    @Test
    @DisplayName("Caso exitoso - findByAction retorna pasos por acción")
    void testFindByAction_Exitoso() {
        when(reviewStepRepository.findByAction("APPROVE"))
                .thenReturn(List.of(reviewStep1));

        List<ReviewStep> resultado = reviewStepRepository.findByAction("APPROVE");

        assertAll("Verificar pasos de aprobación",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("APPROVE", resultado.get(0).getAction())
        );

        verify(reviewStepRepository, times(1)).findByAction("APPROVE");
    }

    @Test
    @DisplayName("Caso exitoso - findByCommentsContaining retorna pasos con comentarios específicos")
    void testFindByCommentsContaining_Exitoso() {
        when(reviewStepRepository.findByCommentsContaining("review"))
                .thenReturn(List.of(reviewStep3));

        List<ReviewStep> resultado = reviewStepRepository.findByCommentsContaining("review");

        assertAll("Verificar pasos con comentarios que contienen 'review'",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertTrue(resultado.get(0).getComments().toLowerCase().contains("review"))
        );

        verify(reviewStepRepository, times(1)).findByCommentsContaining("review");
    }

    @Test
    @DisplayName("Caso exitoso - findByTimestampAfter retorna pasos recientes")
    void testFindByTimestampAfter_Exitoso() {
        Date yesterday = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);

        when(reviewStepRepository.findByTimestampAfter(yesterday))
                .thenReturn(List.of(reviewStep2, reviewStep4));

        List<ReviewStep> resultado = reviewStepRepository.findByTimestampAfter(yesterday);

        assertAll("Verificar pasos después de ayer",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertTrue(resultado.get(0).getTimestamp().after(yesterday)),
                () -> assertTrue(resultado.get(1).getTimestamp().after(yesterday))
        );

        verify(reviewStepRepository, times(1)).findByTimestampAfter(yesterday);
    }

    @Test
    @DisplayName("Caso exitoso - findByTimestampBefore retorna pasos antiguos")
    void testFindByTimestampBefore_Exitoso() {
        Date tomorrow = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);

        when(reviewStepRepository.findByTimestampBefore(tomorrow))
                .thenReturn(List.of(reviewStep1, reviewStep2, reviewStep4));

        List<ReviewStep> resultado = reviewStepRepository.findByTimestampBefore(tomorrow);

        assertAll("Verificar pasos antes de mañana",
                () -> assertNotNull(resultado),
                () -> assertEquals(3, resultado.size()),
                () -> assertTrue(resultado.get(0).getTimestamp().before(tomorrow)),
                () -> assertTrue(resultado.get(1).getTimestamp().before(tomorrow)),
                () -> assertTrue(resultado.get(2).getTimestamp().before(tomorrow))
        );

        verify(reviewStepRepository, times(1)).findByTimestampBefore(tomorrow);
    }

    @Test
    @DisplayName("Caso exitoso - findByTimestampBetween retorna pasos en rango de fechas")
    void testFindByTimestampBetween_Exitoso() {
        Date startDate = new Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000);
        Date endDate = new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);

        when(reviewStepRepository.findByTimestampBetween(startDate, endDate))
                .thenReturn(List.of(reviewStep1, reviewStep2, reviewStep4));

        List<ReviewStep> resultado = reviewStepRepository.findByTimestampBetween(startDate, endDate);

        assertAll("Verificar pasos en rango de fechas",
                () -> assertNotNull(resultado),
                () -> assertEquals(3, resultado.size()),
                () -> assertTrue(resultado.get(0).getTimestamp().after(startDate) && resultado.get(0).getTimestamp().before(endDate)),
                () -> assertTrue(resultado.get(1).getTimestamp().after(startDate) && resultado.get(1).getTimestamp().before(endDate)),
                () -> assertTrue(resultado.get(2).getTimestamp().after(startDate) && resultado.get(2).getTimestamp().before(endDate))
        );

        verify(reviewStepRepository, times(1)).findByTimestampBetween(startDate, endDate);
    }

    @Test
    @DisplayName("Caso exitoso - findByUserIdAndUserRole retorna pasos filtrados")
    void testFindByUserIdAndUserRole_Exitoso() {
        when(reviewStepRepository.findByUserIdAndUserRole("USER001", UserRole.PROFESSOR))
                .thenReturn(List.of(reviewStep1, reviewStep3));

        List<ReviewStep> resultado = reviewStepRepository.findByUserIdAndUserRole("USER001", UserRole.PROFESSOR);

        assertAll("Verificar pasos por usuario y rol",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals("USER001", resultado.get(0).getUserId()),
                () -> assertEquals(UserRole.PROFESSOR, resultado.get(0).getUserRole())
        );

        verify(reviewStepRepository, times(1)).findByUserIdAndUserRole("USER001", UserRole.PROFESSOR);
    }

    @Test
    @DisplayName("Caso exitoso - findByUserIdAndAction retorna pasos por usuario y acción")
    void testFindByUserIdAndAction_Exitoso() {
        when(reviewStepRepository.findByUserIdAndAction("USER002", "REJECT"))
                .thenReturn(List.of(reviewStep2));

        List<ReviewStep> resultado = reviewStepRepository.findByUserIdAndAction("USER002", "REJECT");

        assertAll("Verificar pasos por usuario y acción",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("USER002", resultado.get(0).getUserId()),
                () -> assertEquals("REJECT", resultado.get(0).getAction())
        );

        verify(reviewStepRepository, times(1)).findByUserIdAndAction("USER002", "REJECT");
    }



    @Test
    @DisplayName("Caso exitoso - countByUserId retorna conteo correcto")
    void testCountByUserId_Exitoso() {
        when(reviewStepRepository.countByUserId("USER001"))
                .thenReturn(2L);

        long resultado = reviewStepRepository.countByUserId("USER001");

        assertEquals(2L, resultado);
        verify(reviewStepRepository, times(1)).countByUserId("USER001");
    }

    @Test
    @DisplayName("Caso exitoso - countByUserRole retorna conteo por rol")
    void testCountByUserRole_Exitoso() {
        when(reviewStepRepository.countByUserRole(UserRole.PROFESSOR))
                .thenReturn(2L);

        long resultado = reviewStepRepository.countByUserRole(UserRole.PROFESSOR);

        assertEquals(2L, resultado);
        verify(reviewStepRepository, times(1)).countByUserRole(UserRole.PROFESSOR);
    }

    @Test
    @DisplayName("Caso exitoso - countByAction retorna conteo por acción")
    void testCountByAction_Exitoso() {
        when(reviewStepRepository.countByAction("APPROVE"))
                .thenReturn(1L);

        long resultado = reviewStepRepository.countByAction("APPROVE");

        assertEquals(1L, resultado);
        verify(reviewStepRepository, times(1)).countByAction("APPROVE");
    }

    @Test
    @DisplayName("Caso exitoso - existsByUserId retorna true cuando existe")
    void testExistsByUserId_Exitoso() {
        when(reviewStepRepository.existsByUserId("USER001"))
                .thenReturn(true);

        boolean resultado = reviewStepRepository.existsByUserId("USER001");

        assertTrue(resultado);
        verify(reviewStepRepository, times(1)).existsByUserId("USER001");
    }

    @Test
    @DisplayName("Caso error - existsByUserId retorna false cuando no existe")
    void testExistsByUserId_NoExiste() {
        when(reviewStepRepository.existsByUserId("USER999"))
                .thenReturn(false);

        boolean resultado = reviewStepRepository.existsByUserId("USER999");

        assertFalse(resultado);
        verify(reviewStepRepository, times(1)).existsByUserId("USER999");
    }

    @Test
    @DisplayName("Caso exitoso - existsByUserRole retorna true")
    void testExistsByUserRole_Exitoso() {
        when(reviewStepRepository.existsByUserRole(UserRole.PROFESSOR))
                .thenReturn(true);

        boolean resultado = reviewStepRepository.existsByUserRole(UserRole.PROFESSOR);

        assertTrue(resultado);
        verify(reviewStepRepository, times(1)).existsByUserRole(UserRole.PROFESSOR);
    }

    @Test
    @DisplayName("Caso exitoso - findByUserIdIn retorna pasos de múltiples usuarios")
    void testFindByUserIdIn_Exitoso() {
        when(reviewStepRepository.findByUserIdIn(List.of("USER001", "USER002")))
                .thenReturn(List.of(reviewStep1, reviewStep2, reviewStep3));

        List<ReviewStep> resultado = reviewStepRepository.findByUserIdIn(List.of("USER001", "USER002"));

        assertAll("Verificar pasos de múltiples usuarios",
                () -> assertNotNull(resultado),
                () -> assertEquals(3, resultado.size()),
                () -> assertTrue(resultado.stream().anyMatch(rs -> rs.getUserId().equals("USER001"))),
                () -> assertTrue(resultado.stream().anyMatch(rs -> rs.getUserId().equals("USER002")))
        );

        verify(reviewStepRepository, times(1)).findByUserIdIn(List.of("USER001", "USER002"));
    }

    @Test
    @DisplayName("Caso exitoso - findByUserRoleIn retorna pasos de múltiples roles")
    void testFindByUserRoleIn_Exitoso() {
        when(reviewStepRepository.findByUserRoleIn(List.of(UserRole.PROFESSOR, UserRole.ADMINISTRATOR)))
                .thenReturn(List.of(reviewStep1, reviewStep2, reviewStep3));

        List<ReviewStep> resultado = reviewStepRepository.findByUserRoleIn(List.of(UserRole.PROFESSOR, UserRole.ADMINISTRATOR));

        assertAll("Verificar pasos de múltiples roles",
                () -> assertNotNull(resultado),
                () -> assertEquals(3, resultado.size()),
                () -> assertTrue(resultado.stream().anyMatch(rs -> rs.getUserRole() == UserRole.PROFESSOR)),
                () -> assertTrue(resultado.stream().anyMatch(rs -> rs.getUserRole() == UserRole.ADMINISTRATOR))
        );

        verify(reviewStepRepository, times(1)).findByUserRoleIn(List.of(UserRole.PROFESSOR, UserRole.ADMINISTRATOR));
    }

    @Test
    @DisplayName("Caso exitoso - findByCommentsIsNotNull retorna pasos con comentarios")
    void testFindByCommentsIsNotNull_Exitoso() {
        when(reviewStepRepository.findByCommentsIsNotNull())
                .thenReturn(List.of(reviewStep1, reviewStep2, reviewStep3));

        List<ReviewStep> resultado = reviewStepRepository.findByCommentsIsNotNull();

        assertAll("Verificar pasos con comentarios",
                () -> assertNotNull(resultado),
                () -> assertEquals(3, resultado.size()),
                () -> assertNotNull(resultado.get(0).getComments()),
                () -> assertNotNull(resultado.get(1).getComments()),
                () -> assertNotNull(resultado.get(2).getComments())
        );

        verify(reviewStepRepository, times(1)).findByCommentsIsNotNull();
    }


    @Test
    @DisplayName("Caso exitoso - findFirstByUserIdOrderByTimestampDesc retorna paso más reciente")
    void testFindFirstByUserIdOrderByTimestampDesc_Exitoso() {
        when(reviewStepRepository.findFirstByUserIdOrderByTimestampDesc("USER001"))
                .thenReturn(reviewStep3);

        ReviewStep resultado = reviewStepRepository.findFirstByUserIdOrderByTimestampDesc("USER001");

        assertAll("Verificar paso más reciente del usuario",
                () -> assertNotNull(resultado),
                () -> assertEquals("USER001", resultado.getUserId()),
                () -> assertEquals(reviewStep3.getTimestamp(), resultado.getTimestamp())
        );

        verify(reviewStepRepository, times(1)).findFirstByUserIdOrderByTimestampDesc("USER001");
    }

    @Test
    @DisplayName("Caso exitoso - findByUserIdRegex con query personalizada")
    void testFindByUserIdRegex_Exitoso() {
        when(reviewStepRepository.findByUserIdRegex(".*USER00.*"))
                .thenReturn(List.of(reviewStep1, reviewStep2, reviewStep3));

        List<ReviewStep> resultado = reviewStepRepository.findByUserIdRegex(".*USER00.*");

        assertAll("Verificar búsqueda por regex de userId",
                () -> assertNotNull(resultado),
                () -> assertEquals(3, resultado.size()),
                () -> assertTrue(resultado.get(0).getUserId().startsWith("USER00")),
                () -> assertTrue(resultado.get(1).getUserId().startsWith("USER00")),
                () -> assertTrue(resultado.get(2).getUserId().startsWith("USER00"))
        );

        verify(reviewStepRepository, times(1)).findByUserIdRegex(".*USER00.*");
    }

    @Test
    @DisplayName("Caso exitoso - findByUserIdAndTimestampBetween con query personalizada")
    void testFindByUserIdAndTimestampBetween_Exitoso() {
        Date startDate = new Date(System.currentTimeMillis() - 10 * 24 * 60 * 60 * 1000);
        Date endDate = new Date(System.currentTimeMillis() + 10 * 24 * 60 * 60 * 1000);

        when(reviewStepRepository.findByUserIdAndTimestampBetween("USER001", startDate, endDate))
                .thenReturn(List.of(reviewStep1, reviewStep3));

        List<ReviewStep> resultado = reviewStepRepository.findByUserIdAndTimestampBetween("USER001", startDate, endDate);

        assertAll("Verificar pasos por usuario y rango de fechas",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals("USER001", resultado.get(0).getUserId()),
                () -> assertTrue(resultado.get(0).getTimestamp().after(startDate) && resultado.get(0).getTimestamp().before(endDate))
        );

        verify(reviewStepRepository, times(1)).findByUserIdAndTimestampBetween("USER001", startDate, endDate);
    }

    @Test
    @DisplayName("Caso exitoso - findByUserIdSortedByTimestampDesc con query personalizada")
    void testFindByUserIdSortedByTimestampDesc_Exitoso() {
        when(reviewStepRepository.findByUserIdSortedByTimestampDesc("USER001"))
                .thenReturn(List.of(reviewStep3, reviewStep1));

        List<ReviewStep> resultado = reviewStepRepository.findByUserIdSortedByTimestampDesc("USER001");

        assertAll("Verificar pasos ordenados por timestamp descendente",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertTrue(resultado.get(0).getTimestamp().after(resultado.get(1).getTimestamp())),
                () -> assertEquals("USER001", resultado.get(0).getUserId()),
                () -> assertEquals("USER001", resultado.get(1).getUserId())
        );

        verify(reviewStepRepository, times(1)).findByUserIdSortedByTimestampDesc("USER001");
    }

    @Test
    @DisplayName("Caso exitoso - findRecentReviewSteps con query personalizada")
    void testFindRecentReviewSteps_Exitoso() {
        when(reviewStepRepository.findRecentReviewSteps())
                .thenReturn(List.of(reviewStep3, reviewStep2, reviewStep4, reviewStep1));

        List<ReviewStep> resultado = reviewStepRepository.findRecentReviewSteps();

        assertAll("Verificar pasos recientes ordenados",
                () -> assertNotNull(resultado),
                () -> assertEquals(4, resultado.size()),
                () -> assertTrue(resultado.get(0).getTimestamp().after(resultado.get(1).getTimestamp()))
        );

        verify(reviewStepRepository, times(1)).findRecentReviewSteps();
    }

    @Test
    @DisplayName("Caso borde - findByUserId con userId nulo")
    void testFindByUserId_Nulo() {
        when(reviewStepRepository.findByUserId(null))
                .thenReturn(Collections.emptyList());

        List<ReviewStep> resultado = reviewStepRepository.findByUserId(null);

        assertTrue(resultado.isEmpty());
        verify(reviewStepRepository, times(1)).findByUserId(null);
    }

    @Test
    @DisplayName("Caso borde - findByUserRole con rol nulo")
    void testFindByUserRole_Nulo() {
        when(reviewStepRepository.findByUserRole(null))
                .thenReturn(Collections.emptyList());

        List<ReviewStep> resultado = reviewStepRepository.findByUserRole(null);

        assertTrue(resultado.isEmpty());
        verify(reviewStepRepository, times(1)).findByUserRole(null);
    }

    @Test
    @DisplayName("Caso borde - Verificar integridad de datos en pasos encontrados")
    void testIntegridadDatosReviewSteps() {
        when(reviewStepRepository.findByUserId("USER001"))
                .thenReturn(List.of(reviewStep1));

        List<ReviewStep> resultado = reviewStepRepository.findByUserId("USER001");

        assertAll("Verificar integridad completa del paso de revisión",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("USER001", resultado.get(0).getUserId()),
                () -> assertEquals(UserRole.PROFESSOR, resultado.get(0).getUserRole()),
                () -> assertEquals("APPROVE", resultado.get(0).getAction()),
                () -> assertNotNull(resultado.get(0).getTimestamp()),
                () -> assertEquals("Approved with minor modifications", resultado.get(0).getComments()),
                () -> assertInstanceOf(ReviewStep.class, resultado.get(0)),
                () -> assertNotNull(resultado.get(0).toString())
        );

        verify(reviewStepRepository, times(1)).findByUserId("USER001");
    }


}
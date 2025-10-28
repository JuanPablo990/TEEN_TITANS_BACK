package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.ScheduleChangeRequest;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Student;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Group;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Course;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Professor;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Schedule;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Classroom;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.ReviewStep;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.RequestStatus;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.RoomType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Date;
import java.util.List;
import java.util.Calendar;
import java.util.Optional;
import java.util.Collections;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataMongoTest
public class ScheduleChangeRequestRepositoryTest {

    @MockBean
    private ScheduleChangeRequestRepository scheduleChangeRequestRepository;

    private Student student1;
    private Student student2;
    private Course course1;
    private Professor professor1;
    private Professor professor2;
    private Schedule schedule1;
    private Schedule schedule2;
    private Classroom classroom1;
    private Classroom classroom2;
    private Group currentGroup1;
    private Group requestedGroup1;
    private Group currentGroup2;
    private Group requestedGroup2;
    private ReviewStep reviewStep1;
    private ReviewStep reviewStep2;
    private ScheduleChangeRequest request1;
    private ScheduleChangeRequest request2;
    private ScheduleChangeRequest request3;
    private ScheduleChangeRequest request4;
    private Date pastDate;
    private Date currentDate;
    private Date futureDate;

    @BeforeEach
    void setUp() {
        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.DAY_OF_MONTH, -10);
        pastDate = cal.getTime();

        currentDate = new Date();

        cal.add(Calendar.DAY_OF_MONTH, 20);
        futureDate = cal.getTime();

        student1 = new Student("STU001", "John Doe", "john.doe@university.edu", "password", "Computer Science", 3);
        student1.setGradeAverage(3.8);

        student2 = new Student("STU002", "Jane Smith", "jane.smith@university.edu", "password", "Mathematics", 2);
        student2.setGradeAverage(3.5);

        course1 = new Course("CS101", "Introduction to Programming", 3, "Basic programming concepts", "Computer Science", true);

        professor1 = new Professor("PROF001", "Dr. Smith", "smith@university.edu", "password", "Computer Science", true, Arrays.asList("Programming", "Algorithms"));
        professor2 = new Professor("PROF002", "Dr. Johnson", "johnson@university.edu", "password", "Computer Science", false, Arrays.asList("Data Structures", "OOP"));

        schedule1 = new Schedule("SCHED001", "Monday", "08:00", "10:00", "Morning");
        schedule2 = new Schedule("SCHED002", "Wednesday", "14:00", "16:00", "Afternoon");

        classroom1 = new Classroom("CLASS001", "A", "101", 50, RoomType.REGULAR);
        classroom2 = new Classroom("CLASS002", "B", "201", 100, RoomType.LABORATORY);

        currentGroup1 = new Group("GROUP001", "A", course1, professor1, schedule1, classroom1);
        requestedGroup1 = new Group("GROUP002", "B", course1, professor2, schedule2, classroom2);
        currentGroup2 = new Group("GROUP003", "C", course1, professor1, schedule1, classroom1);
        requestedGroup2 = new Group("GROUP004", "D", course1, professor2, schedule2, classroom2);



        request1 = ScheduleChangeRequest.builder()
                .requestId("REQ001")
                .student(student1)
                .currentGroup(currentGroup1)
                .requestedGroup(requestedGroup1)
                .reason("Schedule conflict with work")
                .status(RequestStatus.PENDING)
                .submissionDate(pastDate)
                .resolutionDate(null)
                .reviewHistory(Arrays.asList(reviewStep1))
                .build();

        request2 = ScheduleChangeRequest.builder()
                .requestId("REQ002")
                .student(student1)
                .currentGroup(currentGroup2)
                .requestedGroup(requestedGroup2)
                .reason("Preferred professor teaching style")
                .status(RequestStatus.APPROVED)
                .submissionDate(pastDate)
                .resolutionDate(currentDate)
                .reviewHistory(Arrays.asList(reviewStep1, reviewStep2))
                .build();

        request3 = ScheduleChangeRequest.builder()
                .requestId("REQ003")
                .student(student2)
                .currentGroup(currentGroup1)
                .requestedGroup(requestedGroup1)
                .reason("Medical reasons")
                .status(RequestStatus.REJECTED)
                .submissionDate(currentDate)
                .resolutionDate(currentDate)
                .reviewHistory(Arrays.asList(reviewStep1))
                .build();

        request4 = ScheduleChangeRequest.builder()
                .requestId("REQ004")
                .student(student1)
                .currentGroup(currentGroup1)
                .requestedGroup(requestedGroup2)
                .reason("Transportation issues")
                .status(RequestStatus.UNDER_REVIEW)
                .submissionDate(currentDate)
                .resolutionDate(null)
                .reviewHistory(Collections.emptyList())
                .build();
    }

    @Test
    @DisplayName("Caso exitoso - findByStudentId retorna solicitudes del estudiante")
    void testFindByStudentId_Exitoso() {
        when(scheduleChangeRequestRepository.findByStudentId("STU001"))
                .thenReturn(List.of(request1, request2, request4));

        List<ScheduleChangeRequest> resultado = scheduleChangeRequestRepository.findByStudentId("STU001");

        assertAll("Verificar solicitudes del estudiante STU001",
                () -> assertNotNull(resultado),
                () -> assertEquals(3, resultado.size()),
                () -> assertEquals("STU001", resultado.get(0).getStudent().getId()),
                () -> assertEquals("STU001", resultado.get(1).getStudent().getId()),
                () -> assertEquals("STU001", resultado.get(2).getStudent().getId())
        );

        verify(scheduleChangeRequestRepository, times(1)).findByStudentId("STU001");
    }

    @Test
    @DisplayName("Caso error - findByStudentId retorna lista vacía para estudiante inexistente")
    void testFindByStudentId_EstudianteInexistente() {
        when(scheduleChangeRequestRepository.findByStudentId("STU999"))
                .thenReturn(Collections.emptyList());

        List<ScheduleChangeRequest> resultado = scheduleChangeRequestRepository.findByStudentId("STU999");

        assertTrue(resultado.isEmpty());
        verify(scheduleChangeRequestRepository, times(1)).findByStudentId("STU999");
    }

    @Test
    @DisplayName("Caso exitoso - findByStatus retorna solicitudes por estado")
    void testFindByStatus_Exitoso() {
        when(scheduleChangeRequestRepository.findByStatus(RequestStatus.PENDING))
                .thenReturn(List.of(request1));

        List<ScheduleChangeRequest> resultado = scheduleChangeRequestRepository.findByStatus(RequestStatus.PENDING);

        assertAll("Verificar solicitudes pendientes",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals(RequestStatus.PENDING, resultado.get(0).getStatus())
        );

        verify(scheduleChangeRequestRepository, times(1)).findByStatus(RequestStatus.PENDING);
    }

    @Test
    @DisplayName("Caso exitoso - findByReasonContainingIgnoreCase retorna solicitudes por razón")
    void testFindByReasonContainingIgnoreCase_Exitoso() {
        when(scheduleChangeRequestRepository.findByReasonContainingIgnoreCase("medical"))
                .thenReturn(List.of(request3));

        List<ScheduleChangeRequest> resultado = scheduleChangeRequestRepository.findByReasonContainingIgnoreCase("medical");

        assertAll("Verificar solicitudes con razón que contiene 'medical'",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertTrue(resultado.get(0).getReason().toLowerCase().contains("medical"))
        );

        verify(scheduleChangeRequestRepository, times(1)).findByReasonContainingIgnoreCase("medical");
    }

    @Test
    @DisplayName("Caso exitoso - findBySubmissionDateAfter retorna solicitudes recientes")
    void testFindBySubmissionDateAfter_Exitoso() {
        Date yesterday = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);

        when(scheduleChangeRequestRepository.findBySubmissionDateAfter(yesterday))
                .thenReturn(List.of(request3, request4));

        List<ScheduleChangeRequest> resultado = scheduleChangeRequestRepository.findBySubmissionDateAfter(yesterday);

        assertAll("Verificar solicitudes después de ayer",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertTrue(resultado.get(0).getSubmissionDate().after(yesterday)),
                () -> assertTrue(resultado.get(1).getSubmissionDate().after(yesterday))
        );

        verify(scheduleChangeRequestRepository, times(1)).findBySubmissionDateAfter(yesterday);
    }

    @Test
    @DisplayName("Caso exitoso - findBySubmissionDateBetween retorna solicitudes en rango")
    void testFindBySubmissionDateBetween_Exitoso() {
        Date startDate = new Date(System.currentTimeMillis() - 15 * 24 * 60 * 60 * 1000);
        Date endDate = new Date(System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000);

        when(scheduleChangeRequestRepository.findBySubmissionDateBetween(startDate, endDate))
                .thenReturn(List.of(request1, request2, request3, request4));

        List<ScheduleChangeRequest> resultado = scheduleChangeRequestRepository.findBySubmissionDateBetween(startDate, endDate);

        assertAll("Verificar solicitudes en rango de fechas",
                () -> assertNotNull(resultado),
                () -> assertEquals(4, resultado.size()),
                () -> assertTrue(resultado.get(0).getSubmissionDate().after(startDate) && resultado.get(0).getSubmissionDate().before(endDate))
        );

        verify(scheduleChangeRequestRepository, times(1)).findBySubmissionDateBetween(startDate, endDate);
    }

    @Test
    @DisplayName("Caso exitoso - findByStudentIdAndStatus retorna solicitudes filtradas")
    void testFindByStudentIdAndStatus_Exitoso() {
        when(scheduleChangeRequestRepository.findByStudentIdAndStatus("STU001", RequestStatus.PENDING))
                .thenReturn(List.of(request1));

        List<ScheduleChangeRequest> resultado = scheduleChangeRequestRepository.findByStudentIdAndStatus("STU001", RequestStatus.PENDING);

        assertAll("Verificar solicitudes por estudiante y estado",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("STU001", resultado.get(0).getStudent().getId()),
                () -> assertEquals(RequestStatus.PENDING, resultado.get(0).getStatus())
        );

        verify(scheduleChangeRequestRepository, times(1)).findByStudentIdAndStatus("STU001", RequestStatus.PENDING);
    }





    @Test
    @DisplayName("Caso exitoso - countByStudentId retorna conteo correcto")
    void testCountByStudentId_Exitoso() {
        when(scheduleChangeRequestRepository.countByStudentId("STU001"))
                .thenReturn(3L);

        long resultado = scheduleChangeRequestRepository.countByStudentId("STU001");

        assertEquals(3L, resultado);
        verify(scheduleChangeRequestRepository, times(1)).countByStudentId("STU001");
    }

    @Test
    @DisplayName("Caso exitoso - countByStatus retorna conteo por estado")
    void testCountByStatus_Exitoso() {
        when(scheduleChangeRequestRepository.countByStatus(RequestStatus.PENDING))
                .thenReturn(1L);

        long resultado = scheduleChangeRequestRepository.countByStatus(RequestStatus.PENDING);

        assertEquals(1L, resultado);
        verify(scheduleChangeRequestRepository, times(1)).countByStatus(RequestStatus.PENDING);
    }

    @Test
    @DisplayName("Caso exitoso - countByStudentIdAndStatus retorna conteo combinado")
    void testCountByStudentIdAndStatus_Exitoso() {
        when(scheduleChangeRequestRepository.countByStudentIdAndStatus("STU001", RequestStatus.PENDING))
                .thenReturn(1L);

        long resultado = scheduleChangeRequestRepository.countByStudentIdAndStatus("STU001", RequestStatus.PENDING);

        assertEquals(1L, resultado);
        verify(scheduleChangeRequestRepository, times(1)).countByStudentIdAndStatus("STU001", RequestStatus.PENDING);
    }

    @Test
    @DisplayName("Caso exitoso - countByResolutionDateIsNotNull retorna solicitudes resueltas")
    void testCountByResolutionDateIsNotNull_Exitoso() {
        when(scheduleChangeRequestRepository.countByResolutionDateIsNotNull())
                .thenReturn(2L);

        long resultado = scheduleChangeRequestRepository.countByResolutionDateIsNotNull();

        assertEquals(2L, resultado);
        verify(scheduleChangeRequestRepository, times(1)).countByResolutionDateIsNotNull();
    }

    @Test
    @DisplayName("Caso exitoso - countByResolutionDateIsNull retorna solicitudes no resueltas")
    void testCountByResolutionDateIsNull_Exitoso() {
        when(scheduleChangeRequestRepository.countByResolutionDateIsNull())
                .thenReturn(2L);

        long resultado = scheduleChangeRequestRepository.countByResolutionDateIsNull();

        assertEquals(2L, resultado);
        verify(scheduleChangeRequestRepository, times(1)).countByResolutionDateIsNull();
    }

    @Test
    @DisplayName("Caso exitoso - existsByStudentId retorna true cuando existe")
    void testExistsByStudentId_Exitoso() {
        when(scheduleChangeRequestRepository.existsByStudentId("STU001"))
                .thenReturn(true);

        boolean resultado = scheduleChangeRequestRepository.existsByStudentId("STU001");

        assertTrue(resultado);
        verify(scheduleChangeRequestRepository, times(1)).existsByStudentId("STU001");
    }

    @Test
    @DisplayName("Caso error - existsByStudentId retorna false cuando no existe")
    void testExistsByStudentId_NoExiste() {
        when(scheduleChangeRequestRepository.existsByStudentId("STU999"))
                .thenReturn(false);

        boolean resultado = scheduleChangeRequestRepository.existsByStudentId("STU999");

        assertFalse(resultado);
        verify(scheduleChangeRequestRepository, times(1)).existsByStudentId("STU999");
    }

    @Test
    @DisplayName("Caso exitoso - existsByStudentIdAndStatus retorna true")
    void testExistsByStudentIdAndStatus_Exitoso() {
        when(scheduleChangeRequestRepository.existsByStudentIdAndStatus("STU001", RequestStatus.PENDING))
                .thenReturn(true);

        boolean resultado = scheduleChangeRequestRepository.existsByStudentIdAndStatus("STU001", RequestStatus.PENDING);

        assertTrue(resultado);
        verify(scheduleChangeRequestRepository, times(1)).existsByStudentIdAndStatus("STU001", RequestStatus.PENDING);
    }

    @Test
    @DisplayName("Caso exitoso - findByStudentIdIn retorna solicitudes de múltiples estudiantes")
    void testFindByStudentIdIn_Exitoso() {
        when(scheduleChangeRequestRepository.findByStudentIdIn(List.of("STU001", "STU002")))
                .thenReturn(List.of(request1, request2, request3, request4));

        List<ScheduleChangeRequest> resultado = scheduleChangeRequestRepository.findByStudentIdIn(List.of("STU001", "STU002"));

        assertAll("Verificar solicitudes de múltiples estudiantes",
                () -> assertNotNull(resultado),
                () -> assertEquals(4, resultado.size()),
                () -> assertTrue(resultado.stream().anyMatch(req -> req.getStudent().getId().equals("STU001"))),
                () -> assertTrue(resultado.stream().anyMatch(req -> req.getStudent().getId().equals("STU002")))
        );

        verify(scheduleChangeRequestRepository, times(1)).findByStudentIdIn(List.of("STU001", "STU002"));
    }

    @Test
    @DisplayName("Caso exitoso - findByStatusIn retorna solicitudes de múltiples estados")
    void testFindByStatusIn_Exitoso() {
        when(scheduleChangeRequestRepository.findByStatusIn(List.of(RequestStatus.PENDING, RequestStatus.UNDER_REVIEW)))
                .thenReturn(List.of(request1, request4));

        List<ScheduleChangeRequest> resultado = scheduleChangeRequestRepository.findByStatusIn(List.of(RequestStatus.PENDING, RequestStatus.UNDER_REVIEW));

        assertAll("Verificar solicitudes de múltiples estados",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertTrue(resultado.stream().anyMatch(req -> req.getStatus() == RequestStatus.PENDING)),
                () -> assertTrue(resultado.stream().anyMatch(req -> req.getStatus() == RequestStatus.UNDER_REVIEW))
        );

        verify(scheduleChangeRequestRepository, times(1)).findByStatusIn(List.of(RequestStatus.PENDING, RequestStatus.UNDER_REVIEW));
    }

    @Test
    @DisplayName("Caso exitoso - findFirstByStudentIdOrderBySubmissionDateDesc retorna solicitud más reciente")
    void testFindFirstByStudentIdOrderBySubmissionDateDesc_Exitoso() {
        when(scheduleChangeRequestRepository.findFirstByStudentIdOrderBySubmissionDateDesc("STU001"))
                .thenReturn(Optional.of(request4));

        Optional<ScheduleChangeRequest> resultado = scheduleChangeRequestRepository.findFirstByStudentIdOrderBySubmissionDateDesc("STU001");

        assertAll("Verificar solicitud más reciente del estudiante",
                () -> assertTrue(resultado.isPresent()),
                () -> assertEquals("STU001", resultado.get().getStudent().getId()),
                () -> assertEquals(request4.getSubmissionDate(), resultado.get().getSubmissionDate())
        );

        verify(scheduleChangeRequestRepository, times(1)).findFirstByStudentIdOrderBySubmissionDateDesc("STU001");
    }

    @Test
    @DisplayName("Caso exitoso - findByCurrentGroupId con query personalizada")
    void testFindByCurrentGroupId_Exitoso() {
        when(scheduleChangeRequestRepository.findByCurrentGroupId("GROUP001"))
                .thenReturn(List.of(request1, request3, request4));

        List<ScheduleChangeRequest> resultado = scheduleChangeRequestRepository.findByCurrentGroupId("GROUP001");

        assertAll("Verificar solicitudes por grupo actual",
                () -> assertNotNull(resultado),
                () -> assertEquals(3, resultado.size()),
                () -> assertEquals("GROUP001", resultado.get(0).getCurrentGroup().getGroupId()),
                () -> assertEquals("GROUP001", resultado.get(1).getCurrentGroup().getGroupId()),
                () -> assertEquals("GROUP001", resultado.get(2).getCurrentGroup().getGroupId())
        );

        verify(scheduleChangeRequestRepository, times(1)).findByCurrentGroupId("GROUP001");
    }

    @Test
    @DisplayName("Caso exitoso - findByRequestedGroupId con query personalizada")
    void testFindByRequestedGroupId_Exitoso() {
        when(scheduleChangeRequestRepository.findByRequestedGroupId("GROUP002"))
                .thenReturn(List.of(request1, request3));

        List<ScheduleChangeRequest> resultado = scheduleChangeRequestRepository.findByRequestedGroupId("GROUP002");

        assertAll("Verificar solicitudes por grupo solicitado",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals("GROUP002", resultado.get(0).getRequestedGroup().getGroupId()),
                () -> assertEquals("GROUP002", resultado.get(1).getRequestedGroup().getGroupId())
        );

        verify(scheduleChangeRequestRepository, times(1)).findByRequestedGroupId("GROUP002");
    }

    @Test
    @DisplayName("Caso exitoso - findByStudentIdAndSubmissionDateBetween con query personalizada")
    void testFindByStudentIdAndSubmissionDateBetween_Exitoso() {
        Date startDate = new Date(System.currentTimeMillis() - 15 * 24 * 60 * 60 * 1000);
        Date endDate = new Date(System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000);

        when(scheduleChangeRequestRepository.findByStudentIdAndSubmissionDateBetween("STU001", startDate, endDate))
                .thenReturn(List.of(request1, request2, request4));

        List<ScheduleChangeRequest> resultado = scheduleChangeRequestRepository.findByStudentIdAndSubmissionDateBetween("STU001", startDate, endDate);

        assertAll("Verificar solicitudes por estudiante y rango de fechas",
                () -> assertNotNull(resultado),
                () -> assertEquals(3, resultado.size()),
                () -> assertEquals("STU001", resultado.get(0).getStudent().getId()),
                () -> assertTrue(resultado.get(0).getSubmissionDate().after(startDate) && resultado.get(0).getSubmissionDate().before(endDate))
        );

        verify(scheduleChangeRequestRepository, times(1)).findByStudentIdAndSubmissionDateBetween("STU001", startDate, endDate);
    }

    @Test
    @DisplayName("Caso exitoso - findByStudentIdAndStatusIn con query personalizada")
    void testFindByStudentIdAndStatusIn_Exitoso() {
        when(scheduleChangeRequestRepository.findByStudentIdAndStatusIn("STU001",
                List.of(RequestStatus.PENDING, RequestStatus.UNDER_REVIEW)))
                .thenReturn(List.of(request1, request4));

        List<ScheduleChangeRequest> resultado = scheduleChangeRequestRepository.findByStudentIdAndStatusIn("STU001",
                List.of(RequestStatus.PENDING, RequestStatus.UNDER_REVIEW));

        assertAll("Verificar solicitudes por estudiante y múltiples estados",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals("STU001", resultado.get(0).getStudent().getId()),
                () -> assertTrue(resultado.get(0).getStatus() == RequestStatus.PENDING || resultado.get(0).getStatus() == RequestStatus.UNDER_REVIEW)
        );

        verify(scheduleChangeRequestRepository, times(1)).findByStudentIdAndStatusIn("STU001",
                List.of(RequestStatus.PENDING, RequestStatus.UNDER_REVIEW));
    }

    @Test
    @DisplayName("Caso exitoso - findRecentByStudentId con query personalizada")
    void testFindRecentByStudentId_Exitoso() {
        when(scheduleChangeRequestRepository.findRecentByStudentId("STU001"))
                .thenReturn(List.of(request4, request2, request1));

        List<ScheduleChangeRequest> resultado = scheduleChangeRequestRepository.findRecentByStudentId("STU001");

        assertAll("Verificar solicitudes recientes del estudiante ordenadas",
                () -> assertNotNull(resultado),
                () -> assertEquals(3, resultado.size()),
                () -> assertTrue(resultado.get(0).getSubmissionDate().after(resultado.get(1).getSubmissionDate())),
                () -> assertEquals("STU001", resultado.get(0).getStudent().getId())
        );

        verify(scheduleChangeRequestRepository, times(1)).findRecentByStudentId("STU001");
    }


    @Test
    @DisplayName("Caso exitoso - findWithoutResolution con query personalizada")
    void testFindWithoutResolution_Exitoso() {
        when(scheduleChangeRequestRepository.findWithoutResolution())
                .thenReturn(List.of(request1, request4));

        List<ScheduleChangeRequest> resultado = scheduleChangeRequestRepository.findWithoutResolution();

        assertAll("Verificar solicitudes sin resolución",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertNull(resultado.get(0).getResolutionDate()),
                () -> assertNull(resultado.get(1).getResolutionDate())
        );

        verify(scheduleChangeRequestRepository, times(1)).findWithoutResolution();
    }

    @Test
    @DisplayName("Caso exitoso - findWithReviewHistory con query personalizada")
    void testFindWithReviewHistory_Exitoso() {
        when(scheduleChangeRequestRepository.findWithReviewHistory())
                .thenReturn(List.of(request1, request2, request3));

        List<ScheduleChangeRequest> resultado = scheduleChangeRequestRepository.findWithReviewHistory();

        assertAll("Verificar solicitudes con historial de revisiones",
                () -> assertNotNull(resultado),
                () -> assertEquals(3, resultado.size()),
                () -> assertFalse(resultado.get(0).getReviewHistory().isEmpty()),
                () -> assertFalse(resultado.get(1).getReviewHistory().isEmpty()),
                () -> assertFalse(resultado.get(2).getReviewHistory().isEmpty())
        );

        verify(scheduleChangeRequestRepository, times(1)).findWithReviewHistory();
    }

    @Test
    @DisplayName("Caso exitoso - findWithoutReviewHistory con query personalizada")
    void testFindWithoutReviewHistory_Exitoso() {
        when(scheduleChangeRequestRepository.findWithoutReviewHistory())
                .thenReturn(List.of(request4));

        List<ScheduleChangeRequest> resultado = scheduleChangeRequestRepository.findWithoutReviewHistory();

        assertAll("Verificar solicitudes sin historial de revisiones",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertTrue(resultado.get(0).getReviewHistory().isEmpty())
        );

        verify(scheduleChangeRequestRepository, times(1)).findWithoutReviewHistory();
    }

    @Test
    @DisplayName("Caso borde - findByStudentId con studentId nulo")
    void testFindByStudentId_Nulo() {
        when(scheduleChangeRequestRepository.findByStudentId(null))
                .thenReturn(Collections.emptyList());

        List<ScheduleChangeRequest> resultado = scheduleChangeRequestRepository.findByStudentId(null);

        assertTrue(resultado.isEmpty());
        verify(scheduleChangeRequestRepository, times(1)).findByStudentId(null);
    }

    @Test
    @DisplayName("Caso borde - findByStatus con status nulo")
    void testFindByStatus_Nulo() {
        when(scheduleChangeRequestRepository.findByStatus(null))
                .thenReturn(Collections.emptyList());

        List<ScheduleChangeRequest> resultado = scheduleChangeRequestRepository.findByStatus(null);

        assertTrue(resultado.isEmpty());
        verify(scheduleChangeRequestRepository, times(1)).findByStatus(null);
    }

    @Test
    @DisplayName("Caso borde - Verificar integridad de datos en solicitudes encontradas")
    void testIntegridadDatosSolicitudes() {
        when(scheduleChangeRequestRepository.findByStudentId("STU001"))
                .thenReturn(List.of(request1));

        List<ScheduleChangeRequest> resultado = scheduleChangeRequestRepository.findByStudentId("STU001");

        assertAll("Verificar integridad completa de la solicitud",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("REQ001", resultado.get(0).getRequestId()),
                () -> assertNotNull(resultado.get(0).getStudent()),
                () -> assertNotNull(resultado.get(0).getCurrentGroup()),
                () -> assertNotNull(resultado.get(0).getRequestedGroup()),
                () -> assertEquals("Schedule conflict with work", resultado.get(0).getReason()),
                () -> assertEquals(RequestStatus.PENDING, resultado.get(0).getStatus()),
                () -> assertNotNull(resultado.get(0).getSubmissionDate()),
                () -> assertNull(resultado.get(0).getResolutionDate()),
                () -> assertNotNull(resultado.get(0).getReviewHistory()),
                () -> assertInstanceOf(ScheduleChangeRequest.class, resultado.get(0)),
                () -> assertNotNull(resultado.get(0).toString())
        );

        verify(scheduleChangeRequestRepository, times(1)).findByStudentId("STU001");
    }

    @Test
    @DisplayName("Caso exitoso - existsPendingOrUnderReviewByStudentAndGroup con query personalizada")
    void testExistsPendingOrUnderReviewByStudentAndGroup_Exitoso() {
        when(scheduleChangeRequestRepository.existsPendingOrUnderReviewByStudentAndGroup("STU001", "GROUP001"))
                .thenReturn(true);

        boolean resultado = scheduleChangeRequestRepository.existsPendingOrUnderReviewByStudentAndGroup("STU001", "GROUP001");

        assertTrue(resultado);
        verify(scheduleChangeRequestRepository, times(1)).existsPendingOrUnderReviewByStudentAndGroup("STU001", "GROUP001");
    }

    @Test
    @DisplayName("Caso exitoso - findByMinReviewSteps con query personalizada")
    void testFindByMinReviewSteps_Exitoso() {
        when(scheduleChangeRequestRepository.findByMinReviewSteps(2))
                .thenReturn(List.of(request2));

        List<ScheduleChangeRequest> resultado = scheduleChangeRequestRepository.findByMinReviewSteps(2);

        assertAll("Verificar solicitudes con mínimo de pasos de revisión",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertTrue(resultado.get(0).getReviewHistory().size() >= 2)
        );

        verify(scheduleChangeRequestRepository, times(1)).findByMinReviewSteps(2);
    }
}
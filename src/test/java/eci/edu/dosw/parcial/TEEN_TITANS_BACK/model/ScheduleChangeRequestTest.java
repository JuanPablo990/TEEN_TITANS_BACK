package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.RequestStatus;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.RoomType;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ScheduleChangeRequestTest {

    private ScheduleChangeRequest request;
    private Student student;
    private Group currentGroup;
    private Group requestedGroup;
    private ReviewStep reviewStep1;
    private ReviewStep reviewStep2;
    private Course course;
    private Professor professor;
    private Schedule schedule;
    private Classroom classroom;

    @BeforeEach
    void setUp() {
        student = new Student(
                "STU001",
                "Juan Pérez",
                "juan.perez@titans.edu",
                "password123",
                "Ingeniería de Sistemas",
                5
        );

        course = new Course(
                "MATH101",
                "Cálculo I",
                4,
                "Curso introductorio de cálculo diferencial e integral",
                "Ingeniería de Sistemas",
                true
        );

        schedule = new Schedule(
                "SCH001",
                "Lunes",
                "08:00",
                "10:00",
                "2025-1"
        );

        classroom = new Classroom(
                "CLS001",
                "Edificio Principal",
                "A101",
                40,
                RoomType.REGULAR
        );

        professor = new Professor(
                "PROF001",
                "Dr. Carlos Rodríguez",
                "carlos.rodriguez@titans.edu",
                "password123",
                "Matemáticas",
                true,
                Arrays.asList("Cálculo", "Álgebra", "Estadística")
        );

        currentGroup = new Group(
                "GRP001",
                "A",
                course,
                professor,
                schedule,
                classroom
        );

        requestedGroup = new Group(
                "GRP002",
                "B",
                course,
                professor,
                schedule,
                classroom
        );

        reviewStep1 = ReviewStep.builder()
                .userId("PROF001")
                .userRole(UserRole.PROFESSOR)
                .action("Revisión inicial")
                .comments("Solicitud en proceso de revisión")
                .build();

        reviewStep2 = ReviewStep.builder()
                .userId("ADMIN001")
                .userRole(UserRole.ADMINISTRATOR)
                .action("Aprobación final")
                .comments("Solicitud aprobada")
                .build();

        request = ScheduleChangeRequest.builder()
                .requestId("REQ001")
                .student(student)
                .currentGroup(currentGroup)
                .requestedGroup(requestedGroup)
                .reason("Conflicto de horario con otro curso")
                .status(RequestStatus.PENDING)
                .submissionDate(new Date())
                .reviewHistory(Arrays.asList(reviewStep1))
                .build();
    }

    @Test
    @DisplayName("Caso exitoso - Builder inicializa todos los campos correctamente")
    void testBuilderInicializacion_Exitoso() {
        assertAll("Validar todos los campos inicializados por el builder",
                () -> assertEquals("REQ001", request.getRequestId()),
                () -> assertEquals(student, request.getStudent()),
                () -> assertEquals(currentGroup, request.getCurrentGroup()),
                () -> assertEquals(requestedGroup, request.getRequestedGroup()),
                () -> assertEquals("Conflicto de horario con otro curso", request.getReason()),
                () -> assertEquals(RequestStatus.PENDING, request.getStatus()),
                () -> assertNotNull(request.getSubmissionDate()),
                () -> assertNull(request.getResolutionDate()),
                () -> assertEquals(1, request.getReviewHistory().size()),
                () -> assertEquals(reviewStep1, request.getReviewHistory().get(0))
        );
    }

    @Test
    @DisplayName("Caso exitoso - Constructor personalizado inicializa campos básicos")
    void testConstructorPersonalizado_Exitoso() {
        ScheduleChangeRequest requestPersonalizado = new ScheduleChangeRequest(
                "REQ002",
                student,
                currentGroup,
                requestedGroup,
                "Razón personalizada"
        );

        assertAll("Validar constructor personalizado",
                () -> assertEquals("REQ002", requestPersonalizado.getRequestId()),
                () -> assertEquals(student, requestPersonalizado.getStudent()),
                () -> assertEquals(currentGroup, requestPersonalizado.getCurrentGroup()),
                () -> assertEquals(requestedGroup, requestPersonalizado.getRequestedGroup()),
                () -> assertEquals("Razón personalizada", requestPersonalizado.getReason()),
                () -> assertEquals(RequestStatus.PENDING, requestPersonalizado.getStatus()),
                () -> assertNotNull(requestPersonalizado.getSubmissionDate()),
                () -> assertNull(requestPersonalizado.getResolutionDate()),
                () -> assertNotNull(requestPersonalizado.getReviewHistory()),
                () -> assertTrue(requestPersonalizado.getReviewHistory().isEmpty())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Constructor por defecto inicializa con valores por defecto")
    void testConstructorPorDefecto_Exitoso() {
        ScheduleChangeRequest requestVacio = new ScheduleChangeRequest();

        assertAll("Validar valores por defecto",
                () -> assertNull(requestVacio.getRequestId()),
                () -> assertNull(requestVacio.getStudent()),
                () -> assertNull(requestVacio.getCurrentGroup()),
                () -> assertNull(requestVacio.getRequestedGroup()),
                () -> assertNull(requestVacio.getReason()),
                () -> assertEquals(RequestStatus.PENDING, requestVacio.getStatus()),
                () -> assertNotNull(requestVacio.getSubmissionDate()),
                () -> assertNull(requestVacio.getResolutionDate()),
                () -> assertNotNull(requestVacio.getReviewHistory()),
                () -> assertTrue(requestVacio.getReviewHistory().isEmpty())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Setters y Getters funcionan correctamente")
    void testSettersAndGetters_Exitoso() {
        ScheduleChangeRequest requestTest = new ScheduleChangeRequest();
        Student newStudent = new Student("STU002", "María García", "maria@titans.edu", "pass", "Medicina", 3);
        Group newCurrentGroup = new Group("GRP003", "C", course, professor, schedule, classroom);
        Group newRequestedGroup = new Group("GRP004", "D", course, professor, schedule, classroom);
        Date newSubmissionDate = new Date(System.currentTimeMillis() - 1000000);
        List<ReviewStep> newReviewHistory = Arrays.asList(reviewStep1, reviewStep2);

        requestTest.setRequestId("REQ003");
        requestTest.setStudent(newStudent);
        requestTest.setCurrentGroup(newCurrentGroup);
        requestTest.setRequestedGroup(newRequestedGroup);
        requestTest.setReason("Nueva razón");
        requestTest.setStatus(RequestStatus.UNDER_REVIEW);
        requestTest.setSubmissionDate(newSubmissionDate);
        requestTest.setResolutionDate(newSubmissionDate);
        requestTest.setReviewHistory(newReviewHistory);

        assertAll("Validar setters y getters",
                () -> assertEquals("REQ003", requestTest.getRequestId()),
                () -> assertEquals(newStudent, requestTest.getStudent()),
                () -> assertEquals(newCurrentGroup, requestTest.getCurrentGroup()),
                () -> assertEquals(newRequestedGroup, requestTest.getRequestedGroup()),
                () -> assertEquals("Nueva razón", requestTest.getReason()),
                () -> assertEquals(RequestStatus.UNDER_REVIEW, requestTest.getStatus()),
                () -> assertEquals(newSubmissionDate, requestTest.getSubmissionDate()),
                () -> assertEquals(newSubmissionDate, requestTest.getResolutionDate()),
                () -> assertEquals(2, requestTest.getReviewHistory().size()),
                () -> assertEquals(newReviewHistory, requestTest.getReviewHistory())
        );
    }

    @Test
    @DisplayName("Caso exitoso - setStatus establece resolutionDate para estados finales")
    void testSetStatus_EstadosFinales_Exitoso() {
        request.setStatus(RequestStatus.APPROVED);

        assertAll("Validar setStatus con estado aprobado",
                () -> assertEquals(RequestStatus.APPROVED, request.getStatus()),
                () -> assertNotNull(request.getResolutionDate())
        );

        request.setStatus(RequestStatus.REJECTED);

        assertAll("Validar setStatus con estado rechazado",
                () -> assertEquals(RequestStatus.REJECTED, request.getStatus()),
                () -> assertNotNull(request.getResolutionDate())
        );
    }

    @Test
    @DisplayName("Caso borde - setStatus no establece resolutionDate para estados no finales")
    void testSetStatus_EstadosNoFinales_Borde() {
        request.setStatus(RequestStatus.UNDER_REVIEW);

        assertAll("Validar setStatus con estado en revisión",
                () -> assertEquals(RequestStatus.UNDER_REVIEW, request.getStatus()),
                () -> assertNull(request.getResolutionDate())
        );

        request.setStatus(RequestStatus.PENDING);

        assertAll("Validar setStatus con estado pendiente",
                () -> assertEquals(RequestStatus.PENDING, request.getStatus()),
                () -> assertNull(request.getResolutionDate())
        );

        request.setStatus(RequestStatus.CANCELLED);

        assertAll("Validar setStatus con estado cancelado",
                () -> assertEquals(RequestStatus.CANCELLED, request.getStatus()),
                () -> assertNull(request.getResolutionDate())
        );
    }




    @Test
    @DisplayName("Caso borde - Campos con valores null")
    void testCamposConValoresNull_Borde() {
        ScheduleChangeRequest requestNull = ScheduleChangeRequest.builder()
                .requestId(null)
                .student(null)
                .currentGroup(null)
                .requestedGroup(null)
                .reason(null)
                .status(null)
                .submissionDate(null)
                .resolutionDate(null)
                .reviewHistory(null)
                .build();

        assertAll("Validar campos null",
                () -> assertNull(requestNull.getRequestId()),
                () -> assertNull(requestNull.getStudent()),
                () -> assertNull(requestNull.getCurrentGroup()),
                () -> assertNull(requestNull.getRequestedGroup()),
                () -> assertNull(requestNull.getReason()),
                () -> assertNull(requestNull.getStatus()),
                () -> assertNull(requestNull.getSubmissionDate()),
                () -> assertNull(requestNull.getResolutionDate()),
                () -> assertNull(requestNull.getReviewHistory())
        );
    }

    @Test
    @DisplayName("Caso borde - Campos con valores vacíos")
    void testCamposConValoresVacios_Borde() {
        request.setRequestId("");
        request.setReason("");

        assertAll("Validar campos vacíos",
                () -> assertEquals("", request.getRequestId()),
                () -> assertEquals("", request.getReason())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Equals y HashCode generados por Lombok")
    void testEqualsAndHashCode_Exitoso() {
        ScheduleChangeRequest request1 = ScheduleChangeRequest.builder()
                .requestId("REQ001")
                .student(student)
                .currentGroup(currentGroup)
                .requestedGroup(requestedGroup)
                .reason("Razón")
                .status(RequestStatus.PENDING)
                .build();

        ScheduleChangeRequest request2 = ScheduleChangeRequest.builder()
                .requestId("REQ001")
                .student(student)
                .currentGroup(currentGroup)
                .requestedGroup(requestedGroup)
                .reason("Razón")
                .status(RequestStatus.PENDING)
                .build();

        ScheduleChangeRequest request3 = ScheduleChangeRequest.builder()
                .requestId("REQ002")
                .student(student)
                .currentGroup(currentGroup)
                .requestedGroup(requestedGroup)
                .reason("Otra razón")
                .status(RequestStatus.APPROVED)
                .build();

        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }

    @Test
    @DisplayName("Caso borde - Equals con objetos null y diferente clase")
    void testEqualsCasosBorde_Borde() {
        ScheduleChangeRequest requestTest = ScheduleChangeRequest.builder()
                .requestId("REQ001")
                .student(student)
                .currentGroup(currentGroup)
                .requestedGroup(requestedGroup)
                .reason("Test")
                .build();

        assertNotEquals(null, requestTest);
        assertNotEquals("No soy un ScheduleChangeRequest", requestTest);
    }



    @Test
    @DisplayName("Caso compuesto - Validación completa de múltiples escenarios")
    void testScheduleChangeRequest_ValidacionCompleta_Compuesto() {
        assertAll("Validación completa de todas las propiedades",
                () -> assertEquals("REQ001", request.getRequestId()),
                () -> assertEquals(student, request.getStudent()),
                () -> assertEquals(currentGroup, request.getCurrentGroup()),
                () -> assertEquals(requestedGroup, request.getRequestedGroup()),
                () -> assertEquals("Conflicto de horario con otro curso", request.getReason()),
                () -> assertEquals(RequestStatus.PENDING, request.getStatus()),
                () -> assertNotNull(request.getSubmissionDate()),
                () -> assertNull(request.getResolutionDate()),
                () -> assertEquals(1, request.getReviewHistory().size()),
                () -> assertNotNull(request.toString()),
                () -> assertTrue(request.toString().contains("ScheduleChangeRequest"))
        );
    }

    @Test
    @DisplayName("Caso borde - RequestStatus con todos los valores posibles")
    void testRequestStatusTodosLosValores_Borde() {
        request.setStatus(RequestStatus.PENDING);
        assertEquals(RequestStatus.PENDING, request.getStatus());

        request.setStatus(RequestStatus.UNDER_REVIEW);
        assertEquals(RequestStatus.UNDER_REVIEW, request.getStatus());

        request.setStatus(RequestStatus.APPROVED);
        assertEquals(RequestStatus.APPROVED, request.getStatus());

        request.setStatus(RequestStatus.REJECTED);
        assertEquals(RequestStatus.REJECTED, request.getStatus());

        request.setStatus(RequestStatus.CANCELLED);
        assertEquals(RequestStatus.CANCELLED, request.getStatus());
    }

    @Test
    @DisplayName("Caso borde - RequestId con diferentes formatos")
    void testRequestIdFormatos_Borde() {
        request.setRequestId("2025-REQ-001");
        assertEquals("2025-REQ-001", request.getRequestId());

        request.setRequestId("SOLICITUD_CAMBIO_1");
        assertEquals("SOLICITUD_CAMBIO_1", request.getRequestId());

        request.setRequestId("123456789");
        assertEquals("123456789", request.getRequestId());
    }

    @Test
    @DisplayName("Caso borde - Razones largas")
    void testRazonesLargas_Borde() {
        String razonLarga = "Esta es una razón muy extensa que justifica detalladamente ".repeat(10);
        request.setReason(razonLarga);
        assertEquals(razonLarga, request.getReason());

        request.setReason("");
        assertEquals("", request.getReason());

        request.setReason(null);
        assertNull(request.getReason());
    }

    @Test
    @DisplayName("Caso borde - Historial de revisiones vacío y con múltiples pasos")
    void testHistorialRevisiones_Borde() {
        ScheduleChangeRequest requestSinHistorial = ScheduleChangeRequest.builder()
                .requestId("REQ004")
                .student(student)
                .currentGroup(currentGroup)
                .requestedGroup(requestedGroup)
                .reason("Test")
                .reviewHistory(Arrays.asList())
                .build();

        ScheduleChangeRequest requestConMultiplesPasos = ScheduleChangeRequest.builder()
                .requestId("REQ005")
                .student(student)
                .currentGroup(currentGroup)
                .requestedGroup(requestedGroup)
                .reason("Test")
                .reviewHistory(Arrays.asList(reviewStep1, reviewStep2))
                .build();

        assertAll("Validar diferentes configuraciones de historial",
                () -> assertTrue(requestSinHistorial.getReviewHistory().isEmpty()),
                () -> assertEquals(2, requestConMultiplesPasos.getReviewHistory().size())
        );
    }
}
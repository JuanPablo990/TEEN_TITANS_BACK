package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.RequestStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentRequestServiceTest {

    @Mock
    private ScheduleChangeRequestRepository scheduleChangeRequestRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private ReviewStepRepository reviewStepRepository;

    @Mock
    private StudentAcademicProgressRepository studentAcademicProgressRepository;

    @InjectMocks
    private StudentRequestService studentRequestService;

    private Student student;
    private StudentAcademicProgress progress;
    private ScheduleChangeRequest pendingRequest;
    private ScheduleChangeRequest approvedRequest;
    private ScheduleChangeRequest rejectedRequest;
    private ScheduleChangeRequest underReviewRequest;
    private Group currentGroup;
    private Group requestedGroup;
    private Course course;
    private Classroom classroom;
    private Schedule schedule;
    private ReviewStep reviewStep;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId("STU001");
        student.setName("John Doe");
        student.setSemester(5);
        student.setActive(true);

        course = new Course();
        course.setCourseCode("CS101");
        course.setName("Introduction to Computer Science");

        classroom = new Classroom();
        classroom.setCapacity(30);

        schedule = new Schedule();
        schedule.setDayOfWeek("Monday");
        schedule.setPeriod("Morning");

        currentGroup = new Group();
        currentGroup.setGroupId("GROUP_001");
        currentGroup.setCourse(course);
        currentGroup.setClassroom(classroom);
        currentGroup.setSchedule(schedule);

        requestedGroup = new Group();
        requestedGroup.setGroupId("GROUP_002");
        requestedGroup.setCourse(course);
        requestedGroup.setClassroom(classroom);
        requestedGroup.setSchedule(schedule);

        progress = new StudentAcademicProgress();
        progress.setStudent(student);
        progress.setCumulativeGPA(4.0);
        progress.setCoursesStatus(new ArrayList<>());

        pendingRequest = ScheduleChangeRequest.builder()
                .requestId("REQ001")
                .student(student)
                .currentGroup(currentGroup)
                .requestedGroup(requestedGroup)
                .reason("Conflictos de horario")
                .status(RequestStatus.PENDING)
                .submissionDate(new Date(System.currentTimeMillis() - 86400000))
                .reviewHistory(new ArrayList<>())
                .build();

        approvedRequest = ScheduleChangeRequest.builder()
                .requestId("REQ002")
                .student(student)
                .currentGroup(currentGroup)
                .requestedGroup(requestedGroup)
                .reason("Cambio necesario")
                .status(RequestStatus.APPROVED)
                .submissionDate(new Date(System.currentTimeMillis() - 172800000))
                .resolutionDate(new Date())
                .reviewHistory(new ArrayList<>())
                .build();

        rejectedRequest = ScheduleChangeRequest.builder()
                .requestId("REQ003")
                .student(student)
                .currentGroup(currentGroup)
                .requestedGroup(requestedGroup)
                .reason("Cambio solicitado")
                .status(RequestStatus.REJECTED)
                .submissionDate(new Date(System.currentTimeMillis() - 259200000))
                .resolutionDate(new Date())
                .reviewHistory(new ArrayList<>())
                .build();

        underReviewRequest = ScheduleChangeRequest.builder()
                .requestId("REQ004")
                .student(student)
                .currentGroup(currentGroup)
                .requestedGroup(requestedGroup)
                .reason("Revisión en proceso")
                .status(RequestStatus.UNDER_REVIEW)
                .submissionDate(new Date(System.currentTimeMillis() - 43200000))
                .reviewHistory(new ArrayList<>())
                .build();

        reviewStep = new ReviewStep();
        reviewStep.setAction("EN_REVISION");
        reviewStep.setTimestamp(new Date(System.currentTimeMillis() - 7200000));
    }

    @Test
    @DisplayName("Caso exitoso - getStudentApprovalRate calcula tasa de aprobación correctamente")
    void testGetStudentApprovalRate_Exitoso() {
        when(studentRepository.existsById("STU001")).thenReturn(true);
        when(scheduleChangeRequestRepository.findByStudentId("STU001"))
                .thenReturn(Arrays.asList(approvedRequest, rejectedRequest, pendingRequest));

        double resultado = studentRequestService.getStudentApprovalRate("STU001");

        assertEquals(50.0, resultado);
        verify(studentRepository, times(1)).existsById("STU001");
        verify(scheduleChangeRequestRepository, times(1)).findByStudentId("STU001");
    }

    @Test
    @DisplayName("Caso borde - getStudentApprovalRate retorna 0 cuando no hay solicitudes resueltas")
    void testGetStudentApprovalRate_SinSolicitudesResueltas() {
        when(studentRepository.existsById("STU001")).thenReturn(true);
        when(scheduleChangeRequestRepository.findByStudentId("STU001"))
                .thenReturn(Arrays.asList(pendingRequest, underReviewRequest));

        double resultado = studentRequestService.getStudentApprovalRate("STU001");

        assertEquals(0.0, resultado);
        verify(studentRepository, times(1)).existsById("STU001");
        verify(scheduleChangeRequestRepository, times(1)).findByStudentId("STU001");
    }

    @Test
    @DisplayName("Caso error - getStudentApprovalRate lanza excepción cuando estudiante no existe")
    void testGetStudentApprovalRate_EstudianteNoExiste() {
        when(studentRepository.existsById("STU_INEXISTENTE")).thenReturn(false);

        AppException exception = assertThrows(AppException.class, () -> {
            studentRequestService.getStudentApprovalRate("STU_INEXISTENTE");
        });

        assertEquals("El estudiante con ID STU_INEXISTENTE no existe.", exception.getMessage());
        verify(studentRepository, times(1)).existsById("STU_INEXISTENTE");
    }

    @Test
    @DisplayName("Caso exitoso - getPendingRequestsStatus retorna solicitudes pendientes y en revisión")
    void testGetPendingRequestsStatus_Exitoso() {
        when(studentRepository.existsById("STU001")).thenReturn(true);
        when(scheduleChangeRequestRepository.findByStudentIdAndStatus("STU001", RequestStatus.PENDING))
                .thenReturn(Arrays.asList(pendingRequest));
        when(scheduleChangeRequestRepository.findByStudentIdAndStatus("STU001", RequestStatus.UNDER_REVIEW))
                .thenReturn(Arrays.asList(underReviewRequest));

        List<ScheduleChangeRequest> resultado = studentRequestService.getPendingRequestsStatus("STU001");

        assertAll("Verificar solicitudes pendientes y en revisión",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertTrue(resultado.stream().anyMatch(r -> r.getStatus() == RequestStatus.PENDING)),
                () -> assertTrue(resultado.stream().anyMatch(r -> r.getStatus() == RequestStatus.UNDER_REVIEW))
        );

        verify(studentRepository, times(1)).existsById("STU001");
        verify(scheduleChangeRequestRepository, times(1)).findByStudentIdAndStatus("STU001", RequestStatus.PENDING);
        verify(scheduleChangeRequestRepository, times(1)).findByStudentIdAndStatus("STU001", RequestStatus.UNDER_REVIEW);
    }

    @Test
    @DisplayName("Caso exitoso - getRequestPriorityPosition calcula posición correctamente")
    void testGetRequestPriorityPosition_Exitoso() {
        when(scheduleChangeRequestRepository.findById("REQ001")).thenReturn(Optional.of(pendingRequest));
        when(studentAcademicProgressRepository.findByStudentId("STU001")).thenReturn(Optional.of(progress));
        when(scheduleChangeRequestRepository.findByStatus(RequestStatus.PENDING))
                .thenReturn(Arrays.asList(pendingRequest));

        int resultado = studentRequestService.getRequestPriorityPosition("REQ001");

        assertEquals(1, resultado);
        verify(scheduleChangeRequestRepository, times(1)).findById("REQ001");
        verify(scheduleChangeRequestRepository, times(1)).findByStatus(RequestStatus.PENDING);
    }





    @Test
    @DisplayName("Caso borde - getRequestEstimatedWaitTime retorna mensaje para solicitud procesada")
    void testGetRequestEstimatedWaitTime_SolicitudProcesada() {
        when(scheduleChangeRequestRepository.findById("REQ002")).thenReturn(Optional.of(approvedRequest));

        String resultado = studentRequestService.getRequestEstimatedWaitTime("REQ002");

        assertEquals("Solicitud ya procesada", resultado);
        verify(scheduleChangeRequestRepository, times(1)).findById("REQ002");
    }

    @Test
    @DisplayName("Caso exitoso - canCancelRequest retorna true para solicitud pendiente")
    void testCanCancelRequest_SolicitudPendiente() {
        when(scheduleChangeRequestRepository.findById("REQ001")).thenReturn(Optional.of(pendingRequest));

        boolean resultado = studentRequestService.canCancelRequest("REQ001");

        assertTrue(resultado);
        verify(scheduleChangeRequestRepository, times(1)).findById("REQ001");
    }

    @Test
    @DisplayName("Caso borde - canCancelRequest retorna false para solicitud aprobada")
    void testCanCancelRequest_SolicitudAprobada() {
        when(scheduleChangeRequestRepository.findById("REQ002")).thenReturn(Optional.of(approvedRequest));

        boolean resultado = studentRequestService.canCancelRequest("REQ002");

        assertFalse(resultado);
        verify(scheduleChangeRequestRepository, times(1)).findById("REQ002");
    }

    @Test
    @DisplayName("Caso exitoso - canCancelRequest retorna true para solicitud en revisión reciente")
    void testCanCancelRequest_SolicitudEnRevisionReciente() {
        underReviewRequest.getReviewHistory().add(reviewStep);
        when(scheduleChangeRequestRepository.findById("REQ004")).thenReturn(Optional.of(underReviewRequest));

        boolean resultado = studentRequestService.canCancelRequest("REQ004");

        assertTrue(resultado);
        verify(scheduleChangeRequestRepository, times(1)).findById("REQ004");
    }

    @Test
    @DisplayName("Caso exitoso - getRequiredDocuments retorna documentos para cambio de grupo")
    void testGetRequiredDocuments_CambioGrupo() {
        List<String> resultado = studentRequestService.getRequiredDocuments("GROUP_CHANGE");

        assertAll("Verificar documentos para cambio de grupo",
                () -> assertNotNull(resultado),
                () -> assertFalse(resultado.isEmpty()),
                () -> assertTrue(resultado.contains("Formulario de cambio de grupo")),
                () -> assertTrue(resultado.contains("Justificación escrita"))
        );
    }

    @Test
    @DisplayName("Caso exitoso - getRequiredDocuments retorna documentos para cambio de curso")
    void testGetRequiredDocuments_CambioCurso() {
        List<String> resultado = studentRequestService.getRequiredDocuments("COURSE_CHANGE");

        assertAll("Verificar documentos para cambio de curso",
                () -> assertNotNull(resultado),
                () -> assertFalse(resultado.isEmpty()),
                () -> assertTrue(resultado.contains("Formulario de cambio de curso")),
                () -> assertTrue(resultado.contains("Aprobación del coordinador"))
        );
    }

    @Test
    @DisplayName("Caso borde - getRequiredDocuments retorna documentos por defecto para tipo desconocido")
    void testGetRequiredDocuments_TipoDesconocido() {
        List<String> resultado = studentRequestService.getRequiredDocuments("TIPO_DESCONOCIDO");

        assertAll("Verificar documentos por defecto",
                () -> assertNotNull(resultado),
                () -> assertFalse(resultado.isEmpty()),
                () -> assertTrue(resultado.contains("Formulario general de solicitud"))
        );
    }

    @Test
    @DisplayName("Caso borde - getStudentApprovalRate retorna 100 cuando todas las solicitudes son aprobadas")
    void testGetStudentApprovalRate_TodasAprobadas() {
        when(studentRepository.existsById("STU001")).thenReturn(true);
        when(scheduleChangeRequestRepository.findByStudentId("STU001"))
                .thenReturn(Arrays.asList(approvedRequest, approvedRequest));

        double resultado = studentRequestService.getStudentApprovalRate("STU001");

        assertEquals(100.0, resultado);
        verify(studentRepository, times(1)).existsById("STU001");
        verify(scheduleChangeRequestRepository, times(1)).findByStudentId("STU001");
    }

    @Test
    @DisplayName("Caso borde - getPendingRequestsStatus retorna lista vacía cuando no hay solicitudes")
    void testGetPendingRequestsStatus_SinSolicitudes() {
        when(studentRepository.existsById("STU001")).thenReturn(true);
        when(scheduleChangeRequestRepository.findByStudentIdAndStatus("STU001", RequestStatus.PENDING))
                .thenReturn(Collections.emptyList());
        when(scheduleChangeRequestRepository.findByStudentIdAndStatus("STU001", RequestStatus.UNDER_REVIEW))
                .thenReturn(Collections.emptyList());

        List<ScheduleChangeRequest> resultado = studentRequestService.getPendingRequestsStatus("STU001");

        assertAll("Verificar lista vacía de solicitudes",
                () -> assertNotNull(resultado),
                () -> assertTrue(resultado.isEmpty())
        );

        verify(studentRepository, times(1)).existsById("STU001");
        verify(scheduleChangeRequestRepository, times(1)).findByStudentIdAndStatus("STU001", RequestStatus.PENDING);
        verify(scheduleChangeRequestRepository, times(1)).findByStudentIdAndStatus("STU001", RequestStatus.UNDER_REVIEW);
    }



    @Test
    @DisplayName("Caso error - getRequestPriorityPosition lanza excepción cuando solicitud no existe")
    void testGetRequestPriorityPosition_SolicitudNoExiste() {
        when(scheduleChangeRequestRepository.findById("REQ_INEXISTENTE")).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            studentRequestService.getRequestPriorityPosition("REQ_INEXISTENTE");
        });

        assertEquals("No se encontró la solicitud con ID: REQ_INEXISTENTE", exception.getMessage());
        verify(scheduleChangeRequestRepository, times(1)).findById("REQ_INEXISTENTE");
    }
}
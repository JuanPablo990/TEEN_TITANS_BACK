package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.RequestStatus;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.*;
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
public class RequestServiceTest {

    @Mock
    private ScheduleChangeRequestRepository scheduleChangeRequestRepository;

    @Mock
    private ReviewStepRepository reviewStepRepository;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private RequestService requestService;

    private Student student;
    private Group currentGroup;
    private Group requestedGroup;
    private Course currentCourse;
    private Course requestedCourse;
    private Classroom classroom;
    private ScheduleChangeRequest pendingRequest;
    private ScheduleChangeRequest approvedRequest;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId("STU001");
        student.setName("John Doe");
        student.setActive(true);

        classroom = new Classroom();
        classroom.setCapacity(30);

        currentCourse = new Course();
        currentCourse.setCourseCode("CS101");
        currentCourse.setName("Introduction to Computer Science");
        currentCourse.setIsActive(true);

        requestedCourse = new Course();
        requestedCourse.setCourseCode("CS102");
        requestedCourse.setName("Data Structures");
        requestedCourse.setIsActive(true);

        currentGroup = new Group();
        currentGroup.setGroupId("GROUP_001");
        currentGroup.setCourse(currentCourse);
        currentGroup.setClassroom(classroom);

        requestedGroup = new Group();
        requestedGroup.setGroupId("GROUP_002");
        requestedGroup.setCourse(requestedCourse);
        requestedGroup.setClassroom(classroom);

        pendingRequest = ScheduleChangeRequest.builder()
                .requestId("REQ001")
                .student(student)
                .currentGroup(currentGroup)
                .requestedGroup(requestedGroup)
                .reason("Conflictos de horario")
                .status(RequestStatus.PENDING)
                .submissionDate(new Date())
                .reviewHistory(new ArrayList<>())
                .build();

        approvedRequest = ScheduleChangeRequest.builder()
                .requestId("REQ002")
                .student(student)
                .currentGroup(currentGroup)
                .requestedGroup(requestedGroup)
                .reason("Cambio necesario")
                .status(RequestStatus.APPROVED)
                .submissionDate(new Date())
                .resolutionDate(new Date())
                .reviewHistory(new ArrayList<>())
                .build();
    }


    @Test
    @DisplayName("Caso error - createGroupChangeRequest lanza excepción cuando estudiante no existe")
    void testCreateGroupChangeRequest_EstudianteNoExiste() {
        when(studentRepository.findById("STU_INEXISTENTE")).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            requestService.createGroupChangeRequest("STU_INEXISTENTE", "GROUP_001", "GROUP_002", "Razón");
        });

        assertEquals("Estudiante no válido o inactivo: STU_INEXISTENTE", exception.getMessage());
        verify(studentRepository, times(1)).findById("STU_INEXISTENTE");
    }

    @Test
    @DisplayName("Caso error - createGroupChangeRequest lanza excepción cuando grupo no existe")
    void testCreateGroupChangeRequest_GrupoNoExiste() {
        when(studentRepository.findById("STU001")).thenReturn(Optional.of(student));
        when(groupRepository.findById("GROUP_001")).thenReturn(Optional.of(currentGroup));
        when(groupRepository.findById("GRUPO_INEXISTENTE")).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            requestService.createGroupChangeRequest("STU001", "GROUP_001", "GRUPO_INEXISTENTE", "Razón");
        });

        assertEquals("Grupo no encontrado: GRUPO_INEXISTENTE", exception.getMessage());
        verify(groupRepository, times(1)).findById("GRUPO_INEXISTENTE");
    }

    @Test
    @DisplayName("Caso error - createGroupChangeRequest lanza excepción cuando mismo grupo")
    void testCreateGroupChangeRequest_MismoGrupo() {
        when(studentRepository.findById("STU001")).thenReturn(Optional.of(student));
        when(groupRepository.findById("GROUP_001")).thenReturn(Optional.of(currentGroup));

        AppException exception = assertThrows(AppException.class, () -> {
            requestService.createGroupChangeRequest("STU001", "GROUP_001", "GROUP_001", "Razón");
        });

        assertEquals("El grupo actual y el solicitado no pueden ser el mismo", exception.getMessage());
    }

    @Test
    @DisplayName("Caso exitoso - createCourseChangeRequest crea solicitud de cambio de curso")
    void testCreateCourseChangeRequest_Exitoso() {
        when(studentRepository.findById("STU001")).thenReturn(Optional.of(student));
        when(courseRepository.findById("CS101")).thenReturn(Optional.of(currentCourse));
        when(courseRepository.findById("CS102")).thenReturn(Optional.of(requestedCourse));
        when(scheduleChangeRequestRepository.countByStudentIdAndStatus("STU001", RequestStatus.PENDING)).thenReturn(0L);
        when(groupRepository.findByCourse_CourseCode("CS101")).thenReturn(Arrays.asList(currentGroup));
        when(groupRepository.findByCourse_CourseCode("CS102")).thenReturn(Arrays.asList(requestedGroup));
        when(groupRepository.findById("GROUP_001")).thenReturn(Optional.of(currentGroup));
        when(groupRepository.findById("GROUP_002")).thenReturn(Optional.of(requestedGroup));
        when(scheduleChangeRequestRepository.findByRequestedGroupId("GROUP_001")).thenReturn(Collections.emptyList());
        when(scheduleChangeRequestRepository.findByRequestedGroupId("GROUP_002")).thenReturn(Collections.emptyList());
        when(scheduleChangeRequestRepository.save(any(ScheduleChangeRequest.class))).thenReturn(pendingRequest);

        ScheduleChangeRequest resultado = requestService.createCourseChangeRequest("STU001", "CS101", "CS102", "Cambio de interés");

        assertAll("Verificar creación de solicitud de cambio de curso",
                () -> assertNotNull(resultado),
                () -> assertEquals("REQ001", resultado.getRequestId()),
                () -> assertEquals(RequestStatus.PENDING, resultado.getStatus())
        );

        verify(studentRepository, times(1)).findById("STU001");
        verify(courseRepository, times(1)).findById("CS101");
        verify(courseRepository, times(1)).findById("CS102");
        verify(scheduleChangeRequestRepository, times(1)).save(any(ScheduleChangeRequest.class));
    }

    @Test
    @DisplayName("Caso exitoso - approveRequest aprueba solicitud correctamente")
    void testApproveRequest_Exitoso() {
        when(scheduleChangeRequestRepository.findById("REQ001")).thenReturn(Optional.of(pendingRequest));
        when(groupRepository.findById("GROUP_002")).thenReturn(Optional.of(requestedGroup));
        when(scheduleChangeRequestRepository.findByRequestedGroupId("GROUP_002")).thenReturn(Collections.emptyList());
        when(scheduleChangeRequestRepository.save(any(ScheduleChangeRequest.class))).thenReturn(approvedRequest);

        ScheduleChangeRequest resultado = requestService.approveRequest("REQ001", "ADMIN001", UserRole.ADMINISTRATOR, "Solicitud aprobada");

        assertAll("Verificar aprobación de solicitud",
                () -> assertNotNull(resultado),
                () -> assertEquals(RequestStatus.APPROVED, resultado.getStatus()),
                () -> assertNotNull(resultado.getResolutionDate())
        );

        verify(scheduleChangeRequestRepository, times(1)).findById("REQ001");
        verify(groupRepository, times(1)).findById("GROUP_002");
        verify(scheduleChangeRequestRepository, times(1)).save(pendingRequest);
    }

    @Test
    @DisplayName("Caso error - approveRequest lanza excepción cuando grupo sin capacidad")
    void testApproveRequest_GrupoSinCapacidad() {
        Classroom classroomLleno = new Classroom();
        classroomLleno.setCapacity(25);

        Group grupoLleno = new Group();
        grupoLleno.setGroupId("GROUP_LLENO");
        grupoLleno.setClassroom(classroomLleno);

        pendingRequest.setRequestedGroup(grupoLleno);

        when(scheduleChangeRequestRepository.findById("REQ001")).thenReturn(Optional.of(pendingRequest));
        when(groupRepository.findById("GROUP_LLENO")).thenReturn(Optional.of(grupoLleno));

        List<ScheduleChangeRequest> solicitudesAprobadas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ScheduleChangeRequest req = new ScheduleChangeRequest();
            req.setStatus(RequestStatus.APPROVED);
            solicitudesAprobadas.add(req);
        }
        when(scheduleChangeRequestRepository.findByRequestedGroupId("GROUP_LLENO")).thenReturn(solicitudesAprobadas);

        AppException exception = assertThrows(AppException.class, () -> {
            requestService.approveRequest("REQ001", "ADMIN001", UserRole.ADMINISTRATOR, "Comentarios");
        });

        assertTrue(exception.getMessage().contains("No se puede aprobar la solicitud: el grupo solicitado ya no tiene capacidad disponible"));
        verify(scheduleChangeRequestRepository, times(1)).findById("REQ001");
        verify(groupRepository, times(1)).findById("GROUP_LLENO");
    }

    @Test
    @DisplayName("Caso exitoso - rejectRequest rechaza solicitud correctamente")
    void testRejectRequest_Exitoso() {
        when(scheduleChangeRequestRepository.findById("REQ001")).thenReturn(Optional.of(pendingRequest));
        when(scheduleChangeRequestRepository.save(any(ScheduleChangeRequest.class))).thenReturn(pendingRequest);

        ScheduleChangeRequest resultado = requestService.rejectRequest("REQ001", "ADMIN001", UserRole.ADMINISTRATOR, "Solicitud rechazada");

        assertAll("Verificar rechazo de solicitud",
                () -> assertNotNull(resultado),
                () -> assertEquals(RequestStatus.REJECTED, resultado.getStatus()),
                () -> assertNotNull(resultado.getResolutionDate())
        );

        verify(scheduleChangeRequestRepository, times(1)).findById("REQ001");
        verify(scheduleChangeRequestRepository, times(1)).save(pendingRequest);
    }

    @Test
    @DisplayName("Caso exitoso - cancelRequest cancela solicitud por estudiante")
    void testCancelRequest_Exitoso() {
        when(scheduleChangeRequestRepository.findById("REQ001")).thenReturn(Optional.of(pendingRequest));
        when(scheduleChangeRequestRepository.save(any(ScheduleChangeRequest.class))).thenReturn(pendingRequest);

        ScheduleChangeRequest resultado = requestService.cancelRequest("REQ001", "STU001");

        assertAll("Verificar cancelación de solicitud",
                () -> assertNotNull(resultado),
                () -> assertEquals(RequestStatus.CANCELLED, resultado.getStatus())
        );

        verify(scheduleChangeRequestRepository, times(1)).findById("REQ001");
        verify(scheduleChangeRequestRepository, times(1)).save(pendingRequest);
    }

    @Test
    @DisplayName("Caso error - cancelRequest lanza excepción cuando no es el propietario")
    void testCancelRequest_NoEsPropietario() {
        when(scheduleChangeRequestRepository.findById("REQ001")).thenReturn(Optional.of(pendingRequest));

        AppException exception = assertThrows(AppException.class, () -> {
            requestService.cancelRequest("REQ001", "OTRO_ESTUDIANTE");
        });

        assertEquals("Solo el estudiante propietario puede cancelar la solicitud", exception.getMessage());
        verify(scheduleChangeRequestRepository, times(1)).findById("REQ001");
    }

    @Test
    @DisplayName("Caso exitoso - getRequestStatus retorna estado de solicitud")
    void testGetRequestStatus_Exitoso() {
        when(scheduleChangeRequestRepository.findById("REQ001")).thenReturn(Optional.of(pendingRequest));

        RequestStatus resultado = requestService.getRequestStatus("REQ001");

        assertEquals(RequestStatus.PENDING, resultado);
        verify(scheduleChangeRequestRepository, times(1)).findById("REQ001");
    }

    @Test
    @DisplayName("Caso exitoso - getRequestHistory retorna historial del estudiante")
    void testGetRequestHistory_Exitoso() {
        when(studentRepository.findById("STU001")).thenReturn(Optional.of(student));
        when(scheduleChangeRequestRepository.findByStudentIdOrderBySubmissionDateDesc("STU001")).thenReturn(Arrays.asList(pendingRequest));

        List<ScheduleChangeRequest> resultado = requestService.getRequestHistory("STU001");

        assertAll("Verificar historial de solicitudes",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("REQ001", resultado.get(0).getRequestId())
        );

        verify(studentRepository, times(1)).findById("STU001");
        verify(scheduleChangeRequestRepository, times(1)).findByStudentIdOrderBySubmissionDateDesc("STU001");
    }

    @Test
    @DisplayName("Caso exitoso - getDecisionHistory retorna historial de decisiones")
    void testGetDecisionHistory_Exitoso() {
        ReviewStep reviewStep = new ReviewStep();
        reviewStep.setAction("SOLICITUD_CREADA");
        pendingRequest.getReviewHistory().add(reviewStep);

        when(scheduleChangeRequestRepository.findById("REQ001")).thenReturn(Optional.of(pendingRequest));

        List<ReviewStep> resultado = requestService.getDecisionHistory("REQ001");

        assertAll("Verificar historial de decisiones",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("SOLICITUD_CREADA", resultado.get(0).getAction())
        );

        verify(scheduleChangeRequestRepository, times(1)).findById("REQ001");
    }

    @Test
    @DisplayName("Caso exitoso - getRequestStatistics retorna estadísticas del estudiante")
    void testGetRequestStatistics_Exitoso() {
        when(studentRepository.findById("STU001")).thenReturn(Optional.of(student));
        when(scheduleChangeRequestRepository.findByStudentId("STU001")).thenReturn(Arrays.asList(pendingRequest, approvedRequest));

        Map<String, Object> resultado = requestService.getRequestStatistics("STU001");

        assertAll("Verificar estadísticas de solicitudes",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, ((Number) resultado.get("total")).intValue()),
                () -> assertEquals(1, ((Number) resultado.get("pending")).intValue()),
                () -> assertEquals(1, ((Number) resultado.get("approved")).intValue())
        );

        verify(studentRepository, times(1)).findById("STU001");
        verify(scheduleChangeRequestRepository, times(1)).findByStudentId("STU001");
    }

    @Test
    @DisplayName("Caso exitoso - getGroupCapacityAlert retorna false para grupo con capacidad")
    void testGetGroupCapacityAlert_ConCapacidad() {
        when(groupRepository.findById("GROUP_001")).thenReturn(Optional.of(currentGroup));
        when(scheduleChangeRequestRepository.findByRequestedGroupId("GROUP_001")).thenReturn(Collections.emptyList());

        boolean resultado = requestService.getGroupCapacityAlert("GROUP_001");

        assertFalse(resultado);
        verify(groupRepository, times(1)).findById("GROUP_001");
    }

    @Test
    @DisplayName("Caso exitoso - updateRequest actualiza solicitud correctamente")
    void testUpdateRequest_Exitoso() {
        when(scheduleChangeRequestRepository.findById("REQ001")).thenReturn(Optional.of(pendingRequest));
        when(scheduleChangeRequestRepository.save(any(ScheduleChangeRequest.class))).thenReturn(pendingRequest);

        Map<String, Object> updates = new HashMap<>();
        updates.put("reason", "Nueva razón para el cambio");

        ScheduleChangeRequest resultado = requestService.updateRequest("REQ001", updates);

        assertAll("Verificar actualización de solicitud",
                () -> assertNotNull(resultado),
                () -> assertEquals("Nueva razón para el cambio", resultado.getReason())
        );

        verify(scheduleChangeRequestRepository, times(1)).findById("REQ001");
        verify(scheduleChangeRequestRepository, times(1)).save(pendingRequest);
    }

    @Test
    @DisplayName("Caso error - updateRequest lanza excepción cuando estado es final")
    void testUpdateRequest_EstadoFinal() {
        when(scheduleChangeRequestRepository.findById("REQ002")).thenReturn(Optional.of(approvedRequest));

        Map<String, Object> updates = new HashMap<>();
        updates.put("reason", "Nueva razón");

        AppException exception = assertThrows(AppException.class, () -> {
            requestService.updateRequest("REQ002", updates);
        });

        assertTrue(exception.getMessage().contains("No se puede modificar una solicitud en estado final"));
        verify(scheduleChangeRequestRepository, times(1)).findById("REQ002");
    }

    @Test
    @DisplayName("Caso exitoso - deleteRequest elimina solicitud pendiente")
    void testDeleteRequest_Exitoso() {
        when(scheduleChangeRequestRepository.findById("REQ001")).thenReturn(Optional.of(pendingRequest));

        boolean resultado = requestService.deleteRequest("REQ001");

        assertTrue(resultado);
        verify(scheduleChangeRequestRepository, times(1)).findById("REQ001");
        verify(scheduleChangeRequestRepository, times(1)).delete(pendingRequest);
    }

    @Test
    @DisplayName("Caso error - deleteRequest lanza excepción cuando estado no es PENDING")
    void testDeleteRequest_EstadoNoPending() {
        when(scheduleChangeRequestRepository.findById("REQ002")).thenReturn(Optional.of(approvedRequest));

        AppException exception = assertThrows(AppException.class, () -> {
            requestService.deleteRequest("REQ002");
        });

        assertEquals("Solo se pueden eliminar solicitudes en estado PENDING", exception.getMessage());
        verify(scheduleChangeRequestRepository, times(1)).findById("REQ002");
    }

    @Test
    @DisplayName("Caso borde - getRequestStatistics con estudiante sin solicitudes")
    void testGetRequestStatistics_SinSolicitudes() {
        when(studentRepository.findById("STU001")).thenReturn(Optional.of(student));
        when(scheduleChangeRequestRepository.findByStudentId("STU001")).thenReturn(Collections.emptyList());

        Map<String, Object> resultado = requestService.getRequestStatistics("STU001");

        assertAll("Verificar estadísticas con cero solicitudes",
                () -> assertNotNull(resultado),
                () -> assertEquals(0, ((Number) resultado.get("total")).intValue()),
                () -> assertEquals(0, ((Number) resultado.get("pending")).intValue()),
                () -> assertEquals(0, ((Number) resultado.get("approved")).intValue())
        );

        verify(studentRepository, times(1)).findById("STU001");
        verify(scheduleChangeRequestRepository, times(1)).findByStudentId("STU001");
    }

    @Test
    @DisplayName("Caso borde - getGroupCapacityAlert retorna true para grupo cerca de capacidad")
    void testGetGroupCapacityAlert_CercaCapacidad() {
        Classroom classroomCercaCapacidad = new Classroom();
        classroomCercaCapacidad.setCapacity(25);

        Group grupoCercaCapacidad = new Group();
        grupoCercaCapacidad.setGroupId("GROUP_CAPACIDAD");
        grupoCercaCapacidad.setClassroom(classroomCercaCapacidad);

        when(groupRepository.findById("GROUP_CAPACIDAD")).thenReturn(Optional.of(grupoCercaCapacidad));

        List<ScheduleChangeRequest> solicitudesAprobadas = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            ScheduleChangeRequest req = new ScheduleChangeRequest();
            req.setStatus(RequestStatus.APPROVED);
            solicitudesAprobadas.add(req);
        }
        when(scheduleChangeRequestRepository.findByRequestedGroupId("GROUP_CAPACIDAD")).thenReturn(solicitudesAprobadas);

        boolean resultado = requestService.getGroupCapacityAlert("GROUP_CAPACIDAD");

        assertTrue(resultado);
        verify(groupRepository, times(1)).findById("GROUP_CAPACIDAD");
    }
}
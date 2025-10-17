package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para AdminRequestService
 */
@ExtendWith(MockitoExtension.class)
public class AdminRequestServiceTest {

    @Mock
    private ScheduleChangeRequestRepository scheduleChangeRequestRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ReviewStepRepository reviewStepRepository;

    @Mock
    private StudentAcademicProgressRepository studentAcademicProgressRepository;

    @Mock
    private DeanRepository deanRepository;

    @Mock
    private AdministratorRepository administratorRepository;

    @InjectMocks
    private AdminRequestService adminRequestService;

    private ScheduleChangeRequest request1;
    private ScheduleChangeRequest request2;
    private Student student1;
    private Student student2;
    private Group group1;
    private Group group2;
    private Course course1;
    private StudentAcademicProgress progress1;
    private StudentAcademicProgress progress2;
    private Classroom classroom1;

    @BeforeEach
    void setUp() {
        student1 = new Student("STU001", "Juan Pérez", "juan@titans.edu", "pass123",
                "Ingeniería de Sistemas", 5);
        student1.setGradeAverage(4.2);

        student2 = new Student("STU002", "María García", "maria@titans.edu", "pass456",
                "Medicina", 3);
        student2.setGradeAverage(3.8);

        course1 = new Course("CS101", "Programación I", 3,
                "Curso introductorio de programación", "Ingeniería", true);

        classroom1 = new Classroom("AULA001", "Edificio A", "101", 30, RoomType.LABORATORY);

        group1 = new Group("GRP001", "A", course1, null, null, classroom1);
        group2 = new Group("GRP002", "B", course1, null, null, classroom1);

        request1 = new ScheduleChangeRequest("REQ001", student1, group1, group2, "Conflicto de horario");
        request1.setStatus(RequestStatus.PENDING);

        request2 = new ScheduleChangeRequest("REQ002", student2, group2, group1, "Razón médica");
        request2.setStatus(RequestStatus.APPROVED);
        request2.setResolutionDate(new Date());

        progress1 = new StudentAcademicProgress("PROG001", student1, "Ingeniería de Sistemas",
                "Ingeniería", "Regular", 5, 10, 60, 160, 4.2, null);

        progress2 = new StudentAcademicProgress("PROG002", student2, "Medicina",
                "Medicina", "Regular", 3, 12, 45, 200, 3.8, null);
    }

    @Test
    @DisplayName("Caso exitoso - getRequestsByFaculty retorna solicitudes de la facultad")
    void testGetRequestsByFaculty_Exitoso() {
        when(studentAcademicProgressRepository.findByFaculty("Ingeniería"))
                .thenReturn(List.of(progress1));
        when(scheduleChangeRequestRepository.findAll())
                .thenReturn(List.of(request1, request2));

        List<ScheduleChangeRequest> resultado = adminRequestService.getRequestsByFaculty("Ingeniería");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("REQ001", resultado.get(0).getRequestId());
        assertEquals("Juan Pérez", resultado.get(0).getStudent().getName());

        verify(studentAcademicProgressRepository, times(1)).findByFaculty("Ingeniería");
        verify(scheduleChangeRequestRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Caso error - getRequestsByFaculty retorna lista vacía para facultad sin solicitudes")
    void testGetRequestsByFaculty_FacultadSinSolicitudes() {
        when(studentAcademicProgressRepository.findByFaculty("Artes"))
                .thenReturn(Collections.emptyList());
        when(scheduleChangeRequestRepository.findAll())
                .thenReturn(List.of(request1, request2));

        List<ScheduleChangeRequest> resultado = adminRequestService.getRequestsByFaculty("Artes");

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(studentAcademicProgressRepository, times(1)).findByFaculty("Artes");
        verify(scheduleChangeRequestRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Caso exitoso - respondToRequest aprueba solicitud correctamente")
    void testRespondToRequest_AprobacionExitoso() {
        when(scheduleChangeRequestRepository.findById("REQ001"))
                .thenReturn(Optional.of(request1));
        when(scheduleChangeRequestRepository.save(any(ScheduleChangeRequest.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ScheduleChangeRequest resultado = adminRequestService
                .respondToRequest("REQ001", RequestStatus.APPROVED, "Solicitud aprobada");

        assertNotNull(resultado);
        assertEquals(RequestStatus.APPROVED, resultado.getStatus());
        assertNotNull(resultado.getResolutionDate());
        assertFalse(resultado.getReviewHistory().isEmpty());
        assertEquals("ADMIN_SYSTEM", resultado.getReviewHistory().get(0).getUserId());
        assertEquals(UserRole.ADMINISTRATOR, resultado.getReviewHistory().get(0).getUserRole());

        verify(scheduleChangeRequestRepository, times(1)).findById("REQ001");
        verify(scheduleChangeRequestRepository, times(1)).save(request1);
    }

    @Test
    @DisplayName("Caso exitoso - respondToRequest rechaza solicitud correctamente")
    void testRespondToRequest_RechazoExitoso() {
        when(scheduleChangeRequestRepository.findById("REQ001"))
                .thenReturn(Optional.of(request1));
        when(scheduleChangeRequestRepository.save(any(ScheduleChangeRequest.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ScheduleChangeRequest resultado = adminRequestService
                .respondToRequest("REQ001", RequestStatus.REJECTED, "Solicitud rechazada");

        assertNotNull(resultado);
        assertEquals(RequestStatus.REJECTED, resultado.getStatus());
        assertNotNull(resultado.getResolutionDate());
        assertFalse(resultado.getReviewHistory().isEmpty());
        assertEquals("SOLICITUD_RECHAZADA", resultado.getReviewHistory().get(0).getAction());

        verify(scheduleChangeRequestRepository, times(1)).findById("REQ001");
        verify(scheduleChangeRequestRepository, times(1)).save(request1);
    }

    @Test
    @DisplayName("Caso error - respondToRequest con decisión inválida lanza excepción")
    void testRespondToRequest_DecisionInvalida() {
        when(scheduleChangeRequestRepository.findById("REQ001"))
                .thenReturn(Optional.of(request1));

        AppException exception = assertThrows(AppException.class, () -> {
            adminRequestService.respondToRequest("REQ001", RequestStatus.PENDING, "Comentario");
        });

        assertEquals("La decisión debe ser APPROVED o REJECTED", exception.getMessage());

        verify(scheduleChangeRequestRepository, times(1)).findById("REQ001");
        verify(scheduleChangeRequestRepository, never()).save(any());
    }

    @Test
    @DisplayName("Caso error - respondToRequest con solicitud no encontrada lanza excepción")
    void testRespondToRequest_SolicitudNoEncontrada() {
        when(scheduleChangeRequestRepository.findById("REQ999"))
                .thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            adminRequestService.respondToRequest("REQ999", RequestStatus.APPROVED, "Comentario");
        });

        assertEquals("Solicitud no encontrada: REQ999", exception.getMessage());

        verify(scheduleChangeRequestRepository, times(1)).findById("REQ999");
        verify(scheduleChangeRequestRepository, never()).save(any());
    }

    @Test
    @DisplayName("Caso exitoso - requestAdditionalInfo actualiza solicitud correctamente")
    void testRequestAdditionalInfo_Exitoso() {
        when(scheduleChangeRequestRepository.findById("REQ001"))
                .thenReturn(Optional.of(request1));
        when(scheduleChangeRequestRepository.save(any(ScheduleChangeRequest.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ScheduleChangeRequest resultado = adminRequestService
                .requestAdditionalInfo("REQ001", "Se requiere documentación médica");

        assertNotNull(resultado);
        assertEquals(RequestStatus.UNDER_REVIEW, resultado.getStatus());
        assertFalse(resultado.getReviewHistory().isEmpty());
        assertTrue(resultado.getReviewHistory().get(0).getComments()
                .contains("Se requiere información adicional: Se requiere documentación médica"));
        assertEquals("INFORMACION_ADICIONAL_SOLICITADA", resultado.getReviewHistory().get(0).getAction());

        verify(scheduleChangeRequestRepository, times(1)).findById("REQ001");
        verify(scheduleChangeRequestRepository, times(1)).save(request1);
    }


    @Test
    @DisplayName("Caso exitoso - approveSpecialCase aprueba caso especial correctamente")
    void testApproveSpecialCase_Exitoso() {
        when(scheduleChangeRequestRepository.findById("REQ001"))
                .thenReturn(Optional.of(request1));
        when(scheduleChangeRequestRepository.save(any(ScheduleChangeRequest.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ScheduleChangeRequest resultado = adminRequestService
                .approveSpecialCase("REQ001", "Caso médico comprobado");

        assertNotNull(resultado);
        assertEquals(RequestStatus.APPROVED, resultado.getStatus());
        assertNotNull(resultado.getResolutionDate());
        assertFalse(resultado.getReviewHistory().isEmpty());
        assertTrue(resultado.getReviewHistory().get(0).getComments()
                .contains("Aprobación especial: Caso médico comprobado"));
        assertEquals("CASO_ESPECIAL_APROBADO", resultado.getReviewHistory().get(0).getAction());

        // Verificar interacciones
        verify(scheduleChangeRequestRepository, times(1)).findById("REQ001");
        verify(scheduleChangeRequestRepository, times(1)).save(request1);
    }

    @Test
    @DisplayName("Caso exitoso - getSpecialCases identifica casos médicos correctamente")
    void testGetSpecialCases_CasoMedico() {
        ScheduleChangeRequest requestMedico = new ScheduleChangeRequest("REQ003", student1, group1, group2,
                "Problema médico urgente con certificado");

        when(scheduleChangeRequestRepository.findAll())
                .thenReturn(List.of(request1, request2, requestMedico));

        List<ScheduleChangeRequest> resultado = adminRequestService.getSpecialCases();

        assertNotNull(resultado);
        assertEquals(1, resultado.size()); // Solo la solicitud médica
        assertEquals("REQ003", resultado.get(0).getRequestId());
        assertTrue(resultado.get(0).getReason().toLowerCase().contains("médico"));

        verify(scheduleChangeRequestRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Caso exitoso - getRequestsByPriority ordena por prioridad correctamente")
    void testGetRequestsByPriority_Exitoso() {
        when(scheduleChangeRequestRepository.findByStatus(RequestStatus.PENDING))
                .thenReturn(List.of(request1));

        List<ScheduleChangeRequest> resultado = adminRequestService.getRequestsByPriority();

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals("REQ001", resultado.get(0).getRequestId());

        verify(scheduleChangeRequestRepository, times(1)).findByStatus(RequestStatus.PENDING);
    }

    @Test
    @DisplayName("Caso exitoso - getApprovalRateByFaculty calcula estadísticas correctamente")
    void testGetApprovalRateByFaculty_Exitoso() {
        when(studentAcademicProgressRepository.findByFaculty("Ingeniería"))
                .thenReturn(List.of(progress1));
        when(scheduleChangeRequestRepository.findAll())
                .thenReturn(List.of(request1, request2));

        AdminRequestService.ApprovalStats resultado =
                adminRequestService.getApprovalRateByFaculty("Ingeniería");

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalRequests());
        assertEquals(0, resultado.getApprovedRequests());
        assertEquals(0, resultado.getRejectedRequests());
        assertEquals(1, resultado.getPendingRequests());
        assertEquals(0.0, resultado.getApprovalRate());

        verify(studentAcademicProgressRepository, times(1)).findByFaculty("Ingeniería");
        verify(scheduleChangeRequestRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Caso exitoso - generateGlobalReport genera reporte completo")
    void testGenerateGlobalReport_Exitoso() {
        when(scheduleChangeRequestRepository.findAll())
                .thenReturn(List.of(request1, request2));

        AdminRequestService.GlobalStats resultado = adminRequestService.generateGlobalReport();

        assertNotNull(resultado);
        assertEquals(2, resultado.getTotalRequests());
        assertEquals(1, resultado.getTotalApproved());
        assertEquals(0, resultado.getTotalRejected());
        assertEquals(1, resultado.getTotalPending());
        assertEquals(50.0, resultado.getOverallApprovalRate()); // 1 de 2 = 50%
        assertNotNull(resultado.getRequestsByFaculty());
        assertNotNull(resultado.getRequestsByStatus());

        verify(scheduleChangeRequestRepository, times(2)).findAll(); // Llamado dos veces por getGlobalApprovalRate
    }




    @Test
    @DisplayName("Caso error - generateCourseReport con curso no encontrado lanza excepción")
    void testGenerateCourseReport_CursoNoEncontrado() {
        when(courseRepository.findById("UNKNOWN"))
                .thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            adminRequestService.generateCourseReport("UNKNOWN");
        });

        assertEquals("Curso no encontrado: UNKNOWN", exception.getMessage());

        verify(courseRepository, times(1)).findById("UNKNOWN");
        verify(groupRepository, never()).findByCourse_CourseCode(any());
    }



    @Test
    @DisplayName("Caso error - generateGroupReport con grupo no encontrado lanza excepción")
    void testGenerateGroupReport_GrupoNoEncontrado() {
        when(groupRepository.findById("GRP999"))
                .thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            adminRequestService.generateGroupReport("GRP999");
        });

        assertEquals("Grupo no encontrado: GRP999", exception.getMessage());

        verify(groupRepository, times(1)).findById("GRP999");
        verify(scheduleChangeRequestRepository, never()).findByRequestedGroupId(any());
        verify(scheduleChangeRequestRepository, never()).findByCurrentGroupId(any());
    }


    @Test
    @DisplayName("Caso exitoso - generateReassignmentReport genera estadísticas de reasignaciones")
    void testGenerateReassignmentReport_Exitoso() {
        when(scheduleChangeRequestRepository.findAll())
                .thenReturn(List.of(request1, request2));

        AdminRequestService.ReassignmentStats resultado = adminRequestService.generateReassignmentReport();

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalReassignments()); // Solo request2 está aprobada
        assertNotNull(resultado.getReassignmentsByFaculty());
        assertNotNull(resultado.getReassignmentsByCourse());
        assertTrue(resultado.getAverageProcessingTime() >= 0);

        verify(scheduleChangeRequestRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Caso exitoso - getGlobalApprovalRate calcula tasa global correctamente")
    void testGetGlobalApprovalRate_Exitoso() {
        when(scheduleChangeRequestRepository.findAll())
                .thenReturn(List.of(request1, request2));

        AdminRequestService.ApprovalStats resultado = adminRequestService.getGlobalApprovalRate();

        assertNotNull(resultado);
        assertEquals(2, resultado.getTotalRequests());
        assertEquals(1, resultado.getApprovedRequests());
        assertEquals(0, resultado.getRejectedRequests());
        assertEquals(1, resultado.getPendingRequests());
        assertEquals(50.0, resultado.getApprovalRate()); // 1 de 2 = 50%

        verify(scheduleChangeRequestRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Caso exitoso - getApprovalRateByCourse calcula tasa por curso")
    void testGetApprovalRateByCourse_Exitoso() {
        when(groupRepository.findByCourse_CourseCode("CS101"))
                .thenReturn(List.of(group1, group2));
        when(scheduleChangeRequestRepository.findAll())
                .thenReturn(List.of(request1, request2));

        AdminRequestService.ApprovalStats resultado = adminRequestService.getApprovalRateByCourse("CS101");

        assertNotNull(resultado);
        assertEquals(2, resultado.getTotalRequests()); // Ambas solicitudes son del curso CS101
        assertEquals(1, resultado.getApprovedRequests());
        assertEquals(0, resultado.getRejectedRequests());
        assertEquals(1, resultado.getPendingRequests());

        verify(groupRepository, times(1)).findByCourse_CourseCode("CS101");
        verify(scheduleChangeRequestRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Caso exitoso - getApprovalRateByGroup calcula tasa por grupo")
    void testGetApprovalRateByGroup_Exitoso() {
        when(scheduleChangeRequestRepository.findByCurrentGroupId("GRP001"))
                .thenReturn(List.of(request1));
        when(scheduleChangeRequestRepository.findByRequestedGroupId("GRP001"))
                .thenReturn(List.of(request2));

        AdminRequestService.ApprovalStats resultado = adminRequestService.getApprovalRateByGroup("GRP001");

        assertNotNull(resultado);
        assertEquals(2, resultado.getTotalRequests()); // request1 (current) + request2 (requested)
        assertEquals(1, resultado.getApprovedRequests()); // request2 está aprobada
        assertEquals(0, resultado.getRejectedRequests());
        assertEquals(1, resultado.getPendingRequests()); // request1 está pendiente

        verify(scheduleChangeRequestRepository, times(1)).findByCurrentGroupId("GRP001");
        verify(scheduleChangeRequestRepository, times(1)).findByRequestedGroupId("GRP001");
    }

    @Test
    @DisplayName("Caso borde - getGlobalApprovalRate con lista vacía")
    void testGetGlobalApprovalRate_ListaVacia() {
        when(scheduleChangeRequestRepository.findAll())
                .thenReturn(Collections.emptyList());

        AdminRequestService.ApprovalStats resultado = adminRequestService.getGlobalApprovalRate();

        assertNotNull(resultado);
        assertEquals(0, resultado.getTotalRequests());
        assertEquals(0, resultado.getApprovedRequests());
        assertEquals(0, resultado.getRejectedRequests());
        assertEquals(0, resultado.getPendingRequests());
        assertEquals(0.0, resultado.getApprovalRate());

        verify(scheduleChangeRequestRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Caso borde - requestAdditionalInfo con solicitud no encontrada")
    void testRequestAdditionalInfo_SolicitudNoEncontrada() {
        when(scheduleChangeRequestRepository.findById("REQ999"))
                .thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            adminRequestService.requestAdditionalInfo("REQ999", "Información requerida");
        });

        assertEquals("Solicitud no encontrada: REQ999", exception.getMessage());

        verify(scheduleChangeRequestRepository, times(1)).findById("REQ999");
        verify(scheduleChangeRequestRepository, never()).save(any());
    }
}
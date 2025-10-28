package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

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
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    private StudentAcademicProgressRepository studentAcademicProgressRepository;

    @InjectMocks
    private AdminRequestService adminRequestService;

    private ScheduleChangeRequest request1;
    private ScheduleChangeRequest request2;
    private Student student1;
    private Student student2;
    private StudentAcademicProgress progress1;
    private StudentAcademicProgress progress2;

    @BeforeEach
    void setUp() {
        student1 = new Student();
        student1.setId("STU001");
        student1.setName("Bruce Wayne");

        student2 = new Student();
        student2.setId("STU002");
        student2.setName("Clark Kent");

        progress1 = new StudentAcademicProgress();
        progress1.setStudent(student1);
        progress1.setFaculty("Ingeniería");

        progress2 = new StudentAcademicProgress();
        progress2.setStudent(student2);
        progress2.setFaculty("Ciencias");

        request1 = new ScheduleChangeRequest();
        request1.setRequestId("REQ001");
        request1.setStudent(student1);
        request1.setStatus(RequestStatus.PENDING);
        request1.setSubmissionDate(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(2)));
        request1.setReviewHistory(new ArrayList<>());

        request2 = new ScheduleChangeRequest();
        request2.setRequestId("REQ002");
        request2.setStudent(student2);
        request2.setStatus(RequestStatus.APPROVED);
        request2.setSubmissionDate(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)));
        request2.setReviewHistory(new ArrayList<>());
    }

    @Test
    @DisplayName("Caso exitoso - getRequestsByFaculty retorna solicitudes de la facultad")
    void testGetRequestsByFaculty_Exitoso() {
        String faculty = "Ingeniería";
        List<StudentAcademicProgress> facultyProgress = List.of(progress1);
        List<ScheduleChangeRequest> allRequests = List.of(request1, request2);

        when(studentAcademicProgressRepository.findByFaculty(faculty)).thenReturn(facultyProgress);
        when(scheduleChangeRequestRepository.findAll()).thenReturn(allRequests);

        List<ScheduleChangeRequest> resultado = adminRequestService.getRequestsByFaculty(faculty);

        assertAll("Verificar solicitudes por facultad",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("REQ001", resultado.get(0).getRequestId())
        );

        verify(studentAcademicProgressRepository, times(1)).findByFaculty(faculty);
        verify(scheduleChangeRequestRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Caso error - getRequestsByFaculty retorna lista vacía para facultad sin solicitudes")
    void testGetRequestsByFaculty_FacultadSinSolicitudes() {
        String faculty = "Artes";
        List<StudentAcademicProgress> facultyProgress = Collections.emptyList();

        when(studentAcademicProgressRepository.findByFaculty(faculty)).thenReturn(facultyProgress);

        List<ScheduleChangeRequest> resultado = adminRequestService.getRequestsByFaculty(faculty);

        assertAll("Verificar lista vacía para facultad sin solicitudes",
                () -> assertNotNull(resultado),
                () -> assertTrue(resultado.isEmpty())
        );

        verify(studentAcademicProgressRepository, times(1)).findByFaculty(faculty);
    }

    @Test
    @DisplayName("Caso exitoso - respondToRequest aprueba solicitud correctamente")
    void testRespondToRequest_AprobacionExitoso() {
        String requestId = "REQ001";
        RequestStatus decision = RequestStatus.APPROVED;
        String comments = "Solicitud aprobada por cumplir requisitos";

        when(scheduleChangeRequestRepository.findById(requestId)).thenReturn(Optional.of(request1));
        when(scheduleChangeRequestRepository.save(any(ScheduleChangeRequest.class))).thenReturn(request1);

        ScheduleChangeRequest resultado = adminRequestService.respondToRequest(requestId, decision, comments);

        assertAll("Verificar aprobación de solicitud",
                () -> assertNotNull(resultado),
                () -> assertEquals(RequestStatus.APPROVED, resultado.getStatus()),
                () -> assertNotNull(resultado.getResolutionDate()),
                () -> assertEquals(1, resultado.getReviewHistory().size())
        );

        verify(scheduleChangeRequestRepository, times(1)).findById(requestId);
        verify(scheduleChangeRequestRepository, times(1)).save(request1);
    }



    @Test
    @DisplayName("Caso error - respondToRequest lanza excepción cuando solicitud no existe")
    void testRespondToRequest_SolicitudNoEncontrada() {
        String requestId = "REQ_INEXISTENTE";
        RequestStatus decision = RequestStatus.APPROVED;
        String comments = "Comentarios";

        when(scheduleChangeRequestRepository.findById(requestId)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            adminRequestService.respondToRequest(requestId, decision, comments);
        });

        assertEquals("Solicitud no encontrada: " + requestId, exception.getMessage());
        verify(scheduleChangeRequestRepository, times(1)).findById(requestId);
    }

    @Test
    @DisplayName("Caso exitoso - requestAdditionalInfo actualiza solicitud correctamente")
    void testRequestAdditionalInfo_Exitoso() {
        String requestId = "REQ001";
        String comments = "Se requiere documentación adicional";

        when(scheduleChangeRequestRepository.findById(requestId)).thenReturn(Optional.of(request1));
        when(scheduleChangeRequestRepository.save(any(ScheduleChangeRequest.class))).thenReturn(request1);

        ScheduleChangeRequest resultado = adminRequestService.requestAdditionalInfo(requestId, comments);

        assertAll("Verificar solicitud de información adicional",
                () -> assertNotNull(resultado),
                () -> assertEquals(RequestStatus.UNDER_REVIEW, resultado.getStatus()),
                () -> assertEquals(1, resultado.getReviewHistory().size()),
                () -> assertTrue(resultado.getReviewHistory().get(0).getComments().contains(comments))
        );

        verify(scheduleChangeRequestRepository, times(1)).findById(requestId);
        verify(scheduleChangeRequestRepository, times(1)).save(request1);
    }



    @Test
    @DisplayName("Caso borde - getGlobalRequests retorna lista vacía cuando no hay solicitudes")
    void testGetGlobalRequests_ListaVacia() {
        when(scheduleChangeRequestRepository.findAll()).thenReturn(Collections.emptyList());

        List<ScheduleChangeRequest> resultado = adminRequestService.getGlobalRequests();

        assertAll("Verificar lista vacía",
                () -> assertNotNull(resultado),
                () -> assertTrue(resultado.isEmpty())
        );

        verify(scheduleChangeRequestRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Caso exitoso - approveSpecialCase aprueba caso especial correctamente")
    void testApproveSpecialCase_Exitoso() {
        String requestId = "REQ001";
        String comments = "Caso especial por situación médica";

        when(scheduleChangeRequestRepository.findById(requestId)).thenReturn(Optional.of(request1));
        when(scheduleChangeRequestRepository.save(any(ScheduleChangeRequest.class))).thenReturn(request1);

        ScheduleChangeRequest resultado = adminRequestService.approveSpecialCase(requestId, comments);

        assertAll("Verificar aprobación de caso especial",
                () -> assertNotNull(resultado),
                () -> assertEquals(RequestStatus.APPROVED, resultado.getStatus()),
                () -> assertNotNull(resultado.getResolutionDate()),
                () -> assertEquals(1, resultado.getReviewHistory().size()),
                () -> assertTrue(resultado.getReviewHistory().get(0).getAction().contains("CASO_ESPECIAL_APROBADO"))
        );

        verify(scheduleChangeRequestRepository, times(1)).findById(requestId);
        verify(scheduleChangeRequestRepository, times(1)).save(request1);
    }

    @Test
    @DisplayName("Caso exitoso - getSpecialCases identifica casos especiales correctamente")
    void testGetSpecialCases_Exitoso() {
        ScheduleChangeRequest specialRequest = new ScheduleChangeRequest();
        specialRequest.setRequestId("REQ_ESPECIAL");
        specialRequest.setStudent(student1);
        specialRequest.setStatus(RequestStatus.PENDING);
        specialRequest.setSubmissionDate(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(20)));
        specialRequest.setReviewHistory(new ArrayList<>());

        List<ScheduleChangeRequest> allRequests = List.of(request1, specialRequest);
        when(scheduleChangeRequestRepository.findAll()).thenReturn(allRequests);

        List<ScheduleChangeRequest> resultado = adminRequestService.getSpecialCases();

        assertAll("Verificar identificación de casos especiales",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("REQ_ESPECIAL", resultado.get(0).getRequestId())
        );

        verify(scheduleChangeRequestRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Caso exitoso - getApprovalRateByFaculty calcula estadísticas correctamente")
    void testGetApprovalRateByFaculty_Exitoso() {
        String faculty = "Ingeniería";
        List<StudentAcademicProgress> facultyProgress = List.of(progress1);
        List<ScheduleChangeRequest> allRequests = List.of(request1, request2);

        when(studentAcademicProgressRepository.findByFaculty(faculty)).thenReturn(facultyProgress);
        when(scheduleChangeRequestRepository.findAll()).thenReturn(allRequests);

        AdminRequestService.ApprovalStats resultado = adminRequestService.getApprovalRateByFaculty(faculty);

        assertAll("Verificar estadísticas de aprobación por facultad",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.getTotalRequests()),
                () -> assertEquals(0, resultado.getApprovedRequests()),
                () -> assertEquals(0, resultado.getRejectedRequests()),
                () -> assertEquals(1, resultado.getPendingRequests()),
                () -> assertEquals(0.0, resultado.getApprovalRate())
        );

        verify(studentAcademicProgressRepository, times(1)).findByFaculty(faculty);
        verify(scheduleChangeRequestRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Caso exitoso - getGlobalApprovalRate calcula estadísticas globales correctamente")
    void testGetGlobalApprovalRate_Exitoso() {
        List<ScheduleChangeRequest> allRequests = List.of(request1, request2);
        when(scheduleChangeRequestRepository.findAll()).thenReturn(allRequests);

        AdminRequestService.ApprovalStats resultado = adminRequestService.getGlobalApprovalRate();

        assertAll("Verificar estadísticas globales de aprobación",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.getTotalRequests()),
                () -> assertEquals(1, resultado.getApprovedRequests()),
                () -> assertEquals(0, resultado.getRejectedRequests()),
                () -> assertEquals(1, resultado.getPendingRequests()),
                () -> assertEquals(50.0, resultado.getApprovalRate())
        );

        verify(scheduleChangeRequestRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Caso exitoso - generateGlobalReport genera reporte completo")
    void testGenerateGlobalReport_Exitoso() {
        List<ScheduleChangeRequest> allRequests = List.of(request1, request2);
        when(scheduleChangeRequestRepository.findAll()).thenReturn(allRequests);

        AdminRequestService.GlobalStats resultado = adminRequestService.generateGlobalReport();

        assertAll("Verificar reporte global completo",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.getTotalRequests()),
                () -> assertEquals(1, resultado.getTotalApproved()),
                () -> assertEquals(0, resultado.getTotalRejected()),
                () -> assertEquals(1, resultado.getTotalPending()),
                () -> assertEquals(50.0, resultado.getOverallApprovalRate()),
                () -> assertNotNull(resultado.getRequestsByStatus())
        );

        verify(scheduleChangeRequestRepository, times(2)).findAll();
    }



    @Test
    @DisplayName("Caso borde - respondToRequest rechaza solicitud correctamente")
    void testRespondToRequest_RechazoExitoso() {
        String requestId = "REQ001";
        RequestStatus decision = RequestStatus.REJECTED;
        String comments = "Solicitud rechazada por falta de cupos";

        when(scheduleChangeRequestRepository.findById(requestId)).thenReturn(Optional.of(request1));
        when(scheduleChangeRequestRepository.save(any(ScheduleChangeRequest.class))).thenReturn(request1);

        ScheduleChangeRequest resultado = adminRequestService.respondToRequest(requestId, decision, comments);

        assertAll("Verificar rechazo de solicitud",
                () -> assertNotNull(resultado),
                () -> assertEquals(RequestStatus.REJECTED, resultado.getStatus()),
                () -> assertNotNull(resultado.getResolutionDate()),
                () -> assertEquals(1, resultado.getReviewHistory().size())
        );

        verify(scheduleChangeRequestRepository, times(1)).findById(requestId);
        verify(scheduleChangeRequestRepository, times(1)).save(request1);
    }

    @Test
    @DisplayName("Caso borde - getGlobalApprovalRate con lista vacía")
    void testGetGlobalApprovalRate_ListaVacia() {
        when(scheduleChangeRequestRepository.findAll()).thenReturn(Collections.emptyList());

        AdminRequestService.ApprovalStats resultado = adminRequestService.getGlobalApprovalRate();

        assertAll("Verificar estadísticas con lista vacía",
                () -> assertNotNull(resultado),
                () -> assertEquals(0, resultado.getTotalRequests()),
                () -> assertEquals(0, resultado.getApprovedRequests()),
                () -> assertEquals(0, resultado.getRejectedRequests()),
                () -> assertEquals(0, resultado.getPendingRequests()),
                () -> assertEquals(0.0, resultado.getApprovalRate())
        );

        verify(scheduleChangeRequestRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Caso borde - requestAdditionalInfo con solicitud no encontrada")
    void testRequestAdditionalInfo_SolicitudNoEncontrada() {
        String requestId = "REQ_INEXISTENTE";
        String comments = "Se requiere información adicional";

        when(scheduleChangeRequestRepository.findById(requestId)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            adminRequestService.requestAdditionalInfo(requestId, comments);
        });

        assertEquals("Solicitud no encontrada: " + requestId, exception.getMessage());
        verify(scheduleChangeRequestRepository, times(1)).findById(requestId);
    }

    @Test
    @DisplayName("Caso borde - approveSpecialCase con solicitud no encontrada")
    void testApproveSpecialCase_SolicitudNoEncontrada() {
        String requestId = "REQ_INEXISTENTE";
        String comments = "Aprobación especial";

        when(scheduleChangeRequestRepository.findById(requestId)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            adminRequestService.approveSpecialCase(requestId, comments);
        });

        assertEquals("Solicitud no encontrada: " + requestId, exception.getMessage());
        verify(scheduleChangeRequestRepository, times(1)).findById(requestId);
    }
}
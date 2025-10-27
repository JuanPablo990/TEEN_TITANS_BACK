package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.RequestStatus;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.StudentRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentRequestControllerTest {

    @Mock
    private StudentRequestService studentRequestService;

    @InjectMocks
    private StudentRequestController studentRequestController;

    private ScheduleChangeRequest scheduleChangeRequest;
    private Group group;
    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student("STU-123", "Juan Pérez", "juan@university.edu", "password", "Computer Science", 3);

        Course course = new Course("CS101", "Introduction to Programming", 4, "Basic programming concepts", "Computer Science", true);

        group = new Group("GRP-1", "A", course, null, null, null);

        scheduleChangeRequest = ScheduleChangeRequest.builder()
                .requestId("REQ-123")
                .student(student)
                .currentGroup(group)
                .requestedGroup(group)
                .reason("Conflictos de horario")
                .status(RequestStatus.PENDING)
                .submissionDate(new Date())
                .reviewHistory(new ArrayList<>())
                .build();
    }

    @Test
    @DisplayName("Caso error - getStudentApprovalRate lanza AppException")
    void testGetStudentApprovalRate_AppException() {
        when(studentRequestService.getStudentApprovalRate("STU-123"))
                .thenThrow(new AppException("Estudiante no encontrado"));

        ResponseEntity<?> response = studentRequestController.getStudentApprovalRate("STU-123");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Estudiante no encontrado", ((Map<?, ?>) response.getBody()).get("error"));
        verify(studentRequestService, times(1)).getStudentApprovalRate("STU-123");
    }

    @Test
    @DisplayName("Caso exitoso - getPendingRequestsStatus retorna solicitudes pendientes")
    void testGetPendingRequestsStatus_Exitoso() {
        List<ScheduleChangeRequest> pendingRequests = Arrays.asList(scheduleChangeRequest);
        when(studentRequestService.getPendingRequestsStatus("STU-123")).thenReturn(pendingRequests);

        ResponseEntity<?> response = studentRequestController.getPendingRequestsStatus("STU-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(Map.class, response.getBody());

        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertEquals("STU-123", responseBody.get("studentId"));
        assertEquals(1, responseBody.get("count"));
        verify(studentRequestService, times(1)).getPendingRequestsStatus("STU-123");
    }

    @Test
    @DisplayName("Caso borde - getPendingRequestsStatus retorna lista vacía")
    void testGetPendingRequestsStatus_ListaVacia() {
        when(studentRequestService.getPendingRequestsStatus("STU-123")).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = studentRequestController.getPendingRequestsStatus("STU-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertEquals(0, responseBody.get("count"));
        verify(studentRequestService, times(1)).getPendingRequestsStatus("STU-123");
    }

    @Test
    @DisplayName("Caso exitoso - getStudentRequestMetrics retorna métricas completas")
    void testGetStudentRequestMetrics_Exitoso() {
        List<ScheduleChangeRequest> pendingRequests = Arrays.asList(scheduleChangeRequest);
        when(studentRequestService.getStudentApprovalRate("STU-123")).thenReturn(80.0);
        when(studentRequestService.getPendingRequestsStatus("STU-123")).thenReturn(pendingRequests);

        ResponseEntity<?> response = studentRequestController.getStudentRequestMetrics("STU-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(Map.class, response.getBody());

        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertEquals("STU-123", responseBody.get("studentId"));
        assertEquals(80.0, responseBody.get("approvalRate"));
        assertEquals("EXCELENTE", responseBody.get("performanceRating"));
        verify(studentRequestService, times(1)).getStudentApprovalRate("STU-123");
        verify(studentRequestService, times(1)).getPendingRequestsStatus("STU-123");
    }

    @Test
    @DisplayName("Caso borde - getNewRequestEligibility con límite alcanzado")
    void testGetNewRequestEligibility_LimiteAlcanzado() {
        List<ScheduleChangeRequest> pendingRequests = Arrays.asList(
                scheduleChangeRequest, scheduleChangeRequest, scheduleChangeRequest, scheduleChangeRequest
        );
        List<String> requiredDocuments = Arrays.asList("Formulario de cambio de grupo", "Justificación escrita");

        when(studentRequestService.getPendingRequestsStatus("STU-123")).thenReturn(pendingRequests);
        when(studentRequestService.getRequiredDocuments("GROUP_CHANGE")).thenReturn(requiredDocuments);

        ResponseEntity<?> response = studentRequestController.getNewRequestEligibility("STU-123", "GROUP_CHANGE");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertEquals(false, responseBody.get("eligible"));
        assertEquals(false, responseBody.get("hasPendingLimit"));
        assertEquals(4, responseBody.get("currentPendingCount"));
        assertEquals(3, responseBody.get("maxPendingAllowed"));
        verify(studentRequestService, times(1)).getPendingRequestsStatus("STU-123");
        verify(studentRequestService, times(1)).getRequiredDocuments("GROUP_CHANGE");
    }

    @Test
    @DisplayName("Caso exitoso - getRequestPriorityPosition retorna posición")
    void testGetRequestPriorityPosition_Exitoso() {
        when(studentRequestService.getRequestPriorityPosition("REQ-123")).thenReturn(2);

        ResponseEntity<?> response = studentRequestController.getRequestPriorityPosition("REQ-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertEquals("REQ-123", responseBody.get("requestId"));
        assertEquals(2, responseBody.get("priorityPosition"));
        verify(studentRequestService, times(1)).getRequestPriorityPosition("REQ-123");
    }

    @Test
    @DisplayName("Caso exitoso - getRecommendedAlternativeGroups retorna grupos alternativos")
    void testGetRecommendedAlternativeGroups_Exitoso() {
        List<Group> alternativeGroups = Arrays.asList(group);
        when(studentRequestService.getRecommendedAlternativeGroups("REQ-123")).thenReturn(alternativeGroups);

        ResponseEntity<?> response = studentRequestController.getRecommendedAlternativeGroups("REQ-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertEquals("REQ-123", responseBody.get("requestId"));
        assertEquals(1, responseBody.get("count"));
        verify(studentRequestService, times(1)).getRecommendedAlternativeGroups("REQ-123");
    }

    @Test
    @DisplayName("Caso borde - getRecommendedAlternativeGroups retorna lista vacía")
    void testGetRecommendedAlternativeGroups_ListaVacia() {
        when(studentRequestService.getRecommendedAlternativeGroups("REQ-123")).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = studentRequestController.getRecommendedAlternativeGroups("REQ-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertEquals(0, responseBody.get("count"));
        assertEquals("No se encontraron grupos alternativos disponibles", responseBody.get("message"));
        verify(studentRequestService, times(1)).getRecommendedAlternativeGroups("REQ-123");
    }

    @Test
    @DisplayName("Caso exitoso - getRequestEstimatedWaitTime retorna tiempo estimado")
    void testGetRequestEstimatedWaitTime_Exitoso() {
        when(studentRequestService.getRequestEstimatedWaitTime("REQ-123")).thenReturn("1-3 días");

        ResponseEntity<?> response = studentRequestController.getRequestEstimatedWaitTime("REQ-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertEquals("REQ-123", responseBody.get("requestId"));
        assertEquals("1-3 días", responseBody.get("estimatedWaitTime"));
        verify(studentRequestService, times(1)).getRequestEstimatedWaitTime("REQ-123");
    }

    @Test
    @DisplayName("Caso exitoso - canCancelRequest retorna true cuando puede cancelar")
    void testCanCancelRequest_PuedeCancelar() {
        when(studentRequestService.canCancelRequest("REQ-123")).thenReturn(true);

        ResponseEntity<?> response = studentRequestController.canCancelRequest("REQ-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertEquals("REQ-123", responseBody.get("requestId"));
        assertEquals(true, responseBody.get("canCancel"));
        assertEquals("La solicitud puede ser cancelada", responseBody.get("message"));
        verify(studentRequestService, times(1)).canCancelRequest("REQ-123");
    }

    @Test
    @DisplayName("Caso borde - canCancelRequest retorna false cuando no puede cancelar")
    void testCanCancelRequest_NoPuedeCancelar() {
        when(studentRequestService.canCancelRequest("REQ-123")).thenReturn(false);

        ResponseEntity<?> response = studentRequestController.canCancelRequest("REQ-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertEquals(false, responseBody.get("canCancel"));
        assertEquals("La solicitud no puede ser cancelada en este momento", responseBody.get("message"));
        verify(studentRequestService, times(1)).canCancelRequest("REQ-123");
    }

    @Test
    @DisplayName("Caso exitoso - getRequestStatusDetails retorna detalles completos")
    void testGetRequestStatusDetails_Exitoso() {
        when(studentRequestService.getRequestPriorityPosition("REQ-123")).thenReturn(3);
        when(studentRequestService.getRequestEstimatedWaitTime("REQ-123")).thenReturn("3-7 días");
        when(studentRequestService.canCancelRequest("REQ-123")).thenReturn(true);
        when(studentRequestService.getRecommendedAlternativeGroups("REQ-123")).thenReturn(Arrays.asList(group));

        ResponseEntity<?> response = studentRequestController.getRequestStatusDetails("REQ-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertEquals("REQ-123", responseBody.get("requestId"));
        assertEquals(3, responseBody.get("priorityPosition"));
        assertEquals("3-7 días", responseBody.get("estimatedWaitTime"));
        assertEquals(true, responseBody.get("canCancel"));
        assertEquals(true, responseBody.get("alternativeGroupsAvailable"));
        verify(studentRequestService, times(1)).getRequestPriorityPosition("REQ-123");
        verify(studentRequestService, times(1)).getRequestEstimatedWaitTime("REQ-123");
        verify(studentRequestService, times(1)).canCancelRequest("REQ-123");
        verify(studentRequestService, times(1)).getRecommendedAlternativeGroups("REQ-123");
    }

    @Test
    @DisplayName("Caso exitoso - getRequiredDocuments retorna documentos requeridos")
    void testGetRequiredDocuments_Exitoso() {
        List<String> documents = Arrays.asList("Formulario de cambio de grupo", "Justificación escrita");
        when(studentRequestService.getRequiredDocuments("GROUP_CHANGE")).thenReturn(documents);

        ResponseEntity<?> response = studentRequestController.getRequiredDocuments("GROUP_CHANGE");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertEquals("GROUP_CHANGE", responseBody.get("requestType"));
        assertEquals(2, responseBody.get("count"));
        verify(studentRequestService, times(1)).getRequiredDocuments("GROUP_CHANGE");
    }

    @Test
    @DisplayName("Caso borde - getRequiredDocuments con tipo desconocido")
    void testGetRequiredDocuments_TipoDesconocido() {
        List<String> defaultDocuments = Arrays.asList("Formulario general de solicitud", "Documento de identificación");
        when(studentRequestService.getRequiredDocuments("UNKNOWN_TYPE")).thenReturn(defaultDocuments);

        ResponseEntity<?> response = studentRequestController.getRequiredDocuments("UNKNOWN_TYPE");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertEquals("UNKNOWN_TYPE", responseBody.get("requestType"));
        assertEquals(2, responseBody.get("count"));
        verify(studentRequestService, times(1)).getRequiredDocuments("UNKNOWN_TYPE");
    }

    @Test
    @DisplayName("Caso borde - getNewRequestEligibility con estudiante elegible")
    void testGetNewRequestEligibility_Elegible() {
        List<ScheduleChangeRequest> pendingRequests = Arrays.asList(scheduleChangeRequest);
        List<String> requiredDocuments = Arrays.asList("Formulario de cambio de grupo", "Justificación escrita");

        when(studentRequestService.getPendingRequestsStatus("STU-123")).thenReturn(pendingRequests);
        when(studentRequestService.getRequiredDocuments("GROUP_CHANGE")).thenReturn(requiredDocuments);

        ResponseEntity<?> response = studentRequestController.getNewRequestEligibility("STU-123", "GROUP_CHANGE");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertEquals(true, responseBody.get("eligible"));
        assertEquals(true, responseBody.get("hasPendingLimit"));
        assertEquals(1, responseBody.get("currentPendingCount"));
        assertEquals("Puede enviar una nueva solicitud", responseBody.get("message"));
        verify(studentRequestService, times(1)).getPendingRequestsStatus("STU-123");
        verify(studentRequestService, times(1)).getRequiredDocuments("GROUP_CHANGE");
    }

    @Test
    @DisplayName("Caso error - getPendingRequestsStatus lanza excepción general")
    void testGetPendingRequestsStatus_ExcepcionGeneral() {
        when(studentRequestService.getPendingRequestsStatus("STU-123"))
                .thenThrow(new RuntimeException("Error de base de datos"));

        ResponseEntity<?> response = studentRequestController.getPendingRequestsStatus("STU-123");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Error al obtener las solicitudes pendientes", ((Map<?, ?>) response.getBody()).get("error"));
        verify(studentRequestService, times(1)).getPendingRequestsStatus("STU-123");
    }

    @Test
    @DisplayName("Caso error - getRequestPriorityPosition lanza AppException")
    void testGetRequestPriorityPosition_AppException() {
        when(studentRequestService.getRequestPriorityPosition("REQ-123"))
                .thenThrow(new AppException("Solicitud no encontrada"));

        ResponseEntity<?> response = studentRequestController.getRequestPriorityPosition("REQ-123");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Solicitud no encontrada", ((Map<?, ?>) response.getBody()).get("error"));
        verify(studentRequestService, times(1)).getRequestPriorityPosition("REQ-123");
    }

    @Test
    @DisplayName("Caso borde - getStudentMetrics con aprobación baja")
    void testGetStudentMetrics_AprobacionBaja() {
        List<ScheduleChangeRequest> pendingRequests = Arrays.asList(scheduleChangeRequest);
        when(studentRequestService.getStudentApprovalRate("STU-123")).thenReturn(30.0);
        when(studentRequestService.getPendingRequestsStatus("STU-123")).thenReturn(pendingRequests);

        ResponseEntity<?> response = studentRequestController.getStudentRequestMetrics("STU-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertEquals("BAJO", responseBody.get("performanceRating"));
        verify(studentRequestService, times(1)).getStudentApprovalRate("STU-123");
        verify(studentRequestService, times(1)).getPendingRequestsStatus("STU-123");
    }

    @Test
    @DisplayName("Caso borde - getRequestStatusDetails sin grupos alternativos")
    void testGetRequestStatusDetails_SinGruposAlternativos() {
        when(studentRequestService.getRequestPriorityPosition("REQ-123")).thenReturn(1);
        when(studentRequestService.getRequestEstimatedWaitTime("REQ-123")).thenReturn("Menos de 1 día");
        when(studentRequestService.canCancelRequest("REQ-123")).thenReturn(false);
        when(studentRequestService.getRecommendedAlternativeGroups("REQ-123")).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = studentRequestController.getRequestStatusDetails("REQ-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertEquals(false, responseBody.get("alternativeGroupsAvailable"));
        assertEquals(0, responseBody.get("alternativeGroupsCount"));
        verify(studentRequestService, times(1)).getRecommendedAlternativeGroups("REQ-123");
    }
}
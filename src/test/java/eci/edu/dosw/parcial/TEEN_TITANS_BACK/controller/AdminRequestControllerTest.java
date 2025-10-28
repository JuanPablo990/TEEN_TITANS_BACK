package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.RequestStatus;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.ScheduleChangeRequest;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.AdminRequestService;
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
public class AdminRequestControllerTest {

    @Mock
    private AdminRequestService adminRequestService;

    @InjectMocks
    private AdminRequestController adminRequestController;

    private ScheduleChangeRequest request;
    private Map<String, String> requestBody;
    private AdminRequestService.ApprovalStats approvalStats;
    private AdminRequestService.FacultyStats facultyStats;
    private AdminRequestService.GlobalStats globalStats;

    @BeforeEach
    void setUp() {
        request = ScheduleChangeRequest.builder()
                .requestId("1")
                .reason("Conflictos de horario")
                .status(RequestStatus.PENDING)
                .submissionDate(new Date())
                .reviewHistory(new ArrayList<>())
                .build();

        requestBody = new HashMap<>();
        requestBody.put("decision", "APPROVED");
        requestBody.put("comments", "Solicitud aprobada");

        approvalStats = new AdminRequestService.ApprovalStats();
        approvalStats.setTotalRequests(10);
        approvalStats.setApprovedRequests(7);
        approvalStats.setRejectedRequests(2);
        approvalStats.setPendingRequests(1);
        approvalStats.setApprovalRate(70.0);

        facultyStats = new AdminRequestService.FacultyStats();
        facultyStats.setFaculty("Engineering");
        facultyStats.setTotalStudents(150);
        facultyStats.setTotalRequests(25);
        facultyStats.setApprovalRate(68.5);

        globalStats = new AdminRequestService.GlobalStats();
        globalStats.setTotalRequests(100);
        globalStats.setTotalApproved(65);
        globalStats.setTotalRejected(25);
        globalStats.setTotalPending(10);
        globalStats.setOverallApprovalRate(65.0);
        globalStats.setRequestsByStatus(Map.of("APPROVED", 65, "REJECTED", 25, "PENDING", 10));
    }

    @Test
    @DisplayName("Caso exitoso - getGlobalRequests retorna lista de solicitudes")
    void testGetGlobalRequests_Exitoso() {
        List<ScheduleChangeRequest> requests = Arrays.asList(request);
        when(adminRequestService.getGlobalRequests()).thenReturn(requests);

        ResponseEntity<?> response = adminRequestController.getGlobalRequests();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(List.class, response.getBody());
        assertEquals(1, ((List<?>) response.getBody()).size());
        verify(adminRequestService, times(1)).getGlobalRequests();
    }

    @Test
    @DisplayName("Caso error - getGlobalRequests lanza excepción general")
    void testGetGlobalRequests_ExcepcionGeneral() {
        when(adminRequestService.getGlobalRequests()).thenThrow(new RuntimeException("Error de base de datos"));

        ResponseEntity<?> response = adminRequestController.getGlobalRequests();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error interno del servidor al obtener las solicitudes globales", response.getBody());
        verify(adminRequestService, times(1)).getGlobalRequests();
    }

    @Test
    @DisplayName("Caso exitoso - getRequestsByFaculty retorna solicitudes por facultad")
    void testGetRequestsByFaculty_Exitoso() {
        List<ScheduleChangeRequest> requests = Arrays.asList(request);
        when(adminRequestService.getRequestsByFaculty("Engineering")).thenReturn(requests);

        ResponseEntity<?> response = adminRequestController.getRequestsByFaculty("Engineering");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(List.class, response.getBody());
        assertEquals(1, ((List<?>) response.getBody()).size());
        verify(adminRequestService, times(1)).getRequestsByFaculty("Engineering");
    }

    @Test
    @DisplayName("Caso exitoso - getSpecialCases retorna casos especiales")
    void testGetSpecialCases_Exitoso() {
        List<ScheduleChangeRequest> specialCases = Arrays.asList(request);
        when(adminRequestService.getSpecialCases()).thenReturn(specialCases);

        ResponseEntity<?> response = adminRequestController.getSpecialCases();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(List.class, response.getBody());
        assertEquals(1, ((List<?>) response.getBody()).size());
        verify(adminRequestService, times(1)).getSpecialCases();
    }

    @Test
    @DisplayName("Caso exitoso - respondToRequest aprueba solicitud")
    void testRespondToRequest_Aprobado_Exitoso() {
        requestBody.put("decision", "APPROVED");
        requestBody.put("comments", "Solicitud aprobada");
        when(adminRequestService.respondToRequest("1", RequestStatus.APPROVED, "Solicitud aprobada")).thenReturn(request);

        ResponseEntity<?> response = adminRequestController.respondToRequest("1", requestBody);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(ScheduleChangeRequest.class, response.getBody());
        verify(adminRequestService, times(1)).respondToRequest("1", RequestStatus.APPROVED, "Solicitud aprobada");
    }

    @Test
    @DisplayName("Caso exitoso - respondToRequest rechaza solicitud")
    void testRespondToRequest_Rechazado_Exitoso() {
        requestBody.put("decision", "REJECTED");
        requestBody.put("comments", "Solicitud rechazada");
        when(adminRequestService.respondToRequest("1", RequestStatus.REJECTED, "Solicitud rechazada")).thenReturn(request);

        ResponseEntity<?> response = adminRequestController.respondToRequest("1", requestBody);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(adminRequestService, times(1)).respondToRequest("1", RequestStatus.REJECTED, "Solicitud rechazada");
    }

    @Test
    @DisplayName("Caso error - respondToRequest con decision nula")
    void testRespondToRequest_DecisionNula() {
        requestBody.remove("decision");

        ResponseEntity<?> response = adminRequestController.respondToRequest("1", requestBody);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El campo 'decision' es requerido", response.getBody());
        verify(adminRequestService, never()).respondToRequest(any(), any(), any());
    }

    @Test
    @DisplayName("Caso error - respondToRequest con decision inválida")
    void testRespondToRequest_DecisionInvalida() {
        requestBody.put("decision", "INVALID_STATUS");

        ResponseEntity<?> response = adminRequestController.respondToRequest("1", requestBody);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Decisión no válida. Debe ser APPROVED o REJECTED", response.getBody());
        verify(adminRequestService, never()).respondToRequest(any(), any(), any());
    }

    @Test
    @DisplayName("Caso error - respondToRequest lanza AppException")
    void testRespondToRequest_AppException() {
        requestBody.put("decision", "APPROVED");
        when(adminRequestService.respondToRequest("1", RequestStatus.APPROVED, "Solicitud aprobada"))
                .thenThrow(new AppException("Solicitud no encontrada"));

        ResponseEntity<?> response = adminRequestController.respondToRequest("1", requestBody);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Solicitud no encontrada", response.getBody());
        verify(adminRequestService, times(1)).respondToRequest("1", RequestStatus.APPROVED, "Solicitud aprobada");
    }

    @Test
    @DisplayName("Caso exitoso - requestAdditionalInfo solicita información adicional")
    void testRequestAdditionalInfo_Exitoso() {
        requestBody.put("comments", "Se requiere documentación adicional");
        when(adminRequestService.requestAdditionalInfo("1", "Se requiere documentación adicional")).thenReturn(request);

        ResponseEntity<?> response = adminRequestController.requestAdditionalInfo("1", requestBody);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(adminRequestService, times(1)).requestAdditionalInfo("1", "Se requiere documentación adicional");
    }

    @Test
    @DisplayName("Caso error - requestAdditionalInfo con comments nulo")
    void testRequestAdditionalInfo_CommentsNulo() {
        requestBody.remove("comments");

        ResponseEntity<?> response = adminRequestController.requestAdditionalInfo("1", requestBody);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El campo 'comments' es requerido", response.getBody());
        verify(adminRequestService, never()).requestAdditionalInfo(any(), any());
    }

    @Test
    @DisplayName("Caso exitoso - approveSpecialCase aprueba caso especial")
    void testApproveSpecialCase_Exitoso() {
        requestBody.put("comments", "Caso especial aprobado");
        when(adminRequestService.approveSpecialCase("1", "Caso especial aprobado")).thenReturn(request);

        ResponseEntity<?> response = adminRequestController.approveSpecialCase("1", requestBody);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(adminRequestService, times(1)).approveSpecialCase("1", "Caso especial aprobado");
    }

    @Test
    @DisplayName("Caso exitoso - getApprovalRateByFaculty retorna estadísticas de facultad")
    void testGetApprovalRateByFaculty_Exitoso() {
        when(adminRequestService.getApprovalRateByFaculty("Engineering")).thenReturn(approvalStats);

        ResponseEntity<?> response = adminRequestController.getApprovalRateByFaculty("Engineering");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(AdminRequestService.ApprovalStats.class, response.getBody());
        verify(adminRequestService, times(1)).getApprovalRateByFaculty("Engineering");
    }

    @Test
    @DisplayName("Caso exitoso - getGlobalApprovalRate retorna estadísticas globales")
    void testGetGlobalApprovalRate_Exitoso() {
        when(adminRequestService.getGlobalApprovalRate()).thenReturn(approvalStats);

        ResponseEntity<?> response = adminRequestController.getGlobalApprovalRate();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(AdminRequestService.ApprovalStats.class, response.getBody());
        verify(adminRequestService, times(1)).getGlobalApprovalRate();
    }

    @Test
    @DisplayName("Caso exitoso - generateFacultyReport genera reporte de facultad")
    void testGenerateFacultyReport_Exitoso() {
        when(adminRequestService.generateFacultyReport("Engineering")).thenReturn(facultyStats);

        ResponseEntity<?> response = adminRequestController.generateFacultyReport("Engineering");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(AdminRequestService.FacultyStats.class, response.getBody());
        verify(adminRequestService, times(1)).generateFacultyReport("Engineering");
    }

    @Test
    @DisplayName("Caso exitoso - generateGlobalReport genera reporte global")
    void testGenerateGlobalReport_Exitoso() {
        when(adminRequestService.generateGlobalReport()).thenReturn(globalStats);

        ResponseEntity<?> response = adminRequestController.generateGlobalReport();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(AdminRequestService.GlobalStats.class, response.getBody());
        verify(adminRequestService, times(1)).generateGlobalReport();
    }

    @Test
    @DisplayName("Caso borde - healthCheck retorna estado correcto")
    void testHealthCheck_Exitoso() {
        ResponseEntity<String> response = adminRequestController.healthCheck();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("AdminRequestController is working properly", response.getBody());
    }

    @Test
    @DisplayName("Caso borde - parseRequestStatus con valor inválido retorna null")
    void testParseRequestStatus_ValorInvalido() {
        RequestStatus result = adminRequestController.parseRequestStatus("INVALID_STATUS");

        assertNull(result);
    }

    @Test
    @DisplayName("Caso borde - parseRequestStatus con valor válido en minúsculas")
    void testParseRequestStatus_ValorValidoMinusculas() {
        RequestStatus result = adminRequestController.parseRequestStatus("approved");

        assertEquals(RequestStatus.APPROVED, result);
    }

    @Test
    @DisplayName("Caso borde - getRequestsByFaculty retorna lista vacía")
    void testGetRequestsByFaculty_ListaVacia() {
        when(adminRequestService.getRequestsByFaculty("Arts")).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = adminRequestController.getRequestsByFaculty("Arts");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(List.class, response.getBody());
        assertTrue(((List<?>) response.getBody()).isEmpty());
        verify(adminRequestService, times(1)).getRequestsByFaculty("Arts");
    }

    @Test
    @DisplayName("Caso error - requestAdditionalInfo lanza AppException")
    void testRequestAdditionalInfo_AppException() {
        requestBody.put("comments", "Se requiere información");
        when(adminRequestService.requestAdditionalInfo("1", "Se requiere información"))
                .thenThrow(new AppException("Solicitud no encontrada"));

        ResponseEntity<?> response = adminRequestController.requestAdditionalInfo("1", requestBody);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Solicitud no encontrada", response.getBody());
        verify(adminRequestService, times(1)).requestAdditionalInfo("1", "Se requiere información");
    }

    @Test
    @DisplayName("Caso error - approveSpecialCase lanza AppException")
    void testApproveSpecialCase_AppException() {
        requestBody.put("comments", "Caso especial");
        when(adminRequestService.approveSpecialCase("1", "Caso especial"))
                .thenThrow(new AppException("Caso especial no encontrado"));

        ResponseEntity<?> response = adminRequestController.approveSpecialCase("1", requestBody);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Caso especial no encontrado", response.getBody());
        verify(adminRequestService, times(1)).approveSpecialCase("1", "Caso especial");
    }
}
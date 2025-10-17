package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.RequestStatus;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.ScheduleChangeRequest;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.AdminRequestService;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminRequestControllerTest {

    @Mock
    private AdminRequestService adminRequestService;

    @InjectMocks
    private AdminRequestController adminRequestController;

    private ScheduleChangeRequest testRequest;

    @BeforeEach
    void setUp() {
        testRequest = new ScheduleChangeRequest();
        testRequest.setRequestId("REQ001");
        testRequest.setStatus(RequestStatus.PENDING);
    }

    @Test
    @DisplayName("Caso exitoso - Obtener todas las solicitudes globales")
    void testGetGlobalRequests_Exitoso() {
        List<ScheduleChangeRequest> requests = List.of(testRequest);
        when(adminRequestService.getGlobalRequests()).thenReturn(requests);

        ResponseEntity<?> response = adminRequestController.getGlobalRequests();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(requests, response.getBody());
        verify(adminRequestService, times(1)).getGlobalRequests();
    }

    @Test
    @DisplayName("Caso error - Obtener todas las solicitudes globales con excepción")
    void testGetGlobalRequests_Excepcion() {
        when(adminRequestService.getGlobalRequests()).thenThrow(new RuntimeException("Error de base de datos"));

        ResponseEntity<?> response = adminRequestController.getGlobalRequests();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error interno del servidor al obtener las solicitudes globales", response.getBody());
    }

    @Test
    @DisplayName("Caso exitoso - Obtener solicitudes por facultad")
    void testGetRequestsByFaculty_Exitoso() {
        String faculty = "Ingeniería";
        List<ScheduleChangeRequest> requests = List.of(testRequest);
        when(adminRequestService.getRequestsByFaculty(faculty)).thenReturn(requests);

        ResponseEntity<?> response = adminRequestController.getRequestsByFaculty(faculty);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(requests, response.getBody());
        verify(adminRequestService, times(1)).getRequestsByFaculty(faculty);
    }

    @Test
    @DisplayName("Caso exitoso - Responder a solicitud con aprobación")
    void testRespondToRequest_AprobacionExitoso() {
        String requestId = "REQ001";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("decision", "APPROVED");
        requestBody.put("comments", "Solicitud aprobada");

        when(adminRequestService.respondToRequest(requestId, RequestStatus.APPROVED, "Solicitud aprobada"))
                .thenReturn(testRequest);

        ResponseEntity<?> response = adminRequestController.respondToRequest(requestId, requestBody);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testRequest, response.getBody());
        verify(adminRequestService, times(1)).respondToRequest(requestId, RequestStatus.APPROVED, "Solicitud aprobada");
    }

    @Test
    @DisplayName("Caso exitoso - Responder a solicitud con rechazo")
    void testRespondToRequest_RechazoExitoso() {
        String requestId = "REQ001";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("decision", "REJECTED");
        requestBody.put("comments", "Solicitud rechazada");

        when(adminRequestService.respondToRequest(requestId, RequestStatus.REJECTED, "Solicitud rechazada"))
                .thenReturn(testRequest);

        ResponseEntity<?> response = adminRequestController.respondToRequest(requestId, requestBody);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testRequest, response.getBody());
        verify(adminRequestService, times(1)).respondToRequest(requestId, RequestStatus.REJECTED, "Solicitud rechazada");
    }

    @Test
    @DisplayName("Caso error - Responder a solicitud sin campo decision")
    void testRespondToRequest_SinDecision() {
        String requestId = "REQ001";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("comments", "Comentarios");

        ResponseEntity<?> response = adminRequestController.respondToRequest(requestId, requestBody);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El campo 'decision' es requerido", response.getBody());
        verify(adminRequestService, never()).respondToRequest(anyString(), any(), anyString());
    }

    @Test
    @DisplayName("Caso error - Responder a solicitud con decision inválida")
    void testRespondToRequest_DecisionInvalida() {
        String requestId = "REQ001";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("decision", "INVALIDO");
        requestBody.put("comments", "Comentarios");

        ResponseEntity<?> response = adminRequestController.respondToRequest(requestId, requestBody);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Decisión no válida. Debe ser APPROVED o REJECTED", response.getBody());
        verify(adminRequestService, never()).respondToRequest(anyString(), any(), anyString());
    }

    @Test
    @DisplayName("Caso error - Responder a solicitud no encontrada")
    void testRespondToRequest_SolicitudNoEncontrada() {
        String requestId = "REQ999";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("decision", "APPROVED");
        requestBody.put("comments", "Comentarios");

        when(adminRequestService.respondToRequest(requestId, RequestStatus.APPROVED, "Comentarios"))
                .thenThrow(new AppException("Solicitud no encontrada"));

        ResponseEntity<?> response = adminRequestController.respondToRequest(requestId, requestBody);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Solicitud no encontrada", response.getBody());
    }

    @Test
    @DisplayName("Caso exitoso - Solicitar información adicional")
    void testRequestAdditionalInfo_Exitoso() {
        String requestId = "REQ001";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("comments", "Se requiere documentación adicional");

        when(adminRequestService.requestAdditionalInfo(requestId, "Se requiere documentación adicional"))
                .thenReturn(testRequest);

        ResponseEntity<?> response = adminRequestController.requestAdditionalInfo(requestId, requestBody);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testRequest, response.getBody());
        verify(adminRequestService, times(1)).requestAdditionalInfo(requestId, "Se requiere documentación adicional");
    }

    @Test
    @DisplayName("Caso error - Solicitar información adicional sin comentarios")
    void testRequestAdditionalInfo_SinComentarios() {
        String requestId = "REQ001";
        Map<String, String> requestBody = new HashMap<>();

        ResponseEntity<?> response = adminRequestController.requestAdditionalInfo(requestId, requestBody);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El campo 'comments' es requerido", response.getBody());
        verify(adminRequestService, never()).requestAdditionalInfo(anyString(), anyString());
    }

    @Test
    @DisplayName("Caso exitoso - Aprobar caso especial")
    void testApproveSpecialCase_Exitoso() {
        String requestId = "REQ001";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("comments", "Caso especial aprobado");

        when(adminRequestService.approveSpecialCase(requestId, "Caso especial aprobado"))
                .thenReturn(testRequest);

        ResponseEntity<?> response = adminRequestController.approveSpecialCase(requestId, requestBody);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testRequest, response.getBody());
        verify(adminRequestService, times(1)).approveSpecialCase(requestId, "Caso especial aprobado");
    }

    @Test
    @DisplayName("Caso exitoso - Obtener casos especiales")
    void testGetSpecialCases_Exitoso() {
        List<ScheduleChangeRequest> specialCases = List.of(testRequest);
        when(adminRequestService.getSpecialCases()).thenReturn(specialCases);

        ResponseEntity<?> response = adminRequestController.getSpecialCases();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(specialCases, response.getBody());
        verify(adminRequestService, times(1)).getSpecialCases();
    }

    @Test
    @DisplayName("Caso exitoso - Obtener solicitudes por prioridad")
    void testGetRequestsByPriority_Exitoso() {
        List<ScheduleChangeRequest> prioritizedRequests = List.of(testRequest);
        when(adminRequestService.getRequestsByPriority()).thenReturn(prioritizedRequests);

        ResponseEntity<?> response = adminRequestController.getRequestsByPriority();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(prioritizedRequests, response.getBody());
        verify(adminRequestService, times(1)).getRequestsByPriority();
    }

    @Test
    @DisplayName("Caso exitoso - Obtener estadísticas de aprobación por facultad")
    void testGetApprovalRateByFaculty_Exitoso() {
        String faculty = "Ingeniería";
        AdminRequestService.ApprovalStats stats = new AdminRequestService.ApprovalStats();
        stats.setTotalRequests(10);
        stats.setApprovalRate(80.0);

        when(adminRequestService.getApprovalRateByFaculty(faculty)).thenReturn(stats);

        ResponseEntity<?> response = adminRequestController.getApprovalRateByFaculty(faculty);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(stats, response.getBody());
        verify(adminRequestService, times(1)).getApprovalRateByFaculty(faculty);
    }

    @Test
    @DisplayName("Caso exitoso - Obtener estadísticas de aprobación por curso")
    void testGetApprovalRateByCourse_Exitoso() {
        String courseCode = "CS101";
        AdminRequestService.ApprovalStats stats = new AdminRequestService.ApprovalStats();
        stats.setTotalRequests(5);
        stats.setApprovalRate(60.0);

        when(adminRequestService.getApprovalRateByCourse(courseCode)).thenReturn(stats);

        ResponseEntity<?> response = adminRequestController.getApprovalRateByCourse(courseCode);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(stats, response.getBody());
        verify(adminRequestService, times(1)).getApprovalRateByCourse(courseCode);
    }

    @Test
    @DisplayName("Caso exitoso - Obtener estadísticas de aprobación por grupo")
    void testGetApprovalRateByGroup_Exitoso() {
        String groupId = "G001";
        AdminRequestService.ApprovalStats stats = new AdminRequestService.ApprovalStats();
        stats.setTotalRequests(3);
        stats.setApprovalRate(66.7);

        when(adminRequestService.getApprovalRateByGroup(groupId)).thenReturn(stats);

        ResponseEntity<?> response = adminRequestController.getApprovalRateByGroup(groupId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(stats, response.getBody());
        verify(adminRequestService, times(1)).getApprovalRateByGroup(groupId);
    }

    @Test
    @DisplayName("Caso exitoso - Obtener estadísticas globales de aprobación")
    void testGetGlobalApprovalRate_Exitoso() {
        AdminRequestService.ApprovalStats stats = new AdminRequestService.ApprovalStats();
        stats.setTotalRequests(100);
        stats.setApprovalRate(75.5);

        when(adminRequestService.getGlobalApprovalRate()).thenReturn(stats);

        ResponseEntity<?> response = adminRequestController.getGlobalApprovalRate();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(stats, response.getBody());
        verify(adminRequestService, times(1)).getGlobalApprovalRate();
    }

    @Test
    @DisplayName("Caso exitoso - Generar reporte de reasignaciones")
    void testGenerateReassignmentReport_Exitoso() {
        AdminRequestService.ReassignmentStats stats = new AdminRequestService.ReassignmentStats();
        stats.setTotalReassignments(50);

        when(adminRequestService.generateReassignmentReport()).thenReturn(stats);

        ResponseEntity<?> response = adminRequestController.generateReassignmentReport();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(stats, response.getBody());
        verify(adminRequestService, times(1)).generateReassignmentReport();
    }

    @Test
    @DisplayName("Caso exitoso - Generar reporte de curso")
    void testGenerateCourseReport_Exitoso() {
        String courseCode = "CS101";
        AdminRequestService.CourseStats stats = new AdminRequestService.CourseStats();
        stats.setCourseCode(courseCode);
        stats.setCourseName("Programación");

        when(adminRequestService.generateCourseReport(courseCode)).thenReturn(stats);

        ResponseEntity<?> response = adminRequestController.generateCourseReport(courseCode);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(stats, response.getBody());
        verify(adminRequestService, times(1)).generateCourseReport(courseCode);
    }

    @Test
    @DisplayName("Caso error - Generar reporte de curso no encontrado")
    void testGenerateCourseReport_CursoNoEncontrado() {
        String courseCode = "CS999";

        when(adminRequestService.generateCourseReport(courseCode))
                .thenThrow(new AppException("Curso no encontrado"));

        ResponseEntity<?> response = adminRequestController.generateCourseReport(courseCode);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Curso no encontrado", response.getBody());
    }

    @Test
    @DisplayName("Caso exitoso - Generar reporte de grupo")
    void testGenerateGroupReport_Exitoso() {
        String groupId = "G001";
        AdminRequestService.GroupStats stats = new AdminRequestService.GroupStats();
        stats.setGroupId(groupId);
        stats.setCapacity(30);

        when(adminRequestService.generateGroupReport(groupId)).thenReturn(stats);

        ResponseEntity<?> response = adminRequestController.generateGroupReport(groupId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(stats, response.getBody());
        verify(adminRequestService, times(1)).generateGroupReport(groupId);
    }

    @Test
    @DisplayName("Caso error - Generar reporte de grupo no encontrado")
    void testGenerateGroupReport_GrupoNoEncontrado() {
        String groupId = "G999";

        when(adminRequestService.generateGroupReport(groupId))
                .thenThrow(new AppException("Grupo no encontrado"));

        ResponseEntity<?> response = adminRequestController.generateGroupReport(groupId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Grupo no encontrado", response.getBody());
    }

    @Test
    @DisplayName("Caso exitoso - Generar reporte de facultad")
    void testGenerateFacultyReport_Exitoso() {
        String faculty = "Ingeniería";
        AdminRequestService.FacultyStats stats = new AdminRequestService.FacultyStats();
        stats.setFaculty(faculty);
        stats.setTotalStudents(500);

        when(adminRequestService.generateFacultyReport(faculty)).thenReturn(stats);

        ResponseEntity<?> response = adminRequestController.generateFacultyReport(faculty);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(stats, response.getBody());
        verify(adminRequestService, times(1)).generateFacultyReport(faculty);
    }

    @Test
    @DisplayName("Caso exitoso - Generar reporte global")
    void testGenerateGlobalReport_Exitoso() {
        AdminRequestService.GlobalStats stats = new AdminRequestService.GlobalStats();
        stats.setTotalRequests(1000);
        stats.setOverallApprovalRate(72.5);

        when(adminRequestService.generateGlobalReport()).thenReturn(stats);

        ResponseEntity<?> response = adminRequestController.generateGlobalReport();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(stats, response.getBody());
        verify(adminRequestService, times(1)).generateGlobalReport();
    }

    @Test
    @DisplayName("Caso exitoso - Health check")
    void testHealthCheck_Exitoso() {
        ResponseEntity<String> response = adminRequestController.healthCheck();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("AdminRequestController is working properly", response.getBody());
    }

    @Test
    @DisplayName("Caso borde - Responder a solicitud con decision en minúsculas")
    void testRespondToRequest_DecisionMinusculas() {
        String requestId = "REQ001";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("decision", "approved");
        requestBody.put("comments", "Comentarios");

        when(adminRequestService.respondToRequest(requestId, RequestStatus.APPROVED, "Comentarios"))
                .thenReturn(testRequest);

        ResponseEntity<?> response = adminRequestController.respondToRequest(requestId, requestBody);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testRequest, response.getBody());
        verify(adminRequestService, times(1)).respondToRequest(requestId, RequestStatus.APPROVED, "Comentarios");
    }

    @Test
    @DisplayName("Caso borde - Solicitar información adicional con comentarios vacíos")
    void testRequestAdditionalInfo_ComentariosVacios() {
        String requestId = "REQ001";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("comments", "");

        when(adminRequestService.requestAdditionalInfo(requestId, ""))
                .thenReturn(testRequest);

        ResponseEntity<?> response = adminRequestController.requestAdditionalInfo(requestId, requestBody);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testRequest, response.getBody());
        verify(adminRequestService, times(1)).requestAdditionalInfo(requestId, "");
    }
}
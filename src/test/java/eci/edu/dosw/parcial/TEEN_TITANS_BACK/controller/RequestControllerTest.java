package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.RequestStatus;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.ReviewStep;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.ScheduleChangeRequest;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.RequestService;
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
public class RequestControllerTest {

    @Mock
    private RequestService requestService;

    @InjectMocks
    private RequestController requestController;

    private ScheduleChangeRequest scheduleChangeRequest;
    private ReviewStep reviewStep;
    private RequestController.GroupChangeRequest groupChangeRequest;
    private RequestController.CourseChangeRequest courseChangeRequest;
    private RequestController.ReviewActionRequest reviewActionRequest;
    private RequestController.CancelRequest cancelRequest;

    @BeforeEach
    void setUp() {
        scheduleChangeRequest = ScheduleChangeRequest.builder()
                .requestId("REQ-123")
                .reason("Conflictos de horario")
                .status(RequestStatus.PENDING)
                .submissionDate(new Date())
                .reviewHistory(new ArrayList<>())
                .build();

        reviewStep = ReviewStep.builder()
                .userId("USER-123")
                .userRole(UserRole.ADMINISTRATOR)
                .action("SOLICITUD_APROBADA")
                .comments("Solicitud aprobada")
                .timestamp(new Date())
                .build();

        groupChangeRequest = new RequestController.GroupChangeRequest();
        groupChangeRequest.setStudentId("STU-123");
        groupChangeRequest.setCurrentGroupId("GRP-1");
        groupChangeRequest.setRequestedGroupId("GRP-2");
        groupChangeRequest.setReason("Conflictos de horario");

        courseChangeRequest = new RequestController.CourseChangeRequest();
        courseChangeRequest.setStudentId("STU-123");
        courseChangeRequest.setCurrentCourseCode("MATH101");
        courseChangeRequest.setRequestedCourseCode("PHYS101");
        courseChangeRequest.setReason("Cambio de carrera");

        reviewActionRequest = new RequestController.ReviewActionRequest();
        reviewActionRequest.setReviewerId("ADMIN-123");
        reviewActionRequest.setReviewerRole(UserRole.ADMINISTRATOR);
        reviewActionRequest.setComments("Solicitud aprobada");

        cancelRequest = new RequestController.CancelRequest();
        cancelRequest.setStudentId("STU-123");
    }

    @Test
    @DisplayName("Caso exitoso - createGroupChangeRequest crea solicitud exitosamente")
    void testCreateGroupChangeRequest_Exitoso() {
        when(requestService.createGroupChangeRequest("STU-123", "GRP-1", "GRP-2", "Conflictos de horario"))
                .thenReturn(scheduleChangeRequest);

        ResponseEntity<?> response = requestController.createGroupChangeRequest(groupChangeRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(ScheduleChangeRequest.class, response.getBody());
        verify(requestService, times(1)).createGroupChangeRequest("STU-123", "GRP-1", "GRP-2", "Conflictos de horario");
    }

    @Test
    @DisplayName("Caso error - createGroupChangeRequest lanza AppException")
    void testCreateGroupChangeRequest_AppException() {
        when(requestService.createGroupChangeRequest("STU-123", "GRP-1", "GRP-2", "Conflictos de horario"))
                .thenThrow(new AppException("Grupo no encontrado"));

        ResponseEntity<?> response = requestController.createGroupChangeRequest(groupChangeRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(Map.class, response.getBody());
        assertEquals("Grupo no encontrado", ((Map<?, ?>) response.getBody()).get("error"));
        verify(requestService, times(1)).createGroupChangeRequest("STU-123", "GRP-1", "GRP-2", "Conflictos de horario");
    }

    @Test
    @DisplayName("Caso exitoso - createCourseChangeRequest crea solicitud exitosamente")
    void testCreateCourseChangeRequest_Exitoso() {
        when(requestService.createCourseChangeRequest("STU-123", "MATH101", "PHYS101", "Cambio de carrera"))
                .thenReturn(scheduleChangeRequest);

        ResponseEntity<?> response = requestController.createCourseChangeRequest(courseChangeRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(ScheduleChangeRequest.class, response.getBody());
        verify(requestService, times(1)).createCourseChangeRequest("STU-123", "MATH101", "PHYS101", "Cambio de carrera");
    }

    @Test
    @DisplayName("Caso exitoso - getRequestStatus retorna estado de solicitud")
    void testGetRequestStatus_Exitoso() {
        when(requestService.getRequestStatus("REQ-123")).thenReturn(RequestStatus.PENDING);

        ResponseEntity<?> response = requestController.getRequestStatus("REQ-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(Map.class, response.getBody());
        assertEquals(RequestStatus.PENDING, ((Map<?, ?>) response.getBody()).get("status"));
        verify(requestService, times(1)).getRequestStatus("REQ-123");
    }

    @Test
    @DisplayName("Caso error - getRequestStatus lanza AppException")
    void testGetRequestStatus_AppException() {
        when(requestService.getRequestStatus("REQ-123")).thenThrow(new AppException("Solicitud no encontrada"));

        ResponseEntity<?> response = requestController.getRequestStatus("REQ-123");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Solicitud no encontrada", ((Map<?, ?>) response.getBody()).get("error"));
        verify(requestService, times(1)).getRequestStatus("REQ-123");
    }

    @Test
    @DisplayName("Caso exitoso - getRequestHistory retorna historial de solicitudes")
    void testGetRequestHistory_Exitoso() {
        List<ScheduleChangeRequest> requests = Arrays.asList(scheduleChangeRequest);
        when(requestService.getRequestHistory("STU-123")).thenReturn(requests);

        ResponseEntity<?> response = requestController.getRequestHistory("STU-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(List.class, response.getBody());
        assertEquals(1, ((List<?>) response.getBody()).size());
        verify(requestService, times(1)).getRequestHistory("STU-123");
    }

    @Test
    @DisplayName("Caso exitoso - getDecisionHistory retorna historial de decisiones")
    void testGetDecisionHistory_Exitoso() {
        List<ReviewStep> decisionHistory = Arrays.asList(reviewStep);
        when(requestService.getDecisionHistory("REQ-123")).thenReturn(decisionHistory);

        ResponseEntity<?> response = requestController.getDecisionHistory("REQ-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(List.class, response.getBody());
        assertEquals(1, ((List<?>) response.getBody()).size());
        verify(requestService, times(1)).getDecisionHistory("REQ-123");
    }

    @Test
    @DisplayName("Caso exitoso - getGroupCapacityAlert retorna alerta de capacidad")
    void testGetGroupCapacityAlert_Exitoso() {
        when(requestService.getGroupCapacityAlert("GRP-1")).thenReturn(true);

        ResponseEntity<?> response = requestController.getGroupCapacityAlert("GRP-1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(Map.class, response.getBody());
        assertEquals(true, ((Map<?, ?>) response.getBody()).get("capacityAlert"));
        verify(requestService, times(1)).getGroupCapacityAlert("GRP-1");
    }

    @Test
    @DisplayName("Caso exitoso - getRequestStatistics retorna estadísticas del estudiante")
    void testGetRequestStatistics_Exitoso() {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("total", 5);
        statistics.put("approved", 2);
        statistics.put("pending", 1);
        when(requestService.getRequestStatistics("STU-123")).thenReturn(statistics);

        ResponseEntity<?> response = requestController.getRequestStatistics("STU-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(Map.class, response.getBody());
        assertEquals(5, ((Map<?, ?>) response.getBody()).get("total"));
        verify(requestService, times(1)).getRequestStatistics("STU-123");
    }

    @Test
    @DisplayName("Caso exitoso - updateRequest actualiza solicitud exitosamente")
    void testUpdateRequest_Exitoso() {
        Map<String, Object> updates = Map.of("reason", "Nueva razón");
        when(requestService.updateRequest("REQ-123", updates)).thenReturn(scheduleChangeRequest);

        ResponseEntity<?> response = requestController.updateRequest("REQ-123", updates);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(ScheduleChangeRequest.class, response.getBody());
        verify(requestService, times(1)).updateRequest("REQ-123", updates);
    }

    @Test
    @DisplayName("Caso borde - deleteRequest retorna true cuando se elimina exitosamente")
    void testDeleteRequest_Exitoso() {
        when(requestService.deleteRequest("REQ-123")).thenReturn(true);

        ResponseEntity<?> response = requestController.deleteRequest("REQ-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(requestService, times(1)).deleteRequest("REQ-123");
    }

    @Test
    @DisplayName("Caso borde - deleteRequest retorna false cuando no existe")
    void testDeleteRequest_NoExiste() {
        when(requestService.deleteRequest("REQ-123")).thenReturn(false);

        ResponseEntity<?> response = requestController.deleteRequest("REQ-123");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Solicitud no encontrada", ((Map<?, ?>) response.getBody()).get("error"));
        verify(requestService, times(1)).deleteRequest("REQ-123");
    }

    @Test
    @DisplayName("Caso exitoso - approveRequest aprueba solicitud exitosamente")
    void testApproveRequest_Exitoso() {
        when(requestService.approveRequest("REQ-123", "ADMIN-123", UserRole.ADMINISTRATOR, "Solicitud aprobada"))
                .thenReturn(scheduleChangeRequest);

        ResponseEntity<?> response = requestController.approveRequest("REQ-123", reviewActionRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(ScheduleChangeRequest.class, response.getBody());
        verify(requestService, times(1)).approveRequest("REQ-123", "ADMIN-123", UserRole.ADMINISTRATOR, "Solicitud aprobada");
    }


    @Test
    @DisplayName("Caso exitoso - cancelRequest cancela solicitud exitosamente")
    void testCancelRequest_Exitoso() {
        when(requestService.cancelRequest("REQ-123", "STU-123")).thenReturn(scheduleChangeRequest);

        ResponseEntity<?> response = requestController.cancelRequest("REQ-123", cancelRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(ScheduleChangeRequest.class, response.getBody());
        verify(requestService, times(1)).cancelRequest("REQ-123", "STU-123");
    }

    @Test
    @DisplayName("Caso borde - getRequestHistory retorna lista vacía")
    void testGetRequestHistory_ListaVacia() {
        when(requestService.getRequestHistory("STU-123")).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = requestController.getRequestHistory("STU-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(List.class, response.getBody());
        assertTrue(((List<?>) response.getBody()).isEmpty());
        verify(requestService, times(1)).getRequestHistory("STU-123");
    }

    @Test
    @DisplayName("Caso borde - getDecisionHistory retorna lista vacía")
    void testGetDecisionHistory_ListaVacia() {
        when(requestService.getDecisionHistory("REQ-123")).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = requestController.getDecisionHistory("REQ-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(List.class, response.getBody());
        assertTrue(((List<?>) response.getBody()).isEmpty());
        verify(requestService, times(1)).getDecisionHistory("REQ-123");
    }

    @Test
    @DisplayName("Caso error - createCourseChangeRequest lanza AppException")
    void testCreateCourseChangeRequest_AppException() {
        when(requestService.createCourseChangeRequest("STU-123", "MATH101", "PHYS101", "Cambio de carrera"))
                .thenThrow(new AppException("Curso no encontrado"));

        ResponseEntity<?> response = requestController.createCourseChangeRequest(courseChangeRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Curso no encontrado", ((Map<?, ?>) response.getBody()).get("error"));
        verify(requestService, times(1)).createCourseChangeRequest("STU-123", "MATH101", "PHYS101", "Cambio de carrera");
    }

    @Test
    @DisplayName("Caso error - getRequestHistory lanza AppException")
    void testGetRequestHistory_AppException() {
        when(requestService.getRequestHistory("STU-123")).thenThrow(new AppException("Estudiante no encontrado"));

        ResponseEntity<?> response = requestController.getRequestHistory("STU-123");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Estudiante no encontrado", ((Map<?, ?>) response.getBody()).get("error"));
        verify(requestService, times(1)).getRequestHistory("STU-123");
    }

    @Test
    @DisplayName("Caso error - updateRequest lanza AppException")
    void testUpdateRequest_AppException() {
        Map<String, Object> updates = Map.of("reason", "Nueva razón");
        when(requestService.updateRequest("REQ-123", updates))
                .thenThrow(new AppException("No se puede modificar solicitud en estado final"));

        ResponseEntity<?> response = requestController.updateRequest("REQ-123", updates);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("No se puede modificar solicitud en estado final", ((Map<?, ?>) response.getBody()).get("error"));
        verify(requestService, times(1)).updateRequest("REQ-123", updates);
    }

    @Test
    @DisplayName("Caso error - approveRequest lanza AppException")
    void testApproveRequest_AppException() {
        when(requestService.approveRequest("REQ-123", "ADMIN-123", UserRole.ADMINISTRATOR, "Solicitud aprobada"))
                .thenThrow(new AppException("No se puede aprobar la solicitud"));

        ResponseEntity<?> response = requestController.approveRequest("REQ-123", reviewActionRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("No se puede aprobar la solicitud", ((Map<?, ?>) response.getBody()).get("error"));
        verify(requestService, times(1)).approveRequest("REQ-123", "ADMIN-123", UserRole.ADMINISTRATOR, "Solicitud aprobada");
    }


    @Test
    @DisplayName("Caso error - cancelRequest lanza AppException")
    void testCancelRequest_AppException() {
        when(requestService.cancelRequest("REQ-123", "STU-123"))
                .thenThrow(new AppException("Solo el estudiante propietario puede cancelar la solicitud"));

        ResponseEntity<?> response = requestController.cancelRequest("REQ-123", cancelRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Solo el estudiante propietario puede cancelar la solicitud", ((Map<?, ?>) response.getBody()).get("error"));
        verify(requestService, times(1)).cancelRequest("REQ-123", "STU-123");
    }

    @Test
    @DisplayName("Caso borde - getGroupCapacityAlert retorna false cuando no hay alerta")
    void testGetGroupCapacityAlert_SinAlerta() {
        when(requestService.getGroupCapacityAlert("GRP-1")).thenReturn(false);

        ResponseEntity<?> response = requestController.getGroupCapacityAlert("GRP-1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(false, ((Map<?, ?>) response.getBody()).get("capacityAlert"));
        verify(requestService, times(1)).getGroupCapacityAlert("GRP-1");
    }
}
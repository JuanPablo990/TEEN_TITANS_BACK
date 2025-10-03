package eci.edu.dosw.parcial.TEEN_TITANS_BACK.services;


import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dtos.DeaneryRequestDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DeaneryServiceTest {

    private DeaneryService service;
    private Request request;
    private Student student;

    @BeforeEach
    void setUp() {
        service = new DeaneryService();

        student = new Student();

        try {
            var field = Student.class.getDeclaredField("career");
            field.setAccessible(true);
            field.set(student, "Engineering");
        } catch (Exception e) {
            fail("No se pudo configurar el estudiante en la prueba");
        }

        request = new Request();
        try {
            setField(request, "id", "REQ-001");
            setField(request, "type", RequestType.REQUEST_FOR_GROUP_CHANGE);
            setField(request, "student", student);
            setField(request, "originalGroup", 101);
            setField(request, "targetGroup", 202);
            setField(request, "status", RequestStatus.PENDING);
            setField(request, "priority", 9);
            setField(request, "creationDate", new Date());
        } catch (Exception e) {
            fail("No se pudo configurar la solicitud en la prueba");
        }

        service.addRequest(request);
    }

    private void setField(Object obj, String fieldName, Object value) throws Exception {
        var field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

    @Test
    void testGetFacultyRequests() {
        List<DeaneryRequestDTO> result = service.getFacultyRequests("Engineering");

        assertAll("Faculty Requests",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("REQ-001", result.get(0).getId()),
                () -> assertEquals("Engineering", result.get(0).getStudent().getCareer())
        );
    }

    @Test
    void testGetStudentAcademicInfoThrowsException() {
        Exception ex = assertThrows(RuntimeException.class,
                () -> service.getStudentAcademicInfo("STUDENT-123"));
        assertTrue(ex.getMessage().contains("Información académica no encontrada"));
    }


    @Test
    void testApproveRequest() {
        DeaneryRequestDTO dto = service.approveRequest("REQ-001", "Aprobado con comentarios");

        assertAll("Approve Request",
                () -> assertEquals("APPROVED", dto.getStatus()),
                () -> assertEquals("Aprobado con comentarios", dto.getObservations()),
                () -> assertNotNull(dto.getResolutionDate())
        );
    }

    @Test
    void testRejectRequest() {
        DeaneryRequestDTO dto = service.rejectRequest("REQ-001", "Razón de rechazo");

        assertAll("Reject Request",
                () -> assertEquals("REJECTED", dto.getStatus()),
                () -> assertEquals("Razón de rechazo", dto.getObservations()),
                () -> assertNotNull(dto.getResolutionDate())
        );
    }

    @Test
    void testRequestAdditionalInfo() {
        DeaneryRequestDTO dto = service.requestAdditionalInfo("REQ-001", "Falta documento");

        assertAll("Additional Info",
                () -> assertNotNull(dto.getObservations()),
                () -> assertTrue(dto.getObservations().contains("Falta documento"))
        );
    }

    @Test
    void testGetGroupCapacityAlerts() {
        List<DeaneryRequestDTO> alerts = service.getGroupCapacityAlerts("Engineering");

        assertAll("Group Capacity Alerts",
                () -> assertNotNull(alerts),
                () -> assertEquals(1, alerts.size()),
                () -> assertEquals("REQ-001", alerts.get(0).getId())
        );
    }

    @Test
    void testMonitorGroupCapacity() {
        Map<String, Object> status = service.monitorGroupCapacity("101");

        assertAll("Monitor Group Capacity",
                () -> assertEquals("101", status.get("groupId")),
                () -> assertEquals(1L, status.get("activeRequests")),
                () -> assertEquals("NORMAL", status.get("status")),
                () -> assertNotNull(status.get("timestamp"))
        );
    }
    @Test
    void testApproveRequestNotFound() {
        Exception ex = assertThrows(RuntimeException.class,
                () -> service.approveRequest("REQ-999", "Aprobado con comentarios"));

        assertTrue(ex.getMessage().contains("Solicitud no encontrada"));
    }

    @Test
    void testRejectRequestNotFound() {
        Exception ex = assertThrows(RuntimeException.class,
                () -> service.rejectRequest("REQ-999", "Razón de rechazo"));

        assertTrue(ex.getMessage().contains("Solicitud no encontrada"));
    }

    @Test
    void testRequestAdditionalInfoNotFound() {
        Exception ex = assertThrows(RuntimeException.class,
                () -> service.requestAdditionalInfo("REQ-999", "Falta documento"));

        assertTrue(ex.getMessage().contains("Solicitud no encontrada"));
    }

    @Test
    void testGetFacultyRequestsEmpty() {
        List<DeaneryRequestDTO> result = service.getFacultyRequests("Law");

        assertNotNull(result);
        assertTrue(result.isEmpty(), "No debería devolver solicitudes para una facultad inexistente");
    }

    @Test
    void testGetGroupCapacityAlertsEmpty() {
        List<DeaneryRequestDTO> alerts = service.getGroupCapacityAlerts("Law");

        assertNotNull(alerts);
        assertTrue(alerts.isEmpty(), "No debería haber alertas para una facultad sin solicitudes");
    }

    @Test
    void testMonitorGroupCapacityWithNoRequests() {
        Map<String, Object> status = service.monitorGroupCapacity("999");

        assertAll("Monitor vacío",
                () -> assertEquals("999", status.get("groupId")),
                () -> assertEquals(0L, status.get("activeRequests")),
                () -> assertEquals("NORMAL", status.get("status")),
                () -> assertNotNull(status.get("timestamp"))
        );
    }



}
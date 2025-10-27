package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Course;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.ScheduleChangeRequest;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.AdminGroupService;
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
public class GroupControllerTest {

    @Mock
    private AdminGroupService adminGroupService;

    @InjectMocks
    private GroupController groupController;

    private Course course;
    private ScheduleChangeRequest scheduleChangeRequest;

    @BeforeEach
    void setUp() {
        course = new Course("MATH101", "Calculus I", 4, "Basic calculus course", "Mathematics", true);
        scheduleChangeRequest = ScheduleChangeRequest.builder()
                .requestId("1")
                .reason("Schedule conflict")
                .status(null)
                .submissionDate(new Date())
                .reviewHistory(new ArrayList<>())
                .build();
    }

    @Test
    @DisplayName("Caso exitoso - getCourse retorna curso del grupo")
    void testGetCourse_Exitoso() {
        when(adminGroupService.getCourse("1")).thenReturn(course);

        ResponseEntity<?> response = groupController.getCourse("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(Course.class, response.getBody());
        assertEquals(course, response.getBody());
        verify(adminGroupService, times(1)).getCourse("1");
    }

    @Test
    @DisplayName("Caso error - getCourse lanza AppException")
    void testGetCourse_AppException() {
        when(adminGroupService.getCourse("1")).thenThrow(new AppException("Grupo no encontrado"));

        ResponseEntity<?> response = groupController.getCourse("1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Grupo no encontrado", response.getBody());
        verify(adminGroupService, times(1)).getCourse("1");
    }

    @Test
    @DisplayName("Caso error - getCourse lanza excepción general")
    void testGetCourse_ExcepcionGeneral() {
        when(adminGroupService.getCourse("1")).thenThrow(new RuntimeException("Error de base de datos"));

        ResponseEntity<?> response = groupController.getCourse("1");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error interno del servidor al obtener el curso del grupo", response.getBody());
        verify(adminGroupService, times(1)).getCourse("1");
    }

    @Test
    @DisplayName("Caso exitoso - getMaxCapacity retorna capacidad máxima")
    void testGetMaxCapacity_Exitoso() {
        when(adminGroupService.getMaxCapacity("1")).thenReturn(30);

        ResponseEntity<?> response = groupController.getMaxCapacity("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(30, response.getBody());
        verify(adminGroupService, times(1)).getMaxCapacity("1");
    }

    @Test
    @DisplayName("Caso error - getMaxCapacity lanza AppException")
    void testGetMaxCapacity_AppException() {
        when(adminGroupService.getMaxCapacity("1")).thenThrow(new AppException("Grupo no tiene aula asignada"));

        ResponseEntity<?> response = groupController.getMaxCapacity("1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Grupo no tiene aula asignada", response.getBody());
        verify(adminGroupService, times(1)).getMaxCapacity("1");
    }

    @Test
    @DisplayName("Caso exitoso - getCurrentEnrollment retorna inscripción actual")
    void testGetCurrentEnrollment_Exitoso() {
        when(adminGroupService.getCurrentEnrollment("1")).thenReturn(18);

        ResponseEntity<?> response = groupController.getCurrentEnrollment("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(18, response.getBody());
        verify(adminGroupService, times(1)).getCurrentEnrollment("1");
    }

    @Test
    @DisplayName("Caso borde - getWaitingList retorna lista vacía")
    void testGetWaitingList_ListaVacia() {
        when(adminGroupService.getWaitingList("1")).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = groupController.getWaitingList("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(List.class, response.getBody());
        assertTrue(((List<?>) response.getBody()).isEmpty());
        verify(adminGroupService, times(1)).getWaitingList("1");
    }

    @Test
    @DisplayName("Caso exitoso - getTotalEnrolledByCourse retorna mapa de inscripciones")
    void testGetTotalEnrolledByCourse_Exitoso() {
        Map<String, Integer> enrollmentMap = Map.of("1", 18, "2", 22);
        when(adminGroupService.getTotalEnrolledByCourse("MATH101")).thenReturn(enrollmentMap);

        ResponseEntity<?> response = groupController.getTotalEnrolledByCourse("MATH101");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(Map.class, response.getBody());
        assertEquals(2, ((Map<?, ?>) response.getBody()).size());
        verify(adminGroupService, times(1)).getTotalEnrolledByCourse("MATH101");
    }

    @Test
    @DisplayName("Caso exitoso - getGroupAvailability calcula disponibilidad correctamente")
    void testGetGroupAvailability_Exitoso() {
        when(adminGroupService.getMaxCapacity("1")).thenReturn(30);
        when(adminGroupService.getCurrentEnrollment("1")).thenReturn(18);

        ResponseEntity<?> response = groupController.getGroupAvailability("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(Map.class, response.getBody());

        Map<?, ?> availability = (Map<?, ?>) response.getBody();
        assertEquals("1", availability.get("groupId"));
        assertEquals(30, availability.get("maxCapacity"));
        assertEquals(18, availability.get("currentEnrollment"));
        assertEquals(12, availability.get("availableSpots"));
        assertEquals(60, availability.get("occupancyRate"));

        verify(adminGroupService, times(1)).getMaxCapacity("1");
        verify(adminGroupService, times(1)).getCurrentEnrollment("1");
    }

    @Test
    @DisplayName("Caso borde - getGroupAvailability con capacidad cero")
    void testGetGroupAvailability_CapacidadCero() {
        when(adminGroupService.getMaxCapacity("1")).thenReturn(0);
        when(adminGroupService.getCurrentEnrollment("1")).thenReturn(0);

        ResponseEntity<?> response = groupController.getGroupAvailability("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<?, ?> availability = (Map<?, ?>) response.getBody();
        assertEquals(0, availability.get("occupancyRate"));
    }

    @Test
    @DisplayName("Caso exitoso - hasAvailableSpots retorna true cuando hay cupos")
    void testHasAvailableSpots_ConCupos() {
        when(adminGroupService.getMaxCapacity("1")).thenReturn(30);
        when(adminGroupService.getCurrentEnrollment("1")).thenReturn(18);

        ResponseEntity<?> response = groupController.hasAvailableSpots("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<?, ?> result = (Map<?, ?>) response.getBody();
        assertEquals("1", result.get("groupId"));
        assertEquals(true, result.get("hasAvailableSpots"));
        assertEquals(12, result.get("availableSpots"));
    }

    @Test
    @DisplayName("Caso borde - hasAvailableSpots retorna false cuando no hay cupos")
    void testHasAvailableSpots_SinCupos() {
        when(adminGroupService.getMaxCapacity("1")).thenReturn(30);
        when(adminGroupService.getCurrentEnrollment("1")).thenReturn(30);

        ResponseEntity<?> response = groupController.hasAvailableSpots("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Map<?, ?> result = (Map<?, ?>) response.getBody();
        assertEquals(false, result.get("hasAvailableSpots"));
        assertEquals(0, result.get("availableSpots"));
    }

    @Test
    @DisplayName("Caso exitoso - getGroupInfo retorna información completa del grupo")
    void testGetGroupInfo_Exitoso() {
        when(adminGroupService.getCourse("1")).thenReturn(course);
        when(adminGroupService.getMaxCapacity("1")).thenReturn(30);
        when(adminGroupService.getCurrentEnrollment("1")).thenReturn(18);
        when(adminGroupService.getWaitingList("1")).thenReturn(Arrays.asList(scheduleChangeRequest));

        ResponseEntity<?> response = groupController.getGroupInfo("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(Map.class, response.getBody());

        Map<?, ?> groupInfo = (Map<?, ?>) response.getBody();
        assertEquals("1", groupInfo.get("groupId"));
        assertEquals(course, groupInfo.get("course"));

        verify(adminGroupService, times(1)).getCourse("1");
        verify(adminGroupService, times(1)).getMaxCapacity("1");
        verify(adminGroupService, times(1)).getCurrentEnrollment("1");
        verify(adminGroupService, times(1)).getWaitingList("1");
    }

    @Test
    @DisplayName("Caso borde - getGroupInfo con lista de espera vacía")
    void testGetGroupInfo_ListaEsperaVacia() {
        when(adminGroupService.getCourse("1")).thenReturn(course);
        when(adminGroupService.getMaxCapacity("1")).thenReturn(30);
        when(adminGroupService.getCurrentEnrollment("1")).thenReturn(18);
        when(adminGroupService.getWaitingList("1")).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = groupController.getGroupInfo("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<?, ?> groupInfo = (Map<?, ?>) response.getBody();
        Map<?, ?> waitingList = (Map<?, ?>) groupInfo.get("waitingList");
        assertEquals(0, waitingList.get("count"));
    }

    @Test
    @DisplayName("Caso error - getGroupInfo lanza AppException")
    void testGetGroupInfo_AppException() {
        when(adminGroupService.getCourse("1")).thenThrow(new AppException("Grupo no encontrado"));

        ResponseEntity<?> response = groupController.getGroupInfo("1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Grupo no encontrado", response.getBody());
    }

    @Test
    @DisplayName("Caso borde - healthCheck retorna estado correcto")
    void testHealthCheck_Exitoso() {
        ResponseEntity<String> response = groupController.healthCheck();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("GroupController is working properly with AdminGroupService", response.getBody());
    }

    @Test
    @DisplayName("Caso borde - getCurrentEnrollment con valores extremos")
    void testGetCurrentEnrollment_ValoresExtremos() {
        when(adminGroupService.getCurrentEnrollment("1")).thenReturn(0);

        ResponseEntity<?> response = groupController.getCurrentEnrollment("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody());
    }

    @Test
    @DisplayName("Caso error - getTotalEnrolledByCourse lanza AppException")
    void testGetTotalEnrolledByCourse_AppException() {
        when(adminGroupService.getTotalEnrolledByCourse("INVALID")).thenThrow(new AppException("Curso no encontrado"));

        ResponseEntity<?> response = groupController.getTotalEnrolledByCourse("INVALID");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Curso no encontrado", response.getBody());
        verify(adminGroupService, times(1)).getTotalEnrolledByCourse("INVALID");
    }

    @Test
    @DisplayName("Caso borde - getWaitingList con múltiples solicitudes")
    void testGetWaitingList_MultiplesSolicitudes() {
        List<ScheduleChangeRequest> requests = Arrays.asList(scheduleChangeRequest, scheduleChangeRequest);
        when(adminGroupService.getWaitingList("1")).thenReturn(requests);

        ResponseEntity<?> response = groupController.getWaitingList("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, ((List<?>) response.getBody()).size());
        verify(adminGroupService, times(1)).getWaitingList("1");
    }

    @Test
    @DisplayName("Caso error - hasAvailableSpots lanza AppException")
    void testHasAvailableSpots_AppException() {
        when(adminGroupService.getMaxCapacity("1")).thenThrow(new AppException("Grupo no encontrado"));

        ResponseEntity<?> response = groupController.hasAvailableSpots("1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Grupo no encontrado", response.getBody());
    }
}
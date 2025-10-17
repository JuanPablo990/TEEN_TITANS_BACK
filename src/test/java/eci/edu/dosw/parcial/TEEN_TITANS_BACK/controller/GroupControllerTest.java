package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Course;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.ScheduleChangeRequest;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.GroupService;
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

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GroupControllerTest {

    @Mock
    private GroupService groupService;

    @InjectMocks
    private GroupController groupController;

    private Course testCourse;
    private ScheduleChangeRequest testRequest;
    private final String GROUP_ID = "group-123";
    private final String COURSE_CODE = "CS101";

    @BeforeEach
    void setUp() {
        testCourse = new Course("CS101", "Programación I", 3, "Curso introductorio de programación", "Ingeniería de Sistemas", true);
        testRequest = new ScheduleChangeRequest("req-001", null, null, null, "Cambio de horario por conflicto");
    }

    @Test
    @DisplayName("Caso exitoso - getCourse retorna curso correctamente")
    void testGetCourse_Exitoso() {
        when(groupService.getCourse(GROUP_ID)).thenReturn(testCourse);

        ResponseEntity<?> response = groupController.getCourse(GROUP_ID);

        assertAll("Verificar respuesta exitosa de getCourse",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(testCourse, response.getBody()),
                () -> verify(groupService, times(1)).getCourse(GROUP_ID)
        );
    }

    @Test
    @DisplayName("Caso exitoso - getMaxCapacity retorna capacidad correcta")
    void testGetMaxCapacity_Exitoso() {
        when(groupService.getMaxCapacity(GROUP_ID)).thenReturn(30);

        ResponseEntity<?> response = groupController.getMaxCapacity(GROUP_ID);

        assertAll("Verificar respuesta exitosa de getMaxCapacity",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(30, response.getBody()),
                () -> verify(groupService, times(1)).getMaxCapacity(GROUP_ID)
        );
    }

    @Test
    @DisplayName("Caso exitoso - getCurrentEnrollment retorna inscripción correcta")
    void testGetCurrentEnrollment_Exitoso() {
        when(groupService.getCurrentEnrollment(GROUP_ID)).thenReturn(25);

        ResponseEntity<?> response = groupController.getCurrentEnrollment(GROUP_ID);

        assertAll("Verificar respuesta exitosa de getCurrentEnrollment",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(25, response.getBody()),
                () -> verify(groupService, times(1)).getCurrentEnrollment(GROUP_ID)
        );
    }

    @Test
    @DisplayName("Caso exitoso - getWaitingList retorna lista de espera")
    void testGetWaitingList_Exitoso() {
        List<ScheduleChangeRequest> waitingList = List.of(testRequest);
        when(groupService.getWaitingList(GROUP_ID)).thenReturn(waitingList);

        ResponseEntity<?> response = groupController.getWaitingList(GROUP_ID);

        assertAll("Verificar respuesta exitosa de getWaitingList",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(waitingList, response.getBody()),
                () -> verify(groupService, times(1)).getWaitingList(GROUP_ID)
        );
    }

    @Test
    @DisplayName("Caso exitoso - getTotalEnrolledByCourse retorna mapa de inscripciones")
    void testGetTotalEnrolledByCourse_Exitoso() {
        Map<String, Integer> enrollmentMap = Map.of("group-1", 20, "group-2", 25);
        when(groupService.getTotalEnrolledByCourse(COURSE_CODE)).thenReturn(enrollmentMap);

        ResponseEntity<?> response = groupController.getTotalEnrolledByCourse(COURSE_CODE);

        assertAll("Verificar respuesta exitosa de getTotalEnrolledByCourse",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(enrollmentMap, response.getBody()),
                () -> verify(groupService, times(1)).getTotalEnrolledByCourse(COURSE_CODE)
        );
    }

    @Test
    @DisplayName("Caso exitoso - getGroupAvailability calcula disponibilidad correctamente")
    void testGetGroupAvailability_Exitoso() {
        when(groupService.getMaxCapacity(GROUP_ID)).thenReturn(30);
        when(groupService.getCurrentEnrollment(GROUP_ID)).thenReturn(25);

        ResponseEntity<?> response = groupController.getGroupAvailability(GROUP_ID);

        assertAll("Verificar respuesta exitosa de getGroupAvailability",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> verify(groupService, times(1)).getMaxCapacity(GROUP_ID),
                () -> verify(groupService, times(1)).getCurrentEnrollment(GROUP_ID)
        );
    }

    @Test
    @DisplayName("Caso exitoso - hasAvailableSpots retorna true cuando hay cupos")
    void testHasAvailableSpots_ConCuposDisponibles() {
        when(groupService.getMaxCapacity(GROUP_ID)).thenReturn(30);
        when(groupService.getCurrentEnrollment(GROUP_ID)).thenReturn(25);

        ResponseEntity<?> response = groupController.hasAvailableSpots(GROUP_ID);

        assertAll("Verificar respuesta cuando hay cupos disponibles",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> verify(groupService, times(1)).getMaxCapacity(GROUP_ID),
                () -> verify(groupService, times(1)).getCurrentEnrollment(GROUP_ID)
        );
    }

    @Test
    @DisplayName("Caso exitoso - getGroupInfo retorna información completa")
    void testGetGroupInfo_Exitoso() {
        when(groupService.getCourse(GROUP_ID)).thenReturn(testCourse);
        when(groupService.getMaxCapacity(GROUP_ID)).thenReturn(30);
        when(groupService.getCurrentEnrollment(GROUP_ID)).thenReturn(25);
        when(groupService.getWaitingList(GROUP_ID)).thenReturn(List.of(testRequest));

        ResponseEntity<?> response = groupController.getGroupInfo(GROUP_ID);

        assertAll("Verificar respuesta exitosa de getGroupInfo",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> verify(groupService, times(1)).getCourse(GROUP_ID),
                () -> verify(groupService, times(1)).getMaxCapacity(GROUP_ID),
                () -> verify(groupService, times(1)).getCurrentEnrollment(GROUP_ID),
                () -> verify(groupService, times(1)).getWaitingList(GROUP_ID)
        );
    }

    @Test
    @DisplayName("Caso exitoso - healthCheck retorna mensaje de salud")
    void testHealthCheck_Exitoso() {
        ResponseEntity<String> response = groupController.healthCheck();

        assertAll("Verificar health check",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals("GroupController is working properly", response.getBody())
        );
    }

    @Test
    @DisplayName("Caso error - getCourse lanza AppException (grupo no encontrado)")
    void testGetCourse_GrupoNoEncontrado() {
        when(groupService.getCourse(GROUP_ID)).thenThrow(new AppException("Grupo no encontrado"));

        ResponseEntity<?> response = groupController.getCourse(GROUP_ID);

        assertAll("Verificar manejo de excepción en getCourse",
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertEquals("Grupo no encontrado", response.getBody()),
                () -> verify(groupService, times(1)).getCourse(GROUP_ID)
        );
    }

    @Test
    @DisplayName("Caso error - getMaxCapacity lanza excepción genérica")
    void testGetMaxCapacity_ExcepcionGenerica() {
        when(groupService.getMaxCapacity(GROUP_ID)).thenThrow(new RuntimeException("Error inesperado"));

        ResponseEntity<?> response = groupController.getMaxCapacity(GROUP_ID);

        assertAll("Verificar manejo de excepción genérica en getMaxCapacity",
                () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode()),
                () -> assertEquals("Error interno del servidor al obtener la capacidad máxima", response.getBody()),
                () -> verify(groupService, times(1)).getMaxCapacity(GROUP_ID)
        );
    }

    @Test
    @DisplayName("Caso error - getCurrentEnrollment lanza AppException")
    void testGetCurrentEnrollment_GrupoNoEncontrado() {
        when(groupService.getCurrentEnrollment(GROUP_ID)).thenThrow(new AppException("Grupo no existe"));

        ResponseEntity<?> response = groupController.getCurrentEnrollment(GROUP_ID);

        assertAll("Verificar manejo de excepción en getCurrentEnrollment",
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertEquals("Grupo no existe", response.getBody()),
                () -> verify(groupService, times(1)).getCurrentEnrollment(GROUP_ID)
        );
    }

    @Test
    @DisplayName("Caso error - getWaitingList lanza excepción genérica")
    void testGetWaitingList_ExcepcionGenerica() {
        when(groupService.getWaitingList(GROUP_ID)).thenThrow(new RuntimeException("Error de base de datos"));

        ResponseEntity<?> response = groupController.getWaitingList(GROUP_ID);

        assertAll("Verificar manejo de excepción genérica en getWaitingList",
                () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode()),
                () -> assertEquals("Error interno del servidor al obtener la lista de espera", response.getBody()),
                () -> verify(groupService, times(1)).getWaitingList(GROUP_ID)
        );
    }

    @Test
    @DisplayName("Caso error - getTotalEnrolledByCourse curso no encontrado")
    void testGetTotalEnrolledByCourse_CursoNoEncontrado() {
        when(groupService.getTotalEnrolledByCourse(COURSE_CODE)).thenThrow(new AppException("Curso no encontrado"));

        ResponseEntity<?> response = groupController.getTotalEnrolledByCourse(COURSE_CODE);

        assertAll("Verificar manejo de excepción en getTotalEnrolledByCourse",
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertEquals("Curso no encontrado", response.getBody()),
                () -> verify(groupService, times(1)).getTotalEnrolledByCourse(COURSE_CODE)
        );
    }

    @Test
    @DisplayName("Caso borde - hasAvailableSpots sin cupos disponibles")
    void testHasAvailableSpots_SinCuposDisponibles() {
        when(groupService.getMaxCapacity(GROUP_ID)).thenReturn(30);
        when(groupService.getCurrentEnrollment(GROUP_ID)).thenReturn(30);

        ResponseEntity<?> response = groupController.hasAvailableSpots(GROUP_ID);

        assertAll("Verificar respuesta cuando no hay cupos disponibles",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> verify(groupService, times(1)).getMaxCapacity(GROUP_ID),
                () -> verify(groupService, times(1)).getCurrentEnrollment(GROUP_ID)
        );
    }

    @Test
    @DisplayName("Caso borde - getGroupInfo con lista de espera vacía")
    void testGetGroupInfo_ListaEsperaVacia() {
        when(groupService.getCourse(GROUP_ID)).thenReturn(testCourse);
        when(groupService.getMaxCapacity(GROUP_ID)).thenReturn(30);
        when(groupService.getCurrentEnrollment(GROUP_ID)).thenReturn(25);
        when(groupService.getWaitingList(GROUP_ID)).thenReturn(List.of());

        ResponseEntity<?> response = groupController.getGroupInfo(GROUP_ID);

        assertAll("Verificar getGroupInfo con lista de espera vacía",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> verify(groupService, times(1)).getWaitingList(GROUP_ID)
        );
    }

    @Test
    @DisplayName("Caso borde - getGroupAvailability con grupo lleno")
    void testGetGroupAvailability_GrupoLleno() {
        when(groupService.getMaxCapacity(GROUP_ID)).thenReturn(30);
        when(groupService.getCurrentEnrollment(GROUP_ID)).thenReturn(30);

        ResponseEntity<?> response = groupController.getGroupAvailability(GROUP_ID);

        assertAll("Verificar getGroupAvailability con grupo lleno",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> verify(groupService, times(1)).getMaxCapacity(GROUP_ID),
                () -> verify(groupService, times(1)).getCurrentEnrollment(GROUP_ID)
        );
    }

    @Test
    @DisplayName("Caso borde - getGroupInfo lanza AppException")
    void testGetGroupInfo_GrupoNoEncontrado() {
        when(groupService.getCourse(GROUP_ID)).thenThrow(new AppException("Grupo no existe"));

        ResponseEntity<?> response = groupController.getGroupInfo(GROUP_ID);

        assertAll("Verificar manejo de excepción en getGroupInfo",
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertEquals("Grupo no existe", response.getBody()),
                () -> verify(groupService, times(1)).getCourse(GROUP_ID)
        );
    }
}
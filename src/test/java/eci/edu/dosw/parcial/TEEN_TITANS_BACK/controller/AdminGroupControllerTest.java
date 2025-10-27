package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminGroupControllerTest {

    @Mock
    private AdminGroupService adminGroupService;

    @InjectMocks
    private AdminGroupController adminGroupController;

    private Group group;
    private Course course;
    private Professor professor;
    private Classroom classroom;
    private ScheduleChangeRequest scheduleChangeRequest;

    @BeforeEach
    void setUp() {
        course = new Course("MATH101", "Calculus I", 4, "Basic calculus course", "Mathematics", true);
        professor = new Professor("1", "Dr. Smith", "smith@university.edu", "password", "Mathematics", true, Arrays.asList("Mathematics", "Calculus"));
        classroom = new Classroom("1", "Main", "101", 30, null);
        group = new Group("1", "A", course, professor, null, classroom);
        scheduleChangeRequest = new ScheduleChangeRequest("1", null, group, group, "Schedule conflict");
    }

    @Test
    @DisplayName("Caso exitoso - createGroup retorna grupo creado")
    void testCreateGroup_Exitoso() {
        when(adminGroupService.createGroup(any(Group.class))).thenReturn(group);

        ResponseEntity<?> response = adminGroupController.createGroup(group);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(Group.class, response.getBody());
        assertEquals(group, response.getBody());
        verify(adminGroupService, times(1)).createGroup(group);
    }

    @Test
    @DisplayName("Caso error - createGroup lanza AppException")
    void testCreateGroup_AppException() {
        when(adminGroupService.createGroup(any(Group.class))).thenThrow(new AppException("Curso no existe"));

        ResponseEntity<?> response = adminGroupController.createGroup(group);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Curso no existe", response.getBody());
        verify(adminGroupService, times(1)).createGroup(group);
    }

    @Test
    @DisplayName("Caso error - createGroup lanza excepción general")
    void testCreateGroup_ExcepcionGeneral() {
        when(adminGroupService.createGroup(any(Group.class))).thenThrow(new RuntimeException("Error inesperado"));

        ResponseEntity<?> response = adminGroupController.createGroup(group);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error interno del servidor al crear el grupo", response.getBody());
        verify(adminGroupService, times(1)).createGroup(group);
    }

    @Test
    @DisplayName("Caso exitoso - getAllGroups retorna lista de grupos")
    void testGetAllGroups_Exitoso() {
        List<Group> groups = Arrays.asList(group);
        when(adminGroupService.listAllGroups()).thenReturn(groups);

        ResponseEntity<?> response = adminGroupController.getAllGroups();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(List.class, response.getBody());
        assertEquals(1, ((List<?>) response.getBody()).size());
        verify(adminGroupService, times(1)).listAllGroups();
    }

    @Test
    @DisplayName("Caso error - getAllGroups lanza excepción general")
    void testGetAllGroups_ExcepcionGeneral() {
        when(adminGroupService.listAllGroups()).thenThrow(new RuntimeException("Error de base de datos"));

        ResponseEntity<?> response = adminGroupController.getAllGroups();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error interno del servidor al obtener los grupos", response.getBody());
        verify(adminGroupService, times(1)).listAllGroups();
    }

    @Test
    @DisplayName("Caso exitoso - assignProfessorToGroup asigna profesor exitosamente")
    void testAssignProfessorToGroup_Exitoso() {
        doNothing().when(adminGroupService).assignProfessorToGroup("1", "1");

        ResponseEntity<?> response = adminGroupController.assignProfessorToGroup("1", "1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Profesor asignado al grupo exitosamente", response.getBody());
        verify(adminGroupService, times(1)).assignProfessorToGroup("1", "1");
    }

    @Test
    @DisplayName("Caso error - assignProfessorToGroup lanza AppException")
    void testAssignProfessorToGroup_AppException() {
        doThrow(new AppException("Grupo no encontrado")).when(adminGroupService).assignProfessorToGroup("1", "1");

        ResponseEntity<?> response = adminGroupController.assignProfessorToGroup("1", "1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Grupo no encontrado", response.getBody());
        verify(adminGroupService, times(1)).assignProfessorToGroup("1", "1");
    }

    @Test
    @DisplayName("Caso exitoso - assignClassroomToGroup asigna aula exitosamente")
    void testAssignClassroomToGroup_Exitoso() {
        doNothing().when(adminGroupService).assignClassroomToGroup("1", "1");

        ResponseEntity<?> response = adminGroupController.assignClassroomToGroup("1", "1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Aula asignada al grupo exitosamente", response.getBody());
        verify(adminGroupService, times(1)).assignClassroomToGroup("1", "1");
    }

    @Test
    @DisplayName("Caso error - assignClassroomToGroup lanza AppException")
    void testAssignClassroomToGroup_AppException() {
        doThrow(new AppException("Aula no encontrada")).when(adminGroupService).assignClassroomToGroup("1", "1");

        ResponseEntity<?> response = adminGroupController.assignClassroomToGroup("1", "1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Aula no encontrada", response.getBody());
        verify(adminGroupService, times(1)).assignClassroomToGroup("1", "1");
    }

    @Test
    @DisplayName("Caso exitoso - getCourse retorna curso del grupo")
    void testGetCourse_Exitoso() {
        when(adminGroupService.getCourse("1")).thenReturn(course);

        ResponseEntity<?> response = adminGroupController.getCourse("1");

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

        ResponseEntity<?> response = adminGroupController.getCourse("1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Grupo no encontrado", response.getBody());
        verify(adminGroupService, times(1)).getCourse("1");
    }

    @Test
    @DisplayName("Caso exitoso - getMaxCapacity retorna capacidad máxima")
    void testGetMaxCapacity_Exitoso() {
        when(adminGroupService.getMaxCapacity("1")).thenReturn(30);

        ResponseEntity<?> response = adminGroupController.getMaxCapacity("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(30, response.getBody());
        verify(adminGroupService, times(1)).getMaxCapacity("1");
    }

    @Test
    @DisplayName("Caso error - getMaxCapacity lanza AppException")
    void testGetMaxCapacity_AppException() {
        when(adminGroupService.getMaxCapacity("1")).thenThrow(new AppException("Grupo no tiene aula asignada"));

        ResponseEntity<?> response = adminGroupController.getMaxCapacity("1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Grupo no tiene aula asignada", response.getBody());
        verify(adminGroupService, times(1)).getMaxCapacity("1");
    }

    @Test
    @DisplayName("Caso exitoso - getCurrentEnrollment retorna inscripción actual")
    void testGetCurrentEnrollment_Exitoso() {
        when(adminGroupService.getCurrentEnrollment("1")).thenReturn(18);

        ResponseEntity<?> response = adminGroupController.getCurrentEnrollment("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(18, response.getBody());
        verify(adminGroupService, times(1)).getCurrentEnrollment("1");
    }

    @Test
    @DisplayName("Caso borde - getWaitingList retorna lista vacía")
    void testGetWaitingList_ListaVacia() {
        when(adminGroupService.getWaitingList("1")).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = adminGroupController.getWaitingList("1");

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

        ResponseEntity<?> response = adminGroupController.getTotalEnrolledByCourse("MATH101");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(Map.class, response.getBody());
        assertEquals(2, ((Map<?, ?>) response.getBody()).size());
        verify(adminGroupService, times(1)).getTotalEnrolledByCourse("MATH101");
    }

    @Test
    @DisplayName("Caso borde - assignProfessor con IDs nulos")
    void testAssignProfessor_IdsNulos() {
        doThrow(new AppException("ID no puede ser nulo")).when(adminGroupService).assignProfessorToGroup(null, null);

        ResponseEntity<?> response = adminGroupController.assignProfessorToGroup(null, null);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(adminGroupService, times(1)).assignProfessorToGroup(null, null);
    }

    @Test
    @DisplayName("Caso borde - getAllGroups retorna lista vacía")
    void testGetAllGroups_ListaVacia() {
        when(adminGroupService.listAllGroups()).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = adminGroupController.getAllGroups();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(List.class, response.getBody());
        assertTrue(((List<?>) response.getBody()).isEmpty());
        verify(adminGroupService, times(1)).listAllGroups();
    }

    @Test
    @DisplayName("Caso error - getWaitingList lanza AppException")
    void testGetWaitingList_AppException() {
        when(adminGroupService.getWaitingList("1")).thenThrow(new AppException("Grupo no encontrado"));

        ResponseEntity<?> response = adminGroupController.getWaitingList("1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Grupo no encontrado", response.getBody());
        verify(adminGroupService, times(1)).getWaitingList("1");
    }

    @Test
    @DisplayName("Caso error - getTotalEnrolledByCourse lanza AppException")
    void testGetTotalEnrolledByCourse_AppException() {
        when(adminGroupService.getTotalEnrolledByCourse("INVALID")).thenThrow(new AppException("Curso no encontrado"));

        ResponseEntity<?> response = adminGroupController.getTotalEnrolledByCourse("INVALID");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Curso no encontrado", response.getBody());
        verify(adminGroupService, times(1)).getTotalEnrolledByCourse("INVALID");
    }
}
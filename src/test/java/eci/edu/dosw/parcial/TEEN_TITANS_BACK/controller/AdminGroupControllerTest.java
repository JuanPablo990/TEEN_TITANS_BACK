package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.AdminGroupService;
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
public class AdminGroupControllerTest {

    @Mock
    private AdminGroupService adminGroupService;

    @InjectMocks
    private AdminGroupController adminGroupController;

    private AcademicPeriod testPeriod;
    private Course testCourse;
    private Group testGroup;

    @BeforeEach
    void setUp() {
        testPeriod = new AcademicPeriod("P2025-1", "Periodo 2025-1", new Date(), new Date(), new Date(), new Date(), new Date(), new Date(), true);
        testCourse = new Course("CS101", "Programación", 3, "Curso de programación básica", "Ingeniería", true);
        testGroup = new Group("G001", "A", testCourse, null, null, null);
    }

    @Test
    @DisplayName("Caso exitoso - Configurar periodo académico")
    void testConfigureAcademicPeriod_Exitoso() {
        doNothing().when(adminGroupService).configureAcademicPeriod(any(AcademicPeriod.class));

        ResponseEntity<?> response = adminGroupController.configureAcademicPeriod(testPeriod);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Periodo académico configurado exitosamente", response.getBody());
        verify(adminGroupService, times(1)).configureAcademicPeriod(testPeriod);
    }

    @Test
    @DisplayName("Caso error - Configurar periodo académico con AppException")
    void testConfigureAcademicPeriod_AppException() {
        doThrow(new AppException("Error en periodo")).when(adminGroupService).configureAcademicPeriod(any(AcademicPeriod.class));

        ResponseEntity<?> response = adminGroupController.configureAcademicPeriod(testPeriod);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error en periodo", response.getBody());
    }

    @Test
    @DisplayName("Caso exitoso - Crear curso")
    void testCreateCourse_Exitoso() {
        when(adminGroupService.createCourse(any(Course.class))).thenReturn(testCourse);

        ResponseEntity<?> response = adminGroupController.createCourse(testCourse);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testCourse, response.getBody());
        verify(adminGroupService, times(1)).createCourse(testCourse);
    }

    @Test
    @DisplayName("Caso error - Crear curso con AppException")
    void testCreateCourse_AppException() {
        when(adminGroupService.createCourse(any(Course.class))).thenThrow(new AppException("Curso ya existe"));

        ResponseEntity<?> response = adminGroupController.createCourse(testCourse);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Curso ya existe", response.getBody());
    }

    @Test
    @DisplayName("Caso exitoso - Actualizar curso")
    void testUpdateCourse_Exitoso() {
        String courseCode = "CS101";
        when(adminGroupService.updateCourse(eq(courseCode), any(Course.class))).thenReturn(testCourse);

        ResponseEntity<?> response = adminGroupController.updateCourse(courseCode, testCourse);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testCourse, response.getBody());
        verify(adminGroupService, times(1)).updateCourse(courseCode, testCourse);
    }

    @Test
    @DisplayName("Caso error - Actualizar curso no encontrado")
    void testUpdateCourse_CursoNoEncontrado() {
        String courseCode = "CS999";
        when(adminGroupService.updateCourse(eq(courseCode), any(Course.class))).thenThrow(new AppException("Curso no encontrado"));

        ResponseEntity<?> response = adminGroupController.updateCourse(courseCode, testCourse);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Curso no encontrado", response.getBody());
    }

    @Test
    @DisplayName("Caso exitoso - Eliminar curso")
    void testDeleteCourse_Exitoso() {
        String courseCode = "CS101";
        doNothing().when(adminGroupService).deleteCourse(courseCode);

        ResponseEntity<?> response = adminGroupController.deleteCourse(courseCode);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Curso eliminado exitosamente", response.getBody());
        verify(adminGroupService, times(1)).deleteCourse(courseCode);
    }

    @Test
    @DisplayName("Caso error - Eliminar curso no encontrado")
    void testDeleteCourse_CursoNoEncontrado() {
        String courseCode = "CS999";
        doThrow(new AppException("Curso no encontrado")).when(adminGroupService).deleteCourse(courseCode);

        ResponseEntity<?> response = adminGroupController.deleteCourse(courseCode);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Curso no encontrado", response.getBody());
    }

    @Test
    @DisplayName("Caso exitoso - Actualizar capacidad del curso")
    void testUpdateCourseCapacity_Exitoso() {
        String courseCode = "CS101";
        Map<String, Integer> request = new HashMap<>();
        request.put("newCapacity", 50);

        doNothing().when(adminGroupService).updateCourseCapacity(courseCode, 50);

        ResponseEntity<?> response = adminGroupController.updateCourseCapacity(courseCode, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Capacidad actualizada exitosamente para todos los grupos del curso", response.getBody());
        verify(adminGroupService, times(1)).updateCourseCapacity(courseCode, 50);
    }

    @Test
    @DisplayName("Caso error - Actualizar capacidad sin newCapacity")
    void testUpdateCourseCapacity_SinNewCapacity() {
        String courseCode = "CS101";
        Map<String, Integer> request = new HashMap<>();

        ResponseEntity<?> response = adminGroupController.updateCourseCapacity(courseCode, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El campo 'newCapacity' es requerido", response.getBody());
        verify(adminGroupService, never()).updateCourseCapacity(anyString(), anyInt());
    }

    @Test
    @DisplayName("Caso error - Actualizar capacidad con AppException")
    void testUpdateCourseCapacity_AppException() {
        String courseCode = "CS101";
        Map<String, Integer> request = new HashMap<>();
        request.put("newCapacity", 50);

        doThrow(new AppException("Curso no encontrado")).when(adminGroupService).updateCourseCapacity(courseCode, 50);

        ResponseEntity<?> response = adminGroupController.updateCourseCapacity(courseCode, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Curso no encontrado", response.getBody());
    }

    @Test
    @DisplayName("Caso exitoso - Crear grupo")
    void testCreateGroup_Exitoso() {
        when(adminGroupService.createGroup(any(Group.class))).thenReturn(testGroup);

        ResponseEntity<?> response = adminGroupController.createGroup(testGroup);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testGroup, response.getBody());
        verify(adminGroupService, times(1)).createGroup(testGroup);
    }

    @Test
    @DisplayName("Caso error - Crear grupo con AppException")
    void testCreateGroup_AppException() {
        when(adminGroupService.createGroup(any(Group.class))).thenThrow(new AppException("Error al crear grupo"));

        ResponseEntity<?> response = adminGroupController.createGroup(testGroup);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error al crear grupo", response.getBody());
    }

    @Test
    @DisplayName("Caso exitoso - Actualizar grupo")
    void testUpdateGroup_Exitoso() {
        String groupId = "G001";
        when(adminGroupService.updateGroup(eq(groupId), any(Group.class))).thenReturn(testGroup);

        ResponseEntity<?> response = adminGroupController.updateGroup(groupId, testGroup);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testGroup, response.getBody());
        verify(adminGroupService, times(1)).updateGroup(groupId, testGroup);
    }

    @Test
    @DisplayName("Caso error - Actualizar grupo no encontrado")
    void testUpdateGroup_GrupoNoEncontrado() {
        String groupId = "G999";
        when(adminGroupService.updateGroup(eq(groupId), any(Group.class))).thenThrow(new AppException("Grupo no encontrado"));

        ResponseEntity<?> response = adminGroupController.updateGroup(groupId, testGroup);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Grupo no encontrado", response.getBody());
    }

    @Test
    @DisplayName("Caso exitoso - Eliminar grupo")
    void testDeleteGroup_Exitoso() {
        String groupId = "G001";
        doNothing().when(adminGroupService).deleteGroup(groupId);

        ResponseEntity<?> response = adminGroupController.deleteGroup(groupId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Grupo eliminado exitosamente", response.getBody());
        verify(adminGroupService, times(1)).deleteGroup(groupId);
    }

    @Test
    @DisplayName("Caso exitoso - Obtener todos los grupos")
    void testGetAllGroups_Exitoso() {
        List<Group> groups = List.of(testGroup);
        when(adminGroupService.getAllGroups()).thenReturn(groups);

        ResponseEntity<?> response = adminGroupController.getAllGroups();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(groups, response.getBody());
        verify(adminGroupService, times(1)).getAllGroups();
    }

    @Test
    @DisplayName("Caso error - Obtener todos los grupos con excepción")
    void testGetAllGroups_Excepcion() {
        when(adminGroupService.getAllGroups()).thenThrow(new RuntimeException("Error de base de datos"));

        ResponseEntity<?> response = adminGroupController.getAllGroups();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error interno del servidor al obtener los grupos", response.getBody());
    }

    @Test
    @DisplayName("Caso exitoso - Obtener grupos más solicitados")
    void testGetMostRequestedGroups_Exitoso() {
        List<Group> groups = List.of(testGroup);
        when(adminGroupService.getMostRequestedGroups()).thenReturn(groups);

        ResponseEntity<?> response = adminGroupController.getMostRequestedGroups();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(groups, response.getBody());
        verify(adminGroupService, times(1)).getMostRequestedGroups();
    }

    @Test
    @DisplayName("Caso exitoso - Obtener grupos por día")
    void testGetGroupsByDay_Exitoso() {
        List<Group> groups = List.of(testGroup);
        when(adminGroupService.getGroupsByDay("Lunes")).thenReturn(groups);

        ResponseEntity<?> response = adminGroupController.getGroupsByDay("Lunes");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(groups, response.getBody());
        verify(adminGroupService, times(1)).getGroupsByDay("Lunes");
    }

    @Test
    @DisplayName("Caso exitoso - Asignar profesor a grupo")
    void testAssignProfessorToGroup_Exitoso() {
        String groupId = "G001";
        String professorId = "P001";
        when(adminGroupService.assignProfessorToGroup(professorId, groupId)).thenReturn(testGroup);

        ResponseEntity<?> response = adminGroupController.assignProfessorToGroup(groupId, professorId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testGroup, response.getBody());
        verify(adminGroupService, times(1)).assignProfessorToGroup(professorId, groupId);
    }

    @Test
    @DisplayName("Caso error - Asignar profesor a grupo no encontrado")
    void testAssignProfessorToGroup_GrupoNoEncontrado() {
        String groupId = "G999";
        String professorId = "P001";
        when(adminGroupService.assignProfessorToGroup(professorId, groupId)).thenThrow(new AppException("Grupo no encontrado"));

        ResponseEntity<?> response = adminGroupController.assignProfessorToGroup(groupId, professorId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Grupo no encontrado", response.getBody());
    }

    @Test
    @DisplayName("Caso exitoso - Remover profesor de grupo")
    void testRemoveProfessorFromGroup_Exitoso() {
        String groupId = "G001";
        when(adminGroupService.removeProfessorFromGroup(groupId)).thenReturn(testGroup);

        ResponseEntity<?> response = adminGroupController.removeProfessorFromGroup(groupId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testGroup, response.getBody());
        verify(adminGroupService, times(1)).removeProfessorFromGroup(groupId);
    }

    @Test
    @DisplayName("Caso exitoso - Asignar estudiante a grupo")
    void testAssignStudentToGroup_Exitoso() {
        String groupId = "G001";
        String studentId = "S001";
        doNothing().when(adminGroupService).assignStudentToGroup(studentId, groupId);

        ResponseEntity<?> response = adminGroupController.assignStudentToGroup(groupId, studentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Estudiante asignado al grupo exitosamente", response.getBody());
        verify(adminGroupService, times(1)).assignStudentToGroup(studentId, groupId);
    }

    @Test
    @DisplayName("Caso exitoso - Remover estudiante de grupo")
    void testRemoveStudentFromGroup_Exitoso() {
        String groupId = "G001";
        String studentId = "S001";
        doNothing().when(adminGroupService).removeStudentFromGroup(studentId, groupId);

        ResponseEntity<?> response = adminGroupController.removeStudentFromGroup(groupId, studentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Estudiante removido del grupo exitosamente", response.getBody());
        verify(adminGroupService, times(1)).removeStudentFromGroup(studentId, groupId);
    }

    @Test
    @DisplayName("Caso exitoso - Asignar curso a estudiante")
    void testAssignCourseToStudent_Exitoso() {
        String studentId = "S001";
        String courseCode = "CS101";
        doNothing().when(adminGroupService).assignCourseToStudent(studentId, courseCode);

        ResponseEntity<?> response = adminGroupController.assignCourseToStudent(studentId, courseCode);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Curso asignado al estudiante exitosamente", response.getBody());
        verify(adminGroupService, times(1)).assignCourseToStudent(studentId, courseCode);
    }

    @Test
    @DisplayName("Caso exitoso - Remover curso de estudiante")
    void testRemoveCourseFromStudent_Exitoso() {
        String studentId = "S001";
        String courseCode = "CS101";
        doNothing().when(adminGroupService).removeCourseFromStudent(studentId, courseCode);

        ResponseEntity<?> response = adminGroupController.removeCourseFromStudent(studentId, courseCode);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Curso removido del estudiante exitosamente", response.getBody());
        verify(adminGroupService, times(1)).removeCourseFromStudent(studentId, courseCode);
    }

    @Test
    @DisplayName("Caso exitoso - Obtener curso de grupo")
    void testGetCourse_Exitoso() {
        String groupId = "G001";
        when(adminGroupService.getCourse(groupId)).thenReturn(testCourse);

        ResponseEntity<?> response = adminGroupController.getCourse(groupId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testCourse, response.getBody());
        verify(adminGroupService, times(1)).getCourse(groupId);
    }

    @Test
    @DisplayName("Caso exitoso - Obtener capacidad máxima")
    void testGetMaxCapacity_Exitoso() {
        String groupId = "G001";
        when(adminGroupService.getMaxCapacity(groupId)).thenReturn(30);

        ResponseEntity<?> response = adminGroupController.getMaxCapacity(groupId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(30, response.getBody());
        verify(adminGroupService, times(1)).getMaxCapacity(groupId);
    }

    @Test
    @DisplayName("Caso exitoso - Obtener inscripción actual")
    void testGetCurrentEnrollment_Exitoso() {
        String groupId = "G001";
        when(adminGroupService.getCurrentEnrollment(groupId)).thenReturn(18);

        ResponseEntity<?> response = adminGroupController.getCurrentEnrollment(groupId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(18, response.getBody());
        verify(adminGroupService, times(1)).getCurrentEnrollment(groupId);
    }

    @Test
    @DisplayName("Caso exitoso - Obtener inscripciones por grupo")
    void testGetTotalEnrolledByCourse_Exitoso() {
        String courseCode = "CS101";
        Map<String, Integer> enrollmentMap = new HashMap<>();
        enrollmentMap.put("G001", 18);
        enrollmentMap.put("G002", 22);

        when(adminGroupService.getTotalEnrolledByCourse(courseCode)).thenReturn(enrollmentMap);

        ResponseEntity<?> response = adminGroupController.getTotalEnrolledByCourse(courseCode);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(enrollmentMap, response.getBody());
        verify(adminGroupService, times(1)).getTotalEnrolledByCourse(courseCode);
    }
}
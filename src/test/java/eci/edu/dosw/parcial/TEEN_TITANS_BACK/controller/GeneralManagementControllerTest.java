package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.CourseStatus;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.RoomType;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.GeneralManagementService;
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
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GeneralManagementControllerTest {

    @Mock
    private GeneralManagementService generalManagementService;

    @InjectMocks
    private GeneralManagementController generalManagementController;

    private AcademicPeriod academicPeriod;
    private Classroom classroom;
    private Course course;
    private CourseStatusDetail courseStatusDetail;
    private ReviewStep reviewStep;
    private Schedule schedule;
    private StudentAcademicProgress studentAcademicProgress;
    private Student student;
    private Group group;
    private Professor professor;

    @BeforeEach
    void setUp() {
        academicPeriod = new AcademicPeriod("1", "2025-1", new Date(), new Date(),
                new Date(), new Date(), new Date(), new Date(), true);

        classroom = new Classroom("1", "A", "101", 50, RoomType.LABORATORY);

        course = new Course("CS101", "Programación I", 4, "Curso de programación básica",
                "Ingeniería de Sistemas", true);

        student = new Student("STU001", "Robin", "robin@titans.edu", "password",
                "Ingeniería de Sistemas", 3);

        professor = new Professor();
        professor.setId("PROF001");
        professor.setName("Starfire");

        group = new Group("1", "A", course, professor, schedule, classroom);

        courseStatusDetail = new CourseStatusDetail("1", course, "STU001", CourseStatus.IN_PROGRESS,
                3.5, "2025-1", new Date(), null, group,
                professor, 4, false, "Buen desempeño");

        reviewStep = new ReviewStep("USER001", UserRole.PROFESSOR, "APPROVED", "Comentario de prueba");

        schedule = new Schedule("1", "LUNES", "08:00", "10:00", "2025-1");

        studentAcademicProgress = new StudentAcademicProgress("1", student, "Ingeniería de Sistemas",
                "Ingeniería", "Regular", 3, 10, 45,
                160, 3.8, Collections.emptyList());
    }

    // Tests existentes (tus 52 tests)
    @Test
    @DisplayName("Caso exitoso - Crear período académico")
    void testCreateAcademicPeriod_Exitoso() {
        when(generalManagementService.createAcademicPeriod(academicPeriod)).thenReturn(academicPeriod);

        ResponseEntity<AcademicPeriod> respuesta = generalManagementController.createAcademicPeriod(academicPeriod);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertEquals(academicPeriod, respuesta.getBody());
        verify(generalManagementService, times(1)).createAcademicPeriod(academicPeriod);
    }

    @Test
    @DisplayName("Caso error - Crear período académico con excepción")
    void testCreateAcademicPeriod_Error() {
        when(generalManagementService.createAcademicPeriod(academicPeriod)).thenThrow(new RuntimeException("Error"));

        ResponseEntity<AcademicPeriod> respuesta = generalManagementController.createAcademicPeriod(academicPeriod);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).createAcademicPeriod(academicPeriod);
    }

    @Test
    @DisplayName("Caso exitoso - Obtener todos los períodos académicos")
    void testGetAllAcademicPeriods_Exitoso() {
        List<AcademicPeriod> periods = Arrays.asList(academicPeriod);
        when(generalManagementService.getAllAcademicPeriods()).thenReturn(periods);

        ResponseEntity<List<AcademicPeriod>> respuesta = generalManagementController.getAllAcademicPeriods();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(periods, respuesta.getBody());
        verify(generalManagementService, times(1)).getAllAcademicPeriods();
    }

    @Test
    @DisplayName("Caso error - Obtener períodos académicos con excepción")
    void testGetAllAcademicPeriods_Error() {
        when(generalManagementService.getAllAcademicPeriods()).thenThrow(new RuntimeException("Error"));

        ResponseEntity<List<AcademicPeriod>> respuesta = generalManagementController.getAllAcademicPeriods();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).getAllAcademicPeriods();
    }

    @Test
    @DisplayName("Caso exitoso - Obtener período académico por ID")
    void testGetAcademicPeriodById_Exitoso() {
        when(generalManagementService.getAcademicPeriodById("1")).thenReturn(academicPeriod);

        ResponseEntity<AcademicPeriod> respuesta = generalManagementController.getAcademicPeriodById("1");

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(academicPeriod, respuesta.getBody());
        verify(generalManagementService, times(1)).getAcademicPeriodById("1");
    }

    @Test
    @DisplayName("Caso error - Obtener período académico por ID no encontrado")
    void testGetAcademicPeriodById_NoEncontrado() {
        when(generalManagementService.getAcademicPeriodById("99")).thenThrow(new AppException("No encontrado"));

        ResponseEntity<AcademicPeriod> respuesta = generalManagementController.getAcademicPeriodById("99");

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).getAcademicPeriodById("99");
    }

    @Test
    @DisplayName("Caso exitoso - Obtener período académico actual")
    void testGetCurrentAcademicPeriod_Exitoso() {
        when(generalManagementService.getCurrentAcademicPeriod()).thenReturn(academicPeriod);

        ResponseEntity<AcademicPeriod> respuesta = generalManagementController.getCurrentAcademicPeriod();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(academicPeriod, respuesta.getBody());
        verify(generalManagementService, times(1)).getCurrentAcademicPeriod();
    }

    @Test
    @DisplayName("Caso error - Obtener período académico actual no encontrado")
    void testGetCurrentAcademicPeriod_NoEncontrado() {
        when(generalManagementService.getCurrentAcademicPeriod()).thenThrow(new AppException("No encontrado"));

        ResponseEntity<AcademicPeriod> respuesta = generalManagementController.getCurrentAcademicPeriod();

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).getCurrentAcademicPeriod();
    }

    @Test
    @DisplayName("Caso exitoso - Actualizar período académico")
    void testUpdateAcademicPeriod_Exitoso() {
        when(generalManagementService.updateAcademicPeriod("1", academicPeriod)).thenReturn(academicPeriod);

        ResponseEntity<AcademicPeriod> respuesta = generalManagementController.updateAcademicPeriod("1", academicPeriod);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(academicPeriod, respuesta.getBody());
        verify(generalManagementService, times(1)).updateAcademicPeriod("1", academicPeriod);
    }

    @Test
    @DisplayName("Caso error - Actualizar período académico con excepción")
    void testUpdateAcademicPeriod_Error() {
        when(generalManagementService.updateAcademicPeriod("1", academicPeriod)).thenThrow(new RuntimeException("Error"));

        ResponseEntity<AcademicPeriod> respuesta = generalManagementController.updateAcademicPeriod("1", academicPeriod);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).updateAcademicPeriod("1", academicPeriod);
    }

    @Test
    @DisplayName("Caso exitoso - Eliminar período académico")
    void testDeleteAcademicPeriod_Exitoso() {
        doNothing().when(generalManagementService).deleteAcademicPeriod("1");

        ResponseEntity<Void> respuesta = generalManagementController.deleteAcademicPeriod("1");

        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).deleteAcademicPeriod("1");
    }

    @Test
    @DisplayName("Caso error - Eliminar período académico con excepción")
    void testDeleteAcademicPeriod_Error() {
        doThrow(new RuntimeException("Error")).when(generalManagementService).deleteAcademicPeriod("1");

        ResponseEntity<Void> respuesta = generalManagementController.deleteAcademicPeriod("1");

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        verify(generalManagementService, times(1)).deleteAcademicPeriod("1");
    }

    @Test
    @DisplayName("Caso exitoso - Activar período académico")
    void testActivateAcademicPeriod_Exitoso() {
        when(generalManagementService.activateAcademicPeriod("1")).thenReturn(academicPeriod);

        ResponseEntity<AcademicPeriod> respuesta = generalManagementController.activateAcademicPeriod("1");

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(academicPeriod, respuesta.getBody());
        verify(generalManagementService, times(1)).activateAcademicPeriod("1");
    }

    @Test
    @DisplayName("Caso error - Activar período académico con excepción")
    void testActivateAcademicPeriod_Error() {
        when(generalManagementService.activateAcademicPeriod("1")).thenThrow(new RuntimeException("Error"));

        ResponseEntity<AcademicPeriod> respuesta = generalManagementController.activateAcademicPeriod("1");

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).activateAcademicPeriod("1");
    }

    @Test
    @DisplayName("Caso exitoso - Crear aula")
    void testCreateClassroom_Exitoso() {
        when(generalManagementService.createClassroom(classroom)).thenReturn(classroom);

        ResponseEntity<Classroom> respuesta = generalManagementController.createClassroom(classroom);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertEquals(classroom, respuesta.getBody());
        verify(generalManagementService, times(1)).createClassroom(classroom);
    }

    @Test
    @DisplayName("Caso error - Crear aula con excepción")
    void testCreateClassroom_Error() {
        when(generalManagementService.createClassroom(classroom)).thenThrow(new RuntimeException("Error"));

        ResponseEntity<Classroom> respuesta = generalManagementController.createClassroom(classroom);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).createClassroom(classroom);
    }

    @Test
    @DisplayName("Caso exitoso - Obtener todas las aulas")
    void testGetAllClassrooms_Exitoso() {
        List<Classroom> classrooms = Arrays.asList(classroom);
        when(generalManagementService.getAllClassrooms()).thenReturn(classrooms);

        ResponseEntity<List<Classroom>> respuesta = generalManagementController.getAllClassrooms();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(classrooms, respuesta.getBody());
        verify(generalManagementService, times(1)).getAllClassrooms();
    }

    @Test
    @DisplayName("Caso error - Obtener aulas con excepción")
    void testGetAllClassrooms_Error() {
        when(generalManagementService.getAllClassrooms()).thenThrow(new RuntimeException("Error"));

        ResponseEntity<List<Classroom>> respuesta = generalManagementController.getAllClassrooms();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).getAllClassrooms();
    }

    @Test
    @DisplayName("Caso exitoso - Obtener aula por ID")
    void testGetClassroomById_Exitoso() {
        when(generalManagementService.getClassroomById("1")).thenReturn(classroom);

        ResponseEntity<Classroom> respuesta = generalManagementController.getClassroomById("1");

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(classroom, respuesta.getBody());
        verify(generalManagementService, times(1)).getClassroomById("1");
    }

    @Test
    @DisplayName("Caso error - Obtener aula por ID no encontrado")
    void testGetClassroomById_NoEncontrado() {
        when(generalManagementService.getClassroomById("99")).thenThrow(new AppException("No encontrado"));

        ResponseEntity<Classroom> respuesta = generalManagementController.getClassroomById("99");

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).getClassroomById("99");
    }

    @Test
    @DisplayName("Caso exitoso - Obtener aulas por tipo")
    void testGetClassroomsByType_Exitoso() {
        List<Classroom> classrooms = Arrays.asList(classroom);
        when(generalManagementService.getClassroomsByType("LABORATORIO")).thenReturn(classrooms);

        ResponseEntity<List<Classroom>> respuesta = generalManagementController.getClassroomsByType("LABORATORIO");

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(classrooms, respuesta.getBody());
        verify(generalManagementService, times(1)).getClassroomsByType("LABORATORIO");
    }

    @Test
    @DisplayName("Caso error - Obtener aulas por tipo con excepción")
    void testGetClassroomsByType_Error() {
        when(generalManagementService.getClassroomsByType("LABORATORIO")).thenThrow(new RuntimeException("Error"));

        ResponseEntity<List<Classroom>> respuesta = generalManagementController.getClassroomsByType("LABORATORIO");

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).getClassroomsByType("LABORATORIO");
    }

    @Test
    @DisplayName("Caso exitoso - Obtener aulas por edificio")
    void testGetClassroomsByBuilding_Exitoso() {
        List<Classroom> classrooms = Arrays.asList(classroom);
        when(generalManagementService.getClassroomsByBuilding("A")).thenReturn(classrooms);

        ResponseEntity<List<Classroom>> respuesta = generalManagementController.getClassroomsByBuilding("A");

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(classrooms, respuesta.getBody());
        verify(generalManagementService, times(1)).getClassroomsByBuilding("A");
    }

    @Test
    @DisplayName("Caso error - Obtener aulas por edificio con excepción")
    void testGetClassroomsByBuilding_Error() {
        when(generalManagementService.getClassroomsByBuilding("A")).thenThrow(new RuntimeException("Error"));

        ResponseEntity<List<Classroom>> respuesta = generalManagementController.getClassroomsByBuilding("A");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).getClassroomsByBuilding("A");
    }

    @Test
    @DisplayName("Caso exitoso - Obtener aulas con capacidad mínima")
    void testGetClassroomsWithMinCapacity_Exitoso() {
        List<Classroom> classrooms = Arrays.asList(classroom);
        when(generalManagementService.getClassroomsWithMinCapacity(30)).thenReturn(classrooms);

        ResponseEntity<List<Classroom>> respuesta = generalManagementController.getClassroomsWithMinCapacity(30);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(classrooms, respuesta.getBody());
        verify(generalManagementService, times(1)).getClassroomsWithMinCapacity(30);
    }

    @Test
    @DisplayName("Caso error - Obtener aulas por capacidad con excepción")
    void testGetClassroomsWithMinCapacity_Error() {
        when(generalManagementService.getClassroomsWithMinCapacity(30)).thenThrow(new RuntimeException("Error"));

        ResponseEntity<List<Classroom>> respuesta = generalManagementController.getClassroomsWithMinCapacity(30);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).getClassroomsWithMinCapacity(30);
    }

    @Test
    @DisplayName("Caso exitoso - Actualizar aula")
    void testUpdateClassroom_Exitoso() {
        when(generalManagementService.updateClassroom("1", classroom)).thenReturn(classroom);

        ResponseEntity<Classroom> respuesta = generalManagementController.updateClassroom("1", classroom);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(classroom, respuesta.getBody());
        verify(generalManagementService, times(1)).updateClassroom("1", classroom);
    }

    @Test
    @DisplayName("Caso error - Actualizar aula con excepción")
    void testUpdateClassroom_Error() {
        when(generalManagementService.updateClassroom("1", classroom)).thenThrow(new RuntimeException("Error"));

        ResponseEntity<Classroom> respuesta = generalManagementController.updateClassroom("1", classroom);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).updateClassroom("1", classroom);
    }

    @Test
    @DisplayName("Caso exitoso - Eliminar aula")
    void testDeleteClassroom_Exitoso() {
        doNothing().when(generalManagementService).deleteClassroom("1");

        ResponseEntity<Void> respuesta = generalManagementController.deleteClassroom("1");

        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).deleteClassroom("1");
    }

    @Test
    @DisplayName("Caso error - Eliminar aula con excepción")
    void testDeleteClassroom_Error() {
        doThrow(new RuntimeException("Error")).when(generalManagementService).deleteClassroom("1");

        ResponseEntity<Void> respuesta = generalManagementController.deleteClassroom("1");

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        verify(generalManagementService, times(1)).deleteClassroom("1");
    }

    @Test
    @DisplayName("Caso exitoso - Crear curso")
    void testCreateCourse_Exitoso() {
        when(generalManagementService.createCourse(course)).thenReturn(course);

        ResponseEntity<Course> respuesta = generalManagementController.createCourse(course);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertEquals(course, respuesta.getBody());
        verify(generalManagementService, times(1)).createCourse(course);
    }

    @Test
    @DisplayName("Caso error - Crear curso con excepción")
    void testCreateCourse_Error() {
        when(generalManagementService.createCourse(course)).thenThrow(new RuntimeException("Error"));

        ResponseEntity<Course> respuesta = generalManagementController.createCourse(course);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).createCourse(course);
    }

    @Test
    @DisplayName("Caso exitoso - Obtener todos los cursos")
    void testGetAllCourses_Exitoso() {
        List<Course> courses = Arrays.asList(course);
        when(generalManagementService.getAllCourses()).thenReturn(courses);

        ResponseEntity<List<Course>> respuesta = generalManagementController.getAllCourses();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(courses, respuesta.getBody());
        verify(generalManagementService, times(1)).getAllCourses();
    }

    @Test
    @DisplayName("Caso error - Obtener cursos con excepción")
    void testGetAllCourses_Error() {
        when(generalManagementService.getAllCourses()).thenThrow(new RuntimeException("Error"));

        ResponseEntity<List<Course>> respuesta = generalManagementController.getAllCourses();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).getAllCourses();
    }

    @Test
    @DisplayName("Caso exitoso - Obtener curso por código")
    void testGetCourseByCode_Exitoso() {
        when(generalManagementService.getCourseByCode("CS101")).thenReturn(course);

        ResponseEntity<Course> respuesta = generalManagementController.getCourseByCode("CS101");

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(course, respuesta.getBody());
        verify(generalManagementService, times(1)).getCourseByCode("CS101");
    }

    @Test
    @DisplayName("Caso error - Obtener curso por código no encontrado")
    void testGetCourseByCode_NoEncontrado() {
        when(generalManagementService.getCourseByCode("CS999")).thenThrow(new AppException("No encontrado"));

        ResponseEntity<Course> respuesta = generalManagementController.getCourseByCode("CS999");

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).getCourseByCode("CS999");
    }

    @Test
    @DisplayName("Caso exitoso - Obtener cursos activos")
    void testGetActiveCourses_Exitoso() {
        List<Course> courses = Arrays.asList(course);
        when(generalManagementService.getActiveCourses()).thenReturn(courses);

        ResponseEntity<List<Course>> respuesta = generalManagementController.getActiveCourses();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(courses, respuesta.getBody());
        verify(generalManagementService, times(1)).getActiveCourses();
    }

    @Test
    @DisplayName("Caso error - Obtener cursos activos con excepción")
    void testGetActiveCourses_Error() {
        when(generalManagementService.getActiveCourses()).thenThrow(new RuntimeException("Error"));

        ResponseEntity<List<Course>> respuesta = generalManagementController.getActiveCourses();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).getActiveCourses();
    }

    @Test
    @DisplayName("Caso exitoso - Obtener cursos por programa")
    void testGetCoursesByProgram_Exitoso() {
        List<Course> courses = Arrays.asList(course);
        when(generalManagementService.getCoursesByProgram("Ingeniería de Sistemas")).thenReturn(courses);

        ResponseEntity<List<Course>> respuesta = generalManagementController.getCoursesByProgram("Ingeniería de Sistemas");

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(courses, respuesta.getBody());
        verify(generalManagementService, times(1)).getCoursesByProgram("Ingeniería de Sistemas");
    }

    @Test
    @DisplayName("Caso error - Obtener cursos por programa con excepción")
    void testGetCoursesByProgram_Error() {
        when(generalManagementService.getCoursesByProgram("Ingeniería de Sistemas")).thenThrow(new RuntimeException("Error"));

        ResponseEntity<List<Course>> respuesta = generalManagementController.getCoursesByProgram("Ingeniería de Sistemas");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).getCoursesByProgram("Ingeniería de Sistemas");
    }

    @Test
    @DisplayName("Caso exitoso - Actualizar curso")
    void testUpdateCourse_Exitoso() {
        when(generalManagementService.updateCourse("CS101", course)).thenReturn(course);

        ResponseEntity<Course> respuesta = generalManagementController.updateCourse("CS101", course);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(course, respuesta.getBody());
        verify(generalManagementService, times(1)).updateCourse("CS101", course);
    }

    @Test
    @DisplayName("Caso error - Actualizar curso con excepción")
    void testUpdateCourse_Error() {
        when(generalManagementService.updateCourse("CS101", course)).thenThrow(new RuntimeException("Error"));

        ResponseEntity<Course> respuesta = generalManagementController.updateCourse("CS101", course);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).updateCourse("CS101", course);
    }

    @Test
    @DisplayName("Caso exitoso - Eliminar curso")
    void testDeleteCourse_Exitoso() {
        doNothing().when(generalManagementService).deleteCourse("CS101");

        ResponseEntity<Void> respuesta = generalManagementController.deleteCourse("CS101");

        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).deleteCourse("CS101");
    }

    @Test
    @DisplayName("Caso error - Eliminar curso con excepción")
    void testDeleteCourse_Error() {
        doThrow(new RuntimeException("Error")).when(generalManagementService).deleteCourse("CS101");

        ResponseEntity<Void> respuesta = generalManagementController.deleteCourse("CS101");

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        verify(generalManagementService, times(1)).deleteCourse("CS101");
    }

    @Test
    @DisplayName("Caso exitoso - Cambiar estado de curso")
    void testToggleCourseStatus_Exitoso() {
        when(generalManagementService.toggleCourseStatus("CS101", true)).thenReturn(course);

        ResponseEntity<Course> respuesta = generalManagementController.toggleCourseStatus("CS101", true);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(course, respuesta.getBody());
        verify(generalManagementService, times(1)).toggleCourseStatus("CS101", true);
    }

    @Test
    @DisplayName("Caso error - Cambiar estado de curso con excepción")
    void testToggleCourseStatus_Error() {
        when(generalManagementService.toggleCourseStatus("CS101", true)).thenThrow(new RuntimeException("Error"));

        ResponseEntity<Course> respuesta = generalManagementController.toggleCourseStatus("CS101", true);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).toggleCourseStatus("CS101", true);
    }

    @Test
    @DisplayName("Caso exitoso - Health check")
    void testHealthCheck_Exitoso() {
        ResponseEntity<String> respuesta = generalManagementController.healthCheck();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals("General Management Controller está funcionando correctamente", respuesta.getBody());
    }

    @Test
    @DisplayName("Caso exitoso - Crear detalle de estado de curso")
    void testCreateCourseStatusDetail_Exitoso() {
        when(generalManagementService.createCourseStatusDetail(courseStatusDetail)).thenReturn(courseStatusDetail);

        ResponseEntity<CourseStatusDetail> respuesta = generalManagementController.createCourseStatusDetail(courseStatusDetail);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertEquals(courseStatusDetail, respuesta.getBody());
        verify(generalManagementService, times(1)).createCourseStatusDetail(courseStatusDetail);
    }

    @Test
    @DisplayName("Caso exitoso - Obtener estadísticas del sistema")
    void testGetSystemStatistics_Exitoso() {
        GeneralManagementService.SystemStatistics stats = new GeneralManagementService.SystemStatistics();
        stats.setTotalClassrooms(10);
        stats.setTotalCourses(50);

        when(generalManagementService.getSystemStatistics()).thenReturn(stats);

        ResponseEntity<GeneralManagementService.SystemStatistics> respuesta = generalManagementController.getSystemStatistics();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(stats, respuesta.getBody());
        verify(generalManagementService, times(1)).getSystemStatistics();
    }

    @Test
    @DisplayName("Caso error - Obtener estadísticas del sistema con excepción")
    void testGetSystemStatistics_Error() {
        when(generalManagementService.getSystemStatistics()).thenThrow(new RuntimeException("Error"));

        ResponseEntity<GeneralManagementService.SystemStatistics> respuesta = generalManagementController.getSystemStatistics();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).getSystemStatistics();
    }

    @Test
    @DisplayName("Caso exitoso - Verificar integridad referencial")
    void testCheckReferentialIntegrity_Exitoso() {
        when(generalManagementService.checkReferentialIntegrity("classroom", "1")).thenReturn(true);

        ResponseEntity<Boolean> respuesta = generalManagementController.checkReferentialIntegrity("classroom", "1");

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertTrue(respuesta.getBody());
        verify(generalManagementService, times(1)).checkReferentialIntegrity("classroom", "1");
    }

    @Test
    @DisplayName("Caso error - Verificar integridad referencial con excepción")
    void testCheckReferentialIntegrity_Error() {
        when(generalManagementService.checkReferentialIntegrity("classroom", "1")).thenThrow(new RuntimeException("Error"));

        ResponseEntity<Boolean> respuesta = generalManagementController.checkReferentialIntegrity("classroom", "1");

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).checkReferentialIntegrity("classroom", "1");
    }

    // Nuevos tests (28 adicionales)
    @Test
    @DisplayName("Caso exitoso - Obtener todos los detalles de estado de curso")
    void testGetAllCourseStatusDetails_Exitoso() {
        List<CourseStatusDetail> details = Arrays.asList(courseStatusDetail);
        when(generalManagementService.getAllCourseStatusDetails()).thenReturn(details);

        ResponseEntity<List<CourseStatusDetail>> respuesta = generalManagementController.getAllCourseStatusDetails();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(details, respuesta.getBody());
        verify(generalManagementService, times(1)).getAllCourseStatusDetails();
    }

    @Test
    @DisplayName("Caso error - Obtener detalles de estado de curso con excepción")
    void testGetAllCourseStatusDetails_Error() {
        when(generalManagementService.getAllCourseStatusDetails()).thenThrow(new RuntimeException("Error"));

        ResponseEntity<List<CourseStatusDetail>> respuesta = generalManagementController.getAllCourseStatusDetails();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).getAllCourseStatusDetails();
    }

    @Test
    @DisplayName("Caso exitoso - Obtener detalle de estado de curso por ID")
    void testGetCourseStatusDetailById_Exitoso() {
        when(generalManagementService.getCourseStatusDetailById("1")).thenReturn(courseStatusDetail);

        ResponseEntity<CourseStatusDetail> respuesta = generalManagementController.getCourseStatusDetailById("1");

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(courseStatusDetail, respuesta.getBody());
        verify(generalManagementService, times(1)).getCourseStatusDetailById("1");
    }

    @Test
    @DisplayName("Caso error - Obtener detalle de estado de curso por ID no encontrado")
    void testGetCourseStatusDetailById_NoEncontrado() {
        when(generalManagementService.getCourseStatusDetailById("99")).thenThrow(new AppException("No encontrado"));

        ResponseEntity<CourseStatusDetail> respuesta = generalManagementController.getCourseStatusDetailById("99");

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).getCourseStatusDetailById("99");
    }

    @Test
    @DisplayName("Caso exitoso - Actualizar detalle de estado de curso")
    void testUpdateCourseStatusDetail_Exitoso() {
        when(generalManagementService.updateCourseStatusDetail("1", courseStatusDetail)).thenReturn(courseStatusDetail);

        ResponseEntity<CourseStatusDetail> respuesta = generalManagementController.updateCourseStatusDetail("1", courseStatusDetail);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(courseStatusDetail, respuesta.getBody());
        verify(generalManagementService, times(1)).updateCourseStatusDetail("1", courseStatusDetail);
    }

    @Test
    @DisplayName("Caso error - Actualizar detalle de estado de curso con excepción")
    void testUpdateCourseStatusDetail_Error() {
        when(generalManagementService.updateCourseStatusDetail("1", courseStatusDetail)).thenThrow(new RuntimeException("Error"));

        ResponseEntity<CourseStatusDetail> respuesta = generalManagementController.updateCourseStatusDetail("1", courseStatusDetail);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).updateCourseStatusDetail("1", courseStatusDetail);
    }

    @Test
    @DisplayName("Caso exitoso - Eliminar detalle de estado de curso")
    void testDeleteCourseStatusDetail_Exitoso() {
        doNothing().when(generalManagementService).deleteCourseStatusDetail("1");

        ResponseEntity<Void> respuesta = generalManagementController.deleteCourseStatusDetail("1");

        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).deleteCourseStatusDetail("1");
    }

    @Test
    @DisplayName("Caso error - Eliminar detalle de estado de curso con excepción")
    void testDeleteCourseStatusDetail_Error() {
        doThrow(new RuntimeException("Error")).when(generalManagementService).deleteCourseStatusDetail("1");

        ResponseEntity<Void> respuesta = generalManagementController.deleteCourseStatusDetail("1");

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        verify(generalManagementService, times(1)).deleteCourseStatusDetail("1");
    }

    @Test
    @DisplayName("Caso exitoso - Obtener detalles de estado de curso por estudiante")
    void testGetCourseStatusDetailsByStudent_Exitoso() {
        List<CourseStatusDetail> details = Arrays.asList(courseStatusDetail);
        when(generalManagementService.getCourseStatusDetailsByStudent("STU001")).thenReturn(details);

        ResponseEntity<List<CourseStatusDetail>> respuesta = generalManagementController.getCourseStatusDetailsByStudent("STU001");

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(details, respuesta.getBody());
        verify(generalManagementService, times(1)).getCourseStatusDetailsByStudent("STU001");
    }

    @Test
    @DisplayName("Caso error - Obtener detalles de estado de curso por estudiante con excepción")
    void testGetCourseStatusDetailsByStudent_Error() {
        when(generalManagementService.getCourseStatusDetailsByStudent("STU001")).thenThrow(new RuntimeException("Error"));

        ResponseEntity<List<CourseStatusDetail>> respuesta = generalManagementController.getCourseStatusDetailsByStudent("STU001");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).getCourseStatusDetailsByStudent("STU001");
    }

    @Test
    @DisplayName("Caso exitoso - Obtener detalles de estado de curso por curso")
    void testGetCourseStatusDetailsByCourse_Exitoso() {
        List<CourseStatusDetail> details = Arrays.asList(courseStatusDetail);
        when(generalManagementService.getCourseStatusDetailsByCourse("CS101")).thenReturn(details);

        ResponseEntity<List<CourseStatusDetail>> respuesta = generalManagementController.getCourseStatusDetailsByCourse("CS101");

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(details, respuesta.getBody());
        verify(generalManagementService, times(1)).getCourseStatusDetailsByCourse("CS101");
    }

    @Test
    @DisplayName("Caso error - Obtener detalles de estado de curso por curso con excepción")
    void testGetCourseStatusDetailsByCourse_Error() {
        when(generalManagementService.getCourseStatusDetailsByCourse("CS101")).thenThrow(new RuntimeException("Error"));

        ResponseEntity<List<CourseStatusDetail>> respuesta = generalManagementController.getCourseStatusDetailsByCourse("CS101");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).getCourseStatusDetailsByCourse("CS101");
    }

    @Test
    @DisplayName("Caso exitoso - Obtener detalles de estado de curso por semestre")
    void testGetCourseStatusDetailsBySemester_Exitoso() {
        List<CourseStatusDetail> details = Arrays.asList(courseStatusDetail);
        when(generalManagementService.getCourseStatusDetailsBySemester("2025-1")).thenReturn(details);

        ResponseEntity<List<CourseStatusDetail>> respuesta = generalManagementController.getCourseStatusDetailsBySemester("2025-1");

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(details, respuesta.getBody());
        verify(generalManagementService, times(1)).getCourseStatusDetailsBySemester("2025-1");
    }

    @Test
    @DisplayName("Caso error - Obtener detalles de estado de curso por semestre con excepción")
    void testGetCourseStatusDetailsBySemester_Error() {
        when(generalManagementService.getCourseStatusDetailsBySemester("2025-1")).thenThrow(new RuntimeException("Error"));

        ResponseEntity<List<CourseStatusDetail>> respuesta = generalManagementController.getCourseStatusDetailsBySemester("2025-1");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).getCourseStatusDetailsBySemester("2025-1");
    }

    @Test
    @DisplayName("Caso exitoso - Eliminar grupo")
    void testDeleteGroup_Exitoso() {
        doNothing().when(generalManagementService).deleteGroup("1");

        ResponseEntity<Void> respuesta = generalManagementController.deleteGroup("1");

        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).deleteGroup("1");
    }

    @Test
    @DisplayName("Caso error - Eliminar grupo con excepción")
    void testDeleteGroup_Error() {
        doThrow(new RuntimeException("Error")).when(generalManagementService).deleteGroup("1");

        ResponseEntity<Void> respuesta = generalManagementController.deleteGroup("1");

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        verify(generalManagementService, times(1)).deleteGroup("1");
    }

    @Test
    @DisplayName("Caso exitoso - Crear paso de revisión")
    void testCreateReviewStep_Exitoso() {
        when(generalManagementService.createReviewStep(reviewStep)).thenReturn(reviewStep);

        ResponseEntity<ReviewStep> respuesta = generalManagementController.createReviewStep(reviewStep);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertEquals(reviewStep, respuesta.getBody());
        verify(generalManagementService, times(1)).createReviewStep(reviewStep);
    }

    @Test
    @DisplayName("Caso error - Crear paso de revisión con excepción")
    void testCreateReviewStep_Error() {
        when(generalManagementService.createReviewStep(reviewStep)).thenThrow(new RuntimeException("Error"));

        ResponseEntity<ReviewStep> respuesta = generalManagementController.createReviewStep(reviewStep);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).createReviewStep(reviewStep);
    }

    @Test
    @DisplayName("Caso exitoso - Obtener todos los pasos de revisión")
    void testGetAllReviewSteps_Exitoso() {
        List<ReviewStep> steps = Arrays.asList(reviewStep);
        when(generalManagementService.getAllReviewSteps()).thenReturn(steps);

        ResponseEntity<List<ReviewStep>> respuesta = generalManagementController.getAllReviewSteps();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(steps, respuesta.getBody());
        verify(generalManagementService, times(1)).getAllReviewSteps();
    }

    @Test
    @DisplayName("Caso error - Obtener pasos de revisión con excepción")
    void testGetAllReviewSteps_Error() {
        when(generalManagementService.getAllReviewSteps()).thenThrow(new RuntimeException("Error"));

        ResponseEntity<List<ReviewStep>> respuesta = generalManagementController.getAllReviewSteps();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).getAllReviewSteps();
    }

    @Test
    @DisplayName("Caso exitoso - Obtener paso de revisión por ID")
    void testGetReviewStepById_Exitoso() {
        when(generalManagementService.getReviewStepById("1")).thenReturn(reviewStep);

        ResponseEntity<ReviewStep> respuesta = generalManagementController.getReviewStepById("1");

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(reviewStep, respuesta.getBody());
        verify(generalManagementService, times(1)).getReviewStepById("1");
    }

    @Test
    @DisplayName("Caso error - Obtener paso de revisión por ID no encontrado")
    void testGetReviewStepById_NoEncontrado() {
        when(generalManagementService.getReviewStepById("99")).thenThrow(new AppException("No encontrado"));

        ResponseEntity<ReviewStep> respuesta = generalManagementController.getReviewStepById("99");

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).getReviewStepById("99");
    }

    @Test
    @DisplayName("Caso exitoso - Obtener pasos de revisión por usuario")
    void testGetReviewStepsByUser_Exitoso() {
        List<ReviewStep> steps = Arrays.asList(reviewStep);
        when(generalManagementService.getReviewStepsByUser("USER001")).thenReturn(steps);

        ResponseEntity<List<ReviewStep>> respuesta = generalManagementController.getReviewStepsByUser("USER001");

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(steps, respuesta.getBody());
        verify(generalManagementService, times(1)).getReviewStepsByUser("USER001");
    }

    @Test
    @DisplayName("Caso error - Obtener pasos de revisión por usuario con excepción")
    void testGetReviewStepsByUser_Error() {
        when(generalManagementService.getReviewStepsByUser("USER001")).thenThrow(new RuntimeException("Error"));

        ResponseEntity<List<ReviewStep>> respuesta = generalManagementController.getReviewStepsByUser("USER001");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).getReviewStepsByUser("USER001");
    }

    @Test
    @DisplayName("Caso exitoso - Obtener pasos de revisión por rol de usuario")
    void testGetReviewStepsByUserRole_Exitoso() {
        List<ReviewStep> steps = Arrays.asList(reviewStep);
        when(generalManagementService.getReviewStepsByUserRole("PROFESSOR")).thenReturn(steps);

        ResponseEntity<List<ReviewStep>> respuesta = generalManagementController.getReviewStepsByUserRole("PROFESSOR");

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(steps, respuesta.getBody());
        verify(generalManagementService, times(1)).getReviewStepsByUserRole("PROFESSOR");
    }

    @Test
    @DisplayName("Caso error - Obtener pasos de revisión por rol de usuario con excepción")
    void testGetReviewStepsByUserRole_Error() {
        when(generalManagementService.getReviewStepsByUserRole("PROFESSOR")).thenThrow(new RuntimeException("Error"));

        ResponseEntity<List<ReviewStep>> respuesta = generalManagementController.getReviewStepsByUserRole("PROFESSOR");

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).getReviewStepsByUserRole("PROFESSOR");
    }

    @Test
    @DisplayName("Caso exitoso - Crear horario")
    void testCreateSchedule_Exitoso() {
        when(generalManagementService.createSchedule(schedule)).thenReturn(schedule);

        ResponseEntity<Schedule> respuesta = generalManagementController.createSchedule(schedule);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertEquals(schedule, respuesta.getBody());
        verify(generalManagementService, times(1)).createSchedule(schedule);
    }

    @Test
    @DisplayName("Caso error - Crear horario con excepción")
    void testCreateSchedule_Error() {
        when(generalManagementService.createSchedule(schedule)).thenThrow(new RuntimeException("Error"));

        ResponseEntity<Schedule> respuesta = generalManagementController.createSchedule(schedule);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).createSchedule(schedule);
    }

    @Test
    @DisplayName("Caso exitoso - Obtener todos los horarios")
    void testGetAllSchedules_Exitoso() {
        List<Schedule> schedules = Arrays.asList(schedule);
        when(generalManagementService.getAllSchedules()).thenReturn(schedules);

        ResponseEntity<List<Schedule>> respuesta = generalManagementController.getAllSchedules();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(schedules, respuesta.getBody());
        verify(generalManagementService, times(1)).getAllSchedules();
    }

    @Test
    @DisplayName("Caso error - Obtener horarios con excepción")
    void testGetAllSchedules_Error() {
        when(generalManagementService.getAllSchedules()).thenThrow(new RuntimeException("Error"));

        ResponseEntity<List<Schedule>> respuesta = generalManagementController.getAllSchedules();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).getAllSchedules();
    }

    @Test
    @DisplayName("Caso exitoso - Obtener horario por ID")
    void testGetScheduleById_Exitoso() {
        when(generalManagementService.getScheduleById("1")).thenReturn(schedule);

        ResponseEntity<Schedule> respuesta = generalManagementController.getScheduleById("1");

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(schedule, respuesta.getBody());
        verify(generalManagementService, times(1)).getScheduleById("1");
    }

    @Test
    @DisplayName("Caso error - Obtener horario por ID no encontrado")
    void testGetScheduleById_NoEncontrado() {
        when(generalManagementService.getScheduleById("99")).thenThrow(new AppException("No encontrado"));

        ResponseEntity<Schedule> respuesta = generalManagementController.getScheduleById("99");

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).getScheduleById("99");
    }

    @Test
    @DisplayName("Caso exitoso - Actualizar horario")
    void testUpdateSchedule_Exitoso() {
        when(generalManagementService.updateSchedule("1", schedule)).thenReturn(schedule);

        ResponseEntity<Schedule> respuesta = generalManagementController.updateSchedule("1", schedule);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(schedule, respuesta.getBody());
        verify(generalManagementService, times(1)).updateSchedule("1", schedule);
    }

    @Test
    @DisplayName("Caso error - Actualizar horario con excepción")
    void testUpdateSchedule_Error() {
        when(generalManagementService.updateSchedule("1", schedule)).thenThrow(new RuntimeException("Error"));

        ResponseEntity<Schedule> respuesta = generalManagementController.updateSchedule("1", schedule);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).updateSchedule("1", schedule);
    }

    @Test
    @DisplayName("Caso exitoso - Eliminar horario")
    void testDeleteSchedule_Exitoso() {
        doNothing().when(generalManagementService).deleteSchedule("1");

        ResponseEntity<Void> respuesta = generalManagementController.deleteSchedule("1");

        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).deleteSchedule("1");
    }

    @Test
    @DisplayName("Caso error - Eliminar horario con excepción")
    void testDeleteSchedule_Error() {
        doThrow(new RuntimeException("Error")).when(generalManagementService).deleteSchedule("1");

        ResponseEntity<Void> respuesta = generalManagementController.deleteSchedule("1");

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        verify(generalManagementService, times(1)).deleteSchedule("1");
    }

    @Test
    @DisplayName("Caso exitoso - Obtener horarios por día")
    void testGetSchedulesByDay_Exitoso() {
        List<Schedule> schedules = Arrays.asList(schedule);
        when(generalManagementService.getSchedulesByDay("LUNES")).thenReturn(schedules);

        ResponseEntity<List<Schedule>> respuesta = generalManagementController.getSchedulesByDay("LUNES");

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(schedules, respuesta.getBody());
        verify(generalManagementService, times(1)).getSchedulesByDay("LUNES");
    }

    @Test
    @DisplayName("Caso error - Obtener horarios por día con excepción")
    void testGetSchedulesByDay_Error() {
        when(generalManagementService.getSchedulesByDay("LUNES")).thenThrow(new RuntimeException("Error"));

        ResponseEntity<List<Schedule>> respuesta = generalManagementController.getSchedulesByDay("LUNES");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).getSchedulesByDay("LUNES");
    }

    @Test
    @DisplayName("Caso exitoso - Crear progreso académico de estudiante")
    void testCreateStudentAcademicProgress_Exitoso() {
        when(generalManagementService.createStudentAcademicProgress(studentAcademicProgress)).thenReturn(studentAcademicProgress);

        ResponseEntity<StudentAcademicProgress> respuesta = generalManagementController.createStudentAcademicProgress(studentAcademicProgress);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertEquals(studentAcademicProgress, respuesta.getBody());
        verify(generalManagementService, times(1)).createStudentAcademicProgress(studentAcademicProgress);
    }

    @Test
    @DisplayName("Caso error - Crear progreso académico de estudiante con excepción")
    void testCreateStudentAcademicProgress_Error() {
        when(generalManagementService.createStudentAcademicProgress(studentAcademicProgress)).thenThrow(new RuntimeException("Error"));

        ResponseEntity<StudentAcademicProgress> respuesta = generalManagementController.createStudentAcademicProgress(studentAcademicProgress);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).createStudentAcademicProgress(studentAcademicProgress);
    }

    @Test
    @DisplayName("Caso exitoso - Obtener todos los progresos académicos")
    void testGetAllStudentAcademicProgress_Exitoso() {
        List<StudentAcademicProgress> progressList = Arrays.asList(studentAcademicProgress);
        when(generalManagementService.getAllStudentAcademicProgress()).thenReturn(progressList);

        ResponseEntity<List<StudentAcademicProgress>> respuesta = generalManagementController.getAllStudentAcademicProgress();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(progressList, respuesta.getBody());
        verify(generalManagementService, times(1)).getAllStudentAcademicProgress();
    }

    @Test
    @DisplayName("Caso error - Obtener progresos académicos con excepción")
    void testGetAllStudentAcademicProgress_Error() {
        when(generalManagementService.getAllStudentAcademicProgress()).thenThrow(new RuntimeException("Error"));

        ResponseEntity<List<StudentAcademicProgress>> respuesta = generalManagementController.getAllStudentAcademicProgress();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).getAllStudentAcademicProgress();
    }

    @Test
    @DisplayName("Caso exitoso - Obtener progreso académico por ID")
    void testGetStudentAcademicProgressById_Exitoso() {
        when(generalManagementService.getStudentAcademicProgressById("1")).thenReturn(studentAcademicProgress);

        ResponseEntity<StudentAcademicProgress> respuesta = generalManagementController.getStudentAcademicProgressById("1");

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(studentAcademicProgress, respuesta.getBody());
        verify(generalManagementService, times(1)).getStudentAcademicProgressById("1");
    }

    @Test
    @DisplayName("Caso error - Obtener progreso académico por ID no encontrado")
    void testGetStudentAcademicProgressById_NoEncontrado() {
        when(generalManagementService.getStudentAcademicProgressById("99")).thenThrow(new AppException("No encontrado"));

        ResponseEntity<StudentAcademicProgress> respuesta = generalManagementController.getStudentAcademicProgressById("99");

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).getStudentAcademicProgressById("99");
    }

    @Test
    @DisplayName("Caso exitoso - Actualizar progreso académico")
    void testUpdateStudentAcademicProgress_Exitoso() {
        when(generalManagementService.updateStudentAcademicProgress("1", studentAcademicProgress)).thenReturn(studentAcademicProgress);

        ResponseEntity<StudentAcademicProgress> respuesta = generalManagementController.updateStudentAcademicProgress("1", studentAcademicProgress);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(studentAcademicProgress, respuesta.getBody());
        verify(generalManagementService, times(1)).updateStudentAcademicProgress("1", studentAcademicProgress);
    }

    @Test
    @DisplayName("Caso error - Actualizar progreso académico con excepción")
    void testUpdateStudentAcademicProgress_Error() {
        when(generalManagementService.updateStudentAcademicProgress("1", studentAcademicProgress)).thenThrow(new RuntimeException("Error"));

        ResponseEntity<StudentAcademicProgress> respuesta = generalManagementController.updateStudentAcademicProgress("1", studentAcademicProgress);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).updateStudentAcademicProgress("1", studentAcademicProgress);
    }

    @Test
    @DisplayName("Caso exitoso - Eliminar progreso académico")
    void testDeleteStudentAcademicProgress_Exitoso() {
        doNothing().when(generalManagementService).deleteStudentAcademicProgress("1");

        ResponseEntity<Void> respuesta = generalManagementController.deleteStudentAcademicProgress("1");

        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).deleteStudentAcademicProgress("1");
    }

    @Test
    @DisplayName("Caso error - Eliminar progreso académico con excepción")
    void testDeleteStudentAcademicProgress_Error() {
        doThrow(new RuntimeException("Error")).when(generalManagementService).deleteStudentAcademicProgress("1");

        ResponseEntity<Void> respuesta = generalManagementController.deleteStudentAcademicProgress("1");

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        verify(generalManagementService, times(1)).deleteStudentAcademicProgress("1");
    }

    @Test
    @DisplayName("Caso exitoso - Obtener progreso académico por estudiante")
    void testGetStudentAcademicProgressByStudentId_Exitoso() {
        when(generalManagementService.getStudentAcademicProgressByStudentId("STU001")).thenReturn(studentAcademicProgress);

        ResponseEntity<StudentAcademicProgress> respuesta = generalManagementController.getStudentAcademicProgressByStudentId("STU001");

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(studentAcademicProgress, respuesta.getBody());
        verify(generalManagementService, times(1)).getStudentAcademicProgressByStudentId("STU001");
    }

    @Test
    @DisplayName("Caso error - Obtener progreso académico por estudiante no encontrado")
    void testGetStudentAcademicProgressByStudentId_NoEncontrado() {
        when(generalManagementService.getStudentAcademicProgressByStudentId("STU999")).thenThrow(new AppException("No encontrado"));

        ResponseEntity<StudentAcademicProgress> respuesta = generalManagementController.getStudentAcademicProgressByStudentId("STU999");

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).getStudentAcademicProgressByStudentId("STU999");
    }

    @Test
    @DisplayName("Caso exitoso - Obtener progresos académicos por facultad")
    void testGetStudentAcademicProgressByFaculty_Exitoso() {
        List<StudentAcademicProgress> progressList = Arrays.asList(studentAcademicProgress);
        when(generalManagementService.getStudentAcademicProgressByFaculty("Ingeniería")).thenReturn(progressList);

        ResponseEntity<List<StudentAcademicProgress>> respuesta = generalManagementController.getStudentAcademicProgressByFaculty("Ingeniería");

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(progressList, respuesta.getBody());
        verify(generalManagementService, times(1)).getStudentAcademicProgressByFaculty("Ingeniería");
    }

    @Test
    @DisplayName("Caso error - Obtener progresos académicos por facultad con excepción")
    void testGetStudentAcademicProgressByFaculty_Error() {
        when(generalManagementService.getStudentAcademicProgressByFaculty("Ingeniería")).thenThrow(new RuntimeException("Error"));

        ResponseEntity<List<StudentAcademicProgress>> respuesta = generalManagementController.getStudentAcademicProgressByFaculty("Ingeniería");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).getStudentAcademicProgressByFaculty("Ingeniería");
    }

    @Test
    @DisplayName("Caso exitoso - Obtener progresos académicos por programa")
    void testGetStudentAcademicProgressByProgram_Exitoso() {
        List<StudentAcademicProgress> progressList = Arrays.asList(studentAcademicProgress);
        when(generalManagementService.getStudentAcademicProgressByProgram("Ingeniería de Sistemas")).thenReturn(progressList);

        ResponseEntity<List<StudentAcademicProgress>> respuesta = generalManagementController.getStudentAcademicProgressByProgram("Ingeniería de Sistemas");

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(progressList, respuesta.getBody());
        verify(generalManagementService, times(1)).getStudentAcademicProgressByProgram("Ingeniería de Sistemas");
    }

    @Test
    @DisplayName("Caso error - Obtener progresos académicos por programa con excepción")
    void testGetStudentAcademicProgressByProgram_Error() {
        when(generalManagementService.getStudentAcademicProgressByProgram("Ingeniería de Sistemas")).thenThrow(new RuntimeException("Error"));

        ResponseEntity<List<StudentAcademicProgress>> respuesta = generalManagementController.getStudentAcademicProgressByProgram("Ingeniería de Sistemas");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(generalManagementService, times(1)).getStudentAcademicProgressByProgram("Ingeniería de Sistemas");
    }
}
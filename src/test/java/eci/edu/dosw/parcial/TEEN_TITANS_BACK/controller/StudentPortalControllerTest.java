package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto.StudentProgressDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.StudentPortalService;
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

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentPortalControllerTest {

    @Mock
    private StudentPortalService studentPortalService;

    @InjectMocks
    private StudentPortalController studentPortalController;

    private final String STUDENT_ID = "student-123";
    private final String COURSE_CODE = "CS101";
    private final String GROUP_ID = "group-456";

    private Student testStudent;
    private Course testCourse;
    private Group testGroup;
    private AcademicPeriod testPeriod;
    private StudentAcademicProgress testProgress;
    private CourseStatusDetail testCourseStatus;

    @BeforeEach
    void setUp() {
        testStudent = new Student(STUDENT_ID, "Juan Pérez", "juan@estudiante.edu.co", "password", "Ingeniería de Sistemas", 5);
        testCourse = new Course(COURSE_CODE, "Programación I", 3, "Curso introductorio de programación", "Ingeniería de Sistemas", true);
        testGroup = new Group(GROUP_ID, "A", testCourse, null, null, null);
        testPeriod = new AcademicPeriod("2025-1", "Primer Semestre 2025", new Date(), new Date(), new Date(), new Date(), new Date(), new Date(), true);


        testCourseStatus = new CourseStatusDetail(
                "cs-001", testCourse, CourseStatus.PASSED, 4.0, "2024-1",
                new Date(), new Date(), testGroup, null, 3, true, "Excelente desempeño"
        );

        testProgress = new StudentAcademicProgress(
                "progress-123", testStudent, "Ingeniería de Sistemas", "Facultad de Ingeniería",
                "Nuevo Plan", 5, 10, 75, 160, 4.2, List.of(testCourseStatus)
        );
    }

    @Test
    @DisplayName("Caso exitoso - getCurrentSchedule retorna horario del estudiante")
    void testGetCurrentSchedule_Exitoso() {
        List<Group> schedule = List.of(testGroup);
        when(studentPortalService.getCurrentSchedule(STUDENT_ID)).thenReturn(schedule);

        ResponseEntity<?> response = studentPortalController.getCurrentSchedule(STUDENT_ID);

        assertAll("Verificar respuesta exitosa de getCurrentSchedule",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(schedule, response.getBody()),
                () -> verify(studentPortalService, times(1)).getCurrentSchedule(STUDENT_ID)
        );
    }

    @Test
    @DisplayName("Caso exitoso - getAvailableGroups retorna grupos disponibles")
    void testGetAvailableGroups_Exitoso() {
        List<Group> availableGroups = List.of(testGroup);
        when(studentPortalService.getAvailableGroups(COURSE_CODE)).thenReturn(availableGroups);

        ResponseEntity<?> response = studentPortalController.getAvailableGroups(COURSE_CODE);

        assertAll("Verificar respuesta exitosa de getAvailableGroups",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(availableGroups, response.getBody()),
                () -> verify(studentPortalService, times(1)).getAvailableGroups(COURSE_CODE)
        );
    }

    @Test
    @DisplayName("Caso exitoso - getAcademicProgress retorna progreso académico")
    void testGetAcademicProgress_Exitoso() {
        when(studentPortalService.getAcademicProgress(STUDENT_ID)).thenReturn(testProgress);

        ResponseEntity<?> response = studentPortalController.getAcademicProgress(STUDENT_ID);

        assertAll("Verificar respuesta exitosa de getAcademicProgress",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(StudentProgressDTO.class, response.getBody()),
                () -> verify(studentPortalService, times(1)).getAcademicProgress(STUDENT_ID)
        );
    }

    @Test
    @DisplayName("Caso exitoso - checkGroupAvailability retorna disponibilidad")
    void testCheckGroupAvailability_Exitoso() {
        when(studentPortalService.checkGroupAvailability(GROUP_ID)).thenReturn(true);

        ResponseEntity<?> response = studentPortalController.checkGroupAvailability(GROUP_ID);

        assertAll("Verificar respuesta exitosa de checkGroupAvailability",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> verify(studentPortalService, times(1)).checkGroupAvailability(GROUP_ID)
        );
    }

    @Test
    @DisplayName("Caso exitoso - getCourseRecommendations retorna recomendaciones")
    void testGetCourseRecommendations_Exitoso() {
        List<Course> recommendations = List.of(testCourse);
        when(studentPortalService.getCourseRecommendations(STUDENT_ID)).thenReturn(recommendations);

        ResponseEntity<?> response = studentPortalController.getCourseRecommendations(STUDENT_ID);

        assertAll("Verificar respuesta exitosa de getCourseRecommendations",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(recommendations, response.getBody()),
                () -> verify(studentPortalService, times(1)).getCourseRecommendations(STUDENT_ID)
        );
    }

    @Test
    @DisplayName("Caso exitoso - getEnrollmentDeadlines retorna periodo académico")
    void testGetEnrollmentDeadlines_Exitoso() {
        when(studentPortalService.getEnrollmentDeadlines()).thenReturn(testPeriod);

        ResponseEntity<?> response = studentPortalController.getEnrollmentDeadlines();

        assertAll("Verificar respuesta exitosa de getEnrollmentDeadlines",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(testPeriod, response.getBody()),
                () -> verify(studentPortalService, times(1)).getEnrollmentDeadlines()
        );
    }

    @Test
    @DisplayName("Caso exitoso - getAcademicAlerts retorna alertas del estudiante")
    void testGetAcademicAlerts_Exitoso() {
        List<String> alerts = List.of("Bajo rendimiento en Matemáticas", "Faltan 2 créditos para graduación");
        when(studentPortalService.getAcademicAlerts(STUDENT_ID)).thenReturn(alerts);

        ResponseEntity<?> response = studentPortalController.getAcademicAlerts(STUDENT_ID);

        assertAll("Verificar respuesta exitosa de getAcademicAlerts",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> verify(studentPortalService, times(1)).getAcademicAlerts(STUDENT_ID)
        );
    }

    @Test
    @DisplayName("Caso exitoso - getGroupCapacityInfo retorna información de capacidad")
    void testGetGroupCapacityInfo_Exitoso() {
        when(studentPortalService.getMaxCapacity(GROUP_ID)).thenReturn(30);
        when(studentPortalService.getCurrentEnrollment(GROUP_ID)).thenReturn(25);
        when(studentPortalService.checkGroupAvailability(GROUP_ID)).thenReturn(true);

        ResponseEntity<?> response = studentPortalController.getGroupCapacityInfo(GROUP_ID);

        assertAll("Verificar respuesta exitosa de getGroupCapacityInfo",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> verify(studentPortalService, times(1)).getMaxCapacity(GROUP_ID),
                () -> verify(studentPortalService, times(1)).getCurrentEnrollment(GROUP_ID),
                () -> verify(studentPortalService, times(1)).checkGroupAvailability(GROUP_ID)
        );
    }

    @Test
    @DisplayName("Caso exitoso - getCourseEnrollmentStats retorna estadísticas")
    void testGetCourseEnrollmentStats_Exitoso() {
        Map<String, Integer> stats = Map.of("group-1", 20, "group-2", 25);
        when(studentPortalService.getTotalEnrolledByCourse(COURSE_CODE)).thenReturn(stats);

        ResponseEntity<?> response = studentPortalController.getCourseEnrollmentStats(COURSE_CODE);

        assertAll("Verificar respuesta exitosa de getCourseEnrollmentStats",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> verify(studentPortalService, times(1)).getTotalEnrolledByCourse(COURSE_CODE)
        );
    }

    @Test
    @DisplayName("Caso exitoso - getAcademicSummary retorna resumen académico")
    void testGetAcademicSummary_Exitoso() {
        when(studentPortalService.getAcademicProgress(STUDENT_ID)).thenReturn(testProgress);

        ResponseEntity<?> response = studentPortalController.getAcademicSummary(STUDENT_ID);

        assertAll("Verificar respuesta exitosa de getAcademicSummary",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> verify(studentPortalService, times(1)).getAcademicProgress(STUDENT_ID)
        );
    }

    @Test
    @DisplayName("Caso exitoso - checkEnrollmentEligibility retorna elegibilidad")
    void testCheckEnrollmentEligibility_Exitoso() {
        when(studentPortalService.getAcademicProgress(STUDENT_ID)).thenReturn(testProgress);
        when(studentPortalService.getAvailableGroups(COURSE_CODE)).thenReturn(List.of(testGroup));

        ResponseEntity<?> response = studentPortalController.checkEnrollmentEligibility(STUDENT_ID, COURSE_CODE);

        assertAll("Verificar respuesta exitosa de checkEnrollmentEligibility",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> verify(studentPortalService, times(1)).getAcademicProgress(STUDENT_ID),
                () -> verify(studentPortalService, times(1)).getAvailableGroups(COURSE_CODE)
        );
    }

    @Test
    @DisplayName("Caso error - getCurrentSchedule lanza AppException")
    void testGetCurrentSchedule_EstudianteNoEncontrado() {
        when(studentPortalService.getCurrentSchedule(STUDENT_ID)).thenThrow(new AppException("Estudiante no encontrado"));

        ResponseEntity<?> response = studentPortalController.getCurrentSchedule(STUDENT_ID);

        assertAll("Verificar manejo de excepción en getCurrentSchedule",
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> assertEquals("Estudiante no encontrado", ((Map<?, ?>) response.getBody()).get("error")),
                () -> verify(studentPortalService, times(1)).getCurrentSchedule(STUDENT_ID)
        );
    }

    @Test
    @DisplayName("Caso error - getAvailableGroups lanza AppException")
    void testGetAvailableGroups_CursoNoEncontrado() {
        when(studentPortalService.getAvailableGroups(COURSE_CODE)).thenThrow(new AppException("Curso no encontrado"));

        ResponseEntity<?> response = studentPortalController.getAvailableGroups(COURSE_CODE);

        assertAll("Verificar manejo de excepción en getAvailableGroups",
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> assertEquals("Curso no encontrado", ((Map<?, ?>) response.getBody()).get("error")),
                () -> verify(studentPortalService, times(1)).getAvailableGroups(COURSE_CODE)
        );
    }

    @Test
    @DisplayName("Caso error - getAcademicProgress lanza excepción genérica")
    void testGetAcademicProgress_ExcepcionGenerica() {
        when(studentPortalService.getAcademicProgress(STUDENT_ID)).thenThrow(new RuntimeException("Error de base de datos"));

        ResponseEntity<?> response = studentPortalController.getAcademicProgress(STUDENT_ID);

        assertAll("Verificar manejo de excepción genérica en getAcademicProgress",
                () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> assertEquals("Error al obtener el progreso académico", ((Map<?, ?>) response.getBody()).get("error")),
                () -> verify(studentPortalService, times(1)).getAcademicProgress(STUDENT_ID)
        );
    }

    @Test
    @DisplayName("Caso error - checkGroupAvailability grupo no encontrado")
    void testCheckGroupAvailability_GrupoNoEncontrado() {
        when(studentPortalService.checkGroupAvailability(GROUP_ID)).thenThrow(new AppException("Grupo no encontrado"));

        ResponseEntity<?> response = studentPortalController.checkGroupAvailability(GROUP_ID);

        assertAll("Verificar manejo de excepción en checkGroupAvailability",
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> assertEquals("Grupo no encontrado", ((Map<?, ?>) response.getBody()).get("error")),
                () -> verify(studentPortalService, times(1)).checkGroupAvailability(GROUP_ID)
        );
    }

    @Test
    @DisplayName("Caso error - getEnrollmentDeadlines periodo no encontrado")
    void testGetEnrollmentDeadlines_PeriodoNoEncontrado() {
        when(studentPortalService.getEnrollmentDeadlines()).thenThrow(new AppException("No hay periodo académico activo"));

        ResponseEntity<?> response = studentPortalController.getEnrollmentDeadlines();

        assertAll("Verificar manejo de excepción en getEnrollmentDeadlines",
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> assertEquals("No hay periodo académico activo", ((Map<?, ?>) response.getBody()).get("error")),
                () -> verify(studentPortalService, times(1)).getEnrollmentDeadlines()
        );
    }

    @Test
    @DisplayName("Caso borde - getAcademicAlerts con lista vacía")
    void testGetAcademicAlerts_ListaVacia() {
        when(studentPortalService.getAcademicAlerts(STUDENT_ID)).thenReturn(List.of());

        ResponseEntity<?> response = studentPortalController.getAcademicAlerts(STUDENT_ID);

        assertAll("Verificar getAcademicAlerts con lista vacía",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> verify(studentPortalService, times(1)).getAcademicAlerts(STUDENT_ID)
        );
    }

    @Test
    @DisplayName("Caso borde - getGroupCapacityInfo con grupo lleno")
    void testGetGroupCapacityInfo_GrupoLleno() {
        when(studentPortalService.getMaxCapacity(GROUP_ID)).thenReturn(30);
        when(studentPortalService.getCurrentEnrollment(GROUP_ID)).thenReturn(30);
        when(studentPortalService.checkGroupAvailability(GROUP_ID)).thenReturn(false);

        ResponseEntity<?> response = studentPortalController.getGroupCapacityInfo(GROUP_ID);

        assertAll("Verificar getGroupCapacityInfo con grupo lleno",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> verify(studentPortalService, times(1)).getMaxCapacity(GROUP_ID),
                () -> verify(studentPortalService, times(1)).getCurrentEnrollment(GROUP_ID),
                () -> verify(studentPortalService, times(1)).checkGroupAvailability(GROUP_ID)
        );
    }

    @Test
    @DisplayName("Caso borde - getAvailableGroups sin grupos disponibles")
    void testGetAvailableGroups_SinGruposDisponibles() {
        when(studentPortalService.getAvailableGroups(COURSE_CODE)).thenReturn(List.of());

        ResponseEntity<?> response = studentPortalController.getAvailableGroups(COURSE_CODE);

        assertAll("Verificar getAvailableGroups sin grupos disponibles",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertTrue(((List<?>) response.getBody()).isEmpty()),
                () -> verify(studentPortalService, times(1)).getAvailableGroups(COURSE_CODE)
        );
    }

    @Test
    @DisplayName("Caso borde - checkEnrollmentEligibility con curso ya aprobado")
    void testCheckEnrollmentEligibility_CursoYaAprobado() {
        when(studentPortalService.getAcademicProgress(STUDENT_ID)).thenReturn(testProgress);
        when(studentPortalService.getAvailableGroups(COURSE_CODE)).thenReturn(List.of(testGroup));

        ResponseEntity<?> response = studentPortalController.checkEnrollmentEligibility(STUDENT_ID, COURSE_CODE);

        assertAll("Verificar elegibilidad con curso ya aprobado",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> verify(studentPortalService, times(1)).getAcademicProgress(STUDENT_ID),
                () -> verify(studentPortalService, times(1)).getAvailableGroups(COURSE_CODE)
        );
    }

    @Test
    @DisplayName("Caso borde - getCourseEnrollmentStats sin estadísticas")
    void testGetCourseEnrollmentStats_SinEstadisticas() {
        when(studentPortalService.getTotalEnrolledByCourse(COURSE_CODE)).thenReturn(Map.of());

        ResponseEntity<?> response = studentPortalController.getCourseEnrollmentStats(COURSE_CODE);

        assertAll("Verificar getCourseEnrollmentStats sin estadísticas",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> verify(studentPortalService, times(1)).getTotalEnrolledByCourse(COURSE_CODE)
        );
    }

    @Test
    @DisplayName("Caso borde - getGroupCapacityInfo lanza excepción en getMaxCapacity")
    void testGetGroupCapacityInfo_ErrorEnMaxCapacity() {
        when(studentPortalService.getMaxCapacity(GROUP_ID)).thenThrow(new AppException("Grupo no encontrado"));

        ResponseEntity<?> response = studentPortalController.getGroupCapacityInfo(GROUP_ID);

        assertAll("Verificar manejo de error en getGroupCapacityInfo",
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> verify(studentPortalService, times(1)).getMaxCapacity(GROUP_ID),
                () -> verify(studentPortalService, never()).getCurrentEnrollment(GROUP_ID),
                () -> verify(studentPortalService, never()).checkGroupAvailability(GROUP_ID)
        );
    }
}
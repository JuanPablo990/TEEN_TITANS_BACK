package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto.CourseStatusDetailDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto.StudentProgressDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.CourseStatus;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.StudentPortalService;
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
public class StudentPortalControllerTest {

    @Mock
    private StudentPortalService studentPortalService;

    @InjectMocks
    private StudentPortalController studentPortalController;

    private Student student;
    private StudentAcademicProgress academicProgress;
    private Group group;
    private Course course;
    private AcademicPeriod academicPeriod;
    private CourseStatusDetail courseStatusDetail;

    @BeforeEach
    void setUp() {
        student = new Student("STU-123", "Juan Pérez", "juan@university.edu", "password", "Computer Science", 3);
        student.setId("STU-123");

        course = new Course("CS101", "Introduction to Programming", 4, "Basic programming concepts", "Computer Science", true);

        group = new Group("GRP-1", "A", course, null, null, null);

        courseStatusDetail = new CourseStatusDetail(course, "STU-123", CourseStatus.IN_PROGRESS, 4.0, "2025-1",
                new Date(), null, group, null, 4, null, "Good performance");

        List<CourseStatusDetail> coursesStatus = Arrays.asList(courseStatusDetail);

        academicProgress = new StudentAcademicProgress(student, "Computer Science", "Engineering", "Regular",
                3, 8, 60, 160, 4.2, coursesStatus);

        academicPeriod = AcademicPeriod.builder()
                .periodId("PER-2025-1")
                .name("2025-1")
                .startDate(new Date())
                .endDate(new Date(System.currentTimeMillis() + 86400000L * 120))
                .enrollmentStart(new Date())
                .enrollmentEnd(new Date(System.currentTimeMillis() + 86400000L * 30))
                .adjustmentPeriodStart(new Date(System.currentTimeMillis() + 86400000L * 15))
                .adjustmentPeriodEnd(new Date(System.currentTimeMillis() + 86400000L * 45))
                .isActive(true)
                .build();
    }

    @Test
    @DisplayName("Caso exitoso - getCurrentSchedule retorna horario del estudiante")
    void testGetCurrentSchedule_Exitoso() {
        List<Group> schedule = Arrays.asList(group);
        when(studentPortalService.getCurrentSchedule("STU-123")).thenReturn(schedule);

        ResponseEntity<?> response = studentPortalController.getCurrentSchedule("STU-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(List.class, response.getBody());
        assertEquals(1, ((List<?>) response.getBody()).size());
        verify(studentPortalService, times(1)).getCurrentSchedule("STU-123");
    }

    @Test
    @DisplayName("Caso error - getCurrentSchedule lanza AppException")
    void testGetCurrentSchedule_AppException() {
        when(studentPortalService.getCurrentSchedule("STU-123")).thenThrow(new AppException("Estudiante no encontrado"));

        ResponseEntity<?> response = studentPortalController.getCurrentSchedule("STU-123");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Estudiante no encontrado", ((Map<?, ?>) response.getBody()).get("error"));
        verify(studentPortalService, times(1)).getCurrentSchedule("STU-123");
    }

    @Test
    @DisplayName("Caso exitoso - getAvailableGroups retorna grupos disponibles")
    void testGetAvailableGroups_Exitoso() {
        List<Group> availableGroups = Arrays.asList(group);
        when(studentPortalService.getAvailableGroups("CS101")).thenReturn(availableGroups);

        ResponseEntity<?> response = studentPortalController.getAvailableGroups("CS101");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(List.class, response.getBody());
        assertEquals(1, ((List<?>) response.getBody()).size());
        verify(studentPortalService, times(1)).getAvailableGroups("CS101");
    }

    @Test
    @DisplayName("Caso exitoso - getAcademicProgress retorna progreso académico")
    void testGetAcademicProgress_Exitoso() {
        when(studentPortalService.getAcademicProgress("STU-123")).thenReturn(academicProgress);

        ResponseEntity<?> response = studentPortalController.getAcademicProgress("STU-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(StudentProgressDTO.class, response.getBody());

        StudentProgressDTO dto = (StudentProgressDTO) response.getBody();
        assertEquals("STU-123", dto.getStudentId());
        assertEquals("Juan Pérez", dto.getStudentName());
        assertEquals("Computer Science", dto.getAcademicProgram());
        verify(studentPortalService, times(1)).getAcademicProgress("STU-123");
    }

    @Test
    @DisplayName("Caso exitoso - checkGroupAvailability retorna disponibilidad")
    void testCheckGroupAvailability_Exitoso() {
        when(studentPortalService.checkGroupAvailability("GRP-1")).thenReturn(true);

        ResponseEntity<?> response = studentPortalController.checkGroupAvailability("GRP-1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(Map.class, response.getBody());
        assertEquals("GRP-1", ((Map<?, ?>) response.getBody()).get("groupId"));
        assertEquals(true, ((Map<?, ?>) response.getBody()).get("available"));
        verify(studentPortalService, times(1)).checkGroupAvailability("GRP-1");
    }

    @Test
    @DisplayName("Caso exitoso - getCourseRecommendations retorna recomendaciones")
    void testGetCourseRecommendations_Exitoso() {
        List<Course> recommendations = Arrays.asList(course);
        when(studentPortalService.getCourseRecommendations("STU-123")).thenReturn(recommendations);

        ResponseEntity<?> response = studentPortalController.getCourseRecommendations("STU-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(List.class, response.getBody());
        assertEquals(1, ((List<?>) response.getBody()).size());
        verify(studentPortalService, times(1)).getCourseRecommendations("STU-123");
    }

    @Test
    @DisplayName("Caso exitoso - getEnrollmentDeadlines retorna periodo académico")
    void testGetEnrollmentDeadlines_Exitoso() {
        when(studentPortalService.getEnrollmentDeadlines()).thenReturn(academicPeriod);

        ResponseEntity<?> response = studentPortalController.getEnrollmentDeadlines();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(AcademicPeriod.class, response.getBody());
        assertEquals("PER-2025-1", ((AcademicPeriod) response.getBody()).getPeriodId());
        verify(studentPortalService, times(1)).getEnrollmentDeadlines();
    }

    @Test
    @DisplayName("Caso borde - getAcademicAlerts retorna lista vacía")
    void testGetAcademicAlerts_ListaVacia() {
        when(studentPortalService.getAcademicAlerts("STU-123")).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = studentPortalController.getAcademicAlerts("STU-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(Map.class, response.getBody());
        assertEquals(0, ((Map<?, ?>) response.getBody()).get("alertCount"));
        verify(studentPortalService, times(1)).getAcademicAlerts("STU-123");
    }

    @Test
    @DisplayName("Caso exitoso - getAcademicAlerts retorna alertas")
    void testGetAcademicAlerts_ConAlertas() {
        List<String> alerts = Arrays.asList("Alerta: GPA bajo", "Alerta: Progreso insuficiente");
        when(studentPortalService.getAcademicAlerts("STU-123")).thenReturn(alerts);

        ResponseEntity<?> response = studentPortalController.getAcademicAlerts("STU-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(Map.class, response.getBody());
        assertEquals(2, ((Map<?, ?>) response.getBody()).get("alertCount"));
        verify(studentPortalService, times(1)).getAcademicAlerts("STU-123");
    }

    @Test
    @DisplayName("Caso exitoso - getGroupCapacityInfo retorna información de capacidad")
    void testGetGroupCapacityInfo_Exitoso() {
        when(studentPortalService.getMaxCapacity("GRP-1")).thenReturn(30);
        when(studentPortalService.getCurrentEnrollment("GRP-1")).thenReturn(18);
        when(studentPortalService.checkGroupAvailability("GRP-1")).thenReturn(true);

        ResponseEntity<?> response = studentPortalController.getGroupCapacityInfo("GRP-1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(Map.class, response.getBody());

        Map<?, ?> capacityInfo = (Map<?, ?>) response.getBody();
        assertEquals("GRP-1", capacityInfo.get("groupId"));
        assertEquals(30, capacityInfo.get("maxCapacity"));
        assertEquals(18, capacityInfo.get("currentEnrollment"));
        assertEquals(12, capacityInfo.get("availableSpots"));
        assertEquals(true, capacityInfo.get("isAvailable"));

        verify(studentPortalService, times(1)).getMaxCapacity("GRP-1");
        verify(studentPortalService, times(1)).getCurrentEnrollment("GRP-1");
        verify(studentPortalService, times(1)).checkGroupAvailability("GRP-1");
    }

    @Test
    @DisplayName("Caso exitoso - getCourseEnrollmentStats retorna estadísticas")
    void testGetCourseEnrollmentStats_Exitoso() {
        Map<String, Integer> enrollmentStats = Map.of("GRP-1", 18, "GRP-2", 22);
        when(studentPortalService.getTotalEnrolledByCourse("CS101")).thenReturn(enrollmentStats);

        ResponseEntity<?> response = studentPortalController.getCourseEnrollmentStats("CS101");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(Map.class, response.getBody());
        assertEquals("CS101", ((Map<?, ?>) response.getBody()).get("courseCode"));
        verify(studentPortalService, times(1)).getTotalEnrolledByCourse("CS101");
    }

    @Test
    @DisplayName("Caso exitoso - getAcademicSummary retorna resumen académico")
    void testGetAcademicSummary_Exitoso() {
        when(studentPortalService.getAcademicProgress("STU-123")).thenReturn(academicProgress);

        ResponseEntity<?> response = studentPortalController.getAcademicSummary("STU-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(Map.class, response.getBody());

        Map<?, ?> summary = (Map<?, ?>) response.getBody();
        assertEquals("STU-123", summary.get("studentId"));
        assertEquals("Juan Pérez", summary.get("studentName"));
        assertEquals("Computer Science", summary.get("academicProgram"));
        verify(studentPortalService, times(1)).getAcademicProgress("STU-123");
    }

    @Test
    @DisplayName("Caso exitoso - checkEnrollmentEligibility retorna elegibilidad")
    void testCheckEnrollmentEligibility_Exitoso() {
        when(studentPortalService.getAcademicProgress("STU-123")).thenReturn(academicProgress);
        when(studentPortalService.getAvailableGroups("CS101")).thenReturn(Arrays.asList(group));

        ResponseEntity<?> response = studentPortalController.checkEnrollmentEligibility("STU-123", "CS101");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(Map.class, response.getBody());

        Map<?, ?> eligibility = (Map<?, ?>) response.getBody();
        assertEquals("STU-123", eligibility.get("studentId"));
        assertEquals("CS101", eligibility.get("courseCode"));
        verify(studentPortalService, times(1)).getAcademicProgress("STU-123");
        verify(studentPortalService, times(1)).getAvailableGroups("CS101");
    }

    @Test
    @DisplayName("Caso borde - getAvailableGroups retorna lista vacía")
    void testGetAvailableGroups_ListaVacia() {
        when(studentPortalService.getAvailableGroups("CS101")).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = studentPortalController.getAvailableGroups("CS101");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(List.class, response.getBody());
        assertTrue(((List<?>) response.getBody()).isEmpty());
        verify(studentPortalService, times(1)).getAvailableGroups("CS101");
    }

    @Test
    @DisplayName("Caso borde - getCourseRecommendations retorna lista vacía")
    void testGetCourseRecommendations_ListaVacia() {
        when(studentPortalService.getCourseRecommendations("STU-123")).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = studentPortalController.getCourseRecommendations("STU-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(List.class, response.getBody());
        assertTrue(((List<?>) response.getBody()).isEmpty());
        verify(studentPortalService, times(1)).getCourseRecommendations("STU-123");
    }

    @Test
    @DisplayName("Caso error - getAcademicProgress lanza AppException")
    void testGetAcademicProgress_AppException() {
        when(studentPortalService.getAcademicProgress("STU-123")).thenThrow(new AppException("Progreso no encontrado"));

        ResponseEntity<?> response = studentPortalController.getAcademicProgress("STU-123");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Progreso no encontrado", ((Map<?, ?>) response.getBody()).get("error"));
        verify(studentPortalService, times(1)).getAcademicProgress("STU-123");
    }

    @Test
    @DisplayName("Caso error - checkGroupAvailability lanza AppException")
    void testCheckGroupAvailability_AppException() {
        when(studentPortalService.checkGroupAvailability("GRP-1")).thenThrow(new AppException("Grupo no encontrado"));

        ResponseEntity<?> response = studentPortalController.checkGroupAvailability("GRP-1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Grupo no encontrado", ((Map<?, ?>) response.getBody()).get("error"));
        verify(studentPortalService, times(1)).checkGroupAvailability("GRP-1");
    }

    @Test
    @DisplayName("Caso error - getCourseRecommendations lanza AppException")
    void testGetCourseRecommendations_AppException() {
        when(studentPortalService.getCourseRecommendations("STU-123")).thenThrow(new AppException("Estudiante no encontrado"));

        ResponseEntity<?> response = studentPortalController.getCourseRecommendations("STU-123");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Estudiante no encontrado", ((Map<?, ?>) response.getBody()).get("error"));
        verify(studentPortalService, times(1)).getCourseRecommendations("STU-123");
    }

    @Test
    @DisplayName("Caso error - getEnrollmentDeadlines lanza AppException")
    void testGetEnrollmentDeadlines_AppException() {
        when(studentPortalService.getEnrollmentDeadlines()).thenThrow(new AppException("No hay periodos activos"));

        ResponseEntity<?> response = studentPortalController.getEnrollmentDeadlines();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("No hay periodos activos", ((Map<?, ?>) response.getBody()).get("error"));
        verify(studentPortalService, times(1)).getEnrollmentDeadlines();
    }

    @Test
    @DisplayName("Caso borde - getGroupCapacityInfo con capacidad completa")
    void testGetGroupCapacityInfo_CapacidadCompleta() {
        when(studentPortalService.getMaxCapacity("GRP-1")).thenReturn(30);
        when(studentPortalService.getCurrentEnrollment("GRP-1")).thenReturn(30);
        when(studentPortalService.checkGroupAvailability("GRP-1")).thenReturn(false);

        ResponseEntity<?> response = studentPortalController.getGroupCapacityInfo("GRP-1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<?, ?> capacityInfo = (Map<?, ?>) response.getBody();
        assertEquals(0, capacityInfo.get("availableSpots"));
        assertEquals(false, capacityInfo.get("isAvailable"));
        assertEquals(100.0, capacityInfo.get("occupancyRate"));
    }

    @Test
    @DisplayName("Caso borde - checkEnrollmentEligibility con curso ya aprobado")
    void testCheckEnrollmentEligibility_CursoYaAprobado() {
        CourseStatusDetail approvedCourse = new CourseStatusDetail(course, "STU-123", CourseStatus.PASSED, 4.5,
                "2024-2", new Date(), new Date(), group, null, 4, true, "Excellent");
        academicProgress.setCoursesStatus(Arrays.asList(approvedCourse));

        when(studentPortalService.getAcademicProgress("STU-123")).thenReturn(academicProgress);
        when(studentPortalService.getAvailableGroups("CS101")).thenReturn(Arrays.asList(group));

        ResponseEntity<?> response = studentPortalController.checkEnrollmentEligibility("STU-123", "CS101");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<?, ?> eligibility = (Map<?, ?>) response.getBody();
        assertEquals(true, eligibility.get("alreadyApproved"));
        assertEquals(false, eligibility.get("eligible"));
    }
}
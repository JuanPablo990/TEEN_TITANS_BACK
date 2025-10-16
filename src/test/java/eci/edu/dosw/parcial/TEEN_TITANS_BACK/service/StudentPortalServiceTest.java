package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para StudentPortalService
 */
@ExtendWith(MockitoExtension.class)
public class StudentPortalServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentAcademicProgressRepository studentAcademicProgressRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private AcademicPeriodRepository academicPeriodRepository;

    @Mock
    private CourseStatusDetailRepository courseStatusDetailRepository;

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private StudentPortalService studentPortalService;

    private Student student;
    private StudentAcademicProgress progress;
    private Course course1, course2;
    private CourseStatusDetail courseStatus1, courseStatus2;
    private Group group1, group2;
    private Classroom classroom;
    private AcademicPeriod academicPeriod;

    @BeforeEach
    void setUp() {
        // Configurar datos de prueba
        student = new Student("ST001", "Robin", "robin@titans.edu", "pass123",
                "Ingeniería de Sistemas", 5);

        course1 = new Course("CS101", "Programación I", 3,
                "Fundamentos de programación", "Ingeniería de Sistemas", true);
        course2 = new Course("CS102", "Programación II", 3,
                "Programación orientada a objetos", "Ingeniería de Sistemas", true);

        classroom = new Classroom("CL001", "Edificio A", "A101", 30, RoomType.LABORATORY);

        group1 = new Group("GR001", "01", course1, null, null, classroom);
        group2 = new Group("GR002", "02", course2, null, null, classroom);

        Date enrollmentDate = new Date();
        courseStatus1 = new CourseStatusDetail("CSD001", course1, CourseStatus.IN_PROGRESS,
                3.5, "2025-1", enrollmentDate, null,
                group1, null, 3, null, "Buen desempeño");


        progress = new StudentAcademicProgress("PROG001", student, "Ingeniería de Sistemas",
                "Facultad de Ingeniería", "Regular", 5, 10,
                45, 160, 3.8, Arrays.asList(courseStatus1, courseStatus2));

        academicPeriod = new AcademicPeriod("PER001", "2025-1", new Date(),
                new Date(System.currentTimeMillis() + 86400000),
                new Date(), new Date(), new Date(), new Date(), true);
    }

    // ========== PRUEBAS PARA getCurrentSchedule ==========



    @Test
    @DisplayName("Caso error - getCurrentSchedule lanza excepción cuando no encuentra progreso académico")
    void testGetCurrentSchedule_ProgresoNoEncontrado() {
        // Configurar
        when(studentAcademicProgressRepository.findById("ST999"))
                .thenReturn(Optional.empty());

        // Ejecutar y Verificar
        AppException exception = assertThrows(AppException.class,
                () -> studentPortalService.getCurrentSchedule("ST999"));

        assertEquals("Progreso académico no encontrado para el estudiante: ST999", exception.getMessage());
        verify(studentAcademicProgressRepository, times(1)).findById("ST999");
    }

    // ========== PRUEBAS PARA getAvailableGroups ==========

    @Test
    @DisplayName("Caso exitoso - getAvailableGroups retorna grupos disponibles")
    void testGetAvailableGroups_Exitoso() {
        // Configurar
        when(groupRepository.findByCourse_CourseCode("CS101"))
                .thenReturn(Arrays.asList(group1, group2));
        when(groupRepository.findById("GR001"))
                .thenReturn(Optional.of(group1));
        when(groupRepository.findById("GR002"))
                .thenReturn(Optional.of(group2));

        // Ejecutar
        List<Group> resultado = studentPortalService.getAvailableGroups("CS101");

        // Verificar
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        verify(groupRepository, times(1)).findByCourse_CourseCode("CS101");
    }

    @Test
    @DisplayName("Caso error - getAvailableGroups lanza excepción cuando no hay grupos")
    void testGetAvailableGroups_NoGruposEncontrados() {
        // Configurar
        when(groupRepository.findByCourse_CourseCode("CS999"))
                .thenReturn(Collections.emptyList());

        // Ejecutar y Verificar
        AppException exception = assertThrows(AppException.class,
                () -> studentPortalService.getAvailableGroups("CS999"));

        assertEquals("No se encontraron grupos para el curso: CS999", exception.getMessage());
        verify(groupRepository, times(1)).findByCourse_CourseCode("CS999");
    }

    // ========== PRUEBAS PARA getAcademicProgress ==========

    @Test
    @DisplayName("Caso exitoso - getAcademicProgress retorna progreso académico")
    void testGetAcademicProgress_Exitoso() {
        // Configurar
        when(studentAcademicProgressRepository.findById("ST001"))
                .thenReturn(Optional.of(progress));

        // Ejecutar
        StudentAcademicProgress resultado = studentPortalService.getAcademicProgress("ST001");

        // Verificar
        assertNotNull(resultado);
        assertEquals("PROG001", resultado.getId());
        assertEquals(3.8, resultado.getCumulativeGPA());
        assertEquals(5, resultado.getCurrentSemester());
        verify(studentAcademicProgressRepository, times(1)).findById("ST001");
    }

    @Test
    @DisplayName("Caso error - getAcademicProgress lanza excepción cuando no encuentra progreso")
    void testGetAcademicProgress_NoEncontrado() {
        // Configurar
        when(studentAcademicProgressRepository.findById("ST999"))
                .thenReturn(Optional.empty());

        // Ejecutar y Verificar
        AppException exception = assertThrows(AppException.class,
                () -> studentPortalService.getAcademicProgress("ST999"));

        assertEquals("Progreso académico no encontrado para el estudiante: ST999", exception.getMessage());
        verify(studentAcademicProgressRepository, times(1)).findById("ST999");
    }

    // ========== PRUEBAS PARA checkGroupAvailability ==========

    @Test
    @DisplayName("Caso exitoso - checkGroupAvailability retorna true cuando hay cupos")
    void testCheckGroupAvailability_ConCupos() {
        // Configurar
        when(groupRepository.findById("GR001"))
                .thenReturn(Optional.of(group1));

        // Ejecutar
        boolean resultado = studentPortalService.checkGroupAvailability("GR001");

        // Verificar
        assertTrue(resultado);
        verify(groupRepository, times(2)).findById("GR001"); // Se llama 2 veces en el método
    }

    @Test
    @DisplayName("Caso error - checkGroupAvailability lanza excepción cuando grupo no existe")
    void testCheckGroupAvailability_GrupoNoEncontrado() {
        // Configurar
        when(groupRepository.findById("GR999"))
                .thenReturn(Optional.empty());

        // Ejecutar y Verificar
        AppException exception = assertThrows(AppException.class,
                () -> studentPortalService.checkGroupAvailability("GR999"));

        assertEquals("Grupo no encontrado: GR999", exception.getMessage());
        verify(groupRepository, times(1)).findById("GR999");
    }

    // ========== PRUEBAS PARA getCourseRecommendations ==========



    @Test
    @DisplayName("Caso error - getCourseRecommendations lanza excepción cuando no encuentra progreso")
    void testGetCourseRecommendations_ProgresoNoEncontrado() {
        // Configurar
        when(studentAcademicProgressRepository.findById("ST999"))
                .thenReturn(Optional.empty());

        // Ejecutar y Verificar
        AppException exception = assertThrows(AppException.class,
                () -> studentPortalService.getCourseRecommendations("ST999"));

        assertEquals("Progreso académico no encontrado para el estudiante: ST999", exception.getMessage());
        verify(studentAcademicProgressRepository, times(1)).findById("ST999");
    }

    // ========== PRUEBAS PARA getEnrollmentDeadlines ==========

    @Test
    @DisplayName("Caso exitoso - getEnrollmentDeadlines retorna periodo académico activo")
    void testGetEnrollmentDeadlines_Exitoso() {
        // Configurar
        when(academicPeriodRepository.findByIsActive(true))
                .thenReturn(Arrays.asList(academicPeriod));

        // Ejecutar
        AcademicPeriod resultado = studentPortalService.getEnrollmentDeadlines();

        // Verificar
        assertNotNull(resultado);
        assertEquals("PER001", resultado.getPeriodId());
        assertEquals("2025-1", resultado.getName());
        assertTrue(resultado.isActive());
        verify(academicPeriodRepository, times(1)).findByIsActive(true);
    }

    @Test
    @DisplayName("Caso error - getEnrollmentDeadlines lanza excepción cuando no hay periodos activos")
    void testGetEnrollmentDeadlines_NoPeriodosActivos() {
        // Configurar
        when(academicPeriodRepository.findByIsActive(true))
                .thenReturn(Collections.emptyList());

        // Ejecutar y Verificar
        AppException exception = assertThrows(AppException.class,
                () -> studentPortalService.getEnrollmentDeadlines());

        assertEquals("No hay periodos académicos activos", exception.getMessage());
        verify(academicPeriodRepository, times(1)).findByIsActive(true);
    }

    // ========== PRUEBAS PARA getAcademicAlerts ==========



    @Test
    @DisplayName("Caso error - getAcademicAlerts lanza excepción cuando no encuentra información")
    void testGetAcademicAlerts_InformacionNoEncontrada() {
        // Configurar
        when(studentAcademicProgressRepository.findById("ST999"))
                .thenReturn(Optional.empty());

        // Ejecutar y Verificar
        AppException exception = assertThrows(AppException.class,
                () -> studentPortalService.getAcademicAlerts("ST999"));

        assertEquals("No se encontró información académica del estudiante: ST999", exception.getMessage());
        verify(studentAcademicProgressRepository, times(1)).findById("ST999");
    }

    // ========== PRUEBAS PARA getTotalEnrolledByCourse ==========

    @Test
    @DisplayName("Caso exitoso - getTotalEnrolledByCourse retorna mapa de inscritos por grupo")
    void testGetTotalEnrolledByCourse_Exitoso() {
        // Configurar
        when(groupRepository.findByCourse_CourseCode("CS101"))
                .thenReturn(Arrays.asList(group1, group2));
        when(groupRepository.findById("GR001"))
                .thenReturn(Optional.of(group1));
        when(groupRepository.findById("GR002"))
                .thenReturn(Optional.of(group2));

        // Ejecutar
        Map<String, Integer> resultado = studentPortalService.getTotalEnrolledByCourse("CS101");

        // Verificar
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.containsKey("GR001"));
        assertTrue(resultado.containsKey("GR002"));
        verify(groupRepository, times(1)).findByCourse_CourseCode("CS101");
    }

    @Test
    @DisplayName("Caso error - getTotalEnrolledByCourse lanza excepción cuando no hay grupos")
    void testGetTotalEnrolledByCourse_NoGrupos() {
        // Configurar
        when(groupRepository.findByCourse_CourseCode("CS999"))
                .thenReturn(Collections.emptyList());

        // Ejecutar y Verificar
        AppException exception = assertThrows(AppException.class,
                () -> studentPortalService.getTotalEnrolledByCourse("CS999"));

        assertEquals("No se encontraron grupos para el curso: CS999", exception.getMessage());
        verify(groupRepository, times(1)).findByCourse_CourseCode("CS999");
    }

    // ========== PRUEBAS DE MÉTODOS HEREDADOS ==========

    @Test
    @DisplayName("Caso exitoso - getCourse retorna curso del grupo")
    void testGetCourse_Exitoso() {
        // Configurar
        when(groupRepository.findById("GR001"))
                .thenReturn(Optional.of(group1));

        // Ejecutar
        Course resultado = studentPortalService.getCourse("GR001");

        // Verificar
        assertNotNull(resultado);
        assertEquals("CS101", resultado.getCourseCode());
        verify(groupRepository, times(1)).findById("GR001");
    }

    @Test
    @DisplayName("Caso borde - getMaxCapacity retorna capacidad del aula")
    void testGetMaxCapacity_Exitoso() {
        // Configurar
        when(groupRepository.findById("GR001"))
                .thenReturn(Optional.of(group1));

        // Ejecutar
        Integer resultado = studentPortalService.getMaxCapacity("GR001");

        // Verificar
        assertNotNull(resultado);
        assertEquals(30, resultado);
        verify(groupRepository, times(1)).findById("GR001");
    }

    @Test
    @DisplayName("Caso borde - getCurrentEnrollment calcula inscripción actual")
    void testGetCurrentEnrollment_Exitoso() {
        // Configurar
        when(groupRepository.findById("GR001"))
                .thenReturn(Optional.of(group1));

        // Ejecutar
        Integer resultado = studentPortalService.getCurrentEnrollment("GR001");

        // Verificar
        assertNotNull(resultado);
        assertEquals(18, resultado);
        verify(groupRepository, times(1)).findById("GR001");
    }


}
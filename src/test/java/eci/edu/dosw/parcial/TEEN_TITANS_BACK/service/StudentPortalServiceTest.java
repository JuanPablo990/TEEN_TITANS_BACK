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

    @Test
    @DisplayName("Caso error - getCurrentSchedule lanza excepción cuando no encuentra progreso académico")
    void testGetCurrentSchedule_ProgresoNoEncontrado() {
        when(studentAcademicProgressRepository.findById("ST999"))
                .thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class,
                () -> studentPortalService.getCurrentSchedule("ST999"));

        assertEquals("Progreso académico no encontrado para el estudiante: ST999", exception.getMessage());
        verify(studentAcademicProgressRepository, times(1)).findById("ST999");
    }

    @Test
    @DisplayName("Caso exitoso - getAvailableGroups retorna grupos disponibles")
    void testGetAvailableGroups_Exitoso() {
        when(groupRepository.findByCourse_CourseCode("CS101"))
                .thenReturn(Arrays.asList(group1, group2));
        when(groupRepository.findById("GR001"))
                .thenReturn(Optional.of(group1));
        when(groupRepository.findById("GR002"))
                .thenReturn(Optional.of(group2));

        List<Group> resultado = studentPortalService.getAvailableGroups("CS101");

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        verify(groupRepository, times(1)).findByCourse_CourseCode("CS101");
    }

    @Test
    @DisplayName("Caso error - getAvailableGroups lanza excepción cuando no hay grupos")
    void testGetAvailableGroups_NoGruposEncontrados() {
        when(groupRepository.findByCourse_CourseCode("CS999"))
                .thenReturn(Collections.emptyList());

        AppException exception = assertThrows(AppException.class,
                () -> studentPortalService.getAvailableGroups("CS999"));

        assertEquals("No se encontraron grupos para el curso: CS999", exception.getMessage());
        verify(groupRepository, times(1)).findByCourse_CourseCode("CS999");
    }

    @Test
    @DisplayName("Caso exitoso - getAcademicProgress retorna progreso académico")
    void testGetAcademicProgress_Exitoso() {
        when(studentAcademicProgressRepository.findById("ST001"))
                .thenReturn(Optional.of(progress));

        StudentAcademicProgress resultado = studentPortalService.getAcademicProgress("ST001");

        assertNotNull(resultado);
        assertEquals("PROG001", resultado.getId());
        assertEquals(3.8, resultado.getCumulativeGPA());
        assertEquals(5, resultado.getCurrentSemester());
        verify(studentAcademicProgressRepository, times(1)).findById("ST001");
    }

    @Test
    @DisplayName("Caso error - getAcademicProgress lanza excepción cuando no encuentra progreso")
    void testGetAcademicProgress_NoEncontrado() {
        when(studentAcademicProgressRepository.findById("ST999"))
                .thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class,
                () -> studentPortalService.getAcademicProgress("ST999"));

        assertEquals("Progreso académico no encontrado para el estudiante: ST999", exception.getMessage());
        verify(studentAcademicProgressRepository, times(1)).findById("ST999");
    }

    @Test
    @DisplayName("Caso exitoso - checkGroupAvailability retorna true cuando hay cupos")
    void testCheckGroupAvailability_ConCupos() {
        when(groupRepository.findById("GR001"))
                .thenReturn(Optional.of(group1));

        boolean resultado = studentPortalService.checkGroupAvailability("GR001");

        assertTrue(resultado);
        verify(groupRepository, times(2)).findById("GR001");
    }

    @Test
    @DisplayName("Caso error - checkGroupAvailability lanza excepción cuando grupo no existe")
    void testCheckGroupAvailability_GrupoNoEncontrado() {
        when(groupRepository.findById("GR999"))
                .thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class,
                () -> studentPortalService.checkGroupAvailability("GR999"));

        assertEquals("Grupo no encontrado: GR999", exception.getMessage());
        verify(groupRepository, times(1)).findById("GR999");
    }

    @Test
    @DisplayName("Caso error - getCourseRecommendations lanza excepción cuando no encuentra progreso")
    void testGetCourseRecommendations_ProgresoNoEncontrado() {
        when(studentAcademicProgressRepository.findById("ST999"))
                .thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class,
                () -> studentPortalService.getCourseRecommendations("ST999"));

        assertEquals("Progreso académico no encontrado para el estudiante: ST999", exception.getMessage());
        verify(studentAcademicProgressRepository, times(1)).findById("ST999");
    }

    @Test
    @DisplayName("Caso exitoso - getEnrollmentDeadlines retorna periodo académico activo")
    void testGetEnrollmentDeadlines_Exitoso() {
        when(academicPeriodRepository.findByIsActive(true))
                .thenReturn(Arrays.asList(academicPeriod));

        AcademicPeriod resultado = studentPortalService.getEnrollmentDeadlines();

        assertNotNull(resultado);
        assertEquals("PER001", resultado.getPeriodId());
        assertEquals("2025-1", resultado.getName());
        assertTrue(resultado.isActive());
        verify(academicPeriodRepository, times(1)).findByIsActive(true);
    }

    @Test
    @DisplayName("Caso error - getEnrollmentDeadlines lanza excepción cuando no hay periodos activos")
    void testGetEnrollmentDeadlines_NoPeriodosActivos() {
        when(academicPeriodRepository.findByIsActive(true))
                .thenReturn(Collections.emptyList());

        AppException exception = assertThrows(AppException.class,
                () -> studentPortalService.getEnrollmentDeadlines());

        assertEquals("No hay periodos académicos activos", exception.getMessage());
        verify(academicPeriodRepository, times(1)).findByIsActive(true);
    }

    @Test
    @DisplayName("Caso error - getAcademicAlerts lanza excepción cuando no encuentra información")
    void testGetAcademicAlerts_InformacionNoEncontrada() {
        when(studentAcademicProgressRepository.findById("ST999"))
                .thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class,
                () -> studentPortalService.getAcademicAlerts("ST999"));

        assertEquals("No se encontró información académica del estudiante: ST999", exception.getMessage());
        verify(studentAcademicProgressRepository, times(1)).findById("ST999");
    }

    @Test
    @DisplayName("Caso exitoso - getTotalEnrolledByCourse retorna mapa de inscritos por grupo")
    void testGetTotalEnrolledByCourse_Exitoso() {
        when(groupRepository.findByCourse_CourseCode("CS101"))
                .thenReturn(Arrays.asList(group1, group2));
        when(groupRepository.findById("GR001"))
                .thenReturn(Optional.of(group1));
        when(groupRepository.findById("GR002"))
                .thenReturn(Optional.of(group2));

        Map<String, Integer> resultado = studentPortalService.getTotalEnrolledByCourse("CS101");

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.containsKey("GR001"));
        assertTrue(resultado.containsKey("GR002"));
        verify(groupRepository, times(1)).findByCourse_CourseCode("CS101");
    }

    @Test
    @DisplayName("Caso error - getTotalEnrolledByCourse lanza excepción cuando no hay grupos")
    void testGetTotalEnrolledByCourse_NoGrupos() {
        when(groupRepository.findByCourse_CourseCode("CS999"))
                .thenReturn(Collections.emptyList());

        AppException exception = assertThrows(AppException.class,
                () -> studentPortalService.getTotalEnrolledByCourse("CS999"));

        assertEquals("No se encontraron grupos para el curso: CS999", exception.getMessage());
        verify(groupRepository, times(1)).findByCourse_CourseCode("CS999");
    }

    @Test
    @DisplayName("Caso exitoso - getCourse retorna curso del grupo")
    void testGetCourse_Exitoso() {
        when(groupRepository.findById("GR001"))
                .thenReturn(Optional.of(group1));

        Course resultado = studentPortalService.getCourse("GR001");

        assertNotNull(resultado);
        assertEquals("CS101", resultado.getCourseCode());
        verify(groupRepository, times(1)).findById("GR001");
    }

    @Test
    @DisplayName("Caso borde - getMaxCapacity retorna capacidad del aula")
    void testGetMaxCapacity_Exitoso() {
        when(groupRepository.findById("GR001"))
                .thenReturn(Optional.of(group1));

        Integer resultado = studentPortalService.getMaxCapacity("GR001");

        assertNotNull(resultado);
        assertEquals(30, resultado);
        verify(groupRepository, times(1)).findById("GR001");
    }

    @Test
    @DisplayName("Caso borde - getCurrentEnrollment calcula inscripción actual")
    void testGetCurrentEnrollment_Exitoso() {
        when(groupRepository.findById("GR001"))
                .thenReturn(Optional.of(group1));

        Integer resultado = studentPortalService.getCurrentEnrollment("GR001");

        assertNotNull(resultado);
        assertEquals(18, resultado);
        verify(groupRepository, times(1)).findById("GR001");
    }


    @Test
    @DisplayName("Caso borde - getCurrentSchedule sin cursos en progreso")
    void testGetCurrentSchedule_SinCursosEnProgreso() {

        CourseStatusDetail cursoAprobado1 = new CourseStatusDetail("CSD001", course1, CourseStatus.PASSED,
                4.0, "2024-2", new Date(), new Date(), group1, null, 3, null, "Aprobado");

        CourseStatusDetail cursoAprobado2 = new CourseStatusDetail("CSD002", course2, CourseStatus.PASSED,
                3.8, "2024-2", new Date(), new Date(), group2, null, 3, null, "Aprobado");

        StudentAcademicProgress progressSoloAprobados = new StudentAcademicProgress("PROG003", student,
                "Ingeniería de Sistemas", "Facultad de Ingeniería", "Regular", 5, 10,
                45, 160, 3.8, Arrays.asList(cursoAprobado1, cursoAprobado2));

        when(studentAcademicProgressRepository.findById("ST001")).thenReturn(Optional.of(progressSoloAprobados));

        List<Group> resultado = studentPortalService.getCurrentSchedule("ST001");

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Caso exitoso - getCourseRecommendations retorna cursos no tomados")
    void testGetCourseRecommendations_Exitoso() {

        CourseStatusDetail cursoTomado = new CourseStatusDetail("CSD001", course1, CourseStatus.PASSED,
                4.0, "2024-2", new Date(), new Date(), group1, null, 3, null, "Aprobado");

        StudentAcademicProgress progress = new StudentAcademicProgress("PROG004", student,
                "Ingeniería de Sistemas", "Facultad de Ingeniería", "Regular", 5, 10,
                45, 160, 3.8, Arrays.asList(cursoTomado));


        Course cursoRecomendado1 = new Course("CS102", "Programación II", 3,
                "Programación orientada a objetos", "Ingeniería de Sistemas", true);

        Course cursoRecomendado2 = new Course("MATH201", "Cálculo I", 4,
                "Cálculo diferencial", "Ingeniería de Sistemas", true);

        Course cursoOtroPrograma = new Course("PHYS101", "Física I", 3,
                "Mecánica clásica", "Física", true); // No debe ser recomendado

        when(studentAcademicProgressRepository.findById("ST001")).thenReturn(Optional.of(progress));
        when(courseRepository.findByAcademicProgramAndIsActive("Ingeniería de Sistemas", true))
                .thenReturn(Arrays.asList(cursoRecomendado1, cursoRecomendado2, cursoOtroPrograma));

        List<Course> recomendaciones = studentPortalService.getCourseRecommendations("ST001");

        assertNotNull(recomendaciones);

        assertEquals(2, recomendaciones.size());
        assertTrue(recomendaciones.stream().anyMatch(c -> c.getCourseCode().equals("CS102")));
        assertTrue(recomendaciones.stream().anyMatch(c -> c.getCourseCode().equals("MATH201")));
        assertTrue(recomendaciones.stream().noneMatch(c -> c.getCourseCode().equals("CS101"))); // Ya tomado
        assertTrue(recomendaciones.stream().noneMatch(c -> c.getCourseCode().equals("PHYS101"))); // Otro programa
    }

    @Test
    @DisplayName("Caso borde - getCourseRecommendations sin cursos disponibles")
    void testGetCourseRecommendations_SinCursosDisponibles() {
        CourseStatusDetail cursoTomado = new CourseStatusDetail("CSD001", course1, CourseStatus.PASSED,
                4.0, "2024-2", new Date(), new Date(), group1, null, 3, null, "Aprobado");

        StudentAcademicProgress progress = new StudentAcademicProgress("PROG005", student,
                "Ingeniería de Sistemas", "Facultad de Ingeniería", "Regular", 5, 10,
                45, 160, 3.8, Arrays.asList(cursoTomado));

        when(studentAcademicProgressRepository.findById("ST001")).thenReturn(Optional.of(progress));
        when(courseRepository.findByAcademicProgramAndIsActive("Ingeniería de Sistemas", true))
                .thenReturn(Collections.emptyList());

        List<Course> recomendaciones = studentPortalService.getCourseRecommendations("ST001");

        assertNotNull(recomendaciones);
        assertTrue(recomendaciones.isEmpty());
    }

    @Test
    @DisplayName("Caso exitoso - getAcademicAlerts con GPA bajo")
    void testGetAcademicAlerts_GPABajo() {
        CourseStatusDetail cursoReprobado = new CourseStatusDetail("CSD001", course1, CourseStatus.FAILED,
                2.0, "2025-1", new Date(), new Date(), group1, null, 3, null, "Reprobado");

        StudentAcademicProgress progress = new StudentAcademicProgress("PROG006", student,
                "Ingeniería de Sistemas", "Facultad de Ingeniería", "Regular", 5, 10,
                30, 150, 2.5, Arrays.asList(cursoReprobado));

        when(studentAcademicProgressRepository.findById("ST001")).thenReturn(Optional.of(progress));

        List<String> alertas = studentPortalService.getAcademicAlerts("ST001");

        assertNotNull(alertas);
        assertFalse(alertas.isEmpty());
        assertTrue(alertas.stream().anyMatch(alerta -> alerta.contains("GPA")));
    }

    @Test
    @DisplayName("Caso exitoso - getAcademicAlerts con progreso lento")
    void testGetAcademicAlerts_ProgresoLento() {

        CourseStatusDetail cursoAprobado = new CourseStatusDetail("CSD001", course1, CourseStatus.PASSED,
                3.5, "2024-2", new Date(), new Date(), group1, null, 3, null, "Aprobado");

        StudentAcademicProgress progress = new StudentAcademicProgress("PROG007", student,
                "Ingeniería de Sistemas", "Facultad de Ingeniería", "Regular", 5, 10,
                20, 150, 3.8, Arrays.asList(cursoAprobado));

        when(studentAcademicProgressRepository.findById("ST001")).thenReturn(Optional.of(progress));

        List<String> alertas = studentPortalService.getAcademicAlerts("ST001");

        assertNotNull(alertas);
        assertTrue(alertas.stream().anyMatch(alerta -> alerta.contains("créditos")));
    }



    @Test
    @DisplayName("Caso borde - getAcademicAlerts sin alertas")
    void testGetAcademicAlerts_SinAlertas() {
        // GPA bueno, progreso adecuado, sin materias reprobadas
        CourseStatusDetail cursoAprobado = new CourseStatusDetail("CSD001", course1, CourseStatus.PASSED,
                4.0, "2024-2", new Date(), new Date(), group1, null, 3, null, "Aprobado");

        StudentAcademicProgress progress = new StudentAcademicProgress("PROG009", student,
                "Ingeniería de Sistemas", "Facultad de Ingeniería", "Regular", 6, 10,
                90, 150, 3.8, Arrays.asList(cursoAprobado));

        when(studentAcademicProgressRepository.findById("ST001")).thenReturn(Optional.of(progress));

        List<String> alertas = studentPortalService.getAcademicAlerts("ST001");

        assertNotNull(alertas);

    }



    @Test
    @DisplayName("Caso borde - getWaitingList siempre retorna lista vacía")
    void testGetWaitingList_RetornaListaVacia() {
        List<ScheduleChangeRequest> resultado = studentPortalService.getWaitingList("GRP001");

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Caso borde - getCurrentEnrollment con capacidad cero")
    void testGetCurrentEnrollment_CapacidadCero() {
        Classroom aulaSinCapacidad = new Classroom("CL003", "Edificio C", "C101", 0, RoomType.REGULAR);
        Group grupoSinCapacidad = new Group("GR004", "04", course1, null, null, aulaSinCapacidad);

        when(groupRepository.findById("GR004")).thenReturn(Optional.of(grupoSinCapacidad));

        Integer resultado = studentPortalService.getCurrentEnrollment("GR004");

        assertNotNull(resultado);
        assertEquals(0, resultado);
    }

    @Test
    @DisplayName("Caso exitoso - getCurrentSchedule con múltiples grupos por curso")
    void testGetCurrentSchedule_MultiplesGruposPorCurso() {
        CourseStatusDetail cursoEnProgreso = new CourseStatusDetail("CSD001", course1, CourseStatus.IN_PROGRESS,
                null, "2025-1", new Date(), null, group1, null, 3, null, "En progreso");

        StudentAcademicProgress progress = new StudentAcademicProgress("PROG010", student,
                "Ingeniería de Sistemas", "Facultad de Ingeniería", "Regular", 5, 10,
                45, 160, 3.8, Arrays.asList(cursoEnProgreso));


        Group grupoAdicional = new Group("GR005", "02", course1, null, null, classroom);

        when(studentAcademicProgressRepository.findById("ST001")).thenReturn(Optional.of(progress));
        when(groupRepository.findByCourse_CourseCode("CS101")).thenReturn(Arrays.asList(group1, grupoAdicional));

        List<Group> resultado = studentPortalService.getCurrentSchedule("ST001");

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
    }

    @Test
    @DisplayName("Caso borde - getAcademicAlerts con curso en estado ENROLLED")
    void testGetAcademicAlerts_CursoEnrolled() {
        CourseStatusDetail cursoEnrolled = new CourseStatusDetail("CSD001", course1, CourseStatus.ENROLLED,
                null, "2025-1", new Date(), null, group1, null, 3, null, "Matriculado");

        StudentAcademicProgress progress = new StudentAcademicProgress("PROG011", student,
                "Ingeniería de Sistemas", "Facultad de Ingeniería", "Regular", 5, 10,
                45, 150, 3.8, Arrays.asList(cursoEnrolled));

        when(studentAcademicProgressRepository.findById("ST001")).thenReturn(Optional.of(progress));

        List<String> alertas = studentPortalService.getAcademicAlerts("ST001");

        assertNotNull(alertas);

    }

    @Test
    @DisplayName("Caso borde - getAcademicAlerts con curso WITHDRAWN")
    void testGetAcademicAlerts_CursoWithdrawn() {
        CourseStatusDetail cursoWithdrawn = new CourseStatusDetail("CSD001", course1, CourseStatus.WITHDRAWN,
                null, "2025-1", new Date(), new Date(), group1, null, 0, false, "Retirado");

        StudentAcademicProgress progress = new StudentAcademicProgress("PROG012", student,
                "Ingeniería de Sistemas", "Facultad de Ingeniería", "Regular", 5, 10,
                45, 150, 3.8, Arrays.asList(cursoWithdrawn));

        when(studentAcademicProgressRepository.findById("ST001")).thenReturn(Optional.of(progress));

        List<String> alertas = studentPortalService.getAcademicAlerts("ST001");

        assertNotNull(alertas);

    }

}
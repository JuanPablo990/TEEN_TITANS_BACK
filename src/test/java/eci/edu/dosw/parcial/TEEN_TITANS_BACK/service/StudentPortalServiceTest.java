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
    private Course course;
    private AcademicPeriod academicPeriod;
    private CourseStatusDetail courseStatusDetail;
    private Group group;
    private Classroom classroom;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId("STU001");
        student.setName("John Doe");
        student.setActive(true);

        course = new Course();
        course.setCourseCode("CS101");
        course.setName("Introduction to Computer Science");
        course.setIsActive(true);
        course.setAcademicProgram("Computer Science");

        classroom = new Classroom();
        classroom.setCapacity(30);

        group = new Group();
        group.setGroupId("GROUP_001");
        group.setCourse(course);
        group.setClassroom(classroom);

        academicPeriod = new AcademicPeriod();
        academicPeriod.setPeriodId("PERIOD_001");
        academicPeriod.setName("2025-1");
        academicPeriod.setActive(true);

        courseStatusDetail = new CourseStatusDetail();
        courseStatusDetail.setCourse(course);
        courseStatusDetail.setGroup(group);
        courseStatusDetail.setIsApproved(null);
        courseStatusDetail.setEnrollmentDate(new Date());
        courseStatusDetail.setCompletionDate(null);

        progress = new StudentAcademicProgress();
        progress.setId("PROG_001");
        progress.setStudent(student);
        progress.setAcademicProgram("Computer Science");
        progress.setCurrentSemester(5);
        progress.setTotalSemesters(10);
        progress.setCompletedCredits(60);
        progress.setTotalCreditsRequired(120);
        progress.setCumulativeGPA(3.8);
        progress.setCoursesStatus(Arrays.asList(courseStatusDetail));
    }

    @Test
    @DisplayName("Caso exitoso - getCourse retorna curso del grupo")
    void testGetCourse_Exitoso() {
        when(groupRepository.findById("GROUP_001")).thenReturn(Optional.of(group));

        Course resultado = studentPortalService.getCourse("GROUP_001");

        assertAll("Verificar obtención de curso del grupo",
                () -> assertNotNull(resultado),
                () -> assertEquals("CS101", resultado.getCourseCode()),
                () -> assertEquals("Introduction to Computer Science", resultado.getName())
        );

        verify(groupRepository, times(1)).findById("GROUP_001");
    }

    @Test
    @DisplayName("Caso error - getCourse lanza excepción cuando grupo no existe")
    void testGetCourse_GrupoNoExiste() {
        when(groupRepository.findById("GRUPO_INEXISTENTE")).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            studentPortalService.getCourse("GRUPO_INEXISTENTE");
        });

        assertEquals("Grupo no encontrado: GRUPO_INEXISTENTE", exception.getMessage());
        verify(groupRepository, times(1)).findById("GRUPO_INEXISTENTE");
    }

    @Test
    @DisplayName("Caso exitoso - getMaxCapacity retorna capacidad del aula")
    void testGetMaxCapacity_Exitoso() {
        when(groupRepository.findById("GROUP_001")).thenReturn(Optional.of(group));

        Integer resultado = studentPortalService.getMaxCapacity("GROUP_001");

        assertAll("Verificar obtención de capacidad máxima",
                () -> assertNotNull(resultado),
                () -> assertEquals(30, resultado)
        );

        verify(groupRepository, times(1)).findById("GROUP_001");
    }

    @Test
    @DisplayName("Caso exitoso - getCurrentEnrollment retorna matrícula estimada")
    void testGetCurrentEnrollment_Exitoso() {
        when(groupRepository.findById("GROUP_001")).thenReturn(Optional.of(group));

        Integer resultado = studentPortalService.getCurrentEnrollment("GROUP_001");

        assertAll("Verificar cálculo de matrícula estimada",
                () -> assertNotNull(resultado),
                () -> assertEquals(18, resultado)
        );

        verify(groupRepository, times(1)).findById("GROUP_001");
    }

    @Test
    @DisplayName("Caso borde - getWaitingList retorna lista vacía")
    void testGetWaitingList_ListaVacia() {
        List<ScheduleChangeRequest> resultado = studentPortalService.getWaitingList("GROUP_001");

        assertAll("Verificar lista de espera vacía",
                () -> assertNotNull(resultado),
                () -> assertTrue(resultado.isEmpty())
        );
    }

    @Test
    @DisplayName("Caso exitoso - getTotalEnrolledByCourse retorna mapa de inscritos")
    void testGetTotalEnrolledByCourse_Exitoso() {
        List<Group> grupos = Arrays.asList(group);
        when(groupRepository.findByCourse_CourseCode("CS101")).thenReturn(grupos);
        when(groupRepository.findById("GROUP_001")).thenReturn(Optional.of(group));

        Map<String, Integer> resultado = studentPortalService.getTotalEnrolledByCourse("CS101");

        assertAll("Verificar mapa de inscritos por curso",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals(18, resultado.get("GROUP_001"))
        );

        verify(groupRepository, times(1)).findByCourse_CourseCode("CS101");
        verify(groupRepository, times(1)).findById("GROUP_001");
    }

    @Test
    @DisplayName("Caso error - getTotalEnrolledByCourse lanza excepción cuando no hay grupos")
    void testGetTotalEnrolledByCourse_SinGrupos() {
        when(groupRepository.findByCourse_CourseCode("CURSO_SIN_GRUPOS")).thenReturn(Collections.emptyList());

        AppException exception = assertThrows(AppException.class, () -> {
            studentPortalService.getTotalEnrolledByCourse("CURSO_SIN_GRUPOS");
        });

        assertEquals("No se encontraron grupos para el curso: CURSO_SIN_GRUPOS", exception.getMessage());
        verify(groupRepository, times(1)).findByCourse_CourseCode("CURSO_SIN_GRUPOS");
    }

    @Test
    @DisplayName("Caso exitoso - getCurrentSchedule retorna horario del estudiante")
    void testGetCurrentSchedule_Exitoso() {
        when(studentAcademicProgressRepository.findByStudentId("STU001")).thenReturn(Optional.of(progress));
        when(groupRepository.findById("GROUP_001")).thenReturn(Optional.of(group));

        List<Group> resultado = studentPortalService.getCurrentSchedule("STU001");

        assertAll("Verificar horario actual del estudiante",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("GROUP_001", resultado.get(0).getGroupId())
        );

        verify(studentAcademicProgressRepository, times(1)).findByStudentId("STU001");
        verify(groupRepository, times(1)).findById("GROUP_001");
    }

    @Test
    @DisplayName("Caso error - getCurrentSchedule lanza excepción cuando no hay progreso")
    void testGetCurrentSchedule_SinProgreso() {
        when(studentAcademicProgressRepository.findByStudentId("STU_INEXISTENTE")).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            studentPortalService.getCurrentSchedule("STU_INEXISTENTE");
        });

        assertEquals("Progreso académico no encontrado para el estudiante: STU_INEXISTENTE", exception.getMessage());
        verify(studentAcademicProgressRepository, times(1)).findByStudentId("STU_INEXISTENTE");
    }

    @Test
    @DisplayName("Caso exitoso - getAvailableGroups retorna grupos disponibles")
    void testGetAvailableGroups_Exitoso() {
        List<Group> grupos = Arrays.asList(group);
        when(groupRepository.findByCourse_CourseCode("CS101")).thenReturn(grupos);
        when(groupRepository.findById("GROUP_001")).thenReturn(Optional.of(group));

        List<Group> resultado = studentPortalService.getAvailableGroups("CS101");

        assertAll("Verificar grupos disponibles",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("GROUP_001", resultado.get(0).getGroupId())
        );

        verify(groupRepository, times(1)).findByCourse_CourseCode("CS101");
        verify(groupRepository, times(2)).findById("GROUP_001");
    }

    @Test
    @DisplayName("Caso exitoso - getAcademicProgress retorna progreso académico")
    void testGetAcademicProgress_Exitoso() {
        when(studentAcademicProgressRepository.findByStudentId("STU001")).thenReturn(Optional.of(progress));

        StudentAcademicProgress resultado = studentPortalService.getAcademicProgress("STU001");

        assertAll("Verificar obtención de progreso académico",
                () -> assertNotNull(resultado),
                () -> assertEquals("PROG_001", resultado.getId()),
                () -> assertEquals("STU001", resultado.getStudent().getId()),
                () -> assertEquals(3.8, resultado.getCumulativeGPA())
        );

        verify(studentAcademicProgressRepository, times(1)).findByStudentId("STU001");
    }

    @Test
    @DisplayName("Caso exitoso - checkGroupAvailability retorna true para grupo con cupos")
    void testCheckGroupAvailability_ConCupos() {
        when(groupRepository.findById("GROUP_001")).thenReturn(Optional.of(group));

        boolean resultado = studentPortalService.checkGroupAvailability("GROUP_001");

        assertTrue(resultado);
        verify(groupRepository, times(2)).findById("GROUP_001");
    }



    @Test
    @DisplayName("Caso exitoso - getCourseRecommendations retorna cursos recomendados")
    void testGetCourseRecommendations_Exitoso() {
        Course cursoRecomendado = new Course();
        cursoRecomendado.setCourseCode("CS102");
        cursoRecomendado.setName("Data Structures");
        cursoRecomendado.setIsActive(true);
        cursoRecomendado.setAcademicProgram("Computer Science");

        when(studentAcademicProgressRepository.findByStudentId("STU001")).thenReturn(Optional.of(progress));
        when(courseRepository.findByAcademicProgramAndIsActive("Computer Science", true))
                .thenReturn(Arrays.asList(course, cursoRecomendado));

        List<Course> resultado = studentPortalService.getCourseRecommendations("STU001");

        assertAll("Verificar recomendaciones de cursos",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("CS102", resultado.get(0).getCourseCode())
        );

        verify(studentAcademicProgressRepository, times(1)).findByStudentId("STU001");
        verify(courseRepository, times(1)).findByAcademicProgramAndIsActive("Computer Science", true);
    }

    @Test
    @DisplayName("Caso exitoso - getEnrollmentDeadlines retorna periodo académico activo")
    void testGetEnrollmentDeadlines_Exitoso() {
        when(academicPeriodRepository.findByIsActiveTrue()).thenReturn(Optional.of(academicPeriod));

        AcademicPeriod resultado = studentPortalService.getEnrollmentDeadlines();

        assertAll("Verificar obtención de periodo académico",
                () -> assertNotNull(resultado),
                () -> assertEquals("PERIOD_001", resultado.getPeriodId()),
                () -> assertEquals("2025-1", resultado.getName()),
                () -> assertTrue(resultado.isActive())
        );

        verify(academicPeriodRepository, times(1)).findByIsActiveTrue();
    }

    @Test
    @DisplayName("Caso error - getEnrollmentDeadlines lanza excepción cuando no hay periodo activo")
    void testGetEnrollmentDeadlines_SinPeriodoActivo() {
        when(academicPeriodRepository.findByIsActiveTrue()).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            studentPortalService.getEnrollmentDeadlines();
        });

        assertEquals("No hay periodos académicos activos", exception.getMessage());
        verify(academicPeriodRepository, times(1)).findByIsActiveTrue();
    }

    @Test
    @DisplayName("Caso exitoso - getAcademicAlerts retorna alertas académicas")
    void testGetAcademicAlerts_Exitoso() {
        when(studentAcademicProgressRepository.findByStudentId("STU001")).thenReturn(Optional.of(progress));

        List<String> resultado = studentPortalService.getAcademicAlerts("STU001");

        assertAll("Verificar alertas académicas",
                () -> assertNotNull(resultado),
                () -> assertTrue(resultado.isEmpty())
        );

        verify(studentAcademicProgressRepository, times(1)).findByStudentId("STU001");
    }


    @Test
    @DisplayName("Caso borde - getCurrentSchedule con curso no inscrito")
    void testGetCurrentSchedule_CursoNoInscrito() {
        CourseStatusDetail cursoCompletado = new CourseStatusDetail();
        cursoCompletado.setCourse(course);
        cursoCompletado.setGroup(group);
        cursoCompletado.setIsApproved(true);
        cursoCompletado.setEnrollmentDate(new Date());
        cursoCompletado.setCompletionDate(new Date());

        StudentAcademicProgress progresoConCursoCompletado = new StudentAcademicProgress();
        progresoConCursoCompletado.setStudent(student);
        progresoConCursoCompletado.setCoursesStatus(Arrays.asList(cursoCompletado));

        when(studentAcademicProgressRepository.findByStudentId("STU001")).thenReturn(Optional.of(progresoConCursoCompletado));

        List<Group> resultado = studentPortalService.getCurrentSchedule("STU001");

        assertAll("Verificar horario sin cursos inscritos",
                () -> assertNotNull(resultado),
                () -> assertTrue(resultado.isEmpty())
        );

        verify(studentAcademicProgressRepository, times(1)).findByStudentId("STU001");
    }

    @Test
    @DisplayName("Caso borde - getCourseRecommendations sin cursos disponibles")
    void testGetCourseRecommendations_SinCursosDisponibles() {
        when(studentAcademicProgressRepository.findByStudentId("STU001")).thenReturn(Optional.of(progress));
        when(courseRepository.findByAcademicProgramAndIsActive("Computer Science", true))
                .thenReturn(Arrays.asList(course));

        List<Course> resultado = studentPortalService.getCourseRecommendations("STU001");

        assertAll("Verificar recomendaciones sin cursos disponibles",
                () -> assertNotNull(resultado),
                () -> assertTrue(resultado.isEmpty())
        );

        verify(studentAcademicProgressRepository, times(1)).findByStudentId("STU001");
        verify(courseRepository, times(1)).findByAcademicProgramAndIsActive("Computer Science", true);
    }
}
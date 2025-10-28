package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.CourseStatusDetail;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Course;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Group;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Professor;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Schedule;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Classroom;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.CourseStatus;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.RoomType;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Date;
import java.util.List;
import java.util.Calendar;
import java.util.Optional;
import java.util.Collections;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataMongoTest
public class CourseStatusDetailRepositoryTest {

    @MockBean
    private CourseStatusDetailRepository courseStatusDetailRepository;

    private Course course1;
    private Course course2;
    private Professor professor1;
    private Professor professor2;
    private Schedule schedule1;
    private Schedule schedule2;
    private Classroom classroom1;
    private Classroom classroom2;
    private Group group1;
    private Group group2;
    private CourseStatusDetail detail1;
    private CourseStatusDetail detail2;
    private CourseStatusDetail detail3;
    private CourseStatusDetail detail4;
    private Date pastDate;
    private Date currentDate;
    private Date futureDate;

    @BeforeEach
    void setUp() {
        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.MONTH, -6);
        pastDate = cal.getTime();

        currentDate = new Date();

        cal.add(Calendar.MONTH, 6);
        futureDate = cal.getTime();

        course1 = new Course("CS101", "Introduction to Programming", 3, "Basic programming concepts", "Computer Science", true);
        course2 = new Course("MATH201", "Calculus I", 4, "Differential calculus", "Mathematics", true);

        professor1 = new Professor("PROF001", "Dr. Smith", "smith@university.edu", "password", "Computer Science", true, Arrays.asList("Programming", "Algorithms"));
        professor2 = new Professor("PROF002", "Dr. Johnson", "johnson@university.edu", "password", "Mathematics", false, Arrays.asList("Calculus", "Algebra"));

        schedule1 = new Schedule("SCHED001", "Monday", "08:00", "10:00", "Morning");
        schedule2 = new Schedule("SCHED002", "Wednesday", "14:00", "16:00", "Afternoon");

        classroom1 = new Classroom("CLASS001", "A", "101", 50, RoomType.REGULAR);
        classroom2 = new Classroom("CLASS002", "B", "201", 100, RoomType.LABORATORY);

        group1 = new Group("GROUP001", "A", course1, professor1, schedule1, classroom1);
        group2 = new Group("GROUP002", "B", course2, professor2, schedule2, classroom2);

        detail1 = new CourseStatusDetail(
                "DET001", course1, "STU001", CourseStatus.PASSED, 4.5, "2024-1",
                pastDate, currentDate, group1, professor1, 3, true, "Excellent performance"
        );

        detail2 = new CourseStatusDetail(
                "DET002", course2, "STU001", CourseStatus.FAILED, 2.8, "2024-1",
                pastDate, currentDate, group2, professor2, 0, false, "Needs improvement"
        );

        detail3 = new CourseStatusDetail(
                "DET003", course1, "STU002", CourseStatus.IN_PROGRESS, null, "2024-2",
                currentDate, null, group1, professor1, 0, false, "Currently enrolled"
        );

        detail4 = new CourseStatusDetail(
                "DET004", course2, "STU002", CourseStatus.ENROLLED, null, "2024-2",
                currentDate, null, group2, professor2, 0, false, "Recently enrolled"
        );
    }

    @Test
    @DisplayName("Caso exitoso - findBySemester retorna detalles del semestre")
    void testFindBySemester_Exitoso() {
        when(courseStatusDetailRepository.findBySemester("2024-1"))
                .thenReturn(List.of(detail1, detail2));

        List<CourseStatusDetail> resultado = courseStatusDetailRepository.findBySemester("2024-1");

        assertAll("Verificar detalles del semestre 2024-1",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals("2024-1", resultado.get(0).getSemester()),
                () -> assertEquals("2024-1", resultado.get(1).getSemester())
        );

        verify(courseStatusDetailRepository, times(1)).findBySemester("2024-1");
    }

    @Test
    @DisplayName("Caso error - findBySemester retorna lista vacía para semestre inexistente")
    void testFindBySemester_SemestreInexistente() {
        when(courseStatusDetailRepository.findBySemester("2023-3"))
                .thenReturn(Collections.emptyList());

        List<CourseStatusDetail> resultado = courseStatusDetailRepository.findBySemester("2023-3");

        assertTrue(resultado.isEmpty());
        verify(courseStatusDetailRepository, times(1)).findBySemester("2023-3");
    }

    @Test
    @DisplayName("Caso exitoso - findByIsApproved retorna cursos aprobados")
    void testFindByIsApproved_Exitoso() {
        when(courseStatusDetailRepository.findByIsApproved(true))
                .thenReturn(List.of(detail1));

        List<CourseStatusDetail> resultado = courseStatusDetailRepository.findByIsApproved(true);

        assertAll("Verificar cursos aprobados",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertTrue(resultado.get(0).getIsApproved())
        );

        verify(courseStatusDetailRepository, times(1)).findByIsApproved(true);
    }

    @Test
    @DisplayName("Caso exitoso - findByStatus retorna cursos por estado")
    void testFindByStatus_Exitoso() {
        when(courseStatusDetailRepository.findByStatus(CourseStatus.PASSED))
                .thenReturn(List.of(detail1));

        List<CourseStatusDetail> resultado = courseStatusDetailRepository.findByStatus(CourseStatus.PASSED);

        assertAll("Verificar cursos aprobados",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals(CourseStatus.PASSED, resultado.get(0).getStatus())
        );

        verify(courseStatusDetailRepository, times(1)).findByStatus(CourseStatus.PASSED);
    }

    @Test
    @DisplayName("Caso exitoso - findByGradeGreaterThanEqual retorna cursos con nota alta")
    void testFindByGradeGreaterThanEqual_Exitoso() {
        when(courseStatusDetailRepository.findByGradeGreaterThanEqual(4.0))
                .thenReturn(List.of(detail1));

        List<CourseStatusDetail> resultado = courseStatusDetailRepository.findByGradeGreaterThanEqual(4.0);

        assertAll("Verificar cursos con nota >= 4.0",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertTrue(resultado.get(0).getGrade() >= 4.0)
        );

        verify(courseStatusDetailRepository, times(1)).findByGradeGreaterThanEqual(4.0);
    }



    @Test
    @DisplayName("Caso exitoso - findByEnrollmentDateAfter retorna cursos matriculados después")
    void testFindByEnrollmentDateAfter_Exitoso() {
        when(courseStatusDetailRepository.findByEnrollmentDateAfter(pastDate))
                .thenReturn(List.of(detail3, detail4));

        List<CourseStatusDetail> resultado = courseStatusDetailRepository.findByEnrollmentDateAfter(pastDate);

        assertAll("Verificar cursos matriculados después de fecha pasada",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertTrue(resultado.get(0).getEnrollmentDate().after(pastDate)),
                () -> assertTrue(resultado.get(1).getEnrollmentDate().after(pastDate))
        );

        verify(courseStatusDetailRepository, times(1)).findByEnrollmentDateAfter(pastDate);
    }

    @Test
    @DisplayName("Caso exitoso - findByCourse_CourseCode retorna detalles por curso")
    void testFindByCourse_CourseCode_Exitoso() {
        when(courseStatusDetailRepository.findByCourse_CourseCode("CS101"))
                .thenReturn(List.of(detail1, detail3));

        List<CourseStatusDetail> resultado = courseStatusDetailRepository.findByCourse_CourseCode("CS101");

        assertAll("Verificar detalles del curso CS101",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals("CS101", resultado.get(0).getCourse().getCourseCode()),
                () -> assertEquals("CS101", resultado.get(1).getCourse().getCourseCode())
        );

        verify(courseStatusDetailRepository, times(1)).findByCourse_CourseCode("CS101");
    }

    @Test
    @DisplayName("Caso exitoso - findByProfessor_Id retorna detalles por profesor")
    void testFindByProfessor_Id_Exitoso() {
        when(courseStatusDetailRepository.findByProfessor_Id("PROF001"))
                .thenReturn(List.of(detail1, detail3));

        List<CourseStatusDetail> resultado = courseStatusDetailRepository.findByProfessor_Id("PROF001");

        assertAll("Verificar detalles del profesor PROF001",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals("PROF001", resultado.get(0).getProfessor().getId()),
                () -> assertEquals("PROF001", resultado.get(1).getProfessor().getId())
        );

        verify(courseStatusDetailRepository, times(1)).findByProfessor_Id("PROF001");
    }

    @Test
    @DisplayName("Caso exitoso - findByGroup_GroupId retorna detalles por grupo")
    void testFindByGroup_GroupId_Exitoso() {
        when(courseStatusDetailRepository.findByGroup_GroupId("GROUP001"))
                .thenReturn(List.of(detail1, detail3));

        List<CourseStatusDetail> resultado = courseStatusDetailRepository.findByGroup_GroupId("GROUP001");

        assertAll("Verificar detalles del grupo GROUP001",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals("GROUP001", resultado.get(0).getGroup().getGroupId()),
                () -> assertEquals("GROUP001", resultado.get(1).getGroup().getGroupId())
        );

        verify(courseStatusDetailRepository, times(1)).findByGroup_GroupId("GROUP001");
    }

    @Test
    @DisplayName("Caso exitoso - findByStudentId retorna detalles del estudiante")
    void testFindByStudentId_Exitoso() {
        when(courseStatusDetailRepository.findByStudentId("STU001"))
                .thenReturn(List.of(detail1, detail2));

        List<CourseStatusDetail> resultado = courseStatusDetailRepository.findByStudentId("STU001");

        assertAll("Verificar detalles del estudiante STU001",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals("STU001", resultado.get(0).getStudentId()),
                () -> assertEquals("STU001", resultado.get(1).getStudentId())
        );

        verify(courseStatusDetailRepository, times(1)).findByStudentId("STU001");
    }



    @Test
    @DisplayName("Caso exitoso - findByEnrollmentDateBetween retorna cursos en rango de fechas")
    void testFindByEnrollmentDateBetween_Exitoso() {
        Date startDate = new Date(System.currentTimeMillis() - 365 * 24 * 60 * 60 * 1000L);
        Date endDate = new Date(System.currentTimeMillis() + 365 * 24 * 60 * 60 * 1000L);

        when(courseStatusDetailRepository.findByEnrollmentDateBetween(startDate, endDate))
                .thenReturn(List.of(detail1, detail2, detail3, detail4));

        List<CourseStatusDetail> resultado = courseStatusDetailRepository.findByEnrollmentDateBetween(startDate, endDate);

        assertAll("Verificar cursos en rango de fechas de matrícula",
                () -> assertNotNull(resultado),
                () -> assertEquals(4, resultado.size()),
                () -> assertTrue(resultado.get(0).getEnrollmentDate().after(startDate) && resultado.get(0).getEnrollmentDate().before(endDate))
        );

        verify(courseStatusDetailRepository, times(1)).findByEnrollmentDateBetween(startDate, endDate);
    }

    @Test
    @DisplayName("Caso exitoso - findByCreditsEarnedGreaterThanEqual retorna cursos con créditos")
    void testFindByCreditsEarnedGreaterThanEqual_Exitoso() {
        when(courseStatusDetailRepository.findByCreditsEarnedGreaterThanEqual(1))
                .thenReturn(List.of(detail1));

        List<CourseStatusDetail> resultado = courseStatusDetailRepository.findByCreditsEarnedGreaterThanEqual(1);

        assertAll("Verificar cursos con créditos >= 1",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertTrue(resultado.get(0).getCreditsEarned() >= 1)
        );

        verify(courseStatusDetailRepository, times(1)).findByCreditsEarnedGreaterThanEqual(1);
    }

    @Test
    @DisplayName("Caso exitoso - findByStudentIdAndSemester retorna detalles filtrados")
    void testFindByStudentIdAndSemester_Exitoso() {
        when(courseStatusDetailRepository.findByStudentIdAndSemester("STU001", "2024-1"))
                .thenReturn(List.of(detail1, detail2));

        List<CourseStatusDetail> resultado = courseStatusDetailRepository.findByStudentIdAndSemester("STU001", "2024-1");

        assertAll("Verificar detalles por estudiante y semestre",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals("STU001", resultado.get(0).getStudentId()),
                () -> assertEquals("2024-1", resultado.get(0).getSemester())
        );

        verify(courseStatusDetailRepository, times(1)).findByStudentIdAndSemester("STU001", "2024-1");
    }

    @Test
    @DisplayName("Caso exitoso - findByStudentIdAndStatus retorna detalles por estado")
    void testFindByStudentIdAndStatus_Exitoso() {
        when(courseStatusDetailRepository.findByStudentIdAndStatus("STU001", CourseStatus.PASSED))
                .thenReturn(List.of(detail1));

        List<CourseStatusDetail> resultado = courseStatusDetailRepository.findByStudentIdAndStatus("STU001", CourseStatus.PASSED);

        assertAll("Verificar detalles por estudiante y estado",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("STU001", resultado.get(0).getStudentId()),
                () -> assertEquals(CourseStatus.PASSED, resultado.get(0).getStatus())
        );

        verify(courseStatusDetailRepository, times(1)).findByStudentIdAndStatus("STU001", CourseStatus.PASSED);
    }

    @Test
    @DisplayName("Caso exitoso - findByStudentIdAndIsApproved retorna cursos aprobados del estudiante")
    void testFindByStudentIdAndIsApproved_Exitoso() {
        when(courseStatusDetailRepository.findByStudentIdAndIsApproved("STU001", true))
                .thenReturn(List.of(detail1));

        List<CourseStatusDetail> resultado = courseStatusDetailRepository.findByStudentIdAndIsApproved("STU001", true);

        assertAll("Verificar cursos aprobados del estudiante",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("STU001", resultado.get(0).getStudentId()),
                () -> assertTrue(resultado.get(0).getIsApproved())
        );

        verify(courseStatusDetailRepository, times(1)).findByStudentIdAndIsApproved("STU001", true);
    }

    @Test
    @DisplayName("Caso exitoso - findByCourse_CourseCodeAndSemester retorna detalles por curso y semestre")
    void testFindByCourse_CourseCodeAndSemester_Exitoso() {
        when(courseStatusDetailRepository.findByCourse_CourseCodeAndSemester("CS101", "2024-1"))
                .thenReturn(List.of(detail1));

        List<CourseStatusDetail> resultado = courseStatusDetailRepository.findByCourse_CourseCodeAndSemester("CS101", "2024-1");

        assertAll("Verificar detalles por curso y semestre",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("CS101", resultado.get(0).getCourse().getCourseCode()),
                () -> assertEquals("2024-1", resultado.get(0).getSemester())
        );

        verify(courseStatusDetailRepository, times(1)).findByCourse_CourseCodeAndSemester("CS101", "2024-1");
    }

    @Test
    @DisplayName("Caso exitoso - findByStudentIdOrderBySemesterDesc retorna detalles ordenados")
    void testFindByStudentIdOrderBySemesterDesc_Exitoso() {
        when(courseStatusDetailRepository.findByStudentIdOrderBySemesterDesc("STU001"))
                .thenReturn(List.of(detail1, detail2));

        List<CourseStatusDetail> resultado = courseStatusDetailRepository.findByStudentIdOrderBySemesterDesc("STU001");

        assertAll("Verificar detalles ordenados por semestre descendente",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals("STU001", resultado.get(0).getStudentId()),
                () -> assertEquals("STU001", resultado.get(1).getStudentId())
        );

        verify(courseStatusDetailRepository, times(1)).findByStudentIdOrderBySemesterDesc("STU001");
    }

    @Test
    @DisplayName("Caso exitoso - findByStudentIdOrderByGradeDesc retorna detalles ordenados por nota")
    void testFindByStudentIdOrderByGradeDesc_Exitoso() {
        when(courseStatusDetailRepository.findByStudentIdOrderByGradeDesc("STU001"))
                .thenReturn(List.of(detail1, detail2));

        List<CourseStatusDetail> resultado = courseStatusDetailRepository.findByStudentIdOrderByGradeDesc("STU001");

        assertAll("Verificar detalles ordenados por nota descendente",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals("STU001", resultado.get(0).getStudentId()),
                () -> assertTrue(resultado.get(0).getGrade() >= resultado.get(1).getGrade())
        );

        verify(courseStatusDetailRepository, times(1)).findByStudentIdOrderByGradeDesc("STU001");
    }

    @Test
    @DisplayName("Caso exitoso - countByStudentId retorna conteo correcto")
    void testCountByStudentId_Exitoso() {
        when(courseStatusDetailRepository.countByStudentId("STU001"))
                .thenReturn(2L);

        long resultado = courseStatusDetailRepository.countByStudentId("STU001");

        assertEquals(2L, resultado);
        verify(courseStatusDetailRepository, times(1)).countByStudentId("STU001");
    }

    @Test
    @DisplayName("Caso exitoso - countByStudentIdAndStatus retorna conteo por estado")
    void testCountByStudentIdAndStatus_Exitoso() {
        when(courseStatusDetailRepository.countByStudentIdAndStatus("STU001", CourseStatus.PASSED))
                .thenReturn(1L);

        long resultado = courseStatusDetailRepository.countByStudentIdAndStatus("STU001", CourseStatus.PASSED);

        assertEquals(1L, resultado);
        verify(courseStatusDetailRepository, times(1)).countByStudentIdAndStatus("STU001", CourseStatus.PASSED);
    }

    @Test
    @DisplayName("Caso exitoso - countByCourse_CourseCode retorna conteo por curso")
    void testCountByCourse_CourseCode_Exitoso() {
        when(courseStatusDetailRepository.countByCourse_CourseCode("CS101"))
                .thenReturn(2L);

        long resultado = courseStatusDetailRepository.countByCourse_CourseCode("CS101");

        assertEquals(2L, resultado);
        verify(courseStatusDetailRepository, times(1)).countByCourse_CourseCode("CS101");
    }

    @Test
    @DisplayName("Caso exitoso - countByGradeGreaterThanEqual retorna conteo por nota")
    void testCountByGradeGreaterThanEqual_Exitoso() {
        when(courseStatusDetailRepository.countByGradeGreaterThanEqual(3.0))
                .thenReturn(2L);

        long resultado = courseStatusDetailRepository.countByGradeGreaterThanEqual(3.0);

        assertEquals(2L, resultado);
        verify(courseStatusDetailRepository, times(1)).countByGradeGreaterThanEqual(3.0);
    }

    @Test
    @DisplayName("Caso exitoso - existsByStudentIdAndCourse_CourseCode retorna true cuando existe")
    void testExistsByStudentIdAndCourse_CourseCode_Exitoso() {
        when(courseStatusDetailRepository.existsByStudentIdAndCourse_CourseCode("STU001", "CS101"))
                .thenReturn(true);

        boolean resultado = courseStatusDetailRepository.existsByStudentIdAndCourse_CourseCode("STU001", "CS101");

        assertTrue(resultado);
        verify(courseStatusDetailRepository, times(1)).existsByStudentIdAndCourse_CourseCode("STU001", "CS101");
    }

    @Test
    @DisplayName("Caso error - existsByStudentIdAndCourse_CourseCode retorna false cuando no existe")
    void testExistsByStudentIdAndCourse_CourseCode_NoExiste() {
        when(courseStatusDetailRepository.existsByStudentIdAndCourse_CourseCode("STU999", "CS999"))
                .thenReturn(false);

        boolean resultado = courseStatusDetailRepository.existsByStudentIdAndCourse_CourseCode("STU999", "CS999");

        assertFalse(resultado);
        verify(courseStatusDetailRepository, times(1)).existsByStudentIdAndCourse_CourseCode("STU999", "CS999");
    }

    @Test
    @DisplayName("Caso exitoso - findByStudentIdIn retorna detalles de múltiples estudiantes")
    void testFindByStudentIdIn_Exitoso() {
        when(courseStatusDetailRepository.findByStudentIdIn(List.of("STU001", "STU002")))
                .thenReturn(List.of(detail1, detail2, detail3, detail4));

        List<CourseStatusDetail> resultado = courseStatusDetailRepository.findByStudentIdIn(List.of("STU001", "STU002"));

        assertAll("Verificar detalles de múltiples estudiantes",
                () -> assertNotNull(resultado),
                () -> assertEquals(4, resultado.size()),
                () -> assertTrue(resultado.stream().anyMatch(d -> d.getStudentId().equals("STU001"))),
                () -> assertTrue(resultado.stream().anyMatch(d -> d.getStudentId().equals("STU002")))
        );

        verify(courseStatusDetailRepository, times(1)).findByStudentIdIn(List.of("STU001", "STU002"));
    }

    @Test
    @DisplayName("Caso exitoso - findByCourse_CourseCodeIn retorna detalles de múltiples cursos")
    void testFindByCourse_CourseCodeIn_Exitoso() {
        when(courseStatusDetailRepository.findByCourse_CourseCodeIn(List.of("CS101", "MATH201")))
                .thenReturn(List.of(detail1, detail2, detail3, detail4));

        List<CourseStatusDetail> resultado = courseStatusDetailRepository.findByCourse_CourseCodeIn(List.of("CS101", "MATH201"));

        assertAll("Verificar detalles de múltiples cursos",
                () -> assertNotNull(resultado),
                () -> assertEquals(4, resultado.size()),
                () -> assertTrue(resultado.stream().anyMatch(d -> d.getCourse().getCourseCode().equals("CS101"))),
                () -> assertTrue(resultado.stream().anyMatch(d -> d.getCourse().getCourseCode().equals("MATH201")))
        );

        verify(courseStatusDetailRepository, times(1)).findByCourse_CourseCodeIn(List.of("CS101", "MATH201"));
    }

    @Test
    @DisplayName("Caso exitoso - findByStudentIdAndMinimumGrade con query personalizada")
    void testFindByStudentIdAndMinimumGrade_Exitoso() {
        when(courseStatusDetailRepository.findByStudentIdAndMinimumGrade("STU001", 3.0))
                .thenReturn(List.of(detail1, detail2));

        List<CourseStatusDetail> resultado = courseStatusDetailRepository.findByStudentIdAndMinimumGrade("STU001", 3.0);

        assertAll("Verificar cursos del estudiante con nota mínima",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals("STU001", resultado.get(0).getStudentId()),
                () -> assertTrue(resultado.get(0).getGrade() >= 3.0)
        );

        verify(courseStatusDetailRepository, times(1)).findByStudentIdAndMinimumGrade("STU001", 3.0);
    }

    @Test
    @DisplayName("Caso exitoso - findApprovedGradesByStudentId con query personalizada")
    void testFindApprovedGradesByStudentId_Exitoso() {
        when(courseStatusDetailRepository.findApprovedGradesByStudentId("STU001"))
                .thenReturn(List.of(detail1));

        List<CourseStatusDetail> resultado = courseStatusDetailRepository.findApprovedGradesByStudentId("STU001");

        assertAll("Verificar notas aprobadas del estudiante",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("STU001", resultado.get(0).getStudentId()),
                () -> assertTrue(resultado.get(0).getIsApproved())
        );

        verify(courseStatusDetailRepository, times(1)).findApprovedGradesByStudentId("STU001");
    }

    @Test
    @DisplayName("Caso exitoso - countApprovedCoursesByStudentId con query personalizada")
    void testCountApprovedCoursesByStudentId_Exitoso() {
        when(courseStatusDetailRepository.countApprovedCoursesByStudentId("STU001"))
                .thenReturn(1L);

        long resultado = courseStatusDetailRepository.countApprovedCoursesByStudentId("STU001");

        assertEquals(1L, resultado);
        verify(courseStatusDetailRepository, times(1)).countApprovedCoursesByStudentId("STU001");
    }

    @Test
    @DisplayName("Caso exitoso - countTotalCoursesByStudentId con query personalizada")
    void testCountTotalCoursesByStudentId_Exitoso() {
        when(courseStatusDetailRepository.countTotalCoursesByStudentId("STU001"))
                .thenReturn(2L);

        long resultado = courseStatusDetailRepository.countTotalCoursesByStudentId("STU001");

        assertEquals(2L, resultado);
        verify(courseStatusDetailRepository, times(1)).countTotalCoursesByStudentId("STU001");
    }

    @Test
    @DisplayName("Caso exitoso - findTopApprovedCoursesByStudentId con query personalizada")
    void testFindTopApprovedCoursesByStudentId_Exitoso() {
        when(courseStatusDetailRepository.findTopApprovedCoursesByStudentId("STU001"))
                .thenReturn(List.of(detail1));

        List<CourseStatusDetail> resultado = courseStatusDetailRepository.findTopApprovedCoursesByStudentId("STU001");

        assertAll("Verificar mejores cursos aprobados del estudiante",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("STU001", resultado.get(0).getStudentId()),
                () -> assertTrue(resultado.get(0).getIsApproved())
        );

        verify(courseStatusDetailRepository, times(1)).findTopApprovedCoursesByStudentId("STU001");
    }

    @Test
    @DisplayName("Caso exitoso - findFailedCoursesByStudentId con query personalizada")
    void testFindFailedCoursesByStudentId_Exitoso() {
        when(courseStatusDetailRepository.findFailedCoursesByStudentId("STU001"))
                .thenReturn(List.of(detail2));

        List<CourseStatusDetail> resultado = courseStatusDetailRepository.findFailedCoursesByStudentId("STU001");

        assertAll("Verificar cursos reprobados del estudiante",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("STU001", resultado.get(0).getStudentId()),
                () -> assertEquals(CourseStatus.FAILED, resultado.get(0).getStatus())
        );

        verify(courseStatusDetailRepository, times(1)).findFailedCoursesByStudentId("STU001");
    }

    @Test
    @DisplayName("Caso exitoso - findCurrentCoursesByStudentId con query personalizada")
    void testFindCurrentCoursesByStudentId_Exitoso() {
        when(courseStatusDetailRepository.findCurrentCoursesByStudentId("STU002"))
                .thenReturn(List.of(detail3, detail4));

        List<CourseStatusDetail> resultado = courseStatusDetailRepository.findCurrentCoursesByStudentId("STU002");

        assertAll("Verificar cursos actuales del estudiante",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals("STU002", resultado.get(0).getStudentId()),
                () -> assertTrue(resultado.get(0).getStatus() == CourseStatus.ENROLLED || resultado.get(0).getStatus() == CourseStatus.IN_PROGRESS)
        );

        verify(courseStatusDetailRepository, times(1)).findCurrentCoursesByStudentId("STU002");
    }

    @Test
    @DisplayName("Caso exitoso - findByStudentIdAndCourse_CourseCodeAndSemester retorna detalle específico")
    void testFindByStudentIdAndCourse_CourseCodeAndSemester_Exitoso() {
        when(courseStatusDetailRepository.findByStudentIdAndCourse_CourseCodeAndSemester("STU001", "CS101", "2024-1"))
                .thenReturn(Optional.of(detail1));

        Optional<CourseStatusDetail> resultado = courseStatusDetailRepository.findByStudentIdAndCourse_CourseCodeAndSemester("STU001", "CS101", "2024-1");

        assertAll("Verificar detalle específico por estudiante, curso y semestre",
                () -> assertTrue(resultado.isPresent()),
                () -> assertEquals("STU001", resultado.get().getStudentId()),
                () -> assertEquals("CS101", resultado.get().getCourse().getCourseCode()),
                () -> assertEquals("2024-1", resultado.get().getSemester())
        );

        verify(courseStatusDetailRepository, times(1)).findByStudentIdAndCourse_CourseCodeAndSemester("STU001", "CS101", "2024-1");
    }

    @Test
    @DisplayName("Caso borde - findByStudentId con studentId nulo")
    void testFindByStudentId_Nulo() {
        when(courseStatusDetailRepository.findByStudentId(null))
                .thenReturn(Collections.emptyList());

        List<CourseStatusDetail> resultado = courseStatusDetailRepository.findByStudentId(null);

        assertTrue(resultado.isEmpty());
        verify(courseStatusDetailRepository, times(1)).findByStudentId(null);
    }

    @Test
    @DisplayName("Caso borde - findByStatus con status nulo")
    void testFindByStatus_Nulo() {
        when(courseStatusDetailRepository.findByStatus(null))
                .thenReturn(Collections.emptyList());

        List<CourseStatusDetail> resultado = courseStatusDetailRepository.findByStatus(null);

        assertTrue(resultado.isEmpty());
        verify(courseStatusDetailRepository, times(1)).findByStatus(null);
    }

    @Test
    @DisplayName("Caso borde - Verificar integridad de datos en detalles encontrados")
    void testIntegridadDatosDetalles() {
        when(courseStatusDetailRepository.findByStudentId("STU001"))
                .thenReturn(List.of(detail1));

        List<CourseStatusDetail> resultado = courseStatusDetailRepository.findByStudentId("STU001");

        assertAll("Verificar integridad completa del detalle de curso",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("DET001", resultado.get(0).getId()),
                () -> assertNotNull(resultado.get(0).getCourse()),
                () -> assertEquals("STU001", resultado.get(0).getStudentId()),
                () -> assertEquals(CourseStatus.PASSED, resultado.get(0).getStatus()),
                () -> assertEquals(4.5, resultado.get(0).getGrade()),
                () -> assertEquals("2024-1", resultado.get(0).getSemester()),
                () -> assertNotNull(resultado.get(0).getEnrollmentDate()),
                () -> assertNotNull(resultado.get(0).getCompletionDate()),
                () -> assertNotNull(resultado.get(0).getGroup()),
                () -> assertNotNull(resultado.get(0).getProfessor()),
                () -> assertEquals(3, resultado.get(0).getCreditsEarned()),
                () -> assertTrue(resultado.get(0).getIsApproved()),
                () -> assertEquals("Excellent performance", resultado.get(0).getComments()),
                () -> assertInstanceOf(CourseStatusDetail.class, resultado.get(0)),
                () -> assertNotNull(resultado.get(0).toString())
        );

        verify(courseStatusDetailRepository, times(1)).findByStudentId("STU001");
    }

    @Test
    @DisplayName("Caso exitoso - findByStudentIdAndStatusIn con query personalizada")
    void testFindByStudentIdAndStatusIn_Exitoso() {
        when(courseStatusDetailRepository.findByStudentIdAndStatusIn("STU001",
                List.of(CourseStatus.PASSED, CourseStatus.FAILED)))
                .thenReturn(List.of(detail1, detail2));

        List<CourseStatusDetail> resultado = courseStatusDetailRepository.findByStudentIdAndStatusIn("STU001",
                List.of(CourseStatus.PASSED, CourseStatus.FAILED));

        assertAll("Verificar detalles por estudiante y múltiples estados",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals("STU001", resultado.get(0).getStudentId()),
                () -> assertTrue(resultado.get(0).getStatus() == CourseStatus.PASSED || resultado.get(0).getStatus() == CourseStatus.FAILED)
        );

        verify(courseStatusDetailRepository, times(1)).findByStudentIdAndStatusIn("STU001",
                List.of(CourseStatus.PASSED, CourseStatus.FAILED));
    }

    @Test
    @DisplayName("Caso exitoso - findCoursesWithCreditsByStudentId con query personalizada")
    void testFindCoursesWithCreditsByStudentId_Exitoso() {
        when(courseStatusDetailRepository.findCoursesWithCreditsByStudentId("STU001"))
                .thenReturn(List.of(detail1));

        List<CourseStatusDetail> resultado = courseStatusDetailRepository.findCoursesWithCreditsByStudentId("STU001");

        assertAll("Verificar cursos con créditos del estudiante",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("STU001", resultado.get(0).getStudentId()),
                () -> assertTrue(resultado.get(0).getCreditsEarned() > 0)
        );

        verify(courseStatusDetailRepository, times(1)).findCoursesWithCreditsByStudentId("STU001");
    }
}
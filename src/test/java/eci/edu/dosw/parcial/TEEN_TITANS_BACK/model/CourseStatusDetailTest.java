package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.CourseStatus;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.RoomType;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class CourseStatusDetailTest {

    private CourseStatusDetail courseStatusDetail;
    private Course course;
    private Group group;
    private Professor professor;
    private Schedule schedule;
    private Classroom classroom;
    private Date enrollmentDate;
    private Date completionDate;

    @BeforeEach
    void setUp() {
        enrollmentDate = new Date(System.currentTimeMillis() - (60L * 24 * 60 * 60 * 1000));
        completionDate = new Date(System.currentTimeMillis() - (10L * 24 * 60 * 60 * 1000));

        course = new Course(
                "MATH101",
                "Cálculo I",
                4,
                "Curso introductorio de cálculo diferencial e integral",
                "Ingeniería de Sistemas",
                true
        );

        schedule = new Schedule(
                "SCH001",
                "Lunes",
                "08:00",
                "10:00",
                "2025-1"
        );

        classroom = new Classroom(
                "CLS001",
                "Edificio Principal",
                "A101",
                40,
                RoomType.REGULAR
        );

        professor = new Professor(
                "PROF001",
                "Dr. Carlos Rodríguez",
                "carlos.rodriguez@titans.edu",
                "password123",
                "Matemáticas",
                true,
                Arrays.asList("Cálculo", "Álgebra", "Estadística")
        );

        group = new Group(
                "GRP001",
                "A",
                course,
                professor,
                schedule,
                classroom
        );

        courseStatusDetail = new CourseStatusDetail(
                "CSD001",
                course,
                "STU001",
                CourseStatus.PASSED,
                4.5,
                "2025-1",
                enrollmentDate,
                completionDate,
                group,
                professor,
                4,
                true,
                "Excelente desempeño en el curso"
        );
    }

    @Test
    @DisplayName("Caso exitoso - Constructor completo inicializa todos los campos")
    void testConstructorCompleto_Exitoso() {
        assertAll("Validar todos los campos del constructor completo",
                () -> assertEquals("CSD001", courseStatusDetail.getId()),
                () -> assertEquals(course, courseStatusDetail.getCourse()),
                () -> assertEquals("STU001", courseStatusDetail.getStudentId()),
                () -> assertEquals(CourseStatus.PASSED, courseStatusDetail.getStatus()),
                () -> assertEquals(4.5, courseStatusDetail.getGrade()),
                () -> assertEquals("2025-1", courseStatusDetail.getSemester()),
                () -> assertEquals(enrollmentDate, courseStatusDetail.getEnrollmentDate()),
                () -> assertEquals(completionDate, courseStatusDetail.getCompletionDate()),
                () -> assertEquals(group, courseStatusDetail.getGroup()),
                () -> assertEquals(professor, courseStatusDetail.getProfessor()),
                () -> assertEquals(4, courseStatusDetail.getCreditsEarned()),
                () -> assertTrue(courseStatusDetail.getIsApproved()),
                () -> assertEquals("Excelente desempeño en el curso", courseStatusDetail.getComments())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Constructor sin ID para generación automática")
    void testConstructorSinID_Exitoso() {
        CourseStatusDetail detailSinId = new CourseStatusDetail(
                course,
                "STU002",
                CourseStatus.IN_PROGRESS,
                null,
                "2025-1",
                enrollmentDate,
                null,
                group,
                professor,
                0,
                false,
                "Curso en progreso"
        );

        assertAll("Validar constructor sin ID",
                () -> assertNull(detailSinId.getId()),
                () -> assertEquals(course, detailSinId.getCourse()),
                () -> assertEquals("STU002", detailSinId.getStudentId()),
                () -> assertEquals(CourseStatus.IN_PROGRESS, detailSinId.getStatus()),
                () -> assertNull(detailSinId.getGrade()),
                () -> assertEquals("2025-1", detailSinId.getSemester()),
                () -> assertEquals(enrollmentDate, detailSinId.getEnrollmentDate()),
                () -> assertNull(detailSinId.getCompletionDate()),
                () -> assertEquals(group, detailSinId.getGroup()),
                () -> assertEquals(professor, detailSinId.getProfessor()),
                () -> assertEquals(0, detailSinId.getCreditsEarned()),
                () -> assertFalse(detailSinId.getIsApproved()),
                () -> assertEquals("Curso en progreso", detailSinId.getComments())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Constructor por defecto inicializa con valores por defecto")
    void testConstructorPorDefecto_Exitoso() {
        CourseStatusDetail detailVacio = new CourseStatusDetail();

        assertAll("Validar valores por defecto",
                () -> assertNull(detailVacio.getId()),
                () -> assertNull(detailVacio.getCourse()),
                () -> assertNull(detailVacio.getStudentId()),
                () -> assertNull(detailVacio.getStatus()),
                () -> assertNull(detailVacio.getGrade()),
                () -> assertNull(detailVacio.getSemester()),
                () -> assertNull(detailVacio.getEnrollmentDate()),
                () -> assertNull(detailVacio.getCompletionDate()),
                () -> assertNull(detailVacio.getGroup()),
                () -> assertNull(detailVacio.getProfessor()),
                () -> assertNull(detailVacio.getCreditsEarned()),
                () -> assertNull(detailVacio.getIsApproved()),
                () -> assertNull(detailVacio.getComments())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Setters y Getters funcionan correctamente")
    void testSettersAndGetters_Exitoso() {
        CourseStatusDetail detailTest = new CourseStatusDetail();
        Course newCourse = new Course("PHY101", "Física I", 3, "Curso de física", "Ingeniería", true);
        Group newGroup = new Group("GRP002", "B", newCourse, professor, schedule, classroom);

        detailTest.setId("CSD002");
        detailTest.setCourse(newCourse);
        detailTest.setStudentId("STU003");
        detailTest.setStatus(CourseStatus.FAILED);
        detailTest.setGrade(2.8);
        detailTest.setSemester("2025-1");
        detailTest.setEnrollmentDate(enrollmentDate);
        detailTest.setCompletionDate(completionDate);
        detailTest.setGroup(newGroup);
        detailTest.setProfessor(professor);
        detailTest.setCreditsEarned(0);
        detailTest.setIsApproved(false);
        detailTest.setComments("Necesita reforzamiento");

        assertAll("Validar setters y getters",
                () -> assertEquals("CSD002", detailTest.getId()),
                () -> assertEquals(newCourse, detailTest.getCourse()),
                () -> assertEquals("STU003", detailTest.getStudentId()),
                () -> assertEquals(CourseStatus.FAILED, detailTest.getStatus()),
                () -> assertEquals(2.8, detailTest.getGrade()),
                () -> assertEquals("2025-1", detailTest.getSemester()),
                () -> assertEquals(enrollmentDate, detailTest.getEnrollmentDate()),
                () -> assertEquals(completionDate, detailTest.getCompletionDate()),
                () -> assertEquals(newGroup, detailTest.getGroup()),
                () -> assertEquals(professor, detailTest.getProfessor()),
                () -> assertEquals(0, detailTest.getCreditsEarned()),
                () -> assertFalse(detailTest.getIsApproved()),
                () -> assertEquals("Necesita reforzamiento", detailTest.getComments())
        );
    }

    @Test
    @DisplayName("Caso borde - Campos con valores null")
    void testCamposConValoresNull_Borde() {
        CourseStatusDetail detailNull = new CourseStatusDetail(
                null, null, null, null, null, null, null, null, null, null, null, null
        );

        assertAll("Validar campos null",
                () -> assertNull(detailNull.getId()),
                () -> assertNull(detailNull.getCourse()),
                () -> assertNull(detailNull.getStudentId()),
                () -> assertNull(detailNull.getStatus()),
                () -> assertNull(detailNull.getGrade()),
                () -> assertNull(detailNull.getSemester()),
                () -> assertNull(detailNull.getEnrollmentDate()),
                () -> assertNull(detailNull.getCompletionDate()),
                () -> assertNull(detailNull.getGroup()),
                () -> assertNull(detailNull.getProfessor()),
                () -> assertNull(detailNull.getCreditsEarned()),
                () -> assertNull(detailNull.getIsApproved()),
                () -> assertNull(detailNull.getComments())
        );
    }

    @Test
    @DisplayName("Caso borde - Campos con valores vacíos")
    void testCamposConValoresVacios_Borde() {
        courseStatusDetail.setStudentId("");
        courseStatusDetail.setSemester("");
        courseStatusDetail.setComments("");

        assertAll("Validar campos vacíos",
                () -> assertEquals("", courseStatusDetail.getStudentId()),
                () -> assertEquals("", courseStatusDetail.getSemester()),
                () -> assertEquals("", courseStatusDetail.getComments())
        );
    }

    @Test
    @DisplayName("Caso borde - Grade con valores extremos")
    void testGradeValoresExtremos_Borde() {
        courseStatusDetail.setGrade(0.0);
        assertEquals(0.0, courseStatusDetail.getGrade());

        courseStatusDetail.setGrade(5.0);
        assertEquals(5.0, courseStatusDetail.getGrade());

        courseStatusDetail.setGrade(-1.0);
        assertEquals(-1.0, courseStatusDetail.getGrade());
    }

    @Test
    @DisplayName("Caso borde - CreditsEarned con valores extremos")
    void testCreditsEarnedValoresExtremos_Borde() {
        courseStatusDetail.setCreditsEarned(0);
        assertEquals(0, courseStatusDetail.getCreditsEarned());

        courseStatusDetail.setCreditsEarned(10);
        assertEquals(10, courseStatusDetail.getCreditsEarned());

        courseStatusDetail.setCreditsEarned(-1);
        assertEquals(-1, courseStatusDetail.getCreditsEarned());
    }

    @Test
    @DisplayName("Caso borde - Campo isApproved con diferentes valores booleanos")
    void testCampoIsApproved_ValoresBooleanos_Borde() {
        courseStatusDetail.setIsApproved(true);
        assertTrue(courseStatusDetail.getIsApproved());

        courseStatusDetail.setIsApproved(false);
        assertFalse(courseStatusDetail.getIsApproved());

        courseStatusDetail.setIsApproved(null);
        assertNull(courseStatusDetail.getIsApproved());
    }

    @Test
    @DisplayName("Caso exitoso - Equals y HashCode generados por Lombok")
    void testEqualsAndHashCode_Exitoso() {
        CourseStatusDetail detail1 = new CourseStatusDetail(
                "CSD001",
                course,
                "STU001",
                CourseStatus.PASSED,
                4.5,
                "2025-1",
                enrollmentDate,
                completionDate,
                group,
                professor,
                4,
                true,
                "Comentario"
        );

        CourseStatusDetail detail2 = new CourseStatusDetail(
                "CSD001",
                course,
                "STU001",
                CourseStatus.PASSED,
                4.5,
                "2025-1",
                enrollmentDate,
                completionDate,
                group,
                professor,
                4,
                true,
                "Comentario"
        );

        CourseStatusDetail detail3 = new CourseStatusDetail(
                "CSD002",
                course,
                "STU002",
                CourseStatus.FAILED,
                2.5,
                "2025-1",
                enrollmentDate,
                completionDate,
                group,
                professor,
                0,
                false,
                "Otro comentario"
        );

        assertEquals(detail1, detail2);
        assertNotEquals(detail1, detail3);
        assertEquals(detail1.hashCode(), detail2.hashCode());
        assertNotEquals(detail1.hashCode(), detail3.hashCode());
    }

    @Test
    @DisplayName("Caso borde - Equals con objetos null y diferente clase")
    void testEqualsCasosBorde_Borde() {
        CourseStatusDetail detailTest = new CourseStatusDetail(
                "CSD001",
                course,
                "STU001",
                CourseStatus.PASSED,
                4.5,
                "2025-1",
                enrollmentDate,
                completionDate,
                group,
                professor,
                4,
                true,
                "Comentario"
        );

        assertNotEquals(null, detailTest);
        assertNotEquals("No soy un CourseStatusDetail", detailTest);
    }

    @Test
    @DisplayName("Caso exitoso - ToString generado por Lombok")
    void testToString_Exitoso() {
        String resultadoToString = courseStatusDetail.toString();

        assertAll("ToString debe contener todos los campos principales",
                () -> assertTrue(resultadoToString.contains("CSD001")),
                () -> assertTrue(resultadoToString.contains("STU001")),
                () -> assertTrue(resultadoToString.contains("PASSED")),
                () -> assertTrue(resultadoToString.contains("4.5")),
                () -> assertTrue(resultadoToString.contains("2025-1")),
                () -> assertTrue(resultadoToString.contains("CourseStatusDetail"))
        );
    }

    @Test
    @DisplayName("Caso compuesto - Validación completa de múltiples escenarios")
    void testCourseStatusDetail_ValidacionCompleta_Compuesto() {
        assertAll("Validación completa de todas las propiedades",
                () -> assertEquals("CSD001", courseStatusDetail.getId()),
                () -> assertEquals(course, courseStatusDetail.getCourse()),
                () -> assertEquals("STU001", courseStatusDetail.getStudentId()),
                () -> assertEquals(CourseStatus.PASSED, courseStatusDetail.getStatus()),
                () -> assertEquals(4.5, courseStatusDetail.getGrade()),
                () -> assertEquals("2025-1", courseStatusDetail.getSemester()),
                () -> assertEquals(enrollmentDate, courseStatusDetail.getEnrollmentDate()),
                () -> assertEquals(completionDate, courseStatusDetail.getCompletionDate()),
                () -> assertEquals(group, courseStatusDetail.getGroup()),
                () -> assertEquals(professor, courseStatusDetail.getProfessor()),
                () -> assertEquals(4, courseStatusDetail.getCreditsEarned()),
                () -> assertTrue(courseStatusDetail.getIsApproved()),
                () -> assertEquals("Excelente desempeño en el curso", courseStatusDetail.getComments()),
                () -> assertNotNull(courseStatusDetail.toString()),
                () -> assertTrue(courseStatusDetail.toString().contains("CourseStatusDetail"))
        );
    }

    @Test
    @DisplayName("Caso borde - CourseStatus con todos los valores posibles")
    void testCourseStatusTodosLosValores_Borde() {
        courseStatusDetail.setStatus(CourseStatus.ENROLLED);
        assertEquals(CourseStatus.ENROLLED, courseStatusDetail.getStatus());

        courseStatusDetail.setStatus(CourseStatus.IN_PROGRESS);
        assertEquals(CourseStatus.IN_PROGRESS, courseStatusDetail.getStatus());

        courseStatusDetail.setStatus(CourseStatus.PASSED);
        assertEquals(CourseStatus.PASSED, courseStatusDetail.getStatus());

        courseStatusDetail.setStatus(CourseStatus.FAILED);
        assertEquals(CourseStatus.FAILED, courseStatusDetail.getStatus());

        courseStatusDetail.setStatus(CourseStatus.WITHDRAWN);
        assertEquals(CourseStatus.WITHDRAWN, courseStatusDetail.getStatus());

        courseStatusDetail.setStatus(CourseStatus.INCOMPLETE);
        assertEquals(CourseStatus.INCOMPLETE, courseStatusDetail.getStatus());
    }

    @Test
    @DisplayName("Caso borde - Semesters con diferentes formatos")
    void testSemesterFormatos_Borde() {
        courseStatusDetail.setSemester("2025-1");
        assertEquals("2025-1", courseStatusDetail.getSemester());

        courseStatusDetail.setSemester("2024-2");
        assertEquals("2024-2", courseStatusDetail.getSemester());

        courseStatusDetail.setSemester("VERANO-2025");
        assertEquals("VERANO-2025", courseStatusDetail.getSemester());
    }

    @Test
    @DisplayName("Caso borde - StudentId con diferentes formatos")
    void testStudentIdFormatos_Borde() {
        courseStatusDetail.setStudentId("202512345");
        assertEquals("202512345", courseStatusDetail.getStudentId());

        courseStatusDetail.setStudentId("STU-001");
        assertEquals("STU-001", courseStatusDetail.getStudentId());

        courseStatusDetail.setStudentId("12345");
        assertEquals("12345", courseStatusDetail.getStudentId());
    }

    @Test
    @DisplayName("Caso borde - Comentarios largos")
    void testComentariosLargos_Borde() {
        String comentarioLargo = "Este es un comentario muy extenso que describe el desempeño del estudiante ".repeat(10);
        courseStatusDetail.setComments(comentarioLargo);
        assertEquals(comentarioLargo, courseStatusDetail.getComments());

        courseStatusDetail.setComments("");
        assertEquals("", courseStatusDetail.getComments());

        courseStatusDetail.setComments(null);
        assertNull(courseStatusDetail.getComments());
    }
}
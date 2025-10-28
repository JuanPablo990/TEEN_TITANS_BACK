package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.CourseStatus;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.RoomType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StudentAcademicProgressTest {

    private StudentAcademicProgress progress;
    private Student student;
    private CourseStatusDetail courseStatus1;
    private CourseStatusDetail courseStatus2;
    private Course course;
    private Professor professor;
    private Schedule schedule;
    private Classroom classroom;
    private Group group;

    @BeforeEach
    void setUp() {
        student = new Student(
                "STU001",
                "Juan Pérez",
                "juan.perez@titans.edu",
                "password123",
                "Ingeniería de Sistemas",
                5
        );

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

        Date enrollmentDate = new Date(System.currentTimeMillis() - (60L * 24 * 60 * 60 * 1000));
        Date completionDate = new Date(System.currentTimeMillis() - (10L * 24 * 60 * 60 * 1000));

        courseStatus1 = new CourseStatusDetail(
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
                "Excelente desempeño"
        );

        courseStatus2 = new CourseStatusDetail(
                "CSD002",
                course,
                "STU001",
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

        progress = new StudentAcademicProgress(
                "PROG001",
                student,
                "Ingeniería de Sistemas",
                "Facultad de Ingeniería",
                "Pregrado",
                5,
                10,
                75,
                160,
                4.2,
                Arrays.asList(courseStatus1, courseStatus2)
        );
    }

    @Test
    @DisplayName("Caso exitoso - Constructor con todos los parámetros inicializa correctamente")
    void testConstructorCompleto_Exitoso() {
        assertAll("Validar todos los campos del constructor completo",
                () -> assertEquals("PROG001", progress.getId()),
                () -> assertEquals(student, progress.getStudent()),
                () -> assertEquals("Ingeniería de Sistemas", progress.getAcademicProgram()),
                () -> assertEquals("Facultad de Ingeniería", progress.getFaculty()),
                () -> assertEquals("Pregrado", progress.getCurriculumType()),
                () -> assertEquals(5, progress.getCurrentSemester()),
                () -> assertEquals(10, progress.getTotalSemesters()),
                () -> assertEquals(75, progress.getCompletedCredits()),
                () -> assertEquals(160, progress.getTotalCreditsRequired()),
                () -> assertEquals(4.2, progress.getCumulativeGPA()),
                () -> assertEquals(2, progress.getCoursesStatus().size()),
                () -> assertEquals(courseStatus1, progress.getCoursesStatus().get(0)),
                () -> assertEquals(courseStatus2, progress.getCoursesStatus().get(1))
        );
    }

    @Test
    @DisplayName("Caso exitoso - Constructor sin ID para generación automática")
    void testConstructorSinID_Exitoso() {
        StudentAcademicProgress progressSinId = new StudentAcademicProgress(
                student,
                "Medicina",
                "Facultad de Medicina",
                "Pregrado",
                3,
                12,
                45,
                200,
                4.8,
                Arrays.asList(courseStatus1)
        );

        assertAll("Validar constructor sin ID",
                () -> assertNull(progressSinId.getId()),
                () -> assertEquals(student, progressSinId.getStudent()),
                () -> assertEquals("Medicina", progressSinId.getAcademicProgram()),
                () -> assertEquals("Facultad de Medicina", progressSinId.getFaculty()),
                () -> assertEquals("Pregrado", progressSinId.getCurriculumType()),
                () -> assertEquals(3, progressSinId.getCurrentSemester()),
                () -> assertEquals(12, progressSinId.getTotalSemesters()),
                () -> assertEquals(45, progressSinId.getCompletedCredits()),
                () -> assertEquals(200, progressSinId.getTotalCreditsRequired()),
                () -> assertEquals(4.8, progressSinId.getCumulativeGPA()),
                () -> assertEquals(1, progressSinId.getCoursesStatus().size())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Constructor por defecto inicializa con valores por defecto")
    void testConstructorPorDefecto_Exitoso() {
        StudentAcademicProgress progressVacio = new StudentAcademicProgress();

        assertAll("Validar valores por defecto",
                () -> assertNull(progressVacio.getId()),
                () -> assertNull(progressVacio.getStudent()),
                () -> assertNull(progressVacio.getAcademicProgram()),
                () -> assertNull(progressVacio.getFaculty()),
                () -> assertNull(progressVacio.getCurriculumType()),
                () -> assertNull(progressVacio.getCurrentSemester()),
                () -> assertNull(progressVacio.getTotalSemesters()),
                () -> assertNull(progressVacio.getCompletedCredits()),
                () -> assertNull(progressVacio.getTotalCreditsRequired()),
                () -> assertNull(progressVacio.getCumulativeGPA()),
                () -> assertNull(progressVacio.getCoursesStatus())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Setters y Getters funcionan correctamente")
    void testSettersAndGetters_Exitoso() {
        StudentAcademicProgress progressTest = new StudentAcademicProgress();
        Student newStudent = new Student("STU002", "María García", "maria@titans.edu", "pass", "Medicina", 3);
        List<CourseStatusDetail> newCoursesStatus = Arrays.asList(courseStatus1);

        progressTest.setId("PROG002");
        progressTest.setStudent(newStudent);
        progressTest.setAcademicProgram("Arquitectura");
        progressTest.setFaculty("Facultad de Arquitectura");
        progressTest.setCurriculumType("Pregrado");
        progressTest.setCurrentSemester(4);
        progressTest.setTotalSemesters(10);
        progressTest.setCompletedCredits(60);
        progressTest.setTotalCreditsRequired(180);
        progressTest.setCumulativeGPA(3.9);
        progressTest.setCoursesStatus(newCoursesStatus);

        assertAll("Validar setters y getters",
                () -> assertEquals("PROG002", progressTest.getId()),
                () -> assertEquals(newStudent, progressTest.getStudent()),
                () -> assertEquals("Arquitectura", progressTest.getAcademicProgram()),
                () -> assertEquals("Facultad de Arquitectura", progressTest.getFaculty()),
                () -> assertEquals("Pregrado", progressTest.getCurriculumType()),
                () -> assertEquals(4, progressTest.getCurrentSemester()),
                () -> assertEquals(10, progressTest.getTotalSemesters()),
                () -> assertEquals(60, progressTest.getCompletedCredits()),
                () -> assertEquals(180, progressTest.getTotalCreditsRequired()),
                () -> assertEquals(3.9, progressTest.getCumulativeGPA()),
                () -> assertEquals(1, progressTest.getCoursesStatus().size())
        );
    }

    @Test
    @DisplayName("Caso borde - Campos con valores null")
    void testCamposConValoresNull_Borde() {
        StudentAcademicProgress progressNull = new StudentAcademicProgress(
                null, null, null, null, null, null, null, null, null, null, null
        );

        assertAll("Validar campos null",
                () -> assertNull(progressNull.getId()),
                () -> assertNull(progressNull.getStudent()),
                () -> assertNull(progressNull.getAcademicProgram()),
                () -> assertNull(progressNull.getFaculty()),
                () -> assertNull(progressNull.getCurriculumType()),
                () -> assertNull(progressNull.getCurrentSemester()),
                () -> assertNull(progressNull.getTotalSemesters()),
                () -> assertNull(progressNull.getCompletedCredits()),
                () -> assertNull(progressNull.getTotalCreditsRequired()),
                () -> assertNull(progressNull.getCumulativeGPA()),
                () -> assertNull(progressNull.getCoursesStatus())
        );
    }

    @Test
    @DisplayName("Caso borde - Campos con valores vacíos")
    void testCamposConValoresVacios_Borde() {
        progress.setId("");
        progress.setAcademicProgram("");
        progress.setFaculty("");
        progress.setCurriculumType("");

        assertAll("Validar campos vacíos",
                () -> assertEquals("", progress.getId()),
                () -> assertEquals("", progress.getAcademicProgram()),
                () -> assertEquals("", progress.getFaculty()),
                () -> assertEquals("", progress.getCurriculumType())
        );
    }

    @Test
    @DisplayName("Caso borde - Campos numéricos con valores extremos")
    void testCamposNumericosValoresExtremos_Borde() {
        progress.setCurrentSemester(0);
        assertEquals(0, progress.getCurrentSemester());

        progress.setCurrentSemester(20);
        assertEquals(20, progress.getCurrentSemester());

        progress.setTotalSemesters(0);
        assertEquals(0, progress.getTotalSemesters());

        progress.setCompletedCredits(-10);
        assertEquals(-10, progress.getCompletedCredits());

        progress.setTotalCreditsRequired(500);
        assertEquals(500, progress.getTotalCreditsRequired());

        progress.setCumulativeGPA(-1.0);
        assertEquals(-1.0, progress.getCumulativeGPA());

        progress.setCumulativeGPA(10.0);
        assertEquals(10.0, progress.getCumulativeGPA());
    }

    @Test
    @DisplayName("Caso borde - Lista de cursos vacía")
    void testListaCursosVacia_Borde() {
        progress.setCoursesStatus(Arrays.asList());

        assertAll("Validar lista vacía",
                () -> assertNotNull(progress.getCoursesStatus()),
                () -> assertTrue(progress.getCoursesStatus().isEmpty())
        );
    }

    @Test
    @DisplayName("Caso borde - Lista de cursos null")
    void testListaCursosNull_Borde() {
        progress.setCoursesStatus(null);

        assertNull(progress.getCoursesStatus());
    }

    @Test
    @DisplayName("Caso exitoso - Equals y HashCode generados por Lombok")
    void testEqualsAndHashCode_Exitoso() {
        StudentAcademicProgress progress1 = new StudentAcademicProgress(
                "PROG001",
                student,
                "Ingeniería",
                "Facultad",
                "Pregrado",
                5,
                10,
                75,
                160,
                4.2,
                Arrays.asList(courseStatus1)
        );

        StudentAcademicProgress progress2 = new StudentAcademicProgress(
                "PROG001",
                student,
                "Ingeniería",
                "Facultad",
                "Pregrado",
                5,
                10,
                75,
                160,
                4.2,
                Arrays.asList(courseStatus1)
        );

        StudentAcademicProgress progress3 = new StudentAcademicProgress(
                "PROG002",
                student,
                "Medicina",
                "Facultad Medicina",
                "Pregrado",
                3,
                12,
                45,
                200,
                4.8,
                Arrays.asList(courseStatus2)
        );

        assertEquals(progress1, progress2);
        assertNotEquals(progress1, progress3);
        assertEquals(progress1.hashCode(), progress2.hashCode());
        assertNotEquals(progress1.hashCode(), progress3.hashCode());
    }

    @Test
    @DisplayName("Caso borde - Equals con objetos null y diferente clase")
    void testEqualsCasosBorde_Borde() {
        StudentAcademicProgress progressTest = new StudentAcademicProgress(
                "PROG001",
                student,
                "Ingeniería",
                "Facultad",
                "Pregrado",
                5,
                10,
                75,
                160,
                4.2,
                Arrays.asList(courseStatus1)
        );

        assertNotEquals(null, progressTest);
        assertNotEquals("No soy un StudentAcademicProgress", progressTest);
    }


    @Test
    @DisplayName("Caso compuesto - Validación completa de múltiples escenarios")
    void testStudentAcademicProgress_ValidacionCompleta_Compuesto() {
        assertAll("Validación completa de todas las propiedades",
                () -> assertEquals("PROG001", progress.getId()),
                () -> assertEquals(student, progress.getStudent()),
                () -> assertEquals("Ingeniería de Sistemas", progress.getAcademicProgram()),
                () -> assertEquals("Facultad de Ingeniería", progress.getFaculty()),
                () -> assertEquals("Pregrado", progress.getCurriculumType()),
                () -> assertEquals(5, progress.getCurrentSemester()),
                () -> assertEquals(10, progress.getTotalSemesters()),
                () -> assertEquals(75, progress.getCompletedCredits()),
                () -> assertEquals(160, progress.getTotalCreditsRequired()),
                () -> assertEquals(4.2, progress.getCumulativeGPA()),
                () -> assertEquals(2, progress.getCoursesStatus().size()),
                () -> assertNotNull(progress.toString()),
                () -> assertTrue(progress.toString().contains("StudentAcademicProgress"))
        );
    }

    @Test
    @DisplayName("Caso borde - IDs con diferentes formatos")
    void testIDsFormatos_Borde() {
        progress.setId("2025-PROG-001");
        assertEquals("2025-PROG-001", progress.getId());

        progress.setId("PROGRESO_ACADEMICO_1");
        assertEquals("PROGRESO_ACADEMICO_1", progress.getId());

        progress.setId("123456789");
        assertEquals("123456789", progress.getId());
    }

    @Test
    @DisplayName("Caso borde - Programas académicos con diferentes formatos")
    void testAcademicProgramFormatos_Borde() {
        progress.setAcademicProgram("Ing. de Sistemas");
        assertEquals("Ing. de Sistemas", progress.getAcademicProgram());

        progress.setAcademicProgram("MEDICINA - CIRUGÍA");
        assertEquals("MEDICINA - CIRUGÍA", progress.getAcademicProgram());

        progress.setAcademicProgram("Administración de Empresas");
        assertEquals("Administración de Empresas", progress.getAcademicProgram());
    }

    @Test
    @DisplayName("Caso borde - Facultades con diferentes formatos")
    void testFacultyFormatos_Borde() {
        progress.setFaculty("Facultad de Ingeniería y Ciencias");
        assertEquals("Facultad de Ingeniería y Ciencias", progress.getFaculty());

        progress.setFaculty("FACULTAD DE MEDICINA");
        assertEquals("FACULTAD DE MEDICINA", progress.getFaculty());

        progress.setFaculty("Facultad de Ciencias Humanas");
        assertEquals("Facultad de Ciencias Humanas", progress.getFaculty());
    }

    @Test
    @DisplayName("Caso borde - Tipos de currículo")
    void testCurriculumTypeValores_Borde() {
        progress.setCurriculumType("Pregrado");
        assertEquals("Pregrado", progress.getCurriculumType());

        progress.setCurriculumType("Posgrado");
        assertEquals("Posgrado", progress.getCurriculumType());

        progress.setCurriculumType("Técnico");
        assertEquals("Técnico", progress.getCurriculumType());

        progress.setCurriculumType("Especialización");
        assertEquals("Especialización", progress.getCurriculumType());
    }

    @Test
    @DisplayName("Caso borde - GPA en límites")
    void testGPALimites_Borde() {
        progress.setCumulativeGPA(0.0);
        assertEquals(0.0, progress.getCumulativeGPA());

        progress.setCumulativeGPA(5.0);
        assertEquals(5.0, progress.getCumulativeGPA());

        progress.setCumulativeGPA(3.75);
        assertEquals(3.75, progress.getCumulativeGPA());
    }

    @Test
    @DisplayName("Caso borde - Relación con CourseStatusDetail")
    void testRelacionConCourseStatusDetail_Borde() {
        List<CourseStatusDetail> cursos = progress.getCoursesStatus();

        assertAll("Validar relación con CourseStatusDetail",
                () -> assertEquals(2, cursos.size()),
                () -> assertEquals("CSD001", cursos.get(0).getId()),
                () -> assertEquals("CSD002", cursos.get(1).getId()),
                () -> assertEquals(CourseStatus.PASSED, cursos.get(0).getStatus()),
                () -> assertEquals(CourseStatus.IN_PROGRESS, cursos.get(1).getStatus()),
                () -> assertTrue(cursos.get(0).getIsApproved()),
                () -> assertFalse(cursos.get(1).getIsApproved())
        );
    }
}
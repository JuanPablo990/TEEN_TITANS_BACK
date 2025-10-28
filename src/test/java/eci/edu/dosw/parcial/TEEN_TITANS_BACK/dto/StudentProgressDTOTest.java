package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.CourseStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StudentProgressDTOTest {

    private StudentProgressDTO dto;
    private CourseStatusDetailDTO course1;
    private CourseStatusDetailDTO course2;
    private CourseStatusDetailDTO course3;

    @BeforeEach
    void setUp() {
        course1 = new CourseStatusDetailDTO(
                "CS001", "COURSE001", "MATH101", "Cálculo I", "STU001",
                CourseStatus.PASSED, 4.5, "2025-1", new Date(), new Date(),
                "GRP001", "A", "PROF001", "Dr. Smith", 3, true, "Excelente"
        );

        course2 = new CourseStatusDetailDTO(
                "CS002", "COURSE002", "PHY101", "Física I", "STU001",
                CourseStatus.IN_PROGRESS, null, "2025-1", new Date(), null,
                "GRP002", "B", "PROF002", "Dr. Johnson", 4, false, "En curso"
        );

        course3 = new CourseStatusDetailDTO(
                "CS003", "COURSE003", "CHEM101", "Química I", "STU001",
                CourseStatus.FAILED, 2.8, "2024-2", new Date(), new Date(),
                "GRP003", "C", "PROF003", "Dr. Brown", 3, false, "Reprobado"
        );

        dto = new StudentProgressDTO(
                "PROG001", "STU001", "Juan Pérez", "Ingeniería de Sistemas",
                "Facultad de Ingeniería", "Pregrado", 5, 10, 75, 160, 4.2,
                Arrays.asList(course1, course2, course3), 46.875, 85, 5
        );
    }

    @Test
    @DisplayName("Caso exitoso - Constructor con parámetros inicializa todos los campos")
    void testConstructorConParametros_Exitoso() {
        assertAll("Validar todos los campos del constructor",
                () -> assertEquals("PROG001", dto.getId()),
                () -> assertEquals("STU001", dto.getStudentId()),
                () -> assertEquals("Juan Pérez", dto.getStudentName()),
                () -> assertEquals("Ingeniería de Sistemas", dto.getAcademicProgram()),
                () -> assertEquals("Facultad de Ingeniería", dto.getFaculty()),
                () -> assertEquals("Pregrado", dto.getCurriculumType()),
                () -> assertEquals(5, dto.getCurrentSemester()),
                () -> assertEquals(10, dto.getTotalSemesters()),
                () -> assertEquals(75, dto.getCompletedCredits()),
                () -> assertEquals(160, dto.getTotalCreditsRequired()),
                () -> assertEquals(4.2, dto.getCumulativeGPA()),
                () -> assertEquals(3, dto.getCoursesStatus().size()),
                () -> assertEquals(46.875, dto.getProgressPercentage()),
                () -> assertEquals(85, dto.getRemainingCredits()),
                () -> assertEquals(5, dto.getRemainingSemesters())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Setters y Getters funcionan correctamente")
    void testSettersAndGetters_Exitoso() {
        dto.setId("PROG002");
        dto.setStudentId("STU002");
        dto.setStudentName("María García");
        dto.setAcademicProgram("Medicina");
        dto.setFaculty("Facultad de Medicina");
        dto.setCurriculumType("Pregrado");
        dto.setCurrentSemester(3);
        dto.setTotalSemesters(12);
        dto.setCompletedCredits(45);
        dto.setTotalCreditsRequired(200);
        dto.setCumulativeGPA(4.8);
        dto.setCoursesStatus(Arrays.asList(course1));
        dto.setProgressPercentage(22.5);
        dto.setRemainingCredits(155);
        dto.setRemainingSemesters(9);

        assertAll("Validar setters y getters",
                () -> assertEquals("PROG002", dto.getId()),
                () -> assertEquals("STU002", dto.getStudentId()),
                () -> assertEquals("María García", dto.getStudentName()),
                () -> assertEquals("Medicina", dto.getAcademicProgram()),
                () -> assertEquals("Facultad de Medicina", dto.getFaculty()),
                () -> assertEquals("Pregrado", dto.getCurriculumType()),
                () -> assertEquals(3, dto.getCurrentSemester()),
                () -> assertEquals(12, dto.getTotalSemesters()),
                () -> assertEquals(45, dto.getCompletedCredits()),
                () -> assertEquals(200, dto.getTotalCreditsRequired()),
                () -> assertEquals(4.8, dto.getCumulativeGPA()),
                () -> assertEquals(1, dto.getCoursesStatus().size()),
                () -> assertEquals(22.5, dto.getProgressPercentage()),
                () -> assertEquals(155, dto.getRemainingCredits()),
                () -> assertEquals(9, dto.getRemainingSemesters())
        );
    }

    @Test
    @DisplayName("Caso borde - Constructor por defecto inicializa con null")
    void testConstructorPorDefecto_Borde() {
        StudentProgressDTO dtoVacio = new StudentProgressDTO();

        assertAll("Campos deben ser null o valores por defecto",
                () -> assertNull(dtoVacio.getId()),
                () -> assertNull(dtoVacio.getStudentId()),
                () -> assertNull(dtoVacio.getStudentName()),
                () -> assertNull(dtoVacio.getAcademicProgram()),
                () -> assertNull(dtoVacio.getFaculty()),
                () -> assertNull(dtoVacio.getCurriculumType()),
                () -> assertNull(dtoVacio.getCurrentSemester()),
                () -> assertNull(dtoVacio.getTotalSemesters()),
                () -> assertNull(dtoVacio.getCompletedCredits()),
                () -> assertNull(dtoVacio.getTotalCreditsRequired()),
                () -> assertNull(dtoVacio.getCumulativeGPA()),
                () -> assertNull(dtoVacio.getCoursesStatus()),
                () -> assertNull(dtoVacio.getProgressPercentage()),
                () -> assertNull(dtoVacio.getRemainingCredits()),
                () -> assertNull(dtoVacio.getRemainingSemesters())
        );
    }

    @Test
    @DisplayName("Caso borde - Campos calculados con valores cero")
    void testCamposCalculadosConValoresCero_Borde() {
        StudentProgressDTO dtoCero = new StudentProgressDTO(
                "PROG003", "STU003", "Carlos López", "Arquitectura",
                "Facultad de Arquitectura", "Pregrado", 0, 0, 0, 0, 0.0,
                Collections.emptyList(), 0.0, 0, 0
        );

        assertAll("Validar campos con valores cero",
                () -> assertEquals(0.0, dtoCero.getProgressPercentage()),
                () -> assertEquals(0, dtoCero.getRemainingCredits()),
                () -> assertEquals(0, dtoCero.getRemainingSemesters())
        );
    }

    @Test
    @DisplayName("Caso borde - Lista de cursos vacía")
    void testListaCursosVacia_Borde() {
        dto.setCoursesStatus(Collections.emptyList());

        assertAll("Validar lista vacía",
                () -> assertNotNull(dto.getCoursesStatus()),
                () -> assertTrue(dto.getCoursesStatus().isEmpty())
        );
    }

    @Test
    @DisplayName("Caso borde - Lista de cursos null")
    void testListaCursosNull_Borde() {
        dto.setCoursesStatus(null);

        assertNull(dto.getCoursesStatus());
    }

    @Test
    @DisplayName("Caso borde - Campos numéricos con valores negativos")
    void testCamposNumericosNegativos_Borde() {
        dto.setCurrentSemester(-1);
        dto.setTotalSemesters(-5);
        dto.setCompletedCredits(-10);
        dto.setTotalCreditsRequired(-100);
        dto.setCumulativeGPA(-1.5);
        dto.setProgressPercentage(-25.0);
        dto.setRemainingCredits(-50);
        dto.setRemainingSemesters(-3);

        assertAll("Validar campos numéricos negativos",
                () -> assertEquals(-1, dto.getCurrentSemester()),
                () -> assertEquals(-5, dto.getTotalSemesters()),
                () -> assertEquals(-10, dto.getCompletedCredits()),
                () -> assertEquals(-100, dto.getTotalCreditsRequired()),
                () -> assertEquals(-1.5, dto.getCumulativeGPA()),
                () -> assertEquals(-25.0, dto.getProgressPercentage()),
                () -> assertEquals(-50, dto.getRemainingCredits()),
                () -> assertEquals(-3, dto.getRemainingSemesters())
        );
    }

    @Test
    @DisplayName("Caso borde - GPA en límites (0.0 y 5.0)")
    void testGPALimites_Borde() {
        dto.setCumulativeGPA(0.0);
        assertEquals(0.0, dto.getCumulativeGPA());

        dto.setCumulativeGPA(5.0);
        assertEquals(5.0, dto.getCumulativeGPA());
    }

    @Test
    @DisplayName("Caso borde - Porcentaje de progreso en límites (0% y 100%)")
    void testProgressPercentageLimites_Borde() {
        dto.setProgressPercentage(0.0);
        assertEquals(0.0, dto.getProgressPercentage());

        dto.setProgressPercentage(100.0);
        assertEquals(100.0, dto.getProgressPercentage());
    }

    @Test
    @DisplayName("Caso exitoso - Equals y HashCode generados por Lombok")
    void testEqualsAndHashCode_Exitoso() {
        StudentProgressDTO dto1 = new StudentProgressDTO(
                "PROG001", "STU001", "Juan Pérez", "Ingeniería", "Facultad", "Pregrado",
                5, 10, 75, 160, 4.2, Arrays.asList(course1), 46.875, 85, 5
        );

        StudentProgressDTO dto2 = new StudentProgressDTO(
                "PROG001", "STU001", "Juan Pérez", "Ingeniería", "Facultad", "Pregrado",
                5, 10, 75, 160, 4.2, Arrays.asList(course1), 46.875, 85, 5
        );

        StudentProgressDTO dto3 = new StudentProgressDTO(
                "PROG002", "STU002", "María García", "Medicina", "Facultad", "Pregrado",
                3, 12, 45, 200, 4.8, Arrays.asList(course2), 22.5, 155, 9
        );

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    @DisplayName("Caso borde - Equals con objetos null y diferente clase")
    void testEqualsCasosBorde_Borde() {
        StudentProgressDTO dto1 = new StudentProgressDTO(
                "PROG001", "STU001", "Juan Pérez", "Ingeniería", "Facultad", "Pregrado",
                5, 10, 75, 160, 4.2, Arrays.asList(course1), 46.875, 85, 5
        );

        assertNotEquals(null, dto1);
        assertNotEquals("No soy un StudentProgressDTO", dto1);
    }

    @Test
    @DisplayName("Caso exitoso - ToString generado por Lombok")
    void testToString_Exitoso() {
        String resultadoToString = dto.toString();

        assertAll("ToString debe contener todos los campos principales",
                () -> assertTrue(resultadoToString.contains("PROG001")),
                () -> assertTrue(resultadoToString.contains("STU001")),
                () -> assertTrue(resultadoToString.contains("Juan Pérez")),
                () -> assertTrue(resultadoToString.contains("Ingeniería de Sistemas")),
                () -> assertTrue(resultadoToString.contains("Facultad de Ingeniería"))
        );
    }

    @Test
    @DisplayName("Caso compuesto - Validación completa de múltiples escenarios")
    void testStudentProgressDTO_ValidacionCompleta_Compuesto() {
        assertAll("Validación completa de todas las propiedades",
                () -> assertEquals("PROG001", dto.getId()),
                () -> assertEquals("STU001", dto.getStudentId()),
                () -> assertEquals("Juan Pérez", dto.getStudentName()),
                () -> assertEquals("Ingeniería de Sistemas", dto.getAcademicProgram()),
                () -> assertEquals("Facultad de Ingeniería", dto.getFaculty()),
                () -> assertEquals("Pregrado", dto.getCurriculumType()),
                () -> assertEquals(5, dto.getCurrentSemester()),
                () -> assertEquals(10, dto.getTotalSemesters()),
                () -> assertEquals(75, dto.getCompletedCredits()),
                () -> assertEquals(160, dto.getTotalCreditsRequired()),
                () -> assertEquals(4.2, dto.getCumulativeGPA()),
                () -> assertEquals(3, dto.getCoursesStatus().size()),
                () -> assertEquals(46.875, dto.getProgressPercentage()),
                () -> assertEquals(85, dto.getRemainingCredits()),
                () -> assertEquals(5, dto.getRemainingSemesters()),
                () -> assertNotNull(dto.toString()),
                () -> assertTrue(dto.toString().contains("StudentProgressDTO"))
        );
    }

    @Test
    @DisplayName("Caso borde - Campos de texto con valores extremos")
    void testCamposTextoValoresExtremos_Borde() {
        dto.setStudentName("");
        dto.setAcademicProgram("   ");
        dto.setFaculty(null);
        dto.setCurriculumType("MuyLargo".repeat(50));

        assertAll("Validar campos de texto con valores extremos",
                () -> assertEquals("", dto.getStudentName()),
                () -> assertEquals("   ", dto.getAcademicProgram()),
                () -> assertNull(dto.getFaculty()),
                () -> assertTrue(dto.getCurriculumType().length() > 100)
        );
    }

    @Test
    @DisplayName("Caso borde - Relación con CourseStatusDetailDTO")
    void testRelacionConCourseStatusDetailDTO_Borde() {
        List<CourseStatusDetailDTO> cursos = dto.getCoursesStatus();

        assertAll("Validar relación con cursos",
                () -> assertEquals(3, cursos.size()),
                () -> assertEquals("MATH101", cursos.get(0).getCourseCode()),
                () -> assertEquals("PHY101", cursos.get(1).getCourseCode()),
                () -> assertEquals("CHEM101", cursos.get(2).getCourseCode()),
                () -> assertTrue(cursos.get(0).getIsApproved()),
                () -> assertFalse(cursos.get(1).getIsApproved()),
                () -> assertFalse(cursos.get(2).getIsApproved())
        );
    }
}
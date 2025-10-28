package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.CourseStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class CourseStatusDetailDTOTest {

    private CourseStatusDetailDTO courseStatusDetailDTO;
    private Date currentDate;

    @BeforeEach
    void setUp() {
        currentDate = new Date();

        courseStatusDetailDTO = new CourseStatusDetailDTO();
        courseStatusDetailDTO.setId("STATUS_001");
        courseStatusDetailDTO.setCourseId("COURSE_001");
        courseStatusDetailDTO.setCourseCode("MATH101");
        courseStatusDetailDTO.setCourseName("Cálculo I");
        courseStatusDetailDTO.setStudentId("STU_001");
        courseStatusDetailDTO.setStatus(CourseStatus.ENROLLED);
        courseStatusDetailDTO.setGrade(4.5);
        courseStatusDetailDTO.setSemester("2025-1");
        courseStatusDetailDTO.setEnrollmentDate(currentDate);
        courseStatusDetailDTO.setCompletionDate(new Date(currentDate.getTime() + (1000L * 60 * 60 * 24 * 120)));
        courseStatusDetailDTO.setGroupId("GROUP_001");
        courseStatusDetailDTO.setGroupCode("G1");
        courseStatusDetailDTO.setProfessorId("PROF_001");
        courseStatusDetailDTO.setProfessorName("Dr. García");
        courseStatusDetailDTO.setCreditsEarned(4);
        courseStatusDetailDTO.setIsApproved(true);
        courseStatusDetailDTO.setComments("Excelente desempeño");
    }

    @Test
    @DisplayName("Caso exitoso - Creación de CourseStatusDetailDTO con constructor completo")
    void testCourseStatusDetailDTOConstructorCompleto_Exitoso() {
        CourseStatusDetailDTO dto = new CourseStatusDetailDTO(
                "STATUS_002", "COURSE_002", "PHYS201", "Física II", "STU_002",
                CourseStatus.PASSED, 3.8, "2025-1", currentDate,
                new Date(currentDate.getTime() + (1000L * 60 * 60 * 24 * 90)),
                "GROUP_002", "G2", "PROF_002", "Dr. Martínez", 3, false, "Buen desempeño"
        );

        assertAll("Verificación de constructor completo",
                () -> assertEquals("STATUS_002", dto.getId()),
                () -> assertEquals("COURSE_002", dto.getCourseId()),
                () -> assertEquals("PHYS201", dto.getCourseCode()),
                () -> assertEquals("Física II", dto.getCourseName()),
                () -> assertEquals("STU_002", dto.getStudentId()),
                () -> assertEquals(CourseStatus.PASSED, dto.getStatus()),
                () -> assertEquals(3.8, dto.getGrade()),
                () -> assertEquals("2025-1", dto.getSemester()),
                () -> assertEquals(currentDate, dto.getEnrollmentDate()),
                () -> assertNotNull(dto.getCompletionDate()),
                () -> assertEquals("GROUP_002", dto.getGroupId()),
                () -> assertEquals("G2", dto.getGroupCode()),
                () -> assertEquals("PROF_002", dto.getProfessorId()),
                () -> assertEquals("Dr. Martínez", dto.getProfessorName()),
                () -> assertEquals(3, dto.getCreditsEarned()),
                () -> assertFalse(dto.getIsApproved()),
                () -> assertEquals("Buen desempeño", dto.getComments())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Getters y setters funcionan correctamente")
    void testGettersAndSetters_Exitoso() {
        assertAll("Verificación de getters y setters",
                () -> assertEquals("STATUS_001", courseStatusDetailDTO.getId()),
                () -> assertEquals("COURSE_001", courseStatusDetailDTO.getCourseId()),
                () -> assertEquals("MATH101", courseStatusDetailDTO.getCourseCode()),
                () -> assertEquals("Cálculo I", courseStatusDetailDTO.getCourseName()),
                () -> assertEquals("STU_001", courseStatusDetailDTO.getStudentId()),
                () -> assertEquals(CourseStatus.ENROLLED, courseStatusDetailDTO.getStatus()),
                () -> assertEquals(4.5, courseStatusDetailDTO.getGrade()),
                () -> assertEquals("2025-1", courseStatusDetailDTO.getSemester()),
                () -> assertEquals(currentDate, courseStatusDetailDTO.getEnrollmentDate()),
                () -> assertNotNull(courseStatusDetailDTO.getCompletionDate()),
                () -> assertEquals("GROUP_001", courseStatusDetailDTO.getGroupId()),
                () -> assertEquals("G1", courseStatusDetailDTO.getGroupCode()),
                () -> assertEquals("PROF_001", courseStatusDetailDTO.getProfessorId()),
                () -> assertEquals("Dr. García", courseStatusDetailDTO.getProfessorName()),
                () -> assertEquals(4, courseStatusDetailDTO.getCreditsEarned()),
                () -> assertTrue(courseStatusDetailDTO.getIsApproved()),
                () -> assertEquals("Excelente desempeño", courseStatusDetailDTO.getComments())
        );
    }

    @Test
    @DisplayName("Caso borde - Constructor sin argumentos crea instancia")
    void testNoArgsConstructor_Exitoso() {
        CourseStatusDetailDTO emptyDTO = new CourseStatusDetailDTO();
        assertNotNull(emptyDTO);
    }

    @Test
    @DisplayName("Caso borde - equals retorna true para DTOs idénticos")
    void testEquals_Exitoso() {
        CourseStatusDetailDTO dto1 = new CourseStatusDetailDTO(
                "STATUS_001", "COURSE_001", "MATH101", "Cálculo I", "STU_001",
                CourseStatus.ENROLLED, 4.5, "2025-1", currentDate,
                new Date(currentDate.getTime() + (1000L * 60 * 60 * 24 * 120)),
                "GROUP_001", "G1", "PROF_001", "Dr. García", 4, true, "Excelente desempeño"
        );

        CourseStatusDetailDTO dto2 = new CourseStatusDetailDTO(
                "STATUS_001", "COURSE_001", "MATH101", "Cálculo I", "STU_001",
                CourseStatus.ENROLLED, 4.5, "2025-1", currentDate,
                new Date(currentDate.getTime() + (1000L * 60 * 60 * 24 * 120)),
                "GROUP_001", "G1", "PROF_001", "Dr. García", 4, true, "Excelente desempeño"
        );

        assertEquals(dto1, dto2);
    }

    @Test
    @DisplayName("Caso error - equals retorna false para DTOs diferentes")
    void testEquals_Diferentes() {
        CourseStatusDetailDTO dto1 = new CourseStatusDetailDTO(
                "STATUS_001", "COURSE_001", "MATH101", "Cálculo I", "STU_001",
                CourseStatus.ENROLLED, 4.5, "2025-1", currentDate, null,
                "GROUP_001", "G1", "PROF_001", "Dr. García", 4, true, "Excelente desempeño"
        );

        CourseStatusDetailDTO dto2 = new CourseStatusDetailDTO(
                "STATUS_002", "COURSE_002", "PHYS201", "Física II", "STU_002",
                CourseStatus.PASSED, 3.8, "2025-2", currentDate, currentDate,
                "GROUP_002", "G2", "PROF_002", "Dr. Martínez", 3, false, "Buen desempeño"
        );

        assertNotEquals(dto1, dto2);
    }

    @Test
    @DisplayName("Caso borde - hashCode consistente para DTOs iguales")
    void testHashCode_Exitoso() {
        CourseStatusDetailDTO dto1 = new CourseStatusDetailDTO(
                "STATUS_001", "COURSE_001", "MATH101", "Cálculo I", "STU_001",
                CourseStatus.ENROLLED, 4.5, "2025-1", currentDate, null,
                "GROUP_001", "G1", "PROF_001", "Dr. García", 4, true, "Excelente desempeño"
        );

        CourseStatusDetailDTO dto2 = new CourseStatusDetailDTO(
                "STATUS_001", "COURSE_001", "MATH101", "Cálculo I", "STU_001",
                CourseStatus.ENROLLED, 4.5, "2025-1", currentDate, null,
                "GROUP_001", "G1", "PROF_001", "Dr. García", 4, true, "Excelente desempeño"
        );

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    @DisplayName("Caso borde - toString no retorna null")
    void testToString_NoNull() {
        String toStringResult = courseStatusDetailDTO.toString();
        assertNotNull(toStringResult);
        assertFalse(toStringResult.isEmpty());
    }

    @Test
    @DisplayName("Caso borde - Campos null manejados correctamente")
    void testCamposNull_Exitoso() {
        CourseStatusDetailDTO dto = new CourseStatusDetailDTO();
        dto.setId(null);
        dto.setCourseId(null);
        dto.setCourseCode(null);
        dto.setCourseName(null);
        dto.setStudentId(null);
        dto.setStatus(null);
        dto.setGrade(null);
        dto.setSemester(null);
        dto.setEnrollmentDate(null);
        dto.setCompletionDate(null);
        dto.setGroupId(null);
        dto.setGroupCode(null);
        dto.setProfessorId(null);
        dto.setProfessorName(null);
        dto.setCreditsEarned(null);
        dto.setIsApproved(null);
        dto.setComments(null);

        assertAll("Verificación de campos null",
                () -> assertNull(dto.getId()),
                () -> assertNull(dto.getCourseId()),
                () -> assertNull(dto.getCourseCode()),
                () -> assertNull(dto.getCourseName()),
                () -> assertNull(dto.getStudentId()),
                () -> assertNull(dto.getStatus()),
                () -> assertNull(dto.getGrade()),
                () -> assertNull(dto.getSemester()),
                () -> assertNull(dto.getEnrollmentDate()),
                () -> assertNull(dto.getCompletionDate()),
                () -> assertNull(dto.getGroupId()),
                () -> assertNull(dto.getGroupCode()),
                () -> assertNull(dto.getProfessorId()),
                () -> assertNull(dto.getProfessorName()),
                () -> assertNull(dto.getCreditsEarned()),
                () -> assertNull(dto.getIsApproved()),
                () -> assertNull(dto.getComments())
        );
    }

    @Test
    @DisplayName("Caso borde - Grade con valores decimales")
    void testGradeValoresDecimales_Exitoso() {
        courseStatusDetailDTO.setGrade(0.0);
        assertEquals(0.0, courseStatusDetailDTO.getGrade());

        courseStatusDetailDTO.setGrade(5.0);
        assertEquals(5.0, courseStatusDetailDTO.getGrade());

        courseStatusDetailDTO.setGrade(2.75);
        assertEquals(2.75, courseStatusDetailDTO.getGrade());

        courseStatusDetailDTO.setGrade(4.999);
        assertEquals(4.999, courseStatusDetailDTO.getGrade());
    }

    @Test
    @DisplayName("Caso borde - Grade negativo")
    void testGradeNegativo_Exitoso() {
        courseStatusDetailDTO.setGrade(-1.5);
        assertEquals(-1.5, courseStatusDetailDTO.getGrade());
    }

    @Test
    @DisplayName("Caso borde - CreditsEarned con valores extremos")
    void testCreditsEarnedValoresExtremos_Exitoso() {
        courseStatusDetailDTO.setCreditsEarned(0);
        assertEquals(0, courseStatusDetailDTO.getCreditsEarned());

        courseStatusDetailDTO.setCreditsEarned(10);
        assertEquals(10, courseStatusDetailDTO.getCreditsEarned());

        courseStatusDetailDTO.setCreditsEarned(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, courseStatusDetailDTO.getCreditsEarned());

        courseStatusDetailDTO.setCreditsEarned(-5);
        assertEquals(-5, courseStatusDetailDTO.getCreditsEarned());
    }

    @Test
    @DisplayName("Caso borde - Semester con diferentes formatos")
    void testSemesterFormatosDiferentes_Exitoso() {
        courseStatusDetailDTO.setSemester("2025-1");
        assertEquals("2025-1", courseStatusDetailDTO.getSemester());

        courseStatusDetailDTO.setSemester("2024-2");
        assertEquals("2024-2", courseStatusDetailDTO.getSemester());

        courseStatusDetailDTO.setSemester("2023-A");
        assertEquals("2023-A", courseStatusDetailDTO.getSemester());

        courseStatusDetailDTO.setSemester("Primer Semestre 2025");
        assertEquals("Primer Semestre 2025", courseStatusDetailDTO.getSemester());
    }

    @Test
    @DisplayName("Caso borde - IsApproved con diferentes valores booleanos")
    void testIsApprovedValoresBooleanos_Exitoso() {
        courseStatusDetailDTO.setIsApproved(true);
        assertTrue(courseStatusDetailDTO.getIsApproved());

        courseStatusDetailDTO.setIsApproved(false);
        assertFalse(courseStatusDetailDTO.getIsApproved());

        courseStatusDetailDTO.setIsApproved(null);
        assertNull(courseStatusDetailDTO.getIsApproved());
    }

    @Test
    @DisplayName("Caso borde - Comments con textos largos")
    void testCommentsTextoLargo_Exitoso() {
        String comentarioLargo = "El estudiante demostró un excelente desempeño durante todo el semestre. " +
                "Su participación en clase fue activa y sus trabajos fueron entregados " +
                "oportunamente con alta calidad. Se recomienda para cursos avanzados.";
        courseStatusDetailDTO.setComments(comentarioLargo);
        assertEquals(comentarioLargo, courseStatusDetailDTO.getComments());

        courseStatusDetailDTO.setComments("");
        assertEquals("", courseStatusDetailDTO.getComments());

        courseStatusDetailDTO.setComments("   ");
        assertEquals("   ", courseStatusDetailDTO.getComments());
    }

    @Test
    @DisplayName("Caso borde - Fechas con valores null")
    void testFechasNull_Exitoso() {
        courseStatusDetailDTO.setEnrollmentDate(null);
        courseStatusDetailDTO.setCompletionDate(null);

        assertNull(courseStatusDetailDTO.getEnrollmentDate());
        assertNull(courseStatusDetailDTO.getCompletionDate());
    }

    @Test
    @DisplayName("Caso exitoso - Todos los CourseStatus funcionan correctamente")
    void testAllCourseStatus_Exitoso() {
        CourseStatusDetailDTO dto = new CourseStatusDetailDTO();

        dto.setStatus(CourseStatus.ENROLLED);
        assertEquals(CourseStatus.ENROLLED, dto.getStatus());

        dto.setStatus(CourseStatus.IN_PROGRESS);
        assertEquals(CourseStatus.IN_PROGRESS, dto.getStatus());

        dto.setStatus(CourseStatus.PASSED);
        assertEquals(CourseStatus.PASSED, dto.getStatus());

        dto.setStatus(CourseStatus.FAILED);
        assertEquals(CourseStatus.FAILED, dto.getStatus());

        dto.setStatus(CourseStatus.WITHDRAWN);
        assertEquals(CourseStatus.WITHDRAWN, dto.getStatus());

        dto.setStatus(CourseStatus.INCOMPLETE);
        assertEquals(CourseStatus.INCOMPLETE, dto.getStatus());
    }

    @Test
    @DisplayName("Caso borde - CourseCode con formato especial")
    void testCourseCodeFormatoEspecial_Exitoso() {
        courseStatusDetailDTO.setCourseCode("CS-101-A");
        assertEquals("CS-101-A", courseStatusDetailDTO.getCourseCode());

        courseStatusDetailDTO.setCourseCode("MATH_202_B");
        assertEquals("MATH_202_B", courseStatusDetailDTO.getCourseCode());
    }

    @Test
    @DisplayName("Caso borde - GroupCode con diferentes formatos")
    void testGroupCodeFormatosDiferentes_Exitoso() {
        courseStatusDetailDTO.setGroupCode("G1");
        assertEquals("G1", courseStatusDetailDTO.getGroupCode());

        courseStatusDetailDTO.setGroupCode("GRUPO-A");
        assertEquals("GRUPO-A", courseStatusDetailDTO.getGroupCode());

        courseStatusDetailDTO.setGroupCode("2025-1-MATH101-01");
        assertEquals("2025-1-MATH101-01", courseStatusDetailDTO.getGroupCode());
    }

    @Test
    @DisplayName("Caso borde - ProfessorName con nombres compuestos")
    void testProfessorNameNombresCompuestos_Exitoso() {
        courseStatusDetailDTO.setProfessorName("Dr. Juan Carlos García López");
        assertEquals("Dr. Juan Carlos García López", courseStatusDetailDTO.getProfessorName());

        courseStatusDetailDTO.setProfessorName("Prof. María Elena Rodríguez");
        assertEquals("Prof. María Elena Rodríguez", courseStatusDetailDTO.getProfessorName());
    }

    @Test
    @DisplayName("Caso borde - StudentId con diferentes formatos")
    void testStudentIdFormatosDiferentes_Exitoso() {
        courseStatusDetailDTO.setStudentId("STU_001");
        assertEquals("STU_001", courseStatusDetailDTO.getStudentId());

        courseStatusDetailDTO.setStudentId("202510001");
        assertEquals("202510001", courseStatusDetailDTO.getStudentId());

        courseStatusDetailDTO.setStudentId("EST-2025-001");
        assertEquals("EST-2025-001", courseStatusDetailDTO.getStudentId());
    }
}
package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StudentPortalDTOTest {

    @Test
    @DisplayName("Caso exitoso - StudentProgressDTO constructor y getters funcionan correctamente")
    void testStudentProgressDTO_ConstructorYGetters_Exitoso() {
        StudentPortalDTO.CourseProgressDTO curso = new StudentPortalDTO.CourseProgressDTO(
                "COURSE001", "MATH101", "Cálculo I", "APPROVED", 4.5, "2025-1", 3, true, "Excelente"
        );

        StudentPortalDTO.StudentProgressDTO dto = new StudentPortalDTO.StudentProgressDTO(
                "PROG001", "STU001", "Juan Pérez", "juan@titans.edu",
                "Ingeniería de Sistemas", "Facultad de Ingeniería", "Pregrado",
                5, 10, 75, 160, 4.2, Arrays.asList(curso)
        );

        assertAll("StudentProgressDTO - Validar todos los campos",
                () -> assertEquals("PROG001", dto.getId()),
                () -> assertEquals("STU001", dto.getStudentId()),
                () -> assertEquals("Juan Pérez", dto.getStudentName()),
                () -> assertEquals("juan@titans.edu", dto.getStudentEmail()),
                () -> assertEquals("Ingeniería de Sistemas", dto.getAcademicProgram()),
                () -> assertEquals("Facultad de Ingeniería", dto.getFaculty()),
                () -> assertEquals("Pregrado", dto.getCurriculumType()),
                () -> assertEquals(5, dto.getCurrentSemester()),
                () -> assertEquals(10, dto.getTotalSemesters()),
                () -> assertEquals(75, dto.getCompletedCredits()),
                () -> assertEquals(160, dto.getTotalCreditsRequired()),
                () -> assertEquals(4.2, dto.getCumulativeGPA()),
                () -> assertEquals(1, dto.getCoursesStatus().size()),
                () -> assertEquals("MATH101", dto.getCoursesStatus().get(0).getCourseCode())
        );
    }

    @Test
    @DisplayName("Caso borde - StudentProgressDTO con lista de cursos vacía")
    void testStudentProgressDTO_ListaCursosVacia_Borde() {
        StudentPortalDTO.StudentProgressDTO dto = new StudentPortalDTO.StudentProgressDTO(
                "PROG002", "STU002", "María García", null, null, null, null,
                null, null, null, null, null, Collections.emptyList()
        );

        assertAll("StudentProgressDTO - Campos null y lista vacía",
                () -> assertNull(dto.getStudentEmail()),
                () -> assertNull(dto.getAcademicProgram()),
                () -> assertNull(dto.getFaculty()),
                () -> assertNull(dto.getCurriculumType()),
                () -> assertNull(dto.getCurrentSemester()),
                () -> assertNull(dto.getTotalSemesters()),
                () -> assertNull(dto.getCompletedCredits()),
                () -> assertNull(dto.getTotalCreditsRequired()),
                () -> assertNull(dto.getCumulativeGPA()),
                () -> assertNotNull(dto.getCoursesStatus()),
                () -> assertTrue(dto.getCoursesStatus().isEmpty())
        );
    }

    @Test
    @DisplayName("Caso exitoso - CourseProgressDTO inicialización completa")
    void testCourseProgressDTO_ConstructorCompleto_Exitoso() {
        StudentPortalDTO.CourseProgressDTO dto = new StudentPortalDTO.CourseProgressDTO(
                "COURSE001", "PHY101", "Física I", "IN_PROGRESS", null, "2025-1", 4, false, "En curso"
        );

        assertAll("CourseProgressDTO - Validar campos",
                () -> assertEquals("COURSE001", dto.getCourseId()),
                () -> assertEquals("PHY101", dto.getCourseCode()),
                () -> assertEquals("Física I", dto.getCourseName()),
                () -> assertEquals("IN_PROGRESS", dto.getStatus()),
                () -> assertNull(dto.getGrade()),
                () -> assertEquals("2025-1", dto.getSemester()),
                () -> assertEquals(4, dto.getCreditsEarned()),
                () -> assertFalse(dto.getApproved()),
                () -> assertEquals("En curso", dto.getComments())
        );
    }

    @Test
    @DisplayName("Caso borde - CourseProgressDTO con valores booleanos extremos")
    void testCourseProgressDTO_ValoresBooleanos_Borde() {
        StudentPortalDTO.CourseProgressDTO dto1 = new StudentPortalDTO.CourseProgressDTO(
                "C001", "CODE1", "Curso 1", "APPROVED", 5.0, "2025-1", 3, true, "Aprobado"
        );

        StudentPortalDTO.CourseProgressDTO dto2 = new StudentPortalDTO.CourseProgressDTO(
                "C002", "CODE2", "Curso 2", "FAILED", 2.5, "2025-1", 0, false, "Reprobado"
        );

        assertAll("CourseProgressDTO - Validar booleanos",
                () -> assertTrue(dto1.getApproved()),
                () -> assertFalse(dto2.getApproved())
        );
    }

    @Test
    @DisplayName("Caso exitoso - AcademicAlertDTO con alertas múltiples")
    void testAcademicAlertDTO_AlertasMultiples_Exitoso() {
        List<String> alertas = Arrays.asList(
                "Bajo rendimiento en Matemáticas",
                "Faltan 3 créditos para próximo semestre",
                "GPA por debajo del mínimo requerido"
        );

        StudentPortalDTO.AcademicAlertDTO dto = new StudentPortalDTO.AcademicAlertDTO(
                "STU003", alertas, 3
        );

        assertAll("AcademicAlertDTO - Validar alertas",
                () -> assertEquals("STU003", dto.getStudentId()),
                () -> assertEquals(3, dto.getAlerts().size()),
                () -> assertEquals(3, dto.getAlertCount()),
                () -> assertEquals("Bajo rendimiento en Matemáticas", dto.getAlerts().get(0)),
                () -> assertEquals("GPA por debajo del mínimo requerido", dto.getAlerts().get(2))
        );
    }

    @Test
    @DisplayName("Caso error - AcademicAlertDTO sin alertas")
    void testAcademicAlertDTO_SinAlertas_Error() {
        StudentPortalDTO.AcademicAlertDTO dto = new StudentPortalDTO.AcademicAlertDTO(
                "STU004", Collections.emptyList(), 0
        );

        assertAll("AcademicAlertDTO - Sin alertas",
                () -> assertEquals("STU004", dto.getStudentId()),
                () -> assertTrue(dto.getAlerts().isEmpty()),
                () -> assertEquals(0, dto.getAlertCount())
        );
    }

    @Test
    @DisplayName("Caso exitoso - GroupCapacityDTO con capacidad disponible")
    void testGroupCapacityDTO_CapacidadDisponible_Exitoso() {
        StudentPortalDTO.GroupCapacityDTO dto = new StudentPortalDTO.GroupCapacityDTO(
                "GRP001", 30, 25, 5, true, 83.33
        );

        assertAll("GroupCapacityDTO - Con capacidad disponible",
                () -> assertEquals("GRP001", dto.getGroupId()),
                () -> assertEquals(30, dto.getMaxCapacity()),
                () -> assertEquals(25, dto.getCurrentEnrollment()),
                () -> assertEquals(5, dto.getAvailableSpots()),
                () -> assertTrue(dto.getIsAvailable()),
                () -> assertEquals(83.33, dto.getOccupancyRate())
        );
    }

    @Test
    @DisplayName("Caso error - GroupCapacityDTO sin cupos disponibles")
    void testGroupCapacityDTO_SinCupos_Error() {
        StudentPortalDTO.GroupCapacityDTO dto = new StudentPortalDTO.GroupCapacityDTO(
                "GRP002", 25, 25, 0, false, 100.0
        );

        assertAll("GroupCapacityDTO - Sin cupos disponibles",
                () -> assertEquals("GRP002", dto.getGroupId()),
                () -> assertEquals(0, dto.getAvailableSpots()),
                () -> assertFalse(dto.getIsAvailable()),
                () -> assertEquals(100.0, dto.getOccupancyRate())
        );
    }

    @Test
    @DisplayName("Caso exitoso - EnrollmentEligibilityDTO estudiante elegible")
    void testEnrollmentEligibilityDTO_Elegible_Exitoso() {
        StudentPortalDTO.EnrollmentEligibilityDTO dto = new StudentPortalDTO.EnrollmentEligibilityDTO(
                "STU005", "MATH101", true, false, false, true, 3
        );

        assertAll("EnrollmentEligibilityDTO - Estudiante elegible",
                () -> assertEquals("STU005", dto.getStudentId()),
                () -> assertEquals("MATH101", dto.getCourseCode()),
                () -> assertTrue(dto.getEligible()),
                () -> assertFalse(dto.getAlreadyApproved()),
                () -> assertFalse(dto.getCurrentlyEnrolled()),
                () -> assertTrue(dto.getHasAvailableGroups()),
                () -> assertEquals(3, dto.getAvailableGroupsCount())
        );
    }

    @Test
    @DisplayName("Caso error - EnrollmentEligibilityDTO no elegible")
    void testEnrollmentEligibilityDTO_NoElegible_Error() {
        StudentPortalDTO.EnrollmentEligibilityDTO dto = new StudentPortalDTO.EnrollmentEligibilityDTO(
                "STU006", "PHY101", false, true, false, false, 0
        );

        assertAll("EnrollmentEligibilityDTO - No elegible",
                () -> assertEquals("STU006", dto.getStudentId()),
                () -> assertEquals("PHY101", dto.getCourseCode()),
                () -> assertFalse(dto.getEligible()),
                () -> assertTrue(dto.getAlreadyApproved()),
                () -> assertFalse(dto.getCurrentlyEnrolled()),
                () -> assertFalse(dto.getHasAvailableGroups()),
                () -> assertEquals(0, dto.getAvailableGroupsCount())
        );
    }

    @Test
    @DisplayName("Caso exitoso - AcademicSummaryDTO con progreso avanzado")
    void testAcademicSummaryDTO_ProgresoAvanzado_Exitoso() {
        StudentPortalDTO.AcademicSummaryDTO dto = new StudentPortalDTO.AcademicSummaryDTO(
                "STU007", "Ana López", "Medicina", 8, 4.5, 180, 200, 90.0, 2L, 25L
        );

        assertAll("AcademicSummaryDTO - Progreso avanzado",
                () -> assertEquals("STU007", dto.getStudentId()),
                () -> assertEquals("Ana López", dto.getStudentName()),
                () -> assertEquals("Medicina", dto.getAcademicProgram()),
                () -> assertEquals(8, dto.getCurrentSemester()),
                () -> assertEquals(4.5, dto.getCumulativeGPA()),
                () -> assertEquals(180, dto.getCompletedCredits()),
                () -> assertEquals(200, dto.getTotalCreditsRequired()),
                () -> assertEquals(90.0, dto.getProgressPercentage()),
                () -> assertEquals(2L, dto.getCoursesInProgress()),
                () -> assertEquals(25L, dto.getCoursesCompleted())
        );
    }

    @Test
    @DisplayName("Caso exitoso - CourseHistoryDTO curso aprobado")
    void testCourseHistoryDTO_CursoAprobado_Exitoso() {
        Date enrollmentDate = new Date(System.currentTimeMillis() - (200L * 24 * 60 * 60 * 1000));
        Date completionDate = new Date(System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000));

        StudentPortalDTO.CourseHistoryDTO dto = new StudentPortalDTO.CourseHistoryDTO(
                "CHEM101", "Química I", 3, "APPROVED", 4.0, "2024-2", true, enrollmentDate, completionDate
        );

        assertAll("CourseHistoryDTO - Curso aprobado",
                () -> assertEquals("CHEM101", dto.getCourseCode()),
                () -> assertEquals("Química I", dto.getCourseName()),
                () -> assertEquals(3, dto.getCredits()),
                () -> assertEquals("APPROVED", dto.getStatus()),
                () -> assertEquals(4.0, dto.getGrade()),
                () -> assertEquals("2024-2", dto.getSemester()),
                () -> assertTrue(dto.getApproved()),
                () -> assertNotNull(dto.getEnrollmentDate()),
                () -> assertNotNull(dto.getCompletionDate())
        );
    }

    @Test
    @DisplayName("Caso error - CourseHistoryDTO curso reprobado")
    void testCourseHistoryDTO_CursoReprobado_Error() {
        StudentPortalDTO.CourseHistoryDTO dto = new StudentPortalDTO.CourseHistoryDTO(
                "MATH201", "Cálculo II", 4, "FAILED", 2.8, "2025-1", false, new Date(), null
        );

        assertAll("CourseHistoryDTO - Curso reprobado",
                () -> assertEquals("MATH201", dto.getCourseCode()),
                () -> assertEquals("Cálculo II", dto.getCourseName()),
                () -> assertEquals(4, dto.getCredits()),
                () -> assertEquals("FAILED", dto.getStatus()),
                () -> assertEquals(2.8, dto.getGrade()),
                () -> assertEquals("2025-1", dto.getSemester()),
                () -> assertFalse(dto.getApproved()),
                () -> assertNotNull(dto.getEnrollmentDate()),
                () -> assertNull(dto.getCompletionDate())
        );
    }

    @Test
    @DisplayName("Caso exitoso - GroupAvailabilityRequest inicialización")
    void testGroupAvailabilityRequest_Exitoso() {
        StudentPortalDTO.GroupAvailabilityRequest dto = new StudentPortalDTO.GroupAvailabilityRequest("GRP005");

        assertEquals("GRP005", dto.getGroupId());
    }

    @Test
    @DisplayName("Caso exitoso - CourseEnrollmentRequest inicialización")
    void testCourseEnrollmentRequest_Exitoso() {
        StudentPortalDTO.CourseEnrollmentRequest dto = new StudentPortalDTO.CourseEnrollmentRequest("BIO101");

        assertEquals("BIO101", dto.getCourseCode());
    }

    @Test
    @DisplayName("Caso compuesto - Relación entre StudentProgressDTO y CourseProgressDTO")
    void testRelacionStudentProgressYCourseProgress_Compuesto() {
        StudentPortalDTO.CourseProgressDTO curso1 = new StudentPortalDTO.CourseProgressDTO(
                "C001", "CODE1", "Curso 1", "APPROVED", 4.5, "2025-1", 3, true, "Aprobado"
        );

        StudentPortalDTO.CourseProgressDTO curso2 = new StudentPortalDTO.CourseProgressDTO(
                "C002", "CODE2", "Curso 2", "IN_PROGRESS", null, "2025-1", 4, false, "En curso"
        );

        StudentPortalDTO.StudentProgressDTO studentProgress = new StudentPortalDTO.StudentProgressDTO(
                "PROG003", "STU008", "Carlos Ruiz", "carlos@titans.edu",
                "Ingeniería Civil", "Facultad de Ingeniería", "Pregrado",
                3, 10, 45, 160, 3.8, Arrays.asList(curso1, curso2)
        );

        assertAll("Relación entre DTOs",
                () -> assertEquals(2, studentProgress.getCoursesStatus().size()),
                () -> assertEquals("CODE1", studentProgress.getCoursesStatus().get(0).getCourseCode()),
                () -> assertEquals("CODE2", studentProgress.getCoursesStatus().get(1).getCourseCode()),
                () -> assertTrue(studentProgress.getCoursesStatus().get(0).getApproved()),
                () -> assertFalse(studentProgress.getCoursesStatus().get(1).getApproved())
        );
    }

    @Test
    @DisplayName("Caso borde - DTOs con valores null en todos los campos")
    void testDTOsConValoresNull_Borde() {
        StudentPortalDTO.StudentProgressDTO studentProgress = new StudentPortalDTO.StudentProgressDTO(
                null, null, null, null, null, null, null, null, null, null, null, null, null
        );

        StudentPortalDTO.CourseProgressDTO courseProgress = new StudentPortalDTO.CourseProgressDTO(
                null, null, null, null, null, null, null, null, null
        );

        assertAll("DTOs con valores null",
                () -> assertNull(studentProgress.getId()),
                () -> assertNull(studentProgress.getStudentId()),
                () -> assertNull(studentProgress.getStudentName()),
                () -> assertNull(studentProgress.getCoursesStatus()),
                () -> assertNull(courseProgress.getCourseId()),
                () -> assertNull(courseProgress.getCourseCode()),
                () -> assertNull(courseProgress.getCourseName()),
                () -> assertNull(courseProgress.getStatus()),
                () -> assertNull(courseProgress.getGrade()),
                () -> assertNull(courseProgress.getSemester()),
                () -> assertNull(courseProgress.getCreditsEarned()),
                () -> assertNull(courseProgress.getApproved()),
                () -> assertNull(courseProgress.getComments())
        );
    }
}
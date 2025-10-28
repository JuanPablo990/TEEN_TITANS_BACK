package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GroupDTOTest {

    private GroupDTO groupDTO;

    @BeforeEach
    void setUp() {
        groupDTO = new GroupDTO();
        groupDTO.setGroupId("GROUP_001");
        groupDTO.setSection("A");
        groupDTO.setCourseId("COURSE_001");
        groupDTO.setProfessorId("PROF_001");
        groupDTO.setScheduleId("SCHEDULE_001");
        groupDTO.setClassroomId("CLASSROOM_001");
    }

    @Test
    @DisplayName("Caso exitoso - Creación de GroupDTO con constructor completo")
    void testGroupDTOConstructorCompleto_Exitoso() {
        GroupDTO dto = new GroupDTO("GROUP_002", "B", "COURSE_002", "PROF_002", "SCHEDULE_002", "CLASSROOM_002");

        assertAll("Verificación de constructor completo",
                () -> assertEquals("GROUP_002", dto.getGroupId()),
                () -> assertEquals("B", dto.getSection()),
                () -> assertEquals("COURSE_002", dto.getCourseId()),
                () -> assertEquals("PROF_002", dto.getProfessorId()),
                () -> assertEquals("SCHEDULE_002", dto.getScheduleId()),
                () -> assertEquals("CLASSROOM_002", dto.getClassroomId())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Getters y setters funcionan correctamente")
    void testGettersAndSetters_Exitoso() {
        assertAll("Verificación de getters y setters",
                () -> assertEquals("GROUP_001", groupDTO.getGroupId()),
                () -> assertEquals("A", groupDTO.getSection()),
                () -> assertEquals("COURSE_001", groupDTO.getCourseId()),
                () -> assertEquals("PROF_001", groupDTO.getProfessorId()),
                () -> assertEquals("SCHEDULE_001", groupDTO.getScheduleId()),
                () -> assertEquals("CLASSROOM_001", groupDTO.getClassroomId())
        );
    }

    @Test
    @DisplayName("Caso borde - Constructor sin argumentos crea instancia")
    void testNoArgsConstructor_Exitoso() {
        GroupDTO emptyDTO = new GroupDTO();
        assertNotNull(emptyDTO);
    }

    @Test
    @DisplayName("Caso borde - equals retorna true para DTOs idénticos")
    void testEquals_Exitoso() {
        GroupDTO dto1 = new GroupDTO("GROUP_001", "A", "COURSE_001", "PROF_001", "SCHEDULE_001", "CLASSROOM_001");
        GroupDTO dto2 = new GroupDTO("GROUP_001", "A", "COURSE_001", "PROF_001", "SCHEDULE_001", "CLASSROOM_001");

        assertEquals(dto1, dto2);
    }

    @Test
    @DisplayName("Caso error - equals retorna false para DTOs diferentes")
    void testEquals_Diferentes() {
        GroupDTO dto1 = new GroupDTO("GROUP_001", "A", "COURSE_001", "PROF_001", "SCHEDULE_001", "CLASSROOM_001");
        GroupDTO dto2 = new GroupDTO("GROUP_002", "B", "COURSE_002", "PROF_002", "SCHEDULE_002", "CLASSROOM_002");

        assertNotEquals(dto1, dto2);
    }

    @Test
    @DisplayName("Caso borde - hashCode consistente para DTOs iguales")
    void testHashCode_Exitoso() {
        GroupDTO dto1 = new GroupDTO("GROUP_001", "A", "COURSE_001", "PROF_001", "SCHEDULE_001", "CLASSROOM_001");
        GroupDTO dto2 = new GroupDTO("GROUP_001", "A", "COURSE_001", "PROF_001", "SCHEDULE_001", "CLASSROOM_001");

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    @DisplayName("Caso borde - toString no retorna null")
    void testToString_NoNull() {
        String toStringResult = groupDTO.toString();
        assertNotNull(toStringResult);
        assertFalse(toStringResult.isEmpty());
    }

    @Test
    @DisplayName("Caso borde - Campos null manejados correctamente")
    void testCamposNull_Exitoso() {
        GroupDTO dto = new GroupDTO();
        dto.setGroupId(null);
        dto.setSection(null);
        dto.setCourseId(null);
        dto.setProfessorId(null);
        dto.setScheduleId(null);
        dto.setClassroomId(null);

        assertAll("Verificación de campos null",
                () -> assertNull(dto.getGroupId()),
                () -> assertNull(dto.getSection()),
                () -> assertNull(dto.getCourseId()),
                () -> assertNull(dto.getProfessorId()),
                () -> assertNull(dto.getScheduleId()),
                () -> assertNull(dto.getClassroomId())
        );
    }

    @Test
    @DisplayName("Caso borde - Section con diferentes formatos")
    void testSectionFormatosDiferentes_Exitoso() {
        groupDTO.setSection("A");
        assertEquals("A", groupDTO.getSection());

        groupDTO.setSection("B1");
        assertEquals("B1", groupDTO.getSection());

        groupDTO.setSection("GRUPO-01");
        assertEquals("GRUPO-01", groupDTO.getSection());

        groupDTO.setSection("SECCION-A");
        assertEquals("SECCION-A", groupDTO.getSection());
    }

    @Test
    @DisplayName("Caso borde - Section vacía")
    void testSectionVacia_Exitoso() {
        groupDTO.setSection("");
        assertEquals("", groupDTO.getSection());

        groupDTO.setSection("   ");
        assertEquals("   ", groupDTO.getSection());
    }

    @Test
    @DisplayName("Caso borde - IDs con diferentes formatos")
    void testIdsFormatosDiferentes_Exitoso() {
        groupDTO.setGroupId("GRP-2025-001");
        assertEquals("GRP-2025-001", groupDTO.getGroupId());

        groupDTO.setCourseId("CURSO-MATH-101");
        assertEquals("CURSO-MATH-101", groupDTO.getCourseId());

        groupDTO.setProfessorId("PROF-ENG-001");
        assertEquals("PROF-ENG-001", groupDTO.getProfessorId());

        groupDTO.setScheduleId("SCH-MON-WED-FRI");
        assertEquals("SCH-MON-WED-FRI", groupDTO.getScheduleId());

        groupDTO.setClassroomId("CLASS-A-101");
        assertEquals("CLASS-A-101", groupDTO.getClassroomId());
    }

    @Test
    @DisplayName("Caso borde - IDs vacíos")
    void testIdsVacios_Exitoso() {
        groupDTO.setGroupId("");
        assertEquals("", groupDTO.getGroupId());

        groupDTO.setCourseId("");
        assertEquals("", groupDTO.getCourseId());

        groupDTO.setProfessorId("");
        assertEquals("", groupDTO.getProfessorId());

        groupDTO.setScheduleId("");
        assertEquals("", groupDTO.getScheduleId());

        groupDTO.setClassroomId("");
        assertEquals("", groupDTO.getClassroomId());
    }

    @Test
    @DisplayName("Caso borde - IDs con valores numéricos")
    void testIdsValoresNumericos_Exitoso() {
        groupDTO.setGroupId("001");
        assertEquals("001", groupDTO.getGroupId());

        groupDTO.setCourseId("101");
        assertEquals("101", groupDTO.getCourseId());

        groupDTO.setProfessorId("001");
        assertEquals("001", groupDTO.getProfessorId());

        groupDTO.setScheduleId("001");
        assertEquals("001", groupDTO.getScheduleId());

        groupDTO.setClassroomId("101");
        assertEquals("101", groupDTO.getClassroomId());
    }

    @Test
    @DisplayName("Caso borde - IDs con caracteres especiales")
    void testIdsCaracteresEspeciales_Exitoso() {
        groupDTO.setGroupId("GRP_001-A");
        assertEquals("GRP_001-A", groupDTO.getGroupId());

        groupDTO.setCourseId("COURSE-101-B");
        assertEquals("COURSE-101-B", groupDTO.getCourseId());

        groupDTO.setProfessorId("PROF-001-C");
        assertEquals("PROF-001-C", groupDTO.getProfessorId());
    }

    @Test
    @DisplayName("Caso borde - Constructor con algunos campos null")
    void testConstructorConCamposNull_Exitoso() {
        GroupDTO dto = new GroupDTO(null, "A", null, "PROF_001", null, "CLASSROOM_001");

        assertAll("Verificación de constructor con campos null",
                () -> assertNull(dto.getGroupId()),
                () -> assertEquals("A", dto.getSection()),
                () -> assertNull(dto.getCourseId()),
                () -> assertEquals("PROF_001", dto.getProfessorId()),
                () -> assertNull(dto.getScheduleId()),
                () -> assertEquals("CLASSROOM_001", dto.getClassroomId())
        );
    }
}
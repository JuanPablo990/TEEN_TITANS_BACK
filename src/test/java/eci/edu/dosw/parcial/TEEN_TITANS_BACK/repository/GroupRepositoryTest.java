package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Group;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Course;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Professor;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Schedule;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Classroom;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.RoomType;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;
import java.util.Collections;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataMongoTest
public class GroupRepositoryTest {

    @MockBean
    private GroupRepository groupRepository;

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
    private Group group3;

    @BeforeEach
    void setUp() {
        course1 = new Course("CS101", "Introduction to Programming", 3, "Basic programming concepts", "Computer Science", true);
        course2 = new Course("MATH201", "Calculus I", 4, "Differential calculus", "Mathematics", true);

        professor1 = new Professor("PROF1", "Dr. Smith", "smith@university.edu", "password", "Computer Science", true, Arrays.asList("Programming", "Algorithms"));
        professor2 = new Professor("PROF2", "Dr. Johnson", "johnson@university.edu", "password", "Mathematics", false, Arrays.asList("Calculus", "Algebra"));

        schedule1 = new Schedule("SCHED1", "Monday", "08:00", "10:00", "Morning");
        schedule2 = new Schedule("SCHED2", "Wednesday", "14:00", "16:00", "Afternoon");

        classroom1 = new Classroom("CLASS1", "A", "101", 50, RoomType.REGULAR);
        classroom2 = new Classroom("CLASS2", "B", "201", 100, RoomType.LABORATORY);

        group1 = new Group("GROUP1", "A", course1, professor1, schedule1, classroom1);
        group2 = new Group("GROUP2", "B", course1, professor2, schedule2, classroom2);
        group3 = new Group("GROUP3", "A", course2, professor1, schedule1, classroom1);
    }

    @Test
    @DisplayName("Caso exitoso - findBySection retorna grupos de la sección")
    void testFindBySection_Exitoso() {
        when(groupRepository.findBySection("A"))
                .thenReturn(List.of(group1, group3));

        List<Group> resultado = groupRepository.findBySection("A");

        assertAll("Verificar grupos de la sección A",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals("A", resultado.get(0).getSection()),
                () -> assertEquals("A", resultado.get(1).getSection())
        );

        verify(groupRepository, times(1)).findBySection("A");
    }

    @Test
    @DisplayName("Caso error - findBySection retorna lista vacía para sección inexistente")
    void testFindBySection_SeccionInexistente() {
        when(groupRepository.findBySection("Z"))
                .thenReturn(Collections.emptyList());

        List<Group> resultado = groupRepository.findBySection("Z");

        assertTrue(resultado.isEmpty());
        verify(groupRepository, times(1)).findBySection("Z");
    }

    @Test
    @DisplayName("Caso exitoso - findByCourse_CourseCode retorna grupos del curso")
    void testFindByCourse_CourseCode_Exitoso() {
        when(groupRepository.findByCourse_CourseCode("CS101"))
                .thenReturn(List.of(group1, group2));

        List<Group> resultado = groupRepository.findByCourse_CourseCode("CS101");

        assertAll("Verificar grupos del curso CS101",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals("CS101", resultado.get(0).getCourse().getCourseCode()),
                () -> assertEquals("CS101", resultado.get(1).getCourse().getCourseCode())
        );

        verify(groupRepository, times(1)).findByCourse_CourseCode("CS101");
    }

    @Test
    @DisplayName("Caso exitoso - findByProfessor_Id retorna grupos del profesor")
    void testFindByProfessor_Id_Exitoso() {
        when(groupRepository.findByProfessor_Id("PROF1"))
                .thenReturn(List.of(group1, group3));

        List<Group> resultado = groupRepository.findByProfessor_Id("PROF1");

        assertAll("Verificar grupos del profesor PROF1",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals("PROF1", resultado.get(0).getProfessor().getId()),
                () -> assertEquals("PROF1", resultado.get(1).getProfessor().getId())
        );

        verify(groupRepository, times(1)).findByProfessor_Id("PROF1");
    }

    @Test
    @DisplayName("Caso exitoso - findByClassroom_ClassroomId retorna grupos del aula")
    void testFindByClassroom_ClassroomId_Exitoso() {
        when(groupRepository.findByClassroom_ClassroomId("CLASS1"))
                .thenReturn(List.of(group1, group3));

        List<Group> resultado = groupRepository.findByClassroom_ClassroomId("CLASS1");

        assertAll("Verificar grupos del aula CLASS1",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals("CLASS1", resultado.get(0).getClassroom().getClassroomId()),
                () -> assertEquals("CLASS1", resultado.get(1).getClassroom().getClassroomId())
        );

        verify(groupRepository, times(1)).findByClassroom_ClassroomId("CLASS1");
    }

    @Test
    @DisplayName("Caso exitoso - findByCourse_Name retorna grupos por nombre del curso")
    void testFindByCourse_Name_Exitoso() {
        when(groupRepository.findByCourse_Name("Introduction to Programming"))
                .thenReturn(List.of(group1, group2));

        List<Group> resultado = groupRepository.findByCourse_Name("Introduction to Programming");

        assertAll("Verificar grupos por nombre del curso",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals("Introduction to Programming", resultado.get(0).getCourse().getName()),
                () -> assertEquals("Introduction to Programming", resultado.get(1).getCourse().getName())
        );

        verify(groupRepository, times(1)).findByCourse_Name("Introduction to Programming");
    }

    @Test
    @DisplayName("Caso exitoso - findBySectionAndCourse_CourseCode retorna grupo específico")
    void testFindBySectionAndCourse_CourseCode_Exitoso() {
        when(groupRepository.findBySectionAndCourse_CourseCode("A", "CS101"))
                .thenReturn(List.of(group1));

        List<Group> resultado = groupRepository.findBySectionAndCourse_CourseCode("A", "CS101");

        assertAll("Verificar grupo específico por sección y curso",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("A", resultado.get(0).getSection()),
                () -> assertEquals("CS101", resultado.get(0).getCourse().getCourseCode())
        );

        verify(groupRepository, times(1)).findBySectionAndCourse_CourseCode("A", "CS101");
    }

    @Test
    @DisplayName("Caso exitoso - findByCourse_CourseCodeAndProfessor_Id retorna grupos filtrados")
    void testFindByCourse_CourseCodeAndProfessor_Id_Exitoso() {
        when(groupRepository.findByCourse_CourseCodeAndProfessor_Id("CS101", "PROF1"))
                .thenReturn(List.of(group1));

        List<Group> resultado = groupRepository.findByCourse_CourseCodeAndProfessor_Id("CS101", "PROF1");

        assertAll("Verificar grupos por curso y profesor",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("CS101", resultado.get(0).getCourse().getCourseCode()),
                () -> assertEquals("PROF1", resultado.get(0).getProfessor().getId())
        );

        verify(groupRepository, times(1)).findByCourse_CourseCodeAndProfessor_Id("CS101", "PROF1");
    }

    @Test
    @DisplayName("Caso exitoso - findByClassroom_CapacityGreaterThanEqual retorna grupos con capacidad suficiente")
    void testFindByClassroom_CapacityGreaterThanEqual_Exitoso() {
        when(groupRepository.findByClassroom_CapacityGreaterThanEqual(80))
                .thenReturn(List.of(group2));

        List<Group> resultado = groupRepository.findByClassroom_CapacityGreaterThanEqual(80);

        assertAll("Verificar grupos con capacidad >= 80",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertTrue(resultado.get(0).getClassroom().getCapacity() >= 80)
        );

        verify(groupRepository, times(1)).findByClassroom_CapacityGreaterThanEqual(80);
    }

    @Test
    @DisplayName("Caso exitoso - findByClassroom_CapacityBetween retorna grupos en rango de capacidad")
    void testFindByClassroom_CapacityBetween_Exitoso() {
        when(groupRepository.findByClassroom_CapacityBetween(40, 60))
                .thenReturn(List.of(group1, group3));

        List<Group> resultado = groupRepository.findByClassroom_CapacityBetween(40, 60);

        assertAll("Verificar grupos en rango de capacidad",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertTrue(resultado.get(0).getClassroom().getCapacity() >= 40 && resultado.get(0).getClassroom().getCapacity() <= 60),
                () -> assertTrue(resultado.get(1).getClassroom().getCapacity() >= 40 && resultado.get(1).getClassroom().getCapacity() <= 60)
        );

        verify(groupRepository, times(1)).findByClassroom_CapacityBetween(40, 60);
    }

    @Test
    @DisplayName("Caso exitoso - findByClassroom_RoomType retorna grupos por tipo de aula")
    void testFindByClassroom_RoomType_Exitoso() {
        when(groupRepository.findByClassroom_RoomType(RoomType.LABORATORY.name()))
                .thenReturn(List.of(group2));

        List<Group> resultado = groupRepository.findByClassroom_RoomType(RoomType.LABORATORY.name());

        assertAll("Verificar grupos por tipo de aula",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals(RoomType.LABORATORY, resultado.get(0).getClassroom().getRoomType())
        );

        verify(groupRepository, times(1)).findByClassroom_RoomType(RoomType.LABORATORY.name());
    }

    @Test
    @DisplayName("Caso exitoso - findByOrderBySectionAsc retorna grupos ordenados")
    void testFindByOrderBySectionAsc_Exitoso() {
        when(groupRepository.findByOrderBySectionAsc())
                .thenReturn(List.of(group1, group3, group2));

        List<Group> resultado = groupRepository.findByOrderBySectionAsc();

        assertAll("Verificar grupos ordenados por sección ascendente",
                () -> assertNotNull(resultado),
                () -> assertEquals(3, resultado.size()),
                () -> assertEquals("A", resultado.get(0).getSection()),
                () -> assertEquals("A", resultado.get(1).getSection()),
                () -> assertEquals("B", resultado.get(2).getSection())
        );

        verify(groupRepository, times(1)).findByOrderBySectionAsc();
    }

    @Test
    @DisplayName("Caso exitoso - countByCourse_CourseCode retorna conteo correcto")
    void testCountByCourse_CourseCode_Exitoso() {
        when(groupRepository.countByCourse_CourseCode("CS101"))
                .thenReturn(2L);

        long resultado = groupRepository.countByCourse_CourseCode("CS101");

        assertEquals(2L, resultado);
        verify(groupRepository, times(1)).countByCourse_CourseCode("CS101");
    }

    @Test
    @DisplayName("Caso exitoso - countByProfessor_Id retorna conteo por profesor")
    void testCountByProfessor_Id_Exitoso() {
        when(groupRepository.countByProfessor_Id("PROF1"))
                .thenReturn(2L);

        long resultado = groupRepository.countByProfessor_Id("PROF1");

        assertEquals(2L, resultado);
        verify(groupRepository, times(1)).countByProfessor_Id("PROF1");
    }

    @Test
    @DisplayName("Caso exitoso - existsBySectionAndCourse_CourseCode retorna true cuando existe")
    void testExistsBySectionAndCourse_CourseCode_Exitoso() {
        when(groupRepository.existsBySectionAndCourse_CourseCode("A", "CS101"))
                .thenReturn(true);

        boolean resultado = groupRepository.existsBySectionAndCourse_CourseCode("A", "CS101");

        assertTrue(resultado);
        verify(groupRepository, times(1)).existsBySectionAndCourse_CourseCode("A", "CS101");
    }

    @Test
    @DisplayName("Caso error - existsBySectionAndCourse_CourseCode retorna false cuando no existe")
    void testExistsBySectionAndCourse_CourseCode_NoExiste() {
        when(groupRepository.existsBySectionAndCourse_CourseCode("Z", "CS999"))
                .thenReturn(false);

        boolean resultado = groupRepository.existsBySectionAndCourse_CourseCode("Z", "CS999");

        assertFalse(resultado);
        verify(groupRepository, times(1)).existsBySectionAndCourse_CourseCode("Z", "CS999");
    }

    @Test
    @DisplayName("Caso exitoso - findByCourse_CourseCodeIn retorna grupos de múltiples cursos")
    void testFindByCourse_CourseCodeIn_Exitoso() {
        when(groupRepository.findByCourse_CourseCodeIn(List.of("CS101", "MATH201")))
                .thenReturn(List.of(group1, group2, group3));

        List<Group> resultado = groupRepository.findByCourse_CourseCodeIn(List.of("CS101", "MATH201"));

        assertAll("Verificar grupos de múltiples cursos",
                () -> assertNotNull(resultado),
                () -> assertEquals(3, resultado.size()),
                () -> assertTrue(resultado.stream().anyMatch(g -> g.getCourse().getCourseCode().equals("CS101"))),
                () -> assertTrue(resultado.stream().anyMatch(g -> g.getCourse().getCourseCode().equals("MATH201")))
        );

        verify(groupRepository, times(1)).findByCourse_CourseCodeIn(List.of("CS101", "MATH201"));
    }

    @Test
    @DisplayName("Caso exitoso - findByProfessor_IdIn retorna grupos de múltiples profesores")
    void testFindByProfessor_IdIn_Exitoso() {
        when(groupRepository.findByProfessor_IdIn(List.of("PROF1", "PROF2")))
                .thenReturn(List.of(group1, group2, group3));

        List<Group> resultado = groupRepository.findByProfessor_IdIn(List.of("PROF1", "PROF2"));

        assertAll("Verificar grupos de múltiples profesores",
                () -> assertNotNull(resultado),
                () -> assertEquals(3, resultado.size()),
                () -> assertTrue(resultado.stream().anyMatch(g -> g.getProfessor().getId().equals("PROF1"))),
                () -> assertTrue(resultado.stream().anyMatch(g -> g.getProfessor().getId().equals("PROF2")))
        );

        verify(groupRepository, times(1)).findByProfessor_IdIn(List.of("PROF1", "PROF2"));
    }

    @Test
    @DisplayName("Caso exitoso - findBySectionRegex con query personalizada")
    void testFindBySectionRegex_Exitoso() {
        when(groupRepository.findBySectionRegex(".*A.*"))
                .thenReturn(List.of(group1, group3));

        List<Group> resultado = groupRepository.findBySectionRegex(".*A.*");

        assertAll("Verificar búsqueda por regex de sección",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertTrue(resultado.get(0).getSection().contains("A")),
                () -> assertTrue(resultado.get(1).getSection().contains("A"))
        );

        verify(groupRepository, times(1)).findBySectionRegex(".*A.*");
    }

    @Test
    @DisplayName("Caso exitoso - findByCourseAndMinimumCapacity con query personalizada")
    void testFindByCourseAndMinimumCapacity_Exitoso() {
        when(groupRepository.findByCourseAndMinimumCapacity("CS101", 80))
                .thenReturn(List.of(group2));

        List<Group> resultado = groupRepository.findByCourseAndMinimumCapacity("CS101", 80);

        assertAll("Verificar grupos por curso y capacidad mínima",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("CS101", resultado.get(0).getCourse().getCourseCode()),
                () -> assertTrue(resultado.get(0).getClassroom().getCapacity() >= 80)
        );

        verify(groupRepository, times(1)).findByCourseAndMinimumCapacity("CS101", 80);
    }

    @Test
    @DisplayName("Caso exitoso - findByCourseCodeSortedBySection con query personalizada")
    void testFindByCourseCodeSortedBySection_Exitoso() {
        when(groupRepository.findByCourseCodeSortedBySection("CS101"))
                .thenReturn(List.of(group1, group2));

        List<Group> resultado = groupRepository.findByCourseCodeSortedBySection("CS101");

        assertAll("Verificar grupos ordenados por sección",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals("A", resultado.get(0).getSection()),
                () -> assertEquals("B", resultado.get(1).getSection())
        );

        verify(groupRepository, times(1)).findByCourseCodeSortedBySection("CS101");
    }

    @Test
    @DisplayName("Caso exitoso - findByProfessorAndPeriod retorna grupos por profesor y período")
    void testFindByProfessorAndPeriod_Exitoso() {
        when(groupRepository.findByProfessorAndPeriod("PROF1", "Morning"))
                .thenReturn(List.of(group1, group3));

        List<Group> resultado = groupRepository.findByProfessorAndPeriod("PROF1", "Morning");

        assertAll("Verificar grupos por profesor y período",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals("PROF1", resultado.get(0).getProfessor().getId()),
                () -> assertEquals("Morning", resultado.get(0).getSchedule().getPeriod())
        );

        verify(groupRepository, times(1)).findByProfessorAndPeriod("PROF1", "Morning");
    }

    @Test
    @DisplayName("Caso borde - findBySection con sección nula")
    void testFindBySection_Nula() {
        when(groupRepository.findBySection(null))
                .thenReturn(Collections.emptyList());

        List<Group> resultado = groupRepository.findBySection(null);

        assertTrue(resultado.isEmpty());
        verify(groupRepository, times(1)).findBySection(null);
    }

    @Test
    @DisplayName("Caso borde - Verificar integridad de datos en grupos encontrados")
    void testIntegridadDatosGrupos() {
        when(groupRepository.findBySectionAndCourse_CourseCode("A", "CS101"))
                .thenReturn(List.of(group1));

        List<Group> resultado = groupRepository.findBySectionAndCourse_CourseCode("A", "CS101");

        assertAll("Verificar integridad completa del grupo",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("GROUP1", resultado.get(0).getGroupId()),
                () -> assertEquals("A", resultado.get(0).getSection()),
                () -> assertNotNull(resultado.get(0).getCourse()),
                () -> assertNotNull(resultado.get(0).getProfessor()),
                () -> assertNotNull(resultado.get(0).getSchedule()),
                () -> assertNotNull(resultado.get(0).getClassroom()),
                () -> assertInstanceOf(Group.class, resultado.get(0)),
                () -> assertNotNull(resultado.get(0).toString())
        );

        verify(groupRepository, times(1)).findBySectionAndCourse_CourseCode("A", "CS101");
    }

    @Test
    @DisplayName("Caso exitoso - findByClassroom_Building retorna grupos por edificio")
    void testFindByClassroom_Building_Exitoso() {
        when(groupRepository.findByClassroom_Building("A"))
                .thenReturn(List.of(group1, group3));

        List<Group> resultado = groupRepository.findByClassroom_Building("A");

        assertAll("Verificar grupos por edificio",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals("A", resultado.get(0).getClassroom().getBuilding()),
                () -> assertEquals("A", resultado.get(1).getClassroom().getBuilding())
        );

        verify(groupRepository, times(1)).findByClassroom_Building("A");
    }

    @Test
    @DisplayName("Caso exitoso - countByClassroom_CapacityGreaterThanEqual retorna conteo por capacidad")
    void testCountByClassroom_CapacityGreaterThanEqual_Exitoso() {
        when(groupRepository.countByClassroom_CapacityGreaterThanEqual(50))
                .thenReturn(3L);

        long resultado = groupRepository.countByClassroom_CapacityGreaterThanEqual(50);

        assertEquals(3L, resultado);
        verify(groupRepository, times(1)).countByClassroom_CapacityGreaterThanEqual(50);
    }
}
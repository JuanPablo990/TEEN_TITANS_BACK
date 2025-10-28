package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.RoomType;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class GroupTest {

    private Group group;
    private Course course;
    private Professor professor;
    private Schedule schedule;
    private Classroom classroom;

    @BeforeEach
    void setUp() {
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
    }

    @Test
    @DisplayName("Caso exitoso - Constructor con todos los parámetros inicializa correctamente")
    void testConstructorCompleto_Exitoso() {
        assertAll("Validar todos los campos del constructor completo",
                () -> assertEquals("GRP001", group.getGroupId()),
                () -> assertEquals("A", group.getSection()),
                () -> assertEquals(course, group.getCourse()),
                () -> assertEquals(professor, group.getProfessor()),
                () -> assertEquals(schedule, group.getSchedule()),
                () -> assertEquals(classroom, group.getClassroom())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Constructor sin groupId para generación automática")
    void testConstructorSinGroupId_Exitoso() {
        Group groupSinId = new Group(
                "B",
                course,
                professor,
                schedule,
                classroom
        );

        assertAll("Validar constructor sin groupId",
                () -> assertNull(groupSinId.getGroupId()),
                () -> assertEquals("B", groupSinId.getSection()),
                () -> assertEquals(course, groupSinId.getCourse()),
                () -> assertEquals(professor, groupSinId.getProfessor()),
                () -> assertEquals(schedule, groupSinId.getSchedule()),
                () -> assertEquals(classroom, groupSinId.getClassroom())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Constructor por defecto inicializa con valores por defecto")
    void testConstructorPorDefecto_Exitoso() {
        Group groupVacio = new Group();

        assertAll("Validar valores por defecto",
                () -> assertNull(groupVacio.getGroupId()),
                () -> assertNull(groupVacio.getSection()),
                () -> assertNull(groupVacio.getCourse()),
                () -> assertNull(groupVacio.getProfessor()),
                () -> assertNull(groupVacio.getSchedule()),
                () -> assertNull(groupVacio.getClassroom())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Setters y Getters funcionan correctamente")
    void testSettersAndGetters_Exitoso() {
        Group groupTest = new Group();

        Course newCourse = new Course("PHY101", "Física I", 3, "Curso de física", "Ingeniería", true);
        Professor newProfessor = new Professor("PROF002", "Dra. María López", "maria.lopez@titans.edu", "password456", "Física", false, Arrays.asList("Mecánica", "Termodinámica"));
        Schedule newSchedule = new Schedule("SCH002", "Martes", "14:00", "16:00", "2025-1");
        Classroom newClassroom = new Classroom("CLS002", "Edificio Ciencias", "LAB201", 25, RoomType.LABORATORY);

        groupTest.setGroupId("GRP002");
        groupTest.setSection("C");
        groupTest.setCourse(newCourse);
        groupTest.setProfessor(newProfessor);
        groupTest.setSchedule(newSchedule);
        groupTest.setClassroom(newClassroom);

        assertAll("Validar setters y getters",
                () -> assertEquals("GRP002", groupTest.getGroupId()),
                () -> assertEquals("C", groupTest.getSection()),
                () -> assertEquals(newCourse, groupTest.getCourse()),
                () -> assertEquals(newProfessor, groupTest.getProfessor()),
                () -> assertEquals(newSchedule, groupTest.getSchedule()),
                () -> assertEquals(newClassroom, groupTest.getClassroom())
        );
    }

    @Test
    @DisplayName("Caso borde - Campos con valores null")
    void testCamposConValoresNull_Borde() {
        Group groupNull = new Group(
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertAll("Validar campos null",
                () -> assertNull(groupNull.getGroupId()),
                () -> assertNull(groupNull.getSection()),
                () -> assertNull(groupNull.getCourse()),
                () -> assertNull(groupNull.getProfessor()),
                () -> assertNull(groupNull.getSchedule()),
                () -> assertNull(groupNull.getClassroom())
        );
    }

    @Test
    @DisplayName("Caso borde - Sección con valores vacíos")
    void testSeccionConValoresVacios_Borde() {
        group.setSection("");
        assertEquals("", group.getSection());

        group.setSection("   ");
        assertEquals("   ", group.getSection());
    }

    @Test
    @DisplayName("Caso exitoso - Equals y HashCode generados por Lombok")
    void testEqualsAndHashCode_Exitoso() {
        Group group1 = new Group(
                "GRP001",
                "A",
                course,
                professor,
                schedule,
                classroom
        );

        Group group2 = new Group(
                "GRP001",
                "A",
                course,
                professor,
                schedule,
                classroom
        );

        Group group3 = new Group(
                "GRP002",
                "B",
                course,
                professor,
                schedule,
                classroom
        );

        assertEquals(group1, group2);
        assertNotEquals(group1, group3);
        assertEquals(group1.hashCode(), group2.hashCode());
        assertNotEquals(group1.hashCode(), group3.hashCode());
    }

    @Test
    @DisplayName("Caso borde - Equals con objetos null y diferente clase")
    void testEqualsCasosBorde_Borde() {
        Group groupTest = new Group(
                "GRP001",
                "A",
                course,
                professor,
                schedule,
                classroom
        );

        assertNotEquals(null, groupTest);
        assertNotEquals("No soy un Group", groupTest);
    }



    @Test
    @DisplayName("Caso compuesto - Validación completa de múltiples escenarios")
    void testGroup_ValidacionCompleta_Compuesto() {
        assertAll("Validación completa de todas las propiedades",
                () -> assertEquals("GRP001", group.getGroupId()),
                () -> assertEquals("A", group.getSection()),
                () -> assertEquals(course, group.getCourse()),
                () -> assertEquals(professor, group.getProfessor()),
                () -> assertEquals(schedule, group.getSchedule()),
                () -> assertEquals(classroom, group.getClassroom()),
                () -> assertNotNull(group.toString()),
                () -> assertTrue(group.toString().contains("Group"))
        );
    }

    @Test
    @DisplayName("Caso borde - Secciones con diferentes formatos")
    void testSeccionesFormatos_Borde() {
        group.setSection("A1");
        assertEquals("A1", group.getSection());

        group.setSection("LAB-01");
        assertEquals("LAB-01", group.getSection());

        group.setSection("GRUPO_ESPECIAL");
        assertEquals("GRUPO_ESPECIAL", group.getSection());

        group.setSection("1");
        assertEquals("1", group.getSection());
    }

    @Test
    @DisplayName("Caso borde - GroupId con diferentes formatos")
    void testGroupIdFormatos_Borde() {
        group.setGroupId("2025-MATH-101-A");
        assertEquals("2025-MATH-101-A", group.getGroupId());

        group.setGroupId("GRUPO_FISICA_1");
        assertEquals("GRUPO_FISICA_1", group.getGroupId());

        group.setGroupId("123456");
        assertEquals("123456", group.getGroupId());
    }

    @Test
    @DisplayName("Caso borde - Referencias DBRef con valores null")
    void testDBRefConValoresNull_Borde() {
        Group groupConNulls = new Group(
                "GRP003",
                "C",
                null,
                null,
                null,
                null
        );

        assertAll("Validar DBRef null",
                () -> assertEquals("GRP003", groupConNulls.getGroupId()),
                () -> assertEquals("C", groupConNulls.getSection()),
                () -> assertNull(groupConNulls.getCourse()),
                () -> assertNull(groupConNulls.getProfessor()),
                () -> assertNull(groupConNulls.getSchedule()),
                () -> assertNull(groupConNulls.getClassroom())
        );
    }

    @Test
    @DisplayName("Caso borde - Diferentes tipos de aula en grupos")
    void testDiferentesTiposAula_Borde() {
        Classroom labComputacion = new Classroom("COMP001", "Edificio Tecnología", "COMP1", 30, RoomType.COMPUTER_LAB);
        Classroom auditorio = new Classroom("AUD001", "Edificio Principal", "AUD1", 200, RoomType.AUDITORIUM);
        Classroom taller = new Classroom("WORK001", "Edificio Talleres", "WORK1", 15, RoomType.WORKSHOP_ROOM);

        Group groupLab = new Group("GRP_LAB", "LAB1", course, professor, schedule, labComputacion);
        Group groupAud = new Group("GRP_AUD", "AUD1", course, professor, schedule, auditorio);
        Group groupWork = new Group("GRP_WORK", "WORK1", course, professor, schedule, taller);

        assertAll("Validar diferentes tipos de aula",
                () -> assertEquals(RoomType.COMPUTER_LAB, groupLab.getClassroom().getRoomType()),
                () -> assertEquals(RoomType.AUDITORIUM, groupAud.getClassroom().getRoomType()),
                () -> assertEquals(RoomType.WORKSHOP_ROOM, groupWork.getClassroom().getRoomType())
        );
    }

    @Test
    @DisplayName("Caso borde - Diferentes horarios en grupos")
    void testDiferentesHorarios_Borde() {
        Schedule manana = new Schedule("SCH_MAN", "Lunes", "08:00", "10:00", "2025-1");
        Schedule tarde = new Schedule("SCH_TAR", "Martes", "14:00", "16:00", "2025-1");
        Schedule noche = new Schedule("SCH_NOC", "Miércoles", "18:00", "20:00", "2025-1");

        Group groupManana = new Group("GRP_MAN", "M", course, professor, manana, classroom);
        Group groupTarde = new Group("GRP_TAR", "T", course, professor, tarde, classroom);
        Group groupNoche = new Group("GRP_NOC", "N", course, professor, noche, classroom);

        assertAll("Validar diferentes horarios",
                () -> assertEquals("08:00", groupManana.getSchedule().getStartHour()),
                () -> assertEquals("14:00", groupTarde.getSchedule().getStartHour()),
                () -> assertEquals("18:00", groupNoche.getSchedule().getStartHour())
        );
    }

    @Test
    @DisplayName("Caso borde - Diferentes profesores en grupos")
    void testDiferentesProfesores_Borde() {
        Professor profMatematicas = new Professor("PROF_MATH", "Dr. Matemático", "math@titans.edu", "pass", "Matemáticas", true, Arrays.asList("Cálculo"));
        Professor profFisica = new Professor("PROF_PHYS", "Dra. Física", "phys@titans.edu", "pass", "Física", false, Arrays.asList("Mecánica"));
        Professor profQuimica = new Professor("PROF_CHEM", "Dr. Químico", "chem@titans.edu", "pass", "Química", true, Arrays.asList("Orgánica"));

        Group groupMath = new Group("GRP_MATH", "A", course, profMatematicas, schedule, classroom);
        Group groupPhys = new Group("GRP_PHYS", "B", course, profFisica, schedule, classroom);
        Group groupChem = new Group("GRP_CHEM", "C", course, profQuimica, schedule, classroom);

        assertAll("Validar diferentes profesores",
                () -> assertEquals("Matemáticas", groupMath.getProfessor().getDepartment()),
                () -> assertEquals("Física", groupPhys.getProfessor().getDepartment()),
                () -> assertEquals("Química", groupChem.getProfessor().getDepartment())
        );
    }
}
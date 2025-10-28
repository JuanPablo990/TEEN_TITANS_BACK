package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.RoomType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClassroomTest {

    private Classroom classroom;

    @BeforeEach
    void setUp() {
        classroom = new Classroom(
                "CLS001",
                "Edificio Principal",
                "A101",
                40,
                RoomType.REGULAR
        );
    }

    @Test
    @DisplayName("Caso exitoso - Constructor con todos los parámetros inicializa correctamente")
    void testConstructorCompleto_Exitoso() {
        assertAll("Validar todos los campos del constructor completo",
                () -> assertEquals("CLS001", classroom.getClassroomId()),
                () -> assertEquals("Edificio Principal", classroom.getBuilding()),
                () -> assertEquals("A101", classroom.getRoomNumber()),
                () -> assertEquals(40, classroom.getCapacity()),
                () -> assertEquals(RoomType.REGULAR, classroom.getRoomType())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Constructor sin ID para generación automática de MongoDB")
    void testConstructorSinID_Exitoso() {
        Classroom classroomSinId = new Classroom(
                "Edificio Ciencias",
                "LAB201",
                25,
                RoomType.LABORATORY
        );

        assertAll("Validar constructor sin ID",
                () -> assertNull(classroomSinId.getClassroomId()),
                () -> assertEquals("Edificio Ciencias", classroomSinId.getBuilding()),
                () -> assertEquals("LAB201", classroomSinId.getRoomNumber()),
                () -> assertEquals(25, classroomSinId.getCapacity()),
                () -> assertEquals(RoomType.LABORATORY, classroomSinId.getRoomType())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Constructor por defecto inicializa con valores por defecto")
    void testConstructorPorDefecto_Exitoso() {
        Classroom classroomVacio = new Classroom();

        assertAll("Validar valores por defecto",
                () -> assertNull(classroomVacio.getClassroomId()),
                () -> assertNull(classroomVacio.getBuilding()),
                () -> assertNull(classroomVacio.getRoomNumber()),
                () -> assertNull(classroomVacio.getCapacity()),
                () -> assertNull(classroomVacio.getRoomType())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Setters y Getters funcionan correctamente")
    void testSettersAndGetters_Exitoso() {
        Classroom classroomTest = new Classroom();

        classroomTest.setClassroomId("CLS002");
        classroomTest.setBuilding("Edificio Ingeniería");
        classroomTest.setRoomNumber("B205");
        classroomTest.setCapacity(60);
        classroomTest.setRoomType(RoomType.AUDITORIUM);

        assertAll("Validar setters y getters",
                () -> assertEquals("CLS002", classroomTest.getClassroomId()),
                () -> assertEquals("Edificio Ingeniería", classroomTest.getBuilding()),
                () -> assertEquals("B205", classroomTest.getRoomNumber()),
                () -> assertEquals(60, classroomTest.getCapacity()),
                () -> assertEquals(RoomType.AUDITORIUM, classroomTest.getRoomType())
        );
    }

    @Test
    @DisplayName("Caso borde - Campos con valores null")
    void testCamposConValoresNull_Borde() {
        Classroom classroomNull = new Classroom(
                null,
                null,
                null,
                null,
                null
        );

        assertAll("Validar campos null",
                () -> assertNull(classroomNull.getClassroomId()),
                () -> assertNull(classroomNull.getBuilding()),
                () -> assertNull(classroomNull.getRoomNumber()),
                () -> assertNull(classroomNull.getCapacity()),
                () -> assertNull(classroomNull.getRoomType())
        );
    }

    @Test
    @DisplayName("Caso borde - Campos con valores vacíos")
    void testCamposConValoresVacios_Borde() {
        classroom.setBuilding("");
        classroom.setRoomNumber("");

        assertAll("Validar campos vacíos",
                () -> assertEquals("", classroom.getBuilding()),
                () -> assertEquals("", classroom.getRoomNumber())
        );
    }

    @Test
    @DisplayName("Caso borde - Capacidad con valores extremos")
    void testCapacidadValoresExtremos_Borde() {
        classroom.setCapacity(0);
        assertEquals(0, classroom.getCapacity());

        classroom.setCapacity(1000);
        assertEquals(1000, classroom.getCapacity());

        classroom.setCapacity(-10);
        assertEquals(-10, classroom.getCapacity());
    }

    @Test
    @DisplayName("Caso exitoso - Equals y HashCode generados por Lombok")
    void testEqualsAndHashCode_Exitoso() {
        Classroom classroom1 = new Classroom(
                "CLS001",
                "Edificio A",
                "101",
                30,
                RoomType.REGULAR
        );

        Classroom classroom2 = new Classroom(
                "CLS001",
                "Edificio A",
                "101",
                30,
                RoomType.REGULAR
        );

        Classroom classroom3 = new Classroom(
                "CLS002",
                "Edificio B",
                "201",
                50,
                RoomType.LABORATORY
        );

        assertEquals(classroom1, classroom2);
        assertNotEquals(classroom1, classroom3);
        assertEquals(classroom1.hashCode(), classroom2.hashCode());
        assertNotEquals(classroom1.hashCode(), classroom3.hashCode());
    }

    @Test
    @DisplayName("Caso borde - Equals con objetos null y diferente clase")
    void testEqualsCasosBorde_Borde() {
        Classroom classroomTest = new Classroom(
                "CLS001",
                "Edificio",
                "101",
                30,
                RoomType.REGULAR
        );

        assertNotEquals(null, classroomTest);
        assertNotEquals("No soy un Classroom", classroomTest);
    }

    @Test
    @DisplayName("Caso exitoso - ToString generado por Lombok")
    void testToString_Exitoso() {
        String resultadoToString = classroom.toString();

        assertAll("ToString debe contener todos los campos principales",
                () -> assertTrue(resultadoToString.contains("CLS001")),
                () -> assertTrue(resultadoToString.contains("Edificio Principal")),
                () -> assertTrue(resultadoToString.contains("A101")),
                () -> assertTrue(resultadoToString.contains("40")),
                () -> assertTrue(resultadoToString.contains("REGULAR")),
                () -> assertTrue(resultadoToString.contains("Classroom"))
        );
    }

    @Test
    @DisplayName("Caso compuesto - Validación completa de múltiples escenarios")
    void testClassroom_ValidacionCompleta_Compuesto() {
        assertAll("Validación completa de todas las propiedades",
                () -> assertEquals("CLS001", classroom.getClassroomId()),
                () -> assertEquals("Edificio Principal", classroom.getBuilding()),
                () -> assertEquals("A101", classroom.getRoomNumber()),
                () -> assertEquals(40, classroom.getCapacity()),
                () -> assertEquals(RoomType.REGULAR, classroom.getRoomType()),
                () -> assertNotNull(classroom.toString()),
                () -> assertTrue(classroom.toString().contains("Classroom"))
        );
    }

    @Test
    @DisplayName("Caso borde - RoomType con todos los valores posibles")
    void testRoomTypeTodosLosValores_Borde() {
        Classroom classroomLab = new Classroom("LAB001", "Edificio Ciencias", "LAB1", 25, RoomType.LABORATORY);
        Classroom classroomAud = new Classroom("AUD001", "Edificio Principal", "AUD1", 200, RoomType.AUDITORIUM);
        Classroom classroomComp = new Classroom("COMP001", "Edificio Tecnología", "COMP1", 30, RoomType.COMPUTER_LAB);
        Classroom classroomSem = new Classroom("SEM001", "Edificio Posgrado", "SEM1", 20, RoomType.SEMINAR_ROOM);
        Classroom classroomWork = new Classroom("WORK001", "Edificio Talleres", "WORK1", 15, RoomType.WORKSHOP_ROOM);
        Classroom classroomSpec = new Classroom("SPEC001", "Edificio Investigación", "SPEC1", 10, RoomType.SPECIALIZED_LAB);

        assertAll("Validar todos los tipos de aula",
                () -> assertEquals(RoomType.LABORATORY, classroomLab.getRoomType()),
                () -> assertEquals(RoomType.AUDITORIUM, classroomAud.getRoomType()),
                () -> assertEquals(RoomType.COMPUTER_LAB, classroomComp.getRoomType()),
                () -> assertEquals(RoomType.SEMINAR_ROOM, classroomSem.getRoomType()),
                () -> assertEquals(RoomType.WORKSHOP_ROOM, classroomWork.getRoomType()),
                () -> assertEquals(RoomType.SPECIALIZED_LAB, classroomSpec.getRoomType())
        );
    }

    @Test
    @DisplayName("Caso borde - Números de sala con diferentes formatos")
    void testRoomNumberFormatosDiferentes_Borde() {
        classroom.setRoomNumber("101-A");
        assertEquals("101-A", classroom.getRoomNumber());

        classroom.setRoomNumber("LAB-2B");
        assertEquals("LAB-2B", classroom.getRoomNumber());

        classroom.setRoomNumber("PISO_3_SALA_C");
        assertEquals("PISO_3_SALA_C", classroom.getRoomNumber());
    }

    @Test
    @DisplayName("Caso borde - Nombres de edificio con caracteres especiales")
    void testBuildingCaracteresEspeciales_Borde() {
        classroom.setBuilding("Edificio Ñandú");
        assertEquals("Edificio Ñandú", classroom.getBuilding());

        classroom.setBuilding("Área de Ciencias 🏫");
        assertEquals("Área de Ciencias 🏫", classroom.getBuilding());

        String buildingLargo = "A".repeat(100);
        classroom.setBuilding(buildingLargo);
        assertEquals(buildingLargo, classroom.getBuilding());
    }

    @Test
    @DisplayName("Caso borde - IDs con diferentes formatos")
    void testClassroomIDsFormatos_Borde() {
        classroom.setClassroomId("2025-CLS-001");
        assertEquals("2025-CLS-001", classroom.getClassroomId());

        classroom.setClassroomId("LAB_COMP_SCI_01");
        assertEquals("LAB_COMP_SCI_01", classroom.getClassroomId());

        classroom.setClassroomId("123456789");
        assertEquals("123456789", classroom.getClassroomId());
    }
}
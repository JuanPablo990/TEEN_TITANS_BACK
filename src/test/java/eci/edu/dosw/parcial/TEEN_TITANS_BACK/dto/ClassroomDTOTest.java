package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.RoomType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClassroomDTOTest {

    private ClassroomDTO classroomDTO;

    @BeforeEach
    void setUp() {
        classroomDTO = new ClassroomDTO();
        classroomDTO.setClassroomId("CLASS_001");
        classroomDTO.setBuilding("A");
        classroomDTO.setRoomNumber("101");
        classroomDTO.setCapacity(30);
        classroomDTO.setRoomType(RoomType.LABORATORY);
        classroomDTO.setDisplayName("A-101");
        classroomDTO.setIsAvailable(true);
    }

    @Test
    @DisplayName("Caso exitoso - Creación de ClassroomDTO con constructor completo")
    void testClassroomDTOConstructorCompleto_Exitoso() {
        ClassroomDTO dto = new ClassroomDTO("CLASS_002", "B", "201", 40, RoomType.REGULAR, "B-201", false);

        assertAll("Verificación de constructor completo",
                () -> assertEquals("CLASS_002", dto.getClassroomId()),
                () -> assertEquals("B", dto.getBuilding()),
                () -> assertEquals("201", dto.getRoomNumber()),
                () -> assertEquals(40, dto.getCapacity()),
                () -> assertEquals(RoomType.REGULAR, dto.getRoomType()),
                () -> assertEquals("B-201", dto.getDisplayName()),
                () -> assertFalse(dto.getIsAvailable())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Getters y setters funcionan correctamente")
    void testGettersAndSetters_Exitoso() {
        assertAll("Verificación de getters y setters",
                () -> assertEquals("CLASS_001", classroomDTO.getClassroomId()),
                () -> assertEquals("A", classroomDTO.getBuilding()),
                () -> assertEquals("101", classroomDTO.getRoomNumber()),
                () -> assertEquals(30, classroomDTO.getCapacity()),
                () -> assertEquals(RoomType.LABORATORY, classroomDTO.getRoomType()),
                () -> assertEquals("A-101", classroomDTO.getDisplayName()),
                () -> assertTrue(classroomDTO.getIsAvailable())
        );
    }

    @Test
    @DisplayName("Caso borde - Constructor sin argumentos crea instancia")
    void testNoArgsConstructor_Exitoso() {
        ClassroomDTO emptyDTO = new ClassroomDTO();
        assertNotNull(emptyDTO);
    }

    @Test
    @DisplayName("Caso borde - equals retorna true para DTOs idénticos")
    void testEquals_Exitoso() {
        ClassroomDTO dto1 = new ClassroomDTO("CLASS_001", "A", "101", 30, RoomType.LABORATORY, "A-101", true);
        ClassroomDTO dto2 = new ClassroomDTO("CLASS_001", "A", "101", 30, RoomType.LABORATORY, "A-101", true);

        assertEquals(dto1, dto2);
    }

    @Test
    @DisplayName("Caso error - equals retorna false para DTOs diferentes")
    void testEquals_Diferentes() {
        ClassroomDTO dto1 = new ClassroomDTO("CLASS_001", "A", "101", 30, RoomType.LABORATORY, "A-101", true);
        ClassroomDTO dto2 = new ClassroomDTO("CLASS_002", "B", "201", 40, RoomType.REGULAR, "B-201", false);

        assertNotEquals(dto1, dto2);
    }

    @Test
    @DisplayName("Caso borde - hashCode consistente para DTOs iguales")
    void testHashCode_Exitoso() {
        ClassroomDTO dto1 = new ClassroomDTO("CLASS_001", "A", "101", 30, RoomType.LABORATORY, "A-101", true);
        ClassroomDTO dto2 = new ClassroomDTO("CLASS_001", "A", "101", 30, RoomType.LABORATORY, "A-101", true);

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    @DisplayName("Caso borde - toString no retorna null")
    void testToString_NoNull() {
        String toStringResult = classroomDTO.toString();
        assertNotNull(toStringResult);
        assertFalse(toStringResult.isEmpty());
    }

    @Test
    @DisplayName("Caso borde - Campos null manejados correctamente")
    void testCamposNull_Exitoso() {
        ClassroomDTO dto = new ClassroomDTO();
        dto.setClassroomId(null);
        dto.setBuilding(null);
        dto.setRoomNumber(null);
        dto.setCapacity(null);
        dto.setRoomType(null);
        dto.setDisplayName(null);
        dto.setIsAvailable(null);

        assertAll("Verificación de campos null",
                () -> assertNull(dto.getClassroomId()),
                () -> assertNull(dto.getBuilding()),
                () -> assertNull(dto.getRoomNumber()),
                () -> assertNull(dto.getCapacity()),
                () -> assertNull(dto.getRoomType()),
                () -> assertNull(dto.getDisplayName()),
                () -> assertNull(dto.getIsAvailable())
        );
    }

    @Test
    @DisplayName("Caso borde - Capacidad cero manejada correctamente")
    void testCapacidadCero_Exitoso() {
        classroomDTO.setCapacity(0);
        assertEquals(0, classroomDTO.getCapacity());
    }

    @Test
    @DisplayName("Caso borde - Capacidad negativa manejada correctamente")
    void testCapacidadNegativa_Exitoso() {
        classroomDTO.setCapacity(-10);
        assertEquals(-10, classroomDTO.getCapacity());
    }

    @Test
    @DisplayName("Caso exitoso - Todos los RoomType funcionan correctamente")
    void testAllRoomTypes_Exitoso() {
        ClassroomDTO dto = new ClassroomDTO();

        dto.setRoomType(RoomType.REGULAR);
        assertEquals(RoomType.REGULAR, dto.getRoomType());

        dto.setRoomType(RoomType.LABORATORY);
        assertEquals(RoomType.LABORATORY, dto.getRoomType());

        dto.setRoomType(RoomType.AUDITORIUM);
        assertEquals(RoomType.AUDITORIUM, dto.getRoomType());

        dto.setRoomType(RoomType.COMPUTER_LAB);
        assertEquals(RoomType.COMPUTER_LAB, dto.getRoomType());

        dto.setRoomType(RoomType.SEMINAR_ROOM);
        assertEquals(RoomType.SEMINAR_ROOM, dto.getRoomType());

        dto.setRoomType(RoomType.WORKSHOP_ROOM);
        assertEquals(RoomType.WORKSHOP_ROOM, dto.getRoomType());

        dto.setRoomType(RoomType.SPECIALIZED_LAB);
        assertEquals(RoomType.SPECIALIZED_LAB, dto.getRoomType());
    }

    @Test
    @DisplayName("Caso borde - DisplayName con valores extremos")
    void testDisplayNameValoresExtremos_Exitoso() {
        classroomDTO.setDisplayName("");
        assertEquals("", classroomDTO.getDisplayName());

        classroomDTO.setDisplayName("Edificio-Muy-Largo-202");
        assertEquals("Edificio-Muy-Largo-202", classroomDTO.getDisplayName());
    }

    @Test
    @DisplayName("Caso borde - RoomNumber con formato especial")
    void testRoomNumberFormatoEspecial_Exitoso() {
        classroomDTO.setRoomNumber("101-A");
        assertEquals("101-A", classroomDTO.getRoomNumber());

        classroomDTO.setRoomNumber("S-202");
        assertEquals("S-202", classroomDTO.getRoomNumber());

        classroomDTO.setRoomNumber("B1-305");
        assertEquals("B1-305", classroomDTO.getRoomNumber());
    }

    @Test
    @DisplayName("Caso borde - Building con nombres compuestos")
    void testBuildingNombresCompuestos_Exitoso() {
        classroomDTO.setBuilding("Edificio Principal");
        assertEquals("Edificio Principal", classroomDTO.getBuilding());

        classroomDTO.setBuilding("Torre Norte");
        assertEquals("Torre Norte", classroomDTO.getBuilding());

        classroomDTO.setBuilding("C");
        assertEquals("C", classroomDTO.getBuilding());
    }

    @Test
    @DisplayName("Caso exitoso - IsAvailable con diferentes valores booleanos")
    void testIsAvailableValoresBooleanos_Exitoso() {
        classroomDTO.setIsAvailable(true);
        assertTrue(classroomDTO.getIsAvailable());

        classroomDTO.setIsAvailable(false);
        assertFalse(classroomDTO.getIsAvailable());

        classroomDTO.setIsAvailable(null);
        assertNull(classroomDTO.getIsAvailable());
    }

    @Test
    @DisplayName("Caso borde - Capacidad con valores grandes")
    void testCapacidadValoresGrandes_Exitoso() {
        classroomDTO.setCapacity(1000);
        assertEquals(1000, classroomDTO.getCapacity());

        classroomDTO.setCapacity(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, classroomDTO.getCapacity());
    }
}
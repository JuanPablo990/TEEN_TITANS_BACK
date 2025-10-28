package eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RoomTypeTest {

    @Test
    @DisplayName("Caso exitoso - Enum contiene todos los valores esperados")
    void testValoresEnum_Exitoso() {
        RoomType[] valores = RoomType.values();

        assertAll("Validar que el enum contiene todos los valores definidos",
                () -> assertEquals(7, valores.length),
                () -> assertEquals(RoomType.REGULAR, valores[0]),
                () -> assertEquals(RoomType.LABORATORY, valores[1]),
                () -> assertEquals(RoomType.AUDITORIUM, valores[2]),
                () -> assertEquals(RoomType.COMPUTER_LAB, valores[3]),
                () -> assertEquals(RoomType.SEMINAR_ROOM, valores[4]),
                () -> assertEquals(RoomType.WORKSHOP_ROOM, valores[5]),
                () -> assertEquals(RoomType.SPECIALIZED_LAB, valores[6])
        );
    }

    @Test
    @DisplayName("Caso exitoso - valueOf retorna el enum correcto")
    void testValueOf_Exitoso() {
        assertAll("Validar valueOf para cada valor del enum",
                () -> assertEquals(RoomType.REGULAR, RoomType.valueOf("REGULAR")),
                () -> assertEquals(RoomType.LABORATORY, RoomType.valueOf("LABORATORY")),
                () -> assertEquals(RoomType.AUDITORIUM, RoomType.valueOf("AUDITORIUM")),
                () -> assertEquals(RoomType.COMPUTER_LAB, RoomType.valueOf("COMPUTER_LAB")),
                () -> assertEquals(RoomType.SEMINAR_ROOM, RoomType.valueOf("SEMINAR_ROOM")),
                () -> assertEquals(RoomType.WORKSHOP_ROOM, RoomType.valueOf("WORKSHOP_ROOM")),
                () -> assertEquals(RoomType.SPECIALIZED_LAB, RoomType.valueOf("SPECIALIZED_LAB"))
        );
    }

    @Test
    @DisplayName("Caso error - valueOf con nombre inválido lanza excepción")
    void testValueOf_NombreInvalido_Error() {
        assertThrows(IllegalArgumentException.class, () -> {
            RoomType.valueOf("NO_EXISTE");
        });
    }

    @Test
    @DisplayName("Caso borde - valueOf es case sensitive")
    void testValueOf_CaseSensitive_Borde() {
        assertThrows(IllegalArgumentException.class, () -> {
            RoomType.valueOf("regular");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            RoomType.valueOf("Computer_Lab");
        });
    }

    @Test
    @DisplayName("Caso exitoso - name() retorna el nombre correcto")
    void testName_Exitoso() {
        assertAll("Validar name() para cada valor del enum",
                () -> assertEquals("REGULAR", RoomType.REGULAR.name()),
                () -> assertEquals("LABORATORY", RoomType.LABORATORY.name()),
                () -> assertEquals("AUDITORIUM", RoomType.AUDITORIUM.name()),
                () -> assertEquals("COMPUTER_LAB", RoomType.COMPUTER_LAB.name()),
                () -> assertEquals("SEMINAR_ROOM", RoomType.SEMINAR_ROOM.name()),
                () -> assertEquals("WORKSHOP_ROOM", RoomType.WORKSHOP_ROOM.name()),
                () -> assertEquals("SPECIALIZED_LAB", RoomType.SPECIALIZED_LAB.name())
        );
    }

    @Test
    @DisplayName("Caso exitoso - ordinal() retorna la posición correcta")
    void testOrdinal_Exitoso() {
        assertAll("Validar ordinal() para cada valor del enum",
                () -> assertEquals(0, RoomType.REGULAR.ordinal()),
                () -> assertEquals(1, RoomType.LABORATORY.ordinal()),
                () -> assertEquals(2, RoomType.AUDITORIUM.ordinal()),
                () -> assertEquals(3, RoomType.COMPUTER_LAB.ordinal()),
                () -> assertEquals(4, RoomType.SEMINAR_ROOM.ordinal()),
                () -> assertEquals(5, RoomType.WORKSHOP_ROOM.ordinal()),
                () -> assertEquals(6, RoomType.SPECIALIZED_LAB.ordinal())
        );
    }

    @Test
    @DisplayName("Caso exitoso - toString() retorna el nombre del enum")
    void testToString_Exitoso() {
        assertAll("Validar toString() para cada valor del enum",
                () -> assertEquals("REGULAR", RoomType.REGULAR.toString()),
                () -> assertEquals("LABORATORY", RoomType.LABORATORY.toString()),
                () -> assertEquals("AUDITORIUM", RoomType.AUDITORIUM.toString()),
                () -> assertEquals("COMPUTER_LAB", RoomType.COMPUTER_LAB.toString()),
                () -> assertEquals("SEMINAR_ROOM", RoomType.SEMINAR_ROOM.toString()),
                () -> assertEquals("WORKSHOP_ROOM", RoomType.WORKSHOP_ROOM.toString()),
                () -> assertEquals("SPECIALIZED_LAB", RoomType.SPECIALIZED_LAB.toString())
        );
    }

    @Test
    @DisplayName("Caso compuesto - Comparación entre valores del enum")
    void testComparacionEnums_Compuesto() {
        assertAll("Validar comparaciones entre enums",
                () -> assertTrue(RoomType.REGULAR.compareTo(RoomType.LABORATORY) < 0),
                () -> assertTrue(RoomType.LABORATORY.compareTo(RoomType.AUDITORIUM) < 0),
                () -> assertTrue(RoomType.AUDITORIUM.compareTo(RoomType.COMPUTER_LAB) < 0),
                () -> assertEquals(0, RoomType.REGULAR.compareTo(RoomType.REGULAR))
        );
    }

    @Test
    @DisplayName("Caso exitoso - equals() funciona correctamente")
    void testEquals_Exitoso() {
        assertAll("Validar equals() para el enum",
                () -> assertEquals(RoomType.REGULAR, RoomType.REGULAR),
                () -> assertEquals(RoomType.COMPUTER_LAB, RoomType.COMPUTER_LAB),
                () -> assertNotEquals(RoomType.REGULAR, RoomType.LABORATORY),
                () -> assertNotEquals(RoomType.AUDITORIUM, RoomType.SEMINAR_ROOM)
        );
    }

    @Test
    @DisplayName("Caso borde - equals() con null y otros objetos")
    void testEquals_CasosBorde_Borde() {
        assertAll("Validar equals() con casos borde",
                () -> assertNotEquals(null, RoomType.REGULAR),
                () -> assertNotEquals("REGULAR", RoomType.REGULAR),
                () -> assertNotEquals(0, RoomType.REGULAR)
        );
    }

    @Test
    @DisplayName("Caso exitoso - hashCode() es consistente")
    void testHashCode_Exitoso() {
        assertAll("Validar hashCode() para el enum",
                () -> assertEquals(RoomType.REGULAR.hashCode(), RoomType.REGULAR.hashCode()),
                () -> assertEquals(RoomType.LABORATORY.hashCode(), RoomType.LABORATORY.hashCode()),
                () -> assertNotEquals(RoomType.REGULAR.hashCode(), RoomType.LABORATORY.hashCode())
        );
    }

    @Test
    @DisplayName("Caso compuesto - Uso en estructuras de datos")
    void testUsoEnEstructurasDatos_Compuesto() {
        java.util.Set<RoomType> set = java.util.EnumSet.allOf(RoomType.class);
        java.util.Map<RoomType, String> map = new java.util.EnumMap<>(RoomType.class);

        map.put(RoomType.REGULAR, "Aula Regular");
        map.put(RoomType.COMPUTER_LAB, "Laboratorio de Computación");

        assertAll("Validar uso en estructuras de datos",
                () -> assertEquals(7, set.size()),
                () -> assertTrue(set.contains(RoomType.REGULAR)),
                () -> assertTrue(set.contains(RoomType.LABORATORY)),
                () -> assertEquals("Aula Regular", map.get(RoomType.REGULAR)),
                () -> assertEquals("Laboratorio de Computación", map.get(RoomType.COMPUTER_LAB))
        );
    }

    @Test
    @DisplayName("Caso borde - Switch statement con enum")
    void testSwitchStatement_Borde() {
        String resultadoRegular = obtenerDescripcionTipo(RoomType.REGULAR);
        String resultadoLab = obtenerDescripcionTipo(RoomType.LABORATORY);
        String resultadoAuditorium = obtenerDescripcionTipo(RoomType.AUDITORIUM);
        String resultadoDefault = obtenerDescripcionTipo(null);

        assertAll("Validar switch statement con enum",
                () -> assertEquals("Aula regular", resultadoRegular),
                () -> assertEquals("Laboratorio general", resultadoLab),
                () -> assertEquals("Auditorio", resultadoAuditorium),
                () -> assertEquals("Tipo desconocido", resultadoDefault)
        );
    }

    private String obtenerDescripcionTipo(RoomType roomType) {
        if (roomType == null) {
            return "Tipo desconocido";
        }

        switch (roomType) {
            case REGULAR:
                return "Aula regular";
            case LABORATORY:
                return "Laboratorio general";
            case AUDITORIUM:
                return "Auditorio";
            case COMPUTER_LAB:
                return "Laboratorio de computación";
            case SEMINAR_ROOM:
                return "Sala de seminarios";
            case WORKSHOP_ROOM:
                return "Taller";
            case SPECIALIZED_LAB:
                return "Laboratorio especializado";
            default:
                return "Tipo desconocido";
        }
    }

    @Test
    @DisplayName("Caso compuesto - Categorización por tipo de espacio")
    void testCategorizacionPorTipo_Compuesto() {
        assertAll("Validar categorización de tipos de aula",
                () -> assertTrue(esAulaTeorica(RoomType.REGULAR)),
                () -> assertTrue(esAulaTeorica(RoomType.AUDITORIUM)),
                () -> assertTrue(esAulaTeorica(RoomType.SEMINAR_ROOM)),
                () -> assertTrue(esAulaPractica(RoomType.LABORATORY)),
                () -> assertTrue(esAulaPractica(RoomType.COMPUTER_LAB)),
                () -> assertTrue(esAulaPractica(RoomType.WORKSHOP_ROOM)),
                () -> assertTrue(esAulaPractica(RoomType.SPECIALIZED_LAB))
        );
    }

    private boolean esAulaTeorica(RoomType roomType) {
        return roomType == RoomType.REGULAR ||
                roomType == RoomType.AUDITORIUM ||
                roomType == RoomType.SEMINAR_ROOM;
    }

    private boolean esAulaPractica(RoomType roomType) {
        return roomType == RoomType.LABORATORY ||
                roomType == RoomType.COMPUTER_LAB ||
                roomType == RoomType.WORKSHOP_ROOM ||
                roomType == RoomType.SPECIALIZED_LAB;
    }

    @Test
    @DisplayName("Caso compuesto - Validación completa de tipos de aula")
    void testTiposAulaCompletos_Compuesto() {
        assertAll("Validar que el enum cubre todos los tipos de aula necesarios",
                () -> assertNotNull(RoomType.REGULAR, "Debe existir aula regular"),
                () -> assertNotNull(RoomType.LABORATORY, "Debe existir laboratorio"),
                () -> assertNotNull(RoomType.AUDITORIUM, "Debe existir auditorio"),
                () -> assertNotNull(RoomType.COMPUTER_LAB, "Debe existir laboratorio de computación"),
                () -> assertNotNull(RoomType.SEMINAR_ROOM, "Debe existir sala de seminarios"),
                () -> assertNotNull(RoomType.WORKSHOP_ROOM, "Debe existir taller"),
                () -> assertNotNull(RoomType.SPECIALIZED_LAB, "Debe existir laboratorio especializado")
        );
    }



    private int obtenerCapacidadRelativa(RoomType roomType) {
        switch (roomType) {
            case AUDITORIUM:
                return 200;
            case REGULAR:
                return 40;
            case SEMINAR_ROOM:
                return 20;
            case LABORATORY:
                return 25;
            case COMPUTER_LAB:
                return 30;
            case WORKSHOP_ROOM:
                return 15;
            case SPECIALIZED_LAB:
                return 10;
            default:
                return 0;
        }
    }
}
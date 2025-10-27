package eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CourseStatusTest {

    @Test
    @DisplayName("Caso exitoso - Enum contiene todos los valores esperados")
    void testValoresEnum_Exitoso() {
        CourseStatus[] valores = CourseStatus.values();

        assertAll("Validar que el enum contiene todos los valores definidos",
                () -> assertEquals(6, valores.length),
                () -> assertEquals(CourseStatus.ENROLLED, valores[0]),
                () -> assertEquals(CourseStatus.IN_PROGRESS, valores[1]),
                () -> assertEquals(CourseStatus.PASSED, valores[2]),
                () -> assertEquals(CourseStatus.FAILED, valores[3]),
                () -> assertEquals(CourseStatus.WITHDRAWN, valores[4]),
                () -> assertEquals(CourseStatus.INCOMPLETE, valores[5])
        );
    }

    @Test
    @DisplayName("Caso exitoso - valueOf retorna el enum correcto")
    void testValueOf_Exitoso() {
        assertAll("Validar valueOf para cada valor del enum",
                () -> assertEquals(CourseStatus.ENROLLED, CourseStatus.valueOf("ENROLLED")),
                () -> assertEquals(CourseStatus.IN_PROGRESS, CourseStatus.valueOf("IN_PROGRESS")),
                () -> assertEquals(CourseStatus.PASSED, CourseStatus.valueOf("PASSED")),
                () -> assertEquals(CourseStatus.FAILED, CourseStatus.valueOf("FAILED")),
                () -> assertEquals(CourseStatus.WITHDRAWN, CourseStatus.valueOf("WITHDRAWN")),
                () -> assertEquals(CourseStatus.INCOMPLETE, CourseStatus.valueOf("INCOMPLETE"))
        );
    }

    @Test
    @DisplayName("Caso error - valueOf con nombre inválido lanza excepción")
    void testValueOf_NombreInvalido_Error() {
        assertThrows(IllegalArgumentException.class, () -> {
            CourseStatus.valueOf("NO_EXISTE");
        });
    }

    @Test
    @DisplayName("Caso borde - valueOf es case sensitive")
    void testValueOf_CaseSensitive_Borde() {
        assertThrows(IllegalArgumentException.class, () -> {
            CourseStatus.valueOf("enrolled");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            CourseStatus.valueOf("In_Progress");
        });
    }

    @Test
    @DisplayName("Caso exitoso - name() retorna el nombre correcto")
    void testName_Exitoso() {
        assertAll("Validar name() para cada valor del enum",
                () -> assertEquals("ENROLLED", CourseStatus.ENROLLED.name()),
                () -> assertEquals("IN_PROGRESS", CourseStatus.IN_PROGRESS.name()),
                () -> assertEquals("PASSED", CourseStatus.PASSED.name()),
                () -> assertEquals("FAILED", CourseStatus.FAILED.name()),
                () -> assertEquals("WITHDRAWN", CourseStatus.WITHDRAWN.name()),
                () -> assertEquals("INCOMPLETE", CourseStatus.INCOMPLETE.name())
        );
    }

    @Test
    @DisplayName("Caso exitoso - ordinal() retorna la posición correcta")
    void testOrdinal_Exitoso() {
        assertAll("Validar ordinal() para cada valor del enum",
                () -> assertEquals(0, CourseStatus.ENROLLED.ordinal()),
                () -> assertEquals(1, CourseStatus.IN_PROGRESS.ordinal()),
                () -> assertEquals(2, CourseStatus.PASSED.ordinal()),
                () -> assertEquals(3, CourseStatus.FAILED.ordinal()),
                () -> assertEquals(4, CourseStatus.WITHDRAWN.ordinal()),
                () -> assertEquals(5, CourseStatus.INCOMPLETE.ordinal())
        );
    }

    @Test
    @DisplayName("Caso exitoso - toString() retorna el nombre del enum")
    void testToString_Exitoso() {
        assertAll("Validar toString() para cada valor del enum",
                () -> assertEquals("ENROLLED", CourseStatus.ENROLLED.toString()),
                () -> assertEquals("IN_PROGRESS", CourseStatus.IN_PROGRESS.toString()),
                () -> assertEquals("PASSED", CourseStatus.PASSED.toString()),
                () -> assertEquals("FAILED", CourseStatus.FAILED.toString()),
                () -> assertEquals("WITHDRAWN", CourseStatus.WITHDRAWN.toString()),
                () -> assertEquals("INCOMPLETE", CourseStatus.INCOMPLETE.toString())
        );
    }

    @Test
    @DisplayName("Caso compuesto - Comparación entre valores del enum")
    void testComparacionEnums_Compuesto() {
        assertAll("Validar comparaciones entre enums",
                () -> assertTrue(CourseStatus.ENROLLED.compareTo(CourseStatus.IN_PROGRESS) < 0),
                () -> assertTrue(CourseStatus.PASSED.compareTo(CourseStatus.FAILED) < 0),
                () -> assertTrue(CourseStatus.WITHDRAWN.compareTo(CourseStatus.INCOMPLETE) < 0),
                () -> assertEquals(0, CourseStatus.ENROLLED.compareTo(CourseStatus.ENROLLED))
        );
    }

    @Test
    @DisplayName("Caso exitoso - equals() funciona correctamente")
    void testEquals_Exitoso() {
        assertAll("Validar equals() para el enum",
                () -> assertEquals(CourseStatus.ENROLLED, CourseStatus.ENROLLED),
                () -> assertEquals(CourseStatus.PASSED, CourseStatus.PASSED),
                () -> assertNotEquals(CourseStatus.ENROLLED, CourseStatus.PASSED),
                () -> assertNotEquals(CourseStatus.IN_PROGRESS, CourseStatus.FAILED)
        );
    }

    @Test
    @DisplayName("Caso borde - equals() con null y otros objetos")
    void testEquals_CasosBorde_Borde() {
        assertAll("Validar equals() con casos borde",
                () -> assertNotEquals(null, CourseStatus.ENROLLED),
                () -> assertNotEquals("ENROLLED", CourseStatus.ENROLLED),
                () -> assertNotEquals(0, CourseStatus.ENROLLED)
        );
    }

    @Test
    @DisplayName("Caso exitoso - hashCode() es consistente")
    void testHashCode_Exitoso() {
        assertAll("Validar hashCode() para el enum",
                () -> assertEquals(CourseStatus.ENROLLED.hashCode(), CourseStatus.ENROLLED.hashCode()),
                () -> assertEquals(CourseStatus.PASSED.hashCode(), CourseStatus.PASSED.hashCode()),
                () -> assertNotEquals(CourseStatus.ENROLLED.hashCode(), CourseStatus.PASSED.hashCode())
        );
    }

    @Test
    @DisplayName("Caso compuesto - Uso en estructuras de datos")
    void testUsoEnEstructurasDatos_Compuesto() {
        java.util.Set<CourseStatus> set = java.util.EnumSet.allOf(CourseStatus.class);
        java.util.Map<CourseStatus, String> map = new java.util.EnumMap<>(CourseStatus.class);

        map.put(CourseStatus.ENROLLED, "Matriculado");
        map.put(CourseStatus.PASSED, "Aprobado");

        assertAll("Validar uso en estructuras de datos",
                () -> assertEquals(6, set.size()),
                () -> assertTrue(set.contains(CourseStatus.ENROLLED)),
                () -> assertTrue(set.contains(CourseStatus.IN_PROGRESS)),
                () -> assertEquals("Matriculado", map.get(CourseStatus.ENROLLED)),
                () -> assertEquals("Aprobado", map.get(CourseStatus.PASSED))
        );
    }

    @Test
    @DisplayName("Caso borde - Switch statement con enum")
    void testSwitchStatement_Borde() {
        String resultadoEnrolled = obtenerDescripcionEstado(CourseStatus.ENROLLED);
        String resultadoPassed = obtenerDescripcionEstado(CourseStatus.PASSED);
        String resultadoFailed = obtenerDescripcionEstado(CourseStatus.FAILED);
        String resultadoDefault = obtenerDescripcionEstado(null);

        assertAll("Validar switch statement con enum",
                () -> assertEquals("Curso matriculado", resultadoEnrolled),
                () -> assertEquals("Curso aprobado", resultadoPassed),
                () -> assertEquals("Curso reprobado", resultadoFailed),
                () -> assertEquals("Estado desconocido", resultadoDefault)
        );
    }

    private String obtenerDescripcionEstado(CourseStatus status) {
        if (status == null) {
            return "Estado desconocido";
        }

        switch (status) {
            case ENROLLED:
                return "Curso matriculado";
            case IN_PROGRESS:
                return "Curso en progreso";
            case PASSED:
                return "Curso aprobado";
            case FAILED:
                return "Curso reprobado";
            case WITHDRAWN:
                return "Curso retirado";
            case INCOMPLETE:
                return "Curso incompleto";
            default:
                return "Estado desconocido";
        }
    }

    @Test
    @DisplayName("Caso compuesto - Validación completa del ciclo de vida del curso")
    void testCicloDeVidaCurso_Compuesto() {
        assertAll("Validar que el enum cubre todo el ciclo de vida de un curso",
                () -> assertNotNull(CourseStatus.ENROLLED, "Debe existir estado de matriculado"),
                () -> assertNotNull(CourseStatus.IN_PROGRESS, "Debe existir estado en progreso"),
                () -> assertNotNull(CourseStatus.PASSED, "Debe existir estado aprobado"),
                () -> assertNotNull(CourseStatus.FAILED, "Debe existir estado reprobado"),
                () -> assertNotNull(CourseStatus.WITHDRAWN, "Debe existir estado retirado"),
                () -> assertNotNull(CourseStatus.INCOMPLETE, "Debe existir estado incompleto")
        );
    }
}
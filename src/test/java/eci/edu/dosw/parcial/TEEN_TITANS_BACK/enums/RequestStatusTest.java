package eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RequestStatusTest {

    @Test
    @DisplayName("Caso exitoso - Enum contiene todos los valores esperados")
    void testValoresEnum_Exitoso() {
        RequestStatus[] valores = RequestStatus.values();

        assertAll("Validar que el enum contiene todos los valores definidos",
                () -> assertEquals(5, valores.length),
                () -> assertEquals(RequestStatus.PENDING, valores[0]),
                () -> assertEquals(RequestStatus.UNDER_REVIEW, valores[1]),
                () -> assertEquals(RequestStatus.APPROVED, valores[2]),
                () -> assertEquals(RequestStatus.REJECTED, valores[3]),
                () -> assertEquals(RequestStatus.CANCELLED, valores[4])
        );
    }

    @Test
    @DisplayName("Caso exitoso - valueOf retorna el enum correcto")
    void testValueOf_Exitoso() {
        assertAll("Validar valueOf para cada valor del enum",
                () -> assertEquals(RequestStatus.PENDING, RequestStatus.valueOf("PENDING")),
                () -> assertEquals(RequestStatus.UNDER_REVIEW, RequestStatus.valueOf("UNDER_REVIEW")),
                () -> assertEquals(RequestStatus.APPROVED, RequestStatus.valueOf("APPROVED")),
                () -> assertEquals(RequestStatus.REJECTED, RequestStatus.valueOf("REJECTED")),
                () -> assertEquals(RequestStatus.CANCELLED, RequestStatus.valueOf("CANCELLED"))
        );
    }

    @Test
    @DisplayName("Caso error - valueOf con nombre inválido lanza excepción")
    void testValueOf_NombreInvalido_Error() {
        assertThrows(IllegalArgumentException.class, () -> {
            RequestStatus.valueOf("NO_EXISTE");
        });
    }

    @Test
    @DisplayName("Caso borde - valueOf es case sensitive")
    void testValueOf_CaseSensitive_Borde() {
        assertThrows(IllegalArgumentException.class, () -> {
            RequestStatus.valueOf("pending");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            RequestStatus.valueOf("Under_Review");
        });
    }

    @Test
    @DisplayName("Caso exitoso - name() retorna el nombre correcto")
    void testName_Exitoso() {
        assertAll("Validar name() para cada valor del enum",
                () -> assertEquals("PENDING", RequestStatus.PENDING.name()),
                () -> assertEquals("UNDER_REVIEW", RequestStatus.UNDER_REVIEW.name()),
                () -> assertEquals("APPROVED", RequestStatus.APPROVED.name()),
                () -> assertEquals("REJECTED", RequestStatus.REJECTED.name()),
                () -> assertEquals("CANCELLED", RequestStatus.CANCELLED.name())
        );
    }

    @Test
    @DisplayName("Caso exitoso - ordinal() retorna la posición correcta")
    void testOrdinal_Exitoso() {
        assertAll("Validar ordinal() para cada valor del enum",
                () -> assertEquals(0, RequestStatus.PENDING.ordinal()),
                () -> assertEquals(1, RequestStatus.UNDER_REVIEW.ordinal()),
                () -> assertEquals(2, RequestStatus.APPROVED.ordinal()),
                () -> assertEquals(3, RequestStatus.REJECTED.ordinal()),
                () -> assertEquals(4, RequestStatus.CANCELLED.ordinal())
        );
    }

    @Test
    @DisplayName("Caso exitoso - toString() retorna el nombre del enum")
    void testToString_Exitoso() {
        assertAll("Validar toString() para cada valor del enum",
                () -> assertEquals("PENDING", RequestStatus.PENDING.toString()),
                () -> assertEquals("UNDER_REVIEW", RequestStatus.UNDER_REVIEW.toString()),
                () -> assertEquals("APPROVED", RequestStatus.APPROVED.toString()),
                () -> assertEquals("REJECTED", RequestStatus.REJECTED.toString()),
                () -> assertEquals("CANCELLED", RequestStatus.CANCELLED.toString())
        );
    }

    @Test
    @DisplayName("Caso compuesto - Comparación entre valores del enum")
    void testComparacionEnums_Compuesto() {
        assertAll("Validar comparaciones entre enums",
                () -> assertTrue(RequestStatus.PENDING.compareTo(RequestStatus.UNDER_REVIEW) < 0),
                () -> assertTrue(RequestStatus.UNDER_REVIEW.compareTo(RequestStatus.APPROVED) < 0),
                () -> assertTrue(RequestStatus.APPROVED.compareTo(RequestStatus.REJECTED) < 0),
                () -> assertTrue(RequestStatus.REJECTED.compareTo(RequestStatus.CANCELLED) < 0),
                () -> assertEquals(0, RequestStatus.PENDING.compareTo(RequestStatus.PENDING))
        );
    }

    @Test
    @DisplayName("Caso exitoso - equals() funciona correctamente")
    void testEquals_Exitoso() {
        assertAll("Validar equals() para el enum",
                () -> assertEquals(RequestStatus.PENDING, RequestStatus.PENDING),
                () -> assertEquals(RequestStatus.APPROVED, RequestStatus.APPROVED),
                () -> assertNotEquals(RequestStatus.PENDING, RequestStatus.APPROVED),
                () -> assertNotEquals(RequestStatus.UNDER_REVIEW, RequestStatus.REJECTED)
        );
    }

    @Test
    @DisplayName("Caso borde - equals() con null y otros objetos")
    void testEquals_CasosBorde_Borde() {
        assertAll("Validar equals() con casos borde",
                () -> assertNotEquals(null, RequestStatus.PENDING),
                () -> assertNotEquals("PENDING", RequestStatus.PENDING),
                () -> assertNotEquals(0, RequestStatus.PENDING)
        );
    }

    @Test
    @DisplayName("Caso exitoso - hashCode() es consistente")
    void testHashCode_Exitoso() {
        assertAll("Validar hashCode() para el enum",
                () -> assertEquals(RequestStatus.PENDING.hashCode(), RequestStatus.PENDING.hashCode()),
                () -> assertEquals(RequestStatus.APPROVED.hashCode(), RequestStatus.APPROVED.hashCode()),
                () -> assertNotEquals(RequestStatus.PENDING.hashCode(), RequestStatus.APPROVED.hashCode())
        );
    }

    @Test
    @DisplayName("Caso compuesto - Uso en estructuras de datos")
    void testUsoEnEstructurasDatos_Compuesto() {
        java.util.Set<RequestStatus> set = java.util.EnumSet.allOf(RequestStatus.class);
        java.util.Map<RequestStatus, String> map = new java.util.EnumMap<>(RequestStatus.class);

        map.put(RequestStatus.PENDING, "Pendiente");
        map.put(RequestStatus.APPROVED, "Aprobado");

        assertAll("Validar uso en estructuras de datos",
                () -> assertEquals(5, set.size()),
                () -> assertTrue(set.contains(RequestStatus.PENDING)),
                () -> assertTrue(set.contains(RequestStatus.UNDER_REVIEW)),
                () -> assertEquals("Pendiente", map.get(RequestStatus.PENDING)),
                () -> assertEquals("Aprobado", map.get(RequestStatus.APPROVED))
        );
    }

    @Test
    @DisplayName("Caso borde - Switch statement con enum")
    void testSwitchStatement_Borde() {
        String resultadoPending = obtenerDescripcionEstado(RequestStatus.PENDING);
        String resultadoApproved = obtenerDescripcionEstado(RequestStatus.APPROVED);
        String resultadoRejected = obtenerDescripcionEstado(RequestStatus.REJECTED);
        String resultadoDefault = obtenerDescripcionEstado(null);

        assertAll("Validar switch statement con enum",
                () -> assertEquals("Solicitud pendiente", resultadoPending),
                () -> assertEquals("Solicitud aprobada", resultadoApproved),
                () -> assertEquals("Solicitud rechazada", resultadoRejected),
                () -> assertEquals("Estado desconocido", resultadoDefault)
        );
    }

    private String obtenerDescripcionEstado(RequestStatus status) {
        if (status == null) {
            return "Estado desconocido";
        }

        switch (status) {
            case PENDING:
                return "Solicitud pendiente";
            case UNDER_REVIEW:
                return "Solicitud en revisión";
            case APPROVED:
                return "Solicitud aprobada";
            case REJECTED:
                return "Solicitud rechazada";
            case CANCELLED:
                return "Solicitud cancelada";
            default:
                return "Estado desconocido";
        }
    }

    @Test
    @DisplayName("Caso compuesto - Estados finales vs estados transitorios")
    void testEstadosFinalesVsTransitorios_Compuesto() {
        assertAll("Validar estados finales y transitorios",
                () -> assertTrue(esEstadoFinal(RequestStatus.APPROVED)),
                () -> assertTrue(esEstadoFinal(RequestStatus.REJECTED)),
                () -> assertTrue(esEstadoFinal(RequestStatus.CANCELLED)),
                () -> assertFalse(esEstadoFinal(RequestStatus.PENDING)),
                () -> assertFalse(esEstadoFinal(RequestStatus.UNDER_REVIEW))
        );
    }

    private boolean esEstadoFinal(RequestStatus status) {
        return status == RequestStatus.APPROVED ||
                status == RequestStatus.REJECTED ||
                status == RequestStatus.CANCELLED;
    }

    @Test
    @DisplayName("Caso compuesto - Validación completa del ciclo de vida de solicitud")
    void testCicloDeVidaSolicitud_Compuesto() {
        assertAll("Validar que el enum cubre todo el ciclo de vida de una solicitud",
                () -> assertNotNull(RequestStatus.PENDING, "Debe existir estado pendiente"),
                () -> assertNotNull(RequestStatus.UNDER_REVIEW, "Debe existir estado en revisión"),
                () -> assertNotNull(RequestStatus.APPROVED, "Debe existir estado aprobado"),
                () -> assertNotNull(RequestStatus.REJECTED, "Debe existir estado rechazado"),
                () -> assertNotNull(RequestStatus.CANCELLED, "Debe existir estado cancelado")
        );
    }

    @Test
    @DisplayName("Caso borde - Orden lógico del ciclo de vida")
    void testOrdenCicloDeVida_Borde() {
        assertAll("Validar orden lógico del ciclo de vida",
                () -> assertTrue(RequestStatus.PENDING.ordinal() < RequestStatus.UNDER_REVIEW.ordinal()),
                () -> assertTrue(RequestStatus.UNDER_REVIEW.ordinal() < RequestStatus.APPROVED.ordinal()),
                () -> assertTrue(RequestStatus.UNDER_REVIEW.ordinal() < RequestStatus.REJECTED.ordinal())
        );
    }
}
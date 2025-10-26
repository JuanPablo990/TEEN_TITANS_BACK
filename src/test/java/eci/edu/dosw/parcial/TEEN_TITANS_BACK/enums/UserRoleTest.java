package eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserRoleTest {

    @Test
    @DisplayName("Caso exitoso - Valores del enum existen")
    void testEnumValues_Existen() {
        assertAll("Verificar que todos los valores del enum existen",
                () -> assertNotNull(UserRole.STUDENT),
                () -> assertNotNull(UserRole.PROFESSOR),
                () -> assertNotNull(UserRole.ADMINISTRATOR),
                () -> assertNotNull(UserRole.DEAN)
        );
    }

    @Test
    @DisplayName("Caso exitoso - Valores del enum tienen nombres correctos")
    void testEnumValues_NombresCorrectos() {
        assertAll("Verificar nombres de los valores del enum",
                () -> assertEquals("STUDENT", UserRole.STUDENT.name()),
                () -> assertEquals("PROFESSOR", UserRole.PROFESSOR.name()),
                () -> assertEquals("ADMINISTRATOR", UserRole.ADMINISTRATOR.name()),
                () -> assertEquals("DEAN", UserRole.DEAN.name())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Orden de los valores del enum")
    void testEnumValues_Orden() {
        UserRole[] values = UserRole.values();

        assertAll("Verificar orden de los valores del enum",
                () -> assertEquals(4, values.length),
                () -> assertEquals(UserRole.STUDENT, values[0]),
                () -> assertEquals(UserRole.PROFESSOR, values[1]),
                () -> assertEquals(UserRole.ADMINISTRATOR, values[2]),
                () -> assertEquals(UserRole.DEAN, values[3])
        );
    }

    @Test
    @DisplayName("Caso exitoso - ValueOf funciona correctamente")
    void testValueOf_Exitoso() {
        assertAll("Verificar valueOf para cada valor del enum",
                () -> assertEquals(UserRole.STUDENT, UserRole.valueOf("STUDENT")),
                () -> assertEquals(UserRole.PROFESSOR, UserRole.valueOf("PROFESSOR")),
                () -> assertEquals(UserRole.ADMINISTRATOR, UserRole.valueOf("ADMINISTRATOR")),
                () -> assertEquals(UserRole.DEAN, UserRole.valueOf("DEAN"))
        );
    }

    @Test
    @DisplayName("Caso error - ValueOf con nombre inválido")
    void testValueOf_NombreInvalido() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> UserRole.valueOf("INVALID_ROLE"));

        assertEquals("No enum constant eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole.INVALID_ROLE",
                exception.getMessage());
    }



    @Test
    @DisplayName("Caso borde - ValueOf case sensitive")
    void testValueOf_CaseSensitive() {
        assertThrows(IllegalArgumentException.class,
                () -> UserRole.valueOf("student"));

        assertThrows(IllegalArgumentException.class,
                () -> UserRole.valueOf("Professor"));

        assertThrows(IllegalArgumentException.class,
                () -> UserRole.valueOf("administrator"));

        assertThrows(IllegalArgumentException.class,
                () -> UserRole.valueOf("dean"));
    }

    @Test
    @DisplayName("Caso exitoso - Ordinal de cada valor")
    void testOrdinal_Valores() {
        assertAll("Verificar ordinal de cada valor del enum",
                () -> assertEquals(0, UserRole.STUDENT.ordinal()),
                () -> assertEquals(1, UserRole.PROFESSOR.ordinal()),
                () -> assertEquals(2, UserRole.ADMINISTRATOR.ordinal()),
                () -> assertEquals(3, UserRole.DEAN.ordinal())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Comparación de valores del enum")
    void testComparacion_Valores() {
        assertAll("Verificar comparación de valores del enum",
                () -> assertTrue(UserRole.STUDENT.compareTo(UserRole.PROFESSOR) < 0),
                () -> assertTrue(UserRole.PROFESSOR.compareTo(UserRole.ADMINISTRATOR) < 0),
                () -> assertTrue(UserRole.ADMINISTRATOR.compareTo(UserRole.DEAN) < 0),
                () -> assertEquals(0, UserRole.STUDENT.compareTo(UserRole.STUDENT))
        );
    }

    @Test
    @DisplayName("Caso exitoso - Igualdad de valores del enum")
    void testIgualdad_Valores() {
        assertAll("Verificar igualdad de valores del enum",
                () -> assertEquals(UserRole.STUDENT, UserRole.STUDENT),
                () -> assertNotEquals(UserRole.STUDENT, UserRole.PROFESSOR),
                () -> assertNotEquals(UserRole.PROFESSOR, UserRole.ADMINISTRATOR),
                () -> assertNotEquals(UserRole.ADMINISTRATOR, UserRole.DEAN)
        );
    }

    @Test
    @DisplayName("Caso exitoso - ToString de valores del enum")
    void testToString_Valores() {
        assertAll("Verificar toString de valores del enum",
                () -> assertEquals("STUDENT", UserRole.STUDENT.toString()),
                () -> assertEquals("PROFESSOR", UserRole.PROFESSOR.toString()),
                () -> assertEquals("ADMINISTRATOR", UserRole.ADMINISTRATOR.toString()),
                () -> assertEquals("DEAN", UserRole.DEAN.toString())
        );
    }

    @Test
    @DisplayName("Caso borde - Valores no son null")
    void testValores_NoNull() {
        UserRole[] values = UserRole.values();

        for (UserRole role : values) {
            assertNotNull(role, "El valor del enum no debe ser null");
        }
    }

    @Test
    @DisplayName("Caso borde - Valores únicos")
    void testValores_Unicos() {
        UserRole[] values = UserRole.values();

        for (int i = 0; i < values.length; i++) {
            for (int j = i + 1; j < values.length; j++) {
                assertNotEquals(values[i], values[j],
                        "Los valores del enum deben ser únicos");
            }
        }
    }

    @Test
    @DisplayName("Caso borde - HashCode consistente")
    void testHashCode_Consistente() {
        assertAll("Verificar hashCode consistente",
                () -> assertEquals(UserRole.STUDENT.hashCode(), UserRole.STUDENT.hashCode()),
                () -> assertEquals(UserRole.PROFESSOR.hashCode(), UserRole.PROFESSOR.hashCode()),
                () -> assertEquals(UserRole.ADMINISTRATOR.hashCode(), UserRole.ADMINISTRATOR.hashCode()),
                () -> assertEquals(UserRole.DEAN.hashCode(), UserRole.DEAN.hashCode())
        );
    }

    @Test
    @DisplayName("Caso borde - HashCode diferente para valores diferentes")
    void testHashCode_Diferente() {
        assertAll("Verificar hashCode diferente para valores diferentes",
                () -> assertNotEquals(UserRole.STUDENT.hashCode(), UserRole.PROFESSOR.hashCode()),
                () -> assertNotEquals(UserRole.PROFESSOR.hashCode(), UserRole.ADMINISTRATOR.hashCode()),
                () -> assertNotEquals(UserRole.ADMINISTRATOR.hashCode(), UserRole.DEAN.hashCode())
        );
    }

    @Test
    @DisplayName("Caso borde - Uso en estructuras de datos")
    void testUso_EnEstructurasDatos() {
        assertAll("Verificar uso en estructuras de datos",
                () -> assertTrue(java.util.EnumSet.allOf(UserRole.class).contains(UserRole.STUDENT)),
                () -> assertTrue(java.util.EnumSet.allOf(UserRole.class).contains(UserRole.PROFESSOR)),
                () -> assertTrue(java.util.EnumSet.allOf(UserRole.class).contains(UserRole.ADMINISTRATOR)),
                () -> assertTrue(java.util.EnumSet.allOf(UserRole.class).contains(UserRole.DEAN))
        );
    }

    @Test
    @DisplayName("Caso borde - Uso en switch statements")
    void testUso_EnSwitch() {
        String result = switch (UserRole.STUDENT) {
            case STUDENT -> "Estudiante";
            case PROFESSOR -> "Profesor";
            case ADMINISTRATOR -> "Administrador";
            case DEAN -> "Decano";
        };

        assertEquals("Estudiante", result);
    }

    @Test
    @DisplayName("Caso borde - Serialización y deserialización")
    void testSerializacion_Deserializacion() {
        String studentJson = "\"STUDENT\"";
        String professorJson = "\"PROFESSOR\"";
        String administratorJson = "\"ADMINISTRATOR\"";
        String deanJson = "\"DEAN\"";

        assertAll("Verificar serialización y deserialización",
                () -> assertEquals(UserRole.STUDENT, UserRole.valueOf(studentJson.replace("\"", ""))),
                () -> assertEquals(UserRole.PROFESSOR, UserRole.valueOf(professorJson.replace("\"", ""))),
                () -> assertEquals(UserRole.ADMINISTRATOR, UserRole.valueOf(administratorJson.replace("\"", ""))),
                () -> assertEquals(UserRole.DEAN, UserRole.valueOf(deanJson.replace("\"", "")))
        );
    }

    @Test
    @DisplayName("Caso borde - Valores en mapas")
    void testUso_EnMapas() {
        java.util.Map<UserRole, String> roleDescriptions = new java.util.HashMap<>();
        roleDescriptions.put(UserRole.STUDENT, "Usuario estudiante");
        roleDescriptions.put(UserRole.PROFESSOR, "Usuario profesor");
        roleDescriptions.put(UserRole.ADMINISTRATOR, "Usuario administrador");
        roleDescriptions.put(UserRole.DEAN, "Usuario decano");

        assertAll("Verificar uso en mapas",
                () -> assertEquals("Usuario estudiante", roleDescriptions.get(UserRole.STUDENT)),
                () -> assertEquals("Usuario profesor", roleDescriptions.get(UserRole.PROFESSOR)),
                () -> assertEquals("Usuario administrador", roleDescriptions.get(UserRole.ADMINISTRATOR)),
                () -> assertEquals("Usuario decano", roleDescriptions.get(UserRole.DEAN))
        );
    }

    @Test
    @DisplayName("Caso borde - Iteración sobre valores")
    void testIteracion_Valores() {
        int count = 0;
        for (UserRole role : UserRole.values()) {
            assertNotNull(role);
            count++;
        }
        assertEquals(4, count);
    }
}
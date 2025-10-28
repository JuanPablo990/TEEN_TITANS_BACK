package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() {
        user1 = new User("1", "Bruce Wayne", "bruce.wayne@titans.edu", "password123", UserRole.ADMINISTRATOR);
        user2 = new User("2", "Clark Kent", "clark.kent@titans.edu", "password456", UserRole.PROFESSOR);
        user3 = new User("1", "Bruce Wayne", "bruce.wayne@titans.edu", "password123", UserRole.ADMINISTRATOR);
    }

    @Test
    @DisplayName("Caso exitoso - Constructor inicializa correctamente los campos")
    void testConstructor_Exitoso() {
        assertAll("Verificar inicialización del constructor",
                () -> assertEquals("1", user1.getId()),
                () -> assertEquals("Bruce Wayne", user1.getName()),
                () -> assertEquals("bruce.wayne@titans.edu", user1.getEmail()),
                () -> assertEquals("password123", user1.getPassword()),
                () -> assertEquals(UserRole.ADMINISTRATOR, user1.getRole()),
                () -> assertTrue(user1.isActive()),
                () -> assertNotNull(user1.getCreatedAt()),
                () -> assertNotNull(user1.getUpdatedAt())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Constructor con todos los argumentos")
    void testConstructor_AllArgsConstructor() {
        Date createdAt = new Date();
        Date updatedAt = new Date();
        User user = new User("10", "Diana Prince", "diana.prince@titans.edu", "password789",
                UserRole.STUDENT, false, createdAt, updatedAt);

        assertAll("Verificar constructor con todos los argumentos",
                () -> assertEquals("10", user.getId()),
                () -> assertEquals("Diana Prince", user.getName()),
                () -> assertEquals("diana.prince@titans.edu", user.getEmail()),
                () -> assertEquals("password789", user.getPassword()),
                () -> assertEquals(UserRole.STUDENT, user.getRole()),
                () -> assertFalse(user.isActive()),
                () -> assertEquals(createdAt, user.getCreatedAt()),
                () -> assertEquals(updatedAt, user.getUpdatedAt())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Constructor asigna valores por defecto")
    void testConstructor_ValoresPorDefecto() {
        User user = new User("3", "Barry Allen", "barry.allen@titans.edu", "password999", UserRole.DEAN);

        assertAll("Verificar valores por defecto",
                () -> assertEquals("3", user.getId()),
                () -> assertEquals("Barry Allen", user.getName()),
                () -> assertTrue(user.isActive()),
                () -> assertNotNull(user.getCreatedAt()),
                () -> assertNotNull(user.getUpdatedAt())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Getters y Setters funcionan correctamente")
    void testGettersAndSetters_Exitoso() {
        User user = new User();
        Date newCreatedAt = new Date();
        Date newUpdatedAt = new Date();

        user.setId("10");
        user.setName("Hal Jordan");
        user.setEmail("hal.jordan@titans.edu");
        user.setPassword("newpassword");
        user.setRole(UserRole.PROFESSOR);
        user.setActive(false);
        user.setCreatedAt(newCreatedAt);
        user.setUpdatedAt(newUpdatedAt);

        assertAll("Verificar getters y setters",
                () -> assertEquals("10", user.getId()),
                () -> assertEquals("Hal Jordan", user.getName()),
                () -> assertEquals("hal.jordan@titans.edu", user.getEmail()),
                () -> assertEquals("newpassword", user.getPassword()),
                () -> assertEquals(UserRole.PROFESSOR, user.getRole()),
                () -> assertFalse(user.isActive()),
                () -> assertEquals(newCreatedAt, user.getCreatedAt()),
                () -> assertEquals(newUpdatedAt, user.getUpdatedAt())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Equals retorna true para objetos iguales")
    void testEquals_ObjetosIguales() {
        assertEquals(user1, user3);
    }

    @Test
    @DisplayName("Caso exitoso - Equals retorna false para objetos diferentes")
    void testEquals_ObjetosDiferentes() {
        assertNotEquals(user1, user2);
    }

    @Test
    @DisplayName("Caso exitoso - HashCode es consistente para objetos iguales")
    void testHashCode_Consistente() {
        assertEquals(user1.hashCode(), user3.hashCode());
    }

    @Test
    @DisplayName("Caso exitoso - HashCode es diferente para objetos distintos")
    void testHashCode_Diferente() {
        assertNotEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    @DisplayName("Caso exitoso - ToString contiene información relevante")
    void testToString_ContieneInformacionRelevante() {
        String toStringResult = user1.toString();

        assertAll("Verificar contenido del toString",
                () -> assertTrue(toStringResult.contains("Bruce Wayne")),
                () -> assertTrue(toStringResult.contains("bruce.wayne@titans.edu")),
                () -> assertTrue(toStringResult.contains("ADMINISTRATOR")),
                () -> assertTrue(toStringResult.contains("active=true"))
        );
    }

    @Test
    @DisplayName("Caso borde - Constructor con null en campos obligatorios")
    void testConstructor_CamposNull() {
        User user = new User(null, null, null, null, null);

        assertAll("Verificar constructor con campos null",
                () -> assertNull(user.getId()),
                () -> assertNull(user.getName()),
                () -> assertNull(user.getEmail()),
                () -> assertNull(user.getPassword()),
                () -> assertNull(user.getRole()),
                () -> assertTrue(user.isActive()),
                () -> assertNotNull(user.getCreatedAt()),
                () -> assertNotNull(user.getUpdatedAt())
        );
    }

    @Test
    @DisplayName("Caso borde - Set campos a null")
    void testSetCampos_Null() {
        user1.setName(null);
        user1.setEmail(null);
        user1.setPassword(null);
        user1.setRole(null);
        user1.setCreatedAt(null);
        user1.setUpdatedAt(null);

        assertAll("Verificar set a null",
                () -> assertNull(user1.getName()),
                () -> assertNull(user1.getEmail()),
                () -> assertNull(user1.getPassword()),
                () -> assertNull(user1.getRole()),
                () -> assertNull(user1.getCreatedAt()),
                () -> assertNull(user1.getUpdatedAt())
        );
    }

    @Test
    @DisplayName("Caso borde - Set campos vacíos")
    void testSetCampos_Vacios() {
        user1.setName("");
        user1.setEmail("");
        user1.setPassword("");

        assertAll("Verificar set vacíos",
                () -> assertEquals("", user1.getName()),
                () -> assertEquals("", user1.getEmail()),
                () -> assertEquals("", user1.getPassword())
        );
    }

    @Test
    @DisplayName("Caso borde - Active con valor false")
    void testActive_False() {
        user1.setActive(false);
        assertFalse(user1.isActive());
    }

    @Test
    @DisplayName("Caso borde - Active con valor true después de false")
    void testActive_TrueDespuesFalse() {
        user1.setActive(false);
        assertFalse(user1.isActive());

        user1.setActive(true);
        assertTrue(user1.isActive());
    }

    @Test
    @DisplayName("Caso borde - Comparación con null")
    void testEquals_ConNull() {
        assertNotEquals(null, user1);
    }

    @Test
    @DisplayName("Caso borde - Comparación con objeto de diferente clase")
    void testEquals_DiferenteClase() {
        Object otroObjeto = new Object();
        assertNotEquals(user1, otroObjeto);
    }

    @Test
    @DisplayName("Caso borde - Constructor con valores límite")
    void testConstructor_ValoresLimite() {
        User user = new User("", "", "", "", UserRole.STUDENT);

        assertAll("Verificar constructor con valores límite",
                () -> assertEquals("", user.getId()),
                () -> assertEquals("", user.getName()),
                () -> assertEquals("", user.getEmail()),
                () -> assertEquals("", user.getPassword()),
                () -> assertEquals(UserRole.STUDENT, user.getRole())
        );
    }

    @Test
    @DisplayName("Caso borde - Name con espacios en blanco")
    void testName_ConEspacios() {
        user1.setName("  Bruce Wayne  ");
        assertEquals("  Bruce Wayne  ", user1.getName());
    }

    @Test
    @DisplayName("Caso borde - Email con formato complejo")
    void testEmail_FormatoComplejo() {
        user1.setEmail("bruce.wayne+jr@engineering.titans.edu");
        assertEquals("bruce.wayne+jr@engineering.titans.edu", user1.getEmail());
    }

    @Test
    @DisplayName("Caso borde - Password con caracteres especiales")
    void testPassword_CaracteresEspeciales() {
        user1.setPassword("P@ssw0rd!123#");
        assertEquals("P@ssw0rd!123#", user1.getPassword());
    }

    @Test
    @DisplayName("Caso borde - Todos los roles de UserRole")
    void testUserRole_TodosLosRoles() {
        user1.setRole(UserRole.STUDENT);
        assertEquals(UserRole.STUDENT, user1.getRole());

        user1.setRole(UserRole.PROFESSOR);
        assertEquals(UserRole.PROFESSOR, user1.getRole());

        user1.setRole(UserRole.ADMINISTRATOR);
        assertEquals(UserRole.ADMINISTRATOR, user1.getRole());

        user1.setRole(UserRole.DEAN);
        assertEquals(UserRole.DEAN, user1.getRole());
    }

    @Test
    @DisplayName("Caso borde - CreatedAt y UpdatedAt con fechas diferentes")
    void testFechas_Diferentes() {
        Date oldDate = new Date(System.currentTimeMillis() - 1000000);
        Date newDate = new Date();

        user1.setCreatedAt(oldDate);
        user1.setUpdatedAt(newDate);

        assertAll("Verificar fechas diferentes",
                () -> assertEquals(oldDate, user1.getCreatedAt()),
                () -> assertEquals(newDate, user1.getUpdatedAt()),
                () -> assertNotEquals(user1.getCreatedAt(), user1.getUpdatedAt())
        );
    }

    @Test
    @DisplayName("Caso borde - CreatedAt y UpdatedAt con la misma fecha")
    void testFechas_Iguales() {
        Date sameDate = new Date();

        user1.setCreatedAt(sameDate);
        user1.setUpdatedAt(sameDate);

        assertEquals(user1.getCreatedAt(), user1.getUpdatedAt());
    }

    @Test
    @DisplayName("Caso borde - User sin ID")
    void testUser_SinId() {
        User user = new User();
        assertNull(user.getId());

        user.setId("new-id");
        assertEquals("new-id", user.getId());
    }

    @Test
    @DisplayName("Caso borde - User con ID muy largo")
    void testUser_IdMuyLargo() {
        String longId = "id-muy-largo-que-supera-los-50-caracteres-de-longitud-establecida";
        user1.setId(longId);
        assertEquals(longId, user1.getId());
    }

    @Test
    @DisplayName("Caso borde - User con email muy largo")
    void testUser_EmailMuyLargo() {
        String longEmail = "usuario.con.email.muy.largo.que.supera.los.caracteres.normales@dominio.titans.edu";
        user1.setEmail(longEmail);
        assertEquals(longEmail, user1.getEmail());
    }

    @Test
    @DisplayName("Caso borde - User con password muy largo")
    void testUser_PasswordMuyLargo() {
        String longPassword = "password-muy-largo-que-supera-los-100-caracteres-de-longitud-" +
                "establecida-para-las-contraseñas-en-el-sistema-universitario";
        user1.setPassword(longPassword);
        assertEquals(longPassword, user1.getPassword());
    }

    @Test
    @DisplayName("Caso borde - User con name muy largo")
    void testUser_NameMuyLargo() {
        String longName = "Dr. Bruce Wayne de la Institución Educativa Teen Titans Backend " +
                "con un nombre extremadamente largo para testing";
        user1.setName(longName);
        assertEquals(longName, user1.getName());
    }

    @Test
    @DisplayName("Caso borde - User recién creado tiene fechas actuales")
    void testUser_FechasActuales() {
        User user = new User("100", "Test User", "test@titans.edu", "testpass", UserRole.STUDENT);
        Date now = new Date();

        assertAll("Verificar fechas actuales",
                () -> assertNotNull(user.getCreatedAt()),
                () -> assertNotNull(user.getUpdatedAt()),
                () -> assertTrue(user.getCreatedAt().getTime() <= now.getTime()),
                () -> assertTrue(user.getUpdatedAt().getTime() <= now.getTime())
        );
    }
}
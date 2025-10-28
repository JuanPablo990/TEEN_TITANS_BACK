package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para LoginRequest DTO
 */
public class LoginRequestTest {

    private LoginRequest loginRequest;
    private LoginRequest loginRequestConDatos;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();

        loginRequestConDatos = new LoginRequest();
        loginRequestConDatos.setEmail("robin@titans.edu");
        loginRequestConDatos.setPassword("password123");
        loginRequestConDatos.setRole(UserRole.STUDENT);
    }

    @Test
    @DisplayName("Caso exitoso - Getters y Setters funcionan correctamente")
    void testGettersAndSetters_Exitoso() {
        loginRequest.setEmail("cyborg@titans.edu");
        loginRequest.setPassword("tech123");
        loginRequest.setRole(UserRole.ADMINISTRATOR);

        assertAll("Validar getters y setters",
                () -> assertEquals("cyborg@titans.edu", loginRequest.getEmail()),
                () -> assertEquals("tech123", loginRequest.getPassword()),
                () -> assertEquals(UserRole.ADMINISTRATOR, loginRequest.getRole())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Constructor por defecto crea objeto")
    void testConstructorPorDefecto_Exitoso() {
        assertNotNull(loginRequest, "El constructor por defecto debe crear un objeto no nulo");

        assertAll("Validar valores por defecto",
                () -> assertNull(loginRequest.getEmail()),
                () -> assertNull(loginRequest.getPassword()),
                () -> assertNull(loginRequest.getRole())
        );
    }

    @Test
    @DisplayName("Caso exitoso - Equals funciona correctamente con mismo objeto")
    void testEquals_MismoObjeto() {
        assertEquals(loginRequestConDatos, loginRequestConDatos);
        assertTrue(loginRequestConDatos.equals(loginRequestConDatos));
    }

    @Test
    @DisplayName("Caso exitoso - Equals funciona correctamente con objetos iguales")
    void testEquals_ObjetosIguales() {
        LoginRequest otroLoginRequest = new LoginRequest();
        otroLoginRequest.setEmail("robin@titans.edu");
        otroLoginRequest.setPassword("password123");
        otroLoginRequest.setRole(UserRole.STUDENT);

        assertEquals(loginRequestConDatos, otroLoginRequest);
        assertEquals(otroLoginRequest, loginRequestConDatos);
    }

    @Test
    @DisplayName("Caso error - Equals retorna false con objeto null")
    void testEquals_ConNull() {
        assertNotEquals(loginRequestConDatos, null);
        assertFalse(loginRequestConDatos.equals(null));
    }

    @Test
    @DisplayName("Caso error - Equals retorna false con objeto de diferente clase")
    void testEquals_ConDiferenteClase() {
        assertNotEquals(loginRequestConDatos, "No soy un LoginRequest");
        assertFalse(loginRequestConDatos.equals("No soy un LoginRequest"));
    }

    @Test
    @DisplayName("Caso error - Equals retorna false con email diferente")
    void testEquals_EmailDiferente() {
        LoginRequest otroLoginRequest = new LoginRequest();
        otroLoginRequest.setEmail("starfire@titans.edu");
        otroLoginRequest.setPassword("password123");
        otroLoginRequest.setRole(UserRole.STUDENT);

        assertNotEquals(loginRequestConDatos, otroLoginRequest);
        assertNotEquals(otroLoginRequest, loginRequestConDatos);
    }

    @Test
    @DisplayName("Caso error - Equals retorna false con password diferente")
    void testEquals_PasswordDiferente() {
        LoginRequest otroLoginRequest = new LoginRequest();
        otroLoginRequest.setEmail("robin@titans.edu");
        otroLoginRequest.setPassword("password456");
        otroLoginRequest.setRole(UserRole.STUDENT);

        assertNotEquals(loginRequestConDatos, otroLoginRequest);
        assertNotEquals(otroLoginRequest, loginRequestConDatos);
    }

    @Test
    @DisplayName("Caso error - Equals retorna false con rol diferente")
    void testEquals_RolDiferente() {
        LoginRequest otroLoginRequest = new LoginRequest();
        otroLoginRequest.setEmail("robin@titans.edu");
        otroLoginRequest.setPassword("password123");
        otroLoginRequest.setRole(UserRole.PROFESSOR);

        assertNotEquals(loginRequestConDatos, otroLoginRequest);
        assertNotEquals(otroLoginRequest, loginRequestConDatos);
    }

    @Test
    @DisplayName("Caso exitoso - HashCode consistente para objetos iguales")
    void testHashCode_Consistente() {
        LoginRequest otroLoginRequest = new LoginRequest();
        otroLoginRequest.setEmail("robin@titans.edu");
        otroLoginRequest.setPassword("password123");
        otroLoginRequest.setRole(UserRole.STUDENT);

        assertEquals(loginRequestConDatos.hashCode(), otroLoginRequest.hashCode());
    }

    @Test
    @DisplayName("Caso exitoso - HashCode diferente para objetos diferentes")
    void testHashCode_Diferente() {
        LoginRequest otroLoginRequest = new LoginRequest();
        otroLoginRequest.setEmail("starfire@titans.edu");
        otroLoginRequest.setPassword("light123");
        otroLoginRequest.setRole(UserRole.PROFESSOR);

        assertNotEquals(loginRequestConDatos.hashCode(), otroLoginRequest.hashCode());
    }

    @Test
    @DisplayName("Caso exitoso - HashCode igual para mismo objeto")
    void testHashCode_MismoObjeto() {
        int hashCode1 = loginRequestConDatos.hashCode();
        int hashCode2 = loginRequestConDatos.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    @DisplayName("Caso exitoso - ToString contiene información relevante")
    void testToString_ContieneInformacion() {
        String toStringResult = loginRequestConDatos.toString();

        assertAll("Validar que toString contiene información importante",
                () -> assertTrue(toStringResult.contains("robin@titans.edu"), "Debe contener el email"),
                () -> assertTrue(toStringResult.contains("password123"), "Debe contener el password"),
                () -> assertTrue(toStringResult.contains("STUDENT"), "Debe contener el rol")
        );
    }

    @Test
    @DisplayName("Caso borde - Campos null en toString")
    void testToString_ConCamposNull() {
        String toStringResult = loginRequest.toString();

        assertNotNull(toStringResult, "ToString no debe ser null incluso con campos null");
        assertTrue(toStringResult.contains("LoginRequest"), "Debe contener el nombre de la clase");
    }

    @Test
    @DisplayName("Caso borde - Email vacío")
    void testEmailVacio() {
        loginRequest.setEmail("");

        assertEquals("", loginRequest.getEmail());
    }

    @Test
    @DisplayName("Caso borde - Password vacío")
    void testPasswordVacio() {
        loginRequest.setPassword("");

        assertEquals("", loginRequest.getPassword());
    }

    @Test
    @DisplayName("Caso borde - Rol null")
    void testRolNull() {
        loginRequest.setRole(null);

        assertNull(loginRequest.getRole());
    }

    @Test
    @DisplayName("Caso borde - Todos los campos null")
    void testTodosCamposNull() {
        LoginRequest requestVacio = new LoginRequest();

        assertAll("Validar objeto con todos los campos null",
                () -> assertNull(requestVacio.getEmail()),
                () -> assertNull(requestVacio.getPassword()),
                () -> assertNull(requestVacio.getRole())
        );
    }

    @Test
    @DisplayName("Caso borde - Email con formato especial")
    void testEmailFormatoEspecial() {
        String emailEspecial = "usuario+test@titans.edu.co";
        loginRequest.setEmail(emailEspecial);

        assertEquals(emailEspecial, loginRequest.getEmail());
    }

    @Test
    @DisplayName("Caso borde - Password con caracteres especiales")
    void testPasswordCaracteresEspeciales() {
        String passwordEspecial = "P@ssw0rd!123#";
        loginRequest.setPassword(passwordEspecial);

        assertEquals(passwordEspecial, loginRequest.getPassword());
    }

    @Test
    @DisplayName("Caso borde - Cambios múltiples en los mismos campos")
    void testCambiosMultiples() {
        loginRequest.setEmail("email1@test.com");
        loginRequest.setEmail("email2@test.com");
        loginRequest.setEmail("email3@test.com");

        loginRequest.setPassword("pass1");
        loginRequest.setPassword("pass2");
        loginRequest.setPassword("pass3");

        loginRequest.setRole(UserRole.STUDENT);
        loginRequest.setRole(UserRole.PROFESSOR);
        loginRequest.setRole(UserRole.DEAN);

        assertAll("Validar últimos valores asignados",
                () -> assertEquals("email3@test.com", loginRequest.getEmail()),
                () -> assertEquals("pass3", loginRequest.getPassword()),
                () -> assertEquals(UserRole.DEAN, loginRequest.getRole())
        );
    }

    @Test
    @DisplayName("Caso borde - Todos los roles de UserRole")
    void testTodosLosRoles() {
        UserRole[] roles = UserRole.values();

        for (UserRole rol : roles) {
            loginRequest.setRole(rol);
            assertEquals(rol, loginRequest.getRole(), "Debe poder asignar y obtener el rol: " + rol);
        }
    }

    @Test
    @DisplayName("Caso borde - Email muy largo")
    void testEmailMuyLargo() {
        String emailLargo = "usuario.muy.largo.con.muchos.caracteres@dominio.muy.largo.edu.co";
        loginRequest.setEmail(emailLargo);

        assertEquals(emailLargo, loginRequest.getEmail());
    }

    @Test
    @DisplayName("Caso borde - Password muy largo")
    void testPasswordMuyLargo() {
        String passwordLargo = "P@ssw0rdMuyLargoConMuchosCaracteresEspeciales!@#$%^&*()1234567890";
        loginRequest.setPassword(passwordLargo);

        assertEquals(passwordLargo, loginRequest.getPassword());
    }

    @Test
    @DisplayName("Caso borde - Comparación con instancia diferente")
    void testEquals_InstanciaDiferente() {
        LoginRequest request1 = new LoginRequest();
        request1.setEmail("test@test.com");
        request1.setPassword("pass");
        request1.setRole(UserRole.STUDENT);

        LoginRequest request2 = new LoginRequest();
        request2.setEmail("test@test.com");
        request2.setPassword("pass");
        request2.setRole(UserRole.STUDENT);

        LoginRequest request3 = new LoginRequest();
        request3.setEmail("otro@test.com");
        request3.setPassword("pass");
        request3.setRole(UserRole.STUDENT);

        assertAll("Validar comparaciones múltiples",
                () -> assertEquals(request1, request2),
                () -> assertNotEquals(request1, request3),
                () -> assertNotEquals(request2, request3)
        );
    }

    @Test
    @DisplayName("Caso borde - Verificar simetría en equals")
    void testEquals_Simetria() {
        LoginRequest request1 = new LoginRequest();
        request1.setEmail("simetria@test.com");
        request1.setPassword("pass");
        request1.setRole(UserRole.PROFESSOR);

        LoginRequest request2 = new LoginRequest();
        request2.setEmail("simetria@test.com");
        request2.setPassword("pass");
        request2.setRole(UserRole.PROFESSOR);

        assertTrue(request1.equals(request2) && request2.equals(request1));
    }

    @Test
    @DisplayName("Caso borde - Verificar transitividad en equals")
    void testEquals_Transitividad() {
        LoginRequest request1 = new LoginRequest();
        request1.setEmail("transitivo@test.com");
        request1.setPassword("pass");
        request1.setRole(UserRole.ADMINISTRATOR);

        LoginRequest request2 = new LoginRequest();
        request2.setEmail("transitivo@test.com");
        request2.setPassword("pass");
        request2.setRole(UserRole.ADMINISTRATOR);

        LoginRequest request3 = new LoginRequest();
        request3.setEmail("transitivo@test.com");
        request3.setPassword("pass");
        request3.setRole(UserRole.ADMINISTRATOR);

        assertAll("Validar transitividad",
                () -> assertEquals(request1, request2),
                () -> assertEquals(request2, request3),
                () -> assertEquals(request1, request3)
        );
    }

    @Test
    @DisplayName("Caso borde - Verificar consistencia en hashCode")
    void testHashCode_Consistencia() {
        LoginRequest request = new LoginRequest();
        request.setEmail("consistencia@test.com");
        request.setPassword("pass");
        request.setRole(UserRole.DEAN);

        int hashCode1 = request.hashCode();
        int hashCode2 = request.hashCode();
        int hashCode3 = request.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertEquals(hashCode2, hashCode3);
        assertEquals(hashCode1, hashCode3);
    }

    @Test
    @DisplayName("Caso borde - Not equals después de modificación")
    void testNotEquals_DespuesModificacion() {
        LoginRequest request1 = new LoginRequest();
        request1.setEmail("original@test.com");
        request1.setPassword("pass");
        request1.setRole(UserRole.STUDENT);

        LoginRequest request2 = new LoginRequest();
        request2.setEmail("original@test.com");
        request2.setPassword("pass");
        request2.setRole(UserRole.STUDENT);

        assertEquals(request1, request2);

        request2.setEmail("modificado@test.com");

        assertNotEquals(request1, request2);
    }
}
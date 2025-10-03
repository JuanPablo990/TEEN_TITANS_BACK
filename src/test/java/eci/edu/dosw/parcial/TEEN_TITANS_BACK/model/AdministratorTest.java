package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class AdministratorTest {

    private Administrator admin;

    @BeforeEach
    void setUp() {
        admin = new Administrator();
    }

    @Test
    void testInitialValuesAreNull() {
        assertAll("Valores iniciales",
                () -> assertNull(admin.getPrivilegios()),
                () -> assertNull(admin.getGabTrivingGoal())
        );
    }

    @Test
    void testSetAndGetPrivilegios() throws Exception {
        setPrivateField("privilegios", "Acceso total");
        assertEquals("Acceso total", admin.getPrivilegios());
    }

    @Test
    void testSetAndGetGabTrivingGoal() throws Exception {
        setPrivateField("gabTrivingGoal", "Optimizar recursos");
        assertEquals("Optimizar recursos", admin.getGabTrivingGoal());
    }

    @Test
    void testDifferentInstancesNotSame() throws Exception {
        Administrator anotherAdmin = new Administrator();
        setPrivateField("privilegios", "Acceso limitado");

        assertNotSame(admin, anotherAdmin);
        assertNotEquals(anotherAdmin.getPrivilegios(), admin.getPrivilegios());
    }

    @Test
    void testIsInstanceOfUser() {
        assertInstanceOf(User.class, admin);
    }

    private void setPrivateField(String fieldName, Object value) throws Exception {
        Field field = Administrator.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(admin, value);
    }
}

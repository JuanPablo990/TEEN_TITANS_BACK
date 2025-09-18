package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Tests_Gestion_de_Estudiantes;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Estudiantes.Autenticacion;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AutenticacionTest {

    @Test
    void testRegistrarYAutenticarUsuario() {
        Autenticacion auth = new Autenticacion();
        auth.registrarUsuario("juan", "1234");

        assertTrue(auth.autenticar("juan", "1234"));
        assertFalse(auth.autenticar("juan", "0000"));
        assertFalse(auth.autenticar("maria", "1234"));
    }
}

package eci.edu.dosw.parcial.TEEN_TITANS_BACK.testsGestion_por_Decanatura;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_por_Decanatura.Decanatura;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DecanaturaTest {

    @Test
    void testConstructorYGetters() {
        Decanatura dec = new Decanatura("Ingeniería", "user123");
        assertEquals("Ingeniería", dec.getNombreFacultad());
        assertEquals("user123", dec.getUsuario());
    }

    @Test
    void testRevisarSolicitud() {
        Decanatura dec = new Decanatura("Derecho", "user456");
        assertDoesNotThrow(() -> dec.revisarSolicitud("S1"));
    }
}

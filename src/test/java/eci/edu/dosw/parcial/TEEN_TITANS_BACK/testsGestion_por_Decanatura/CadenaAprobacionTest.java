package eci.edu.dosw.parcial.TEEN_TITANS_BACK.testsGestion_por_Decanatura;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_por_Decanatura.CadenaAprobacion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CadenaAprobacionTest {

    @Test
    void testConstruirCadenaYProcesar() {
        CadenaAprobacion cadena = new CadenaAprobacion();
        cadena.construirCadena();
        assertDoesNotThrow(() -> cadena.procesarSolicitud("S1", "Ingeniería"));
    }

    @Test
    void testProcesarSinConstruir() {
        CadenaAprobacion cadena = new CadenaAprobacion();
        assertDoesNotThrow(() -> cadena.procesarSolicitud("S2", "Ingeniería"));
    }
}

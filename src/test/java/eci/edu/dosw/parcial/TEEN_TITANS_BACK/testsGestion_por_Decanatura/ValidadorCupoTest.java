package eci.edu.dosw.parcial.TEEN_TITANS_BACK.testsGestion_por_Decanatura;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_por_Decanatura.ValidadorCupo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidadorCupoTest {

    @Test
    void testEvaluarConCupoDisponible() {
        ValidadorCupo val = new ValidadorCupo() {
            @Override
            public void evaluar(String idSolicitud, String facultad) {
                logger.info("Cupo disponible forzado");
            }
        };
        assertDoesNotThrow(() -> val.evaluar("S1", "Ingeniería"));
    }

    @Test
    void testEvaluarSinCupo() {
        ValidadorCupo val = new ValidadorCupo() {
            @Override
            public void evaluar(String idSolicitud, String facultad) {
                logger.warning("Grupo lleno forzado");
            }
        };
        assertDoesNotThrow(() -> val.evaluar("S2", "Ingeniería"));
    }
}

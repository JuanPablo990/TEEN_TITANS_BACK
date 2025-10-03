package eci.edu.dosw.parcial.TEEN_TITANS_BACK.testsGestion_por_Decanatura;


import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_por_Decanatura.ValidadorCalendario;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_por_Decanatura.ValidadorCupo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ValidadorCalendarioTest {

    @Test
    void testEvaluarNoLanzaExcepcion() {
        ValidadorCalendario val = new ValidadorCalendario();
        assertDoesNotThrow(() -> val.evaluar("SOL-1", "Ingeniería"));
    }

    @Test
    void testEvaluarConSiguienteNoLanzaExcepcion() {
        ValidadorCalendario val = new ValidadorCalendario();
        val.setSiguiente(new ValidadorCupo()); // cualquier evaluador como siguiente
        assertDoesNotThrow(() -> val.evaluar("SOL-2", "Ingeniería"));
    }

    @Test
    void testEvaluarConFacultadDiferenteNoLanzaExcepcion() {
        ValidadorCalendario val = new ValidadorCalendario();
        assertDoesNotThrow(() -> val.evaluar("SOL-3", "Medicina"));
    }
}

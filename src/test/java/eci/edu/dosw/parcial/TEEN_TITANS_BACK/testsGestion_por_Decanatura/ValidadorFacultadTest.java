package eci.edu.dosw.parcial.TEEN_TITANS_BACK.testsGestion_por_Decanatura;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_por_Decanatura.EvaluadorSolicitud;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_por_Decanatura.ValidadorFacultad;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidadorFacultadTest {

    @Test
    void testEvaluarFacultadCorrecta() {
        ValidadorFacultad val = new ValidadorFacultad();
        val.setSiguiente(new EvaluadorSolicitud() {
            @Override
            public void evaluar(String idSolicitud, String facultad) {
            }
        });
        assertDoesNotThrow(() -> val.evaluar("S1", "Ingeniería"));
    }

    @Test
    void testEvaluarFacultadIncorrecta() {
        ValidadorFacultad val = new ValidadorFacultad();
        assertDoesNotThrow(() -> val.evaluar("S2", "Derecho"));
    }
}


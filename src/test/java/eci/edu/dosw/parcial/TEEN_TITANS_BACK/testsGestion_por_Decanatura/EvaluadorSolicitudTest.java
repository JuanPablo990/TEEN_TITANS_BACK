package eci.edu.dosw.parcial.TEEN_TITANS_BACK.testsGestion_por_Decanatura;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_por_Decanatura.EvaluadorSolicitud;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EvaluadorSolicitudTest {

    @Test
    void testSetSiguienteAndChainInvocation() {
        final boolean[] invoked = {false};

        EvaluadorSolicitud primero = new EvaluadorSolicitud() {
            @Override
            public void evaluar(String idSolicitud, String facultad) {

                if (siguiente != null) siguiente.evaluar(idSolicitud, facultad);
            }
        };

        EvaluadorSolicitud segundo = new EvaluadorSolicitud() {
            @Override
            public void evaluar(String idSolicitud, String facultad) {
                invoked[0] = true;
            }
        };

        primero.setSiguiente(segundo);
        assertDoesNotThrow(() -> primero.evaluar("S-CHAIN-1", "Ingeniería"));
        assertTrue(invoked[0], "El siguiente manejador en la cadena debe ser invocado.");
    }

    @Test
    void testEvaluarWithoutSiguienteDoesNotThrow() {
        EvaluadorSolicitud nodo = new EvaluadorSolicitud() {
            @Override
            public void evaluar(String idSolicitud, String facultad) {

            }
        };


        assertDoesNotThrow(() -> nodo.evaluar("S-NONE-1", "Derecho"));
    }

    @Test
    void testSetSiguienteNullIsAllowed() {
        EvaluadorSolicitud nodo = new EvaluadorSolicitud() {
            @Override
            public void evaluar(String idSolicitud, String facultad) {
                if (siguiente != null) siguiente.evaluar(idSolicitud, facultad);
            }
        };

        nodo.setSiguiente(null);
        assertDoesNotThrow(() -> nodo.evaluar("S-NULL-1", "Medicina"));
    }
}


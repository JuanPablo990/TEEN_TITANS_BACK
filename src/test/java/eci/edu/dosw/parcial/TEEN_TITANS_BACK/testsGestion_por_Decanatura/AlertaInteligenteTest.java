package eci.edu.dosw.parcial.TEEN_TITANS_BACK.testsGestion_por_Decanatura;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_por_Decanatura.AlertaInteligente;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlertaInteligenteTest {

    @Test
    void testNotificarCambioEstadoRechazada() {
        AlertaInteligente alerta = new AlertaInteligente();
        assertDoesNotThrow(() -> alerta.notificarCambioEstado("S1", "RECHAZADA"));
    }

    @Test
    void testNotificarCambioEstadoAprobada() {
        AlertaInteligente alerta = new AlertaInteligente();
        assertDoesNotThrow(() -> alerta.notificarCambioEstado("S2", "APROBADA"));
    }

    @Test
    void testNotificarCambioEstadoOtro() {
        AlertaInteligente alerta = new AlertaInteligente();
        assertDoesNotThrow(() -> alerta.notificarCambioEstado("S3", "EN PROCESO"));
    }
}

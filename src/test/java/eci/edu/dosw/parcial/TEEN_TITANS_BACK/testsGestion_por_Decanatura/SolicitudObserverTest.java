package eci.edu.dosw.parcial.TEEN_TITANS_BACK.testsGestion_por_Decanatura;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_por_Decanatura.AlertaInteligente;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_por_Decanatura.DashboardDecanatura;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_por_Decanatura.SolicitudObserver;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SolicitudObserverTest {


    static class DummyObserver implements SolicitudObserver {
        String lastId = null;
        String lastEstado = null;

        @Override
        public void notificarCambioEstado(String idSolicitud, String nuevoEstado) {
            this.lastId = idSolicitud;
            this.lastEstado = nuevoEstado;
        }
    }

    @Test
    void testDummyObserverReceivesNotification() {
        DummyObserver obs = new DummyObserver();
        obs.notificarCambioEstado("S100", "APROBADA");

        assertEquals("S100", obs.lastId);
        assertEquals("APROBADA", obs.lastEstado);
    }

    @Test
    void testImplementacionesExistentesNoLanzanExcepcionAlNotificar() {
        SolicitudObserver dashboard = new DashboardDecanatura();
        SolicitudObserver alerta = new AlertaInteligente();

        assertDoesNotThrow(() -> dashboard.notificarCambioEstado("S-UI-1", "PENDIENTE"));
        assertDoesNotThrow(() -> alerta.notificarCambioEstado("S-UI-2", "RECHAZADA"));
    }
}

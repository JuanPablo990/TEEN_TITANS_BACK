package eci.edu.dosw.parcial.TEEN_TITANS_BACK.testsGestion_por_Decanatura;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_por_Decanatura.DashboardDecanatura;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DashboardDecanaturaTest {

    @Test
    void testNotificarCambioEstado() {
        DashboardDecanatura dashboard = new DashboardDecanatura();
        assertDoesNotThrow(() -> dashboard.notificarCambioEstado("S1", "APROBADA"));
    }
}

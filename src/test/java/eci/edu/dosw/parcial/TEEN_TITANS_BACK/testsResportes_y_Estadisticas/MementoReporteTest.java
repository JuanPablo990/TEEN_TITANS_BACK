package eci.edu.dosw.parcial.TEEN_TITANS_BACK.testsResportes_y_Estadisticas;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Reportes_y_Estadísticas.MementoReporte;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MementoReporteTest {

    @Test
    void testGetConfiguracion() {
        MementoReporte m = new MementoReporte("config-xyz");
        assertEquals("config-xyz", m.getConfiguracion());
    }
}

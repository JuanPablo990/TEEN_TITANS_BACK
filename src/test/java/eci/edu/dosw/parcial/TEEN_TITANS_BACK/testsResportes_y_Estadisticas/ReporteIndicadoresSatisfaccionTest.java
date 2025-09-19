package eci.edu.dosw.parcial.TEEN_TITANS_BACK.testsResportes_y_Estadisticas;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Reportes_y_Estadísticas.ExportarPDFVisitor;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Reportes_y_Estadísticas.ReporteIndicadoresSatisfaccion;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Reportes_y_Estadísticas.ReporteVisitor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReporteIndicadoresSatisfaccionTest {

    @Test
    void testGenerarNoLanzaExcepcion() {
        ReporteIndicadoresSatisfaccion reporte = new ReporteIndicadoresSatisfaccion();
        assertDoesNotThrow(reporte::generar);
    }

    @Test
    void testAceptarVisitor() {
        ReporteIndicadoresSatisfaccion reporte = new ReporteIndicadoresSatisfaccion();
        ReporteVisitor visitor = new ExportarPDFVisitor();
        assertDoesNotThrow(() -> reporte.aceptar(visitor));
    }
}

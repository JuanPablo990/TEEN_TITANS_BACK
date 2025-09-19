package eci.edu.dosw.parcial.TEEN_TITANS_BACK.testsResportes_y_Estadisticas;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Reportes_y_Estadísticas.ExportarPDFVisitor;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Reportes_y_Estadísticas.ReporteGruposCriticos;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Reportes_y_Estadísticas.ReporteVisitor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReporteGruposCriticosTest {

    @Test
    void testGenerarNoLanzaExcepcion() {
        ReporteGruposCriticos reporte = new ReporteGruposCriticos();
        assertDoesNotThrow(reporte::generar);
    }

    @Test
    void testAceptarVisitor() {
        ReporteGruposCriticos reporte = new ReporteGruposCriticos();
        ReporteVisitor visitor = new ExportarPDFVisitor();
        assertDoesNotThrow(() -> reporte.aceptar(visitor));
    }
}

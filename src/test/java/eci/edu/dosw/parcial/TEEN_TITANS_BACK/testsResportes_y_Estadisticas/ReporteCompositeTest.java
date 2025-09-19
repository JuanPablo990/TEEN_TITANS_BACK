package eci.edu.dosw.parcial.TEEN_TITANS_BACK.testsResportes_y_Estadisticas;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Reportes_y_Estadísticas.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReporteCompositeTest {

    @Test
    void testAgregarYGenerar() {
        ReporteComposite composite = new ReporteComposite();
        composite.agregarReporte(new ReporteGruposCriticos());
        composite.agregarReporte(new ReporteIndicadoresSatisfaccion());
        assertDoesNotThrow(composite::generar);
    }

    @Test
    void testAceptarVisitor() {
        ReporteComposite composite = new ReporteComposite();
        composite.agregarReporte(new ReporteGruposCriticos());
        composite.agregarReporte(new ReporteHistorialEstudiante("E321"));
        ReporteVisitor visitor = new ExportarPDFVisitor();
        assertDoesNotThrow(() -> composite.aceptar(visitor));
    }
}

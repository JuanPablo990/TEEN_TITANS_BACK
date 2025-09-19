package eci.edu.dosw.parcial.TEEN_TITANS_BACK.testsResportes_y_Estadisticas;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Reportes_y_Estadísticas.ExportarPDFVisitor;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Reportes_y_Estadísticas.ReporteGruposCriticos;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Reportes_y_Estadísticas.ReporteHistorialEstudiante;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Reportes_y_Estadísticas.ReporteIndicadoresSatisfaccion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ExportarPDFVisitorTest {

    @Test
    void testVisitarHistorial() {
        ExportarPDFVisitor visitor = new ExportarPDFVisitor();
        ReporteHistorialEstudiante reporte = new ReporteHistorialEstudiante("E001");
        assertDoesNotThrow(() -> visitor.visitarHistorial(reporte));
    }

    @Test
    void testVisitarGruposCriticos() {
        ExportarPDFVisitor visitor = new ExportarPDFVisitor();
        ReporteGruposCriticos reporte = new ReporteGruposCriticos();
        assertDoesNotThrow(() -> visitor.visitarGruposCriticos(reporte));
    }

    @Test
    void testVisitarIndicadoresSatisfaccion() {
        ExportarPDFVisitor visitor = new ExportarPDFVisitor();
        ReporteIndicadoresSatisfaccion reporte = new ReporteIndicadoresSatisfaccion();
        assertDoesNotThrow(() -> visitor.visitarIndicadoresSatisfaccion(reporte));
    }
}

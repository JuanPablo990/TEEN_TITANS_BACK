package eci.edu.dosw.parcial.TEEN_TITANS_BACK.testsResportes_y_Estadisticas;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Reportes_y_Estadísticas.ReporteGruposCriticos;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Reportes_y_Estadísticas.ReporteHistorialEstudiante;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Reportes_y_Estadísticas.ReporteIndicadoresSatisfaccion;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Reportes_y_Estadísticas.ReporteVisitor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReporteHistorialEstudianteTest {

    @Test
    void testGetEstudianteId() {
        ReporteHistorialEstudiante reporte = new ReporteHistorialEstudiante("E123");
        assertEquals("E123", reporte.getEstudianteId());
    }

    @Test
    void testGenerarNoLanzaExcepcion() {
        ReporteHistorialEstudiante reporte = new ReporteHistorialEstudiante("E123");
        assertDoesNotThrow(reporte::generar);
    }

    @Test
    void testAceptarVisitor() {
        ReporteHistorialEstudiante reporte = new ReporteHistorialEstudiante("E123");

        ReporteVisitor visitor = new ReporteVisitor() {
            boolean llamado = false;
            @Override
            public void visitarHistorial(ReporteHistorialEstudiante r) { llamado = true; }
            @Override
            public void visitarGruposCriticos(ReporteGruposCriticos r) {}
            @Override
            public void visitarIndicadoresSatisfaccion(ReporteIndicadoresSatisfaccion r) {}

            boolean fueLlamado() { return llamado; }
        };

        visitor.visitarHistorial(reporte);

        assertDoesNotThrow(() -> reporte.aceptar(visitor));
    }
}


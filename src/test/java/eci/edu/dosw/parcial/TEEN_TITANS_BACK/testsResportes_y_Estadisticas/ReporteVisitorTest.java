package eci.edu.dosw.parcial.TEEN_TITANS_BACK.testsResportes_y_Estadisticas;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Reportes_y_Estadísticas.ReporteGruposCriticos;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Reportes_y_Estadísticas.ReporteHistorialEstudiante;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Reportes_y_Estadísticas.ReporteIndicadoresSatisfaccion;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Reportes_y_Estadísticas.ReporteVisitor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ReporteVisitorTest {

    @Test
    void testAnonymousImplementation() {
        ReporteVisitor visitor = new ReporteVisitor() {
            boolean historial = false;
            boolean grupos = false;
            boolean satisfaccion = false;

            @Override
            public void visitarHistorial(ReporteHistorialEstudiante reporte) { historial = true; }
            @Override
            public void visitarGruposCriticos(ReporteGruposCriticos reporte) { grupos = true; }
            @Override
            public void visitarIndicadoresSatisfaccion(ReporteIndicadoresSatisfaccion reporte) { satisfaccion = true; }

            boolean allCalled() { return historial && grupos && satisfaccion; }
        };

        visitor.visitarHistorial(new ReporteHistorialEstudiante("E"));
        visitor.visitarGruposCriticos(new ReporteGruposCriticos());
        visitor.visitarIndicadoresSatisfaccion(new ReporteIndicadoresSatisfaccion());

        assertTrue(((ReporteVisitorTest.AnonymousClass) visitor).allCalled());
    }

    private interface AnonymousClass extends ReporteVisitor {
        boolean allCalled();
    }
}

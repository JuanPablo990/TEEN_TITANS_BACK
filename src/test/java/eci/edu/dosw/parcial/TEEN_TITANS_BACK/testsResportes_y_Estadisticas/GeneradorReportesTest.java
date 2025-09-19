package eci.edu.dosw.parcial.TEEN_TITANS_BACK.testsResportes_y_Estadisticas;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Reportes_y_Estadísticas.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeneradorReportesTest {

    @Test
    void testGenerarEjecutaMetodoEnSubclase() {
        final boolean[] ejecutado = {false};

        GeneradorReportes gen = new GeneradorReportes() {
            @Override
            public void generar() {
                ejecutado[0] = true;
            }

            @Override
            public void aceptar(ReporteVisitor visitor) {
            }
        };

        assertFalse(ejecutado[0], "Antes de llamar a generar() la bandera debe ser false");
        assertDoesNotThrow(gen::generar, "Llamar a generar() no debe lanzar excepción");
        assertTrue(ejecutado[0], "Después de generar(), la bandera debe ser true (método ejecutado)");
    }

    @Test
    void testAceptarInvocaMetodoDelVisitor() {

        final boolean[] visitorLlamado = {false};

        ReporteVisitor visitor = new ReporteVisitor() {
            @Override
            public void visitarHistorial(ReporteHistorialEstudiante reporte) {
                visitorLlamado[0] = true;
            }

            @Override
            public void visitarGruposCriticos(ReporteGruposCriticos reporte) { /* no usado */ }

            @Override
            public void visitarIndicadoresSatisfaccion(ReporteIndicadoresSatisfaccion reporte) { /* no usado */ }
        };

        GeneradorReportes gen = new GeneradorReportes() {
            @Override
            public void generar() {

            }

            @Override
            public void aceptar(ReporteVisitor v) {

                v.visitarHistorial(new ReporteHistorialEstudiante("EST-123"));
            }
        };

        assertFalse(visitorLlamado[0], "Antes de aceptar, el visitor no debe haber sido llamado");
        assertDoesNotThrow(() -> gen.aceptar(visitor), "Llamar a aceptar(visitor) no debe lanzar excepción");
        assertTrue(visitorLlamado[0], "Después de aceptar, el método correspondiente del visitor debe haber sido invocado");
    }

    @Test
    void testGenerarYAceptarNoLanzanExcepcionCuandoNoHacenNada() {
        GeneradorReportes gen = new GeneradorReportes() {
            @Override
            public void generar() {

            }

            @Override
            public void aceptar(ReporteVisitor visitor) {

            }
        };

        assertDoesNotThrow(gen::generar);
        assertDoesNotThrow(() -> gen.aceptar(new ExportarPDFVisitor()));
    }
}


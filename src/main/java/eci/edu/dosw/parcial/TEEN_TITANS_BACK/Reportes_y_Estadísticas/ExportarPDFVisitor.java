package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Reportes_y_Estadísticas;

import java.util.logging.Logger;

public class ExportarPDFVisitor implements ReporteVisitor {
    private static final Logger logger = Logger.getLogger(ExportarPDFVisitor.class.getName());

    @Override
    public void visitarHistorial(ReporteHistorialEstudiante reporte) {
        logger.info("Exportando historial de estudiante " + reporte.getEstudianteId() + " a PDF.");
    }

    @Override
    public void visitarGruposCriticos(ReporteGruposCriticos reporte) {
        logger.info("Exportando reporte de grupos críticos a PDF.");
    }

    @Override
    public void visitarIndicadoresSatisfaccion(ReporteIndicadoresSatisfaccion reporte) {
        logger.info("Exportando reporte de satisfacción a PDF.");
    }
}

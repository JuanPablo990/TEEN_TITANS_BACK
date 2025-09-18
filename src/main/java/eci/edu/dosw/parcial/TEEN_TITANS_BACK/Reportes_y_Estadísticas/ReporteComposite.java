package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Reportes_y_Estadísticas;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ReporteComposite extends GeneradorReportes {
    private static final Logger logger = Logger.getLogger(ReporteComposite.class.getName());
    private List<GeneradorReportes> reportes = new ArrayList<>();

    public void agregarReporte(GeneradorReportes reporte) {
        reportes.add(reporte);
    }

    @Override
    public void generar() {
        logger.info("Generando reporte compuesto...");
        reportes.forEach(GeneradorReportes::generar);
    }

    @Override
    public void aceptar(ReporteVisitor visitor) {
        reportes.forEach(r -> r.aceptar(visitor));
    }
}

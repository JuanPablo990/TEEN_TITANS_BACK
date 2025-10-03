package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Reportes_y_Estadísticas;

public class ReporteIndicadoresSatisfaccion extends GeneradorReportes {
    @Override
    public void generar() {
        logger.info("Generando reporte de indicadores de satisfacción estudiantil...");
    }

    @Override
    public void aceptar(ReporteVisitor visitor) {
        visitor.visitarIndicadoresSatisfaccion(this);
    }
}

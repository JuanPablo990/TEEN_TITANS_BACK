package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Reportes_y_Estadísticas;

public class ReporteGruposCriticos extends GeneradorReportes {
    @Override
    public void generar() {
        logger.info("Generando reporte de grupos críticos...");
    }

    @Override
    public void aceptar(ReporteVisitor visitor) {
        visitor.visitarGruposCriticos(this);
    }
}

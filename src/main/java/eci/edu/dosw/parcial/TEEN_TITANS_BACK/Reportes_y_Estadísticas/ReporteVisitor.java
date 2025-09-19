package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Reportes_y_Estadísticas;

public interface ReporteVisitor {
    void visitarHistorial(ReporteHistorialEstudiante reporte);
    void visitarGruposCriticos(ReporteGruposCriticos reporte);
    void visitarIndicadoresSatisfaccion(ReporteIndicadoresSatisfaccion reporte);
}

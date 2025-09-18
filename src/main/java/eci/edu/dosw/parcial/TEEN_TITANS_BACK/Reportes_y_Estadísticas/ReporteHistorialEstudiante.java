package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Reportes_y_Estadísticas;

public class ReporteHistorialEstudiante extends GeneradorReportes {
    private String estudianteId;

    public ReporteHistorialEstudiante(String estudianteId) {
        this.estudianteId = estudianteId;
    }

    @Override
    public void generar() {
        logger.info("Generando reporte de historial para estudiante: " + estudianteId);
    }

    @Override
    public void aceptar(ReporteVisitor visitor) {
        visitor.visitarHistorial(this);
    }

    public String getEstudianteId() { return estudianteId; }
}

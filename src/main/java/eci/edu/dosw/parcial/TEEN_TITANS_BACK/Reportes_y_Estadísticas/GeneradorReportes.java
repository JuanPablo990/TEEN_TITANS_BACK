package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Reportes_y_Estadísticas;

import java.util.logging.Logger;

public abstract class GeneradorReportes {
    protected static final Logger logger = Logger.getLogger(GeneradorReportes.class.getName());

    public abstract void generar();
    public abstract void aceptar(ReporteVisitor visitor);
}

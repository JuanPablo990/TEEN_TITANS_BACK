package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Grupos_y_Materias;

import java.util.logging.Logger;

public class CapacidadDinamica {
    private static final Logger logger = Logger.getLogger(CapacidadDinamica.class.getName());

    private final int porcentajeExtra;

    public CapacidadDinamica(int porcentajeExtra) {
        this.porcentajeExtra = porcentajeExtra;
        logger.info(() -> "Regla de capacidad dinámica creada: +" + porcentajeExtra + "%");
    }

    public int calcularCupoExtendido(int cupoMaximo) {
        int nuevoCupo = cupoMaximo + (cupoMaximo * porcentajeExtra / 100);
        logger.info(() -> "Cupo extendido calculado: " + nuevoCupo + " (antes: " + cupoMaximo + ")");
        return nuevoCupo;
    }
}

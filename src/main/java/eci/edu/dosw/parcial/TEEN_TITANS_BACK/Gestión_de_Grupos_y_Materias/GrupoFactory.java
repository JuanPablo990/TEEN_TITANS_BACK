package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Grupos_y_Materias;

import java.util.logging.Logger;

public class GrupoFactory {
    private static final Logger logger = Logger.getLogger(GrupoFactory.class.getName());

    public static Grupo crearGrupo(String idGrupo, Materia materia, Profesor profesor, int cupoMaximo, CapacidadDinamica capacidadExtra) {
        int cupoFinal = (capacidadExtra != null) ? capacidadExtra.calcularCupoExtendido(cupoMaximo) : cupoMaximo;
        logger.info(() -> "Creando grupo " + idGrupo + " con cupo final: " + cupoFinal);
        return new Grupo(idGrupo, materia, profesor, cupoFinal);
    }
}

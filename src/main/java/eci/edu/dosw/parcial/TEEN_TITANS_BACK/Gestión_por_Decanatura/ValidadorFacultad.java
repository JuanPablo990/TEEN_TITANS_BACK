package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_por_Decanatura;

public class ValidadorFacultad extends EvaluadorSolicitud {

    @Override
    public void evaluar(String idSolicitud, String facultad) {
        logger.info(() -> "Validando facultad para solicitud " + idSolicitud);
        if ("Ingeniería".equals(facultad)) {
            logger.info(() -> "Solicitud " + idSolicitud + " pertenece a facultad correcta.");
            if (siguiente != null) siguiente.evaluar(idSolicitud, facultad);
        } else {
            logger.warning(() -> " Solicitud " + idSolicitud + " no corresponde a la facultad.");
        }
    }
}

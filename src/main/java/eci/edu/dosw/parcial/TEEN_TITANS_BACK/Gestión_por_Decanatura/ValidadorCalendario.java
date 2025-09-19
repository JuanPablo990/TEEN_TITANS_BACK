package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_por_Decanatura;

public class ValidadorCalendario extends EvaluadorSolicitud {
    @Override
    public void evaluar(String idSolicitud, String facultad) {
        logger.info(() -> "Validando calendario académico para solicitud " + idSolicitud);
        if (Math.random() > 0.2) { // simulación
            logger.info(() -> "Solicitud " + idSolicitud + " dentro del calendario académico.");
            if (siguiente != null) siguiente.evaluar(idSolicitud, facultad);
        } else {
            logger.warning(() -> " Solicitud " + idSolicitud + " fuera de fechas permitidas.");
        }
    }
}

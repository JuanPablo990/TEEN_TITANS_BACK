package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_por_Decanatura;

public class ValidadorCupo extends EvaluadorSolicitud {
    @Override
    public void evaluar(String idSolicitud, String facultad) {
        logger.info(() -> "Validando cupo disponible para solicitud " + idSolicitud);
        if (Math.random() > 0.5) {
            logger.info(() -> " Cupo disponible para solicitud " + idSolicitud);
        } else {
            logger.warning(() -> " Grupo lleno. No se aprueba la solicitud " + idSolicitud);
        }
    }
}

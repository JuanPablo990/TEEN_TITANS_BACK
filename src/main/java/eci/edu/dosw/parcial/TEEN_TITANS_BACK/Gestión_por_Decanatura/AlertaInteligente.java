package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_por_Decanatura;

import java.util.logging.Logger;

public class AlertaInteligente implements SolicitudObserver {
    private static final Logger logger = Logger.getLogger(AlertaInteligente.class.getName());

    @Override
    public void notificarCambioEstado(String idSolicitud, String nuevoEstado) {
        if ("RECHAZADA".equals(nuevoEstado)) {
            logger.warning(() -> " Alerta: Solicitud " + idSolicitud + " fue rechazada. Revisar causas.");
        } else if ("APROBADA".equals(nuevoEstado)) {
            logger.info(() -> " Alerta: Solicitud " + idSolicitud + " aprobada.");
        }
    }
}

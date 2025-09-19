package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_por_Decanatura;

import java.util.logging.Logger;

public class DashboardDecanatura implements SolicitudObserver {
    private static final Logger logger = Logger.getLogger(DashboardDecanatura.class.getName());

    @Override
    public void notificarCambioEstado(String idSolicitud, String nuevoEstado) {
        logger.info(() -> " Dashboard actualizado: Solicitud " + idSolicitud + " cambió a estado: " + nuevoEstado);
    }
}

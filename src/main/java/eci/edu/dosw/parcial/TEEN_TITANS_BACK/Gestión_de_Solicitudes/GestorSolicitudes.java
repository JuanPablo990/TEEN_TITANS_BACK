package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Solicitudes;

import java.util.*;
import java.util.logging.Logger;

public class GestorSolicitudes implements SolicitudMediator {
    private static final Logger logger = Logger.getLogger(GestorSolicitudes.class.getName());
    private List<Solicitud> solicitudes = new ArrayList<>();

    @Override
    public void registrarSolicitud(Solicitud solicitud) {
        solicitudes.add(solicitud);
        logger.info("Solicitud registrada: " + solicitud.getId());
    }

    @Override
    public void notificarCambioEstado(Solicitud solicitud) {
        logger.info("Notificación de cambio de estado en solicitud: " + solicitud.getId());
    }

    public IteradorSolicitudes getIteradorPorEstado(EstadoSolicitud estado) {
        return new IteradorSolicitudes(solicitudes, estado);
    }
}

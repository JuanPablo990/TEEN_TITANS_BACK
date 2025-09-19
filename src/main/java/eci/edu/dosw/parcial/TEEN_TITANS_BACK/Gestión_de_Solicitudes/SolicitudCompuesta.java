package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Solicitudes;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SolicitudCompuesta extends Solicitud {
    private static final Logger logger = Logger.getLogger(SolicitudCompuesta.class.getName());
    private List<Solicitud> solicitudes = new ArrayList<>();

    public void agregarSolicitud(Solicitud s) {
        solicitudes.add(s);
    }

    @Override
    public void procesar() {
        logger.info("Procesando solicitud compuesta: " + getId());
        for (Solicitud s : solicitudes) {
            s.procesar();
        }
        estado.manejar(this);
    }
}

package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Solicitudes;

import java.util.logging.Logger;

public class SolicitudSimple extends Solicitud {
    private static final Logger logger = Logger.getLogger(SolicitudSimple.class.getName());

    @Override
    public void procesar() {
        logger.info("Procesando solicitud simple: " + getId());
        estado.manejar(this);
    }
}

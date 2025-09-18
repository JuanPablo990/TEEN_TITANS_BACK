package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_por_Decanatura;

import java.util.logging.Logger;

public class Decanatura {
    private static final Logger logger = Logger.getLogger(Decanatura.class.getName());
    private final String nombreFacultad;
    private final String usuario;

    public Decanatura(String nombreFacultad, String usuario) {
        this.nombreFacultad = nombreFacultad;
        this.usuario = usuario;
        logger.info(() -> "Decanatura creada: " + nombreFacultad + " - Usuario: " + usuario);
    }

    public String getNombreFacultad() {
        return nombreFacultad;
    }

    public String getUsuario() {
        return usuario;
    }

    public void revisarSolicitud(String idSolicitud) {
        logger.info(() -> "Decanatura " + nombreFacultad + " revisa solicitud: " + idSolicitud);
    }
}

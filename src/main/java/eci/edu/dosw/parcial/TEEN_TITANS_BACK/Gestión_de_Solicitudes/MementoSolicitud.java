package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Solicitudes;

public class MementoSolicitud {
    private final Solicitud.EstadoSolicitud estado;

    public MementoSolicitud(Solicitud.EstadoSolicitud estado) {
        this.estado = estado;
    }

    public Solicitud.EstadoSolicitud getEstado() {
        return estado;
    }
}

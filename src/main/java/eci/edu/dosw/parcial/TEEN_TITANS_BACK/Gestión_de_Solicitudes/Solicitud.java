package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Solicitudes;

import java.util.UUID;

public abstract class Solicitud {
    private final String id;
    protected EstadoSolicitud estado;

    public Solicitud() {
        this.id = UUID.randomUUID().toString();
        this.estado = new EstadoPendiente();
    }

    public String getId() { return id; }
    public EstadoSolicitud getEstado() { return estado; }

    public void cambiarEstado(EstadoSolicitud nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public abstract void procesar();
}

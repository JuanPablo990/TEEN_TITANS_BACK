package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Solicitudes;

import java.util.UUID;

public abstract class Solicitud {
    private final String id;
    protected EstadoSolicitud estado;

    public Solicitud() {
        this.id = UUID.randomUUID().toString();
        this.estado = new EstadoPendiente();
    }

    public String getId() {
        return id;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public void cambiarEstado(EstadoSolicitud nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public void manejar() {
        estado.manejar(this);
    }

    public abstract void procesar();

    public interface EstadoSolicitud {
        void manejar(Solicitud solicitud);
    }

    public static class EstadoPendiente implements EstadoSolicitud {
        @Override
        public void manejar(Solicitud solicitud) {
            solicitud.cambiarEstado(new EstadoEnProceso());
        }
    }

    public static class EstadoEnProceso implements EstadoSolicitud {
        @Override
        public void manejar(Solicitud solicitud) {
            solicitud.cambiarEstado(new EstadoAprobada());
        }
    }

    public static class EstadoAprobada implements EstadoSolicitud {
        @Override
        public void manejar(Solicitud solicitud) {
        }
    }

    public static class EstadoRechazada implements EstadoSolicitud {
        @Override
        public void manejar(Solicitud solicitud) {
        }
    }
}
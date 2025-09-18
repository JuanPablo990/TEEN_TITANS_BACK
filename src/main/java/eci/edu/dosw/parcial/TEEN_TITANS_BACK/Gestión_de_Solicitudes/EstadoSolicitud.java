package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Solicitudes;

public interface EstadoSolicitud {
    void manejar(Solicitud solicitud);
}

class EstadoPendiente implements EstadoSolicitud {
    @Override
    public void manejar(Solicitud solicitud) {
        solicitud.cambiarEstado(new EstadoEnProceso());
    }
}

class EstadoEnProceso implements EstadoSolicitud {
    @Override
    public void manejar(Solicitud solicitud) {
        solicitud.cambiarEstado(new EstadoAprobada());
    }
}

class EstadoAprobada implements EstadoSolicitud {
    @Override
    public void manejar(Solicitud solicitud) {
        // Estado final, no cambia más
    }
}

class EstadoRechazada implements EstadoSolicitud {
    @Override
    public void manejar(Solicitud solicitud) {
        // Estado final
    }
}

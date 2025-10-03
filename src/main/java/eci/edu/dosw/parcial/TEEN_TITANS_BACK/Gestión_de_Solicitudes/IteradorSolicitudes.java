package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Solicitudes;

import java.util.List;

public class IteradorSolicitudes {
    private List<Solicitud> solicitudes;
    private Solicitud.EstadoSolicitud estadoFiltro; // Usar la referencia completa
    private int posicion;

    public IteradorSolicitudes(List<Solicitud> solicitudes, Solicitud.EstadoSolicitud estado) {
        this.solicitudes = solicitudes;
        this.estadoFiltro = estado;
        this.posicion = 0;
    }

    public boolean hasNext() {
        while (posicion < solicitudes.size()) {
            Solicitud solicitud = solicitudes.get(posicion);
            if (solicitud.getEstado().getClass().equals(estadoFiltro.getClass())) {
                return true;
            }
            posicion++;
        }
        return false;
    }

    public Solicitud next() {
        if (hasNext()) {
            return solicitudes.get(posicion++);
        }
        return null;
    }
}
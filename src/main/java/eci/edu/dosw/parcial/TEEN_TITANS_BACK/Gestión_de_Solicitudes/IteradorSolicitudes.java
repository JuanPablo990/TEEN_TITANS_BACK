package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Solicitudes;

import java.util.Iterator;
import java.util.List;

public class IteradorSolicitudes implements Iterator<Solicitud> {
    private List<Solicitud> solicitudes;
    private EstadoSolicitud estado;
    private int posicion = 0;

    public IteradorSolicitudes(List<Solicitud> solicitudes, EstadoSolicitud estado) {
        this.solicitudes = solicitudes;
        this.estado = estado;
    }

    @Override
    public boolean hasNext() {
        while (posicion < solicitudes.size()) {
            if (solicitudes.get(posicion).getEstado().getClass().equals(estado.getClass())) {
                return true;
            }
            posicion++;
        }
        return false;
    }

    @Override
    public Solicitud next() {
        return solicitudes.get(posicion++);
    }
}

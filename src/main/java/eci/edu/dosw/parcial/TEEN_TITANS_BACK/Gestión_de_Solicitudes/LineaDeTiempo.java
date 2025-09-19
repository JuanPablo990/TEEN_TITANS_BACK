package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Solicitudes;

import java.util.List;

public class LineaDeTiempo {
    private final List<Trazabilidad> historial;

    public LineaDeTiempo(List<Trazabilidad> historial) {
        this.historial = historial;
    }

    public void mostrar() {
        historial.forEach(h ->
                System.out.println(h.getFecha() + " -> " + h.getEstado())
        );
    }
}

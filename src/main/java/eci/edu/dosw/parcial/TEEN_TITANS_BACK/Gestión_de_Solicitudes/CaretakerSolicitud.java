package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Solicitudes;

import java.util.Stack;

public class CaretakerSolicitud {
    private Stack<MementoSolicitud> historial = new Stack<>();

    public void guardar(MementoSolicitud memento) {
        historial.push(memento);
    }

    public MementoSolicitud restaurar() {
        return historial.isEmpty() ? null : historial.pop();
    }
}

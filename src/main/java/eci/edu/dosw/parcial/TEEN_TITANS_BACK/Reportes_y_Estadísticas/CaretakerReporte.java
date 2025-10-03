package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Reportes_y_Estadísticas;

import java.util.Stack;

public class CaretakerReporte {
    private Stack<MementoReporte> historial = new Stack<>();

    public void guardar(MementoReporte memento) {
        historial.push(memento);
    }

    public MementoReporte restaurar() {
        return historial.isEmpty() ? null : historial.pop();
    }
}

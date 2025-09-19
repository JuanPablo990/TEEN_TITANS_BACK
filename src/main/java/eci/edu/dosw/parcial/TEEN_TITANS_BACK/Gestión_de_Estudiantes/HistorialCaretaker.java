package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Estudiantes;

import java.util.*;
import java.util.concurrent.*;

class HistorialCaretaker {
    private final Deque<HistorialMemento> historial = new ArrayDeque<>();

    public void guardarEstado(HistorialMemento memento) {
        historial.push(memento);
    }

    public HistorialMemento restaurarEstado() {
        return historial.isEmpty() ? null : historial.pop();
    }
}
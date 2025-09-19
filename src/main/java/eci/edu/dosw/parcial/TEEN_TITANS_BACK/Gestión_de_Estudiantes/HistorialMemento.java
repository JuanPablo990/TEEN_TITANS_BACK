package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Estudiantes;

import java.util.*;

public class HistorialMemento {
    private final List<MateriaInscrita> estadoHorario;

    public HistorialMemento(List<MateriaInscrita> estadoHorario) {
        this.estadoHorario = new ArrayList<>(estadoHorario);
    }

    public List<MateriaInscrita> getEstadoHorario() {
        return estadoHorario;
    }
}
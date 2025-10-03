package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Grupos_y_Materias;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

public class ListaEspera {
    private static final Logger logger = Logger.getLogger(ListaEspera.class.getName());

    private final Queue<String> estudiantesEnEspera = new LinkedList<>();

    public void agregarALista(String idEstudiante) {
        estudiantesEnEspera.add(idEstudiante);
        logger.info(() -> "Estudiante " + idEstudiante + " agregado a lista de espera.");
    }

    public String siguienteEnEspera() {
        String estudiante = estudiantesEnEspera.poll();
        logger.info(() -> "Sacando estudiante de lista de espera: " + estudiante);
        return estudiante;
    }
}

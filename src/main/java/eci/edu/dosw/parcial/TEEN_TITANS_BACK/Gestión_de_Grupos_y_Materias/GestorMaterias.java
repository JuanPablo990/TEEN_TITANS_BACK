package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Grupos_y_Materias;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class GestorMaterias {
    private static final Logger logger = Logger.getLogger(GestorMaterias.class.getName());
    private static GestorMaterias instancia;

    private final Map<String, Materia> materias = new HashMap<>();

    private GestorMaterias() {
        logger.info("GestorMaterias inicializado (Singleton).");
    }

    public static synchronized GestorMaterias getInstancia() {
        if (instancia == null) {
            instancia = new GestorMaterias();
        }
        return instancia;
    }

    public void registrarMateria(Materia materia) {
        materias.put(materia.getCodigo(), materia);
        logger.info(() -> "Materia registrada: " + materia.getNombre());
    }

    public Materia consultarMateria(String codigo) {
        return materias.get(codigo);
    }
}

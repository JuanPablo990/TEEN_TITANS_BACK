package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Grupos_y_Materias;

import java.util.logging.Logger;

public class Profesor {
    private static final Logger logger = Logger.getLogger(Profesor.class.getName());

    private final String idProfesor;
    private final String nombre;

    public Profesor(String idProfesor, String nombre) {
        this.idProfesor = idProfesor;
        this.nombre = nombre;
        logger.info(() -> "Profesor registrado: " + nombre + " (ID: " + idProfesor + ")");
    }

    public String getIdProfesor() {
        return idProfesor;
    }

    public String getNombre() {
        return nombre;
    }
}

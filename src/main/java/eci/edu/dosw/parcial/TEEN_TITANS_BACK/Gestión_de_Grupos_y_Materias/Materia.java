package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Grupos_y_Materias;

import java.util.logging.Logger;

public class Materia {
    private static final Logger logger = Logger.getLogger(Materia.class.getName());

    private final String codigo;
    private final String nombre;

    public Materia(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
        logger.info(() -> "Materia creada: " + codigo + " - " + nombre);
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }
}

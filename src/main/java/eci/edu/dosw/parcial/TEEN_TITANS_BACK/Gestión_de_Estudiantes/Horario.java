package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Estudiantes;

import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Logger;

class Horario {
    private static final Logger logger = Logger.getLogger(Horario.class.getName());

    private Hashtable<String, MateriaInscrita> materias; // clave: código materia

    public Horario() {
        materias = new Hashtable<>();
    }

    public void agregarMateria(MateriaInscrita materia) {
        materias.put(materia.getCodigo(), materia);
        logger.info("Materia agregada: " + materia.getCodigo());
    }

    public Collection<MateriaInscrita> getMaterias() {
        return materias.values();
    }
}



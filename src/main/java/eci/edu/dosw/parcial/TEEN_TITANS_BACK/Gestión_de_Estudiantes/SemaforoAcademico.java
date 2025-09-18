package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Estudiantes;

import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Logger;

class SemaforoAcademico {
    private static final Logger logger = Logger.getLogger(SemaforoAcademico.class.getName());

    public String calcularEstado(List<MateriaInscrita> materias) {
        int creditos = materias.stream().mapToInt(MateriaInscrita::getCreditos).sum();
        logger.info("Calculando semáforo para créditos: " + creditos);
        if (creditos < 10) return "ROJO";
        if (creditos < 20) return "AMARILLO";
        return "VERDE";
    }
}
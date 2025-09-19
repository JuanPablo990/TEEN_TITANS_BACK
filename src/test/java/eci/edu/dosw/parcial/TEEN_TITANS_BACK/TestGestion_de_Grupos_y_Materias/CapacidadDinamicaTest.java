package eci.edu.dosw.parcial.TEEN_TITANS_BACK.TestGestion_de_Grupos_y_Materias;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Grupos_y_Materias.CapacidadDinamica;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CapacidadDinamicaTest {

    @Test
    void testCalcularCupoExtendido() {
        CapacidadDinamica cd = new CapacidadDinamica(20);
        int nuevoCupo = cd.calcularCupoExtendido(50);
        assertEquals(60, nuevoCupo);
    }
}


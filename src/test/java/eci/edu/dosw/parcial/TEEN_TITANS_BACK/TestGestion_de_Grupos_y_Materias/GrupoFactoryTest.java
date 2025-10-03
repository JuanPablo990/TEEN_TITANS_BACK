package eci.edu.dosw.parcial.TEEN_TITANS_BACK.TestGestion_de_Grupos_y_Materias;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Grupos_y_Materias.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GrupoFactoryTest {

    @Test
    void testCrearGrupoSinCapacidadExtra() {
        Materia materia = new Materia("MAT303", "Álgebra");
        Profesor profesor = new Profesor("P6", "Luis");

        Grupo grupo = GrupoFactory.crearGrupo("G6", materia, profesor, 30, null);

        assertEquals("G6", grupo.getIdGrupo());
    }

    @Test
    void testCrearGrupoConCapacidadExtra() {
        Materia materia = new Materia("INF101", "Programación");
        Profesor profesor = new Profesor("P7", "Marta");
        CapacidadDinamica capacidad = new CapacidadDinamica(10);

        Grupo grupo = GrupoFactory.crearGrupo("G7", materia, profesor, 40, capacidad);

        assertEquals("G7", grupo.getIdGrupo());
    }
}

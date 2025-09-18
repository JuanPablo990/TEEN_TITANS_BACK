package eci.edu.dosw.parcial.TEEN_TITANS_BACK.TestGestion_de_Grupos_y_Materias;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Grupos_y_Materias.Grupo;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Grupos_y_Materias.Materia;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Grupos_y_Materias.Profesor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GrupoTest {

    @Test
    void testGetIdGrupo() {
        Grupo grupo = new Grupo("G1", new Materia("MAT202", "Cálculo"), new Profesor("P1", "Carlos"), 2);
        assertEquals("G1", grupo.getIdGrupo());
    }

    @Test
    void testInscribirEstudianteDentroDelCupo() {
        Grupo grupo = new Grupo("G2", new Materia("HIS101", "Historia"), new Profesor("P2", "Laura"), 2);

        assertTrue(grupo.inscribirEstudiante("E1"));
        assertTrue(grupo.inscribirEstudiante("E2"));
    }

    @Test
    void testInscribirEstudianteFueraDelCupo() {
        Grupo grupo = new Grupo("G3", new Materia("BIO101", "Biología"), new Profesor("P3", "María"), 1);

        assertTrue(grupo.inscribirEstudiante("E1"));
        assertFalse(grupo.inscribirEstudiante("E2")); // se va a lista de espera
    }

    @Test
    void testMostrarInformacionNoLanzaExcepcion() {
        Grupo grupo = new Grupo("G4", new Materia("FIS101", "Física"), new Profesor("P4", "José"), 2);
        assertDoesNotThrow(grupo::mostrarInformacion);
    }
}

package eci.edu.dosw.parcial.TEEN_TITANS_BACK.TestGestion_de_Grupos_y_Materias;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Grupos_y_Materias.Grupo;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Grupos_y_Materias.GrupoComposite;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Grupos_y_Materias.Materia;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Grupos_y_Materias.Profesor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class GrupoCompositeTest {

    @Test
    void testAgregarGrupoYMostrarInformacion() {
        GrupoComposite composite = new GrupoComposite("Composite 1");
        Grupo grupo = new Grupo("G5", new Materia("QUI101", "Química"), new Profesor("P5", "Ana"), 2);

        composite.agregarGrupo(grupo);

        assertDoesNotThrow(composite::mostrarInformacion);
    }
}

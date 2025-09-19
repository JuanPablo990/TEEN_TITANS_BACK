package eci.edu.dosw.parcial.TEEN_TITANS_BACK.TestGestion_de_Grupos_y_Materias;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Grupos_y_Materias.Materia;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MateriaTest {

    @Test
    void testGetCodigoYNombre() {
        Materia materia = new Materia("MAT404", "Estadística");

        assertEquals("MAT404", materia.getCodigo());
        assertEquals("Estadística", materia.getNombre());
    }
}

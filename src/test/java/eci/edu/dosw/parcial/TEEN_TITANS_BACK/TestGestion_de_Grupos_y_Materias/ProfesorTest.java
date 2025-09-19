package eci.edu.dosw.parcial.TEEN_TITANS_BACK.TestGestion_de_Grupos_y_Materias;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Grupos_y_Materias.Profesor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProfesorTest {

    @Test
    void testGetIdProfesorYNombre() {
        Profesor profesor = new Profesor("P8", "Andrés");

        assertEquals("P8", profesor.getIdProfesor());
        assertEquals("Andrés", profesor.getNombre());
    }
}

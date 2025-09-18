package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Tests_Gestion_de_Estudiantes;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Estudiantes.MateriaInscrita;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MateriaInscritaTest {

    @Test
    void testGetters() {
        MateriaInscrita m = new MateriaInscrita("MAT101", "Matemáticas", 3);

        assertEquals("MAT101", m.getCodigo());
        assertEquals("Matemáticas", m.getNombre());
        assertEquals(3, m.getCreditos());
    }
}

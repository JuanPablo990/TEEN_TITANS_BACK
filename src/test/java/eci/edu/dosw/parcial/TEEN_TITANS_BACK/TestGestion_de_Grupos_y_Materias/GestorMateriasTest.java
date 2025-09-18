package eci.edu.dosw.parcial.TEEN_TITANS_BACK.TestGestion_de_Grupos_y_Materias;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Grupos_y_Materias.GestorMaterias;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Grupos_y_Materias.Materia;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GestorMateriasTest {

    @Test
    void testSingleton() {
        GestorMaterias g1 = GestorMaterias.getInstancia();
        GestorMaterias g2 = GestorMaterias.getInstancia();
        assertSame(g1, g2);
    }

    @Test
    void testRegistrarYConsultarMateria() {
        GestorMaterias gestor = GestorMaterias.getInstancia();
        Materia materia = new Materia("MAT101", "Matemáticas");
        gestor.registrarMateria(materia);

        Materia encontrada = gestor.consultarMateria("MAT101");
        assertEquals("Matemáticas", encontrada.getNombre());
    }
}


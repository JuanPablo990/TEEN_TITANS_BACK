package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Tests_Gestion_de_Estudiantes;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Estudiantes.Horario;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Estudiantes.MateriaInscrita;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HorarioTest {

    @Test
    void testAgregarYObtenerMaterias() {
        Horario horario = new Horario();
        MateriaInscrita m = new MateriaInscrita("MAT101", "Matemáticas", 3);

        horario.agregarMateria(m);

        assertEquals(1, horario.getMaterias().size());
    }
}

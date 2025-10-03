package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Tests_Gestion_de_Estudiantes;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Estudiantes.HistorialCaretaker;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Estudiantes.HistorialMemento;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Estudiantes.MateriaInscrita;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class HistorialCaretakerTest {

    @Test
    void testGuardarYRestaurarEstado() {
        HistorialCaretaker caretaker = new HistorialCaretaker();
        MateriaInscrita m = new MateriaInscrita("MAT101", "Matemáticas", 3);
        HistorialMemento memento = new HistorialMemento(List.of(m));

        caretaker.guardarEstado(memento);
        HistorialMemento restaurado = caretaker.restaurarEstado();

        assertNotNull(restaurado);
        assertEquals(1, restaurado.getEstadoHorario().size());
    }

    @Test
    void testRestaurarEstadoVacio() {
        HistorialCaretaker caretaker = new HistorialCaretaker();
        assertNull(caretaker.restaurarEstado());
    }
}

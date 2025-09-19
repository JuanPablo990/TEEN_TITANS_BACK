package eci.edu.dosw.parcial.TEEN_TITANS_BACK.TestGestion_de_Grupos_y_Materias;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Grupos_y_Materias.ListaEspera;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ListaEsperaTest {

    @Test
    void testAgregarYSacarEstudiante() {
        ListaEspera lista = new ListaEspera();

        lista.agregarALista("E1");
        lista.agregarALista("E2");

        assertEquals("E1", lista.siguienteEnEspera());
        assertEquals("E2", lista.siguienteEnEspera());
        assertNull(lista.siguienteEnEspera());
    }
}

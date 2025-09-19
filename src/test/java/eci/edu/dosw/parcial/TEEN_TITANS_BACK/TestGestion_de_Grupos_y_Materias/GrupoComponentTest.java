package eci.edu.dosw.parcial.TEEN_TITANS_BACK.TestGestion_de_Grupos_y_Materias;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Grupos_y_Materias.*;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class GrupoComponentTest {

    @Test
    void testGrupoImplementsGrupoComponent() {
        Grupo grupo = new Grupo("G-test", new Materia("MAT000", "Prueba"), new Profesor("Ptest", "Profesor"), 10);
        assertTrue(grupo instanceof GrupoComponent, "Grupo debe implementar GrupoComponent");
    }

    @Test
    void testGrupoCompositeImplementsGrupoComponent() {
        GrupoComposite composite = new GrupoComposite("Composite-test");
        assertTrue(composite instanceof GrupoComponent, "GrupoComposite debe implementar GrupoComponent");
    }

    @Test
    void testMostrarInformacionViaInterfaceNoLanzaExcepcion() {
        GrupoComponent comp1 = new Grupo("G-intf", new Materia("MAT111", "X"), new Profesor("P1", "X"), 5);
        GrupoComponent comp2 = new GrupoComposite("Composite-intf");
        assertDoesNotThrow(comp1::mostrarInformacion, "Invocar mostrarInformacion en Grupo vía interfaz no debe lanzar excepción");
        assertDoesNotThrow(comp2::mostrarInformacion, "Invocar mostrarInformacion en GrupoComposite vía interfaz no debe lanzar excepción");
    }

    @Test
    void testAnonymousImplementationIsCalled() {
        AtomicBoolean invoked = new AtomicBoolean(false);

        GrupoComponent anon = new GrupoComponent() {
            @Override
            public void mostrarInformacion() {
                invoked.set(true);
            }
        };

        // Llamada y verificación
        assertFalse(invoked.get(), "Antes de la llamada, invoked debe ser false");
        anon.mostrarInformacion();
        assertTrue(invoked.get(), "Después de la llamada, invoked debe ser true (método ejecutado)");
    }
}


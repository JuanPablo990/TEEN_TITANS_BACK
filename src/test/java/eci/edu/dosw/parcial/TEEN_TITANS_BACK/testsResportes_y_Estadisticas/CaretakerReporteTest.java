package eci.edu.dosw.parcial.TEEN_TITANS_BACK.testsResportes_y_Estadisticas;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Reportes_y_Estadísticas.CaretakerReporte;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Reportes_y_Estadísticas.MementoReporte;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CaretakerReporteTest {

    @Test
    void testGuardarYRestaurar() {
        CaretakerReporte caretaker = new CaretakerReporte();
        MementoReporte m1 = new MementoReporte("conf1");
        MementoReporte m2 = new MementoReporte("conf2");

        caretaker.guardar(m1);
        caretaker.guardar(m2);

        assertEquals("conf2", caretaker.restaurar().getConfiguracion());
        assertEquals("conf1", caretaker.restaurar().getConfiguracion());
    }

    @Test
    void testRestaurarCuandoVacio() {
        CaretakerReporte caretaker = new CaretakerReporte();
        assertNull(caretaker.restaurar());
    }
}


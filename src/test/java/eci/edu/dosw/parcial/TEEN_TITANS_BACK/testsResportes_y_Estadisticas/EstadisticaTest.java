package eci.edu.dosw.parcial.TEEN_TITANS_BACK.testsResportes_y_Estadisticas;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Reportes_y_Estadísticas.Estadistica;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EstadisticaTest {

    @Test
    void testCalcularTasaAprobacion() {
        Estadistica estadistica = new Estadistica();
        List<Integer> notas = Arrays.asList(2, 3, 4, 5);
        double tasa = estadistica.calcularTasaAprobacion(notas);
        assertEquals(0.75, tasa, 0.001);
    }

    @Test
    void testCalcularPromedio() {
        Estadistica estadistica = new Estadistica();
        List<Integer> notas = Arrays.asList(3, 4, 5);
        double promedio = estadistica.calcularPromedio(notas);
        assertEquals(4.0, promedio, 0.001);
    }

    @Test
    void testPromedioListaVacia() {
        Estadistica estadistica = new Estadistica();
        double promedio = estadistica.calcularPromedio(List.of());
        assertEquals(0.0, promedio);
    }
}

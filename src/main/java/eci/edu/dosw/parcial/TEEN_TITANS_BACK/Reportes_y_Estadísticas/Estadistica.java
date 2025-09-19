package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Reportes_y_Estadísticas;

import java.util.List;

public class Estadistica {
    public double calcularTasaAprobacion(List<Integer> notas) {
        long aprobados = notas.stream().filter(n -> n >= 3).count();
        return (double) aprobados / notas.size();
    }

    public double calcularPromedio(List<Integer> notas) {
        return notas.stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }
}

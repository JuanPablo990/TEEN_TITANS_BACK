package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Tests_Gestion_de_Estudiantes;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Estudiantes.MateriaInscrita;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Estudiantes.SemaforoAcademico;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class SemaforoAcademicoTest {

    @Test
    void testCalcularEstadoRojo() {
        SemaforoAcademico semaforo = new SemaforoAcademico();
        MateriaInscrita m = new MateriaInscrita("MAT101", "Matemáticas", 3);

        String estado = semaforo.calcularEstado(List.of(m));
        assertEquals("ROJO", estado);
    }

    @Test
    void testCalcularEstadoAmarillo() {
        SemaforoAcademico semaforo = new SemaforoAcademico();
        MateriaInscrita m1 = new MateriaInscrita("MAT101", "Matemáticas", 10);

        String estado = semaforo.calcularEstado(List.of(m1));
        assertEquals("AMARILLO", estado);
    }

    @Test
    void testCalcularEstadoVerde() {
        SemaforoAcademico semaforo = new SemaforoAcademico();
        MateriaInscrita m1 = new MateriaInscrita("MAT101", "Matemáticas", 15);
        MateriaInscrita m2 = new MateriaInscrita("FIS101", "Física", 10);

        String estado = semaforo.calcularEstado(List.of(m1, m2));
        assertEquals("VERDE", estado);
    }
}

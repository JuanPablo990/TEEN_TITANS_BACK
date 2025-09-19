package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Tests_Gestion_de_Estudiantes;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Estudiantes.Estudiante;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Estudiantes.MateriaInscrita;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Estudiantes.SolicitudCambio;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EstudianteTest {

    @Test
    void testAgregarYObtenerSolicitudesPendientes() {
        Estudiante estudiante = new Estudiante("1", "Ana");
        MateriaInscrita m1 = new MateriaInscrita("MAT101", "Matemáticas", 3);
        MateriaInscrita m2 = new MateriaInscrita("FIS101", "Física", 3);
        SolicitudCambio solicitud = new SolicitudCambio("S1", m1, m2);

        estudiante.agregarSolicitud(solicitud);

        assertEquals(1, estudiante.getSolicitudesPendientes().size());
    }

    @Test
    void testSolicitudesPendientesFiltraPorEstado() {
        Estudiante estudiante = new Estudiante("2", "Pedro");
        MateriaInscrita m1 = new MateriaInscrita("MAT101", "Matemáticas", 3);
        MateriaInscrita m2 = new MateriaInscrita("FIS101", "Física", 3);

        SolicitudCambio s1 = new SolicitudCambio("S1", m1, m2);
        SolicitudCambio s2 = new SolicitudCambio("S2", m1, m2);
        s2.aprobar();

        estudiante.agregarSolicitud(s1);
        estudiante.agregarSolicitud(s2);

        assertEquals(1, estudiante.getSolicitudesPendientes().size());
    }
}

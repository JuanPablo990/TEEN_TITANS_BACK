package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Tests_Gestion_de_Estudiantes;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Estudiantes.MateriaInscrita;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Estudiantes.SolicitudCambio;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SolicitudCambioTest {

    @Test
    void testEstadoInicialPendiente() {
        MateriaInscrita m1 = new MateriaInscrita("MAT101", "Matemáticas", 3);
        MateriaInscrita m2 = new MateriaInscrita("FIS101", "Física", 3);
        SolicitudCambio solicitud = new SolicitudCambio("S1", m1, m2);

        assertEquals("PENDIENTE", solicitud.getEstado());
    }

    @Test
    void testAprobarSolicitud() {
        MateriaInscrita m1 = new MateriaInscrita("MAT101", "Matemáticas", 3);
        MateriaInscrita m2 = new MateriaInscrita("FIS101", "Física", 3);
        SolicitudCambio solicitud = new SolicitudCambio("S1", m1, m2);

        solicitud.aprobar();
        assertEquals("APROBADA", solicitud.getEstado());
    }

    @Test
    void testRechazarSolicitud() {
        MateriaInscrita m1 = new MateriaInscrita("MAT101", "Matemáticas", 3);
        MateriaInscrita m2 = new MateriaInscrita("FIS101", "Física", 3);
        SolicitudCambio solicitud = new SolicitudCambio("S1", m1, m2);

        solicitud.rechazar();
        assertEquals("RECHAZADA", solicitud.getEstado());
    }
}


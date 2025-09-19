package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Tests_Gestion_de_Estudiantes;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Estudiantes.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SolicitudCommandTest {

    @Test
    void testCrearSolicitudCommandEjecutar() {
        Estudiante estudiante = new Estudiante("10", "Camila");
        MateriaInscrita m1 = new MateriaInscrita("MAT202", "Cálculo II", 4);
        MateriaInscrita m2 = new MateriaInscrita("HIS101", "Historia", 3);
        SolicitudCambio solicitud = new SolicitudCambio("SC1", m1, m2);

        SolicitudCommand cmd = new CrearSolicitudCommand(estudiante, solicitud);
        cmd.ejecutar();

        assertEquals(1, estudiante.getSolicitudesPendientes().size());
        assertEquals("PENDIENTE", estudiante.getSolicitudesPendientes().get(0).getEstado());
    }

}


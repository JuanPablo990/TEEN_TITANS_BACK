package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Tests_Gestion_de_Estudiantes;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Estudiantes.CrearSolicitudCommand;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Estudiantes.Estudiante;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Estudiantes.MateriaInscrita;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Estudiantes.SolicitudCambio;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CrearSolicitudCommandTest {

    @Test
    void testEjecutarAgregaSolicitud() {
        Estudiante estudiante = new Estudiante("1", "Juan");
        MateriaInscrita m1 = new MateriaInscrita("MAT101", "Matemáticas", 3);
        MateriaInscrita m2 = new MateriaInscrita("FIS101", "Física", 3);
        SolicitudCambio solicitud = new SolicitudCambio("S1", m1, m2);

        CrearSolicitudCommand cmd = new CrearSolicitudCommand(estudiante, solicitud);
        cmd.ejecutar();

        assertEquals(1, estudiante.getSolicitudesPendientes().size());
    }
}

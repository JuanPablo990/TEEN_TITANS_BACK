package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Estudiantes;

import java.util.*;
import java.util.concurrent.*;

public class CrearSolicitudCommand implements SolicitudCommand {
    private Estudiante estudiante;
    private SolicitudCambio solicitud;

    public CrearSolicitudCommand(Estudiante estudiante, SolicitudCambio solicitud) {
        this.estudiante = estudiante;
        this.solicitud = solicitud;
    }

    @Override
    public void ejecutar() {
        estudiante.agregarSolicitud(solicitud);
    }
}
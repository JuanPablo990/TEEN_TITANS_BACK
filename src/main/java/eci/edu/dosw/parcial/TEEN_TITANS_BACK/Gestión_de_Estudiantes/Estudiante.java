package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Estudiantes;

import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Estudiante {
    private static final Logger logger = Logger.getLogger(Estudiante.class.getName());

    private String id;
    private String nombre;
    public Horario horario;
    private SemaforoAcademico semaforo;
    private List<SolicitudCambio> solicitudes;

    public Estudiante(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.horario = new Horario();
        this.semaforo = new SemaforoAcademico();
        this.solicitudes = new ArrayList<>();
        logger.info("Estudiante creado: " + nombre);
    }

    public void agregarSolicitud(SolicitudCambio solicitud) {
        solicitudes.add(solicitud);
        logger.info("Solicitud agregada para estudiante " + nombre);
    }

    public List<SolicitudCambio> getSolicitudesPendientes() {
        return solicitudes.stream()
                .filter(s -> s.getEstado().equals("PENDIENTE"))
                .collect(Collectors.toList());
    }

    public String getNombre() {
        return nombre;
    }
}

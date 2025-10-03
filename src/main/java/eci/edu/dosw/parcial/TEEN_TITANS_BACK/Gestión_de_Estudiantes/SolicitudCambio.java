package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Estudiantes;

import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class  SolicitudCambio {
    private static final Logger logger = Logger.getLogger(SolicitudCambio.class.getName());

    private String id;
    private String estado;
    private MateriaInscrita materiaProblema;
    private MateriaInscrita materiaSugerida;

    public SolicitudCambio(String id, MateriaInscrita problema, MateriaInscrita sugerida) {
        this.id = id;
        this.materiaProblema = problema;
        this.materiaSugerida = sugerida;
        this.estado = "PENDIENTE";
        logger.info("Solicitud creada: " + id);
    }

    public String getEstado() { return estado; }
    public void aprobar() { estado = "APROBADA"; logger.info("Solicitud " + id + " aprobada."); }
    public void rechazar() { estado = "RECHAZADA"; logger.info("Solicitud " + id + " rechazada."); }
}

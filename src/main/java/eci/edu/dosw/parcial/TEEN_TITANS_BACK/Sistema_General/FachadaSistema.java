package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Sistema_General;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Estudiantes.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_por_Decanatura.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Grupos_y_Materias.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Solicitudes.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Reportes_y_Estadísticas.*;

public class FachadaSistema {
    private static final Logger logger = Logger.getLogger(FachadaSistema.class.getName());

    private final List<Estudiante> estudiantesRegistrados = new ArrayList<>();
    private final GestorSolicitudes gestorSolicitudes;
    private final GestorMaterias gestorMaterias;
    private final DashboardDecanatura dashboardDecanatura;
    private final CadenaAprobacion cadenaAprobacion;

    public FachadaSistema() {
        this.gestorSolicitudes = new GestorSolicitudes();
        this.gestorMaterias = GestorMaterias.getInstancia();
        this.dashboardDecanatura = new DashboardDecanatura();
        this.cadenaAprobacion = new CadenaAprobacion();
        this.cadenaAprobacion.construirCadena();
        logger.info("FachadaSistema inicializada.");
    }

    public boolean autenticarEstudiante(String usuario, String clave) {
        Autenticacion auth = new Autenticacion();
        boolean ok = auth.autenticar(usuario, clave);
        logger.info(() -> "Autenticación para " + usuario + ": " + ok);
        return ok;
    }

    public void crearSolicitudCambioParaEstudiante(Estudiante estudiante,
                                                   String idSolicitud,
                                                   MateriaInscrita materiaProblema,
                                                   MateriaInscrita materiaSugerida) {
        SolicitudCambio sc = new SolicitudCambio(idSolicitud, materiaProblema, materiaSugerida);
        CrearSolicitudCommand comando = new CrearSolicitudCommand(estudiante, sc);
        comando.ejecutar();
        logger.info(() -> "SolicitudCambio creada y añadida al estudiante " + estudiante);
        SolicitudSimple registroSimple = new SolicitudSimple();
        gestorSolicitudes.registrarSolicitud(registroSimple);
        logger.info(() -> "SolicitudSimple registrada en GestorSolicitudes (id interno: " + registroSimple.getId() + ")");
    }

    public String verSemaforoAcademico(Estudiante estudiante) {
        SemaforoAcademico sem = new SemaforoAcademico();
        Horario horario = estudiante != null ? estudiante.horario : null;
        Collection<MateriaInscrita> materias;
        if (horario != null) {
            materias = (Collection<MateriaInscrita>) horario.getMaterias();
        } else {
            materias = new ArrayList<>();
        }
        String estado = sem.calcularEstado(new ArrayList<>(materias));
        logger.info(() -> "Semáforo calculado para estudiante: " + estado);
        return estado;
    }

    public void registrarMateria(String codigo, String nombre) {
        Materia materia = new Materia(codigo, nombre);
        gestorMaterias.registrarMateria(materia);
        logger.info(() -> "Materia registrada en GestorMaterias: " + codigo);
    }

    public Grupo crearGrupo(String idGrupo, Materia materia, Profesor profesor, int cupoMaximo, CapacidadDinamica capacidadExtra) {
        Grupo grupo = GrupoFactory.crearGrupo(idGrupo, materia, profesor, cupoMaximo, capacidadExtra);
        logger.info(() -> "Grupo creado: " + idGrupo + " para materia " + materia.getCodigo());
        return grupo;
    }

    public void registrarSolicitudCentral(Solicitud solicitud) {
        gestorSolicitudes.registrarSolicitud(solicitud);
        logger.info(() -> "Solicitud registrada en GestorSolicitudes (id: " + solicitud.getId() + ")");
    }

    public void procesarSolicitudEnCadena(String idSolicitud, String facultad) {
        cadenaAprobacion.procesarSolicitud(idSolicitud, facultad);
        logger.info(() -> "Procesada solicitud en cadena: " + idSolicitud);
    }

    public IteradorSolicitudes obtenerIteradorPorEstado(Solicitud.EstadoSolicitud estado) {
        return gestorSolicitudes.getIteradorPorEstado(estado);
    }

    public void generarReporteHistorial(String estudianteId) {
        ReporteHistorialEstudiante r = new ReporteHistorialEstudiante(estudianteId);
        r.generar();
        logger.info(() -> "ReporteHistorialEstudiante generado para: " + estudianteId);
    }

    public void generarReporteCompuestoEjemplo() {
        ReporteComposite compuesto = new ReporteComposite();
        compuesto.agregarReporte(new ReporteGruposCriticos());
        compuesto.agregarReporte(new ReporteIndicadoresSatisfaccion());
        compuesto.generar();
        logger.info("Reporte compuesto generado.");
    }

    public DashboardDecanatura getDashboardDecanatura() {
        return dashboardDecanatura;
    }

    public void registrarEstudiante(Estudiante estudiante) {
        if (estudiante == null) {
            logger.warning("Intento de registrar un estudiante nulo.");
            return;
        }
        estudiantesRegistrados.add(estudiante);
        logger.info(() -> "Estudiante registrado: " + estudiante.getNombre());
    }


    public List<Estudiante> getEstudiantesRegistrados() {
        return new ArrayList<>(estudiantesRegistrados); // devuelve copia para seguridad
    }

    public void registrarEstudiante(String idEst, String nombre) {
        Estudiante estudiante = new Estudiante(idEst, nombre);
        registrarEstudiante(estudiante);
    }
}

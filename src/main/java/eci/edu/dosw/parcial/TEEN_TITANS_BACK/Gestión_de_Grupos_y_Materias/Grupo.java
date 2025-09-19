package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Grupos_y_Materias;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Grupo implements GrupoComponent {
    private static final Logger logger = Logger.getLogger(Grupo.class.getName());

    private final String idGrupo;
    private final Materia materia;
    private final Profesor profesor;
    private final int cupoMaximo;
    private final List<String> estudiantesInscritos = new ArrayList<>();
    private final ListaEspera listaEspera = new ListaEspera();

    public Grupo(String idGrupo, Materia materia, Profesor profesor, int cupoMaximo) {
        this.idGrupo = idGrupo;
        this.materia = materia;
        this.profesor = profesor;
        this.cupoMaximo = cupoMaximo;
        logger.info(() -> "Grupo creado: " + idGrupo + " para materia " + materia.getNombre());
    }

    public String getIdGrupo() {
        return idGrupo;
    }

    public boolean inscribirEstudiante(String idEstudiante) {
        if (estudiantesInscritos.size() < cupoMaximo) {
            estudiantesInscritos.add(idEstudiante);
            logger.info(() -> "Estudiante " + idEstudiante + " inscrito en grupo " + idGrupo);
            return true;
        } else {
            listaEspera.agregarALista(idEstudiante);
            logger.warning(() -> "Grupo lleno. Estudiante " + idEstudiante + " enviado a lista de espera en grupo " + idGrupo);
            return false;
        }
    }

    @Override
    public void mostrarInformacion() {
        logger.info(() -> "Grupo " + idGrupo + " - " + materia.getNombre() + " (Profesor: " + profesor.getNombre() + ")");
    }
}

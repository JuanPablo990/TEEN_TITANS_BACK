package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Grupos_y_Materias;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class GrupoComposite implements GrupoComponent {
    private static final Logger logger = Logger.getLogger(GrupoComposite.class.getName());

    private final List<GrupoComponent> grupos = new ArrayList<>();
    private final String nombreComposite;

    public GrupoComposite(String nombreComposite) {
        this.nombreComposite = nombreComposite;
        logger.info(() -> "GrupoComposite creado: " + nombreComposite);
    }

    public void agregarGrupo(GrupoComponent grupo) {
        grupos.add(grupo);
        logger.info(() -> "Grupo agregado a " + nombreComposite);
    }

    @Override
    public void mostrarInformacion() {
        logger.info(() -> "Mostrando información de " + nombreComposite);
        for (GrupoComponent grupo : grupos) {
            grupo.mostrarInformacion();
        }
    }
}

package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_por_Decanatura;

import java.util.logging.Logger;

public class CadenaAprobacion {
    private static final Logger logger = Logger.getLogger(CadenaAprobacion.class.getName());
    private EvaluadorSolicitud inicioCadena;

    public void construirCadena() {
        EvaluadorSolicitud valFacultad = new ValidadorFacultad();
        EvaluadorSolicitud valCalendario = new ValidadorCalendario();
        EvaluadorSolicitud valCupo = new ValidadorCupo();

        valFacultad.setSiguiente(valCalendario);
        valCalendario.setSiguiente(valCupo);

        this.inicioCadena = valFacultad;
        logger.info("Cadena de aprobación construida.");
    }

    public void procesarSolicitud(String idSolicitud, String facultad) {
        if (inicioCadena != null) {
            inicioCadena.evaluar(idSolicitud, facultad);
        } else {
            logger.warning("Cadena de aprobación no inicializada.");
        }
    }
}

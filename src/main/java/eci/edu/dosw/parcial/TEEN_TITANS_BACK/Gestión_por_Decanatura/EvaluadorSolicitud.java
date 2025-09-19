package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_por_Decanatura;

import java.util.logging.Logger;

public abstract class EvaluadorSolicitud {
    protected EvaluadorSolicitud siguiente;
    protected static final Logger logger = Logger.getLogger(EvaluadorSolicitud.class.getName());

    public void setSiguiente(EvaluadorSolicitud siguiente) {
        this.siguiente = siguiente;
    }

    public abstract void evaluar(String idSolicitud, String facultad);
}

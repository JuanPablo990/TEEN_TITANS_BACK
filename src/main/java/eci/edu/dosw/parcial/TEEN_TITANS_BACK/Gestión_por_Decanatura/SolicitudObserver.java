package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_por_Decanatura;

public interface SolicitudObserver {
    void notificarCambioEstado(String idSolicitud, String nuevoEstado);
}

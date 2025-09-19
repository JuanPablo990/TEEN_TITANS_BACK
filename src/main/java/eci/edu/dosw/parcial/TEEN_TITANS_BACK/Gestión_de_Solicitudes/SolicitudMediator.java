package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Solicitudes;

public interface SolicitudMediator {
    void registrarSolicitud(Solicitud solicitud);
    void notificarCambioEstado(Solicitud solicitud);
}

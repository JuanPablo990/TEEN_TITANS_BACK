package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Solicitudes;

import java.time.LocalDateTime;

public class Trazabilidad {
    private final String estado;
    private final LocalDateTime fecha;

    public Trazabilidad(String estado) {
        this.estado = estado;
        this.fecha = LocalDateTime.now();
    }

    public String getEstado() { return estado; }
    public LocalDateTime getFecha() { return fecha; }
}

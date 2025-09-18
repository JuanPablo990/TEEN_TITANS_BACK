package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Estudiantes;

import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Logger;

class Autenticacion {
    private static final Logger logger = Logger.getLogger(Autenticacion.class.getName());
    private final Hashtable<String, String> credenciales; // usuario -> password

    public Autenticacion() {
        credenciales = new Hashtable<>();
    }

    public void registrarUsuario(String usuario, String password) {
        credenciales.put(usuario, password);
        logger.info("Usuario registrado: " + usuario);
    }

    public boolean autenticar(String usuario, String password) {
        boolean exito = credenciales.containsKey(usuario) && credenciales.get(usuario).equals(password);
        logger.info("Autenticación " + (exito ? "exitosa" : "fallida") + " para usuario " + usuario);
        return exito;
    }
}

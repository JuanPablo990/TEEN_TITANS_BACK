package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

/**
 * Representa un administrador del sistema con privilegios y objetivos específicos.
 * Hereda de la clase User y añade funcionalidades administrativas.
 */
public class Administrator extends User {
    private String privilegios;
    private String gabTrivingGoal;

    /**
     * Obtiene los privilegios del administrador.
     *
     * @return Privilegios del administrador
     */
    public String getPrivilegios() {
        return privilegios;
    }

    /**
     * Obtiene el objetivo de gestión del administrador.
     *
     * @return Objetivo de gestión
     */
    public String getGabTrivingGoal() {
        return gabTrivingGoal;
    }
}
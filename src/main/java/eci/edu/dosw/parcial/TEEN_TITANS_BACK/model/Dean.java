package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

/**
 * Representa un decano asociado a una facultad específica.
 * Hereda de la clase User y añade información sobre la facultad que dirige.
 */
public class Dean extends User {
    private String faculty;

    /**
     * Obtiene la facultad asociada al decano.
     *
     * @return Facultad del decano
     */
    public String getFaculty() {
        return faculty;
    }
}
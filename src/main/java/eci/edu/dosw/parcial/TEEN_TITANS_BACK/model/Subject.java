package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

/**
 * Materia academica del sistema.
 */
public class Subject {
    private int group;
    private String name;
    private int credits;
    private int quotas;
    private String teacher;
    private int registered;
    private String classTime;

    /**
     * Obtiene el grupo de la materia.
     * @return Grupo de la materia
     */
    public int getGroup() {
        return group;
    }

    /**
     * Obtiene el nombre de la materia.
     * @return Nombre de la materia
     */
    public String getName() {
        return name;
    }

    /**
     * Obtiene los creditos de la materia.
     * @return Creditos de la materia
     */
    public int getCredits() {
        return credits;
    }

    /**
     * Obtiene las cupos disponibles de la materia.
     * @return Cupos disponibles
     */
    public int getQuotas() {
        return quotas;
    }

    /**
     * Obtiene el profesor de la materia.
     * @return Profesor de la materia
     */
    public String getTeacher() {
        return teacher;
    }

    /**
     * Obtiene la cantidad de estudiantes registrados.
     * @return Estudiantes registrados
     */
    public int getRegistered() {
        return registered;
    }

    /**
     * Obtiene el horario de clase.
     * @return Horario de clase
     */
    public String getClassTime() {
        return classTime;
    }
}
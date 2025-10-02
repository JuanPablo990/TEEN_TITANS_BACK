package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

/**
 * Informacion academica del estudiante.
 */
public class Student {
    private int standard;
    private String career;
    private String gatSemester;
    private String gatCareer;

    /**
     * Obtiene el estandar del estudiante.
     * @return Estandar del estudiante
     */
    public int getStandard() {
        return standard;
    }

    /**
     * Obtiene la carrera del estudiante.
     * @return Carrera del estudiante
     */
    public String getCareer() {
        return career;
    }

    /**
     * Obtiene el semestre GAT del estudiante.
     * @return Semestre GAT
     */
    public String getGatSemester() {
        return gatSemester;
    }

    /**
     * Obtiene la carrera GAT del estudiante.
     * @return Carrera GAT
     */
    public String getGatCareer() {
        return gatCareer;
    }
}
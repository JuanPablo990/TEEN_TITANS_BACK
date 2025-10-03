package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

/**
 * Plan de estudios academico.
 */
public class StudyPlan {
    private int id;
    private String name;
    private int totalCredits;
    private int totalCourses;

    /**
     * Obtiene el identificador del plan de estudios.
     * @return Identificador del plan
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene el nombre del plan de estudios.
     * @return Nombre del plan
     */
    public String getName() {
        return name;
    }

    /**
     * Obtiene el total de creditos del plan de estudios.
     * @return Total de creditos
     */
    public int getTotalCredits() {
        return totalCredits;
    }

    /**
     * Obtiene el total de cursos del plan de estudios.
     * @return Total de cursos
     */
    public int getTotalCourses() {
        return totalCourses;
    }
}
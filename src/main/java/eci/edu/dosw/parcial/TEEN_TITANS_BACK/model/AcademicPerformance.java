package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import java.util.List;

/**
 * Representa el desempeño académico de un estudiante, incluyendo promedios acumulados,
 * de calificaciones y por semestre.
 */
public class AcademicPerformance {
    private Float cumulativeAverage;
    private Float gradeAverage;
    private List<Float> semesterAverage;

    /**
     * Obtiene el promedio acumulado del estudiante.
     *
     * @return Promedio acumulado
     */
    public Float getCumulativeAverage() {
        return cumulativeAverage;
    }

    /**
     * Obtiene el promedio de calificaciones del estudiante.
     *
     * @return Promedio de calificaciones
     */
    public Float getGradeAverage() {
        return gradeAverage;
    }

    /**
     * Obtiene la lista de promedios por semestre del estudiante.
     *
     * @return Lista de promedios semestrales
     */
    public List<Float> getSemesterAverage() {
        return semesterAverage;
    }
}
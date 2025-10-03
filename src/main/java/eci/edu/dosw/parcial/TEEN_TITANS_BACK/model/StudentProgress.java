package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

/**
 * Progreso academico del estudiante en creditos y cursos.
 */
public class StudentProgress {
    private int approvedCredits;
    private int pendingCredits;
    private int approvedCourses;
    private int pendingCourses;
    private int failedCourses;
    private int enrolledCourses;

    /**
     * Obtiene la cantidad de creditos aprobados.
     * @return Creditos aprobados
     */
    public int getApprovedCredits() {
        return approvedCredits;
    }

    /**
     * Obtiene la cantidad de creditos pendientes.
     * @return Creditos pendientes
     */
    public int getPendingCredits() {
        return pendingCredits;
    }

    /**
     * Obtiene la cantidad de cursos aprobados.
     * @return Cursos aprobados
     */
    public int getApprovedCourses() {
        return approvedCourses;
    }

    /**
     * Obtiene la cantidad de cursos pendientes.
     * @return Cursos pendientes
     */
    public int getPendingCourses() {
        return pendingCourses;
    }

    /**
     * Obtiene la cantidad de cursos reprobados.
     * @return Cursos reprobados
     */
    public int getFailedCourses() {
        return failedCourses;
    }

    /**
     * Obtiene la cantidad de cursos matriculados.
     * @return Cursos matriculados
     */
    public int getEnrolledCourses() {
        return enrolledCourses;
    }
}
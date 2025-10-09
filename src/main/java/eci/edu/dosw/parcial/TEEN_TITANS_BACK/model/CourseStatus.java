package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

/**
 * Representa los posibles estados de un curso en el historial académico del estudiante.
 * Esta enumeración define los diferentes estados por los que puede pasar un curso
 * durante el proceso de formación del estudiante.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
public enum CourseStatus {
    ENROLLED,
    IN_PROGRESS,
    PASSED,
    FAILED,
    WITHDRAWN,
    INCOMPLETE
}

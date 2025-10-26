package eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums;

/**
 * Representa los diferentes estados que puede tener una solicitud de cambio de horario.
 * Esta enumeración define el ciclo de vida completo de una solicitud desde su creación
 * hasta su resolución final en el sistema de gestión académica.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
public enum RequestStatus {
    PENDING,
    UNDER_REVIEW,
    APPROVED,
    REJECTED,
    CANCELLED
}

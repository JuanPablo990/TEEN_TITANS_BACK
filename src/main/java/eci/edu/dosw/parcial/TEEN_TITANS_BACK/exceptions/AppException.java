package eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions;

/**
 * Excepción personalizada utilizada en la aplicación Teen Titans.
 *
 * Esta clase extiende {@link RuntimeException} y se utiliza para manejar
 * errores específicos de la lógica de negocio o validaciones dentro del sistema.
 *
 * Al ser una excepción no verificada (unchecked), no requiere declaración
 * explícita en las firmas de los métodos.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
public class AppException extends RuntimeException {

    /**
     * Crea una nueva excepción personalizada con un mensaje descriptivo.
     *
     * @param message mensaje que describe la causa del error
     */
    public AppException(String message) {
        super(message);
    }
}
package eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Manejador global de excepciones para la aplicación Teen Titans.
 *
 * Esta clase centraliza el manejo de errores lanzados en los controladores,
 * proporcionando respuestas HTTP coherentes para diferentes tipos de excepciones.
 *
 * Utiliza la anotación {@link ControllerAdvice} de Spring para interceptar las
 * excepciones a nivel global dentro del contexto de la aplicación.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja las excepciones de tipo {@link AppException} lanzadas desde cualquier
     * parte de la aplicación y devuelve una respuesta HTTP con el código 400 (Bad Request).
     *
     * @param ex la excepción {@link AppException} capturada
     * @return una respuesta HTTP con el mensaje de error y estado {@link HttpStatus#BAD_REQUEST}
     */
    @ExceptionHandler(AppException.class)
    public ResponseEntity<String> handleAppException(AppException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
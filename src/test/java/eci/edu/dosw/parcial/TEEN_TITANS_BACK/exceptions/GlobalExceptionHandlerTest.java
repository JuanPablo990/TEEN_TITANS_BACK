package eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("Caso exitoso - handleAppException retorna BAD_REQUEST con mensaje")
    void testHandleAppException_Exitoso() {
        AppException appException = new AppException("Error de validación: Email ya existe");

        ResponseEntity<String> response = globalExceptionHandler.handleAppException(appException);

        assertAll("Verificar manejo de AppException",
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("Error de validación: Email ya existe", response.getBody())
        );
    }

    @Test
    @DisplayName("Caso borde - handleAppException con mensaje vacío")
    void testHandleAppException_MensajeVacio() {
        AppException appException = new AppException("");

        ResponseEntity<String> response = globalExceptionHandler.handleAppException(appException);

        assertAll("Verificar AppException con mensaje vacío",
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("", response.getBody())
        );
    }

    @Test
    @DisplayName("Caso borde - handleAppException con mensaje null")
    void testHandleAppException_MensajeNull() {
        AppException appException = new AppException(null);

        ResponseEntity<String> response = globalExceptionHandler.handleAppException(appException);

        assertAll("Verificar AppException con mensaje null",
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
                () -> assertNull(response.getBody())
        );
    }

    @Test
    @DisplayName("Caso error - handleAppException con excepción de recurso no encontrado")
    void testHandleAppException_RecursoNoEncontrado() {
        AppException appException = new AppException("Profesor no encontrado con ID: 999");

        ResponseEntity<String> response = globalExceptionHandler.handleAppException(appException);

        assertAll("Verificar AppException de recurso no encontrado",
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
                () -> assertEquals("Profesor no encontrado con ID: 999", response.getBody())
        );
    }

    @Test
    @DisplayName("Caso error - handleAppException con excepción de validación de datos")
    void testHandleAppException_ValidacionDatos() {
        AppException appException = new AppException("El email ya está registrado en el sistema");

        ResponseEntity<String> response = globalExceptionHandler.handleAppException(appException);

        assertAll("Verificar AppException de validación de datos",
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
                () -> assertEquals("El email ya está registrado en el sistema", response.getBody())
        );
    }

    @Test
    @DisplayName("Caso error - handleAppException con excepción de integridad referencial")
    void testHandleAppException_IntegridadReferencial() {
        AppException appException = new AppException("No se puede eliminar el profesor porque tiene cursos asignados");

        ResponseEntity<String> response = globalExceptionHandler.handleAppException(appException);

        assertAll("Verificar AppException de integridad referencial",
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
                () -> assertEquals("No se puede eliminar el profesor porque tiene cursos asignados", response.getBody())
        );
    }

    @Test
    @DisplayName("Caso borde - handleAppException con mensaje muy largo")
    void testHandleAppException_MensajeMuyLargo() {
        String mensajeLargo = "Este es un mensaje de error muy largo que contiene detalles extensos sobre la excepción ocurrida en el sistema, incluyendo información sobre el contexto, los parámetros involucrados y las posibles soluciones para resolver el problema identificado";
        AppException appException = new AppException(mensajeLargo);

        ResponseEntity<String> response = globalExceptionHandler.handleAppException(appException);

        assertAll("Verificar AppException con mensaje muy largo",
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
                () -> assertEquals(mensajeLargo, response.getBody())
        );
    }

    @Test
    @DisplayName("Caso error - handleAppException con caracteres especiales")
    void testHandleAppException_CaracteresEspeciales() {
        AppException appException = new AppException("Error: usuario@dominio.com no válido - código #12345");

        ResponseEntity<String> response = globalExceptionHandler.handleAppException(appException);

        assertAll("Verificar AppException con caracteres especiales",
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
                () -> assertEquals("Error: usuario@dominio.com no válido - código #12345", response.getBody())
        );
    }

    @Test
    @DisplayName("Caso borde - handleAppException con múltiples líneas")
    void testHandleAppException_MultiplesLineas() {
        String mensajeMultilinea = "Error en validación:\n- Campo email requerido\n- Campo nombre muy corto\n- Departamento no existe";
        AppException appException = new AppException(mensajeMultilinea);

        ResponseEntity<String> response = globalExceptionHandler.handleAppException(appException);

        assertAll("Verificar AppException con múltiples líneas",
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
                () -> assertEquals(mensajeMultilinea, response.getBody())
        );
    }

    @Test
    @DisplayName("Caso error - handleAppException con excepción de duplicado")
    void testHandleAppException_Duplicado() {
        AppException appException = new AppException("El registro ya existe en la base de datos");

        ResponseEntity<String> response = globalExceptionHandler.handleAppException(appException);

        assertAll("Verificar AppException de duplicado",
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
                () -> assertEquals("El registro ya existe en la base de datos", response.getBody())
        );
    }

    @Test
    @DisplayName("Caso borde - handleAppException verifica tipo de respuesta")
    void testHandleAppException_TipoRespuesta() {
        AppException appException = new AppException("Error de prueba");

        ResponseEntity<String> response = globalExceptionHandler.handleAppException(appException);

        assertAll("Verificar tipo de respuesta",
                () -> assertInstanceOf(ResponseEntity.class, response),
                () -> assertInstanceOf(String.class, response.getBody()),
                () -> assertTrue(response.hasBody())
        );
    }
}
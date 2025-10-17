package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Student;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.StudentAcademicProgress;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.TrafficLightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrafficLightControllerTest {

    @Mock
    private TrafficLightService trafficLightService;

    @InjectMocks
    private TrafficLightController trafficLightController;

    private final String STUDENT_ID = "student-123";
    private Student testStudent;
    private StudentAcademicProgress testProgress;

    @BeforeEach
    void setUp() {
        testStudent = new Student(STUDENT_ID, "Juan Pérez", "juan@estudiante.edu.co", "password", "Ingeniería de Sistemas", 5);
        testProgress = new StudentAcademicProgress(
                "progress-123", testStudent, "Ingeniería de Sistemas", "Facultad de Ingeniería",
                "Nuevo Plan", 5, 10, 75, 160, 4.2, null
        );
    }

    @Test
    @DisplayName("Caso exitoso - getAcademicTrafficLight retorna semáforo verde")
    void testGetAcademicTrafficLight_Verde() {
        when(trafficLightService.getAcademicTrafficLight(STUDENT_ID)).thenReturn("GREEN");

        ResponseEntity<?> response = trafficLightController.getAcademicTrafficLight(STUDENT_ID);

        assertAll("Verificar respuesta exitosa de getAcademicTrafficLight",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> assertEquals("GREEN", ((Map<?, ?>) response.getBody()).get("trafficLight")),
                () -> assertEquals("Rendimiento académico satisfactorio", ((Map<?, ?>) response.getBody()).get("statusDescription")),
                () -> verify(trafficLightService, times(1)).getAcademicTrafficLight(STUDENT_ID)
        );
    }

    @Test
    @DisplayName("Caso exitoso - getAcademicTrafficLight retorna semáforo amarillo")
    void testGetAcademicTrafficLight_Amarillo() {
        when(trafficLightService.getAcademicTrafficLight(STUDENT_ID)).thenReturn("YELLOW");

        ResponseEntity<?> response = trafficLightController.getAcademicTrafficLight(STUDENT_ID);

        assertAll("Verificar respuesta con semáforo amarillo",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("YELLOW", ((Map<?, ?>) response.getBody()).get("trafficLight")),
                () -> assertEquals("Rendimiento regular - necesita mejorar", ((Map<?, ?>) response.getBody()).get("statusDescription")),
                () -> verify(trafficLightService, times(1)).getAcademicTrafficLight(STUDENT_ID)
        );
    }

    @Test
    @DisplayName("Caso exitoso - getAcademicTrafficLight retorna semáforo rojo")
    void testGetAcademicTrafficLight_Rojo() {
        when(trafficLightService.getAcademicTrafficLight(STUDENT_ID)).thenReturn("RED");

        ResponseEntity<?> response = trafficLightController.getAcademicTrafficLight(STUDENT_ID);

        assertAll("Verificar respuesta con semáforo rojo",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("RED", ((Map<?, ?>) response.getBody()).get("trafficLight")),
                () -> assertEquals("En riesgo académico - requiere atención inmediata", ((Map<?, ?>) response.getBody()).get("statusDescription")),
                () -> verify(trafficLightService, times(1)).getAcademicTrafficLight(STUDENT_ID)
        );
    }

    @Test
    @DisplayName("Caso exitoso - getStudentInformation retorna información del estudiante")
    void testGetStudentInformation_Exitoso() {
        when(trafficLightService.getStudentInformation(STUDENT_ID)).thenReturn(testStudent);

        ResponseEntity<?> response = trafficLightController.getStudentInformation(STUDENT_ID);

        assertAll("Verificar respuesta exitosa de getStudentInformation",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(testStudent, response.getBody()),
                () -> verify(trafficLightService, times(1)).getStudentInformation(STUDENT_ID)
        );
    }

    @Test
    @DisplayName("Caso exitoso - getCurriculumProgress retorna progreso curricular")
    void testGetCurriculumProgress_Exitoso() {
        when(trafficLightService.getCurriculumProgress(STUDENT_ID)).thenReturn(testProgress);

        ResponseEntity<?> response = trafficLightController.getCurriculumProgress(STUDENT_ID);

        assertAll("Verificar respuesta exitosa de getCurriculumProgress",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(testProgress, response.getBody()),
                () -> verify(trafficLightService, times(1)).getCurriculumProgress(STUDENT_ID)
        );
    }

    @Test
    @DisplayName("Caso exitoso - getCurriculumProgressPercentage retorna porcentaje")
    void testGetCurriculumProgressPercentage_Exitoso() {
        when(trafficLightService.getCurriculumProgressPercentage(STUDENT_ID)).thenReturn(75.5);

        ResponseEntity<?> response = trafficLightController.getCurriculumProgressPercentage(STUDENT_ID);

        assertAll("Verificar respuesta exitosa de getCurriculumProgressPercentage",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> assertEquals(75.5, ((Map<?, ?>) response.getBody()).get("progressPercentage")),
                () -> assertEquals("Avance bueno", ((Map<?, ?>) response.getBody()).get("progressDescription")),
                () -> verify(trafficLightService, times(1)).getCurriculumProgressPercentage(STUDENT_ID)
        );
    }

    @Test
    @DisplayName("Caso exitoso - isStudentAtAcademicRisk retorna false")
    void testIsStudentAtAcademicRisk_SinRiesgo() {
        when(trafficLightService.isStudentAtAcademicRisk(STUDENT_ID)).thenReturn(false);

        ResponseEntity<?> response = trafficLightController.isStudentAtAcademicRisk(STUDENT_ID);

        assertAll("Verificar respuesta sin riesgo académico",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> assertEquals(false, ((Map<?, ?>) response.getBody()).get("atAcademicRisk")),
                () -> assertEquals("BAJO", ((Map<?, ?>) response.getBody()).get("riskLevel")),
                () -> verify(trafficLightService, times(1)).isStudentAtAcademicRisk(STUDENT_ID)
        );
    }

    @Test
    @DisplayName("Caso exitoso - isStudentAtAcademicRisk retorna true")
    void testIsStudentAtAcademicRisk_ConRiesgo() {
        when(trafficLightService.isStudentAtAcademicRisk(STUDENT_ID)).thenReturn(true);

        ResponseEntity<?> response = trafficLightController.isStudentAtAcademicRisk(STUDENT_ID);

        assertAll("Verificar respuesta con riesgo académico",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> assertEquals(true, ((Map<?, ?>) response.getBody()).get("atAcademicRisk")),
                () -> assertEquals("ALTO", ((Map<?, ?>) response.getBody()).get("riskLevel")),
                () -> verify(trafficLightService, times(1)).isStudentAtAcademicRisk(STUDENT_ID)
        );
    }

    @Test
    @DisplayName("Caso exitoso - getAcademicStatusSummary retorna resumen")
    void testGetAcademicStatusSummary_Exitoso() {
        String summary = "Estudiante con rendimiento académico satisfactorio";
        when(trafficLightService.getAcademicStatusSummary(STUDENT_ID)).thenReturn(summary);

        ResponseEntity<?> response = trafficLightController.getAcademicStatusSummary(STUDENT_ID);

        assertAll("Verificar respuesta exitosa de getAcademicStatusSummary",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> assertEquals(summary, ((Map<?, ?>) response.getBody()).get("academicSummary")),
                () -> verify(trafficLightService, times(1)).getAcademicStatusSummary(STUDENT_ID)
        );
    }

    @Test
    @DisplayName("Caso exitoso - getCompleteAcademicStatus retorna estado completo")
    void testGetCompleteAcademicStatus_Exitoso() {
        when(trafficLightService.getStudentInformation(STUDENT_ID)).thenReturn(testStudent);
        when(trafficLightService.getCurriculumProgress(STUDENT_ID)).thenReturn(testProgress);
        when(trafficLightService.getAcademicTrafficLight(STUDENT_ID)).thenReturn("GREEN");
        when(trafficLightService.getCurriculumProgressPercentage(STUDENT_ID)).thenReturn(75.5);
        when(trafficLightService.isStudentAtAcademicRisk(STUDENT_ID)).thenReturn(false);

        ResponseEntity<?> response = trafficLightController.getCompleteAcademicStatus(STUDENT_ID);

        assertAll("Verificar respuesta exitosa de getCompleteAcademicStatus",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> verify(trafficLightService, times(1)).getStudentInformation(STUDENT_ID),
                () -> verify(trafficLightService, times(1)).getCurriculumProgress(STUDENT_ID),
                () -> verify(trafficLightService, times(1)).getAcademicTrafficLight(STUDENT_ID),
                () -> verify(trafficLightService, times(1)).getCurriculumProgressPercentage(STUDENT_ID),
                () -> verify(trafficLightService, times(1)).isStudentAtAcademicRisk(STUDENT_ID)
        );
    }

    @Test
    @DisplayName("Caso error - getAcademicTrafficLight lanza AppException")
    void testGetAcademicTrafficLight_EstudianteNoEncontrado() {
        when(trafficLightService.getAcademicTrafficLight(STUDENT_ID)).thenThrow(new AppException("Estudiante no encontrado"));

        ResponseEntity<?> response = trafficLightController.getAcademicTrafficLight(STUDENT_ID);

        assertAll("Verificar manejo de excepción en getAcademicTrafficLight",
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> assertEquals("Estudiante no encontrado", ((Map<?, ?>) response.getBody()).get("error")),
                () -> verify(trafficLightService, times(1)).getAcademicTrafficLight(STUDENT_ID)
        );
    }

    @Test
    @DisplayName("Caso error - getStudentInformation lanza excepción genérica")
    void testGetStudentInformation_ExcepcionGenerica() {
        when(trafficLightService.getStudentInformation(STUDENT_ID)).thenThrow(new RuntimeException("Error de base de datos"));

        ResponseEntity<?> response = trafficLightController.getStudentInformation(STUDENT_ID);

        assertAll("Verificar manejo de excepción genérica en getStudentInformation",
                () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> assertEquals("Error al obtener la información del estudiante", ((Map<?, ?>) response.getBody()).get("error")),
                () -> verify(trafficLightService, times(1)).getStudentInformation(STUDENT_ID)
        );
    }

    @Test
    @DisplayName("Caso error - getCurriculumProgress lanza AppException")
    void testGetCurriculumProgress_ProgresoNoEncontrado() {
        when(trafficLightService.getCurriculumProgress(STUDENT_ID)).thenThrow(new AppException("Progreso curricular no encontrado"));

        ResponseEntity<?> response = trafficLightController.getCurriculumProgress(STUDENT_ID);

        assertAll("Verificar manejo de excepción en getCurriculumProgress",
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> assertEquals("Progreso curricular no encontrado", ((Map<?, ?>) response.getBody()).get("error")),
                () -> verify(trafficLightService, times(1)).getCurriculumProgress(STUDENT_ID)
        );
    }

    @Test
    @DisplayName("Caso error - getCurriculumProgressPercentage lanza AppException")
    void testGetCurriculumProgressPercentage_EstudianteNoEncontrado() {
        when(trafficLightService.getCurriculumProgressPercentage(STUDENT_ID)).thenThrow(new AppException("Estudiante no existe"));

        ResponseEntity<?> response = trafficLightController.getCurriculumProgressPercentage(STUDENT_ID);

        assertAll("Verificar manejo de excepción en getCurriculumProgressPercentage",
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> assertEquals("Estudiante no existe", ((Map<?, ?>) response.getBody()).get("error")),
                () -> verify(trafficLightService, times(1)).getCurriculumProgressPercentage(STUDENT_ID)
        );
    }

    @Test
    @DisplayName("Caso error - isStudentAtAcademicRisk lanza excepción genérica")
    void testIsStudentAtAcademicRisk_ExcepcionGenerica() {
        when(trafficLightService.isStudentAtAcademicRisk(STUDENT_ID)).thenThrow(new RuntimeException("Error de cálculo"));

        ResponseEntity<?> response = trafficLightController.isStudentAtAcademicRisk(STUDENT_ID);

        assertAll("Verificar manejo de excepción genérica en isStudentAtAcademicRisk",
                () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> assertEquals("Error al verificar el riesgo académico", ((Map<?, ?>) response.getBody()).get("error")),
                () -> verify(trafficLightService, times(1)).isStudentAtAcademicRisk(STUDENT_ID)
        );
    }

    @Test
    @DisplayName("Caso error - getAcademicStatusSummary lanza AppException")
    void testGetAcademicStatusSummary_EstudianteNoEncontrado() {
        when(trafficLightService.getAcademicStatusSummary(STUDENT_ID)).thenThrow(new AppException("No se puede generar resumen"));

        ResponseEntity<?> response = trafficLightController.getAcademicStatusSummary(STUDENT_ID);

        assertAll("Verificar manejo de excepción en getAcademicStatusSummary",
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> assertEquals("No se puede generar resumen", ((Map<?, ?>) response.getBody()).get("error")),
                () -> verify(trafficLightService, times(1)).getAcademicStatusSummary(STUDENT_ID)
        );
    }

    @Test
    @DisplayName("Caso error - getCompleteAcademicStatus lanza AppException en getStudentInformation")
    void testGetCompleteAcademicStatus_ErrorEnStudentInformation() {
        when(trafficLightService.getStudentInformation(STUDENT_ID)).thenThrow(new AppException("Estudiante no encontrado"));

        ResponseEntity<?> response = trafficLightController.getCompleteAcademicStatus(STUDENT_ID);

        assertAll("Verificar manejo de error en getCompleteAcademicStatus",
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> assertEquals("Estudiante no encontrado", ((Map<?, ?>) response.getBody()).get("error")),
                () -> verify(trafficLightService, times(1)).getStudentInformation(STUDENT_ID),
                () -> verify(trafficLightService, never()).getCurriculumProgress(STUDENT_ID),
                () -> verify(trafficLightService, never()).getAcademicTrafficLight(STUDENT_ID)
        );
    }

    @Test
    @DisplayName("Caso borde - getCurriculumProgressPercentage con 0%")
    void testGetCurriculumProgressPercentage_CeroPorcentaje() {
        when(trafficLightService.getCurriculumProgressPercentage(STUDENT_ID)).thenReturn(0.0);

        ResponseEntity<?> response = trafficLightController.getCurriculumProgressPercentage(STUDENT_ID);

        assertAll("Verificar respuesta con 0% de progreso",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals(0.0, ((Map<?, ?>) response.getBody()).get("progressPercentage")),
                () -> assertEquals("Avance muy bajo", ((Map<?, ?>) response.getBody()).get("progressDescription")),
                () -> verify(trafficLightService, times(1)).getCurriculumProgressPercentage(STUDENT_ID)
        );
    }

    @Test
    @DisplayName("Caso borde - getCurriculumProgressPercentage con 100%")
    void testGetCurriculumProgressPercentage_CienPorcentaje() {
        when(trafficLightService.getCurriculumProgressPercentage(STUDENT_ID)).thenReturn(100.0);

        ResponseEntity<?> response = trafficLightController.getCurriculumProgressPercentage(STUDENT_ID);

        assertAll("Verificar respuesta con 100% de progreso",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals(100.0, ((Map<?, ?>) response.getBody()).get("progressPercentage")),
                () -> assertEquals("Avance excelente", ((Map<?, ?>) response.getBody()).get("progressDescription")),
                () -> verify(trafficLightService, times(1)).getCurriculumProgressPercentage(STUDENT_ID)
        );
    }

    @Test
    @DisplayName("Caso borde - getCurriculumProgressPercentage con 40%")
    void testGetCurriculumProgressPercentage_CuarentaPorcentaje() {
        when(trafficLightService.getCurriculumProgressPercentage(STUDENT_ID)).thenReturn(40.0);

        ResponseEntity<?> response = trafficLightController.getCurriculumProgressPercentage(STUDENT_ID);

        assertAll("Verificar respuesta con 40% de progreso",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals(40.0, ((Map<?, ?>) response.getBody()).get("progressPercentage")),
                () -> assertEquals("Avance bajo", ((Map<?, ?>) response.getBody()).get("progressDescription")),
                () -> verify(trafficLightService, times(1)).getCurriculumProgressPercentage(STUDENT_ID)
        );
    }

    @Test
    @DisplayName("Caso borde - getCurriculumProgressPercentage con 60%")
    void testGetCurriculumProgressPercentage_SesentaPorcentaje() {
        when(trafficLightService.getCurriculumProgressPercentage(STUDENT_ID)).thenReturn(60.0);

        ResponseEntity<?> response = trafficLightController.getCurriculumProgressPercentage(STUDENT_ID);

        assertAll("Verificar respuesta con 60% de progreso",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals(60.0, ((Map<?, ?>) response.getBody()).get("progressPercentage")),
                () -> assertEquals("Avance regular", ((Map<?, ?>) response.getBody()).get("progressDescription")),
                () -> verify(trafficLightService, times(1)).getCurriculumProgressPercentage(STUDENT_ID)
        );
    }

    @Test
    @DisplayName("Caso borde - getCurriculumProgressPercentage con 90%")
    void testGetCurriculumProgressPercentage_NoventaPorcentaje() {
        when(trafficLightService.getCurriculumProgressPercentage(STUDENT_ID)).thenReturn(90.0);

        ResponseEntity<?> response = trafficLightController.getCurriculumProgressPercentage(STUDENT_ID);

        assertAll("Verificar respuesta con 90% de progreso",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals(90.0, ((Map<?, ?>) response.getBody()).get("progressPercentage")),
                () -> assertEquals("Avance excelente", ((Map<?, ?>) response.getBody()).get("progressDescription")),
                () -> verify(trafficLightService, times(1)).getCurriculumProgressPercentage(STUDENT_ID)
        );
    }

    @Test
    @DisplayName("Caso borde - getStatusDescription con color desconocido")
    void testGetStatusDescription_ColorDesconocido() {
        when(trafficLightService.getAcademicTrafficLight(STUDENT_ID)).thenReturn("UNKNOWN");

        ResponseEntity<?> response = trafficLightController.getAcademicTrafficLight(STUDENT_ID);

        assertAll("Verificar manejo de color desconocido",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("UNKNOWN", ((Map<?, ?>) response.getBody()).get("trafficLight")),
                () -> assertEquals("Estado desconocido", ((Map<?, ?>) response.getBody()).get("statusDescription"))
        );
    }

    @Test
    @DisplayName("Caso borde - getCompleteAcademicStatus con error en múltiples servicios")
    void testGetCompleteAcademicStatus_ErrorEnCurriculumProgress() {
        when(trafficLightService.getStudentInformation(STUDENT_ID)).thenReturn(testStudent);
        when(trafficLightService.getCurriculumProgress(STUDENT_ID)).thenThrow(new AppException("Error en progreso"));

        ResponseEntity<?> response = trafficLightController.getCompleteAcademicStatus(STUDENT_ID);

        assertAll("Verificar manejo de error en getCompleteAcademicStatus",
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> assertEquals("Error en progreso", ((Map<?, ?>) response.getBody()).get("error")),
                () -> verify(trafficLightService, times(1)).getStudentInformation(STUDENT_ID),
                () -> verify(trafficLightService, times(1)).getCurriculumProgress(STUDENT_ID),
                () -> verify(trafficLightService, never()).getAcademicTrafficLight(STUDENT_ID)
        );
    }
}
package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto.TrafficLightDTO;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrafficLightControllerTest {

    @Mock
    private TrafficLightService trafficLightService;

    @InjectMocks
    private TrafficLightController trafficLightController;

    private Student student;
    private StudentAcademicProgress academicProgress;

    @BeforeEach
    void setUp() {
        student = new Student("STU-123", "Juan Pérez", "juan@university.edu", "password", "Computer Science", 3);
        student.setId("STU-123");
        student.setActive(true);

        academicProgress = new StudentAcademicProgress(student, "Computer Science", "Engineering", "Regular",
                3, 8, 60, 160, 4.2, Collections.emptyList());
    }

    @Test
    @DisplayName("Caso exitoso - getCompleteAcademicStatus retorna estado completo")
    void testGetCompleteAcademicStatus_Exitoso() {
        when(trafficLightService.getStudentInformation("STU-123")).thenReturn(student);
        when(trafficLightService.getCurriculumProgress("STU-123")).thenReturn(academicProgress);
        when(trafficLightService.getAcademicTrafficLight("STU-123")).thenReturn("GREEN");
        when(trafficLightService.getCurriculumProgressPercentage("STU-123")).thenReturn(75.5);
        when(trafficLightService.isStudentAtAcademicRisk("STU-123")).thenReturn(false);

        ResponseEntity<TrafficLightDTO> response = trafficLightController.getCompleteAcademicStatus("STU-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Operación exitosa", response.getBody().getMessage());

        verify(trafficLightService, times(1)).getStudentInformation("STU-123");
        verify(trafficLightService, times(1)).getCurriculumProgress("STU-123");
        verify(trafficLightService, times(1)).getAcademicTrafficLight("STU-123");
        verify(trafficLightService, times(1)).getCurriculumProgressPercentage("STU-123");
        verify(trafficLightService, times(1)).isStudentAtAcademicRisk("STU-123");
    }

    @Test
    @DisplayName("Caso error - getCompleteAcademicStatus lanza AppException")
    void testGetCompleteAcademicStatus_AppException() {
        when(trafficLightService.getStudentInformation("STU-123"))
                .thenThrow(new AppException("Estudiante no encontrado"));

        ResponseEntity<TrafficLightDTO> response = trafficLightController.getCompleteAcademicStatus("STU-123");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Estudiante no encontrado", response.getBody().getMessage());
        verify(trafficLightService, times(1)).getStudentInformation("STU-123");
    }

    @Test
    @DisplayName("Caso exitoso - getAcademicTrafficLight retorna semáforo académico")
    void testGetAcademicTrafficLight_Exitoso() {
        when(trafficLightService.getAcademicTrafficLight("STU-123")).thenReturn("YELLOW");
        when(trafficLightService.isStudentAtAcademicRisk("STU-123")).thenReturn(false);

        ResponseEntity<TrafficLightDTO> response = trafficLightController.getAcademicTrafficLight("STU-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());

        Object data = response.getBody().getData();
        assertNotNull(data);
        assertInstanceOf(java.util.Map.class, data);
        verify(trafficLightService, times(1)).getAcademicTrafficLight("STU-123");
        verify(trafficLightService, times(1)).isStudentAtAcademicRisk("STU-123");
    }

    @Test
    @DisplayName("Caso error - getAcademicTrafficLight lanza excepción general")
    void testGetAcademicTrafficLight_ExcepcionGeneral() {
        when(trafficLightService.getAcademicTrafficLight("STU-123"))
                .thenThrow(new RuntimeException("Error de base de datos"));

        ResponseEntity<TrafficLightDTO> response = trafficLightController.getAcademicTrafficLight("STU-123");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Error al obtener el semáforo académico", response.getBody().getMessage());
        verify(trafficLightService, times(1)).getAcademicTrafficLight("STU-123");
    }

    @Test
    @DisplayName("Caso exitoso - getCurriculumProgress retorna progreso curricular")
    void testGetCurriculumProgress_Exitoso() {
        when(trafficLightService.getStudentInformation("STU-123")).thenReturn(student);
        when(trafficLightService.getCurriculumProgress("STU-123")).thenReturn(academicProgress);
        when(trafficLightService.getCurriculumProgressPercentage("STU-123")).thenReturn(75.5);

        ResponseEntity<TrafficLightDTO> response = trafficLightController.getCurriculumProgress("STU-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        verify(trafficLightService, times(1)).getStudentInformation("STU-123");
        verify(trafficLightService, times(1)).getCurriculumProgress("STU-123");
        verify(trafficLightService, times(1)).getCurriculumProgressPercentage("STU-123");
    }

    @Test
    @DisplayName("Caso exitoso - getCurriculumProgressPercentage retorna porcentaje de avance")
    void testGetCurriculumProgressPercentage_Exitoso() {
        when(trafficLightService.getCurriculumProgressPercentage("STU-123")).thenReturn(80.0);

        ResponseEntity<TrafficLightDTO> response = trafficLightController.getCurriculumProgressPercentage("STU-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());

        Object data = response.getBody().getData();
        assertNotNull(data);
        assertInstanceOf(java.util.Map.class, data);
        verify(trafficLightService, times(1)).getCurriculumProgressPercentage("STU-123");
    }

    @Test
    @DisplayName("Caso exitoso - isStudentAtAcademicRisk retorna false cuando no está en riesgo")
    void testIsStudentAtAcademicRisk_NoEnRiesgo() {
        when(trafficLightService.isStudentAtAcademicRisk("STU-123")).thenReturn(false);

        ResponseEntity<TrafficLightDTO> response = trafficLightController.isStudentAtAcademicRisk("STU-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());

        Object data = response.getBody().getData();
        assertNotNull(data);
        assertInstanceOf(java.util.Map.class, data);
        verify(trafficLightService, times(1)).isStudentAtAcademicRisk("STU-123");
    }

    @Test
    @DisplayName("Caso borde - isStudentAtAcademicRisk retorna true cuando está en riesgo")
    void testIsStudentAtAcademicRisk_EnRiesgo() {
        when(trafficLightService.isStudentAtAcademicRisk("STU-123")).thenReturn(true);

        ResponseEntity<TrafficLightDTO> response = trafficLightController.isStudentAtAcademicRisk("STU-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());

        Object data = response.getBody().getData();
        assertNotNull(data);
        java.util.Map<?, ?> dataMap = (java.util.Map<?, ?>) data;
        assertEquals(true, dataMap.get("atAcademicRisk"));
        assertEquals("ALTO", dataMap.get("riskLevel"));
        verify(trafficLightService, times(1)).isStudentAtAcademicRisk("STU-123");
    }

    @Test
    @DisplayName("Caso borde - getStudentsAtRiskByProgram retorna lista vacía")
    void testGetStudentsAtRiskByProgram_ListaVacia() {
        when(trafficLightService.getStudentsAtRiskByProgram("Computer Science")).thenReturn(Collections.emptyList());
        when(trafficLightService.getTrafficLightStatisticsByProgram("Computer Science")).thenReturn(new int[]{0, 0, 0});

        ResponseEntity<TrafficLightDTO> response = trafficLightController.getStudentsAtRiskByProgram("Computer Science");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());

        Object data = response.getBody().getData();
        assertNotNull(data);
        java.util.Map<?, ?> dataMap = (java.util.Map<?, ?>) data;
        assertEquals(0, dataMap.get("studentsAtRiskCount"));
        verify(trafficLightService, times(1)).getStudentsAtRiskByProgram("Computer Science");
        verify(trafficLightService, times(1)).getTrafficLightStatisticsByProgram("Computer Science");
    }

    @Test
    @DisplayName("Caso exitoso - getStudentsAtRiskByProgram retorna estudiantes en riesgo")
    void testGetStudentsAtRiskByProgram_ConEstudiantes() {
        List<Student> studentsAtRisk = Arrays.asList(student);
        when(trafficLightService.getStudentsAtRiskByProgram("Computer Science")).thenReturn(studentsAtRisk);
        when(trafficLightService.getTrafficLightStatisticsByProgram("Computer Science")).thenReturn(new int[]{10, 5, 3});

        ResponseEntity<TrafficLightDTO> response = trafficLightController.getStudentsAtRiskByProgram("Computer Science");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());

        Object data = response.getBody().getData();
        assertNotNull(data);
        java.util.Map<?, ?> dataMap = (java.util.Map<?, ?>) data;
        assertEquals(1, dataMap.get("studentsAtRiskCount"));
        verify(trafficLightService, times(1)).getStudentsAtRiskByProgram("Computer Science");
        verify(trafficLightService, times(1)).getTrafficLightStatisticsByProgram("Computer Science");
    }

    @Test
    @DisplayName("Caso borde - getCurriculumProgressPercentage con porcentaje nulo")
    void testGetCurriculumProgressPercentage_PorcentajeNulo() {
        when(trafficLightService.getCurriculumProgressPercentage("STU-123")).thenReturn(null);

        ResponseEntity<TrafficLightDTO> response = trafficLightController.getCurriculumProgressPercentage("STU-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        verify(trafficLightService, times(1)).getCurriculumProgressPercentage("STU-123");
    }

    @Test
    @DisplayName("Caso error - getCurriculumProgress lanza AppException")
    void testGetCurriculumProgress_AppException() {
        when(trafficLightService.getStudentInformation("STU-123"))
                .thenThrow(new AppException("Estudiante no encontrado"));

        ResponseEntity<TrafficLightDTO> response = trafficLightController.getCurriculumProgress("STU-123");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Estudiante no encontrado", response.getBody().getMessage());
        verify(trafficLightService, times(1)).getStudentInformation("STU-123");
    }

    @Test
    @DisplayName("Caso error - getCurriculumProgressPercentage lanza AppException")
    void testGetCurriculumProgressPercentage_AppException() {
        when(trafficLightService.getCurriculumProgressPercentage("STU-123"))
                .thenThrow(new AppException("Progreso no encontrado"));

        ResponseEntity<TrafficLightDTO> response = trafficLightController.getCurriculumProgressPercentage("STU-123");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Progreso no encontrado", response.getBody().getMessage());
        verify(trafficLightService, times(1)).getCurriculumProgressPercentage("STU-123");
    }

    @Test
    @DisplayName("Caso error - isStudentAtAcademicRisk lanza AppException")
    void testIsStudentAtAcademicRisk_AppException() {
        when(trafficLightService.isStudentAtAcademicRisk("STU-123"))
                .thenThrow(new AppException("Estudiante no encontrado"));

        ResponseEntity<TrafficLightDTO> response = trafficLightController.isStudentAtAcademicRisk("STU-123");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Estudiante no encontrado", response.getBody().getMessage());
        verify(trafficLightService, times(1)).isStudentAtAcademicRisk("STU-123");
    }

    @Test
    @DisplayName("Caso borde - getAcademicTrafficLight con semáforo rojo")
    void testGetAcademicTrafficLight_SemaforoRojo() {
        when(trafficLightService.getAcademicTrafficLight("STU-123")).thenReturn("RED");
        when(trafficLightService.isStudentAtAcademicRisk("STU-123")).thenReturn(true);

        ResponseEntity<TrafficLightDTO> response = trafficLightController.getAcademicTrafficLight("STU-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());

        Object data = response.getBody().getData();
        assertNotNull(data);
        java.util.Map<?, ?> dataMap = (java.util.Map<?, ?>) data;
        assertEquals("RED", dataMap.get("trafficLight"));
        assertEquals("En riesgo académico - requiere atención inmediata", dataMap.get("description"));
        assertEquals(true, dataMap.get("atAcademicRisk"));
        verify(trafficLightService, times(1)).getAcademicTrafficLight("STU-123");
        verify(trafficLightService, times(1)).isStudentAtAcademicRisk("STU-123");
    }

    @Test
    @DisplayName("Caso borde - getStudentsAtRiskByProgram con estadísticas completas")
    void testGetStudentsAtRiskByProgram_EstadisticasCompletas() {
        List<Student> studentsAtRisk = Arrays.asList(student, student);
        when(trafficLightService.getStudentsAtRiskByProgram("Computer Science")).thenReturn(studentsAtRisk);
        when(trafficLightService.getTrafficLightStatisticsByProgram("Computer Science")).thenReturn(new int[]{15, 8, 2});

        ResponseEntity<TrafficLightDTO> response = trafficLightController.getStudentsAtRiskByProgram("Computer Science");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());

        Object data = response.getBody().getData();
        assertNotNull(data);
        java.util.Map<?, ?> dataMap = (java.util.Map<?, ?>) data;
        assertEquals(2, dataMap.get("studentsAtRiskCount"));

        Object statistics = dataMap.get("trafficLightStatistics");
        assertNotNull(statistics);
        assertInstanceOf(java.util.Map.class, statistics);
        verify(trafficLightService, times(1)).getStudentsAtRiskByProgram("Computer Science");
        verify(trafficLightService, times(1)).getTrafficLightStatisticsByProgram("Computer Science");
    }

    @Test
    @DisplayName("Caso borde - getCurriculumProgressPercentage con porcentaje bajo")
    void testGetCurriculumProgressPercentage_PorcentajeBajo() {
        when(trafficLightService.getCurriculumProgressPercentage("STU-123")).thenReturn(25.0);

        ResponseEntity<TrafficLightDTO> response = trafficLightController.getCurriculumProgressPercentage("STU-123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());

        Object data = response.getBody().getData();
        assertNotNull(data);
        java.util.Map<?, ?> dataMap = (java.util.Map<?, ?>) data;
        assertEquals(25.0, dataMap.get("progressPercentage"));
        assertEquals("Avance muy bajo", dataMap.get("description"));
        verify(trafficLightService, times(1)).getCurriculumProgressPercentage("STU-123");
    }
}
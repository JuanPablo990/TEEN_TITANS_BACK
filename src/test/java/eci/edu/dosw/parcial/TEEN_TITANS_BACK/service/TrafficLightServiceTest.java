package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.StudentRepository;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.StudentAcademicProgressRepository;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.CourseStatusDetailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrafficLightServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentAcademicProgressRepository studentAcademicProgressRepository;

    @Mock
    private CourseStatusDetailRepository courseStatusDetailRepository;

    @InjectMocks
    private TrafficLightService trafficLightService;

    private Student student;
    private StudentAcademicProgress progress;
    private CourseStatusDetail courseStatus1, courseStatus2;

    @BeforeEach
    void setUp() {
        student = new Student("ST001", "Robin", "robin@titans.edu", "pass123",
                "Ingeniería de Sistemas", 5);

        courseStatus1 = new CourseStatusDetail("CSD001", null, CourseStatus.PASSED,
                4.5, "5", new Date(), null,
                null, null, 3, true, "Excelente");
        courseStatus2 = new CourseStatusDetail("CSD002", null, CourseStatus.FAILED,
                2.5, "5", new Date(), new Date(),
                null, null, 0, false, "Reprobado");

        progress = new StudentAcademicProgress("PROG001", student, "Ingeniería de Sistemas",
                "Facultad de Ingeniería", "Regular", 5, 10,
                75, 160, 4.2, Arrays.asList(courseStatus1, courseStatus2));
    }

    @Test
    @DisplayName("Caso borde - getAcademicTrafficLight retorna RED para estudiante inactivo")
    void testGetAcademicTrafficLight_EstudianteInactivo() {
        Student studentInactivo = new Student("ST001", "Robin", "robin@titans.edu", "pass123",
                "Ingeniería de Sistemas", 5);
        studentInactivo.setActive(false);

        when(studentRepository.findById("ST001"))
                .thenReturn(Optional.of(studentInactivo));

        String resultado = trafficLightService.getAcademicTrafficLight("ST001");

        assertEquals("RED", resultado);
        verify(studentRepository, times(1)).findById("ST001");
        verify(studentAcademicProgressRepository, never()).findByAcademicProgram(anyString());
    }

    @Test
    @DisplayName("Caso error - getAcademicTrafficLight lanza excepción cuando estudiante no existe")
    void testGetAcademicTrafficLight_EstudianteNoEncontrado() {
        when(studentRepository.findById("ST999"))
                .thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class,
                () -> trafficLightService.getAcademicTrafficLight("ST999"));

        assertEquals("Estudiante no encontrado: ST999", exception.getMessage());
        verify(studentRepository, times(1)).findById("ST999");
    }

    @Test
    @DisplayName("Caso error - getAcademicTrafficLight lanza excepción cuando progreso no existe")
    void testGetAcademicTrafficLight_ProgresoNoEncontrado() {
        when(studentRepository.findById("ST001"))
                .thenReturn(Optional.of(student));
        when(studentAcademicProgressRepository.findByAcademicProgram("Ingeniería de Sistemas"))
                .thenReturn(Collections.emptyList());

        AppException exception = assertThrows(AppException.class,
                () -> trafficLightService.getAcademicTrafficLight("ST001"));

        assertEquals("Progreso académico no encontrado para el estudiante: ST001", exception.getMessage());
        verify(studentRepository, times(1)).findById("ST001");
        verify(studentAcademicProgressRepository, times(1)).findByAcademicProgram("Ingeniería de Sistemas");
    }

    @Test
    @DisplayName("Caso exitoso - getCurriculumProgressPercentage calcula porcentaje correctamente")
    void testGetCurriculumProgressPercentage_Exitoso() {
        when(studentRepository.findById("ST001"))
                .thenReturn(Optional.of(student));
        when(studentAcademicProgressRepository.findByAcademicProgram("Ingeniería de Sistemas"))
                .thenReturn(Arrays.asList(progress));

        Double resultado = trafficLightService.getCurriculumProgressPercentage("ST001");

        assertNotNull(resultado);
        assertEquals(46.88, resultado, 0.01);
        verify(studentRepository, times(1)).findById("ST001");
    }

    @Test
    @DisplayName("Caso borde - getCurriculumProgressPercentage retorna 0 cuando créditos requeridos son 0")
    void testGetCurriculumProgressPercentage_CreditosRequeridosCero() {
        StudentAcademicProgress progressCero = new StudentAcademicProgress("PROG001", student,
                "Ingeniería de Sistemas", "Facultad de Ingeniería", "Regular", 5, 10,
                75, 0, 4.2, Arrays.asList(courseStatus1));

        when(studentRepository.findById("ST001"))
                .thenReturn(Optional.of(student));
        when(studentAcademicProgressRepository.findByAcademicProgram("Ingeniería de Sistemas"))
                .thenReturn(Arrays.asList(progressCero));

        Double resultado = trafficLightService.getCurriculumProgressPercentage("ST001");

        assertNotNull(resultado);
        assertEquals(0.0, resultado, 0.01);
        verify(studentRepository, times(1)).findById("ST001");
    }

    @Test
    @DisplayName("Caso borde - getCurriculumProgressPercentage retorna 100 cuando créditos completados exceden requeridos")
    void testGetCurriculumProgressPercentage_CreditosExcedidos() {
        StudentAcademicProgress progressExcedido = new StudentAcademicProgress("PROG001", student,
                "Ingeniería de Sistemas", "Facultad de Ingeniería", "Regular", 5, 10,
                200, 160, 4.2, Arrays.asList(courseStatus1));

        when(studentRepository.findById("ST001"))
                .thenReturn(Optional.of(student));
        when(studentAcademicProgressRepository.findByAcademicProgram("Ingeniería de Sistemas"))
                .thenReturn(Arrays.asList(progressExcedido));

        Double resultado = trafficLightService.getCurriculumProgressPercentage("ST001");

        assertNotNull(resultado);
        assertEquals(100.0, resultado, 0.01);
        verify(studentRepository, times(1)).findById("ST001");
    }

    @Test
    @DisplayName("Caso borde - getCurriculumProgressPercentage con valores nulos")
    void testGetCurriculumProgressPercentage_ValoresNulos() {
        StudentAcademicProgress progressNulo = new StudentAcademicProgress("PROG001", student,
                "Ingeniería de Sistemas", "Facultad de Ingeniería", "Regular", 5, 10,
                null, null, 4.2, Arrays.asList(courseStatus1));

        when(studentRepository.findById("ST001"))
                .thenReturn(Optional.of(student));
        when(studentAcademicProgressRepository.findByAcademicProgram("Ingeniería de Sistemas"))
                .thenReturn(Arrays.asList(progressNulo));

        Double resultado = trafficLightService.getCurriculumProgressPercentage("ST001");

        assertNotNull(resultado);
        assertEquals(0.0, resultado, 0.01);
        verify(studentRepository, times(1)).findById("ST001");
    }

    @Test
    @DisplayName("Caso borde - getAcademicStatusSummary maneja valores nulos correctamente")
    void testGetAcademicStatusSummary_ValoresNulos() {
        StudentAcademicProgress progressNulo = new StudentAcademicProgress("PROG001", student,
                "Ingeniería de Sistemas", "Facultad de Ingeniería", "Regular", null, 10,
                null, null, null, Arrays.asList(courseStatus1));

        when(studentRepository.findById("ST001"))
                .thenReturn(Optional.of(student));
        when(studentAcademicProgressRepository.findByAcademicProgram("Ingeniería de Sistemas"))
                .thenReturn(Arrays.asList(progressNulo));
        when(courseStatusDetailRepository.findBySemester("5"))
                .thenReturn(Collections.emptyList());

        String resultado = trafficLightService.getAcademicStatusSummary("ST001");

        assertNotNull(resultado);
        assertTrue(resultado.contains("Estudiante: Robin"));
        assertTrue(resultado.contains("Programa: Ingeniería de Sistemas"));
    }

    @Test
    @DisplayName("Caso exitoso - getStudentInformation retorna información del estudiante")
    void testGetStudentInformation_Exitoso() {
        when(studentRepository.findById("ST001"))
                .thenReturn(Optional.of(student));

        Student resultado = trafficLightService.getStudentInformation("ST001");

        assertNotNull(resultado);
        assertEquals("ST001", resultado.getId());
        assertEquals("Robin", resultado.getName());
        assertEquals("Ingeniería de Sistemas", resultado.getAcademicProgram());
        verify(studentRepository, times(1)).findById("ST001");
    }

    @Test
    @DisplayName("Caso error - getStudentInformation lanza excepción cuando estudiante no existe")
    void testGetStudentInformation_EstudianteNoEncontrado() {
        when(studentRepository.findById("ST999"))
                .thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class,
                () -> trafficLightService.getStudentInformation("ST999"));

        assertEquals("Estudiante no encontrado: ST999", exception.getMessage());
        verify(studentRepository, times(1)).findById("ST999");
    }

    @Test
    @DisplayName("Caso error - getStudentInformation lanza excepción cuando estudiante inactivo")
    void testGetStudentInformation_EstudianteInactivo() {
        Student studentInactivo = new Student("ST001", "Robin", "robin@titans.edu", "pass123",
                "Ingeniería de Sistemas", 5);
        studentInactivo.setActive(false);

        when(studentRepository.findById("ST001"))
                .thenReturn(Optional.of(studentInactivo));

        AppException exception = assertThrows(AppException.class,
                () -> trafficLightService.getStudentInformation("ST001"));

        assertEquals("Estudiante inactivo: ST001", exception.getMessage());
        verify(studentRepository, times(1)).findById("ST001");
    }

    @Test
    @DisplayName("Caso exitoso - getCurriculumProgress retorna progreso curricular")
    void testGetCurriculumProgress_Exitoso() {
        when(studentRepository.findById("ST001"))
                .thenReturn(Optional.of(student));
        when(studentAcademicProgressRepository.findByAcademicProgram("Ingeniería de Sistemas"))
                .thenReturn(Arrays.asList(progress));

        StudentAcademicProgress resultado = trafficLightService.getCurriculumProgress("ST001");

        assertNotNull(resultado);
        assertEquals("PROG001", resultado.getId());
        assertEquals(4.2, resultado.getCumulativeGPA());
        assertEquals(75, resultado.getCompletedCredits());
        verify(studentRepository, times(1)).findById("ST001");
        verify(studentAcademicProgressRepository, times(1)).findByAcademicProgram("Ingeniería de Sistemas");
    }

    @Test
    @DisplayName("Caso error - getCurriculumProgress lanza excepción cuando progreso no existe")
    void testGetCurriculumProgress_ProgresoNoEncontrado() {
        when(studentRepository.findById("ST001"))
                .thenReturn(Optional.of(student));
        when(studentAcademicProgressRepository.findByAcademicProgram("Ingeniería de Sistemas"))
                .thenReturn(Collections.emptyList());

        AppException exception = assertThrows(AppException.class,
                () -> trafficLightService.getCurriculumProgress("ST001"));

        assertEquals("Progreso curricular no encontrado para el estudiante: ST001", exception.getMessage());
        verify(studentRepository, times(1)).findById("ST001");
        verify(studentAcademicProgressRepository, times(1)).findByAcademicProgram("Ingeniería de Sistemas");
    }

    @Test
    @DisplayName("Caso compuesto - Flujo completo de evaluación académica")
    void testFlujoCompletoEvaluacionAcademica() {
        when(studentRepository.findById("ST001"))
                .thenReturn(Optional.of(student));
        when(studentAcademicProgressRepository.findByAcademicProgram("Ingeniería de Sistemas"))
                .thenReturn(Arrays.asList(progress));
        when(courseStatusDetailRepository.findBySemester("5"))
                .thenReturn(Arrays.asList(courseStatus1));

        assertAll("Flujo completo de evaluación académica",
                () -> {
                    String trafficLight = trafficLightService.getAcademicTrafficLight("ST001");
                    assertNotNull(trafficLight);
                },
                () -> {
                    Student studentInfo = trafficLightService.getStudentInformation("ST001");
                    assertNotNull(studentInfo);
                },
                () -> {
                    StudentAcademicProgress curriculum = trafficLightService.getCurriculumProgress("ST001");
                    assertNotNull(curriculum);
                },
                () -> {
                    Double progressPercentage = trafficLightService.getCurriculumProgressPercentage("ST001");
                    assertNotNull(progressPercentage);
                },
                () -> {
                    boolean atRisk = trafficLightService.isStudentAtAcademicRisk("ST001");
                    assertNotNull(atRisk);
                }
        );
    }

    @Test
    @DisplayName("Caso borde - Semáforo con GPA exactamente en los límites")
    void testTrafficLight_GPA_Limites() {
        StudentAcademicProgress progressGPA30 = new StudentAcademicProgress("PROG001", student,
                "Ingeniería de Sistemas", "Facultad de Ingeniería", "Regular", 5, 10,
                80, 160, 3.0, Arrays.asList(courseStatus1));

        when(studentRepository.findById("ST001")).thenReturn(Optional.of(student));
        when(studentAcademicProgressRepository.findByAcademicProgram("Ingeniería de Sistemas"))
                .thenReturn(Arrays.asList(progressGPA30));
        when(courseStatusDetailRepository.findBySemester("5")).thenReturn(Arrays.asList(courseStatus1));

        String resultado = trafficLightService.getAcademicTrafficLight("ST001");
        assertEquals("GREEN", resultado);
    }

    @Test
    @DisplayName("Caso borde - Semáforo con progreso de créditos exacto")
    void testTrafficLight_Creditos_Limites() {
        StudentAcademicProgress progressCredits70 = new StudentAcademicProgress("PROG001", student,
                "Ingeniería de Sistemas", "Facultad de Ingeniería", "Regular", 5, 10,
                56, 160, 2.7, Arrays.asList(courseStatus1));

        when(studentRepository.findById("ST001")).thenReturn(Optional.of(student));
        when(studentAcademicProgressRepository.findByAcademicProgram("Ingeniería de Sistemas"))
                .thenReturn(Arrays.asList(progressCredits70));
        when(courseStatusDetailRepository.findBySemester("5")).thenReturn(Arrays.asList(courseStatus1));

        String resultado = trafficLightService.getAcademicTrafficLight("ST001");
        assertEquals("YELLOW", resultado);
    }
}
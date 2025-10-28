package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Student;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.StudentAcademicProgress;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.CourseStatusDetail;
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

import java.util.List;
import java.util.Optional;

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
    private CourseStatusDetail courseStatus;

    @BeforeEach
    void setUp() {
        student = new Student("1", "Robin", "robin@titans.edu", "1234", "Ingeniería", 5);
        student.setActive(true);

        progress = new StudentAcademicProgress();
        progress.setStudent(student);
        progress.setAcademicProgram("Ingeniería");
        progress.setCurrentSemester(5);
        progress.setTotalSemesters(10);
        progress.setCompletedCredits(80);
        progress.setTotalCreditsRequired(160);
        progress.setCumulativeGPA(4.2);

        courseStatus = new CourseStatusDetail();
        courseStatus.setIsApproved(true);
    }

    @Test
    @DisplayName("Caso exitoso - getAcademicTrafficLight retorna GREEN para estudiante excelente")
    void testGetAcademicTrafficLight_Exitoso_Green() {
        when(studentRepository.findById("1")).thenReturn(Optional.of(student));
        when(studentAcademicProgressRepository.findByStudentId("1")).thenReturn(Optional.of(progress));
        when(courseStatusDetailRepository.findByStudentIdAndSemester("1", "5")).thenReturn(List.of(courseStatus));

        String resultado = trafficLightService.getAcademicTrafficLight("1");

        assertEquals("GREEN", resultado);
        verify(studentRepository, times(2)).findById("1");
        verify(studentAcademicProgressRepository, times(1)).findByStudentId("1");
        verify(courseStatusDetailRepository, times(1)).findByStudentIdAndSemester("1", "5");
    }

    @Test
    @DisplayName("Caso error - getAcademicTrafficLight lanza excepción cuando estudiante no existe")
    void testGetAcademicTrafficLight_Error_EstudianteNoExiste() {
        when(studentRepository.findById("99")).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            trafficLightService.getAcademicTrafficLight("99");
        });

        assertEquals("Estudiante no encontrado: 99", exception.getMessage());
        verify(studentRepository, times(1)).findById("99");
        verify(studentAcademicProgressRepository, never()).findByStudentId(anyString());
    }

    @Test
    @DisplayName("Caso error - getAcademicTrafficLight lanza excepción cuando progreso académico no existe")
    void testGetAcademicTrafficLight_Error_ProgresoNoExiste() {
        when(studentRepository.findById("1")).thenReturn(Optional.of(student));
        when(studentAcademicProgressRepository.findByStudentId("1")).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            trafficLightService.getAcademicTrafficLight("1");
        });

        assertEquals("Progreso académico no encontrado para el estudiante: 1", exception.getMessage());
        verify(studentRepository, times(1)).findById("1");
        verify(studentAcademicProgressRepository, times(1)).findByStudentId("1");
    }


    @Test
    @DisplayName("Caso exitoso - getStudentInformation retorna estudiante activo")
    void testGetStudentInformation_Exitoso() {
        when(studentRepository.findById("1")).thenReturn(Optional.of(student));

        Student resultado = trafficLightService.getStudentInformation("1");

        assertNotNull(resultado);
        assertEquals("1", resultado.getId());
        assertEquals("Robin", resultado.getName());
        assertTrue(resultado.isActive());
        verify(studentRepository, times(1)).findById("1");
    }

    @Test
    @DisplayName("Caso error - getStudentInformation lanza excepción cuando estudiante no existe")
    void testGetStudentInformation_Error_EstudianteNoExiste() {
        when(studentRepository.findById("99")).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            trafficLightService.getStudentInformation("99");
        });

        assertEquals("Estudiante no encontrado: 99", exception.getMessage());
        verify(studentRepository, times(1)).findById("99");
    }

    @Test
    @DisplayName("Caso error - getStudentInformation lanza excepción cuando estudiante inactivo")
    void testGetStudentInformation_Error_EstudianteInactivo() {
        Student inactiveStudent = new Student("1", "Robin", "robin@titans.edu", "1234", "Ingeniería", 5);
        inactiveStudent.setActive(false);
        when(studentRepository.findById("1")).thenReturn(Optional.of(inactiveStudent));

        AppException exception = assertThrows(AppException.class, () -> {
            trafficLightService.getStudentInformation("1");
        });

        assertEquals("Estudiante inactivo: 1", exception.getMessage());
        verify(studentRepository, times(1)).findById("1");
    }

    @Test
    @DisplayName("Caso exitoso - getCurriculumProgress retorna progreso académico")
    void testGetCurriculumProgress_Exitoso() {
        when(studentAcademicProgressRepository.findByStudentId("1")).thenReturn(Optional.of(progress));

        StudentAcademicProgress resultado = trafficLightService.getCurriculumProgress("1");

        assertNotNull(resultado);
        assertEquals("Ingeniería", resultado.getAcademicProgram());
        assertEquals(5, resultado.getCurrentSemester());
        verify(studentAcademicProgressRepository, times(1)).findByStudentId("1");
    }

    @Test
    @DisplayName("Caso error - getCurriculumProgress lanza excepción cuando progreso no existe")
    void testGetCurriculumProgress_Error_ProgresoNoExiste() {
        when(studentAcademicProgressRepository.findByStudentId("1")).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            trafficLightService.getCurriculumProgress("1");
        });

        assertEquals("Progreso curricular no encontrado para el estudiante: 1", exception.getMessage());
        verify(studentAcademicProgressRepository, times(1)).findByStudentId("1");
    }

    @Test
    @DisplayName("Caso exitoso - getCurriculumProgressPercentage calcula porcentaje correcto")
    void testGetCurriculumProgressPercentage_Exitoso() {
        when(studentAcademicProgressRepository.findByStudentId("1")).thenReturn(Optional.of(progress));

        Double resultado = trafficLightService.getCurriculumProgressPercentage("1");

        assertEquals(50.0, resultado);
        verify(studentAcademicProgressRepository, times(1)).findByStudentId("1");
    }

    @Test
    @DisplayName("Caso borde - getCurriculumProgressPercentage retorna 0 cuando créditos requeridos son 0")
    void testGetCurriculumProgressPercentage_Borde_CreditosCero() {
        progress.setTotalCreditsRequired(0);
        when(studentAcademicProgressRepository.findByStudentId("1")).thenReturn(Optional.of(progress));

        Double resultado = trafficLightService.getCurriculumProgressPercentage("1");

        assertEquals(0.0, resultado);
        verify(studentAcademicProgressRepository, times(1)).findByStudentId("1");
    }

    @Test
    @DisplayName("Caso exitoso - isStudentAtAcademicRisk retorna false para estudiante GREEN")
    void testIsStudentAtAcademicRisk_Exitoso_False() {
        when(studentRepository.findById("1")).thenReturn(Optional.of(student));
        when(studentAcademicProgressRepository.findByStudentId("1")).thenReturn(Optional.of(progress));
        when(courseStatusDetailRepository.findByStudentIdAndSemester("1", "5")).thenReturn(List.of(courseStatus));

        boolean resultado = trafficLightService.isStudentAtAcademicRisk("1");

        assertFalse(resultado);
        verify(studentRepository, times(2)).findById("1");
    }



    @Test
    @DisplayName("Caso exitoso - getAcademicStatusSummary retorna resumen completo")
    void testGetAcademicStatusSummary_Exitoso() {
        when(studentRepository.findById("1")).thenReturn(Optional.of(student));
        when(studentAcademicProgressRepository.findByStudentId("1")).thenReturn(Optional.of(progress));
        when(courseStatusDetailRepository.findByStudentIdAndSemester("1", "5")).thenReturn(List.of(courseStatus));

        String resultado = trafficLightService.getAcademicStatusSummary("1");

        assertNotNull(resultado);
        assertTrue(resultado.contains("Estudiante: Robin"));
        assertTrue(resultado.contains("Programa: Ingeniería"));
        assertTrue(resultado.contains("Semáforo académico: GREEN"));
        verify(studentRepository, times(3)).findById("1");
        verify(studentAcademicProgressRepository, times(3)).findByStudentId("1");
    }

    @Test
    @DisplayName("Caso exitoso - getStudentsAtRiskByProgram filtra estudiantes en riesgo")
    void testGetStudentsAtRiskByProgram_Exitoso() {
        Student student2 = new Student("2", "Starfire", "starfire@titans.edu", "5678", "Ingeniería", 3);
        student2.setActive(true);

        when(studentRepository.findByAcademicProgramAndActive("Ingeniería", true))
                .thenReturn(List.of(student, student2));
        when(studentRepository.findById("1")).thenReturn(Optional.of(student));
        when(studentRepository.findById("2")).thenReturn(Optional.of(student2));
        when(studentAcademicProgressRepository.findByStudentId("1")).thenReturn(Optional.of(progress));
        when(studentAcademicProgressRepository.findByStudentId("2")).thenReturn(Optional.of(progress));
        when(courseStatusDetailRepository.findByStudentIdAndSemester("1", "5")).thenReturn(List.of(courseStatus));
        when(courseStatusDetailRepository.findByStudentIdAndSemester("2", "3")).thenReturn(List.of(courseStatus));

        List<Student> resultado = trafficLightService.getStudentsAtRiskByProgram("Ingeniería");

        assertNotNull(resultado);
        verify(studentRepository, times(1)).findByAcademicProgramAndActive("Ingeniería", true);
    }

    @Test
    @DisplayName("Caso exitoso - getTrafficLightStatisticsByProgram retorna estadísticas correctas")
    void testGetTrafficLightStatisticsByProgram_Exitoso() {
        Student student2 = new Student("2", "Starfire", "starfire@titans.edu", "5678", "Ingeniería", 3);
        student2.setActive(true);

        when(studentRepository.findByAcademicProgramAndActive("Ingeniería", true))
                .thenReturn(List.of(student, student2));
        when(studentRepository.findById("1")).thenReturn(Optional.of(student));
        when(studentRepository.findById("2")).thenReturn(Optional.of(student2));
        when(studentAcademicProgressRepository.findByStudentId("1")).thenReturn(Optional.of(progress));
        when(studentAcademicProgressRepository.findByStudentId("2")).thenReturn(Optional.of(progress));
        when(courseStatusDetailRepository.findByStudentIdAndSemester("1", "5")).thenReturn(List.of(courseStatus));
        when(courseStatusDetailRepository.findByStudentIdAndSemester("2", "3")).thenReturn(List.of(courseStatus));

        int[] resultado = trafficLightService.getTrafficLightStatisticsByProgram("Ingeniería");

        assertNotNull(resultado);
        assertEquals(3, resultado.length);
        assertTrue(resultado[0] >= 0);
        assertTrue(resultado[1] >= 0);
        assertTrue(resultado[2] >= 0);
        verify(studentRepository, times(1)).findByAcademicProgramAndActive("Ingeniería", true);
    }

    @Test
    @DisplayName("Caso exitoso - getStudentProgressInfo retorna información combinada")
    void testGetStudentProgressInfo_Exitoso() {
        when(studentRepository.findById("1")).thenReturn(Optional.of(student));
        when(studentAcademicProgressRepository.findByStudentId("1")).thenReturn(Optional.of(progress));
        when(courseStatusDetailRepository.findByStudentIdAndSemester("1", "5")).thenReturn(List.of(courseStatus));

        TrafficLightService.StudentProgressInfo resultado = trafficLightService.getStudentProgressInfo("1");

        assertNotNull(resultado);
        assertEquals(student, resultado.getStudent());
        assertEquals(progress, resultado.getProgress());
        assertEquals("GREEN", resultado.getTrafficLight());
        assertEquals(50.0, resultado.getProgressPercentage());
        verify(studentRepository, times(3)).findById("1");
        verify(studentAcademicProgressRepository, times(3)).findByStudentId("1");
    }

    @Test
    @DisplayName("Caso borde - calculateTrafficLight retorna YELLOW para GPA mínimo")
    void testCalculateTrafficLight_Borde_Yellow() {
        progress.setCumulativeGPA(2.5);
        progress.setCompletedCredits(56);

        CourseStatusDetail failedCourse = new CourseStatusDetail();
        failedCourse.setIsApproved(false);

        when(studentRepository.findById("1")).thenReturn(Optional.of(student));
        when(studentAcademicProgressRepository.findByStudentId("1")).thenReturn(Optional.of(progress));
        when(courseStatusDetailRepository.findByStudentIdAndSemester("1", "5"))
                .thenReturn(List.of(courseStatus, failedCourse));

        String resultado = trafficLightService.getAcademicTrafficLight("1");

        assertEquals("YELLOW", resultado);
    }

    @Test
    @DisplayName("Caso borde - calculateTrafficLight retorna RED para GPA bajo")
    void testCalculateTrafficLight_Borde_Red() {
        progress.setCumulativeGPA(2.0);
        progress.setCompletedCredits(40);

        CourseStatusDetail failedCourse1 = new CourseStatusDetail();
        failedCourse1.setIsApproved(false);
        CourseStatusDetail failedCourse2 = new CourseStatusDetail();
        failedCourse2.setIsApproved(false);

        when(studentRepository.findById("1")).thenReturn(Optional.of(student));
        when(studentAcademicProgressRepository.findByStudentId("1")).thenReturn(Optional.of(progress));
        when(courseStatusDetailRepository.findByStudentIdAndSemester("1", "5"))
                .thenReturn(List.of(failedCourse1, failedCourse2));

        String resultado = trafficLightService.getAcademicTrafficLight("1");

        assertEquals("RED", resultado);
    }
}
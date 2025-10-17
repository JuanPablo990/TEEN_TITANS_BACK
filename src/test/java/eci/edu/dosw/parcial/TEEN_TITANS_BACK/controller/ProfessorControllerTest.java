package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Professor;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.ProfessorService;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfessorControllerTest {

    @Mock
    private ProfessorService professorService;

    @InjectMocks
    private ProfessorController professorController;

    private Professor professor;
    private String professorId;

    @BeforeEach
    void setUp() {
        professorId = "123";
        professor = new Professor("Computer Science", true, Arrays.asList("AI", "Algorithms"));
    }

    @Test
    void createProfessor_HappyPath() {
        when(professorService.createProfessor(any(Professor.class))).thenReturn(professor);

        ResponseEntity<?> response = professorController.createProfessor(professor);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(professor, response.getBody());
        verify(professorService, times(1)).createProfessor(professor);
    }

    @Test
    void createProfessor_ErrorPath() {
        when(professorService.createProfessor(any(Professor.class))).thenThrow(new AppException("Error creating professor"));

        ResponseEntity<?> response = professorController.createProfessor(professor);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error creating professor", response.getBody());
        verify(professorService, times(1)).createProfessor(professor);
    }

    @Test
    void getProfessorById_HappyPath() {
        when(professorService.getProfessorById(professorId)).thenReturn(professor);

        ResponseEntity<?> response = professorController.getProfessorById(professorId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(professor, response.getBody());
        verify(professorService, times(1)).getProfessorById(professorId);
    }

    @Test
    void getProfessorById_ErrorPath() {
        when(professorService.getProfessorById(professorId)).thenThrow(new AppException("Professor not found"));

        ResponseEntity<?> response = professorController.getProfessorById(professorId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Professor not found", response.getBody());
        verify(professorService, times(1)).getProfessorById(professorId);
    }

    @Test
    void getAllProfessors_HappyPath() {
        List<Professor> professors = Arrays.asList(professor, new Professor("Mathematics", false, Arrays.asList("Calculus")));
        when(professorService.getAllProfessors()).thenReturn(professors);

        ResponseEntity<?> response = professorController.getAllProfessors();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(professors, response.getBody());
        verify(professorService, times(1)).getAllProfessors();
    }

    @Test
    void getAllProfessors_ErrorPath() {
        when(professorService.getAllProfessors()).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = professorController.getAllProfessors();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Error interno del servidor"));
        verify(professorService, times(1)).getAllProfessors();
    }

    @Test
    void updateProfessor_HappyPath() {
        when(professorService.updateProfessor(eq(professorId), any(Professor.class))).thenReturn(professor);

        ResponseEntity<?> response = professorController.updateProfessor(professorId, professor);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(professor, response.getBody());
        verify(professorService, times(1)).updateProfessor(professorId, professor);
    }

    @Test
    void updateProfessor_ErrorPath() {
        when(professorService.updateProfessor(eq(professorId), any(Professor.class))).thenThrow(new AppException("Update failed"));

        ResponseEntity<?> response = professorController.updateProfessor(professorId, professor);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Update failed", response.getBody());
        verify(professorService, times(1)).updateProfessor(professorId, professor);
    }

    @Test
    void deleteProfessor_HappyPath() {
        doNothing().when(professorService).deleteProfessor(professorId);

        ResponseEntity<?> response = professorController.deleteProfessor(professorId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Profesor eliminado exitosamente", response.getBody());
        verify(professorService, times(1)).deleteProfessor(professorId);
    }

    @Test
    void deleteProfessor_ErrorPath() {
        doThrow(new AppException("Professor not found")).when(professorService).deleteProfessor(professorId);

        ResponseEntity<?> response = professorController.deleteProfessor(professorId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Professor not found", response.getBody());
        verify(professorService, times(1)).deleteProfessor(professorId);
    }

    @Test
    void findByDepartment_HappyPath() {
        List<Professor> professors = Arrays.asList(professor);
        when(professorService.findByDepartment("Computer Science")).thenReturn(professors);

        ResponseEntity<?> response = professorController.findByDepartment("Computer Science");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(professors, response.getBody());
        verify(professorService, times(1)).findByDepartment("Computer Science");
    }

    @Test
    void findByDepartment_ErrorPath() {
        when(professorService.findByDepartment("Computer Science")).thenThrow(new RuntimeException("Service error"));

        ResponseEntity<?> response = professorController.findByDepartment("Computer Science");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Error interno del servidor"));
        verify(professorService, times(1)).findByDepartment("Computer Science");
    }

    @Test
    void findByTenured_HappyPath() {
        List<Professor> professors = Arrays.asList(professor);
        when(professorService.findByTenured(true)).thenReturn(professors);

        ResponseEntity<?> response = professorController.findByTenured(true);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(professors, response.getBody());
        verify(professorService, times(1)).findByTenured(true);
    }

    @Test
    void findByTenured_ErrorPath() {
        when(professorService.findByTenured(true)).thenThrow(new RuntimeException("Service error"));

        ResponseEntity<?> response = professorController.findByTenured(true);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Error interno del servidor"));
        verify(professorService, times(1)).findByTenured(true);
    }

    @Test
    void findByAreaOfExpertise_HappyPath() {
        List<Professor> professors = Arrays.asList(professor);
        when(professorService.findByAreaOfExpertise("AI")).thenReturn(professors);

        ResponseEntity<?> response = professorController.findByAreaOfExpertise("AI");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(professors, response.getBody());
        verify(professorService, times(1)).findByAreaOfExpertise("AI");
    }

    @Test
    void findByAreaOfExpertise_ErrorPath() {
        when(professorService.findByAreaOfExpertise("AI")).thenThrow(new RuntimeException("Service error"));

        ResponseEntity<?> response = professorController.findByAreaOfExpertise("AI");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Error interno del servidor"));
        verify(professorService, times(1)).findByAreaOfExpertise("AI");
    }

    @Test
    void findActiveProfessors_HappyPath() {
        List<Professor> professors = Arrays.asList(professor);
        when(professorService.findActiveProfessors()).thenReturn(professors);

        ResponseEntity<?> response = professorController.findActiveProfessors();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(professors, response.getBody());
        verify(professorService, times(1)).findActiveProfessors();
    }

    @Test
    void findActiveProfessors_ErrorPath() {
        when(professorService.findActiveProfessors()).thenThrow(new RuntimeException("Service error"));

        ResponseEntity<?> response = professorController.findActiveProfessors();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Error interno del servidor"));
        verify(professorService, times(1)).findActiveProfessors();
    }

    @Test
    void getProfessorStats_HappyPath() {
        List<Professor> allProfessors = Arrays.asList(
                professor,
                new Professor("Mathematics", false, Arrays.asList("Calculus")),
                new Professor("Physics", true, Arrays.asList("Quantum Mechanics"))
        );
        List<Professor> tenuredProfessors = Arrays.asList(professor, allProfessors.get(2));
        List<Professor> activeProfessors = Arrays.asList(professor, allProfessors.get(1));

        when(professorService.getAllProfessors()).thenReturn(allProfessors);
        when(professorService.findByTenured(true)).thenReturn(tenuredProfessors);
        when(professorService.findActiveProfessors()).thenReturn(activeProfessors);

        ResponseEntity<?> response = professorController.getProfessorStats();

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Map<String, Object> stats = (Map<String, Object>) response.getBody();
        assertEquals(3, stats.get("totalProfessors"));
        assertEquals(2, stats.get("tenuredProfessors"));
        assertEquals(2, stats.get("activeProfessors"));
        assertEquals(66.67, stats.get("tenuredPercentage"));

        verify(professorService, times(1)).getAllProfessors();
        verify(professorService, times(1)).findByTenured(true);
        verify(professorService, times(1)).findActiveProfessors();
    }

    @Test
    void getProfessorStats_ErrorPath() {
        when(professorService.getAllProfessors()).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = professorController.getProfessorStats();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Error interno del servidor"));
        verify(professorService, times(1)).getAllProfessors();
    }

    @Test
    void existsById_HappyPath_Exists() {
        when(professorService.getProfessorById(professorId)).thenReturn(professor);

        ResponseEntity<?> response = professorController.existsById(professorId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());
        verify(professorService, times(1)).getProfessorById(professorId);
    }

    @Test
    void existsById_HappyPath_NotExists() {
        when(professorService.getProfessorById(professorId)).thenThrow(new AppException("Not found"));

        ResponseEntity<?> response = professorController.existsById(professorId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(false, response.getBody());
        verify(professorService, times(1)).getProfessorById(professorId);
    }

    @Test
    void existsById_ErrorPath() {
        when(professorService.getProfessorById(professorId)).thenThrow(new RuntimeException("Service error"));

        ResponseEntity<?> response = professorController.existsById(professorId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Error interno del servidor"));
        verify(professorService, times(1)).getProfessorById(professorId);
    }

    @Test
    void healthCheck_HappyPath() {
        ResponseEntity<String> response = professorController.healthCheck();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("ProfessorController is working properly", response.getBody());
    }
}
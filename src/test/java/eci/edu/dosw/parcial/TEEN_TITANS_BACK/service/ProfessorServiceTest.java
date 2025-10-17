package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Professor;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.UserRole;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.ProfessorRepository;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfessorServiceTest {

    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProfessorService professorService;

    @Test
    void createProfessor_WhenEmailNotExists_ShouldCreateProfessor() {
        Professor professor = new Professor("Computer Science", true, Arrays.asList("AI", "Algorithms"));
        professor.setEmail("professor@university.edu");
        professor.setName("John Doe");

        when(userRepository.existsByEmail("professor@university.edu")).thenReturn(false);
        when(professorRepository.save(any(Professor.class))).thenReturn(professor);

        Professor result = professorService.createProfessor(professor);

        assertNotNull(result);
        assertEquals(UserRole.PROFESSOR, result.getRole());
        assertEquals("Computer Science", result.getDepartment());
        verify(userRepository).existsByEmail("professor@university.edu");
        verify(professorRepository).save(professor);
    }

    @Test
    void getProfessorById_WhenProfessorExists_ShouldReturnProfessor() {
        String professorId = "12345";
        Professor professor = new Professor("Mathematics", false, Arrays.asList("Calculus"));
        professor.setId(professorId);
        professor.setName("Jane Smith");

        when(professorRepository.findById(professorId)).thenReturn(Optional.of(professor));

        Professor result = professorService.getProfessorById(professorId);

        assertNotNull(result);
        assertEquals(professorId, result.getId());
        assertEquals("Mathematics", result.getDepartment());
        verify(professorRepository).findById(professorId);
    }

    @Test
    void updateProfessor_WhenValidData_ShouldUpdateProfessor() {
        String professorId = "12345";
        Professor existingProfessor = new Professor("Physics", true, Arrays.asList("Mechanics"));
        existingProfessor.setId(professorId);
        existingProfessor.setEmail("old@email.com");

        Professor updatedProfessor = new Professor("Quantum Physics", true, Arrays.asList("Quantum Mechanics"));
        updatedProfessor.setEmail("new@email.com");

        when(professorRepository.findById(professorId)).thenReturn(Optional.of(existingProfessor));
        when(userRepository.existsByEmail("new@email.com")).thenReturn(false);
        when(professorRepository.save(any(Professor.class))).thenReturn(updatedProfessor);

        Professor result = professorService.updateProfessor(professorId, updatedProfessor);

        assertNotNull(result);
        assertEquals("new@email.com", result.getEmail());
        assertEquals("Quantum Physics", result.getDepartment());
        verify(professorRepository).findById(professorId);
        verify(professorRepository).save(updatedProfessor);
    }

    @Test
    void findByDepartment_WhenDepartmentExists_ShouldReturnProfessors() {
        String department = "Computer Science";
        Professor professor1 = new Professor(department, true, Arrays.asList("Programming"));
        Professor professor2 = new Professor(department, false, Arrays.asList("Databases"));
        List<Professor> expectedProfessors = Arrays.asList(professor1, professor2);

        when(professorRepository.findByDepartment(department)).thenReturn(expectedProfessors);

        List<Professor> result = professorService.findByDepartment(department);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(department, result.get(0).getDepartment());
        assertEquals(department, result.get(1).getDepartment());
        verify(professorRepository).findByDepartment(department);
    }

    @Test
    void createProfessor_WhenEmailExists_ShouldThrowException() {
        Professor professor = new Professor("Biology", false, Arrays.asList("Genetics"));
        professor.setEmail("existing@email.com");

        when(userRepository.existsByEmail("existing@email.com")).thenReturn(true);

        AppException exception = assertThrows(AppException.class, () -> {
            professorService.createProfessor(professor);
        });

        assertEquals("El email ya está registrado: existing@email.com", exception.getMessage());
        verify(userRepository).existsByEmail("existing@email.com");
        verify(professorRepository, never()).save(any(Professor.class));
    }

    @Test
    void getProfessorById_WhenProfessorNotExists_ShouldThrowException() {
        String nonExistentId = "99999";
        when(professorRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            professorService.getProfessorById(nonExistentId);
        });

        assertEquals("Profesor no encontrado con ID: 99999", exception.getMessage());
        verify(professorRepository).findById(nonExistentId);
    }

    @Test
    void updateProfessor_WhenEmailTakenByOtherUser_ShouldThrowException() {
        String professorId = "12345";
        Professor existingProfessor = new Professor("Chemistry", true, Arrays.asList("Organic"));
        existingProfessor.setId(professorId);
        existingProfessor.setEmail("current@email.com");

        Professor updatedProfessor = new Professor("Chemistry", true, Arrays.asList("Inorganic"));
        updatedProfessor.setEmail("taken@email.com");

        when(professorRepository.findById(professorId)).thenReturn(Optional.of(existingProfessor));
        when(userRepository.existsByEmail("taken@email.com")).thenReturn(true);

        AppException exception = assertThrows(AppException.class, () -> {
            professorService.updateProfessor(professorId, updatedProfessor);
        });

        assertEquals("El email ya está en uso por otro usuario: taken@email.com", exception.getMessage());
        verify(professorRepository).findById(professorId);
        verify(userRepository).existsByEmail("taken@email.com");
        verify(professorRepository, never()).save(any(Professor.class));
    }

    @Test
    void deleteProfessor_WhenProfessorNotExists_ShouldThrowException() {
        String nonExistentId = "99999";
        when(professorRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            professorService.deleteProfessor(nonExistentId);
        });

        assertEquals("Profesor no encontrado con ID: 99999", exception.getMessage());
        verify(professorRepository).findById(nonExistentId);
        verify(professorRepository, never()).deleteById(anyString());
    }
}
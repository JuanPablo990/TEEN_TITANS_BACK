package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Professor;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ProfessorRepositoryTest {

    @Mock
    private ProfessorRepository professorRepository;

    private Professor tenuredProfessor;
    private Professor nonTenuredProfessor;

    @BeforeEach
    void setUp() {
        tenuredProfessor = new Professor(
                "Ingeniería de Sistemas",
                true,
                Arrays.asList("Inteligencia Artificial", "Base de Datos", "Algoritmos")
        );

        nonTenuredProfessor = new Professor(
                "Matemáticas",
                false,
                Arrays.asList("Cálculo", "Álgebra Lineal", "Estadística")
        );
    }

    @Test
    void findByDepartment_HappyPath_ReturnsProfessors() {
        String department = "Ingeniería de Sistemas";
        List<Professor> expectedProfessors = Collections.singletonList(tenuredProfessor);
        when(professorRepository.findByDepartment(department)).thenReturn(expectedProfessors);

        List<Professor> result = professorRepository.findByDepartment(department);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(department, result.get(0).getDepartment());
        verify(professorRepository, times(1)).findByDepartment(department);
    }

    @Test
    void findByDepartment_HappyPath_DifferentDepartment() {
        String department = "Matemáticas";
        List<Professor> expectedProfessors = Collections.singletonList(nonTenuredProfessor);
        when(professorRepository.findByDepartment(department)).thenReturn(expectedProfessors);

        List<Professor> result = professorRepository.findByDepartment(department);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(department, result.get(0).getDepartment());
        verify(professorRepository, times(1)).findByDepartment(department);
    }

    @Test
    void findByDepartment_Error_NoProfessorsInDepartment() {
        String department = "Departamento Inexistente";
        when(professorRepository.findByDepartment(department)).thenReturn(Collections.emptyList());

        List<Professor> result = professorRepository.findByDepartment(department);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(professorRepository, times(1)).findByDepartment(department);
    }

    @Test
    void findByDepartment_Error_NullParameter() {
        when(professorRepository.findByDepartment(null)).thenReturn(Collections.emptyList());

        List<Professor> result = professorRepository.findByDepartment(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(professorRepository, times(1)).findByDepartment(null);
    }

    @Test
    void findByIsTenured_HappyPath_TenuredProfessors() {
        List<Professor> tenuredProfessors = Collections.singletonList(tenuredProfessor);
        when(professorRepository.findByIsTenured(true)).thenReturn(tenuredProfessors);

        List<Professor> result = professorRepository.findByIsTenured(true);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).isTenured());
        verify(professorRepository, times(1)).findByIsTenured(true);
    }

    @Test
    void findByIsTenured_HappyPath_NonTenuredProfessors() {
        List<Professor> nonTenuredProfessors = Collections.singletonList(nonTenuredProfessor);
        when(professorRepository.findByIsTenured(false)).thenReturn(nonTenuredProfessors);

        List<Professor> result = professorRepository.findByIsTenured(false);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertFalse(result.get(0).isTenured());
        verify(professorRepository, times(1)).findByIsTenured(false);
    }

    @Test
    void findByIsTenured_Error_NoTenuredProfessors() {
        when(professorRepository.findByIsTenured(true)).thenReturn(Collections.emptyList());

        List<Professor> result = professorRepository.findByIsTenured(true);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(professorRepository, times(1)).findByIsTenured(true);
    }

    @Test
    void findByIsTenured_Error_NoNonTenuredProfessors() {
        when(professorRepository.findByIsTenured(false)).thenReturn(Collections.emptyList());

        List<Professor> result = professorRepository.findByIsTenured(false);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(professorRepository, times(1)).findByIsTenured(false);
    }

    @Test
    void findByAreasOfExpertiseContaining_HappyPath_ReturnsProfessors() {
        String expertise = "Inteligencia Artificial";
        List<Professor> expectedProfessors = Collections.singletonList(tenuredProfessor);
        when(professorRepository.findByAreasOfExpertiseContaining(expertise)).thenReturn(expectedProfessors);

        List<Professor> result = professorRepository.findByAreasOfExpertiseContaining(expertise);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getAreasOfExpertise().contains(expertise));
        verify(professorRepository, times(1)).findByAreasOfExpertiseContaining(expertise);
    }

    @Test
    void findByAreasOfExpertiseContaining_HappyPath_DifferentExpertise() {
        String expertise = "Cálculo";
        List<Professor> expectedProfessors = Collections.singletonList(nonTenuredProfessor);
        when(professorRepository.findByAreasOfExpertiseContaining(expertise)).thenReturn(expectedProfessors);

        List<Professor> result = professorRepository.findByAreasOfExpertiseContaining(expertise);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getAreasOfExpertise().contains(expertise));
        verify(professorRepository, times(1)).findByAreasOfExpertiseContaining(expertise);
    }

    @Test
    void findByAreasOfExpertiseContaining_Error_NoProfessorsWithExpertise() {
        String expertise = "Expertise Inexistente";
        when(professorRepository.findByAreasOfExpertiseContaining(expertise)).thenReturn(Collections.emptyList());

        List<Professor> result = professorRepository.findByAreasOfExpertiseContaining(expertise);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(professorRepository, times(1)).findByAreasOfExpertiseContaining(expertise);
    }

    @Test
    void findByAreasOfExpertiseContaining_Error_NullParameter() {
        when(professorRepository.findByAreasOfExpertiseContaining(null)).thenReturn(Collections.emptyList());

        List<Professor> result = professorRepository.findByAreasOfExpertiseContaining(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(professorRepository, times(1)).findByAreasOfExpertiseContaining(null);
    }
}
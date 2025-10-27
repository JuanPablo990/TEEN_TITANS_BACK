package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Professor;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.ProfessorRepository;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProfessorServiceTest {

    @Mock
    private ProfessorRepository professorRepository;

    @InjectMocks
    private ProfessorService professorService;

    private Professor professor1;
    private Professor professor2;
    private Professor professor3;

    @BeforeEach
    void setUp() {
        professor1 = new Professor("1", "Dr. Bruce Banner", "bruce.banner@titans.edu", "password123",
                "Physics", true, Arrays.asList("Nuclear Physics", "Gamma Radiation"));
        professor2 = new Professor("2", "Dr. Charles Xavier", "charles.xavier@titans.edu", "password456",
                "Genetics", true, Arrays.asList("Telepathy", "Mutation Studies"));
        professor3 = new Professor("3", "Dr. Reed Richards", "reed.richards@titans.edu", "password789",
                "Physics", false, Arrays.asList("Quantum Mechanics", "Space Travel"));
    }

    @Test
    @DisplayName("Caso exitoso - createProfessor crea profesor correctamente")
    void testCreateProfessor_Exitoso() {
        when(professorRepository.existsByEmail(professor1.getEmail())).thenReturn(false);
        when(professorRepository.save(any(Professor.class))).thenReturn(professor1);

        Professor resultado = professorService.createProfessor(professor1);

        assertAll("Verificar creación de profesor",
                () -> assertNotNull(resultado),
                () -> assertEquals("Dr. Bruce Banner", resultado.getName()),
                () -> assertEquals("Physics", resultado.getDepartment()),
                () -> assertEquals(UserRole.PROFESSOR, resultado.getRole())
        );

        verify(professorRepository, times(1)).existsByEmail(professor1.getEmail());
        verify(professorRepository, times(1)).save(professor1);
    }

    @Test
    @DisplayName("Caso error - createProfessor lanza excepción cuando email ya existe")
    void testCreateProfessor_EmailExistente() {
        when(professorRepository.existsByEmail(professor1.getEmail())).thenReturn(true);

        AppException exception = assertThrows(AppException.class,
                () -> professorService.createProfessor(professor1));

        assertEquals("El email ya está registrado: " + professor1.getEmail(), exception.getMessage());

        verify(professorRepository, never()).save(any(Professor.class));
        verify(professorRepository, times(1)).existsByEmail(professor1.getEmail());
    }

    @Test
    @DisplayName("Caso exitoso - getProfessorById retorna profesor existente")
    void testGetProfessorById_Exitoso() {
        when(professorRepository.findById("1")).thenReturn(Optional.of(professor1));

        Professor resultado = professorService.getProfessorById("1");

        assertAll("Verificar profesor encontrado",
                () -> assertNotNull(resultado),
                () -> assertEquals("1", resultado.getId()),
                () -> assertEquals("Dr. Bruce Banner", resultado.getName())
        );

        verify(professorRepository, times(1)).findById("1");
    }

    @Test
    @DisplayName("Caso error - getProfessorById lanza excepción cuando ID no existe")
    void testGetProfessorById_NoEncontrado() {
        when(professorRepository.findById("99")).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class,
                () -> professorService.getProfessorById("99"));

        assertEquals("Profesor no encontrado con ID: 99", exception.getMessage());
        verify(professorRepository, times(1)).findById("99");
    }

    @Test
    @DisplayName("Caso exitoso - getAllProfessors retorna todos los profesores")
    void testGetAllProfessors_Exitoso() {
        List<Professor> profesores = Arrays.asList(professor1, professor2, professor3);
        when(professorRepository.findAll()).thenReturn(profesores);

        List<Professor> resultado = professorService.getAllProfessors();

        assertAll("Verificar lista de profesores",
                () -> assertNotNull(resultado),
                () -> assertEquals(3, resultado.size()),
                () -> assertEquals("Dr. Bruce Banner", resultado.get(0).getName())
        );

        verify(professorRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Caso borde - getAllProfessors retorna lista vacía cuando no hay profesores")
    void testGetAllProfessors_ListaVacia() {
        when(professorRepository.findAll()).thenReturn(Collections.emptyList());

        List<Professor> resultado = professorService.getAllProfessors();

        assertTrue(resultado.isEmpty());
        verify(professorRepository, times(1)).findAll();
    }


    @Test
    @DisplayName("Caso error - updateProfessor lanza excepción cuando email ya está en uso")
    void testUpdateProfessor_EmailEnUso() {
        when(professorRepository.findById("1")).thenReturn(Optional.of(professor1));
        when(professorRepository.findByEmail("nuevo.email@titans.edu")).thenReturn(Arrays.asList(professor2));

        Professor profesorActualizado = new Professor("1", "Dr. Bruce Banner",
                "nuevo.email@titans.edu", "password123",
                "Physics", true,
                Arrays.asList("Nuclear Physics"));

        AppException exception = assertThrows(AppException.class,
                () -> professorService.updateProfessor("1", profesorActualizado));

        assertEquals("El email ya está en uso por otro profesor: nuevo.email@titans.edu", exception.getMessage());

        verify(professorRepository, times(1)).findById("1");
        verify(professorRepository, times(1)).findByEmail("nuevo.email@titans.edu");
        verify(professorRepository, never()).save(any(Professor.class));
    }

    @Test
    @DisplayName("Caso exitoso - deleteProfessor elimina profesor existente")
    void testDeleteProfessor_Exitoso() {
        when(professorRepository.existsById("1")).thenReturn(true);
        doNothing().when(professorRepository).deleteById("1");

        assertDoesNotThrow(() -> professorService.deleteProfessor("1"));

        verify(professorRepository, times(1)).existsById("1");
        verify(professorRepository, times(1)).deleteById("1");
    }

    @Test
    @DisplayName("Caso error - deleteProfessor lanza excepción cuando ID no existe")
    void testDeleteProfessor_NoEncontrado() {
        when(professorRepository.existsById("99")).thenReturn(false);

        AppException exception = assertThrows(AppException.class,
                () -> professorService.deleteProfessor("99"));

        assertEquals("Profesor no encontrado con ID: 99", exception.getMessage());

        verify(professorRepository, times(1)).existsById("99");
        verify(professorRepository, never()).deleteById(anyString());
    }

    @Test
    @DisplayName("Caso exitoso - findByDepartment retorna profesores del departamento")
    void testFindByDepartment_Exitoso() {
        List<Professor> profesoresFisica = Arrays.asList(professor1, professor3);
        when(professorRepository.findByDepartment("Physics")).thenReturn(profesoresFisica);

        List<Professor> resultado = professorService.findByDepartment("Physics");

        assertAll("Verificar profesores por departamento",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertTrue(resultado.stream().allMatch(p -> "Physics".equals(p.getDepartment())))
        );

        verify(professorRepository, times(1)).findByDepartment("Physics");
    }

    @Test
    @DisplayName("Caso exitoso - findByTenured retorna profesores titulares")
    void testFindByTenured_Exitoso() {
        List<Professor> profesoresTitulares = Arrays.asList(professor1, professor2);
        when(professorRepository.findByIsTenured(true)).thenReturn(profesoresTitulares);

        List<Professor> resultado = professorService.findByTenured(true);

        assertAll("Verificar profesores titulares",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertTrue(resultado.stream().allMatch(Professor::getIsTenured))
        );

        verify(professorRepository, times(1)).findByIsTenured(true);
    }

    @Test
    @DisplayName("Caso exitoso - findByAreaOfExpertise retorna profesores por área")
    void testFindByAreaOfExpertise_Exitoso() {
        List<Professor> profesoresFisicaNuclear = Arrays.asList(professor1);
        when(professorRepository.findByAreasOfExpertiseContaining("Nuclear Physics"))
                .thenReturn(profesoresFisicaNuclear);

        List<Professor> resultado = professorService.findByAreaOfExpertise("Nuclear Physics");

        assertAll("Verificar profesores por área de expertise",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("Dr. Bruce Banner", resultado.get(0).getName())
        );

        verify(professorRepository, times(1)).findByAreasOfExpertiseContaining("Nuclear Physics");
    }

    @Test
    @DisplayName("Caso borde - findByDepartmentAndTenured retorna lista vacía cuando no hay coincidencias")
    void testFindByDepartmentAndTenured_SinCoincidencias() {
        when(professorRepository.findByDepartmentAndIsTenured("Chemistry", true))
                .thenReturn(Collections.emptyList());

        List<Professor> resultado = professorService.findByDepartmentAndTenured("Chemistry", true);

        assertTrue(resultado.isEmpty());
        verify(professorRepository, times(1)).findByDepartmentAndIsTenured("Chemistry", true);
    }

    @Test
    @DisplayName("Caso exitoso - countByDepartment retorna conteo correcto")
    void testCountByDepartment_Exitoso() {
        when(professorRepository.countByDepartment("Physics")).thenReturn(2L);

        long resultado = professorService.countByDepartment("Physics");

        assertEquals(2L, resultado);
        verify(professorRepository, times(1)).countByDepartment("Physics");
    }

    @Test
    @DisplayName("Caso borde - countByTenured retorna cero cuando no hay titulares")
    void testCountByTenured_Cero() {
        when(professorRepository.countByIsTenured(false)).thenReturn(0L);

        long resultado = professorService.countByTenured(false);

        assertEquals(0L, resultado);
        verify(professorRepository, times(1)).countByIsTenured(false);
    }

    @Test
    @DisplayName("Caso exitoso - findActiveProfessors retorna profesores activos")
    void testFindActiveProfessors_Exitoso() {
        List<Professor> profesoresActivos = Arrays.asList(professor1, professor2);
        when(professorRepository.findByActive(true)).thenReturn(profesoresActivos);

        List<Professor> resultado = professorService.findActiveProfessors();

        assertAll("Verificar profesores activos",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size())
        );

        verify(professorRepository, times(1)).findByActive(true);
    }

    @Test
    @DisplayName("Caso exitoso - findByAreasOfExpertiseIn retorna profesores por múltiples áreas")
    void testFindByAreasOfExpertiseIn_Exitoso() {
        List<String> areas = Arrays.asList("Nuclear Physics", "Quantum Mechanics");
        List<Professor> profesores = Arrays.asList(professor1, professor3);
        when(professorRepository.findByAreasOfExpertiseIn(areas)).thenReturn(profesores);

        List<Professor> resultado = professorService.findByAreasOfExpertiseIn(areas);

        assertAll("Verificar profesores por múltiples áreas",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size())
        );

        verify(professorRepository, times(1)).findByAreasOfExpertiseIn(areas);
    }

    @Test
    @DisplayName("Caso exitoso - findByNameContaining retorna profesores por nombre")
    void testFindByNameContaining_Exitoso() {
        List<Professor> profesores = Arrays.asList(professor1);
        when(professorRepository.findByNameContainingIgnoreCase("Bruce")).thenReturn(profesores);

        List<Professor> resultado = professorService.findByNameContaining("Bruce");

        assertAll("Verificar profesores por nombre",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("Dr. Bruce Banner", resultado.get(0).getName())
        );

        verify(professorRepository, times(1)).findByNameContainingIgnoreCase("Bruce");
    }

    @Test
    @DisplayName("Caso exitoso - findByDepartmentPattern retorna profesores por patrón de departamento")
    void testFindByDepartmentPattern_Exitoso() {
        List<Professor> profesores = Arrays.asList(professor1, professor3);
        when(professorRepository.findByDepartmentRegex("Phys")).thenReturn(profesores);

        List<Professor> resultado = professorService.findByDepartmentPattern("Phys");

        assertAll("Verificar profesores por patrón de departamento",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size())
        );

        verify(professorRepository, times(1)).findByDepartmentRegex("Phys");
    }
}
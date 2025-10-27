package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Professor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProfessorRepositoryTest {

    @Mock
    private ProfessorRepository professorRepository;

    private Professor professor1;
    private Professor professor2;
    private Professor professor3;

    @BeforeEach
    void setUp() {
        List<String> areas1 = Arrays.asList("Nuclear Physics", "Gamma Radiation", "Quantum Mechanics");
        List<String> areas2 = Arrays.asList("Genetics", "Mutation Studies", "Telepathy");
        List<String> areas3 = Arrays.asList("Computer Science", "Artificial Intelligence", "Machine Learning");

        professor1 = new Professor("1", "Dr. Bruce Banner", "bruce.banner@titans.edu", "password123",
                "Physics", true, areas1);
        professor2 = new Professor("2", "Dr. Charles Xavier", "charles.xavier@titans.edu", "password456",
                "Genetics", true, areas2);
        professor3 = new Professor("3", "Dr. Tony Stark", "tony.stark@titans.edu", "password789",
                "Computer Science", false, areas3);
    }

    @Test
    @DisplayName("Caso exitoso - save guarda profesor correctamente")
    void testSave_Exitoso() {
        List<String> newAreas = Arrays.asList("Robotics", "Engineering");
        Professor newProfessor = new Professor("4", "Dr. Reed Richards", "reed.richards@titans.edu", "password000",
                "Engineering", true, newAreas);

        when(professorRepository.save(newProfessor)).thenReturn(newProfessor);

        Professor savedProfessor = professorRepository.save(newProfessor);

        assertAll("Verificar guardado de profesor",
                () -> assertNotNull(savedProfessor),
                () -> assertEquals("Dr. Reed Richards", savedProfessor.getName()),
                () -> assertEquals("Engineering", savedProfessor.getDepartment()),
                () -> assertTrue(savedProfessor.getIsTenured()),
                () -> assertEquals(2, savedProfessor.getAreasOfExpertise().size())
        );

        verify(professorRepository, times(1)).save(newProfessor);
    }

    @Test
    @DisplayName("Caso exitoso - findById retorna profesor existente")
    void testFindById_Exitoso() {
        when(professorRepository.findById("1")).thenReturn(Optional.of(professor1));

        Optional<Professor> foundProfessor = professorRepository.findById("1");

        assertAll("Verificar búsqueda por ID",
                () -> assertTrue(foundProfessor.isPresent()),
                () -> assertEquals("Dr. Bruce Banner", foundProfessor.get().getName()),
                () -> assertEquals("Physics", foundProfessor.get().getDepartment())
        );

        verify(professorRepository, times(1)).findById("1");
    }

    @Test
    @DisplayName("Caso error - findById retorna vacío para ID inexistente")
    void testFindById_NoEncontrado() {
        when(professorRepository.findById("99")).thenReturn(Optional.empty());

        Optional<Professor> foundProfessor = professorRepository.findById("99");

        assertTrue(foundProfessor.isEmpty());
        verify(professorRepository, times(1)).findById("99");
    }

    @Test
    @DisplayName("Caso exitoso - findAll retorna todos los profesores")
    void testFindAll_Exitoso() {
        List<Professor> professors = Arrays.asList(professor1, professor2, professor3);
        when(professorRepository.findAll()).thenReturn(professors);

        List<Professor> result = professorRepository.findAll();

        assertAll("Verificar búsqueda de todos los profesores",
                () -> assertNotNull(result),
                () -> assertEquals(3, result.size()),
                () -> assertEquals("Dr. Bruce Banner", result.get(0).getName())
        );

        verify(professorRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Caso borde - findAll retorna lista vacía cuando no hay profesores")
    void testFindAll_ListaVacia() {
        when(professorRepository.findAll()).thenReturn(Collections.emptyList());

        List<Professor> result = professorRepository.findAll();

        assertTrue(result.isEmpty());
        verify(professorRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Caso exitoso - findByDepartment retorna profesores del departamento")
    void testFindByDepartment_Exitoso() {
        List<Professor> physicsProfessors = Arrays.asList(professor1);
        when(professorRepository.findByDepartment("Physics")).thenReturn(physicsProfessors);

        List<Professor> result = professorRepository.findByDepartment("Physics");

        assertAll("Verificar búsqueda por departamento",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("Physics", result.get(0).getDepartment()),
                () -> assertEquals("Dr. Bruce Banner", result.get(0).getName())
        );

        verify(professorRepository, times(1)).findByDepartment("Physics");
    }

    @Test
    @DisplayName("Caso error - findByDepartment retorna lista vacía para departamento inexistente")
    void testFindByDepartment_DepartamentoInexistente() {
        when(professorRepository.findByDepartment("Mathematics")).thenReturn(Collections.emptyList());

        List<Professor> result = professorRepository.findByDepartment("Mathematics");

        assertTrue(result.isEmpty());
        verify(professorRepository, times(1)).findByDepartment("Mathematics");
    }

    @Test
    @DisplayName("Caso exitoso - findByIsTenured retorna profesores titulares")
    void testFindByIsTenured_Exitoso() {
        List<Professor> tenuredProfessors = Arrays.asList(professor1, professor2);
        when(professorRepository.findByIsTenured(true)).thenReturn(tenuredProfessors);

        List<Professor> result = professorRepository.findByIsTenured(true);

        assertAll("Verificar búsqueda por titularidad",
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size()),
                () -> assertTrue(result.stream().allMatch(Professor::getIsTenured))
        );

        verify(professorRepository, times(1)).findByIsTenured(true);
    }

    @Test
    @DisplayName("Caso exitoso - findByAreasOfExpertiseContaining retorna profesores por área")
    void testFindByAreasOfExpertiseContaining_Exitoso() {
        List<Professor> physicsProfessors = Arrays.asList(professor1);
        when(professorRepository.findByAreasOfExpertiseContaining("Physics")).thenReturn(physicsProfessors);

        List<Professor> result = professorRepository.findByAreasOfExpertiseContaining("Physics");

        assertAll("Verificar búsqueda por área de expertise",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertTrue(result.get(0).getAreasOfExpertise().contains("Nuclear Physics")),
                () -> assertEquals("Dr. Bruce Banner", result.get(0).getName())
        );

        verify(professorRepository, times(1)).findByAreasOfExpertiseContaining("Physics");
    }

    @Test
    @DisplayName("Caso exitoso - findByDepartmentAndIsTenured retorna profesores combinados")
    void testFindByDepartmentAndIsTenured_Exitoso() {
        List<Professor> professors = Arrays.asList(professor1);
        when(professorRepository.findByDepartmentAndIsTenured("Physics", true)).thenReturn(professors);

        List<Professor> result = professorRepository.findByDepartmentAndIsTenured("Physics", true);

        assertAll("Verificar búsqueda combinada por departamento y titularidad",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("Physics", result.get(0).getDepartment()),
                () -> assertTrue(result.get(0).getIsTenured())
        );

        verify(professorRepository, times(1)).findByDepartmentAndIsTenured("Physics", true);
    }

    @Test
    @DisplayName("Caso exitoso - findByDepartmentAndIsTenuredTrue retorna profesores titulares del departamento")
    void testFindByDepartmentAndIsTenuredTrue_Exitoso() {
        List<Professor> professors = Arrays.asList(professor1);
        when(professorRepository.findByDepartmentAndIsTenuredTrue("Physics")).thenReturn(professors);

        List<Professor> result = professorRepository.findByDepartmentAndIsTenuredTrue("Physics");

        assertAll("Verificar búsqueda por departamento y titularidad true",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("Physics", result.get(0).getDepartment()),
                () -> assertTrue(result.get(0).getIsTenured())
        );

        verify(professorRepository, times(1)).findByDepartmentAndIsTenuredTrue("Physics");
    }

    @Test
    @DisplayName("Caso exitoso - findByDepartmentAndIsTenuredFalse retorna profesores no titulares del departamento")
    void testFindByDepartmentAndIsTenuredFalse_Exitoso() {
        List<Professor> professors = Arrays.asList(professor3);
        when(professorRepository.findByDepartmentAndIsTenuredFalse("Computer Science")).thenReturn(professors);

        List<Professor> result = professorRepository.findByDepartmentAndIsTenuredFalse("Computer Science");

        assertAll("Verificar búsqueda por departamento y titularidad false",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("Computer Science", result.get(0).getDepartment()),
                () -> assertFalse(result.get(0).getIsTenured())
        );

        verify(professorRepository, times(1)).findByDepartmentAndIsTenuredFalse("Computer Science");
    }

    @Test
    @DisplayName("Caso exitoso - findByAreasOfExpertiseIn retorna profesores por múltiples áreas")
    void testFindByAreasOfExpertiseIn_Exitoso() {
        List<String> areas = Arrays.asList("Physics", "Genetics");
        List<Professor> professors = Arrays.asList(professor1, professor2);
        when(professorRepository.findByAreasOfExpertiseIn(areas)).thenReturn(professors);

        List<Professor> result = professorRepository.findByAreasOfExpertiseIn(areas);

        assertAll("Verificar búsqueda por múltiples áreas de expertise",
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size()),
                () -> assertTrue(result.stream().anyMatch(p -> p.getName().equals("Dr. Bruce Banner"))),
                () -> assertTrue(result.stream().anyMatch(p -> p.getName().equals("Dr. Charles Xavier")))
        );

        verify(professorRepository, times(1)).findByAreasOfExpertiseIn(areas);
    }

    @Test
    @DisplayName("Caso exitoso - findByName retorna profesor por nombre")
    void testFindByName_Exitoso() {
        List<Professor> professors = Arrays.asList(professor1);
        when(professorRepository.findByName("Dr. Bruce Banner")).thenReturn(professors);

        List<Professor> result = professorRepository.findByName("Dr. Bruce Banner");

        assertAll("Verificar búsqueda por nombre",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("Dr. Bruce Banner", result.get(0).getName())
        );

        verify(professorRepository, times(1)).findByName("Dr. Bruce Banner");
    }

    @Test
    @DisplayName("Caso exitoso - findByEmail retorna profesor por email")
    void testFindByEmail_Exitoso() {
        List<Professor> professors = Arrays.asList(professor1);
        when(professorRepository.findByEmail("bruce.banner@titans.edu")).thenReturn(professors);

        List<Professor> result = professorRepository.findByEmail("bruce.banner@titans.edu");

        assertAll("Verificar búsqueda por email",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("bruce.banner@titans.edu", result.get(0).getEmail())
        );

        verify(professorRepository, times(1)).findByEmail("bruce.banner@titans.edu");
    }

    @Test
    @DisplayName("Caso exitoso - findByNameContainingIgnoreCase retorna profesores con patrón en nombre")
    void testFindByNameContainingIgnoreCase_Exitoso() {
        List<Professor> professors = Arrays.asList(professor1);
        when(professorRepository.findByNameContainingIgnoreCase("bruce")).thenReturn(professors);

        List<Professor> result = professorRepository.findByNameContainingIgnoreCase("bruce");

        assertAll("Verificar búsqueda por patrón en nombre",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertTrue(result.get(0).getName().toLowerCase().contains("bruce"))
        );

        verify(professorRepository, times(1)).findByNameContainingIgnoreCase("bruce");
    }

    @Test
    @DisplayName("Caso exitoso - findByActive retorna profesores activos")
    void testFindByActive_Exitoso() {
        List<Professor> activeProfessors = Arrays.asList(professor1, professor3);
        when(professorRepository.findByActive(true)).thenReturn(activeProfessors);

        List<Professor> result = professorRepository.findByActive(true);

        assertAll("Verificar búsqueda por estado activo",
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size()),
                () -> assertTrue(result.stream().allMatch(Professor::isActive))
        );

        verify(professorRepository, times(1)).findByActive(true);
    }

    @Test
    @DisplayName("Caso exitoso - findByDepartmentAndActive retorna profesores combinados")
    void testFindByDepartmentAndActive_Exitoso() {
        List<Professor> professors = Arrays.asList(professor1);
        when(professorRepository.findByDepartmentAndActive("Physics", true)).thenReturn(professors);

        List<Professor> result = professorRepository.findByDepartmentAndActive("Physics", true);

        assertAll("Verificar búsqueda combinada por departamento y estado",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("Physics", result.get(0).getDepartment()),
                () -> assertTrue(result.get(0).isActive())
        );

        verify(professorRepository, times(1)).findByDepartmentAndActive("Physics", true);
    }

    @Test
    @DisplayName("Caso exitoso - findByIsTenuredAndActive retorna profesores por titularidad y estado")
    void testFindByIsTenuredAndActive_Exitoso() {
        List<Professor> professors = Arrays.asList(professor1, professor2);
        when(professorRepository.findByIsTenuredAndActive(true, true)).thenReturn(professors);

        List<Professor> result = professorRepository.findByIsTenuredAndActive(true, true);

        assertAll("Verificar búsqueda por titularidad y estado",
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size()),
                () -> assertTrue(result.stream().allMatch(Professor::getIsTenured)),
                () -> assertTrue(result.stream().allMatch(Professor::isActive))
        );

        verify(professorRepository, times(1)).findByIsTenuredAndActive(true, true);
    }

    @Test
    @DisplayName("Caso exitoso - findByNameAndDepartment retorna profesor por nombre y departamento")
    void testFindByNameAndDepartment_Exitoso() {
        List<Professor> professors = Arrays.asList(professor1);
        when(professorRepository.findByNameAndDepartment("Dr. Bruce Banner", "Physics")).thenReturn(professors);

        List<Professor> result = professorRepository.findByNameAndDepartment("Dr. Bruce Banner", "Physics");

        assertAll("Verificar búsqueda por nombre y departamento",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("Dr. Bruce Banner", result.get(0).getName()),
                () -> assertEquals("Physics", result.get(0).getDepartment())
        );

        verify(professorRepository, times(1)).findByNameAndDepartment("Dr. Bruce Banner", "Physics");
    }

    @Test
    @DisplayName("Caso exitoso - findByNameAndIsTenured retorna profesor por nombre y titularidad")
    void testFindByNameAndIsTenured_Exitoso() {
        List<Professor> professors = Arrays.asList(professor1);
        when(professorRepository.findByNameAndIsTenured("Dr. Bruce Banner", true)).thenReturn(professors);

        List<Professor> result = professorRepository.findByNameAndIsTenured("Dr. Bruce Banner", true);

        assertAll("Verificar búsqueda por nombre y titularidad",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("Dr. Bruce Banner", result.get(0).getName()),
                () -> assertTrue(result.get(0).getIsTenured())
        );

        verify(professorRepository, times(1)).findByNameAndIsTenured("Dr. Bruce Banner", true);
    }

    @Test
    @DisplayName("Caso exitoso - findByOrderByNameAsc retorna profesores ordenados")
    void testFindByOrderByNameAsc_Exitoso() {
        List<Professor> professors = Arrays.asList(professor1, professor2, professor3);
        when(professorRepository.findByOrderByNameAsc()).thenReturn(professors);

        List<Professor> result = professorRepository.findByOrderByNameAsc();

        assertAll("Verificar ordenamiento por nombre ascendente",
                () -> assertNotNull(result),
                () -> assertEquals(3, result.size()),
                () -> assertEquals("Dr. Bruce Banner", result.get(0).getName()),
                () -> assertEquals("Dr. Charles Xavier", result.get(1).getName()),
                () -> assertEquals("Dr. Tony Stark", result.get(2).getName())
        );

        verify(professorRepository, times(1)).findByOrderByNameAsc();
    }

    @Test
    @DisplayName("Caso exitoso - countByDepartment retorna conteo correcto")
    void testCountByDepartment_Exitoso() {
        when(professorRepository.countByDepartment("Physics")).thenReturn(1L);

        long result = professorRepository.countByDepartment("Physics");

        assertEquals(1L, result);
        verify(professorRepository, times(1)).countByDepartment("Physics");
    }

    @Test
    @DisplayName("Caso exitoso - countByIsTenured retorna conteo por titularidad")
    void testCountByIsTenured_Exitoso() {
        when(professorRepository.countByIsTenured(true)).thenReturn(2L);
        when(professorRepository.countByIsTenured(false)).thenReturn(1L);

        long tenuredCount = professorRepository.countByIsTenured(true);
        long nonTenuredCount = professorRepository.countByIsTenured(false);

        assertAll("Verificar conteo por titularidad",
                () -> assertEquals(2L, tenuredCount),
                () -> assertEquals(1L, nonTenuredCount)
        );

        verify(professorRepository, times(1)).countByIsTenured(true);
        verify(professorRepository, times(1)).countByIsTenured(false);
    }

    @Test
    @DisplayName("Caso exitoso - countByDepartmentAndIsTenured retorna conteo combinado")
    void testCountByDepartmentAndIsTenured_Exitoso() {
        when(professorRepository.countByDepartmentAndIsTenured("Physics", true)).thenReturn(1L);

        long result = professorRepository.countByDepartmentAndIsTenured("Physics", true);

        assertEquals(1L, result);
        verify(professorRepository, times(1)).countByDepartmentAndIsTenured("Physics", true);
    }

    @Test
    @DisplayName("Caso exitoso - countByAreasOfExpertiseContaining retorna conteo por área")
    void testCountByAreasOfExpertiseContaining_Exitoso() {
        when(professorRepository.countByAreasOfExpertiseContaining("Physics")).thenReturn(1L);

        long result = professorRepository.countByAreasOfExpertiseContaining("Physics");

        assertEquals(1L, result);
        verify(professorRepository, times(1)).countByAreasOfExpertiseContaining("Physics");
    }

    @Test
    @DisplayName("Caso exitoso - findByDepartmentRegex retorna profesores por patrón regex")
    void testFindByDepartmentRegex_Exitoso() {
        List<Professor> professors = Arrays.asList(professor1);
        when(professorRepository.findByDepartmentRegex("Phy.*")).thenReturn(professors);

        List<Professor> result = professorRepository.findByDepartmentRegex("Phy.*");

        assertAll("Verificar búsqueda por regex de departamento",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("Physics", result.get(0).getDepartment())
        );

        verify(professorRepository, times(1)).findByDepartmentRegex("Phy.*");
    }

    @Test
    @DisplayName("Caso exitoso - existsByDepartment retorna true cuando existe departamento")
    void testExistsByDepartment_Exitoso() {
        when(professorRepository.existsByDepartment("Physics")).thenReturn(true);

        boolean result = professorRepository.existsByDepartment("Physics");

        assertTrue(result);
        verify(professorRepository, times(1)).existsByDepartment("Physics");
    }

    @Test
    @DisplayName("Caso error - existsByDepartment retorna false cuando no existe departamento")
    void testExistsByDepartment_NoExiste() {
        when(professorRepository.existsByDepartment("Mathematics")).thenReturn(false);

        boolean result = professorRepository.existsByDepartment("Mathematics");

        assertFalse(result);
        verify(professorRepository, times(1)).existsByDepartment("Mathematics");
    }

    @Test
    @DisplayName("Caso exitoso - existsByEmail retorna true cuando existe email")
    void testExistsByEmail_Exitoso() {
        when(professorRepository.existsByEmail("bruce.banner@titans.edu")).thenReturn(true);

        boolean result = professorRepository.existsByEmail("bruce.banner@titans.edu");

        assertTrue(result);
        verify(professorRepository, times(1)).existsByEmail("bruce.banner@titans.edu");
    }

    @Test
    @DisplayName("Caso error - existsByEmail retorna false cuando no existe email")
    void testExistsByEmail_NoExiste() {
        when(professorRepository.existsByEmail("inexistente@titans.edu")).thenReturn(false);

        boolean result = professorRepository.existsByEmail("inexistente@titans.edu");

        assertFalse(result);
        verify(professorRepository, times(1)).existsByEmail("inexistente@titans.edu");
    }

    @Test
    @DisplayName("Caso exitoso - existsByDepartmentAndIsTenured retorna true cuando existe combinación")
    void testExistsByDepartmentAndIsTenured_Exitoso() {
        when(professorRepository.existsByDepartmentAndIsTenured("Physics", true)).thenReturn(true);

        boolean result = professorRepository.existsByDepartmentAndIsTenured("Physics", true);

        assertTrue(result);
        verify(professorRepository, times(1)).existsByDepartmentAndIsTenured("Physics", true);
    }

    @Test
    @DisplayName("Caso exitoso - findByDepartmentIn retorna profesores de múltiples departamentos")
    void testFindByDepartmentIn_Exitoso() {
        List<String> departments = Arrays.asList("Physics", "Computer Science");
        List<Professor> professors = Arrays.asList(professor1, professor3);
        when(professorRepository.findByDepartmentIn(departments)).thenReturn(professors);

        List<Professor> result = professorRepository.findByDepartmentIn(departments);

        assertAll("Verificar búsqueda por múltiples departamentos",
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size()),
                () -> assertTrue(result.stream().anyMatch(p -> p.getDepartment().equals("Physics"))),
                () -> assertTrue(result.stream().anyMatch(p -> p.getDepartment().equals("Computer Science")))
        );

        verify(professorRepository, times(1)).findByDepartmentIn(departments);
    }

    @Test
    @DisplayName("Caso borde - findByDepartmentIn con lista vacía")
    void testFindByDepartmentIn_ListaVacia() {
        when(professorRepository.findByDepartmentIn(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<Professor> result = professorRepository.findByDepartmentIn(Collections.emptyList());

        assertTrue(result.isEmpty());
        verify(professorRepository, times(1)).findByDepartmentIn(Collections.emptyList());
    }

    @Test
    @DisplayName("Caso exitoso - deleteById elimina profesor existente")
    void testDeleteById_Exitoso() {
        doNothing().when(professorRepository).deleteById("1");

        assertDoesNotThrow(() -> professorRepository.deleteById("1"));

        verify(professorRepository, times(1)).deleteById("1");
    }
}
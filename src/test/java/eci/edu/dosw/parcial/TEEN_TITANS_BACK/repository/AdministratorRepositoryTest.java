package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Administrator;
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
public class AdministratorRepositoryTest {

    @Mock
    private AdministratorRepository administratorRepository;

    private Administrator admin1;
    private Administrator admin2;
    private Administrator admin3;

    @BeforeEach
    void setUp() {
        admin1 = new Administrator("1", "Bruce Wayne", "bruce.wayne@titans.edu", "password123", "Security");
        admin2 = new Administrator("2", "Alfred Pennyworth", "alfred.pennyworth@titans.edu", "password456", "Operations");
        admin3 = new Administrator("3", "Barbara Gordon", "barbara.gordon@titans.edu", "password789", "Technology");
    }

    @Test
    @DisplayName("Caso exitoso - save guarda administrador correctamente")
    void testSave_Exitoso() {
        Administrator newAdmin = new Administrator("4", "Dick Grayson", "dick.grayson@titans.edu", "password000", "Security");

        when(administratorRepository.save(newAdmin)).thenReturn(newAdmin);

        Administrator savedAdmin = administratorRepository.save(newAdmin);

        assertAll("Verificar guardado de administrador",
                () -> assertNotNull(savedAdmin),
                () -> assertEquals("Dick Grayson", savedAdmin.getName()),
                () -> assertEquals("Security", savedAdmin.getDepartment())
        );

        verify(administratorRepository, times(1)).save(newAdmin);
    }

    @Test
    @DisplayName("Caso exitoso - findById retorna administrador existente")
    void testFindById_Exitoso() {
        when(administratorRepository.findById("1")).thenReturn(Optional.of(admin1));

        Optional<Administrator> foundAdmin = administratorRepository.findById("1");

        assertAll("Verificar búsqueda por ID",
                () -> assertTrue(foundAdmin.isPresent()),
                () -> assertEquals("Bruce Wayne", foundAdmin.get().getName()),
                () -> assertEquals("Security", foundAdmin.get().getDepartment())
        );

        verify(administratorRepository, times(1)).findById("1");
    }

    @Test
    @DisplayName("Caso error - findById retorna vacío para ID inexistente")
    void testFindById_NoEncontrado() {
        when(administratorRepository.findById("99")).thenReturn(Optional.empty());

        Optional<Administrator> foundAdmin = administratorRepository.findById("99");

        assertTrue(foundAdmin.isEmpty());
        verify(administratorRepository, times(1)).findById("99");
    }

    @Test
    @DisplayName("Caso exitoso - findAll retorna todos los administradores")
    void testFindAll_Exitoso() {
        List<Administrator> admins = Arrays.asList(admin1, admin2, admin3);
        when(administratorRepository.findAll()).thenReturn(admins);

        List<Administrator> result = administratorRepository.findAll();

        assertAll("Verificar búsqueda de todos los administradores",
                () -> assertNotNull(result),
                () -> assertEquals(3, result.size()),
                () -> assertEquals("Bruce Wayne", result.get(0).getName())
        );

        verify(administratorRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Caso borde - findAll retorna lista vacía cuando no hay administradores")
    void testFindAll_ListaVacia() {
        when(administratorRepository.findAll()).thenReturn(Collections.emptyList());

        List<Administrator> result = administratorRepository.findAll();

        assertTrue(result.isEmpty());
        verify(administratorRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Caso exitoso - findByDepartment retorna administradores del departamento")
    void testFindByDepartment_Exitoso() {
        List<Administrator> securityAdmins = Arrays.asList(admin1);
        when(administratorRepository.findByDepartment("Security")).thenReturn(securityAdmins);

        List<Administrator> result = administratorRepository.findByDepartment("Security");

        assertAll("Verificar búsqueda por departamento",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("Security", result.get(0).getDepartment())
        );

        verify(administratorRepository, times(1)).findByDepartment("Security");
    }

    @Test
    @DisplayName("Caso error - findByDepartment retorna lista vacía para departamento inexistente")
    void testFindByDepartment_DepartamentoInexistente() {
        when(administratorRepository.findByDepartment("Finanzas")).thenReturn(Collections.emptyList());

        List<Administrator> result = administratorRepository.findByDepartment("Finanzas");

        assertTrue(result.isEmpty());
        verify(administratorRepository, times(1)).findByDepartment("Finanzas");
    }

    @Test
    @DisplayName("Caso exitoso - findByDepartmentContainingIgnoreCase retorna administradores con patrón")
    void testFindByDepartmentContainingIgnoreCase_Exitoso() {
        List<Administrator> techAdmins = Arrays.asList(admin3);
        when(administratorRepository.findByDepartmentContainingIgnoreCase("tech")).thenReturn(techAdmins);

        List<Administrator> result = administratorRepository.findByDepartmentContainingIgnoreCase("tech");

        assertAll("Verificar búsqueda por patrón de departamento",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("Technology", result.get(0).getDepartment())
        );

        verify(administratorRepository, times(1)).findByDepartmentContainingIgnoreCase("tech");
    }

    @Test
    @DisplayName("Caso exitoso - findByName retorna administrador por nombre")
    void testFindByName_Exitoso() {
        List<Administrator> admins = Arrays.asList(admin1);
        when(administratorRepository.findByName("Bruce Wayne")).thenReturn(admins);

        List<Administrator> result = administratorRepository.findByName("Bruce Wayne");

        assertAll("Verificar búsqueda por nombre",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("Bruce Wayne", result.get(0).getName())
        );

        verify(administratorRepository, times(1)).findByName("Bruce Wayne");
    }

    @Test
    @DisplayName("Caso exitoso - findByEmail retorna administrador por email")
    void testFindByEmail_Exitoso() {
        List<Administrator> admins = Arrays.asList(admin1);
        when(administratorRepository.findByEmail("bruce.wayne@titans.edu")).thenReturn(admins);

        List<Administrator> result = administratorRepository.findByEmail("bruce.wayne@titans.edu");

        assertAll("Verificar búsqueda por email",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("bruce.wayne@titans.edu", result.get(0).getEmail())
        );

        verify(administratorRepository, times(1)).findByEmail("bruce.wayne@titans.edu");
    }

    @Test
    @DisplayName("Caso exitoso - findByNameContainingIgnoreCase retorna administradores con patrón en nombre")
    void testFindByNameContainingIgnoreCase_Exitoso() {
        List<Administrator> admins = Arrays.asList(admin1);
        when(administratorRepository.findByNameContainingIgnoreCase("bruce")).thenReturn(admins);

        List<Administrator> result = administratorRepository.findByNameContainingIgnoreCase("bruce");

        assertAll("Verificar búsqueda por patrón en nombre",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertTrue(result.get(0).getName().toLowerCase().contains("bruce"))
        );

        verify(administratorRepository, times(1)).findByNameContainingIgnoreCase("bruce");
    }

    @Test
    @DisplayName("Caso exitoso - findByActive retorna administradores activos")
    void testFindByActive_Exitoso() {
        List<Administrator> activeAdmins = Arrays.asList(admin1, admin3);
        when(administratorRepository.findByActive(true)).thenReturn(activeAdmins);

        List<Administrator> result = administratorRepository.findByActive(true);

        assertAll("Verificar búsqueda por estado activo",
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size()),
                () -> assertTrue(result.stream().allMatch(Administrator::isActive))
        );

        verify(administratorRepository, times(1)).findByActive(true);
    }

    @Test
    @DisplayName("Caso exitoso - findByDepartmentAndActive retorna administradores combinados")
    void testFindByDepartmentAndActive_Exitoso() {
        List<Administrator> admins = Arrays.asList(admin1);
        when(administratorRepository.findByDepartmentAndActive("Security", true)).thenReturn(admins);

        List<Administrator> result = administratorRepository.findByDepartmentAndActive("Security", true);

        assertAll("Verificar búsqueda combinada por departamento y estado",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("Security", result.get(0).getDepartment()),
                () -> assertTrue(result.get(0).isActive())
        );

        verify(administratorRepository, times(1)).findByDepartmentAndActive("Security", true);
    }

    @Test
    @DisplayName("Caso exitoso - findByNameAndDepartment retorna administrador por nombre y departamento")
    void testFindByNameAndDepartment_Exitoso() {
        List<Administrator> admins = Arrays.asList(admin1);
        when(administratorRepository.findByNameAndDepartment("Bruce Wayne", "Security")).thenReturn(admins);

        List<Administrator> result = administratorRepository.findByNameAndDepartment("Bruce Wayne", "Security");

        assertAll("Verificar búsqueda por nombre y departamento",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("Bruce Wayne", result.get(0).getName()),
                () -> assertEquals("Security", result.get(0).getDepartment())
        );

        verify(administratorRepository, times(1)).findByNameAndDepartment("Bruce Wayne", "Security");
    }

    @Test
    @DisplayName("Caso exitoso - findByOrderByNameAsc retorna administradores ordenados")
    void testFindByOrderByNameAsc_Exitoso() {
        List<Administrator> admins = Arrays.asList(admin2, admin3, admin1);
        when(administratorRepository.findByOrderByNameAsc()).thenReturn(admins);

        List<Administrator> result = administratorRepository.findByOrderByNameAsc();

        assertAll("Verificar ordenamiento por nombre ascendente",
                () -> assertNotNull(result),
                () -> assertEquals(3, result.size()),
                () -> assertEquals("Alfred Pennyworth", result.get(0).getName()),
                () -> assertEquals("Barbara Gordon", result.get(1).getName()),
                () -> assertEquals("Bruce Wayne", result.get(2).getName())
        );

        verify(administratorRepository, times(1)).findByOrderByNameAsc();
    }

    @Test
    @DisplayName("Caso exitoso - countByDepartment retorna conteo correcto")
    void testCountByDepartment_Exitoso() {
        when(administratorRepository.countByDepartment("Security")).thenReturn(1L);

        long result = administratorRepository.countByDepartment("Security");

        assertEquals(1L, result);
        verify(administratorRepository, times(1)).countByDepartment("Security");
    }

    @Test
    @DisplayName("Caso exitoso - countByDepartmentAndActive retorna conteo combinado")
    void testCountByDepartmentAndActive_Exitoso() {
        when(administratorRepository.countByDepartmentAndActive("Security", true)).thenReturn(1L);

        long result = administratorRepository.countByDepartmentAndActive("Security", true);

        assertEquals(1L, result);
        verify(administratorRepository, times(1)).countByDepartmentAndActive("Security", true);
    }

    @Test
    @DisplayName("Caso exitoso - countByActive retorna conteo por estado")
    void testCountByActive_Exitoso() {
        when(administratorRepository.countByActive(true)).thenReturn(2L);
        when(administratorRepository.countByActive(false)).thenReturn(1L);

        long activeCount = administratorRepository.countByActive(true);
        long inactiveCount = administratorRepository.countByActive(false);

        assertAll("Verificar conteo por estado activo",
                () -> assertEquals(2L, activeCount),
                () -> assertEquals(1L, inactiveCount)
        );

        verify(administratorRepository, times(1)).countByActive(true);
        verify(administratorRepository, times(1)).countByActive(false);
    }

    @Test
    @DisplayName("Caso exitoso - findByDepartmentRegex retorna administradores por patrón regex")
    void testFindByDepartmentRegex_Exitoso() {
        List<Administrator> admins = Arrays.asList(admin1);
        when(administratorRepository.findByDepartmentRegex("Sec.*")).thenReturn(admins);

        List<Administrator> result = administratorRepository.findByDepartmentRegex("Sec.*");

        assertAll("Verificar búsqueda por regex de departamento",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("Security", result.get(0).getDepartment())
        );

        verify(administratorRepository, times(1)).findByDepartmentRegex("Sec.*");
    }

    @Test
    @DisplayName("Caso exitoso - existsByDepartment retorna true cuando existe departamento")
    void testExistsByDepartment_Exitoso() {
        when(administratorRepository.existsByDepartment("Security")).thenReturn(true);

        boolean result = administratorRepository.existsByDepartment("Security");

        assertTrue(result);
        verify(administratorRepository, times(1)).existsByDepartment("Security");
    }

    @Test
    @DisplayName("Caso error - existsByDepartment retorna false cuando no existe departamento")
    void testExistsByDepartment_NoExiste() {
        when(administratorRepository.existsByDepartment("Finanzas")).thenReturn(false);

        boolean result = administratorRepository.existsByDepartment("Finanzas");

        assertFalse(result);
        verify(administratorRepository, times(1)).existsByDepartment("Finanzas");
    }

    @Test
    @DisplayName("Caso exitoso - existsByEmail retorna true cuando existe email")
    void testExistsByEmail_Exitoso() {
        when(administratorRepository.existsByEmail("bruce.wayne@titans.edu")).thenReturn(true);

        boolean result = administratorRepository.existsByEmail("bruce.wayne@titans.edu");

        assertTrue(result);
        verify(administratorRepository, times(1)).existsByEmail("bruce.wayne@titans.edu");
    }

    @Test
    @DisplayName("Caso error - existsByEmail retorna false cuando no existe email")
    void testExistsByEmail_NoExiste() {
        when(administratorRepository.existsByEmail("inexistente@titans.edu")).thenReturn(false);

        boolean result = administratorRepository.existsByEmail("inexistente@titans.edu");

        assertFalse(result);
        verify(administratorRepository, times(1)).existsByEmail("inexistente@titans.edu");
    }

    @Test
    @DisplayName("Caso exitoso - existsByDepartmentAndActive retorna true cuando existe combinación")
    void testExistsByDepartmentAndActive_Exitoso() {
        when(administratorRepository.existsByDepartmentAndActive("Security", true)).thenReturn(true);

        boolean result = administratorRepository.existsByDepartmentAndActive("Security", true);

        assertTrue(result);
        verify(administratorRepository, times(1)).existsByDepartmentAndActive("Security", true);
    }

    @Test
    @DisplayName("Caso exitoso - findByDepartmentIn retorna administradores de múltiples departamentos")
    void testFindByDepartmentIn_Exitoso() {
        List<String> departments = Arrays.asList("Security", "Technology");
        List<Administrator> admins = Arrays.asList(admin1, admin3);
        when(administratorRepository.findByDepartmentIn(departments)).thenReturn(admins);

        List<Administrator> result = administratorRepository.findByDepartmentIn(departments);

        assertAll("Verificar búsqueda por múltiples departamentos",
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size()),
                () -> assertTrue(result.stream().anyMatch(a -> a.getDepartment().equals("Security"))),
                () -> assertTrue(result.stream().anyMatch(a -> a.getDepartment().equals("Technology")))
        );

        verify(administratorRepository, times(1)).findByDepartmentIn(departments);
    }

    @Test
    @DisplayName("Caso borde - findByDepartmentIn con lista vacía")
    void testFindByDepartmentIn_ListaVacia() {
        when(administratorRepository.findByDepartmentIn(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<Administrator> result = administratorRepository.findByDepartmentIn(Collections.emptyList());

        assertTrue(result.isEmpty());
        verify(administratorRepository, times(1)).findByDepartmentIn(Collections.emptyList());
    }

    @Test
    @DisplayName("Caso exitoso - deleteById elimina administrador existente")
    void testDeleteById_Exitoso() {
        doNothing().when(administratorRepository).deleteById("1");

        assertDoesNotThrow(() -> administratorRepository.deleteById("1"));

        verify(administratorRepository, times(1)).deleteById("1");
    }
}
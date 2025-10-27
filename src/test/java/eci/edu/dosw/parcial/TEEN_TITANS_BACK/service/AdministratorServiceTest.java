package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Administrator;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.AdministratorRepository;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdministratorServiceTest {

    @Mock
    private AdministratorRepository adminRepository;

    @InjectMocks
    private AdministratorService administratorService;

    private Administrator admin1;
    private Administrator admin2;

    @BeforeEach
    void setUp() {
        admin1 = new Administrator();
        admin1.setId("1");
        admin1.setName("Admin One");
        admin1.setEmail("admin1@titans.edu");
        admin1.setDepartment("Security");
        admin1.setActive(true);

        admin2 = new Administrator();
        admin2.setId("2");
        admin2.setName("Admin Two");
        admin2.setEmail("admin2@titans.edu");
        admin2.setDepartment("Technology");
        admin2.setActive(true);
    }

    @Test
    @DisplayName("Caso exitoso - createAdministrator crea administrador correctamente")
    void testCreateAdministrator_Exitoso() {
        when(adminRepository.existsByEmail("admin1@titans.edu")).thenReturn(false);
        when(adminRepository.save(any(Administrator.class))).thenReturn(admin1);

        Administrator result = administratorService.createAdministrator(admin1);

        assertAll("Verificar creación exitosa",
                () -> assertNotNull(result),
                () -> assertEquals("Admin One", result.getName()),
                () -> assertEquals(UserRole.ADMINISTRATOR, result.getRole())
        );

        verify(adminRepository, times(1)).existsByEmail("admin1@titans.edu");
        verify(adminRepository, times(1)).save(any(Administrator.class));
    }

    @Test
    @DisplayName("Caso error - createAdministrator lanza excepción cuando email existe")
    void testCreateAdministrator_EmailExistente() {
        when(adminRepository.existsByEmail("admin1@titans.edu")).thenReturn(true);

        AppException exception = assertThrows(AppException.class, () -> {
            administratorService.createAdministrator(admin1);
        });

        assertEquals("El email ya está registrado: admin1@titans.edu", exception.getMessage());
        verify(adminRepository, times(1)).existsByEmail("admin1@titans.edu");
        verify(adminRepository, never()).save(any(Administrator.class));
    }

    @Test
    @DisplayName("Caso exitoso - getAdministratorById retorna administrador existente")
    void testGetAdministratorById_Exitoso() {
        when(adminRepository.findById("1")).thenReturn(Optional.of(admin1));

        Administrator result = administratorService.getAdministratorById("1");

        assertAll("Verificar obtención por ID exitosa",
                () -> assertNotNull(result),
                () -> assertEquals("1", result.getId()),
                () -> assertEquals("Admin One", result.getName())
        );

        verify(adminRepository, times(1)).findById("1");
    }

    @Test
    @DisplayName("Caso error - getAdministratorById lanza excepción cuando no existe")
    void testGetAdministratorById_NoEncontrado() {
        when(adminRepository.findById("99")).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            administratorService.getAdministratorById("99");
        });

        assertEquals("Administrador no encontrado con ID: 99", exception.getMessage());
        verify(adminRepository, times(1)).findById("99");
    }

    @Test
    @DisplayName("Caso exitoso - getAllAdministrators retorna lista de administradores")
    void testGetAllAdministrators_Exitoso() {
        List<Administrator> admins = Arrays.asList(admin1, admin2);
        when(adminRepository.findAll()).thenReturn(admins);

        List<Administrator> result = administratorService.getAllAdministrators();

        assertAll("Verificar obtención de todos los administradores",
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size()),
                () -> assertEquals("Admin One", result.get(0).getName())
        );

        verify(adminRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Caso borde - getAllAdministrators retorna lista vacía")
    void testGetAllAdministrators_ListaVacia() {
        when(adminRepository.findAll()).thenReturn(Collections.emptyList());

        List<Administrator> result = administratorService.getAllAdministrators();

        assertTrue(result.isEmpty());
        verify(adminRepository, times(1)).findAll();
    }


    @Test
    @DisplayName("Caso error - updateAdministrator lanza excepción cuando no existe")
    void testUpdateAdministrator_NoEncontrado() {
        when(adminRepository.findById("99")).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            administratorService.updateAdministrator("99", admin1);
        });

        assertEquals("Administrador no encontrado con ID: 99", exception.getMessage());
        verify(adminRepository, times(1)).findById("99");
        verify(adminRepository, never()).save(any(Administrator.class));
    }

    @Test
    @DisplayName("Caso error - updateAdministrator lanza excepción cuando email existe en otro admin")
    void testUpdateAdministrator_EmailEnUso() {
        Administrator existingAdmin = new Administrator();
        existingAdmin.setId("2");
        existingAdmin.setEmail("otro@titans.edu");

        when(adminRepository.findById("1")).thenReturn(Optional.of(admin1));
        when(adminRepository.findByEmail("nuevo@titans.edu")).thenReturn(Arrays.asList(existingAdmin));

        Administrator updatedAdmin = new Administrator();
        updatedAdmin.setEmail("nuevo@titans.edu");

        AppException exception = assertThrows(AppException.class, () -> {
            administratorService.updateAdministrator("1", updatedAdmin);
        });

        assertEquals("El email ya está en uso por otro administrador: nuevo@titans.edu", exception.getMessage());
        verify(adminRepository, times(1)).findById("1");
        verify(adminRepository, times(1)).findByEmail("nuevo@titans.edu");
        verify(adminRepository, never()).save(any(Administrator.class));
    }

    @Test
    @DisplayName("Caso exitoso - deleteAdministrator elimina administrador correctamente")
    void testDeleteAdministrator_Exitoso() {
        when(adminRepository.existsById("1")).thenReturn(true);
        doNothing().when(adminRepository).deleteById("1");

        assertDoesNotThrow(() -> administratorService.deleteAdministrator("1"));

        verify(adminRepository, times(1)).existsById("1");
        verify(adminRepository, times(1)).deleteById("1");
    }

    @Test
    @DisplayName("Caso error - deleteAdministrator lanza excepción cuando no existe")
    void testDeleteAdministrator_NoEncontrado() {
        when(adminRepository.existsById("99")).thenReturn(false);

        AppException exception = assertThrows(AppException.class, () -> {
            administratorService.deleteAdministrator("99");
        });

        assertEquals("Administrador no encontrado con ID: 99", exception.getMessage());
        verify(adminRepository, times(1)).existsById("99");
        verify(adminRepository, never()).deleteById(anyString());
    }

    @Test
    @DisplayName("Caso exitoso - findByDepartment retorna administradores del departamento")
    void testFindByDepartment_Exitoso() {
        List<Administrator> admins = Arrays.asList(admin1);
        when(adminRepository.findByDepartment("Security")).thenReturn(admins);

        List<Administrator> result = administratorService.findByDepartment("Security");

        assertAll("Verificar búsqueda por departamento",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("Security", result.get(0).getDepartment())
        );

        verify(adminRepository, times(1)).findByDepartment("Security");
    }

    @Test
    @DisplayName("Caso borde - findByDepartment retorna lista vacía para departamento sin admins")
    void testFindByDepartment_DepartamentoSinAdmins() {
        when(adminRepository.findByDepartment("Finanzas")).thenReturn(Collections.emptyList());

        List<Administrator> result = administratorService.findByDepartment("Finanzas");

        assertTrue(result.isEmpty());
        verify(adminRepository, times(1)).findByDepartment("Finanzas");
    }

    @Test
    @DisplayName("Caso exitoso - findByDepartmentContaining retorna administradores")
    void testFindByDepartmentContaining_Exitoso() {
        List<Administrator> admins = Arrays.asList(admin1);
        when(adminRepository.findByDepartmentContainingIgnoreCase("Sec")).thenReturn(admins);

        List<Administrator> result = administratorService.findByDepartmentContaining("Sec");

        assertNotNull(result);
        verify(adminRepository, times(1)).findByDepartmentContainingIgnoreCase("Sec");
    }

    @Test
    @DisplayName("Caso exitoso - findByNameContaining retorna administradores")
    void testFindByNameContaining_Exitoso() {
        List<Administrator> admins = Arrays.asList(admin1);
        when(adminRepository.findByNameContainingIgnoreCase("Admin")).thenReturn(admins);

        List<Administrator> result = administratorService.findByNameContaining("Admin");

        assertNotNull(result);
        verify(adminRepository, times(1)).findByNameContainingIgnoreCase("Admin");
    }

    @Test
    @DisplayName("Caso exitoso - findActiveAdministrators retorna administradores activos")
    void testFindActiveAdministrators_Exitoso() {
        List<Administrator> admins = Arrays.asList(admin1, admin2);
        when(adminRepository.findByActive(true)).thenReturn(admins);

        List<Administrator> result = administratorService.findActiveAdministrators();

        assertAll("Verificar administradores activos",
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size())
        );

        verify(adminRepository, times(1)).findByActive(true);
    }

    @Test
    @DisplayName("Caso exitoso - findByDepartmentAndActive retorna administradores")
    void testFindByDepartmentAndActive_Exitoso() {
        List<Administrator> admins = Arrays.asList(admin1);
        when(adminRepository.findByDepartmentAndActive("Security", true)).thenReturn(admins);

        List<Administrator> result = administratorService.findByDepartmentAndActive("Security", true);

        assertNotNull(result);
        verify(adminRepository, times(1)).findByDepartmentAndActive("Security", true);
    }

    @Test
    @DisplayName("Caso exitoso - findByNameAndDepartment retorna administradores")
    void testFindByNameAndDepartment_Exitoso() {
        List<Administrator> admins = Arrays.asList(admin1);
        when(adminRepository.findByNameAndDepartment("Admin One", "Security")).thenReturn(admins);

        List<Administrator> result = administratorService.findByNameAndDepartment("Admin One", "Security");

        assertNotNull(result);
        verify(adminRepository, times(1)).findByNameAndDepartment("Admin One", "Security");
    }

    @Test
    @DisplayName("Caso exitoso - countByDepartment retorna conteo correcto")
    void testCountByDepartment_Exitoso() {
        when(adminRepository.countByDepartment("Security")).thenReturn(3L);

        long result = administratorService.countByDepartment("Security");

        assertEquals(3L, result);
        verify(adminRepository, times(1)).countByDepartment("Security");
    }

    @Test
    @DisplayName("Caso exitoso - countByDepartmentAndActive retorna conteo correcto")
    void testCountByDepartmentAndActive_Exitoso() {
        when(adminRepository.countByDepartmentAndActive("Security", true)).thenReturn(2L);

        long result = administratorService.countByDepartmentAndActive("Security", true);

        assertEquals(2L, result);
        verify(adminRepository, times(1)).countByDepartmentAndActive("Security", true);
    }

    @Test
    @DisplayName("Caso exitoso - findByDepartments retorna administradores")
    void testFindByDepartments_Exitoso() {
        List<String> departments = Arrays.asList("Security", "Technology");
        List<Administrator> admins = Arrays.asList(admin1, admin2);
        when(adminRepository.findByDepartmentIn(departments)).thenReturn(admins);

        List<Administrator> result = administratorService.findByDepartments(departments);

        assertAll("Verificar búsqueda por múltiples departamentos",
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size())
        );

        verify(adminRepository, times(1)).findByDepartmentIn(departments);
    }

    @Test
    @DisplayName("Caso exitoso - findByDepartmentPattern retorna administradores")
    void testFindByDepartmentPattern_Exitoso() {
        List<Administrator> admins = Arrays.asList(admin1);
        when(adminRepository.findByDepartmentRegex("Sec.*")).thenReturn(admins);

        List<Administrator> result = administratorService.findByDepartmentPattern("Sec.*");

        assertNotNull(result);
        verify(adminRepository, times(1)).findByDepartmentRegex("Sec.*");
    }

    @Test
    @DisplayName("Caso borde - updateAdministrator con mismo email no valida duplicado")
    void testUpdateAdministrator_MismoEmail() {
        Administrator updatedAdmin = new Administrator();
        updatedAdmin.setName("Admin Actualizado");
        updatedAdmin.setEmail("admin1@titans.edu");
        updatedAdmin.setDepartment("Security Updated");

        when(adminRepository.findById("1")).thenReturn(Optional.of(admin1));
        when(adminRepository.save(any(Administrator.class))).thenReturn(updatedAdmin);

        Administrator result = administratorService.updateAdministrator("1", updatedAdmin);

        assertNotNull(result);
        verify(adminRepository, times(1)).findById("1");
        verify(adminRepository, never()).findByEmail("admin1@titans.edu");
        verify(adminRepository, times(1)).save(any(Administrator.class));
    }

    @Test
    @DisplayName("Caso borde - findByDepartment con null")
    void testFindByDepartment_Null() {
        when(adminRepository.findByDepartment(null)).thenReturn(Collections.emptyList());

        List<Administrator> result = administratorService.findByDepartment(null);

        assertTrue(result.isEmpty());
        verify(adminRepository, times(1)).findByDepartment(null);
    }

    @Test
    @DisplayName("Caso borde - findByNameContaining con string vacío")
    void testFindByNameContaining_StringVacio() {
        when(adminRepository.findByNameContainingIgnoreCase("")).thenReturn(Arrays.asList(admin1, admin2));

        List<Administrator> result = administratorService.findByNameContaining("");

        assertEquals(2, result.size());
        verify(adminRepository, times(1)).findByNameContainingIgnoreCase("");
    }

    @Test
    @DisplayName("Caso borde - findByDepartments con lista vacía")
    void testFindByDepartments_ListaVacia() {
        when(adminRepository.findByDepartmentIn(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<Administrator> result = administratorService.findByDepartments(Collections.emptyList());

        assertTrue(result.isEmpty());
        verify(adminRepository, times(1)).findByDepartmentIn(Collections.emptyList());
    }
}
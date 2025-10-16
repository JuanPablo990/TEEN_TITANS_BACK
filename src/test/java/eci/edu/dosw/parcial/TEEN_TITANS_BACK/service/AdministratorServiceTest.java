package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Administrator;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.UserRole;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.AdministratorRepository;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
        admin1 = new Administrator("ADM001", "Carlos López", "carlos.lopez@university.edu",
                "password123", "Tecnología");
        admin2 = new Administrator("ADM002", "Ana Martínez", "ana.martinez@university.edu",
                "password456", "Académico");
    }

    @Test
    void createAdministrator_HappyPath_ReturnsCreatedAdmin() {
        when(adminRepository.save(any(Administrator.class))).thenReturn(admin1);

        Administrator result = administratorService.createAdministrator(admin1);

        assertNotNull(result);
        assertEquals("ADM001", result.getId());
        assertEquals("Carlos López", result.getName());
        assertEquals("Tecnología", result.getDepartment());
        verify(adminRepository, times(1)).save(admin1);
    }

    @Test
    void createAdministrator_HappyPath_DifferentAdmin() {
        when(adminRepository.save(any(Administrator.class))).thenReturn(admin2);

        Administrator result = administratorService.createAdministrator(admin2);

        assertNotNull(result);
        assertEquals("ADM002", result.getId());
        assertEquals("Ana Martínez", result.getName());
        assertEquals("Académico", result.getDepartment());
        verify(adminRepository, times(1)).save(admin2);
    }

    @Test
    void getAdministratorById_HappyPath_ReturnsAdmin() {
        when(adminRepository.findById("ADM001")).thenReturn(Optional.of(admin1));

        Administrator result = administratorService.getAdministratorById("ADM001");

        assertNotNull(result);
        assertEquals("ADM001", result.getId());
        assertEquals("Carlos López", result.getName());
        verify(adminRepository, times(1)).findById("ADM001");
    }

    @Test
    void getAdministratorById_HappyPath_DifferentAdmin() {
        when(adminRepository.findById("ADM002")).thenReturn(Optional.of(admin2));

        Administrator result = administratorService.getAdministratorById("ADM002");

        assertNotNull(result);
        assertEquals("ADM002", result.getId());
        assertEquals("Ana Martínez", result.getName());
        verify(adminRepository, times(1)).findById("ADM002");
    }

    @Test
    void getAdministratorById_Error_AdminNotFound() {
        when(adminRepository.findById("ADM999")).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            administratorService.getAdministratorById("ADM999");
        });

        assertEquals("Administrador no encontrado con ID: ADM999", exception.getMessage());
        verify(adminRepository, times(1)).findById("ADM999");
    }

    @Test
    void getAdministratorById_Error_NullId() {
        when(adminRepository.findById(null)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            administratorService.getAdministratorById(null);
        });

        assertTrue(exception.getMessage().contains("Administrador no encontrado con ID: null"));
        verify(adminRepository, times(1)).findById(null);
    }

    @Test
    void getAllAdministrators_HappyPath_ReturnsAllAdmins() {
        List<Administrator> admins = Arrays.asList(admin1, admin2);
        when(adminRepository.findAll()).thenReturn(admins);

        List<Administrator> result = administratorService.getAllAdministrators();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(adminRepository, times(1)).findAll();
    }

    @Test
    void getAllAdministrators_HappyPath_EmptyList() {
        when(adminRepository.findAll()).thenReturn(Collections.emptyList());

        List<Administrator> result = administratorService.getAllAdministrators();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(adminRepository, times(1)).findAll();
    }

    @Test
    void updateAdministrator_HappyPath_ReturnsUpdatedAdmin() {
        Administrator updatedAdmin = new Administrator("ADM001", "Carlos López Updated",
                "carlos.updated@university.edu", "newpassword",
                "Tecnología Actualizado");

        when(adminRepository.findById("ADM001")).thenReturn(Optional.of(admin1));
        when(adminRepository.save(any(Administrator.class))).thenReturn(updatedAdmin);

        Administrator result = administratorService.updateAdministrator("ADM001", updatedAdmin);

        assertNotNull(result);
        assertEquals("Carlos López Updated", result.getName());
        assertEquals("carlos.updated@university.edu", result.getEmail());
        assertEquals("Tecnología Actualizado", result.getDepartment());
        verify(adminRepository, times(1)).findById("ADM001");
        verify(adminRepository, times(1)).save(updatedAdmin);
    }

    @Test
    void updateAdministrator_HappyPath_DifferentAdmin() {
        Administrator updatedAdmin = new Administrator("ADM002", "Ana Martínez Updated",
                "ana.updated@university.edu", "newpassword456",
                "Académico Actualizado");

        when(adminRepository.findById("ADM002")).thenReturn(Optional.of(admin2));
        when(adminRepository.save(any(Administrator.class))).thenReturn(updatedAdmin);

        Administrator result = administratorService.updateAdministrator("ADM002", updatedAdmin);

        assertNotNull(result);
        assertEquals("Ana Martínez Updated", result.getName());
        assertEquals("ana.updated@university.edu", result.getEmail());
        assertEquals("Académico Actualizado", result.getDepartment());
        verify(adminRepository, times(1)).findById("ADM002");
        verify(adminRepository, times(1)).save(updatedAdmin);
    }

    @Test
    void updateAdministrator_Error_AdminNotFound() {
        Administrator updatedAdmin = new Administrator("ADM999", "Non Existent",
                "nonexistent@university.edu", "password",
                "Department");

        when(adminRepository.findById("ADM999")).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            administratorService.updateAdministrator("ADM999", updatedAdmin);
        });

        assertEquals("Administrador no encontrado con ID: ADM999", exception.getMessage());
        verify(adminRepository, times(1)).findById("ADM999");
        verify(adminRepository, never()).save(any(Administrator.class));
    }

    @Test
    void updateAdministrator_Error_NullId() {
        when(adminRepository.findById(null)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            administratorService.updateAdministrator(null, admin1);
        });

        assertTrue(exception.getMessage().contains("Administrador no encontrado con ID: null"));
        verify(adminRepository, times(1)).findById(null);
        verify(adminRepository, never()).save(any(Administrator.class));
    }

    @Test
    void deleteAdministrator_HappyPath_DeletesAdmin() {
        when(adminRepository.findById("ADM001")).thenReturn(Optional.of(admin1));
        doNothing().when(adminRepository).deleteById("ADM001");

        administratorService.deleteAdministrator("ADM001");

        verify(adminRepository, times(1)).findById("ADM001");
        verify(adminRepository, times(1)).deleteById("ADM001");
    }

    @Test
    void deleteAdministrator_HappyPath_DifferentAdmin() {
        when(adminRepository.findById("ADM002")).thenReturn(Optional.of(admin2));
        doNothing().when(adminRepository).deleteById("ADM002");

        administratorService.deleteAdministrator("ADM002");

        verify(adminRepository, times(1)).findById("ADM002");
        verify(adminRepository, times(1)).deleteById("ADM002");
    }

    @Test
    void deleteAdministrator_Error_AdminNotFound() {
        when(adminRepository.findById("ADM999")).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            administratorService.deleteAdministrator("ADM999");
        });

        assertEquals("Administrador no encontrado con ID: ADM999", exception.getMessage());
        verify(adminRepository, times(1)).findById("ADM999");
        verify(adminRepository, never()).deleteById(anyString());
    }

    @Test
    void deleteAdministrator_Error_NullId() {
        when(adminRepository.findById(null)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            administratorService.deleteAdministrator(null);
        });

        assertTrue(exception.getMessage().contains("Administrador no encontrado con ID: null"));
        verify(adminRepository, times(1)).findById(null);
        verify(adminRepository, never()).deleteById(anyString());
    }

    @Test
    void findByDepartment_HappyPath_ReturnsAdmins() {
        String department = "Tecnología";
        List<Administrator> admins = Collections.singletonList(admin1);
        when(adminRepository.findByDepartment(department)).thenReturn(admins);

        List<Administrator> result = administratorService.findByDepartment(department);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(department, result.get(0).getDepartment());
        verify(adminRepository, times(1)).findByDepartment(department);
    }

    @Test
    void findByDepartment_HappyPath_DifferentDepartment() {
        String department = "Académico";
        List<Administrator> admins = Collections.singletonList(admin2);
        when(adminRepository.findByDepartment(department)).thenReturn(admins);

        List<Administrator> result = administratorService.findByDepartment(department);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(department, result.get(0).getDepartment());
        verify(adminRepository, times(1)).findByDepartment(department);
    }

    @Test
    void findByDepartment_Error_NoAdminsInDepartment() {
        String department = "Departamento Inexistente";
        when(adminRepository.findByDepartment(department)).thenReturn(Collections.emptyList());

        List<Administrator> result = administratorService.findByDepartment(department);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(adminRepository, times(1)).findByDepartment(department);
    }

    @Test
    void findByDepartment_Error_NullParameter() {
        when(adminRepository.findByDepartment(null)).thenReturn(Collections.emptyList());

        List<Administrator> result = administratorService.findByDepartment(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(adminRepository, times(1)).findByDepartment(null);
    }
}
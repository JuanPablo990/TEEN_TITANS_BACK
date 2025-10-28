package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto.AdministratorDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Administrator;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.AdministratorService;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdministratorControllerTest {

    @Mock
    private AdministratorService administratorService;

    @InjectMocks
    private AdministratorController administratorController;

    private Administrator admin1;
    private Administrator admin2;
    private AdministratorDTO adminDTO1;
    private AdministratorDTO adminDTO2;

    @BeforeEach
    void setUp() {
        admin1 = new Administrator("1", "Robin", "robin@titans.edu", "pass123", "Security");
        admin2 = new Administrator("2", "Cyborg", "cyborg@titans.edu", "pass456", "Technology");

        adminDTO1 = new AdministratorDTO();
        adminDTO1.setId("1");
        adminDTO1.setName("Robin");
        adminDTO1.setEmail("robin@titans.edu");
        adminDTO1.setDepartment("Security");
        adminDTO1.setActive(true);

        adminDTO2 = new AdministratorDTO();
        adminDTO2.setId("2");
        adminDTO2.setName("Cyborg");
        adminDTO2.setEmail("cyborg@titans.edu");
        adminDTO2.setDepartment("Technology");
        adminDTO2.setActive(true);
    }

    @Test
    @DisplayName("Caso exitoso - Crear administrador correctamente")
    void testCreateAdministrator_Exitoso() {
        when(administratorService.createAdministrator(any(Administrator.class))).thenReturn(admin1);

        ResponseEntity<?> response = administratorController.createAdministrator(adminDTO1);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        AdministratorDTO responseDTO = (AdministratorDTO) response.getBody();
        assertEquals("Robin", responseDTO.getName());
        assertEquals("Security", responseDTO.getDepartment());
        verify(administratorService, times(1)).createAdministrator(any(Administrator.class));
    }

    @Test
    @DisplayName("Caso error - Crear administrador con AppException")
    void testCreateAdministrator_ConAppException() {
        when(administratorService.createAdministrator(any(Administrator.class)))
                .thenThrow(new AppException("Email ya registrado"));

        ResponseEntity<?> response = administratorController.createAdministrator(adminDTO1);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertEquals("Email ya registrado", errorResponse.get("error"));
        verify(administratorService, times(1)).createAdministrator(any(Administrator.class));
    }

    @Test
    @DisplayName("Caso exitoso - Obtener administrador por ID")
    void testGetAdministratorById_Exitoso() {
        when(administratorService.getAdministratorById("1")).thenReturn(admin1);

        ResponseEntity<?> response = administratorController.getAdministratorById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        AdministratorDTO responseDTO = (AdministratorDTO) response.getBody();
        assertEquals("Robin", responseDTO.getName());
        assertEquals("robin@titans.edu", responseDTO.getEmail());
        verify(administratorService, times(1)).getAdministratorById("1");
    }

    @Test
    @DisplayName("Caso error - Obtener administrador por ID no encontrado")
    void testGetAdministratorById_NoEncontrado() {
        when(administratorService.getAdministratorById("99"))
                .thenThrow(new AppException("Administrador no encontrado"));

        ResponseEntity<?> response = administratorController.getAdministratorById("99");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertEquals("Administrador no encontrado", errorResponse.get("error"));
        verify(administratorService, times(1)).getAdministratorById("99");
    }

    @Test
    @DisplayName("Caso exitoso - Obtener todos los administradores")
    void testGetAllAdministrators_Exitoso() {
        List<Administrator> admins = Arrays.asList(admin1, admin2);
        when(administratorService.getAllAdministrators()).thenReturn(admins);

        ResponseEntity<?> response = administratorController.getAllAdministrators();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        List<AdministratorDTO> adminsDTO = (List<AdministratorDTO>) responseBody.get("administrators");
        assertEquals(2, responseBody.get("count"));
        assertEquals(2, adminsDTO.size());
        verify(administratorService, times(1)).getAllAdministrators();
    }

    @Test
    @DisplayName("Caso exitoso - Actualizar administrador")
    void testUpdateAdministrator_Exitoso() {
        when(administratorService.updateAdministrator(eq("1"), any(Administrator.class))).thenReturn(admin1);

        ResponseEntity<?> response = administratorController.updateAdministrator("1", adminDTO1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        AdministratorDTO responseDTO = (AdministratorDTO) response.getBody();
        assertEquals("Robin", responseDTO.getName());
        verify(administratorService, times(1)).updateAdministrator(eq("1"), any(Administrator.class));
    }

    @Test
    @DisplayName("Caso error - Actualizar administrador con AppException")
    void testUpdateAdministrator_ConAppException() {
        when(administratorService.updateAdministrator(eq("1"), any(Administrator.class)))
                .thenThrow(new AppException("Email en uso"));

        ResponseEntity<?> response = administratorController.updateAdministrator("1", adminDTO1);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertEquals("Email en uso", errorResponse.get("error"));
        verify(administratorService, times(1)).updateAdministrator(eq("1"), any(Administrator.class));
    }

    @Test
    @DisplayName("Caso exitoso - Eliminar administrador")
    void testDeleteAdministrator_Exitoso() {
        doNothing().when(administratorService).deleteAdministrator("1");

        ResponseEntity<?> response = administratorController.deleteAdministrator("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertEquals("Administrador eliminado exitosamente", responseBody.get("message"));
        assertEquals("1", responseBody.get("deletedId"));
        verify(administratorService, times(1)).deleteAdministrator("1");
    }

    @Test
    @DisplayName("Caso error - Eliminar administrador no encontrado")
    void testDeleteAdministrator_NoEncontrado() {
        doThrow(new AppException("Administrador no encontrado")).when(administratorService).deleteAdministrator("99");

        ResponseEntity<?> response = administratorController.deleteAdministrator("99");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertEquals("Administrador no encontrado", errorResponse.get("error"));
        verify(administratorService, times(1)).deleteAdministrator("99");
    }

    @Test
    @DisplayName("Caso exitoso - Buscar por departamento")
    void testFindByDepartment_Exitoso() {
        List<Administrator> admins = Arrays.asList(admin1);
        when(administratorService.findByDepartment("Security")).thenReturn(admins);

        ResponseEntity<?> response = administratorController.findByDepartment("Security");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("Security", responseBody.get("department"));
        List<AdministratorDTO> adminsDTO = (List<AdministratorDTO>) responseBody.get("administrators");
        assertEquals(1, responseBody.get("count"));
        assertEquals(1, adminsDTO.size());
        verify(administratorService, times(1)).findByDepartment("Security");
    }

    @Test
    @DisplayName("Caso exitoso - Buscar por patrón de departamento")
    void testFindByDepartmentContaining_Exitoso() {
        List<Administrator> admins = Arrays.asList(admin1, admin2);
        when(administratorService.findByDepartmentContaining("Tech")).thenReturn(admins);

        ResponseEntity<?> response = administratorController.findByDepartmentContaining("Tech");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("Tech", responseBody.get("pattern"));
        List<AdministratorDTO> adminsDTO = (List<AdministratorDTO>) responseBody.get("administrators");
        assertEquals(2, responseBody.get("count"));
        assertEquals(2, adminsDTO.size());
        verify(administratorService, times(1)).findByDepartmentContaining("Tech");
    }

    @Test
    @DisplayName("Caso exitoso - Buscar por nombre")
    void testFindByNameContaining_Exitoso() {
        List<Administrator> admins = Arrays.asList(admin1);
        when(administratorService.findByNameContaining("Rob")).thenReturn(admins);

        ResponseEntity<?> response = administratorController.findByNameContaining("Rob");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("Rob", responseBody.get("name"));
        List<AdministratorDTO> adminsDTO = (List<AdministratorDTO>) responseBody.get("administrators");
        assertEquals(1, responseBody.get("count"));
        assertEquals(1, adminsDTO.size());
        verify(administratorService, times(1)).findByNameContaining("Rob");
    }

    @Test
    @DisplayName("Caso exitoso - Obtener administradores activos")
    void testFindActiveAdministrators_Exitoso() {
        List<Administrator> admins = Arrays.asList(admin1, admin2);
        when(administratorService.findActiveAdministrators()).thenReturn(admins);

        ResponseEntity<?> response = administratorController.findActiveAdministrators();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        List<AdministratorDTO> adminsDTO = (List<AdministratorDTO>) responseBody.get("activeAdministrators");
        assertEquals(2, responseBody.get("count"));
        assertEquals(2, adminsDTO.size());
        verify(administratorService, times(1)).findActiveAdministrators();
    }



    @Test
    @DisplayName("Caso exitoso - Health check")
    void testHealthCheck_Exitoso() {
        ResponseEntity<String> response = administratorController.healthCheck();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("AdministratorController is working properly", response.getBody());
    }

    @Test
    @DisplayName("Caso borde - Crear administrador con campos nulos")
    void testCreateAdministrator_CamposNulos() {
        AdministratorDTO dtoNulo = new AdministratorDTO();
        dtoNulo.setName(null);
        dtoNulo.setEmail(null);
        dtoNulo.setDepartment(null);

        when(administratorService.createAdministrator(any(Administrator.class))).thenReturn(admin1);

        ResponseEntity<?> response = administratorController.createAdministrator(dtoNulo);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(administratorService, times(1)).createAdministrator(any(Administrator.class));
    }

    @Test
    @DisplayName("Caso borde - Buscar por departamento vacío")
    void testFindByDepartment_Vacio() {
        List<Administrator> admins = Arrays.asList();
        when(administratorService.findByDepartment("")).thenReturn(admins);

        ResponseEntity<?> response = administratorController.findByDepartment("");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("", responseBody.get("department"));
        List<AdministratorDTO> adminsDTO = (List<AdministratorDTO>) responseBody.get("administrators");
        assertEquals(0, responseBody.get("count"));
        assertTrue(adminsDTO.isEmpty());
        verify(administratorService, times(1)).findByDepartment("");
    }
}
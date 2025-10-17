package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.AdministratorService;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Administrator;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdministratorControllerTest {

    @Mock
    private AdministratorService administratorService;

    @InjectMocks
    private AdministratorController administratorController;

    @Test
    void createAdministrator_HappyPath1_ShouldReturnCreated() {

        Administrator admin = new Administrator();
        admin.setId("ADMIN001");
        when(administratorService.createAdministrator(any(Administrator.class))).thenReturn(admin);


        ResponseEntity<?> response = administratorController.createAdministrator(admin);


        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(admin, response.getBody());
        verify(administratorService, times(1)).createAdministrator(admin);
    }

    @Test
    void createAdministrator_HappyPath2_WithDifferentAdmin_ShouldReturnCreated() {

        Administrator admin = new Administrator("ADMIN002", "Maria Garcia", "maria@university.edu",
                "password123", "Admissions");
        when(administratorService.createAdministrator(any(Administrator.class))).thenReturn(admin);


        ResponseEntity<?> response = administratorController.createAdministrator(admin);


        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(admin, response.getBody());
    }

    @Test
    void createAdministrator_ErrorPath1_AppException_ShouldReturnBadRequest() {

        when(administratorService.createAdministrator(any(Administrator.class)))
                .thenThrow(new AppException("Email ya registrado"));

        Administrator admin = new Administrator();


        ResponseEntity<?> response = administratorController.createAdministrator(admin);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email ya registrado", response.getBody());
    }

    @Test
    void createAdministrator_ErrorPath2_GenericException_ShouldReturnInternalServerError() {

        when(administratorService.createAdministrator(any(Administrator.class)))
                .thenThrow(new RuntimeException("Database error"));

        Administrator admin = new Administrator();


        ResponseEntity<?> response = administratorController.createAdministrator(admin);


        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Error interno del servidor"));
    }

    @Test
    void getAdministratorById_HappyPath1_ShouldReturnOk() {

        Administrator admin = new Administrator();
        admin.setId("ADMIN001");
        when(administratorService.getAdministratorById("ADMIN001")).thenReturn(admin);


        ResponseEntity<?> response = administratorController.getAdministratorById("ADMIN001");


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(admin, response.getBody());
        verify(administratorService, times(1)).getAdministratorById("ADMIN001");
    }

    @Test
    void getAdministratorById_HappyPath2_WithDifferentId_ShouldReturnOk() {

        Administrator admin = new Administrator("ADMIN002", "Carlos Lopez", "carlos@university.edu",
                "password123", "Registrar");
        when(administratorService.getAdministratorById("ADMIN002")).thenReturn(admin);


        ResponseEntity<?> response = administratorController.getAdministratorById("ADMIN002");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(admin, response.getBody());
    }

    @Test
    void getAdministratorById_ErrorPath1_AppException_ShouldReturnNotFound() {

        when(administratorService.getAdministratorById("ADMIN999"))
                .thenThrow(new AppException("Administrador no encontrado"));


        ResponseEntity<?> response = administratorController.getAdministratorById("ADMIN999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Administrador no encontrado", response.getBody());
    }

    @Test
    void getAdministratorById_ErrorPath2_GenericException_ShouldReturnInternalServerError() {

        when(administratorService.getAdministratorById("ADMIN001"))
                .thenThrow(new RuntimeException("Connection error"));


        ResponseEntity<?> response = administratorController.getAdministratorById("ADMIN001");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Error interno del servidor"));
    }

    @Test
    void getAllAdministrators_HappyPath1_ShouldReturnOk() {

        List<Administrator> admins = Arrays.asList(
                new Administrator("ADMIN001", "Admin One", "admin1@university.edu", "pass1", "IT"),
                new Administrator("ADMIN002", "Admin Two", "admin2@university.edu", "pass2", "HR")
        );
        when(administratorService.getAllAdministrators()).thenReturn(admins);

        ResponseEntity<?> response = administratorController.getAllAdministrators();


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(admins, response.getBody());
        verify(administratorService, times(1)).getAllAdministrators();
    }

    @Test
    void getAllAdministrators_HappyPath2_EmptyList_ShouldReturnOk() {

        List<Administrator> admins = Arrays.asList();
        when(administratorService.getAllAdministrators()).thenReturn(admins);


        ResponseEntity<?> response = administratorController.getAllAdministrators();


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, ((List<?>) response.getBody()).size());
    }

    @Test
    void getAllAdministrators_ErrorPath1_GenericException_ShouldReturnInternalServerError() {

        when(administratorService.getAllAdministrators())
                .thenThrow(new RuntimeException("Database unavailable"));

        ResponseEntity<?> response = administratorController.getAllAdministrators();


        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Error interno del servidor"));
    }

    @Test
    void updateAdministrator_HappyPath1_ShouldReturnOk() {

        Administrator admin = new Administrator("ADMIN001", "Updated Name", "updated@university.edu",
                "newpassword", "Updated Dept");
        when(administratorService.updateAdministrator(eq("ADMIN001"), any(Administrator.class))).thenReturn(admin);


        ResponseEntity<?> response = administratorController.updateAdministrator("ADMIN001", admin);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(admin, response.getBody());
        verify(administratorService, times(1)).updateAdministrator("ADMIN001", admin);
    }

    @Test
    void updateAdministrator_HappyPath2_WithDifferentData_ShouldReturnOk() {

        Administrator admin = new Administrator("ADMIN002", "Another Admin", "another@university.edu",
                "password", "Finance");
        when(administratorService.updateAdministrator(eq("ADMIN002"), any(Administrator.class))).thenReturn(admin);


        ResponseEntity<?> response = administratorController.updateAdministrator("ADMIN002", admin);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(admin, response.getBody());
    }

    @Test
    void updateAdministrator_ErrorPath1_AppException_ShouldReturnNotFound() {

        when(administratorService.updateAdministrator(eq("ADMIN999"), any(Administrator.class)))
                .thenThrow(new AppException("Administrador no existe"));

        Administrator admin = new Administrator();


        ResponseEntity<?> response = administratorController.updateAdministrator("ADMIN999", admin);


        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Administrador no existe", response.getBody());
    }

    @Test
    void updateAdministrator_ErrorPath2_GenericException_ShouldReturnInternalServerError() {

        when(administratorService.updateAdministrator(eq("ADMIN001"), any(Administrator.class)))
                .thenThrow(new RuntimeException("Update failed"));

        Administrator admin = new Administrator();


        ResponseEntity<?> response = administratorController.updateAdministrator("ADMIN001", admin);


        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Error interno del servidor"));
    }

    @Test
    void deleteAdministrator_HappyPath1_ShouldReturnOk() {

        doNothing().when(administratorService).deleteAdministrator("ADMIN001");


        ResponseEntity<?> response = administratorController.deleteAdministrator("ADMIN001");


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Administrador eliminado exitosamente", response.getBody());
        verify(administratorService, times(1)).deleteAdministrator("ADMIN001");
    }

    @Test
    void deleteAdministrator_HappyPath2_WithDifferentId_ShouldReturnOk() {

        doNothing().when(administratorService).deleteAdministrator("ADMIN002");


        ResponseEntity<?> response = administratorController.deleteAdministrator("ADMIN002");


        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteAdministrator_ErrorPath1_AppException_ShouldReturnNotFound() {

        doThrow(new AppException("Administrador no encontrado"))
                .when(administratorService).deleteAdministrator("ADMIN999");


        ResponseEntity<?> response = administratorController.deleteAdministrator("ADMIN999");


        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Administrador no encontrado", response.getBody());
    }

    @Test
    void deleteAdministrator_ErrorPath2_GenericException_ShouldReturnInternalServerError() {

        doThrow(new RuntimeException("Delete constraint violation"))
                .when(administratorService).deleteAdministrator("ADMIN001");

        ResponseEntity<?> response = administratorController.deleteAdministrator("ADMIN001");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Error interno del servidor"));
    }

    @Test
    void findByDepartment_HappyPath1_ShouldReturnOk() {

        List<Administrator> admins = Arrays.asList(
                new Administrator("ADMIN001", "IT Admin", "it@university.edu", "pass", "IT"),
                new Administrator("ADMIN003", "IT Manager", "itmgr@university.edu", "pass", "IT")
        );
        when(administratorService.findByDepartment("IT")).thenReturn(admins);


        ResponseEntity<?> response = administratorController.findByDepartment("IT");


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(admins, response.getBody());
        verify(administratorService, times(1)).findByDepartment("IT");
    }

    @Test
    void findByDepartment_HappyPath2_EmptyResult_ShouldReturnOk() {

        List<Administrator> admins = Arrays.asList();
        when(administratorService.findByDepartment("Nonexistent")).thenReturn(admins);

        ResponseEntity<?> response = administratorController.findByDepartment("Nonexistent");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, ((List<?>) response.getBody()).size());
    }

    @Test
    void findByDepartment_ErrorPath1_GenericException_ShouldReturnInternalServerError() {

        when(administratorService.findByDepartment("IT"))
                .thenThrow(new RuntimeException("Search error"));


        ResponseEntity<?> response = administratorController.findByDepartment("IT");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Error interno del servidor"));
    }

    @Test
    void healthCheck_HappyPath_ShouldReturnOk() {

        ResponseEntity<String> response = administratorController.healthCheck();


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("AdministratorController is working properly", response.getBody());
    }
}
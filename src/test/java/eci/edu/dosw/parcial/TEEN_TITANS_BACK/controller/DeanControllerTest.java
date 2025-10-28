package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto.DeanDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Dean;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.DeanService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeanControllerTest {

    @Mock
    private DeanService deanService;

    @InjectMocks
    private DeanController deanController;

    private Dean dean1;
    private Dean dean2;
    private DeanDTO deanDTO1;
    private DeanDTO deanDTO2;

    @BeforeEach
    void setUp() {
        dean1 = new Dean("1", "Dr. Starfire", "starfire@titans.edu", "pass123", "Engineering", "Building A");
        dean2 = new Dean("2", "Dr. Raven", "raven@titans.edu", "pass456", "Science", "Building B");

        deanDTO1 = new DeanDTO();
        deanDTO1.setId("1");
        deanDTO1.setName("Dr. Starfire");
        deanDTO1.setEmail("starfire@titans.edu");
        deanDTO1.setFaculty("Engineering");
        deanDTO1.setOfficeLocation("Building A");
        deanDTO1.setActive(true);

        deanDTO2 = new DeanDTO();
        deanDTO2.setId("2");
        deanDTO2.setName("Dr. Raven");
        deanDTO2.setEmail("raven@titans.edu");
        deanDTO2.setFaculty("Science");
        deanDTO2.setOfficeLocation("Building B");
        deanDTO2.setActive(true);
    }

    @Test
    @DisplayName("Caso exitoso - Crear decano correctamente")
    void testCreateDean_Exitoso() {
        when(deanService.createDean(any(Dean.class))).thenReturn(dean1);

        ResponseEntity<?> response = deanController.createDean(deanDTO1);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        DeanDTO responseDTO = (DeanDTO) response.getBody();
        assertEquals("Dr. Starfire", responseDTO.getName());
        assertEquals("Engineering", responseDTO.getFaculty());
        assertEquals("Building A", responseDTO.getOfficeLocation());
        verify(deanService, times(1)).createDean(any(Dean.class));
    }

    @Test
    @DisplayName("Caso error - Crear decano con AppException")
    void testCreateDean_ConAppException() {
        when(deanService.createDean(any(Dean.class)))
                .thenThrow(new AppException("Email ya registrado"));

        ResponseEntity<?> response = deanController.createDean(deanDTO1);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertEquals("Email ya registrado", errorResponse.get("error"));
        verify(deanService, times(1)).createDean(any(Dean.class));
    }

    @Test
    @DisplayName("Caso exitoso - Obtener decano por ID")
    void testGetDeanById_Exitoso() {
        when(deanService.getDeanById("1")).thenReturn(dean1);

        ResponseEntity<?> response = deanController.getDeanById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        DeanDTO responseDTO = (DeanDTO) response.getBody();
        assertEquals("Dr. Starfire", responseDTO.getName());
        assertEquals("Engineering", responseDTO.getFaculty());
        verify(deanService, times(1)).getDeanById("1");
    }

    @Test
    @DisplayName("Caso error - Obtener decano por ID no encontrado")
    void testGetDeanById_NoEncontrado() {
        when(deanService.getDeanById("99"))
                .thenThrow(new AppException("Decano no encontrado"));

        ResponseEntity<?> response = deanController.getDeanById("99");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertEquals("Decano no encontrado", errorResponse.get("error"));
        verify(deanService, times(1)).getDeanById("99");
    }

    @Test
    @DisplayName("Caso exitoso - Obtener todos los decanos")
    void testGetAllDeans_Exitoso() {
        List<Dean> deans = Arrays.asList(dean1, dean2);
        when(deanService.getAllDeans()).thenReturn(deans);

        ResponseEntity<?> response = deanController.getAllDeans();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        List<DeanDTO> deansDTO = (List<DeanDTO>) responseBody.get("deans");
        assertEquals(2, responseBody.get("count"));
        assertEquals(2, deansDTO.size());
        verify(deanService, times(1)).getAllDeans();
    }

    @Test
    @DisplayName("Caso exitoso - Actualizar decano")
    void testUpdateDean_Exitoso() {
        when(deanService.updateDean(eq("1"), any(Dean.class))).thenReturn(dean1);

        ResponseEntity<?> response = deanController.updateDean("1", deanDTO1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        DeanDTO responseDTO = (DeanDTO) response.getBody();
        assertEquals("Dr. Starfire", responseDTO.getName());
        verify(deanService, times(1)).updateDean(eq("1"), any(Dean.class));
    }

    @Test
    @DisplayName("Caso error - Actualizar decano con AppException")
    void testUpdateDean_ConAppException() {
        when(deanService.updateDean(eq("1"), any(Dean.class)))
                .thenThrow(new AppException("Email en uso"));

        ResponseEntity<?> response = deanController.updateDean("1", deanDTO1);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertEquals("Email en uso", errorResponse.get("error"));
        verify(deanService, times(1)).updateDean(eq("1"), any(Dean.class));
    }

    @Test
    @DisplayName("Caso exitoso - Eliminar decano")
    void testDeleteDean_Exitoso() {
        doNothing().when(deanService).deleteDean("1");

        ResponseEntity<?> response = deanController.deleteDean("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertEquals("Decano eliminado exitosamente", responseBody.get("message"));
        assertEquals("1", responseBody.get("deletedId"));
        verify(deanService, times(1)).deleteDean("1");
    }

    @Test
    @DisplayName("Caso error - Eliminar decano no encontrado")
    void testDeleteDean_NoEncontrado() {
        doThrow(new AppException("Decano no encontrado")).when(deanService).deleteDean("99");

        ResponseEntity<?> response = deanController.deleteDean("99");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertEquals("Decano no encontrado", errorResponse.get("error"));
        verify(deanService, times(1)).deleteDean("99");
    }

    @Test
    @DisplayName("Caso exitoso - Buscar por facultad")
    void testFindByFaculty_Exitoso() {
        List<Dean> deans = Arrays.asList(dean1);
        when(deanService.findByFaculty("Engineering")).thenReturn(deans);

        ResponseEntity<?> response = deanController.findByFaculty("Engineering");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("Engineering", responseBody.get("faculty"));
        List<DeanDTO> deansDTO = (List<DeanDTO>) responseBody.get("deans");
        assertEquals(1, responseBody.get("count"));
        assertEquals(1, deansDTO.size());
        verify(deanService, times(1)).findByFaculty("Engineering");
    }

    @Test
    @DisplayName("Caso exitoso - Buscar por ubicación de oficina")
    void testFindByOfficeLocation_Exitoso() {
        List<Dean> deans = Arrays.asList(dean1);
        when(deanService.findByOfficeLocation("Building A")).thenReturn(deans);

        ResponseEntity<?> response = deanController.findByOfficeLocation("Building A");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("Building A", responseBody.get("officeLocation"));
        List<DeanDTO> deansDTO = (List<DeanDTO>) responseBody.get("deans");
        assertEquals(1, responseBody.get("count"));
        assertEquals(1, deansDTO.size());
        verify(deanService, times(1)).findByOfficeLocation("Building A");
    }

    @Test
    @DisplayName("Caso exitoso - Obtener decano por facultad")
    void testGetDeanByFaculty_Exitoso() {
        when(deanService.getDeanByFaculty("Engineering")).thenReturn(dean1);

        ResponseEntity<?> response = deanController.getDeanByFaculty("Engineering");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        DeanDTO responseDTO = (DeanDTO) response.getBody();
        assertEquals("Dr. Starfire", responseDTO.getName());
        assertEquals("Engineering", responseDTO.getFaculty());
        verify(deanService, times(1)).getDeanByFaculty("Engineering");
    }

    @Test
    @DisplayName("Caso error - Obtener decano por facultad no encontrado")
    void testGetDeanByFaculty_NoEncontrado() {
        when(deanService.getDeanByFaculty("Artes"))
                .thenThrow(new AppException("No se encontró decano para la facultad: Artes"));

        ResponseEntity<?> response = deanController.getDeanByFaculty("Artes");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertEquals("No se encontró decano para la facultad: Artes", errorResponse.get("error"));
        verify(deanService, times(1)).getDeanByFaculty("Artes");
    }

    @Test
    @DisplayName("Caso exitoso - Buscar por facultad y ubicación de oficina")
    void testFindByFacultyAndOfficeLocation_Exitoso() {
        List<Dean> deans = Arrays.asList(dean1);
        when(deanService.findByFacultyAndOfficeLocation("Engineering", "Building A")).thenReturn(deans);

        ResponseEntity<?> response = deanController.findByFacultyAndOfficeLocation("Engineering", "Building A");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("Engineering", responseBody.get("faculty"));
        assertEquals("Building A", responseBody.get("officeLocation"));
        List<DeanDTO> deansDTO = (List<DeanDTO>) responseBody.get("deans");
        assertEquals(1, responseBody.get("count"));
        assertEquals(1, deansDTO.size());
        verify(deanService, times(1)).findByFacultyAndOfficeLocation("Engineering", "Building A");
    }

    @Test
    @DisplayName("Caso exitoso - Buscar por nombre")
    void testFindByNameContaining_Exitoso() {
        List<Dean> deans = Arrays.asList(dean1);
        when(deanService.findByNameContaining("Star")).thenReturn(deans);

        ResponseEntity<?> response = deanController.findByNameContaining("Star");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("Star", responseBody.get("name"));
        List<DeanDTO> deansDTO = (List<DeanDTO>) responseBody.get("deans");
        assertEquals(1, responseBody.get("count"));
        assertEquals(1, deansDTO.size());
        verify(deanService, times(1)).findByNameContaining("Star");
    }

    @Test
    @DisplayName("Caso exitoso - Buscar por patrón de facultad")
    void testFindByFacultyPattern_Exitoso() {
        List<Dean> deans = Arrays.asList(dean1, dean2);
        when(deanService.findByFacultyContaining("Eng")).thenReturn(deans);

        ResponseEntity<?> response = deanController.findByFacultyPattern("Eng");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("Eng", responseBody.get("pattern"));
        List<DeanDTO> deansDTO = (List<DeanDTO>) responseBody.get("deans");
        assertEquals(2, responseBody.get("count"));
        assertEquals(2, deansDTO.size());
        verify(deanService, times(1)).findByFacultyContaining("Eng");
    }

    @Test
    @DisplayName("Caso exitoso - Buscar por patrón de ubicación de oficina")
    void testFindByOfficeLocationPattern_Exitoso() {
        List<Dean> deans = Arrays.asList(dean1);
        when(deanService.findByOfficeLocationContaining("Building")).thenReturn(deans);

        ResponseEntity<?> response = deanController.findByOfficeLocationPattern("Building");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("Building", responseBody.get("pattern"));
        List<DeanDTO> deansDTO = (List<DeanDTO>) responseBody.get("deans");
        assertEquals(1, responseBody.get("count"));
        assertEquals(1, deansDTO.size());
        verify(deanService, times(1)).findByOfficeLocationContaining("Building");
    }

    @Test
    @DisplayName("Caso exitoso - Obtener decanos activos")
    void testFindActiveDeans_Exitoso() {
        List<Dean> deans = Arrays.asList(dean1, dean2);
        when(deanService.findActiveDeans()).thenReturn(deans);

        ResponseEntity<?> response = deanController.findActiveDeans();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        List<DeanDTO> deansDTO = (List<DeanDTO>) responseBody.get("activeDeans");
        assertEquals(2, responseBody.get("count"));
        assertEquals(2, deansDTO.size());
        verify(deanService, times(1)).findActiveDeans();
    }

    @Test
    @DisplayName("Caso exitoso - Búsqueda de decanos con criterios")
    void testSearchDeans_Exitoso() {
        List<Dean> deans = Arrays.asList(dean1);
        when(deanService.getAllDeans()).thenReturn(deans);

        ResponseEntity<?> response = deanController.searchDeans("Engineering", "Building A", true);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        Map<String, Object> searchCriteria = (Map<String, Object>) responseBody.get("searchCriteria");
        assertEquals("Engineering", searchCriteria.get("faculty"));
        assertEquals("Building A", searchCriteria.get("officeLocation"));
        assertEquals(true, searchCriteria.get("active"));
        List<DeanDTO> deansDTO = (List<DeanDTO>) responseBody.get("deans");
        assertEquals(1, responseBody.get("count"));
        assertEquals(1, deansDTO.size());
        verify(deanService, times(1)).getAllDeans();
    }



    @Test
    @DisplayName("Caso exitoso - Contar decanos por facultad")
    void testCountByFaculty_Exitoso() {
        when(deanService.countByFaculty("Engineering")).thenReturn(1L);

        ResponseEntity<?> response = deanController.countByFaculty("Engineering");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("Engineering", responseBody.get("faculty"));
        assertEquals(1L, responseBody.get("count"));
        verify(deanService, times(1)).countByFaculty("Engineering");
    }

    @Test
    @DisplayName("Caso exitoso - Contar decanos por ubicación de oficina")
    void testCountByOfficeLocation_Exitoso() {
        when(deanService.countByOfficeLocation("Building A")).thenReturn(1L);

        ResponseEntity<?> response = deanController.countByOfficeLocation("Building A");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("Building A", responseBody.get("officeLocation"));
        assertEquals(1L, responseBody.get("count"));
        verify(deanService, times(1)).countByOfficeLocation("Building A");
    }

    @Test
    @DisplayName("Caso exitoso - Obtener todas las facultades")
    void testGetAllFaculties_Exitoso() {
        List<Dean> deans = Arrays.asList(dean1, dean2);
        when(deanService.getAllDeans()).thenReturn(deans);

        ResponseEntity<?> response = deanController.getAllFaculties();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        List<String> faculties = (List<String>) responseBody.get("faculties");
        assertEquals(2, responseBody.get("count"));
        assertTrue(faculties.contains("Engineering"));
        assertTrue(faculties.contains("Science"));
        verify(deanService, times(1)).getAllDeans();
    }

    @Test
    @DisplayName("Caso exitoso - Health check")
    void testHealthCheck_Exitoso() {
        ResponseEntity<String> response = deanController.healthCheck();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("DeanController is working properly", response.getBody());
    }

    @Test
    @DisplayName("Caso borde - Buscar por facultad vacía")
    void testFindByFaculty_Vacio() {
        List<Dean> deans = Arrays.asList();
        when(deanService.findByFaculty("")).thenReturn(deans);

        ResponseEntity<?> response = deanController.findByFaculty("");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("", responseBody.get("faculty"));
        List<DeanDTO> deansDTO = (List<DeanDTO>) responseBody.get("deans");
        assertEquals(0, responseBody.get("count"));
        assertTrue(deansDTO.isEmpty());
        verify(deanService, times(1)).findByFaculty("");
    }

    @Test
    @DisplayName("Caso borde - Crear decano con campos nulos")
    void testCreateDean_CamposNulos() {
        DeanDTO dtoNulo = new DeanDTO();
        dtoNulo.setName(null);
        dtoNulo.setEmail(null);
        dtoNulo.setFaculty(null);
        dtoNulo.setOfficeLocation(null);

        when(deanService.createDean(any(Dean.class))).thenReturn(dean1);

        ResponseEntity<?> response = deanController.createDean(dtoNulo);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(deanService, times(1)).createDean(any(Dean.class));
    }


    @Test
    @DisplayName("Caso error - Obtener estadísticas de decanos con excepción")
    void testGetDeanStatistics_ConExcepcion() {
        when(deanService.getAllDeans()).thenThrow(new RuntimeException("Error inesperado"));

        ResponseEntity<?> response = deanController.getDeanStatistics();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertEquals("Error al obtener estadísticas de decanos", errorResponse.get("error"));
        verify(deanService, times(1)).getAllDeans();
    }

    @Test
    @DisplayName("Caso exitoso - Migrar roles de decanos")
    void testMigrateDeanRoles_Exitoso() {
        // Configurar decanos con roles nulos
        dean1.setRole(null);
        dean2.setRole(null);
        List<Dean> deans = Arrays.asList(dean1, dean2);

        when(deanService.getAllDeans()).thenReturn(deans);
        when(deanService.updateDean(anyString(), any(Dean.class))).thenReturn(dean1);

        ResponseEntity<?> response = deanController.migrateDeanRoles();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("Migración de roles de decanos completada", responseBody.get("message"));
        assertEquals(2, responseBody.get("deansUpdated"));
        assertEquals(2, responseBody.get("totalDeans"));
        verify(deanService, times(2)).updateDean(anyString(), any(Dean.class));
    }

    @Test
    @DisplayName("Caso error - Migrar roles de decanos con excepción")
    void testMigrateDeanRoles_ConExcepcion() {
        when(deanService.getAllDeans()).thenThrow(new RuntimeException("Error de base de datos"));

        ResponseEntity<?> response = deanController.migrateDeanRoles();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertTrue(errorResponse.get("error").contains("Error durante la migración"));
        verify(deanService, times(1)).getAllDeans();
    }

    @Test
    @DisplayName("Caso borde - Migrar roles cuando no hay decanos para actualizar")
    void testMigrateDeanRoles_SinActualizaciones() {
        // Los decanos ya tienen rol asignado
        List<Dean> deans = Arrays.asList(dean1, dean2);
        when(deanService.getAllDeans()).thenReturn(deans);

        ResponseEntity<?> response = deanController.migrateDeanRoles();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals(0, responseBody.get("deansUpdated"));
        verify(deanService, never()).updateDean(anyString(), any(Dean.class));
    }

    @Test
    @DisplayName("Caso error - Obtener todos los decanos con excepción")
    void testGetAllDeans_ConExcepcion() {
        when(deanService.getAllDeans()).thenThrow(new RuntimeException("Error de base de datos"));

        ResponseEntity<?> response = deanController.getAllDeans();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertEquals("Error al obtener la lista de decanos", errorResponse.get("error"));
    }

    @Test
    @DisplayName("Caso error - Buscar por facultad con excepción")
    void testFindByFaculty_ConExcepcion() {
        when(deanService.findByFaculty("Engineering"))
                .thenThrow(new RuntimeException("Error de base de datos"));

        ResponseEntity<?> response = deanController.findByFaculty("Engineering");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertEquals("Error al buscar decanos por facultad", errorResponse.get("error"));
    }

    @Test
    @DisplayName("Caso error - Buscar por ubicación de oficina con excepción")
    void testFindByOfficeLocation_ConExcepcion() {
        when(deanService.findByOfficeLocation("Building A"))
                .thenThrow(new RuntimeException("Error de base de datos"));

        ResponseEntity<?> response = deanController.findByOfficeLocation("Building A");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertEquals("Error al buscar decanos por ubicación de oficina", errorResponse.get("error"));
    }

    @Test
    @DisplayName("Caso error - Buscar por nombre con excepción")
    void testFindByNameContaining_ConExcepcion() {
        when(deanService.findByNameContaining("Star"))
                .thenThrow(new RuntimeException("Error de base de datos"));

        ResponseEntity<?> response = deanController.findByNameContaining("Star");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertEquals("Error al buscar decanos por nombre", errorResponse.get("error"));
    }

    @Test
    @DisplayName("Caso error - Buscar por patrón de facultad con excepción")
    void testFindByFacultyPattern_ConExcepcion() {
        when(deanService.findByFacultyContaining("Eng"))
                .thenThrow(new RuntimeException("Error de base de datos"));

        ResponseEntity<?> response = deanController.findByFacultyPattern("Eng");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertEquals("Error al buscar decanos por patrón de facultad", errorResponse.get("error"));
    }

    @Test
    @DisplayName("Caso error - Buscar por patrón de ubicación con excepción")
    void testFindByOfficeLocationPattern_ConExcepcion() {
        when(deanService.findByOfficeLocationContaining("Building"))
                .thenThrow(new RuntimeException("Error de base de datos"));

        ResponseEntity<?> response = deanController.findByOfficeLocationPattern("Building");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertEquals("Error al buscar decanos por patrón de ubicación de oficina", errorResponse.get("error"));
    }

    @Test
    @DisplayName("Caso error - Obtener decanos activos con excepción")
    void testFindActiveDeans_ConExcepcion() {
        when(deanService.findActiveDeans())
                .thenThrow(new RuntimeException("Error de base de datos"));

        ResponseEntity<?> response = deanController.findActiveDeans();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertEquals("Error al obtener los decanos activos", errorResponse.get("error"));
    }

    @Test
    @DisplayName("Caso error - Búsqueda de decanos con excepción")
    void testSearchDeans_ConExcepcion() {
        when(deanService.getAllDeans())
                .thenThrow(new RuntimeException("Error de base de datos"));

        ResponseEntity<?> response = deanController.searchDeans("Engineering", "Building A", true);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertEquals("Error en la búsqueda de decanos", errorResponse.get("error"));
    }

    @Test
    @DisplayName("Caso error - Obtener facultades con excepción")
    void testGetAllFaculties_ConExcepcion() {
        when(deanService.getAllDeans())
                .thenThrow(new RuntimeException("Error de base de datos"));

        ResponseEntity<?> response = deanController.getAllFaculties();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertEquals("Error al obtener la lista de facultades", errorResponse.get("error"));
    }

    @Test
    @DisplayName("Caso error - Contar por facultad con excepción")
    void testCountByFaculty_ConExcepcion() {
        when(deanService.countByFaculty("Engineering"))
                .thenThrow(new RuntimeException("Error de base de datos"));

        ResponseEntity<?> response = deanController.countByFaculty("Engineering");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertEquals("Error al contar decanos por facultad", errorResponse.get("error"));
    }

    @Test
    @DisplayName("Caso error - Contar por ubicación de oficina con excepción")
    void testCountByOfficeLocation_ConExcepcion() {
        when(deanService.countByOfficeLocation("Building A"))
                .thenThrow(new RuntimeException("Error de base de datos"));

        ResponseEntity<?> response = deanController.countByOfficeLocation("Building A");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertEquals("Error al contar decanos por ubicación de oficina", errorResponse.get("error"));
    }



}
package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeanControllerTest {

    @Mock
    private DeanService deanService;

    @InjectMocks
    private DeanController deanController;

    private Dean testDean;

    @BeforeEach
    void setUp() {
        testDean = new Dean("D001", "Dr. Juan Pérez", "juan.perez@universidad.edu", "password", "Ingeniería", "Edificio A, Oficina 101");
    }

    @Test
    @DisplayName("Caso exitoso - Crear decano")
    void testCreateDean_Exitoso() {
        when(deanService.createDean(any(Dean.class))).thenReturn(testDean);

        ResponseEntity<?> response = deanController.createDean(testDean);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testDean, response.getBody());
        verify(deanService, times(1)).createDean(testDean);
    }

    @Test
    @DisplayName("Caso error - Crear decano con AppException")
    void testCreateDean_AppException() {
        when(deanService.createDean(any(Dean.class))).thenThrow(new AppException("Error al crear decano"));

        ResponseEntity<?> response = deanController.createDean(testDean);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error al crear decano", response.getBody());
    }

    @Test
    @DisplayName("Caso exitoso - Obtener decano por ID")
    void testGetDeanById_Exitoso() {
        String deanId = "D001";
        when(deanService.getDeanById(deanId)).thenReturn(testDean);

        ResponseEntity<?> response = deanController.getDeanById(deanId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testDean, response.getBody());
        verify(deanService, times(1)).getDeanById(deanId);
    }

    @Test
    @DisplayName("Caso error - Obtener decano por ID no encontrado")
    void testGetDeanById_NoEncontrado() {
        String deanId = "D999";
        when(deanService.getDeanById(deanId)).thenThrow(new AppException("Decano no encontrado"));

        ResponseEntity<?> response = deanController.getDeanById(deanId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Decano no encontrado", response.getBody());
    }

    @Test
    @DisplayName("Caso exitoso - Obtener todos los decanos")
    void testGetAllDeans_Exitoso() {
        List<Dean> deans = List.of(testDean);
        when(deanService.getAllDeans()).thenReturn(deans);

        ResponseEntity<?> response = deanController.getAllDeans();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(deans, response.getBody());
        verify(deanService, times(1)).getAllDeans();
    }

    @Test
    @DisplayName("Caso error - Obtener todos los decanos con excepción")
    void testGetAllDeans_Excepcion() {
        when(deanService.getAllDeans()).thenThrow(new RuntimeException("Error de base de datos"));

        ResponseEntity<?> response = deanController.getAllDeans();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error interno del servidor al obtener los decanos", response.getBody());
    }

    @Test
    @DisplayName("Caso exitoso - Actualizar decano")
    void testUpdateDean_Exitoso() {
        String deanId = "D001";
        when(deanService.updateDean(eq(deanId), any(Dean.class))).thenReturn(testDean);

        ResponseEntity<?> response = deanController.updateDean(deanId, testDean);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testDean, response.getBody());
        verify(deanService, times(1)).updateDean(deanId, testDean);
    }

    @Test
    @DisplayName("Caso error - Actualizar decano no encontrado")
    void testUpdateDean_NoEncontrado() {
        String deanId = "D999";
        when(deanService.updateDean(eq(deanId), any(Dean.class))).thenThrow(new AppException("Decano no encontrado"));

        ResponseEntity<?> response = deanController.updateDean(deanId, testDean);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Decano no encontrado", response.getBody());
    }

    @Test
    @DisplayName("Caso exitoso - Eliminar decano")
    void testDeleteDean_Exitoso() {
        String deanId = "D001";
        doNothing().when(deanService).deleteDean(deanId);

        ResponseEntity<?> response = deanController.deleteDean(deanId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Decano eliminado exitosamente", response.getBody());
        verify(deanService, times(1)).deleteDean(deanId);
    }

    @Test
    @DisplayName("Caso error - Eliminar decano no encontrado")
    void testDeleteDean_NoEncontrado() {
        String deanId = "D999";
        doThrow(new AppException("Decano no encontrado")).when(deanService).deleteDean(deanId);

        ResponseEntity<?> response = deanController.deleteDean(deanId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Decano no encontrado", response.getBody());
    }

    @Test
    @DisplayName("Caso exitoso - Buscar decanos por facultad")
    void testFindByFaculty_Exitoso() {
        String faculty = "Ingeniería";
        List<Dean> deans = List.of(testDean);
        when(deanService.findByFaculty(faculty)).thenReturn(deans);

        ResponseEntity<?> response = deanController.findByFaculty(faculty);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(deans, response.getBody());
        verify(deanService, times(1)).findByFaculty(faculty);
    }

    @Test
    @DisplayName("Caso error - Buscar decanos por facultad con excepción")
    void testFindByFaculty_Excepcion() {
        String faculty = "Ingeniería";
        when(deanService.findByFaculty(faculty)).thenThrow(new RuntimeException("Error de base de datos"));

        ResponseEntity<?> response = deanController.findByFaculty(faculty);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error interno del servidor al buscar decanos por facultad", response.getBody());
    }

    @Test
    @DisplayName("Caso exitoso - Buscar decanos por ubicación de oficina")
    void testFindByOfficeLocation_Exitoso() {
        String location = "Edificio A";
        List<Dean> deans = List.of(testDean);
        when(deanService.findByOfficeLocation(location)).thenReturn(deans);

        ResponseEntity<?> response = deanController.findByOfficeLocation(location);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(deans, response.getBody());
        verify(deanService, times(1)).findByOfficeLocation(location);
    }

    @Test
    @DisplayName("Caso exitoso - Verificar existencia de decano por ID")
    void testExistsById_Exitoso() {
        String deanId = "D001";
        when(deanService.existsById(deanId)).thenReturn(true);

        ResponseEntity<?> response = deanController.existsById(deanId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());
        verify(deanService, times(1)).existsById(deanId);
    }

    @Test
    @DisplayName("Caso error - Verificar existencia de decano con excepción")
    void testExistsById_Excepcion() {
        String deanId = "D001";
        when(deanService.existsById(deanId)).thenThrow(new RuntimeException("Error de base de datos"));

        ResponseEntity<?> response = deanController.existsById(deanId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error interno del servidor al verificar la existencia del decano", response.getBody());
    }

    @Test
    @DisplayName("Caso exitoso - Obtener decano por facultad")
    void testGetDeanByFaculty_Exitoso() {
        String faculty = "Ingeniería";
        when(deanService.getDeanByFaculty(faculty)).thenReturn(testDean);

        ResponseEntity<?> response = deanController.getDeanByFaculty(faculty);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testDean, response.getBody());
        verify(deanService, times(1)).getDeanByFaculty(faculty);
    }

    @Test
    @DisplayName("Caso error - Obtener decano por facultad no encontrado")
    void testGetDeanByFaculty_NoEncontrado() {
        String faculty = "Medicina";
        when(deanService.getDeanByFaculty(faculty)).thenThrow(new AppException("No se encontró decano para la facultad"));

        ResponseEntity<?> response = deanController.getDeanByFaculty(faculty);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No se encontró decano para la facultad", response.getBody());
    }

    @Test
    @DisplayName("Caso exitoso - Health check")
    void testHealthCheck_Exitoso() {
        ResponseEntity<String> response = deanController.healthCheck();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("DeanController is working properly", response.getBody());
    }

    @Test
    @DisplayName("Caso borde - Buscar decanos por facultad sin resultados")
    void testFindByFaculty_SinResultados() {
        String faculty = "Arquitectura";
        List<Dean> emptyList = List.of();
        when(deanService.findByFaculty(faculty)).thenReturn(emptyList);

        ResponseEntity<?> response = deanController.findByFaculty(faculty);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(emptyList, response.getBody());
        assertTrue(((List<?>) response.getBody()).isEmpty());
    }

    @Test
    @DisplayName("Caso borde - Verificar existencia de decano con ID inexistente")
    void testExistsById_Inexistente() {
        String deanId = "D999";
        when(deanService.existsById(deanId)).thenReturn(false);

        ResponseEntity<?> response = deanController.existsById(deanId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(false, response.getBody());
    }

    @Test
    @DisplayName("Caso borde - Actualizar decano con datos nulos")
    void testUpdateDean_DatosNulos() {
        String deanId = "D001";
        Dean deanNulo = new Dean();
        when(deanService.updateDean(eq(deanId), any(Dean.class))).thenReturn(deanNulo);

        ResponseEntity<?> response = deanController.updateDean(deanId, deanNulo);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
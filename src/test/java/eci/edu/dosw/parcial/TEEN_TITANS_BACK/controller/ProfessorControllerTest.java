package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto.ProfessorDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Professor;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.ProfessorService;
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
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProfessorControllerTest {

    @Mock
    private ProfessorService professorService;

    @InjectMocks
    private ProfessorController professorController;

    private Professor professor1;
    private Professor professor2;
    private Professor professor3;
    private ProfessorDTO professorDTO1;
    private ProfessorDTO professorDTO2;
    private ProfessorDTO professorDTO3;

    @BeforeEach
    void setUp() {
        professor1 = new Professor();
        professor1.setId("1");
        professor1.setName("Profesor A");
        professor1.setEmail("profesorA@titans.edu");
        professor1.setDepartment("Mathematics");
        professor1.setIsTenured(true);
        professor1.setActive(true);
        professor1.setAreasOfExpertise(Arrays.asList("Álgebra", "Cálculo"));

        professor2 = new Professor();
        professor2.setId("2");
        professor2.setName("Profesor B");
        professor2.setEmail("profesorB@titans.edu");
        professor2.setDepartment("Physics");
        professor2.setIsTenured(false);
        professor2.setActive(true);
        professor2.setAreasOfExpertise(Arrays.asList("Física", "Mecánica"));

        professor3 = new Professor();
        professor3.setId("3");
        professor3.setName("Profesor C");
        professor3.setEmail("profesorC@titans.edu");
        professor3.setDepartment("Mathematics");
        professor3.setIsTenured(true);
        professor3.setActive(false);
        professor3.setAreasOfExpertise(Arrays.asList("Geometría", "Estadística"));

        professorDTO1 = new ProfessorDTO();
        professorDTO1.setId("1");
        professorDTO1.setName("Profesor A");
        professorDTO1.setEmail("profesorA@titans.edu");
        professorDTO1.setDepartment("Mathematics");
        professorDTO1.setIsTenured(true);
        professorDTO1.setActive(true);
        professorDTO1.setAreasOfExpertise(Arrays.asList("Álgebra", "Cálculo"));

        professorDTO2 = new ProfessorDTO();
        professorDTO2.setId("2");
        professorDTO2.setName("Profesor B");
        professorDTO2.setEmail("profesorB@titans.edu");
        professorDTO2.setDepartment("Physics");
        professorDTO2.setIsTenured(false);
        professorDTO2.setActive(true);
        professorDTO2.setAreasOfExpertise(Arrays.asList("Física", "Mecánica"));

        professorDTO3 = new ProfessorDTO();
        professorDTO3.setId("3");
        professorDTO3.setName("Profesor C");
        professorDTO3.setEmail("profesorC@titans.edu");
        professorDTO3.setDepartment("Mathematics");
        professorDTO3.setIsTenured(true);
        professorDTO3.setActive(false);
        professorDTO3.setAreasOfExpertise(Arrays.asList("Geometría", "Estadística"));
    }

    @Test
    @DisplayName("Caso exitoso - createProfessor crea profesor correctamente")
    void testCreateProfessor_Exitoso() {
        when(professorService.createProfessor(any(Professor.class))).thenReturn(professor1);

        ResponseEntity<?> response = professorController.createProfessor(professorDTO1);

        assertAll("Verificar respuesta de creación exitosa",
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(ProfessorDTO.class, response.getBody()),
                () -> assertEquals("Profesor A", ((ProfessorDTO) response.getBody()).getName())
        );

        verify(professorService, times(1)).createProfessor(any(Professor.class));
    }

    @Test
    @DisplayName("Caso error - createProfessor retorna error cuando AppException ocurre")
    void testCreateProfessor_ErrorAppException() {
        when(professorService.createProfessor(any(Professor.class)))
                .thenThrow(new AppException("Error al crear profesor"));

        ResponseEntity<?> response = professorController.createProfessor(professorDTO1);

        assertAll("Verificar respuesta de error por AppException",
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> assertTrue(((Map<?, ?>) response.getBody()).containsKey("error"))
        );

        verify(professorService, times(1)).createProfessor(any(Professor.class));
    }

    @Test
    @DisplayName("Caso exitoso - getProfessorById retorna profesor existente")
    void testGetProfessorById_Exitoso() {
        when(professorService.getProfessorById("1")).thenReturn(professor1);

        ResponseEntity<?> response = professorController.getProfessorById("1");

        assertAll("Verificar obtención exitosa de profesor",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(ProfessorDTO.class, response.getBody()),
                () -> assertEquals("1", ((ProfessorDTO) response.getBody()).getId()),
                () -> assertEquals("Profesor A", ((ProfessorDTO) response.getBody()).getName())
        );

        verify(professorService, times(1)).getProfessorById("1");
    }

    @Test
    @DisplayName("Caso error - getProfessorById retorna 404 cuando profesor no existe")
    void testGetProfessorById_NoEncontrado() {
        when(professorService.getProfessorById("99"))
                .thenThrow(new AppException("Profesor no encontrado"));

        ResponseEntity<?> response = professorController.getProfessorById("99");

        assertAll("Verificar respuesta 404",
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertTrue(((Map<?, ?>) response.getBody()).containsKey("error"))
        );

        verify(professorService, times(1)).getProfessorById("99");
    }

    @Test
    @DisplayName("Caso exitoso - getAllProfessors retorna lista de profesores")
    void testGetAllProfessors_Exitoso() {
        List<Professor> professors = Arrays.asList(professor1, professor2);
        when(professorService.getAllProfessors()).thenReturn(professors);

        ResponseEntity<?> response = professorController.getAllProfessors();

        assertAll("Verificar obtención exitosa de todos los profesores",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> assertTrue(((Map<?, ?>) response.getBody()).containsKey("professors")),
                () -> assertTrue(((Map<?, ?>) response.getBody()).containsKey("count")),
                () -> assertEquals(2, ((Map<?, ?>) response.getBody()).get("count"))
        );

        verify(professorService, times(1)).getAllProfessors();
    }

    @Test
    @DisplayName("Caso error - getAllProfessors retorna error interno")
    void testGetAllProfessors_ErrorInterno() {
        when(professorService.getAllProfessors()).thenThrow(new RuntimeException("Error de base de datos"));

        ResponseEntity<?> response = professorController.getAllProfessors();

        assertAll("Verificar error interno del servidor",
                () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertTrue(((Map<?, ?>) response.getBody()).containsKey("error"))
        );

        verify(professorService, times(1)).getAllProfessors();
    }

    @Test
    @DisplayName("Caso exitoso - updateProfessor actualiza profesor correctamente")
    void testUpdateProfessor_Exitoso() {
        when(professorService.updateProfessor(eq("1"), any(Professor.class))).thenReturn(professor1);

        ResponseEntity<?> response = professorController.updateProfessor("1", professorDTO1);

        assertAll("Verificar actualización exitosa",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(ProfessorDTO.class, response.getBody())
        );

        verify(professorService, times(1)).updateProfessor(eq("1"), any(Professor.class));
    }

    @Test
    @DisplayName("Caso error - updateProfessor retorna error cuando profesor no existe")
    void testUpdateProfessor_NoEncontrado() {
        when(professorService.updateProfessor(eq("99"), any(Professor.class)))
                .thenThrow(new AppException("Profesor no encontrado"));

        ResponseEntity<?> response = professorController.updateProfessor("99", professorDTO1);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(professorService, times(1)).updateProfessor(eq("99"), any(Professor.class));
    }

    @Test
    @DisplayName("Caso exitoso - deleteProfessor elimina profesor correctamente")
    void testDeleteProfessor_Exitoso() {
        doNothing().when(professorService).deleteProfessor("1");

        ResponseEntity<?> response = professorController.deleteProfessor("1");

        assertAll("Verificar eliminación exitosa",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> assertEquals("1", ((Map<?, ?>) response.getBody()).get("deletedId"))
        );

        verify(professorService, times(1)).deleteProfessor("1");
    }

    @Test
    @DisplayName("Caso error - deleteProfessor retorna 404 cuando profesor no existe")
    void testDeleteProfessor_NoEncontrado() {
        doThrow(new AppException("Profesor no encontrado"))
                .when(professorService).deleteProfessor("99");

        ResponseEntity<?> response = professorController.deleteProfessor("99");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(professorService, times(1)).deleteProfessor("99");
    }

    @Test
    @DisplayName("Caso exitoso - findByDepartment retorna profesores del departamento")
    void testFindByDepartment_Exitoso() {
        List<Professor> professors = Arrays.asList(professor1);
        when(professorService.findByDepartment("Mathematics")).thenReturn(professors);

        ResponseEntity<?> response = professorController.findByDepartment("Mathematics");

        assertAll("Verificar búsqueda por departamento exitosa",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertInstanceOf(Map.class, response.getBody()),
                () -> assertEquals("Mathematics", ((Map<?, ?>) response.getBody()).get("department")),
                () -> assertEquals(1, ((Map<?, ?>) response.getBody()).get("count"))
        );

        verify(professorService, times(1)).findByDepartment("Mathematics");
    }

    @Test
    @DisplayName("Caso borde - findByDepartment retorna lista vacía para departamento sin profesores")
    void testFindByDepartment_DepartamentoSinProfesores() {
        when(professorService.findByDepartment("Química")).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = professorController.findByDepartment("Química");

        assertAll("Verificar departamento sin profesores",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(0, ((Map<?, ?>) response.getBody()).get("count"))
        );

        verify(professorService, times(1)).findByDepartment("Química");
    }

    @Test
    @DisplayName("Caso exitoso - healthCheck retorna mensaje de estado")
    void testHealthCheck_Exitoso() {
        ResponseEntity<String> response = professorController.healthCheck();

        assertAll("Verificar health check",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("ProfessorController is working properly", response.getBody())
        );
    }

    @Test
    @DisplayName("Caso borde - getAllProfessors retorna lista vacía cuando no hay profesores")
    void testGetAllProfessors_ListaVacia() {
        when(professorService.getAllProfessors()).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = professorController.getAllProfessors();

        assertAll("Verificar lista vacía de profesores",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(0, ((Map<?, ?>) response.getBody()).get("count"))
        );

        verify(professorService, times(1)).getAllProfessors();
    }

    @Test
    @DisplayName("Caso error - createProfessor retorna error interno por excepción genérica")
    void testCreateProfessor_ErrorInterno() {
        when(professorService.createProfessor(any(Professor.class)))
                .thenThrow(new RuntimeException("Error de conexión"));

        ResponseEntity<?> response = professorController.createProfessor(professorDTO1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(professorService, times(1)).createProfessor(any(Professor.class));
    }

    @Test
    @DisplayName("Caso exitoso - findByTenured retorna profesores titulares")
    void testFindByTenured_Exitoso() {
        List<Professor> professors = Arrays.asList(professor1, professor3);
        when(professorService.findByTenured(true)).thenReturn(professors);

        ResponseEntity<?> response = professorController.findByTenured(true);

        assertAll("Verificar búsqueda por titularidad exitosa",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals(true, ((Map<?, ?>) response.getBody()).get("tenured")),
                () -> assertEquals(2, ((Map<?, ?>) response.getBody()).get("count"))
        );

        verify(professorService, times(1)).findByTenured(true);
    }

    @Test
    @DisplayName("Caso borde - findByTenured retorna lista vacía para titularidad sin profesores")
    void testFindByTenured_SinProfesores() {
        when(professorService.findByTenured(false)).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = professorController.findByTenured(false);

        assertAll("Verificar titularidad sin profesores",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(0, ((Map<?, ?>) response.getBody()).get("count"))
        );

        verify(professorService, times(1)).findByTenured(false);
    }

    @Test
    @DisplayName("Caso exitoso - findByAreaOfExpertise retorna profesores por especialización")
    void testFindByAreaOfExpertise_Exitoso() {
        List<Professor> professors = Arrays.asList(professor1);
        when(professorService.findByAreaOfExpertise("Álgebra")).thenReturn(professors);

        ResponseEntity<?> response = professorController.findByAreaOfExpertise("Álgebra");

        assertAll("Verificar búsqueda por especialización exitosa",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("Álgebra", ((Map<?, ?>) response.getBody()).get("expertise")),
                () -> assertEquals(1, ((Map<?, ?>) response.getBody()).get("count"))
        );

        verify(professorService, times(1)).findByAreaOfExpertise("Álgebra");
    }

    @Test
    @DisplayName("Caso error - findByAreaOfExpertise retorna error interno")
    void testFindByAreaOfExpertise_ErrorInterno() {
        when(professorService.findByAreaOfExpertise("Biología"))
                .thenThrow(new RuntimeException("Error de base de datos"));

        ResponseEntity<?> response = professorController.findByAreaOfExpertise("Biología");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(professorService, times(1)).findByAreaOfExpertise("Biología");
    }

    @Test
    @DisplayName("Caso exitoso - findActiveProfessors retorna profesores activos")
    void testFindActiveProfessors_Exitoso() {
        List<Professor> professors = Arrays.asList(professor1, professor2);
        when(professorService.findActiveProfessors()).thenReturn(professors);

        ResponseEntity<?> response = professorController.findActiveProfessors();

        assertAll("Verificar búsqueda de profesores activos exitosa",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals(2, ((Map<?, ?>) response.getBody()).get("count"))
        );

        verify(professorService, times(1)).findActiveProfessors();
    }

    @Test
    @DisplayName("Caso borde - findActiveProfessors retorna lista vacía sin profesores activos")
    void testFindActiveProfessors_SinActivos() {
        when(professorService.findActiveProfessors()).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = professorController.findActiveProfessors();

        assertAll("Verificar sin profesores activos",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(0, ((Map<?, ?>) response.getBody()).get("count"))
        );

        verify(professorService, times(1)).findActiveProfessors();
    }




    @Test
    @DisplayName("Caso error - getProfessorStats retorna error interno")
    void testGetProfessorStats_ErrorInterno() {
        when(professorService.getAllProfessors()).thenThrow(new RuntimeException("Error de base de datos"));

        ResponseEntity<?> response = professorController.getProfessorStats();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(professorService, times(1)).getAllProfessors();
    }

    @Test
    @DisplayName("Caso exitoso - findByDepartmentAndTenured retorna profesores filtrados")
    void testFindByDepartmentAndTenured_Exitoso() {
        List<Professor> professors = Arrays.asList(professor1);
        when(professorService.findByDepartmentAndTenured("Mathematics", true)).thenReturn(professors);

        ResponseEntity<?> response = professorController.findByDepartmentAndTenured("Mathematics", true);

        assertAll("Verificar búsqueda combinada exitosa",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("Mathematics", ((Map<?, ?>) response.getBody()).get("department")),
                () -> assertEquals(true, ((Map<?, ?>) response.getBody()).get("tenured")),
                () -> assertEquals(1, ((Map<?, ?>) response.getBody()).get("count"))
        );

        verify(professorService, times(1)).findByDepartmentAndTenured("Mathematics", true);
    }

    @Test
    @DisplayName("Caso borde - findByDepartmentAndTenured retorna lista vacía")
    void testFindByDepartmentAndTenured_ListaVacia() {
        when(professorService.findByDepartmentAndTenured("Historia", true)).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = professorController.findByDepartmentAndTenured("Historia", true);

        assertAll("Verificar búsqueda combinada sin resultados",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(0, ((Map<?, ?>) response.getBody()).get("count"))
        );

        verify(professorService, times(1)).findByDepartmentAndTenured("Historia", true);
    }

    @Test
    @DisplayName("Caso exitoso - findByNameContaining retorna profesores por nombre")
    void testFindByNameContaining_Exitoso() {
        List<Professor> professors = Arrays.asList(professor1);
        when(professorService.findByNameContaining("Profesor")).thenReturn(professors);

        ResponseEntity<?> response = professorController.findByNameContaining("Profesor");

        assertAll("Verificar búsqueda por nombre exitosa",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("Profesor", ((Map<?, ?>) response.getBody()).get("name")),
                () -> assertEquals(1, ((Map<?, ?>) response.getBody()).get("count"))
        );

        verify(professorService, times(1)).findByNameContaining("Profesor");
    }

    @Test
    @DisplayName("Caso borde - findByNameContaining retorna lista vacía para nombre no encontrado")
    void testFindByNameContaining_NombreNoEncontrado() {
        when(professorService.findByNameContaining("Inexistente")).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = professorController.findByNameContaining("Inexistente");

        assertAll("Verificar nombre no encontrado",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(0, ((Map<?, ?>) response.getBody()).get("count"))
        );

        verify(professorService, times(1)).findByNameContaining("Inexistente");
    }

    @Test
    @DisplayName("Caso error - updateProfessor retorna error interno")
    void testUpdateProfessor_ErrorInterno() {
        when(professorService.updateProfessor(eq("1"), any(Professor.class)))
                .thenThrow(new RuntimeException("Error de base de datos"));

        ResponseEntity<?> response = professorController.updateProfessor("1", professorDTO1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(professorService, times(1)).updateProfessor(eq("1"), any(Professor.class));
    }

    @Test
    @DisplayName("Caso error - deleteProfessor retorna error interno")
    void testDeleteProfessor_ErrorInterno() {
        doThrow(new RuntimeException("Error de base de datos"))
                .when(professorService).deleteProfessor("1");

        ResponseEntity<?> response = professorController.deleteProfessor("1");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(professorService, times(1)).deleteProfessor("1");
    }



    @Test
    @DisplayName("Caso exitoso - updateProfessor con email duplicado retorna error")
    void testUpdateProfessor_EmailDuplicado() {
        when(professorService.updateProfessor(eq("1"), any(Professor.class)))
                .thenThrow(new AppException("El email ya está en uso"));

        ResponseEntity<?> response = professorController.updateProfessor("1", professorDTO1);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(professorService, times(1)).updateProfessor(eq("1"), any(Professor.class));

    }


    @Test
    @DisplayName("Caso borde - findByNameContaining con nombre vacío")
    void testFindByNameContaining_NombreVacio() {
        when(professorService.findByNameContaining("")).thenReturn(Arrays.asList(professor1, professor2, professor3));

        ResponseEntity<?> response = professorController.findByNameContaining("");

        assertAll("Verificar búsqueda con nombre vacío",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(3, ((Map<?, ?>) response.getBody()).get("count"))
        );

        verify(professorService, times(1)).findByNameContaining("");
    }

    @Test
    @DisplayName("Caso error - findByDepartmentAndTenured retorna error interno")
    void testFindByDepartmentAndTenured_ErrorInterno() {
        when(professorService.findByDepartmentAndTenured("Mathematics", true))
                .thenThrow(new RuntimeException("Error de base de datos"));

        ResponseEntity<?> response = professorController.findByDepartmentAndTenured("Mathematics", true);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(professorService, times(1)).findByDepartmentAndTenured("Mathematics", true);
    }

    @Test
    @DisplayName("Caso borde - createProfessor con DTO campos mínimos")
    void testCreateProfessor_CamposMinimos() {
        ProfessorDTO minimalDTO = new ProfessorDTO();
        minimalDTO.setName("Profesor Min");
        minimalDTO.setEmail("min@titans.edu");

        Professor minimalProfessor = new Professor();
        minimalProfessor.setId("min-1");
        minimalProfessor.setName("Profesor Min");
        minimalProfessor.setEmail("min@titans.edu");
        minimalProfessor.setActive(true);

        when(professorService.createProfessor(any(Professor.class))).thenReturn(minimalProfessor);

        ResponseEntity<?> response = professorController.createProfessor(minimalDTO);

        assertAll("Verificar creación con campos mínimos",
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("Profesor Min", ((ProfessorDTO) response.getBody()).getName())
        );

        verify(professorService, times(1)).createProfessor(any(Professor.class));
    }


}
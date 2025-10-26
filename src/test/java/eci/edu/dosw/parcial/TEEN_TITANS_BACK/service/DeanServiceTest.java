package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Dean;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.DeanRepository;
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
class DeanServiceTest {

    @Mock
    private DeanRepository deanRepository;

    @InjectMocks
    private DeanService deanService;

    private Dean dean1;
    private Dean dean2;

    @BeforeEach
    void setUp() {
        dean1 = new Dean();
        dean1.setId("1");
        dean1.setName("Dean One");
        dean1.setEmail("dean1@titans.edu");
        dean1.setFaculty("Engineering");
        dean1.setOfficeLocation("Building A");
        dean1.setActive(true);

        dean2 = new Dean();
        dean2.setId("2");
        dean2.setName("Dean Two");
        dean2.setEmail("dean2@titans.edu");
        dean2.setFaculty("Medicine");
        dean2.setOfficeLocation("Building B");
        dean2.setActive(true);
    }

    @Test
    @DisplayName("Caso exitoso - createDean crea decano correctamente")
    void testCreateDean_Exitoso() {
        when(deanRepository.existsByEmail("dean1@titans.edu")).thenReturn(false);
        when(deanRepository.save(any(Dean.class))).thenReturn(dean1);

        Dean result = deanService.createDean(dean1);

        assertAll("Verificar creación exitosa",
                () -> assertNotNull(result),
                () -> assertEquals("Dean One", result.getName()),
                () -> assertEquals(UserRole.DEAN, result.getRole())
        );

        verify(deanRepository, times(1)).existsByEmail("dean1@titans.edu");
        verify(deanRepository, times(1)).save(any(Dean.class));
    }

    @Test
    @DisplayName("Caso error - createDean lanza excepción cuando email existe")
    void testCreateDean_EmailExistente() {
        when(deanRepository.existsByEmail("dean1@titans.edu")).thenReturn(true);

        AppException exception = assertThrows(AppException.class, () -> {
            deanService.createDean(dean1);
        });

        assertEquals("El email ya está registrado: dean1@titans.edu", exception.getMessage());
        verify(deanRepository, times(1)).existsByEmail("dean1@titans.edu");
        verify(deanRepository, never()).save(any(Dean.class));
    }

    @Test
    @DisplayName("Caso exitoso - getDeanById retorna decano existente")
    void testGetDeanById_Exitoso() {
        when(deanRepository.findById("1")).thenReturn(Optional.of(dean1));

        Dean result = deanService.getDeanById("1");

        assertAll("Verificar obtención por ID exitosa",
                () -> assertNotNull(result),
                () -> assertEquals("1", result.getId()),
                () -> assertEquals("Dean One", result.getName())
        );

        verify(deanRepository, times(1)).findById("1");
    }

    @Test
    @DisplayName("Caso error - getDeanById lanza excepción cuando no existe")
    void testGetDeanById_NoEncontrado() {
        when(deanRepository.findById("99")).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            deanService.getDeanById("99");
        });

        assertEquals("Decano no encontrado con ID: 99", exception.getMessage());
        verify(deanRepository, times(1)).findById("99");
    }

    @Test
    @DisplayName("Caso exitoso - getAllDeans retorna lista de decanos")
    void testGetAllDeans_Exitoso() {
        List<Dean> deans = Arrays.asList(dean1, dean2);
        when(deanRepository.findAll()).thenReturn(deans);

        List<Dean> result = deanService.getAllDeans();

        assertAll("Verificar obtención de todos los decanos",
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size()),
                () -> assertEquals("Dean One", result.get(0).getName())
        );

        verify(deanRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Caso borde - getAllDeans retorna lista vacía")
    void testGetAllDeans_ListaVacia() {
        when(deanRepository.findAll()).thenReturn(Collections.emptyList());

        List<Dean> result = deanService.getAllDeans();

        assertTrue(result.isEmpty());
        verify(deanRepository, times(1)).findAll();
    }


    @Test
    @DisplayName("Caso error - updateDean lanza excepción cuando no existe")
    void testUpdateDean_NoEncontrado() {
        when(deanRepository.findById("99")).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            deanService.updateDean("99", dean1);
        });

        assertEquals("Decano no encontrado con ID: 99", exception.getMessage());
        verify(deanRepository, times(1)).findById("99");
        verify(deanRepository, never()).save(any(Dean.class));
    }

    @Test
    @DisplayName("Caso error - updateDean lanza excepción cuando email existe en otro decano")
    void testUpdateDean_EmailEnUso() {
        Dean existingDean = new Dean();
        existingDean.setId("2");
        existingDean.setEmail("otro@titans.edu");

        when(deanRepository.findById("1")).thenReturn(Optional.of(dean1));
        when(deanRepository.findByEmail("nuevo@titans.edu")).thenReturn(Arrays.asList(existingDean));

        Dean updatedDean = new Dean();
        updatedDean.setEmail("nuevo@titans.edu");

        AppException exception = assertThrows(AppException.class, () -> {
            deanService.updateDean("1", updatedDean);
        });

        assertEquals("El email ya está en uso por otro decano: nuevo@titans.edu", exception.getMessage());
        verify(deanRepository, times(1)).findById("1");
        verify(deanRepository, times(1)).findByEmail("nuevo@titans.edu");
        verify(deanRepository, never()).save(any(Dean.class));
    }

    @Test
    @DisplayName("Caso exitoso - deleteDean elimina decano correctamente")
    void testDeleteDean_Exitoso() {
        when(deanRepository.existsById("1")).thenReturn(true);
        doNothing().when(deanRepository).deleteById("1");

        assertDoesNotThrow(() -> deanService.deleteDean("1"));

        verify(deanRepository, times(1)).existsById("1");
        verify(deanRepository, times(1)).deleteById("1");
    }

    @Test
    @DisplayName("Caso error - deleteDean lanza excepción cuando no existe")
    void testDeleteDean_NoEncontrado() {
        when(deanRepository.existsById("99")).thenReturn(false);

        AppException exception = assertThrows(AppException.class, () -> {
            deanService.deleteDean("99");
        });

        assertEquals("Decano no encontrado con ID: 99", exception.getMessage());
        verify(deanRepository, times(1)).existsById("99");
        verify(deanRepository, never()).deleteById(anyString());
    }

    @Test
    @DisplayName("Caso exitoso - findByFaculty retorna decanos de la facultad")
    void testFindByFaculty_Exitoso() {
        List<Dean> deans = Arrays.asList(dean1);
        when(deanRepository.findByFaculty("Engineering")).thenReturn(deans);

        List<Dean> result = deanService.findByFaculty("Engineering");

        assertAll("Verificar búsqueda por facultad",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("Engineering", result.get(0).getFaculty())
        );

        verify(deanRepository, times(1)).findByFaculty("Engineering");
    }

    @Test
    @DisplayName("Caso borde - findByFaculty retorna lista vacía para facultad sin decanos")
    void testFindByFaculty_FacultadSinDecanos() {
        when(deanRepository.findByFaculty("Law")).thenReturn(Collections.emptyList());

        List<Dean> result = deanService.findByFaculty("Law");

        assertTrue(result.isEmpty());
        verify(deanRepository, times(1)).findByFaculty("Law");
    }

    @Test
    @DisplayName("Caso exitoso - findByOfficeLocation retorna decanos de la ubicación")
    void testFindByOfficeLocation_Exitoso() {
        List<Dean> deans = Arrays.asList(dean1);
        when(deanRepository.findByOfficeLocation("Building A")).thenReturn(deans);

        List<Dean> result = deanService.findByOfficeLocation("Building A");

        assertAll("Verificar búsqueda por ubicación de oficina",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("Building A", result.get(0).getOfficeLocation())
        );

        verify(deanRepository, times(1)).findByOfficeLocation("Building A");
    }

    @Test
    @DisplayName("Caso exitoso - existsById retorna true cuando decano existe")
    void testExistsById_Exitoso() {
        when(deanRepository.existsById("1")).thenReturn(true);

        boolean result = deanService.existsById("1");

        assertTrue(result);
        verify(deanRepository, times(1)).existsById("1");
    }

    @Test
    @DisplayName("Caso error - existsById retorna false cuando decano no existe")
    void testExistsById_NoExiste() {
        when(deanRepository.existsById("99")).thenReturn(false);

        boolean result = deanService.existsById("99");

        assertFalse(result);
        verify(deanRepository, times(1)).existsById("99");
    }

    @Test
    @DisplayName("Caso exitoso - getDeanByFaculty retorna decano de la facultad")
    void testGetDeanByFaculty_Exitoso() {
        List<Dean> deans = Arrays.asList(dean1);
        when(deanRepository.findByFaculty("Engineering")).thenReturn(deans);

        Dean result = deanService.getDeanByFaculty("Engineering");

        assertAll("Verificar obtención por facultad",
                () -> assertNotNull(result),
                () -> assertEquals("Engineering", result.getFaculty())
        );

        verify(deanRepository, times(1)).findByFaculty("Engineering");
    }

    @Test
    @DisplayName("Caso error - getDeanByFaculty lanza excepción cuando no hay decano para la facultad")
    void testGetDeanByFaculty_NoEncontrado() {
        when(deanRepository.findByFaculty("Law")).thenReturn(Collections.emptyList());

        AppException exception = assertThrows(AppException.class, () -> {
            deanService.getDeanByFaculty("Law");
        });

        assertEquals("No se encontró decano para la facultad: Law", exception.getMessage());
        verify(deanRepository, times(1)).findByFaculty("Law");
    }

    @Test
    @DisplayName("Caso exitoso - findByFacultyAndOfficeLocation retorna decanos")
    void testFindByFacultyAndOfficeLocation_Exitoso() {
        List<Dean> deans = Arrays.asList(dean1);
        when(deanRepository.findByFacultyAndOfficeLocation("Engineering", "Building A")).thenReturn(deans);

        List<Dean> result = deanService.findByFacultyAndOfficeLocation("Engineering", "Building A");

        assertNotNull(result);
        verify(deanRepository, times(1)).findByFacultyAndOfficeLocation("Engineering", "Building A");
    }

    @Test
    @DisplayName("Caso exitoso - findByFacultyContaining retorna decanos")
    void testFindByFacultyContaining_Exitoso() {
        List<Dean> deans = Arrays.asList(dean1);
        when(deanRepository.findByFacultyContainingIgnoreCase("Eng")).thenReturn(deans);

        List<Dean> result = deanService.findByFacultyContaining("Eng");

        assertNotNull(result);
        verify(deanRepository, times(1)).findByFacultyContainingIgnoreCase("Eng");
    }

    @Test
    @DisplayName("Caso exitoso - findByOfficeLocationContaining retorna decanos")
    void testFindByOfficeLocationContaining_Exitoso() {
        List<Dean> deans = Arrays.asList(dean1);
        when(deanRepository.findByOfficeLocationContainingIgnoreCase("Building")).thenReturn(deans);

        List<Dean> result = deanService.findByOfficeLocationContaining("Building");

        assertNotNull(result);
        verify(deanRepository, times(1)).findByOfficeLocationContainingIgnoreCase("Building");
    }

    @Test
    @DisplayName("Caso exitoso - findByNameContaining retorna decanos")
    void testFindByNameContaining_Exitoso() {
        List<Dean> deans = Arrays.asList(dean1);
        when(deanRepository.findByNameContainingIgnoreCase("Dean")).thenReturn(deans);

        List<Dean> result = deanService.findByNameContaining("Dean");

        assertNotNull(result);
        verify(deanRepository, times(1)).findByNameContainingIgnoreCase("Dean");
    }

    @Test
    @DisplayName("Caso exitoso - findActiveDeans retorna decanos activos")
    void testFindActiveDeans_Exitoso() {
        List<Dean> deans = Arrays.asList(dean1, dean2);
        when(deanRepository.findByActive(true)).thenReturn(deans);

        List<Dean> result = deanService.findActiveDeans();

        assertAll("Verificar decanos activos",
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size())
        );

        verify(deanRepository, times(1)).findByActive(true);
    }

    @Test
    @DisplayName("Caso exitoso - countByFaculty retorna conteo correcto")
    void testCountByFaculty_Exitoso() {
        when(deanRepository.countByFaculty("Engineering")).thenReturn(2L);

        long result = deanService.countByFaculty("Engineering");

        assertEquals(2L, result);
        verify(deanRepository, times(1)).countByFaculty("Engineering");
    }

    @Test
    @DisplayName("Caso exitoso - countByOfficeLocation retorna conteo correcto")
    void testCountByOfficeLocation_Exitoso() {
        when(deanRepository.countByOfficeLocation("Building A")).thenReturn(1L);

        long result = deanService.countByOfficeLocation("Building A");

        assertEquals(1L, result);
        verify(deanRepository, times(1)).countByOfficeLocation("Building A");
    }

    @Test
    @DisplayName("Caso exitoso - findByFaculties retorna decanos")
    void testFindByFaculties_Exitoso() {
        List<String> faculties = Arrays.asList("Engineering", "Medicine");
        List<Dean> deans = Arrays.asList(dean1, dean2);
        when(deanRepository.findByFacultyIn(faculties)).thenReturn(deans);

        List<Dean> result = deanService.findByFaculties(faculties);

        assertAll("Verificar búsqueda por múltiples facultades",
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size())
        );

        verify(deanRepository, times(1)).findByFacultyIn(faculties);
    }

    @Test
    @DisplayName("Caso borde - updateDean con mismo email no valida duplicado")
    void testUpdateDean_MismoEmail() {
        Dean updatedDean = new Dean();
        updatedDean.setName("Dean Actualizado");
        updatedDean.setEmail("dean1@titans.edu");
        updatedDean.setFaculty("Engineering Updated");
        updatedDean.setOfficeLocation("Building A Updated");

        when(deanRepository.findById("1")).thenReturn(Optional.of(dean1));
        when(deanRepository.save(any(Dean.class))).thenReturn(updatedDean);

        Dean result = deanService.updateDean("1", updatedDean);

        assertNotNull(result);
        verify(deanRepository, times(1)).findById("1");
        verify(deanRepository, never()).findByEmail("dean1@titans.edu");
        verify(deanRepository, times(1)).save(any(Dean.class));
    }

    @Test
    @DisplayName("Caso borde - findByFaculty con null")
    void testFindByFaculty_Null() {
        when(deanRepository.findByFaculty(null)).thenReturn(Collections.emptyList());

        List<Dean> result = deanService.findByFaculty(null);

        assertTrue(result.isEmpty());
        verify(deanRepository, times(1)).findByFaculty(null);
    }

    @Test
    @DisplayName("Caso borde - findByFaculties con lista vacía")
    void testFindByFaculties_ListaVacia() {
        when(deanRepository.findByFacultyIn(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<Dean> result = deanService.findByFaculties(Collections.emptyList());

        assertTrue(result.isEmpty());
        verify(deanRepository, times(1)).findByFacultyIn(Collections.emptyList());
    }

    @Test
    @DisplayName("Caso borde - getDeanByFaculty con múltiples decanos retorna el primero")
    void testGetDeanByFaculty_MultiplesDecanos() {
        List<Dean> multipleDeans = Arrays.asList(dean1, dean2);
        when(deanRepository.findByFaculty("Engineering")).thenReturn(multipleDeans);

        Dean result = deanService.getDeanByFaculty("Engineering");

        assertNotNull(result);
        assertEquals("Dean One", result.getName());
        verify(deanRepository, times(1)).findByFaculty("Engineering");
    }
}
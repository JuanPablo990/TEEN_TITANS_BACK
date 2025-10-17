package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Dean;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.UserRole;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.DeanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para DeanService
 */
@ExtendWith(MockitoExtension.class)
public class DeanServiceTest {

    @Mock
    private DeanRepository deanRepository;

    @InjectMocks
    private DeanService deanService;

    private Dean dean1;
    private Dean dean2;
    private Dean dean3;

    @BeforeEach
    void setUp() {
        dean1 = new Dean("DEAN001", "Dr. Carlos Rodríguez", "carlos.rodriguez@titans.edu",
                "pass123", "Ingeniería", "Edificio A - Oficina 301");

        dean2 = new Dean("DEAN002", "Dra. Ana Martínez", "ana.martinez@titans.edu",
                "pass456", "Medicina", "Edificio B - Oficina 201");

        dean3 = new Dean("DEAN003", "Dr. Luis García", "luis.garcia@titans.edu",
                "pass789", "Ciencias", "Edificio A - Oficina 302");
    }

    @Test
    @DisplayName("Caso exitoso - createDean crea decano correctamente")
    void testCreateDean_Exitoso() {
        when(deanRepository.save(any(Dean.class)))
                .thenReturn(dean1);

        Dean resultado = deanService.createDean(dean1);

        assertNotNull(resultado);
        assertEquals("DEAN001", resultado.getId());
        assertEquals("Dr. Carlos Rodríguez", resultado.getName());
        assertEquals("Ingeniería", resultado.getFaculty());
        assertEquals("Edificio A - Oficina 301", resultado.getOfficeLocation());
        assertEquals(UserRole.DEAN, resultado.getRole());

        verify(deanRepository, times(1)).save(dean1);
    }

    @Test
    @DisplayName("Caso exitoso - getDeanById retorna decano existente")
    void testGetDeanById_Exitoso() {
        when(deanRepository.findById("DEAN001"))
                .thenReturn(Optional.of(dean1));

        Dean resultado = deanService.getDeanById("DEAN001");

        assertNotNull(resultado);
        assertEquals("DEAN001", resultado.getId());
        assertEquals("Dr. Carlos Rodríguez", resultado.getName());
        assertEquals("Ingeniería", resultado.getFaculty());

        verify(deanRepository, times(1)).findById("DEAN001");
    }

    @Test
    @DisplayName("Caso error - getDeanById con ID inexistente lanza excepción")
    void testGetDeanById_NoEncontrado() {
        when(deanRepository.findById("DEAN999"))
                .thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            deanService.getDeanById("DEAN999");
        });

        assertEquals("Decano no encontrado con ID: DEAN999", exception.getMessage());

        verify(deanRepository, times(1)).findById("DEAN999");
    }

    @Test
    @DisplayName("Caso exitoso - getAllDeans retorna todos los decanos")
    void testGetAllDeans_Exitoso() {
        when(deanRepository.findAll())
                .thenReturn(List.of(dean1, dean2, dean3));

        List<Dean> resultado = deanService.getAllDeans();

        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        assertEquals("DEAN001", resultado.get(0).getId());
        assertEquals("DEAN002", resultado.get(1).getId());
        assertEquals("DEAN003", resultado.get(2).getId());

        verify(deanRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Caso exitoso - getAllDeans retorna lista vacía cuando no hay decanos")
    void testGetAllDeans_ListaVacia() {
        when(deanRepository.findAll())
                .thenReturn(List.of());

        List<Dean> resultado = deanService.getAllDeans();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(deanRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Caso exitoso - updateDean actualiza decano existente")
    void testUpdateDean_Exitoso() {
        Dean deanActualizado = new Dean("DEAN001", "Dr. Carlos Rodríguez Actualizado",
                "carlos.actualizado@titans.edu", "newpass123",
                "Ingeniería Actualizada", "Edificio C - Oficina 401");

        when(deanRepository.findById("DEAN001"))
                .thenReturn(Optional.of(dean1));
        when(deanRepository.save(any(Dean.class)))
                .thenReturn(deanActualizado);

        Dean resultado = deanService.updateDean("DEAN001", deanActualizado);

        assertNotNull(resultado);
        assertEquals("DEAN001", resultado.getId());
        assertEquals("Dr. Carlos Rodríguez Actualizado", resultado.getName());
        assertEquals("Ingeniería Actualizada", resultado.getFaculty());
        assertEquals("Edificio C - Oficina 401", resultado.getOfficeLocation());

        verify(deanRepository, times(1)).findById("DEAN001");
        verify(deanRepository, times(1)).save(deanActualizado);
    }

    @Test
    @DisplayName("Caso error - updateDean con ID inexistente lanza excepción")
    void testUpdateDean_NoEncontrado() {
        when(deanRepository.findById("DEAN999"))
                .thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            deanService.updateDean("DEAN999", dean1);
        });

        assertEquals("Decano no encontrado con ID: DEAN999", exception.getMessage());

        verify(deanRepository, times(1)).findById("DEAN999");
        verify(deanRepository, never()).save(any());
    }

    @Test
    @DisplayName("Caso exitoso - deleteDean elimina decano existente")
    void testDeleteDean_Exitoso() {
        when(deanRepository.findById("DEAN001"))
                .thenReturn(Optional.of(dean1));
        doNothing().when(deanRepository).deleteById("DEAN001");

        deanService.deleteDean("DEAN001");

        assertDoesNotThrow(() -> deanService.deleteDean("DEAN001"));

        verify(deanRepository, times(2)).findById("DEAN001");
        verify(deanRepository, times(2)).deleteById("DEAN001");
    }

    @Test
    @DisplayName("Caso error - deleteDean con ID inexistente lanza excepción")
    void testDeleteDean_NoEncontrado() {
        when(deanRepository.findById("DEAN999"))
                .thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            deanService.deleteDean("DEAN999");
        });

        assertEquals("Decano no encontrado con ID: DEAN999", exception.getMessage());

        verify(deanRepository, times(1)).findById("DEAN999");
        verify(deanRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Caso exitoso - findByFaculty retorna decano de la facultad")
    void testFindByFaculty_Exitoso() {
        when(deanRepository.findByFaculty("Ingeniería"))
                .thenReturn(Optional.of(dean1));

        List<Dean> resultado = deanService.findByFaculty("Ingeniería");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("DEAN001", resultado.get(0).getId());
        assertEquals("Ingeniería", resultado.get(0).getFaculty());

        verify(deanRepository, times(1)).findByFaculty("Ingeniería");
    }

    @Test
    @DisplayName("Caso error - findByFaculty retorna lista vacía para facultad sin decano")
    void testFindByFaculty_NoEncontrado() {
        when(deanRepository.findByFaculty("Artes"))
                .thenReturn(Optional.empty());

        List<Dean> resultado = deanService.findByFaculty("Artes");

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(deanRepository, times(1)).findByFaculty("Artes");
    }

    @Test
    @DisplayName("Caso exitoso - findByOfficeLocation retorna decanos en ubicación")
    void testFindByOfficeLocation_Exitoso() {
        when(deanRepository.findByOfficeLocation("Edificio A - Oficina 301"))
                .thenReturn(List.of(dean1));

        List<Dean> resultado = deanService.findByOfficeLocation("Edificio A - Oficina 301");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("DEAN001", resultado.get(0).getId());
        assertEquals("Edificio A - Oficina 301", resultado.get(0).getOfficeLocation());

        verify(deanRepository, times(1)).findByOfficeLocation("Edificio A - Oficina 301");
    }

    @Test
    @DisplayName("Caso exitoso - findByOfficeLocation retorna múltiples decanos en misma ubicación")
    void testFindByOfficeLocation_MultiplesDecanos() {
        when(deanRepository.findByOfficeLocation("Edificio A"))
                .thenReturn(List.of(dean1, dean3));

        List<Dean> resultado = deanService.findByOfficeLocation("Edificio A");

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().anyMatch(d -> d.getId().equals("DEAN001")));
        assertTrue(resultado.stream().anyMatch(d -> d.getId().equals("DEAN003")));

        verify(deanRepository, times(1)).findByOfficeLocation("Edificio A");
    }

    @Test
    @DisplayName("Caso error - findByOfficeLocation retorna lista vacía para ubicación sin decanos")
    void testFindByOfficeLocation_NoEncontrado() {
        when(deanRepository.findByOfficeLocation("Edificio Z"))
                .thenReturn(List.of());

        List<Dean> resultado = deanService.findByOfficeLocation("Edificio Z");

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(deanRepository, times(1)).findByOfficeLocation("Edificio Z");
    }

    @Test
    @DisplayName("Caso exitoso - existsById retorna true para ID existente")
    void testExistsById_Exitoso() {
        when(deanRepository.existsById("DEAN001"))
                .thenReturn(true);

        boolean resultado = deanService.existsById("DEAN001");

        assertTrue(resultado);

        verify(deanRepository, times(1)).existsById("DEAN001");
    }

    @Test
    @DisplayName("Caso error - existsById retorna false para ID inexistente")
    void testExistsById_NoExiste() {
        when(deanRepository.existsById("DEAN999"))
                .thenReturn(false);

        boolean resultado = deanService.existsById("DEAN999");

        assertFalse(resultado);

        verify(deanRepository, times(1)).existsById("DEAN999");
    }

    @Test
    @DisplayName("Caso exitoso - getDeanByFaculty retorna decano de facultad existente")
    void testGetDeanByFaculty_Exitoso() {
        when(deanRepository.findByFaculty("Ingeniería"))
                .thenReturn(Optional.of(dean1));

        Dean resultado = deanService.getDeanByFaculty("Ingeniería");

        assertNotNull(resultado);
        assertEquals("DEAN001", resultado.getId());
        assertEquals("Ingeniería", resultado.getFaculty());

        verify(deanRepository, times(1)).findByFaculty("Ingeniería");
    }

    @Test
    @DisplayName("Caso error - getDeanByFaculty con facultad sin decano lanza excepción")
    void testGetDeanByFaculty_NoEncontrado() {
        when(deanRepository.findByFaculty("Artes"))
                .thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            deanService.getDeanByFaculty("Artes");
        });

        assertEquals("No se encontró decano para la facultad: Artes", exception.getMessage());

        verify(deanRepository, times(1)).findByFaculty("Artes");
    }

    @Test
    @DisplayName("Caso borde - updateDean mantiene el mismo ID al actualizar")
    void testUpdateDean_MantieneId() {
        Dean deanConIdDiferente = new Dean("DEAN999", "Nuevo Nombre", "nuevo@titans.edu",
                "pass", "Nueva Facultad", "Nueva Ubicación");

        when(deanRepository.findById("DEAN001"))
                .thenReturn(Optional.of(dean1));
        when(deanRepository.save(any(Dean.class)))
                .thenAnswer(invocation -> {
                    Dean deanGuardado = invocation.getArgument(0);
                    assertEquals("DEAN001", deanGuardado.getId());
                    return deanGuardado;
                });

        deanService.updateDean("DEAN001", deanConIdDiferente);

        verify(deanRepository, times(1)).findById("DEAN001");
        verify(deanRepository, times(1)).save(any(Dean.class));
    }

    @Test
    @DisplayName("Caso borde - createDean con decano nulo")
    void testCreateDean_Nulo() {
        when(deanRepository.save(null))
                .thenThrow(new IllegalArgumentException("Dean no puede ser nulo"));

        assertThrows(IllegalArgumentException.class, () -> {
            deanService.createDean(null);
        });

        verify(deanRepository, times(1)).save(null);
    }

    @Test
    @DisplayName("Caso borde - findByFaculty con facultad nula")
    void testFindByFaculty_Nula() {
        when(deanRepository.findByFaculty(null))
                .thenReturn(Optional.empty());

        List<Dean> resultado = deanService.findByFaculty(null);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(deanRepository, times(1)).findByFaculty(null);
    }

    @Test
    @DisplayName("Caso borde - findByOfficeLocation con ubicación nula")
    void testFindByOfficeLocation_Nula() {
        when(deanRepository.findByOfficeLocation(null))
                .thenReturn(List.of());

        List<Dean> resultado = deanService.findByOfficeLocation(null);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(deanRepository, times(1)).findByOfficeLocation(null);
    }
}
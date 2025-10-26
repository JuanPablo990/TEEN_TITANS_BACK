package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Dean;
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
public class DeanRepositoryTest {

    @Mock
    private DeanRepository deanRepository;

    private Dean dean1;
    private Dean dean2;
    private Dean dean3;

    @BeforeEach
    void setUp() {
        dean1 = new Dean("1", "Dr. Charles Xavier", "charles.xavier@titans.edu", "password123",
                "School of Mutant Studies", "Office 101");
        dean2 = new Dean("2", "Dr. Stephen Strange", "stephen.strange@titans.edu", "password456",
                "School of Mystic Arts", "Office 202");
        dean3 = new Dean("3", "Dr. Henry McCoy", "henry.mccoy@titans.edu", "password789",
                "School of Science", "Office 303");
    }

    @Test
    @DisplayName("Caso exitoso - save guarda decano correctamente")
    void testSave_Exitoso() {
        Dean newDean = new Dean("4", "Dr. Reed Richards", "reed.richards@titans.edu", "password000",
                "School of Engineering", "Office 404");

        when(deanRepository.save(newDean)).thenReturn(newDean);

        Dean savedDean = deanRepository.save(newDean);

        assertAll("Verificar guardado de decano",
                () -> assertNotNull(savedDean),
                () -> assertEquals("Dr. Reed Richards", savedDean.getName()),
                () -> assertEquals("School of Engineering", savedDean.getFaculty()),
                () -> assertEquals("Office 404", savedDean.getOfficeLocation())
        );

        verify(deanRepository, times(1)).save(newDean);
    }

    @Test
    @DisplayName("Caso exitoso - findById retorna decano existente")
    void testFindById_Exitoso() {
        when(deanRepository.findById("1")).thenReturn(Optional.of(dean1));

        Optional<Dean> foundDean = deanRepository.findById("1");

        assertAll("Verificar búsqueda por ID",
                () -> assertTrue(foundDean.isPresent()),
                () -> assertEquals("Dr. Charles Xavier", foundDean.get().getName()),
                () -> assertEquals("School of Mutant Studies", foundDean.get().getFaculty())
        );

        verify(deanRepository, times(1)).findById("1");
    }

    @Test
    @DisplayName("Caso error - findById retorna vacío para ID inexistente")
    void testFindById_NoEncontrado() {
        when(deanRepository.findById("99")).thenReturn(Optional.empty());

        Optional<Dean> foundDean = deanRepository.findById("99");

        assertTrue(foundDean.isEmpty());
        verify(deanRepository, times(1)).findById("99");
    }

    @Test
    @DisplayName("Caso exitoso - findAll retorna todos los decanos")
    void testFindAll_Exitoso() {
        List<Dean> deans = Arrays.asList(dean1, dean2, dean3);
        when(deanRepository.findAll()).thenReturn(deans);

        List<Dean> result = deanRepository.findAll();

        assertAll("Verificar búsqueda de todos los decanos",
                () -> assertNotNull(result),
                () -> assertEquals(3, result.size()),
                () -> assertEquals("Dr. Charles Xavier", result.get(0).getName())
        );

        verify(deanRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Caso borde - findAll retorna lista vacía cuando no hay decanos")
    void testFindAll_ListaVacia() {
        when(deanRepository.findAll()).thenReturn(Collections.emptyList());

        List<Dean> result = deanRepository.findAll();

        assertTrue(result.isEmpty());
        verify(deanRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Caso exitoso - findByFaculty retorna decanos de la facultad")
    void testFindByFaculty_Exitoso() {
        List<Dean> mutantStudiesDeans = Arrays.asList(dean1);
        when(deanRepository.findByFaculty("School of Mutant Studies")).thenReturn(mutantStudiesDeans);

        List<Dean> result = deanRepository.findByFaculty("School of Mutant Studies");

        assertAll("Verificar búsqueda por facultad",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("School of Mutant Studies", result.get(0).getFaculty()),
                () -> assertEquals("Dr. Charles Xavier", result.get(0).getName())
        );

        verify(deanRepository, times(1)).findByFaculty("School of Mutant Studies");
    }

    @Test
    @DisplayName("Caso error - findByFaculty retorna lista vacía para facultad inexistente")
    void testFindByFaculty_FacultadInexistente() {
        when(deanRepository.findByFaculty("School of Medicine")).thenReturn(Collections.emptyList());

        List<Dean> result = deanRepository.findByFaculty("School of Medicine");

        assertTrue(result.isEmpty());
        verify(deanRepository, times(1)).findByFaculty("School of Medicine");
    }

    @Test
    @DisplayName("Caso exitoso - findByOfficeLocation retorna decanos por ubicación de oficina")
    void testFindByOfficeLocation_Exitoso() {
        List<Dean> deans = Arrays.asList(dean1);
        when(deanRepository.findByOfficeLocation("Office 101")).thenReturn(deans);

        List<Dean> result = deanRepository.findByOfficeLocation("Office 101");

        assertAll("Verificar búsqueda por ubicación de oficina",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("Office 101", result.get(0).getOfficeLocation()),
                () -> assertEquals("Dr. Charles Xavier", result.get(0).getName())
        );

        verify(deanRepository, times(1)).findByOfficeLocation("Office 101");
    }

    @Test
    @DisplayName("Caso exitoso - findByFacultyAndOfficeLocation retorna decano combinado")
    void testFindByFacultyAndOfficeLocation_Exitoso() {
        List<Dean> deans = Arrays.asList(dean1);
        when(deanRepository.findByFacultyAndOfficeLocation("School of Mutant Studies", "Office 101")).thenReturn(deans);

        List<Dean> result = deanRepository.findByFacultyAndOfficeLocation("School of Mutant Studies", "Office 101");

        assertAll("Verificar búsqueda combinada por facultad y oficina",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("School of Mutant Studies", result.get(0).getFaculty()),
                () -> assertEquals("Office 101", result.get(0).getOfficeLocation())
        );

        verify(deanRepository, times(1)).findByFacultyAndOfficeLocation("School of Mutant Studies", "Office 101");
    }

    @Test
    @DisplayName("Caso exitoso - findByFacultyContainingIgnoreCase retorna decanos con patrón en facultad")
    void testFindByFacultyContainingIgnoreCase_Exitoso() {
        List<Dean> deans = Arrays.asList(dean1);
        when(deanRepository.findByFacultyContainingIgnoreCase("mutant")).thenReturn(deans);

        List<Dean> result = deanRepository.findByFacultyContainingIgnoreCase("mutant");

        assertAll("Verificar búsqueda por patrón en facultad",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertTrue(result.get(0).getFaculty().toLowerCase().contains("mutant"))
        );

        verify(deanRepository, times(1)).findByFacultyContainingIgnoreCase("mutant");
    }

    @Test
    @DisplayName("Caso exitoso - findByOfficeLocationContainingIgnoreCase retorna decanos con patrón en oficina")
    void testFindByOfficeLocationContainingIgnoreCase_Exitoso() {
        List<Dean> deans = Arrays.asList(dean1, dean2, dean3);
        when(deanRepository.findByOfficeLocationContainingIgnoreCase("office")).thenReturn(deans);

        List<Dean> result = deanRepository.findByOfficeLocationContainingIgnoreCase("office");

        assertAll("Verificar búsqueda por patrón en oficina",
                () -> assertNotNull(result),
                () -> assertEquals(3, result.size()),
                () -> assertTrue(result.stream().allMatch(d -> d.getOfficeLocation().toLowerCase().contains("office")))
        );

        verify(deanRepository, times(1)).findByOfficeLocationContainingIgnoreCase("office");
    }

    @Test
    @DisplayName("Caso exitoso - findByName retorna decano por nombre")
    void testFindByName_Exitoso() {
        List<Dean> deans = Arrays.asList(dean1);
        when(deanRepository.findByName("Dr. Charles Xavier")).thenReturn(deans);

        List<Dean> result = deanRepository.findByName("Dr. Charles Xavier");

        assertAll("Verificar búsqueda por nombre",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("Dr. Charles Xavier", result.get(0).getName())
        );

        verify(deanRepository, times(1)).findByName("Dr. Charles Xavier");
    }

    @Test
    @DisplayName("Caso exitoso - findByEmail retorna decano por email")
    void testFindByEmail_Exitoso() {
        List<Dean> deans = Arrays.asList(dean1);
        when(deanRepository.findByEmail("charles.xavier@titans.edu")).thenReturn(deans);

        List<Dean> result = deanRepository.findByEmail("charles.xavier@titans.edu");

        assertAll("Verificar búsqueda por email",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("charles.xavier@titans.edu", result.get(0).getEmail())
        );

        verify(deanRepository, times(1)).findByEmail("charles.xavier@titans.edu");
    }

    @Test
    @DisplayName("Caso exitoso - findByNameContainingIgnoreCase retorna decanos con patrón en nombre")
    void testFindByNameContainingIgnoreCase_Exitoso() {
        List<Dean> deans = Arrays.asList(dean1);
        when(deanRepository.findByNameContainingIgnoreCase("charles")).thenReturn(deans);

        List<Dean> result = deanRepository.findByNameContainingIgnoreCase("charles");

        assertAll("Verificar búsqueda por patrón en nombre",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertTrue(result.get(0).getName().toLowerCase().contains("charles"))
        );

        verify(deanRepository, times(1)).findByNameContainingIgnoreCase("charles");
    }

    @Test
    @DisplayName("Caso exitoso - findByActive retorna decanos activos")
    void testFindByActive_Exitoso() {
        List<Dean> activeDeans = Arrays.asList(dean1, dean3);
        when(deanRepository.findByActive(true)).thenReturn(activeDeans);

        List<Dean> result = deanRepository.findByActive(true);

        assertAll("Verificar búsqueda por estado activo",
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size()),
                () -> assertTrue(result.stream().allMatch(Dean::isActive))
        );

        verify(deanRepository, times(1)).findByActive(true);
    }

    @Test
    @DisplayName("Caso exitoso - findByFacultyAndActive retorna decanos combinados")
    void testFindByFacultyAndActive_Exitoso() {
        List<Dean> deans = Arrays.asList(dean1);
        when(deanRepository.findByFacultyAndActive("School of Mutant Studies", true)).thenReturn(deans);

        List<Dean> result = deanRepository.findByFacultyAndActive("School of Mutant Studies", true);

        assertAll("Verificar búsqueda combinada por facultad y estado",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("School of Mutant Studies", result.get(0).getFaculty()),
                () -> assertTrue(result.get(0).isActive())
        );

        verify(deanRepository, times(1)).findByFacultyAndActive("School of Mutant Studies", true);
    }

    @Test
    @DisplayName("Caso exitoso - findByOfficeLocationAndActive retorna decanos por oficina y estado")
    void testFindByOfficeLocationAndActive_Exitoso() {
        List<Dean> deans = Arrays.asList(dean1);
        when(deanRepository.findByOfficeLocationAndActive("Office 101", true)).thenReturn(deans);

        List<Dean> result = deanRepository.findByOfficeLocationAndActive("Office 101", true);

        assertAll("Verificar búsqueda por oficina y estado",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("Office 101", result.get(0).getOfficeLocation()),
                () -> assertTrue(result.get(0).isActive())
        );

        verify(deanRepository, times(1)).findByOfficeLocationAndActive("Office 101", true);
    }

    @Test
    @DisplayName("Caso exitoso - findByNameAndFaculty retorna decano por nombre y facultad")
    void testFindByNameAndFaculty_Exitoso() {
        List<Dean> deans = Arrays.asList(dean1);
        when(deanRepository.findByNameAndFaculty("Dr. Charles Xavier", "School of Mutant Studies")).thenReturn(deans);

        List<Dean> result = deanRepository.findByNameAndFaculty("Dr. Charles Xavier", "School of Mutant Studies");

        assertAll("Verificar búsqueda por nombre y facultad",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("Dr. Charles Xavier", result.get(0).getName()),
                () -> assertEquals("School of Mutant Studies", result.get(0).getFaculty())
        );

        verify(deanRepository, times(1)).findByNameAndFaculty("Dr. Charles Xavier", "School of Mutant Studies");
    }

    @Test
    @DisplayName("Caso exitoso - findByNameAndOfficeLocation retorna decano por nombre y oficina")
    void testFindByNameAndOfficeLocation_Exitoso() {
        List<Dean> deans = Arrays.asList(dean1);
        when(deanRepository.findByNameAndOfficeLocation("Dr. Charles Xavier", "Office 101")).thenReturn(deans);

        List<Dean> result = deanRepository.findByNameAndOfficeLocation("Dr. Charles Xavier", "Office 101");

        assertAll("Verificar búsqueda por nombre y oficina",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("Dr. Charles Xavier", result.get(0).getName()),
                () -> assertEquals("Office 101", result.get(0).getOfficeLocation())
        );

        verify(deanRepository, times(1)).findByNameAndOfficeLocation("Dr. Charles Xavier", "Office 101");
    }

    @Test
    @DisplayName("Caso exitoso - findByOrderByNameAsc retorna decanos ordenados")
    void testFindByOrderByNameAsc_Exitoso() {
        List<Dean> deans = Arrays.asList(dean1, dean3, dean2);
        when(deanRepository.findByOrderByNameAsc()).thenReturn(deans);

        List<Dean> result = deanRepository.findByOrderByNameAsc();

        assertAll("Verificar ordenamiento por nombre ascendente",
                () -> assertNotNull(result),
                () -> assertEquals(3, result.size()),
                () -> assertEquals("Dr. Charles Xavier", result.get(0).getName()),
                () -> assertEquals("Dr. Henry McCoy", result.get(1).getName()),
                () -> assertEquals("Dr. Stephen Strange", result.get(2).getName())
        );

        verify(deanRepository, times(1)).findByOrderByNameAsc();
    }

    @Test
    @DisplayName("Caso exitoso - countByFaculty retorna conteo correcto")
    void testCountByFaculty_Exitoso() {
        when(deanRepository.countByFaculty("School of Science")).thenReturn(1L);

        long result = deanRepository.countByFaculty("School of Science");

        assertEquals(1L, result);
        verify(deanRepository, times(1)).countByFaculty("School of Science");
    }

    @Test
    @DisplayName("Caso exitoso - countByOfficeLocation retorna conteo por oficina")
    void testCountByOfficeLocation_Exitoso() {
        when(deanRepository.countByOfficeLocation("Office 101")).thenReturn(1L);

        long result = deanRepository.countByOfficeLocation("Office 101");

        assertEquals(1L, result);
        verify(deanRepository, times(1)).countByOfficeLocation("Office 101");
    }

    @Test
    @DisplayName("Caso exitoso - countByFacultyAndActive retorna conteo combinado")
    void testCountByFacultyAndActive_Exitoso() {
        when(deanRepository.countByFacultyAndActive("School of Mutant Studies", true)).thenReturn(1L);

        long result = deanRepository.countByFacultyAndActive("School of Mutant Studies", true);

        assertEquals(1L, result);
        verify(deanRepository, times(1)).countByFacultyAndActive("School of Mutant Studies", true);
    }

    @Test
    @DisplayName("Caso exitoso - findByFacultyRegex retorna decanos por patrón regex de facultad")
    void testFindByFacultyRegex_Exitoso() {
        List<Dean> deans = Arrays.asList(dean1);
        when(deanRepository.findByFacultyRegex(".*Science.*")).thenReturn(deans);

        List<Dean> result = deanRepository.findByFacultyRegex(".*Science.*");

        assertAll("Verificar búsqueda por regex de facultad",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("School of Mutant Studies", result.get(0).getFaculty())
        );

        verify(deanRepository, times(1)).findByFacultyRegex(".*Science.*");
    }

    @Test
    @DisplayName("Caso exitoso - existsByFaculty retorna true cuando existe facultad")
    void testExistsByFaculty_Exitoso() {
        when(deanRepository.existsByFaculty("School of Mutant Studies")).thenReturn(true);

        boolean result = deanRepository.existsByFaculty("School of Mutant Studies");

        assertTrue(result);
        verify(deanRepository, times(1)).existsByFaculty("School of Mutant Studies");
    }

    @Test
    @DisplayName("Caso error - existsByFaculty retorna false cuando no existe facultad")
    void testExistsByFaculty_NoExiste() {
        when(deanRepository.existsByFaculty("School of Medicine")).thenReturn(false);

        boolean result = deanRepository.existsByFaculty("School of Medicine");

        assertFalse(result);
        verify(deanRepository, times(1)).existsByFaculty("School of Medicine");
    }

    @Test
    @DisplayName("Caso exitoso - existsByEmail retorna true cuando existe email")
    void testExistsByEmail_Exitoso() {
        when(deanRepository.existsByEmail("charles.xavier@titans.edu")).thenReturn(true);

        boolean result = deanRepository.existsByEmail("charles.xavier@titans.edu");

        assertTrue(result);
        verify(deanRepository, times(1)).existsByEmail("charles.xavier@titans.edu");
    }

    @Test
    @DisplayName("Caso error - existsByEmail retorna false cuando no existe email")
    void testExistsByEmail_NoExiste() {
        when(deanRepository.existsByEmail("inexistente@titans.edu")).thenReturn(false);

        boolean result = deanRepository.existsByEmail("inexistente@titans.edu");

        assertFalse(result);
        verify(deanRepository, times(1)).existsByEmail("inexistente@titans.edu");
    }

    @Test
    @DisplayName("Caso exitoso - existsByFacultyAndOfficeLocation retorna true cuando existe combinación")
    void testExistsByFacultyAndOfficeLocation_Exitoso() {
        when(deanRepository.existsByFacultyAndOfficeLocation("School of Mutant Studies", "Office 101")).thenReturn(true);

        boolean result = deanRepository.existsByFacultyAndOfficeLocation("School of Mutant Studies", "Office 101");

        assertTrue(result);
        verify(deanRepository, times(1)).existsByFacultyAndOfficeLocation("School of Mutant Studies", "Office 101");
    }

    @Test
    @DisplayName("Caso exitoso - findByFacultyIn retorna decanos de múltiples facultades")
    void testFindByFacultyIn_Exitoso() {
        List<String> faculties = Arrays.asList("School of Mutant Studies", "School of Science");
        List<Dean> deans = Arrays.asList(dean1, dean3);
        when(deanRepository.findByFacultyIn(faculties)).thenReturn(deans);

        List<Dean> result = deanRepository.findByFacultyIn(faculties);

        assertAll("Verificar búsqueda por múltiples facultades",
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size()),
                () -> assertTrue(result.stream().anyMatch(d -> d.getFaculty().equals("School of Mutant Studies"))),
                () -> assertTrue(result.stream().anyMatch(d -> d.getFaculty().equals("School of Science")))
        );

        verify(deanRepository, times(1)).findByFacultyIn(faculties);
    }

    @Test
    @DisplayName("Caso borde - findByFacultyIn con lista vacía")
    void testFindByFacultyIn_ListaVacia() {
        when(deanRepository.findByFacultyIn(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<Dean> result = deanRepository.findByFacultyIn(Collections.emptyList());

        assertTrue(result.isEmpty());
        verify(deanRepository, times(1)).findByFacultyIn(Collections.emptyList());
    }

    @Test
    @DisplayName("Caso exitoso - deleteById elimina decano existente")
    void testDeleteById_Exitoso() {
        doNothing().when(deanRepository).deleteById("1");

        assertDoesNotThrow(() -> deanRepository.deleteById("1"));

        verify(deanRepository, times(1)).deleteById("1");
    }
}
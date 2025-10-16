package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Dean;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class DeanRepositoryTest {

    @Mock
    private DeanRepository deanRepository;

    private Dean activeDean;
    private Dean inactiveDean;

    @BeforeEach
    void setUp() {
        activeDean = new Dean(
                "D001",
                "Juan Pérez",
                "juan.perez@university.edu",
                "password123",
                "Ingeniería",
                "Edificio A, Oficina 101"
        );

        inactiveDean = new Dean(
                "D002",
                "María García",
                "maria.garcia@university.edu",
                "password456",
                "Ciencias",
                "Edificio B, Oficina 201"
        );
    }

    @Test
    void findByFaculty_HappyPath_ReturnsDean() {
        String faculty = "Ingeniería";
        when(deanRepository.findByFaculty(faculty)).thenReturn(Optional.of(activeDean));

        Optional<Dean> result = deanRepository.findByFaculty(faculty);

        assertTrue(result.isPresent());
        assertEquals(faculty, result.get().getFaculty());
        verify(deanRepository, times(1)).findByFaculty(faculty);
    }

    @Test
    void findByFaculty_HappyPath_DifferentFaculty() {
        String faculty = "Ciencias";
        when(deanRepository.findByFaculty(faculty)).thenReturn(Optional.of(inactiveDean));

        Optional<Dean> result = deanRepository.findByFaculty(faculty);

        assertTrue(result.isPresent());
        assertEquals(faculty, result.get().getFaculty());
        verify(deanRepository, times(1)).findByFaculty(faculty);
    }

    @Test
    void findByFaculty_Error_FacultyNotFound() {
        String faculty = "Facultad Inexistente";
        when(deanRepository.findByFaculty(faculty)).thenReturn(Optional.empty());

        Optional<Dean> result = deanRepository.findByFaculty(faculty);

        assertFalse(result.isPresent());
        verify(deanRepository, times(1)).findByFaculty(faculty);
    }

    @Test
    void findByFaculty_Error_NullParameter() {
        when(deanRepository.findByFaculty(null)).thenReturn(Optional.empty());

        Optional<Dean> result = deanRepository.findByFaculty(null);

        assertFalse(result.isPresent());
        verify(deanRepository, times(1)).findByFaculty(null);
    }

    @Test
    void findByOfficeLocation_HappyPath_ReturnsDeans() {
        String officeLocation = "Edificio A, Oficina 101";
        List<Dean> expectedDeans = Collections.singletonList(activeDean);
        when(deanRepository.findByOfficeLocation(officeLocation)).thenReturn(expectedDeans);

        List<Dean> result = deanRepository.findByOfficeLocation(officeLocation);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(officeLocation, result.get(0).getOfficeLocation());
        verify(deanRepository, times(1)).findByOfficeLocation(officeLocation);
    }

    @Test
    void findByOfficeLocation_HappyPath_MultipleDeans() {
        String officeLocation = "Edificio Central";

        Dean dean1 = new Dean("D003", "Carlos López", "carlos.lopez@university.edu",
                "password789", "Arquitectura", officeLocation);
        Dean dean2 = new Dean("D004", "Ana Martínez", "ana.martinez@university.edu",
                "password012", "Derecho", officeLocation);

        List<Dean> expectedDeans = Arrays.asList(dean1, dean2);
        when(deanRepository.findByOfficeLocation(officeLocation)).thenReturn(expectedDeans);

        List<Dean> result = deanRepository.findByOfficeLocation(officeLocation);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(officeLocation, result.get(0).getOfficeLocation());
        assertEquals(officeLocation, result.get(1).getOfficeLocation());
        verify(deanRepository, times(1)).findByOfficeLocation(officeLocation);
    }

    @Test
    void findByOfficeLocation_Error_NoDeansInLocation() {
        String officeLocation = "Ubicación Inexistente";
        when(deanRepository.findByOfficeLocation(officeLocation)).thenReturn(Collections.emptyList());

        List<Dean> result = deanRepository.findByOfficeLocation(officeLocation);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(deanRepository, times(1)).findByOfficeLocation(officeLocation);
    }

    @Test
    void findByOfficeLocation_Error_NullParameter() {
        when(deanRepository.findByOfficeLocation(null)).thenReturn(Collections.emptyList());

        List<Dean> result = deanRepository.findByOfficeLocation(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(deanRepository, times(1)).findByOfficeLocation(null);
    }

    @Test
    void existsByFaculty_HappyPath_FacultyExists() {
        String faculty = "Ingeniería";
        when(deanRepository.existsByFaculty(faculty)).thenReturn(true);

        boolean result = deanRepository.existsByFaculty(faculty);

        assertTrue(result);
        verify(deanRepository, times(1)).existsByFaculty(faculty);
    }

    @Test
    void existsByFaculty_HappyPath_DifferentFacultyExists() {
        String faculty = "Ciencias";
        when(deanRepository.existsByFaculty(faculty)).thenReturn(true);

        boolean result = deanRepository.existsByFaculty(faculty);

        assertTrue(result);
        verify(deanRepository, times(1)).existsByFaculty(faculty);
    }

    @Test
    void existsByFaculty_Error_FacultyNotExists() {
        String faculty = "Facultad Inexistente";
        when(deanRepository.existsByFaculty(faculty)).thenReturn(false);

        boolean result = deanRepository.existsByFaculty(faculty);

        assertFalse(result);
        verify(deanRepository, times(1)).existsByFaculty(faculty);
    }

    @Test
    void existsByFaculty_Error_NullParameter() {
        when(deanRepository.existsByFaculty(null)).thenReturn(false);

        boolean result = deanRepository.existsByFaculty(null);

        assertFalse(result);
        verify(deanRepository, times(1)).existsByFaculty(null);
    }

    @Test
    void findByActive_HappyPath_ActiveDeans() {
        List<Dean> activeDeans = Collections.singletonList(activeDean);
        when(deanRepository.findByActive(true)).thenReturn(activeDeans);

        List<Dean> result = deanRepository.findByActive(true);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(deanRepository, times(1)).findByActive(true);
    }

    @Test
    void findByActive_HappyPath_InactiveDeans() {
        List<Dean> inactiveDeans = Collections.singletonList(inactiveDean);
        when(deanRepository.findByActive(false)).thenReturn(inactiveDeans);

        List<Dean> result = deanRepository.findByActive(false);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(deanRepository, times(1)).findByActive(false);
    }

    @Test
    void findByActive_Error_NoActiveDeans() {
        when(deanRepository.findByActive(true)).thenReturn(Collections.emptyList());

        List<Dean> result = deanRepository.findByActive(true);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(deanRepository, times(1)).findByActive(true);
    }

    @Test
    void findByActive_Error_NoInactiveDeans() {
        when(deanRepository.findByActive(false)).thenReturn(Collections.emptyList());

        List<Dean> result = deanRepository.findByActive(false);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(deanRepository, times(1)).findByActive(false);
    }
}
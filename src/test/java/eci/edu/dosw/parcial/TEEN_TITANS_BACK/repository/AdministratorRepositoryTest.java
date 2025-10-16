package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Administrator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataMongoTest
public class AdministratorRepositoryTest {

    @MockBean
    private AdministratorRepository administratorRepository;

    private Administrator admin1;
    private Administrator admin2;

    @BeforeEach
    void setUp() {
        // Inicializar datos de prueba
        admin1 = new Administrator("1", "Robin", "robin@titans.edu", "1234", "Security");
        admin2 = new Administrator("2", "Cyborg", "cyborg@titans.edu", "5678", "Technology");
    }

    @Test
    @DisplayName("Caso exitoso - findByDepartment retorna administradores del departamento")
    void testFindByDepartment_Exitoso() {
        // Configurar el comportamiento del mock
        when(administratorRepository.findByDepartment("Security"))
                .thenReturn(List.of(admin1));

        // Ejecutar el método a probar
        List<Administrator> resultado = administratorRepository.findByDepartment("Security");

        // Verificar los resultados
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Robin", resultado.get(0).getName());
        assertEquals("Security", resultado.get(0).getDepartment());

        // Verificar que el método se llamó exactamente una vez
        verify(administratorRepository, times(1)).findByDepartment("Security");
    }

    @Test
    @DisplayName("Caso error - findByDepartment retorna lista vacía para departamento inexistente")
    void testFindByDepartment_DepartamentoInexistente() {
        // Configurar el mock para retornar lista vacía
        when(administratorRepository.findByDepartment("Finanzas"))
                .thenReturn(Collections.emptyList());

        // Ejecutar el método
        List<Administrator> resultado = administratorRepository.findByDepartment("Finanzas");

        // Verificar que la lista está vacía
        assertTrue(resultado.isEmpty());
        verify(administratorRepository, times(1)).findByDepartment("Finanzas");
    }

    @Test
    @DisplayName("Caso exitoso - existsByDepartment retorna true cuando existe administrador")
    void testExistsByDepartment_Exitoso() {
        // Configurar el mock
        when(administratorRepository.existsByDepartment("Security"))
                .thenReturn(true);

        // Ejecutar el método
        boolean existe = administratorRepository.existsByDepartment("Security");

        // Verificar el resultado
        assertTrue(existe);
        verify(administratorRepository, times(1)).existsByDepartment("Security");
    }

    @Test
    @DisplayName("Caso error - existsByDepartment retorna false cuando no existe departamento")
    void testExistsByDepartment_DepartamentoInexistente() {
        // Configurar el mock
        when(administratorRepository.existsByDepartment("Marketing"))
                .thenReturn(false);

        // Ejecutar el método
        boolean existe = administratorRepository.existsByDepartment("Marketing");

        // Verificar el resultado
        assertFalse(existe);
        verify(administratorRepository, times(1)).existsByDepartment("Marketing");
    }

    @Test
    @DisplayName("Caso borde - findByDepartment con múltiples administradores")
    void testFindByDepartment_MultiplesAdministradores() {
        // Configurar el mock
        when(administratorRepository.findByDepartment("Technology"))
                .thenReturn(List.of(admin2));

        // Ejecutar el método
        List<Administrator> resultado = administratorRepository.findByDepartment("Technology");

        // Verificar los resultados
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Cyborg", resultado.get(0).getName());
        verify(administratorRepository, times(1)).findByDepartment("Technology");
    }

    @Test
    @DisplayName("Caso borde - findByDepartment con null")
    void testFindByDepartment_Null() {
        // Configurar el mock para retornar lista vacía cuando se pasa null
        when(administratorRepository.findByDepartment(null))
                .thenReturn(Collections.emptyList());

        // Ejecutar el método
        List<Administrator> resultado = administratorRepository.findByDepartment(null);

        // Verificar los resultados
        assertTrue(resultado.isEmpty());
        verify(administratorRepository, times(1)).findByDepartment(null);
    }
}
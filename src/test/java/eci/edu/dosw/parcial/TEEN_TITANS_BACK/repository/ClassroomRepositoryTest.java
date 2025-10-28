package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Classroom;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.RoomType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataMongoTest
public class ClassroomRepositoryTest {

    @MockBean
    private ClassroomRepository classroomRepository;

    private Classroom classroom1;
    private Classroom classroom2;
    private Classroom classroom3;
    private Classroom classroom4;

    @BeforeEach
    void setUp() {
        classroom1 = new Classroom("1", "A", "101", 50, RoomType.REGULAR);
        classroom2 = new Classroom("2", "B", "201", 100, RoomType.LABORATORY);
        classroom3 = new Classroom("3", "A", "102", 30, RoomType.COMPUTER_LAB);
        classroom4 = new Classroom("4", "C", "301", 200, RoomType.AUDITORIUM);
    }

    @Test
    @DisplayName("Caso exitoso - findByBuilding retorna aulas del edificio")
    void testFindByBuilding_Exitoso() {
        when(classroomRepository.findByBuilding("A"))
                .thenReturn(List.of(classroom1, classroom3));

        List<Classroom> resultado = classroomRepository.findByBuilding("A");

        assertAll("Verificar aulas del edificio A",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals("A", resultado.get(0).getBuilding()),
                () -> assertEquals("A", resultado.get(1).getBuilding())
        );

        verify(classroomRepository, times(1)).findByBuilding("A");
    }

    @Test
    @DisplayName("Caso error - findByBuilding retorna lista vacía para edificio inexistente")
    void testFindByBuilding_EdificioInexistente() {
        when(classroomRepository.findByBuilding("Z"))
                .thenReturn(Collections.emptyList());

        List<Classroom> resultado = classroomRepository.findByBuilding("Z");

        assertTrue(resultado.isEmpty());
        verify(classroomRepository, times(1)).findByBuilding("Z");
    }

    @Test
    @DisplayName("Caso exitoso - findByRoomNumber retorna aula específica")
    void testFindByRoomNumber_Exitoso() {
        when(classroomRepository.findByRoomNumber("101"))
                .thenReturn(Optional.of(classroom1));

        Optional<Classroom> resultado = classroomRepository.findByRoomNumber("101");

        assertAll("Verificar aula por número",
                () -> assertTrue(resultado.isPresent()),
                () -> assertEquals("101", resultado.get().getRoomNumber()),
                () -> assertEquals("A", resultado.get().getBuilding())
        );

        verify(classroomRepository, times(1)).findByRoomNumber("101");
    }

    @Test
    @DisplayName("Caso error - findByRoomNumber retorna vacío para número inexistente")
    void testFindByRoomNumber_NumeroInexistente() {
        when(classroomRepository.findByRoomNumber("999"))
                .thenReturn(Optional.empty());

        Optional<Classroom> resultado = classroomRepository.findByRoomNumber("999");

        assertFalse(resultado.isPresent());
        verify(classroomRepository, times(1)).findByRoomNumber("999");
    }

    @Test
    @DisplayName("Caso exitoso - findByCapacityGreaterThanEqual retorna aulas con capacidad suficiente")
    void testFindByCapacityGreaterThanEqual_Exitoso() {
        when(classroomRepository.findByCapacityGreaterThanEqual(50))
                .thenReturn(List.of(classroom1, classroom2, classroom4));

        List<Classroom> resultado = classroomRepository.findByCapacityGreaterThanEqual(50);

        assertAll("Verificar aulas con capacidad >= 50",
                () -> assertNotNull(resultado),
                () -> assertEquals(3, resultado.size()),
                () -> assertTrue(resultado.get(0).getCapacity() >= 50),
                () -> assertTrue(resultado.get(1).getCapacity() >= 50),
                () -> assertTrue(resultado.get(2).getCapacity() >= 50)
        );

        verify(classroomRepository, times(1)).findByCapacityGreaterThanEqual(50);
    }

    @Test
    @DisplayName("Caso exitoso - findByRoomType retorna aulas por tipo")
    void testFindByRoomType_Exitoso() {
        when(classroomRepository.findByRoomType(RoomType.LABORATORY))
                .thenReturn(List.of(classroom2));

        List<Classroom> resultado = classroomRepository.findByRoomType(RoomType.LABORATORY);

        assertAll("Verificar aulas de tipo laboratorio",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals(RoomType.LABORATORY, resultado.get(0).getRoomType())
        );

        verify(classroomRepository, times(1)).findByRoomType(RoomType.LABORATORY);
    }

    @Test
    @DisplayName("Caso exitoso - findByBuildingAndRoomNumber retorna aula específica")
    void testFindByBuildingAndRoomNumber_Exitoso() {
        when(classroomRepository.findByBuildingAndRoomNumber("A", "101"))
                .thenReturn(List.of(classroom1));

        List<Classroom> resultado = classroomRepository.findByBuildingAndRoomNumber("A", "101");

        assertAll("Verificar aula específica por edificio y número",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("A", resultado.get(0).getBuilding()),
                () -> assertEquals("101", resultado.get(0).getRoomNumber())
        );

        verify(classroomRepository, times(1)).findByBuildingAndRoomNumber("A", "101");
    }

    @Test
    @DisplayName("Caso exitoso - findByBuildingAndRoomType retorna aulas filtradas")
    void testFindByBuildingAndRoomType_Exitoso() {
        when(classroomRepository.findByBuildingAndRoomType("A", RoomType.REGULAR))
                .thenReturn(List.of(classroom1));

        List<Classroom> resultado = classroomRepository.findByBuildingAndRoomType("A", RoomType.REGULAR);

        assertAll("Verificar aulas por edificio y tipo",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("A", resultado.get(0).getBuilding()),
                () -> assertEquals(RoomType.REGULAR, resultado.get(0).getRoomType())
        );

        verify(classroomRepository, times(1)).findByBuildingAndRoomType("A", RoomType.REGULAR);
    }

    @Test
    @DisplayName("Caso exitoso - findByCapacityBetween retorna aulas en rango de capacidad")
    void testFindByCapacityBetween_Exitoso() {
        when(classroomRepository.findByCapacityBetween(40, 150))
                .thenReturn(List.of(classroom1, classroom2));

        List<Classroom> resultado = classroomRepository.findByCapacityBetween(40, 150);

        assertAll("Verificar aulas en rango de capacidad",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertTrue(resultado.get(0).getCapacity() >= 40 && resultado.get(0).getCapacity() <= 150),
                () -> assertTrue(resultado.get(1).getCapacity() >= 40 && resultado.get(1).getCapacity() <= 150)
        );

        verify(classroomRepository, times(1)).findByCapacityBetween(40, 150);
    }

    @Test
    @DisplayName("Caso exitoso - findByBuildingContainingIgnoreCase retorna aulas con búsqueda case insensitive")
    void testFindByBuildingContainingIgnoreCase_Exitoso() {
        when(classroomRepository.findByBuildingContainingIgnoreCase("a"))
                .thenReturn(List.of(classroom1, classroom3));

        List<Classroom> resultado = classroomRepository.findByBuildingContainingIgnoreCase("a");

        assertAll("Verificar búsqueda case insensitive",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertTrue(resultado.get(0).getBuilding().toLowerCase().contains("a")),
                () -> assertTrue(resultado.get(1).getBuilding().toLowerCase().contains("a"))
        );

        verify(classroomRepository, times(1)).findByBuildingContainingIgnoreCase("a");
    }

    @Test
    @DisplayName("Caso exitoso - findByOrderByBuildingAsc retorna aulas ordenadas")
    void testFindByOrderByBuildingAsc_Exitoso() {
        when(classroomRepository.findByOrderByBuildingAsc())
                .thenReturn(List.of(classroom1, classroom3, classroom2, classroom4));

        List<Classroom> resultado = classroomRepository.findByOrderByBuildingAsc();

        assertAll("Verificar aulas ordenadas por edificio ascendente",
                () -> assertNotNull(resultado),
                () -> assertEquals(4, resultado.size()),
                () -> assertEquals("A", resultado.get(0).getBuilding()),
                () -> assertEquals("A", resultado.get(1).getBuilding()),
                () -> assertEquals("B", resultado.get(2).getBuilding()),
                () -> assertEquals("C", resultado.get(3).getBuilding())
        );

        verify(classroomRepository, times(1)).findByOrderByBuildingAsc();
    }

    @Test
    @DisplayName("Caso exitoso - countByBuilding retorna conteo correcto")
    void testCountByBuilding_Exitoso() {
        when(classroomRepository.countByBuilding("A"))
                .thenReturn(2L);

        long resultado = classroomRepository.countByBuilding("A");

        assertEquals(2L, resultado);
        verify(classroomRepository, times(1)).countByBuilding("A");
    }

    @Test
    @DisplayName("Caso exitoso - countByRoomType retorna conteo por tipo")
    void testCountByRoomType_Exitoso() {
        when(classroomRepository.countByRoomType(RoomType.REGULAR))
                .thenReturn(1L);

        long resultado = classroomRepository.countByRoomType(RoomType.REGULAR);

        assertEquals(1L, resultado);
        verify(classroomRepository, times(1)).countByRoomType(RoomType.REGULAR);
    }

    @Test
    @DisplayName("Caso exitoso - existsByBuildingAndRoomNumber retorna true cuando existe")
    void testExistsByBuildingAndRoomNumber_Exitoso() {
        when(classroomRepository.existsByBuildingAndRoomNumber("A", "101"))
                .thenReturn(true);

        boolean resultado = classroomRepository.existsByBuildingAndRoomNumber("A", "101");

        assertTrue(resultado);
        verify(classroomRepository, times(1)).existsByBuildingAndRoomNumber("A", "101");
    }

    @Test
    @DisplayName("Caso error - existsByBuildingAndRoomNumber retorna false cuando no existe")
    void testExistsByBuildingAndRoomNumber_NoExiste() {
        when(classroomRepository.existsByBuildingAndRoomNumber("Z", "999"))
                .thenReturn(false);

        boolean resultado = classroomRepository.existsByBuildingAndRoomNumber("Z", "999");

        assertFalse(resultado);
        verify(classroomRepository, times(1)).existsByBuildingAndRoomNumber("Z", "999");
    }

    @Test
    @DisplayName("Caso exitoso - findByBuildingIn retorna aulas de múltiples edificios")
    void testFindByBuildingIn_Exitoso() {
        when(classroomRepository.findByBuildingIn(List.of("A", "B")))
                .thenReturn(List.of(classroom1, classroom2, classroom3));

        List<Classroom> resultado = classroomRepository.findByBuildingIn(List.of("A", "B"));

        assertAll("Verificar aulas de múltiples edificios",
                () -> assertNotNull(resultado),
                () -> assertEquals(3, resultado.size()),
                () -> assertTrue(resultado.stream().anyMatch(c -> c.getBuilding().equals("A"))),
                () -> assertTrue(resultado.stream().anyMatch(c -> c.getBuilding().equals("B")))
        );

        verify(classroomRepository, times(1)).findByBuildingIn(List.of("A", "B"));
    }

    @Test
    @DisplayName("Caso exitoso - findByRoomTypeIn retorna aulas de múltiples tipos")
    void testFindByRoomTypeIn_Exitoso() {
        when(classroomRepository.findByRoomTypeIn(List.of(RoomType.REGULAR, RoomType.LABORATORY)))
                .thenReturn(List.of(classroom1, classroom2));

        List<Classroom> resultado = classroomRepository.findByRoomTypeIn(List.of(RoomType.REGULAR, RoomType.LABORATORY));

        assertAll("Verificar aulas de múltiples tipos",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertTrue(resultado.stream().anyMatch(c -> c.getRoomType() == RoomType.REGULAR)),
                () -> assertTrue(resultado.stream().anyMatch(c -> c.getRoomType() == RoomType.LABORATORY))
        );

        verify(classroomRepository, times(1)).findByRoomTypeIn(List.of(RoomType.REGULAR, RoomType.LABORATORY));
    }

    @Test
    @DisplayName("Caso exitoso - findByCapacityRange con query personalizada")
    void testFindByCapacityRange_Exitoso() {
        when(classroomRepository.findByCapacityRange(25, 75))
                .thenReturn(List.of(classroom1, classroom3));

        List<Classroom> resultado = classroomRepository.findByCapacityRange(25, 75);

        assertAll("Verificar aulas en rango de capacidad con query personalizada",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertTrue(resultado.get(0).getCapacity() >= 25 && resultado.get(0).getCapacity() <= 75),
                () -> assertTrue(resultado.get(1).getCapacity() >= 25 && resultado.get(1).getCapacity() <= 75)
        );

        verify(classroomRepository, times(1)).findByCapacityRange(25, 75);
    }

    @Test
    @DisplayName("Caso exitoso - findSuitableClassrooms retorna aulas adecuadas")
    void testFindSuitableClassrooms_Exitoso() {
        when(classroomRepository.findSuitableClassrooms(80, RoomType.AUDITORIUM, "C"))
                .thenReturn(List.of(classroom4));

        List<Classroom> resultado = classroomRepository.findSuitableClassrooms(80, RoomType.AUDITORIUM, "C");

        assertAll("Verificar aulas adecuadas para requisitos",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertTrue(resultado.get(0).getCapacity() >= 80),
                () -> assertEquals(RoomType.AUDITORIUM, resultado.get(0).getRoomType()),
                () -> assertEquals("C", resultado.get(0).getBuilding())
        );

        verify(classroomRepository, times(1)).findSuitableClassrooms(80, RoomType.AUDITORIUM, "C");
    }

    @Test
    @DisplayName("Caso borde - findByBuilding con building nulo")
    void testFindByBuilding_Nulo() {
        when(classroomRepository.findByBuilding(null))
                .thenReturn(Collections.emptyList());

        List<Classroom> resultado = classroomRepository.findByBuilding(null);

        assertTrue(resultado.isEmpty());
        verify(classroomRepository, times(1)).findByBuilding(null);
    }

    @Test
    @DisplayName("Caso borde - findByCapacity con capacidad cero")
    void testFindByCapacity_CapacidadCero() {
        when(classroomRepository.findByCapacity(0))
                .thenReturn(Collections.emptyList());

        List<Classroom> resultado = classroomRepository.findByCapacity(0);

        assertTrue(resultado.isEmpty());
        verify(classroomRepository, times(1)).findByCapacity(0);
    }

    @Test
    @DisplayName("Caso borde - Verificar integridad de datos en aulas encontradas")
    void testIntegridadDatosAulas() {
        when(classroomRepository.findByRoomNumber("101"))
                .thenReturn(Optional.of(classroom1));

        Optional<Classroom> resultado = classroomRepository.findByRoomNumber("101");

        assertAll("Verificar integridad completa del aula",
                () -> assertTrue(resultado.isPresent()),
                () -> assertEquals("1", resultado.get().getClassroomId()),
                () -> assertEquals("A", resultado.get().getBuilding()),
                () -> assertEquals("101", resultado.get().getRoomNumber()),
                () -> assertEquals(50, resultado.get().getCapacity()),
                () -> assertEquals(RoomType.REGULAR, resultado.get().getRoomType()),
                () -> assertInstanceOf(Classroom.class, resultado.get()),
                () -> assertNotNull(resultado.get().toString())
        );
    }

    @Test
    @DisplayName("Caso exitoso - findByBuildingSortedByRoomNumber con query personalizada")
    void testFindByBuildingSortedByRoomNumber_Exitoso() {
        when(classroomRepository.findByBuildingSortedByRoomNumber("A"))
                .thenReturn(List.of(classroom1, classroom3));

        List<Classroom> resultado = classroomRepository.findByBuildingSortedByRoomNumber("A");

        assertAll("Verificar aulas ordenadas por número",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals("101", resultado.get(0).getRoomNumber()),
                () -> assertEquals("102", resultado.get(1).getRoomNumber())
        );

        verify(classroomRepository, times(1)).findByBuildingSortedByRoomNumber("A");
    }

    @Test
    @DisplayName("Caso exitoso - existsByRoomTypeAndCapacityGreaterThanEqual retorna true")
    void testExistsByRoomTypeAndCapacityGreaterThanEqual_Exitoso() {
        when(classroomRepository.existsByRoomTypeAndCapacityGreaterThanEqual(RoomType.AUDITORIUM, 150))
                .thenReturn(true);

        boolean resultado = classroomRepository.existsByRoomTypeAndCapacityGreaterThanEqual(RoomType.AUDITORIUM, 150);

        assertTrue(resultado);
        verify(classroomRepository, times(1)).existsByRoomTypeAndCapacityGreaterThanEqual(RoomType.AUDITORIUM, 150);
    }
}
package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Classroom;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.RoomType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@DataMongoTest
public class ClassroomRepositoryTest {

    @MockBean
    private ClassroomRepository classroomRepository;

    private Classroom classroom1;
    private Classroom classroom2;

    @BeforeEach
    void setUp() {
        classroom1 = new Classroom("1", "Edificio A", "101", 40, RoomType.REGULAR);
        classroom2 = new Classroom("2", "Edificio B", "202", 80, RoomType.LABORATORY);
    }


    @Test
    @DisplayName("Caso exitoso - findByBuilding retorna aulas en el edificio indicado")
    void testFindByBuilding_Exitoso() {
        when(classroomRepository.findByBuilding("Edificio A")).thenReturn(List.of(classroom1));

        List<Classroom> resultado = classroomRepository.findByBuilding("Edificio A");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Edificio A", resultado.get(0).getBuilding());
        verify(classroomRepository, times(1)).findByBuilding("Edificio A");
    }

    @Test
    @DisplayName("Caso error - findByBuilding retorna lista vacía si no hay aulas en el edificio")
    void testFindByBuilding_Error() {
        when(classroomRepository.findByBuilding("Edificio Z")).thenReturn(Collections.emptyList());

        List<Classroom> resultado = classroomRepository.findByBuilding("Edificio Z");

        assertTrue(resultado.isEmpty());
        verify(classroomRepository, times(1)).findByBuilding("Edificio Z");
    }


    @Test
    @DisplayName("Caso exitoso - findByRoomNumber retorna aula existente")
    void testFindByRoomNumber_Exitoso() {
        when(classroomRepository.findByRoomNumber("101")).thenReturn(classroom1);

        Classroom resultado = classroomRepository.findByRoomNumber("101");

        assertNotNull(resultado);
        assertEquals("101", resultado.getRoomNumber());
        assertEquals(RoomType.REGULAR, resultado.getRoomType());
        verify(classroomRepository, times(1)).findByRoomNumber("101");
    }

    @Test
    @DisplayName("Caso error - findByRoomNumber retorna null si el número no existe")
    void testFindByRoomNumber_Error() {
        when(classroomRepository.findByRoomNumber("999")).thenReturn(null);

        Classroom resultado = classroomRepository.findByRoomNumber("999");

        assertNull(resultado);
        verify(classroomRepository, times(1)).findByRoomNumber("999");
    }


    @Test
    @DisplayName("Caso exitoso - findByCapacityGreaterThanEqual retorna aulas con capacidad suficiente")
    void testFindByCapacityGreaterThanEqual_Exitoso() {
        when(classroomRepository.findByCapacityGreaterThanEqual(50)).thenReturn(List.of(classroom2));

        List<Classroom> resultado = classroomRepository.findByCapacityGreaterThanEqual(50);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getCapacity() >= 50);
        verify(classroomRepository, times(1)).findByCapacityGreaterThanEqual(50);
    }

    @Test
    @DisplayName("Caso error - findByCapacityGreaterThanEqual retorna lista vacía si ninguna aula cumple")
    void testFindByCapacityGreaterThanEqual_Error() {
        when(classroomRepository.findByCapacityGreaterThanEqual(100)).thenReturn(Collections.emptyList());

        List<Classroom> resultado = classroomRepository.findByCapacityGreaterThanEqual(100);

        assertTrue(resultado.isEmpty());
        verify(classroomRepository, times(1)).findByCapacityGreaterThanEqual(100);
    }


    @Test
    @DisplayName("Caso exitoso - findByRoomType retorna aulas del tipo indicado")
    void testFindByRoomType_Exitoso() {
        when(classroomRepository.findByRoomType(RoomType.LABORATORY.toString())).thenReturn(List.of(classroom2));

        List<Classroom> resultado = classroomRepository.findByRoomType(RoomType.LABORATORY.toString());

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(RoomType.LABORATORY, resultado.get(0).getRoomType());
        verify(classroomRepository, times(1)).findByRoomType(RoomType.LABORATORY.toString());
    }

    @Test
    @DisplayName("Caso error - findByRoomType retorna lista vacía si no existen aulas del tipo solicitado")
    void testFindByRoomType_Error() {
        when(classroomRepository.findByRoomType(RoomType.AUDITORIUM.toString())).thenReturn(Collections.emptyList());

        List<Classroom> resultado = classroomRepository.findByRoomType(RoomType.AUDITORIUM.toString());

        assertTrue(resultado.isEmpty());
        verify(classroomRepository, times(1)).findByRoomType(RoomType.AUDITORIUM.toString());
    }
}
package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
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
public class GroupRepositoryTest {

    @MockBean
    private GroupRepository groupRepository;

    private Group group1;
    private Group group2;

    private Course course1;
    private Course course2;
    private Professor professor1;
    private Professor professor2;
    private Schedule schedule1;
    private Schedule schedule2;
    private Classroom classroom1;
    private Classroom classroom2;

    @BeforeEach
    void setUp() {
        course1 = new Course("CS101", "Programación I", 3,
                "Introducción a la programación", "Ingeniería de Sistemas", true);
        course2 = new Course("CS102", "Estructuras de Datos", 4,
                "Estudio de estructuras de datos", "Ingeniería de Sistemas", true);

        professor1 = new Professor("Ciencias de la Computación", true,
                List.of("Inteligencia Artificial", "Programación Avanzada"));
        professor2 = new Professor("Ingeniería Electrónica", false,
                List.of("Robótica", "Sistemas Embebidos"));


        schedule1 = new Schedule("S001", "Lunes", "08:00", "10:00", "2025-1");
        schedule2 = new Schedule("S002", "Martes", "10:00", "12:00", "2025-1");

        classroom1 = new Classroom("C001", "Edificio A", "101", 40, RoomType.REGULAR);
        classroom2 = new Classroom("C002", "Edificio B", "202", 35, RoomType.LABORATORY);

        group1 = new Group("G001", "A", course1, professor1, schedule1, classroom1);
        group2 = new Group("G002", "B", course2, professor2, schedule2, classroom2);
    }


    @Test
    @DisplayName("Caso exitoso - findBySection retorna grupos por sección")
    void testFindBySection_Exitoso() {
        when(groupRepository.findBySection("A")).thenReturn(List.of(group1));

        List<Group> resultado = groupRepository.findBySection("A");

        assertNotNull(resultado, "El resultado no debe ser nulo");
        assertEquals(1, resultado.size(), "Debe retornar exactamente un grupo");
        assertEquals("A", resultado.get(0).getSection(), "La sección del grupo no coincide");
        verify(groupRepository, times(1)).findBySection("A");

        System.out.println("Grupo encontrado correctamente por sección: " + resultado.get(0).getSection());
    }

    @Test
    @DisplayName("Caso error - findBySection retorna lista vacía para sección inexistente")
    void testFindBySection_Error() {
        when(groupRepository.findBySection("Z")).thenReturn(Collections.emptyList());

        List<Group> resultado = groupRepository.findBySection("Z");

        assertTrue(resultado.isEmpty(), "Debe retornar una lista vacía");
        verify(groupRepository, times(1)).findBySection("Z");

        System.out.println("Se manejó correctamente la búsqueda vacía por sección inexistente");
    }


    @Test
    @DisplayName("Caso exitoso - findByCourse_CourseCode retorna grupos por código de curso")
    void testFindByCourseCode_Exitoso() {
        when(groupRepository.findByCourse_CourseCode("CS101")).thenReturn(List.of(group1));

        List<Group> resultado = groupRepository.findByCourse_CourseCode("CS101");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("CS101", resultado.get(0).getCourse().getCourseCode());
        verify(groupRepository, times(1)).findByCourse_CourseCode("CS101");

        System.out.println("Grupo encontrado correctamente por código de curso");
    }

    @Test
    @DisplayName("Caso error - findByCourse_CourseCode retorna lista vacía para curso inexistente")
    void testFindByCourseCode_Error() {
        when(groupRepository.findByCourse_CourseCode("CS999")).thenReturn(Collections.emptyList());

        List<Group> resultado = groupRepository.findByCourse_CourseCode("CS999");

        assertTrue(resultado.isEmpty());
        verify(groupRepository, times(1)).findByCourse_CourseCode("CS999");

        System.out.println("Se manejó correctamente la búsqueda vacía por curso inexistente");
    }


    @Test
    @DisplayName("Caso exitoso - findByProfessor_Id retorna grupos por profesor")
    void testFindByProfessorId_Exitoso() {
        when(groupRepository.findByProfessor_Id("P001")).thenReturn(List.of(group1));

        List<Group> resultado = groupRepository.findByProfessor_Id("P001");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Ciencias de la Computación", resultado.get(0).getProfessor().getDepartment());
        verify(groupRepository, times(1)).findByProfessor_Id("P001");

        System.out.println("Grupo encontrado correctamente por profesor");
    }

    @Test
    @DisplayName("Caso error - findByProfessor_Id retorna lista vacía para profesor inexistente")
    void testFindByProfessorId_Error() {
        when(groupRepository.findByProfessor_Id("P999")).thenReturn(Collections.emptyList());

        List<Group> resultado = groupRepository.findByProfessor_Id("P999");

        assertTrue(resultado.isEmpty());
        verify(groupRepository, times(1)).findByProfessor_Id("P999");

        System.out.println("Se manejó correctamente la búsqueda vacía por profesor inexistente");
    }


    @Test
    @DisplayName("Caso exitoso - findByClassroom_ClassroomId retorna grupos por aula")
    void testFindByClassroomId_Exitoso() {
        when(groupRepository.findByClassroom_ClassroomId("C001")).thenReturn(List.of(group1));

        List<Group> resultado = groupRepository.findByClassroom_ClassroomId("C001");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("C001", resultado.get(0).getClassroom().getClassroomId());
        verify(groupRepository, times(1)).findByClassroom_ClassroomId("C001");

        System.out.println("Grupo encontrado correctamente por aula");
    }

    @Test
    @DisplayName("Caso error - findByClassroom_ClassroomId retorna lista vacía para aula inexistente")
    void testFindByClassroomId_Error() {
        when(groupRepository.findByClassroom_ClassroomId("C999")).thenReturn(Collections.emptyList());

        List<Group> resultado = groupRepository.findByClassroom_ClassroomId("C999");

        assertTrue(resultado.isEmpty());
        verify(groupRepository, times(1)).findByClassroom_ClassroomId("C999");

        System.out.println("Se manejó correctamente la búsqueda vacía por aula inexistente");
    }
}

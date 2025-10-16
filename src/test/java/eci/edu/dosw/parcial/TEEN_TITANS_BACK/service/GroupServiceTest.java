package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para los métodos abstractos de GroupService
 * probados a través de AdminGroupService
 */
@ExtendWith(MockitoExtension.class)
public class GroupServiceTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ClassroomRepository classroomRepository;

    @InjectMocks
    private AdminGroupService adminGroupService;

    private Group group1;
    private Group group2;
    private Course course1;
    private Course course2;
    private Classroom classroom1;
    private Classroom classroom2;
    private Schedule schedule;

    @BeforeEach
    void setUp() {
        // Inicializar cursos
        course1 = new Course("CS101", "Programación I", 3,
                "Curso introductorio de programación", "Ingeniería", true);
        course2 = new Course("MATH101", "Cálculo I", 4,
                "Curso de cálculo diferencial", "Ingeniería", true);

        // Inicializar aulas
        classroom1 = new Classroom("AULA001", "Edificio A", "101", 30, RoomType.LABORATORY);

        // Inicializar horario
        schedule = new Schedule("SCH001", "Lunes", "08:00", "10:00", "2025-1");

        // Inicializar grupos
        group1 = new Group("GRP001", "A", course1, null, schedule, classroom1);
        group2 = new Group("GRP002", "B", course1, null, schedule, classroom2);
    }

    // TESTS PARA MÉTODOS ABSTRACTOS DE GROUPSERVICE

    @Test
    @DisplayName("Caso exitoso - getCourse retorna curso del grupo")
    void testGetCourse_Exitoso() {
        // Configurar mock
        when(groupRepository.findById("GRP001"))
                .thenReturn(Optional.of(group1));

        // Ejecutar método
        Course resultado = adminGroupService.getCourse("GRP001");

        // Verificar resultados
        assertNotNull(resultado);
        assertEquals("CS101", resultado.getCourseCode());
        assertEquals("Programación I", resultado.getName());
        assertEquals("Ingeniería", resultado.getAcademicProgram());

        // Verificar interacciones
        verify(groupRepository, times(1)).findById("GRP001");
    }

    @Test
    @DisplayName("Caso error - getCourse con grupo inexistente lanza excepción")
    void testGetCourse_GrupoNoEncontrado() {
        // Configurar mock
        when(groupRepository.findById("GRP999"))
                .thenReturn(Optional.empty());

        // Ejecutar y verificar excepción
        AppException exception = assertThrows(AppException.class, () -> {
            adminGroupService.getCourse("GRP999");
        });

        assertEquals("Grupo no encontrado con ID: GRP999", exception.getMessage());

        // Verificar interacciones
        verify(groupRepository, times(1)).findById("GRP999");
    }

    @Test
    @DisplayName("Caso exitoso - getMaxCapacity retorna capacidad del aula")
    void testGetMaxCapacity_Exitoso() {
        // Configurar mock
        when(groupRepository.findById("GRP001"))
                .thenReturn(Optional.of(group1));

        // Ejecutar método
        Integer resultado = adminGroupService.getMaxCapacity("GRP001");

        // Verificar resultados
        assertNotNull(resultado);
        assertEquals(30, resultado);

        // Verificar interacciones
        verify(groupRepository, times(1)).findById("GRP001");
    }

    @Test
    @DisplayName("Caso error - getMaxCapacity con grupo inexistente lanza excepción")
    void testGetMaxCapacity_GrupoNoEncontrado() {
        // Configurar mock
        when(groupRepository.findById("GRP999"))
                .thenReturn(Optional.empty());

        // Ejecutar y verificar excepción
        AppException exception = assertThrows(AppException.class, () -> {
            adminGroupService.getMaxCapacity("GRP999");
        });

        assertEquals("Grupo no encontrado con ID: GRP999", exception.getMessage());

        // Verificar interacciones
        verify(groupRepository, times(1)).findById("GRP999");
    }

    @Test
    @DisplayName("Caso error - getMaxCapacity con grupo sin aula lanza excepción")
    void testGetMaxCapacity_GrupoSinAula() {
        // Crear grupo sin aula
        Group groupSinAula = new Group("GRP003", "C", course1, null, schedule, null);

        // Configurar mock
        when(groupRepository.findById("GRP003"))
                .thenReturn(Optional.of(groupSinAula));

        // Ejecutar y verificar excepción
        AppException exception = assertThrows(AppException.class, () -> {
            adminGroupService.getMaxCapacity("GRP003");
        });

        assertEquals("El grupo no tiene aula asignada: GRP003", exception.getMessage());

        // Verificar interacciones
        verify(groupRepository, times(1)).findById("GRP003");
    }

    @Test
    @DisplayName("Caso exitoso - getCurrentEnrollment calcula matrícula actual")
    void testGetCurrentEnrollment_Exitoso() {
        // Configurar mock
        when(groupRepository.findById("GRP001"))
                .thenReturn(Optional.of(group1));

        // Ejecutar método
        Integer resultado = adminGroupService.getCurrentEnrollment("GRP001");

        // Verificar resultados
        assertNotNull(resultado);
        assertEquals(18, resultado); // 30 * 0.6 = 18

        // Verificar interacciones
        verify(groupRepository, times(1)).findById("GRP001");
    }

    @Test
    @DisplayName("Caso error - getCurrentEnrollment con grupo inexistente lanza excepción")
    void testGetCurrentEnrollment_GrupoNoEncontrado() {
        // Configurar mock
        when(groupRepository.findById("GRP999"))
                .thenReturn(Optional.empty());

        // Ejecutar y verificar excepción
        AppException exception = assertThrows(AppException.class, () -> {
            adminGroupService.getCurrentEnrollment("GRP999");
        });

        assertEquals("Grupo no encontrado con ID: GRP999", exception.getMessage());

        // Verificar interacciones
        verify(groupRepository, times(1)).findById("GRP999");
    }

    @Test
    @DisplayName("Caso exitoso - getWaitingList retorna lista vacía")
    void testGetWaitingList_Exitoso() {
        // Configurar mock
        when(groupRepository.existsById("GRP001"))
                .thenReturn(true);

        // Ejecutar método
        List<ScheduleChangeRequest> resultado = adminGroupService.getWaitingList("GRP001");

        // Verificar resultados
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        // Verificar interacciones
        verify(groupRepository, times(1)).existsById("GRP001");
    }

    @Test
    @DisplayName("Caso error - getWaitingList con grupo inexistente lanza excepción")
    void testGetWaitingList_GrupoNoEncontrado() {
        // Configurar mock
        when(groupRepository.existsById("GRP999"))
                .thenReturn(false);

        // Ejecutar y verificar excepción
        AppException exception = assertThrows(AppException.class, () -> {
            adminGroupService.getWaitingList("GRP999");
        });

        assertEquals("Grupo no encontrado con ID: GRP999", exception.getMessage());

        // Verificar interacciones
        verify(groupRepository, times(1)).existsById("GRP999");
    }



    @Test
    @DisplayName("Caso error - getTotalEnrolledByCourse con curso inexistente lanza excepción")
    void testGetTotalEnrolledByCourse_CursoNoEncontrado() {
        // Configurar mock
        when(courseRepository.existsById("UNKNOWN"))
                .thenReturn(false);

        // Ejecutar y verificar excepción
        AppException exception = assertThrows(AppException.class, () -> {
            adminGroupService.getTotalEnrolledByCourse("UNKNOWN");
        });

        assertEquals("Curso no encontrado con código: UNKNOWN", exception.getMessage());

        // Verificar interacciones
        verify(courseRepository, times(1)).existsById("UNKNOWN");
        verify(groupRepository, never()).findByCourse_CourseCode(any());
    }

    @Test
    @DisplayName("Caso borde - getTotalEnrolledByCourse con curso sin grupos")
    void testGetTotalEnrolledByCourse_CursoSinGrupos() {
        // Configurar mocks
        when(courseRepository.existsById("MATH101"))
                .thenReturn(true);
        when(groupRepository.findByCourse_CourseCode("MATH101"))
                .thenReturn(Collections.emptyList());

        // Ejecutar método
        Map<String, Integer> resultado = adminGroupService.getTotalEnrolledByCourse("MATH101");

        // Verificar resultados
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        // Verificar interacciones
        verify(courseRepository, times(1)).existsById("MATH101");
        verify(groupRepository, times(1)).findByCourse_CourseCode("MATH101");
    }

    // TESTS ADICIONALES PARA MÉTODOS CONCRETOS DE ADMINGROUPSERVICE

    @Test
    @DisplayName("Caso exitoso - createGroup crea grupo correctamente")
    void testCreateGroup_Exitoso() {
        // Configurar mock
        when(groupRepository.save(any(Group.class)))
                .thenReturn(group1);

        // Ejecutar método
        Group resultado = adminGroupService.createGroup(group1);

        // Verificar resultados
        assertNotNull(resultado);
        assertEquals("GRP001", resultado.getGroupId());
        assertEquals("A", resultado.getSection());
        assertEquals("CS101", resultado.getCourse().getCourseCode());

        // Verificar interacciones
        verify(groupRepository, times(1)).save(group1);
    }

    @Test
    @DisplayName("Caso exitoso - updateGroup actualiza grupo existente")
    void testUpdateGroup_Exitoso() {
        // Grupo actualizado
        Group grupoActualizado = new Group("GRP001", "A Actualizada", course2, null, schedule, classroom2);

        // Configurar mocks
        when(groupRepository.existsById("GRP001"))
                .thenReturn(true);
        when(groupRepository.save(any(Group.class)))
                .thenReturn(grupoActualizado);

        // Ejecutar método
        Group resultado = adminGroupService.updateGroup("GRP001", grupoActualizado);

        // Verificar resultados
        assertNotNull(resultado);
        assertEquals("GRP001", resultado.getGroupId());
        assertEquals("A Actualizada", resultado.getSection());
        assertEquals("MATH101", resultado.getCourse().getCourseCode());

        // Verificar interacciones
        verify(groupRepository, times(1)).existsById("GRP001");
        verify(groupRepository, times(1)).save(any(Group.class));
    }

    @Test
    @DisplayName("Caso error - updateGroup con grupo inexistente lanza excepción")
    void testUpdateGroup_GrupoNoEncontrado() {
        // Configurar mock
        when(groupRepository.existsById("GRP999"))
                .thenReturn(false);

        // Ejecutar y verificar excepción
        AppException exception = assertThrows(AppException.class, () -> {
            adminGroupService.updateGroup("GRP999", group1);
        });

        assertEquals("Grupo no encontrado con ID: GRP999", exception.getMessage());

        // Verificar interacciones
        verify(groupRepository, times(1)).existsById("GRP999");
        verify(groupRepository, never()).save(any());
    }

    @Test
    @DisplayName("Caso exitoso - deleteGroup elimina grupo existente")
    void testDeleteGroup_Exitoso() {
        // Configurar mocks
        when(groupRepository.existsById("GRP001"))
                .thenReturn(true);
        doNothing().when(groupRepository).deleteById("GRP001");

        // Ejecutar método
        adminGroupService.deleteGroup("GRP001");

        // Verificar que no se lanzó excepción
        assertDoesNotThrow(() -> adminGroupService.deleteGroup("GRP001"));

        // Verificar interacciones
        verify(groupRepository, times(2)).existsById("GRP001");
        verify(groupRepository, times(2)).deleteById("GRP001");
    }


    @Test
    @DisplayName("Caso borde - getWaitingList con grupo existente")
    void testGetWaitingList_GrupoExistente() {
        // Configurar mock
        when(groupRepository.existsById("GRP001"))
                .thenReturn(true);

        // Ejecutar método
        List<ScheduleChangeRequest> resultado = adminGroupService.getWaitingList("GRP001");

        // Verificar que siempre retorna lista vacía
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        // Verificar interacciones
        verify(groupRepository, times(1)).existsById("GRP001");
    }

    @Test
    @DisplayName("Caso borde - getTotalEnrolledByCourse con un solo grupo")
    void testGetTotalEnrolledByCourse_UnSoloGrupo() {
        // Configurar mocks
        when(courseRepository.existsById("CS101"))
                .thenReturn(true);
        when(groupRepository.findByCourse_CourseCode("CS101"))
                .thenReturn(List.of(group1));
        when(groupRepository.findById("GRP001"))
                .thenReturn(Optional.of(group1));

        // Ejecutar método
        Map<String, Integer> resultado = adminGroupService.getTotalEnrolledByCourse("CS101");

        // Verificar resultados
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.containsKey("GRP001"));
        assertEquals(18, resultado.get("GRP001"));

        // Verificar interacciones
        verify(courseRepository, times(1)).existsById("CS101");
        verify(groupRepository, times(1)).findByCourse_CourseCode("CS101");
        verify(groupRepository, times(1)).findById("GRP001");
    }


}
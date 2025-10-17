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
        course1 = new Course("CS101", "Programación I", 3,
                "Curso introductorio de programación", "Ingeniería", true);
        course2 = new Course("MATH101", "Cálculo I", 4,
                "Curso de cálculo diferencial", "Ingeniería", true);

        classroom1 = new Classroom("AULA001", "Edificio A", "101", 30, RoomType.LABORATORY);

        schedule = new Schedule("SCH001", "Lunes", "08:00", "10:00", "2025-1");

        group1 = new Group("GRP001", "A", course1, null, schedule, classroom1);
        group2 = new Group("GRP002", "B", course1, null, schedule, classroom2);
    }

    @Test
    @DisplayName("Caso exitoso - getCourse retorna curso del grupo")
    void testGetCourse_Exitoso() {
        when(groupRepository.findById("GRP001"))
                .thenReturn(Optional.of(group1));

        Course resultado = adminGroupService.getCourse("GRP001");

        assertNotNull(resultado);
        assertEquals("CS101", resultado.getCourseCode());
        assertEquals("Programación I", resultado.getName());
        assertEquals("Ingeniería", resultado.getAcademicProgram());

        verify(groupRepository, times(1)).findById("GRP001");
    }

    @Test
    @DisplayName("Caso error - getCourse con grupo inexistente lanza excepción")
    void testGetCourse_GrupoNoEncontrado() {
        when(groupRepository.findById("GRP999"))
                .thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            adminGroupService.getCourse("GRP999");
        });

        assertEquals("Grupo no encontrado con ID: GRP999", exception.getMessage());
        verify(groupRepository, times(1)).findById("GRP999");
    }

    @Test
    @DisplayName("Caso exitoso - getMaxCapacity retorna capacidad del aula")
    void testGetMaxCapacity_Exitoso() {
        when(groupRepository.findById("GRP001"))
                .thenReturn(Optional.of(group1));

        Integer resultado = adminGroupService.getMaxCapacity("GRP001");

        assertNotNull(resultado);
        assertEquals(30, resultado);
        verify(groupRepository, times(1)).findById("GRP001");
    }

    @Test
    @DisplayName("Caso error - getMaxCapacity con grupo inexistente lanza excepción")
    void testGetMaxCapacity_GrupoNoEncontrado() {
        when(groupRepository.findById("GRP999"))
                .thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            adminGroupService.getMaxCapacity("GRP999");
        });

        assertEquals("Grupo no encontrado con ID: GRP999", exception.getMessage());
        verify(groupRepository, times(1)).findById("GRP999");
    }

    @Test
    @DisplayName("Caso error - getMaxCapacity con grupo sin aula lanza excepción")
    void testGetMaxCapacity_GrupoSinAula() {
        Group groupSinAula = new Group("GRP003", "C", course1, null, schedule, null);

        when(groupRepository.findById("GRP003"))
                .thenReturn(Optional.of(groupSinAula));

        AppException exception = assertThrows(AppException.class, () -> {
            adminGroupService.getMaxCapacity("GRP003");
        });

        assertEquals("El grupo no tiene aula asignada: GRP003", exception.getMessage());
        verify(groupRepository, times(1)).findById("GRP003");
    }

    @Test
    @DisplayName("Caso exitoso - getCurrentEnrollment calcula matrícula actual")
    void testGetCurrentEnrollment_Exitoso() {
        when(groupRepository.findById("GRP001"))
                .thenReturn(Optional.of(group1));

        Integer resultado = adminGroupService.getCurrentEnrollment("GRP001");

        assertNotNull(resultado);
        assertEquals(18, resultado);
        verify(groupRepository, times(1)).findById("GRP001");
    }

    @Test
    @DisplayName("Caso error - getCurrentEnrollment con grupo inexistente lanza excepción")
    void testGetCurrentEnrollment_GrupoNoEncontrado() {
        when(groupRepository.findById("GRP999"))
                .thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            adminGroupService.getCurrentEnrollment("GRP999");
        });

        assertEquals("Grupo no encontrado con ID: GRP999", exception.getMessage());
        verify(groupRepository, times(1)).findById("GRP999");
    }

    @Test
    @DisplayName("Caso exitoso - getWaitingList retorna lista vacía")
    void testGetWaitingList_Exitoso() {
        when(groupRepository.existsById("GRP001"))
                .thenReturn(true);

        List<ScheduleChangeRequest> resultado = adminGroupService.getWaitingList("GRP001");

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(groupRepository, times(1)).existsById("GRP001");
    }

    @Test
    @DisplayName("Caso error - getWaitingList con grupo inexistente lanza excepción")
    void testGetWaitingList_GrupoNoEncontrado() {
        when(groupRepository.existsById("GRP999"))
                .thenReturn(false);

        AppException exception = assertThrows(AppException.class, () -> {
            adminGroupService.getWaitingList("GRP999");
        });

        assertEquals("Grupo no encontrado con ID: GRP999", exception.getMessage());
        verify(groupRepository, times(1)).existsById("GRP999");
    }

    @Test
    @DisplayName("Caso error - getTotalEnrolledByCourse con curso inexistente lanza excepción")
    void testGetTotalEnrolledByCourse_CursoNoEncontrado() {
        when(courseRepository.existsById("UNKNOWN"))
                .thenReturn(false);

        AppException exception = assertThrows(AppException.class, () -> {
            adminGroupService.getTotalEnrolledByCourse("UNKNOWN");
        });

        assertEquals("Curso no encontrado con código: UNKNOWN", exception.getMessage());
        verify(courseRepository, times(1)).existsById("UNKNOWN");
        verify(groupRepository, never()).findByCourse_CourseCode(any());
    }

    @Test
    @DisplayName("Caso borde - getTotalEnrolledByCourse con curso sin grupos")
    void testGetTotalEnrolledByCourse_CursoSinGrupos() {
        when(courseRepository.existsById("MATH101"))
                .thenReturn(true);
        when(groupRepository.findByCourse_CourseCode("MATH101"))
                .thenReturn(Collections.emptyList());

        Map<String, Integer> resultado = adminGroupService.getTotalEnrolledByCourse("MATH101");

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(courseRepository, times(1)).existsById("MATH101");
        verify(groupRepository, times(1)).findByCourse_CourseCode("MATH101");
    }

    @Test
    @DisplayName("Caso exitoso - createGroup crea grupo correctamente")
    void testCreateGroup_Exitoso() {
        when(groupRepository.save(any(Group.class)))
                .thenReturn(group1);

        Group resultado = adminGroupService.createGroup(group1);

        assertNotNull(resultado);
        assertEquals("GRP001", resultado.getGroupId());
        assertEquals("A", resultado.getSection());
        assertEquals("CS101", resultado.getCourse().getCourseCode());
        verify(groupRepository, times(1)).save(group1);
    }

    @Test
    @DisplayName("Caso exitoso - updateGroup actualiza grupo existente")
    void testUpdateGroup_Exitoso() {
        Group grupoActualizado = new Group("GRP001", "A Actualizada", course2, null, schedule, classroom2);

        when(groupRepository.existsById("GRP001"))
                .thenReturn(true);
        when(groupRepository.save(any(Group.class)))
                .thenReturn(grupoActualizado);

        Group resultado = adminGroupService.updateGroup("GRP001", grupoActualizado);

        assertNotNull(resultado);
        assertEquals("GRP001", resultado.getGroupId());
        assertEquals("A Actualizada", resultado.getSection());
        assertEquals("MATH101", resultado.getCourse().getCourseCode());
        verify(groupRepository, times(1)).existsById("GRP001");
        verify(groupRepository, times(1)).save(any(Group.class));
    }

    @Test
    @DisplayName("Caso error - updateGroup con grupo inexistente lanza excepción")
    void testUpdateGroup_GrupoNoEncontrado() {
        when(groupRepository.existsById("GRP999"))
                .thenReturn(false);

        AppException exception = assertThrows(AppException.class, () -> {
            adminGroupService.updateGroup("GRP999", group1);
        });

        assertEquals("Grupo no encontrado con ID: GRP999", exception.getMessage());
        verify(groupRepository, times(1)).existsById("GRP999");
        verify(groupRepository, never()).save(any());
    }

    @Test
    @DisplayName("Caso exitoso - deleteGroup elimina grupo existente")
    void testDeleteGroup_Exitoso() {
        when(groupRepository.existsById("GRP001"))
                .thenReturn(true);
        doNothing().when(groupRepository).deleteById("GRP001");

        adminGroupService.deleteGroup("GRP001");

        assertDoesNotThrow(() -> adminGroupService.deleteGroup("GRP001"));
        verify(groupRepository, times(2)).existsById("GRP001");
        verify(groupRepository, times(2)).deleteById("GRP001");
    }

    @Test
    @DisplayName("Caso borde - getWaitingList con grupo existente")
    void testGetWaitingList_GrupoExistente() {
        when(groupRepository.existsById("GRP001"))
                .thenReturn(true);

        List<ScheduleChangeRequest> resultado = adminGroupService.getWaitingList("GRP001");

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(groupRepository, times(1)).existsById("GRP001");
    }

    @Test
    @DisplayName("Caso borde - getTotalEnrolledByCourse con un solo grupo")
    void testGetTotalEnrolledByCourse_UnSoloGrupo() {
        when(courseRepository.existsById("CS101"))
                .thenReturn(true);
        when(groupRepository.findByCourse_CourseCode("CS101"))
                .thenReturn(List.of(group1));
        when(groupRepository.findById("GRP001"))
                .thenReturn(Optional.of(group1));

        Map<String, Integer> resultado = adminGroupService.getTotalEnrolledByCourse("CS101");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.containsKey("GRP001"));
        assertEquals(18, resultado.get("GRP001"));
        verify(courseRepository, times(1)).existsById("CS101");
        verify(groupRepository, times(1)).findByCourse_CourseCode("CS101");
        verify(groupRepository, times(1)).findById("GRP001");
    }
}
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

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminGroupServiceTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private ClassroomRepository classroomRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentAcademicProgressRepository studentAcademicProgressRepository;

    @Mock
    private CourseStatusDetailRepository courseStatusDetailRepository;

    @Mock
    private ScheduleChangeRequestRepository scheduleChangeRequestRepository;

    @InjectMocks
    private AdminGroupService adminGroupService;

    private Group testGroup;
    private Course testCourse;
    private Professor testProfessor;
    private Classroom testClassroom;

    @BeforeEach
    void setUp() {
        testCourse = new Course("CS101", "Programación I", 4, "Curso de programación", "Ingeniería", true);
        testProfessor = new Professor("PROF001", "Bruce Wayne", "bruce@wayne.com", "1234", "Computer Science", true, List.of("Java", "Spring"));
        testClassroom = new Classroom("A101", "Edificio A", 30, null);

        testGroup = new Group();
        testGroup.setGroupId("G001");
        testGroup.setCourse(testCourse);
        testGroup.setProfessor(testProfessor);
        testGroup.setClassroom(testClassroom);
    }

    @Test
    @DisplayName("Éxito - getCourse retorna curso cuando grupo existe")
    void testGetCourse_Exitoso() {
        when(groupRepository.findById("G001")).thenReturn(Optional.of(testGroup));

        Course resultado = adminGroupService.getCourse("G001");

        assertAll("Verificar curso retornado",
                () -> assertNotNull(resultado),
                () -> assertEquals("CS101", resultado.getCourseCode()),
                () -> assertEquals("Programación I", resultado.getName())
        );

        verify(groupRepository, times(1)).findById("G001");
    }

    @Test
    @DisplayName("Error - getCourse lanza excepción cuando grupo no existe")
    void testGetCourse_GrupoNoEncontrado() {
        when(groupRepository.findById("G999")).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            adminGroupService.getCourse("G999");
        });

        assertEquals("Grupo no encontrado con ID: G999", exception.getMessage());
        verify(groupRepository, times(1)).findById("G999");
    }

    @Test
    @DisplayName("Éxito - getMaxCapacity retorna capacidad cuando grupo tiene aula")
    void testGetMaxCapacity_Exitoso() {
        when(groupRepository.findById("G001")).thenReturn(Optional.of(testGroup));

        Integer capacidad = adminGroupService.getMaxCapacity("G001");

        assertAll("Verificar capacidad",
                () -> assertNotNull(capacidad),
                () -> assertEquals(30, capacidad)
        );

        verify(groupRepository, times(1)).findById("G001");
    }

    @Test
    @DisplayName("Error - getMaxCapacity lanza excepción cuando grupo no tiene aula")
    void testGetMaxCapacity_GrupoSinAula() {
        testGroup.setClassroom(null);
        when(groupRepository.findById("G001")).thenReturn(Optional.of(testGroup));

        AppException exception = assertThrows(AppException.class, () -> {
            adminGroupService.getMaxCapacity("G001");
        });

        assertEquals("El grupo no tiene aula asignada: G001", exception.getMessage());
        verify(groupRepository, times(1)).findById("G001");
    }

    @Test
    @DisplayName("Éxito - getCurrentEnrollment retorna inscripción actual")
    void testGetCurrentEnrollment_Exitoso() {
        when(groupRepository.findById("G001")).thenReturn(Optional.of(testGroup));

        Integer inscripcion = adminGroupService.getCurrentEnrollment("G001");

        assertAll("Verificar inscripción actual",
                () -> assertNotNull(inscripcion),
                () -> assertEquals(18, inscripcion)
        );

        verify(groupRepository, times(1)).findById("G001");
    }

    @Test
    @DisplayName("Error - getCurrentEnrollment lanza excepción cuando grupo no tiene aula")
    void testGetCurrentEnrollment_GrupoSinAula() {
        testGroup.setClassroom(null);
        when(groupRepository.findById("G001")).thenReturn(Optional.of(testGroup));

        AppException exception = assertThrows(AppException.class, () -> {
            adminGroupService.getCurrentEnrollment("G001");
        });

        assertEquals("El grupo no tiene aula asignada: G001", exception.getMessage());
        verify(groupRepository, times(1)).findById("G001");
    }

    @Test
    @DisplayName("Éxito - getWaitingList retorna lista vacía cuando grupo existe")
    void testGetWaitingList_Exitoso() {
        when(groupRepository.existsById("G001")).thenReturn(true);

        List<ScheduleChangeRequest> resultado = adminGroupService.getWaitingList("G001");

        assertTrue(resultado.isEmpty());
        verify(groupRepository, times(1)).existsById("G001");
    }

    @Test
    @DisplayName("Error - getWaitingList lanza excepción cuando grupo no existe")
    void testGetWaitingList_GrupoNoEncontrado() {
        when(groupRepository.existsById("G999")).thenReturn(false);

        AppException exception = assertThrows(AppException.class, () -> {
            adminGroupService.getWaitingList("G999");
        });

        assertEquals("Grupo no encontrado con ID: G999", exception.getMessage());
        verify(groupRepository, times(1)).existsById("G999");
    }

    @Test
    @DisplayName("Éxito - assignProfessorToGroup asigna profesor correctamente")
    void testAssignProfessorToGroup_Exitoso() {
        when(groupRepository.findById("G001")).thenReturn(Optional.of(testGroup));
        when(professorRepository.findById("PROF001")).thenReturn(Optional.of(testProfessor));
        when(groupRepository.save(any(Group.class))).thenReturn(testGroup);

        assertDoesNotThrow(() -> {
            adminGroupService.assignProfessorToGroup("G001", "PROF001");
        });

        verify(groupRepository, times(1)).findById("G001");
        verify(professorRepository, times(1)).findById("PROF001");
        verify(groupRepository, times(1)).save(testGroup);
    }

    @Test
    @DisplayName("Error - assignProfessorToGroup lanza excepción cuando grupo no existe")
    void testAssignProfessorToGroup_GrupoNoEncontrado() {
        when(groupRepository.findById("G999")).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            adminGroupService.assignProfessorToGroup("G999", "PROF001");
        });

        assertEquals("Grupo no encontrado: G999", exception.getMessage());
        verify(groupRepository, times(1)).findById("G999");
        verify(professorRepository, never()).findById(anyString());
    }

    @Test
    @DisplayName("Error - assignProfessorToGroup lanza excepción cuando profesor no existe")
    void testAssignProfessorToGroup_ProfesorNoEncontrado() {
        when(groupRepository.findById("G001")).thenReturn(Optional.of(testGroup));
        when(professorRepository.findById("PROF999")).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            adminGroupService.assignProfessorToGroup("G001", "PROF999");
        });

        assertEquals("Profesor no encontrado: PROF999", exception.getMessage());
        verify(groupRepository, times(1)).findById("G001");
        verify(professorRepository, times(1)).findById("PROF999");
        verify(groupRepository, never()).save(any(Group.class));
    }

    @Test
    @DisplayName("Éxito - assignClassroomToGroup asigna aula correctamente")
    void testAssignClassroomToGroup_Exitoso() {
        when(groupRepository.findById("G001")).thenReturn(Optional.of(testGroup));
        when(classroomRepository.findById("A101")).thenReturn(Optional.of(testClassroom));
        when(groupRepository.save(any(Group.class))).thenReturn(testGroup);

        assertDoesNotThrow(() -> {
            adminGroupService.assignClassroomToGroup("G001", "A101");
        });

        verify(groupRepository, times(1)).findById("G001");
        verify(classroomRepository, times(1)).findById("A101");
        verify(groupRepository, times(1)).save(testGroup);
    }

    @Test
    @DisplayName("Error - assignClassroomToGroup lanza excepción cuando aula no existe")
    void testAssignClassroomToGroup_AulaNoEncontrada() {
        when(groupRepository.findById("G001")).thenReturn(Optional.of(testGroup));
        when(classroomRepository.findById("A999")).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            adminGroupService.assignClassroomToGroup("G001", "A999");
        });

        assertEquals("Aula no encontrada: A999", exception.getMessage());
        verify(groupRepository, times(1)).findById("G001");
        verify(classroomRepository, times(1)).findById("A999");
        verify(groupRepository, never()).save(any(Group.class));
    }

    @Test
    @DisplayName("Éxito - createGroup crea grupo correctamente")
    void testCreateGroup_Exitoso() {
        when(courseRepository.existsById("CS101")).thenReturn(true);
        when(groupRepository.save(any(Group.class))).thenReturn(testGroup);

        Group resultado = adminGroupService.createGroup(testGroup);

        assertAll("Verificar grupo creado",
                () -> assertNotNull(resultado),
                () -> assertEquals("G001", resultado.getGroupId())
        );

        verify(courseRepository, times(1)).existsById("CS101");
        verify(groupRepository, times(1)).save(testGroup);
    }

    @Test
    @DisplayName("Error - createGroup lanza excepción cuando curso no existe")
    void testCreateGroup_CursoNoExiste() {
        testGroup.getCourse().setCourseCode("INVALID");
        when(courseRepository.existsById("INVALID")).thenReturn(false);

        AppException exception = assertThrows(AppException.class, () -> {
            adminGroupService.createGroup(testGroup);
        });

        assertEquals("El curso especificado no existe o es inválido.", exception.getMessage());
        verify(courseRepository, times(1)).existsById("INVALID");
        verify(groupRepository, never()).save(any(Group.class));
    }

    @Test
    @DisplayName("Error - createGroup lanza excepción cuando curso es null")
    void testCreateGroup_CursoNull() {
        testGroup.setCourse(null);

        AppException exception = assertThrows(AppException.class, () -> {
            adminGroupService.createGroup(testGroup);
        });

        assertEquals("El curso especificado no existe o es inválido.", exception.getMessage());
        verify(courseRepository, never()).existsById(anyString());
        verify(groupRepository, never()).save(any(Group.class));
    }

    @Test
    @DisplayName("Éxito - listAllGroups retorna todos los grupos")
    void testListAllGroups_Exitoso() {
        List<Group> grupos = List.of(testGroup, testGroup);
        when(groupRepository.findAll()).thenReturn(grupos);

        List<Group> resultado = adminGroupService.listAllGroups();

        assertAll("Verificar lista de grupos",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size())
        );

        verify(groupRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Éxito - listAllGroups retorna lista vacía cuando no hay grupos")
    void testListAllGroups_ListaVacia() {
        when(groupRepository.findAll()).thenReturn(Collections.emptyList());

        List<Group> resultado = adminGroupService.listAllGroups();

        assertTrue(resultado.isEmpty());
        verify(groupRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Éxito - getTotalEnrolledByCourse retorna mapa de inscripciones")
    void testGetTotalEnrolledByCourse_Exitoso() {
        when(courseRepository.existsById("CS101")).thenReturn(true);
        when(groupRepository.findByCourse_CourseCode("CS101")).thenReturn(List.of(testGroup));
        when(groupRepository.findById("G001")).thenReturn(Optional.of(testGroup));

        Map<String, Integer> resultado = adminGroupService.getTotalEnrolledByCourse("CS101");

        assertAll("Verificar mapa de inscripciones",
                () -> assertNotNull(resultado),
                () -> assertFalse(resultado.isEmpty()),
                () -> assertTrue(resultado.containsKey("G001"))
        );

        verify(courseRepository, times(1)).existsById("CS101");
        verify(groupRepository, times(1)).findByCourse_CourseCode("CS101");
    }

    @Test
    @DisplayName("Error - getTotalEnrolledByCourse lanza excepción cuando curso no existe")
    void testGetTotalEnrolledByCourse_CursoNoExiste() {
        when(courseRepository.existsById("INVALID")).thenReturn(false);

        AppException exception = assertThrows(AppException.class, () -> {
            adminGroupService.getTotalEnrolledByCourse("INVALID");
        });

        assertEquals("Curso no encontrado con código: INVALID", exception.getMessage());
        verify(courseRepository, times(1)).existsById("INVALID");
        verify(groupRepository, never()).findByCourse_CourseCode(anyString());
    }

    @Test
    @DisplayName("Caso borde - Múltiples operaciones con el mismo grupo")
    void testOperacionesMultiplesConMismoGrupo() {
        when(groupRepository.findById("G001")).thenReturn(Optional.of(testGroup));
        when(professorRepository.findById("PROF001")).thenReturn(Optional.of(testProfessor));
        when(classroomRepository.findById("A101")).thenReturn(Optional.of(testClassroom));
        when(groupRepository.save(any(Group.class))).thenReturn(testGroup);

        assertAll("Múltiples operaciones con el mismo grupo",
                () -> assertDoesNotThrow(() -> adminGroupService.assignProfessorToGroup("G001", "PROF001")),
                () -> assertDoesNotThrow(() -> adminGroupService.assignClassroomToGroup("G001", "A101")),
                () -> assertNotNull(adminGroupService.getCourse("G001")),
                () -> assertEquals(30, adminGroupService.getMaxCapacity("G001"))
        );

        verify(groupRepository, times(4)).findById("G001");
        verify(professorRepository, times(1)).findById("PROF001");
        verify(classroomRepository, times(1)).findById("A101");
        verify(groupRepository, times(2)).save(testGroup);
    }

    @Test
    @DisplayName("Caso borde - Grupo sin profesor asignado")
    void testGrupoSinProfesor() {
        testGroup.setProfessor(null);
        when(groupRepository.findById("G001")).thenReturn(Optional.of(testGroup));

        Course resultado = adminGroupService.getCourse("G001");

        assertNotNull(resultado);
        assertEquals("CS101", resultado.getCourseCode());
        verify(groupRepository, times(1)).findById("G001");
    }

    @Test
    @DisplayName("Caso borde - getCurrentEnrollment con capacidad cero")
    void testGetCurrentEnrollment_CapacidadCero() {
        testClassroom.setCapacity(0);
        testGroup.setClassroom(testClassroom);
        when(groupRepository.findById("G001")).thenReturn(Optional.of(testGroup));

        Integer inscripcion = adminGroupService.getCurrentEnrollment("G001");

        assertEquals(0, inscripcion);
        verify(groupRepository, times(1)).findById("G001");
    }
}
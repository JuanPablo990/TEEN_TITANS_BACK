package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Course;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.ScheduleChangeRequest;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Group;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Classroom;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.RequestStatus;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.CourseStatusDetailRepository;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.GroupRepository;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.ScheduleChangeRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GroupServiceTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private CourseStatusDetailRepository courseStatusDetailRepository;

    @Mock
    private ScheduleChangeRequestRepository scheduleChangeRequestRepository;

    @InjectMocks
    private GroupService groupService;

    private Group group1;
    private Group group2;
    private Course course;
    private Classroom classroom;
    private ScheduleChangeRequest request1;
    private ScheduleChangeRequest request2;

    @BeforeEach
    void setUp() {
        course = new Course();
        course.setCourseCode("CS101");
        course.setName("Introduction to Computer Science");

        classroom = new Classroom();
        classroom.setCapacity(30);

        group1 = new Group();
        group1.setGroupId("GROUP_001");
        group1.setCourse(course);
        group1.setClassroom(classroom);

        group2 = new Group();
        group2.setGroupId("GROUP_002");
        group2.setCourse(course);
        group2.setClassroom(classroom);

        request1 = new ScheduleChangeRequest();
        request1.setRequestId("REQ001");
        request1.setStatus(RequestStatus.PENDING);
        request1.setRequestedGroup(group1);

        request2 = new ScheduleChangeRequest();
        request2.setRequestId("REQ002");
        request2.setStatus(RequestStatus.UNDER_REVIEW);
        request2.setRequestedGroup(group1);
    }

    @Test
    @DisplayName("Caso exitoso - getCourse retorna curso del grupo")
    void testGetCourse_Exitoso() {
        when(groupRepository.findById("GROUP_001")).thenReturn(Optional.of(group1));

        Course resultado = groupService.getCourse("GROUP_001");

        assertAll("Verificar obtención de curso del grupo",
                () -> assertNotNull(resultado),
                () -> assertEquals("CS101", resultado.getCourseCode()),
                () -> assertEquals("Introduction to Computer Science", resultado.getName())
        );

        verify(groupRepository, times(1)).findById("GROUP_001");
    }

    @Test
    @DisplayName("Caso error - getCourse retorna null cuando grupo no existe")
    void testGetCourse_GrupoNoExiste() {
        when(groupRepository.findById("GRUPO_INEXISTENTE")).thenReturn(Optional.empty());

        Course resultado = groupService.getCourse("GRUPO_INEXISTENTE");

        assertNull(resultado);
        verify(groupRepository, times(1)).findById("GRUPO_INEXISTENTE");
    }

    @Test
    @DisplayName("Caso exitoso - getMaxCapacity retorna capacidad del aula")
    void testGetMaxCapacity_Exitoso() {
        when(groupRepository.findById("GROUP_001")).thenReturn(Optional.of(group1));

        Integer resultado = groupService.getMaxCapacity("GROUP_001");

        assertAll("Verificar obtención de capacidad máxima",
                () -> assertNotNull(resultado),
                () -> assertEquals(30, resultado)
        );

        verify(groupRepository, times(1)).findById("GROUP_001");
    }

    @Test
    @DisplayName("Caso error - getMaxCapacity retorna 0 cuando grupo no existe")
    void testGetMaxCapacity_GrupoNoExiste() {
        when(groupRepository.findById("GRUPO_INEXISTENTE")).thenReturn(Optional.empty());

        Integer resultado = groupService.getMaxCapacity("GRUPO_INEXISTENTE");

        assertEquals(0, resultado);
        verify(groupRepository, times(1)).findById("GRUPO_INEXISTENTE");
    }


    @Test
    @DisplayName("Caso borde - getCurrentEnrollment retorna 0 cuando no hay estudiantes")
    void testGetCurrentEnrollment_SinEstudiantes() {
        when(courseStatusDetailRepository.findByGroup_GroupId("GROUP_001")).thenReturn(Collections.emptyList());

        Integer resultado = groupService.getCurrentEnrollment("GROUP_001");

        assertAll("Verificar matrícula cero",
                () -> assertNotNull(resultado),
                () -> assertEquals(0, resultado)
        );

        verify(courseStatusDetailRepository, times(1)).findByGroup_GroupId("GROUP_001");
    }

    @Test
    @DisplayName("Caso exitoso - getWaitingList retorna solicitudes pendientes del grupo")
    void testGetWaitingList_Exitoso() {
        List<ScheduleChangeRequest> solicitudes = Arrays.asList(request1);
        when(scheduleChangeRequestRepository.findByRequestedGroupId("GROUP_001")).thenReturn(solicitudes);

        List<ScheduleChangeRequest> resultado = groupService.getWaitingList("GROUP_001");

        assertAll("Verificar lista de espera",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("REQ001", resultado.get(0).getRequestId()),
                () -> assertEquals(RequestStatus.PENDING, resultado.get(0).getStatus())
        );

        verify(scheduleChangeRequestRepository, times(1)).findByRequestedGroupId("GROUP_001");
    }

    @Test
    @DisplayName("Caso borde - getWaitingList filtra solo solicitudes pendientes")
    void testGetWaitingList_FiltraSoloPendientes() {
        List<ScheduleChangeRequest> todasSolicitudes = Arrays.asList(request1, request2);
        when(scheduleChangeRequestRepository.findByRequestedGroupId("GROUP_001")).thenReturn(todasSolicitudes);

        List<ScheduleChangeRequest> resultado = groupService.getWaitingList("GROUP_001");

        assertAll("Verificar filtrado de solo pendientes",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals(RequestStatus.PENDING, resultado.get(0).getStatus())
        );

        verify(scheduleChangeRequestRepository, times(1)).findByRequestedGroupId("GROUP_001");
    }

    @Test
    @DisplayName("Caso borde - getWaitingList retorna lista vacía cuando no hay solicitudes")
    void testGetWaitingList_ListaVacia() {
        when(scheduleChangeRequestRepository.findByRequestedGroupId("GROUP_001")).thenReturn(Collections.emptyList());

        List<ScheduleChangeRequest> resultado = groupService.getWaitingList("GROUP_001");

        assertAll("Verificar lista de espera vacía",
                () -> assertNotNull(resultado),
                () -> assertTrue(resultado.isEmpty())
        );

        verify(scheduleChangeRequestRepository, times(1)).findByRequestedGroupId("GROUP_001");
    }




    @Test
    @DisplayName("Caso borde - getTotalEnrolledByCourse retorna mapa vacío para curso sin grupos")
    void testGetTotalEnrolledByCourse_CursoSinGrupos() {
        when(groupRepository.findByCourse_CourseCode("CURSO_SIN_GRUPOS")).thenReturn(Collections.emptyList());

        Map<String, Integer> resultado = groupService.getTotalEnrolledByCourse("CURSO_SIN_GRUPOS");

        assertAll("Verificar mapa vacío para curso sin grupos",
                () -> assertNotNull(resultado),
                () -> assertTrue(resultado.isEmpty())
        );

        verify(groupRepository, times(1)).findByCourse_CourseCode("CURSO_SIN_GRUPOS");
    }

    @Test
    @DisplayName("Caso exitoso - getExtendedWaitingList retorna lista extendida con múltiples estados")
    void testGetExtendedWaitingList_Exitoso() {
        List<ScheduleChangeRequest> todasSolicitudes = Arrays.asList(request1, request2);
        when(scheduleChangeRequestRepository.findByRequestedGroupId("GROUP_001")).thenReturn(todasSolicitudes);

        List<ScheduleChangeRequest> resultado = groupService.getExtendedWaitingList("GROUP_001");

        assertAll("Verificar lista de espera extendida",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertTrue(resultado.stream().anyMatch(r -> r.getStatus() == RequestStatus.PENDING)),
                () -> assertTrue(resultado.stream().anyMatch(r -> r.getStatus() == RequestStatus.UNDER_REVIEW))
        );

        verify(scheduleChangeRequestRepository, times(1)).findByRequestedGroupId("GROUP_001");
    }

    @Test
    @DisplayName("Caso borde - getExtendedWaitingList filtra estados no permitidos")
    void testGetExtendedWaitingList_FiltraEstadosNoPermitidos() {
        ScheduleChangeRequest requestAprobada = new ScheduleChangeRequest();
        requestAprobada.setRequestId("REQ003");
        requestAprobada.setStatus(RequestStatus.APPROVED);
        requestAprobada.setRequestedGroup(group1);

        List<ScheduleChangeRequest> todasSolicitudes = Arrays.asList(request1, request2, requestAprobada);
        when(scheduleChangeRequestRepository.findByRequestedGroupId("GROUP_001")).thenReturn(todasSolicitudes);

        List<ScheduleChangeRequest> resultado = groupService.getExtendedWaitingList("GROUP_001");

        assertAll("Verificar filtrado de estados no permitidos",
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size()),
                () -> assertTrue(resultado.stream().noneMatch(r -> r.getStatus() == RequestStatus.APPROVED))
        );

        verify(scheduleChangeRequestRepository, times(1)).findByRequestedGroupId("GROUP_001");
    }

    @Test
    @DisplayName("Caso exitoso - getStudentWaitingRequests retorna solicitudes específicas del estudiante")
    void testGetStudentWaitingRequests_Exitoso() {
        List<ScheduleChangeRequest> solicitudesEstudiante = Arrays.asList(request1);
        when(scheduleChangeRequestRepository.findByStudentIdAndStatusIn("STU001",
                Arrays.asList(RequestStatus.PENDING, RequestStatus.UNDER_REVIEW)))
                .thenReturn(solicitudesEstudiante);

        List<ScheduleChangeRequest> resultado = groupService.getStudentWaitingRequests("GROUP_001", "STU001");

        assertAll("Verificar solicitudes de espera del estudiante",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("REQ001", resultado.get(0).getRequestId())
        );

        verify(scheduleChangeRequestRepository, times(1))
                .findByStudentIdAndStatusIn("STU001", Arrays.asList(RequestStatus.PENDING, RequestStatus.UNDER_REVIEW));
    }

    @Test
    @DisplayName("Caso borde - getStudentWaitingRequests retorna lista vacía cuando no hay solicitudes")
    void testGetStudentWaitingRequests_SinSolicitudes() {
        when(scheduleChangeRequestRepository.findByStudentIdAndStatusIn("STU001",
                Arrays.asList(RequestStatus.PENDING, RequestStatus.UNDER_REVIEW)))
                .thenReturn(Collections.emptyList());

        List<ScheduleChangeRequest> resultado = groupService.getStudentWaitingRequests("GROUP_001", "STU001");

        assertAll("Verificar lista vacía de solicitudes del estudiante",
                () -> assertNotNull(resultado),
                () -> assertTrue(resultado.isEmpty())
        );

        verify(scheduleChangeRequestRepository, times(1))
                .findByStudentIdAndStatusIn("STU001", Arrays.asList(RequestStatus.PENDING, RequestStatus.UNDER_REVIEW));
    }

    @Test
    @DisplayName("Caso borde - getStudentWaitingRequests filtra por grupo específico")
    void testGetStudentWaitingRequests_FiltraPorGrupo() {
        ScheduleChangeRequest requestOtroGrupo = new ScheduleChangeRequest();
        requestOtroGrupo.setRequestId("REQ003");
        requestOtroGrupo.setStatus(RequestStatus.PENDING);
        requestOtroGrupo.setRequestedGroup(group2);

        List<ScheduleChangeRequest> todasSolicitudes = Arrays.asList(request1, requestOtroGrupo);
        when(scheduleChangeRequestRepository.findByStudentIdAndStatusIn("STU001",
                Arrays.asList(RequestStatus.PENDING, RequestStatus.UNDER_REVIEW)))
                .thenReturn(todasSolicitudes);

        List<ScheduleChangeRequest> resultado = groupService.getStudentWaitingRequests("GROUP_001", "STU001");

        assertAll("Verificar filtrado por grupo específico",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("REQ001", resultado.get(0).getRequestId())
        );

        verify(scheduleChangeRequestRepository, times(1))
                .findByStudentIdAndStatusIn("STU001", Arrays.asList(RequestStatus.PENDING, RequestStatus.UNDER_REVIEW));
    }


}
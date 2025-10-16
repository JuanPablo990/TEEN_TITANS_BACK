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
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class AdminGroupServiceTest {

    @Mock private GroupRepository groupRepository;
    @Mock private CourseRepository courseRepository;
    @Mock private AcademicPeriodRepository academicPeriodRepository;
    @Mock private StudentRepository studentRepository;
    @Mock private ProfessorRepository professorRepository;
    @Mock private ClassroomRepository classroomRepository;
    @Mock private StudentAcademicProgressRepository studentAcademicProgressRepository;
    @Mock private CourseStatusDetailRepository courseStatusDetailRepository;

    @InjectMocks
    private AdminGroupService adminGroupService;

    private Group group;
    private Course course;
    private Classroom classroom;

    @BeforeEach
    void setUp() {
        course = new Course();
        course.setCourseCode("MAT101");

        classroom = new Classroom("CR1", "Edificio A", "101", 40, RoomType.LABORATORY);

        group = new Group("G1", "A", course, null, null, classroom);
    }

    // --- getCourse() ---
    @Test
    @DisplayName("Caso exitoso - Obtener curso de un grupo existente")
    void getCourse_Success() {
        given(groupRepository.findById("G1")).willReturn(Optional.of(group));

        Course result = adminGroupService.getCourse("G1");

        assertNotNull(result);
        assertEquals("MAT101", result.getCourseCode());
        verify(groupRepository).findById("G1");
    }

    @Test
    @DisplayName("Caso error - Grupo no encontrado al obtener curso")
    void getCourse_GroupNotFound() {
        given(groupRepository.findById("G1")).willReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> adminGroupService.getCourse("G1"));
        assertEquals("Grupo no encontrado con ID: G1", ex.getMessage());
        verify(groupRepository).findById("G1");
    }

    // --- getMaxCapacity() ---
    @Test
    @DisplayName("Caso exitoso - Obtener capacidad máxima del aula del grupo")
    void getMaxCapacity_Success() {
        given(groupRepository.findById("G1")).willReturn(Optional.of(group));

        Integer capacity = adminGroupService.getMaxCapacity("G1");

        assertEquals(40, capacity);
        verify(groupRepository).findById("G1");
    }

    @Test
    @DisplayName("Caso error - Grupo no encontrado al obtener capacidad")
    void getMaxCapacity_GroupNotFound() {
        given(groupRepository.findById("G1")).willReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> adminGroupService.getMaxCapacity("G1"));
        assertEquals("Grupo no encontrado con ID: G1", ex.getMessage());
        verify(groupRepository).findById("G1");
    }


    // --- getCurrentEnrollment() ---
    @Test
    @DisplayName("Caso exitoso - Calcular inscripción actual estimada")
    void getCurrentEnrollment_Success() {
        given(groupRepository.findById("G1")).willReturn(Optional.of(group));

        Integer current = adminGroupService.getCurrentEnrollment("G1");

        assertEquals(24, current); // 60% de 40
        verify(groupRepository).findById("G1");
    }


    // --- getWaitingList() ---
    @Test
    @DisplayName("Caso exitoso - Obtener lista de espera vacía de grupo existente")
    void getWaitingList_Success() {
        given(groupRepository.existsById("G1")).willReturn(true);

        List<ScheduleChangeRequest> result = adminGroupService.getWaitingList("G1");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(groupRepository).existsById("G1");
    }

    @Test
    @DisplayName("Caso error - Grupo no encontrado al obtener lista de espera")
    void getWaitingList_GroupNotFound() {
        given(groupRepository.existsById("G1")).willReturn(false);

        AppException ex = assertThrows(AppException.class, () -> adminGroupService.getWaitingList("G1"));
        assertEquals("Grupo no encontrado con ID: G1", ex.getMessage());
        verify(groupRepository).existsById("G1");
    }

    // --- getTotalEnrolledByCourse() ---
    @Test
    @DisplayName("Caso exitoso - Obtener total de inscritos por curso")
    void getTotalEnrolledByCourse_Success() {
        given(courseRepository.existsById("MAT101")).willReturn(true);
        given(groupRepository.findByCourse_CourseCode("MAT101")).willReturn(List.of(group));
        given(groupRepository.findById("G1")).willReturn(Optional.of(group));

        Map<String, Integer> result = adminGroupService.getTotalEnrolledByCourse("MAT101");

        assertEquals(1, result.size());
        assertEquals(24, result.get("G1"));
        verify(courseRepository).existsById("MAT101");
        verify(groupRepository).findByCourse_CourseCode("MAT101");
    }

    @Test
    @DisplayName("Caso error - Curso no encontrado al obtener inscritos por curso")
    void getTotalEnrolledByCourse_CourseNotFound() {
        given(courseRepository.existsById("MAT101")).willReturn(false);

        AppException ex = assertThrows(AppException.class, () -> adminGroupService.getTotalEnrolledByCourse("MAT101"));
        assertEquals("Curso no encontrado con código: MAT101", ex.getMessage());
        verify(courseRepository).existsById("MAT101");
    }
}

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



    @Test
    @DisplayName("Caso exitoso - Calcular inscripción actual estimada")
    void getCurrentEnrollment_Success() {
        given(groupRepository.findById("G1")).willReturn(Optional.of(group));

        Integer current = adminGroupService.getCurrentEnrollment("G1");

        assertEquals(24, current); // 60% de 40
        verify(groupRepository).findById("G1");
    }



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

    @Test
    void assignStudentToGroup_Success() {
        String studentId = "S1";
        String groupId = "G1";

        Student student = new Student(studentId, "John Doe", "john@eci.edu", "password", "Engineering", 1);
        Course course = new Course("MAT101", "Mathematics", 3, "Math course", "Engineering", true);
        Classroom classroom = new Classroom("CR1", "Building A", "101", 30, RoomType.REGULAR);
        Group group = new Group(groupId, "A", course, null, null, classroom);

        given(studentRepository.findById(studentId)).willReturn(Optional.of(student));
        given(groupRepository.findById(groupId)).willReturn(Optional.of(group));

        assertDoesNotThrow(() -> adminGroupService.assignStudentToGroup(studentId, groupId));

        verify(studentRepository).findById(studentId);
        verify(groupRepository).findById(groupId);
    }

    @Test
    void assignStudentToGroup_StudentNotFound() {
        String studentId = "S1";
        String groupId = "G1";

        given(studentRepository.findById(studentId)).willReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class,
                () -> adminGroupService.assignStudentToGroup(studentId, groupId));
        assertEquals("Estudiante no encontrado con ID: " + studentId, ex.getMessage());
    }

    @Test
    void removeStudentFromGroup_Success() {
        String studentId = "S1";
        String groupId = "G1";

        Student student = new Student(studentId, "John Doe", "john@eci.edu", "password", "Engineering", 1);
        Course course = new Course("MAT101", "Mathematics", 3, "Math course", "Engineering", true);
        Classroom classroom = new Classroom("CR1", "Building A", "101", 30, RoomType.REGULAR);
        Group group = new Group(groupId, "A", course, null, null, classroom);

        given(studentRepository.findById(studentId)).willReturn(Optional.of(student));
        given(groupRepository.findById(groupId)).willReturn(Optional.of(group));

        assertDoesNotThrow(() -> adminGroupService.removeStudentFromGroup(studentId, groupId));

        verify(studentRepository).findById(studentId);
        verify(groupRepository).findById(groupId);
    }

    @Test
    void assignProfessorToGroup_Success() {
        String professorId = "P1";
        String groupId = "G1";

        Professor professor = new Professor("Computer Science", true, List.of("AI", "Algorithms"));
        professor.setId(professorId);

        Course course = new Course("MAT101", "Mathematics", 3, "Math course", "Engineering", true);
        Classroom classroom = new Classroom("CR1", "Building A", "101", 30, RoomType.REGULAR);
        Group group = new Group(groupId, "A", course, null, null, classroom);

        given(professorRepository.findById(professorId)).willReturn(Optional.of(professor));
        given(groupRepository.findById(groupId)).willReturn(Optional.of(group));

        assertDoesNotThrow(() -> adminGroupService.assignProfessorToGroup(professorId, groupId));

        verify(professorRepository).findById(professorId);
        verify(groupRepository).findById(groupId);
    }



    @Test
    void updateCourse_Success() {
        String courseCode = "MAT101";
        Course updatedCourse = new Course(courseCode, "Mathematics Advanced", 4, "Advanced math", "Engineering", true);

        given(courseRepository.existsById(courseCode)).willReturn(true);
        given(courseRepository.save(any(Course.class))).willReturn(updatedCourse);

        Course result = adminGroupService.updateCourse(courseCode, updatedCourse);

        assertNotNull(result);
        assertEquals("Mathematics Advanced", result.getName());
        verify(courseRepository).existsById(courseCode);
        verify(courseRepository).save(updatedCourse);
    }

    @Test
    void updateCourse_CourseNotFound() {
        String courseCode = "MAT101";
        Course updatedCourse = new Course(courseCode, "Mathematics", 3, "Math course", "Engineering", true);

        given(courseRepository.existsById(courseCode)).willReturn(false);

        AppException ex = assertThrows(AppException.class,
                () -> adminGroupService.updateCourse(courseCode, updatedCourse));
        assertEquals("Curso no encontrado con código: " + courseCode, ex.getMessage());
    }

    @Test
    void deleteCourse_Success() {
        String courseCode = "MAT101";

        given(courseRepository.existsById(courseCode)).willReturn(true);

        assertDoesNotThrow(() -> adminGroupService.deleteCourse(courseCode));

        verify(courseRepository).existsById(courseCode);
        verify(courseRepository).deleteById(courseCode);
    }

    @Test
    void deleteCourse_CourseNotFound() {
        String courseCode = "MAT101";

        given(courseRepository.existsById(courseCode)).willReturn(false);

        AppException ex = assertThrows(AppException.class,
                () -> adminGroupService.deleteCourse(courseCode));
        assertEquals("Curso no encontrado con código: " + courseCode, ex.getMessage());
    }

    @Test
    void getGroupsByDay_Success() {
        String dayOfWeek = "Lunes";

        Schedule schedule = new Schedule("S1", "Lunes", "08:00", "10:00", "2025-1");
        Course course = new Course("MAT101", "Mathematics", 3, "Math course", "Engineering", true);
        Classroom classroom = new Classroom("CR1", "Building A", "101", 30, RoomType.REGULAR);
        Group group = new Group("G1", "A", course, null, schedule, classroom);

        given(groupRepository.findAll()).willReturn(List.of(group));

        List<Group> result = adminGroupService.getGroupsByDay(dayOfWeek);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("G1", result.get(0).getGroupId());
        verify(groupRepository).findAll();
    }



    @Test
    void createCourse_Success() {
        Course course = new Course("MAT101", "Mathematics", 3, "Math course", "Engineering", true);

        given(courseRepository.save(any(Course.class))).willReturn(course);

        Course result = adminGroupService.createCourse(course);

        assertNotNull(result);
        assertEquals("MAT101", result.getCourseCode());
        verify(courseRepository).save(course);
    }

    @Test
    void getAllGroups_Success() {
        Course course = new Course("MAT101", "Mathematics", 3, "Math course", "Engineering", true);
        Classroom classroom = new Classroom("CR1", "Building A", "101", 30, RoomType.REGULAR);
        Group group1 = new Group("G1", "A", course, null, null, classroom);
        Group group2 = new Group("G2", "B", course, null, null, classroom);

        given(groupRepository.findAll()).willReturn(List.of(group1, group2));

        List<Group> result = adminGroupService.getAllGroups();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(groupRepository).findAll();
    }

    @Test
    void deleteGroup_Success() {
        String groupId = "G1";

        given(groupRepository.existsById(groupId)).willReturn(true);

        assertDoesNotThrow(() -> adminGroupService.deleteGroup(groupId));

        verify(groupRepository).existsById(groupId);
        verify(groupRepository).deleteById(groupId);
    }

    @Test
    void deleteGroup_GroupNotFound() {
        String groupId = "G1";

        given(groupRepository.existsById(groupId)).willReturn(false);

        AppException ex = assertThrows(AppException.class,
                () -> adminGroupService.deleteGroup(groupId));
        assertEquals("Grupo no encontrado con ID: " + groupId, ex.getMessage());
    }

    @Test
    void getCurrentEnrollment_ZeroCapacity() {
        Classroom zeroCapacityClassroom = new Classroom("CR1", "Building A", "101", 0, RoomType.REGULAR);
        Course course = new Course("MAT101", "Mathematics", 3, "Math course", "Engineering", true);
        Group zeroCapacityGroup = new Group("G1", "A", course, null, null, zeroCapacityClassroom);

        given(groupRepository.findById("G1")).willReturn(Optional.of(zeroCapacityGroup));

        Integer result = adminGroupService.getCurrentEnrollment("G1");

        assertEquals(0, result);
        verify(groupRepository).findById("G1");
    }

    @Test
    void getGroupsByDay_NoResults() {
        String dayOfWeek = "Domingo";

        Schedule schedule = new Schedule("S1", "Lunes", "08:00", "10:00", "2025-1");
        Course course = new Course("MAT101", "Mathematics", 3, "Math course", "Engineering", true);
        Classroom classroom = new Classroom("CR1", "Building A", "101", 30, RoomType.REGULAR);
        Group group = new Group("G1", "A", course, null, schedule, classroom);

        given(groupRepository.findAll()).willReturn(List.of(group));

        List<Group> result = adminGroupService.getGroupsByDay(dayOfWeek);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(groupRepository).findAll();
    }

    @Test
    void getMostRequestedGroups_EmptyList() {
        given(groupRepository.findAll()).willReturn(List.of());

        List<Group> result = adminGroupService.getMostRequestedGroups();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(groupRepository).findAll();
    }

}

package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.RoomType;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GeneralManagementServiceTest {

    @Mock
    private AcademicPeriodRepository academicPeriodRepository;

    @Mock
    private ClassroomRepository classroomRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseStatusDetailRepository courseStatusDetailRepository;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private ReviewStepRepository reviewStepRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private StudentAcademicProgressRepository studentAcademicProgressRepository;

    @InjectMocks
    private GeneralManagementService generalManagementService;

    private AcademicPeriod academicPeriod;
    private Classroom classroom;
    private Course course;
    private CourseStatusDetail courseStatusDetail;
    private Group group;
    private ReviewStep reviewStep;
    private Schedule schedule;
    private StudentAcademicProgress studentAcademicProgress;

    @BeforeEach
    void setUp() {
        academicPeriod = new AcademicPeriod();
        academicPeriod.setPeriodId("PERIOD_001");
        academicPeriod.setName("2025-1");
        academicPeriod.setActive(true);

        classroom = new Classroom();
        classroom.setClassroomId("CLASS_001");
        classroom.setBuilding("A");
        classroom.setRoomNumber("101");
        classroom.setRoomType(RoomType.LABORATORY);
        classroom.setCapacity(30);

        course = new Course();
        course.setCourseCode("CS101");
        course.setName("Introduction to Computer Science");
        course.setIsActive(true);
        course.setAcademicProgram("Computer Science");

        courseStatusDetail = new CourseStatusDetail();
        courseStatusDetail.setId("CSD_001");
        courseStatusDetail.setStudentId("STU001");
        courseStatusDetail.setCourse(course);
        courseStatusDetail.setSemester("2025-1");

        group = new Group();
        group.setGroupId("GROUP_001");
        group.setCourse(course);
        group.setClassroom(classroom);

        reviewStep = new ReviewStep();
        reviewStep.setUserId("USER001");
        reviewStep.setUserRole(UserRole.ADMINISTRATOR);
        reviewStep.setAction("APPROVED");
        reviewStep.setComments("Request approved");

        schedule = new Schedule();
        schedule.setScheduleId("SCHED_001");
        schedule.setDayOfWeek("Monday");
        schedule.setStartHour("08:00");
        schedule.setEndHour("10:00");

        Student student = new Student();
        student.setId("STU001");
        student.setName("John Doe");

        studentAcademicProgress = new StudentAcademicProgress();
        studentAcademicProgress.setId("PROG_001");
        studentAcademicProgress.setStudent(student);
        studentAcademicProgress.setFaculty("Engineering");
        studentAcademicProgress.setAcademicProgram("Computer Science");
        studentAcademicProgress.setCurrentSemester(5);
        studentAcademicProgress.setCumulativeGPA(4.5);
    }

    @Test
    @DisplayName("Caso exitoso - createAcademicPeriod crea período académico correctamente")
    void testCreateAcademicPeriod_Exitoso() {
        when(academicPeriodRepository.existsById("PERIOD_001")).thenReturn(false);
        when(academicPeriodRepository.findByIsActive(true)).thenReturn(Collections.emptyList());
        when(academicPeriodRepository.save(any(AcademicPeriod.class))).thenReturn(academicPeriod);

        AcademicPeriod resultado = generalManagementService.createAcademicPeriod(academicPeriod);

        assertAll("Verificar creación de período académico",
                () -> assertNotNull(resultado),
                () -> assertEquals("PERIOD_001", resultado.getPeriodId()),
                () -> assertEquals("2025-1", resultado.getName())
        );

        verify(academicPeriodRepository, times(1)).existsById("PERIOD_001");
        verify(academicPeriodRepository, times(1)).save(academicPeriod);
    }

    @Test
    @DisplayName("Caso error - createAcademicPeriod lanza excepción cuando período ya existe")
    void testCreateAcademicPeriod_PeriodoYaExiste() {
        when(academicPeriodRepository.existsById("PERIOD_001")).thenReturn(true);

        AppException exception = assertThrows(AppException.class, () -> {
            generalManagementService.createAcademicPeriod(academicPeriod);
        });

        assertEquals("Ya existe un período académico con ID: PERIOD_001", exception.getMessage());
        verify(academicPeriodRepository, times(1)).existsById("PERIOD_001");
    }

    @Test
    @DisplayName("Caso exitoso - getAllAcademicPeriods retorna todos los períodos")
    void testGetAllAcademicPeriods_Exitoso() {
        List<AcademicPeriod> periodos = Arrays.asList(academicPeriod);
        when(academicPeriodRepository.findAll()).thenReturn(periodos);

        List<AcademicPeriod> resultado = generalManagementService.getAllAcademicPeriods();

        assertAll("Verificar obtención de todos los períodos",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("PERIOD_001", resultado.get(0).getPeriodId())
        );

        verify(academicPeriodRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Caso exitoso - getAcademicPeriodById retorna período específico")
    void testGetAcademicPeriodById_Exitoso() {
        when(academicPeriodRepository.findById("PERIOD_001")).thenReturn(Optional.of(academicPeriod));

        AcademicPeriod resultado = generalManagementService.getAcademicPeriodById("PERIOD_001");

        assertAll("Verificar obtención de período por ID",
                () -> assertNotNull(resultado),
                () -> assertEquals("PERIOD_001", resultado.getPeriodId()),
                () -> assertEquals("2025-1", resultado.getName())
        );

        verify(academicPeriodRepository, times(1)).findById("PERIOD_001");
    }

    @Test
    @DisplayName("Caso error - getAcademicPeriodById lanza excepción cuando período no existe")
    void testGetAcademicPeriodById_PeriodoNoExiste() {
        when(academicPeriodRepository.findById("PERIOD_INEXISTENTE")).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            generalManagementService.getAcademicPeriodById("PERIOD_INEXISTENTE");
        });

        assertEquals("Período académico no encontrado con ID: PERIOD_INEXISTENTE", exception.getMessage());
        verify(academicPeriodRepository, times(1)).findById("PERIOD_INEXISTENTE");
    }

    @Test
    @DisplayName("Caso exitoso - getCurrentAcademicPeriod retorna período activo")
    void testGetCurrentAcademicPeriod_Exitoso() {
        when(academicPeriodRepository.findByIsActiveTrue()).thenReturn(Optional.of(academicPeriod));

        AcademicPeriod resultado = generalManagementService.getCurrentAcademicPeriod();

        assertAll("Verificar obtención de período activo",
                () -> assertNotNull(resultado),
                () -> assertTrue(resultado.isActive()),
                () -> assertEquals("PERIOD_001", resultado.getPeriodId())
        );

        verify(academicPeriodRepository, times(1)).findByIsActiveTrue();
    }

    @Test
    @DisplayName("Caso error - getCurrentAcademicPeriod lanza excepción cuando no hay período activo")
    void testGetCurrentAcademicPeriod_NoHayPeriodoActivo() {
        when(academicPeriodRepository.findByIsActiveTrue()).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            generalManagementService.getCurrentAcademicPeriod();
        });

        assertEquals("No hay período académico activo", exception.getMessage());
        verify(academicPeriodRepository, times(1)).findByIsActiveTrue();
    }

    @Test
    @DisplayName("Caso exitoso - updateAcademicPeriod actualiza período correctamente")
    void testUpdateAcademicPeriod_Exitoso() {
        when(academicPeriodRepository.existsById("PERIOD_001")).thenReturn(true);
        when(academicPeriodRepository.findByIsActive(true)).thenReturn(Collections.emptyList());
        when(academicPeriodRepository.save(any(AcademicPeriod.class))).thenReturn(academicPeriod);

        AcademicPeriod resultado = generalManagementService.updateAcademicPeriod("PERIOD_001", academicPeriod);

        assertAll("Verificar actualización de período académico",
                () -> assertNotNull(resultado),
                () -> assertEquals("PERIOD_001", resultado.getPeriodId())
        );

        verify(academicPeriodRepository, times(1)).existsById("PERIOD_001");
        verify(academicPeriodRepository, times(1)).save(academicPeriod);
    }

    @Test
    @DisplayName("Caso error - updateAcademicPeriod lanza excepción cuando período no existe")
    void testUpdateAcademicPeriod_PeriodoNoExiste() {
        when(academicPeriodRepository.existsById("PERIOD_INEXISTENTE")).thenReturn(false);

        AppException exception = assertThrows(AppException.class, () -> {
            generalManagementService.updateAcademicPeriod("PERIOD_INEXISTENTE", academicPeriod);
        });

        assertEquals("Período académico no encontrado con ID: PERIOD_INEXISTENTE", exception.getMessage());
        verify(academicPeriodRepository, times(1)).existsById("PERIOD_INEXISTENTE");
    }

    @Test
    @DisplayName("Caso exitoso - deleteAcademicPeriod elimina período inactivo")
    void testDeleteAcademicPeriod_Exitoso() {
        academicPeriod.setActive(false);
        when(academicPeriodRepository.findById("PERIOD_001")).thenReturn(Optional.of(academicPeriod));

        assertDoesNotThrow(() -> generalManagementService.deleteAcademicPeriod("PERIOD_001"));

        verify(academicPeriodRepository, times(1)).findById("PERIOD_001");
        verify(academicPeriodRepository, times(1)).deleteById("PERIOD_001");
    }

    @Test
    @DisplayName("Caso error - deleteAcademicPeriod lanza excepción cuando período está activo")
    void testDeleteAcademicPeriod_PeriodoActivo() {
        when(academicPeriodRepository.findById("PERIOD_001")).thenReturn(Optional.of(academicPeriod));

        AppException exception = assertThrows(AppException.class, () -> {
            generalManagementService.deleteAcademicPeriod("PERIOD_001");
        });

        assertEquals("No se puede eliminar un período académico activo", exception.getMessage());
        verify(academicPeriodRepository, times(1)).findById("PERIOD_001");
    }

    @Test
    @DisplayName("Caso exitoso - activateAcademicPeriod activa período correctamente")
    void testActivateAcademicPeriod_Exitoso() {
        when(academicPeriodRepository.findById("PERIOD_001")).thenReturn(Optional.of(academicPeriod));
        when(academicPeriodRepository.findByIsActive(true)).thenReturn(Collections.emptyList());
        when(academicPeriodRepository.save(any(AcademicPeriod.class))).thenReturn(academicPeriod);

        AcademicPeriod resultado = generalManagementService.activateAcademicPeriod("PERIOD_001");

        assertAll("Verificar activación de período académico",
                () -> assertNotNull(resultado),
                () -> assertTrue(resultado.isActive())
        );

        verify(academicPeriodRepository, times(1)).findById("PERIOD_001");
        verify(academicPeriodRepository, times(1)).save(academicPeriod);
    }

    @Test
    @DisplayName("Caso exitoso - createClassroom crea aula correctamente")
    void testCreateClassroom_Exitoso() {
        when(classroomRepository.save(any(Classroom.class))).thenReturn(classroom);

        Classroom resultado = generalManagementService.createClassroom(classroom);

        assertAll("Verificar creación de aula",
                () -> assertNotNull(resultado),
                () -> assertEquals("CLASS_001", resultado.getClassroomId()),
                () -> assertEquals("A", resultado.getBuilding())
        );

        verify(classroomRepository, times(1)).save(classroom);
    }

    @Test
    @DisplayName("Caso exitoso - getAllClassrooms retorna todas las aulas")
    void testGetAllClassrooms_Exitoso() {
        List<Classroom> aulas = Arrays.asList(classroom);
        when(classroomRepository.findAll()).thenReturn(aulas);

        List<Classroom> resultado = generalManagementService.getAllClassrooms();

        assertAll("Verificar obtención de todas las aulas",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("CLASS_001", resultado.get(0).getClassroomId())
        );

        verify(classroomRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Caso exitoso - getClassroomById retorna aula específica")
    void testGetClassroomById_Exitoso() {
        when(classroomRepository.findById("CLASS_001")).thenReturn(Optional.of(classroom));

        Classroom resultado = generalManagementService.getClassroomById("CLASS_001");

        assertAll("Verificar obtención de aula por ID",
                () -> assertNotNull(resultado),
                () -> assertEquals("CLASS_001", resultado.getClassroomId()),
                () -> assertEquals("A", resultado.getBuilding())
        );

        verify(classroomRepository, times(1)).findById("CLASS_001");
    }

    @Test
    @DisplayName("Caso exitoso - updateClassroom actualiza aula correctamente")
    void testUpdateClassroom_Exitoso() {
        when(classroomRepository.existsById("CLASS_001")).thenReturn(true);
        when(classroomRepository.save(any(Classroom.class))).thenReturn(classroom);

        Classroom resultado = generalManagementService.updateClassroom("CLASS_001", classroom);

        assertAll("Verificar actualización de aula",
                () -> assertNotNull(resultado),
                () -> assertEquals("CLASS_001", resultado.getClassroomId())
        );

        verify(classroomRepository, times(1)).existsById("CLASS_001");
        verify(classroomRepository, times(1)).save(classroom);
    }

    @Test
    @DisplayName("Caso exitoso - deleteClassroom elimina aula sin grupos")
    void testDeleteClassroom_Exitoso() {
        when(classroomRepository.existsById("CLASS_001")).thenReturn(true);
        when(groupRepository.findByClassroom_ClassroomId("CLASS_001")).thenReturn(Collections.emptyList());

        assertDoesNotThrow(() -> generalManagementService.deleteClassroom("CLASS_001"));

        verify(classroomRepository, times(1)).existsById("CLASS_001");
        verify(groupRepository, times(1)).findByClassroom_ClassroomId("CLASS_001");
        verify(classroomRepository, times(1)).deleteById("CLASS_001");
    }

    @Test
    @DisplayName("Caso error - deleteClassroom lanza excepción cuando aula tiene grupos")
    void testDeleteClassroom_AulaConGrupos() {
        when(classroomRepository.existsById("CLASS_001")).thenReturn(true);
        when(groupRepository.findByClassroom_ClassroomId("CLASS_001")).thenReturn(Arrays.asList(group));

        AppException exception = assertThrows(AppException.class, () -> {
            generalManagementService.deleteClassroom("CLASS_001");
        });

        assertTrue(exception.getMessage().contains("No se puede eliminar el aula porque está siendo utilizada"));
        verify(classroomRepository, times(1)).existsById("CLASS_001");
        verify(groupRepository, times(1)).findByClassroom_ClassroomId("CLASS_001");
    }

    @Test
    @DisplayName("Caso exitoso - getClassroomsByType retorna aulas por tipo")
    void testGetClassroomsByType_Exitoso() {
        List<Classroom> aulas = Arrays.asList(classroom);
        when(classroomRepository.findByRoomType(RoomType.LABORATORY)).thenReturn(aulas);

        List<Classroom> resultado = generalManagementService.getClassroomsByType(RoomType.LABORATORY);

        assertAll("Verificar obtención de aulas por tipo",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals(RoomType.LABORATORY, resultado.get(0).getRoomType())
        );

        verify(classroomRepository, times(1)).findByRoomType(RoomType.LABORATORY);
    }

    @Test
    @DisplayName("Caso exitoso - getClassroomsByType con String retorna aulas por tipo")
    void testGetClassroomsByType_StringExitoso() {
        List<Classroom> aulas = Arrays.asList(classroom);
        when(classroomRepository.findByRoomType(RoomType.LABORATORY)).thenReturn(aulas);

        List<Classroom> resultado = generalManagementService.getClassroomsByType("laboratory");

        assertAll("Verificar obtención de aulas por tipo con String",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals(RoomType.LABORATORY, resultado.get(0).getRoomType())
        );

        verify(classroomRepository, times(1)).findByRoomType(RoomType.LABORATORY);
    }

    @Test
    @DisplayName("Caso error - getClassroomsByType con String inválido lanza excepción")
    void testGetClassroomsByType_StringInvalido() {
        AppException exception = assertThrows(AppException.class, () -> {
            generalManagementService.getClassroomsByType("tipo_invalido");
        });

        assertEquals("Tipo de aula no válido: tipo_invalido", exception.getMessage());
    }

    @Test
    @DisplayName("Caso exitoso - createCourse crea curso correctamente")
    void testCreateCourse_Exitoso() {
        when(courseRepository.existsById("CS101")).thenReturn(false);
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        Course resultado = generalManagementService.createCourse(course);

        assertAll("Verificar creación de curso",
                () -> assertNotNull(resultado),
                () -> assertEquals("CS101", resultado.getCourseCode()),
                () -> assertEquals("Introduction to Computer Science", resultado.getName())
        );

        verify(courseRepository, times(1)).existsById("CS101");
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    @DisplayName("Caso error - createCourse lanza excepción cuando curso ya existe")
    void testCreateCourse_CursoYaExiste() {
        when(courseRepository.existsById("CS101")).thenReturn(true);

        AppException exception = assertThrows(AppException.class, () -> {
            generalManagementService.createCourse(course);
        });

        assertEquals("Ya existe un curso con código: CS101", exception.getMessage());
        verify(courseRepository, times(1)).existsById("CS101");
    }

    @Test
    @DisplayName("Caso exitoso - getAllCourses retorna todos los cursos")
    void testGetAllCourses_Exitoso() {
        List<Course> cursos = Arrays.asList(course);
        when(courseRepository.findAll()).thenReturn(cursos);

        List<Course> resultado = generalManagementService.getAllCourses();

        assertAll("Verificar obtención de todos los cursos",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("CS101", resultado.get(0).getCourseCode())
        );

        verify(courseRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Caso exitoso - getCourseByCode retorna curso específico")
    void testGetCourseByCode_Exitoso() {
        when(courseRepository.findById("CS101")).thenReturn(Optional.of(course));

        Course resultado = generalManagementService.getCourseByCode("CS101");

        assertAll("Verificar obtención de curso por código",
                () -> assertNotNull(resultado),
                () -> assertEquals("CS101", resultado.getCourseCode()),
                () -> assertEquals("Introduction to Computer Science", resultado.getName())
        );

        verify(courseRepository, times(1)).findById("CS101");
    }

    @Test
    @DisplayName("Caso exitoso - updateCourse actualiza curso correctamente")
    void testUpdateCourse_Exitoso() {
        when(courseRepository.existsById("CS101")).thenReturn(true);
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        Course resultado = generalManagementService.updateCourse("CS101", course);

        assertAll("Verificar actualización de curso",
                () -> assertNotNull(resultado),
                () -> assertEquals("CS101", resultado.getCourseCode())
        );

        verify(courseRepository, times(1)).existsById("CS101");
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    @DisplayName("Caso exitoso - deleteCourse elimina curso sin grupos")
    void testDeleteCourse_Exitoso() {
        when(courseRepository.existsById("CS101")).thenReturn(true);
        when(groupRepository.findByCourse_CourseCode("CS101")).thenReturn(Collections.emptyList());

        assertDoesNotThrow(() -> generalManagementService.deleteCourse("CS101"));

        verify(courseRepository, times(1)).existsById("CS101");
        verify(groupRepository, times(1)).findByCourse_CourseCode("CS101");
        verify(courseRepository, times(1)).deleteById("CS101");
    }

    @Test
    @DisplayName("Caso error - deleteCourse lanza excepción cuando curso tiene grupos")
    void testDeleteCourse_CursoConGrupos() {
        when(courseRepository.existsById("CS101")).thenReturn(true);
        when(groupRepository.findByCourse_CourseCode("CS101")).thenReturn(Arrays.asList(group));

        AppException exception = assertThrows(AppException.class, () -> {
            generalManagementService.deleteCourse("CS101");
        });

        assertTrue(exception.getMessage().contains("No se puede eliminar el curso porque está siendo utilizado"));
        verify(courseRepository, times(1)).existsById("CS101");
        verify(groupRepository, times(1)).findByCourse_CourseCode("CS101");
    }

    @Test
    @DisplayName("Caso exitoso - createCourseStatusDetail crea detalle correctamente")
    void testCreateCourseStatusDetail_Exitoso() {
        when(courseStatusDetailRepository.save(any(CourseStatusDetail.class))).thenReturn(courseStatusDetail);

        CourseStatusDetail resultado = generalManagementService.createCourseStatusDetail(courseStatusDetail);

        assertAll("Verificar creación de detalle de estado de curso",
                () -> assertNotNull(resultado),
                () -> assertEquals("CSD_001", resultado.getId()),
                () -> assertEquals("STU001", resultado.getStudentId())
        );

        verify(courseStatusDetailRepository, times(1)).save(courseStatusDetail);
    }

    @Test
    @DisplayName("Caso exitoso - getAllCourseStatusDetails retorna todos los detalles")
    void testGetAllCourseStatusDetails_Exitoso() {
        List<CourseStatusDetail> detalles = Arrays.asList(courseStatusDetail);
        when(courseStatusDetailRepository.findAll()).thenReturn(detalles);

        List<CourseStatusDetail> resultado = generalManagementService.getAllCourseStatusDetails();

        assertAll("Verificar obtención de todos los detalles",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("CSD_001", resultado.get(0).getId())
        );

        verify(courseStatusDetailRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Caso exitoso - getCourseStatusDetailById retorna detalle específico")
    void testGetCourseStatusDetailById_Exitoso() {
        when(courseStatusDetailRepository.findById("CSD_001")).thenReturn(Optional.of(courseStatusDetail));

        CourseStatusDetail resultado = generalManagementService.getCourseStatusDetailById("CSD_001");

        assertAll("Verificar obtención de detalle por ID",
                () -> assertNotNull(resultado),
                () -> assertEquals("CSD_001", resultado.getId()),
                () -> assertEquals("STU001", resultado.getStudentId())
        );

        verify(courseStatusDetailRepository, times(1)).findById("CSD_001");
    }

    @Test
    @DisplayName("Caso exitoso - deleteGroup elimina grupo sin detalles")
    void testDeleteGroup_Exitoso() {
        when(groupRepository.findById("GROUP_001")).thenReturn(Optional.of(group));
        when(courseStatusDetailRepository.findByGroup_GroupId("GROUP_001")).thenReturn(Collections.emptyList());

        assertDoesNotThrow(() -> generalManagementService.deleteGroup("GROUP_001"));

        verify(groupRepository, times(1)).findById("GROUP_001");
        verify(courseStatusDetailRepository, times(1)).findByGroup_GroupId("GROUP_001");
        verify(groupRepository, times(1)).deleteById("GROUP_001");
    }

    @Test
    @DisplayName("Caso error - deleteGroup lanza excepción cuando grupo tiene detalles")
    void testDeleteGroup_GrupoConDetalles() {
        when(groupRepository.findById("GROUP_001")).thenReturn(Optional.of(group));
        when(courseStatusDetailRepository.findByGroup_GroupId("GROUP_001")).thenReturn(Arrays.asList(courseStatusDetail));

        AppException exception = assertThrows(AppException.class, () -> {
            generalManagementService.deleteGroup("GROUP_001");
        });

        assertTrue(exception.getMessage().contains("No se puede eliminar el grupo porque está siendo utilizado"));
        verify(groupRepository, times(1)).findById("GROUP_001");
        verify(courseStatusDetailRepository, times(1)).findByGroup_GroupId("GROUP_001");
    }

    @Test
    @DisplayName("Caso exitoso - createReviewStep crea paso de revisión correctamente")
    void testCreateReviewStep_Exitoso() {
        when(reviewStepRepository.save(any(ReviewStep.class))).thenReturn(reviewStep);

        ReviewStep resultado = generalManagementService.createReviewStep(reviewStep);

        assertAll("Verificar creación de paso de revisión",
                () -> assertNotNull(resultado),
                () -> assertEquals("USER001", resultado.getUserId())
        );

        verify(reviewStepRepository, times(1)).save(reviewStep);
    }

    @Test
    @DisplayName("Caso exitoso - getReviewStepsByUserRole retorna pasos por rol")
    void testGetReviewStepsByUserRole_Exitoso() {
        List<ReviewStep> pasos = Arrays.asList(reviewStep);
        when(reviewStepRepository.findByUserRole(UserRole.ADMINISTRATOR)).thenReturn(pasos);

        List<ReviewStep> resultado = generalManagementService.getReviewStepsByUserRole(UserRole.ADMINISTRATOR);

        assertAll("Verificar obtención de pasos por rol",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals(UserRole.ADMINISTRATOR, resultado.get(0).getUserRole())
        );

        verify(reviewStepRepository, times(1)).findByUserRole(UserRole.ADMINISTRATOR);
    }

    @Test
    @DisplayName("Caso exitoso - createSchedule crea horario correctamente")
    void testCreateSchedule_Exitoso() {
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(schedule);

        Schedule resultado = generalManagementService.createSchedule(schedule);

        assertAll("Verificar creación de horario",
                () -> assertNotNull(resultado),
                () -> assertEquals("SCHED_001", resultado.getScheduleId()),
                () -> assertEquals("Monday", resultado.getDayOfWeek())
        );

        verify(scheduleRepository, times(1)).save(schedule);
    }

    @Test
    @DisplayName("Caso exitoso - deleteSchedule elimina horario sin grupos")
    void testDeleteSchedule_Exitoso() {
        when(scheduleRepository.existsById("SCHED_001")).thenReturn(true);
        when(groupRepository.findBySchedule_ScheduleId("SCHED_001")).thenReturn(Collections.emptyList());

        assertDoesNotThrow(() -> generalManagementService.deleteSchedule("SCHED_001"));

        verify(scheduleRepository, times(1)).existsById("SCHED_001");
        verify(groupRepository, times(1)).findBySchedule_ScheduleId("SCHED_001");
        verify(scheduleRepository, times(1)).deleteById("SCHED_001");
    }

    @Test
    @DisplayName("Caso exitoso - createStudentAcademicProgress crea progreso correctamente")
    void testCreateStudentAcademicProgress_Exitoso() {
        when(studentAcademicProgressRepository.save(any(StudentAcademicProgress.class))).thenReturn(studentAcademicProgress);

        StudentAcademicProgress resultado = generalManagementService.createStudentAcademicProgress(studentAcademicProgress);

        assertAll("Verificar creación de progreso académico",
                () -> assertNotNull(resultado),
                () -> assertEquals("PROG_001", resultado.getId()),
                () -> assertEquals("Engineering", resultado.getFaculty())
        );

        verify(studentAcademicProgressRepository, times(1)).save(studentAcademicProgress);
    }

    @Test
    @DisplayName("Caso exitoso - getStudentAcademicProgressByStudentId retorna progreso específico")
    void testGetStudentAcademicProgressByStudentId_Exitoso() {
        when(studentAcademicProgressRepository.findByStudentId("STU001")).thenReturn(Optional.of(studentAcademicProgress));

        StudentAcademicProgress resultado = generalManagementService.getStudentAcademicProgressByStudentId("STU001");

        assertAll("Verificar obtención de progreso por ID de estudiante",
                () -> assertNotNull(resultado),
                () -> assertEquals("PROG_001", resultado.getId()),
                () -> assertEquals("STU001", resultado.getStudent().getId())
        );

        verify(studentAcademicProgressRepository, times(1)).findByStudentId("STU001");
    }

    @Test
    @DisplayName("Caso exitoso - checkReferentialIntegrity retorna true para entidad sin referencias")
    void testCheckReferentialIntegrity_SinReferencias() {
        when(groupRepository.findByClassroom_ClassroomId("CLASS_001")).thenReturn(Collections.emptyList());

        boolean resultado = generalManagementService.checkReferentialIntegrity("classroom", "CLASS_001");

        assertTrue(resultado);
        verify(groupRepository, times(1)).findByClassroom_ClassroomId("CLASS_001");
    }

    @Test
    @DisplayName("Caso exitoso - checkReferentialIntegrity retorna false para entidad con referencias")
    void testCheckReferentialIntegrity_ConReferencias() {
        when(groupRepository.findByClassroom_ClassroomId("CLASS_001")).thenReturn(Arrays.asList(group));

        boolean resultado = generalManagementService.checkReferentialIntegrity("classroom", "CLASS_001");

        assertFalse(resultado);
        verify(groupRepository, times(1)).findByClassroom_ClassroomId("CLASS_001");
    }

    @Test
    @DisplayName("Caso exitoso - getSystemStatistics genera estadísticas correctamente")
    void testGetSystemStatistics_Exitoso() {
        when(classroomRepository.count()).thenReturn(10L);
        when(courseRepository.count()).thenReturn(20L);
        when(groupRepository.count()).thenReturn(30L);
        when(scheduleRepository.count()).thenReturn(40L);
        when(academicPeriodRepository.count()).thenReturn(5L);
        when(academicPeriodRepository.findByIsActive(true)).thenReturn(Arrays.asList(academicPeriod));

        GeneralManagementService.SystemStatistics resultado = generalManagementService.getSystemStatistics();

        assertAll("Verificar generación de estadísticas del sistema",
                () -> assertNotNull(resultado),
                () -> assertEquals(10L, resultado.getTotalClassrooms()),
                () -> assertEquals(20L, resultado.getTotalCourses()),
                () -> assertEquals(30L, resultado.getTotalGroups()),
                () -> assertEquals(40L, resultado.getTotalSchedules()),
                () -> assertEquals(5L, resultado.getTotalAcademicPeriods()),
                () -> assertEquals(1L, resultado.getActiveAcademicPeriods())
        );

        verify(classroomRepository, times(1)).count();
        verify(courseRepository, times(1)).count();
        verify(groupRepository, times(1)).count();
        verify(scheduleRepository, times(1)).count();
        verify(academicPeriodRepository, times(1)).count();
        verify(academicPeriodRepository, times(1)).findByIsActive(true);
    }

    @Test
    @DisplayName("Caso borde - getSystemStatistics con datos vacíos")
    void testGetSystemStatistics_DatosVacios() {
        when(classroomRepository.count()).thenReturn(0L);
        when(courseRepository.count()).thenReturn(0L);
        when(groupRepository.count()).thenReturn(0L);
        when(scheduleRepository.count()).thenReturn(0L);
        when(academicPeriodRepository.count()).thenReturn(0L);
        when(academicPeriodRepository.findByIsActive(true)).thenReturn(Collections.emptyList());

        GeneralManagementService.SystemStatistics resultado = generalManagementService.getSystemStatistics();

        assertAll("Verificar estadísticas con datos vacíos",
                () -> assertNotNull(resultado),
                () -> assertEquals(0L, resultado.getTotalClassrooms()),
                () -> assertEquals(0L, resultado.getTotalCourses()),
                () -> assertEquals(0L, resultado.getTotalGroups()),
                () -> assertEquals(0L, resultado.getTotalSchedules()),
                () -> assertEquals(0L, resultado.getTotalAcademicPeriods()),
                () -> assertEquals(0L, resultado.getActiveAcademicPeriods())
        );
    }
}
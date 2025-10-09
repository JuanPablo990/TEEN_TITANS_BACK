package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio que gestiona la información académica y el portal estudiantil.
 * <p>
 * Permite a los estudiantes consultar su horario actual, progreso académico,
 * grupos disponibles, recomendaciones de cursos y alertas académicas.
 * Extiende {@link GroupService} para reutilizar la lógica de grupos.
 * </p>
 */
@Service
public class StudentPortalService extends GroupService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentAcademicProgressRepository studentAcademicProgressRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AcademicPeriodRepository academicPeriodRepository;

    @Autowired
    private CourseStatusDetailRepository courseStatusDetailRepository;

    @Autowired
    private GroupRepository groupRepository;

    /**
     * Obtiene el curso asociado a un grupo.
     *
     * @param groupId identificador único del grupo
     * @return el curso del grupo, o {@code null} si no existe
     */
    @Override
    public Course getCourse(String groupId) {
        return groupRepository.findById(groupId)
                .map(Group::getCourse)
                .orElse(null);
    }

    /**
     * Obtiene la capacidad máxima del aula de un grupo.
     *
     * @param groupId identificador del grupo
     * @return la capacidad del aula o 0 si no se encuentra
     */
    @Override
    public Integer getMaxCapacity(String groupId) {
        return groupRepository.findById(groupId)
                .map(Group::getClassroom)
                .map(Classroom::getCapacity)
                .orElse(0);
    }

    /**
     * Calcula el número estimado de estudiantes inscritos actualmente en un grupo.
     *
     * @param groupId identificador del grupo
     * @return número estimado de estudiantes inscritos
     */
    @Override
    public Integer getCurrentEnrollment(String groupId) {
        return groupRepository.findById(groupId)
                .map(group -> (int) (group.getClassroom().getCapacity() * 0.6))
                .orElse(0);
    }

    /**
     * Retorna la lista de solicitudes de cambio de horario pendientes para un grupo.
     *
     * @param groupId identificador del grupo
     * @return lista vacía por defecto
     */
    @Override
    public List<ScheduleChangeRequest> getWaitingList(String groupId) {
        return Collections.emptyList();
    }

    /**
     * Obtiene el total de estudiantes inscritos por grupo dentro de un curso.
     *
     * @param courseCode código del curso
     * @return mapa con los IDs de grupo y el número de inscritos
     */
    @Override
    public Map<String, Integer> getTotalEnrolledByCourse(String courseCode) {
        return groupRepository.findByCourse_CourseCode(courseCode).stream()
                .collect(Collectors.toMap(
                        Group::getGroupId,
                        group -> getCurrentEnrollment(group.getGroupId())
                ));
    }

    /**
     * Obtiene el horario actual del estudiante, basado en los cursos en los que está inscrito.
     *
     * @param studentId identificador del estudiante
     * @return lista de grupos en los que está inscrito actualmente
     */
    public List<Group> getCurrentSchedule(String studentId) {
        return studentAcademicProgressRepository.findById(studentId)
                .map(StudentAcademicProgress::getCoursesStatus)
                .orElse(Collections.emptyList())
                .stream()
                .filter(this::isCourseCurrentlyEnrolled)
                .map(CourseStatusDetail::getCourse)
                .map(Course::getCourseCode)
                .map(groupRepository::findByCourse_CourseCode)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene los grupos disponibles para un curso específico.
     *
     * @param courseCode código del curso
     * @return lista de grupos con cupos disponibles
     */
    public List<Group> getAvailableGroups(String courseCode) {
        return groupRepository.findByCourse_CourseCode(courseCode).stream()
                .filter(group -> checkGroupAvailability(group.getGroupId()))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene el progreso académico completo de un estudiante.
     *
     * @param studentId identificador del estudiante
     * @return objeto {@link StudentAcademicProgress} o {@code null} si no existe
     */
    public StudentAcademicProgress getAcademicProgress(String studentId) {
        return studentAcademicProgressRepository.findById(studentId).orElse(null);
    }

    /**
     * Verifica si un grupo tiene disponibilidad de cupos.
     *
     * @param groupId identificador del grupo
     * @return {@code true} si hay cupos disponibles, {@code false} en caso contrario
     */
    public boolean checkGroupAvailability(String groupId) {
        Integer maxCapacity = getMaxCapacity(groupId);
        Integer currentEnrollment = getCurrentEnrollment(groupId);
        return currentEnrollment < maxCapacity;
    }

    /**
     * Genera una lista de recomendaciones de cursos para un estudiante
     * según su programa académico y los cursos que aún no ha tomado.
     *
     * @param studentId identificador del estudiante
     * @return lista de cursos recomendados
     */
    public List<Course> getCourseRecommendations(String studentId) {
        return studentAcademicProgressRepository.findById(studentId)
                .map(progress -> {
                    Set<String> takenCourses = progress.getCoursesStatus().stream()
                            .map(CourseStatusDetail::getCourse)
                            .map(Course::getCourseCode)
                            .collect(Collectors.toSet());

                    return courseRepository.findByAcademicProgramAndIsActive(
                                    progress.getAcademicProgram(), true).stream()
                            .filter(course -> !takenCourses.contains(course.getCourseCode()))
                            .filter(course -> isCourseRecommended(course, progress))
                            .collect(Collectors.toList());
                })
                .orElse(Collections.emptyList());
    }

    /**
     * Obtiene el periodo académico actual y sus fechas de inscripción.
     *
     * @return el periodo académico activo o {@code null} si no hay uno activo
     */
    public AcademicPeriod getEnrollmentDeadlines() {
        return academicPeriodRepository.findByIsActive(true).stream()
                .findFirst()
                .orElse(null);
    }

    /**
     * Genera una lista de alertas académicas personalizadas para un estudiante.
     *
     * @param studentId identificador del estudiante
     * @return lista de alertas académicas o mensaje de error si no hay información
     */
    public List<String> getAcademicAlerts(String studentId) {
        return studentAcademicProgressRepository.findById(studentId)
                .map(this::generateAcademicAlerts)
                .orElse(Arrays.asList("No se encontró información académica del estudiante"));
    }

    /**
     * Verifica si un curso está actualmente en progreso (inscrito y no finalizado).
     *
     * @param courseStatus objeto {@link CourseStatusDetail}
     * @return {@code true} si el curso está en progreso, {@code false} en caso contrario
     */
    private boolean isCourseCurrentlyEnrolled(CourseStatusDetail courseStatus) {
        return courseStatus.isApproved() == null ||
                (courseStatus.getEnrollmentDate() != null &&
                        courseStatus.getCompletionDate() == null);
    }

    /**
     * Determina si un curso es recomendado para un estudiante según su programa académico.
     *
     * @param course   curso a evaluar
     * @param progress progreso académico del estudiante
     * @return {@code true} si el curso pertenece al mismo programa y está activo
     */
    private boolean isCourseRecommended(Course course, StudentAcademicProgress progress) {
        return course.isActive() &&
                course.getAcademicProgram().equals(progress.getAcademicProgram());
    }

    /**
     * Genera alertas académicas basadas en el progreso del estudiante,
     * incluyendo GPA bajo, avance de créditos y materias reprobadas.
     *
     * @param progress objeto {@link StudentAcademicProgress} del estudiante
     * @return lista de mensajes de alerta académica
     */
    private List<String> generateAcademicAlerts(StudentAcademicProgress progress) {
        List<String> alerts = new ArrayList<>();

        Optional.ofNullable(progress.getCumulativeGPA())
                .filter(gpa -> gpa < 3.0)
                .ifPresent(gpa -> alerts.add(
                        "Alerta: GPA acumulado por debajo del mínimo requerido (" + gpa + ")"));

        double progressPercentage =
                (double) progress.getCompletedCredits() / progress.getTotalCreditsRequired();
        double expectedProgress =
                getExpectedProgress(progress.getCurrentSemester(), progress.getTotalSemesters());

        if (progressPercentage < expectedProgress) {
            alerts.add("Alerta: Progreso de créditos por debajo del esperado para el semestre actual");
        }

        long failedCoursesCount = progress.getCoursesStatus().stream()
                .filter(cs -> cs.isApproved() != null && !cs.isApproved())
                .count();

        if (failedCoursesCount > 0) {
            alerts.add("Tiene " + failedCoursesCount + " materia(s) reprobada(s) que debe repetir");
        }

        return alerts;
    }

    /**
     * Calcula el progreso esperado en créditos según el semestre actual.
     *
     * @param currentSemester número de semestre actual
     * @param totalSemesters  número total de semestres del programa
     * @return progreso esperado expresado como porcentaje (0 a 1)
     */
    private double getExpectedProgress(int currentSemester, int totalSemesters) {
        return (double) currentSemester / totalSemesters;
    }
}

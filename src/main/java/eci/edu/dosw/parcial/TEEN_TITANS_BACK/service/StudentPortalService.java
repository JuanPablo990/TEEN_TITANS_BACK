package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
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
     * @return el curso del grupo
     * @throws AppException si no se encuentra el grupo
     */
    @Override
    public Course getCourse(String groupId) {
        return groupRepository.findById(groupId)
                .map(Group::getCourse)
                .orElseThrow(() -> new AppException("Grupo no encontrado: " + groupId));
    }

    /**
     * Obtiene la capacidad máxima del aula de un grupo.
     *
     * @param groupId identificador del grupo
     * @return la capacidad del aula
     * @throws AppException si no se encuentra el grupo
     */
    @Override
    public Integer getMaxCapacity(String groupId) {
        return groupRepository.findById(groupId)
                .map(Group::getClassroom)
                .map(Classroom::getCapacity)
                .orElseThrow(() -> new AppException("Grupo no encontrado: " + groupId));
    }

    /**
     * Calcula el número estimado de estudiantes inscritos actualmente en un grupo.
     *
     * @param groupId identificador del grupo
     * @return número estimado de estudiantes inscritos
     * @throws AppException si no se encuentra el grupo
     */
    @Override
    public Integer getCurrentEnrollment(String groupId) {
        return groupRepository.findById(groupId)
                .map(group -> (int) (group.getClassroom().getCapacity() * 0.6))
                .orElseThrow(() -> new AppException("Grupo no encontrado: " + groupId));
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
     * @throws AppException si no se encuentran grupos para el curso
     */
    @Override
    public Map<String, Integer> getTotalEnrolledByCourse(String courseCode) {
        List<Group> groups = groupRepository.findByCourse_CourseCode(courseCode);
        if (groups.isEmpty()) {
            throw new AppException("No se encontraron grupos para el curso: " + courseCode);
        }

        return groups.stream()
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
     * @throws AppException si no se encuentra el progreso académico del estudiante
     */
    public List<Group> getCurrentSchedule(String studentId) {
        StudentAcademicProgress progress = studentAcademicProgressRepository.findById(studentId)
                .orElseThrow(() -> new AppException("Progreso académico no encontrado para el estudiante: " + studentId));

        return progress.getCoursesStatus().stream()
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
     * @throws AppException si no se encuentran grupos para el curso
     */
    public List<Group> getAvailableGroups(String courseCode) {
        List<Group> groups = groupRepository.findByCourse_CourseCode(courseCode);
        if (groups.isEmpty()) {
            throw new AppException("No se encontraron grupos para el curso: " + courseCode);
        }

        return groups.stream()
                .filter(group -> checkGroupAvailability(group.getGroupId()))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene el progreso académico completo de un estudiante.
     *
     * @param studentId identificador del estudiante
     * @return objeto {@link StudentAcademicProgress}
     * @throws AppException si no se encuentra el progreso académico
     */
    public StudentAcademicProgress getAcademicProgress(String studentId) {
        return studentAcademicProgressRepository.findById(studentId)
                .orElseThrow(() -> new AppException("Progreso académico no encontrado para el estudiante: " + studentId));
    }

    /**
     * Verifica si un grupo tiene disponibilidad de cupos.
     *
     * @param groupId identificador del grupo
     * @return {@code true} si hay cupos disponibles, {@code false} en caso contrario
     * @throws AppException si no se encuentra el grupo
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
     * @throws AppException si no se encuentra el progreso académico del estudiante
     */
    public List<Course> getCourseRecommendations(String studentId) {
        StudentAcademicProgress progress = studentAcademicProgressRepository.findById(studentId)
                .orElseThrow(() -> new AppException("Progreso académico no encontrado para el estudiante: " + studentId));

        Set<String> takenCourses = progress.getCoursesStatus().stream()
                .map(CourseStatusDetail::getCourse)
                .map(Course::getCourseCode)
                .collect(Collectors.toSet());

        return courseRepository.findByAcademicProgramAndIsActive(
                        progress.getAcademicProgram(), true).stream()
                .filter(course -> !takenCourses.contains(course.getCourseCode()))
                .filter(course -> isCourseRecommended(course, progress))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene el periodo académico actual y sus fechas de inscripción.
     *
     * @return el periodo académico activo
     * @throws AppException si no hay periodos académicos activos
     */
    public AcademicPeriod getEnrollmentDeadlines() {
        return academicPeriodRepository.findByIsActive(true).stream()
                .findFirst()
                .orElseThrow(() -> new AppException("No hay periodos académicos activos"));
    }

    /**
     * Genera una lista de alertas académicas personalizadas para un estudiante.
     *
     * @param studentId identificador del estudiante
     * @return lista de alertas académicas
     * @throws AppException si no se encuentra información académica del estudiante
     */
    public List<String> getAcademicAlerts(String studentId) {
        StudentAcademicProgress progress = studentAcademicProgressRepository.findById(studentId)
                .orElseThrow(() -> new AppException("No se encontró información académica del estudiante: " + studentId));

        return generateAcademicAlerts(progress);
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
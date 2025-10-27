package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio que gestiona la información académica y el portal estudiantil.
 * Permite a los estudiantes consultar su horario actual, progreso académico,
 * grupos disponibles, recomendaciones de cursos y alertas académicas.
 *  * @author Equipo Teen Titans
 *  * @version 2.0
 *  * @since 2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StudentPortalService {

    private final StudentRepository studentRepository;
    private final StudentAcademicProgressRepository studentAcademicProgressRepository;
    private final CourseRepository courseRepository;
    private final AcademicPeriodRepository academicPeriodRepository;
    private final CourseStatusDetailRepository courseStatusDetailRepository;
    private final GroupRepository groupRepository;

    /**
     * Obtiene el curso asociado a un grupo.
     *
     * @param groupId identificador único del grupo
     * @return el curso del grupo
     * @throws AppException si no se encuentra el grupo
     */
    public Course getCourse(String groupId) {
        log.debug("Obteniendo curso para el grupo: {}", groupId);
        return groupRepository.findById(groupId)
                .map(Group::getCourse)
                .orElseThrow(() -> {
                    log.error("Grupo no encontrado: {}", groupId);
                    return new AppException("Grupo no encontrado: " + groupId);
                });
    }

    /**
     * Obtiene la capacidad máxima del aula de un grupo.
     *
     * @param groupId identificador del grupo
     * @return la capacidad del aula
     * @throws AppException si no se encuentra el grupo
     */
    public Integer getMaxCapacity(String groupId) {
        log.debug("Obteniendo capacidad máxima para el grupo: {}", groupId);
        return groupRepository.findById(groupId)
                .map(Group::getClassroom)
                .map(Classroom::getCapacity)
                .orElseThrow(() -> {
                    log.error("Grupo no encontrado: {}", groupId);
                    return new AppException("Grupo no encontrado: " + groupId);
                });
    }

    /**
     * Calcula el número estimado de estudiantes inscritos actualmente en un grupo.
     *
     * @param groupId identificador del grupo
     * @return número estimado de estudiantes inscritos
     * @throws AppException si no se encuentra el grupo
     */
    public Integer getCurrentEnrollment(String groupId) {
        log.debug("Calculando matrícula actual para el grupo: {}", groupId);
        return groupRepository.findById(groupId)
                .map(group -> {
                    int estimatedEnrollment = (int) (group.getClassroom().getCapacity() * 0.6);
                    log.info("Matrícula estimada del grupo {}: {}", groupId, estimatedEnrollment);
                    return estimatedEnrollment;
                })
                .orElseThrow(() -> {
                    log.error("Grupo no encontrado: {}", groupId);
                    return new AppException("Grupo no encontrado: " + groupId);
                });
    }

    /**
     * Retorna la lista de solicitudes de cambio de horario pendientes para un grupo.
     *
     * @param groupId identificador del grupo
     * @return lista vacía por defecto
     */
    public List<ScheduleChangeRequest> getWaitingList(String groupId) {
        log.debug("Obteniendo lista de espera para el grupo: {}", groupId);
        return Collections.emptyList();
    }

    /**
     * Obtiene el total de estudiantes inscritos por grupo dentro de un curso.
     *
     * @param courseCode código del curso
     * @return mapa con los IDs de grupo y el número de inscritos
     * @throws AppException si no se encuentran grupos para el curso
     */
    public Map<String, Integer> getTotalEnrolledByCourse(String courseCode) {
        log.debug("Obteniendo total de inscritos por grupo para el curso: {}", courseCode);
        List<Group> groups = groupRepository.findByCourse_CourseCode(courseCode);

        if (groups.isEmpty()) {
            log.warn("No se encontraron grupos para el curso: {}", courseCode);
            throw new AppException("No se encontraron grupos para el curso: " + courseCode);
        }

        Map<String, Integer> enrollmentMap = groups.stream()
                .collect(Collectors.toMap(
                        Group::getGroupId,
                        group -> getCurrentEnrollment(group.getGroupId())
                ));

        log.info("Total de inscritos por grupo para el curso {}: {} grupos", courseCode, enrollmentMap.size());
        return enrollmentMap;
    }

    /**
     * Obtiene el horario actual del estudiante, basado en los cursos en los que está inscrito.
     *
     * @param studentId identificador del estudiante
     * @return lista de grupos en los que está inscrito actualmente
     * @throws AppException si no se encuentra el progreso académico del estudiante
     */
    public List<Group> getCurrentSchedule(String studentId) {
        log.debug("Obteniendo horario actual para el estudiante: {}", studentId);
        StudentAcademicProgress progress = studentAcademicProgressRepository.findByStudentId(studentId)
                .orElseThrow(() -> {
                    log.error("Progreso académico no encontrado para el estudiante: {}", studentId);
                    return new AppException("Progreso académico no encontrado para el estudiante: " + studentId);
                });

        List<Group> currentSchedule = progress.getCoursesStatus().stream()
                .filter(this::isCourseCurrentlyEnrolled)
                .map(CourseStatusDetail::getGroup)
                .filter(Objects::nonNull)
                .map(group -> groupRepository.findById(group.getGroupId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        log.info("Horario actual del estudiante {}: {} grupos", studentId, currentSchedule.size());
        return currentSchedule;
    }

    /**
     * Obtiene los grupos disponibles para un curso específico.
     *
     * @param courseCode código del curso
     * @return lista de grupos con cupos disponibles
     * @throws AppException si no se encuentran grupos para el curso
     */
    public List<Group> getAvailableGroups(String courseCode) {
        log.debug("Obteniendo grupos disponibles para el curso: {}", courseCode);
        List<Group> groups = groupRepository.findByCourse_CourseCode(courseCode);

        if (groups.isEmpty()) {
            log.warn("No se encontraron grupos para el curso: {}", courseCode);
            throw new AppException("No se encontraron grupos para el curso: " + courseCode);
        }

        List<Group> availableGroups = groups.stream()
                .filter(group -> checkGroupAvailability(group.getGroupId()))
                .collect(Collectors.toList());

        log.info("Grupos disponibles para el curso {}: {} de {} grupos", courseCode, availableGroups.size(), groups.size());
        return availableGroups;
    }

    /**
     * Obtiene el progreso académico completo de un estudiante.
     *
     * @param studentId identificador del estudiante
     * @return objeto StudentAcademicProgress
     * @throws AppException si no se encuentra el progreso académico
     */
    public StudentAcademicProgress getAcademicProgress(String studentId) {
        log.debug("Obteniendo progreso académico para el estudiante: {}", studentId);
        return studentAcademicProgressRepository.findByStudentId(studentId)
                .orElseThrow(() -> {
                    log.error("Progreso académico no encontrado para el estudiante: {}", studentId);
                    return new AppException("Progreso académico no encontrado para el estudiante: " + studentId);
                });
    }

    /**
     * Verifica si un grupo tiene disponibilidad de cupos.
     *
     * @param groupId identificador del grupo
     * @return true si hay cupos disponibles, false en caso contrario
     * @throws AppException si no se encuentra el grupo
     */
    public boolean checkGroupAvailability(String groupId) {
        log.debug("Verificando disponibilidad del grupo: {}", groupId);
        Integer maxCapacity = getMaxCapacity(groupId);
        Integer currentEnrollment = getCurrentEnrollment(groupId);
        boolean isAvailable = currentEnrollment < maxCapacity;

        log.info("Disponibilidad del grupo {}: {} (capacidad: {}/{})",
                groupId, isAvailable ? "DISPONIBLE" : "LLENO", currentEnrollment, maxCapacity);
        return isAvailable;
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
        log.debug("Generando recomendaciones de cursos para el estudiante: {}", studentId);
        StudentAcademicProgress progress = studentAcademicProgressRepository.findByStudentId(studentId)
                .orElseThrow(() -> {
                    log.error("Progreso académico no encontrado para el estudiante: {}", studentId);
                    return new AppException("Progreso académico no encontrado para el estudiante: " + studentId);
                });

        Set<String> takenCourses = progress.getCoursesStatus().stream()
                .map(CourseStatusDetail::getCourse)
                .map(Course::getCourseCode)
                .collect(Collectors.toSet());

        List<Course> recommendations = courseRepository.findByAcademicProgramAndIsActive(
                        progress.getAcademicProgram(), true).stream()
                .filter(course -> !takenCourses.contains(course.getCourseCode()))
                .filter(course -> isCourseRecommended(course, progress))
                .collect(Collectors.toList());

        log.info("Recomendaciones de cursos para el estudiante {}: {} cursos", studentId, recommendations.size());
        return recommendations;
    }

    /**
     * Obtiene el periodo académico actual y sus fechas de inscripción.
     *
     * @return el periodo académico activo
     * @throws AppException si no hay periodos académicos activos
     */
    public AcademicPeriod getEnrollmentDeadlines() {
        log.debug("Obteniendo periodo académico activo");
        return academicPeriodRepository.findByIsActiveTrue()
                .orElseThrow(() -> {
                    log.error("No hay periodos académicos activos");
                    return new AppException("No hay periodos académicos activos");
                });
    }

    /**
     * Genera una lista de alertas académicas personalizadas para un estudiante.
     *
     * @param studentId identificador del estudiante
     * @return lista de alertas académicas
     * @throws AppException si no se encuentra información académica del estudiante
     */
    public List<String> getAcademicAlerts(String studentId) {
        log.debug("Generando alertas académicas para el estudiante: {}", studentId);
        StudentAcademicProgress progress = studentAcademicProgressRepository.findByStudentId(studentId)
                .orElseThrow(() -> {
                    log.error("No se encontró información académica del estudiante: {}", studentId);
                    return new AppException("No se encontró información académica del estudiante: " + studentId);
                });

        List<String> alerts = generateAcademicAlerts(progress);
        log.info("Alertas académicas para el estudiante {}: {} alertas", studentId, alerts.size());
        return alerts;
    }

    /**
     * Verifica si un curso está actualmente en progreso.
     *
     * @param courseStatus objeto CourseStatusDetail
     * @return true si el curso está en progreso, false en caso contrario
     */
    private boolean isCourseCurrentlyEnrolled(CourseStatusDetail courseStatus) {
        boolean isEnrolled = courseStatus.getIsApproved() == null ||
                (courseStatus.getEnrollmentDate() != null &&
                        courseStatus.getCompletionDate() == null);
        log.debug("Curso {} está inscrito: {}", courseStatus.getCourse().getCourseCode(), isEnrolled);
        return isEnrolled;
    }

    /**
     * Determina si un curso es recomendado para un estudiante según su programa académico.
     *
     * @param course curso a evaluar
     * @param progress progreso académico del estudiante
     * @return true si el curso pertenece al mismo programa y está activo
     */
    private boolean isCourseRecommended(Course course, StudentAcademicProgress progress) {
        boolean isRecommended = course.getIsActive() &&
                course.getAcademicProgram().equals(progress.getAcademicProgram());
        log.debug("Curso {} es recomendado: {}", course.getCourseCode(), isRecommended);
        return isRecommended;
    }

    /**
     * Genera alertas académicas basadas en el progreso del estudiante.
     *
     * @param progress objeto StudentAcademicProgress del estudiante
     * @return lista de mensajes de alerta académica
     */
    private List<String> generateAcademicAlerts(StudentAcademicProgress progress) {
        List<String> alerts = new ArrayList<>();

        Optional.ofNullable(progress.getCumulativeGPA())
                .filter(gpa -> gpa < 3.0)
                .ifPresent(gpa -> {
                    String alert = String.format("Alerta: GPA acumulado por debajo del mínimo requerido (%.2f)", gpa);
                    alerts.add(alert);
                    log.debug("Alerta GPA generada: {}", alert);
                });

        double progressPercentage = progress.getTotalCreditsRequired() > 0 ?
                (double) progress.getCompletedCredits() / progress.getTotalCreditsRequired() : 0.0;

        double expectedProgress = progress.getTotalSemesters() > 0 ?
                getExpectedProgress(progress.getCurrentSemester(), progress.getTotalSemesters()) : 0.0;

        if (progressPercentage < expectedProgress) {
            alerts.add("Alerta: Progreso de créditos por debajo del esperado para el semestre actual");
            log.debug("Alerta de progreso de créditos generada");
        }

        long failedCoursesCount = progress.getCoursesStatus().stream()
                .filter(cs -> cs.getIsApproved() != null && !cs.getIsApproved())
                .count();

        if (failedCoursesCount > 0) {
            String alert = String.format("Tiene %d materia(s) reprobada(s) que debe repetir", failedCoursesCount);
            alerts.add(alert);
            log.debug("Alerta de materias reprobadas generada: {}", alert);
        }

        return alerts;
    }

    /**
     * Calcula el progreso esperado en créditos según el semestre actual.
     *
     * @param currentSemester número de semestre actual
     * @param totalSemesters número total de semestres del programa
     * @return progreso esperado expresado como porcentaje
     */
    private double getExpectedProgress(int currentSemester, int totalSemesters) {
        double expectedProgress = (double) currentSemester / totalSemesters;
        log.debug("Progreso esperado para semestre {}/{}: {:.2f}%",
                currentSemester, totalSemesters, expectedProgress * 100);
        return expectedProgress;
    }
}
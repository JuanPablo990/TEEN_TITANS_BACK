package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto.StudentProgressDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto.CourseStatusDetailDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.StudentPortalService;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador que gestiona la información académica, el progreso y las recomendaciones
 * del estudiante en el portal estudiantil.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@RestController
@RequestMapping("/api/student-portal")
@CrossOrigin(origins = "*")
public class StudentPortalController {

    private final StudentPortalService studentPortalService;

    /**
     * Constructor del controlador del portal estudiantil.
     *
     * @param studentPortalService el servicio del portal estudiantil
     */
    @Autowired
    public StudentPortalController(StudentPortalService studentPortalService) {
        this.studentPortalService = studentPortalService;
    }

    /**
     * Obtiene el horario actual del estudiante.
     *
     * @param studentId el ID del estudiante
     * @return ResponseEntity con la lista de grupos del horario actual
     */
    @GetMapping("/{studentId}/current-schedule")
    public ResponseEntity<?> getCurrentSchedule(@PathVariable String studentId) {
        try {
            List<Group> schedule = studentPortalService.getCurrentSchedule(studentId);
            return ResponseEntity.ok(schedule);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener el horario del estudiante"));
        }
    }

    /**
     * Obtiene los grupos disponibles para un curso.
     *
     * @param courseCode el código del curso
     * @return ResponseEntity con la lista de grupos disponibles
     */
    @GetMapping("/courses/{courseCode}/available-groups")
    public ResponseEntity<?> getAvailableGroups(@PathVariable String courseCode) {
        try {
            List<Group> availableGroups = studentPortalService.getAvailableGroups(courseCode);
            return ResponseEntity.ok(availableGroups);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener grupos disponibles"));
        }
    }

    /**
     * Obtiene el progreso académico del estudiante.
     *
     * @param studentId el ID del estudiante
     * @return ResponseEntity con el DTO de progreso académico
     */
    @GetMapping("/{studentId}/academic-progress")
    public ResponseEntity<?> getAcademicProgress(@PathVariable String studentId) {
        try {
            StudentAcademicProgress progress = studentPortalService.getAcademicProgress(studentId);
            StudentProgressDTO progressDTO = convertToProgressDTO(progress);
            return ResponseEntity.ok(progressDTO);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener el progreso académico"));
        }
    }

    /**
     * Verifica la disponibilidad de un grupo.
     *
     * @param groupId el ID del grupo
     * @return ResponseEntity con el estado de disponibilidad
     */
    @GetMapping("/groups/{groupId}/availability")
    public ResponseEntity<?> checkGroupAvailability(@PathVariable String groupId) {
        try {
            boolean isAvailable = studentPortalService.checkGroupAvailability(groupId);
            return ResponseEntity.ok(Map.of("groupId", groupId, "available", isAvailable));
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al verificar disponibilidad del grupo"));
        }
    }

    /**
     * Obtiene recomendaciones de cursos para el estudiante.
     *
     * @param studentId el ID del estudiante
     * @return ResponseEntity con la lista de cursos recomendados
     */
    @GetMapping("/{studentId}/course-recommendations")
    public ResponseEntity<?> getCourseRecommendations(@PathVariable String studentId) {
        try {
            List<Course> recommendations = studentPortalService.getCourseRecommendations(studentId);
            return ResponseEntity.ok(recommendations);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener recomendaciones de cursos"));
        }
    }

    /**
     * Obtiene las fechas límite de inscripción.
     *
     * @return ResponseEntity con el período académico y fechas límite
     */
    @GetMapping("/enrollment-deadlines")
    public ResponseEntity<?> getEnrollmentDeadlines() {
        try {
            AcademicPeriod period = studentPortalService.getEnrollmentDeadlines();
            return ResponseEntity.ok(period);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener las fechas de inscripción"));
        }
    }

    /**
     * Obtiene las alertas académicas del estudiante.
     *
     * @param studentId el ID del estudiante
     * @return ResponseEntity con la lista de alertas académicas
     */
    @GetMapping("/{studentId}/academic-alerts")
    public ResponseEntity<?> getAcademicAlerts(@PathVariable String studentId) {
        try {
            List<String> alerts = studentPortalService.getAcademicAlerts(studentId);
            return ResponseEntity.ok(Map.of("studentId", studentId, "alerts", alerts, "alertCount", alerts.size()));
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener alertas académicas"));
        }
    }

    /**
     * Obtiene información de capacidad de un grupo.
     *
     * @param groupId el ID del grupo
     * @return ResponseEntity con la información de capacidad del grupo
     */
    @GetMapping("/groups/{groupId}/capacity")
    public ResponseEntity<?> getGroupCapacityInfo(@PathVariable String groupId) {
        try {
            Integer maxCapacity = studentPortalService.getMaxCapacity(groupId);
            Integer currentEnrollment = studentPortalService.getCurrentEnrollment(groupId);
            boolean isAvailable = studentPortalService.checkGroupAvailability(groupId);

            return ResponseEntity.ok(Map.of(
                    "groupId", groupId,
                    "maxCapacity", maxCapacity,
                    "currentEnrollment", currentEnrollment,
                    "availableSpots", maxCapacity - currentEnrollment,
                    "isAvailable", isAvailable,
                    "occupancyRate", (double) currentEnrollment / maxCapacity * 100
            ));
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener información de capacidad del grupo"));
        }
    }

    /**
     * Obtiene estadísticas de inscripción por grupo para un curso.
     *
     * @param courseCode el código del curso
     * @return ResponseEntity con las estadísticas de inscripción
     */
    @GetMapping("/courses/{courseCode}/enrollment-stats")
    public ResponseEntity<?> getCourseEnrollmentStats(@PathVariable String courseCode) {
        try {
            Map<String, Integer> enrollmentStats = studentPortalService.getTotalEnrolledByCourse(courseCode);
            return ResponseEntity.ok(Map.of("courseCode", courseCode, "enrollmentByGroup", enrollmentStats));
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener estadísticas de inscripción"));
        }
    }

    /**
     * Obtiene un resumen académico del estudiante.
     *
     * @param studentId el ID del estudiante
     * @return ResponseEntity con el resumen académico
     */
    @GetMapping("/{studentId}/academic-summary")
    public ResponseEntity<?> getAcademicSummary(@PathVariable String studentId) {
        try {
            StudentAcademicProgress progress = studentPortalService.getAcademicProgress(studentId);
            Map<String, Object> summary = Map.of(
                    "studentId", studentId,
                    "studentName", progress.getStudent().getName(),
                    "academicProgram", progress.getAcademicProgram(),
                    "currentSemester", progress.getCurrentSemester(),
                    "cumulativeGPA", progress.getCumulativeGPA(),
                    "completedCredits", progress.getCompletedCredits(),
                    "totalCreditsRequired", progress.getTotalCreditsRequired(),
                    "progressPercentage", (double) progress.getCompletedCredits() / progress.getTotalCreditsRequired() * 100,
                    "coursesInProgress", progress.getCoursesStatus().stream()
                            .filter(cs -> cs.getStatus() != null && cs.getStatus().name().equals("IN_PROGRESS"))
                            .count(),
                    "coursesCompleted", progress.getCoursesStatus().stream()
                            .filter(cs -> cs.getIsApproved() != null && cs.getIsApproved())
                            .count()
            );
            return ResponseEntity.ok(summary);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener el resumen académico"));
        }
    }

    /**
     * Verifica la elegibilidad de inscripción para un curso.
     *
     * @param studentId el ID del estudiante
     * @param courseCode el código del curso
     * @return ResponseEntity con la información de elegibilidad
     */
    @GetMapping("/{studentId}/courses/{courseCode}/enrollment-eligibility")
    public ResponseEntity<?> checkEnrollmentEligibility(@PathVariable String studentId,
                                                        @PathVariable String courseCode) {
        try {
            StudentAcademicProgress progress = studentPortalService.getAcademicProgress(studentId);

            boolean alreadyApproved = progress.getCoursesStatus().stream()
                    .filter(cs -> cs.getCourse() != null && courseCode.equals(cs.getCourse().getCourseCode()))
                    .anyMatch(cs -> Boolean.TRUE.equals(cs.getIsApproved()));

            boolean currentlyEnrolled = progress.getCoursesStatus().stream()
                    .filter(cs -> cs.getCourse() != null && courseCode.equals(cs.getCourse().getCourseCode()))
                    .anyMatch(cs -> cs.getStatus() != null && cs.getStatus().name().equals("IN_PROGRESS"));

            List<Group> availableGroups = studentPortalService.getAvailableGroups(courseCode);
            boolean hasAvailableGroups = !availableGroups.isEmpty();

            Map<String, Object> eligibility = Map.of(
                    "studentId", studentId,
                    "courseCode", courseCode,
                    "eligible", !alreadyApproved && !currentlyEnrolled && hasAvailableGroups,
                    "alreadyApproved", alreadyApproved,
                    "currentlyEnrolled", currentlyEnrolled,
                    "hasAvailableGroups", hasAvailableGroups,
                    "availableGroupsCount", availableGroups.size()
            );

            return ResponseEntity.ok(eligibility);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al verificar elegibilidad de inscripción"));
        }
    }

    /**
     * Convierte un objeto StudentAcademicProgress a StudentProgressDTO.
     *
     * @param progress el progreso académico a convertir
     * @return el DTO de progreso del estudiante
     */
    private StudentProgressDTO convertToProgressDTO(StudentAcademicProgress progress) {
        StudentProgressDTO dto = new StudentProgressDTO();
        Student student = progress.getStudent();

        dto.setId(progress.getId());
        dto.setStudentId(student.getId());
        dto.setStudentName(student.getName());
        dto.setAcademicProgram(progress.getAcademicProgram());
        dto.setFaculty(progress.getFaculty());
        dto.setCurriculumType(progress.getCurriculumType());
        dto.setCurrentSemester(progress.getCurrentSemester());
        dto.setTotalSemesters(progress.getTotalSemesters());
        dto.setCompletedCredits(progress.getCompletedCredits());
        dto.setTotalCreditsRequired(progress.getTotalCreditsRequired());
        dto.setCumulativeGPA(progress.getCumulativeGPA());

        if (progress.getCoursesStatus() != null) {
            List<CourseStatusDetailDTO> courseDTOs = progress.getCoursesStatus().stream()
                    .map(this::convertToCourseStatusDetailDTO)
                    .collect(Collectors.toList());
            dto.setCoursesStatus(courseDTOs);
        }

        if (progress.getTotalCreditsRequired() != null && progress.getTotalCreditsRequired() > 0) {
            dto.setProgressPercentage((double) progress.getCompletedCredits() / progress.getTotalCreditsRequired() * 100);
        }
        dto.setRemainingCredits(progress.getTotalCreditsRequired() - progress.getCompletedCredits());
        dto.setRemainingSemesters(progress.getTotalSemesters() - progress.getCurrentSemester());

        return dto;
    }

    /**
     * Convierte CourseStatusDetail a CourseStatusDetailDTO.
     *
     * @param courseStatus el detalle del estado del curso a convertir
     * @return el DTO de detalle del estado del curso
     */
    private CourseStatusDetailDTO convertToCourseStatusDetailDTO(CourseStatusDetail courseStatus) {
        CourseStatusDetailDTO dto = new CourseStatusDetailDTO();

        dto.setId(courseStatus.getId());
        dto.setStudentId(courseStatus.getStudentId());
        dto.setStatus(courseStatus.getStatus());
        dto.setGrade(courseStatus.getGrade());
        dto.setSemester(courseStatus.getSemester());
        dto.setEnrollmentDate(courseStatus.getEnrollmentDate());
        dto.setCompletionDate(courseStatus.getCompletionDate());
        dto.setCreditsEarned(courseStatus.getCreditsEarned());
        dto.setIsApproved(courseStatus.getIsApproved());
        dto.setComments(courseStatus.getComments());

        if (courseStatus.getCourse() != null) {
            dto.setCourseId(courseStatus.getCourse().getCourseCode());
            dto.setCourseCode(courseStatus.getCourse().getCourseCode());
            dto.setCourseName(courseStatus.getCourse().getName());
        }

        if (courseStatus.getGroup() != null) {
            dto.setGroupId(courseStatus.getGroup().getGroupId());
            dto.setGroupCode(courseStatus.getGroup().getSection());
        }

        if (courseStatus.getProfessor() != null) {
            dto.setProfessorId(courseStatus.getProfessor().getId());
            dto.setProfessorName(courseStatus.getProfessor().getName());
        }

        return dto;
    }
}
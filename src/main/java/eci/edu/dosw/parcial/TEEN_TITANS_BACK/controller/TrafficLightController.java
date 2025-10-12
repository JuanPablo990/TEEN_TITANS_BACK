package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Student;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.StudentAcademicProgress;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.TrafficLightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controlador REST para gestionar el semáforo académico de los estudiantes.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@RestController
@RequestMapping("/api/traffic-light")
@CrossOrigin(origins = "*")
public class TrafficLightController {

    private final TrafficLightService trafficLightService;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param trafficLightService Servicio de semáforo académico.
     */
    @Autowired
    public TrafficLightController(TrafficLightService trafficLightService) {
        this.trafficLightService = trafficLightService;
    }

    /**
     * Obtiene el semáforo académico del estudiante.
     *
     * @param studentId ID del estudiante
     * @return Color del semáforo ("GREEN", "YELLOW", "RED")
     */
    @GetMapping("/{studentId}")
    public ResponseEntity<?> getAcademicTrafficLight(@PathVariable String studentId) {
        try {
            String trafficLight = trafficLightService.getAcademicTrafficLight(studentId);
            return ResponseEntity.ok(Map.of(
                    "studentId", studentId,
                    "trafficLight", trafficLight,
                    "statusDescription", getStatusDescription(trafficLight)
            ));
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener el semáforo académico"));
        }
    }

    /**
     * Obtiene la información completa del estudiante.
     *
     * @param studentId ID del estudiante
     * @return Información del estudiante
     */
    @GetMapping("/{studentId}/student-info")
    public ResponseEntity<?> getStudentInformation(@PathVariable String studentId) {
        try {
            Student student = trafficLightService.getStudentInformation(studentId);
            return ResponseEntity.ok(student);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener la información del estudiante"));
        }
    }

    /**
     * Obtiene el progreso curricular completo del estudiante.
     *
     * @param studentId ID del estudiante
     * @return Progreso curricular del estudiante
     */
    @GetMapping("/{studentId}/curriculum-progress")
    public ResponseEntity<?> getCurriculumProgress(@PathVariable String studentId) {
        try {
            StudentAcademicProgress progress = trafficLightService.getCurriculumProgress(studentId);
            return ResponseEntity.ok(progress);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener el progreso curricular"));
        }
    }

    /**
     * Obtiene el porcentaje de avance curricular del estudiante.
     *
     * @param studentId ID del estudiante
     * @return Porcentaje de avance (0.0 a 100.0)
     */
    @GetMapping("/{studentId}/progress-percentage")
    public ResponseEntity<?> getCurriculumProgressPercentage(@PathVariable String studentId) {
        try {
            Double percentage = trafficLightService.getCurriculumProgressPercentage(studentId);
            return ResponseEntity.ok(Map.of(
                    "studentId", studentId,
                    "progressPercentage", percentage,
                    "progressDescription", getProgressDescription(percentage)
            ));
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener el porcentaje de avance"));
        }
    }

    /**
     * Verifica si el estudiante está en riesgo académico.
     *
     * @param studentId ID del estudiante
     * @return true si el estudiante está en riesgo académico
     */
    @GetMapping("/{studentId}/academic-risk")
    public ResponseEntity<?> isStudentAtAcademicRisk(@PathVariable String studentId) {
        try {
            boolean atRisk = trafficLightService.isStudentAtAcademicRisk(studentId);
            return ResponseEntity.ok(Map.of(
                    "studentId", studentId,
                    "atAcademicRisk", atRisk,
                    "riskLevel", atRisk ? "ALTO" : "BAJO"
            ));
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al verificar el riesgo académico"));
        }
    }

    /**
     * Obtiene un resumen completo del estado académico del estudiante.
     *
     * @param studentId ID del estudiante
     * @return Resumen completo del estado académico
     */
    @GetMapping("/{studentId}/academic-summary")
    public ResponseEntity<?> getAcademicStatusSummary(@PathVariable String studentId) {
        try {
            String summary = trafficLightService.getAcademicStatusSummary(studentId);
            return ResponseEntity.ok(Map.of(
                    "studentId", studentId,
                    "academicSummary", summary
            ));
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener el resumen académico"));
        }
    }

    /**
     * Obtiene el estado académico completo del estudiante en formato estructurado.
     *
     * @param studentId ID del estudiante
     * @return Estado académico completo con todos los datos
     */
    @GetMapping("/{studentId}/complete-status")
    public ResponseEntity<?> getCompleteAcademicStatus(@PathVariable String studentId) {
        try {
            Student student = trafficLightService.getStudentInformation(studentId);
            StudentAcademicProgress progress = trafficLightService.getCurriculumProgress(studentId);
            String trafficLight = trafficLightService.getAcademicTrafficLight(studentId);
            Double progressPercentage = trafficLightService.getCurriculumProgressPercentage(studentId);
            boolean atRisk = trafficLightService.isStudentAtAcademicRisk(studentId);

            return ResponseEntity.ok(Map.of(
                    "student", Map.of(
                            "id", student.getId(),
                            "name", student.getName(),
                            "email", student.getEmail(),
                            "academicProgram", student.getAcademicProgram(),
                            "semester", student.getSemester(),
                            "active", student.getActive()
                    ),
                    "academicProgress", Map.of(
                            "currentSemester", progress.getCurrentSemester(),
                            "totalSemesters", progress.getTotalSemesters(),
                            "completedCredits", progress.getCompletedCredits(),
                            "totalCreditsRequired", progress.getTotalCreditsRequired(),
                            "cumulativeGPA", progress.getCumulativeGPA(),
                            "progressPercentage", progressPercentage
                    ),
                    "trafficLight", Map.of(
                            "color", trafficLight,
                            "description", getStatusDescription(trafficLight),
                            "atAcademicRisk", atRisk
                    ),
                    "timestamp", java.time.LocalDateTime.now()
            ));
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener el estado académico completo"));
        }
    }

    /**
     * Obtiene la descripción del estado basado en el color del semáforo.
     */
    private String getStatusDescription(String trafficLight) {
        switch (trafficLight) {
            case "GREEN":
                return "Rendimiento académico satisfactorio";
            case "YELLOW":
                return "Rendimiento regular - necesita mejorar";
            case "RED":
                return "En riesgo académico - requiere atención inmediata";
            default:
                return "Estado desconocido";
        }
    }

    /**
     * Obtiene la descripción del progreso basado en el porcentaje.
     */
    private String getProgressDescription(Double percentage) {
        if (percentage >= 90) return "Avance excelente";
        if (percentage >= 75) return "Avance bueno";
        if (percentage >= 60) return "Avance regular";
        if (percentage >= 40) return "Avance bajo";
        return "Avance muy bajo";
    }
}
package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto.TrafficLightDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Student;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.StudentAcademicProgress;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.TrafficLightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/traffic-light")
@CrossOrigin(origins = "*")
public class TrafficLightController {

    private final TrafficLightService trafficLightService;

    @Autowired
    public TrafficLightController(TrafficLightService trafficLightService) {
        this.trafficLightService = trafficLightService;
    }

    @GetMapping("/{studentId}/complete-status")
    public ResponseEntity<TrafficLightDTO> getCompleteAcademicStatus(@PathVariable String studentId) {
        try {
            Student student = trafficLightService.getStudentInformation(studentId);
            StudentAcademicProgress progress = trafficLightService.getCurriculumProgress(studentId);
            String trafficLight = trafficLightService.getAcademicTrafficLight(studentId);
            Double progressPercentage = trafficLightService.getCurriculumProgressPercentage(studentId);
            boolean atRisk = trafficLightService.isStudentAtAcademicRisk(studentId);

            Map<String, Object> data = new HashMap<>();
            data.put("student", Map.of(
                    "id", student.getId(),
                    "name", student.getName(),
                    "email", student.getEmail(),
                    "academicProgram", student.getAcademicProgram(),
                    "semester", student.getSemester(),
                    "active", student.isActive()
            ));
            data.put("academicProgress", Map.of(
                    "currentSemester", progress.getCurrentSemester(),
                    "totalSemesters", progress.getTotalSemesters(),
                    "completedCredits", progress.getCompletedCredits(),
                    "totalCreditsRequired", progress.getTotalCreditsRequired(),
                    "cumulativeGPA", progress.getCumulativeGPA(),
                    "progressPercentage", progressPercentage
            ));
            data.put("trafficLight", Map.of(
                    "color", trafficLight,
                    "description", getStatusDescription(trafficLight),
                    "atAcademicRisk", atRisk
            ));

            return ResponseEntity.ok(TrafficLightDTO.success(data));

        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(TrafficLightDTO.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(TrafficLightDTO.error("Error al obtener el estado académico completo"));
        }
    }

    @GetMapping("/{studentId}/traffic-light")
    public ResponseEntity<TrafficLightDTO> getAcademicTrafficLight(@PathVariable String studentId) {
        try {
            String trafficLight = trafficLightService.getAcademicTrafficLight(studentId);
            boolean atRisk = trafficLightService.isStudentAtAcademicRisk(studentId);

            Map<String, Object> data = new HashMap<>();
            data.put("studentId", studentId);
            data.put("trafficLight", trafficLight);
            data.put("description", getStatusDescription(trafficLight));
            data.put("atAcademicRisk", atRisk);

            return ResponseEntity.ok(TrafficLightDTO.success(data));

        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(TrafficLightDTO.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(TrafficLightDTO.error("Error al obtener el semáforo académico"));
        }
    }

    @GetMapping("/{studentId}/curriculum-progress")
    public ResponseEntity<TrafficLightDTO> getCurriculumProgress(@PathVariable String studentId) {
        try {
            Student student = trafficLightService.getStudentInformation(studentId);
            StudentAcademicProgress progress = trafficLightService.getCurriculumProgress(studentId);
            Double progressPercentage = trafficLightService.getCurriculumProgressPercentage(studentId);

            Map<String, Object> data = new HashMap<>();
            data.put("student", Map.of(
                    "id", student.getId(),
                    "name", student.getName(),
                    "academicProgram", student.getAcademicProgram(),
                    "semester", student.getSemester()
            ));
            data.put("academicProgress", Map.of(
                    "currentSemester", progress.getCurrentSemester(),
                    "totalSemesters", progress.getTotalSemesters(),
                    "completedCredits", progress.getCompletedCredits(),
                    "totalCreditsRequired", progress.getTotalCreditsRequired(),
                    "cumulativeGPA", progress.getCumulativeGPA(),
                    "progressPercentage", progressPercentage
            ));

            return ResponseEntity.ok(TrafficLightDTO.success(data));

        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(TrafficLightDTO.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(TrafficLightDTO.error("Error al obtener el progreso curricular"));
        }
    }

    @GetMapping("/{studentId}/progress-percentage")
    public ResponseEntity<TrafficLightDTO> getCurriculumProgressPercentage(@PathVariable String studentId) {
        try {
            Double percentage = trafficLightService.getCurriculumProgressPercentage(studentId);

            Map<String, Object> data = new HashMap<>();
            data.put("studentId", studentId);
            data.put("progressPercentage", percentage);
            data.put("description", getProgressDescription(percentage));

            return ResponseEntity.ok(TrafficLightDTO.success(data));

        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(TrafficLightDTO.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(TrafficLightDTO.error("Error al obtener el porcentaje de avance"));
        }
    }

    @GetMapping("/{studentId}/academic-risk")
    public ResponseEntity<TrafficLightDTO> isStudentAtAcademicRisk(@PathVariable String studentId) {
        try {
            boolean atRisk = trafficLightService.isStudentAtAcademicRisk(studentId);

            Map<String, Object> data = new HashMap<>();
            data.put("studentId", studentId);
            data.put("atAcademicRisk", atRisk);
            data.put("riskLevel", atRisk ? "ALTO" : "BAJO");

            return ResponseEntity.ok(TrafficLightDTO.success(data));

        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(TrafficLightDTO.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(TrafficLightDTO.error("Error al verificar el riesgo académico"));
        }
    }

    @GetMapping("/program/{academicProgram}/at-risk")
    public ResponseEntity<TrafficLightDTO> getStudentsAtRiskByProgram(@PathVariable String academicProgram) {
        try {
            List<Student> students = trafficLightService.getStudentsAtRiskByProgram(academicProgram);
            int[] statistics = trafficLightService.getTrafficLightStatisticsByProgram(academicProgram);

            Map<String, Object> data = new HashMap<>();
            data.put("academicProgram", academicProgram);
            data.put("studentsAtRiskCount", students.size());
            data.put("trafficLightStatistics", Map.of(
                    "green", statistics[0],
                    "yellow", statistics[1],
                    "red", statistics[2]
            ));

            return ResponseEntity.ok(TrafficLightDTO.success(data));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(TrafficLightDTO.error("Error al obtener estudiantes en riesgo"));
        }
    }

    private String getStatusDescription(String trafficLight) {
        switch (trafficLight) {
            case "GREEN": return "Rendimiento académico satisfactorio";
            case "YELLOW": return "Rendimiento regular - necesita mejorar";
            case "RED": return "En riesgo académico - requiere atención inmediata";
            default: return "Estado desconocido";
        }
    }

    private String getProgressDescription(Double percentage) {
        if (percentage == null) return "No disponible";
        if (percentage >= 90) return "Avance excelente";
        if (percentage >= 75) return "Avance bueno";
        if (percentage >= 60) return "Avance regular";
        if (percentage >= 40) return "Avance bajo";
        return "Avance muy bajo";
    }
}
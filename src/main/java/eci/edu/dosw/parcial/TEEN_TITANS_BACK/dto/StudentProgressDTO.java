package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * DTO para representar el progreso académico de un estudiante.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentProgressDTO {
    private String id;
    private String studentId;
    private String studentName;
    private String academicProgram;
    private String faculty;
    private String curriculumType;
    private Integer currentSemester;
    private Integer totalSemesters;
    private Integer completedCredits;
    private Integer totalCreditsRequired;
    private Double cumulativeGPA;
    private List<CourseStatusDetailDTO> coursesStatus;

    // Campos calculados útiles para el frontend
    private Double progressPercentage;
    private Integer remainingCredits;
    private Integer remainingSemesters;
}
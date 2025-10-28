package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * DTO principal para el Portal del Estudiante que contiene todos los sub-DTOs necesarios.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
public class StudentPortalDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudentProgressDTO {
        private String id;
        private String studentId;
        private String studentName;
        private String studentEmail;
        private String academicProgram;
        private String faculty;
        private String curriculumType;
        private Integer currentSemester;
        private Integer totalSemesters;
        private Integer completedCredits;
        private Integer totalCreditsRequired;
        private Double cumulativeGPA;
        private List<CourseProgressDTO> coursesStatus;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CourseProgressDTO {
        private String courseId;
        private String courseCode;
        private String courseName;
        private String status;
        private Double grade;
        private String semester;
        private Integer creditsEarned;
        private Boolean approved;
        private String comments;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AcademicAlertDTO {
        private String studentId;
        private List<String> alerts;
        private Integer alertCount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GroupCapacityDTO {
        private String groupId;
        private Integer maxCapacity;
        private Integer currentEnrollment;
        private Integer availableSpots;
        private Boolean isAvailable;
        private Double occupancyRate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnrollmentEligibilityDTO {
        private String studentId;
        private String courseCode;
        private Boolean eligible;
        private Boolean alreadyApproved;
        private Boolean currentlyEnrolled;
        private Boolean hasAvailableGroups;
        private Integer availableGroupsCount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AcademicSummaryDTO {
        private String studentId;
        private String studentName;
        private String academicProgram;
        private Integer currentSemester;
        private Double cumulativeGPA;
        private Integer completedCredits;
        private Integer totalCreditsRequired;
        private Double progressPercentage;
        private Long coursesInProgress;
        private Long coursesCompleted;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CourseHistoryDTO {
        private String courseCode;
        private String courseName;
        private Integer credits;
        private String status;
        private Double grade;
        private String semester;
        private Boolean approved;
        private Date enrollmentDate;
        private Date completionDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GroupAvailabilityRequest {
        private String groupId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CourseEnrollmentRequest {
        private String courseCode;
    }
}
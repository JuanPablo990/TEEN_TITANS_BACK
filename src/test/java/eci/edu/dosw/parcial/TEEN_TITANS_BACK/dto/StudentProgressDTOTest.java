package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class StudentProgressDTOTest {

    @Test
    void testConstructorWithParameters_Valid() {
        List<StudentProgressDTO.CourseProgressDTO> courses = List.of(
                new StudentProgressDTO.CourseProgressDTO("C1", "MATH101", "Calculus", "APPROVED", 4.5, "2025-1", 3, true, "Good performance")
        );

        StudentProgressDTO progress = new StudentProgressDTO("PROG001", "STU123", "John Doe", "john@university.edu",
                "Computer Science", "Engineering", "New", 5, 10, 60, 160, 4.2, courses);

        assertEquals("PROG001", progress.getId());
        assertEquals("STU123", progress.getStudentId());
        assertEquals("John Doe", progress.getStudentName());
        assertEquals("john@university.edu", progress.getStudentEmail());
        assertEquals("Computer Science", progress.getAcademicProgram());
        assertEquals("Engineering", progress.getFaculty());
        assertEquals("New", progress.getCurriculumType());
        assertEquals(5, progress.getCurrentSemester());
        assertEquals(10, progress.getTotalSemesters());
        assertEquals(60, progress.getCompletedCredits());
        assertEquals(160, progress.getTotalCreditsRequired());
        assertEquals(4.2, progress.getCumulativeGPA());
        assertEquals(1, progress.getCoursesStatus().size());
    }

    @Test
    void testConstructorWithParameters_NullValues() {
        StudentProgressDTO progress = new StudentProgressDTO(null, null, null, null, null, null, null, null, null, null, null, null, null);

        assertNull(progress.getId());
        assertNull(progress.getStudentId());
        assertNull(progress.getStudentName());
        assertNull(progress.getStudentEmail());
        assertNull(progress.getAcademicProgram());
        assertNull(progress.getFaculty());
        assertNull(progress.getCurriculumType());
        assertNull(progress.getCurrentSemester());
        assertNull(progress.getTotalSemesters());
        assertNull(progress.getCompletedCredits());
        assertNull(progress.getTotalCreditsRequired());
        assertNull(progress.getCumulativeGPA());
        assertNull(progress.getCoursesStatus());
    }

    @Test
    void testAddCourseProgress_FirstCourse() {
        StudentProgressDTO progress = new StudentProgressDTO();
        StudentProgressDTO.CourseProgressDTO course = new StudentProgressDTO.CourseProgressDTO("C1", "PHY101", "Physics", "IN_PROGRESS", null, "2025-1", 4, false, "Regular attendance");

        progress.addCourseProgress(course);

        assertEquals(1, progress.getCoursesStatus().size());
        assertEquals("C1", progress.getCoursesStatus().get(0).getCourseId());
        assertEquals("PHY101", progress.getCoursesStatus().get(0).getCourseCode());
        assertEquals("Physics", progress.getCoursesStatus().get(0).getCourseName());
        assertEquals("IN_PROGRESS", progress.getCoursesStatus().get(0).getStatus());
        assertNull(progress.getCoursesStatus().get(0).getGrade());
        assertEquals("2025-1", progress.getCoursesStatus().get(0).getSemester());
        assertEquals(4, progress.getCoursesStatus().get(0).getCreditsEarned());
        assertFalse(progress.getCoursesStatus().get(0).getApproved());
        assertEquals("Regular attendance", progress.getCoursesStatus().get(0).getComments());
    }

    @Test
    void testAddCourseProgress_MultipleCourses() {
        StudentProgressDTO progress = new StudentProgressDTO();
        StudentProgressDTO.CourseProgressDTO course1 = new StudentProgressDTO.CourseProgressDTO("C1", "MATH101", "Calculus", "APPROVED", 4.0, "2024-2", 3, true, null);
        StudentProgressDTO.CourseProgressDTO course2 = new StudentProgressDTO.CourseProgressDTO("C2", "CS101", "Programming", "APPROVED", 4.5, "2024-2", 4, true, "Excellent");

        progress.addCourseProgress(course1);
        progress.addCourseProgress(course2);

        assertEquals(2, progress.getCoursesStatus().size());
        assertEquals("MATH101", progress.getCoursesStatus().get(0).getCourseCode());
        assertEquals("CS101", progress.getCoursesStatus().get(1).getCourseCode());
    }

    @Test
    void testCourseProgressDTOConstructor_Valid() {
        StudentProgressDTO.CourseProgressDTO course = new StudentProgressDTO.CourseProgressDTO("C1", "CHEM101", "Chemistry", "FAILED", 2.8, "2025-1", 0, false, "Needs improvement");

        assertEquals("C1", course.getCourseId());
        assertEquals("CHEM101", course.getCourseCode());
        assertEquals("Chemistry", course.getCourseName());
        assertEquals("FAILED", course.getStatus());
        assertEquals(2.8, course.getGrade());
        assertEquals("2025-1", course.getSemester());
        assertEquals(0, course.getCreditsEarned());
        assertFalse(course.getApproved());
        assertEquals("Needs improvement", course.getComments());
    }

    @Test
    void testCourseProgressDTOConstructor_NullValues() {
        StudentProgressDTO.CourseProgressDTO course = new StudentProgressDTO.CourseProgressDTO(null, null, null, null, null, null, null, null, null);

        assertNull(course.getCourseId());
        assertNull(course.getCourseCode());
        assertNull(course.getCourseName());
        assertNull(course.getStatus());
        assertNull(course.getGrade());
        assertNull(course.getSemester());
        assertNull(course.getCreditsEarned());
        assertNull(course.getApproved());
        assertNull(course.getComments());
    }

    @Test
    void testGetProgressPercentage_Valid() {
        StudentProgressDTO progress = new StudentProgressDTO();
        progress.setCompletedCredits(80);
        progress.setTotalCreditsRequired(160);

        Double result = progress.getProgressPercentage();

        assertEquals(50.0, result);
    }

    @Test
    void testGetProgressPercentage_Zero() {
        StudentProgressDTO progress = new StudentProgressDTO();
        progress.setCompletedCredits(0);
        progress.setTotalCreditsRequired(0);

        Double result = progress.getProgressPercentage();

        assertEquals(0.0, result);
    }

    @Test
    void testIsFinalSemester_True() {
        StudentProgressDTO progress = new StudentProgressDTO();
        progress.setCurrentSemester(10);
        progress.setTotalSemesters(10);

        Boolean result = progress.isFinalSemester();

        assertTrue(result);
    }

    @Test
    void testIsFinalSemester_False() {
        StudentProgressDTO progress = new StudentProgressDTO();
        progress.setCurrentSemester(5);
        progress.setTotalSemesters(10);

        Boolean result = progress.isFinalSemester();

        assertFalse(result);
    }

    @Test
    void testSettersAndGetters_Valid() {
        StudentProgressDTO progress = new StudentProgressDTO();
        List<StudentProgressDTO.CourseProgressDTO> courses = List.of(
                new StudentProgressDTO.CourseProgressDTO("C1", "ENG101", "English", "APPROVED", 3.8, "2024-1", 2, true, "Good")
        );

        progress.setId("PROG002");
        progress.setStudentId("STU456");
        progress.setStudentName("Jane Smith");
        progress.setStudentEmail("jane@university.edu");
        progress.setAcademicProgram("Electrical Engineering");
        progress.setFaculty("Engineering");
        progress.setCurriculumType("Old");
        progress.setCurrentSemester(8);
        progress.setTotalSemesters(10);
        progress.setCompletedCredits(120);
        progress.setTotalCreditsRequired(180);
        progress.setCumulativeGPA(3.9);
        progress.setCoursesStatus(courses);

        assertEquals("PROG002", progress.getId());
        assertEquals("STU456", progress.getStudentId());
        assertEquals("Jane Smith", progress.getStudentName());
        assertEquals("jane@university.edu", progress.getStudentEmail());
        assertEquals("Electrical Engineering", progress.getAcademicProgram());
        assertEquals("Engineering", progress.getFaculty());
        assertEquals("Old", progress.getCurriculumType());
        assertEquals(8, progress.getCurrentSemester());
        assertEquals(10, progress.getTotalSemesters());
        assertEquals(120, progress.getCompletedCredits());
        assertEquals(180, progress.getTotalCreditsRequired());
        assertEquals(3.9, progress.getCumulativeGPA());
        assertEquals(1, progress.getCoursesStatus().size());
    }

    @Test
    void testSettersAndGetters_NullValues() {
        StudentProgressDTO progress = new StudentProgressDTO();

        progress.setId(null);
        progress.setStudentId(null);
        progress.setStudentName(null);
        progress.setStudentEmail(null);
        progress.setAcademicProgram(null);
        progress.setFaculty(null);
        progress.setCurriculumType(null);
        progress.setCurrentSemester(null);
        progress.setTotalSemesters(null);
        progress.setCompletedCredits(null);
        progress.setTotalCreditsRequired(null);
        progress.setCumulativeGPA(null);
        progress.setCoursesStatus(null);

        assertNull(progress.getId());
        assertNull(progress.getStudentId());
        assertNull(progress.getStudentName());
        assertNull(progress.getStudentEmail());
        assertNull(progress.getAcademicProgram());
        assertNull(progress.getFaculty());
        assertNull(progress.getCurriculumType());
        assertNull(progress.getCurrentSemester());
        assertNull(progress.getTotalSemesters());
        assertNull(progress.getCompletedCredits());
        assertNull(progress.getTotalCreditsRequired());
        assertNull(progress.getCumulativeGPA());
        assertNull(progress.getCoursesStatus());
    }
}

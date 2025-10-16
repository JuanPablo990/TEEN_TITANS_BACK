package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class CourseRepositoryTest {

    @Mock
    private CourseRepository courseRepository;

    private Course activeCourse;
    private Course inactiveCourse;
    private List<Course> courseList;

    @BeforeEach
    void setUp() {
        activeCourse = new Course(
                "CS101",
                "Programación I",
                3,
                "Curso introductorio de programación",
                "Ingeniería de Sistemas",
                true
        );

        inactiveCourse = new Course(
                "MA101",
                "Matemáticas I",
                4,
                "Curso básico de matemáticas",
                "Ingeniería Civil",
                false
        );

        courseList = Arrays.asList(activeCourse, inactiveCourse);
    }

    @Test
    void findByName_HappyPath_ReturnsCourse() {
        String courseName = "Programación I";
        when(courseRepository.findByName(courseName)).thenReturn(activeCourse);

        Course result = courseRepository.findByName(courseName);

        assertNotNull(result);
        assertEquals(courseName, result.getName());
        verify(courseRepository, times(1)).findByName(courseName);
    }

    @Test
    void findByName_HappyPath_DifferentCourse() {
        String courseName = "Matemáticas I";
        when(courseRepository.findByName(courseName)).thenReturn(inactiveCourse);

        Course result = courseRepository.findByName(courseName);

        assertNotNull(result);
        assertEquals(courseName, result.getName());
        assertFalse(result.isActive());
        verify(courseRepository, times(1)).findByName(courseName);
    }

    @Test
    void findByName_Error_CourseNotFound() {
        String nonExistentCourse = "Curso Inexistente";
        when(courseRepository.findByName(nonExistentCourse)).thenReturn(null);

        Course result = courseRepository.findByName(nonExistentCourse);

        assertNull(result);
        verify(courseRepository, times(1)).findByName(nonExistentCourse);
    }

    @Test
    void findByName_Error_NullParameter() {
        when(courseRepository.findByName(null)).thenReturn(null);

        Course result = courseRepository.findByName(null);

        assertNull(result);
        verify(courseRepository, times(1)).findByName(null);
    }

    @Test
    void findByIsActive_HappyPath_ActiveCourses() {
        List<Course> activeCourses = Collections.singletonList(activeCourse);
        when(courseRepository.findByIsActive(true)).thenReturn(activeCourses);

        List<Course> result = courseRepository.findByIsActive(true);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).isActive());
        verify(courseRepository, times(1)).findByIsActive(true);
    }

    @Test
    void findByIsActive_HappyPath_InactiveCourses() {
        List<Course> inactiveCourses = Collections.singletonList(inactiveCourse);
        when(courseRepository.findByIsActive(false)).thenReturn(inactiveCourses);

        List<Course> result = courseRepository.findByIsActive(false);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertFalse(result.get(0).isActive());
        verify(courseRepository, times(1)).findByIsActive(false);
    }

    @Test
    void findByIsActive_Error_NoActiveCourses() {
        when(courseRepository.findByIsActive(true)).thenReturn(Collections.emptyList());

        List<Course> result = courseRepository.findByIsActive(true);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(courseRepository, times(1)).findByIsActive(true);
    }

    @Test
    void findByIsActive_Error_NoInactiveCourses() {
        when(courseRepository.findByIsActive(false)).thenReturn(Collections.emptyList());

        List<Course> result = courseRepository.findByIsActive(false);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(courseRepository, times(1)).findByIsActive(false);
    }

    @Test
    void findByAcademicProgram_HappyPath_ReturnsCourses() {
        String program = "Ingeniería de Sistemas";
        List<Course> expectedCourses = Collections.singletonList(activeCourse);
        when(courseRepository.findByAcademicProgram(program)).thenReturn(expectedCourses);

        List<Course> result = courseRepository.findByAcademicProgram(program);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(program, result.get(0).getAcademicProgram());
        verify(courseRepository, times(1)).findByAcademicProgram(program);
    }

    @Test
    void findByAcademicProgram_HappyPath_DifferentProgram() {
        String program = "Ingeniería Civil";
        List<Course> expectedCourses = Collections.singletonList(inactiveCourse);
        when(courseRepository.findByAcademicProgram(program)).thenReturn(expectedCourses);

        List<Course> result = courseRepository.findByAcademicProgram(program);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(program, result.get(0).getAcademicProgram());
        verify(courseRepository, times(1)).findByAcademicProgram(program);
    }

    @Test
    void findByAcademicProgram_Error_NoCoursesForProgram() {
        String program = "Programa Inexistente";
        when(courseRepository.findByAcademicProgram(program)).thenReturn(Collections.emptyList());

        List<Course> result = courseRepository.findByAcademicProgram(program);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(courseRepository, times(1)).findByAcademicProgram(program);
    }

    @Test
    void findByAcademicProgram_Error_NullParameter() {
        when(courseRepository.findByAcademicProgram(null)).thenReturn(Collections.emptyList());

        List<Course> result = courseRepository.findByAcademicProgram(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(courseRepository, times(1)).findByAcademicProgram(null);
    }

    @Test
    void findByCredits_HappyPath_ReturnsCourses() {
        Integer credits = 3;
        List<Course> expectedCourses = Collections.singletonList(activeCourse);
        when(courseRepository.findByCredits(credits)).thenReturn(expectedCourses);

        List<Course> result = courseRepository.findByCredits(credits);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(credits, result.get(0).getCredits());
        verify(courseRepository, times(1)).findByCredits(credits);
    }

    @Test
    void findByCredits_HappyPath_DifferentCredits() {
        Integer credits = 4;
        List<Course> expectedCourses = Collections.singletonList(inactiveCourse);
        when(courseRepository.findByCredits(credits)).thenReturn(expectedCourses);

        List<Course> result = courseRepository.findByCredits(credits);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(credits, result.get(0).getCredits());
        verify(courseRepository, times(1)).findByCredits(credits);
    }

    @Test
    void findByCredits_Error_NoCoursesWithCredits() {
        Integer credits = 5;
        when(courseRepository.findByCredits(credits)).thenReturn(Collections.emptyList());

        List<Course> result = courseRepository.findByCredits(credits);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(courseRepository, times(1)).findByCredits(credits);
    }

    @Test
    void findByCredits_Error_NullParameter() {
        when(courseRepository.findByCredits(null)).thenReturn(Collections.emptyList());

        List<Course> result = courseRepository.findByCredits(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(courseRepository, times(1)).findByCredits(null);
    }

    @Test
    void findByAcademicProgramAndIsActive_HappyPath_ActiveCourses() {
        String program = "Ingeniería de Sistemas";
        boolean isActive = true;
        List<Course> expectedCourses = Collections.singletonList(activeCourse);
        when(courseRepository.findByAcademicProgramAndIsActive(program, isActive))
                .thenReturn(expectedCourses);

        List<Course> result = courseRepository.findByAcademicProgramAndIsActive(program, isActive);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(program, result.get(0).getAcademicProgram());
        assertTrue(result.get(0).isActive());
        verify(courseRepository, times(1)).findByAcademicProgramAndIsActive(program, isActive);
    }

    @Test
    void findByAcademicProgramAndIsActive_HappyPath_InactiveCourses() {
        String program = "Ingeniería Civil";
        boolean isActive = false;
        List<Course> expectedCourses = Collections.singletonList(inactiveCourse);
        when(courseRepository.findByAcademicProgramAndIsActive(program, isActive))
                .thenReturn(expectedCourses);

        List<Course> result = courseRepository.findByAcademicProgramAndIsActive(program, isActive);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(program, result.get(0).getAcademicProgram());
        assertFalse(result.get(0).isActive());
        verify(courseRepository, times(1)).findByAcademicProgramAndIsActive(program, isActive);
    }

    @Test
    void findByAcademicProgramAndIsActive_Error_NoMatchingCourses() {
        String program = "Programa Inexistente";
        boolean isActive = true;
        when(courseRepository.findByAcademicProgramAndIsActive(program, isActive))
                .thenReturn(Collections.emptyList());

        List<Course> result = courseRepository.findByAcademicProgramAndIsActive(program, isActive);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(courseRepository, times(1)).findByAcademicProgramAndIsActive(program, isActive);
    }

    @Test
    void findByAcademicProgramAndIsActive_Error_NullProgram() {
        when(courseRepository.findByAcademicProgramAndIsActive(null, true))
                .thenReturn(Collections.emptyList());

        List<Course> result = courseRepository.findByAcademicProgramAndIsActive(null, true);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(courseRepository, times(1)).findByAcademicProgramAndIsActive(null, true);
    }
}
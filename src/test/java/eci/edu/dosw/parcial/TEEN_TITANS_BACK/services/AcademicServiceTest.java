package eci.edu.dosw.parcial.TEEN_TITANS_BACK.services;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dtos.AcademicDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Subject;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.AcademicCycle;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.AcademicPerformance;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.StudentProgress;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class AcademicServiceTest {

    @Test
    void testGetStudentDashboard() {
        AcademicService academicService = new AcademicService();
        String studentId = "12345";

        AcademicDTO result = academicService.getStudentDashboard(studentId);

        assertNotNull(result);
        assertNotNull(result.getCurrentSchedule());
        assertNotNull(result.getAcademicHistory());
        assertNotNull(result.getAcademicTrafficLight());
    }

    @Test
    void testGetCurrentSchedule() {
        AcademicService academicService = new AcademicService();
        String studentId = "12345";

        List<Subject> result = academicService.getCurrentSchedule(studentId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAcademicHistory() {
        AcademicService academicService = new AcademicService();
        String studentId = "12345";

        List<AcademicCycle> result = academicService.getAcademicHistory(studentId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAcademicTrafficLightWithNullPerformance() {
        AcademicService academicService = new AcademicService();
        String studentId = "12345";

        String result = academicService.getAcademicTrafficLight(studentId);

        assertEquals("AMARILLO", result);
    }

    @Test
    void testGetAcademicTrafficLightGreen() {
        AcademicService academicService = new AcademicService();
        String studentId = "12345";

        String result = academicService.getAcademicTrafficLight(studentId);

        assertNotNull(result);
        assertTrue(result.equals("VERDE") || result.equals("AMARILLO") || result.equals("ROJO"));
    }

    @Test
    void testGetAcademicTrafficLightWithValidStudent() {
        AcademicService academicService = new AcademicService();
        String studentId = "67890";

        String result = academicService.getAcademicTrafficLight(studentId);

        assertNotNull(result);
        assertEquals("AMARILLO", result);
    }

    @Test
    void testGetStudentDashboardWithDifferentStudentIds() {
        AcademicService academicService = new AcademicService();
        String studentId1 = "11111";
        String studentId2 = "22222";

        AcademicDTO result1 = academicService.getStudentDashboard(studentId1);
        AcademicDTO result2 = academicService.getStudentDashboard(studentId2);

        assertNotNull(result1);
        assertNotNull(result2);
        assertEquals(result1.getClass(), result2.getClass());
    }

    @Test
    void testAcademicTrafficLightConsistency() {
        AcademicService academicService = new AcademicService();
        String studentId = "12345";

        String result1 = academicService.getAcademicTrafficLight(studentId);
        String result2 = academicService.getAcademicTrafficLight(studentId);

        assertEquals(result1, result2);
    }

    @Test
    void testDashboardAcademicSituation() {
        AcademicService academicService = new AcademicService();
        String studentId = "12345";

        AcademicDTO result = academicService.getStudentDashboard(studentId);

        assertNull(result.getAcademicSituation());
        assertNull(result.getCumulativeAverage());
    }
}
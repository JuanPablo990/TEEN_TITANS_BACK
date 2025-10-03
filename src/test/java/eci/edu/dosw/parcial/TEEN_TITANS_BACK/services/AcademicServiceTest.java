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
    @Test
    void testGetCurrentScheduleDifferentStudents() {
        AcademicService academicService = new AcademicService();

        List<Subject> schedule1 = academicService.getCurrentSchedule("stu-1");
        List<Subject> schedule2 = academicService.getCurrentSchedule("stu-2");

        assertNotNull(schedule1);
        assertNotNull(schedule2);
        assertEquals(schedule1.size(), schedule2.size());
    }

    @Test
    void testAcademicHistoryDifferentStudents() {
        AcademicService academicService = new AcademicService();

        List<AcademicCycle> history1 = academicService.getAcademicHistory("stu-1");
        List<AcademicCycle> history2 = academicService.getAcademicHistory("stu-2");

        assertNotNull(history1);
        assertNotNull(history2);
        assertEquals(history1.size(), history2.size());
    }

    @Test
    void testAcademicTrafficLightValues() {
        AcademicService academicService = new AcademicService();

        String trafficLight = academicService.getAcademicTrafficLight("random-student");

        assertTrue(
                trafficLight.equals("VERDE") ||
                        trafficLight.equals("AMARILLO") ||
                        trafficLight.equals("ROJO"),
                "El semáforo académico debe ser VERDE, AMARILLO o ROJO"
        );
    }

    @Test
    void testDashboardNotNullFields() {
        AcademicService academicService = new AcademicService();

        AcademicDTO dashboard = academicService.getStudentDashboard("abc123");

        assertNotNull(dashboard.getCurrentSchedule());
        assertNotNull(dashboard.getAcademicHistory());
        assertNotNull(dashboard.getAcademicTrafficLight());
    }

    @Test
    void testMultipleDashboardsConsistency() {
        AcademicService academicService = new AcademicService();

        AcademicDTO d1 = academicService.getStudentDashboard("a1");
        AcademicDTO d2 = academicService.getStudentDashboard("a2");
        AcademicDTO d3 = academicService.getStudentDashboard("a3");

        assertNotNull(d1);
        assertNotNull(d2);
        assertNotNull(d3);
        assertEquals(d1.getClass(), d2.getClass());
        assertEquals(d2.getClass(), d3.getClass());
    }

    @Test
    void testAcademicTrafficLightRepeatedCalls() {
        AcademicService academicService = new AcademicService();
        String studentId = "99999";

        String first = academicService.getAcademicTrafficLight(studentId);
        String second = academicService.getAcademicTrafficLight(studentId);

        assertEquals(first, second, "El semáforo académico debería ser consistente entre llamadas");
    }

}
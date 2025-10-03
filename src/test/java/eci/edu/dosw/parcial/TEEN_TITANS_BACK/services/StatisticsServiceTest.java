package eci.edu.dosw.parcial.TEEN_TITANS_BACK.services;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dtos.DateRange;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Request;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.RequestStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.Date;
import java.util.Map;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StatisticsServiceTest {

    private StatisticsService statisticsService;

    @BeforeEach
    void setUp() {
        statisticsService = new StatisticsService();
    }

    @Test
    void testGetApprovalRejectionStatsEmpty() {
        DateRange dateRange = new DateRange();

        Map<String, Object> result = statisticsService.getApprovalRejectionStats(dateRange);

        assertEquals(0, result.get("total"));
    }

    @Test
    void testGetRequestVolumeStatsEmpty() {
        String timePeriod = "monthly";

        Map<String, Object> result = statisticsService.getRequestVolumeStats(timePeriod);

        assertEquals(0, result.get("totalRequests"));
    }

    @Test
    void testGetProcessingTimeStatsEmpty() {
        Map<String, Object> result = statisticsService.getProcessingTimeStats();

        assertEquals(0, result.get("totalProcessed"));
    }

    @Test
    void testGetFacultyApprovalStatsEmpty() {
        String faculty = "Engineering";
        DateRange dateRange = new DateRange();

        Map<String, Object> result = statisticsService.getFacultyApprovalStats(faculty, dateRange);

        assertEquals(0, result.get("totalRequests"));
    }

    @Test
    void testGetFacultyMostRequestedSubjectsEmpty() {
        String faculty = "Engineering";

        List<Map<String, Object>> result = statisticsService.getFacultyMostRequestedSubjects(faculty);

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetFacultyStudentPerformanceEmpty() {
        String faculty = "Engineering";

        Map<String, Object> result = statisticsService.getFacultyStudentPerformance(faculty);

        assertEquals(0, result.get("totalStudents"));
    }

    @Test
    void testGetFacultyCapacityUtilizationEmpty() {
        String faculty = "Engineering";

        Map<String, Object> result = statisticsService.getFacultyCapacityUtilization(faculty);

        assertEquals(0, result.get("totalSubjects"));
    }


    @Test
    void testGetGlobalMostRequestedSubjectsEmpty() {
        List<Map<String, Object>> result = statisticsService.getGlobalMostRequestedSubjects();

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetOverallAcademicProgressEmpty() {
        Map<String, Object> result = statisticsService.getOverallAcademicProgress();

        assertEquals(0, result.get("totalStudents"));
    }

    @Test
    void testGetInstitutionalCapacityOverviewEmpty() {
        Map<String, Object> result = statisticsService.getInstitutionalCapacityOverview();

        assertEquals(0, result.get("totalCapacity"));
    }

    @Test
    void testGetRequestVolumeStatsWithPeriod() {
        String timePeriod = "yearly";

        Map<String, Object> result = statisticsService.getRequestVolumeStats(timePeriod);

        assertEquals(timePeriod, result.get("timePeriod"));
    }

    @Test
    void testGetFacultyApprovalStatsWithFaculty() {
        String faculty = "Medicine";
        DateRange dateRange = new DateRange();

        Map<String, Object> result = statisticsService.getFacultyApprovalStats(faculty, dateRange);

        assertEquals(faculty, result.get("faculty"));
    }

    @Test
    void testGetFacultyStudentPerformanceWithFaculty() {
        String faculty = "Law";

        Map<String, Object> result = statisticsService.getFacultyStudentPerformance(faculty);

        assertEquals(faculty, result.get("faculty"));
    }

    @Test
    void testGetFacultyCapacityUtilizationWithFaculty() {
        String faculty = "Business";

        Map<String, Object> result = statisticsService.getFacultyCapacityUtilization(faculty);

        assertEquals(faculty, result.get("faculty"));
    }
}
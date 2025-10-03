package eci.edu.dosw.parcial.TEEN_TITANS_BACK.services;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dtos.AcademicDashboardDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Subject;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.AcademicCycle;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.AcademicPerformance;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.StudentProgress;
import java.util.List;
import java.util.ArrayList;

public class AcademicService {

    public AcademicDashboardDTO getStudentDashboard(String studentId) {
        AcademicDashboardDTO dashboard = new AcademicDashboardDTO();

        dashboard.setCurrentSchedule(getCurrentSchedule(studentId));
        dashboard.setAcademicHistory(getAcademicHistory(studentId));
        dashboard.setAcademicTrafficLight(getAcademicTrafficLight(studentId));
        dashboard.setStudentProgress(getStudentProgress(studentId));

        AcademicPerformance performance = getAcademicPerformance(studentId);
        if (performance != null) {
            dashboard.setCumulativeAverage(performance.getCumulativeAverage());
        }

        AcademicCycle currentCycle = getCurrentAcademicCycle(studentId);
        if (currentCycle != null) {
            dashboard.setAcademicSituation(currentCycle.getAcademicSituation());
        }

        return dashboard;
    }

    public List<Subject> getCurrentSchedule(String studentId) {
        return new ArrayList<>();
    }

    public List<AcademicCycle> getAcademicHistory(String studentId) {
        return new ArrayList<>();
    }

    public String getAcademicTrafficLight(String studentId) {
        AcademicPerformance performance = getAcademicPerformance(studentId);
        if (performance == null || performance.getCumulativeAverage() == null) {
            return "AMARILLO";
        }

        float average = performance.getCumulativeAverage();
        if (average >= 4.0f) {
            return "VERDE";
        } else if (average >= 3.0f) {
            return "AMARILLO";
        } else {
            return "ROJO";
        }
    }

    private AcademicPerformance getAcademicPerformance(String studentId) {
        return null;
    }

    private StudentProgress getStudentProgress(String studentId) {
        return null;
    }

    private AcademicCycle getCurrentAcademicCycle(String studentId) {
        return null;
    }
}
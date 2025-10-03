package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dtos;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Subject;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.AcademicCycle;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.StudentProgress;
import java.util.List;

public class AcademicDashboardDTO {
    private List<Subject> currentSchedule;
    private List<AcademicCycle> academicHistory;
    private String academicTrafficLight;
    private StudentProgress studentProgress;
    private Float cumulativeAverage;
    private String academicSituation;

    public List<Subject> getCurrentSchedule() {
        return currentSchedule;
    }

    public void setCurrentSchedule(List<Subject> currentSchedule) {
        this.currentSchedule = currentSchedule;
    }

    public List<AcademicCycle> getAcademicHistory() {
        return academicHistory;
    }

    public void setAcademicHistory(List<AcademicCycle> academicHistory) {
        this.academicHistory = academicHistory;
    }

    public String getAcademicTrafficLight() {
        return academicTrafficLight;
    }

    public void setAcademicTrafficLight(String academicTrafficLight) {
        this.academicTrafficLight = academicTrafficLight;
    }

    public StudentProgress getStudentProgress() {
        return studentProgress;
    }

    public void setStudentProgress(StudentProgress studentProgress) {
        this.studentProgress = studentProgress;
    }

    public Float getCumulativeAverage() {
        return cumulativeAverage;
    }

    public void setCumulativeAverage(Float cumulativeAverage) {
        this.cumulativeAverage = cumulativeAverage;
    }

    public String getAcademicSituation() {
        return academicSituation;
    }

    public void setAcademicSituation(String academicSituation) {
        this.academicSituation = academicSituation;
    }
}
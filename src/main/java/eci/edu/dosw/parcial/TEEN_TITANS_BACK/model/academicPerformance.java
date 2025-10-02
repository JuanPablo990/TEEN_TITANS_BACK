package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import java.util.List;

public class academicPerformance {
    private Float cumulativeAverage;
    private Float gradeAverage;
    private List<Float> semesterAverage;

    public Float getCumulativeAverage() {
        return cumulativeAverage;
    }

    public Float getGradeAverage() {
        return gradeAverage;
    }

    public List<Float> getSemesterAverage() {
        return semesterAverage;
    }
}
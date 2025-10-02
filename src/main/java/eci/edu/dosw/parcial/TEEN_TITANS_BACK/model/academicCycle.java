package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

public class academicCycle {
    private int cycleId;
    private int semesterToAttend;
    private int lastSemesterAttended;
    private String admissionCycle;
    private String academicSituation;
    private String programStatus;
    private String actionReason;
    private academicCalendar academicCalendar;
    private academicPerformance academicPerformance;
    private studentProgress studentProgress;

    public int getCycleId() {
        return cycleId;
    }

    public int getSemesterToAttend() {
        return semesterToAttend;
    }

    public int getLastSemesterAttended() {
        return lastSemesterAttended;
    }

    public String getAdmissionCycle() {
        return admissionCycle;
    }

    public String getAcademicSituation() {
        return academicSituation;
    }

    public String getProgramStatus() {
        return programStatus;
    }

    public String getActionReason() {
        return actionReason;
    }

    public academicCalendar getAcademicCalendar() {
        return academicCalendar;
    }

    public academicPerformance getAcademicPerformance() {
        return academicPerformance;
    }

    public studentProgress getStudentProgress() {
        return studentProgress;
    }
}
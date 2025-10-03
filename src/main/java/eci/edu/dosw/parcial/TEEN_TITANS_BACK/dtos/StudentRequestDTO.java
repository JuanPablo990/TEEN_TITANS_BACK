package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dtos;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Student;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Subject;

public class StudentRequestDTO {
    private String requestType;
    private Student student;
    private Subject originalSubject;
    private Subject targetSubject;
    private int originalGroup;
    private int targetGroup;
    private String reason;

    public String getRequestType() { return requestType; }
    public void setRequestType(String requestType) { this.requestType = requestType; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public Subject getOriginalSubject() { return originalSubject; }
    public void setOriginalSubject(Subject originalSubject) { this.originalSubject = originalSubject; }
    public Subject getTargetSubject() { return targetSubject; }
    public void setTargetSubject(Subject targetSubject) { this.targetSubject = targetSubject; }
    public int getOriginalGroup() { return originalGroup; }
    public void setOriginalGroup(int originalGroup) { this.originalGroup = originalGroup; }
    public int getTargetGroup() { return targetGroup; }
    public void setTargetGroup(int targetGroup) { this.targetGroup = targetGroup; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
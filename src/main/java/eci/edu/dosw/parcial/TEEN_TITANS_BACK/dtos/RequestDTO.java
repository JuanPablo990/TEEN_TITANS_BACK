package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dtos;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.RequestType;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Student;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Subject;

public class RequestDTO {
    private RequestType type;
    private Student student;
    private Subject originalSubject;
    private Subject targetSubject;
    private int originalGroup;
    private int targetGroup;
    private int priority;
    private String observations;

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Subject getOriginalSubject() {
        return originalSubject;
    }

    public void setOriginalSubject(Subject originalSubject) {
        this.originalSubject = originalSubject;
    }

    public Subject getTargetSubject() {
        return targetSubject;
    }

    public void setTargetSubject(Subject targetSubject) {
        this.targetSubject = targetSubject;
    }

    public int getOriginalGroup() {
        return originalGroup;
    }

    public void setOriginalGroup(int originalGroup) {
        this.originalGroup = originalGroup;
    }

    public int getTargetGroup() {
        return targetGroup;
    }

    public void setTargetGroup(int targetGroup) {
        this.targetGroup = targetGroup;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }
}
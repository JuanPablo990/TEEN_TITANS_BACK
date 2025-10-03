package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dtos;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Student;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Subject;
import java.util.Date;

public class DeaneryRequestDTO {
    private String id;
    private String type;
    private Student student;
    private Subject originalSubject;
    private Subject targetSubject;
    private int originalGroup;
    private int targetGroup;
    private String status;
    private int priority;
    private Date creationDate;
    private Date resolutionDate;
    private String observations;

    public DeaneryRequestDTO() {}

    public DeaneryRequestDTO(String id, String type, Student student, String status,
                             int priority, Date creationDate) {
        this.id = id;
        this.type = type;
        this.student = student;
        this.status = status;
        this.priority = priority;
        this.creationDate = creationDate;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
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
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    public Date getCreationDate() { return creationDate; }
    public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }
    public Date getResolutionDate() { return resolutionDate; }
    public void setResolutionDate(Date resolutionDate) { this.resolutionDate = resolutionDate; }
    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }
}
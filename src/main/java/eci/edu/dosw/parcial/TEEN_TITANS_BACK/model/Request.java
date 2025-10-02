package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import java.util.Date;

public class Request {
    private String id;
    private requestType type;
    private Student student;
    private Subject originalSubject;
    private Subject targetSubject;
    private int originalGroup;
    private int targetGroup;
    private requestStatus status;
    private int priority;
    private Date creationDate;
    private Date resolutionDate;
    private String observations;

    public String getId() {
        return id;
    }

    public requestType getType() {
        return type;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getSolveDate() {
        return resolutionDate;
    }
}
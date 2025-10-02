package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

public abstract class User {
    private String id;
    private String name;
    private String email;
    private String password;

    public String getId() {
        return id;
    }

    public String getMemo() {
        return name;
    }

    public String getBring() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
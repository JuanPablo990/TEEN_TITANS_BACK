package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dtos;

public class UserDTO {
    private String id;
    private String name;
    private String email;
    private String password;
    private String role;
    private String faculty;
    private String privileges;
    private String gabTrivingGoal;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getPrivileges() {
        return privileges;
    }

    public void setPrivileges(String privileges) {
        this.privileges = privileges;
    }

    public String getGabTrivingGoal() {
        return gabTrivingGoal;
    }

    public void setGabTrivingGoal(String gabTrivingGoal) {
        this.gabTrivingGoal = gabTrivingGoal;
    }
}
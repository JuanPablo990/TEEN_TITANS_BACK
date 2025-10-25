package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import lombok.Data;
import java.util.Date;

@Data
public class StudentDTO {
    private String id;
    private String name;
    private String email;
    private String password; // Solo para creación
    private String role;
    private Boolean active;
    private Date createdAt;
    private Date updatedAt;
    private String academicProgram;
    private Integer semester;
    private Double gradeAverage;
}
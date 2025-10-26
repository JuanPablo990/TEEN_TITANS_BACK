package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import lombok.Data;
import java.util.List;
import java.util.Date;

@Data
public class ProfessorDTO {
    private String id;
    private String name;
    private String email;
    private String department;
    private Boolean isTenured;
    private List<String> areasOfExpertise;
    private Boolean active;
    private Date createdAt;
    private Date updatedAt;


}
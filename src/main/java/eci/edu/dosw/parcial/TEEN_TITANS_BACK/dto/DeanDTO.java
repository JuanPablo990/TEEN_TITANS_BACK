package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import lombok.Data;
import java.util.Date;

@Data
public class DeanDTO {
    private String id;
    private String name;
    private String email;
    private String faculty;
    private String officeLocation;
    private Boolean active;
    private Date createdAt;
    private Date updatedAt;

    // ❌ NO incluye: password
}
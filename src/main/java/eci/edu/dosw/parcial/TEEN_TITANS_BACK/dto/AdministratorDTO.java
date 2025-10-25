package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import lombok.Data;
import java.util.Date;

@Data
public class AdministratorDTO {
    private String id;
    private String name;
    private String email;
    private String department;
    private Boolean active;
    private Date createdAt;
    private Date updatedAt;

    // ❌ NO incluye: password
}
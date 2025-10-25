package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import lombok.Data;
import java.util.Date;

@Data
public class UserDTO {
    private String id;
    private String name;
    private String email;
    private String password; // Solo para creación/actualización
    private String role;
    private Boolean active;
    private Date createdAt;
    private Date updatedAt;
}
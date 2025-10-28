package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeanDTO {

    private String id;
    private String name;
    private String email;
    private String faculty;
    private String officeLocation;
    private Boolean active;
    private Date createdAt;
    private Date updatedAt;
    private String password;
    private UserRole role;
}
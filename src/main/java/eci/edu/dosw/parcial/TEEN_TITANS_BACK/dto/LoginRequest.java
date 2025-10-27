package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
    private UserRole role;
}
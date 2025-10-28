package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import lombok.Data;
import java.util.Date;

/**
 * Data Transfer Object (DTO) para la entidad {@code Administrator}.
 *
 * Esta clase se utiliza para transferir información de administradores
 * entre las capas de la aplicación (por ejemplo, entre el backend y el frontend),
 * sin incluir información sensible como la contraseña.
 *
 * Contiene los datos principales de un administrador, incluyendo
 * su identificación, nombre, correo electrónico, departamento y estado.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Data
public class AdministratorDTO {
    private String id;
    private String name;
    private String email;
    private String password;
    private UserRole role;
    private String department;
    private Boolean active;
    private Date createdAt;
    private Date updatedAt;
}
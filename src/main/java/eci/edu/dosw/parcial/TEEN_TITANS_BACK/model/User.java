package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import java.util.Date;

/**
 * Representa un usuario del sistema universitario.
 * Esta clase es la base para todos los tipos de usuarios del sistema y encapsula
 * la información común como credenciales, roles y estado.
 *
 * @author Equipo Teen Titans
 * @version 2.0
 * @since 2025
 */
@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private UserRole role;
    private boolean active = true;
    private Date createdAt = new Date();
    private Date updatedAt = new Date();

    /**
     * Constructor para crear un nuevo usuario del sistema.
     *
     * @param id el identificador único del usuario
     * @param name el nombre completo del usuario
     * @param email el correo electrónico institucional
     * @param password la contraseña de acceso
     * @param role el rol del usuario en el sistema
     */
    public User(String id, String name, String email, String password, UserRole role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.active = true;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

}
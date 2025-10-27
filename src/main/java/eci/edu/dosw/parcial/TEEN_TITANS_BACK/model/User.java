package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import java.util.Date;

/**
 * Representa un usuario del sistema Teen Titans.
 *
 * Esta clase sirve como base para los diferentes tipos de usuarios
 * (estudiantes, profesores, decanos y administradores) y contiene la información
 * común a todos ellos, como credenciales, rol, estado y fechas de creación.
 *
 * Se almacena en la colección "users" de MongoDB.
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
     * Crea un nuevo usuario con los datos básicos requeridos.
     *
     * @param id       Identificador único del usuario.
     * @param name     Nombre completo del usuario.
     * @param email    Correo electrónico institucional.
     * @param password Contraseña de acceso.
     * @param role     Rol del usuario en el sistema.
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
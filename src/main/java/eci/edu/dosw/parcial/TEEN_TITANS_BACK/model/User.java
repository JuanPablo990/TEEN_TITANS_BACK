package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Representa un usuario del sistema universitario.
 * Esta clase es la base para todos los tipos de usuarios del sistema y encapsula
 * la información común como credenciales, roles y estado.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private UserRole role;
    private boolean active;
    private Date createdAt;
    private Date updatedAt;

    /**
     * Constructor vacío para la clase User.
     */
    public User() {
    }

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

    /**
     * Obtiene el identificador único del usuario.
     *
     * @return el ID del usuario como String
     */
    public String getId() {
        return id;
    }

    /**
     * Obtiene el nombre completo del usuario.
     *
     * @return el nombre del usuario como String
     */
    public String getName() {
        return name;
    }

    /**
     * Obtiene el correo electrónico institucional del usuario.
     *
     * @return el email del usuario como String
     */
    public String getEmail() {
        return email;
    }

    /**
     * Obtiene la contraseña de acceso del usuario.
     *
     * @return la contraseña como String
     */
    public String getPassword() {
        return password;
    }

    /**
     * Obtiene el rol del usuario dentro del sistema.
     *
     * @return el rol del usuario como UserRole
     */
    public UserRole getRole() {
        return role;
    }

    /**
     * Verifica si el usuario está activo en el sistema.
     *
     * @return true si el usuario está activo, false en caso contrario
     */
    public boolean getActive() {
        return active;
    }

    /**
     * Obtiene la fecha y hora de creación del usuario en el sistema.
     *
     * @return la fecha de creación como objeto Date
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Obtiene la fecha y hora de la última actualización del usuario.
     *
     * @return la fecha de última actualización como objeto Date
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Actualiza el identificador único del usuario.
     *
     * @param id el nuevo identificador del usuario
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Actualiza el nombre del usuario.
     *
     * @param name el nuevo nombre del usuario
     */
    public void setName(String name) {
        this.name = name;
        this.updatedAt = new Date();
    }

    /**
     * Actualiza el correo electrónico del usuario.
     *
     * @param email el nuevo correo electrónico
     */
    public void setEmail(String email) {
        this.email = email;
        this.updatedAt = new Date();
    }

    /**
     * Actualiza la contraseña del usuario.
     *
     * @param password la nueva contraseña
     */
    public void setPassword(String password) {
        this.password = password;
        this.updatedAt = new Date();
    }

    /**
     * Actualiza el rol del usuario en el sistema.
     *
     * @param role el nuevo rol del usuario
     */
    public void setRole(UserRole role) {
        this.role = role;
        this.updatedAt = new Date();
    }

    /**
     * Establece el estado de activación del usuario.
     *
     * @param active true para activar, false para desactivar
     */
    public void setActive(boolean active) {
        this.active = active;
        this.updatedAt = new Date();
    }

    /**
     * Actualiza la fecha de creación del usuario.
     *
     * @param createdAt la nueva fecha de creación
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Actualiza la fecha de última actualización del usuario.
     *
     * @param updatedAt la nueva fecha de última actualización
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Actualiza la marca de tiempo de última modificación.
     * Este método es utilizado por las clases hijas para actualizar el timestamp.
     */
    protected void updateTimestamp() {
        this.updatedAt = new Date();
    }
}
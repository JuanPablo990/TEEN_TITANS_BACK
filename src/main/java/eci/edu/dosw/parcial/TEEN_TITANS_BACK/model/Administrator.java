package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Representa un administrador dentro del sistema universitario.
 * Esta clase extiende de User y añade información del departamento
 * al que pertenece el administrador.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Document(collection = "administrators")
public class Administrator extends User {
    private String department;

    /**
     * Constructor vacío para la clase Administrator.
     */
    public Administrator() {
    }

    /**
     * Constructor para crear un nuevo administrador.
     *
     * @param id el identificador único del administrador
     * @param name el nombre completo del administrador
     * @param email el correo electrónico institucional
     * @param password la contraseña de acceso
     * @param department el departamento al que pertenece el administrador
     */
    public Administrator(String id, String name, String email, String password, String department) {
        super(id, name, email, password, UserRole.ADMINISTRATOR);
        this.department = department;
    }

    /**
     * Obtiene el departamento al que está asignado el administrador.
     *
     * @return el nombre del departamento como String
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Actualiza el departamento del administrador.
     *
     * @param department el nuevo departamento asignado
     */
    public void setDepartment(String department) {
        this.department = department;
        updateTimestamp();
    }
}
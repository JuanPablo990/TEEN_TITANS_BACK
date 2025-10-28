package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;

/**
 * Representa un administrador dentro del sistema Teen Titans.
 * Hereda de la clase {@link User} y añade información específica
 * como el departamento al que pertenece.
 *
 * Esta clase se almacena en la colección "administrators" de MongoDB.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Document(collection = "administrators")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Administrator extends User {
    private String department;

    /**
     * Crea un nuevo administrador con la información especificada.
     *
     * @param id          Identificador único del administrador.
     * @param name        Nombre del administrador.
     * @param email       Correo electrónico del administrador.
     * @param password    Contraseña del administrador.
     * @param department  Departamento al que pertenece el administrador.
     */
    public Administrator(String id, String name, String email, String password, String department) {
        super(id, name, email, password, UserRole.ADMINISTRATOR);
        this.department = department;
    }
}
package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;

/**
 * Representa a un decano dentro del sistema Teen Titans.
 * Hereda de la clase {@link User} y añade información específica
 * como la facultad y la ubicación de su oficina.
 *
 * Esta clase se almacena en la colección "deans" de MongoDB.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Document(collection = "deans")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Dean extends User {

    /**
     * Facultad a la que pertenece el decano.
     */
    private String faculty;

    /**
     * Ubicación de la oficina del decano.
     */
    private String officeLocation;

    /**
     * Crea un nuevo decano con la información especificada.
     *
     * @param id              Identificador único del decano.
     * @param name            Nombre del decano.
     * @param email           Correo electrónico del decano.
     * @param password        Contraseña del decano.
     * @param faculty         Facultad a la que pertenece el decano.
     * @param officeLocation  Ubicación de la oficina del decano.
     */
    public Dean(String id, String name, String email, String password,
                String faculty, String officeLocation) {
        super(id, name, email, password, UserRole.DEAN);
        this.faculty = faculty;
        this.officeLocation = officeLocation;
    }
}
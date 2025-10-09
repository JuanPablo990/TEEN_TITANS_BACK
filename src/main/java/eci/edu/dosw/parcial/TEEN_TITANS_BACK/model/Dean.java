package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Representa un decano dentro del sistema universitario.
 * Esta clase extiende de User y añade información específica de la facultad
 * y ubicación de oficina del decano.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */

@Document(collection = "deans")
public class Dean extends User {
    private String faculty;
    private String officeLocation;

    /**
     * Constructor vacío para la clase Dean.
     */
    public Dean() {
    }

    /**
     * Constructor para crear un nuevo decano.
     *
     * @param id el identificador único del decano
     * @param name el nombre completo del decano
     * @param email el correo electrónico institucional
     * @param password la contraseña de acceso
     * @param faculty la facultad que dirige el decano
     * @param officeLocation la ubicación de la oficina del decano
     */
    public Dean(String id, String name, String email, String password,
                String faculty, String officeLocation) {
        super(id, name, email, password, UserRole.DEAN);
        this.faculty = faculty;
        this.officeLocation = officeLocation;
    }

    /**
     * Obtiene la facultad que está bajo la dirección del decano.
     *
     * @return el nombre de la facultad como String
     */
    public String getFaculty() {
        return faculty;
    }

    /**
     * Obtiene la ubicación física de la oficina del decano.
     *
     * @return la ubicación de la oficina como String
     */
    public String getOfficeLocation() {
        return officeLocation;
    }

    /**
     * Actualiza la facultad asignada al decano.
     *
     * @param faculty la nueva facultad asignada
     */
    public void setFaculty(String faculty) {
        this.faculty = faculty;
        updateTimestamp();
    }

    /**
     * Actualiza la ubicación de la oficina del decano.
     *
     * @param officeLocation la nueva ubicación de oficina
     */
    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
        updateTimestamp();
    }
}
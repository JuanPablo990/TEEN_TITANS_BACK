package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import java.util.List;

/**
 * Representa a un profesor dentro del sistema Teen Titans.
 * Hereda de la clase {@link User} y añade información específica
 * como el departamento, el estado de titularidad y las áreas de especialización.
 *
 * Esta clase se almacena en la colección "professors" de MongoDB.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Document(collection = "professors")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Professor extends User {

    private String department;
    private Boolean isTenured;
    private List<String> areasOfExpertise;

    /**
     * Crea un nuevo profesor con la información especificada.
     *
     * @param id                Identificador único del profesor.
     * @param name              Nombre del profesor.
     * @param email             Correo electrónico del profesor.
     * @param password          Contraseña del profesor.
     * @param department        Departamento al que pertenece el profesor.
     * @param isTenured         Indica si el profesor es titular.
     * @param areasOfExpertise  Lista de áreas de especialización del profesor.
     */
    public Professor(String id, String name, String email, String password,
                     String department, Boolean isTenured, List<String> areasOfExpertise) {
        super(id, name, email, password, UserRole.PROFESSOR);
        this.department = department;
        this.isTenured = isTenured;
        this.areasOfExpertise = areasOfExpertise;
    }
}
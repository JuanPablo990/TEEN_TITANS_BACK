package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import lombok.Data;
import java.util.List;
import java.util.Date;

/**
 * Data Transfer Object (DTO) para la entidad {@code Professor}.
 *
 * Esta clase se utiliza para transferir la información de los profesores
 * entre las capas de la aplicación sin incluir datos sensibles como la contraseña.
 *
 * Incluye información académica y laboral del profesor, como su departamento,
 * estado de titularidad y áreas de especialización.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Data
public class ProfessorDTO {
    private String id;
    private String name;
    private String email;
    private String department;
    private Boolean isTenured;
    private List<String> areasOfExpertise;
    private Boolean active;
    private Date createdAt;
    private Date updatedAt;
}
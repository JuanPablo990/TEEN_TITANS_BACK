package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

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

    /** Identificador único del administrador. */
    private String id;

    /** Nombre completo del administrador. */
    private String name;

    /** Correo electrónico institucional del administrador. */
    private String email;

    /** Departamento al que pertenece el administrador. */
    private String department;

    /** Estado de actividad del administrador (activo o inactivo). */
    private Boolean active;

    /** Fecha de creación del registro del administrador. */
    private Date createdAt;

    /** Fecha de la última actualización del registro del administrador. */
    private Date updatedAt;

}
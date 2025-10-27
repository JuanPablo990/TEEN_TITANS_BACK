package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Representa a un estudiante dentro del sistema Teen Titans.
 * Hereda de la clase {@link User} y agrega información académica específica
 * como el programa académico, el semestre y el promedio de calificaciones.
 *
 * Esta clase se almacena en la colección "students" de MongoDB.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Document(collection = "students")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Student extends User {
    private String academicProgram;
    private Integer semester;
    private Double gradeAverage = 0.0;

    /**
     * Crea un nuevo estudiante con la información especificada.
     *
     * @param id               Identificador único del estudiante.
     * @param name             Nombre del estudiante.
     * @param email            Correo electrónico del estudiante.
     * @param password         Contraseña del estudiante.
     * @param academicProgram  Programa académico al que pertenece el estudiante.
     * @param semester         Semestre actual del estudiante.
     */
    public Student(String id, String name, String email, String password,
                   String academicProgram, Integer semester) {
        super(id, name, email, password, UserRole.STUDENT);
        this.academicProgram = academicProgram;
        this.semester = semester;
        this.gradeAverage = 0.0;
    }
}
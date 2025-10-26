package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Representa un grupo de estudiantes para un curso específico.
 * Esta clase encapsula la información de un grupo académico, incluyendo su sección,
 * curso asignado, profesor, horario y aula.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Document(collection = "groups")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Group {
    @Id
    private String groupId;
    private String section;

    @DBRef
    private Course course;

    @DBRef
    private Professor professor;

    @DBRef
    private Schedule schedule;

    @DBRef
    private Classroom classroom;

    /**
     * Constructor sin el groupId, para cuando MongoDB genera automáticamente el ID.
     *
     * @param section Sección específica del grupo
     * @param course Curso asociado al grupo
     * @param professor Profesor asignado al grupo
     * @param schedule Horario establecido para el grupo
     * @param classroom Aula asignada al grupo
     */
    public Group(String section, Course course, Professor professor, Schedule schedule, Classroom classroom) {
        this.section = section;
        this.course = course;
        this.professor = professor;
        this.schedule = schedule;
        this.classroom = classroom;
    }
}
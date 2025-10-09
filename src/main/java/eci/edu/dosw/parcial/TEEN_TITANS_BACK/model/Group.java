package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

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
     * Constructor vacío. Requerido para frameworks que necesitan instanciar la clase sin parámetros.
     */
    public Group() {
    }

    /**
     * Constructor con todos los parámetros.
     *
     * @param groupId Identificador único del grupo
     * @param section Sección específica del grupo
     * @param course Curso asociado al grupo
     * @param professor Profesor asignado al grupo
     * @param schedule Horario establecido para el grupo
     * @param classroom Aula asignada al grupo
     */
    public Group(String groupId, String section, Course course, Professor professor, Schedule schedule, Classroom classroom) {
        this.groupId = groupId;
        this.section = section;
        this.course = course;
        this.professor = professor;
        this.schedule = schedule;
        this.classroom = classroom;
    }

    /**
     * Obtiene el identificador único del grupo.
     *
     * @return el ID del grupo como String
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * Obtiene la sección específica del grupo.
     *
     * @return la sección del grupo como String
     */
    public String getSection() {
        return section;
    }

    /**
     * Obtiene el curso asociado a este grupo.
     *
     * @return el objeto Course asociado al grupo
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Obtiene el profesor asignado a este grupo.
     *
     * @return el objeto Professor asignado al grupo
     */
    public Professor getProfessor() {
        return professor;
    }

    /**
     * Obtiene el horario establecido para este grupo.
     *
     * @return el objeto Schedule del grupo
     */
    public Schedule getSchedule() {
        return schedule;
    }

    /**
     * Obtiene el aula asignada para este grupo.
     *
     * @return el objeto Classroom asignado al grupo
     */
    public Classroom getClassroom() {
        return classroom;
    }
}
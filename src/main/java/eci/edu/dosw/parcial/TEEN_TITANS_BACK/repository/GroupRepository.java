package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para manejar las operaciones CRUD y consultas personalizadas
 * sobre la colección de grupos (Group) en MongoDB.
 *
 * Este repositorio permite realizar búsquedas basadas en criterios específicos
 * como la sección, el curso, el profesor y el aula asignada, además de las
 * operaciones CRUD básicas proporcionadas por Spring Data.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Repository
public interface GroupRepository extends MongoRepository<Group, String> {

    /**
     * Busca todos los grupos pertenecientes a una sección específica.
     *
     * @param section Sección a la que pertenece el grupo.
     * @return Lista de grupos asociados a la sección indicada.
     */
    List<Group> findBySection(String section);

    /**
     * Busca todos los grupos que dictan un curso específico.
     *
     * @param courseCode Código único del curso.
     * @return Lista de grupos que imparten el curso indicado.
     */
    List<Group> findByCourse_CourseCode(String courseCode);

    /**
     * Busca todos los grupos dictados por un profesor específico.
     *
     * @param professorId Identificador único del profesor.
     * @return Lista de grupos dictados por el profesor indicado.
     */
    List<Group> findByProfessor_Id(String professorId);

    /**
     * Busca todos los grupos asignados a un aula específica.
     *
     * @param classroomId Identificador único del aula.
     * @return Lista de grupos que se desarrollan en el aula indicada.
     */
    List<Group> findByClassroom_ClassroomId(String classroomId);
}
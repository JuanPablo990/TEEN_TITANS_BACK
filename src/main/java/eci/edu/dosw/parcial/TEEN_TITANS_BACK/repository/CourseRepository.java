package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Course;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para manejar las operaciones CRUD y consultas personalizadas
 * sobre la colección de cursos (Course) en MongoDB.
 *
 * Este repositorio permite realizar búsquedas basadas en diferentes criterios
 * como nombre, programa académico, número de créditos y estado (activo o inactivo),
 * además de las operaciones CRUD básicas proporcionadas por Spring Data.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Repository
public interface CourseRepository extends MongoRepository<Course, String> {

    /**
     * Busca un curso por su nombre exacto.
     *
     * @param name Nombre del curso.
     * @return El curso correspondiente o null si no existe.
     */
    Course findByName(String name);

    /**
     * Obtiene todos los cursos que están activos o inactivos.
     *
     * @param isActive Estado del curso (true = activo, false = inactivo).
     * @return Lista de cursos filtrados por estado.
     */
    List<Course> findByIsActive(boolean isActive);

    /**
     * Busca todos los cursos pertenecientes a un programa académico específico.
     *
     * @param academicProgram Nombre del programa académico.
     * @return Lista de cursos que pertenecen al programa indicado.
     */
    List<Course> findByAcademicProgram(String academicProgram);

    /**
     * Busca todos los cursos que tienen una cantidad específica de créditos.
     *
     * @param credits Número de créditos.
     * @return Lista de cursos que coinciden con el número de créditos indicado.
     */
    List<Course> findByCredits(Integer credits);

    /**
     * Busca los cursos activos que pertenecen a un programa académico determinado.
     *
     * @param academicProgram Programa académico del curso.
     * @param isActive Estado del curso (true = activo).
     * @return Lista de cursos activos del programa académico especificado.
     */
    List<Course> findByAcademicProgramAndIsActive(String academicProgram, boolean isActive);
}

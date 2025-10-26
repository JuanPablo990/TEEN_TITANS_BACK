package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Course;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    // Búsquedas básicas por atributos individuales
    Optional<Course> findByName(String name);
    List<Course> findByIsActive(boolean isActive);
    List<Course> findByAcademicProgram(String academicProgram);
    List<Course> findByCredits(Integer credits);

    // Búsquedas por rangos de créditos
    List<Course> findByCreditsGreaterThan(Integer credits);
    List<Course> findByCreditsLessThan(Integer credits);
    List<Course> findByCreditsBetween(Integer minCredits, Integer maxCredits);
    List<Course> findByCreditsGreaterThanEqual(Integer credits);
    List<Course> findByCreditsLessThanEqual(Integer credits);

    // Búsquedas combinadas
    List<Course> findByAcademicProgramAndIsActive(String academicProgram, boolean isActive);
    List<Course> findByAcademicProgramAndCredits(String academicProgram, Integer credits);
    List<Course> findByIsActiveAndCreditsGreaterThan(boolean isActive, Integer credits);
    List<Course> findByAcademicProgramAndCreditsGreaterThanEqual(String academicProgram, Integer credits);
    List<Course> findByAcademicProgramAndIsActiveAndCreditsBetween(String academicProgram, boolean isActive, Integer minCredits, Integer maxCredits);

    // Búsquedas por texto (ignore case y containing)
    List<Course> findByNameContainingIgnoreCase(String name);
    List<Course> findByAcademicProgramContainingIgnoreCase(String academicProgram);
    List<Course> findByDescriptionContainingIgnoreCase(String description);
    List<Course> findByNameContaining(String name);
    List<Course> findByAcademicProgramContaining(String academicProgram);

    // Búsquedas con ordenamiento
    List<Course> findByOrderByNameAsc();
    List<Course> findByOrderByCreditsAsc();
    List<Course> findByOrderByCreditsDesc();
    List<Course> findByAcademicProgramOrderByNameAsc(String academicProgram);
    List<Course> findByIsActiveOrderByNameAsc(boolean isActive);
    List<Course> findByAcademicProgramOrderByCreditsDesc(String academicProgram);
    List<Course> findByIsActiveOrderByCreditsDesc(boolean isActive);

    // Consultas de conteo
    long countByAcademicProgram(String academicProgram);
    long countByIsActive(boolean isActive);
    long countByCredits(Integer credits);
    long countByAcademicProgramAndIsActive(String academicProgram, boolean isActive);
    long countByCreditsGreaterThan(Integer credits);
    long countByCreditsBetween(Integer minCredits, Integer maxCredits);

    // Consultas personalizadas con @Query
    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    List<Course> findByNameRegex(String namePattern);

    @Query("{ 'academicProgram': { $regex: ?0, $options: 'i' } }")
    List<Course> findByAcademicProgramRegex(String academicProgramPattern);

    @Query("{ 'description': { $regex: ?0, $options: 'i' } }")
    List<Course> findByDescriptionRegex(String descriptionPattern);

    @Query("{ 'name': { $regex: ?0, $options: 'i' }, 'isActive': ?1 }")
    List<Course> findByNamePatternAndActiveStatus(String namePattern, boolean isActive);

    @Query("{ 'academicProgram': ?0, 'credits': { $gte: ?1 } }")
    List<Course> findByProgramAndMinimumCredits(String academicProgram, Integer minCredits);

    @Query("{ 'credits': { $gte: ?0, $lte: ?1 } }")
    List<Course> findByCreditsRange(Integer minCredits, Integer maxCredits);

    @Query("{ 'academicProgram': ?0, 'credits': { $gte: ?1, $lte: ?2 } }")
    List<Course> findByProgramAndCreditsRange(String academicProgram, Integer minCredits, Integer maxCredits);

    @Query(value = "{ 'academicProgram': ?0 }", sort = "{ 'name': 1 }")
    List<Course> findByAcademicProgramSortedByName(String academicProgram);

    @Query(value = "{ 'isActive': true }", sort = "{ 'credits': -1 }")
    List<Course> findActiveCoursesSortedByCreditsDesc();

    @Query(value = "{ 'academicProgram': ?0, 'isActive': ?1 }", sort = "{ 'name': 1 }")
    List<Course> findByProgramAndActiveStatusSortedByName(String academicProgram, boolean isActive);

    // Consultas para búsqueda avanzada
    @Query("""
        { 
            $or: [
                { 'name': { $regex: ?0, $options: 'i' } },
                { 'description': { $regex: ?0, $options: 'i' } },
                { 'academicProgram': { $regex: ?0, $options: 'i' } }
            ],
            'isActive': ?1
        }
    """)
    List<Course> searchCourses(String searchTerm, boolean isActive);

    @Query("""
        { 
            $or: [
                { 'name': { $regex: ?0, $options: 'i' } },
                { 'description': { $regex: ?0, $options: 'i' } }
            ],
            'academicProgram': ?1,
            'isActive': ?2
        }
    """)
    List<Course> searchCoursesInProgram(String searchTerm, String academicProgram, boolean isActive);

    // Verificación de existencia
    boolean existsByName(String name);
    boolean existsByAcademicProgram(String academicProgram);
    boolean existsByNameAndAcademicProgram(String name, String academicProgram);
    boolean existsByCourseCodeAndIsActive(String courseCode, boolean isActive);

    // Búsqueda por múltiples valores
    List<Course> findByAcademicProgramIn(List<String> academicPrograms);
    List<Course> findByCreditsIn(List<Integer> credits);
    List<Course> findByIsActiveAndAcademicProgramIn(boolean isActive, List<String> academicPrograms);
    List<Course> findByCourseCodeIn(List<String> courseCodes);

    // Búsqueda de cursos con descripción no vacía
    List<Course> findByDescriptionIsNotNull();
    List<Course> findByDescriptionIsNotNullAndIsActive(boolean isActive);

    // Búsqueda de cursos populares (con muchos créditos)
    @Query("{ 'credits': { $gte: ?0 } }")
    List<Course> findHighCreditCourses(Integer minCredits);

    // Búsqueda de cursos básicos (pocos créditos)
    @Query("{ 'credits': { $lte: ?0 } }")
    List<Course> findLowCreditCourses(Integer maxCredits);

    // Búsqueda de un curso por código exacto (alternativa al findById)
    Optional<Course> findByCourseCode(String courseCode);
}
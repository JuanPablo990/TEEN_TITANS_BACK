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
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Repository
public interface CourseRepository extends MongoRepository<Course, String> {

    Optional<Course> findByName(String name);

    List<Course> findByIsActive(boolean isActive);

    List<Course> findByAcademicProgram(String academicProgram);

    List<Course> findByCredits(Integer credits);

    List<Course> findByCreditsGreaterThan(Integer credits);

    List<Course> findByCreditsLessThan(Integer credits);

    List<Course> findByCreditsBetween(Integer minCredits, Integer maxCredits);

    List<Course> findByCreditsGreaterThanEqual(Integer credits);

    List<Course> findByCreditsLessThanEqual(Integer credits);

    List<Course> findByAcademicProgramAndIsActive(String academicProgram, boolean isActive);

    List<Course> findByAcademicProgramAndCredits(String academicProgram, Integer credits);

    List<Course> findByIsActiveAndCreditsGreaterThan(boolean isActive, Integer credits);

    List<Course> findByAcademicProgramAndCreditsGreaterThanEqual(String academicProgram, Integer credits);

    List<Course> findByAcademicProgramAndIsActiveAndCreditsBetween(String academicProgram, boolean isActive, Integer minCredits, Integer maxCredits);

    List<Course> findByNameContainingIgnoreCase(String name);

    List<Course> findByAcademicProgramContainingIgnoreCase(String academicProgram);

    List<Course> findByDescriptionContainingIgnoreCase(String description);

    List<Course> findByNameContaining(String name);

    List<Course> findByAcademicProgramContaining(String academicProgram);

    List<Course> findByOrderByNameAsc();

    List<Course> findByOrderByCreditsAsc();

    List<Course> findByOrderByCreditsDesc();

    List<Course> findByAcademicProgramOrderByNameAsc(String academicProgram);

    List<Course> findByIsActiveOrderByNameAsc(boolean isActive);

    List<Course> findByAcademicProgramOrderByCreditsDesc(String academicProgram);

    List<Course> findByIsActiveOrderByCreditsDesc(boolean isActive);

    long countByAcademicProgram(String academicProgram);

    long countByIsActive(boolean isActive);

    long countByCredits(Integer credits);

    long countByAcademicProgramAndIsActive(String academicProgram, boolean isActive);

    long countByCreditsGreaterThan(Integer credits);

    long countByCreditsBetween(Integer minCredits, Integer maxCredits);

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

    @Query("{ $or: [ { 'name': { $regex: ?0, $options: 'i' } }, { 'description': { $regex: ?0, $options: 'i' } }, { 'academicProgram': { $regex: ?0, $options: 'i' } } ], 'isActive': ?1 }")
    List<Course> searchCourses(String searchTerm, boolean isActive);

    @Query("{ $or: [ { 'name': { $regex: ?0, $options: 'i' } }, { 'description': { $regex: ?0, $options: 'i' } } ], 'academicProgram': ?1, 'isActive': ?2 }")
    List<Course> searchCoursesInProgram(String searchTerm, String academicProgram, boolean isActive);

    boolean existsByName(String name);

    boolean existsByAcademicProgram(String academicProgram);

    boolean existsByNameAndAcademicProgram(String name, String academicProgram);

    boolean existsByCourseCodeAndIsActive(String courseCode, boolean isActive);

    List<Course> findByAcademicProgramIn(List<String> academicPrograms);

    List<Course> findByCreditsIn(List<Integer> credits);

    List<Course> findByIsActiveAndAcademicProgramIn(boolean isActive, List<String> academicPrograms);

    List<Course> findByCourseCodeIn(List<String> courseCodes);

    List<Course> findByDescriptionIsNotNull();

    List<Course> findByDescriptionIsNotNullAndIsActive(boolean isActive);

    @Query("{ 'credits': { $gte: ?0 } }")
    List<Course> findHighCreditCourses(Integer minCredits);

    @Query("{ 'credits': { $lte: ?0 } }")
    List<Course> findLowCreditCourses(Integer maxCredits);

    Optional<Course> findByCourseCode(String courseCode);
}
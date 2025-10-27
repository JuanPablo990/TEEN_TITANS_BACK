package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Professor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la gestión de profesores en la base de datos MongoDB.
 * Proporciona métodos para buscar profesores por diferentes criterios.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Repository
public interface ProfessorRepository extends MongoRepository<Professor, String> {

    List<Professor> findByDepartment(String department);
    List<Professor> findByIsTenured(Boolean isTenured);
    List<Professor> findByAreasOfExpertiseContaining(String areaOfExpertise);

    List<Professor> findByDepartmentAndIsTenured(String department, Boolean isTenured);
    List<Professor> findByDepartmentAndIsTenuredTrue(String department);
    List<Professor> findByDepartmentAndIsTenuredFalse(String department);

    List<Professor> findByAreasOfExpertiseIn(List<String> areasOfExpertise);

    List<Professor> findByName(String name);
    List<Professor> findByEmail(String email);
    List<Professor> findByNameContainingIgnoreCase(String name);
    List<Professor> findByEmailContainingIgnoreCase(String email);
    List<Professor> findByActive(boolean active);

    List<Professor> findByDepartmentAndActive(String department, boolean active);
    List<Professor> findByIsTenuredAndActive(Boolean isTenured, boolean active);
    List<Professor> findByNameAndDepartment(String name, String department);
    List<Professor> findByNameAndIsTenured(String name, Boolean isTenured);

    List<Professor> findByOrderByNameAsc();
    List<Professor> findByDepartmentOrderByNameAsc(String department);
    List<Professor> findByIsTenuredTrueOrderByNameAsc();

    long countByDepartment(String department);
    long countByIsTenured(Boolean isTenured);
    long countByDepartmentAndIsTenured(String department, Boolean isTenured);
    long countByAreasOfExpertiseContaining(String areaOfExpertise);

    @Query("{ 'department': { $regex: ?0, $options: 'i' } }")
    List<Professor> findByDepartmentRegex(String departmentPattern);

    @Query("{ 'areasOfExpertise': { $in: ?0 } }")
    List<Professor> findByAreasOfExpertiseList(List<String> areasOfExpertise);

    @Query("{ 'name': { $regex: ?0, $options: 'i' }, 'department': ?1 }")
    List<Professor> findByNamePatternAndDepartment(String namePattern, String department);

    @Query("{ 'areasOfExpertise': { $size: ?0 } }")
    List<Professor> findByNumberOfAreasOfExpertise(int numberOfAreas);

    @Query("{ 'areasOfExpertise': { $size: { $gte: ?0 } } }")
    List<Professor> findByMinimumAreasOfExpertise(int minAreas);

    boolean existsByDepartment(String department);
    boolean existsByEmail(String email);
    boolean existsByDepartmentAndIsTenured(String department, Boolean isTenured);

    List<Professor> findByDepartmentIn(List<String> departments);
}
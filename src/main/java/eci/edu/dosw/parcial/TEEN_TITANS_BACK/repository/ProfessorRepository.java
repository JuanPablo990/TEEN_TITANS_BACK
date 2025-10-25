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

    // Búsquedas por atributos específicos de Professor
    List<Professor> findByDepartment(String department);
    List<Professor> findByIsTenured(Boolean isTenured);
    List<Professor> findByAreasOfExpertiseContaining(String areaOfExpertise);

    // Búsquedas por departamento y titularidad
    List<Professor> findByDepartmentAndIsTenured(String department, Boolean isTenured);
    List<Professor> findByDepartmentAndIsTenuredTrue(String department);
    List<Professor> findByDepartmentAndIsTenuredFalse(String department);

    // Búsquedas por múltiples áreas de expertise
    List<Professor> findByAreasOfExpertiseIn(List<String> areasOfExpertise);

    // Búsquedas por atributos heredados de User
    List<Professor> findByName(String name);
    List<Professor> findByEmail(String email);
    List<Professor> findByNameContainingIgnoreCase(String name);
    List<Professor> findByEmailContainingIgnoreCase(String email);
    List<Professor> findByActive(boolean active);

    // Búsquedas combinadas con atributos heredados
    List<Professor> findByDepartmentAndActive(String department, boolean active);
    List<Professor> findByIsTenuredAndActive(Boolean isTenured, boolean active);
    List<Professor> findByNameAndDepartment(String name, String department);
    List<Professor> findByNameAndIsTenured(String name, Boolean isTenured);

    // Búsquedas con ordenamiento
    List<Professor> findByOrderByNameAsc();
    List<Professor> findByDepartmentOrderByNameAsc(String department);
    List<Professor> findByIsTenuredTrueOrderByNameAsc();

    // Consultas de conteo
    long countByDepartment(String department);
    long countByIsTenured(Boolean isTenured);
    long countByDepartmentAndIsTenured(String department, Boolean isTenured);
    long countByAreasOfExpertiseContaining(String areaOfExpertise);

    // Consultas personalizadas con @Query
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

    // Verificación de existencia
    boolean existsByDepartment(String department);
    boolean existsByEmail(String email);
    boolean existsByDepartmentAndIsTenured(String department, Boolean isTenured);

    // Búsqueda de profesores por múltiples departamentos
    List<Professor> findByDepartmentIn(List<String> departments);
}
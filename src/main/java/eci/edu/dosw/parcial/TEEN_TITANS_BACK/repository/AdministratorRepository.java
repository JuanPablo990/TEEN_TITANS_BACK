package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Administrator;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la gestión de administradores en la base de datos MongoDB.
 * Proporciona métodos para buscar administradores por diferentes criterios.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Repository
public interface AdministratorRepository extends MongoRepository<Administrator, String> {

    // Búsquedas por atributos específicos de Administrator
    List<Administrator> findByDepartment(String department);

    // Búsquedas por departamento (parciales)
    List<Administrator> findByDepartmentContainingIgnoreCase(String department);

    // Búsquedas por atributos heredados de User
    List<Administrator> findByName(String name);
    List<Administrator> findByEmail(String email);
    List<Administrator> findByNameContainingIgnoreCase(String name);
    List<Administrator> findByEmailContainingIgnoreCase(String email);
    List<Administrator> findByActive(boolean active);

    // Búsquedas combinadas con atributos heredados
    List<Administrator> findByDepartmentAndActive(String department, boolean active);
    List<Administrator> findByNameAndDepartment(String name, String department);
    List<Administrator> findByEmailAndDepartment(String email, String department);

    // Búsquedas con ordenamiento
    List<Administrator> findByOrderByNameAsc();
    List<Administrator> findByDepartmentOrderByNameAsc(String department);
    List<Administrator> findByOrderByDepartmentAsc();

    // Consultas de conteo
    long countByDepartment(String department);
    long countByDepartmentAndActive(String department, boolean active);
    long countByActive(boolean active);

    // Consultas personalizadas con @Query
    @Query("{ 'department': { $regex: ?0, $options: 'i' } }")
    List<Administrator> findByDepartmentRegex(String departmentPattern);

    @Query("{ 'name': { $regex: ?0, $options: 'i' }, 'department': ?1 }")
    List<Administrator> findByNamePatternAndDepartment(String namePattern, String department);

    @Query("{ 'department': ?0, 'active': ?1 }")
    List<Administrator> findByDepartmentAndActiveStatus(String department, boolean active);

    // Verificación de existencia
    boolean existsByDepartment(String department);
    boolean existsByEmail(String email);
    boolean existsByDepartmentAndActive(String department, boolean active);

    // Búsqueda por múltiples departamentos
    List<Administrator> findByDepartmentIn(List<String> departments);
}
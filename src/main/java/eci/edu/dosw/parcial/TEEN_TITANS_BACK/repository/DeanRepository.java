package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Dean;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la gestión de decanos en la base de datos MongoDB.
 * Proporciona métodos para buscar decanos por diferentes criterios.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Repository
public interface DeanRepository extends MongoRepository<Dean, String> {

    // Búsquedas por atributos específicos de Dean
    List<Dean> findByFaculty(String faculty);
    List<Dean> findByOfficeLocation(String officeLocation);

    // Búsquedas combinadas específicas
    List<Dean> findByFacultyAndOfficeLocation(String faculty, String officeLocation);
    List<Dean> findByFacultyContainingIgnoreCase(String faculty);
    List<Dean> findByOfficeLocationContainingIgnoreCase(String officeLocation);

    // Búsquedas por atributos heredados de User
    List<Dean> findByName(String name);
    List<Dean> findByEmail(String email);
    List<Dean> findByNameContainingIgnoreCase(String name);
    List<Dean> findByEmailContainingIgnoreCase(String email);
    List<Dean> findByActive(boolean active);

    // Búsquedas combinadas con atributos heredados
    List<Dean> findByFacultyAndActive(String faculty, boolean active);
    List<Dean> findByOfficeLocationAndActive(String officeLocation, boolean active);
    List<Dean> findByNameAndFaculty(String name, String faculty);
    List<Dean> findByNameAndOfficeLocation(String name, String officeLocation);

    // Búsquedas con ordenamiento
    List<Dean> findByOrderByNameAsc();
    List<Dean> findByFacultyOrderByNameAsc(String faculty);
    List<Dean> findByOrderByFacultyAsc();

    // Consultas de conteo
    long countByFaculty(String faculty);
    long countByOfficeLocation(String officeLocation);
    long countByFacultyAndActive(String faculty, boolean active);

    // Consultas personalizadas con @Query
    @Query("{ 'faculty': { $regex: ?0, $options: 'i' } }")
    List<Dean> findByFacultyRegex(String facultyPattern);

    @Query("{ 'officeLocation': { $regex: ?0, $options: 'i' } }")
    List<Dean> findByOfficeLocationRegex(String officeLocationPattern);

    @Query("{ 'name': { $regex: ?0, $options: 'i' }, 'faculty': ?1 }")
    List<Dean> findByNamePatternAndFaculty(String namePattern, String faculty);

    @Query("{ 'faculty': ?0, 'officeLocation': { $regex: ?1, $options: 'i' } }")
    List<Dean> findByFacultyAndOfficeLocationPattern(String faculty, String officeLocationPattern);

    // Verificación de existencia
    boolean existsByFaculty(String faculty);
    boolean existsByEmail(String email);
    boolean existsByFacultyAndOfficeLocation(String faculty, String officeLocation);

    // Búsqueda por múltiples facultades
    List<Dean> findByFacultyIn(List<String> faculties);
}
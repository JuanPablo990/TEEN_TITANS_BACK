package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Dean;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para manejar las operaciones CRUD y consultas personalizadas
 * sobre la colección de decanos (Dean) en MongoDB.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Repository
public interface DeanRepository extends MongoRepository<Dean, String> {

    /**
     * Busca un decano por la facultad que dirige.
     * @param faculty Facultad del decano.
     * @return El decano encontrado o null si no existe.
     */
    Optional<Dean> findByFaculty(String faculty);

    /**
     * Busca decanos por ubicación de oficina.
     * @param officeLocation Ubicación de la oficina.
     * @return Lista de decanos en la ubicación especificada.
     */
    List<Dean> findByOfficeLocation(String officeLocation);

    /**
     * Verifica si existe un decano para la facultad especificada.
     * @param faculty Facultad a verificar.
     * @return true si existe un decano para esa facultad, false en caso contrario.
     */
    boolean existsByFaculty(String faculty);

    /**
     * Busca decanos por estado de activación.
     * @param active Estado de activación.
     * @return Lista de decanos filtrados por estado.
     */
    List<Dean> findByActive(boolean active);
}
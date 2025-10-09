package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Classroom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para manejar las operaciones CRUD y consultas personalizadas
 * sobre la colección de aulas (Classroom) en MongoDB.
 *
 * Este repositorio permite realizar búsquedas basadas en el edificio,
 * el número de sala, la capacidad y el tipo de aula, además de las
 * operaciones CRUD básicas proporcionadas por Spring Data.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Repository
public interface ClassroomRepository extends MongoRepository<Classroom, String> {

    /**
     * Busca todas las aulas ubicadas en un edificio específico.
     *
     * @param building Nombre del edificio donde se encuentran las aulas.
     * @return Lista de aulas localizadas en el edificio indicado.
     */
    List<Classroom> findByBuilding(String building);

    /**
     * Busca un aula por su número dentro del edificio.
     *
     * @param roomNumber Número de la sala o aula.
     * @return Aula correspondiente al número indicado, o null si no existe.
     */
    Classroom findByRoomNumber(String roomNumber);

    /**
     * Obtiene todas las aulas que tienen una capacidad mayor o igual a la indicada.
     *
     * @param capacity Capacidad mínima requerida.
     * @return Lista de aulas que cumplen con la capacidad mínima especificada.
     */
    List<Classroom> findByCapacityGreaterThanEqual(Integer capacity);

    /**
     * Busca todas las aulas que corresponden a un tipo específico.
     *
     * @param roomType Tipo de aula (por ejemplo, laboratorio, auditorio, sala regular).
     * @return Lista de aulas que coinciden con el tipo indicado.
     */
    List<Classroom> findByRoomType(String roomType);
}

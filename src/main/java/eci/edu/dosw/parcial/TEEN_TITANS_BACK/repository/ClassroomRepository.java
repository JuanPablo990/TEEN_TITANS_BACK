package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Classroom;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.RoomType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    // Búsquedas básicas por atributos individuales
    List<Classroom> findByBuilding(String building);
    Optional<Classroom> findByRoomNumber(String roomNumber);
    List<Classroom> findByCapacityGreaterThanEqual(Integer capacity);
    List<Classroom> findByRoomType(RoomType roomType);

    // Búsquedas por combinaciones
    List<Classroom> findByBuildingAndRoomNumber(String building, String roomNumber);
    List<Classroom> findByBuildingAndRoomType(String building, RoomType roomType);
    List<Classroom> findByBuildingAndCapacityGreaterThanEqual(String building, Integer capacity);
    List<Classroom> findByRoomTypeAndCapacityGreaterThanEqual(RoomType roomType, Integer capacity);
    List<Classroom> findByBuildingAndRoomTypeAndCapacityGreaterThanEqual(String building, RoomType roomType, Integer capacity);

    // Búsquedas por rangos de capacidad
    List<Classroom> findByCapacityLessThanEqual(Integer capacity);
    List<Classroom> findByCapacityBetween(Integer minCapacity, Integer maxCapacity);
    List<Classroom> findByCapacityGreaterThan(Integer capacity);
    List<Classroom> findByCapacityLessThan(Integer capacity);

    // Búsquedas con operadores de texto
    List<Classroom> findByBuildingContainingIgnoreCase(String building);
    List<Classroom> findByRoomNumberContaining(String roomNumber);
    List<Classroom> findByBuildingContaining(String building);

    // Búsquedas con ordenamiento
    List<Classroom> findByOrderByBuildingAsc();
    List<Classroom> findByOrderByRoomNumberAsc();
    List<Classroom> findByOrderByCapacityAsc();
    List<Classroom> findByOrderByCapacityDesc();
    List<Classroom> findByBuildingOrderByRoomNumberAsc(String building);
    List<Classroom> findByRoomTypeOrderByCapacityDesc(RoomType roomType);
    List<Classroom> findByBuildingOrderByCapacityDesc(String building);
    List<Classroom> findByCapacityGreaterThanEqualOrderByCapacityAsc(Integer capacity);

    // Consultas de conteo
    long countByBuilding(String building);
    long countByRoomType(RoomType roomType);
    long countByCapacityGreaterThanEqual(Integer capacity);
    long countByCapacityBetween(Integer minCapacity, Integer maxCapacity);
    long countByBuildingAndRoomType(String building, RoomType roomType);
    long countByCapacityLessThan(Integer capacity);

    // Consultas personalizadas con @Query
    @Query("{ 'building': { $regex: ?0, $options: 'i' } }")
    List<Classroom> findByBuildingRegex(String buildingPattern);

    @Query("{ 'roomNumber': { $regex: ?0, $options: 'i' } }")
    List<Classroom> findByRoomNumberRegex(String roomNumberPattern);

    @Query("{ 'capacity': { $gte: ?0, $lte: ?1 } }")
    List<Classroom> findByCapacityRange(Integer minCapacity, Integer maxCapacity);

    @Query("{ 'building': ?0, 'capacity': { $gte: ?1 } }")
    List<Classroom> findByBuildingAndMinimumCapacity(String building, Integer minCapacity);

    @Query("{ 'roomType': ?0, 'capacity': { $gte: ?1, $lte: ?2 } }")
    List<Classroom> findByRoomTypeAndCapacityRange(RoomType roomType, Integer minCapacity, Integer maxCapacity);

    @Query(value = "{ 'building': ?0 }", sort = "{ 'roomNumber': 1 }")
    List<Classroom> findByBuildingSortedByRoomNumber(String building);

    @Query(value = "{ 'roomType': ?0 }", sort = "{ 'capacity': -1 }")
    List<Classroom> findByRoomTypeSortedByCapacityDesc(RoomType roomType);

    @Query(value = "{ 'capacity': { $gte: ?0 } }", sort = "{ 'building': 1, 'roomNumber': 1 }")
    List<Classroom> findByMinimumCapacitySortedByLocation(Integer minCapacity);

    // Consultas para búsqueda avanzada de aulas disponibles
    @Query("""
        {
            'capacity': { $gte: ?0 },
            'roomType': ?1,
            'building': ?2
        }
    """)
    List<Classroom> findSuitableClassrooms(Integer requiredCapacity, RoomType requiredRoomType, String preferredBuilding);

    @Query("""
        {
            'capacity': { $gte: ?0 },
            'roomType': { $in: ?1 }
        }
    """)
    List<Classroom> findSuitableClassroomsByRoomTypes(Integer requiredCapacity, List<RoomType> allowedRoomTypes);

    // Consultas para análisis de utilización de espacios
    @Query("{ 'capacity': { $gte: ?0, $lte: ?1 } }")
    List<Classroom> findClassroomsByCapacityRange(Integer minCapacity, Integer maxCapacity);

    @Query("{ 'building': { $in: ?0 } }")
    List<Classroom> findByBuildingsIn(List<String> buildings);

    // Verificación de existencia
    boolean existsByBuildingAndRoomNumber(String building, String roomNumber);
    boolean existsByRoomNumber(String roomNumber);
    boolean existsByBuilding(String building);
    boolean existsByRoomTypeAndCapacityGreaterThanEqual(RoomType roomType, Integer capacity);
    boolean existsByBuildingAndRoomType(String building, RoomType roomType);

    // Búsqueda por múltiples valores
    List<Classroom> findByBuildingIn(List<String> buildings);
    List<Classroom> findByRoomTypeIn(List<RoomType> roomTypes);
    List<Classroom> findByCapacityIn(List<Integer> capacities);
    List<Classroom> findByBuildingInAndRoomTypeIn(List<String> buildings, List<RoomType> roomTypes);

    // Búsqueda de aulas únicas o específicas
    Optional<Classroom> findByBuildingAndRoomNumberAndRoomType(String building, String roomNumber, RoomType roomType);

    // Búsqueda de aulas con capacidad exacta
    List<Classroom> findByCapacity(Integer capacity);

    // Búsqueda de aulas por tipo y edificio con ordenamiento
    List<Classroom> findByBuildingAndRoomTypeOrderByCapacityDesc(String building, RoomType roomType);

    // Búsqueda de aulas más grandes por edificio
    @Query(value = "{ 'building': ?0 }", sort = "{ 'capacity': -1 }")
    List<Classroom> findLargestClassroomsByBuilding(String building);

    // Búsqueda de aulas más pequeñas por tipo
    @Query(value = "{ 'roomType': ?0 }", sort = "{ 'capacity': 1 }")
    List<Classroom> findSmallestClassroomsByType(RoomType roomType);

    // Búsqueda de aulas para eventos especiales (capacidad muy grande)
    @Query("{ 'capacity': { $gte: ?0 } }")
    List<Classroom> findLargeVenues(Integer minCapacityForEvent);

    // Búsqueda de aulas para grupos pequeños
    @Query("{ 'capacity': { $lte: ?0 } }")
    List<Classroom> findSmallClassrooms(Integer maxCapacityForSmallGroup);
}
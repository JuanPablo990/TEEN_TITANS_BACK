package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para manejar las operaciones CRUD y consultas personalizadas
 * sobre la colección de grupos (Group) en MongoDB.
 *
 * Este repositorio permite realizar búsquedas basadas en criterios específicos
 * como la sección, el curso, el profesor y el aula asignada, además de las
 * operaciones CRUD básicas proporcionadas por Spring Data.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Repository
public interface GroupRepository extends MongoRepository<Group, String> {

    // Búsquedas básicas por atributos individuales
    List<Group> findBySection(String section);
    List<Group> findByCourse_CourseCode(String courseCode);
    List<Group> findByProfessor_Id(String professorId);
    List<Group> findByClassroom_ClassroomId(String classroomId);

    // Búsquedas por atributos anidados adicionales
    List<Group> findByCourse_Name(String courseName);
    List<Group> findByProfessor_Name(String professorName);
    List<Group> findByClassroom_Building(String building);
    List<Group> findByClassroom_RoomNumber(String roomNumber);
    List<Group> findBySchedule_ScheduleId(String scheduleId);

    // Búsquedas combinadas
    List<Group> findBySectionAndCourse_CourseCode(String section, String courseCode);
    List<Group> findByCourse_CourseCodeAndProfessor_Id(String courseCode, String professorId);
    List<Group> findByProfessor_IdAndClassroom_ClassroomId(String professorId, String classroomId);
    List<Group> findByCourse_CourseCodeAndClassroom_ClassroomId(String courseCode, String classroomId);
    List<Group> findBySectionAndCourse_CourseCodeAndProfessor_Id(String section, String courseCode, String professorId);
    List<Group> findByCourse_CourseCodeAndProfessor_IdAndClassroom_ClassroomId(String courseCode, String professorId, String classroomId);

    // Búsquedas con operadores de comparación para aulas
    List<Group> findByClassroom_CapacityGreaterThanEqual(Integer capacity);
    List<Group> findByClassroom_CapacityLessThanEqual(Integer capacity);
    List<Group> findByClassroom_CapacityBetween(Integer minCapacity, Integer maxCapacity);

    // Búsquedas por tipo de aula
    List<Group> findByClassroom_RoomType(String roomType);

    // Búsquedas con ordenamiento
    List<Group> findByOrderBySectionAsc();
    List<Group> findByCourse_CourseCodeOrderBySectionAsc(String courseCode);
    List<Group> findByProfessor_IdOrderBySectionAsc(String professorId);
    List<Group> findByClassroom_ClassroomIdOrderBySectionAsc(String classroomId);
    List<Group> findByOrderByClassroom_BuildingAsc();
    List<Group> findByOrderByClassroom_RoomNumberAsc();

    // Consultas de conteo
    long countBySection(String section);
    long countByCourse_CourseCode(String courseCode);
    long countByProfessor_Id(String professorId);
    long countByClassroom_ClassroomId(String classroomId);
    long countByCourse_CourseCodeAndSection(String courseCode, String section);
    long countByProfessor_IdAndCourse_CourseCode(String professorId, String courseCode);
    long countByClassroom_CapacityGreaterThanEqual(Integer capacity);

    // Consultas personalizadas con @Query
    @Query("{ 'section': { $regex: ?0, $options: 'i' } }")
    List<Group> findBySectionRegex(String sectionPattern);

    @Query("{ 'course.name': { $regex: ?0, $options: 'i' } }")
    List<Group> findByCourseNameRegex(String courseNamePattern);

    @Query("{ 'professor.name': { $regex: ?0, $options: 'i' } }")
    List<Group> findByProfessorNameRegex(String professorNamePattern);

    @Query("{ 'classroom.building': { $regex: ?0, $options: 'i' } }")
    List<Group> findByBuildingRegex(String buildingPattern);

    @Query("{ 'course.courseCode': ?0, 'classroom.capacity': { $gte: ?1 } }")
    List<Group> findByCourseAndMinimumCapacity(String courseCode, Integer minCapacity);

    @Query("{ 'professor.id': ?0, 'course.courseCode': ?1 }")
    List<Group> findByProfessorAndCourse(String professorId, String courseCode);

    @Query("{ 'classroom.building': ?0, 'classroom.roomType': ?1 }")
    List<Group> findByBuildingAndRoomType(String building, String roomType);

    @Query(value = "{ 'course.courseCode': ?0 }", sort = "{ 'section': 1 }")
    List<Group> findByCourseCodeSortedBySection(String courseCode);

    @Query(value = "{ 'professor.id': ?0 }", sort = "{ 'course.name': 1 }")
    List<Group> findByProfessorIdSortedByCourseName(String professorId);

    // Consultas para grupos con horarios específicos - CORREGIDAS
    @Query("{ 'schedule.dayOfWeek': ?0, 'schedule.startHour': ?1 }")
    List<Group> findByDayAndStartHour(String dayOfWeek, String startHour);

    @Query("{ 'schedule.dayOfWeek': ?0, 'schedule.period': ?1 }")
    List<Group> findByDayAndPeriod(String dayOfWeek, String period);

    @Query("{ 'schedule.period': ?0 }")
    List<Group> findByPeriod(String period);

    // Consultas para encontrar grupos disponibles (sin conflictos)
    @Query("""
        {
            'classroom.classroomId': ?0,
            'schedule.dayOfWeek': ?1,
            'schedule.startHour': ?2,
            'schedule.endHour': ?3,
            'schedule.period': ?4
        }
    """)
    Optional<Group> findGroupByClassroomAndSchedule(String classroomId, String dayOfWeek, String startHour, String endHour, String period);

    // Verificación de existencia - CORREGIDAS
    boolean existsBySectionAndCourse_CourseCode(String section, String courseCode);
    boolean existsByProfessor_IdAndCourse_CourseCode(String professorId, String courseCode);
    boolean existsByClassroom_ClassroomIdAndSchedule_ScheduleId(String classroomId, String scheduleId);

    // CORREGIDO: Usar Schedule_Period en lugar de Period directo
    boolean existsByCourse_CourseCodeAndSectionAndSchedule_Period(String courseCode, String section, String period);

    // Búsqueda por múltiples valores
    List<Group> findBySectionIn(List<String> sections);
    List<Group> findByCourse_CourseCodeIn(List<String> courseCodes);
    List<Group> findByProfessor_IdIn(List<String> professorIds);
    List<Group> findByClassroom_ClassroomIdIn(List<String> classroomIds);
    List<Group> findByClassroom_BuildingIn(List<String> buildings);
    List<Group> findByClassroom_RoomTypeIn(List<String> roomTypes);

    // Búsqueda de grupos por profesor y período - CORREGIDAS
    @Query("{ 'professor.id': ?0, 'schedule.period': ?1 }")
    List<Group> findByProfessorAndPeriod(String professorId, String period);

    // Búsqueda de grupos por curso y período - CORREGIDAS
    @Query("{ 'course.courseCode': ?0, 'schedule.period': ?1 }")
    List<Group> findByCourseAndPeriod(String courseCode, String period);

    // Búsqueda de grupos disponibles en un aula específica en un horario específico
    @Query("""
        {
            'classroom.classroomId': ?0,
            'schedule.period': ?1,
            'schedule.dayOfWeek': ?2,
            $or: [
                {
                    $and: [
                        { 'schedule.startHour': { $lte: ?3 } },
                        { 'schedule.endHour': { $gt: ?3 } }
                    ]
                },
                {
                    $and: [
                        { 'schedule.startHour': { $lt: ?4 } },
                        { 'schedule.endHour': { $gte: ?4 } }
                    ]
                },
                {
                    $and: [
                        { 'schedule.startHour': { $gte: ?3 } },
                        { 'schedule.endHour': { $lte: ?4 } }
                    ]
                }
            ]
        }
    """)
    List<Group> findGroupsOccupyingClassroom(String classroomId, String period, String dayOfWeek, String startHour, String endHour);
}
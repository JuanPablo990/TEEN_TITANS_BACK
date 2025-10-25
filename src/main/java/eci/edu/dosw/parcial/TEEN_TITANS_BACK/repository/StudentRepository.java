package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la gestión de estudiantes en la base de datos MongoDB.
 * Proporciona métodos para buscar estudiantes por diferentes criterios.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Repository
public interface StudentRepository extends MongoRepository<Student, String> {

    // Búsquedas por atributos específicos de Student
    List<Student> findByAcademicProgram(String academicProgram);
    List<Student> findBySemester(Integer semester);
    List<Student> findByGradeAverage(Double gradeAverage);

    // Búsquedas por rangos
    List<Student> findBySemesterGreaterThan(Integer semester);
    List<Student> findBySemesterLessThan(Integer semester);
    List<Student> findBySemesterBetween(Integer startSemester, Integer endSemester);
    List<Student> findByGradeAverageGreaterThan(Double gradeAverage);
    List<Student> findByGradeAverageLessThan(Double gradeAverage);
    List<Student> findByGradeAverageBetween(Double minGrade, Double maxGrade);

    // Búsquedas combinadas con atributos de Student
    List<Student> findByAcademicProgramAndSemester(String academicProgram, Integer semester);
    List<Student> findByAcademicProgramAndGradeAverageGreaterThan(String academicProgram, Double gradeAverage);
    List<Student> findBySemesterAndGradeAverageGreaterThan(Integer semester, Double gradeAverage);

    // Búsquedas por atributos heredados de User
    List<Student> findByName(String name);
    List<Student> findByEmail(String email);
    List<Student> findByNameContainingIgnoreCase(String name);
    List<Student> findByEmailContainingIgnoreCase(String email);
    List<Student> findByActive(boolean active);

    // Búsquedas combinadas con atributos heredados
    List<Student> findByAcademicProgramAndActive(String academicProgram, boolean active);
    List<Student> findBySemesterAndActive(Integer semester, boolean active);
    List<Student> findByNameAndAcademicProgram(String name, String academicProgram);
    List<Student> findByNameAndSemester(String name, Integer semester);

    // Búsquedas con ordenamiento
    List<Student> findByOrderByGradeAverageDesc();
    List<Student> findByOrderBySemesterAsc();
    List<Student> findByAcademicProgramOrderByGradeAverageDesc(String academicProgram);
    List<Student> findByAcademicProgramOrderByNameAsc(String academicProgram);

    // Consultas de conteo
    long countByAcademicProgram(String academicProgram);
    long countBySemester(Integer semester);
    long countByAcademicProgramAndSemester(String academicProgram, Integer semester);
    long countByGradeAverageGreaterThan(Double gradeAverage);

    // Consultas personalizadas con @Query
    @Query("{ 'academicProgram': { $regex: ?0, $options: 'i' } }")
    List<Student> findByAcademicProgramRegex(String academicProgramPattern);

    @Query("{ 'semester': { $gte: ?0, $lte: ?1 } }")
    List<Student> findBySemesterRange(Integer minSemester, Integer maxSemester);

    @Query("{ 'gradeAverage': { $gte: ?0 } }")
    List<Student> findTopStudents(Double minGradeAverage);

    @Query("{ 'academicProgram': ?0, 'semester': ?1, 'gradeAverage': { $gte: ?2 } }")
    List<Student> findHighAchieversByProgramAndSemester(String academicProgram, Integer semester, Double minGradeAverage);

    // Verificación de existencia
    boolean existsByAcademicProgramAndSemester(String academicProgram, Integer semester);
    boolean existsByEmail(String email);
}
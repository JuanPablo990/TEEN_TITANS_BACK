package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.StudentAcademicProgress;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para manejar las operaciones CRUD y consultas personalizadas
 * sobre la colección de progreso académico de los estudiantes (StudentAcademicProgress)
 * en MongoDB.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Repository
public interface StudentAcademicProgressRepository extends MongoRepository<StudentAcademicProgress, String> {

    List<StudentAcademicProgress> findByAcademicProgram(String academicProgram);

    List<StudentAcademicProgress> findByFaculty(String faculty);

    List<StudentAcademicProgress> findByCurrentSemester(Integer currentSemester);

    List<StudentAcademicProgress> findByCurriculumType(String curriculumType);

    Optional<StudentAcademicProgress> findByStudentId(String studentId);

    List<StudentAcademicProgress> findByCumulativeGPAGreaterThanEqual(Double cumulativeGPA);

    List<StudentAcademicProgress> findByCumulativeGPALessThanEqual(Double cumulativeGPA);

    List<StudentAcademicProgress> findByCumulativeGPABetween(Double minGPA, Double maxGPA);

    List<StudentAcademicProgress> findByCurrentSemesterBetween(Integer minSemester, Integer maxSemester);

    List<StudentAcademicProgress> findByCompletedCreditsGreaterThanEqual(Integer completedCredits);

    List<StudentAcademicProgress> findByCompletedCreditsLessThanEqual(Integer completedCredits);

    List<StudentAcademicProgress> findByAcademicProgramAndFaculty(String academicProgram, String faculty);

    List<StudentAcademicProgress> findByAcademicProgramAndCurrentSemester(String academicProgram, Integer currentSemester);

    List<StudentAcademicProgress> findByFacultyAndCurrentSemester(String faculty, Integer currentSemester);

    List<StudentAcademicProgress> findByAcademicProgramAndCumulativeGPAGreaterThanEqual(String academicProgram, Double cumulativeGPA);

    List<StudentAcademicProgress> findByFacultyAndCumulativeGPAGreaterThanEqual(String faculty, Double cumulativeGPA);

    List<StudentAcademicProgress> findByAcademicProgramOrderByCumulativeGPADesc(String academicProgram);

    List<StudentAcademicProgress> findByFacultyOrderByCumulativeGPADesc(String faculty);

    List<StudentAcademicProgress> findByOrderByCumulativeGPADesc();

    List<StudentAcademicProgress> findByAcademicProgramOrderByCurrentSemesterAsc(String academicProgram);

    List<StudentAcademicProgress> findByOrderByCompletedCreditsDesc();

    long countByAcademicProgram(String academicProgram);

    long countByFaculty(String faculty);

    long countByCurrentSemester(Integer currentSemester);

    long countByAcademicProgramAndCurrentSemester(String academicProgram, Integer currentSemester);

    long countByCumulativeGPAGreaterThanEqual(Double cumulativeGPA);

    long countByCompletedCreditsGreaterThanEqual(Integer completedCredits);

    @Query("{ 'academicProgram': { $regex: ?0, $options: 'i' } }")
    List<StudentAcademicProgress> findByAcademicProgramRegex(String academicProgramPattern);

    @Query("{ 'faculty': { $regex: ?0, $options: 'i' } }")
    List<StudentAcademicProgress> findByFacultyRegex(String facultyPattern);

    @Query("{ 'cumulativeGPA': { $gte: ?0, $lte: ?1 } }")
    List<StudentAcademicProgress> findByCumulativeGPARange(Double minGPA, Double maxGPA);

    @Query("{ 'academicProgram': ?0, 'cumulativeGPA': { $gte: ?1 } }")
    List<StudentAcademicProgress> findHighAchieversByProgram(String academicProgram, Double minGPA);

    @Query("{ 'completedCredits': { $gte: ?0 } }")
    List<StudentAcademicProgress> findByMinimumCompletedCredits(Integer minCredits);

    @Query("{ 'academicProgram': ?0, 'currentSemester': ?1, 'cumulativeGPA': { $gte: ?2 } }")
    List<StudentAcademicProgress> findTopStudentsByProgramAndSemester(String academicProgram, Integer semester, Double minGPA);

    @Query(value = "{ 'academicProgram': ?0 }", sort = "{ 'cumulativeGPA': -1 }")
    List<StudentAcademicProgress> findByAcademicProgramSortedByGPA(String academicProgram);

    boolean existsByStudentId(String studentId);

    boolean existsByAcademicProgramAndStudentId(String academicProgram, String studentId);

    boolean existsByFacultyAndCurrentSemester(String faculty, Integer currentSemester);

    List<StudentAcademicProgress> findByAcademicProgramIn(List<String> academicPrograms);

    List<StudentAcademicProgress> findByFacultyIn(List<String> faculties);

    List<StudentAcademicProgress> findByCurrentSemesterIn(List<Integer> semesters);

    Optional<StudentAcademicProgress> findFirstByStudentIdOrderByCumulativeGPADesc(String studentId);
}
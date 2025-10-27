package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.CourseStatusDetail;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.CourseStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para manejar las operaciones CRUD y consultas personalizadas
 * sobre la colección de detalles de estado de curso (CourseStatusDetail) en MongoDB.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Repository
public interface CourseStatusDetailRepository extends MongoRepository<CourseStatusDetail, String> {

    List<CourseStatusDetail> findBySemester(String semester);

    List<CourseStatusDetail> findByIsApproved(Boolean isApproved);

    List<CourseStatusDetail> findByStatus(CourseStatus status);

    List<CourseStatusDetail> findByGradeGreaterThanEqual(Double grade);

    List<CourseStatusDetail> findByCompletionDateBefore(Date completionDate);

    List<CourseStatusDetail> findByEnrollmentDateAfter(Date enrollmentDate);

    List<CourseStatusDetail> findByCourse_CourseCode(String courseCode);

    List<CourseStatusDetail> findByProfessor_Id(String professorId);

    List<CourseStatusDetail> findByGroup_GroupId(String groupId);

    List<CourseStatusDetail> findByStudentId(String studentId);

    List<CourseStatusDetail> findByGradeBetween(Double minGrade, Double maxGrade);

    List<CourseStatusDetail> findByGradeLessThanEqual(Double grade);

    List<CourseStatusDetail> findByEnrollmentDateBetween(Date startDate, Date endDate);

    List<CourseStatusDetail> findByCompletionDateBetween(Date startDate, Date endDate);

    List<CourseStatusDetail> findByCreditsEarnedGreaterThanEqual(Integer creditsEarned);

    List<CourseStatusDetail> findByCreditsEarnedBetween(Integer minCredits, Integer maxCredits);

    List<CourseStatusDetail> findByStudentIdAndSemester(String studentId, String semester);

    List<CourseStatusDetail> findByStudentIdAndStatus(String studentId, CourseStatus status);

    List<CourseStatusDetail> findByStudentIdAndIsApproved(String studentId, Boolean isApproved);

    List<CourseStatusDetail> findByCourse_CourseCodeAndSemester(String courseCode, String semester);

    List<CourseStatusDetail> findByCourse_CourseCodeAndStatus(String courseCode, CourseStatus status);

    List<CourseStatusDetail> findByProfessor_IdAndSemester(String professorId, String semester);

    List<CourseStatusDetail> findBySemesterAndStatus(String semester, CourseStatus status);

    List<CourseStatusDetail> findBySemesterAndIsApproved(String semester, Boolean isApproved);

    List<CourseStatusDetail> findByStudentIdAndCourse_CourseCode(String studentId, String courseCode);

    List<CourseStatusDetail> findByStudentIdAndSemesterAndStatus(String studentId, String semester, CourseStatus status);

    List<CourseStatusDetail> findByStudentIdOrderBySemesterDesc(String studentId);

    List<CourseStatusDetail> findByStudentIdOrderByGradeDesc(String studentId);

    List<CourseStatusDetail> findByStudentIdOrderByEnrollmentDateDesc(String studentId);

    List<CourseStatusDetail> findByCourse_CourseCodeOrderByGradeDesc(String courseCode);

    List<CourseStatusDetail> findByProfessor_IdOrderBySemesterDesc(String professorId);

    List<CourseStatusDetail> findBySemesterOrderByGradeDesc(String semester);

    List<CourseStatusDetail> findByOrderByGradeDesc();

    long countByStudentId(String studentId);

    long countByStudentIdAndStatus(String studentId, CourseStatus status);

    long countByStudentIdAndIsApproved(String studentId, Boolean isApproved);

    long countByCourse_CourseCode(String courseCode);

    long countByCourse_CourseCodeAndStatus(String courseCode, CourseStatus status);

    long countByProfessor_Id(String professorId);

    long countBySemester(String semester);

    long countBySemesterAndStatus(String semester, CourseStatus status);

    long countByGradeGreaterThanEqual(Double grade);

    long countByIsApproved(Boolean isApproved);

    @Query(value = "{ 'studentId': ?0, 'isApproved': true }", fields = "{ 'grade': 1 }")
    List<CourseStatusDetail> findApprovedGradesByStudentId(String studentId);

    @Query(value = "{ 'studentId': ?0, 'isApproved': true }", count = true)
    long countApprovedCoursesByStudentId(String studentId);

    @Query(value = "{ 'studentId': ?0 }", count = true)
    long countTotalCoursesByStudentId(String studentId);

    @Query("{ 'semester': { $regex: ?0, $options: 'i' } }")
    List<CourseStatusDetail> findBySemesterRegex(String semesterPattern);

    @Query("{ 'studentId': ?0, 'grade': { $gte: ?1 } }")
    List<CourseStatusDetail> findByStudentIdAndMinimumGrade(String studentId, Double minGrade);

    @Query("{ 'studentId': ?0, 'semester': ?1, 'grade': { $gte: ?2 } }")
    List<CourseStatusDetail> findByStudentIdAndSemesterAndMinimumGrade(String studentId, String semester, Double minGrade);

    @Query("{ 'course.courseCode': ?0, 'grade': { $gte: ?1 } }")
    List<CourseStatusDetail> findByCourseAndMinimumGrade(String courseCode, Double minGrade);

    @Query("{ 'studentId': ?0, 'completionDate': { $gte: ?1, $lte: ?2 } }")
    List<CourseStatusDetail> findByStudentIdAndCompletionDateBetween(String studentId, Date startDate, Date endDate);

    @Query("{ 'studentId': ?0, 'status': { $in: ?1 } }")
    List<CourseStatusDetail> findByStudentIdAndStatusIn(String studentId, List<CourseStatus> statuses);

    @Query(value = "{ 'studentId': ?0, 'isApproved': true }", sort = "{ 'grade': -1 }")
    List<CourseStatusDetail> findTopApprovedCoursesByStudentId(String studentId);

    @Query(value = "{ 'course.courseCode': ?0 }", sort = "{ 'grade': -1 }")
    List<CourseStatusDetail> findTopGradesByCourse(String courseCode);

    @Query("{ 'studentId': ?0, 'creditsEarned': { $gt: 0 } }")
    List<CourseStatusDetail> findCoursesWithCreditsByStudentId(String studentId);

    @Query("{ 'studentId': ?0, 'status': 'FAILED' }")
    List<CourseStatusDetail> findFailedCoursesByStudentId(String studentId);

    @Query("{ 'studentId': ?0, 'status': 'WITHDRAWN' }")
    List<CourseStatusDetail> findWithdrawnCoursesByStudentId(String studentId);

    @Query("{ 'studentId': ?0, 'status': 'INCOMPLETE' }")
    List<CourseStatusDetail> findIncompleteCoursesByStudentId(String studentId);

    boolean existsByStudentIdAndCourse_CourseCode(String studentId, String courseCode);

    boolean existsByStudentIdAndCourse_CourseCodeAndStatus(String studentId, String courseCode, CourseStatus status);

    boolean existsByStudentIdAndSemester(String studentId, String semester);

    boolean existsByCourse_CourseCodeAndProfessor_Id(String courseCode, String professorId);

    List<CourseStatusDetail> findByStudentIdIn(List<String> studentIds);

    List<CourseStatusDetail> findByCourse_CourseCodeIn(List<String> courseCodes);

    List<CourseStatusDetail> findByProfessor_IdIn(List<String> professorIds);

    List<CourseStatusDetail> findBySemesterIn(List<String> semesters);

    List<CourseStatusDetail> findByStatusIn(List<CourseStatus> statuses);

    @Query("{ 'studentId': ?0, 'status': { $in: ['ENROLLED', 'IN_PROGRESS'] } }")
    List<CourseStatusDetail> findCurrentCoursesByStudentId(String studentId);

    @Query("{ 'studentId': ?0, 'isApproved': true, 'creditsEarned': { $gt: 0 } }")
    List<CourseStatusDetail> findApprovedCoursesWithCreditsByStudentId(String studentId);

    Optional<CourseStatusDetail> findByStudentIdAndCourse_CourseCodeAndSemester(String studentId, String courseCode, String semester);
}
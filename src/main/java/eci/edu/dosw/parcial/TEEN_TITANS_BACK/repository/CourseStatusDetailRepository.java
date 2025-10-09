package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.CourseStatusDetail;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Repositorio para manejar las operaciones CRUD y consultas personalizadas
 * sobre la colección de detalles de estado de curso (CourseStatusDetail) en MongoDB.
 *
 * Este repositorio permite realizar búsquedas basadas en semestre, estado del curso,
 * aprobación y fechas, además de las operaciones CRUD básicas proporcionadas por
 * Spring Data.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Repository
public interface CourseStatusDetailRepository extends MongoRepository<CourseStatusDetail, String> {

    /**
     * Busca todos los registros asociados a un semestre específico.
     *
     * @param semester Semestre en el que se cursó la materia.
     * @return Lista de detalles de curso asociados al semestre indicado.
     */
    List<CourseStatusDetail> findBySemester(String semester);

    /**
     * Busca todos los cursos que fueron aprobados o no aprobados.
     *
     * @param isApproved Indica si el curso fue aprobado (true) o no (false).
     * @return Lista de detalles de curso filtrados por estado de aprobación.
     */
    List<CourseStatusDetail> findByIsApproved(Boolean isApproved);

    /**
     * Busca todos los registros donde la calificación obtenida es igual o superior a un valor dado.
     *
     * @param grade Calificación mínima.
     * @return Lista de detalles de curso con nota igual o superior al valor indicado.
     */
    List<CourseStatusDetail> findByGradeGreaterThanEqual(Double grade);

    /**
     * Busca todos los registros de cursos completados antes de una fecha específica.
     *
     * @param completionDate Fecha límite superior.
     * @return Lista de cursos completados antes de la fecha indicada.
     */
    List<CourseStatusDetail> findByCompletionDateBefore(Date completionDate);

    /**
     * Busca todos los registros de cursos matriculados después de una fecha específica.
     *
     * @param enrollmentDate Fecha límite inferior.
     * @return Lista de cursos matriculados después de la fecha indicada.
     */
    List<CourseStatusDetail> findByEnrollmentDateAfter(Date enrollmentDate);
}

package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.StudentAcademicProgress;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para manejar las operaciones CRUD y consultas personalizadas
 * sobre la colección de progreso académico de los estudiantes (StudentAcademicProgress)
 * en MongoDB.
 *
 * Este repositorio permite realizar búsquedas basadas en el programa académico,
 * la facultad, el semestre y el promedio acumulado, además de las operaciones
 * CRUD básicas proporcionadas por Spring Data.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Repository
public interface StudentAcademicProgressRepository extends MongoRepository<StudentAcademicProgress, String> {

    /**
     * Busca todos los registros de progreso académico pertenecientes a un programa específico.
     *
     * @param academicProgram Nombre del programa académico.
     * @return Lista de progresos académicos asociados al programa indicado.
     */
    List<StudentAcademicProgress> findByAcademicProgram(String academicProgram);

    /**
     * Busca todos los registros de progreso académico asociados a una facultad determinada.
     *
     * @param faculty Nombre de la facultad.
     * @return Lista de progresos académicos pertenecientes a la facultad indicada.
     */
    List<StudentAcademicProgress> findByFaculty(String faculty);

    /**
     * Busca todos los progresos académicos en un semestre actual específico.
     *
     * @param currentSemester Número del semestre actual.
     * @return Lista de progresos académicos de estudiantes en el semestre indicado.
     */
    List<StudentAcademicProgress> findByCurrentSemester(Integer currentSemester);

    /**
     * Busca todos los registros cuyo promedio acumulado sea igual o superior al valor indicado.
     *
     * @param cumulativeGPA Valor mínimo del promedio acumulado.
     * @return Lista de progresos académicos con GPA igual o superior al valor indicado.
     */
    List<StudentAcademicProgress> findByCumulativeGPAGreaterThanEqual(Double cumulativeGPA);
}
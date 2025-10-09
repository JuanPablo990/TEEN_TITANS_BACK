package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para manejar las operaciones CRUD y consultas personalizadas
 * sobre la colección de estudiantes (Student) en MongoDB.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Repository
public interface StudentRepository extends MongoRepository<Student, String> {

    /**
     * Busca estudiantes por programa académico.
     * @param academicProgram Programa académico del estudiante.
     * @return Lista de estudiantes del programa académico especificado.
     */
    List<Student> findByAcademicProgram(String academicProgram);

    /**
     * Busca estudiantes por semestre actual.
     * @param semester Semestre del estudiante.
     * @return Lista de estudiantes del semestre especificado.
     */
    List<Student> findBySemester(Integer semester);

    /**
     * Busca estudiantes por programa académico y semestre.
     * @param academicProgram Programa académico del estudiante.
     * @param semester Semestre del estudiante.
     * @return Lista de estudiantes que cumplen con ambos criterios.
     */
    List<Student> findByAcademicProgramAndSemester(String academicProgram, Integer semester);

    /**
     * Busca estudiantes con promedio mayor o igual al especificado.
     * @param gradeAverage Promedio mínimo.
     * @return Lista de estudiantes con promedio mayor o igual al especificado.
     */
    List<Student> findByGradeAverageGreaterThanEqual(Double gradeAverage);

    /**
     * Busca estudiantes con promedio menor o igual al especificado.
     * @param gradeAverage Promedio máximo.
     * @return Lista de estudiantes con promedio menor o igual al especificado.
     */
    List<Student> findByGradeAverageLessThanEqual(Double gradeAverage);

    /**
     * Busca estudiantes por programa académico y estado de activación.
     * @param academicProgram Programa académico del estudiante.
     * @param active Estado de activación.
     * @return Lista de estudiantes que cumplen con ambos criterios.
     */
    List<Student> findByAcademicProgramAndActive(String academicProgram, boolean active);
}
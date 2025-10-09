package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Professor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para manejar las operaciones CRUD y consultas personalizadas
 * sobre la colección de profesores (Professor) en MongoDB.
 *
 * Este repositorio permite realizar búsquedas basadas en el departamento,
 * la titularidad y las áreas de especialización, además de las operaciones
 * CRUD básicas proporcionadas por Spring Data.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Repository
public interface ProfessorRepository extends MongoRepository<Professor, String> {

    /**
     * Busca todos los profesores que pertenecen a un departamento específico.
     *
     * @param department Nombre del departamento académico al que pertenece el profesor.
     * @return Lista de profesores asociados al departamento indicado.
     */
    List<Professor> findByDepartment(String department);

    /**
     * Busca todos los profesores según su estatus de titularidad.
     *
     * @param isTenured Indica si el profesor es titular (true) o no (false).
     * @return Lista de profesores filtrados por su estatus de titularidad.
     */
    List<Professor> findByIsTenured(boolean isTenured);

    /**
     * Busca todos los profesores que tienen experiencia en un área específica.
     *
     * @param areaOfExpertise Nombre del área de especialización a buscar.
     * @return Lista de profesores que poseen la especialización indicada.
     */
    List<Professor> findByAreasOfExpertiseContaining(String areaOfExpertise);
}

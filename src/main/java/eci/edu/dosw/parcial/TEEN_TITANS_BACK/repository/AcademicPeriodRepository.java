package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.AcademicPeriod;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Repositorio para manejar las operaciones CRUD y consultas personalizadas
 * sobre la colección de períodos académicos (AcademicPeriod) en MongoDB.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Repository
public interface AcademicPeriodRepository extends MongoRepository<AcademicPeriod, String> {

    /**
     * Busca un período académico por su nombre exacto.
     * @param name Nombre del período académico.
     * @return El período académico encontrado o null si no existe.
     */
    AcademicPeriod findByName(String name);

    /**
     * Obtiene todos los períodos académicos que están activos o inactivos.
     * @param isActive Estado del período (true = activo, false = inactivo).
     * @return Lista de períodos académicos filtrados por estado.
     */
    List<AcademicPeriod> findByIsActive(boolean isActive);

    /**
     * Busca todos los períodos que inician después de la fecha dada.
     * @param fecha Fecha límite inferior.
     * @return Lista de períodos académicos que comienzan después de la fecha indicada.
     */
    List<AcademicPeriod> findByStartDateAfter(Date fecha);

    /**
     * Busca todos los períodos que terminan antes de la fecha dada.
     * @param fecha Fecha límite superior.
     * @return Lista de períodos académicos que finalizan antes de la fecha indicada.
     */
    List<AcademicPeriod> findByEndDateBefore(Date fecha);

    /**
     * Busca todos los períodos que se desarrollan entre dos fechas dadas.
     * @param inicio Fecha de inicio del rango.
     * @param fin Fecha de fin del rango.
     * @return Lista de períodos académicos que se encuentran dentro del rango.
     */
    List<AcademicPeriod> findByStartDateBetween(Date inicio, Date fin);

    /**
     * Busca los períodos activos dentro de un rango de fechas.
     * @param isActive Estado de los períodos (true = activos).
     * @param inicio Fecha de inicio del rango.
     * @param fin Fecha de fin del rango.
     * @return Lista de períodos activos dentro del rango de fechas.
     */
    List<AcademicPeriod> findByIsActiveAndStartDateBetween(boolean isActive, Date inicio, Date fin);
}

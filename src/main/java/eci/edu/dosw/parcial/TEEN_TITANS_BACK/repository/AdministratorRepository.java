package eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Administrator;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para manejar las operaciones CRUD y consultas personalizadas
 * sobre la colección de administradores (Administrator) en MongoDB.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Repository
public interface AdministratorRepository extends MongoRepository<Administrator, String> {

    /**
     * Busca administradores por departamento.
     * @param department Departamento del administrador.
     * @return Lista de administradores del departamento especificado.
     */
    List<Administrator> findByDepartment(String department);

    /**
     * Busca administradores por departamento y estado de activación.
     * @param department Departamento del administrador.
     * @param active Estado de activación.
     * @return Lista de administradores que cumplen con ambos criterios.
     */
    List<Administrator> findByDepartmentAndActive(String department, boolean active);

    /**
     * Verifica si existe un administrador en el departamento especificado.
     * @param department Departamento a verificar.
     * @return true si existe un administrador en ese departamento, false en caso contrario.
     */
    boolean existsByDepartment(String department);
}
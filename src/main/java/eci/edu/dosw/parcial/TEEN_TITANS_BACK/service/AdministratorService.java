package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Administrator;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.AdministratorRepository;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio para la gestión de administradores del sistema.
 * Proporciona operaciones CRUD y métodos de búsqueda para administradores.
 *
 *  * @author Equipo Teen Titans
 *  * @version 1.0
 *  * @since 2025
 */
@Service
public class AdministratorService {

    @Autowired
    private AdministratorRepository adminRepository;

    /**
     * Crea un nuevo administrador en el sistema.
     *
     * @param admin Administrador a crear
     * @return El administrador creado
     * @throws AppException si el email ya está registrado
     */
    public Administrator createAdministrator(Administrator admin) {
        if (adminRepository.existsByEmail(admin.getEmail())) {
            throw new AppException("El email ya está registrado: " + admin.getEmail());
        }

        admin.setRole(UserRole.ADMINISTRATOR);
        return adminRepository.save(admin);
    }

    /**
     * Obtiene un administrador por su ID.
     *
     * @param id ID del administrador
     * @return El administrador encontrado
     * @throws AppException si no se encuentra el administrador
     */
    public Administrator getAdministratorById(String id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new AppException("Administrador no encontrado con ID: " + id));
    }

    /**
     * Obtiene todos los administradores del sistema.
     *
     * @return Lista de todos los administradores
     */
    public List<Administrator> getAllAdministrators() {
        return adminRepository.findAll();
    }

    /**
     * Actualiza la información de un administrador existente.
     *
     * @param id ID del administrador a actualizar
     * @param admin Nuevos datos del administrador
     * @return El administrador actualizado
     * @throws AppException si no se encuentra el administrador o el email está en uso
     */
    public Administrator updateAdministrator(String id, Administrator admin) {
        Administrator existingAdmin = adminRepository.findById(id)
                .orElseThrow(() -> new AppException("Administrador no encontrado con ID: " + id));

        if (!existingAdmin.getEmail().equals(admin.getEmail())) {
            List<Administrator> adminsWithEmail = adminRepository.findByEmail(admin.getEmail());
            if (!adminsWithEmail.isEmpty()) {
                throw new AppException("El email ya está en uso por otro administrador: " + admin.getEmail());
            }
        }

        admin.setId(id);
        admin.setRole(UserRole.ADMINISTRATOR);
        return adminRepository.save(admin);
    }

    /**
     * Elimina un administrador del sistema.
     *
     * @param id ID del administrador a eliminar
     * @throws AppException si no se encuentra el administrador
     */
    public void deleteAdministrator(String id) {
        if (!adminRepository.existsById(id)) {
            throw new AppException("Administrador no encontrado con ID: " + id);
        }
        adminRepository.deleteById(id);
    }

    /**
     * Busca administradores por departamento.
     *
     * @param department Departamento a buscar
     * @return Lista de administradores del departamento especificado
     */
    public List<Administrator> findByDepartment(String department) {
        return adminRepository.findByDepartment(department);
    }

    /**
     * Busca administradores por departamento (búsqueda parcial sin distinguir mayúsculas/minúsculas).
     *
     * @param department Texto del departamento a buscar
     * @return Lista de administradores que coinciden con el departamento
     */
    public List<Administrator> findByDepartmentContaining(String department) {
        return adminRepository.findByDepartmentContainingIgnoreCase(department);
    }

    /**
     * Busca administradores por nombre (búsqueda parcial sin distinguir mayúsculas/minúsculas).
     *
     * @param name Texto del nombre a buscar
     * @return Lista de administradores que coinciden con el nombre
     */
    public List<Administrator> findByNameContaining(String name) {
        return adminRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Obtiene administradores activos o inactivos.
     *
     * @return Lista de administradores activos
     */
    public List<Administrator> findActiveAdministrators() {
        return adminRepository.findByActive(true);
    }

    /**
     * Busca administradores por departamento y estado activo.
     *
     * @param department Departamento a buscar
     * @param active Estado activo
     * @return Lista de administradores que coinciden con los criterios
     */
    public List<Administrator> findByDepartmentAndActive(String department, boolean active) {
        return adminRepository.findByDepartmentAndActive(department, active);
    }

    /**
     * Busca administradores por nombre y departamento exactos.
     *
     * @param name Nombre exacto
     * @param department Departamento exacto
     * @return Lista de administradores que coinciden con nombre y departamento
     */
    public List<Administrator> findByNameAndDepartment(String name, String department) {
        return adminRepository.findByNameAndDepartment(name, department);
    }

    /**
     * Cuenta administradores por departamento.
     *
     * @param department Departamento a contar
     * @return Número de administradores en el departamento
     */
    public long countByDepartment(String department) {
        return adminRepository.countByDepartment(department);
    }

    /**
     * Cuenta administradores por departamento y estado activo.
     *
     * @param department Departamento a contar
     * @param active Estado activo
     * @return Número de administradores que coinciden con los criterios
     */
    public long countByDepartmentAndActive(String department, boolean active) {
        return adminRepository.countByDepartmentAndActive(department, active);
    }

    /**
     * Busca administradores por múltiples departamentos.
     *
     * @param departments Lista de departamentos
     * @return Lista de administradores de los departamentos especificados
     */
    public List<Administrator> findByDepartments(List<String> departments) {
        return adminRepository.findByDepartmentIn(departments);
    }

    /**
     * Busca administradores por patrón de departamento usando regex.
     *
     * @param departmentPattern Patrón del departamento
     * @return Lista de administradores que coinciden con el patrón
     */
    public List<Administrator> findByDepartmentPattern(String departmentPattern) {
        return adminRepository.findByDepartmentRegex(departmentPattern);
    }
}
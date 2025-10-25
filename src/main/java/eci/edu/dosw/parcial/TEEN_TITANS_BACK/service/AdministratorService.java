package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Administrator;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.AdministratorRepository;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdministratorService {

    @Autowired
    private AdministratorRepository adminRepository;

    /**
     * Crea un nuevo administrador en el sistema.
     *
     * @param admin Administrador a crear
     * @return El administrador creado
     */
    public Administrator createAdministrator(Administrator admin) {
        // VALIDACIÓN: Verificar que el email no exista
        if (adminRepository.existsByEmail(admin.getEmail())) {
            throw new AppException("El email ya está registrado: " + admin.getEmail());
        }

        // Asegurar que el rol sea ADMINISTRATOR
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
     * @throws AppException si no se encuentra el administrador
     */
    public Administrator updateAdministrator(String id, Administrator admin) {
        // Verificar que el administrador existe
        Administrator existingAdmin = adminRepository.findById(id)
                .orElseThrow(() -> new AppException("Administrador no encontrado con ID: " + id));

        // Validar que el email no esté siendo usado por otro administrador
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
        // Verificación más eficiente
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

    // MÉTODOS ADICIONALES ÚTILES que YA TIENES en tu AdministratorRepository:

    public List<Administrator> findByDepartmentContaining(String department) {
        return adminRepository.findByDepartmentContainingIgnoreCase(department);
    }

    public List<Administrator> findByNameContaining(String name) {
        return adminRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Administrator> findActiveAdministrators() {
        return adminRepository.findByActive(true);
    }

    public List<Administrator> findByDepartmentAndActive(String department, boolean active) {
        return adminRepository.findByDepartmentAndActive(department, active);
    }

    public List<Administrator> findByNameAndDepartment(String name, String department) {
        return adminRepository.findByNameAndDepartment(name, department);
    }

    public long countByDepartment(String department) {
        return adminRepository.countByDepartment(department);
    }

    public long countByDepartmentAndActive(String department, boolean active) {
        return adminRepository.countByDepartmentAndActive(department, active);
    }

    /**
     * Busca administradores por múltiples departamentos
     * @param departments Lista de departamentos
     * @return Lista de administradores de los departamentos especificados
     */
    public List<Administrator> findByDepartments(List<String> departments) {
        return adminRepository.findByDepartmentIn(departments);
    }

    /**
     * Busca administradores por patrón de departamento usando regex
     * @param departmentPattern Patrón del departamento
     * @return Lista de administradores que coinciden con el patrón
     */
    public List<Administrator> findByDepartmentPattern(String departmentPattern) {
        return adminRepository.findByDepartmentRegex(departmentPattern);
    }
}
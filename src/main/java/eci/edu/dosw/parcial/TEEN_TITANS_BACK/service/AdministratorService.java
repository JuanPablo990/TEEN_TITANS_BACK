package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Administrator;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.AdministratorRepository;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar las operaciones CRUD y consultas personalizadas
 * de administradores en el sistema.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
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
     */
    public Administrator createAdministrator(Administrator admin) {
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
        Optional<Administrator> admin = adminRepository.findById(id);
        if (admin.isPresent()) {
            return admin.get();
        } else {
            throw new AppException("Administrador no encontrado con ID: " + id);
        }
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
        Optional<Administrator> existingAdmin = adminRepository.findById(id);
        if (existingAdmin.isPresent()) {
            admin.setId(id); // Asegurar que se mantenga el mismo ID
            return adminRepository.save(admin);
        } else {
            throw new AppException("Administrador no encontrado con ID: " + id);
        }
    }

    /**
     * Elimina un administrador del sistema.
     *
     * @param id ID del administrador a eliminar
     * @throws AppException si no se encuentra el administrador
     */
    public void deleteAdministrator(String id) {
        Optional<Administrator> admin = adminRepository.findById(id);
        if (admin.isPresent()) {
            adminRepository.deleteById(id);
        } else {
            throw new AppException("Administrador no encontrado con ID: " + id);
        }
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
}
package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Dean;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.DeanRepository;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeanService {

    @Autowired
    private DeanRepository deanRepository;

    /**
     * Crea un nuevo decano en el sistema.
     *
     * @param dean Decano a crear
     * @return El decano creado
     */
    public Dean createDean(Dean dean) {
        // VALIDACIÓN: Verificar que el email no exista
        if (deanRepository.existsByEmail(dean.getEmail())) {
            throw new AppException("El email ya está registrado: " + dean.getEmail());
        }

        // Asegurar que el rol sea DEAN
        dean.setRole(UserRole.DEAN);
        return deanRepository.save(dean);
    }

    /**
     * Obtiene un decano por su ID.
     *
     * @param id ID del decano
     * @return El decano encontrado
     * @throws AppException si no se encuentra el decano
     */
    public Dean getDeanById(String id) {
        return deanRepository.findById(id)
                .orElseThrow(() -> new AppException("Decano no encontrado con ID: " + id));
    }

    /**
     * Obtiene todos los decanos del sistema.
     *
     * @return Lista de todos los decanos
     */
    public List<Dean> getAllDeans() {
        return deanRepository.findAll();
    }

    /**
     * Actualiza la información de un decano existente.
     *
     * @param id ID del decano a actualizar
     * @param dean Nuevos datos del decano
     * @return El decano actualizado
     * @throws AppException si no se encuentra el decano
     */
    public Dean updateDean(String id, Dean dean) {
        // Verificar que el decano existe
        Dean existingDean = deanRepository.findById(id)
                .orElseThrow(() -> new AppException("Decano no encontrado con ID: " + id));

        // Validar que el email no esté siendo usado por otro decano
        if (!existingDean.getEmail().equals(dean.getEmail())) {
            List<Dean> deansWithEmail = deanRepository.findByEmail(dean.getEmail());
            if (!deansWithEmail.isEmpty()) {
                throw new AppException("El email ya está en uso por otro decano: " + dean.getEmail());
            }
        }

        dean.setId(id);
        dean.setRole(UserRole.DEAN);
        return deanRepository.save(dean);
    }

    /**
     * Elimina un decano del sistema.
     *
     * @param id ID del decano a eliminar
     * @throws AppException si no se encuentra el decano
     */
    public void deleteDean(String id) {
        // Verificación más eficiente
        if (!deanRepository.existsById(id)) {
            throw new AppException("Decano no encontrado con ID: " + id);
        }
        deanRepository.deleteById(id);
    }

    /**
     * Busca decanos por facultad.
     *
     * @param faculty Facultad a buscar
     * @return Lista de decanos de la facultad especificada
     */
    public List<Dean> findByFaculty(String faculty) {
        // CORREGIDO: findByFaculty devuelve List<Dean>, no Optional<Dean>
        return deanRepository.findByFaculty(faculty);
    }

    /**
     * Busca decanos por ubicación de oficina.
     *
     * @param location Ubicación de oficina a buscar
     * @return Lista de decanos en la ubicación especificada
     */
    public List<Dean> findByOfficeLocation(String location) {
        return deanRepository.findByOfficeLocation(location);
    }

    /**
     * Verifica si existe un decano con el ID especificado.
     *
     * @param id ID del decano a verificar
     * @return true si existe, false en caso contrario
     */
    public boolean existsById(String id) {
        return deanRepository.existsById(id);
    }

    /**
     * Obtiene un decano por facultad, lanzando excepción si no se encuentra.
     *
     * @param faculty Facultad a buscar
     * @return El decano de la facultad especificada
     * @throws AppException si no se encuentra el decano para la facultad
     */
    public Dean getDeanByFaculty(String faculty) {
        // CORREGIDO: findByFaculty devuelve List, tomamos el primero si existe
        List<Dean> deans = deanRepository.findByFaculty(faculty);
        if (deans.isEmpty()) {
            throw new AppException("No se encontró decano para la facultad: " + faculty);
        }
        return deans.get(0);
    }

    // MÉTODOS ADICIONALES ÚTILES que YA TIENES en tu DeanRepository:

    public List<Dean> findByFacultyAndOfficeLocation(String faculty, String officeLocation) {
        return deanRepository.findByFacultyAndOfficeLocation(faculty, officeLocation);
    }

    public List<Dean> findByFacultyContaining(String faculty) {
        return deanRepository.findByFacultyContainingIgnoreCase(faculty);
    }

    public List<Dean> findByOfficeLocationContaining(String officeLocation) {
        return deanRepository.findByOfficeLocationContainingIgnoreCase(officeLocation);
    }

    public List<Dean> findByNameContaining(String name) {
        return deanRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Dean> findActiveDeans() {
        return deanRepository.findByActive(true);
    }

    public long countByFaculty(String faculty) {
        return deanRepository.countByFaculty(faculty);
    }

    public long countByOfficeLocation(String officeLocation) {
        return deanRepository.countByOfficeLocation(officeLocation);
    }

    /**
     * Busca decanos por múltiples facultades
     * @param faculties Lista de facultades
     * @return Lista de decanos de las facultades especificadas
     */
    public List<Dean> findByFaculties(List<String> faculties) {
        return deanRepository.findByFacultyIn(faculties);
    }
}
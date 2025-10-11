package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Dean;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.DeanRepository;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar las operaciones CRUD y consultas personalizadas
 * de decanos en el sistema.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
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
        Optional<Dean> dean = deanRepository.findById(id);
        if (dean.isPresent()) {
            return dean.get();
        } else {
            throw new AppException("Decano no encontrado con ID: " + id);
        }
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
        Optional<Dean> existingDean = deanRepository.findById(id);
        if (existingDean.isPresent()) {
            dean.setId(id); // Asegurar que se mantenga el mismo ID
            return deanRepository.save(dean);
        } else {
            throw new AppException("Decano no encontrado con ID: " + id);
        }
    }

    /**
     * Elimina un decano del sistema.
     *
     * @param id ID del decano a eliminar
     * @throws AppException si no se encuentra el decano
     */
    public void deleteDean(String id) {
        Optional<Dean> dean = deanRepository.findById(id);
        if (dean.isPresent()) {
            deanRepository.deleteById(id);
        } else {
            throw new AppException("Decano no encontrado con ID: " + id);
        }
    }

    /**
     * Busca decanos por facultad.
     * Nota: Aunque findByFaculty devuelve Optional, lo convertimos a List
     * para mantener consistencia con la interfaz del servicio.
     *
     * @param faculty Facultad a buscar
     * @return Lista de decanos de la facultad especificada
     */
    public List<Dean> findByFaculty(String faculty) {
        Optional<Dean> dean = deanRepository.findByFaculty(faculty);
        return dean.map(List::of).orElse(List.of());
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
        return deanRepository.findByFaculty(faculty)
                .orElseThrow(() -> new AppException("No se encontró decano para la facultad: " + faculty));
    }
}
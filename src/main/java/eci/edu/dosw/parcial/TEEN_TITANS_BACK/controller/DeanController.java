package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Dean;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.DeanService;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de decanos del sistema.
 * <p>
 * Proporciona endpoints para operaciones CRUD y consultas personalizadas
 * sobre los decanos registrados en el sistema.
 * </p>
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@RestController
@RequestMapping("/api/deans")
@CrossOrigin(origins = "*")
public class DeanController {

    @Autowired
    private DeanService deanService;

    /**
     * Crea un nuevo decano en el sistema.
     *
     * @param dean Decano a crear
     * @return ResponseEntity con el decano creado
     */
    @PostMapping
    public ResponseEntity<?> createDean(@RequestBody Dean dean) {
        try {
            Dean createdDean = deanService.createDean(dean);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDean);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al crear el decano");
        }
    }

    /**
     * Obtiene un decano por su ID.
     *
     * @param id ID del decano
     * @return ResponseEntity con el decano encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getDeanById(@PathVariable String id) {
        try {
            Dean dean = deanService.getDeanById(id);
            return ResponseEntity.ok(dean);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al obtener el decano");
        }
    }

    /**
     * Obtiene todos los decanos del sistema.
     *
     * @return ResponseEntity con la lista de todos los decanos
     */
    @GetMapping
    public ResponseEntity<?> getAllDeans() {
        try {
            List<Dean> deans = deanService.getAllDeans();
            return ResponseEntity.ok(deans);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al obtener los decanos");
        }
    }

    /**
     * Actualiza la información de un decano existente.
     *
     * @param id ID del decano a actualizar
     * @param dean Nuevos datos del decano
     * @return ResponseEntity con el decano actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDean(@PathVariable String id, @RequestBody Dean dean) {
        try {
            Dean updatedDean = deanService.updateDean(id, dean);
            return ResponseEntity.ok(updatedDean);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al actualizar el decano");
        }
    }

    /**
     * Elimina un decano del sistema.
     *
     * @param id ID del decano a eliminar
     * @return ResponseEntity con el resultado de la operación
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDean(@PathVariable String id) {
        try {
            deanService.deleteDean(id);
            return ResponseEntity.ok().body("Decano eliminado exitosamente");
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al eliminar el decano");
        }
    }

    /**
     * Busca decanos por facultad.
     *
     * @param faculty Facultad a buscar
     * @return ResponseEntity con la lista de decanos de la facultad especificada
     */
    @GetMapping("/faculty/{faculty}")
    public ResponseEntity<?> findByFaculty(@PathVariable String faculty) {
        try {
            List<Dean> deans = deanService.findByFaculty(faculty);
            return ResponseEntity.ok(deans);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al buscar decanos por facultad");
        }
    }

    /**
     * Busca decanos por ubicación de oficina.
     *
     * @param location Ubicación de oficina a buscar
     * @return ResponseEntity con la lista de decanos en la ubicación especificada
     */
    @GetMapping("/office/{location}")
    public ResponseEntity<?> findByOfficeLocation(@PathVariable String location) {
        try {
            List<Dean> deans = deanService.findByOfficeLocation(location);
            return ResponseEntity.ok(deans);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al buscar decanos por ubicación de oficina");
        }
    }

    /**
     * Verifica si existe un decano con el ID especificado.
     *
     * @param id ID del decano a verificar
     * @return ResponseEntity con el resultado de la verificación
     */
    @GetMapping("/{id}/exists")
    public ResponseEntity<?> existsById(@PathVariable String id) {
        try {
            boolean exists = deanService.existsById(id);
            return ResponseEntity.ok().body(exists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al verificar la existencia del decano");
        }
    }

    /**
     * Obtiene un decano por facultad.
     *
     * @param faculty Facultad a buscar
     * @return ResponseEntity con el decano de la facultad especificada
     */
    @GetMapping("/faculty/{faculty}/dean")
    public ResponseEntity<?> getDeanByFaculty(@PathVariable String faculty) {
        try {
            Dean dean = deanService.getDeanByFaculty(faculty);
            return ResponseEntity.ok(dean);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al obtener el decano por facultad");
        }
    }

    /**
     * Endpoint de salud para verificar que el controlador está funcionando.
     *
     * @return Mensaje de confirmación
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("DeanController is working properly");
    }
}
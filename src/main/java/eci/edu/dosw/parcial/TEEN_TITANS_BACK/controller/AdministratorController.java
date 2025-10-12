package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Administrator;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.AdministratorService;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de administradores del sistema.
 * <p>
 * Proporciona endpoints para operaciones CRUD y consultas personalizadas
 * sobre los administradores registrados en el sistema.
 * </p>
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@RestController
@RequestMapping("/api/administrators")
@CrossOrigin(origins = "*")
public class AdministratorController {

    @Autowired
    private AdministratorService administratorService;

    /**
     * Crea un nuevo administrador en el sistema.
     *
     * @param administrator Administrador a crear
     * @return ResponseEntity con el administrador creado
     */
    @PostMapping
    public ResponseEntity<?> createAdministrator(@RequestBody Administrator administrator) {
        try {
            Administrator createdAdmin = administratorService.createAdministrator(administrator);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAdmin);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al crear el administrador");
        }
    }

    /**
     * Obtiene un administrador por su ID.
     *
     * @param id ID del administrador
     * @return ResponseEntity con el administrador encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getAdministratorById(@PathVariable String id) {
        try {
            Administrator administrator = administratorService.getAdministratorById(id);
            return ResponseEntity.ok(administrator);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al obtener el administrador");
        }
    }

    /**
     * Obtiene todos los administradores del sistema.
     *
     * @return ResponseEntity con la lista de todos los administradores
     */
    @GetMapping
    public ResponseEntity<?> getAllAdministrators() {
        try {
            List<Administrator> administrators = administratorService.getAllAdministrators();
            return ResponseEntity.ok(administrators);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al obtener los administradores");
        }
    }

    /**
     * Actualiza la información de un administrador existente.
     *
     * @param id ID del administrador a actualizar
     * @param administrator Nuevos datos del administrador
     * @return ResponseEntity con el administrador actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAdministrator(@PathVariable String id, @RequestBody Administrator administrator) {
        try {
            Administrator updatedAdmin = administratorService.updateAdministrator(id, administrator);
            return ResponseEntity.ok(updatedAdmin);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al actualizar el administrador");
        }
    }

    /**
     * Elimina un administrador del sistema.
     *
     * @param id ID del administrador a eliminar
     * @return ResponseEntity con el resultado de la operación
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAdministrator(@PathVariable String id) {
        try {
            administratorService.deleteAdministrator(id);
            return ResponseEntity.ok().body("Administrador eliminado exitosamente");
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al eliminar el administrador");
        }
    }

    /**
     * Busca administradores por departamento.
     *
     * @param department Departamento a buscar
     * @return ResponseEntity con la lista de administradores del departamento especificado
     */
    @GetMapping("/department/{department}")
    public ResponseEntity<?> findByDepartment(@PathVariable String department) {
        try {
            List<Administrator> administrators = administratorService.findByDepartment(department);
            return ResponseEntity.ok(administrators);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al buscar administradores por departamento");
        }
    }

    /**
     * Endpoint de salud para verificar que el controlador está funcionando.
     *
     * @return Mensaje de confirmación
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("AdministratorController is working properly");
    }
}
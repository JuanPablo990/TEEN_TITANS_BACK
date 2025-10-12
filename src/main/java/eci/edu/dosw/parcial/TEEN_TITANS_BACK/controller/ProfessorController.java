package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Professor;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.ProfessorService;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestión de profesores del sistema.
 * <p>
 * Proporciona endpoints para operaciones CRUD y consultas personalizadas
 * sobre los profesores registrados en el sistema.
 * </p>
 *
 * @author Equipo Teen Titans
 * @version 1.0
 */
@RestController
@RequestMapping("/api/professors")
@CrossOrigin(origins = "*")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    /**
     * Crea un nuevo profesor en el sistema.
     *
     * @param professor Profesor a crear
     * @return ResponseEntity con el profesor creado
     */
    @PostMapping
    public ResponseEntity<?> createProfessor(@RequestBody Professor professor) {
        try {
            Professor createdProfessor = professorService.createProfessor(professor);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProfessor);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al crear el profesor");
        }
    }

    /**
     * Obtiene un profesor por su ID.
     *
     * @param id Identificador del profesor
     * @return ResponseEntity con el profesor encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getProfessorById(@PathVariable String id) {
        try {
            Professor professor = professorService.getProfessorById(id);
            return ResponseEntity.ok(professor);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al obtener el profesor");
        }
    }

    /**
     * Obtiene todos los profesores del sistema.
     *
     * @return ResponseEntity con la lista de todos los profesores
     */
    @GetMapping
    public ResponseEntity<?> getAllProfessors() {
        try {
            List<Professor> professors = professorService.getAllProfessors();
            return ResponseEntity.ok(professors);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al obtener los profesores");
        }
    }

    /**
     * Actualiza la información de un profesor.
     *
     * @param id Identificador del profesor a actualizar
     * @param professor Nuevos datos del profesor
     * @return ResponseEntity con el profesor actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfessor(@PathVariable String id, @RequestBody Professor professor) {
        try {
            Professor updatedProfessor = professorService.updateProfessor(id, professor);
            return ResponseEntity.ok(updatedProfessor);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al actualizar el profesor");
        }
    }

    /**
     * Elimina un profesor del sistema.
     *
     * @param id Identificador del profesor a eliminar
     * @return ResponseEntity con el resultado de la operación
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProfessor(@PathVariable String id) {
        try {
            professorService.deleteProfessor(id);
            return ResponseEntity.ok().body("Profesor eliminado exitosamente");
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al eliminar el profesor");
        }
    }

    /**
     * Busca profesores por departamento.
     *
     * @param department Departamento a buscar
     * @return ResponseEntity con la lista de profesores del departamento especificado
     */
    @GetMapping("/department/{department}")
    public ResponseEntity<?> findByDepartment(@PathVariable String department) {
        try {
            List<Professor> professors = professorService.findByDepartment(department);
            return ResponseEntity.ok(professors);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al buscar profesores por departamento");
        }
    }

    /**
     * Busca profesores por titularidad.
     *
     * @param isTenured true para profesores titulares, false para no titulares
     * @return ResponseEntity con la lista de profesores según titularidad
     */
    @GetMapping("/tenured/{isTenured}")
    public ResponseEntity<?> findByTenured(@PathVariable Boolean isTenured) {
        try {
            List<Professor> professors = professorService.findByTenured(isTenured);
            return ResponseEntity.ok(professors);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al buscar profesores por titularidad");
        }
    }

    /**
     * Busca profesores por área de especialización.
     *
     * @param area Área de especialización a buscar
     * @return ResponseEntity con la lista de profesores con la especialización especificada
     */
    @GetMapping("/expertise/{area}")
    public ResponseEntity<?> findByAreaOfExpertise(@PathVariable String area) {
        try {
            List<Professor> professors = professorService.findByAreaOfExpertise(area);
            return ResponseEntity.ok(professors);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al buscar profesores por área de especialización");
        }
    }

    /**
     * Obtiene profesores activos.
     *
     * @return ResponseEntity con la lista de profesores activos
     */
    @GetMapping("/active")
    public ResponseEntity<?> findActiveProfessors() {
        try {
            List<Professor> activeProfessors = professorService.findActiveProfessors();
            return ResponseEntity.ok(activeProfessors);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al obtener los profesores activos");
        }
    }

    /**
     * Obtiene estadísticas básicas de los profesores.
     *
     * @return ResponseEntity con estadísticas de profesores
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getProfessorStats() {
        try {
            List<Professor> allProfessors = professorService.getAllProfessors();
            List<Professor> tenuredProfessors = professorService.findByTenured(true);
            List<Professor> activeProfessors = professorService.findActiveProfessors();

            // Calcular las estadísticas primero
            int totalProfessors = allProfessors.size();
            int tenuredCount = tenuredProfessors.size();
            int activeCount = activeProfessors.size();
            double tenuredPercentage = totalProfessors > 0 ?
                    (double) tenuredCount / totalProfessors * 100 : 0;


            Map<String, Object> stats = new HashMap<>();
            stats.put("totalProfessors", totalProfessors);
            stats.put("tenuredProfessors", tenuredCount);
            stats.put("activeProfessors", activeCount);
            stats.put("tenuredPercentage", Math.round(tenuredPercentage * 100.0) / 100.0);

            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al obtener estadísticas de profesores");
        }
    }

    /**
     * Verifica si existe un profesor con el ID especificado.
     *
     * @param id ID del profesor a verificar
     * @return ResponseEntity con el resultado de la verificación
     */
    @GetMapping("/{id}/exists")
    public ResponseEntity<?> existsById(@PathVariable String id) {
        try {

            professorService.getProfessorById(id);
            return ResponseEntity.ok().body(true);
        } catch (AppException e) {
            return ResponseEntity.ok().body(false);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al verificar la existencia del profesor");
        }
    }

    /**
     * Endpoint de salud para verificar que el controlador está funcionando.
     *
     * @return Mensaje de confirmación
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("ProfessorController is working properly");
    }
}
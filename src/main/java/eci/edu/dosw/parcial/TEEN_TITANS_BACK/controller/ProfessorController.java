package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto.ProfessorDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Professor;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.ProfessorService;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador REST para gestionar las operaciones relacionadas con los profesores.
 * Proporciona endpoints para crear, consultar, actualizar, eliminar y filtrar profesores.
 */
@RestController
@RequestMapping("/api/professors")
@CrossOrigin(origins = "*")
public class ProfessorController {

    private final ProfessorService professorService;

    /**
     * Constructor del controlador con inyección de dependencias.
     *
     * @param professorService servicio de profesores
     */
    @Autowired
    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    /**
     * Crea un nuevo profesor en el sistema.
     *
     * @param professorDTO datos del profesor a crear
     * @return respuesta HTTP con el profesor creado o error
     */
    @PostMapping
    public ResponseEntity<?> createProfessor(@RequestBody ProfessorDTO professorDTO) {
        try {
            Professor professor = convertToEntity(professorDTO);
            Professor createdProfessor = professorService.createProfessor(professor);
            ProfessorDTO responseDTO = convertToDTO(createdProfessor);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al crear el profesor: " + e.getMessage()));
        }
    }

    /**
     * Obtiene un profesor por su identificador.
     *
     * @param id identificador del profesor
     * @return respuesta HTTP con los datos del profesor o error
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getProfessorById(@PathVariable String id) {
        try {
            Professor professor = professorService.getProfessorById(id);
            ProfessorDTO professorDTO = convertToDTO(professor);
            return ResponseEntity.ok(professorDTO);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener el profesor"));
        }
    }

    /**
     * Obtiene la lista completa de profesores.
     *
     * @return lista de profesores y cantidad total
     */
    @GetMapping
    public ResponseEntity<?> getAllProfessors() {
        try {
            List<Professor> professors = professorService.getAllProfessors();
            List<ProfessorDTO> professorDTOs = professors.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "professors", professorDTOs,
                    "count", professorDTOs.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener la lista de profesores"));
        }
    }

    /**
     * Actualiza los datos de un profesor existente.
     *
     * @param id identificador del profesor
     * @param professorDTO datos actualizados
     * @return profesor actualizado o mensaje de error
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfessor(@PathVariable String id, @RequestBody ProfessorDTO professorDTO) {
        try {
            Professor professor = convertToEntity(professorDTO);
            Professor updatedProfessor = professorService.updateProfessor(id, professor);
            ProfessorDTO responseDTO = convertToDTO(updatedProfessor);
            return ResponseEntity.ok(responseDTO);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al actualizar el profesor"));
        }
    }

    /**
     * Elimina un profesor por su identificador.
     *
     * @param id identificador del profesor
     * @return mensaje de éxito o error
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProfessor(@PathVariable String id) {
        try {
            professorService.deleteProfessor(id);
            return ResponseEntity.ok(Map.of(
                    "message", "Profesor eliminado exitosamente",
                    "deletedId", id
            ));
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al eliminar el profesor"));
        }
    }

    /**
     * Busca profesores por departamento.
     *
     * @param department nombre del departamento
     * @return lista de profesores del departamento
     */
    @GetMapping("/department/{department}")
    public ResponseEntity<?> findByDepartment(@PathVariable String department) {
        try {
            List<Professor> professors = professorService.findByDepartment(department);
            List<ProfessorDTO> professorDTOs = professors.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "department", department,
                    "professors", professorDTOs,
                    "count", professorDTOs.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al buscar profesores por departamento"));
        }
    }

    /**
     * Busca profesores por titularidad.
     *
     * @param isTenured indica si es titular
     * @return lista de profesores filtrados
     */
    @GetMapping("/tenured/{isTenured}")
    public ResponseEntity<?> findByTenured(@PathVariable Boolean isTenured) {
        try {
            List<Professor> professors = professorService.findByTenured(isTenured);
            List<ProfessorDTO> professorDTOs = professors.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "tenured", isTenured,
                    "professors", professorDTOs,
                    "count", professorDTOs.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al buscar profesores por titularidad"));
        }
    }

    /**
     * Busca profesores por área de especialización.
     *
     * @param area área de especialización
     * @return lista de profesores con dicha especialización
     */
    @GetMapping("/expertise/{area}")
    public ResponseEntity<?> findByAreaOfExpertise(@PathVariable String area) {
        try {
            List<Professor> professors = professorService.findByAreaOfExpertise(area);
            List<ProfessorDTO> professorDTOs = professors.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "expertise", area,
                    "professors", professorDTOs,
                    "count", professorDTOs.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al buscar profesores por área de especialización"));
        }
    }

    /**
     * Obtiene los profesores activos.
     *
     * @return lista de profesores activos
     */
    @GetMapping("/active")
    public ResponseEntity<?> findActiveProfessors() {
        try {
            List<Professor> activeProfessors = professorService.findActiveProfessors();
            List<ProfessorDTO> professorDTOs = activeProfessors.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "activeProfessors", professorDTOs,
                    "count", professorDTOs.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener los profesores activos"));
        }
    }

    /**
     * Obtiene estadísticas generales de los profesores.
     *
     * @return mapa con estadísticas como cantidad total, titulares y activos
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getProfessorStats() {
        try {
            List<Professor> allProfessors = professorService.getAllProfessors();
            List<Professor> tenuredProfessors = professorService.findByTenured(true);

            long activeCount = allProfessors.stream().filter(Professor::isActive).count();
            long inactiveCount = allProfessors.size() - activeCount;
            long tenuredCount = tenuredProfessors.size();
            double tenuredPercentage = allProfessors.size() > 0 ?
                    (double) tenuredCount / allProfessors.size() * 100 : 0;

            return ResponseEntity.ok(Map.of(
                    "totalProfessors", allProfessors.size(),
                    "tenuredProfessors", tenuredCount,
                    "activeProfessors", activeCount,
                    "inactiveProfessors", inactiveCount,
                    "tenuredPercentage", Math.round(tenuredPercentage * 100.0) / 100.0
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener estadísticas de profesores"));
        }
    }

    /**
     * Busca profesores por departamento y titularidad.
     *
     * @param department nombre del departamento
     * @param isTenured  indicador de titularidad
     * @return lista de profesores filtrados
     */
    @GetMapping("/department/{department}/tenured/{isTenured}")
    public ResponseEntity<?> findByDepartmentAndTenured(@PathVariable String department, @PathVariable Boolean isTenured) {
        try {
            List<Professor> professors = professorService.findByDepartmentAndTenured(department, isTenured);
            List<ProfessorDTO> professorDTOs = professors.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "department", department,
                    "tenured", isTenured,
                    "professors", professorDTOs,
                    "count", professorDTOs.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al buscar profesores por departamento y titularidad"));
        }
    }

    /**
     * Busca profesores cuyo nombre contenga una cadena específica.
     *
     * @param name parte del nombre
     * @return lista de profesores cuyo nombre coincide parcialmente
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<?> findByNameContaining(@PathVariable String name) {
        try {
            List<Professor> professors = professorService.findByNameContaining(name);
            List<ProfessorDTO> professorDTOs = professors.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "name", name,
                    "professors", professorDTOs,
                    "count", professorDTOs.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al buscar profesores por nombre"));
        }
    }

    /**
     * Verifica el estado del controlador.
     *
     * @return mensaje de estado
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("ProfessorController is working properly");
    }

    /**
     * Convierte un objeto DTO en una entidad {@link Professor}.
     *
     * @param dto objeto de transferencia de datos
     * @return entidad {@link Professor}
     */
    private Professor convertToEntity(ProfessorDTO dto) {
        Professor professor = new Professor();
        professor.setId(dto.getId());
        professor.setName(dto.getName());
        professor.setEmail(dto.getEmail());
        professor.setDepartment(dto.getDepartment());
        professor.setIsTenured(dto.getIsTenured());
        professor.setAreasOfExpertise(dto.getAreasOfExpertise());
        professor.setActive(dto.getActive() != null ? dto.getActive() : true);
        return professor;
    }

    /**
     * Convierte una entidad {@link Professor} en un objeto {@link ProfessorDTO}.
     *
     * @param professor entidad de profesor
     * @return objeto DTO
     */
    private ProfessorDTO convertToDTO(Professor professor) {
        ProfessorDTO dto = new ProfessorDTO();
        dto.setId(professor.getId());
        dto.setName(professor.getName());
        dto.setEmail(professor.getEmail());
        dto.setDepartment(professor.getDepartment());
        dto.setIsTenured(professor.getIsTenured());
        dto.setAreasOfExpertise(professor.getAreasOfExpertise());
        dto.setActive(professor.isActive());
        dto.setCreatedAt(professor.getCreatedAt());
        dto.setUpdatedAt(professor.getUpdatedAt());
        return dto;
    }
}
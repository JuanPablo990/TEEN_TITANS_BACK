package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto.ProfessorDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Professor;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.ProfessorService;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/professors")
@CrossOrigin(origins = "*")
public class ProfessorController {

    private final ProfessorService professorService;

    @Autowired
    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

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

    @GetMapping("/stats")
    public ResponseEntity<?> getProfessorStats() {
        try {
            List<Professor> allProfessors = professorService.getAllProfessors();
            List<Professor> tenuredProfessors = professorService.findByTenured(true);
            List<Professor> activeProfessors = professorService.findActiveProfessors();

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

    // MÉTODOS ADICIONALES ÚTILES
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

    @GetMapping("/department-pattern/{pattern}")
    public ResponseEntity<?> findByDepartmentPattern(@PathVariable String pattern) {
        try {
            List<Professor> professors = professorService.findByDepartmentPattern(pattern);
            List<ProfessorDTO> professorDTOs = professors.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "pattern", pattern,
                    "professors", professorDTOs,
                    "count", professorDTOs.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al buscar profesores por patrón de departamento"));
        }
    }

    @GetMapping("/expertise/in")
    public ResponseEntity<?> findByAreasOfExpertiseIn(@RequestParam List<String> areas) {
        try {
            List<Professor> professors = professorService.findByAreasOfExpertiseIn(areas);
            List<ProfessorDTO> professorDTOs = professors.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "areas", areas,
                    "professors", professorDTOs,
                    "count", professorDTOs.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al buscar profesores por áreas de especialización"));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchProfessors(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Boolean tenured,
            @RequestParam(required = false) Boolean active) {

        try {
            List<Professor> allProfessors = professorService.getAllProfessors();
            List<Professor> filteredProfessors = allProfessors.stream()
                    .filter(prof -> department == null || department.equals(prof.getDepartment()))
                    .filter(prof -> tenured == null || tenured.equals(prof.getIsTenured()))
                    .filter(prof -> active == null || active.equals(prof.isActive()))
                    .collect(Collectors.toList());

            List<ProfessorDTO> professorDTOs = filteredProfessors.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "searchCriteria", Map.of(
                            "department", department,
                            "tenured", tenured,
                            "active", active
                    ),
                    "professors", professorDTOs,
                    "count", professorDTOs.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error en la búsqueda de profesores"));
        }
    }

    @GetMapping("/count/department/{department}")
    public ResponseEntity<?> countByDepartment(@PathVariable String department) {
        try {
            long count = professorService.countByDepartment(department);
            return ResponseEntity.ok(Map.of(
                    "department", department,
                    "count", count
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al contar profesores por departamento"));
        }
    }

    @GetMapping("/count/tenured/{isTenured}")
    public ResponseEntity<?> countByTenured(@PathVariable Boolean isTenured) {
        try {
            long count = professorService.countByTenured(isTenured);
            return ResponseEntity.ok(Map.of(
                    "tenured", isTenured,
                    "count", count
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al contar profesores por titularidad"));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("ProfessorController is working properly");
    }

    // MÉTODOS DE CONVERSIÓN
    private Professor convertToEntity(ProfessorDTO dto) {
        Professor professor = new Professor();
        professor.setId(dto.getId());
        professor.setName(dto.getName());
        professor.setEmail(dto.getEmail());
        professor.setDepartment(dto.getDepartment());
        professor.setIsTenured(dto.getIsTenured());
        professor.setAreasOfExpertise(dto.getAreasOfExpertise());

        // Campos con valores por defecto si son null
        if (dto.getActive() != null) {
            professor.setActive(dto.getActive());
        } else {
            professor.setActive(true);
        }

        return professor;
    }

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
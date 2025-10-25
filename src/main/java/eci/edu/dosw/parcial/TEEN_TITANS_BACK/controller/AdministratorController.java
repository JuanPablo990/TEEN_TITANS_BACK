package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto.AdministratorDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Administrator;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.AdministratorService;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/administrators")
@CrossOrigin(origins = "*")
public class AdministratorController {

    private final AdministratorService administratorService;

    @Autowired
    public AdministratorController(AdministratorService administratorService) {
        this.administratorService = administratorService;
    }

    @PostMapping
    public ResponseEntity<?> createAdministrator(@RequestBody AdministratorDTO administratorDTO) {
        try {
            Administrator administrator = convertToEntity(administratorDTO);
            Administrator createdAdmin = administratorService.createAdministrator(administrator);
            AdministratorDTO responseDTO = convertToDTO(createdAdmin);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al crear el administrador: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAdministratorById(@PathVariable String id) {
        try {
            Administrator administrator = administratorService.getAdministratorById(id);
            AdministratorDTO administratorDTO = convertToDTO(administrator);
            return ResponseEntity.ok(administratorDTO);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener el administrador"));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllAdministrators() {
        try {
            List<Administrator> administrators = administratorService.getAllAdministrators();
            List<AdministratorDTO> administratorDTOs = administrators.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "administrators", administratorDTOs,
                    "count", administratorDTOs.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener la lista de administradores"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAdministrator(@PathVariable String id, @RequestBody AdministratorDTO administratorDTO) {
        try {
            Administrator administrator = convertToEntity(administratorDTO);
            Administrator updatedAdmin = administratorService.updateAdministrator(id, administrator);
            AdministratorDTO responseDTO = convertToDTO(updatedAdmin);
            return ResponseEntity.ok(responseDTO);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al actualizar el administrador"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAdministrator(@PathVariable String id) {
        try {
            administratorService.deleteAdministrator(id);
            return ResponseEntity.ok(Map.of(
                    "message", "Administrador eliminado exitosamente",
                    "deletedId", id
            ));
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al eliminar el administrador"));
        }
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<?> findByDepartment(@PathVariable String department) {
        try {
            List<Administrator> administrators = administratorService.findByDepartment(department);
            List<AdministratorDTO> administratorDTOs = administrators.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "department", department,
                    "administrators", administratorDTOs,
                    "count", administratorDTOs.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al buscar administradores por departamento"));
        }
    }

    // MÉTODOS ADICIONALES ÚTILES
    @GetMapping("/department-pattern/{pattern}")
    public ResponseEntity<?> findByDepartmentContaining(@PathVariable String pattern) {
        try {
            List<Administrator> administrators = administratorService.findByDepartmentContaining(pattern);
            List<AdministratorDTO> administratorDTOs = administrators.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "pattern", pattern,
                    "administrators", administratorDTOs,
                    "count", administratorDTOs.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al buscar administradores por patrón de departamento"));
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> findByNameContaining(@PathVariable String name) {
        try {
            List<Administrator> administrators = administratorService.findByNameContaining(name);
            List<AdministratorDTO> administratorDTOs = administrators.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "name", name,
                    "administrators", administratorDTOs,
                    "count", administratorDTOs.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al buscar administradores por nombre"));
        }
    }

    @GetMapping("/active")
    public ResponseEntity<?> findActiveAdministrators() {
        try {
            List<Administrator> activeAdministrators = administratorService.findActiveAdministrators();
            List<AdministratorDTO> administratorDTOs = activeAdministrators.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "activeAdministrators", administratorDTOs,
                    "count", administratorDTOs.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener los administradores activos"));
        }
    }

    @GetMapping("/department/{department}/active/{active}")
    public ResponseEntity<?> findByDepartmentAndActive(@PathVariable String department, @PathVariable Boolean active) {
        try {
            List<Administrator> administrators = administratorService.findByDepartmentAndActive(department, active);
            List<AdministratorDTO> administratorDTOs = administrators.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "department", department,
                    "active", active,
                    "administrators", administratorDTOs,
                    "count", administratorDTOs.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al buscar administradores por departamento y estado activo"));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchAdministrators(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Boolean active) {

        try {
            List<Administrator> allAdministrators = administratorService.getAllAdministrators();
            List<Administrator> filteredAdministrators = allAdministrators.stream()
                    .filter(admin -> department == null || department.equals(admin.getDepartment()))
                    .filter(admin -> active == null || active.equals(admin.isActive()))
                    .collect(Collectors.toList());

            List<AdministratorDTO> administratorDTOs = filteredAdministrators.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "searchCriteria", Map.of(
                            "department", department,
                            "active", active
                    ),
                    "administrators", administratorDTOs,
                    "count", administratorDTOs.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error en la búsqueda de administradores"));
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getAdministratorStatistics() {
        try {
            List<Administrator> allAdministrators = administratorService.getAllAdministrators();

            long activeCount = allAdministrators.stream().filter(Administrator::isActive).count();
            long inactiveCount = allAdministrators.size() - activeCount;

            Map<String, Long> administratorsByDepartment = allAdministrators.stream()
                    .collect(Collectors.groupingBy(Administrator::getDepartment, Collectors.counting()));

            return ResponseEntity.ok(Map.of(
                    "totalAdministrators", allAdministrators.size(),
                    "activeAdministrators", activeCount,
                    "inactiveAdministrators", inactiveCount,
                    "administratorsByDepartment", administratorsByDepartment,
                    "activePercentage", allAdministrators.isEmpty() ?
                            0 : Math.round((double) activeCount / allAdministrators.size() * 100 * 100.0) / 100.0
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener estadísticas de administradores"));
        }
    }

    @GetMapping("/count/department/{department}")
    public ResponseEntity<?> countByDepartment(@PathVariable String department) {
        try {
            long count = administratorService.countByDepartment(department);
            return ResponseEntity.ok(Map.of(
                    "department", department,
                    "count", count
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al contar administradores por departamento"));
        }
    }

    @GetMapping("/count/department/{department}/active/{active}")
    public ResponseEntity<?> countByDepartmentAndActive(@PathVariable String department, @PathVariable Boolean active) {
        try {
            long count = administratorService.countByDepartmentAndActive(department, active);
            return ResponseEntity.ok(Map.of(
                    "department", department,
                    "active", active,
                    "count", count
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al contar administradores por departamento y estado activo"));
        }
    }

    @GetMapping("/departments")
    public ResponseEntity<?> getAllDepartments() {
        try {
            List<Administrator> allAdministrators = administratorService.getAllAdministrators();
            List<String> departments = allAdministrators.stream()
                    .map(Administrator::getDepartment)
                    .distinct()
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "departments", departments,
                    "count", departments.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener la lista de departamentos"));
        }
    }

    @GetMapping("/department-pattern/{pattern}/count")
    public ResponseEntity<?> countByDepartmentPattern(@PathVariable String pattern) {
        try {
            List<Administrator> administrators = administratorService.findByDepartmentPattern(pattern);
            return ResponseEntity.ok(Map.of(
                    "pattern", pattern,
                    "count", administrators.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al contar administradores por patrón de departamento"));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("AdministratorController is working properly");
    }

    // MÉTODOS DE CONVERSIÓN
    private Administrator convertToEntity(AdministratorDTO dto) {
        Administrator administrator = new Administrator();
        administrator.setId(dto.getId());
        administrator.setName(dto.getName());
        administrator.setEmail(dto.getEmail());
        administrator.setDepartment(dto.getDepartment());

        // Campos con valores por defecto si son null
        if (dto.getActive() != null) {
            administrator.setActive(dto.getActive());
        } else {
            administrator.setActive(true);
        }

        return administrator;
    }

    private AdministratorDTO convertToDTO(Administrator administrator) {
        AdministratorDTO dto = new AdministratorDTO();
        dto.setId(administrator.getId());
        dto.setName(administrator.getName());
        dto.setEmail(administrator.getEmail());
        dto.setDepartment(administrator.getDepartment());
        dto.setActive(administrator.isActive());
        dto.setCreatedAt(administrator.getCreatedAt());
        dto.setUpdatedAt(administrator.getUpdatedAt());
        return dto;
    }
}
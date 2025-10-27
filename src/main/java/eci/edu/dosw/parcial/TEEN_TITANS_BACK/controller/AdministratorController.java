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

/**
 * Controlador REST para la gestión de administradores dentro del sistema universitario.
 * Proporciona endpoints para realizar operaciones CRUD, búsquedas, conteos
 * y estadísticas sobre los administradores registrados en la base de datos.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@RestController
@RequestMapping("/api/administrators")
@CrossOrigin(origins = "*")
public class AdministratorController {

    private final AdministratorService administratorService;

    /**
     * Constructor para inyectar la dependencia del servicio de administradores.
     *
     * @param administratorService servicio encargado de la lógica de negocio de administradores
     */
    @Autowired
    public AdministratorController(AdministratorService administratorService) {
        this.administratorService = administratorService;
    }

    /**
     * Crea un nuevo administrador en el sistema.
     *
     * @param administratorDTO datos del administrador a registrar
     * @return el administrador creado o un mensaje de error
     */
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

    /**
     * Obtiene un administrador por su identificador único.
     *
     * @param id identificador del administrador
     * @return el administrador correspondiente o un mensaje de error
     */
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

    /**
     * Obtiene la lista completa de administradores.
     *
     * @return lista de administradores y cantidad total
     */
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

    /**
     * Actualiza los datos de un administrador existente.
     *
     * @param id identificador del administrador a actualizar
     * @param administratorDTO datos actualizados del administrador
     * @return el administrador actualizado o un mensaje de error
     */
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

    /**
     * Elimina un administrador del sistema por su identificador.
     *
     * @param id identificador del administrador
     * @return mensaje de confirmación o error
     */
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

    /**
     * Busca administradores por departamento.
     *
     * @param department nombre del departamento
     * @return lista de administradores del departamento
     */
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

    /**
     * Busca administradores cuyos departamentos coincidan parcialmente con un patrón.
     *
     * @param pattern patrón de búsqueda
     * @return lista de administradores que coinciden con el patrón
     */
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

    /**
     * Busca administradores por coincidencia parcial de nombre.
     *
     * @param name patrón del nombre
     * @return lista de administradores que coinciden con el nombre
     */
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

    /**
     * Obtiene todos los administradores activos.
     *
     * @return lista de administradores activos
     */
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

    /**
     * Busca administradores por departamento y estado activo.
     *
     * @param department nombre del departamento
     * @param active estado del administrador
     * @return lista de administradores filtrados
     */
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

    /**
     * Realiza una búsqueda dinámica de administradores según criterios opcionales.
     *
     * @param department departamento opcional
     * @param active estado activo opcional
     * @return lista filtrada de administradores
     */
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

    /**
     * Obtiene estadísticas generales sobre los administradores registrados.
     *
     * @return métricas de distribución, actividad y conteo
     */
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

    /**
     * Cuenta el número de administradores por departamento.
     *
     * @param department nombre del departamento
     * @return cantidad de administradores en el departamento
     */
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

    /**
     * Cuenta el número de administradores en un departamento según su estado.
     *
     * @param department departamento a evaluar
     * @param active estado activo/inactivo
     * @return cantidad de administradores filtrados
     */
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

    /**
     * Obtiene la lista de todos los departamentos con administradores registrados.
     *
     * @return lista de nombres de departamentos y cantidad total
     */
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

    /**
     * Cuenta administradores que coincidan con un patrón de nombre de departamento.
     *
     * @param pattern patrón de búsqueda
     * @return cantidad de administradores coincidentes
     */
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

    /**
     * Endpoint de verificación del estado del controlador.
     *
     * @return mensaje de confirmación de funcionamiento
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("AdministratorController is working properly");
    }

    /**
     * Convierte un DTO en una entidad de tipo {@link Administrator}.
     *
     * @param dto objeto de transferencia de datos
     * @return entidad {@link Administrator}
     */
    private Administrator convertToEntity(AdministratorDTO dto) {
        Administrator administrator = new Administrator();
        administrator.setId(dto.getId());
        administrator.setName(dto.getName());
        administrator.setEmail(dto.getEmail());
        administrator.setDepartment(dto.getDepartment());
        if (dto.getActive() != null) {
            administrator.setActive(dto.getActive());
        } else {
            administrator.setActive(true);
        }
        return administrator;
    }

    /**
     * Convierte una entidad {@link Administrator} a su respectivo DTO.
     *
     * @param administrator entidad a convertir
     * @return objeto {@link AdministratorDTO}
     */
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
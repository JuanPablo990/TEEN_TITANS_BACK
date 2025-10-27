package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto.AdministratorDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Administrator;
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
 * y estadísticas sobre los administradores registrados.
 *
 * @author
 * @version 1.0
 * @since 2025
 */
@RestController
@RequestMapping("/api/administrators")
@CrossOrigin(origins = "*")
public class AdministratorController {

    private final AdministratorService administratorService;

    /**
     * Constructor para inyectar el servicio de administradores.
     *
     * @param administratorService servicio encargado de la lógica de negocio de administradores
     */
    @Autowired
    public AdministratorController(AdministratorService administratorService) {
        this.administratorService = administratorService;
    }

    /**
     * Crea un nuevo administrador.
     *
     * @param administratorDTO datos del administrador a registrar
     * @return administrador creado o mensaje de error
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
                    .body(Map.of("error", "Error al crear el administrador"));
        }
    }

    /**
     * Obtiene un administrador por su ID.
     *
     * @param id identificador del administrador
     * @return administrador encontrado o mensaje de error
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getAdministratorById(@PathVariable String id) {
        try {
            Administrator administrator = administratorService.getAdministratorById(id);
            return ResponseEntity.ok(convertToDTO(administrator));
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener el administrador"));
        }
    }

    /**
     * Obtiene todos los administradores registrados.
     *
     * @return lista de administradores
     */
    @GetMapping
    public ResponseEntity<?> getAllAdministrators() {
        try {
            List<AdministratorDTO> administrators = administratorService.getAllAdministrators()
                    .stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "administrators", administrators,
                    "count", administrators.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener los administradores"));
        }
    }

    /**
     * Actualiza un administrador existente.
     *
     * @param id identificador del administrador
     * @param administratorDTO datos actualizados
     * @return administrador actualizado o mensaje de error
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAdministrator(@PathVariable String id, @RequestBody AdministratorDTO administratorDTO) {
        try {
            Administrator administrator = convertToEntity(administratorDTO);
            Administrator updatedAdmin = administratorService.updateAdministrator(id, administrator);
            return ResponseEntity.ok(convertToDTO(updatedAdmin));
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al actualizar el administrador"));
        }
    }

    /**
     * Elimina un administrador.
     *
     * @param id identificador del administrador
     * @return mensaje de confirmación
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
            List<AdministratorDTO> administrators = administratorService.findByDepartment(department)
                    .stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "department", department,
                    "administrators", administrators,
                    "count", administrators.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al buscar administradores por departamento"));
        }
    }

    /**
     * Busca administradores por coincidencia parcial en el nombre del departamento.
     *
     * @param pattern patrón de búsqueda
     * @return lista de administradores coincidentes
     */
    @GetMapping("/department-pattern/{pattern}")
    public ResponseEntity<?> findByDepartmentContaining(@PathVariable String pattern) {
        try {
            List<AdministratorDTO> administrators = administratorService.findByDepartmentContaining(pattern)
                    .stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "pattern", pattern,
                    "administrators", administrators,
                    "count", administrators.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al buscar administradores por patrón"));
        }
    }

    /**
     * Busca administradores por coincidencia parcial de nombre.
     *
     * @param name nombre o parte del nombre
     * @return lista de administradores
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<?> findByNameContaining(@PathVariable String name) {
        try {
            List<AdministratorDTO> administrators = administratorService.findByNameContaining(name)
                    .stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "name", name,
                    "administrators", administrators,
                    "count", administrators.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al buscar administradores por nombre"));
        }
    }

    /**
     * Obtiene administradores activos.
     *
     * @return lista de administradores activos
     */
    @GetMapping("/active")
    public ResponseEntity<?> findActiveAdministrators() {
        try {
            List<AdministratorDTO> administrators = administratorService.findActiveAdministrators()
                    .stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "activeAdministrators", administrators,
                    "count", administrators.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener administradores activos"));
        }
    }

    /**
     * Endpoint de prueba para verificar el estado del controlador.
     *
     * @return mensaje de estado
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("AdministratorController is working properly");
    }

    /**
     * Convierte un DTO en una entidad {@link Administrator}.
     *
     * @param dto objeto de transferencia de datos
     * @return entidad convertida
     */
    private Administrator convertToEntity(AdministratorDTO dto) {
        Administrator administrator = new Administrator();
        administrator.setId(dto.getId());
        administrator.setName(dto.getName());
        administrator.setEmail(dto.getEmail());
        administrator.setDepartment(dto.getDepartment());
        administrator.setActive(dto.getActive() != null ? dto.getActive() : true);
        return administrator;
    }

    /**
     * Convierte una entidad {@link Administrator} a un DTO.
     *
     * @param administrator entidad a convertir
     * @return DTO correspondiente
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

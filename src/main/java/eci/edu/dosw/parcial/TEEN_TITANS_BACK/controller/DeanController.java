package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto.DeanDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Dean;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.DeanService;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador REST encargado de la gestión de decanos en el sistema.
 * Proporciona endpoints para la creación, consulta, actualización,
 * eliminación, búsqueda avanzada y estadísticas relacionadas con los decanos.
 *
 * @author
 * @version 1.0
 * @since 2025
 */
@RestController
@RequestMapping("/api/deans")
@CrossOrigin(origins = "*")
public class DeanController {

    private final DeanService deanService;

    /**
     * Constructor para la inyección del servicio de decanos.
     *
     * @param deanService servicio de lógica de negocio para los decanos
     */
    @Autowired
    public DeanController(DeanService deanService) {
        this.deanService = deanService;
    }

    /**
     * Crea un nuevo decano en el sistema.
     *
     * @param deanDTO objeto con los datos del decano a crear
     * @return el decano creado o un mensaje de error
     */
    @PostMapping
    public ResponseEntity<?> createDean(@RequestBody DeanDTO deanDTO) {
        try {
            Dean dean = convertToEntity(deanDTO);
            Dean createdDean = deanService.createDean(dean);
            DeanDTO responseDTO = convertToDTO(createdDean);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al crear el decano: " + e.getMessage()));
        }
    }

    /**
     * Obtiene un decano específico por su identificador.
     *
     * @param id identificador del decano
     * @return el decano encontrado o mensaje de error
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getDeanById(@PathVariable String id) {
        try {
            Dean dean = deanService.getDeanById(id);
            DeanDTO deanDTO = convertToDTO(dean);
            return ResponseEntity.ok(deanDTO);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener el decano"));
        }
    }

    /**
     * Obtiene todos los decanos registrados en el sistema.
     *
     * @return lista de decanos y cantidad total
     */
    @GetMapping
    public ResponseEntity<?> getAllDeans() {
        try {
            List<Dean> deans = deanService.getAllDeans();
            List<DeanDTO> deanDTOs = deans.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "deans", deanDTOs,
                    "count", deanDTOs.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener la lista de decanos"));
        }
    }

    /**
     * Actualiza la información de un decano existente.
     *
     * @param id identificador del decano
     * @param deanDTO objeto con los nuevos datos del decano
     * @return el decano actualizado o mensaje de error
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDean(@PathVariable String id, @RequestBody DeanDTO deanDTO) {
        try {
            Dean dean = convertToEntity(deanDTO);
            Dean updatedDean = deanService.updateDean(id, dean);
            DeanDTO responseDTO = convertToDTO(updatedDean);
            return ResponseEntity.ok(responseDTO);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al actualizar el decano"));
        }
    }

    /**
     * Elimina un decano por su identificador.
     *
     * @param id identificador del decano
     * @return mensaje de confirmación o error
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDean(@PathVariable String id) {
        try {
            deanService.deleteDean(id);
            return ResponseEntity.ok(Map.of(
                    "message", "Decano eliminado exitosamente",
                    "deletedId", id
            ));
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al eliminar el decano"));
        }
    }

    /**
     * Busca decanos por facultad.
     *
     * @param faculty nombre de la facultad
     * @return lista de decanos de la facultad especificada
     */
    @GetMapping("/faculty/{faculty}")
    public ResponseEntity<?> findByFaculty(@PathVariable String faculty) {
        try {
            List<Dean> deans = deanService.findByFaculty(faculty);
            List<DeanDTO> deanDTOs = deans.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "faculty", faculty,
                    "deans", deanDTOs,
                    "count", deanDTOs.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al buscar decanos por facultad"));
        }
    }

    /**
     * Busca decanos por ubicación de oficina.
     *
     * @param location ubicación de oficina
     * @return lista de decanos encontrados
     */
    @GetMapping("/office/{location}")
    public ResponseEntity<?> findByOfficeLocation(@PathVariable String location) {
        try {
            List<Dean> deans = deanService.findByOfficeLocation(location);
            List<DeanDTO> deanDTOs = deans.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "officeLocation", location,
                    "deans", deanDTOs,
                    "count", deanDTOs.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al buscar decanos por ubicación de oficina"));
        }
    }

    /**
     * Obtiene el decano asignado a una facultad específica.
     *
     * @param faculty nombre de la facultad
     * @return el decano correspondiente o mensaje de error
     */
    @GetMapping("/faculty/{faculty}/dean")
    public ResponseEntity<?> getDeanByFaculty(@PathVariable String faculty) {
        try {
            Dean dean = deanService.getDeanByFaculty(faculty);
            DeanDTO deanDTO = convertToDTO(dean);
            return ResponseEntity.ok(deanDTO);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener el decano por facultad"));
        }
    }

    /**
     * Busca decanos por facultad y ubicación de oficina.
     *
     * @param faculty nombre de la facultad
     * @param officeLocation ubicación de oficina
     * @return lista de decanos filtrados
     */
    @GetMapping("/faculty/{faculty}/office/{officeLocation}")
    public ResponseEntity<?> findByFacultyAndOfficeLocation(@PathVariable String faculty, @PathVariable String officeLocation) {
        try {
            List<Dean> deans = deanService.findByFacultyAndOfficeLocation(faculty, officeLocation);
            List<DeanDTO> deanDTOs = deans.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "faculty", faculty,
                    "officeLocation", officeLocation,
                    "deans", deanDTOs,
                    "count", deanDTOs.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al buscar decanos por facultad y ubicación de oficina"));
        }
    }

    /**
     * Busca decanos cuyos nombres coincidan parcialmente con el patrón proporcionado.
     *
     * @param name patrón de nombre
     * @return lista de decanos coincidentes
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<?> findByNameContaining(@PathVariable String name) {
        try {
            List<Dean> deans = deanService.findByNameContaining(name);
            List<DeanDTO> deanDTOs = deans.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "name", name,
                    "deans", deanDTOs,
                    "count", deanDTOs.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al buscar decanos por nombre"));
        }
    }

    /**
     * Busca decanos por patrón de facultad.
     *
     * @param pattern patrón de búsqueda
     * @return lista de decanos que coinciden con el patrón
     */
    @GetMapping("/faculty-pattern/{pattern}")
    public ResponseEntity<?> findByFacultyPattern(@PathVariable String pattern) {
        try {
            List<Dean> deans = deanService.findByFacultyContaining(pattern);
            List<DeanDTO> deanDTOs = deans.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "pattern", pattern,
                    "deans", deanDTOs,
                    "count", deanDTOs.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al buscar decanos por patrón de facultad"));
        }
    }

    /**
     * Busca decanos por patrón de ubicación de oficina.
     *
     * @param pattern patrón de búsqueda
     * @return lista de decanos que coinciden con el patrón
     */
    @GetMapping("/office-pattern/{pattern}")
    public ResponseEntity<?> findByOfficeLocationPattern(@PathVariable String pattern) {
        try {
            List<Dean> deans = deanService.findByOfficeLocationContaining(pattern);
            List<DeanDTO> deanDTOs = deans.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "pattern", pattern,
                    "deans", deanDTOs,
                    "count", deanDTOs.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al buscar decanos por patrón de ubicación de oficina"));
        }
    }

    /**
     * Obtiene la lista de decanos activos.
     *
     * @return lista de decanos activos y cantidad total
     */
    @GetMapping("/active")
    public ResponseEntity<?> findActiveDeans() {
        try {
            List<Dean> activeDeans = deanService.findActiveDeans();
            List<DeanDTO> deanDTOs = activeDeans.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "activeDeans", deanDTOs,
                    "count", deanDTOs.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener los decanos activos"));
        }
    }

    /**
     * Realiza una búsqueda avanzada filtrando por facultad, oficina o estado activo.
     *
     * @param faculty facultad opcional
     * @param officeLocation ubicación de oficina opcional
     * @param active estado activo opcional
     * @return lista filtrada de decanos
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchDeans(
            @RequestParam(required = false) String faculty,
            @RequestParam(required = false) String officeLocation,
            @RequestParam(required = false) Boolean active) {

        try {
            List<Dean> allDeans = deanService.getAllDeans();
            List<Dean> filteredDeans = allDeans.stream()
                    .filter(dean -> faculty == null || faculty.equals(dean.getFaculty()))
                    .filter(dean -> officeLocation == null || officeLocation.equals(dean.getOfficeLocation()))
                    .filter(dean -> active == null || active.equals(dean.isActive()))
                    .collect(Collectors.toList());

            List<DeanDTO> deanDTOs = filteredDeans.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "searchCriteria", Map.of(
                            "faculty", faculty,
                            "officeLocation", officeLocation,
                            "active", active
                    ),
                    "deans", deanDTOs,
                    "count", deanDTOs.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error en la búsqueda de decanos"));
        }
    }

    /**
     * Genera estadísticas generales sobre los decanos del sistema.
     *
     * @return métricas de actividad, distribución y conteos
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getDeanStatistics() {
        try {
            List<Dean> allDeans = deanService.getAllDeans();

            long activeCount = allDeans.stream().filter(Dean::isActive).count();
            long inactiveCount = allDeans.size() - activeCount;

            Map<String, Long> deansByFaculty = allDeans.stream()
                    .collect(Collectors.groupingBy(Dean::getFaculty, Collectors.counting()));

            Map<String, Long> deansByOfficeLocation = allDeans.stream()
                    .collect(Collectors.groupingBy(Dean::getOfficeLocation, Collectors.counting()));

            return ResponseEntity.ok(Map.of(
                    "totalDeans", allDeans.size(),
                    "activeDeans", activeCount,
                    "inactiveDeans", inactiveCount,
                    "deansByFaculty", deansByFaculty,
                    "deansByOfficeLocation", deansByOfficeLocation,
                    "activePercentage", allDeans.isEmpty() ?
                            0 : Math.round((double) activeCount / allDeans.size() * 100 * 100.0) / 100.0
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener estadísticas de decanos"));
        }
    }

    /**
     * Cuenta el número de decanos por facultad.
     *
     * @param faculty nombre de la facultad
     * @return cantidad de decanos registrados en la facultad
     */
    @GetMapping("/count/faculty/{faculty}")
    public ResponseEntity<?> countByFaculty(@PathVariable String faculty) {
        try {
            long count = deanService.countByFaculty(faculty);
            return ResponseEntity.ok(Map.of(
                    "faculty", faculty,
                    "count", count
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al contar decanos por facultad"));
        }
    }

    /**
     * Cuenta los decanos por ubicación de oficina.
     *
     * @param officeLocation ubicación de oficina
     * @return cantidad de decanos encontrados
     */
    @GetMapping("/count/office/{officeLocation}")
    public ResponseEntity<?> countByOfficeLocation(@PathVariable String officeLocation) {
        try {
            long count = deanService.countByOfficeLocation(officeLocation);
            return ResponseEntity.ok(Map.of(
                    "officeLocation", officeLocation,
                    "count", count
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al contar decanos por ubicación de oficina"));
        }
    }

    /**
     * Obtiene la lista de todas las facultades con decanos registrados.
     *
     * @return lista de nombres de facultades y cantidad total
     */
    @GetMapping("/faculties")
    public ResponseEntity<?> getAllFaculties() {
        try {
            List<Dean> allDeans = deanService.getAllDeans();
            List<String> faculties = allDeans.stream()
                    .map(Dean::getFaculty)
                    .distinct()
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "faculties", faculties,
                    "count", faculties.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener la lista de facultades"));
        }
    }

    /**
     * Verifica el estado de funcionamiento del controlador.
     *
     * @return mensaje de confirmación de funcionamiento
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("DeanController is working properly");
    }

    /**
     * Convierte un DTO en una entidad {@link Dean}.
     *
     * @param dto objeto con los datos del decano
     * @return entidad {@link Dean}
     */
    private Dean convertToEntity(DeanDTO dto) {
        Dean dean = new Dean();
        dean.setId(dto.getId());
        dean.setName(dto.getName());
        dean.setEmail(dto.getEmail());
        dean.setFaculty(dto.getFaculty());
        dean.setOfficeLocation(dto.getOfficeLocation());
        if (dto.getActive() != null) {
            dean.setActive(dto.getActive());
        } else {
            dean.setActive(true);
        }
        return dean;
    }

    /**
     * Convierte una entidad {@link Dean} en un DTO.
     *
     * @param dean entidad decano
     * @return objeto {@link DeanDTO}
     */
    private DeanDTO convertToDTO(Dean dean) {
        DeanDTO dto = new DeanDTO();
        dto.setId(dean.getId());
        dto.setName(dean.getName());
        dto.setEmail(dean.getEmail());
        dto.setFaculty(dean.getFaculty());
        dto.setOfficeLocation(dean.getOfficeLocation());
        dto.setActive(dean.isActive());
        dto.setCreatedAt(dean.getCreatedAt());
        dto.setUpdatedAt(dean.getUpdatedAt());
        return dto;
    }
}
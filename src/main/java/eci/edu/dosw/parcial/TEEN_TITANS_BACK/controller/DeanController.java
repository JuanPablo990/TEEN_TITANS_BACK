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

@RestController
@RequestMapping("/api/deans")
@CrossOrigin(origins = "*")
public class DeanController {

    private final DeanService deanService;

    @Autowired
    public DeanController(DeanService deanService) {
        this.deanService = deanService;
    }

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

    // MÉTODOS ADICIONALES ÚTILES
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

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("DeanController is working properly");
    }

    // MÉTODOS DE CONVERSIÓN
    private Dean convertToEntity(DeanDTO dto) {
        Dean dean = new Dean();
        dean.setId(dto.getId());
        dean.setName(dto.getName());
        dean.setEmail(dto.getEmail());
        dean.setFaculty(dto.getFaculty());
        dean.setOfficeLocation(dto.getOfficeLocation());

        // Campos con valores por defecto si son null
        if (dto.getActive() != null) {
            dean.setActive(dto.getActive());
        } else {
            dean.setActive(true);
        }

        return dean;
    }

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
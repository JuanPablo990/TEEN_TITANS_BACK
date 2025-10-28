package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto.GroupDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.AdminGroupService;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador REST para la gestión administrativa de grupos, cursos, profesores y aulas.
 * Proporciona endpoints para crear grupos, asignar profesores o aulas, y consultar información
 * académica relacionada.
 *
 * @author Equipo Teen Titans
 * @version 2.0
 */
@Slf4j
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AdminGroupController {

    private final AdminGroupService adminGroupService;

    /**
     * Crea un nuevo grupo académico.
     */
    @PostMapping("/groups")
    public ResponseEntity<?> createGroup(@RequestBody Group group) {
        log.info("Solicitud para crear grupo: {}", group.getGroupId());
        try {
            Group createdGroup = adminGroupService.createGroup(group);
            log.info("Grupo creado exitosamente: {}", createdGroup.getGroupId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdGroup);
        } catch (AppException e) {
            log.warn("Error al crear grupo: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("Error interno al crear grupo: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al crear el grupo");
        }
    }

    /**
     * Crea un nuevo grupo académico a partir de un DTO validado.
     */
    @PostMapping("/groups/dto")
    public ResponseEntity<?> createGroupFromDTO(@Valid @RequestBody GroupDTO groupDTO, BindingResult bindingResult) {
        log.info("Solicitud para crear grupo desde DTO: {}", groupDTO.getGroupId());

        // Validar errores de validación del DTO
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            log.warn("Errores de validación en DTO: {}", errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Map.of(
                            "message", "Errores de validación",
                            "errors", errors,
                            "timestamp", LocalDateTime.now()
                    )
            );
        }

        try {
            Group createdGroup = adminGroupService.createGroupFromDTO(groupDTO);
            log.info("Grupo creado exitosamente desde DTO: {}", createdGroup.getGroupId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdGroup);
        } catch (AppException e) {
            log.warn("Error al crear grupo desde DTO: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Map.of(
                            "message", e.getMessage(),
                            "timestamp", LocalDateTime.now()
                    )
            );
        } catch (Exception e) {
            log.error("Error interno al crear grupo desde DTO: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al crear el grupo desde DTO");
        }
    }

    /**
     * Obtiene todos los grupos registrados.
     */
    @GetMapping("/groups")
    public ResponseEntity<?> getAllGroups() {
        try {
            List<Group> groups = adminGroupService.listAllGroups();
            log.info("Obtenidos {} grupos", groups.size());
            return ResponseEntity.ok(groups);
        } catch (Exception e) {
            log.error("Error al obtener todos los grupos: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al obtener los grupos");
        }
    }

    /**
     * Obtiene un grupo específico por ID.
     */
    @GetMapping("/groups/{groupId}")
    public ResponseEntity<?> getGroupById(@PathVariable String groupId) {
        log.info("Solicitando grupo: {}", groupId);
        try {
            Group group = adminGroupService.getGroupById(groupId);
            return ResponseEntity.ok(group);
        } catch (AppException e) {
            log.warn("Grupo no encontrado: {}", groupId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.error("Error interno al obtener grupo {}: {}", groupId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al obtener el grupo");
        }
    }

    /**
     * Elimina un grupo por ID.
     */
    @DeleteMapping("/groups/{groupId}")
    public ResponseEntity<?> deleteGroup(@PathVariable String groupId) {
        log.info("Solicitud para eliminar grupo: {}", groupId);
        try {
            adminGroupService.deleteGroup(groupId);
            log.info("Grupo eliminado exitosamente: {}", groupId);
            return ResponseEntity.ok(Map.of(
                    "message", "Grupo eliminado exitosamente",
                    "groupId", groupId
            ));
        } catch (AppException e) {
            log.warn("Error al eliminar grupo: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("Error interno al eliminar grupo {}: {}", groupId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al eliminar el grupo");
        }
    }

    /**
     * Asigna un profesor a un grupo.
     */
    @PutMapping("/groups/{groupId}/professor/{professorId}")
    public ResponseEntity<?> assignProfessorToGroup(@PathVariable String groupId,
                                                    @PathVariable String professorId) {
        log.info("Asignando profesor {} al grupo {}", professorId, groupId);
        try {
            adminGroupService.assignProfessorToGroup(groupId, professorId);
            log.info("Profesor asignado exitosamente");
            return ResponseEntity.ok().body("Profesor asignado al grupo exitosamente");
        } catch (AppException e) {
            log.warn("Error al asignar profesor: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.error("Error interno al asignar profesor: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al asignar el profesor al grupo");
        }
    }

    /**
     * Asigna un aula a un grupo.
     */
    @PutMapping("/groups/{groupId}/classroom/{classroomId}")
    public ResponseEntity<?> assignClassroomToGroup(@PathVariable String groupId,
                                                    @PathVariable String classroomId) {
        log.info("Asignando aula {} al grupo {}", classroomId, groupId);
        try {
            adminGroupService.assignClassroomToGroup(groupId, classroomId);
            log.info("Aula asignada exitosamente");
            return ResponseEntity.ok().body("Aula asignada al grupo exitosamente");
        } catch (AppException e) {
            log.warn("Error al asignar aula: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.error("Error interno al asignar aula: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al asignar el aula al grupo");
        }
    }

    /**
     * Endpoint de salud para verificar que el controlador está funcionando.
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        log.info("Health check administrativo solicitado");
        return ResponseEntity.ok("AdminGroupController is working properly");
    }
}
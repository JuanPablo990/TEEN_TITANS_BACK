package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto.StudentDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Student;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.StudentService;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador REST encargado de manejar las operaciones CRUD y consultas avanzadas
 * relacionadas con los estudiantes del sistema.
 * <p>
 * Permite crear, obtener, actualizar, eliminar y filtrar estudiantes
 * según diversos criterios como programa académico, semestre, promedio y estado activo.
 *
 * @author Teen Titans
 * @version 1.0
 * @since 2025
 */
@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    private final StudentService studentService;

    /**
     * Constructor que inyecta el servicio de estudiantes.
     *
     * @param studentService servicio de lógica de negocio para los estudiantes
     */
    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * Crea un nuevo estudiante en el sistema.
     *
     * @param studentDTO datos del estudiante a registrar
     * @return el estudiante creado o un mensaje de error
     */
    @PostMapping
    public ResponseEntity<?> createStudent(@RequestBody StudentDTO studentDTO) {
        try {
            Student student = convertToStudent(studentDTO);
            Student createdStudent = studentService.createStudent(student);
            StudentDTO responseDTO = convertToStudentDTO(createdStudent);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al crear el estudiante: " + e.getMessage()));
        }
    }

    /**
     * Obtiene un estudiante específico por su identificador.
     *
     * @param id identificador único del estudiante
     * @return el estudiante encontrado o un mensaje de error
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable String id) {
        try {
            Student student = studentService.getStudentById(id);
            StudentDTO studentDTO = convertToStudentDTO(student);
            return ResponseEntity.ok(studentDTO);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener el estudiante"));
        }
    }

    /**
     * Obtiene todos los estudiantes registrados en el sistema.
     *
     * @return lista de estudiantes y cantidad total
     */
    @GetMapping
    public ResponseEntity<?> getAllStudents() {
        try {
            List<Student> students = studentService.getAllStudents();
            List<StudentDTO> studentDTOs = students.stream()
                    .map(this::convertToStudentDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(Map.of(
                    "students", studentDTOs,
                    "count", studentDTOs.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener la lista de estudiantes"));
        }
    }

    /**
     * Actualiza la información de un estudiante existente.
     *
     * @param id          identificador del estudiante
     * @param studentDTO  datos actualizados del estudiante
     * @return el estudiante actualizado o un mensaje de error
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable String id, @RequestBody StudentDTO studentDTO) {
        try {
            Student student = convertToStudent(studentDTO);
            Student updatedStudent = studentService.updateStudent(id, student);
            StudentDTO responseDTO = convertToStudentDTO(updatedStudent);
            return ResponseEntity.ok(responseDTO);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al actualizar el estudiante"));
        }
    }

    /**
     * Elimina un estudiante por su identificador.
     *
     * @param id identificador del estudiante a eliminar
     * @return mensaje de confirmación o error
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable String id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.ok(Map.of(
                    "message", "Estudiante eliminado exitosamente",
                    "deletedId", id
            ));
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al eliminar el estudiante"));
        }
    }

    /**
     * Busca estudiantes por su programa académico.
     *
     * @param program nombre del programa académico
     * @return lista de estudiantes pertenecientes al programa
     */
    @GetMapping("/program/{program}")
    public ResponseEntity<?> findByAcademicProgram(@PathVariable String program) {
        try {
            List<Student> students = studentService.findByAcademicProgram(program);
            List<StudentDTO> studentDTOs = students.stream()
                    .map(this::convertToStudentDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(Map.of(
                    "program", program,
                    "students", studentDTOs,
                    "count", studentDTOs.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al buscar estudiantes por programa académico"));
        }
    }

    /**
     * Busca estudiantes por semestre.
     *
     * @param semester número del semestre académico
     * @return lista de estudiantes en el semestre especificado
     */
    @GetMapping("/semester/{semester}")
    public ResponseEntity<?> findBySemester(@PathVariable Integer semester) {
        try {
            List<Student> students = studentService.findBySemester(semester);
            List<StudentDTO> studentDTOs = students.stream()
                    .map(this::convertToStudentDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(Map.of(
                    "semester", semester,
                    "students", studentDTOs,
                    "count", studentDTOs.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al buscar estudiantes por semestre"));
        }
    }

    /**
     * Busca estudiantes cuyo promedio sea mayor que el valor especificado.
     *
     * @param grade promedio mínimo requerido
     * @return lista de estudiantes con promedio mayor al indicado
     */
    @GetMapping("/grade-average/greater-than/{grade}")
    public ResponseEntity<?> findByGradeAverageGreaterThan(@PathVariable Double grade) {
        try {
            List<Student> students = studentService.findByGradeAverageGreaterThan(grade);
            List<StudentDTO> studentDTOs = students.stream()
                    .map(this::convertToStudentDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(Map.of(
                    "minGrade", grade,
                    "students", studentDTOs,
                    "count", studentDTOs.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al buscar estudiantes por promedio"));
        }
    }

    /**
     * Obtiene todos los estudiantes activos y calcula su porcentaje respecto al total.
     *
     * @return lista de estudiantes activos, cantidad y porcentaje
     */
    @GetMapping("/active")
    public ResponseEntity<?> getActiveStudents() {
        try {
            List<Student> allStudents = studentService.getAllStudents();
            List<Student> activeStudents = allStudents.stream()
                    .filter(Student::isActive)
                    .collect(Collectors.toList());
            List<StudentDTO> activeStudentDTOs = activeStudents.stream()
                    .map(this::convertToStudentDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "activeStudents", activeStudentDTOs,
                    "count", activeStudentDTOs.size(),
                    "activePercentage", allStudents.isEmpty() ?
                            0 : (double) activeStudentDTOs.size() / allStudents.size() * 100
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener estudiantes activos"));
        }
    }

    /**
     * Genera estadísticas generales de los estudiantes del sistema.
     * Incluye promedios, cantidad de activos, inactivos y agrupaciones.
     *
     * @return mapa con estadísticas globales
     */
    @GetMapping("/statistics")
    public ResponseEntity<?> getStudentStatistics() {
        try {
            List<Student> allStudents = studentService.getAllStudents();

            long activeCount = allStudents.stream().filter(Student::isActive).count();
            long inactiveCount = allStudents.size() - activeCount;

            double averageGrade = allStudents.stream()
                    .filter(s -> s.getGradeAverage() != null)
                    .mapToDouble(Student::getGradeAverage)
                    .average()
                    .orElse(0.0);

            Map<String, Long> studentsByProgram = allStudents.stream()
                    .collect(Collectors.groupingBy(Student::getAcademicProgram, Collectors.counting()));

            Map<Integer, Long> studentsBySemester = allStudents.stream()
                    .filter(s -> s.getSemester() != null)
                    .collect(Collectors.groupingBy(Student::getSemester, Collectors.counting()));

            return ResponseEntity.ok(Map.of(
                    "totalStudents", allStudents.size(),
                    "activeStudents", activeCount,
                    "inactiveStudents", inactiveCount,
                    "averageGrade", Math.round(averageGrade * 100.0) / 100.0,
                    "studentsByProgram", studentsByProgram,
                    "studentsBySemester", studentsBySemester
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener estadísticas de estudiantes"));
        }
    }

    /**
     * Permite realizar búsquedas dinámicas de estudiantes según múltiples filtros opcionales.
     *
     * @param program  programa académico (opcional)
     * @param semester semestre (opcional)
     * @param minGrade promedio mínimo (opcional)
     * @param active   estado de actividad (opcional)
     * @return lista de estudiantes filtrados según los criterios ingresados
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchStudents(
            @RequestParam(required = false) String program,
            @RequestParam(required = false) Integer semester,
            @RequestParam(required = false) Double minGrade,
            @RequestParam(required = false) Boolean active) {

        try {
            List<Student> allStudents = studentService.getAllStudents();
            List<Student> filteredStudents = allStudents.stream()
                    .filter(s -> program == null || program.equals(s.getAcademicProgram()))
                    .filter(s -> semester == null || semester.equals(s.getSemester()))
                    .filter(s -> minGrade == null || (s.getGradeAverage() != null && s.getGradeAverage() >= minGrade))
                    .filter(s -> active == null || active.equals(s.isActive()))
                    .collect(Collectors.toList());
            List<StudentDTO> filteredDTOs = filteredStudents.stream()
                    .map(this::convertToStudentDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "searchCriteria", Map.of(
                            "program", program,
                            "semester", semester,
                            "minGrade", minGrade,
                            "active", active
                    ),
                    "students", filteredDTOs,
                    "count", filteredDTOs.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error en la búsqueda de estudiantes"));
        }
    }

    /**
     * Endpoint para migrar todos los estudiantes existentes y asignarles el rol STUDENT
     * si no lo tienen.
     *
     * @return mensaje de confirmación de migración
     */
    @PostMapping("/migrate-roles")
    public ResponseEntity<?> migrateStudentRoles() {
        try {
            List<Student> allStudents = studentService.getAllStudents();
            int updatedCount = 0;

            for (Student student : allStudents) {
                if (student.getRole() == null) {
                    student.setRole(UserRole.STUDENT);
                    studentService.updateStudent(student.getId(), student);
                    updatedCount++;
                }
            }

            return ResponseEntity.ok(Map.of(
                    "message", "Migración de roles completada",
                    "studentsUpdated", updatedCount,
                    "totalStudents", allStudents.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error durante la migración: " + e.getMessage()));
        }
    }

    /**
     * Convierte un objeto DTO a entidad {@link Student}.
     *
     * @param studentDTO datos del estudiante
     * @return entidad {@link Student}
     */
    private Student convertToStudent(StudentDTO studentDTO) {
        // Usar el constructor que incluye el rol
        Student student = new Student(
                studentDTO.getId(),
                studentDTO.getName(),
                studentDTO.getEmail(),
                studentDTO.getPassword(),
                studentDTO.getAcademicProgram(),
                studentDTO.getSemester()
        );

        // Establecer propiedades adicionales que no están en el constructor
        if (studentDTO.getGradeAverage() != null) {
            student.setGradeAverage(studentDTO.getGradeAverage());
        }
        if (studentDTO.getActive() != null) {
            student.setActive(studentDTO.getActive());
        }
        if (studentDTO.getCreatedAt() != null) {
            student.setCreatedAt(studentDTO.getCreatedAt());
        }
        if (studentDTO.getUpdatedAt() != null) {
            student.setUpdatedAt(studentDTO.getUpdatedAt());
        }

        // Asegurar que el rol esté establecido (por si acaso)
        if (student.getRole() == null) {
            student.setRole(UserRole.STUDENT);
        }

        return student;
    }

    /**
     * Convierte una entidad {@link Student} a un objeto {@link StudentDTO}.
     *
     * @param student entidad estudiante
     * @return objeto DTO del estudiante
     */
    private StudentDTO convertToStudentDTO(Student student) {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(student.getId());
        studentDTO.setName(student.getName());
        studentDTO.setEmail(student.getEmail());
        studentDTO.setPassword(student.getPassword());
        studentDTO.setRole("STUDENT"); // Asegurar que el rol se incluya siempre
        studentDTO.setActive(student.isActive());
        studentDTO.setCreatedAt(student.getCreatedAt());
        studentDTO.setUpdatedAt(student.getUpdatedAt());
        studentDTO.setAcademicProgram(student.getAcademicProgram());
        studentDTO.setSemester(student.getSemester());
        studentDTO.setGradeAverage(student.getGradeAverage());
        return studentDTO;
    }
}
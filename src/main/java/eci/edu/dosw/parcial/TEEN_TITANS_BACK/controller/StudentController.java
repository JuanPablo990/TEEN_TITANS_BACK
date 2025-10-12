package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto.UserDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Student;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.service.StudentService;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador REST para gestionar operaciones CRUD y consultas avanzadas sobre estudiantes.
 *
 * @author Equipo Teen Titans
 * @version 1.1
 * @since 2025
 */
@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    private final StudentService studentService;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param studentService Servicio de estudiantes.
     */
    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * Crea un nuevo estudiante.
     *
     * @param student Objeto {@link Student} con los datos del estudiante.
     * @return Estudiante creado.
     */
    @PostMapping
    public ResponseEntity<?> createStudent(@RequestBody Student student) {
        try {
            Student createdStudent = studentService.createStudent(student);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al crear el estudiante: " + e.getMessage()));
        }
    }

    /**
     * Obtiene un estudiante por su ID.
     *
     * @param id Identificador del estudiante.
     * @return Estudiante encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable String id) {
        try {
            Student student = studentService.getStudentById(id);
            return ResponseEntity.ok(student);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener el estudiante"));
        }
    }

    /**
     * Obtiene la lista de todos los estudiantes registrados.
     *
     * @return Lista de estudiantes y su conteo total.
     */
    @GetMapping
    public ResponseEntity<?> getAllStudents() {
        try {
            List<Student> students = studentService.getAllStudents();
            return ResponseEntity.ok(Map.of(
                    "students", students,
                    "count", students.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener la lista de estudiantes"));
        }
    }

    /**
     * Actualiza la información de un estudiante existente.
     *
     * @param id      ID del estudiante.
     * @param student Datos actualizados.
     * @return Estudiante actualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable String id, @RequestBody Student student) {
        try {
            Student updatedStudent = studentService.updateStudent(id, student);
            return ResponseEntity.ok(updatedStudent);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al actualizar el estudiante"));
        }
    }

    /**
     * Elimina un estudiante por su ID.
     *
     * @param id ID del estudiante a eliminar.
     * @return Mensaje de confirmación.
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
     * Busca estudiantes por programa académico.
     *
     * @param program Nombre del programa académico.
     * @return Estudiantes del programa especificado.
     */
    @GetMapping("/program/{program}")
    public ResponseEntity<?> findByAcademicProgram(@PathVariable String program) {
        try {
            List<Student> students = studentService.findByAcademicProgram(program);
            return ResponseEntity.ok(Map.of(
                    "program", program,
                    "students", students,
                    "count", students.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al buscar estudiantes por programa académico"));
        }
    }

    /**
     * Busca estudiantes por semestre.
     *
     * @param semester Semestre académico.
     * @return Estudiantes del semestre indicado.
     */
    @GetMapping("/semester/{semester}")
    public ResponseEntity<?> findBySemester(@PathVariable Integer semester) {
        try {
            List<Student> students = studentService.findBySemester(semester);
            return ResponseEntity.ok(Map.of(
                    "semester", semester,
                    "students", students,
                    "count", students.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al buscar estudiantes por semestre"));
        }
    }

    /**
     * Busca estudiantes con promedio mayor al valor especificado.
     *
     * @param grade Promedio mínimo.
     * @return Estudiantes con promedio mayor al indicado.
     */
    @GetMapping("/grade-average/greater-than/{grade}")
    public ResponseEntity<?> findByGradeAverageGreaterThan(@PathVariable Double grade) {
        try {
            List<Student> students = studentService.findByGradeAverageGreaterThan(grade);
            return ResponseEntity.ok(Map.of(
                    "minGrade", grade,
                    "students", students,
                    "count", students.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al buscar estudiantes por promedio"));
        }
    }

    /**
     * Obtiene los estudiantes activos en el sistema.
     *
     * @return Estudiantes activos, su cantidad y porcentaje sobre el total.
     */
    @GetMapping("/active")
    public ResponseEntity<?> getActiveStudents() {
        try {
            List<Student> allStudents = studentService.getAllStudents();
            List<Student> activeStudents = allStudents.stream()
                    .filter(Student::getActive)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "activeStudents", activeStudents,
                    "count", activeStudents.size(),
                    "activePercentage", allStudents.isEmpty() ?
                            0 : (double) activeStudents.size() / allStudents.size() * 100
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener estudiantes activos"));
        }
    }

    /**
     * Obtiene estadísticas generales sobre los estudiantes.
     *
     * @return Estadísticas agrupadas por programa, semestre, promedio y estado.
     */
    @GetMapping("/statistics")
    public ResponseEntity<?> getStudentStatistics() {
        try {
            List<Student> allStudents = studentService.getAllStudents();

            long activeCount = allStudents.stream().filter(Student::getActive).count();
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
     * Convierte un estudiante a un objeto {@link UserDTO}.
     *
     * @param id ID del estudiante.
     * @return DTO con información del estudiante.
     */
    @GetMapping("/{id}/user-dto")
    public ResponseEntity<?> getStudentAsUserDTO(@PathVariable String id) {
        try {
            Student student = studentService.getStudentById(id);
            UserDTO userDTO = convertToUserDTO(student);
            return ResponseEntity.ok(userDTO);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al convertir estudiante a UserDTO"));
        }
    }

    /**
     * Realiza una búsqueda avanzada de estudiantes por múltiples criterios.
     *
     * @param program  Programa académico (opcional).
     * @param semester Semestre (opcional).
     * @param minGrade Promedio mínimo (opcional).
     * @param active   Estado activo (opcional).
     * @return Lista filtrada de estudiantes.
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
                    .filter(s -> active == null || active.equals(s.getActive()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "searchCriteria", Map.of(
                            "program", program,
                            "semester", semester,
                            "minGrade", minGrade,
                            "active", active
                    ),
                    "students", filteredStudents,
                    "count", filteredStudents.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error en la búsqueda de estudiantes"));
        }
    }

    /**
     * Convierte una entidad {@link Student} en un {@link UserDTO}.
     *
     * @param student Estudiante a convertir.
     * @return Objeto {@link UserDTO}.
     */
    private UserDTO convertToUserDTO(Student student) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(student.getId());
        userDTO.setName(student.getName());
        userDTO.setEmail(student.getEmail());
        userDTO.setPassword(student.getPassword());
        userDTO.setRole("STUDENT");
        userDTO.setActive(student.getActive());
        userDTO.setCreatedAt(student.getCreatedAt());
        userDTO.setUpdatedAt(student.getUpdatedAt());
        userDTO.setAcademicProgram(student.getAcademicProgram());
        userDTO.setSemester(student.getSemester());
        userDTO.setGradeAverage(student.getGradeAverage());
        return userDTO;
    }

    /**
     * DTO para actualizar un estudiante.
     */
    public static class StudentUpdateRequest {
        private String name;
        private String email;
        private String academicProgram;
        private Integer semester;
        private Double gradeAverage;
        private Boolean active;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getAcademicProgram() { return academicProgram; }
        public void setAcademicProgram(String academicProgram) { this.academicProgram = academicProgram; }
        public Integer getSemester() { return semester; }
        public void setSemester(Integer semester) { this.semester = semester; }
        public Double getGradeAverage() { return gradeAverage; }
        public void setGradeAverage(Double gradeAverage) { this.gradeAverage = gradeAverage; }
        public Boolean getActive() { return active; }
        public void setActive(Boolean active) { this.active = active; }
    }

    /**
     * DTO para crear un estudiante.
     */
    public static class StudentCreateRequest {
        private String name;
        private String email;
        private String password;
        private String academicProgram;
        private Integer semester;
        private Double gradeAverage;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getAcademicProgram() { return academicProgram; }
        public void setAcademicProgram(String academicProgram) { this.academicProgram = academicProgram; }
        public Integer getSemester() { return semester; }
        public void setSemester(Integer semester) { this.semester = semester; }
        public Double getGradeAverage() { return gradeAverage; }
        public void setGradeAverage(Double gradeAverage) { this.gradeAverage = gradeAverage; }
    }
}

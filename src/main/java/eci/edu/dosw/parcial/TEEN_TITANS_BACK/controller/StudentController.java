package eci.edu.dosw.parcial.TEEN_TITANS_BACK.controller;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto.StudentDTO;
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

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

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

    @GetMapping("/active")
    public ResponseEntity<?> getActiveStudents() {
        try {
            List<Student> allStudents = studentService.getAllStudents();
            List<Student> activeStudents = allStudents.stream()
                    .filter(Student::isActive) // Usar isActive() porque es boolean
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
                    .filter(s -> active == null || active.equals(s.isActive())) // Usar isActive()
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

    private Student convertToStudent(StudentDTO studentDTO) {
        Student student = new Student();
        student.setId(studentDTO.getId());
        student.setName(studentDTO.getName());
        student.setEmail(studentDTO.getEmail());
        student.setPassword(studentDTO.getPassword());
        // El rol se setea a STUDENT en el servicio, pero por si acaso lo seteamos aquí también
        // student.setRole(UserRole.STUDENT); // No hay setRole en Student? Sí, hereda de User.
        // Pero en el servicio de creación se setea a STUDENT. Aquí no lo seteamos para no pisar.
        student.setActive(studentDTO.getActive() != null ? studentDTO.getActive() : true);
        // Fechas: si no vienen, se setean en el servicio o en la entidad.
        student.setAcademicProgram(studentDTO.getAcademicProgram());
        student.setSemester(studentDTO.getSemester());
        student.setGradeAverage(studentDTO.getGradeAverage());
        return student;
    }

    private StudentDTO convertToStudentDTO(Student student) {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(student.getId());
        studentDTO.setName(student.getName());
        studentDTO.setEmail(student.getEmail());
        // No incluir password en el DTO de respuesta por seguridad
        studentDTO.setRole("STUDENT");
        studentDTO.setActive(student.isActive()); // Usar isActive() porque es boolean
        studentDTO.setCreatedAt(student.getCreatedAt());
        studentDTO.setUpdatedAt(student.getUpdatedAt());
        studentDTO.setAcademicProgram(student.getAcademicProgram());
        studentDTO.setSemester(student.getSemester());
        studentDTO.setGradeAverage(student.getGradeAverage());
        return studentDTO;
    }
}
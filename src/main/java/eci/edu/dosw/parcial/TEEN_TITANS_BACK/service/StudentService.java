package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Student;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de estudiantes del sistema.
 * Proporciona operaciones CRUD y métodos de búsqueda para estudiantes.
 */
@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    /**
     * Crea un nuevo estudiante en el sistema.
     *
     * @param student Estudiante a crear
     * @return Estudiante creado
     * @throws AppException si el email ya está registrado
     */
    public Student createStudent(Student student) {
        List<Student> existingStudents = studentRepository.findByEmail(student.getEmail());
        if (!existingStudents.isEmpty()) {
            throw new AppException("El email ya está registrado: " + student.getEmail());
        }
        return studentRepository.save(student);
    }

    /**
     * Obtiene un estudiante por su ID.
     *
     * @param id ID del estudiante
     * @return Estudiante encontrado
     * @throws AppException si no se encuentra el estudiante
     */
    public Student getStudentById(String id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new AppException("Estudiante no encontrado con ID: " + id));
    }

    /**
     * Obtiene todos los estudiantes del sistema.
     *
     * @return Lista de todos los estudiantes
     */
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    /**
     * Actualiza la información de un estudiante existente.
     *
     * @param id ID del estudiante a actualizar
     * @param student Nuevos datos del estudiante
     * @return Estudiante actualizado
     * @throws AppException si el estudiante no existe o el email está en uso
     */
    public Student updateStudent(String id, Student student) {
        if (!studentRepository.existsById(id)) {
            throw new AppException("Estudiante no encontrado con ID: " + id);
        }

        List<Student> existingStudentsWithEmail = studentRepository.findByEmail(student.getEmail());
        if (!existingStudentsWithEmail.isEmpty() &&
                !existingStudentsWithEmail.get(0).getId().equals(id)) {
            throw new AppException("El email ya está en uso por otro estudiante: " + student.getEmail());
        }

        student.setId(id);
        return studentRepository.save(student);
    }

    /**
     * Elimina un estudiante del sistema.
     *
     * @param id ID del estudiante a eliminar
     * @throws AppException si el estudiante no existe
     */
    public void deleteStudent(String id) {
        if (!studentRepository.existsById(id)) {
            throw new AppException("Estudiante no encontrado con ID: " + id);
        }
        studentRepository.deleteById(id);
    }

    /**
     * Busca un estudiante por su email.
     *
     * @param email Email del estudiante
     * @return Optional con el estudiante encontrado
     */
    public Optional<Student> findStudentByEmail(String email) {
        List<Student> students = studentRepository.findByEmail(email);
        return students.isEmpty() ? Optional.empty() : Optional.of(students.get(0));
    }

    /**
     * Busca estudiantes por programa académico.
     *
     * @param program Programa académico
     * @return Lista de estudiantes del programa
     */
    public List<Student> findByAcademicProgram(String program) {
        return studentRepository.findByAcademicProgram(program);
    }

    /**
     * Busca estudiantes por semestre.
     *
     * @param semester Semestre
     * @return Lista de estudiantes del semestre
     */
    public List<Student> findBySemester(Integer semester) {
        return studentRepository.findBySemester(semester);
    }

    /**
     * Busca estudiantes con promedio mayor al especificado.
     *
     * @param grade Promedio mínimo
     * @return Lista de estudiantes que cumplen el criterio
     */
    public List<Student> findByGradeAverageGreaterThan(Double grade) {
        return studentRepository.findByGradeAverageGreaterThan(grade);
    }

    /**
     * Busca estudiantes por programa académico y semestre.
     *
     * @param program Programa académico
     * @param semester Semestre
     * @return Lista de estudiantes que cumplen los criterios
     */
    public List<Student> findByAcademicProgramAndSemester(String program, Integer semester) {
        return studentRepository.findByAcademicProgramAndSemester(program, semester);
    }

    /**
     * Busca estudiantes por rango de semestres.
     *
     * @param minSemester Semestre mínimo
     * @param maxSemester Semestre máximo
     * @return Lista de estudiantes en el rango de semestres
     */
    public List<Student> findBySemesterBetween(Integer minSemester, Integer maxSemester) {
        return studentRepository.findBySemesterBetween(minSemester, maxSemester);
    }

    /**
     * Busca estudiantes por rango de promedios.
     *
     * @param minGrade Promedio mínimo
     * @param maxGrade Promedio máximo
     * @return Lista de estudiantes en el rango de promedios
     */
    public List<Student> findByGradeAverageBetween(Double minGrade, Double maxGrade) {
        return studentRepository.findByGradeAverageBetween(minGrade, maxGrade);
    }

    /**
     * Busca estudiantes destacados con promedio mayor al especificado.
     *
     * @param minGradeAverage Promedio mínimo para considerarse destacado
     * @return Lista de estudiantes destacados
     */
    public List<Student> findTopStudents(Double minGradeAverage) {
        return studentRepository.findTopStudents(minGradeAverage);
    }

    /**
     * Busca estudiantes por nombre (búsqueda parcial sin distinguir mayúsculas/minúsculas).
     *
     * @param name Texto del nombre a buscar
     * @return Lista de estudiantes que coinciden con el nombre
     */
    public List<Student> findByNameContaining(String name) {
        return studentRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Cuenta estudiantes por programa académico.
     *
     * @param program Programa académico
     * @return Número de estudiantes en el programa
     */
    public long countByAcademicProgram(String program) {
        return studentRepository.countByAcademicProgram(program);
    }

    /**
     * Actualiza el promedio de calificaciones de un estudiante.
     *
     * @param id ID del estudiante
     * @param newGrade Nuevo promedio
     * @return Estudiante actualizado
     * @throws AppException si no se encuentra el estudiante
     */
    public Student updateStudentGrade(String id, Double newGrade) {
        Student student = getStudentById(id);
        student.setGradeAverage(newGrade);
        return studentRepository.save(student);
    }
}
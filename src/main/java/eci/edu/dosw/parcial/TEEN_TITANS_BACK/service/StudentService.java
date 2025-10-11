package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Student;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar las operaciones CRUD y consultas personalizadas
 * de estudiantes en el sistema.
 *
 * <p>Este servicio implementa la lógica de negocio para la creación, obtención,
 * actualización y eliminación de estudiantes, además de búsquedas personalizadas
 * basadas en criterios académicos.</p>
 *
 * @author Equipo Teen Titans
 * @version 1.1
 * @since 2025
 */
@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    /**
     * Crea un nuevo estudiante en el sistema.
     *
     * @param student Estudiante a crear.
     * @return El estudiante creado.
     */
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    /**
     * Obtiene un estudiante por su ID.
     *
     * @param id ID del estudiante.
     * @return El estudiante encontrado.
     * @throws AppException si no se encuentra el estudiante.
     */
    public Student getStudentById(String id) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent()) {
            return student.get();
        } else {
            throw new AppException("Estudiante no encontrado con ID: " + id);
        }
    }

    /**
     * Obtiene todos los estudiantes del sistema.
     *
     * @return Lista de todos los estudiantes.
     */
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    /**
     * Actualiza la información de un estudiante existente.
     *
     * @param id ID del estudiante a actualizar.
     * @param student Nuevos datos del estudiante.
     * @return El estudiante actualizado.
     * @throws AppException si no se encuentra el estudiante.
     */
    public Student updateStudent(String id, Student student) {
        Optional<Student> existingStudent = studentRepository.findById(id);
        if (existingStudent.isPresent()) {
            student.setId(id); // Asegura que se mantenga el mismo ID.
            return studentRepository.save(student);
        } else {
            throw new AppException("Estudiante no encontrado con ID: " + id);
        }
    }

    /**
     * Elimina un estudiante del sistema.
     *
     * @param id ID del estudiante a eliminar.
     * @throws AppException si no se encuentra el estudiante.
     */
    public void deleteStudent(String id) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent()) {
            studentRepository.deleteById(id);
        } else {
            throw new AppException("Estudiante no encontrado con ID: " + id);
        }
    }

    /**
     * Busca estudiantes por programa académico.
     *
     * @param program Programa académico a buscar.
     * @return Lista de estudiantes del programa especificado.
     */
    public List<Student> findByAcademicProgram(String program) {
        return studentRepository.findByAcademicProgram(program);
    }

    /**
     * Busca estudiantes por semestre actual.
     *
     * @param semester Semestre a buscar.
     * @return Lista de estudiantes del semestre especificado.
     */
    public List<Student> findBySemester(Integer semester) {
        return studentRepository.findBySemester(semester);
    }

    /**
     * Busca estudiantes con promedio mayor al especificado.
     *
     * @param grade Promedio mínimo (exclusivo).
     * @return Lista de estudiantes con promedio mayor al especificado.
     */
    public List<Student> findByGradeAverageGreaterThan(Double grade) {
        return studentRepository.findByGradeAverageGreaterThanEqual(grade)
                .stream()
                .filter(s -> s.getGradeAverage() > grade)
                .toList();
    }
}

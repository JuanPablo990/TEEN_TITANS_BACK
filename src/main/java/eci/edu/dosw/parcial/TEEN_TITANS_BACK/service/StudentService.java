package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Student;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public Student createStudent(Student student) {
        // CORREGIDO: Maneja List en lugar de Optional
        List<Student> existingStudents = studentRepository.findByEmail(student.getEmail());
        if (!existingStudents.isEmpty()) {
            throw new AppException("El email ya está registrado: " + student.getEmail());
        }
        return studentRepository.save(student);
    }

    public Student getStudentById(String id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new AppException("Estudiante no encontrado con ID: " + id));
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student updateStudent(String id, Student student) {
        if (!studentRepository.existsById(id)) {
            throw new AppException("Estudiante no encontrado con ID: " + id);
        }

        // CORREGIDO: Maneja List en lugar de Optional
        List<Student> existingStudentsWithEmail = studentRepository.findByEmail(student.getEmail());
        if (!existingStudentsWithEmail.isEmpty() &&
                !existingStudentsWithEmail.get(0).getId().equals(id)) {
            throw new AppException("El email ya está en uso por otro estudiante: " + student.getEmail());
        }

        student.setId(id);
        return studentRepository.save(student);
    }

    public void deleteStudent(String id) {
        if (!studentRepository.existsById(id)) {
            throw new AppException("Estudiante no encontrado con ID: " + id);
        }
        studentRepository.deleteById(id);
    }

    // CORREGIDO: Método para buscar por email que devuelve Optional
    public Optional<Student> findStudentByEmail(String email) {
        List<Student> students = studentRepository.findByEmail(email);
        return students.isEmpty() ? Optional.empty() : Optional.of(students.get(0));
    }

    // MÉTODOS ORIGINALES (sin cambios)
    public List<Student> findByAcademicProgram(String program) {
        return studentRepository.findByAcademicProgram(program);
    }

    public List<Student> findBySemester(Integer semester) {
        return studentRepository.findBySemester(semester);
    }

    public List<Student> findByGradeAverageGreaterThan(Double grade) {
        return studentRepository.findByGradeAverageGreaterThan(grade);
    }

    public List<Student> findByAcademicProgramAndSemester(String program, Integer semester) {
        return studentRepository.findByAcademicProgramAndSemester(program, semester);
    }

    public List<Student> findBySemesterBetween(Integer minSemester, Integer maxSemester) {
        return studentRepository.findBySemesterBetween(minSemester, maxSemester);
    }

    public List<Student> findByGradeAverageBetween(Double minGrade, Double maxGrade) {
        return studentRepository.findByGradeAverageBetween(minGrade, maxGrade);
    }

    public List<Student> findTopStudents(Double minGradeAverage) {
        return studentRepository.findTopStudents(minGradeAverage);
    }

    public List<Student> findByNameContaining(String name) {
        return studentRepository.findByNameContainingIgnoreCase(name);
    }

    public long countByAcademicProgram(String program) {
        return studentRepository.countByAcademicProgram(program);
    }

    public Student updateStudentGrade(String id, Double newGrade) {
        Student student = getStudentById(id);
        student.setGradeAverage(newGrade);
        return studentRepository.save(student);
    }
}
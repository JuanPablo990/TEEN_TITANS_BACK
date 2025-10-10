package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Student;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.StudentAcademicProgress;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.StudentRepository;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.StudentAcademicProgressRepository;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.CourseStatusDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar el semáforo académico de los estudiantes.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Service
public class TrafficLightService {

    private final StudentRepository studentRepository;
    private final StudentAcademicProgressRepository studentAcademicProgressRepository;
    private final CourseStatusDetailRepository courseStatusDetailRepository;

    /**
     * Constructor para inyección de dependencias.
     *
     * @param studentRepository Repositorio de estudiantes
     * @param studentAcademicProgressRepository Repositorio de progreso académico
     * @param courseStatusDetailRepository Repositorio de detalles de estado de cursos
     */
    @Autowired
    public TrafficLightService(StudentRepository studentRepository,
                               StudentAcademicProgressRepository studentAcademicProgressRepository,
                               CourseStatusDetailRepository courseStatusDetailRepository) {
        this.studentRepository = studentRepository;
        this.studentAcademicProgressRepository = studentAcademicProgressRepository;
        this.courseStatusDetailRepository = courseStatusDetailRepository;
    }

    /**
     * Obtiene el semáforo académico del estudiante.
     * El semáforo indica el estado académico del estudiante:
     * - VERDE: Buen rendimiento académico
     * - AMARILLO: Rendimiento académico regular (necesita mejorar)
     * - ROJO: Bajo rendimiento académico (en riesgo académico)
     *
     * @param studentId ID del estudiante
     * @return String que representa el color del semáforo ("GREEN", "YELLOW", "RED")
     * @throws RuntimeException si el estudiante no existe o no tiene progreso académico
     */
    public String getAcademicTrafficLight(String studentId) {
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        if (studentOptional.isEmpty()) {
            throw new RuntimeException("Estudiante no encontrado: " + studentId);
        }

        Student student = studentOptional.get();
        if (!student.getActive()) {
            return "RED"; // Estudiante inactivo
        }

        List<StudentAcademicProgress> progressList =
                studentAcademicProgressRepository.findByAcademicProgram(student.getAcademicProgram());

        StudentAcademicProgress progress = findProgressByStudent(progressList, studentId);

        if (progress == null) {
            throw new RuntimeException("Progreso académico no encontrado para el estudiante: " + studentId);
        }

        return calculateTrafficLight(progress, student);
    }

    /**
     * Obtiene la información completa del estudiante.
     *
     * @param studentId ID del estudiante
     * @return Objeto Student con la información del estudiante
     * @throws RuntimeException si el estudiante no existe
     */
    public Student getStudentInformation(String studentId) {
        Optional<Student> studentOptional = studentRepository.findById(studentId);

        if (studentOptional.isEmpty()) {
            throw new RuntimeException("Estudiante no encontrado: " + studentId);
        }

        Student student = studentOptional.get();
        if (!student.getActive()) {
            throw new RuntimeException("Estudiante inactivo: " + studentId);
        }

        return student;
    }

    /**
     * Obtiene el progreso curricular completo del estudiante.
     *
     * @param studentId ID del estudiante
     * @return Objeto StudentAcademicProgress con el progreso curricular
     * @throws RuntimeException si el progreso académico no existe
     */
    public StudentAcademicProgress getCurriculumProgress(String studentId) {
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        if (studentOptional.isEmpty()) {
            throw new RuntimeException("Estudiante no encontrado: " + studentId);
        }

        Student student = studentOptional.get();

        List<StudentAcademicProgress> progressList =
                studentAcademicProgressRepository.findByAcademicProgram(student.getAcademicProgram());

        StudentAcademicProgress progress = findProgressByStudent(progressList, studentId);

        if (progress == null) {
            throw new RuntimeException("Progreso curricular no encontrado para el estudiante: " + studentId);
        }

        return progress;
    }

    /**
     * Método auxiliar para encontrar el progreso académico de un estudiante específico
     * en una lista de progresos por programa académico.
     */
    private StudentAcademicProgress findProgressByStudent(List<StudentAcademicProgress> progressList, String studentId) {
        return progressList.stream()
                .filter(progress -> progress.getStudent() != null &&
                        progress.getStudent().getId().equals(studentId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Calcula el color del semáforo académico basado en el progreso del estudiante.
     */
    private String calculateTrafficLight(StudentAcademicProgress progress, Student student) {
        Double gpa = progress.getCumulativeGPA() != null ? progress.getCumulativeGPA() : 0.0;
        Integer completedCredits = progress.getCompletedCredits() != null ? progress.getCompletedCredits() : 0;
        Integer currentSemester = progress.getCurrentSemester() != null ? progress.getCurrentSemester() : 1;
        Integer totalCreditsRequired = progress.getTotalCreditsRequired() != null ? progress.getTotalCreditsRequired() : 1;

        int expectedCreditsPerSemester = totalCreditsRequired / progress.getTotalSemesters();
        int expectedCredits = currentSemester * expectedCreditsPerSemester;

        long failedCoursesCount = getFailedCoursesCount(student.getId());

        boolean hasExcellentGPA = gpa >= 4.0; // Escala de 5.0
        boolean hasGoodGPA = gpa >= 3.0;
        boolean hasMinimumGPA = gpa >= 2.5;
        boolean hasGoodCreditProgress = completedCredits >= expectedCredits;
        boolean hasMinimumCreditProgress = completedCredits >= (expectedCredits * 0.7);
        boolean hasNoRecentFailures = failedCoursesCount == 0;
        boolean hasFewFailures = failedCoursesCount <= 1;

        if (hasExcellentGPA && hasGoodCreditProgress && hasNoRecentFailures) {
            return "GREEN";
        } else if (hasGoodGPA && hasMinimumCreditProgress && hasFewFailures) {
            return "GREEN";
        } else if (hasMinimumGPA && hasMinimumCreditProgress) {
            return "YELLOW";
        } else {
            return "RED";
        }
    }

    /**
     * Obtiene el número de cursos reprobados en el último semestre.
     */
    private long getFailedCoursesCount(String studentId) {

        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isEmpty()) return 0;

        Integer currentSemester = student.get().getSemester();
        if (currentSemester == null) return 0;


        String currentSemesterStr = currentSemester.toString();


        return courseStatusDetailRepository.findBySemester(currentSemesterStr)
                .stream()
                .filter(course -> !course.isApproved() &&
                        course.getGrade() != null &&
                        course.getGrade() < 3.0) // Nota menor a 3.0 es reprobado
                .count();
    }

    /**
     * Método adicional: Obtiene el porcentaje de avance curricular.
     *
     * @param studentId ID del estudiante
     * @return Porcentaje de avance (0.0 a 100.0)
     */
    public Double getCurriculumProgressPercentage(String studentId) {
        StudentAcademicProgress progress = getCurriculumProgress(studentId);

        if (progress.getTotalCreditsRequired() == null || progress.getTotalCreditsRequired() == 0) {
            return 0.0;
        }

        double percentage = ((double) progress.getCompletedCredits() / progress.getTotalCreditsRequired()) * 100;
        return Math.min(100.0, Math.round(percentage * 100.0) / 100.0); // Redondear a 2 decimales
    }

    /**
     * Método adicional: Verifica si el estudiante está en riesgo académico.
     *
     * @param studentId ID del estudiante
     * @return true si el estudiante está en riesgo académico
     */
    public boolean isStudentAtAcademicRisk(String studentId) {
        String trafficLight = getAcademicTrafficLight(studentId);
        return "RED".equals(trafficLight);
    }

    /**
     * Método adicional: Obtiene un resumen completo del estado académico del estudiante.
     *
     * @param studentId ID del estudiante
     * @return String con el resumen del estado académico
     */
    public String getAcademicStatusSummary(String studentId) {
        Student student = getStudentInformation(studentId);
        StudentAcademicProgress progress = getCurriculumProgress(studentId);
        String trafficLight = getAcademicTrafficLight(studentId);
        Double progressPercentage = getCurriculumProgressPercentage(studentId);

        return String.format(
                "Estudiante: %s\n" +
                        "Programa: %s\n" +
                        "Semestre: %d\n" +
                        "GPA: %.2f\n" +
                        "Créditos completados: %d/%d (%.1f%%)\n" +
                        "Semáforo académico: %s\n" +
                        "Estado: %s",
                student.getName(),
                student.getAcademicProgram(),
                progress.getCurrentSemester(),
                progress.getCumulativeGPA(),
                progress.getCompletedCredits(),
                progress.getTotalCreditsRequired(),
                progressPercentage,
                trafficLight,
                getStatusDescription(trafficLight)
        );
    }

    /**
     * Obtiene la descripción del estado basado en el color del semáforo.
     */
    private String getStatusDescription(String trafficLight) {
        switch (trafficLight) {
            case "GREEN":
                return "Rendimiento académico satisfactorio";
            case "YELLOW":
                return "Rendimiento regular - necesita mejorar";
            case "RED":
                return "En riesgo académico - requiere atención inmediata";
            default:
                return "Estado desconocido";
        }
    }
}
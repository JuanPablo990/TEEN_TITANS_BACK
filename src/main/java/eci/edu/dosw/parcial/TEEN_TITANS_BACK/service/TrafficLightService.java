package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Student;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.StudentAcademicProgress;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.CourseStatusDetail;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.StudentRepository;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.StudentAcademicProgressRepository;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.CourseStatusDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar el semáforo académico de los estudiantes.
 * Evalúa el rendimiento académico y asigna colores (VERDE, AMARILLO, ROJO)
 * según el progreso curricular del estudiante.
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
     *
     * @param studentId ID del estudiante
     * @return String que representa el color del semáforo ("GREEN", "YELLOW", "RED")
     * @throws AppException si el estudiante no existe o no tiene progreso académico
     */
    public String getAcademicTrafficLight(String studentId) {
        Student student = getStudentInformation(studentId);

        if (!student.isActive()) {
            return "RED";
        }

        Optional<StudentAcademicProgress> progressOptional =
                studentAcademicProgressRepository.findByStudentId(studentId);

        if (progressOptional.isEmpty()) {
            throw new AppException("Progreso académico no encontrado para el estudiante: " + studentId);
        }

        StudentAcademicProgress progress = progressOptional.get();
        return calculateTrafficLight(progress, student);
    }

    /**
     * Obtiene la información completa del estudiante.
     *
     * @param studentId ID del estudiante
     * @return Objeto Student con la información del estudiante
     * @throws AppException si el estudiante no existe
     */
    public Student getStudentInformation(String studentId) {
        Optional<Student> studentOptional = studentRepository.findById(studentId);

        if (studentOptional.isEmpty()) {
            throw new AppException("Estudiante no encontrado: " + studentId);
        }

        Student student = studentOptional.get();
        if (!student.isActive()) {
            throw new AppException("Estudiante inactivo: " + studentId);
        }

        return student;
    }

    /**
     * Obtiene el progreso curricular completo del estudiante.
     *
     * @param studentId ID del estudiante
     * @return Objeto StudentAcademicProgress con el progreso curricular
     * @throws AppException si el progreso académico no existe
     */
    public StudentAcademicProgress getCurriculumProgress(String studentId) {
        Optional<StudentAcademicProgress> progressOptional =
                studentAcademicProgressRepository.findByStudentId(studentId);

        if (progressOptional.isEmpty()) {
            throw new AppException("Progreso curricular no encontrado para el estudiante: " + studentId);
        }

        return progressOptional.get();
    }

    /**
     * Obtiene el porcentaje de avance curricular.
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
        return Math.min(100.0, Math.round(percentage * 100.0) / 100.0);
    }

    /**
     * Verifica si el estudiante está en riesgo académico.
     *
     * @param studentId ID del estudiante
     * @return true si el estudiante está en riesgo académico
     */
    public boolean isStudentAtAcademicRisk(String studentId) {
        String trafficLight = getAcademicTrafficLight(studentId);
        return "RED".equals(trafficLight);
    }

    /**
     * Obtiene un resumen completo del estado académico del estudiante.
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
                "Estudiante: %s%n" +
                        "Programa: %s%n" +
                        "Semestre: %d%n" +
                        "GPA: %.2f%n" +
                        "Créditos completados: %d/%d (%.1f%%)%n" +
                        "Semáforo académico: %s%n" +
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
     * Obtiene estudiantes en riesgo académico por programa.
     *
     * @param academicProgram Programa académico
     * @return Lista de estudiantes en riesgo
     */
    public List<Student> getStudentsAtRiskByProgram(String academicProgram) {
        List<Student> students = studentRepository.findByAcademicProgramAndActive(academicProgram, true);

        return students.stream()
                .filter(student -> isStudentAtAcademicRisk(student.getId()))
                .toList();
    }

    /**
     * Obtiene estadísticas del semáforo académico por programa.
     *
     * @param academicProgram Programa académico
     * @return Array con [verde, amarillo, rojo] counts
     */
    public int[] getTrafficLightStatisticsByProgram(String academicProgram) {
        List<Student> students = studentRepository.findByAcademicProgramAndActive(academicProgram, true);

        int green = 0, yellow = 0, red = 0;

        for (Student student : students) {
            try {
                String trafficLight = getAcademicTrafficLight(student.getId());
                switch (trafficLight) {
                    case "GREEN": green++; break;
                    case "YELLOW": yellow++; break;
                    case "RED": red++; break;
                }
            } catch (AppException e) {
                red++;
            }
        }

        return new int[]{green, yellow, red};
    }

    /**
     * Obtiene el progreso académico con información del estudiante.
     *
     * @param studentId ID del estudiante
     * @return Objeto combinado con información del estudiante y progreso
     */
    public StudentProgressInfo getStudentProgressInfo(String studentId) {
        Student student = getStudentInformation(studentId);
        StudentAcademicProgress progress = getCurriculumProgress(studentId);
        String trafficLight = getAcademicTrafficLight(studentId);
        Double progressPercentage = getCurriculumProgressPercentage(studentId);

        return new StudentProgressInfo(student, progress, trafficLight, progressPercentage);
    }

    /**
     * Calcula el color del semáforo académico basado en el progreso del estudiante.
     *
     * @param progress Progreso académico del estudiante
     * @param student Información del estudiante
     * @return Color del semáforo ("GREEN", "YELLOW", "RED")
     */
    private String calculateTrafficLight(StudentAcademicProgress progress, Student student) {
        Double gpa = progress.getCumulativeGPA() != null ? progress.getCumulativeGPA() : 0.0;
        Integer completedCredits = progress.getCompletedCredits() != null ? progress.getCompletedCredits() : 0;
        Integer currentSemester = progress.getCurrentSemester() != null ? progress.getCurrentSemester() : 1;
        Integer totalCreditsRequired = progress.getTotalCreditsRequired() != null ? progress.getTotalCreditsRequired() : 1;
        Integer totalSemesters = progress.getTotalSemesters() != null ? progress.getTotalSemesters() : 1;

        int expectedCreditsPerSemester = totalCreditsRequired / totalSemesters;
        int expectedCredits = currentSemester * expectedCreditsPerSemester;

        long failedCoursesCount = getFailedCoursesCount(student.getId());

        boolean hasExcellentGPA = gpa >= 4.0;
        boolean hasGoodGPA = gpa >= 3.0;
        boolean hasMinimumGPA = gpa >= 2.5;
        boolean hasGoodCreditProgress = completedCredits >= expectedCredits;
        boolean hasMinimumCreditProgress = completedCredits >= (int) (expectedCredits * 0.7);
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
     *
     * @param studentId ID del estudiante
     * @return Número de cursos reprobados
     */
    private long getFailedCoursesCount(String studentId) {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isEmpty()) return 0;

        Integer currentSemester = student.get().getSemester();
        if (currentSemester == null) return 0;

        String currentSemesterStr = currentSemester.toString();

        List<CourseStatusDetail> currentSemesterCourses =
                courseStatusDetailRepository.findByStudentIdAndSemester(studentId, currentSemesterStr);

        return currentSemesterCourses.stream()
                .filter(course -> !course.getIsApproved())
                .count();
    }

    /**
     * Obtiene la descripción del estado basado en el color del semáforo.
     *
     * @param trafficLight Color del semáforo
     * @return Descripción del estado
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

    /**
     * Clase interna para contener información combinada del estudiante y progreso.
     */
    public static class StudentProgressInfo {
        private final Student student;
        private final StudentAcademicProgress progress;
        private final String trafficLight;
        private final Double progressPercentage;

        /**
         * Constructor para StudentProgressInfo.
         *
         * @param student Información del estudiante
         * @param progress Progreso académico
         * @param trafficLight Color del semáforo
         * @param progressPercentage Porcentaje de progreso
         */
        public StudentProgressInfo(Student student, StudentAcademicProgress progress,
                                   String trafficLight, Double progressPercentage) {
            this.student = student;
            this.progress = progress;
            this.trafficLight = trafficLight;
            this.progressPercentage = progressPercentage;
        }

        public Student getStudent() { return student; }
        public StudentAcademicProgress getProgress() { return progress; }
        public String getTrafficLight() { return trafficLight; }
        public Double getProgressPercentage() { return progressPercentage; }
    }
}
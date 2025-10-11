package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import java.util.List;

/**
 * DTO que representa el progreso académico de un estudiante, incluyendo información
 * general del estudiante, su avance en créditos y el estado de los cursos cursados.
 * <p>
 * Este objeto se utiliza para transferir datos consolidados del rendimiento del estudiante,
 * su programa académico, promedio acumulado y el detalle del avance en cada curso.
 * </p>
 *
 * @author Juan Pablo Nieto
 * @version 1.0
 */
public class StudentProgressDTO {

    // ===== Datos generales del progreso académico =====
    private String id;
    private String studentId;
    private String studentName;
    private String studentEmail;
    private String academicProgram;
    private String faculty;
    private String curriculumType;
    private Integer currentSemester;
    private Integer totalSemesters;
    private Integer completedCredits;
    private Integer totalCreditsRequired;
    private Double cumulativeGPA;
    private List<CourseProgressDTO> coursesStatus;

    /**
     * Clase interna que representa el progreso individual de un curso dentro del
     * historial académico del estudiante. Contiene información sobre el curso,
     * su estado, calificación, créditos obtenidos y observaciones.
     */
    public static class CourseProgressDTO {
        private String courseId;
        private String courseCode;
        private String courseName;
        private String status;
        private Double grade;
        private String semester;
        private Integer creditsEarned;
        private Boolean approved;
        private String comments;

        /**
         * Constructor vacío para facilitar la serialización y deserialización.
         */
        public CourseProgressDTO() {}

        /**
         * Constructor que inicializa un curso con sus atributos principales.
         *
         * @param courseId       Identificador único del curso.
         * @param courseCode     Código del curso.
         * @param courseName     Nombre del curso.
         * @param status         Estado actual del curso (por ejemplo: "APROBADO", "EN CURSO", "REPROBADO").
         * @param grade          Calificación obtenida por el estudiante.
         * @param semester       Semestre en el que se cursó la materia.
         * @param creditsEarned  Créditos obtenidos por el curso.
         * @param approved       Indica si el curso fue aprobado.
         * @param comments       Comentarios adicionales o notas sobre el curso.
         */
        public CourseProgressDTO(String courseId, String courseCode, String courseName, String status,
                                 Double grade, String semester, Integer creditsEarned,
                                 Boolean approved, String comments) {
            this.courseId = courseId;
            this.courseCode = courseCode;
            this.courseName = courseName;
            this.status = status;
            this.grade = grade;
            this.semester = semester;
            this.creditsEarned = creditsEarned;
            this.approved = approved;
            this.comments = comments;
        }


        public String getCourseId() { return courseId; }
        public void setCourseId(String courseId) { this.courseId = courseId; }

        public String getCourseCode() { return courseCode; }
        public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

        public String getCourseName() { return courseName; }
        public void setCourseName(String courseName) { this.courseName = courseName; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public Double getGrade() { return grade; }
        public void setGrade(Double grade) { this.grade = grade; }

        public String getSemester() { return semester; }
        public void setSemester(String semester) { this.semester = semester; }

        public Integer getCreditsEarned() { return creditsEarned; }
        public void setCreditsEarned(Integer creditsEarned) { this.creditsEarned = creditsEarned; }

        public Boolean getApproved() { return approved; }
        public void setApproved(Boolean approved) { this.approved = approved; }

        public String getComments() { return comments; }
        public void setComments(String comments) { this.comments = comments; }
    }

    /**
     * Constructor vacío por defecto.
     */
    public StudentProgressDTO() {}

    /**
     * Constructor que inicializa todos los campos principales del progreso académico del estudiante.
     *
     * @param id                    Identificador único del registro.
     * @param studentId             Identificador del estudiante.
     * @param studentName           Nombre completo del estudiante.
     * @param studentEmail          Correo institucional del estudiante.
     * @param academicProgram       Programa académico al que pertenece el estudiante.
     * @param faculty               Facultad a la que pertenece el programa.
     * @param curriculumType        Tipo de plan de estudios (por ejemplo: "Antiguo", "Nuevo").
     * @param currentSemester       Semestre actual del estudiante.
     * @param totalSemesters        Número total de semestres del programa.
     * @param completedCredits      Créditos aprobados hasta el momento.
     * @param totalCreditsRequired  Total de créditos requeridos para graduarse.
     * @param cumulativeGPA         Promedio acumulado del estudiante.
     * @param coursesStatus         Lista con el estado de los cursos cursados.
     */
    public StudentProgressDTO(String id, String studentId, String studentName, String studentEmail,
                              String academicProgram, String faculty, String curriculumType,
                              Integer currentSemester, Integer totalSemesters, Integer completedCredits,
                              Integer totalCreditsRequired, Double cumulativeGPA,
                              List<CourseProgressDTO> coursesStatus) {
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.academicProgram = academicProgram;
        this.faculty = faculty;
        this.curriculumType = curriculumType;
        this.currentSemester = currentSemester;
        this.totalSemesters = totalSemesters;
        this.completedCredits = completedCredits;
        this.totalCreditsRequired = totalCreditsRequired;
        this.cumulativeGPA = cumulativeGPA;
        this.coursesStatus = coursesStatus;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }

    public String getAcademicProgram() { return academicProgram; }
    public void setAcademicProgram(String academicProgram) { this.academicProgram = academicProgram; }

    public String getFaculty() { return faculty; }
    public void setFaculty(String faculty) { this.faculty = faculty; }

    public String getCurriculumType() { return curriculumType; }
    public void setCurriculumType(String curriculumType) { this.curriculumType = curriculumType; }

    public Integer getCurrentSemester() { return currentSemester; }
    public void setCurrentSemester(Integer currentSemester) { this.currentSemester = currentSemester; }

    public Integer getTotalSemesters() { return totalSemesters; }
    public void setTotalSemesters(Integer totalSemesters) { this.totalSemesters = totalSemesters; }

    public Integer getCompletedCredits() { return completedCredits; }
    public void setCompletedCredits(Integer completedCredits) { this.completedCredits = completedCredits; }

    public Integer getTotalCreditsRequired() { return totalCreditsRequired; }
    public void setTotalCreditsRequired(Integer totalCreditsRequired) { this.totalCreditsRequired = totalCreditsRequired; }

    public Double getCumulativeGPA() { return cumulativeGPA; }
    public void setCumulativeGPA(Double cumulativeGPA) { this.cumulativeGPA = cumulativeGPA; }

    public List<CourseProgressDTO> getCoursesStatus() { return coursesStatus; }
    public void setCoursesStatus(List<CourseProgressDTO> coursesStatus) { this.coursesStatus = coursesStatus; }



    /**
     * Agrega un nuevo curso al historial de progreso del estudiante.
     *
     * @param courseProgress Objeto {@link CourseProgressDTO} que contiene el detalle del curso.
     */
    public void addCourseProgress(CourseProgressDTO courseProgress) {
        if (this.coursesStatus == null) {
            this.coursesStatus = new java.util.ArrayList<>();
        }
        this.coursesStatus.add(courseProgress);
    }

    /**
     * Calcula el porcentaje de créditos completados en relación al total requerido.
     *
     * @return Porcentaje de progreso académico (entre 0 y 100).
     */
    public Double getProgressPercentage() {
        if (totalCreditsRequired == null || totalCreditsRequired == 0) {
            return 0.0;
        }
        return (completedCredits.doubleValue() / totalCreditsRequired.doubleValue()) * 100;
    }

    /**
     * Verifica si el estudiante se encuentra actualmente en su último semestre.
     *
     * @return {@code true} si el estudiante está en el último semestre; de lo contrario, {@code false}.
     */
    public Boolean isFinalSemester() {
        return currentSemester != null && totalSemesters != null &&
                currentSemester.equals(totalSemesters);
    }
}

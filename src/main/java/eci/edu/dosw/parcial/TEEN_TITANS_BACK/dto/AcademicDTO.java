package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import java.util.Date;
import java.util.List;

/**
 * Representa un objeto de transferencia de datos (DTO) que unifica información relacionada
 * con un curso académico, incluyendo datos de curso, horario, solicitud de cambio de horario,
 * detalles del estado del curso, grupo y aula de clase.
 */
public class AcademicDTO {

    private String courseCode;
    private String courseName;
    private Integer courseCredits;
    private String courseDescription;
    private String academicProgram;
    private Boolean isCourseActive;

    private String scheduleId;
    private String dayOfWeek;
    private String startHour;
    private String endHour;
    private String period;

    private String requestId;
    private String studentId;
    private String currentGroupId;
    private String requestedGroupId;
    private String changeReason;
    private String requestStatus;
    private Date submissionDate;
    private Date resolutionDate;
    private List<ReviewStepDTO> reviewHistory;

    private String courseStatusDetailId;
    private String courseStatus;
    private Double grade;
    private String semester;
    private Date enrollmentDate;
    private Date completionDate;
    private Integer creditsEarned;
    private Boolean isApproved;
    private String comments;

    private String groupId;
    private String section;

    private String classroomId;
    private String building;
    private String roomNumber;
    private Integer capacity;
    private String roomType;

    /**
     * Representa una etapa de revisión dentro del historial de una solicitud de cambio de horario.
     */
    public static class ReviewStepDTO {
        private String userId;
        private String userRole;
        private String action;
        private Date timestamp;
        private String comments;

        /**
         * Constructor vacío por defecto.
         */
        public ReviewStepDTO() {}

        /**
         * Crea una nueva instancia de ReviewStepDTO.
         *
         * @param userId   Identificador del usuario que realiza la acción.
         * @param userRole Rol del usuario.
         * @param action   Acción realizada.
         * @param comments Comentarios adicionales.
         */
        public ReviewStepDTO(String userId, String userRole, String action, String comments) {
            this.userId = userId;
            this.userRole = userRole;
            this.action = action;
            this.comments = comments;
            this.timestamp = new Date();
        }

        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getUserRole() { return userRole; }
        public void setUserRole(String userRole) { this.userRole = userRole; }
        public String getAction() { return action; }
        public void setAction(String action) { this.action = action; }
        public Date getTimestamp() { return timestamp; }
        public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
        public String getComments() { return comments; }
        public void setComments(String comments) { this.comments = comments; }
    }

    /**
     * Constructor vacío por defecto.
     */
    public AcademicDTO() {}

    /**
     * Crea una instancia completa de AcademicDTO con todos los campos principales.
     *
     * @param courseCode Código del curso.
     * @param courseName Nombre del curso.
     * @param courseCredits Créditos del curso.
     * @param courseDescription Descripción del curso.
     * @param academicProgram Programa académico al que pertenece el curso.
     * @param isCourseActive Indica si el curso está activo.
     * @param scheduleId Identificador del horario.
     * @param dayOfWeek Día de la semana.
     * @param startHour Hora de inicio.
     * @param endHour Hora de finalización.
     * @param period Periodo académico.
     * @param requestId Identificador de la solicitud de cambio de horario.
     * @param studentId Identificador del estudiante solicitante.
     * @param currentGroupId Grupo actual del estudiante.
     * @param requestedGroupId Grupo solicitado.
     * @param changeReason Motivo del cambio.
     * @param requestStatus Estado de la solicitud.
     * @param submissionDate Fecha de envío de la solicitud.
     * @param resolutionDate Fecha de resolución de la solicitud.
     * @param courseStatusDetailId Identificador del detalle del estado del curso.
     * @param courseStatus Estado actual del curso.
     * @param grade Nota obtenida.
     * @param semester Semestre académico.
     * @param enrollmentDate Fecha de inscripción.
     * @param completionDate Fecha de finalización.
     * @param creditsEarned Créditos obtenidos.
     * @param isApproved Indica si el curso fue aprobado.
     * @param comments Comentarios adicionales.
     * @param groupId Identificador del grupo.
     * @param section Sección del grupo.
     * @param classroomId Identificador del aula.
     * @param building Edificio donde se dicta el curso.
     * @param roomNumber Número del aula.
     * @param capacity Capacidad del aula.
     * @param roomType Tipo de sala.
     */
    public AcademicDTO(String courseCode, String courseName, Integer courseCredits,
                       String courseDescription, String academicProgram, Boolean isCourseActive,
                       String scheduleId, String dayOfWeek, String startHour, String endHour, String period,
                       String requestId, String studentId, String currentGroupId, String requestedGroupId,
                       String changeReason, String requestStatus, Date submissionDate, Date resolutionDate,
                       String courseStatusDetailId, String courseStatus, Double grade, String semester,
                       Date enrollmentDate, Date completionDate, Integer creditsEarned, Boolean isApproved,
                       String comments, String groupId, String section, String classroomId, String building,
                       String roomNumber, Integer capacity, String roomType) {

        this.courseCode = courseCode;
        this.courseName = courseName;
        this.courseCredits = courseCredits;
        this.courseDescription = courseDescription;
        this.academicProgram = academicProgram;
        this.isCourseActive = isCourseActive;
        this.scheduleId = scheduleId;
        this.dayOfWeek = dayOfWeek;
        this.startHour = startHour;
        this.endHour = endHour;
        this.period = period;
        this.requestId = requestId;
        this.studentId = studentId;
        this.currentGroupId = currentGroupId;
        this.requestedGroupId = requestedGroupId;
        this.changeReason = changeReason;
        this.requestStatus = requestStatus;
        this.submissionDate = submissionDate;
        this.resolutionDate = resolutionDate;
        this.courseStatusDetailId = courseStatusDetailId;
        this.courseStatus = courseStatus;
        this.grade = grade;
        this.semester = semester;
        this.enrollmentDate = enrollmentDate;
        this.completionDate = completionDate;
        this.creditsEarned = creditsEarned;
        this.isApproved = isApproved;
        this.comments = comments;
        this.groupId = groupId;
        this.section = section;
        this.classroomId = classroomId;
        this.building = building;
        this.roomNumber = roomNumber;
        this.capacity = capacity;
        this.roomType = roomType;
    }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public Integer getCourseCredits() { return courseCredits; }
    public void setCourseCredits(Integer courseCredits) { this.courseCredits = courseCredits; }
    public String getCourseDescription() { return courseDescription; }
    public void setCourseDescription(String courseDescription) { this.courseDescription = courseDescription; }
    public String getAcademicProgram() { return academicProgram; }
    public void setAcademicProgram(String academicProgram) { this.academicProgram = academicProgram; }
    public Boolean getIsCourseActive() { return isCourseActive; }
    public void setIsCourseActive(Boolean isCourseActive) { this.isCourseActive = isCourseActive; }
    public String getScheduleId() { return scheduleId; }
    public void setScheduleId(String scheduleId) { this.scheduleId = scheduleId; }
    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public String getStartHour() { return startHour; }
    public void setStartHour(String startHour) { this.startHour = startHour; }
    public String getEndHour() { return endHour; }
    public void setEndHour(String endHour) { this.endHour = endHour; }
    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }
    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getCurrentGroupId() { return currentGroupId; }
    public void setCurrentGroupId(String currentGroupId) { this.currentGroupId = currentGroupId; }
    public String getRequestedGroupId() { return requestedGroupId; }
    public void setRequestedGroupId(String requestedGroupId) { this.requestedGroupId = requestedGroupId; }
    public String getChangeReason() { return changeReason; }
    public void setChangeReason(String changeReason) { this.changeReason = changeReason; }
    public String getRequestStatus() { return requestStatus; }
    public void setRequestStatus(String requestStatus) { this.requestStatus = requestStatus; }
    public Date getSubmissionDate() { return submissionDate; }
    public void setSubmissionDate(Date submissionDate) { this.submissionDate = submissionDate; }
    public Date getResolutionDate() { return resolutionDate; }
    public void setResolutionDate(Date resolutionDate) { this.resolutionDate = resolutionDate; }
    public List<ReviewStepDTO> getReviewHistory() { return reviewHistory; }
    public void setReviewHistory(List<ReviewStepDTO> reviewHistory) { this.reviewHistory = reviewHistory; }
    public String getCourseStatusDetailId() { return courseStatusDetailId; }
    public void setCourseStatusDetailId(String courseStatusDetailId) { this.courseStatusDetailId = courseStatusDetailId; }
    public String getCourseStatus() { return courseStatus; }
    public void setCourseStatus(String courseStatus) { this.courseStatus = courseStatus; }
    public Double getGrade() { return grade; }
    public void setGrade(Double grade) { this.grade = grade; }
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
    public Date getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(Date enrollmentDate) { this.enrollmentDate = enrollmentDate; }
    public Date getCompletionDate() { return completionDate; }
    public void setCompletionDate(Date completionDate) { this.completionDate = completionDate; }
    public Integer getCreditsEarned() { return creditsEarned; }
    public void setCreditsEarned(Integer creditsEarned) { this.creditsEarned = creditsEarned; }
    public Boolean getIsApproved() { return isApproved; }
    public void setIsApproved(Boolean isApproved) { this.isApproved = isApproved; }
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }
    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }
    public String getClassroomId() { return classroomId; }
    public void setClassroomId(String classroomId) { this.classroomId = classroomId; }
    public String getBuilding() { return building; }
    public void setBuilding(String building) { this.building = building; }
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }

    /**
     * Agrega un nuevo paso de revisión al historial.
     *
     * @param reviewStep Objeto {@link ReviewStepDTO} que representa la acción de revisión a agregar.
     */
    public void addReviewStep(ReviewStepDTO reviewStep) {
        if (this.reviewHistory == null) {
            this.reviewHistory = new java.util.ArrayList<>();
        }
        this.reviewHistory.add(reviewStep);
    }
}

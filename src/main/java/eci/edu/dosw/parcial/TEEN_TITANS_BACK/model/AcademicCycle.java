package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

/**
 * Representa un ciclo académico de un estudiante, conteniendo información sobre
 * su progreso, calendario académico y desempeño durante el ciclo.
 */
public class AcademicCycle {
    private int cycleId;
    private int semesterToAttend;
    private int lastSemesterAttended;
    private String admissionCycle;
    private String academicSituation;
    private String programStatus;
    private String actionReason;
    private AcademicCalendar academicCalendar;
    private AcademicPerformance academicPerformance;
    private StudentProgress studentProgress;

    /**
     * Obtiene el identificador único del ciclo académico.
     *
     * @return Identificador del ciclo
     */
    public int getCycleId() {
        return cycleId;
    }

    /**
     * Obtiene el semestre que el estudiante debe atender.
     *
     * @return Semestre a atender
     */
    public int getSemesterToAttend() {
        return semesterToAttend;
    }

    /**
     * Obtiene el último semestre que el estudiante atendió.
     *
     * @return Último semestre atendido
     */
    public int getLastSemesterAttended() {
        return lastSemesterAttended;
    }

    /**
     * Obtiene el ciclo de admisión del estudiante.
     *
     * @return Ciclo de admisión
     */
    public String getAdmissionCycle() {
        return admissionCycle;
    }

    /**
     * Obtiene la situación académica actual del estudiante.
     *
     * @return Situación académica
     */
    public String getAcademicSituation() {
        return academicSituation;
    }

    /**
     * Obtiene el estado del programa académico del estudiante.
     *
     * @return Estado del programa
     */
    public String getProgramStatus() {
        return programStatus;
    }

    /**
     * Obtiene la razón de acción o motivo especial del ciclo.
     *
     * @return Razón de acción
     */
    public String getActionReason() {
        return actionReason;
    }

    /**
     * Obtiene el calendario académico asociado al ciclo.
     *
     * @return Calendario académico del ciclo
     */
    public AcademicCalendar getAcademicCalendar() {
        return academicCalendar;
    }

    /**
     * Obtiene el desempeño académico del estudiante en el ciclo.
     *
     * @return Desempeño académico
     */
    public AcademicPerformance getAcademicPerformance() {
        return academicPerformance;
    }

    /**
     * Obtiene el progreso estudiantil en el ciclo académico.
     *
     * @return Progreso del estudiante
     */
    public StudentProgress getStudentProgress() {
        return studentProgress;
    }
}
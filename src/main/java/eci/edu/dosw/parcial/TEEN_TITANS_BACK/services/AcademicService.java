package eci.edu.dosw.parcial.TEEN_TITANS_BACK.services;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dtos.AcademicDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Subject;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.AcademicCycle;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.AcademicPerformance;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.StudentProgress;
import java.util.List;
import java.util.ArrayList;

/**
 * Servicio que gestiona la información académica de los estudiantes,
 * incluyendo horarios, historial académico, promedios y situación académica.
 */
public class AcademicService {

    /**
     * Obtiene el panel académico (dashboard) del estudiante.
     * Este incluye horario actual, historial, semáforo académico,
     * progreso en créditos/cursos, promedio acumulado y situación académica.
     *
     * @param studentId Identificador único del estudiante
     * @return Objeto {@link AcademicDTO} con la información del estudiante
     */
    public AcademicDTO getStudentDashboard(String studentId) {
        AcademicDTO dashboard = new AcademicDTO();

        dashboard.setCurrentSchedule(getCurrentSchedule(studentId));
        dashboard.setAcademicHistory(getAcademicHistory(studentId));
        dashboard.setAcademicTrafficLight(getAcademicTrafficLight(studentId));
        dashboard.setStudentProgress(getStudentProgress(studentId));

        AcademicPerformance performance = getAcademicPerformance(studentId);
        if (performance != null) {
            dashboard.setCumulativeAverage(performance.getCumulativeAverage());
        }

        AcademicCycle currentCycle = getCurrentAcademicCycle(studentId);
        if (currentCycle != null) {
            dashboard.setAcademicSituation(currentCycle.getAcademicSituation());
        }

        return dashboard;
    }

    /**
     * Obtiene el horario actual del estudiante.
     *
     * @param studentId Identificador único del estudiante
     * @return Lista de asignaturas ({@link Subject}) del ciclo actual
     */
    public List<Subject> getCurrentSchedule(String studentId) {
        return new ArrayList<>();
    }

    /**
     * Obtiene el historial académico completo del estudiante,
     * organizado por ciclos académicos.
     *
     * @param studentId Identificador único del estudiante
     * @return Lista de ciclos académicos ({@link AcademicCycle}) cursados
     */
    public List<AcademicCycle> getAcademicHistory(String studentId) {
        return new ArrayList<>();
    }

    /**
     * Determina el estado del estudiante según su promedio acumulado,
     * representado en un semáforo académico.
     * <ul>
     *   <li>VERDE: promedio mayor o igual a 4.0</li>
     *   <li>AMARILLO: promedio entre 3.0 y 3.9</li>
     *   <li>ROJO: promedio menor a 3.0</li>
     * </ul>
     *
     * @param studentId Identificador único del estudiante
     * @return Estado del semáforo académico (VERDE, AMARILLO o ROJO)
     */
    public String getAcademicTrafficLight(String studentId) {
        AcademicPerformance performance = getAcademicPerformance(studentId);
        if (performance == null || performance.getCumulativeAverage() == null) {
            return "AMARILLO";
        }

        float average = performance.getCumulativeAverage();
        if (average >= 4.0f) {
            return "VERDE";
        } else if (average >= 3.0f) {
            return "AMARILLO";
        } else {
            return "ROJO";
        }
    }

    /**
     * Obtiene el rendimiento académico acumulado del estudiante.
     *
     * @param studentId Identificador único del estudiante
     * @return Objeto {@link AcademicPerformance} con el rendimiento académico, o null si no existe
     */
    private AcademicPerformance getAcademicPerformance(String studentId) {
        return null;
    }

    /**
     * Obtiene el progreso del estudiante en créditos y cursos.
     *
     * @param studentId Identificador único del estudiante
     * @return Objeto {@link StudentProgress} con el progreso académico, o null si no existe
     */
    private StudentProgress getStudentProgress(String studentId) {
        return null;
    }

    /**
     * Obtiene el ciclo académico actual del estudiante.
     *
     * @param studentId Identificador único del estudiante
     * @return Objeto {@link AcademicCycle} con el ciclo académico, o null si no existe
     */
    private AcademicCycle getCurrentAcademicCycle(String studentId) {
        return null;
    }
}

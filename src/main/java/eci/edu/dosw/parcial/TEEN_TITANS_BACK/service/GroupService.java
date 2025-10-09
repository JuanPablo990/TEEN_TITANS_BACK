package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Course;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.ScheduleChangeRequest;

import java.util.List;
import java.util.Map;

/**
 * Servicio abstracto que define la estructura base para la gestión de grupos académicos.
 * <p>
 * Esta clase establece los métodos esenciales que deben ser implementados por
 * los servicios que manejen información relacionada con grupos, cursos y
 * sus inscripciones. Proporciona una interfaz estándar para consultar
 * datos de capacidad, matrículas y listas de espera.
 * </p>
 *
 * @author Equipo Teen Titans
 * @version 1.0
 */
public abstract class GroupService {

    /**
     * Obtiene el curso asociado a un grupo específico.
     *
     * @param groupId identificador único del grupo.
     * @return el objeto {@link Course} correspondiente al grupo, o {@code null} si no existe.
     */
    public abstract Course getCourse(String groupId);

    /**
     * Obtiene la capacidad máxima del aula asignada al grupo.
     *
     * @param groupId identificador único del grupo.
     * @return la capacidad máxima del grupo, o {@code 0} si no se encuentra información.
     */
    public abstract Integer getMaxCapacity(String groupId);

    /**
     * Calcula el número actual de estudiantes matriculados en el grupo.
     *
     * @param groupId identificador único del grupo.
     * @return el número actual de estudiantes inscritos en el grupo.
     */
    public abstract Integer getCurrentEnrollment(String groupId);

    /**
     * Devuelve la lista de solicitudes en espera asociadas al grupo.
     * <p>
     * Las solicitudes de cambio de horario representan estudiantes que desean
     * ingresar al grupo o modificar su horario actual.
     * </p>
     *
     * @param groupId identificador único del grupo.
     * @return una lista de {@link ScheduleChangeRequest} correspondientes a la lista de espera.
     */
    public abstract List<ScheduleChangeRequest> getWaitingList(String groupId);

    /**
     * Obtiene un mapa con el total de estudiantes inscritos por grupo
     * dentro de un curso específico.
     * <p>
     * La clave del mapa corresponde al identificador del grupo, y el valor
     * al número de estudiantes actualmente matriculados en dicho grupo.
     * </p>
     *
     * @param courseCode código del curso académico.
     * @return un mapa con los identificadores de grupo y sus respectivos totales de inscripción.
     */
    public abstract Map<String, Integer> getTotalEnrolledByCourse(String courseCode);
}

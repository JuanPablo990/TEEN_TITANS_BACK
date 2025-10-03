package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import java.util.Date;

/**
 * Representa una solicitud académica realizada por un estudiante para cambios de materia,
 * grupo u otros procesos académicos.
 */
public class Request {
    private String id;
    private RequestType type;
    private Student student;
    private Subject originalSubject;
    private Subject targetSubject;
    private int originalGroup;
    private int targetGroup;
    private RequestStatus status;
    private int priority;
    private Date creationDate;
    private Date resolutionDate;
    private String observations;

    /**
     * Obtiene el identificador único de la solicitud.
     *
     * @return Identificador de la solicitud
     */
    public String getId() {
        return id;
    }

    /**
     * Obtiene el tipo de solicitud académica.
     *
     * @return Tipo de solicitud
     */
    public RequestType getType() {
        return type;
    }

    /**
     * Obtiene el estudiante que realizó la solicitud.
     *
     * @return Estudiante solicitante
     */
    public Student getStudent() {
        return student;
    }

    /**
     * Obtiene la materia original de la solicitud.
     *
     * @return Materia original
     */
    public Subject getOriginalSubject() {
        return originalSubject;
    }

    /**
     * Obtiene la materia objetivo de la solicitud.
     *
     * @return Materia objetivo
     */
    public Subject getTargetSubject() {
        return targetSubject;
    }

    /**
     * Obtiene el grupo original de la solicitud.
     *
     * @return Grupo original
     */
    public int getOriginalGroup() {
        return originalGroup;
    }

    /**
     * Obtiene el grupo objetivo de la solicitud.
     *
     * @return Grupo objetivo
     */
    public int getTargetGroup() {
        return targetGroup;
    }

    /**
     * Obtiene el estado actual de la solicitud.
     *
     * @return Estado de la solicitud
     */
    public RequestStatus getStatus() {
        return status;
    }

    /**
     * Obtiene la prioridad de la solicitud.
     *
     * @return Prioridad de la solicitud
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Obtiene la fecha de creación de la solicitud.
     *
     * @return Fecha de creación
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Obtiene la fecha de resolución de la solicitud.
     *
     * @return Fecha de resolución
     */
    public Date getSolveDate() {
        return resolutionDate;
    }

    /**
     * Obtiene las observaciones de la solicitud.
     *
     * @return Observaciones de la solicitud
     */
    public String getObservations() {
        return observations;
    }
}
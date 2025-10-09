package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

/**
 * Representa un aula o sala de clases en el sistema académico.
 * Esta clase encapsula la información física de un espacio destinado a la impartición de clases,
 * incluyendo su ubicación, capacidad y tipo.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
public class Classroom {
    private String classroomId;
    private String building;
    private String roomNumber;
    private Integer capacity;
    private RoomType roomType;

    /**
     * Obtiene el identificador único del aula.
     *
     * @return el ID del aula como String
     */
    public String getClassroomId() {
        return classroomId;
    }

    /**
     * Obtiene el edificio donde se encuentra ubicado el aula.
     *
     * @return el nombre del edificio como String
     */
    public String getBuilding() {
        return building;
    }

    /**
     * Obtiene el número de la sala o aula dentro del edificio.
     *
     * @return el número de la sala como String
     */
    public String getRoomNumber() {
        return roomNumber;
    }

    /**
     * Obtiene la capacidad máxima de estudiantes del aula.
     *
     * @return la capacidad del aula como Integer
     */
    public Integer getCapacity() {
        return capacity;
    }

    /**
     * Obtiene el tipo de aula (ej: laboratorio, auditorio, sala regular).
     *
     * @return el tipo de aula como String
     */
    public RoomType getRoomType() {
        return roomType;
    }
}

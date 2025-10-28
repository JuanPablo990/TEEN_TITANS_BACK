package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.RoomType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Representa un aula o sala de clases en el sistema académico.
 * Esta clase encapsula la información física de un espacio destinado a la impartición de clases,
 * incluyendo su ubicación, capacidad y tipo.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Document(collection = "classrooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Classroom {
    @Id
    private String classroomId;
    private String building;
    private String roomNumber;
    private Integer capacity;
    private RoomType roomType;

    /**
     * Constructor sin el classroomId, para cuando MongoDB genera automáticamente el ID.
     *
     * @param building Edificio donde se encuentra el aula
     * @param roomNumber Número de la sala o aula dentro del edificio
     * @param capacity Capacidad máxima de estudiantes del aula
     * @param roomType Tipo de aula (ej: laboratorio, auditorio, sala regular)
     */
    public Classroom(String building, String roomNumber, Integer capacity, RoomType roomType) {
        this.building = building;
        this.roomNumber = roomNumber;
        this.capacity = capacity;
        this.roomType = roomType;
    }
}
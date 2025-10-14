package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

/**
 * Representa un profesor en el sistema académico.
 * Esta clase encapsula la información profesional de un profesor, incluyendo
 * su departamento, estatus de titularidad y áreas de especialización.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Document(collection = "professors")
public class Professor extends User{
    private String department;
    private Boolean isTenured;
    private List<String> areasOfExpertise;

    /**
     * Constructor vacío. Requerido para frameworks que necesitan instanciar la clase sin parámetros.
     */
    public Professor() {
    }

    /**
     * Constructor con todos los parámetros.
     *
     * @param department Departamento académico al que pertenece el profesor
     * @param isTenured Indica si el profesor es titular
     * @param areasOfExpertise Lista de áreas de especialización del profesor
     */
    public Professor(String department, Boolean isTenured, List<String> areasOfExpertise) {
        this.department = department;
        this.isTenured = isTenured;
        this.areasOfExpertise = areasOfExpertise;
    }

    /**
     * Obtiene el departamento académico al que pertenece el profesor.
     *
     * @return el departamento como String
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Verifica si el profesor tiene estatus de titular (tenured).
     *
     * @return true si el profesor es titular, false en caso contrario
     */
    public Boolean isTenured() {
        return isTenured;
    }

    /**
     * Obtiene la lista de áreas de especialización del profesor.
     *
     * @return la lista de áreas de especialización como List<String>
     */
    public List<String> getAreasOfExpertise() {
        return areasOfExpertise;
    }
}
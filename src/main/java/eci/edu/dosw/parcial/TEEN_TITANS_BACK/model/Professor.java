package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

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
public class Professor {
    private String department;
    private Boolean isTenured;
    private List<String> areasOfExpertise;

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
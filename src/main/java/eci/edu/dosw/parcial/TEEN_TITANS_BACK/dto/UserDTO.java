package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import java.util.Date;
import java.util.List;

/**
 * DTO unificado para representar información de usuarios del sistema:
 * User, Student, Professor, Dean y Administrator.
 */
public class UserDTO {

    private String id;
    private String name;
    private String email;
    private String password;
    private String role;
    private Boolean active;
    private Date createdAt;
    private Date updatedAt;
    private String academicProgram;
    private Integer semester;
    private Double gradeAverage;
    private String professorDepartment;
    private Boolean isTenured;
    private List<String> areasOfExpertise;
    private String faculty;
    private String officeLocation;
    private String adminDepartment;

    /**
     * Constructor vacío.
     */
    public UserDTO() {}

    /**
     * Constructor básico del usuario.
     *
     * @param id identificador del usuario
     * @param name nombre del usuario
     * @param email correo electrónico
     * @param password contraseña
     * @param role rol del usuario
     * @param active estado activo
     * @param createdAt fecha de creación
     * @param updatedAt fecha de actualización
     */
    public UserDTO(String id, String name, String email, String password,
                   String role, Boolean active, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Constructor completo del usuario.
     *
     * @param id identificador del usuario
     * @param name nombre del usuario
     * @param email correo electrónico
     * @param password contraseña
     * @param role rol del usuario
     * @param active estado activo
     * @param createdAt fecha de creación
     * @param updatedAt fecha de actualización
     * @param academicProgram programa académico del estudiante
     * @param semester semestre actual
     * @param gradeAverage promedio del estudiante
     * @param professorDepartment departamento del profesor
     * @param isTenured indica si es profesor de planta
     * @param areasOfExpertise áreas de conocimiento del profesor
     * @param faculty facultad del decano
     * @param officeLocation ubicación de la oficina del decano
     * @param adminDepartment departamento del administrador
     */
    public UserDTO(String id, String name, String email, String password, String role,
                   Boolean active, Date createdAt, Date updatedAt, String academicProgram,
                   Integer semester, Double gradeAverage, String professorDepartment,
                   Boolean isTenured, List<String> areasOfExpertise, String faculty,
                   String officeLocation, String adminDepartment) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.academicProgram = academicProgram;
        this.semester = semester;
        this.gradeAverage = gradeAverage;
        this.professorDepartment = professorDepartment;
        this.isTenured = isTenured;
        this.areasOfExpertise = areasOfExpertise;
        this.faculty = faculty;
        this.officeLocation = officeLocation;
        this.adminDepartment = adminDepartment;
    }

    /** @return identificador del usuario */
    public String getId() { return id; }

    /** @param id identificador del usuario */
    public void setId(String id) { this.id = id; }

    /** @return nombre del usuario */
    public String getName() { return name; }

    /** @param name nombre del usuario */
    public void setName(String name) { this.name = name; }

    /** @return correo electrónico */
    public String getEmail() { return email; }

    /** @param email correo electrónico */
    public void setEmail(String email) { this.email = email; }

    /** @return contraseña */
    public String getPassword() { return password; }

    /** @param password contraseña */
    public void setPassword(String password) { this.password = password; }

    /** @return rol del usuario */
    public String getRole() { return role; }

    /** @param role rol del usuario */
    public void setRole(String role) { this.role = role; }

    /** @return estado activo */
    public Boolean getActive() { return active; }

    /** @param active estado activo */
    public void setActive(Boolean active) { this.active = active; }

    /** @return fecha de creación */
    public Date getCreatedAt() { return createdAt; }

    /** @param createdAt fecha de creación */
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    /** @return fecha de actualización */
    public Date getUpdatedAt() { return updatedAt; }

    /** @param updatedAt fecha de actualización */
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    /** @return programa académico */
    public String getAcademicProgram() { return academicProgram; }

    /** @param academicProgram programa académico */
    public void setAcademicProgram(String academicProgram) { this.academicProgram = academicProgram; }

    /** @return semestre actual */
    public Integer getSemester() { return semester; }

    /** @param semester semestre actual */
    public void setSemester(Integer semester) { this.semester = semester; }

    /** @return promedio del estudiante */
    public Double getGradeAverage() { return gradeAverage; }

    /** @param gradeAverage promedio del estudiante */
    public void setGradeAverage(Double gradeAverage) { this.gradeAverage = gradeAverage; }

    /** @return departamento del profesor */
    public String getProfessorDepartment() { return professorDepartment; }

    /** @param professorDepartment departamento del profesor */
    public void setProfessorDepartment(String professorDepartment) { this.professorDepartment = professorDepartment; }

    /** @return indica si es profesor de planta */
    public Boolean getIsTenured() { return isTenured; }

    /** @param isTenured indica si es profesor de planta */
    public void setIsTenured(Boolean isTenured) { this.isTenured = isTenured; }

    /** @return áreas de conocimiento */
    public List<String> getAreasOfExpertise() { return areasOfExpertise; }

    /** @param areasOfExpertise áreas de conocimiento */
    public void setAreasOfExpertise(List<String> areasOfExpertise) { this.areasOfExpertise = areasOfExpertise; }

    /** @return facultad del decano */
    public String getFaculty() { return faculty; }

    /** @param faculty facultad del decano */
    public void setFaculty(String faculty) { this.faculty = faculty; }

    /** @return ubicación de la oficina */
    public String getOfficeLocation() { return officeLocation; }

    /** @param officeLocation ubicación de la oficina */
    public void setOfficeLocation(String officeLocation) { this.officeLocation = officeLocation; }

    /** @return departamento administrativo */
    public String getAdminDepartment() { return adminDepartment; }

    /** @param adminDepartment departamento administrativo */
    public void setAdminDepartment(String adminDepartment) { this.adminDepartment = adminDepartment; }

    /**
     * Agrega un área de especialización.
     *
     * @param area área de especialización
     */
    public void addAreaOfExpertise(String area) {
        if (this.areasOfExpertise == null) {
            this.areasOfExpertise = new java.util.ArrayList<>();
        }
        this.areasOfExpertise.add(area);
    }

    /**
     * Verifica si el usuario pertenece a un rol.
     *
     * @param role rol a verificar
     * @return true si coincide, false en caso contrario
     */
    public boolean isRole(String role) {
        return this.role != null && this.role.equals(role);
    }

    /**
     * Actualiza la fecha de modificación.
     */
    public void updateTimestamp() {
        this.updatedAt = new Date();
    }

    /**
     * Retorna una representación en texto del usuario.
     *
     * @return cadena descriptiva
     */
    @Override
    public String toString() {
        return "UnifiedUserDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", active=" + active +
                '}';
    }
}

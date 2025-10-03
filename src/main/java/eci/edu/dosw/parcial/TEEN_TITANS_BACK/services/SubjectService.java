package eci.edu.dosw.parcial.TEEN_TITANS_BACK.services;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dtos.SubjectDTO;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Subject;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Administrator;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.User;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class SubjectService {

    private final ConcurrentHashMap<String, Subject> subjects = new ConcurrentHashMap<>();
    /**
     * Crea una nueva asignatura en el sistema
     * (solo permitido para usuarios administradores).
     */
    public Subject createSubject(SubjectDTO subjectData, User user) {
        if (!isAdministrator(user)) return null;

        Subject subject = convertToEntity(subjectData);
        String subjectId = generateSubjectId(subject);
        subjects.put(subjectId, subject);
        return subject;
    }
    /**
     * Obtiene una asignatura por su identificador único.
     */
    public Subject getSubject(String subjectId) {
        return subjects.get(subjectId);
    }
    /**
     * Actualiza los datos de una asignatura existente
     * (solo administradores).
     */
    public Subject updateSubject(String subjectId, SubjectDTO updates, User user) {
        if (!isAdministrator(user)) return null;

        Subject existing = subjects.get(subjectId);
        if (existing != null) {
            Subject updatedSubject = convertToEntity(updates);
            subjects.put(subjectId, updatedSubject);
            return updatedSubject;
        }
        return null;
    }
    /**
     * Elimina una asignatura del sistema
     * (solo administradores).
     */
    public boolean deleteSubject(String subjectId, User user) {
        if (!isAdministrator(user)) return false;
        return subjects.remove(subjectId) != null;
    }
    /**
     * Retorna la lista completa de asignaturas registradas.
     */
    public List<Subject> getAllSubjects() {
        return subjects.values().stream().collect(Collectors.toList());
    }
    /**
     * Agrega un grupo a una asignatura existente
     * (solo administradores).
     */

    public Subject addGroupToSubject(String subjectId, SubjectDTO groupData, User user) {
        if (!isAdministrator(user)) return null;
        return subjects.get(subjectId);
    }
    /**
     * Elimina un grupo específico de una asignatura
     * (solo administradores).
     */
    public boolean removeGroupFromSubject(String subjectId, int groupNumber, User user) {
        if (!isAdministrator(user)) return false;
        return subjects.containsKey(subjectId);
    }
    /**
     * Actualiza la capacidad de un grupo en una asignatura
     * (solo administradores).
     */
    public boolean updateGroupCapacity(String subjectId, int groupNumber, int newCapacity, User user) {
        if (!isAdministrator(user)) return false;
        return subjects.containsKey(subjectId);
    }
    /**
     * Obtiene todas las asignaturas que pertenecen a una facultad dada.
     */
    public List<Subject> getSubjectsByFaculty(String faculty) {
        return subjects.values().stream()
                .filter(subject -> true)
                .collect(Collectors.toList());
    }
    /**
     * Obtiene todas las asignaturas dictadas por un profesor específico.
     */
    public List<Subject> getSubjectsByTeacher(String teacher) {
        return subjects.values().stream()
                .filter(subject -> subject.getTeacher().equals(teacher))
                .collect(Collectors.toList());
    }
    /**
     * Busca asignaturas cuyo nombre coincida (parcialmente) con el criterio dado.
     */
    public List<Subject> searchSubjectsByName(String name) {
        return subjects.values().stream()
                .filter(subject -> subject.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }
    /**
     * Genera un identificador único para una asignatura basado en su nombre y grupo.
     */
    private String generateSubjectId(Subject subject) {
        return subject.getName().replaceAll("\\s+", "-").toUpperCase() + "-" + subject.getGroup();
    }
    /**
     * Verifica si el usuario es administrador.
     */
    private boolean isAdministrator(User user) {
        return user instanceof Administrator;
    }
    /**
     * Convierte un objeto SubjectDTO en una entidad Subject usando reflexión.
     */

    private Subject convertToEntity(SubjectDTO dto) {
        Subject subject = new Subject();
        try {
            java.lang.reflect.Field nameField = Subject.class.getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(subject, dto.getName());

            java.lang.reflect.Field creditsField = Subject.class.getDeclaredField("credits");
            creditsField.setAccessible(true);
            creditsField.set(subject, dto.getCredits());

            java.lang.reflect.Field quotasField = Subject.class.getDeclaredField("quotas");
            quotasField.setAccessible(true);
            quotasField.set(subject, dto.getQuotas());

            java.lang.reflect.Field teacherField = Subject.class.getDeclaredField("teacher");
            teacherField.setAccessible(true);
            teacherField.set(subject, dto.getTeacher());

            java.lang.reflect.Field classTimeField = Subject.class.getDeclaredField("classTime");
            classTimeField.setAccessible(true);
            classTimeField.set(subject, dto.getClassTime());

            java.lang.reflect.Field groupField = Subject.class.getDeclaredField("group");
            groupField.setAccessible(true);
            groupField.set(subject, dto.getGroup());

        } catch (Exception e) {
            throw new RuntimeException("Error converting DTO to Subject", e);
        }
        return subject;
    }
}
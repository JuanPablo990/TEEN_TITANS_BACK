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

    public Subject createSubject(SubjectDTO subjectData, User user) {
        if (!isAdministrator(user)) return null;

        Subject subject = convertToEntity(subjectData);
        String subjectId = generateSubjectId(subject);
        subjects.put(subjectId, subject);
        return subject;
    }

    public Subject getSubject(String subjectId) {
        return subjects.get(subjectId);
    }

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

    public boolean deleteSubject(String subjectId, User user) {
        if (!isAdministrator(user)) return false;
        return subjects.remove(subjectId) != null;
    }

    public List<Subject> getAllSubjects() {
        return subjects.values().stream().collect(Collectors.toList());
    }

    public Subject addGroupToSubject(String subjectId, SubjectDTO groupData, User user) {
        if (!isAdministrator(user)) return null;
        return subjects.get(subjectId);
    }

    public boolean removeGroupFromSubject(String subjectId, int groupNumber, User user) {
        if (!isAdministrator(user)) return false;
        return subjects.containsKey(subjectId);
    }

    public boolean updateGroupCapacity(String subjectId, int groupNumber, int newCapacity, User user) {
        if (!isAdministrator(user)) return false;
        return subjects.containsKey(subjectId);
    }

    public List<Subject> getSubjectsByFaculty(String faculty) {
        return subjects.values().stream()
                .filter(subject -> true)
                .collect(Collectors.toList());
    }

    public List<Subject> getSubjectsByTeacher(String teacher) {
        return subjects.values().stream()
                .filter(subject -> subject.getTeacher().equals(teacher))
                .collect(Collectors.toList());
    }

    public List<Subject> searchSubjectsByName(String name) {
        return subjects.values().stream()
                .filter(subject -> subject.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    private String generateSubjectId(Subject subject) {
        return subject.getName().replaceAll("\\s+", "-").toUpperCase() + "-" + subject.getGroup();
    }

    private boolean isAdministrator(User user) {
        return user instanceof Administrator;
    }

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
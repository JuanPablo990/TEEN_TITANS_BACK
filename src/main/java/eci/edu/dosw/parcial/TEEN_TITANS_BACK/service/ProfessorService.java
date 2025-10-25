package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Professor;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.ProfessorRepository;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessorService {

    @Autowired
    private ProfessorRepository professorRepository;

    public Professor createProfessor(Professor professor) {
        if (professorRepository.existsByEmail(professor.getEmail())) {
            throw new AppException("El email ya está registrado: " + professor.getEmail());
        }

        professor.setRole(UserRole.PROFESSOR);
        return professorRepository.save(professor);
    }

    public Professor getProfessorById(String id) {
        return professorRepository.findById(id)
                .orElseThrow(() -> new AppException("Profesor no encontrado con ID: " + id));
    }

    public List<Professor> getAllProfessors() {
        return professorRepository.findAll();
    }

    public Professor updateProfessor(String id, Professor professor) {
        Professor existingProfessor = professorRepository.findById(id)
                .orElseThrow(() -> new AppException("Profesor no encontrado con ID: " + id));

        if (!existingProfessor.getEmail().equals(professor.getEmail())) {
            List<Professor> professorsWithEmail = professorRepository.findByEmail(professor.getEmail());
            if (!professorsWithEmail.isEmpty()) {
                throw new AppException("El email ya está en uso por otro profesor: " + professor.getEmail());
            }
        }

        professor.setId(id);
        professor.setRole(UserRole.PROFESSOR);

        return professorRepository.save(professor);
    }

    public void deleteProfessor(String id) {
        if (!professorRepository.existsById(id)) {
            throw new AppException("Profesor no encontrado con ID: " + id);
        }
        professorRepository.deleteById(id);
    }

    public List<Professor> findByDepartment(String department) {
        return professorRepository.findByDepartment(department);
    }

    public List<Professor> findByTenured(Boolean isTenured) {
        return professorRepository.findByIsTenured(isTenured);
    }

    public List<Professor> findByAreaOfExpertise(String area) {
        return professorRepository.findByAreasOfExpertiseContaining(area);
    }

    public List<Professor> findActiveProfessors() {
        return professorRepository.findByActive(true);
    }

    public List<Professor> findByDepartmentAndTenured(String department, Boolean isTenured) {
        return professorRepository.findByDepartmentAndIsTenured(department, isTenured);
    }

    public List<Professor> findByAreasOfExpertiseIn(List<String> areas) {
        return professorRepository.findByAreasOfExpertiseIn(areas);
    }

    public List<Professor> findByNameContaining(String name) {
        return professorRepository.findByNameContainingIgnoreCase(name);
    }

    // MÉTODO ELIMINADO porque no existe en el Repository
    // public List<Professor> findByDepartmentContaining(String department) {
    //     return professorRepository.findByDepartmentContainingIgnoreCase(department);
    // }

    // EN SU LUGAR, puedes usar este método con el Regex que SÍ existe:
    public List<Professor> findByDepartmentPattern(String departmentPattern) {
        return professorRepository.findByDepartmentRegex(departmentPattern);
    }

    public long countByDepartment(String department) {
        return professorRepository.countByDepartment(department);
    }

    public long countByTenured(Boolean isTenured) {
        return professorRepository.countByIsTenured(isTenured);
    }
}
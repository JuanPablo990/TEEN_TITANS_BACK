package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.Professor;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.UserRole;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.ProfessorRepository;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.exceptions.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfessorService extends UserService {

    @Autowired
    private ProfessorRepository professorRepository;

    /**
     * Crea un nuevo profesor en el sistema
     * @param professor Profesor a crear
     * @return Profesor creado
     * @throws AppException si el email ya está registrado
     */
    public Professor createProfessor(Professor professor) {
        // Validar que el email no exista
        if (userRepository.existsByEmail(professor.getEmail())) {
            throw new AppException("El email ya está registrado: " + professor.getEmail());
        }

        // Asegurar que el rol sea PROFESSOR
        professor.setRole(UserRole.PROFESSOR);

        return professorRepository.save(professor);
    }

    /**
     * Obtiene un profesor por su ID
     * @param id Identificador del profesor
     * @return Profesor encontrado
     * @throws AppException si no se encuentra el profesor
     */
    public Professor getProfessorById(String id) {
        return professorRepository.findById(id)
                .orElseThrow(() -> new AppException("Profesor no encontrado con ID: " + id));
    }

    /**
     * Obtiene todos los profesores del sistema
     * @return Lista de todos los profesores
     */
    public List<Professor> getAllProfessors() {
        return professorRepository.findAll();
    }

    /**
     * Actualiza la información de un profesor
     * @param id Identificador del profesor a actualizar
     * @param professor Nuevos datos del profesor
     * @return Profesor actualizado
     * @throws AppException si no se encuentra el profesor o el email ya está en uso
     */
    public Professor updateProfessor(String id, Professor professor) {
        // Verificar que el profesor existe
        Professor existingProfessor = professorRepository.findById(id)
                .orElseThrow(() -> new AppException("Profesor no encontrado con ID: " + id));

        // Verificar que el email no esté siendo usado por otro usuario
        if (!existingProfessor.getEmail().equals(professor.getEmail()) &&
                userRepository.existsByEmail(professor.getEmail())) {
            throw new AppException("El email ya está en uso por otro usuario: " + professor.getEmail());
        }

        // Establecer el ID y rol para asegurar la actualización correcta
        professor.setId(id);
        professor.setRole(UserRole.PROFESSOR);

        return professorRepository.save(professor);
    }

    /**
     * Elimina un profesor del sistema
     * @param id Identificador del profesor a eliminar
     * @throws AppException si no se encuentra el profesor
     */
    public void deleteProfessor(String id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new AppException("Profesor no encontrado con ID: " + id));
        professorRepository.deleteById(id);
    }

    /**
     * Busca profesores por departamento
     * @param department Departamento a buscar
     * @return Lista de profesores del departamento especificada
     */
    public List<Professor> findByDepartment(String department) {
        return professorRepository.findByDepartment(department);
    }

    /**
     * Busca profesores por titularidad
     * @param isTenured true para profesores titulares, false para no titulares
     * @return Lista de profesores según titularidad
     */
    public List<Professor> findByTenured(Boolean isTenured) {
        return professorRepository.findByIsTenured(isTenured);
    }

    /**
     * Busca profesores por área de especialización
     * @param area Área de especialización a buscar
     * @return Lista de profesores con la especialización especificada
     */
    public List<Professor> findByAreaOfExpertise(String area) {
        return professorRepository.findByAreasOfExpertiseContaining(area);
    }

    /**
     * Método específico para buscar profesores activos
     * @return Lista de profesores activos
     */
    public List<Professor> findActiveProfessors() {
        return userRepository.findByRoleAndActive(UserRole.PROFESSOR, true)
                .stream()
                .map(user -> (Professor) user)
                .toList();
    }
}

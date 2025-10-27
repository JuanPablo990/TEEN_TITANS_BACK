package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto.LoginRequest;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private DeanRepository deanRepository;

    @Autowired
    private AdministratorRepository administratorRepository;

    public Object login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        UserRole role = loginRequest.getRole();

        // Buscar usuario por email y verificar que esté activo
        Object user = findUserByEmailAndRole(email, role);

        if (user == null) {
            throw new RuntimeException("Usuario no encontrado o inactivo");
        }

        // Verificar contraseña
        if (!isPasswordValid(user, password)) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return user;
    }

    private Object findUserByEmailAndRole(String email, UserRole role) {
        switch (role) {
            case STUDENT:
                List<Student> students = studentRepository.findByEmail(email);
                return students.stream()
                        .filter(Student::isActive)
                        .findFirst()
                        .orElse(null);

            case PROFESSOR:
                List<Professor> professors = professorRepository.findByEmail(email);
                return professors.stream()
                        .filter(Professor::isActive)
                        .findFirst()
                        .orElse(null);

            case DEAN:
                List<Dean> deans = deanRepository.findByEmail(email);
                return deans.stream()
                        .filter(Dean::isActive)
                        .findFirst()
                        .orElse(null);

            case ADMINISTRATOR:
                List<Administrator> admins = administratorRepository.findByEmail(email);
                return admins.stream()
                        .filter(Administrator::isActive)
                        .findFirst()
                        .orElse(null);

            default:
                throw new RuntimeException("Rol no válido: " + role);
        }
    }

    private boolean isPasswordValid(Object user, String password) {
        if (user instanceof Student) {
            return ((Student) user).getPassword().equals(password);
        } else if (user instanceof Professor) {
            return ((Professor) user).getPassword().equals(password);
        } else if (user instanceof Dean) {
            return ((Dean) user).getPassword().equals(password);
        } else if (user instanceof Administrator) {
            return ((Administrator) user).getPassword().equals(password);
        }
        return false;
    }
}
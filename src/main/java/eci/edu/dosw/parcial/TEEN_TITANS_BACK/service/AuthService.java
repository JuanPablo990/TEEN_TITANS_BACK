package eci.edu.dosw.parcial.TEEN_TITANS_BACK.service;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto.LoginRequest;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.model.*;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio encargado de gestionar la autenticación de los diferentes tipos de usuarios del sistema.
 *
 * <p>Este servicio valida las credenciales de acceso de los usuarios (estudiantes, profesores,
 * decanos y administradores) y verifica su estado de actividad en la base de datos.</p>
 *
 * <p>El servicio actúa como capa intermedia entre el controlador de autenticación y los repositorios,
 * garantizando la lógica de negocio relacionada con el proceso de login.</p>
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025-10
 */
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

    /**
     * Realiza el proceso de autenticación del usuario.
     *
     * <p>Verifica que el usuario exista, esté activo y que la contraseña proporcionada sea válida.
     * Si el proceso es exitoso, devuelve el objeto del usuario autenticado.</p>
     *
     * @param loginRequest Objeto que contiene las credenciales y el rol del usuario.
     * @return El objeto del usuario autenticado (Student, Professor, Dean o Administrator).
     * @throws RuntimeException si el usuario no existe, está inactivo o la contraseña es incorrecta.
     */
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

    /**
     * Busca un usuario en el repositorio correspondiente según su rol y correo electrónico.
     *
     * @param email Correo electrónico del usuario a buscar.
     * @param role  Rol del usuario (STUDENT, PROFESSOR, DEAN, ADMINISTRATOR).
     * @return El usuario activo correspondiente o {@code null} si no se encuentra.
     * @throws RuntimeException si el rol proporcionado no es válido.
     */
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

    /**
     * Valida la contraseña de un usuario de acuerdo con su tipo de entidad.
     *
     * @param user     Objeto del usuario autenticado (Student, Professor, Dean o Administrator).
     * @param password Contraseña proporcionada por el cliente.
     * @return {@code true} si la contraseña es correcta, {@code false} en caso contrario.
     */
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

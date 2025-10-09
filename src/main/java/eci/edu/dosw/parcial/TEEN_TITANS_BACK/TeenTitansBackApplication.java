package eci.edu.dosw.parcial.TEEN_TITANS_BACK;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Clase principal que arranca la aplicación Spring Boot.
 * Escanea automáticamente los componentes, servicios, controladores y repositorios.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@SpringBootApplication(scanBasePackages = "eci.edu.dosw.parcial.TEEN_TITANS_BACK")
@EnableMongoRepositories(basePackages = "eci.edu.dosw.parcial.TEEN_TITANS_BACK.repository")
public class TeenTitansBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeenTitansBackApplication.class, args);
    }
}

package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import java.util.List;

@Document(collection = "professors")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Professor extends User {
    private String department;
    private Boolean isTenured;
    private List<String> areasOfExpertise;

    public Professor(String id, String name, String email, String password,
                     String department, Boolean isTenured, List<String> areasOfExpertise) {
        super(id, name, email, password, UserRole.PROFESSOR);
        this.department = department;
        this.isTenured = isTenured;
        this.areasOfExpertise = areasOfExpertise;
    }
}
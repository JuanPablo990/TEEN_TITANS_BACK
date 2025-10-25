package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;

@Document(collection = "administrators")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Administrator extends User {
    private String department;

    public Administrator(String id, String name, String email, String password, String department) {
        super(id, name, email, password, UserRole.ADMINISTRATOR);
        this.department = department;
    }
}
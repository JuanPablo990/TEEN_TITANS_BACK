package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;

@Document(collection = "deans")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Dean extends User {
    private String faculty;
    private String officeLocation;

    public Dean(String id, String name, String email, String password,
                String faculty, String officeLocation) {
        super(id, name, email, password, UserRole.DEAN);
        this.faculty = faculty;
        this.officeLocation = officeLocation;
    }
}
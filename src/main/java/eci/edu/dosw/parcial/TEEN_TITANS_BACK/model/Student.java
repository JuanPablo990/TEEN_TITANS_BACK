package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.enums.UserRole;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Document(collection = "students")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Student extends User {
    private String academicProgram;
    private Integer semester;
    private Double gradeAverage = 0.0;

    public Student(String id, String name, String email, String password,
                   String academicProgram, Integer semester) {
        super(id, name, email, password, UserRole.STUDENT);
        this.academicProgram = academicProgram;
        this.semester = semester;
        this.gradeAverage = 0.0;
    }
}
package eci.edu.dosw.parcial.TEEN_TITANS_BACK.model;

import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * Representa un período académico en el sistema educativo.
 * Esta clase encapsula toda la información relevante sobre un término académico específico,
 * incluyendo períodos de matrícula y ajuste.
 *
 * @author Equipo Teen Titans
 * @version 1.0
 * @since 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "academic_periods")
public class AcademicPeriod {
    @Id
    private String periodId;
    private String name;
    private Date startDate;
    private Date endDate;
    private Date enrollmentStart;
    private Date enrollmentEnd;
    private Date adjustmentPeriodStart;
    private Date adjustmentPeriodEnd;

    @Builder.Default
    private boolean isActive = false;

}
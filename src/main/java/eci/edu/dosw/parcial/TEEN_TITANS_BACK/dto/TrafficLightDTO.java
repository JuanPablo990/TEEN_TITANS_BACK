package eci.edu.dosw.parcial.TEEN_TITANS_BACK.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrafficLightDTO {
    private boolean success;
    private String message;
    private Object data;
    private LocalDateTime timestamp;


    public static TrafficLightDTO success(Object data) {
        return new TrafficLightDTO(true, "Operación exitosa", data, LocalDateTime.now());
    }


    public static TrafficLightDTO error(String message) {
        return new TrafficLightDTO(false, message, null, LocalDateTime.now());
    }
}
package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;


/**
 * Служебный класс для удобства создания Udr записей
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CallDataDto {
    String msisdn;
    Duration incomingDuration = Duration.ofSeconds(0);
    Duration outcomingDuration = Duration.ofSeconds(0);

    public CallDataDto(String msisdn) {
        this.msisdn = msisdn;
    }

    public void addIncoming(LocalDateTime start, LocalDateTime end) {
        outcomingDuration = outcomingDuration.plus(Duration.between(start, end));
    }

    public void addOutcoming(LocalDateTime start, LocalDateTime end) {
        incomingDuration = incomingDuration.plus(Duration.between(start, end));
    }
}

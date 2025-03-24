package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Duration;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CallDataDto {
    String number;
    Duration incomingDuration=Duration.ofSeconds(0);
    Duration outcomingDuration=Duration.ofSeconds(0);

    public CallDataDto(String number) {
        this.number=number;
    }

    public void addIncoming(Timestamp start,Timestamp end){
        outcomingDuration = outcomingDuration.plus(Duration.between(start.toLocalDateTime(),end.toLocalDateTime()));
    }
    public void addOutcoming(Timestamp start,Timestamp end){
        incomingDuration = incomingDuration.plus(Duration.between(start.toLocalDateTime(),end.toLocalDateTime()));
    }
}

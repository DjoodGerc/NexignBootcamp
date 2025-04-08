package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;


/**
 * body для /createCdrReport
 * могут быть добавлены аннотации-ограничения на startDate и endDate
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportInputData {
    String msisdn;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime endDate;
}

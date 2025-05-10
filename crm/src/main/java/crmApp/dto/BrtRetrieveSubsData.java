package crmApp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrtRetrieveSubsData {

    private Long id;

    private String name;

    private String msisdn;

    private Long tariffId;

    private LocalDateTime lastMonthTarifficationDate;

    private Double balance;

    private LocalDateTime registrationDate;

    private String passportData;

    private Long tariffBalance;
    @JsonProperty("isActive")
    private Boolean isActive;
}

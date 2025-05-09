package crmApp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Data
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

    private Boolean isActive;
}

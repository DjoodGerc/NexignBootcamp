package crmApp.dto;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HrsTariffInfo {
    private Long id;

    String name;

    boolean isActive;

    LocalDateTime creationDate;

    String description;

}

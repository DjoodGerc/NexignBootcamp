package crmApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriberCrmDto {
    String fullName;
    String passport;
    String msisdn;
    long tariff;
    Double balance;
}

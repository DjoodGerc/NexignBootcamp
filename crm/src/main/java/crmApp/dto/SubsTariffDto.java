package crmApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubsTariffDto {
    String msisdn;
    TariffDto currentTariff;
    List<TariffDto> availableTariffs;
}

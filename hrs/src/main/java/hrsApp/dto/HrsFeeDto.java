package hrsApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HrsFeeDto {
    long tariffBalanceChange;
    double balanceChange;
}

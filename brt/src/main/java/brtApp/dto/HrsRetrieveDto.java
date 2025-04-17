package brtApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HrsRetrieveDto {
    Long tariffBalanceChange;
    Double balanceChange;
}

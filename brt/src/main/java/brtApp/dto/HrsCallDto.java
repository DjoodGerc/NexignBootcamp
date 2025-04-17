package brtApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HrsCallDto {
    int minutes;
    int callType;
    boolean isRomashkaCall;
    long tariffId;
    int tariffBalance;
    double balance;
}

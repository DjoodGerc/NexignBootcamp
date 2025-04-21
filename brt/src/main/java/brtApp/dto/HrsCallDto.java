package brtApp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HrsCallDto {
    int minutes;
    long callType;
    boolean isRomashkaCall;
    long tariffId;
    long tariffBalance;
    double balance;
}

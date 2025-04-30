package hrsApp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
        @JsonProperty
        boolean isRomashkaCall;
        long tariffId;
        long tariffBalance;
        double balance;
    }
package brtApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CdrFlagEnum {
    INCOMING("01"),
    OUTGOING("02");

    private String flagId;


}

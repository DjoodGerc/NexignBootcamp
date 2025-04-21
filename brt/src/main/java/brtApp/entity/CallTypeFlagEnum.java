package brtApp.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CallTypeFlagEnum {
    INCOMING(1L),
    OUTGOING(2L);
    long flagId;
}

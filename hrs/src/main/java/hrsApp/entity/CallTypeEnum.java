package hrsApp.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CallTypeEnum {
    OUTGOING(1L),
    INCOMING(2L);
    long id;
}
package hrsApp.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CallTypeEnum {
    INCOMING(1L),
    OUTGOING(2L);
    long id;
}
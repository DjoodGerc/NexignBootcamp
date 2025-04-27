package hrsApp.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TariffTypeEnum {
    EVENT(1L),
    MONTH(2L),
    UNLIMITED(3L);
    long id;
}

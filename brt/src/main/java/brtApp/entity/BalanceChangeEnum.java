package brtApp.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BalanceChangeEnum {
    INCREASE(1L),
    DECREASE(2L);
    long id;

}

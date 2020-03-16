package ru.geracimov.otus.java.atmdepartament.money;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class CashBundleItem {
    private final Denomination denomination;
    private final Long count;

}

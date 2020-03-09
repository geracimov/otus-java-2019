package ru.geracimov.otus.java.atmdepartament.money;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@ToString
@EqualsAndHashCode
public class CashBundle {
    private final Set<CashBundleItem> items = new HashSet<>();

    public Set<CashBundleItem> getCashBundleItems() {
        return items;
    }

    public void put(CashBundleItem item) {
        items.add(item);
    }

}

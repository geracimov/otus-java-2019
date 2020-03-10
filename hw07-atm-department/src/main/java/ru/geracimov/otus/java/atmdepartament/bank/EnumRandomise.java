package ru.geracimov.otus.java.atmdepartament.bank;

import java.util.Random;

public class EnumRandomise<T extends Enum<T>> {
    private final Random random = new Random(System.currentTimeMillis());
    private final Class<T> clazz;

    public EnumRandomise(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T random() {
        final T[] enumConstants = clazz.getEnumConstants();
        final int index = random.nextInt(enumConstants.length);
        return enumConstants[index];
    }

}

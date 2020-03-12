package ru.geracimov.otus.java.serializer.type;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Field;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractFieldType implements FieldType {
    private final Field field;
    private final Object object;

}

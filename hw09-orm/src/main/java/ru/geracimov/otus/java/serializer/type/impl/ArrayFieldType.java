package ru.geracimov.otus.java.serializer.type.impl;


import ru.geracimov.otus.java.serializer.service.VisitorService;
import ru.geracimov.otus.java.serializer.type.AbstractFieldType;

import java.lang.reflect.Field;

public class ArrayFieldType extends AbstractFieldType {

    public ArrayFieldType(Field field, Object object) {
        super(field, object);
    }

    @Override
    public String accept(VisitorService service) {
        return service.visit(this);
    }

}

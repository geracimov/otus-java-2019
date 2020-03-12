package ru.geracimov.otus.java.serializer.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.geracimov.otus.java.orm.annotation.Id;
import ru.geracimov.otus.java.orm.exception.OrmException;
import ru.geracimov.otus.java.serializer.type.impl.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

@Slf4j
public class InsertSerializerService implements VisitorService {
    private final static String COMMA = ", ";
    private final static String INSERT = "insert into ";
    private final static String LBRACKET = " (";
    private final static String RBRACKET = ") ";
    private final static String VALUES = " values ";
    private final static String PLACEHOLDER = "?";

    private final Map<Class<?>, String> cache = new HashMap<>();

    @Override
    public String serialize(Object object) {
        return cache.computeIfAbsent(object.getClass(), (k) -> serialize(object, null, this));
    }

    private String serialize(Object object, Field fieldForObject, VisitorService service) {
        log.debug(object.toString());
        final StringBuilder sb = new StringBuilder();
        final Class<?> clazz = object.getClass();

        if (clazz.isPrimitive() || Number.class.isAssignableFrom(clazz)) {
            sb.append(new PrimitiveFieldType(fieldForObject, object).accept(service));
        } else if (String.class.isAssignableFrom(clazz)) {
            sb.append(new StringFieldType(fieldForObject, object).accept(service));
        } else {
            sb.append(new ObjectFieldType(fieldForObject, object).accept(service));
        }
        return sb.toString();
    }

    @Override
    @SneakyThrows
    public String visit(PrimitiveArrayFieldType value) {
        throw new UnsupportedOperationException();
    }

    @Override
    @SneakyThrows
    public String visit(StringArrayFieldType value) {
        throw new UnsupportedOperationException();
    }

    @Override
    @SneakyThrows
    public String visit(PrimitiveFieldType value) {
        return value.getField().getName();
    }

    @Override
    @SneakyThrows
    public String visit(StringFieldType value) {
        return value.getField().getName();
    }

    @Override
    @SneakyThrows
    public String visit(StringCollectionFieldType value) {
        throw new UnsupportedOperationException();
    }

    @Override
    @SneakyThrows
    public String visit(ObjectFieldType objectFieldType) {
        StringBuilder sb = new StringBuilder();
        final Object object = objectFieldType.getObject();

        sb.append(INSERT)
                .append(objectFieldType.getObject().getClass().getSimpleName())
                .append(LBRACKET);


        int i = 0;
        final Field[] declaredFields = object.getClass().getDeclaredFields();
        StringJoiner placeholders = new StringJoiner(COMMA);
        Field fieldId = null;
        for (Field field : declaredFields) {
            if (field.getAnnotation(Id.class) != null) fieldId = field;
            field.setAccessible(true);
            Object fieldValue = field.get(object);
            Class<?> fieldClass = fieldValue.getClass();
            if (fieldClass.isPrimitive() || Number.class.isAssignableFrom(fieldClass)) {
                sb.append(new PrimitiveFieldType(field, object).accept(this));
            } else {
                sb.append(serialize(fieldValue, field, this));
            }
            placeholders.add(PLACEHOLDER);
            if (++i < declaredFields.length) sb.append(COMMA);
        }
        if (fieldId == null) throw new OrmException("Class does not contains a field, annotated by Id");
        sb.append(RBRACKET)
                .append(VALUES)
                .append(LBRACKET)
                .append(placeholders)
                .append(RBRACKET);
        return sb.toString();
    }

}

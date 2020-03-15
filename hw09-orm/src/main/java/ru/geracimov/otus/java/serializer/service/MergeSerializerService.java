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
public class MergeSerializerService implements VisitorService {
    private final static String COMMA = ", ";
    private final static String MERGE = "merge into ";
    private final static String LBRACKET = "(";
    private final static String RBRACKET = ")";
    private final static String VALUES = " values ";
    private final static String KEY = " key ";
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

        if (isBoxingPrimitiveClass(clazz)) {
            sb.append(new PrimitiveFieldType(fieldForObject, object).accept(service));
        } else if (isStringClass(clazz)) {
            sb.append(new StringFieldType(fieldForObject, object).accept(service));
        } else {
            sb.append(new ObjectFieldType(fieldForObject, object).accept(service));
        }
        return sb.toString();
    }

    @Override
    public String visit(ArrayFieldType value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String visit(CollectionFieldType value) {
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
    public String visit(ObjectFieldType objectFieldType) {
        StringBuilder sb = new StringBuilder(MERGE);
        final Object object = objectFieldType.getObject();
        sb.append(objectFieldType.getObject().getClass().getSimpleName())
          .append(KEY)
          .append(LBRACKET);

        int i = 0;
        final Field[] declaredFields = object.getClass().getDeclaredFields();
        StringJoiner placeholders = new StringJoiner(COMMA);
        Field fieldId = null;
        for (Field field : declaredFields) {
            if (field.getAnnotation(Id.class) != null)
                if (fieldId != null)
                    throw new OrmException("Class already contains a field, annotated by @Id - " + fieldId.getName());
                else fieldId = field;
            placeholders.add(PLACEHOLDER);
        }
        if (fieldId == null) throw new OrmException("Class does not contains a field, annotated by @Id");
        fieldId.setAccessible(true);
        sb.append(serialize(fieldId.get(object), fieldId, this));
        sb.append(RBRACKET)
          .append(VALUES)
          .append(LBRACKET)
          .append(placeholders)
          .append(RBRACKET);
        return sb.toString();
    }


    private boolean isBoxingPrimitiveClass(Class<?> clazz) {
        return clazz.isPrimitive()
                || Number.class.isAssignableFrom(clazz)
                || Boolean.class.isAssignableFrom(clazz);
    }

    private boolean isStringClass(Class<?> clazz) {
        return String.class.isAssignableFrom(clazz)
                || Character.class.isAssignableFrom(clazz);
    }

}

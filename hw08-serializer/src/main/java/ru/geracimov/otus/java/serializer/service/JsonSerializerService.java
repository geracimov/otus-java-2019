package ru.geracimov.otus.java.serializer.service;

import lombok.SneakyThrows;
import ru.geracimov.otus.java.serializer.type.impl.*;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.stream.Stream;

public class JsonSerializerService implements VisitorService {
    private final static String QUOTE = "\"";
    private final static String COLON = ":";
    private final static String COMMA = ",";
    private final static String LBRACE = "{";
    private final static String RBRACE = "}";
    private final static String LBRACKET = "[";
    private final static String RBRACKET = "]";
    private final static String NULL = "null";
    private final static String EMPTY = "";

    public String serialize(Object object) {
        if (object == null) return NULL;
        return serialize(object, null, this);
    }

    private String serialize(Object object, Field fieldForObject, VisitorService service) {
        final StringBuilder sb = new StringBuilder();
        final Class<?> clazz = object.getClass();

        if (clazz.isArray()) {
            sb.append(new ArrayFieldType(fieldForObject, object).accept(service));
        } else if (Collection.class.isAssignableFrom(clazz)) {
            sb.append(new CollectionFieldType(fieldForObject, object).accept(service));
        } else if (isBoxingPrimitiveClass(clazz)) {
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
        final Object object = value.getObject();
        final int length = Array.getLength(object);

        final Object[] objects = new Object[length];
        for (int i = 0; i < length; i++) {
            objects[i] = Array.get(object, i);
        }
        return arrToString(value.getField(), objects);
    }

    @Override
    @SneakyThrows
    public String visit(PrimitiveFieldType value) {
        return value.getField() != null
                ? quoteString(value.getField().getName()) + COLON + value.getField().get(value.getObject())
                : value.getObject().toString();
    }

    @Override
    @SneakyThrows
    public String visit(StringFieldType value) {
        return value.getField() != null
                ? quoteString(value.getField().getName()) + COLON + quoteString(value.getObject())
                : quoteString(value.getObject());
    }

    @Override
    @SneakyThrows
    public String visit(CollectionFieldType value) {
        StringBuilder sb = new StringBuilder();
        if (value.getField() != null) {
            sb.append(quoteString(value.getField().getName())).append(COLON);
        }

        sb.append(LBRACKET);
        final Collection<?> objects = (Collection<?>) value.getObject();
        int i = 0;
        for (Object o : objects) {
            sb.append(serialize(o, null, this));
            if (++i < objects.size()) sb.append(COMMA);
        }
        return sb.append(RBRACKET).toString();
    }

    @Override
    @SneakyThrows
    public String visit(ObjectFieldType objectFieldType) {
        final Field fieldForObject = objectFieldType.getField();
        StringBuilder sb = new StringBuilder();
        final Object object = objectFieldType.getObject();

        if (fieldForObject != null) {
            sb.append(quoteString(fieldForObject.getName())).append(COLON);
        }
        sb.append(LBRACE);

        int i = 0;
        final Field[] declaredFields = getSerializedFields(object);
        for (Field field : declaredFields) {
            field.setAccessible(true);
            Object fieldValue = field.get(object);
            Class<?> fieldClass = fieldValue.getClass();
            if (isBoxingPrimitiveClass(fieldClass)) {
                sb.append(new PrimitiveFieldType(field, object).accept(this));
            } else {
                sb.append(serialize(fieldValue, field, this));
            }
            if (++i < declaredFields.length) sb.append(COMMA);
        }
        return sb.append(RBRACE).toString();
    }

    private Field[] getSerializedFields(Object object) {
        return Stream.of(object.getClass().getDeclaredFields())
                     .filter(f -> !Modifier.isStatic(f.getModifiers()))
                     .toArray(Field[]::new);
    }

    private String arrToString(Field field, Object[] objects) {
        StringBuilder sb = new StringBuilder();
        if (field != null)
            sb.append(quoteString(field.getName())).append(COLON);
        if (objects == null)
            return sb.append(NULL).toString();
        if (objects.length == 0)
            return sb.append(LBRACKET + RBRACKET).toString();

        sb.append(LBRACKET);
        String wrapper;
        int i = 0;
        for (Object o : objects) {
            wrapper = isStringClass(o.getClass()) ? QUOTE : EMPTY;
            sb.append(wrapper).append(o).append(wrapper);
            if (++i < objects.length) sb.append(COMMA);
        }
        return sb.append(RBRACKET).toString();
    }

    private String quoteString(Object object) {
        return object == null
                ? NULL
                : QUOTE + object.toString() + QUOTE;
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

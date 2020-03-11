package ru.geracimov.otus.java.serializer.service;

import lombok.SneakyThrows;
import ru.geracimov.otus.java.serializer.type.impl.*;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

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
        return serialize(object, null, this);
    }

    private String serialize(Object object, Field fieldForObject, VisitorService service) {
        final StringBuilder sb = new StringBuilder();
        final Class<?> clazz = object.getClass();

        if (clazz.isArray()) {
            final Class<?> componentType = clazz.getComponentType();
            if (componentType.isPrimitive()) {
                sb.append(new PrimitiveArrayFieldType(fieldForObject, object).accept(service));
            } else if (String.class.isAssignableFrom(componentType)) {
                sb.append(new StringArrayFieldType(fieldForObject, object).accept(service));
            } else {
                for (int i = 0; i < Array.getLength(object); i++) {
                    sb.append(serialize(Array.get(object, i), fieldForObject, service));
                }
            }
        } else if (Collection.class.isAssignableFrom(clazz)) {
            sb.append(new StringCollectionFieldType(fieldForObject, object).accept(service));
        } else if (clazz.isPrimitive() || Number.class.isAssignableFrom(clazz)) {
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
        Object[] objects = new Object[Array.getLength(value.getObject())];
        for (int i = 0; i < objects.length; i++) {
            objects[i] = Array.get(value.getObject(), i);
        }
        return arrToString(value.getField().getName(), objects, EMPTY);
    }

    @Override
    @SneakyThrows
    public String visit(StringArrayFieldType value) {
        final Object[] objects = (Object[]) value.getObject();
        return arrToString(value.getField().getName(), objects, QUOTE);
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
    public String visit(StringCollectionFieldType value) {
        final String name = value.getField().getName();
        StringBuilder sb = new StringBuilder(quoteString(name)).append(COLON);
        final ParameterizedType genericType = (ParameterizedType) value.getField().getGenericType();
        sb.append(LBRACKET);
        if (genericType.getActualTypeArguments().length > 0) {
            final Type actualTypeArgument = genericType.getActualTypeArguments()[0];
            final Class<?> aClass = Class.forName(actualTypeArgument.getTypeName());

            int i = 0;
            final Collection<?> objects = (Collection<?>) value.getObject();
            for (Object o : objects) {
                sb.append(serialize(aClass.cast(o), null, this));
                if (++i < objects.size()) sb.append(COMMA);
            }
        }
        sb.append(RBRACKET);
        return sb.toString();
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
        final Field[] declaredFields = object.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            Object fieldValue = field.get(object);
            Class<?> fieldClass = fieldValue.getClass();
            if (fieldClass.isPrimitive() || Number.class.isAssignableFrom(fieldClass)) {
                sb.append(new PrimitiveFieldType(field, object).accept(this));
            } else {
                sb.append(serialize(fieldValue, field, this));
            }
            if (++i < declaredFields.length) sb.append(COMMA);
        }
        return sb.append(RBRACE).toString();
    }

    private String arrToString(String name, Object[] objects, String wrapper) {
        StringBuilder sb = new StringBuilder().append(quoteString(name)).append(COLON);
        if (objects == null)
            return sb.append(NULL).toString();
        if (objects.length == 0)
            return sb.append(LBRACKET + RBRACKET).toString();

        sb.append(LBRACKET);
        int i = 0;
        for (Object o : objects) {
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

}

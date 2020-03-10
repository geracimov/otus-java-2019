package ru.geracimov.otus.java.serializer.service;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;
import ru.geracimov.otus.java.serializer.type.impl.PrimitiveArrayFieldType;
import ru.geracimov.otus.java.serializer.type.impl.PrimitiveFieldType;
import ru.geracimov.otus.java.serializer.type.impl.StringArrayFieldType;
import ru.geracimov.otus.java.serializer.type.impl.StringFieldType;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class JsonSerializerService implements VisitorService {

    public String serialize(Object object) {
        return serialize(object, null, this);
    }

    @SneakyThrows
    private String serialize(Object object, Field fieldForObject, VisitorService service) {
        StringBuilder sb = new StringBuilder();
        if (object.getClass().isArray()) {
            final Class<?> componentType = object.getClass().getComponentType();
            if (componentType.isPrimitive()) {
                val primitiveArrayFieldType = new PrimitiveArrayFieldType(fieldForObject, object);
                sb.append(primitiveArrayFieldType.accept(service));
            } else if (componentType.isAssignableFrom(String.class)) {
                val stringArrayFieldType = new StringArrayFieldType(fieldForObject, object);
                sb.append(stringArrayFieldType.accept(service));
            } else {
                for (int i = 0; i < Array.getLength(object); i++) {
                    sb.append(serialize(Array.get(object, i), fieldForObject, service));
                }
            }
        } else if (object.getClass().isPrimitive() || Number.class.isAssignableFrom(object.getClass())) {
            val primitiveFieldType = new PrimitiveFieldType(fieldForObject, object);
            sb.append(primitiveFieldType.accept(service));
        } else if (String.class.isAssignableFrom(object.getClass())) {
            val stringFieldType = new StringFieldType(fieldForObject, object);
            sb.append(stringFieldType.accept(service));
        } else {
            if (fieldForObject != null) sb.append("\"").append(fieldForObject.getName()).append("\":");

            sb.append("{");

            Field[] fields = object.getClass().getDeclaredFields();
            boolean secondAndMore = false;
            for (Field field : fields) {
                if (secondAndMore) sb.append(",");
                field.setAccessible(true);
                val fieldValue = field.get(object);
                val fieldClass = fieldValue.getClass();
                if (fieldClass.isPrimitive() || Number.class.isAssignableFrom(fieldClass)) {
                    val primitiveFieldType = new PrimitiveFieldType(field, object);
                    sb.append(primitiveFieldType.accept(service));
                } else {
                    sb.append(serialize(fieldValue, field, service));
                }
                secondAndMore = true;
            }
            sb.append("}");
        }
        return sb.toString();
    }

    @Override
    @SneakyThrows
    public String visit(PrimitiveArrayFieldType value) {
        Object[] arr = new Object[Array.getLength(value.getObject())];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = Array.get(value.getObject(), i);
        }
        return arrToString(value.getField().getName(), arr, "");
    }

    @Override
    @SneakyThrows
    public String visit(StringArrayFieldType value) {
        final Object[] arr = (Object[]) value.getObject();
        return arrToString(value.getField().getName(), arr, "\"");
    }

    @Override
    @SneakyThrows
    public String visit(PrimitiveFieldType value) {
        final String name = value.getField().getName();
        final Object o = value.getField().get(value.getObject());
        return String.format("\"%s\":%s", name, o);
    }

    @Override
    @SneakyThrows
    public String visit(StringFieldType value) {
        return String.format("\"%s\":\"%s\"", value.getField().getName(), value.getObject());
    }

    private String arrToString(String name, @NonNull Object[] arr, String wrapper) {
        StringBuilder sb = new StringBuilder("\"").append(name).append("\":");
        if (arr == null)
            sb.append("null");
        int iMax = arr.length - 1;
        if (iMax == -1)
            sb.append("[]");

        sb.append('[');
        for (int i = 0; ; i++) {
            sb.append(wrapper).append(Array.get(arr, i)).append(wrapper);
            if (i == iMax)
                return sb.append(']').toString();
            sb.append(",");
        }
    }

}

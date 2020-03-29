package ru.otus.jdbc.query;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EntityHelper<T> {
    private final Class<T> clazz;
    private final Collection<Field> fields;

    public EntityHelper(Class<T> clazz) {
        this.clazz = clazz;
        fields = List.of(clazz.getDeclaredFields());
    }

    public String getTableName() {
        return clazz.getSimpleName();
    }

    public String getFieldNames(Predicate<Field> filterPredicate, Function<Field, String> valueFunction) {
        return fields.stream()
                     .filter(filterPredicate)
                     .map(valueFunction)
                     .collect(Collectors.joining(", "));
    }

    public Predicate<Field> isId() {
        return f -> f.isAnnotationPresent(Id.class);
    }

    public Predicate<Field> nonId() {
        return isId().negate();
    }

    public Predicate<Field> all() {
        return f -> true;
    }

}

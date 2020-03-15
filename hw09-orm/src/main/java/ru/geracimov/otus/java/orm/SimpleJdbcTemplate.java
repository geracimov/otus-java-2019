package ru.geracimov.otus.java.orm;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.geracimov.otus.java.orm.annotation.Id;
import ru.geracimov.otus.java.orm.exception.OrmException;
import ru.geracimov.otus.java.serializer.service.*;
import ru.otus.jdbc.DbExecutor;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Slf4j
public class SimpleJdbcTemplate<T> implements JdbcTemplate<T> {
    private final DbExecutor<T> executor;
    private final Connection connection;
    private final VisitorService select;
    private final VisitorService insert;
    private final VisitorService update;
    private final VisitorService merge;

    public SimpleJdbcTemplate(DbExecutor<T> executor, Connection connection) {
        this.executor = executor;
        this.connection = connection;
        this.select = new SelectSerializerService();
        this.insert = new InsertSerializerService();
        this.update = new UpdateSerializerService();
        this.merge = new MergeSerializerService();
    }

    @Override
    @SneakyThrows
    public void create(T objectData) {
        final List<String> parameters = getParameters(objectData, false);
        final String sql = insert.serialize(objectData);
        executor.insertRecord(connection, sql, parameters);
    }

    @Override
    @SneakyThrows
    public void update(T objectData) {
        final List<String> parameters = getParameters(objectData, true);
        final String sql = update.serialize(objectData);
        executor.insertRecord(connection, sql, parameters);
    }

    @Override
    @SneakyThrows
    public void createOrUpdate(T objectData) {
        final List<String> parameters = getParameters(objectData, false);
        final String sql = merge.serialize(objectData);
        executor.insertRecord(connection, sql, parameters);
    }

    @SneakyThrows
    @Override
    public T load(long id, Class<T> clazz) {
        final String sql = select.serialize(clazz);
        return executor.selectRecord(connection, sql, id, rsHandler(clazz))
                       .orElseThrow(() -> new OrmException(clazz.getName() + " with @Id " + id + " not found"));
    }

    @SneakyThrows
    private List<String> getParameters(Object object, boolean idLast) {
        List<String> parameters = new ArrayList<>();
        String id = null;
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (idLast && field.getAnnotation(Id.class) != null)
                id = field.get(object).toString();
            else {
                parameters.add(field.get(object).toString());
            }
            field.setAccessible(false);
        }
        if (idLast)
            parameters.add(id);
        return parameters;
    }

    private Function<ResultSet, T> rsHandler(Class<T> clazz) {
        return (resultSet -> {
            try {
                if (resultSet.next()) {
                    if (!resultSet.isLast())
                        throw new OrmException("Object is not unique");

                    T instance = clazz.getConstructor().newInstance();
                    for (Field field : clazz.getDeclaredFields()) {
                        field.setAccessible(true);
                        final Object object = resultSet.getObject(field.getName());
                        field.set(instance, object);
                    }
                    return instance;
                }
            } catch (Exception e) {
                throw new OrmException(e);
            }
            return null;
        });
    }

}

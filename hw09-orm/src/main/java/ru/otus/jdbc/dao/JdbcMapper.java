package ru.otus.jdbc.dao;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

@Slf4j
public class JdbcMapper {

    <T> Function<ResultSet, T> map(Class<T> clazz) {
        return resultSet -> {
            try {
                if (!resultSet.next()) {
                    return null;
                }
                if (!resultSet.isLast()) {
                    throw new RuntimeException("More than one");
                }

                final Constructor<T> constructor = clazz.getConstructor();
                final T instance = constructor.newInstance();
                for (Field field : clazz.getDeclaredFields()) {
                    field.setAccessible(true);
                    field.set(instance, resultSet.getObject(field.getName()));
                }
                return instance;
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Class doesn't have default constructor");
            } catch (SQLException e) {
                log.error("SQL exception", e);
                throw new RuntimeException(e);
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        };
    }

}

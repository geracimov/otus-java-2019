package ru.otus.jdbc.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.otus.jdbc.query.MetaDataSource;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
public class JdbcMapper {
    private final MetaDataSource metaData;

    <T> Function<ResultSet, T> map(Class<T> clazz) {
        return resultSet -> {
            try {
                if (!resultSet.next()) {
                    return null;
                }
                if (!resultSet.isLast()) {
                    throw new RuntimeException("More than one");
                }

                @SuppressWarnings("unchecked")
                final Constructor<T> constructor = (Constructor<T>) metaData.getConstructor(clazz);
                final T instance = constructor.newInstance();
                for (Field field : metaData.getFields(clazz)) {
                    field.setAccessible(true);
                    field.set(instance, resultSet.getObject(field.getName()));
                }
                return instance;
            } catch (SQLException e) {
                log.error("SQL exception", e);
                throw new RuntimeException(e);
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        };
    }

}

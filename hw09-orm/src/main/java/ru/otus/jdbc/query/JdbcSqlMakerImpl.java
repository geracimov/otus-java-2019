package ru.otus.jdbc.query;

import java.lang.reflect.Field;

public class JdbcSqlMakerImpl<T> implements JdbcSqlMaker<T> {
    private static final String SELECT_QUERY = "select %s from %s where %s = ?";
    private static final String INSERT_QUERY = "insert into %s (%s) values (%s)";
    private static final String UPDATE_QUERY = "update %s set %s where %s = ?";

    private final EntityHelper<T> entityHelper;

    public JdbcSqlMakerImpl(Class<T> clazz) {
        entityHelper = new EntityHelper<>(clazz);
    }

    @Override
    public String selectQuery() {
        return String.format(
                SELECT_QUERY,
                entityHelper.getFieldNames(entityHelper.all(), Field::getName),
                entityHelper.getTableName(),
                entityHelper.getFieldNames(entityHelper.isId(), Field::getName));
    }

    @Override
    public String insertQuery() {
        return String.format(
                INSERT_QUERY,
                entityHelper.getTableName(),
                entityHelper.getFieldNames(entityHelper.nonId(), Field::getName),
                entityHelper.getFieldNames(entityHelper.nonId(), f -> "?"));
    }

    @Override
    public String updateQuery() {
        return String.format(
                UPDATE_QUERY,
                entityHelper.getTableName(),
                entityHelper.getFieldNames(entityHelper.nonId(), f -> f.getName() + " = ?"),
                entityHelper.getFieldNames(entityHelper.isId(), Field::getName));
    }

}

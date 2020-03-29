package ru.otus.jdbc.query;

public interface JdbcSqlMaker<T> {

    String selectQuery();

    String insertQuery();

    String updateQuery();

}

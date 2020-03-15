package ru.geracimov.otus.java.orm.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.PreparedStatement;

@Slf4j
@UtilityClass
public class TableCreator {

    @SneakyThrows
    public static void createUserTable(DataSource dataSource) {
        PreparedStatement pst = dataSource.getConnection().prepareStatement(
                "create table if not exists User(id bigint(20) NOT NULL auto_increment, name varchar(255), age int(3))");
        pst.executeUpdate();
        log.debug("table User created");
    }

    @SneakyThrows
    public static void createAccountTable(DataSource dataSource) {
        PreparedStatement pst = dataSource.getConnection().prepareStatement(
                "create table if not exists Account(no bigint(20) NOT NULL auto_increment, type varchar(255), rest number)");
        pst.executeUpdate();
        log.debug("table Account created");
    }

}

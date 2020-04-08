package ru.otus.jdbc.query;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

public class MetaDataSource {
    private static final String SELECT_QUERY = "select %s from %s where %s = ?";
    private static final String INSERT_QUERY = "insert into %s (%s) values (%s)";
    private static final String UPDATE_QUERY = "update %s set %s where %s = ?";
    private final Map<String, String[]> allFieldsNames;
    private final Map<String, String[]> allFieldsNamesExceptId;
    private final Map<String, String> fieldIdNames;
    private final Map<String, String> tableNames;
    private final Map<String, String> selectQueries;
    private final Map<String, String> insertQueries;
    private final Map<String, String> updateQueries;
    private final Map<String, Constructor<?>> classConstructors;
    private final Map<String, Field[]> classFields;

    public MetaDataSource() {
        this.allFieldsNames = new HashMap<>();
        this.allFieldsNamesExceptId = new HashMap<>();
        this.fieldIdNames = new HashMap<>();
        this.tableNames = new HashMap<>();
        this.selectQueries = new HashMap<>();
        this.insertQueries = new HashMap<>();
        this.updateQueries = new HashMap<>();
        this.classConstructors = new HashMap<>();
        this.classFields = new HashMap<>();
    }

    public String getTableName(Class<?> clazz) {
        return getOrLoad(tableNames, clazz);
    }

    public String[] getFieldNamesExceptId(Class<?> clazz) {
        return getOrLoad(allFieldsNamesExceptId, clazz);
    }

    public String[] getFieldNames(Class<?> clazz) {
        return getOrLoad(allFieldsNames, clazz);
    }

    public String getFieldIdName(Class<?> clazz) {
        return getOrLoad(fieldIdNames, clazz);
    }

    public String getSelectQuery(Class<?> clazz) {
        return getOrLoad(selectQueries, clazz);
    }

    public String getInsertQuery(Class<?> clazz) {
        return getOrLoad(insertQueries, clazz);
    }

    public String getUpdateQuery(Class<?> clazz) {
        return getOrLoad(updateQueries, clazz);
    }

    public Constructor<?> getConstructor(Class<?> clazz) {
        return getOrLoad(classConstructors, clazz);
    }

    public Field[] getFields(Class<?> clazz) {
        return getOrLoad(classFields, clazz);
    }

    private <V> V getOrLoad(Map<String, V> map, Class<?> clazz) throws MetaDataSourceException {
        if (map.containsKey(clazz.getName()))
            return map.get(clazz.getName());

        addClass(clazz);
        return map.get(clazz.getName());
    }

    private void addClass(Class<?> clazz) {
        final String clazzName = clazz.getName();
        final Field[] declaredFields = clazz.getDeclaredFields();
        final int fieldsCount = declaredFields.length;
        if (fieldsCount == 0)
            throw new MetaDataSourceException(String.format("Class %s doesn't have any fields", clazz.getName()));
        String[] fieldNames = new String[fieldsCount];
        String[] fieldNamesExceptIdNull = new String[fieldsCount];
        String idFieldName = null;
        for (int i = 0; i < declaredFields.length; i++) {
            Field field = declaredFields[i];

            if (field.isAnnotationPresent(Id.class)) {
                if (idFieldName != null) {
                    throw new MetaDataSourceException(String.format("Class %s has too many Id fields",
                                                                    clazz.getName()));
                }
                idFieldName = field.getName();
            } else {
                fieldNamesExceptIdNull[i] = field.getName();
            }
            fieldNames[i] = field.getName();
        }
        if (idFieldName == null) {
            throw new MetaDataSourceException(String.format("Class %s doesn't have Id field", clazz.getName()));
        }
        String[] fieldNamesExceptId = Arrays.stream(fieldNamesExceptIdNull)
                                            .filter(Objects::nonNull)
                                            .toArray(String[]::new);
        try {
            classConstructors.put(clazzName, clazz.getConstructor());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Class doesn't have default constructor");
        }
        classFields.put(clazzName, declaredFields);
        fieldIdNames.put(clazzName, idFieldName);
        allFieldsNames.put(clazzName, fieldNames);
        allFieldsNamesExceptId.put(clazzName, fieldNamesExceptId);
        tableNames.put(clazzName, clazz.getSimpleName());
        selectQueries.put(clazzName, getSelectQuery(clazz.getSimpleName(), fieldNames, idFieldName));
        updateQueries.put(clazzName, getUpdateQuery(clazz.getSimpleName(), fieldNamesExceptId, idFieldName));
        insertQueries.put(clazzName, getInsertQuery(clazz.getSimpleName(), fieldNamesExceptId));
    }

    private String getSelectQuery(String tableName, String[] fieldNames, String idFieldName) {
        StringJoiner commaStringJoiner = new StringJoiner(", ");
        for (String fieldName : fieldNames) {
            commaStringJoiner.add(fieldName);
        }
        return String.format(SELECT_QUERY, commaStringJoiner.toString(), tableName, idFieldName);
    }

    private String getUpdateQuery(String tableName, String[] fieldNamesExceptId, String idFieldName) {
        StringJoiner commaStringJoiner = new StringJoiner(", ");
        for (String fieldName : fieldNamesExceptId) {
            commaStringJoiner.add(fieldName + " = ?");
        }
        return String.format(UPDATE_QUERY, tableName, commaStringJoiner.toString(), idFieldName);
    }

    private String getInsertQuery(String tableName, String[] fieldNamesExceptId) {
        StringJoiner commaStringJoiner = new StringJoiner(", ");
        StringJoiner commaQuestionMarkJoiner = new StringJoiner(", ");
        for (String fieldName : fieldNamesExceptId) {
            commaStringJoiner.add(fieldName);
            commaQuestionMarkJoiner.add("?");
        }
        return String.format(INSERT_QUERY, tableName, commaStringJoiner.toString(), commaQuestionMarkJoiner.toString());
    }

}

package ru.otus.java.pro.mapper;

import ru.otus.java.pro.core.repository.DataTemplate;
import ru.otus.java.pro.core.repository.DataTemplateException;
import ru.otus.java.pro.core.repository.executor.DbExecutor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return constructObject(rs);
                }
                return null;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
            var result = new ArrayList<T>();
            try {
                while (rs.next()) {
                    result.add(constructObject(rs));
                }
                return result;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T object) {
        try {
            return dbExecutor.executeStatement(
                    connection,
                    entitySQLMetaData.getInsertSql(),
                    getInsertParams(object)
            );
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T object) {
        try {
            dbExecutor.executeStatement(
                    connection,
                    entitySQLMetaData.getUpdateSql(),
                    getUpdateParams(object)
            );
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private List<Object> getUpdateParams(T object) {
        List<Field> fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        List<Object> result = new ArrayList<>();
        for (Field field : fieldsWithoutId) {
            result.add(getFieldValue(field, object));
        }
        Field idField = entityClassMetaData.getIdField();
        result.add(getFieldValue(idField, object));
        return result;
    }

    private List<Object> getInsertParams(T object) {
        List<Field> fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        List<Object> result = new ArrayList<>();
        for (Field field : fieldsWithoutId) {
            result.add(getFieldValue(field, object));
        }
        return result;
    }

    private T constructObject(ResultSet resultSet) {
        try {
            Constructor<T> constructor = entityClassMetaData.getConstructor();
            T newInstance = constructor.newInstance();
            for (Field field : entityClassMetaData.getAllFields()) {
                field.setAccessible(true);
                field.set(newInstance, resultSet.getObject(field.getName()));
            }
            return newInstance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object getFieldValue(Field field, T object) {
        field.setAccessible(true);
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            throw new CustomReflectionException(e);
        }
    }
}

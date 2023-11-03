package ru.otus.java.pro.mapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    private final EntityClassMetaData entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return String.format("select * from %s", entityClassMetaData.getName());
    }

    @Override
    public String getSelectByIdSql() {
        String typeName = entityClassMetaData.getName();
        String idFieldName = entityClassMetaData.getIdField().getName();
        return String.format(
                "select * from %s where %s = ?",
                typeName,
                idFieldName
        );
    }

    @Override
    public String getInsertSql() {
        String typeName = entityClassMetaData.getName();
        List<Field> fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        String fields = fieldsWithoutId.stream()
                .map(Field::getName)
                .collect(Collectors.joining(", "));
        String valuesPlaceHolders = fieldsWithoutId.stream().map(it -> "?").collect(Collectors.joining(", "));
        return String.format("insert into %s(%s) values (%s)", typeName, fields, valuesPlaceHolders);
    }

    @Override
    public String getUpdateSql() {
        String typeName = entityClassMetaData.getName();
        String idField = entityClassMetaData.getIdField().getName();
        List<Field> fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        String fields = fieldsWithoutId.stream()
                .map(Field::getName)
                .map(it -> it + " = ?")
                .collect(Collectors.joining(", "));
        return String.format("update %s set %s where %s = ?", typeName, fields, idField);
    }
}

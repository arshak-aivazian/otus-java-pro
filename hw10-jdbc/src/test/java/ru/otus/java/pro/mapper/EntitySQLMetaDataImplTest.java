package ru.otus.java.pro.mapper;

import org.junit.jupiter.api.Test;
import ru.otus.java.pro.crm.model.Client;
import ru.otus.java.pro.crm.model.Manager;

import static org.junit.jupiter.api.Assertions.*;

class EntitySQLMetaDataImplTest {
    private final  EntitySQLMetaData entitySQLMetaData = new EntitySQLMetaDataImpl(new EntityClassMetaDataImpl(Manager.class));

    @Test
    void getSelectAllSql() {
        System.out.println(entitySQLMetaData.getSelectAllSql());
    }

    @Test
    void getSelectByIdSql() {
        System.out.println(entitySQLMetaData.getSelectByIdSql());
    }

    @Test
    void getInsertSql() {
        System.out.println(entitySQLMetaData.getInsertSql());
    }

    @Test
    void getUpdateSql() {
        System.out.println(entitySQLMetaData.getUpdateSql());
    }
}
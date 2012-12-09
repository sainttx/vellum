/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package saltserver.storage.secret;

import crocserver.storage.adminuser.AdminUserMeta;
import vellum.entity.AbstractEntityStorage;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import saltserver.app.SecretAppStorage;
import vellum.query.QueryMap;
import vellum.query.RowSets;
import vellum.storage.ConnectionEntry;
import vellum.storage.StorageException;
import vellum.storage.StorageExceptionType;
import vellum.util.Lists;

/**
 *
 * @author evan
 */
public class SecretValueStorage extends AbstractEntityStorage<Long, SecretRecord> {

    static QueryMap sqlMap = new QueryMap(SecretValueStorage.class);
    SecretAppStorage storage;

    public SecretValueStorage(SecretAppStorage storage) {
        this.storage = storage;
    }

    public void validate() throws SQLException {
        ConnectionEntry connection = storage.getConnectionPool().takeEntry();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    sqlMap.get(SecretValueQuery.validate.name()));
            ResultSet resultSet = statement.executeQuery();
            List<String> columnNameList = RowSets.getColumnNameList(resultSet.getMetaData());
            for (Enum columnNameEnum : SecretValueMeta.values()) {
                String columnName = columnNameEnum.name().toUpperCase();
                if (!columnNameList.contains(columnName)) {
                    throw new SQLException(columnName);
                }                
                columnNameList.remove(columnName);
            }
            if (!columnNameList.isEmpty()) {
                throw new SQLException(Lists.format(columnNameList));
            }
        } finally {
            storage.getConnectionPool().releaseConnection(connection);
        }
    }
    
    private SecretRecord get(ResultSet resultSet) throws SQLException {
        SecretRecord secret = new SecretRecord();
        secret.setId(resultSet.getLong(SecretValueMeta.secret_id.name()));
        secret.setGroup(resultSet.getString(SecretValueMeta.group_.name()));
        secret.setName(resultSet.getString(SecretValueMeta.name_.name()));
        secret.setSecret(resultSet.getString(SecretValueMeta.secret.name()));
        secret.setStored(true);
        return secret;
    }

    public long insert(SecretRecord secret) throws SQLException {
        ConnectionEntry connection = storage.getConnectionPool().takeEntry();
        try {
            PreparedStatement statement = connection.prepareStatement(
                sqlMap.get(SecretValueQuery.insert.name()));
            int index = 0;
            statement.setString(++index, secret.getGroup());
            statement.setString(++index, secret.getName());
            statement.setString(++index, secret.getSecret());
            int updateCount = statement.executeUpdate();
            connection.setOk(true);
            if (updateCount != 1) {
                throw new SQLException();
            }
            ResultSet resultSet = statement.getGeneratedKeys();
            if (!resultSet.next()) {
                throw new SQLException();    
            }
            long id = resultSet.getLong(1);
            secret.setId(id);
            secret.setStored(true);
            connection.setOk(true);
            return id;
        } finally {
            storage.getConnectionPool().releaseConnection(connection);
        }
    }

    public boolean exists(String group, String name) throws SQLException {
        ConnectionEntry connection = storage.getConnectionPool().takeEntry();
        try {
            PreparedStatement statement = connection.prepareStatement(sqlMap.get(SecretValueQuery.exists.name()));
            int index = 0;
            statement.setString(++index, group);
            statement.setString(++index, name);
            ResultSet resultSet = statement.executeQuery();
            boolean exists = resultSet.next();
            connection.setOk(true);
            return exists;
        } finally {
            storage.getConnectionPool().releaseConnection(connection);
        }
    }

    public SecretRecord get(String group, String name) throws SQLException {
        SecretRecord secret = find(group, name);
        if (secret == null) {
            throw new StorageException(StorageExceptionType.NOT_FOUND, group, name);
        }
        return secret;
    }
    
    public SecretRecord find(String group, String name) throws SQLException {
        ConnectionEntry connection = storage.getConnectionPool().takeEntry();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    sqlMap.get(SecretValueQuery.find.name()));
            int index = 0;
            statement.setString(++index, group);
            statement.setString(++index, name);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            connection.setOk(true);
            return get(resultSet);
        } finally {
            storage.getConnectionPool().releaseConnection(connection);
        }
    }
    
    @Override
    public SecretRecord find(Long id) throws SQLException {
        ConnectionEntry connection = storage.getConnectionPool().takeEntry();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    sqlMap.get(SecretValueQuery.find_id.name()));
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            connection.setOk(true);
            return get(resultSet);
        } finally {
            storage.getConnectionPool().releaseConnection(connection);
        }
    }

    public void update(SecretRecord secret) throws SQLException {
        ConnectionEntry connection = storage.getConnectionPool().takeEntry();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    sqlMap.get(SecretValueQuery.update.name()));
            int index = 0;
            statement.setString(++index, secret.getSecret());
            statement.setLong(++index, secret.getId());
            int updateCount = statement.executeUpdate();
            connection.setOk(true);
            if (updateCount != 1) {
                throw new SQLException();
            }
            connection.setOk(true);
        } finally {
            storage.getConnectionPool().releaseConnection(connection);
        }
    }

    public List<SecretRecord> getList() throws SQLException {
        ConnectionEntry connection = storage.getConnectionPool().takeEntry();
        try {
            List<SecretRecord> list = new ArrayList();
            PreparedStatement statement = connection.prepareStatement(sqlMap.get(SecretValueQuery.list.name()));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                list.add(get(resultSet));
            }
            connection.setOk(true);
            return list;
        } finally {
            storage.getConnectionPool().releaseConnection(connection);
        }
    }
}

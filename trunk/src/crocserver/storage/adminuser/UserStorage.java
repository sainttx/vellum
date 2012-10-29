/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package crocserver.storage.adminuser;

import crocserver.storage.common.CrocStorage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import vellum.query.QueryMap;
import vellum.storage.StorageException;
import vellum.storage.StorageExceptionType;

/**
 *
 * @author evan
 */
public class UserStorage {

    static QueryMap sqlMap = new QueryMap(UserStorage.class);
    CrocStorage storage;

    public UserStorage(CrocStorage storage) {
        this.storage = storage;
    }
    public void insert(User adminUser) throws SQLException, StorageException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            PreparedStatement statement = connection.prepareStatement(
                sqlMap.get(UserQuery.insert.name()));
            int index = 0;
            statement.setString(++index, adminUser.getUserName());
            statement.setString(++index, adminUser.getDisplayName());
            statement.setString(++index, adminUser.getEmail());
            if (adminUser.getRole() != null) {
                statement.setString(++index, adminUser.getRole().name());
            } else {
                statement.setString(++index, null);    
            }
            int updateCount = statement.executeUpdate();
            ok = true;
            if (updateCount != 1) {
                throw new StorageException(StorageExceptionType.NOT_FOUND);
            }
        } finally {
            storage.getConnectionPool().releaseConnection(connection, ok);
        }
    }

    private User get(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setUserName(resultSet.getString(UserMeta.user_name.name()));
        user.setDisplayName(resultSet.getString(UserMeta.display_name.name()));
        user.setEmail(resultSet.getString(UserMeta.email.name()));
        user.setRole(AdminRole.valueOf(resultSet.getString(UserMeta.role_.name())));
        user.setLastLogin(resultSet.getTimestamp(UserMeta.last_login.name()));
        user.setUpdated(resultSet.getTimestamp(UserMeta.updated.name()));
        return user;
    }

    public boolean exists(String email) throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            PreparedStatement statement = connection.prepareStatement(sqlMap.get(UserQuery.exists_username.name()));
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            boolean exists = resultSet.next();
            ok = true;
            return exists;
        } finally {
            storage.getConnectionPool().releaseConnection(connection, ok);
        }
    }

    public User get(String username) throws SQLException, StorageException {
        User adminUser = find(username);
        if (adminUser == null) {
            throw new StorageException(StorageExceptionType.NOT_FOUND);
        }
        return adminUser;
    }
    
    public User find(String username) throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    sqlMap.get(UserQuery.find_username.name()));
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            return get(resultSet);
        } finally {
            storage.getConnectionPool().releaseConnection(connection, ok);
        }
    }
    
    public User findByEmail(String email) throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    sqlMap.get(sqlMap.get(UserQuery.find_email.name())));
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            return get(resultSet);
        } finally {
            storage.getConnectionPool().releaseConnection(connection, ok);
        }
    }

    public void update(User AdminUser) throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    sqlMap.get(UserQuery.update.name()));
            statement.setString(1, AdminUser.getUserName());
            int updateCount = statement.executeUpdate();
            ok = true;
            if (updateCount != 1) {
                throw new SQLException();
            }
        } finally {
            storage.getConnectionPool().releaseConnection(connection, ok);
        }
    }

    public List<User> getList() throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            List<User> list = new ArrayList();
            PreparedStatement statement = connection.prepareStatement(sqlMap.get(UserQuery.list.name()));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                list.add(get(resultSet));
            }
            ok = true;
            return list;
        } finally {
            storage.getConnectionPool().releaseConnection(connection, ok);
        }
    }
}

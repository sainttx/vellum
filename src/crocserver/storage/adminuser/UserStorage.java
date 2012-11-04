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

    private AdminUser get(ResultSet resultSet) throws SQLException {
        AdminUser user = new AdminUser();
        user.setUserName(resultSet.getString(UserMeta.user_name.name()));
        user.setDisplayName(resultSet.getString(UserMeta.display_name.name()));
        user.setEmail(resultSet.getString(UserMeta.email.name()));
        user.setSubject(resultSet.getString(UserMeta.subject.name()));
        user.setRole(AdminRole.valueOf(resultSet.getString(UserMeta.role_.name())));
        user.setLastLogin(resultSet.getTimestamp(UserMeta.last_login.name()));
        user.setUpdated(resultSet.getTimestamp(UserMeta.updated.name()));
        user.setStored(true);
        return user;
    }

    public void insert(AdminUser user) throws SQLException, StorageException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            PreparedStatement statement = connection.prepareStatement(
                sqlMap.get(UserQuery.insert.name()));
            int index = 0;
            statement.setString(++index, user.getUserName());
            statement.setString(++index, user.getDisplayName());
            statement.setString(++index, user.getEmail());
            statement.setString(++index, user.getSubject());
            if (user.getRole() != null) {
                statement.setString(++index, user.getRole().name());
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

    public boolean exists(String userName) throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            PreparedStatement statement = connection.prepareStatement(sqlMap.get(UserQuery.exists_username.name()));
            statement.setString(1, userName);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            ok = true;
            return count == 1;
        } finally {
            storage.getConnectionPool().releaseConnection(connection, ok);
        }
    }

    public boolean existsEmail(String email) throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            PreparedStatement statement = connection.prepareStatement(sqlMap.get(UserQuery.exists_email.name()));
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            ok = true;
            return count == 1;
        } finally {
            storage.getConnectionPool().releaseConnection(connection, ok);
        }
    }
    
    public AdminUser get(String username) throws SQLException, StorageException {
        AdminUser adminUser = find(username);
        if (adminUser == null) {
            throw new StorageException(StorageExceptionType.NOT_FOUND, username);
        }
        return adminUser;
    }
    
    public AdminUser find(String username) throws SQLException {
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
    
    public AdminUser findEmail(String email) throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    sqlMap.get(UserQuery.find_email.name()));
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

    public void update(AdminUser user) throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    sqlMap.get(UserQuery.update_display_name_subject.name()));
            statement.setString(1, user.getDisplayName());
            statement.setString(2, user.getSubject());
            statement.setString(3, user.getUserName());
            int updateCount = statement.executeUpdate();
            ok = true;
            if (updateCount != 1) {
                throw new SQLException();
            }
        } finally {
            storage.getConnectionPool().releaseConnection(connection, ok);
        }
    }

    public List<AdminUser> getList() throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            List<AdminUser> list = new ArrayList();
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

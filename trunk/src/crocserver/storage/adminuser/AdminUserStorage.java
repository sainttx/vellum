/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package crocserver.storage.adminuser;

import crocserver.storage.CrocStorage;
import crocserver.storage.org.Org;
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
public class AdminUserStorage {

    static QueryMap sqlMap = new QueryMap(AdminUserStorage.class);
    CrocStorage storage;

    public AdminUserStorage(CrocStorage storage) {
        this.storage = storage;
    }
    public void insert(Org org, User adminUser) throws SQLException, StorageException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            PreparedStatement statement = connection.prepareStatement(
                sqlMap.get(AdminUserQuery.insert.name()));
            int index = 0;
            statement.setLong(++index, org.getId());
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
        user.setUserName(resultSet.getString(AdminUserMeta.user_name.name()));
        user.setDisplayName(resultSet.getString(AdminUserMeta.display_name.name()));
        user.setEmail(resultSet.getString(AdminUserMeta.email.name()));
        user.setOrgId(resultSet.getLong(AdminUserMeta.org_id.name()));
        user.setRole(AdminRole.valueOf(resultSet.getString(AdminUserMeta.role_.name())));
        user.setLastLogin(resultSet.getTimestamp(AdminUserMeta.last_login.name()));
        user.setInserted(resultSet.getTimestamp(AdminUserMeta.inserted.name()));
        return user;
    }

    public boolean exists(String email) throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            PreparedStatement statement = connection.prepareStatement(sqlMap.get(AdminUserQuery.exists_username.name()));
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
                    sqlMap.get(AdminUserQuery.find_username.name()));
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
                    sqlMap.get(sqlMap.get(AdminUserQuery.find_email.name())));
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
                    sqlMap.get(AdminUserQuery.update.name()));
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
            PreparedStatement statement = connection.prepareStatement(sqlMap.get(AdminUserQuery.list.name()));
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

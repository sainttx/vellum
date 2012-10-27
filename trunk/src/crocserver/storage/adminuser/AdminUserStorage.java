/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package crocserver.storage.adminuser;

import crocserver.storage.CrocStorage;
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

    public void insert(AdminUser adminUser) throws SQLException, StorageException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            PreparedStatement statement = connection.prepareStatement(
                sqlMap.get(AdminUserQuery.insert.name()));
            statement.setString(1, adminUser.getUsername());
            statement.setString(2, adminUser.getDisplayName());
            statement.setString(3, adminUser.getEmail());
            if (adminUser.getRole() != null) {
                statement.setString(4, adminUser.getRole().name());
            } else {
                statement.setString(4, null);                
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

    private AdminUser get(ResultSet resultSet) throws SQLException {
        AdminUser adminUser = new AdminUser();
        adminUser.setEmail(resultSet.getString("email"));
        adminUser.setUsername(resultSet.getString("username"));
        adminUser.setDisplayName(resultSet.getString("display_name"));
        adminUser.setRole(AdminRole.valueOf(resultSet.getString("role_")));
        adminUser.setLastLogin(resultSet.getTimestamp("last_login"));
        adminUser.setInserted(resultSet.getTimestamp("inserted"));
        return adminUser;
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

    public AdminUser get(String username) throws SQLException, StorageException {
        AdminUser adminUser = find(username);
        if (adminUser == null) {
            throw new StorageException(StorageExceptionType.NOT_FOUND);
        }
        return adminUser;
    }
    
    public AdminUser find(String username) throws SQLException {
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
    
    public AdminUser findByEmail(String email) throws SQLException {
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

    public void update(AdminUser AdminUser) throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    sqlMap.get(AdminUserQuery.update.name()));
            statement.setString(1, AdminUser.getUsername());
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
            PreparedStatement statement = connection.prepareStatement(sqlMap.get("list"));
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

    public void insertAll(List<AdminUser> adminUserList) throws SQLException, StorageException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            for (AdminUser adminUser : adminUserList) {
                insert(adminUser);
            }
            ok = true;
        } finally {
            storage.getConnectionPool().releaseConnection(connection, ok);
        }
    }
}

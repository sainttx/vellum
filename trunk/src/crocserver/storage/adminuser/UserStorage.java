/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package crocserver.storage.adminuser;

import crocserver.storage.common.CrocStorage;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
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
public class UserStorage {

    static Logr logger = LogrFactory.getLogger(UserStorage.class);
    static QueryMap sqlMap = new QueryMap(UserStorage.class);
    CrocStorage storage;

    public UserStorage(CrocStorage storage) {
        this.storage = storage;
    }

    public void validate() throws SQLException {
        ConnectionEntry connection = storage.getConnectionPool().takeEntry();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    sqlMap.get(UserQuery.validate.name()));
            ResultSet resultSet = statement.executeQuery();
            List<String> columnNameList = RowSets.getColumnNameList(resultSet.getMetaData());
            for (Enum columnNameEnum : UserMeta.values()) {
                String columnName = columnNameEnum.name().toUpperCase();
                logger.info("validate", columnName);        
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
    
    private AdminUser get(ResultSet resultSet) throws SQLException {
        AdminUser user = new AdminUser();
        user.setUserName(resultSet.getString(UserMeta.user_name.name()));
        user.setFirstName(resultSet.getString(UserMeta.first_name.name()));
        user.setLastName(resultSet.getString(UserMeta.last_name.name()));
        user.setDisplayName(resultSet.getString(UserMeta.display_name.name()));
        user.setEmail(resultSet.getString(UserMeta.email.name()));
        user.setSubject(resultSet.getString(UserMeta.subject.name()));
        user.setCert(resultSet.getString(UserMeta.cert.name()));
        user.setSecret(resultSet.getString(UserMeta.secret.name()));
        user.setRole(AdminRole.valueOf(resultSet.getString(UserMeta.role_.name())));
        user.setLoginTime(resultSet.getTimestamp(UserMeta.login.name()));
        user.setLogoutTime(resultSet.getTimestamp(UserMeta.logout.name()));
        user.setUpdated(resultSet.getTimestamp(UserMeta.updated.name()));
        user.setStored(true);
        return user;
    }

    public void insert(AdminUser user) throws SQLException, StorageException {
        ConnectionEntry connection = storage.getConnectionPool().takeEntry();
        try {
            PreparedStatement statement = connection.prepareStatement(
                sqlMap.get(UserQuery.insert.name()));
            int index = 0;
            statement.setString(++index, user.getUserName());
            statement.setString(++index, user.getFirstName());
            statement.setString(++index, user.getLastName());
            statement.setString(++index, user.getDisplayName());
            statement.setString(++index, user.getEmail());
            statement.setString(++index, user.getSubject());
            statement.setString(++index, user.getSecret());
            if (user.getRole() != null) {
                statement.setString(++index, user.getRole().name());
            } else {
                statement.setString(++index, null);    
            }
            statement.setTimestamp(++index, new Timestamp(user.getLoginTime().getTime()));
            int updateCount = statement.executeUpdate();
            connection.setOk(true);
            if (updateCount != 1) {
                throw new StorageException(StorageExceptionType.NOT_FOUND);
            }
        } finally {
            storage.getConnectionPool().releaseConnection(connection);
        }
    }

    public boolean exists(String userName) throws SQLException {
        ConnectionEntry connection = storage.getConnectionPool().takeEntry();
        try {
            PreparedStatement statement = connection.prepareStatement(sqlMap.get(UserQuery.exists_username.name()));
            statement.setString(1, userName);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            connection.setOk(true);
            return count == 1;
        } finally {
            storage.getConnectionPool().releaseConnection(connection);
        }
    }

    public boolean existsEmail(String email) throws SQLException {
        ConnectionEntry connection = storage.getConnectionPool().takeEntry();
        try {
            PreparedStatement statement = connection.prepareStatement(sqlMap.get(UserQuery.exists_email.name()));
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            connection.setOk(true);
            return count > 0;
        } finally {
            storage.getConnectionPool().releaseConnection(connection);
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
        ConnectionEntry connection = storage.getConnectionPool().takeEntry();
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
            storage.getConnectionPool().releaseConnection(connection);
        }
    }
    
    public AdminUser findEmail(String email) throws SQLException {
        ConnectionEntry connection = storage.getConnectionPool().takeEntry();
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
            storage.getConnectionPool().releaseConnection(connection);
        }
    }

    public void update(AdminUser user) throws SQLException {
        ConnectionEntry connection = storage.getConnectionPool().takeEntry();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    sqlMap.get(UserQuery.update_display_name.name()));
            int index = 0;
            statement.setString(++index, user.getDisplayName());
            statement.setString(++index, user.getUserName());
            int updateCount = statement.executeUpdate();
            connection.setOk(true);
            if (updateCount != 1) {
                throw new SQLException();
            }
        } finally {
            storage.getConnectionPool().releaseConnection(connection);
        }
    }

    public void updateSecret(AdminUser user) throws SQLException {
        ConnectionEntry connection = storage.getConnectionPool().takeEntry();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    sqlMap.get(UserQuery.update_secret.name()));
            statement.setString(1, user.getSecret());
            statement.setString(2, user.getUserName());
            int updateCount = statement.executeUpdate();
            connection.setOk(true);
            if (updateCount != 1) {
                throw new SQLException();
            }
        } finally {
            storage.getConnectionPool().releaseConnection(connection);
        }
    }

    public void updateCert(AdminUser user) throws SQLException {
        ConnectionEntry connection = storage.getConnectionPool().takeEntry();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    sqlMap.get(UserQuery.update_cert.name()));
            statement.setString(1, user.getSubject());
            statement.setString(2, user.getCert());
            statement.setString(3, user.getUserName());
            int updateCount = statement.executeUpdate();
            connection.setOk(true);
            if (updateCount != 1) {
                throw new SQLException();
            }
        } finally {
            storage.getConnectionPool().releaseConnection(connection);
        }
    }
    
    public void updateLogin(AdminUser user) throws SQLException {
        ConnectionEntry connection = storage.getConnectionPool().takeEntry();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    sqlMap.get(UserQuery.update_login.name()));
            statement.setTimestamp(1, new Timestamp(user.getLoginTime().getTime()));
            statement.setString(2, user.getUserName());
            int updateCount = statement.executeUpdate();
            connection.setOk(true);
            if (updateCount != 1) {
                throw new SQLException();
            }
        } finally {
            storage.getConnectionPool().releaseConnection(connection);
        }
    }
    
    public void updateLogout(AdminUser user) throws SQLException {
        ConnectionEntry connection = storage.getConnectionPool().takeEntry();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    sqlMap.get(UserQuery.update_logout.name()));
            statement.setTimestamp(1, new Timestamp(user.getLogoutTime().getTime()));
            statement.setString(2, user.getUserName());
            int updateCount = statement.executeUpdate();
            connection.setOk(true);
            if (updateCount != 1) {
                throw new SQLException();
            }
        } finally {
            storage.getConnectionPool().releaseConnection(connection);
        }
    }
    
    public List<AdminUser> getList() throws SQLException {
        ConnectionEntry connection = storage.getConnectionPool().takeEntry();
        try {
            List<AdminUser> list = new ArrayList();
            PreparedStatement statement = connection.prepareStatement(sqlMap.get(UserQuery.list.name()));
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

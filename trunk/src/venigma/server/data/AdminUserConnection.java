/*
 * Copyright Evan Summers
 * 
 */
package venigma.server.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import vellum.sql.common.QueryMap;
import venigma.common.AdminUser;

/**
 *
 * @author evan
 */
public class AdminUserConnection {
    static QueryMap sqlMap = new QueryMap(AdminUserConnection.class);
    Connection connection;

    public AdminUserConnection(Connection connection) {
        this.connection = connection;
    }
        
    public AdminUser get(ResultSet resultSet) throws SQLException {
        AdminUser adminUser = new AdminUser();
        adminUser.setEmail(resultSet.getString("email"));
        adminUser.setUsername(resultSet.getString("username"));
        adminUser.setPasswordHash(resultSet.getString("password_hash"));
        adminUser.setPasswordSalt(resultSet.getString("password_salt"));
        adminUser.setLastLogin(resultSet.getTimestamp("last_login"));
        return adminUser;        
    }

    public boolean exists(String email) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sqlMap.get("exists"));
        statement.setString(1, email);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }
    
    public AdminUser find(String email) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sqlMap.get("find by email"));
        statement.setString(1, email);
        ResultSet resultSet = statement.executeQuery();
        if (!resultSet.next()) {
            return null;
        }
        return get(resultSet);
    }
    
    public void insert(AdminUser AdminUser) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sqlMap.get("insert"));
        statement.setString(1, AdminUser.getUsername());
        int updateCount = statement.executeUpdate();        
        if (updateCount != 1) {
            throw new SQLException();            
        }
    }

    public void update(AdminUser AdminUser) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sqlMap.get("update"));
        statement.setString(1, AdminUser.getUsername());
        int updateCount = statement.executeUpdate();        
        if (updateCount != 1) {
            throw new SQLException();            
        }
    }
    
    public List<AdminUser> getList() throws SQLException {
        List<AdminUser> list = new ArrayList();
        PreparedStatement statement = connection.prepareStatement(sqlMap.get("list all"));
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            list.add(get(resultSet));
        }
        return list;
    }

    public void insertAll(List<AdminUser> adminUserList) {
    }
}

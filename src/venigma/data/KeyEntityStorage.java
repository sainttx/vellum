/*
 * Copyright Evan Summers
 * 
 */
package venigma.data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import vellum.sql.common.QueryMap;
import vellum.util.Base64;

/**
 *
 * @author evan
 */
public class KeyEntityStorage {
    static QueryMap sqlMap = new QueryMap(KeyEntityStorage.class);
    Connection connection;

    public KeyEntityStorage(Connection connection) {
        this.connection = connection;
    }
        
    public KeyEntity build(ResultSet resultSet) throws SQLException {
        KeyEntity keyInfo = new KeyEntity();
        keyInfo.setKeyAlias(resultSet.getString("key_alias"));
        keyInfo.setKeySize(resultSet.getInt("key_size"));
        keyInfo.setEncryptedKey(Base64.decode(resultSet.getString("data_")));
        return keyInfo;        
    }

    public boolean exists(String keyAlias) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sqlMap.get("exists"));
        statement.setString(1, keyAlias);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }
    
    public KeyEntity find(String keyAlias) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sqlMap.get("find by email"));
        statement.setString(1, keyAlias);
        ResultSet resultSet = statement.executeQuery();
        if (!resultSet.next()) {
            return null;
        }
        return build(resultSet);
    }
    
    public void insert(KeyEntity keyInfo) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sqlMap.get("insert"));
        statement.setString(1, keyInfo.getKeyAlias());
        statement.setInt(2, keyInfo.getKeySize());
        statement.setString(3, Base64.encode(keyInfo.getEncryptedKey()));
        int updateCount = statement.executeUpdate();        
        if (updateCount != 1) {
            throw new SQLException();            
        }
    }

    public void update(KeyEntity KeyInfo) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sqlMap.get("update"));
        statement.setString(1, KeyInfo.getKeyAlias());
        int updateCount = statement.executeUpdate();        
        if (updateCount != 1) {
            throw new SQLException();            
        }
    }
    
    public List<KeyEntity> getList() throws SQLException {
        List<KeyEntity> list = new ArrayList();
        PreparedStatement statement = connection.prepareStatement(sqlMap.get("list all"));
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            list.add(build(resultSet));
        }
        return list;
    }

    public void insertAll(List<KeyEntity> keyInfoList) throws SQLException {
        for (KeyEntity keyInfo : keyInfoList) {
            insert(keyInfo);
        }
    }
}

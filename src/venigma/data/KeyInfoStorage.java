/*
 * Copyright Evan Summers
 * 
 */
package venigma.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.SecretKey;
import vellum.logger.Logr;
import vellum.logger.LogrFactory;
import vellum.sql.common.QueryMap;
import vellum.util.Base64;
import venigma.server.storage.StorageException;
import venigma.server.storage.StorageExceptionType;

/**
 *
 * @author evan
 */
public class KeyInfoStorage {
    static QueryMap sqlMap = new QueryMap(KeyInfoStorage.class);
    static Logr logger = LogrFactory.getLogger(KeyInfoStorage.class);
    Connection connection;

    public KeyInfoStorage(Connection connection) {
        this.connection = connection;
    }
        
    public KeyInfo build(ResultSet resultSet) throws SQLException {
        KeyInfo keyInfo = new KeyInfo();
        keyInfo.setKeyAlias(resultSet.getString("key_alias"));
        keyInfo.setKeySize(resultSet.getInt("key_size"));
        keyInfo.setKeyRevisionNumber(resultSet.getInt("revision_number"));
        keyInfo.setEncryptedKey(Base64.decode(resultSet.getString("data_")));
        return keyInfo;        
    }

    public boolean exists(KeyId keyId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sqlMap.get("exists"));
        statement.setString(1, keyId.getKeyAlias());
        statement.setInt(2, keyId.getKeyRevisionNumber());
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next() && resultSet.getBoolean(1);
    }
    
    public KeyInfo find(KeyId keyId) throws Exception {
        PreparedStatement statement = connection.prepareStatement(sqlMap.get("find"));
        statement.setString(1, keyId.getKeyAlias());
        statement.setInt(2, keyId.getKeyRevisionNumber());
        ResultSet resultSet = statement.executeQuery();
        if (!resultSet.next()) {
            throw new StorageException(StorageExceptionType.KEY_NOT_FOUND);
        }
        return build(resultSet);
    }
    
    public void insert(KeyInfo keyInfo) throws Exception {
        PreparedStatement statement = connection.prepareStatement(sqlMap.get("insert"));
        statement.setString(1, keyInfo.getKeyAlias());
        statement.setInt(2, keyInfo.getKeySize());
        statement.setInt(3, keyInfo.getKeyRevisionNumber());
        statement.setString(3, Base64.encode(keyInfo.getEncryptedKey()));
        int updateCount = statement.executeUpdate();        
        if (updateCount != 1) {
            throw new StorageException(StorageExceptionType.KEY_NOT_INSERTED);
        }
    }

    public void delete(KeyId keyId) throws Exception {
        PreparedStatement statement = connection.prepareStatement(sqlMap.get("delete"));
        statement.setString(1, keyId.getKeyAlias());
        int updateCount = statement.executeUpdate();        
        if (updateCount != 1) {
            throw new StorageException(StorageExceptionType.KEY_NOT_DELETED);
        }
    }
    
    public List<KeyInfo> getList() throws SQLException {
        List<KeyInfo> list = new ArrayList();
        PreparedStatement statement = connection.prepareStatement(sqlMap.get("list"));
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            list.add(build(resultSet));
        }
        return list;
    }

       
}

/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package crocserver.storage.org;

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
public class OrgStorage {

    static QueryMap sqlMap = new QueryMap(OrgStorage.class);
    CrocStorage storage;

    public OrgStorage(CrocStorage storage) {
        this.storage = storage;
    }

    public void insert(Org organisation) throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            PreparedStatement statement = connection.prepareStatement(
                sqlMap.get(OrgQuery.insert.name()));
            statement.setString(1, organisation.getName());
            statement.setString(2, organisation.getDisplayName());
            int updateCount = statement.executeUpdate();
            ok = true;
            if (updateCount != 1) {
                throw new SQLException();
            }
        } finally {
            storage.getConnectionPool().releaseConnection(connection, ok);
        }
    }

    private Org get(ResultSet resultSet) throws SQLException {
        Org organisation = new Org();
        organisation.setName(resultSet.getString("name"));
        organisation.setDisplayName(resultSet.getString("display_name"));
        organisation.setInserted(resultSet.getTimestamp("inserted"));
        return organisation;
    }

    public boolean exists(String email) throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            PreparedStatement statement = connection.prepareStatement(sqlMap.get(OrgQuery.exists.name()));
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            boolean exists = resultSet.next();
            ok = true;
            return exists;
        } finally {
            storage.getConnectionPool().releaseConnection(connection, ok);
        }
    }

    public Org get(String name) throws SQLException {
        Org org = find(name);
        if (org == null) {
            throw new StorageException(StorageExceptionType.NOT_FOUND);
        }
        return org;
    }
    
    public Org find(String name) throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    sqlMap.get(OrgQuery.find_name.name()));
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            return get(resultSet);
        } finally {
            storage.getConnectionPool().releaseConnection(connection, ok);
        }
    }
    
    public Org find(Long id) throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    sqlMap.get(sqlMap.get(OrgQuery.find_id.name())));
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            return get(resultSet);
        } finally {
            storage.getConnectionPool().releaseConnection(connection, ok);
        }
    }

    public void update(Org Organisation) throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    sqlMap.get(OrgQuery.update.name()));
            statement.setString(1, Organisation.getName());
            int updateCount = statement.executeUpdate();
            ok = true;
            if (updateCount != 1) {
                throw new SQLException();
            }
        } finally {
            storage.getConnectionPool().releaseConnection(connection, ok);
        }
    }

    public List<Org> getList() throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            List<Org> list = new ArrayList();
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
}

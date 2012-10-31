/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package crocserver.storage.org;

import crocserver.storage.common.CrocStorage;
import crocserver.storage.common.AbstractEntityStorage;
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
public class OrgStorage extends AbstractEntityStorage<Long, Org> {

    static QueryMap sqlMap = new QueryMap(OrgStorage.class);
    CrocStorage storage;

    public OrgStorage(CrocStorage storage) {
        this.storage = storage;
    }

    private Org get(ResultSet resultSet) throws SQLException {
        Org org = new Org();
        org.setId(resultSet.getLong(OrgMeta.org_id.name()));
        org.setName(resultSet.getString(OrgMeta.org_name.name()));
        org.setDisplayName(resultSet.getString(OrgMeta.display_name.name()));
        org.setUrl(resultSet.getString(OrgMeta.url.name()));
        org.setUpdated(resultSet.getTimestamp(OrgMeta.updated.name()));
        org.setUpdatedBy(resultSet.getString(OrgMeta.updated_by.name()));
        org.setStored(true);
        return org;
    }

    public long insert(Org org) throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            PreparedStatement statement = connection.prepareStatement(
                sqlMap.get(OrgQuery.insert.name()));
            int index = 0;
            statement.setString(++index, org.getName());
            statement.setString(++index, org.getUrl());
            statement.setString(++index, org.getDisplayName());
            statement.setString(++index, org.getUpdatedBy());
            int updateCount = statement.executeUpdate();
            ok = true;
            if (updateCount != 1) {
                throw new SQLException();
            }
            ResultSet resultSet = statement.getGeneratedKeys();
            if (!resultSet.next()) {
                throw new SQLException();    
            }
            long id = resultSet.getLong(1);
            org.setId(id);
            org.setStored(true);
            ok = true;
            return id;
        } finally {
            storage.getConnectionPool().releaseConnection(connection, ok);
        }
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

    public Org get(long id) throws SQLException {
        Org org = find(id);
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
            ok = true;
            return get(resultSet);
        } finally {
            storage.getConnectionPool().releaseConnection(connection, ok);
        }
    }
    
    @Override
    public Org find(Long id) throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    sqlMap.get(OrgQuery.find_id.name()));
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            ok = true;
            return get(resultSet);
        } finally {
            storage.getConnectionPool().releaseConnection(connection, ok);
        }
    }

    public void update(Org org) throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    sqlMap.get(OrgQuery.update_url_display_name_where_org_name.name()));
            statement.setString(1, org.getUrl());
            statement.setString(2, org.getDisplayName());
            statement.setString(3, org.getName());
            int updateCount = statement.executeUpdate();
            ok = true;
            if (updateCount != 1) {
                throw new SQLException();
            }
            ok = true;
        } finally {
            storage.getConnectionPool().releaseConnection(connection, ok);
        }
    }

    public List<Org> getList() throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            List<Org> list = new ArrayList();
            PreparedStatement statement = connection.prepareStatement(sqlMap.get(OrgQuery.list.name()));
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

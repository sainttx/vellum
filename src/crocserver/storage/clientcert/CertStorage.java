/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package crocserver.storage.clientcert;

import crocserver.storage.common.CrocStorage;
import java.security.cert.X509Certificate;
import java.sql.*;
import vellum.storage.StorageExceptionType;
import java.util.ArrayList;
import java.util.List;
import vellum.query.QueryMap;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.storage.ConnectionEntry;
import vellum.storage.StorageException;

/**
 *
 * @author evan
 */
public class CertStorage {

    static QueryMap sqlMap = new QueryMap(CertStorage.class);
    Logr logger = LogrFactory.getLogger(CertStorage.class);
    CrocStorage storage;

    public CertStorage(CrocStorage storage) {
        this.storage = storage;
    }

    private Cert build(ResultSet resultSet) throws SQLException {
        Cert cert = new Cert();
        cert.setSubject(resultSet.getString(CertMeta.subject.name()));
        cert.setCert(resultSet.getString(CertMeta.cert.name()));
        cert.setUpdated(resultSet.getTimestamp(CertMeta.updated.name()));
        cert.setUpdatedBy(resultSet.getString(CertMeta.updated_by.name()));
        cert.setStored(true);
        return cert;
    }

    public void insert(Cert cert) throws StorageException, SQLException {
        ConnectionEntry connection = storage.getConnectionPool().takeEntry();
        try {
            PreparedStatement statement = connection.prepareStatement(sqlMap.get(CertQuery.insert.name()));
            int index = 0;
            statement.setString(++index, cert.getSubject());
            statement.setString(++index, cert.getCert());
            statement.setString(++index, cert.getUpdatedBy());
            int insertCount = statement.executeUpdate();
            if (insertCount != 1) {
                throw new StorageException(StorageExceptionType.NOT_INSERTED);
            }
            ResultSet generatedKeys = statement.getGeneratedKeys();
            generatedKeys.next();
            cert.setId(generatedKeys.getLong((1)));
            cert.setStored(true);
            connection.setOk(true);
        } finally {
            storage.getConnectionPool().releaseConnection(connection);
        }
    }

    public void update(Cert cert) throws SQLException {
        ConnectionEntry connection = storage.getConnectionPool().takeEntry();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    sqlMap.get(CertQuery.update.name()));
            int index = 0;
            statement.setString(++index, cert.getCert());
            statement.setString(++index, cert.getUpdatedBy());
            statement.setString(++index, cert.getSubject());
            int updateCount = statement.executeUpdate();
            if (updateCount != 1) {
                throw new StorageException(StorageExceptionType.UPDATE_COUNT);
            }
        } finally {
            storage.getConnectionPool().releaseConnection(connection);
        }
    }

    public Cert find(long id) throws SQLException {
        ConnectionEntry connection = storage.getConnectionPool().takeEntry();
        try {
            PreparedStatement statement = connection.prepareStatement(sqlMap.get(CertQuery.find_id.name()));
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            return build(resultSet);
        } finally {
            storage.getConnectionPool().releaseConnection(connection);
        }
    }

    public Cert find(String subject) throws SQLException {
        ConnectionEntry connection = storage.getConnectionPool().takeEntry();
        try {
            PreparedStatement statement = connection.prepareStatement(sqlMap.get(CertQuery.find_subject.name()));
            statement.setString(1, subject);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            return build(resultSet);
        } finally {
            storage.getConnectionPool().releaseConnection(connection);
        }
    }
    
    public boolean existsEnabled(String subject) throws SQLException {
        ConnectionEntry connection = storage.getConnectionPool().takeEntry();
        try {
            PreparedStatement statement = connection.prepareStatement(sqlMap.get(CertQuery.enabled.name()));
            statement.setString(1, subject);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            connection.setOk(true);
            return count > 0;
        } finally {
            storage.getConnectionPool().releaseConnection(connection);
        }
    }
    
    public List<Cert> getList() throws SQLException {
        ConnectionEntry connection = storage.getConnectionPool().takeEntry();
        try {
            List<Cert> list = new ArrayList();
            PreparedStatement statement = connection.prepareStatement(
                    sqlMap.get(CertQuery.list.name()));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                list.add(build(resultSet));
            }
            connection.setOk(true);
            return list;
        } finally {
            storage.getConnectionPool().releaseConnection(connection);
        }
    }

    public void save(X509Certificate x509Cert, String updatedBy) throws SQLException {
        Cert clientCert = find(x509Cert.getSubjectDN().getName());
        if (clientCert == null) {
            clientCert = new Cert();
        }
        clientCert.setCert(x509Cert);
        clientCert.setUpdatedBy(updatedBy);
        if (clientCert.isStored()) {
            update(clientCert);
        } else {
            insert(clientCert);
        }
    }
}

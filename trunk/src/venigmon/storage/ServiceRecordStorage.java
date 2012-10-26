/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package venigmon.storage;

import bizstat.entity.Host;
import bizstat.entity.HostServiceKey;
import bizstat.entity.Service;
import bizstat.entity.ServiceRecord;
import bizstat.enumtype.ServiceStatus;
import java.sql.*;
import vellum.storage.StorageExceptionType;
import java.util.ArrayList;
import java.util.List;
import vellum.query.QueryMap;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.storage.StorageException;

/**
 *
 * @author evan
 */
public class ServiceRecordStorage {

    static QueryMap sqlMap = new QueryMap(ServiceRecordStorage.class);
    Logr logger = LogrFactory.getLogger(ServiceRecordStorage.class);
    CrocStorage storage;

    public ServiceRecordStorage(CrocStorage storage) {
        this.storage = storage;
    }

    private ServiceRecord build(ResultSet resultSet) throws SQLException {
        ServiceRecord serviceRecord = new ServiceRecord(resultSet.getString("host_"),                
                resultSet.getString("service"));
        serviceRecord.setId(resultSet.getLong("service_record_id"));
        serviceRecord.setDispatchedMillis(getTimestamp(resultSet, "dispatched_time", 0));
        serviceRecord.setTimestampMillis(getTimestamp(resultSet, "time_", 0));
        serviceRecord.setNotifiedMillis(getTimestamp(resultSet, "notified_time", 0));
        serviceRecord.setServiceStatus(ServiceStatus.valueOf(resultSet.getString("status")));
        serviceRecord.setOutText(resultSet.getString("out_"));
        return serviceRecord;
    }
    
    private long getTimestamp(ResultSet resultSet, String columnName, long defaultValue) throws SQLException {
        Timestamp timestamp = resultSet.getTimestamp(columnName);
        if (timestamp == null) {
            return defaultValue;
        }
        return timestamp.getTime();
        
    }

    public ServiceRecord find(long id) throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            PreparedStatement statement = connection.prepareStatement(sqlMap.get("find (id)"));
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            return build(resultSet);
        } finally {
            storage.getConnectionPool().releaseConnection(connection, ok);
        }
    }
    
    public ServiceRecord find(String hostName, String serviceName) throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            PreparedStatement statement = connection.prepareStatement(sqlMap.get("find (host, service)"));
            statement.setString(1, hostName);
            statement.setString(2, serviceName);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            return build(resultSet);
        } finally {
            storage.getConnectionPool().releaseConnection(connection, ok);
        }
    }
    
    public void insert(ServiceRecord serviceRecord) throws StorageException, SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            PreparedStatement statement = connection.prepareStatement(sqlMap.get("insert"));
            statement.setString(1, serviceRecord.getHostName());
            statement.setString(2, serviceRecord.getServiceName());
            if (serviceRecord.getServiceStatus() == null) {
                statement.setString(3, null);
            } else {
                statement.setString(3, serviceRecord.getServiceStatus().name());                
            }
            statement.setTimestamp(4, new Timestamp(serviceRecord.getTimestamp()));
            if (serviceRecord.getDispatchedMillis() == 0) {
                statement.setTimestamp(5, null);                
            } else {
                statement.setTimestamp(5, new Timestamp(serviceRecord.getDispatchedMillis()));
            }
            if (serviceRecord.getNotifiedMillis() > 0) {
                statement.setTimestamp(6, new Timestamp(serviceRecord.getNotifiedMillis()));
            } else {
                statement.setTimestamp(6, null);                
            }
            statement.setInt(7, serviceRecord.getExitCode());
            statement.setString(8, serviceRecord.getOutText());
            statement.setString(9, serviceRecord.getErrText());
            int insertCount = statement.executeUpdate();
            if (insertCount != 1) {
                throw new StorageException(StorageExceptionType.NOT_INSERTED);
            }
            ok = true;
        } finally {
            storage.getConnectionPool().releaseConnection(connection, ok);
        }
    }

    public List<ServiceRecord> getList(HostServiceKey key) throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            List<ServiceRecord> list = new ArrayList();
            PreparedStatement statement = connection.prepareStatement(sqlMap.get("list by time"));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                list.add(build(resultSet));
            }
            ok = true;
            return list;
        } finally {
            storage.getConnectionPool().releaseConnection(connection, ok);
        }
    }

    public List<ServiceRecord> getList() throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            List<ServiceRecord> list = new ArrayList();
            PreparedStatement statement = connection.prepareStatement(sqlMap.get("list by time"));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                list.add(build(resultSet));
            }
            ok = true;
            return list;
        } finally {
            storage.getConnectionPool().releaseConnection(connection, ok);
        }
    }    
}

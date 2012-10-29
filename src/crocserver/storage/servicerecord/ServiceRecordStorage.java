/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package crocserver.storage.servicerecord;

import bizstat.enumtype.ServiceStatus;
import crocserver.storage.common.CrocStorage;
import crocserver.storage.org.Org;
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
        ServiceRecord serviceRecord = new ServiceRecord(resultSet.getString(ServiceRecordMeta.host_name.name()),
                resultSet.getString(ServiceRecordMeta.service_name.name()));
        serviceRecord.setId(resultSet.getLong(ServiceRecordMeta.service_record_id.name()));
        serviceRecord.setDispatchedMillis(getTimestamp(resultSet, ServiceRecordMeta.dispatched_time.name(), 0));
        serviceRecord.setTimestampMillis(getTimestamp(resultSet, ServiceRecordMeta.time_.name(), 0));
        serviceRecord.setNotifiedMillis(getTimestamp(resultSet, ServiceRecordMeta.notified_time.name(), 0));
        serviceRecord.setServiceStatus(ServiceStatus.valueOf(resultSet.getString(ServiceRecordMeta.status.name())));
        serviceRecord.setNotify(resultSet.getBoolean(ServiceRecordMeta.notify.name()));
        serviceRecord.setOutText(resultSet.getString(ServiceRecordMeta.out_.name()));
        return serviceRecord;
    }

    public void insert(Org org, ServiceRecord serviceRecord) throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            PreparedStatement statement = connection.prepareStatement(sqlMap.get(ServiceRecordQuery.insert.name()));
            int index = 0;
            statement.setLong(++index, org.getId());
            statement.setString(++index, serviceRecord.getHostName());
            statement.setString(++index, serviceRecord.getServiceName());
            if (serviceRecord.getServiceStatus() == null) {
                statement.setString(++index, null);
            } else {
                statement.setString(++index, serviceRecord.getServiceStatus().name());
            }
            statement.setTimestamp(++index, new Timestamp(serviceRecord.getTimestamp()));
            if (serviceRecord.getDispatchedMillis() == 0) {
                statement.setTimestamp(++index, null);
            } else {
                statement.setTimestamp(++index, new Timestamp(serviceRecord.getDispatchedMillis()));
            }
            if (serviceRecord.getNotifiedMillis() > 0) {
                statement.setTimestamp(++index, new Timestamp(serviceRecord.getNotifiedMillis()));
            } else {
                statement.setTimestamp(++index, null);
            }
            statement.setInt(++index, serviceRecord.getExitCode());
            statement.setString(++index, serviceRecord.getOutText());
            statement.setString(++index, serviceRecord.getErrText());
            int insertCount = statement.executeUpdate();
            if (insertCount != 1) {
                throw new StorageException(StorageExceptionType.NOT_INSERTED);
            }
            ok = true;
        } finally {
            storage.getConnectionPool().releaseConnection(connection, ok);
        }
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
            PreparedStatement statement = connection.prepareStatement(sqlMap.get(ServiceRecordQuery.find_id.name()));
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
    
    public ServiceRecord findLatest(long orgId, String hostName, String serviceName) throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            PreparedStatement statement = connection.prepareStatement(sqlMap.get(ServiceRecordQuery.find_latest.name()));
            statement.setLong(1, orgId);
            statement.setString(2, hostName);
            statement.setString(3, serviceName);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            ServiceRecord serviceRecord = build(resultSet);
            if (resultSet.next()) {
                throw new StorageException(StorageExceptionType.MULTIPLE_RESULTS);
            }
            return serviceRecord;
        } finally {
            storage.getConnectionPool().releaseConnection(connection, ok);
        }
    }
    
    public List<ServiceRecord> getList() throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            List<ServiceRecord> list = new ArrayList();
            PreparedStatement statement = connection.prepareStatement(sqlMap.get(ServiceRecordQuery.list.name()));
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

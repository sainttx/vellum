/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package venigmon.storage;

import bizstat.entity.Host;
import bizstat.entity.HostServiceKey;
import bizstat.entity.Service;
import bizstat.entity.StatusInfo;
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
public class StatusInfoStorage {

    static QueryMap sqlMap = new QueryMap(StatusInfoStorage.class);
    Logr logger = LogrFactory.getLogger(StatusInfoStorage.class);
    VenigmonStorage storage;

    public StatusInfoStorage(VenigmonStorage storage) {
        this.storage = storage;
    }

    private StatusInfo build(ResultSet resultSet) throws SQLException {
        StatusInfo statusInfo = new StatusInfo(
                storage.get(Host.class, resultSet.getString("host_")),
                storage.get(Service.class, resultSet.getString("service")),
                resultSet.getTimestamp("dispatched_time").getTime()
                );
        statusInfo.setTimestampMillis(getTimestamp(resultSet, "time_", 0));
        statusInfo.setNotifiedMillis(getTimestamp(resultSet, "notified_time", 0));
        statusInfo.setServiceStatus(ServiceStatus.valueOf(resultSet.getString("status")));
        statusInfo.setOutText(resultSet.getString("out_"));
        return statusInfo;
    }
    
    private long getTimestamp(ResultSet resultSet, String columnName, long defaultValue) throws SQLException {
        Timestamp timestamp = resultSet.getTimestamp(columnName);
        if (timestamp == null) {
            return defaultValue;
        }
        return timestamp.getTime();
        
    }

    public void insert(StatusInfo statusInfo) throws Exception {
        Connection connection = storage.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(sqlMap.get("insert"));
            statement.setString(1, statusInfo.getHost().getName());
            statement.setString(2, statusInfo.getService().getName());
            statement.setString(3, statusInfo.getServiceStatus().name());
            statement.setTimestamp(4, new Timestamp(statusInfo.getTimestamp()));
            statement.setTimestamp(5, new Timestamp(statusInfo.getDispatchedMillis()));
            if (statusInfo.getNotifiedMillis() > 0) {
                statement.setTimestamp(6, new Timestamp(statusInfo.getNotifiedMillis()));
            } else {
                statement.setTimestamp(6, null);                
            }
            statement.setInt(7, statusInfo.getExitCode());
            statement.setString(8, statusInfo.getOutText());
            statement.setString(9, statusInfo.getErrText());
            int insertCount = statement.executeUpdate();
            if (insertCount != 1) {
                throw new StorageException(StorageExceptionType.NOT_INSERTED);
            }
        } finally {
            storage.releaseConnection(connection);
        }
    }

    public List<StatusInfo> getList(HostServiceKey key) throws SQLException {
        Connection connection = storage.getConnection();
        try {
            List<StatusInfo> list = new ArrayList();
            PreparedStatement statement = connection.prepareStatement(sqlMap.get("list"));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                list.add(build(resultSet));
            }
            return list;
        } finally {
            storage.releaseConnection(connection);
        }
    }

    public List<StatusInfo> getList() throws SQLException {
        Connection connection = storage.getConnection();
        try {
            List<StatusInfo> list = new ArrayList();
            PreparedStatement statement = connection.prepareStatement(sqlMap.get("list"));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                list.add(build(resultSet));
            }
            return list;
        } finally {
            storage.releaseConnection(connection);
        }
    }
    
}

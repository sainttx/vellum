/*
 * Contributed (2013) by Evan Summers via https://code.google.com/p/vellum 2011, iPay (Pty) Ltd
 */

package vellum.query;

import vellum.exception.Exceptions;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.RowSet;
import vellum.query.QueryInfo;

/**
 *
 * @author evan.summers
 */
public class ResultList<K> {
    List<String> columnNameList;
    List<String> columnTypeNameList = new ArrayList();
    Map<String, String> columnTypeNameMap = new HashMap();
    List<Object[]> rowList = new ArrayList();

    public ResultList(QueryInfo queryInfo) {
        try {
            RowSet rowSet = RowSets.getRowSet(queryInfo);
            ResultSetMetaData md = rowSet.getMetaData();
            columnNameList = RowSets.getColumnNameList(md);
            for (int i = 0; i < md.getColumnCount(); i++) {
                int columnIndex = i + 1;
                String columnName = md.getColumnName(columnIndex);
                columnTypeNameList.add(md.getColumnTypeName(columnIndex));
                columnTypeNameMap.put(columnName, md.getColumnTypeName(columnIndex));
            }
            while (rowSet.next()) {
                Object[] array = new Object[columnNameList.size()];
                for (int i = 0; i < md.getColumnCount(); i++) {
                    int columnIndex = i + 1;
                    array[i] = rowSet.getObject(columnIndex);
                }
                rowList.add(array);
            }
            rowSet.close();
        } catch (Exception e) {
            throw Exceptions.newRuntimeException(e);
        }
    }

    public List<String> getColumnNameList() {
        return columnNameList;
    }

    public Map<String, String> getColumnTypeNameMap() {
        return columnTypeNameMap;
    }

    public List<Object[]> getRowList() {
        return rowList;
    }
}

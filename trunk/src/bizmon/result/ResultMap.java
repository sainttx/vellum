/*
 * Copyright 2011, iPay (Pty) Ltd, Evan Summers
 * Apache Software License 2.0
 * Supported by BizSwitch.net
 */
package bizmon.result;

import jdbc.query.QueryInfo;
import bizmon.exception.Exceptions;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.sql.RowSet;

/**
 *
 * @author evanx
 */
public class ResultMap<K> {

   String keyColumn;
   List<String> columnNameList;
   List<String> columnTypeNameList = new ArrayList();
   Map<String, String> columnTypeNameMap = new HashMap();
   List<Map> rowList = new ArrayList();
   Map<K, Map<String, Object>> rowMap = new TreeMap();
   int rowCount;

   public ResultMap(QueryInfo queryInfo, String keyColumn) {
      this(RowSets.getRowSet(queryInfo), keyColumn);
   }

   public ResultMap(RowSet rowSet, String keyColumn) {
      this.keyColumn = keyColumn;
      try {
         ResultSetMetaData md = rowSet.getMetaData();
         columnNameList = RowSets.getColumnNameList(md);
         for (int i = 1; i <= md.getColumnCount(); i++) {
            String columnName = md.getColumnName(i);
            columnTypeNameList.add(md.getColumnTypeName(i));
            columnTypeNameMap.put(columnName, md.getColumnTypeName(i));
         }
         rowSet.beforeFirst();
         while (rowSet.next()) {
            Map map = new HashMap();
            for (String columnName : columnNameList) {
               map.put(columnName, rowSet.getObject(columnName));
            }
            rowMap.put((K) rowSet.getObject(keyColumn), map);
            rowList.add(map);
            rowCount++;
         }
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

   public Map<K, Map<String, Object>> getRowMap() {
      return rowMap;
   }

   public Map<String, Object> getColumnMap(K key) {
      return rowMap.get(key);
   }

   public Object getCell(K key, String columnName) {
      if (!rowMap.containsKey(key)) {
         if (true) {
            return null;
         }
         throw new NullPointerException(key.toString());
      }
      return rowMap.get(key).get(columnName);
   }

   public List<Map> getRowList() {
      return rowList;
   }

   @Override
   public String toString() {
      return String.format("rows %d, mapped %d", rowCount, rowMap.size());
   }


}

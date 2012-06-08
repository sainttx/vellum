/*
 * (c) Copyright 2011, iPay (Pty) Ltd, Evan Summers
 * Apache Software License 2.0
 * Supported by BizSwitch.net
 */
package vellum.sql.query;

import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.util.Args;
import java.util.HashMap;
import java.util.Map;
import javax.sql.RowSet;

/**
 *
 * @author evanx
 */
public class QueryInfo {

   static Logr logger = LogrFactory.getLogger(QueryInfo.class);
   String queryName;
   String parameterisedQuery;
   String query;
   String database;
   String user;
   String schema;
   String table;
   String column;
   RowSet rowSet;
   Map<String, String> parameters = new HashMap();

   public QueryInfo() {
   }

   public QueryInfo(String user, String query) {
      this.user = user;
      this.query = query;
      if (database == null) {
         throw new IllegalArgumentException(user);
      }
   }

   public QueryInfo(String queryName, String query, String database, String user) {
      this.queryName = queryName;
      this.query = query;
      this.database = database;
      this.user = user;
   }

   public boolean isExecutable() {
      return database != null && user != null && isSelect();
   }

   public boolean isSelect() {
      String trimmed = query.trim().toLowerCase();
      if (query.endsWith(";")) {
         query = query.substring(0, query.length() - 1);
      }
      if (!trimmed.startsWith("select")) {
         return false;
      }
      if (query.contains(";")) {
         return false;
      }
      if (query.contains("update ")) {
         return false;
      }
      if (query.contains("insert ")) {
         return false;
      }
      if (query.contains("delete ")) {
         return false;
      }
      return true;
   }

   public String getDatabase() {
      return database;
   }

   public void putAll(Map<String, String> parameters) {
      if (parameterisedQuery == null) {
         parameterisedQuery = query;
      }
      query = parameterisedQuery;
      for (String key : parameters.keySet()) {
         String keyPattern = "${" + key + "}";
         String value = parameters.get(key);
         if (!query.contains(keyPattern)) {
            logger.trace(keyPattern);
         }
         query = query.replace(keyPattern, value);
      }
   }

   public void put(String key, String value) {
      parameters.put(key, value);
   }

   public String getQuery() {
      if (parameters.size() > 0) putAll(parameters);
      return query;
   }

   public String getQueryName() {
      return queryName;
   }

   public String getUser() {
      return user;
   }

   public void setDatabase(String database) {
      this.database = database;
   }

   public void setQuery(String query) {
      this.query = query;
   }

   public void setQueryName(String queryName) {
      this.queryName = queryName;
   }

   public void setSchema(String schema) {
      this.schema = schema;
   }

   public void setUser(String user) {
      this.user = user;
   }

   public void setTable(String table) {
      this.table = table;
   }

   public String getTable() {
      return table;
   }

   public void setColumn(String column) {
      this.column = column;
   }

   public String getColumn() {
      return column;
   }

   public RowSet getRowSet() {
      return rowSet;
   }

   public void setRowSet(RowSet rowSet) {
      this.rowSet = rowSet;
   }

   public void add(String where) {
      if (parameterisedQuery == null) {
         parameterisedQuery = query;
      } else {
         query = parameterisedQuery;
      }
      int index = query.indexOf("order by");
      if (index > 0) {
         query = query.substring(0, index) + "and " + where + "\n" + query.substring(index);
      }
   }

   public Map<String, String> getParameters() {
      return parameters;
   }

   @Override
   public String toString() {
      return Args.format(queryName, database, schema, user);
   }


}

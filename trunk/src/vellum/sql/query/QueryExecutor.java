/*
 * (c) Copyright 2011, iPay (Pty) Ltd, Evan Summers
 * Apache Software License 2.0
 * Supported by BizSwitch.net
 *
 */
package vellum.sql.query;

import vellum.jdbc.RowSets;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import com.sun.rowset.CachedRowSetImpl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.sql.RowSet;
import javax.sql.rowset.CachedRowSet;

/**
 *
 * @author evanx
 */
public class QueryExecutor {

   Logr logger = LogrFactory.getLogger(getClass());
   Connection connection;
   Statement statement;
   long durationMillis;

   public QueryExecutor() {
   }

   public RowSet execute(QueryInfo queryInfo) throws Exception {
      try {
         connection = RowSets.getConnection(queryInfo);
         statement = connection.createStatement();
         durationMillis = System.currentTimeMillis();
         ResultSet res = statement.executeQuery(queryInfo.getQuery());
         CachedRowSet rowSet = new CachedRowSetImpl();
         rowSet.populate(res);
         res.close();
         statement.close();
         connection.close();
         queryInfo.setRowSet(rowSet);
         return rowSet;
      } catch (Exception e) {
         logger.warn(null, queryInfo, queryInfo.getQuery());
         throw e;
      } finally {
         durationMillis = System.currentTimeMillis() - durationMillis;
      }
   }
}

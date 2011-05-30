/*
 * (c) Copyright 2010, iPay (Pty) Ltd, Evan Summers
 * Apache Software License 2.0
 * Supported by BizSwitch.net
 *
 */
package bizdb.query;

import bizdb.common.RowSets;
import bizmon.logger.Logr;
import bizmon.logger.LogrFactory;
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
         connection = RowSets.getConnection(queryInfo.getDatabase(), queryInfo.getUser());
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
         logger.warn(queryInfo, queryInfo.getQuery());
         throw e;
      } finally {
         durationMillis = System.currentTimeMillis() - durationMillis;
      }
   }
}

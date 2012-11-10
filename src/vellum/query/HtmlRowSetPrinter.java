/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers 2011, iPay (Pty) Ltd, Evan Summers
 * Apache Software License 2.0
 * Supported by BizSwitch.net
 */
package vellum.query;

import vellum.exception.Exceptions;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.printer.Printer;
import vellum.util.Types;
import java.sql.ResultSetMetaData;
import javax.sql.RowSet;
import vellum.format.ArgFormats;

/**
 *
 * @author evanx
 */
public class HtmlRowSetPrinter {

   Logr logger = LogrFactory.getLogger(getClass());
   Printer out;
   boolean outputInfo;

   public HtmlRowSetPrinter(Printer out, boolean outputInfo) {
      this.out = out;
      this.outputInfo = outputInfo;
   }

   public void print(RowSet set) {
      try {
         ResultSetMetaData md = set.getMetaData();
         out.printf("<table class='resultSet'>\n");
         out.printf("<thead>\n");
         for (int index = 1; index <= md.getColumnCount(); index++) {
            out.printf("<th>%s\n", md.getColumnName(index));
         }
         out.printf("</thead>\n");
         out.printf("<tbody>\n");
         int resultCount = 0;
         set.beforeFirst();
         while (set.next()) {
            out.printf("<tr class='row%d'>\n", resultCount % 2);
            for (int index = 1; index <= md.getColumnCount(); index++) {
               Object value = set.getObject(index);
               String string = ArgFormats.displayFormatter.format(value);
               if (string.endsWith(".0")) {
                  string = string.substring(0, string.length() - 2);
               }
               out.printf("<td class='%sCell'>%s\n", md.getColumnTypeName(index), string);
            }
            resultCount++;
         }
         out.printf("</tbody>\n");
         out.printf("</table>\n");
         out.flush();
         if (outputInfo) {
            String results = "no results";
            if (resultCount == 1) {
               results = "1 result";
            } else if (resultCount > 1) {
               results = String.format("%d results", resultCount);
            }

            out.printf("<div class='resultInfo'>%s</div>\n", results);
            out.flush();
         }
      } catch (Exception e) {
         throw Exceptions.newRuntimeException(e);
      }
   }
}

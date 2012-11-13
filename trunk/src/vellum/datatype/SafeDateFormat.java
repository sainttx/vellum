/*
 * Apache Software License 2.0   
 *
 */
package vellum.datatype;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import vellum.exception.ParseRuntimeException;

/**
 *
 * @author evans
 */
public class SafeDateFormat {

   SimpleDateFormat dateFormat;
   String pattern;

   public SafeDateFormat(String pattern) {
      this.pattern = pattern;
      dateFormat = new SimpleDateFormat(pattern);
   }

   public String getPattern() {
      return pattern;
   }

   public synchronized String format(Date date) {
      if (date == null) {
         return "";
      }
      return dateFormat.format(date);
   }

   public synchronized Date parse(String string){
       return parse(string, null);
       
   }
   
   public synchronized Date parse(String string, Date defaultValue) {
      if (string == null || string.isEmpty()) {
         return defaultValue;
      }
      if (string.length() > pattern.length()) {
          string = string.substring(0, pattern.length());
      }
        try {
            return dateFormat.parse(string);
        } catch (ParseException e) {
            throw new ParseRuntimeException(string, e);
        }
   }
}

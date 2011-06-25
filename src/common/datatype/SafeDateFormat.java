/*
 * Apache Software License 2.0
 * Supported by BizSwitch.net   
 *
 */
package common.datatype;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author evanx
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

   public synchronized Date parse(String string) throws ParseException {
      if (string == null || string.isEmpty()) {
         return null;
      }
      if (string.length() > pattern.length()) {
          string = string.substring(0, pattern.length());
      }
      return dateFormat.parse(string);
   }
}

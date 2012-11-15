/*
 * Apache Software License 2.0
 * Supported by iPay (Pty) Ltd, BizSwitch.net
 * Apache Software License 2.0, (c) Copyright 2012 Evan Summers, 2010 iPay (Pty) Ltd
 */

package vellum.datatype;

/**
 *
 * @author evanx
 */
public class Interval {

   long millis;

   public Interval() {
   }
      
   public long getSeconds() {
      return millis/1000;
   }

   public long getMinutes() {
      return millis/1000/60;
   }

   public long getHours() {
      return millis/1000/60/60;
   }

   public long getDays() {
      return millis/1000/60/60/24;
   }

}

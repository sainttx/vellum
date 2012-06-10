/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellum.util;

/**
 *
 * @author evan
 */
public class Bytes {

    public static String formatHex(byte[] bytes) {
        if (bytes == null) {
            return "null{}";            
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            if (builder.length() > 0) {
                builder.append(",");
            }
            builder.append(String.format("%02x", bytes[i]));
        }
        return "{" + builder.toString() + "}";
    }
    
    
}

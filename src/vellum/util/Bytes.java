/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellum.util;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import vellum.exception.ParseRuntimeException;

/**
 *
 * @author evan
 */
public class Bytes {

    public static long fromK(long size) {
        return size*1024;
    }

    public static long fromM(long size) {
        return size*1024*1024;
    }

    public static long fromG(long size) {
        return size*1024*1024*1024;
    }
    
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

    public static Long parseConfig(String string, Long defaultValue) {
        if (string == null) return defaultValue;
        if (string.length() >= 2 &&
                Character.isLowerCase(string.charAt(string.length() - 1)) && 
                Character.isDigit(string.charAt(string.length() - 2))) {            
            long value = Long.parseLong(string.substring(0, string.length() - 1));
            if (string.endsWith("b")) {
                return value;
            } else if (string.endsWith("k")) {
                return value*1024;
            } else if (string.endsWith("m")) {
                return value*1024*1024;
            } else if (string.endsWith("g")) {
                return value*1024*1024*1024;
            }
        }
        throw new ParseRuntimeException(string);
    }  
    
    public static char[] toCharArray(byte[] bytes) {
        return Charset.forName(Strings.UTF8).decode(ByteBuffer.wrap(bytes)).array();
    }
}

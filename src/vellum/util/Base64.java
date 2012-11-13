/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellum.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import vellum.exception.Exceptions;

/**
 *
 * @author evan
 */
public class Base64 {
    
    public static String encode(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return new BASE64Encoder().encode(bytes);
    }

    public static byte[] decode(String string) {
        if (string == null) {
            return null;
        }
        try {
            return new BASE64Decoder().decodeBuffer(string);
        } catch (Exception e) {
            throw Exceptions.newRuntimeException(e);
        }
    }

}

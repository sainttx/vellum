/*
 * Copyright Evan Summers
 * 
 */
package vellum.util;

import java.io.IOException;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 *
 * @author evan
 */
public class Base64 {

    public static String encode(byte[] bytes) {
        return new BASE64Encoder().encode(bytes);        
    }

    public static byte[] decode(String string) throws IOException {
        return new BASE64Decoder().decodeBuffer(string);        
    }
    
}

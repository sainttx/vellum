/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellum.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.CharBuffer;

/**
 *
 * @author evan
 */
public class Chars {

    public static char[] readChars(InputStream inputStream, int capacity) throws IOException {
        CharBuffer buffer = CharBuffer.allocate(capacity);
        while (true) {
            int b = inputStream.read();
            if (b < 0) {
                return buffer.array();
            }
            buffer.append((char) b);
        }
    }   
    
}

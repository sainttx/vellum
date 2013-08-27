/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellum.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
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

    public static byte[] getBytes(char[] chars) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(baos);
        writer.write(chars);
        writer.close();
        return baos.toByteArray();
    }    
    
    public static byte[] getAsciiBytes(char[] chars) {
        byte[] array = new byte[chars.length];
        for (int i = 0; i < chars.length; i++) {
            array[i] = (byte) chars[i];    
        }
        return array;
    }           
}

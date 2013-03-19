/*
 * Copyright Evan Summers
 * 
 */
package vellum.crypto;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 *
 * @author evan
 */
public class PasswordHashSerializer1 {
    
    private static final int[] ITERATION_COUNT_MAP = {
        1000, 2000, 10000, 20000, 30000, 60000, 300000
    };
        
    PasswordHash o;
    
    public PasswordHashSerializer1(PasswordHash passwordHash) {
        this.o = passwordHash;
    }
    
    public void writeObject(OutputStream stream) throws IOException {
        if (o.keySize % 8 != 0) {
            throw new IOException("Invalid key size " + o.keySize);
        }
        int iterationCountIndex = Arrays.binarySearch(ITERATION_COUNT_MAP, o.iterationCount);
        if (iterationCountIndex < 0) {
            throw new IOException("Invalid iteration count " + o.iterationCount);
        }
        stream.write(o.hash.length);
        stream.write(o.salt.length);
        stream.write(o.iv.length);
        stream.write(iterationCountIndex);
        stream.write(o.keySize / 8);
        stream.write(o.hash);
        stream.write(o.salt);
        stream.write(o.iv);
    }

    public void readObject(InputStream stream) throws IOException {
        o.hash = new byte[stream.read()];
        o.salt = new byte[stream.read()];
        o.iv = new byte[stream.read()];
        o.iterationCount = ITERATION_COUNT_MAP[stream.read()];
        o.keySize = stream.read() * 8;
        stream.read(o.hash);
        stream.read(o.salt);
        stream.read(o.iv);
    }    
}

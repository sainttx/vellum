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
public class MinimalPasswordHashSerializer {
    
    private static final int[] ITERATION_COUNT_MAP = {
        1000, 2000, 10000, 20000, 30000, 60000, 300000
    };
        
    static void writeObject(PasswordHash passwordHash, OutputStream stream) throws IOException {
        if (passwordHash.salt.length != 16) {
            throw new IOException("Invalid salt length: " + passwordHash.salt.length);
        }
        if (passwordHash.iv.length != 0) {
            throw new IOException("IV not supported");
        }
        if (passwordHash.keySize % 8 != 0) {
            throw new IOException("Invalid key size: " + passwordHash.keySize);
        }
        int iterationCountIndex = Arrays.binarySearch(ITERATION_COUNT_MAP, passwordHash.iterationCount);
        if (iterationCountIndex < 0) {
            throw new IOException("Invalid iteration count: " + passwordHash.iterationCount);
        }
        stream.write(iterationCountIndex); // using index as the version
        stream.write(passwordHash.hash.length);
        stream.write(passwordHash.keySize / 8);
        stream.write(passwordHash.hash);
        stream.write(passwordHash.salt);
    }

    static void readObject(PasswordHash passwordHash, InputStream stream, int version) throws IOException {
        passwordHash.hash = new byte[stream.read()];
        passwordHash.salt = new byte[16];
        passwordHash.iv = new byte[0];
        passwordHash.iterationCount = ITERATION_COUNT_MAP[version];
        passwordHash.keySize = stream.read() * 8;
        stream.read(passwordHash.hash);
        stream.read(passwordHash.salt);
    }    
}

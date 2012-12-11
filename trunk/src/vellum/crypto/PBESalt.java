/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellum.crypto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author evan
 */
public class PBESalt {
    private static final byte version = 0x00;    
    int iterationCount;
    int keySize;
    byte[] salt;
    byte[] iv;
    byte[] encryptedSalt;

    public PBESalt(byte[] salt, int iterationCount, int keySize, 
            byte[] iv, byte[] encryptedSalt) {
        this.salt = salt;
        this.iterationCount = iterationCount;
        this.keySize = keySize;
        this.iv = iv;
        this.encryptedSalt = encryptedSalt;
    }

    public PBESalt(byte[] bytes) throws IOException {
        ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
        if (stream.read() != version || stream.read() != bytes.length) {
            throw new IOException();
        }
        salt = new byte[stream.read()];
        iv = new byte[stream.read()];
        encryptedSalt = new byte[stream.read()];
        iterationCount = stream.read()*256 + stream.read();
        keySize = 16 * stream.read();
        stream.read(salt);
        stream.read(iv);
        stream.read(encryptedSalt);
    }

    public int getIterationCount() {
        return iterationCount;
    }

    public int getKeySize() {
        return keySize;
    }

    public byte[] getSalt() {
        return salt;
    }

    public byte[] getIv() {
        return iv;
    }

    public byte[] getEncryptedSalt() {
        return encryptedSalt;
    }
    
    public byte[] getBytes() {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            stream.write(version);
            stream.write(salt.length + iv.length + encryptedSalt.length + 8);
            stream.write(salt.length);
            stream.write(iv.length);
            stream.write(encryptedSalt.length);
            stream.write(iterationCount/256);
            stream.write(iterationCount%256);
            stream.write(keySize/16);
            stream.write(salt);
            stream.write(iv);
            stream.write(encryptedSalt);
            return stream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static boolean verifyBytes(byte[] bytes) {
        ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
        if (stream.read() != version || 
            stream.read() != bytes.length || 
            stream.read() + stream.read() + stream.read() + 8 != bytes.length) {
            return false;
        }
        return true;
    }        
}
